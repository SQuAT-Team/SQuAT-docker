package test;

import java.util.List;

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
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.performance.PerformanceMetric;
import io.github.squat_team.performance.PerformancePCMScenario;
import io.github.squat_team.performance.peropteryx.PerOpteryxPCMBot;
import io.github.squat_team.performance.peropteryx.configuration.Configuration;
import io.github.squat_team.util.PCMHelper;
import io.github.squat_team.util.SQuATHelper;
import main.TestConstants;


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
		configuration.getPerOpteryxConfig().setQmlDefinitionFile(TestConstants.QML_FILE_PATH);
		configuration.getLqnsConfig().setLqnsOutputDir(TestConstants.LQN_OUTPUT);
		configuration.getExporterConfig().setPcmOutputFolder(TestConstants.PCM_STORAGE_PATH);
		configuration.getPcmModelsConfig().setPathmapFolder(TestConstants.PCM_MODEL_FILES);

		// init bot
		PerOpteryxPCMBot bot = new PerOpteryxPCMBot(scenario, configuration);
		bot.setDebugMode(true);

		// create Instance
		Allocation allocation = SQuATHelper.loadAllocationModel("file:/" + TestConstants.ALLOCATION_FILE_PATH);
		UsageModel usageModel = SQuATHelper.loadUsageModel("file:/" + TestConstants.USAGE_FILE_PATH);
		PCMArchitectureInstance architecture = new PCMArchitectureInstance("", null, null, allocation, null,
				usageModel);

		optimize(bot, architecture);
	}

	public static void analyse(PerOpteryxPCMBot bot, PCMArchitectureInstance architecture) {
		// run bot analyse
		PCMScenarioResult result = bot.analyze(architecture);

		System.out.println("BOT FINISHED: ");
		System.out.println(result.getOriginatingBot());
		System.out.println(result.getResult().getResponse());
		System.out.println(result.getResultingArchitecture().getName());
		System.out.println(result.getResultingArchitecture().getAllocation());
		System.out.println(result.getResultingArchitecture().getRepository());
		System.out.println(result.getResultingArchitecture().getResourceEnvironment());
		System.out.println(result.getResultingArchitecture().getSystem());
		System.out.println(result.getResultingArchitecture().getUsageModel());
	}

	public static void optimize(PerOpteryxPCMBot bot, PCMArchitectureInstance architecture) {
		// run bot optimization
		List<PCMScenarioResult> results = bot.searchForAlternatives(architecture);

		System.out.println("BOT FINISHED: ");
		for (PCMScenarioResult result : results) {
			System.out.println(result.getOriginatingBot());
			System.out.println(result.getResult().getResponse());
			System.out.println(result.getResultingArchitecture().getName());
			System.out.println(result.getResultingArchitecture().getAllocation());
			System.out.println(result.getResultingArchitecture().getRepository());
			System.out.println(result.getResultingArchitecture().getResourceEnvironment());
			System.out.println(result.getResultingArchitecture().getSystem());
			System.out.println(result.getResultingArchitecture().getUsageModel());
		}

	}
}
