package io.github.squat_team.modifiability.kamp;

import java.util.List;

import org.junit.Test;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.modifiability.ModifiabilityElement;
import io.github.squat_team.modifiability.ModifiabilityInstruction;
import io.github.squat_team.modifiability.ModifiabilityOperation;
import io.github.squat_team.modifiability.ModifiabilityPCMScenario;
import io.github.squat_team.util.PCMHelper;

/**
 * Tests a full execution of {@link KAMPPCMBot} for the STPLUS model. This
 * includes analysis and search for alternatives.
 */
public class ModifiabilityBotTest {
	public static final String MODEL_NAME = "default"; // cocome-cloud
	public static final String MODEL_PATH = "model"; // models/cocomeWithoutPickUpStoreAndServiceAdapter
	public static final String ALTERNATIVE_REPOSITORY_PATH = System.getProperty("user.dir") // <- HACK: For some reason, the implementation expects an absolute path 
			+ "/model/alternativeRepository.repository"; // /Users/santiagovidal/Documents/Programacion/kamp-test/squat-tool/models/cocomeWithoutPickUpStoreAndServiceAdapter/alternativescocome-cloud.repository

	@Test
	public void runStplusTest() throws Exception {
		System.out.println("Start STPLUS Test");
		System.out.println("Initialize Architecture");
		PCMArchitectureInstance initialArchitecture = initializeInitialArchitecture();
		System.out.println("Initialize Scenario");
		ModifiabilityPCMScenario scenario = initializeScenario();

		KAMPPCMBot bot = new KAMPPCMBot(scenario);
		bot.setEvaluationType(EvaluationType.COMPLEXITY);
		System.out.println("Run initial analysis");
		float initialValue = (Float) bot.analyze(initialArchitecture).getResult().getResponse();
		System.out.println("Initial value is: " + initialValue);
		System.out.println("Run search for alternatives");
		List<PCMScenarioResult> alternatives = bot.searchForAlternatives(initialArchitecture);
		System.out.println("=========================");
		System.out.println("The returned values are:");
		for (PCMScenarioResult alternative : alternatives) {
			System.out.println(alternative.getResult().getResponse());
		}
		System.out.println("Finish STPLUS Test");
	}

	/**
	 * Sets the initial architecture.
	 * 
	 * @return the architecture.
	 */
	private PCMArchitectureInstance initializeInitialArchitecture() {
		ArchitecturalVersion initialArchitecture = new ArchitecturalVersion(MODEL_NAME, MODEL_PATH, "");
		initialArchitecture.setFullPathToAlternativeRepository(ALTERNATIVE_REPOSITORY_PATH);
		return PCMHelper.createArchitecture(initialArchitecture);
	}

	/**
	 * Initializes a scenario for the modifiability bot.
	 * 
	 * @return the scenario.
	 */
	private ModifiabilityPCMScenario initializeScenario() {
		Float responseTimeScenario = 120f;
		return (new StplusScenarios()).createModifiabilityScenarioS1(ResponseMeasureType.DECIMAL, responseTimeScenario);
	}

}
