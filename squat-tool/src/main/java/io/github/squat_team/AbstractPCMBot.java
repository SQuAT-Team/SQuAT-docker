package io.github.squat_team;

import java.util.List;

import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.model.PCMScenarioResult;

public abstract class AbstractPCMBot {
	protected PCMScenario scenario;
	
	public AbstractPCMBot(PCMScenario scenario) {
		super();
		this.scenario = scenario;
	}
	
	public abstract PCMScenarioResult analyze(PCMArchitectureInstance currentArchitecture);
	public abstract List<PCMScenarioResult> searchForAlternatives(PCMArchitectureInstance currentArchitecture);
	
	public PCMScenario getScenario() {
		return scenario;
	}
	
	public void setScenario(PCMScenario scenario) {
		this.scenario = scenario;
	}
}
