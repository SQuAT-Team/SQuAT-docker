package io.github.squat_team.performance.peropteryx.configuration;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.palladiosimulator.analyzer.workflow.configurations.PCMWorkflowConfigurationBuilder;

import de.uka.ipd.sdq.dsexplore.launch.DSELaunch;
import de.uka.ipd.sdq.dsexplore.launch.DSEWorkflowConfiguration;
import de.uka.ipd.sdq.workflow.launchconfig.AbstractWorkflowConfigurationBuilder;
import io.github.squat_team.performance.peropteryx.environment.Pcm2LqnLaunchConfiguration;

public class DSEWorkflowConfigurationBuilder{
	
	private Configuration config;
	
	public void init(Configuration config){
		this.config = config;
	}
	
	public DSEWorkflowConfiguration build(DSELaunch launch) throws CoreException {
		validateConfig();
		
		ILaunchConfiguration iLaunchConfiguration = deriveLaunchConfiguration();
		DSEWorkflowConfiguration workflowConfiguration = deriveDSEWorkflowConfiguration(iLaunchConfiguration, "run", launch);
		workflowConfiguration.setDeleteTemporaryDataAfterAnalysis(true);

		return workflowConfiguration;
	}
	
	private void validateConfig(){
		if(this.config == null){
			throw new RuntimeException("init has to be called first!");
		}
		if(!this.config.validate()){
			throw new RuntimeException("Configurations not properly initialized!");
		}
	}
	
	private ILaunchConfiguration deriveLaunchConfiguration(){
		Map<String, Object> attr = new HashMap<>();
		config.copyValuesTo(attr);
		return new Pcm2LqnLaunchConfiguration(attr);
	}
	
	private DSEWorkflowConfiguration deriveDSEWorkflowConfiguration(ILaunchConfiguration configuration, String mode,
			DSELaunch launch) throws CoreException {

		AbstractWorkflowConfigurationBuilder builder;
		builder = new PCMWorkflowConfigurationBuilder(configuration, mode);

		DSEWorkflowConfiguration config = new DSEWorkflowConfiguration();
		builder.fillConfiguration(config);

		builder = new de.uka.ipd.sdq.dsexplore.launch.DSEWorkflowConfigurationBuilder(configuration, mode, launch);
		builder.fillConfiguration(config);

		config.setRawConfig(configuration);

		return config;
	}
	
}
