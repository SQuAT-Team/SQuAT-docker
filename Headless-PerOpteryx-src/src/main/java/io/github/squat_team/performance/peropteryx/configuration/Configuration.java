package io.github.squat_team.performance.peropteryx.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Configuration extends AbstractConfig {
	private List<AbstractConfig> configs;

	private PcmInstanceConfig pcmInstanceConfig;
	private LQNSConfig lqnsConfig;
	private TacticsConfig tacticsConfig;
	private PcmModelsConfig pcmModelsConfig;
	private PerOpteryxConfig perOpteryxConfig;
	private TerminationCriteriaConfig terminationCriteriaConfig;
	
	private ExporterConfig exporterConfig;

	@Override
	public void initializeDefault() {
		configs  = new ArrayList<AbstractConfig>();
		
		pcmInstanceConfig = new PcmInstanceConfig();
		lqnsConfig = new LQNSConfig();
		tacticsConfig = new TacticsConfig();
		pcmModelsConfig = new PcmModelsConfig();
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
		for (AbstractConfig config : configs) {
			config.copyValuesTo(attr);
		}
		return attr;
	}

	@Override
	public boolean validate() {
		for (AbstractConfig config : configs) {
			if (!config.validate()) {
				return false;
			}
		}
		return true;
	}

	
	public PcmInstanceConfig getPcmInstanceConfig() {
		return pcmInstanceConfig;
	}

	public LQNSConfig getLqnsConfig() {
		return lqnsConfig;
	}

	public TacticsConfig getTacticsConfig() {
		return tacticsConfig;
	}

	public PcmModelsConfig getPcmModelsConfig() {
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
