package io.github.squat_team.performance.peropteryx.configuration;

import java.util.Map;

import de.uka.ipd.sdq.dsexplore.launch.DSEConstantsContainer;
import io.github.squat_team.performance.peropteryx.start.HeadlessPerOpteryxConstants;

public class PerOpteryxConfig extends AbstractConfig {

	public enum Mode {
		OPTIMIZE, DESIGN_DECISIONS, DESIGN_DECISIONS_AND_OPTIMIZE;
	}

	private Mode mode = Mode.DESIGN_DECISIONS_AND_OPTIMIZE;

	private String designDecisionFile = "";
	private String qmlDefinitionFile = "";
	private Integer maxIterations = new Integer(1);
	private Integer generationSize = new Integer(1);
	private Double crossoverRate = new Double(0.5);

	@Override
	public void initializeDefault() {		
		defaultAttr.put(DSEConstantsContainer.SEARCH_METHOD, DSEConstantsContainer.SEARCH_EVOLUTIONARY);
		defaultAttr.put(DSEConstantsContainer.DSE_ITERATIONS, "1");

		defaultAttr.put(
				DSEConstantsContainer.getAnalysisMethod(DSEConstantsContainer.QualityAttribute.PERFORMANCE_QUALITY),
				HeadlessPerOpteryxConstants.LQN_SOLVER_METHOD_NAME);
		defaultAttr.put(DSEConstantsContainer.getAnalysisMethod(DSEConstantsContainer.QualityAttribute.COST_QUALITY),
				DSEConstantsContainer.NONE);
		defaultAttr.put(
				DSEConstantsContainer.getAnalysisMethod(DSEConstantsContainer.QualityAttribute.RELIABILITY_QUALITY),
				DSEConstantsContainer.NONE);
		defaultAttr.put(
				DSEConstantsContainer.getAnalysisMethod(DSEConstantsContainer.QualityAttribute.SECURITY_QUALITY),
				DSEConstantsContainer.NONE);
		defaultAttr.put(DSEConstantsContainer.getAnalysisMethod(DSEConstantsContainer.QualityAttribute.NQR_QUALITY),
				DSEConstantsContainer.NONE);

		// optional .csv paths
		defaultAttr.put(DSEConstantsContainer.PREDEFINED_INSTANCES, "");
		defaultAttr.put(DSEConstantsContainer.CACHE_INSTANCES, "");
		defaultAttr.put(DSEConstantsContainer.ALL_CANDIDATES, "");
		defaultAttr.put(DSEConstantsContainer.ARCHIVE_CANDIDATES, "");

		// starting population heuristic
		defaultAttr.put(DSEConstantsContainer.USE_STARTING_POPULATION_HEURISTIC, false);
		defaultAttr.put(DSEConstantsContainer.MIN_NUMBER_RESOURCE_CONTAINERS, "2");
		defaultAttr.put(DSEConstantsContainer.MAX_NUMBER_RESOURCE_CONTAINERS, "9");
		defaultAttr.put(DSEConstantsContainer.NUMBER_OF_CANDIDATES_PER_ALLOCATION_LEVEL, "10");

		defaultAttr.put(DSEConstantsContainer.TC_GENERAL_USE_TERMINATION_CRITERIA, false); // TODO!
		defaultAttr.put(DSEConstantsContainer.STOP_ON_INITIAL_FAILURE, false);
		defaultAttr.put(DSEConstantsContainer.STORE_RESULTS_AS_EMF, false);
		defaultAttr.put(DSEConstantsContainer.STORE_RESULTS_AS_CSV, true);

		defaultAttr.put("de.uka.ipd.sdq.workflowengine.debuglevel", 2);
	}

	@Override
	public Map<String, Object> copyValuesTo(Map<String, Object> attr) {
		attr.putAll(defaultAttr);
		attr.put(DSEConstantsContainer.MAX_ITERATIONS, maxIterations.toString());
		attr.put(DSEConstantsContainer.INDIVIDUALS_PER_GENERATION, generationSize.toString());
		attr.put(DSEConstantsContainer.CROSSOVER_RATE, crossoverRate.toString());
		attr.put(DSEConstantsContainer.QML_DEFINITION_FILE, qmlDefinitionFile);
		attr.put(DSEConstantsContainer.DESIGN_DECISION_FILE, designDecisionFile);
		copyModeValuesTo(attr);
		return attr;
	}

	private void copyModeValuesTo(Map<String, Object> attr) {
		switch (mode) {
		case DESIGN_DECISIONS:
			attr.put(DSEConstantsContainer.OPTIMISATION_ONLY, false);
			attr.put(DSEConstantsContainer.DESIGN_DECISIONS_ONLY, true);
			break;
		case DESIGN_DECISIONS_AND_OPTIMIZE:
			attr.put(DSEConstantsContainer.OPTIMISATION_ONLY, false);
			attr.put(DSEConstantsContainer.DESIGN_DECISIONS_ONLY, false);
			break;
		case OPTIMIZE:
			attr.put(DSEConstantsContainer.OPTIMISATION_ONLY, true);
			attr.put(DSEConstantsContainer.DESIGN_DECISIONS_ONLY, false);
			break;
		default:
			attr.put(DSEConstantsContainer.OPTIMISATION_ONLY, false);
			attr.put(DSEConstantsContainer.DESIGN_DECISIONS_ONLY, false);
			break;
		}
	}

	@Override
	public boolean validate() {
		boolean designdecisionCheck = true;
		if (mode == Mode.OPTIMIZE) {
			designdecisionCheck = validatePath(designDecisionFile);
		}
		return validateInteger(maxIterations) && validateInteger(generationSize) && validatePercentage(crossoverRate)
				&& validatePath(qmlDefinitionFile) && designdecisionCheck;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public int getGenerationSize() {
		return generationSize;
	}

	public void setGenerationSize(int generationSize) {
		this.generationSize = generationSize;
	}

	public Double getCrossoverRate() {
		return crossoverRate;
	}

	public void setCrossoverRate(Double crossoverRate) {
		this.crossoverRate = crossoverRate;
	}

	public String getDesignDecisionFile() {
		return designDecisionFile;
	}

	public void setDesignDecisionFile(String designDecisionFile) {
		this.designDecisionFile = designDecisionFile;
	}

	public String getQmlDefinitionFile() {
		return qmlDefinitionFile;
	}

	public void setQmlDefinitionFile(String qmlDefinitionFile) {
		this.qmlDefinitionFile = qmlDefinitionFile;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

}
