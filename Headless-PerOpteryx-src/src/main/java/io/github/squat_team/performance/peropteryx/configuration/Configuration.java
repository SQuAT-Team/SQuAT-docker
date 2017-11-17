package io.github.squat_team.performance.peropteryx.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Configuration extends AbstractConfiguration {
	private List<AbstractConfiguration> configs;

	private PCMInstanceConfig pcmInstanceConfig;
	private LQNSConfig lqnsConfig;
	private TacticsConfig tacticsConfig;
	private PCMModelsConfig pcmModelsConfig;
	private PerOpteryxConfig perOpteryxConfig;
	private TerminationCriteriaConfig terminationCriteriaConfig;
	
	private ExporterConfig exporterConfig;

	@Override
	public void initializeDefault() {
		configs  = new ArrayList<AbstractConfiguration>();
		
		pcmInstanceConfig = new PCMInstanceConfig();
		lqnsConfig = new LQNSConfig();
		tacticsConfig = new TacticsConfig();
		pcmModelsConfig = new PCMModelsConfig();
		perOpteryxConfig = new PerOpteryxConfig();
		exporterConfig = new ExporterConfig();
		terminationCriteriaConfig = new TerminationCriteriaConfig();

		configs.add(pcmInstanceConfig);
		configs.add(lqnsConfig);
		configs.add(tacticsConfig);
		configs.add(pcmModelsConfig);
		configs.add(perOpteryxConfig);
		configs.add(exporterConfig);
		configs.add(terminationCriteriaConfig);
	}

	@Override
	public Map<String, Object> copyValuesTo(Map<String, Object> attr) {
		for (AbstractConfiguration config : configs) {
			config.copyValuesTo(attr);
		}
		return attr;
	}

	@Override
	public boolean validate() {
		for (AbstractConfiguration config : configs) {
			if (!config.validate()) {
				return false;
			}
		}
		return true;
	}

	
	public PCMInstanceConfig getPcmInstanceConfig() {
		return pcmInstanceConfig;
	}

	public LQNSConfig getLqnsConfig() {
		return lqnsConfig;
	}

	public TacticsConfig getTacticsConfig() {
		return tacticsConfig;
	}

	public PCMModelsConfig getPcmModelsConfig() {
		return pcmModelsConfig;
	}

	public PerOpteryxConfig getPerOpteryxConfig() {
		return perOpteryxConfig;
	}

	public ExporterConfig getExporterConfig() {
		return exporterConfig;
	}

	public TerminationCriteriaConfig getTerminationCriteriaConfig() {
		return terminationCriteriaConfig;
	}

	public void setTerminationCriteriaConfig(TerminationCriteriaConfig terminationCriteriaConfig) {
		this.terminationCriteriaConfig = terminationCriteriaConfig;
	}
	
}
