package io.github.squat_team.performance.peropteryx.configuration;

import java.util.Map;

import de.uka.ipd.sdq.dsexplore.launch.DSEConstantsContainer;

public class TacticsConfig extends AbstractConfig{

	private Double tacticsProbability = new Double(0.6);
	
	private Boolean activateReallocation = false;
	private Boolean activateProcessingRate = false;
	private Boolean activateServerConsolidation = false;
	private Boolean activateServerExpansion = false;
	private Boolean activateLinkReallocation = false;
	private Boolean activateAntipatterns = false;
	
	@Override
	public void initializeDefault() {
		// Reallocation Tactic
		defaultAttr.put(DSEConstantsContainer.REALLOCATION_UTILISATION_DIFFERENCE, "0.5");
		defaultAttr.put(DSEConstantsContainer.REALLOCATION_WEIGHT, "1.0");

		// Processing Rate Tactic
		defaultAttr.put(DSEConstantsContainer.PROCESSING_RATE_DECREASE_FACTOR, "0.1");
		defaultAttr.put(DSEConstantsContainer.PROCESSING_RATE_INCREASE_FACTOR, "0.1");
		defaultAttr.put(DSEConstantsContainer.PROCESSING_RATE_THRESHOLD_HIGH_UTILISATION, "0.8");
		defaultAttr.put(DSEConstantsContainer.PROCESSING_RATE_THRESHOLD_LOW_UTILISATION, "0.2");
		defaultAttr.put(DSEConstantsContainer.PROCESSING_RATE_WEIGHT, "0.1");

		// Server Consolidation Tactic
		defaultAttr.put(DSEConstantsContainer.SERVER_CONSOLIDATION_THRESHOLD_LOW_UTILISATION, "0.3");
		defaultAttr.put(DSEConstantsContainer.SERVER_CONSOLIDATION_WEIGHT, "0.5");

		// Server Expansion Tactic
		defaultAttr.put(DSEConstantsContainer.SERVER_EXPANSION_MAX_NUMBER_OF_REPLACEMENTS, "1");
		defaultAttr.put(DSEConstantsContainer.SERVER_EXPANSION_THRESHOLD_HIGH_UTILISATION, "0.7");
		defaultAttr.put(DSEConstantsContainer.SERVER_EXPANSION_WEIGHT, "0.5");
	}

	@Override
	public Map<String, Object> copyValuesTo(Map<String, Object> attr) {
		attr.putAll(defaultAttr);
		
		attr.put(DSEConstantsContainer.TACTICS_PROBABILITY, this.tacticsProbability.toString());
		attr.put(DSEConstantsContainer.USE_REALLOCATION, this.activateReallocation);
		attr.put(DSEConstantsContainer.USE_PROCESSING_RATE, this.activateProcessingRate);
		attr.put(DSEConstantsContainer.USE_SERVER_CONSOLIDATION, this.activateServerConsolidation);
		attr.put(DSEConstantsContainer.USE_SERVER_EXPANSION, this.activateServerExpansion);
		attr.put(DSEConstantsContainer.USE_LINK_REALLOCATION, this.activateLinkReallocation);
		attr.put(DSEConstantsContainer.USE_ANTIPATTERNS, this.activateAntipatterns);
		
		return defaultAttr;
	}

	@Override
	public boolean validate() {
		return validatePercentage(tacticsProbability);
	}

	public Double getTacticsProbability() {
		return tacticsProbability;
	}

	public void setTacticsProbability(Double tacticsProbability) {
		this.tacticsProbability = tacticsProbability;
	}

	public Boolean getActivateReallocation() {
		return activateReallocation;
	}

	public void setActivateReallocation(Boolean activateReallocation) {
		this.activateReallocation = activateReallocation;
	}

	public Boolean getActivateProcessingRate() {
		return activateProcessingRate;
	}

	public void setActivateProcessingRate(Boolean activateProcessingRate) {
		this.activateProcessingRate = activateProcessingRate;
	}

	public Boolean getActivateServerConsolidation() {
		return activateServerConsolidation;
	}

	public void setActivateServerConsolidation(Boolean activateServerConsolidation) {
		this.activateServerConsolidation = activateServerConsolidation;
	}

	public Boolean getActivateServerExpansion() {
		return activateServerExpansion;
	}

	public void setActivateServerExpansion(Boolean activateServerExpansion) {
		this.activateServerExpansion = activateServerExpansion;
	}

	public Boolean getActivateLinkReallocation() {
		return activateLinkReallocation;
	}

	public void setActivateLinkReallocation(Boolean activateLinkReallocation) {
		this.activateLinkReallocation = activateLinkReallocation;
	}

	public Boolean getActivateAntipatterns() {
		return activateAntipatterns;
	}

	public void setActivateAntipatterns(Boolean activateAntipatterns) {
		this.activateAntipatterns = activateAntipatterns;
	}

	public void useTactics(Boolean allTactics){
		this.activateLinkReallocation = allTactics;
		this.activateProcessingRate = allTactics;
		this.activateReallocation = allTactics;
		this.activateServerConsolidation = allTactics;
		this.activateServerExpansion = allTactics;
		// Caused the analysis to fail sometimes
		//this.activateAntipatterns = allTactics;
	}
}
