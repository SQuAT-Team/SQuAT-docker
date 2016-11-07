package io.github.squat_team.utility;

import io.github.squat_team.model.PCMScenarioResult;

public interface UtilityFunction {
	
	//This should ideally return a value between 0 and 1
	int compute(PCMScenarioResult scenarioResult);
}