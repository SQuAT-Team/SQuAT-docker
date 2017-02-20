package io.github.squat_team.performance.peropteryx;

import org.palladiosimulator.pcm.core.entity.NamedElement;
import org.palladiosimulator.solver.models.PCMInstance;

import de.fakeller.palladio.analysis.pcm2lqn.runner.PcmLqnsAnalyzer;
import de.fakeller.palladio.analysis.pcm2lqn.runner.PcmLqnsAnalyzerConfig;
import de.fakeller.palladio.analysis.pcm2lqn.runner.PcmLqnsAnalyzerContext;
import de.fakeller.performance.analysis.result.PerformanceResult;

/**
 * Conducts a more detailed analysis of a PCM instance. This step should not be
 * done together with the normal analysis, as the extended analysis will make
 * changes to the LQN Solver output file and (possibly) the PCM instance itself.
 * 
 * The environment for Palladio analysis has to be setup before!
 */
public class PerOpteryxPCMDetailedAnalyser {
	private PCMInstance pcmInstance;

	public PerOpteryxPCMDetailedAnalyser(PCMInstance pcmInstance) {
		this.pcmInstance = pcmInstance;
	}

	public PerformanceResult<NamedElement> analyze() {
		final PcmLqnsAnalyzerConfig config = PcmLqnsAnalyzerConfig.defaultConfig();
		final PcmLqnsAnalyzer analyzer = new PcmLqnsAnalyzer(config);
		final PcmLqnsAnalyzerContext ctx = analyzer.setupAnalysis(pcmInstance);
		PerformanceResult<NamedElement> result = ctx.analyze();
		ctx.untrace();
		return result;
	}

}
