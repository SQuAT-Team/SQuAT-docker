package io.github.squat_team.modifiability.kamp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.modifiability.kamp.KAMPPCMBot;
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
import io.github.squat_team.util.PCMHelper;
import io.github.squat_team.util.SQuATHelper;

public class ModifiabilityBotTest {

	private final String MODEL_NAME = "default"; // cocome-cloud
	private final String MODEL_PATH = "/home/sebastian/git/SQuAT-docker/squat.modifiability/model"; // models/cocomeWithoutPickUpStoreAndServiceAdapter
	private final String ALTERNATIVE_REPOSITORY_PATH = "/home/sebastian/git/SQuAT-docker/squat.modifiability/model/alternativeRepository.repository"; // /Users/santiagovidal/Documents/Programacion/kamp-test/squat-tool/models/cocomeWithoutPickUpStoreAndServiceAdapter/alternativescocome-cloud.repository

	@Test
	public void runStplusTest() throws Exception {
		System.out.println("Start STPLUS Test");
		System.out.println("Initialize Architecture");
		ArchitecturalVersion initialArchitecture = initializeInitialArchitecture();
		System.out.println("Initialize Scenario");
		PCMScenario scenario = initializeScenario();

		System.out.println("Run initial analysis");
		float initialValue = runInitialAnalysis(scenario, initialArchitecture);
		System.out.println("Initial value is: " + initialValue);
		System.out.println("Run search for alternatives");
		List<ArchitecturalVersion> alternatives = searchForAlternatives(initialArchitecture);
		System.out.println("Run analysis of alternatives");
		List<Float> alternativeValues = runAnalysis(alternatives, scenario);
		System.out.println("=========================");
		System.out.println("The returned values are:");
		for (Float value : alternativeValues) {
			System.out.println(value);
		}
		System.out.println("Finish STPLUS Test");
	}

	/**
	 * Sets the initial architecture.
	 * 
	 * @return the architecture.
	 */
	private ArchitecturalVersion initializeInitialArchitecture() {
		ArchitecturalVersion initialArchitecture = new ArchitecturalVersion(MODEL_NAME, MODEL_PATH, "");
		initialArchitecture.setFullPathToAlternativeRepository(ALTERNATIVE_REPOSITORY_PATH);
		return initialArchitecture;
	}

	/**
	 * Initializes a scenario for the modifiability bot.
	 * 
	 * @return the scenario.
	 */
	private PCMScenario initializeScenario() {
		Float responseTimeScenario = 120f;
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
	private PCMScenario createModifiabilityScenarioForStplus(ResponseMeasureType type, Comparable<Float> response) {
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

	/**
	 * Initializes the initial architecture.
	 * 
	 * @param scenario
	 *            the scenario of the bot.
	 * @param initialArchitecture
	 *            the initial architecture.
	 * @throws Exception
	 */
	private float runInitialAnalysis(PCMScenario scenario, ArchitecturalVersion initialArchitecture) throws Exception {
		return calculateModifiabilityComplexity(scenario, KAMPPCMBot.TYPE_COMPLEXITY, initialArchitecture);
	}

	/**
	 * Searches for alternatives for the given architecture.
	 * 
	 * @param initialArchitecture
	 *            the initial architecture.
	 * @return the alternatives
	 */
	private List<ArchitecturalVersion> searchForAlternatives(ArchitecturalVersion initialArchitecture) {
		return (new ModifiabilityTransformationsFactory()).runModifiabilityTransformationsInAModel(initialArchitecture);
	}

	/**
	 * Analysis the given architectures based on the given scenario.
	 * 
	 * @param alternatives
	 * @param scenario
	 * @return
	 * @throws Exception
	 */
	private List<Float> runAnalysis(List<ArchitecturalVersion> alternatives, PCMScenario scenario) throws Exception {
		List<Float> results = new ArrayList<Float>();
		for (ArchitecturalVersion architecture : alternatives) {
			results.add(calculateModifiabilityComplexity(scenario, KAMPPCMBot.TYPE_COMPLEXITY, architecture));
		}
		return results;
	}

	/**
	 * Analysis of a specific architecture instance.
	 * 
	 * @param scenario
	 * @param evaluationType
	 * @param architecturalVersion
	 * @return
	 * @throws Exception
	 */
	private float calculateModifiabilityComplexity(PCMScenario scenario, String evaluationType,
			ArchitecturalVersion architecturalVersion) throws Exception {
		boolean debug = true;
		@SuppressWarnings("unchecked")
		Comparable<Float> expectedResponse = scenario.getExpectedResult().getResponse();
		if (debug)
			java.lang.System.out.println("The goal of scenario: " + expectedResponse.toString());
		KAMPPCMBot bot = new KAMPPCMBot(scenario);
		bot.setEvaluationType(evaluationType);
		if (debug)
			java.lang.System.out.println("The evaluation type is: " + evaluationType);
		//

		PCMArchitectureInstance model = PCMHelper.loadSpecificModel(architecturalVersion);
		PCMScenarioResult scenarioResult = bot.analyze(model);
		String satisfaction_alt1 = scenarioResult.isSatisfied() >= 0 ? "SATISFIED" : "NOT SATISFIED";
		if (debug)
			java.lang.System.out
					.println("The scenario satisfaction with " + model.getName() + " is: " + satisfaction_alt1);
		@SuppressWarnings("unchecked")
		Comparable<Float> response_alt1 = scenarioResult.getResult().getResponse();
		return ((Float) response_alt1).floatValue();
	}



}
