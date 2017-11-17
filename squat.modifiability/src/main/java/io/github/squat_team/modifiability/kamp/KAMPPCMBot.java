package io.github.squat_team.modifiability.kamp;

import java.util.ArrayList;
import java.util.List;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.AbstractPCMBot;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.modifiability.ModifiabilityPCMScenario;
import io.github.squat_team.util.PCMHelper;

/**
 * A implementation of a modifiability bot. The analysis is based on KAMP and
 * the search for alternatives is based on Henshin transformations.
 * <p>
 * Only a minimal result is returned. This means that the current implementation
 * does not return all information specified by the SQuAT Bot interface.
 * However, it is enough for the negotiator to work
 * </p>
 */
public class KAMPPCMBot extends AbstractPCMBot {
	private EvaluationType evaluationType = EvaluationType.COMPLEXITY;

	public KAMPPCMBot(ModifiabilityPCMScenario scenario) {
		super(scenario);
	}

	public EvaluationType getEvaluationType() {
		return evaluationType;
	}

	public void setEvaluationType(EvaluationType evaluationType) {
		this.evaluationType = evaluationType;
	}

	@Override
	public PCMScenarioResult analyze(PCMArchitectureInstance currentArchitecture) {
		// Run the propagation and create the result
		PCMScenarioResult scenarioResult = new PCMScenarioResult(this);
		// Register the initial architecture
		// There is no tactic, because this is just an evaluation
		scenarioResult.setAppliedTactic(null);
		// The resulting architecture is just the original one, because we did not make
		// changes
		scenarioResult.setResultingArchitecture(currentArchitecture);
		// Run the analyzer
		KAMPAnalyzer analyzer = new KAMPAnalyzer((ModifiabilityPCMScenario) scenario, evaluationType);
		scenarioResult.setResult(analyzer.analyze(currentArchitecture));

		if (KAMPPCMBotTriggers.DEBUG) {
			java.lang.System.out
					.println("The goal of scenario: " + scenario.getExpectedResult().getResponse().toString());
			java.lang.System.out.println("The evaluation type is: " + evaluationType);
			String satisfaction_alt1;
			try {
				satisfaction_alt1 = scenarioResult.isSatisfied() >= 0 ? "SATISFIED" : "NOT SATISFIED";
				java.lang.System.out.println("The scenario satisfaction with " + currentArchitecture.getName() + " is: "
						+ satisfaction_alt1);
			} catch (Exception e) {
				// The debug print out should not let the bot fail
			}
		}
		return scenarioResult;
	}

	@Override
	public List<PCMScenarioResult> searchForAlternatives(PCMArchitectureInstance currentArchitecture) {
		ArchitecturalVersion architecture = PCMHelper.createArchitecture(currentArchitecture);
		List<ArchitecturalVersion> transformationResults = (new ModifiabilityTransformationsFactory())
				.runModifiabilityTransformationsInAModel(architecture);

		List<PCMScenarioResult> results = new ArrayList<>();
		for (ArchitecturalVersion transformationResult : transformationResults) {
			results.add(analyze(PCMHelper.createArchitecture(transformationResult)));
		}
		return results;
	}

}
