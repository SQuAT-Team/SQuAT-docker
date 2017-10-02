package io.github.squat_team.agentsUtils;

import java.io.File;
import java.util.ArrayList;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.performance.AbstractPerformancePCMScenario;
import io.github.squat_team.performance.PerformanceMetric;
import io.github.squat_team.performance.PerformancePCMWokloadScenario;
import io.github.squat_team.performance.peropteryx.PerOpteryxPCMBot;
import io.github.squat_team.performance.peropteryx.configuration.ConfigurationImprovedImproved;
import io.github.squat_team.util.SQuATHelper;
import test.TestConstants;

/**
 * A auxiliary class to setup the performance scenario.
 */
public class PerformanceScenarioHelper {
	public static AbstractPerformancePCMScenario createScenarioOfWorkload() {
		ArrayList<String> workloadIDs = new ArrayList<String>();
		workloadIDs.add(TestConstants.WORKLOAD_ID);
		AbstractPerformancePCMScenario scenario = new PerformancePCMWokloadScenario(OptimizationType.MINIMIZATION, workloadIDs, 1.1);
		PCMResult expectedResponse = new PCMResult(ResponseMeasureType.DECIMAL);
		expectedResponse.setResponse(6.0);
		scenario.setExpectedResponse(expectedResponse);
		scenario.setMetric(PerformanceMetric.RESPONSE_TIME);
		return scenario;
	}
	public static AbstractPerformancePCMScenario createScenarioOfCPU() {
		ArrayList<String> cpuIDs = new ArrayList<String>();
		cpuIDs.add(TestConstants.CPU_ID);
		AbstractPerformancePCMScenario scenario = new PerformancePCMWokloadScenario(OptimizationType.MINIMIZATION, cpuIDs, 0.5);
		PCMResult expectedResponse = new PCMResult(ResponseMeasureType.DECIMAL);
		expectedResponse.setResponse(6.0);
		scenario.setExpectedResponse(expectedResponse);
		scenario.setMetric(PerformanceMetric.RESPONSE_TIME);
		return scenario;
	}
	public static PerOpteryxPCMBot createPCMBot(AbstractPerformancePCMScenario scenario){
		// create configuration
		ConfigurationImprovedImproved configuration = new ConfigurationImprovedImproved();
		configuration.getPerOpteryxConfig().setGenerationSize(100);
		configuration.getPerOpteryxConfig().setMaxIterations(10);

		configuration.getLqnsConfig().setLqnsOutputDir(TestConstants.LQN_OUTPUT);
		configuration.getExporterConfig().setPcmOutputFolder(TestConstants.PCM_STORAGE_PATH);
		configuration.getPcmModelsConfig().setPathmapFolder(TestConstants.PCM_MODEL_FILES);
		// init bot
		PerOpteryxPCMBot bot = new PerOpteryxPCMBot(scenario, configuration);
		bot.setDebugMode(true);
		bot.setDetailedAnalysis(true);		
		return bot;
	}
	public static PCMArchitectureInstance createArchitecture(ArchitecturalVersion architecturalVersion){
		// create Instance
		Allocation allocation = SQuATHelper.loadAllocationModel("file:/" + architecturalVersion.getAbsolutePath() +File.separator+architecturalVersion.getAllocationFilename());
		org.palladiosimulator.pcm.system.System system = SQuATHelper.loadSystemModel("file:/" + architecturalVersion.getAbsolutePath() +File.separator+architecturalVersion.getSystemFilename());
		ResourceEnvironment resourceenvironment = SQuATHelper.loadResourceEnvironmentModel("file:/"+ architecturalVersion.getAbsolutePath() +File.separator+architecturalVersion.getResourceEnvironmentFilename());
		Repository repository = SQuATHelper.loadRepositoryModel("file:/" +architecturalVersion.getAbsolutePath() +File.separator+architecturalVersion.getRepositoryFilename());
		UsageModel usageModel = SQuATHelper.loadUsageModel("file:/" + architecturalVersion.getAbsolutePath() +File.separator+architecturalVersion.getUsageFilename());
		
		PCMArchitectureInstance architecture = new PCMArchitectureInstance(architecturalVersion.getFileName(), repository, system, allocation,resourceenvironment, usageModel);
		if(architecturalVersion.getFullPathToAlternativeRepository()!=null){
			Repository repositoryAlternatives = SQuATHelper.loadRepositoryModel("file:/" +architecturalVersion.getFullPathToAlternativeRepository());
			architecture.setRepositoryWithAlternatives(repositoryAlternatives);
		}
		
		return architecture;	
	}
}
