package io.github.squat_team.performance.peropteryx.start;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.LogManager;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import de.uka.ipd.sdq.dsexplore.launch.DSELaunch;
import de.uka.ipd.sdq.dsexplore.launch.DSEWorkflowConfiguration;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;
import io.github.squat_team.performance.peropteryx.configuration.Configuration;
import io.github.squat_team.performance.peropteryx.configuration.DSEWorkflowConfigurationBuilder;
import io.github.squat_team.performance.peropteryx.environment.PalladioEclipseEnvironment;
import io.github.squat_team.performance.peropteryx.environment.PerOpteryxEclipseEnvironment;
import io.github.squat_team.performance.peropteryx.export.PCMFileExporter;
import io.github.squat_team.performance.peropteryx.export.PCMResultsProvider;
import io.github.squat_team.performance.peropteryx.export.PerOpteryxPCMResult;
import io.github.squat_team.performance.peropteryx.overwrite.MyDSELaunch;
import io.github.squat_team.performance.peropteryx.overwrite.jobs.MyPerOpteryxJob;

public class HeadlessPerOpteryxRunner implements Callable<List<PerOpteryxPCMResult>> {
	private static Logger logger = Logger.getLogger(HeadlessPerOpteryxRunner.class.getName());
	private Configuration configuration;
	private boolean debugMode = false;
	private Level loglevel;

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public void init(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public List<PerOpteryxPCMResult> call() throws Exception {
		List<PerOpteryxPCMResult> results;
		validate();
		deactivateLog();
		initialize();
		execute();
		results = export();
		activateLog();
		cleanUp();
		return results;
	}

	private void activateLog() {
		if (!debugMode) {
			org.apache.log4j.LogManager.getRootLogger().setLevel(loglevel);
		}
	}

	private void deactivateLog() {
		if (!debugMode) {
			LogManager.getLogManager().reset();
			loglevel = org.apache.log4j.LogManager.getRootLogger().getLevel();
			org.apache.log4j.LogManager.getRootLogger().setLevel(Level.OFF);
		}
	}

	private void cleanUp() {
		System.gc(); // Run Garbage Collection
	}

	private List<PerOpteryxPCMResult> export() {
		List<PerOpteryxPCMResult> results = PCMResultsProvider.getInstance().provide();
		return results;
	}

	private void execute() {
		try {
			DSELaunch launch = new MyDSELaunch(); // just uses reset debugger
			IProgressMonitor monitor = new NullProgressMonitor();

			DSEWorkflowConfigurationBuilder builder = new DSEWorkflowConfigurationBuilder();
			builder.init(configuration);
			DSEWorkflowConfiguration dseConfiguration = builder.build(launch);

			MyPerOpteryxJob job = new MyPerOpteryxJob(dseConfiguration, launch);
			job.setBlackboard(new MDSDBlackboard());

			job.execute(monitor);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	private void validate() {
		if (configuration == null) {
			throw new RuntimeException("init must be called first!");
		}
	}

	private void initialize() {
		initializePalladio();
		initializePerOpteryx();
		initializeExporter();
	}

	private void initializePalladio() {
		PalladioEclipseEnvironment.INSTANCE.setup(configuration.getPcmModelsConfig().getPathmapFolder());
	}

	private void initializePerOpteryx() {
		PerOpteryxEclipseEnvironment.INSTANCE.setup();
	}

	private void initializeExporter() {
		PCMFileExporter.getInstance().init(configuration.getExporterConfig().getPcmOutputFolder());
		PCMResultsProvider.getInstance().setBoundaryValue(configuration.getExporterConfig().getBoundaryValue());
		PCMResultsProvider.getInstance().setDirection(configuration.getExporterConfig().getOptimizationDirection());
		PCMResultsProvider.getInstance().setExportMode(configuration.getExporterConfig().getExportMode());
		PCMResultsProvider.getInstance().setAmount(configuration.getExporterConfig().getAmount());
	}

}
