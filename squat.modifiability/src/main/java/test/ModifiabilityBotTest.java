package test;

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
import io.github.squat_team.modifiability.kamp.EvaluationType;
import io.github.squat_team.modifiability.kamp.KAMPPCMBot;
import io.github.squat_team.util.PCMHelper;

public class ModifiabilityBotTest {

	public static void main(String[] args) throws Exception {
		runStplusTest();
		runStplusTest();
	}

	private static void runStplusTest() throws Exception {
		System.out.println("Start STPLUS Test");
		System.out.println("Initialize Architecture");
		PCMArchitectureInstance initialArchitecture = initializeInitialArchitecture();
		System.out.println("Initialize Scenario");
		ModifiabilityPCMScenario scenario = initializeScenario();

		KAMPPCMBot bot = new KAMPPCMBot(scenario);
		bot.setEvaluationType(EvaluationType.COMPLEXITY);
		System.out.println("Run initial analysis");
		float initialValue = (float) bot.analyze(initialArchitecture).getResult().getResponse();
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
	private static PCMArchitectureInstance initializeInitialArchitecture() {
		ArchitecturalVersion initialArchitecture = new ArchitecturalVersion(TestConstants.MODEL_NAME,
				TestConstants.MODEL_PATH, "");
		initialArchitecture.setFullPathToAlternativeRepository(TestConstants.ALTERNATIVE_REPOSITORY_PATH);
		return PCMHelper.createArchitecture(initialArchitecture);
	}

	/**
	 * Initializes a scenario for the modifiability bot.
	 * 
	 * @return the scenario.
	 */
	public static ModifiabilityPCMScenario initializeScenario() {
		double responseTimeScenario = 120;
		return createModifiabilityScenarioForStplus(ResponseMeasureType.DECIMAL, responseTimeScenario);
	}

	/**
	 * Creates a modifiability scenario for the stplus model.
	 * 
	 * @param type
	 *            the type of the response measure type.
	 * @param response
	 *            the expected response for the scenario.
	 * @return
	 */
	private static ModifiabilityPCMScenario createModifiabilityScenarioForStplus(ResponseMeasureType type,
			Comparable<Double> response) {
		ModifiabilityPCMScenario scenario = new ModifiabilityPCMScenario(OptimizationType.MINIMIZATION);
		PCMResult expectedResult = new PCMResult(type);
		expectedResult.setResponse(response);
		scenario.setExpectedResponse(expectedResult);

		ModifiabilityInstruction i1 = new ModifiabilityInstruction();
		i1.operation = ModifiabilityOperation.MODIFY;
		i1.element = ModifiabilityElement.INTERFACE;
		i1.parameters.put("name", "IExternalPayment");
		scenario.addChange(i1);
		ModifiabilityInstruction i2 = new ModifiabilityInstruction();
		i2.operation = ModifiabilityOperation.MODIFY;
		i2.element = ModifiabilityElement.COMPONENT;
		i2.parameters.put("name", "BusinessTripMgmt");
		scenario.addChange(i2);

		return scenario;
	}
}
