package io.github.squat_team.performance.peropteryx;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.LogManager;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.palladiosimulator.pcm.core.entity.NamedElement;
import org.palladiosimulator.solver.models.PCMInstance;

import de.fakeller.palladio.analysis.pcm2lqn.runner.PcmLqnsAnalyzer;
import de.fakeller.palladio.analysis.pcm2lqn.runner.PcmLqnsAnalyzerConfig;
import de.fakeller.palladio.analysis.pcm2lqn.runner.PcmLqnsAnalyzerContext;
import de.fakeller.palladio.analysis.provider.FileSystemProvider;
import de.fakeller.palladio.config.PcmModelConfig;
import de.fakeller.performance.analysis.result.PerformanceResult;
import io.github.squat_team.AbstractPCMBot;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.performance.PerformancePCMScenario;
import io.github.squat_team.performance.lqns.LQNSResultConverter;
import io.github.squat_team.performance.lqns.LQNSResultExtractor;
import io.github.squat_team.performance.lqns.LQNSDetailedResultWriter;
import io.github.squat_team.performance.lqns.LQNSResult;
import io.github.squat_team.performance.peropteryx.configuration.Configuration;
import io.github.squat_team.performance.peropteryx.configuration.PerOpteryxConfig.Mode;
import io.github.squat_team.performance.peropteryx.environment.PalladioEclipseEnvironment;
import io.github.squat_team.performance.peropteryx.export.ExportMode;
import io.github.squat_team.performance.peropteryx.export.OptimizationDirection;
import io.github.squat_team.performance.peropteryx.export.PerOpteryxPCMResult;
import io.github.squat_team.performance.peropteryx.start.HeadlessPerOpteryxRunner;

public class PerOpteryxPCMBot extends AbstractPCMBot {
	private static Logger logger = Logger.getLogger(PerOpteryxPCMBot.class.getName());
	private Level loglevel;
	private Configuration configuration;
	private PerformancePCMScenario performanceScenario;
	private Boolean debugMode = false;
	private Boolean detailedAnalysis = false;

	/**
	 * This bot uses a LQN solver to analyze, and PerOpteryx (based on the LQN
	 * solver) to optimize architectures. All PCM files have to be in the same
	 * directory! If the bot fails, null/a empty list will be returned.
	 * 
	 * @param scenario
	 * @param configuration
	 *            the configuration should at least contain the paths to the
	 *            general pcm files (not the instance!) and the paths for the
	 *            export of the pcm models (should not contain other
	 *            data/folders!). A QML file and a designdecision file will be
	 *            generated automatically, if no path is given. Some values will
	 *            be added or overwritten later.
	 */
	public PerOpteryxPCMBot(PerformancePCMScenario scenario, Configuration configuration) {
		super(scenario);
		this.configuration = configuration;
		this.performanceScenario = scenario;
	}

	@Override
	public PCMScenarioResult analyze(PCMArchitectureInstance currentArchitecture) {
		try {
			configureWith(currentArchitecture);
			configureWith(this.performanceScenario);
			deactivateLog();
			setupEnvironmentforAnalysis();
			PCMInstance pcmInstance = buildPcmInstanceForAnalysis();
			String outputPath = executeHeadlessLqns(pcmInstance);
			LQNSResult lqnsResult = LQNSResultExtractor.extract(pcmInstance, configuration, outputPath);
			activateLog();
			if (detailedAnalysis) {
				analyzeDetailed(currentArchitecture);
			}
			return LQNSResultConverter.convert(currentArchitecture, lqnsResult, performanceScenario.getMetric(), this);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	private void analyzeDetailed(PCMArchitectureInstance currentArchitecture) {
		try {
			configureWith(currentArchitecture);
			configureWith(this.performanceScenario);
			deactivateLog();
			setupEnvironmentforAnalysis();
			PCMInstance pcmInstance = buildPcmInstanceForAnalysis();

			PerOpteryxPCMDetailedAnalyser detailedAnalyser = new PerOpteryxPCMDetailedAnalyser(pcmInstance);
			PerformanceResult<NamedElement> analysisResult = detailedAnalyser.analyze();
			LQNSDetailedResultWriter detailedWriter = new LQNSDetailedResultWriter(analysisResult);
			File exportDestination = LQNSDetailedResultWriter.determineFileDestination(currentArchitecture);
			detailedWriter.writeTo(exportDestination);

			activateLog();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void analyzeDetailed(List<PCMScenarioResult> results) {
		for (PCMScenarioResult result : results) {
			analyzeDetailed(result.getResultingArchitecture());
		}
	}

	@Override
	public List<PCMScenarioResult> searchForAlternatives(PCMArchitectureInstance currentArchitecture) {
		try {
			configureWith(currentArchitecture);
			configureWith(this.performanceScenario);
			configurePerOpteryx();
			configureExportForOptimization();
			configurePerOpteryxForOptimization();
			validateConfiguration();
			Future<List<PerOpteryxPCMResult>> future = runPerOpteryx();
			List<PCMScenarioResult> results = exportOptimizationResults(future);
			if (detailedAnalysis) {
				analyzeDetailed(results);
			}
			return results;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<PCMScenarioResult>();
		}
	}

	private void setupEnvironmentforAnalysis() {
		PalladioEclipseEnvironment.INSTANCE.setup(configuration.getPcmModelsConfig().getPathmapFolder());
		de.fakeller.palladio.environment.PalladioEclipseEnvironment.INSTANCE.setup();
	}

	private PCMInstance buildPcmInstanceForAnalysis() {
		PcmModelConfig pcmConfig = new PcmModelConfig();
		pcmConfig.setAllocationModel(configuration.getPcmInstanceConfig().getAllocationModel());
		pcmConfig.setUsageModel(configuration.getPcmInstanceConfig().getUsageModel());
		FileSystemProvider provider = new FileSystemProvider(pcmConfig);
		return provider.provide();
	}

	private String executeHeadlessLqns(PCMInstance pcmInstance) {
		final PcmLqnsAnalyzerConfig config = PcmLqnsAnalyzerConfig.defaultConfig();
		final PcmLqnsAnalyzer analyzer = new PcmLqnsAnalyzer(config);
		final PcmLqnsAnalyzerContext ctx = analyzer.setupAnalysis(pcmInstance);
		ctx.executePalladio();
		return config.getOutputPath();
	}

	private void deactivateLog() {
		if (!debugMode) {
			LogManager.getLogManager().reset();
			loglevel = org.apache.log4j.LogManager.getRootLogger().getLevel();
			org.apache.log4j.LogManager.getRootLogger().setLevel(Level.OFF);
		}
	}

	private void activateLog() {
		if (!debugMode) {
			org.apache.log4j.LogManager.getRootLogger().setLevel(loglevel);
		}
	}

	private List<PCMScenarioResult> exportOptimizationResults(Future<List<PerOpteryxPCMResult>> future) {
		try {
			List<PerOpteryxPCMResult> peropteryxResult = future.get();
			return PerOpteryxResultConverter.convert(peropteryxResult, this);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<PCMScenarioResult>();
		} catch (ExecutionException e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<PCMScenarioResult>();
		}
	}

	private Future<List<PerOpteryxPCMResult>> runPerOpteryx() {
		HeadlessPerOpteryxRunner runner = new HeadlessPerOpteryxRunner();
		runner.init(configuration);
		runner.setDebugMode(this.debugMode);
		return ThreadPoolProvider.POOL.submit(runner);
	}

	private void configurePerOpteryx() {
		String designDecisionFile = configuration.getPerOpteryxConfig().getDesignDecisionFile();
		if (designDecisionFile == null || designDecisionFile.isEmpty()) {
			configuration.getPerOpteryxConfig().setMode(Mode.DESIGN_DECISIONS_AND_OPTIMIZE);
		} else {
			configuration.getPerOpteryxConfig().setMode(Mode.OPTIMIZE);
		}
	}

	private void configurePerOpteryxForOptimization() {
		// TODO: choose good values
		configuration.getTacticsConfig().useTactics(true);
		if (configuration.getPerOpteryxConfig().getGenerationSize() <= 1
				&& configuration.getPerOpteryxConfig().getMaxIterations() <= 1) {
			configuration.getPerOpteryxConfig().setGenerationSize(100);
			configuration.getPerOpteryxConfig().setMaxIterations(20);
		}

		// TODO: use a stop criteria? Will improve speed in many cases
		configuration.getTerminationCriteriaConfig().setActivateTerminationCriteria(true);
		configuration.getTerminationCriteriaConfig().setActivateInsignificantFrontChange(true);
		configuration.getTerminationCriteriaConfig().setInsignificantFrontChangeGenerationNumber(6);
		configuration.getTerminationCriteriaConfig().setInsignificantFrontChangeImprovementPercentage(1);
	}

	private void configureExportForOptimization() {
		// TODO: which results should be exported? all/better than expected/x
		// best/only the best?
		configuration.getExporterConfig().setAmount(10);
		configuration.getExporterConfig().setExportMode(ExportMode.AMOUNT);
	}

	private void configureWith(PCMArchitectureInstance currentArchitecture) {
		String allocationPath = currentArchitecture.getAllocation().eResource().getURI().toString();
		String usagemodelPath = currentArchitecture.getUsageModel().eResource().getURI().toString();
		allocationPath = allocationPath.replaceAll("file:/", "");
		usagemodelPath = usagemodelPath.replaceAll("file:/", "");
		configuration.getPcmInstanceConfig().setAllocationModel(allocationPath);
		configuration.getPcmInstanceConfig().setUsageModel(usagemodelPath);
	}

	private void configureWith(PerformancePCMScenario scenario) throws IOException {
		configureOptimizationDirection(scenario.getType());
		configureBoundaryValue(scenario.getExpectedResult().getResponse());
		String qmlPath = configuration.getPerOpteryxConfig().getQmlDefinitionFile();
		if (qmlPath == null || qmlPath.isEmpty()) {
			String generatedQmlFilePath = PerOpteryxQMLConverter
					.convert(configuration.getPcmInstanceConfig().getUsageModel(), scenario);
			configuration.getPerOpteryxConfig().setQmlDefinitionFile(generatedQmlFilePath);
		}
	}

	private void configureOptimizationDirection(OptimizationType type) {
		if (type == OptimizationType.MINIMIZATION) {
			configuration.getExporterConfig().setOptimizationDirection(OptimizationDirection.MINIMIZE);
		} else if (type == OptimizationType.MAXIMIZATION) {
			configuration.getExporterConfig().setOptimizationDirection(OptimizationDirection.MAXIMIZE);
		}
	}

	@SuppressWarnings("rawtypes")
	private void configureBoundaryValue(Comparable comparable) {
		// TODO: maybe there is a better way to do this: either change interface
		// type or type in peropteryx, but just comparable is maybe to general -
		// not needed if we don't want the ExportMode to be 'Better'
		if (comparable instanceof Double) {
			configuration.getExporterConfig().setBoundaryValue((Double) comparable);
		} else if (comparable instanceof Float) {
			Float value = (Float) comparable;
			configuration.getExporterConfig().setBoundaryValue(value.doubleValue());
		} else if (comparable instanceof Integer) {
			Integer value = (Integer) comparable;
			configuration.getExporterConfig().setBoundaryValue(value.doubleValue());
		}
	}

	private void validateConfiguration() {
		if (configuration == null) {
			throw new RuntimeException("Headless PerOpteryx needs a configuration");
		}
		if (!configuration.validate()) {
			throw new RuntimeException("Configuration for Headless PerOpteryx incomplete");
		}
	}

	public Boolean getDebugMode() {
		return debugMode;
	}

	/**
	 * Prints all information from the loggers to the console. This option
	 * requires a higher computational effort and is therefore deactivated by
	 * default.
	 * 
	 * @param debugMode
	 *            true activates the unfiltered console output
	 */
	public void setDebugMode(Boolean debugMode) {
		this.debugMode = debugMode;
	}

	public Boolean getDetailedAnalysis() {
		return detailedAnalysis;
	}

	/**
	 * The detailed analysis writes additional information to the destination of
	 * the pcm instances. This includes the utilization of the components and
	 * servers used in the model. This option requires a higher computational
	 * effort and is therefore deactivated by default.
	 * 
	 * @param detailedAnalysis
	 *            true activates the detailed analysis
	 */
	public void setDetailedAnalysis(Boolean detailedAnalysis) {
		this.detailedAnalysis = detailedAnalysis;
	}

}
