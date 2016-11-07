package io.github.squat_team.performance.peropteryx.configuration;

import java.util.Map;

import de.uka.ipd.sdq.dsexplore.launch.DSEConstantsContainer;

public class TerminationCriteriaConfig extends AbstractConfig{
	private Boolean activateTerminationCriteria = false;
	private Boolean activateInsignificantFrontChange = false;
	private Integer insignificantFrontChangeGenerationNumber = 1;
	private Integer insignificantFrontChangeImprovementPercentage = 1;

	
	@Override
	protected void initializeDefault() {
		// do nothing
	}

	@Override
	protected Map<String, Object> copyValuesTo(Map<String, Object> attr) {
		attr.put(DSEConstantsContainer.TC_GENERAL_USE_TERMINATION_CRITERIA, activateTerminationCriteria);
		attr.put(DSEConstantsContainer.TC_INSIGNIFICANT_FRONT_CHANGE_ACTIVATE, activateInsignificantFrontChange);
		attr.put(DSEConstantsContainer.TC_INSIGNIFICANT_FRONT_CHANGE_GENERATION_X, insignificantFrontChangeGenerationNumber);
		attr.put(DSEConstantsContainer.TC_INSIGNIFICANT_FRONT_CHANGE_IMPROVEMENT, insignificantFrontChangeImprovementPercentage);		
		return attr;
	}

	@Override
	protected boolean validate() {
		return true;
	}

	public Boolean getActivateTerminationCriteria() {
		return activateTerminationCriteria;
	}

	public void setActivateTerminationCriteria(Boolean activateTerminationCriteria) {
		this.activateTerminationCriteria = activateTerminationCriteria;
	}

	public Boolean getActivateInsignificantFrontChange() {
		return activateInsignificantFrontChange;
	}

	public void setActivateInsignificantFrontChange(Boolean activateInsignificantFrontChange) {
		this.activateInsignificantFrontChange = activateInsignificantFrontChange;
	}

	public Integer getInsignificantFrontChangeGenerationNumber() {
		return insignificantFrontChangeGenerationNumber;
	}

	public void setInsignificantFrontChangeGenerationNumber(Integer insignificantFrontChangeGenerationNumber) {
		this.insignificantFrontChangeGenerationNumber = insignificantFrontChangeGenerationNumber;
	}

	public Integer getInsignificantFrontChangeImprovementPercentage() {
		return insignificantFrontChangeImprovementPercentage;
	}

	public void setInsignificantFrontChangeImprovementPercentage(
			Integer insignificantFrontChangeImprovementPercentage) {
		this.insignificantFrontChangeImprovementPercentage = insignificantFrontChangeImprovementPercentage;
	}

}
