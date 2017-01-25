package test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.performance.PerformanceMetric;
import io.github.squat_team.performance.PerformancePCMScenario;
import io.github.squat_team.performance.peropteryx.PerOpteryxPCMBot;
import io.github.squat_team.performance.peropteryx.configuration.Configuration;
import io.github.squat_team.performance.peropteryx.start.OptimizationInfo;
import io.github.squat_team.util.SQuATHelper;
import test.TestConstants;

/**
 * Main class to run the SQuAT Performance Bot
 */
public class SQuATMain {

	private static void register() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		EPackage.Registry.INSTANCE.put(RepositoryPackage.eNS_URI, RepositoryPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(ResourceenvironmentPackage.eNS_URI, ResourceenvironmentPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(SystemPackage.eNS_URI, SystemPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(AllocationPackage.eNS_URI, AllocationPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(UsagemodelPackage.eNS_URI, UsagemodelPackage.eINSTANCE);
	}

	public static void main(String[] args) {
		register();

		// create scenario
		PerformancePCMScenario scenario = new PerformancePCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResponse = new PCMResult(ResponseMeasureType.DECIMAL);
		expectedResponse.setResponse(6.0);
		scenario.setExpectedResponse(expectedResponse);
		scenario.setMetric(PerformanceMetric.RESPONSE_TIME);

		// create configuration
		Configuration configuration = new Configuration();
		configuration.getPerOpteryxConfig().setDesignDecisionFile(TestConstants.DESIGNDECISION_FILE_PATH);
		//configuration.getPerOpteryxConfig().setQmlDefinitionFile(TestConstants.QML_FILE_PATH);
		configuration.getLqnsConfig().setLqnsOutputDir(TestConstants.LQN_OUTPUT);
		configuration.getExporterConfig().setPcmOutputFolder(TestConstants.PCM_STORAGE_PATH);
		configuration.getPcmModelsConfig().setPathmapFolder(TestConstants.PCM_MODEL_FILES);

		// init bot
		PerOpteryxPCMBot bot = new PerOpteryxPCMBot(scenario, configuration);
		bot.setDebugMode(false);

		// create Instance
		Allocation allocation = SQuATHelper.loadAllocationModel("file:/" + TestConstants.ALLOCATION_FILE_PATH);
		UsageModel usageModel = SQuATHelper.loadUsageModel("file:/" + TestConstants.USAGE_FILE_PATH);
		PCMArchitectureInstance architecture = new PCMArchitectureInstance("", null, null, allocation, null,
				usageModel);
		
		//configuration.getPerOpteryxConfig().setMaxIterations(1);
		//configuration.getPerOpteryxConfig().setGenerationSize(1);
		optimize(bot, architecture);
		//analyze(bot, architecture);
		
		
		// AUTOMATIC EVALUATION - cant be used
		/*
		Map<Integer, Comparable> values = new HashMap<Integer, Comparable>();
		Map<Integer, Long> times = new HashMap<Integer, Long>();
		for(int i = 0; i < 1; i++){
			System.out.println("Starting iteration " + i);
			
			//execute
			long start = System.currentTimeMillis();
			List<PCMScenarioResult> results = bot.searchForAlternatives(architecture);
			long end = System.currentTimeMillis();

			times.put(i, (end-start));
			Comparable value = 100000.0;
			for(PCMScenarioResult result : results){
				if(result.getResult().getResponse().compareTo(value) < 0){
					value = result.getResult().getResponse();
				}
			}
			values.put(i, value);
			
			System.gc();
		}
		
		System.out.println("RESULTS:");
		for(int i : values.keySet()){
			System.out.println("====================");
			System.out.println("iteration: " + i);
			System.out.println("result: " + values.get(i));
			System.out.println("time: " + times.get(i));
		}
		System.out.println("====================");*/
		
	}

	public static void analyze(PerOpteryxPCMBot bot, PCMArchitectureInstance architecture) {
		// run bot analyse
		long start = System.currentTimeMillis();
		PCMScenarioResult result = bot.analyze(architecture);
		long end = System.currentTimeMillis();
		
		System.out.println("BOT FINISHED: ");
		System.out.println(result.getOriginatingBot());
		System.out.println(result.getResult().getResponse());
		System.out.println(result.getResultingArchitecture().getName());
		System.out.println(result.getResultingArchitecture().getAllocation());
		System.out.println(result.getResultingArchitecture().getRepository());
		System.out.println(result.getResultingArchitecture().getResourceEnvironment());
		System.out.println(result.getResultingArchitecture().getSystem());
		System.out.println(result.getResultingArchitecture().getUsageModel());
		System.out.println(end-start);

	}

	public static void optimize(PerOpteryxPCMBot bot, PCMArchitectureInstance architecture) {
		// run bot optimization
		long start = System.currentTimeMillis();
		List<PCMScenarioResult> results = bot.searchForAlternatives(architecture);
		long end = System.currentTimeMillis();
		
		System.out.println("BOT FINISHED: ");
		System.out.println("Population Size: "+ 100);
		System.out.println("Max Iterations: " + 20);
		System.out.println("Runtime " + (end-start) + " ms");
		System.out.println("Real Iterations: " + OptimizationInfo.getIterations());
		System.out.println("");
		System.out.println("Best 10 Candidates:");
		for (PCMScenarioResult result : results) {
			System.out.println("----");
			String uri = result.getResultingArchitecture().getAllocation().eResource().getURI().segment(4).toString();			
			System.out.println("Name: " + uri);

			//System.out.println(result.getOriginatingBot());
			System.out.println("Response Time: " + result.getResult().getResponse());
			/*System.out.println(result.getResultingArchitecture().getName());
			System.out.println(result.getResultingArchitecture().getAllocation());
			System.out.println(result.getResultingArchitecture().getRepository());
			System.out.println(result.getResultingArchitecture().getResourceEnvironment());
			System.out.println(result.getResultingArchitecture().getSystem());
			System.out.println(result.getResultingArchitecture().getUsageModel());*/
		}

	}
}
