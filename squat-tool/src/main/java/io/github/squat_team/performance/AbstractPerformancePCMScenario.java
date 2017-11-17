package io.github.squat_team.performance;

import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMScenario;

public abstract class AbstractPerformancePCMScenario extends PCMScenario
		implements PerformancePCMTransformationScenario {
	private PerformanceMetric metric;

	public AbstractPerformancePCMScenario(OptimizationType type) {
		super(type);
	}

	public PerformanceMetric getMetric() {
		return metric;
	}

	public void setMetric(PerformanceMetric metric) {
		this.metric = metric;
	}
}