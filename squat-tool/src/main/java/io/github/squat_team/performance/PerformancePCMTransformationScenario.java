package io.github.squat_team.performance;

import io.github.squat_team.model.PCMArchitectureInstance;

/**
 * Scenarios for performance are based on a change in the PCM instance (e.g.
 * workload intensity). This change has to be reverted in the end.
 */
public interface PerformancePCMTransformationScenario {

	/**
	 * The transformation representing the scenario (e.g. doubled workload). Has
	 * to be applied before the analysis / optimization.
	 * 
	 * @param architecture
 	 *            the initial architecture, will be changed. 
	 */
	public void transform(PCMArchitectureInstance architecture);

	/**
	 * The transformation which reverts the scenario transformation. Has to be
	 * applied after the analysis / optimization.
	 * 
	 * @param architecture
	 *            the initial architecture, will be changed.
	 */
	public void inverseTransform(PCMArchitectureInstance architecture);
}