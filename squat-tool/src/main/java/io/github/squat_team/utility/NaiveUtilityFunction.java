package io.github.squat_team.utility;

import io.github.squat_team.model.PCMScenarioResult;

public class NaiveUtilityFunction implements UtilityFunction {

	public int compute(PCMScenarioResult scenarioResult) {
		int utility = -1;
		try {
			utility = PCMScenarioSatisfaction.compute(scenarioResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return utility;
	}
}
