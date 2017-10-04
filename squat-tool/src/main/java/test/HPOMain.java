package test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.github.squat_team.performance.peropteryx.configuration.Configuration;
import io.github.squat_team.performance.peropteryx.configuration.PerOpteryxConfig;
import io.github.squat_team.performance.peropteryx.export.ExportMode;
import io.github.squat_team.performance.peropteryx.export.OptimizationDirection;
import io.github.squat_team.performance.peropteryx.export.PerOpteryxPCMResult;
import io.github.squat_team.performance.peropteryx.start.HeadlessPerOpteryxRunner;

/**
 * Main class to run Headless PerOpteryx
 */
public class HPOMain {
	
	private static Configuration config;
	
	public static void mainFn(String[] args) throws InterruptedException, ExecutionException {
		configurate();
		start();
	}

	private static void configurate(){
		config = new Configuration();
		config.getPerOpteryxConfig().setMaxIterations(1);
		config.getPerOpteryxConfig().setGenerationSize(1);
		
		config.getPcmInstanceConfig().setAllocationModel(TestConstants.BASIC_FILE_PATH + ".allocation");
		config.getPcmInstanceConfig().setUsageModel(TestConstants.BASIC_FILE_PATH + ".usagemodel");
		config.getPerOpteryxConfig().setDesignDecisionFile(TestConstants.DESIGNDECISION_FILE_PATH);
		config.getPerOpteryxConfig().setQmlDefinitionFile(TestConstants.QML_FILE_PATH);
		config.getPerOpteryxConfig().setMode(PerOpteryxConfig.Mode.OPTIMIZE);
		
		config.getPcmModelsConfig().setPathmapFolder(TestConstants.PCM_MODEL_FILES);
		
		config.getLqnsConfig().setLqnsOutputDir(TestConstants.LQN_OUTPUT);
		config.getExporterConfig().setPcmOutputFolder(TestConstants.PCM_STORAGE_PATH);
		config.getExporterConfig().setExportMode(ExportMode.AMOUNT);
		config.getExporterConfig().setAmount(2);
		config.getExporterConfig().setOptimizationDirection(OptimizationDirection.MINIMIZE);
		config.getExporterConfig().setBoundaryValue(6.0);
	}
	
	private static void start() throws InterruptedException, ExecutionException{
	    ExecutorService pool = Executors.newFixedThreadPool(4);
		
	    long start = System.currentTimeMillis();
		HeadlessPerOpteryxRunner runner = new HeadlessPerOpteryxRunner();
		runner.init(config);
		runner.setDebugMode(false);
	    Future<List<PerOpteryxPCMResult>> future = pool.submit(runner);
	    future.get();
	    long end = System.currentTimeMillis();
	    System.out.println("TIME: " + (end-start));
	    System.out.println(future.get().get(0).getValue());
	}
	
}
