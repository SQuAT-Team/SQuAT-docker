package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.performance.AbstractPerformancePCMScenario;
import io.github.squat_team.performance.PerformanceMetric;
import io.github.squat_team.performance.PerformancePCMCPUScenario;
import io.github.squat_team.performance.peropteryx.PerOpteryxPCMBot;
import io.github.squat_team.performance.peropteryx.configuration.Configuration;
import io.github.squat_team.performance.peropteryx.start.OptimizationInfo;
import io.github.squat_team.util.SQuATHelper;

/**
 * Main class to run the SQuAT Performance Bot
 */
public class SQuATMain {
	private static Boolean multiOptimisation = false;
	private static TestConstants testConstants = new TestConstants();

	public SQuATMain(TestConstants testConstants) {
		SQuATMain.testConstants = testConstants;
	}

	private static void register() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		EPackage.Registry.INSTANCE.put(RepositoryPackage.eNS_URI, RepositoryPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(ResourceenvironmentPackage.eNS_URI, ResourceenvironmentPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(SystemPackage.eNS_URI, SystemPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(AllocationPackage.eNS_URI, AllocationPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(UsagemodelPackage.eNS_URI, UsagemodelPackage.eINSTANCE);
	}

	public static void mainFn(String[] args) throws IOException {
		register();
		ArrayList<String> workloadIDs = new ArrayList<String>();
		workloadIDs.add(testConstants.WORKLOAD_ID);

		// create scenario
		ArrayList<String> cpuIDs = new ArrayList<String>();
		cpuIDs.add(testConstants.CPU_ID);

		// AbstractPerformancePCMScenario scenario = new
		// PerformancePCMWokloadScenario(OptimizationType.MINIMIZATION,
		// workloadIDs, 0.5);
		AbstractPerformancePCMScenario scenario = new PerformancePCMCPUScenario(OptimizationType.MINIMIZATION, cpuIDs,
				1.0); // scenario with 1.0 does nothing 

		PCMResult expectedResponse = new PCMResult(ResponseMeasureType.DECIMAL);
		expectedResponse.setResponse(6.0);
		scenario.setExpectedResponse(expectedResponse);
		scenario.setMetric(PerformanceMetric.RESPONSE_TIME);

		// create configuration
		Configuration configuration = new Configuration();
		// configuration.getPerOpteryxConfig().setDesignDecisionFile(testConstants.DESIGNDECISION_FILE_PATH);
		// configuration.getPerOpteryxConfig().setQmlDefinitionFile(TestConstants.QML_FILE_PATH);
		configuration.getPerOpteryxConfig().setGenerationSize(100);
		configuration.getPerOpteryxConfig().setMaxIterations(10);
		configuration.getLqnsConfig().setLqnsOutputDir(testConstants.LQN_OUTPUT);
		configuration.getExporterConfig().setPcmOutputFolder(testConstants.PCM_STORAGE_PATH);
		configuration.getPcmModelsConfig().setPathmapFolder(testConstants.PCM_MODEL_FILES);

		// init bot
		PerOpteryxPCMBot bot = new PerOpteryxPCMBot(scenario, configuration);
		bot.setDebugMode(false);
		bot.setDetailedAnalysis(true);

		List<String> basicPaths = new ArrayList<String>();

		/*
		 * Searchs for subfolders with allocation files in it.
		 */
		File file = new File(testConstants.BASIC_FILE_PATH);
		multiOptimisation = file.isDirectory();
		System.out.println("Switched to multi optimization/analysis mode");
		if (multiOptimisation) {
			System.out.println("Directory Mode");
			File[] subFolders = file.listFiles();
			for (File subFolder : subFolders) {
				if (subFolder.isDirectory()) {
					File[] subFiles = subFolder.listFiles();
					for (File subFile : subFiles) {
						String subFilePath = subFile.getPath();
						if (subFilePath.endsWith(".allocation")) {
							String newBasicPath = subFilePath.substring(0, subFilePath.length() - 11);
							newBasicPath = newBasicPath.replace("\\", "\\\\");
							basicPaths.add(newBasicPath);
						}
					}
				}
			}
		} else {
			System.out.println("Single File Mode");
			basicPaths.add(testConstants.BASIC_FILE_PATH);
		}

		System.out.println("OPTIMIZE PCM INSTANCES:");
		System.out.println(basicPaths);
		System.out.println("no of instances: " + basicPaths.size());

		for (String basicPath : basicPaths) {
			if (multiOptimisation) {
				configuration.getExporterConfig().setPcmOutputFolder(basicPath.replace("\\\\", "/"));
			}

			// create Instance
			Allocation allocation = SQuATHelper.loadAllocationModel("file:/" + basicPath + ".allocation");
			org.palladiosimulator.pcm.system.System system = SQuATHelper
					.loadSystemModel("file:/" + basicPath + ".system");
			ResourceEnvironment resourceenvironment = SQuATHelper
					.loadResourceEnvironmentModel("file:/" + basicPath + ".resourceenvironment");
			Repository repository = SQuATHelper.loadRepositoryModel("file:/" + basicPath + ".repository");
			UsageModel usageModel = SQuATHelper.loadUsageModel("file:/" + basicPath + ".usagemodel");
			PCMArchitectureInstance architecture = new PCMArchitectureInstance("", repository, system, allocation,
					resourceenvironment, usageModel);
			// TODO: should not be used in multiOptimization
			architecture.setRepositoryWithAlternatives(
					SQuATHelper.loadRepositoryModel("file:/" + testConstants.ALTERNATIVE_REPOSITORY_PATH));

			// configuration.getPerOpteryxConfig().setMaxIterations(1);
			// configuration.getPerOpteryxConfig().setGenerationSize(1);

			// TODO:
			optimize(bot, architecture, basicPath, configuration);
			//analyze(bot, architecture, basicPath);
		}
		// AUTOMATIC EVALUATION - cant be used
		/*
		 * Map<Integer, Comparable> values = new HashMap<Integer, Comparable>();
		 * Map<Integer, Long> times = new HashMap<Integer, Long>(); for(int i =
		 * 0; i < 1; i++){ System.out.println("Starting iteration " + i);
		 * 
		 * //execute long start = System.currentTimeMillis();
		 * List<PCMScenarioResult> results =
		 * bot.searchForAlternatives(architecture); long end =
		 * System.currentTimeMillis();
		 * 
		 * times.put(i, (end-start)); Comparable value = 100000.0;
		 * for(PCMScenarioResult result : results){
		 * if(result.getResult().getResponse().compareTo(value) < 0){ value =
		 * result.getResult().getResponse(); } } values.put(i, value);
		 * 
		 * System.gc(); }
		 * 
		 * System.out.println("RESULTS:"); for(int i : values.keySet()){
		 * System.out.println("===================="); System.out.println(
		 * "iteration: " + i); System.out.println("result: " + values.get(i));
		 * System.out.println("time: " + times.get(i)); }
		 * System.out.println("====================");
		 */
	}

	public static void analyze(PerOpteryxPCMBot bot, PCMArchitectureInstance architecture, String basicPath)
			throws IOException { // run bot analyze
		PCMScenarioResult result = bot.analyze(architecture);

		File basicFile = new File(testConstants.BASIC_FILE_PATH);
		File metricFile;
		if (basicFile.isDirectory()) {
			metricFile = new File(basicFile, "analysisResults.txt");
		} else {
			metricFile = new File(basicFile.getParentFile(), "analysisResults.txt");
		}

		metricFile.createNewFile();
		FileOutputStream is = new FileOutputStream(metricFile, true);
		OutputStreamWriter osw = new OutputStreamWriter(is);
		BufferedWriter w = new BufferedWriter(osw);
		w.newLine();
		w.write("-------------------");
		w.newLine();
		w.write("Candidate: " + (new File(basicPath)).getParentFile().getName());
		w.newLine();
		try {
			w.write("result: " + result.getResult().getResponse());
		} catch (Exception e) {
			w.write("result: error (unsolvable)");
		}
		w.newLine();
		w.write("-------------------");
		w.newLine();
		w.close();
	}

	public static void optimize(PerOpteryxPCMBot bot, PCMArchitectureInstance architecture, String basicPath,
			Configuration configuration) throws IOException { // run bot
																// optimization
		long start = System.currentTimeMillis();

		List<PCMScenarioResult> results = bot.searchForAlternatives(architecture);
		long end = System.currentTimeMillis();

		File metricFile = new File(basicPath.replace("\\\\", "/") + "_Metrics.txt");

		if (metricFile.exists()) {
			metricFile.delete();
		}
		metricFile.createNewFile();

		FileOutputStream is = new FileOutputStream(metricFile);
		OutputStreamWriter osw = new OutputStreamWriter(is);
		BufferedWriter w = new BufferedWriter(osw);

		System.out.println("BOT FINISHED: ");
		w.write("BOT FINISHED: ");
		w.newLine();
		System.out.println("Population Size: " + configuration.getPerOpteryxConfig().getGenerationSize());
		w.write("Population Size: " + configuration.getPerOpteryxConfig().getGenerationSize());
		w.newLine();
		System.out.println("Max Iterations: " + configuration.getPerOpteryxConfig().getMaxIterations());
		w.write("Max Iterations: " + configuration.getPerOpteryxConfig().getMaxIterations());
		w.newLine();
		System.out.println("Runtime " + (end - start) + " ms");
		w.write("Runtime " + (end - start) + " ms");
		w.newLine();
		System.out.println("Real Iterations: " + OptimizationInfo.getIterations());
		w.write("Real Iterations: " + OptimizationInfo.getIterations());
		w.newLine();
		System.out.println("");
		w.write("");
		w.newLine();
		System.out.println("Best 10 Candidates:");
		w.write("Best 10 Candidates:");
		w.newLine();
		for (PCMScenarioResult result : results) {
			// bot.analyze(result.getResultingArchitecture());

			System.out.println("----");
			w.write("----");
			w.newLine();
			String uri = result.getResultingArchitecture().getAllocation().eResource().getURI()
					.segment(result.getResultingArchitecture().getAllocation().eResource().getURI().segmentCount() - 2)
					.toString();
			System.out.println("Name: " + uri);
			w.write("Name: " + uri);
			w.newLine();

			// System.out.println(result.getOriginatingBot());
			System.out.println("Response Time: " + result.getResult().getResponse());
			w.write("Response Time: " + result.getResult().getResponse());
			w.newLine();
		}
		w.close();
	}
}
