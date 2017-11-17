package io.github.squat_team.performance.peropteryx.overwrite;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import org.apache.commons.logging.impl.LogFactoryImpl;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.palladiosimulator.analyzer.workflow.configurations.PCMWorkflowConfigurationBuilder;

import de.uka.ipd.sdq.dsexplore.launch.DSEConstantsContainer;
import de.uka.ipd.sdq.dsexplore.launch.DSELaunch;
import de.uka.ipd.sdq.dsexplore.launch.DSEWorkflowConfiguration;
import de.uka.ipd.sdq.dsexplore.launch.DSEWorkflowConfigurationBuilder;
import de.uka.ipd.sdq.dsexplore.launch.PerOpteryxJob;
import de.uka.ipd.sdq.workflow.Workflow;
import de.uka.ipd.sdq.workflow.configuration.InvalidWorkflowJobConfigurationException;
import de.uka.ipd.sdq.workflow.jobs.IJob;
import de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowBasedLaunchConfigurationDelegate;
import de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowConfigurationBuilder;
import de.uka.ipd.sdq.workflow.logging.console.LoggerAppenderStruct;
import de.uka.ipd.sdq.workflow.ui.WorkflowProcess;

public class MyDSELaunch extends DSELaunch{


	WorkflowProcess myWorkflowProcess;
	
	/**
	 * Logger of this class 
	 */
	private static Logger logger = Logger.getLogger(DSELaunch.class);
	

	private ILaunchConfiguration originalConfiguration;
	private ILaunch originalLaunch;


	/**
	 * Test for starting multiple simulations.
	 * @param configuration
	 * @param mode
	 * @param launch
	 * @param monitor
	 */
	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
		this.originalConfiguration = configuration;
		this.originalLaunch = launch;
		
		int iterations = getNumberOfDSEIterations(configuration);
		
		logger.warn("Will start "+iterations+" analysis runs. Only first one may be visible on the console.");
		for (int i = 0; i < iterations; i++) {
			try {
				mylaunch(configuration, mode, launch, monitor);
			} catch (InvalidWorkflowJobConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*private void mylaunch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException, JobFailedException, UserCanceledException{
		DSEWorkflowConfiguration dseconfig = this.deriveConfiguration(configuration, mode);
		IJob job = this.createWorkflowJob(dseconfig, launch);
		job.execute(monitor);
	}*/
	
	private void mylaunch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException, InvalidWorkflowJobConfigurationException {

		// Setup a new classloader to allow reconfiguration of apache commons logging
		ClassLoader oldClassLoader = configureNewClassloader();
		// Reconfigure apache commons logging to use Log4J as backend logger
		System.setProperty(LogFactoryImpl.LOG_PROPERTY, "org.apache.commons.logging.impl.Log4JLogger");

		logger = Logger.getLogger(AbstractWorkflowBasedLaunchConfigurationDelegate.class);

		// Add a process to this launch, needed for Eclipse UI updates
		//this.myProcess = getProcess(launch);
		// Configure logging output to the Eclipse console
		//setupLogging(configuration.getAttribute(VERBOSE_LOGGING, false) ? Level.DEBUG : Level.INFO );
		//launch.addProcess(getProcess());
		
		try {
			logger.info("Create workflow configuration");
			DSEWorkflowConfiguration workflowConfiguration = 
				deriveConfiguration(configuration, mode);
		
			logger.info("Validating workflow configuration");
			workflowConfiguration.validateAndFreeze();
	
			logger.info("Creating workflow engine");
			Workflow workflow = createWorkflow(workflowConfiguration,
					monitor, launch);
			
			logger.info("Executing workflow");
			workflow.run();
		} finally {
			// Reset classloader to original value
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
		
		//for(LoggerAppenderStruct l : this.myLogger) {
		//	l.getLogger().removeAppender(l.getAppender());
		//}
		
		// Singnal execution terminatation to Eclipse to update UI 
		//launch.getProcesses()[0].terminate();
	}

	/**
	 * Create a new classloader to be used by this thread. Return the old
	 * classloader for later resets
	 * @return Old classloader
	 */
	private ClassLoader configureNewClassloader() {
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		URLClassLoader cl = new URLClassLoader(new URL[]{},oldClassLoader);
		Thread.currentThread().setContextClassLoader(cl);
		return oldClassLoader;
	}
	
	private int getNumberOfDSEIterations(ILaunchConfiguration configuration) {
		String stringValue;
		try {
			stringValue = configuration.getAttribute(DSEConstantsContainer.DSE_ITERATIONS, "0");
			if (!stringValue.equals("")){
				int i = Integer.parseInt(stringValue);
				return i;
			}
		} catch (Exception e) { /* just use 1 */ }
		return 1;
	}

	/**
	 * Copied from AbstractWorkflowBasedLaunchConfigurationDelegate
	 * Setup logger for the workflow run. May be overridden by clients to configure further logger
	 * for other namespaces than de.uka.ipd.sdq.workflow. Use protected method setupLogger to configure
	 * additional loggers
	 * @param logLevel The apache log4j log level requested by the user as log level
	 * @throws CoreException 
	 */
	@Override
	protected ArrayList<LoggerAppenderStruct> setupLogging(Level logLevel) throws CoreException {
		ArrayList<LoggerAppenderStruct> loggerList = super.setupLogging(logLevel);

		// Setup SDQ workflow engine logging
		loggerList.add(setupLogger("de.uka.ipd.sdq.dsexplore", logLevel, Level.DEBUG == logLevel ? DETAILED_LOG_PATTERN : SHORT_LOG_PATTERN));
		loggerList.add(setupLogger("de.uka.ipd.sdq.reliability.solver", logLevel, Level.DEBUG == logLevel ? DETAILED_LOG_PATTERN : SHORT_LOG_PATTERN));
		loggerList.add(setupLogger("de.uka.ipd.sdq.pcmsolver", logLevel, Level.DEBUG == logLevel ? DETAILED_LOG_PATTERN : SHORT_LOG_PATTERN));
		loggerList.add(setupLogger("de.uka.ipd.sdq.simucomframework.variables", logLevel, Level.DEBUG == logLevel ? DETAILED_LOG_PATTERN : SHORT_LOG_PATTERN));
		loggerList.add(setupLogger("de.uka.ipd.sdq.stoex.analyser", logLevel, Level.DEBUG == logLevel ? DETAILED_LOG_PATTERN : SHORT_LOG_PATTERN));
		
		return loggerList;
	}



	@Override
	protected IJob createWorkflowJob(
			DSEWorkflowConfiguration config, ILaunch launch)
			throws CoreException {
		return new PerOpteryxJob(config, this);
	}



	@Override
	protected DSEWorkflowConfiguration deriveConfiguration(
			ILaunchConfiguration configuration, String mode)
			throws CoreException {
		
		AbstractWorkflowConfigurationBuilder builder;
		builder = new PCMWorkflowConfigurationBuilder(configuration, mode);
		
		DSEWorkflowConfiguration config = new DSEWorkflowConfiguration();
		builder.fillConfiguration(config);
		
		builder = new DSEWorkflowConfigurationBuilder(configuration, mode, this);
		builder.fillConfiguration(config);
		
		config.setRawConfig(configuration);
		
		return config;
	}
	
	/**
	 * FIXME: This is just a workaround to fix the logging. 
	 * There is a problem when the workflows for the Analyses are started. 
	 * Afterwards, the logging is broken. Thus, I reset it here.
	 * Problem: I do not know how much overhead this creates, maybe it is the cause for crashing eclipse after 1600 candidates with LQNS.  
	 * @throws CoreException
	 */
	public void resetLoggers() throws CoreException{
		//Logger.getRootLogger().removeAllAppenders();
		//Logger.getLogger(loggerName);
//		for (LoggerAppenderStruct logger : this.myLoggerList) {
//			this.myWorkflowProcess.addAppender(logger.getAppender());
//		}
	}

	public ILaunch getOriginalLaunch() {
		return this.originalLaunch;
	}
	
	public ILaunchConfiguration getOriginalConfiguration() {
		return originalConfiguration;
	}
	
	/** 
	 * Get the Eclipse process used by the workflow engine. When called first, 
	 * instatiate new process. Later return the same. 
	 * 
	 * @param launch The ILaunch passed to this launch by Eclipse
	 * @return The process used to execute this launch
	 */
	@Override
	protected WorkflowProcess getProcess(ILaunch launch) {
		if (this.myWorkflowProcess == null){
			this.myWorkflowProcess = new WorkflowProcess(launch);
		}
		return this.myWorkflowProcess; 
	}
	
	
}
