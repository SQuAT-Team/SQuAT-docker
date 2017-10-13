package io.github.squat_team.agentsUtils;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONStringer;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.performance.PerformanceMetric;
import io.github.squat_team.util.SQuATHelper;


/**
 * A auxiliary class to setup the performance scenario. TODO: PA! Setup the
 * scenarios for the stplus model and use the REST Interface. Maybe remove this
 * class and create a central Bot Registry instead of creating a bot each time
 * it is required.
 */
public class PerformanceScenarioHelper {

    /**
     * @param jsonStringer this json stringer is used to insert the scenario
     *  into the JSON object. This {@link JSONStringer} is required to be in 
     *  a state where a key can be created
     */
    public static void createScenarioOfWorkload(JSONStringer jsonStringer) {
        ArrayList<String> workloadIDs = new ArrayList<String>();
        workloadIDs.add("_Uc-igC6OEd-Jla2o7wkBzQ");

        jsonStringer.key("scenario");
        jsonStringer.object();
        
        jsonStringer.key("type").value(OptimizationType.MINIMIZATION);
        jsonStringer.key("ids").array();
        for(String id : workloadIDs) jsonStringer.value(id);
        jsonStringer.endArray();
        jsonStringer.key("rate").value(1.1);
        jsonStringer.key("scenario-type").value("WORKLOAD");

        // PCMRESULT
        jsonStringer.key("expectedResult").object();
        jsonStringer.key("responseMeasureType").value(ResponseMeasureType.DECIMAL);
        jsonStringer.key("response").value("120");
        jsonStringer.endObject();

        jsonStringer.key("metric").value(PerformanceMetric.RESPONSE_TIME);

        jsonStringer.endObject();
    }

    /**
     * @param jsonStringer this json stringer is used to insert the scenario
     *  into the JSON object. This {@link JSONStringer} is required to be in 
     *  a state where a key can be created
     */
    public static void createScenarioOfCPU(JSONStringer jsonStringer) {
        ArrayList<String> workloadIDs = new ArrayList<String>();
        workloadIDs.add("_Uc-igC6OEd-Jla2o7wkBzQ");     // CPU ID

        jsonStringer.key("scenario");
        jsonStringer.object();

        jsonStringer.key("type").value(OptimizationType.MINIMIZATION);
        jsonStringer.key("ids").array();
        for(String id : workloadIDs) jsonStringer.value(id);
        jsonStringer.endArray();
        jsonStringer.key("rate").value(0.5);
        jsonStringer.key("scenario-type").value("WORKLOAD");

        // PCMRESULT
        jsonStringer.key("expectedResult").object();
        jsonStringer.key("responseMeasureType").value(ResponseMeasureType.DECIMAL);
        jsonStringer.key("response").value(6.0);
        jsonStringer.endObject();

        jsonStringer.key("metric").value(PerformanceMetric.RESPONSE_TIME);

        jsonStringer.endObject();
    }

    /*
    public static AbstractPerformancePCMScenario createScenarioOfWorkload() {
        ArrayList<String> workloadIDs = new ArrayList<String>();
        workloadIDs.add(TestConstants.WORKLOAD_ID);
        AbstractPerformancePCMScenario scenario = new PerformancePCMWokloadScenario(OptimizationType.MINIMIZATION,
                workloadIDs, 1.1);
        PCMResult expectedResponse = new PCMResult(ResponseMeasureType.DECIMAL);
        expectedResponse.setResponse(6.0);
        scenario.setExpectedResponse(expectedResponse);
        scenario.setMetric(PerformanceMetric.RESPONSE_TIME);
        return scenario;
    }

    public static AbstractPerformancePCMScenario createScenarioOfCPU() {
        ArrayList<String> cpuIDs = new ArrayList<String>();
        cpuIDs.add(TestConstants.CPU_ID);
        AbstractPerformancePCMScenario scenario = new PerformancePCMWokloadScenario(OptimizationType.MINIMIZATION,
                cpuIDs, 0.5);
        PCMResult expectedResponse = new PCMResult(ResponseMeasureType.DECIMAL);
        expectedResponse.setResponse(6.0);
        scenario.setExpectedResponse(expectedResponse);
        scenario.setMetric(PerformanceMetric.RESPONSE_TIME);
        return scenario;
    }

    public static PerOpteryxPCMBot createPCMBot(AbstractPerformancePCMScenario scenario) {
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
    */

    public static PCMArchitectureInstance createArchitecture(ArchitecturalVersion architecturalVersion) {
        // create Instance
        Allocation allocation = SQuATHelper.loadAllocationModel("file:/" + architecturalVersion.getAbsolutePath()
                + File.separator + architecturalVersion.getAllocationFilename());
        org.palladiosimulator.pcm.system.System system = SQuATHelper.loadSystemModel("file:/"
                + architecturalVersion.getAbsolutePath() + File.separator + architecturalVersion.getSystemFilename());
        ResourceEnvironment resourceenvironment = SQuATHelper
                .loadResourceEnvironmentModel("file:/" + architecturalVersion.getAbsolutePath() + File.separator
                        + architecturalVersion.getResourceEnvironmentFilename());
        Repository repository = SQuATHelper.loadRepositoryModel("file:/" + architecturalVersion.getAbsolutePath()
                + File.separator + architecturalVersion.getRepositoryFilename());
        UsageModel usageModel = SQuATHelper.loadUsageModel("file:/" + architecturalVersion.getAbsolutePath()
                + File.separator + architecturalVersion.getUsageFilename());

        PCMArchitectureInstance architecture = new PCMArchitectureInstance(architecturalVersion.getFileName(),
                repository, system, allocation, resourceenvironment, usageModel);
        if (architecturalVersion.getFullPathToAlternativeRepository() != null) {
            Repository repositoryAlternatives = SQuATHelper
                    .loadRepositoryModel("file:/" + architecturalVersion.getFullPathToAlternativeRepository());
            architecture.setRepositoryWithAlternatives(repositoryAlternatives);
        }

        return architecture;
    }
}
