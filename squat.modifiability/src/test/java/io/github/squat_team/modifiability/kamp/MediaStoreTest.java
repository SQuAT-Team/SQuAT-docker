package io.github.squat_team.modifiability.kamp;

import org.junit.Test;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.junit.Assert;

import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.modifiability.ModifiabilityElement;
import io.github.squat_team.modifiability.ModifiabilityInstruction;
import io.github.squat_team.modifiability.ModifiabilityOperation;
import io.github.squat_team.modifiability.ModifiabilityPCMScenario;
import io.github.squat_team.util.SQuATHelper;

public class MediaStoreTest {
	private static String machinePath = "/Users/santiagovidal/Documents/Programacion/kamp-test/squat-tool/src/test/resources/";
	private static String dirPath = machinePath + "io/github/squat_team/casestudies/mediastore/";
	private static String repositoryFile = dirPath + "ms.repository";
	private static String resourceEnvironmentFile = dirPath + "ms.resourceenvironment";
	private static String baseSystemFile = dirPath + "ms_base.system";
	private static String baseAllocationFile = dirPath + "ms_base.allocation";
	private static String baseUsageFile = dirPath + "ms_base_usage_all.usagemodel";
	
	@SuppressWarnings("rawtypes")
	// Deactivated for Docker: @Test
	public void testAnalysis() throws Exception {
		PCMScenario scenario = this.createModifiabilityScenario();
		KAMPPCMBotDeprecated bot = new KAMPPCMBotDeprecated(scenario);
		PCMArchitectureInstance mediaStore = loadMediaStore("MediaStore");
		
		PCMScenarioResult scenarioResult = bot.analyze(mediaStore);
		Assert.assertTrue(scenarioResult.isSatisfied() < 0);
		
		int AFFECTED_COMPONENTS = 16;
		Comparable response = scenarioResult.getResult().getResponse();
		Assert.assertEquals(((Integer)response).intValue(), AFFECTED_COMPONENTS);
	}
	
	//@Test
	public void testAlternatives() {
		PCMScenario scenario = this.createModifiabilityScenario();
		KAMPPCMBotDeprecated bot = new KAMPPCMBotDeprecated(scenario);
		PCMArchitectureInstance mediaStore = loadMediaStore("MediaStore");
	}
	
	private PCMScenario createModifiabilityScenario() {
		ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(ResponseMeasureType.NUMERIC);
		expectedResult.setResponse(new Integer(5));
		scenario.setExpectedResponse(expectedResult);
		ModifiabilityInstruction i1 = new ModifiabilityInstruction();
		i1.operation = ModifiabilityOperation.MODIFY;
		i1.element = ModifiabilityElement.INTERFACE;
		i1.parameters.put("name", "IDownload");
		scenario.addChange(i1);
		ModifiabilityInstruction i2 = new ModifiabilityInstruction();
		i2.operation = ModifiabilityOperation.MODIFY;
		i2.element = ModifiabilityElement.COMPONENT;
		i2.parameters.put("name", "MediaAccess");
		scenario.addChange(i2);
		return scenario;
	}
	
	private PCMArchitectureInstance loadMediaStore(String name) {
		Repository repository = SQuATHelper.loadRepositoryModel(repositoryFile);
		ResourceEnvironment resourceEnvironment = SQuATHelper.loadResourceEnvironmentModel(resourceEnvironmentFile);
		System system = SQuATHelper.loadSystemModel(baseSystemFile);
		Allocation allocation = SQuATHelper.loadAllocationModel(baseAllocationFile);
		UsageModel usageModel = SQuATHelper.loadUsageModel(baseUsageFile);
		PCMArchitectureInstance instance = new PCMArchitectureInstance(name, repository, system, allocation, resourceEnvironment, usageModel);
		return instance;
	}
	
}
