package io.github.squat_team.performance;

import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMScenario;

public class PerformancePCMScenario extends PCMScenario {
	private PerformanceMetric metric;

	public PerformancePCMScenario(OptimizationType type) {
		super(type);
	}

	public PerformanceMetric getMetric() {
		return metric;
	}

	public void setMetric(PerformanceMetric metric) {
		this.metric = metric;
	}

}
