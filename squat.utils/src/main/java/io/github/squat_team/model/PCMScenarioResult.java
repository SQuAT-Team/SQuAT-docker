package io.github.squat_team.model;

import io.github.squat_team.AbstractPCMBot;
import io.github.squat_team.utility.PCMScenarioSatisfaction;

public class PCMScenarioResult {
	private AbstractPCMBot originatingBot;
	private PCMArchitectureInstance resultingArchitecture;
	//Optional (could be empty)
	private PCMTactic appliedTactic;
	
	private PCMResult result;
	
	public PCMScenarioResult(AbstractPCMBot originatingBot) {
		super();
		this.originatingBot = originatingBot;
	}

	//We should use a tri-state return type, instead a binary/integer return type
	public int isSatisfied() throws Exception {
		if(result == null || resultingArchitecture == null)
			throw new Exception("Results haven't been calculated yet");
		else {
			return PCMScenarioSatisfaction.compute(this);
		}
	}

	public AbstractPCMBot getOriginatingBot() {
		return originatingBot;
	}

	public void setOriginatingBot(AbstractPCMBot originatingBot) {
		this.originatingBot = originatingBot;
	}

	public PCMArchitectureInstance getResultingArchitecture() {
		return resultingArchitecture;
	}

	public void setResultingArchitecture(PCMArchitectureInstance resultingArchitecture) {
		this.resultingArchitecture = resultingArchitecture;
	}

	public PCMTactic getAppliedTactic() {
		return appliedTactic;
	}

	public void setAppliedTactic(PCMTactic appliedTactic) {
		this.appliedTactic = appliedTactic;
	}

	public PCMResult getResult() {
		return result;
	}

	public void setResult(PCMResult result) {
		this.result = result;
	}
}
