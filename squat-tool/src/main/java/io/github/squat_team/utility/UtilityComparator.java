package io.github.squat_team.utility;

import java.util.Comparator;

import io.github.squat_team.model.PCMScenarioResult;

public class UtilityComparator implements Comparator<PCMScenarioResult> {
	private UtilityFunction function;
	
	public UtilityComparator(UtilityFunction function) {
		this.function = function;
	}

	public int compare(PCMScenarioResult sr1, PCMScenarioResult sr2) {
		return function.compute(sr2) - function.compute(sr1);
	}
}
