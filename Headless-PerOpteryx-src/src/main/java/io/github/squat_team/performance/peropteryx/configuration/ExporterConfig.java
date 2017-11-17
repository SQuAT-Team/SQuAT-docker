package io.github.squat_team.performance.peropteryx.configuration;

import java.util.Map;

import io.github.squat_team.performance.peropteryx.export.ExportMode;
import io.github.squat_team.performance.peropteryx.export.OptimizationDirection;

public class ExporterConfig extends AbstractConfiguration{

	private String pcmOutputFolder = "";
	private double boundaryValue;
	private int amount;
	private ExportMode extractionMode;
	private OptimizationDirection optimizationDirection;

	public String getPcmOutputFolder() {
		return pcmOutputFolder;
	}

	public void setPcmOutputFolder(String pcmOutputFolder) {
		this.pcmOutputFolder = pcmOutputFolder;
	}

	public double getBoundaryValue() {
		return boundaryValue;
	}

	public void setBoundaryValue(double boundaryValue) {
		this.boundaryValue = boundaryValue;
	}

	public ExportMode getExportMode() {
		return extractionMode;
	}

	public void setExportMode(ExportMode extractionMode) {
		this.extractionMode = extractionMode;
	}

	public OptimizationDirection getOptimizationDirection() {
		return optimizationDirection;
	}

	public void setOptimizationDirection(OptimizationDirection optimizationDirection) {
		this.optimizationDirection = optimizationDirection;
	}

	@Override
	protected void initializeDefault() {
		boundaryValue = 0.0;
		extractionMode = ExportMode.PARETO;
		optimizationDirection = OptimizationDirection.MINIMIZE;
	}

	@Override
	protected Map<String, Object> copyValuesTo(Map<String, Object> attr) {
		// do nothing
		return attr;
	}

	@Override
	protected boolean validate() {
		return validatePath(pcmOutputFolder);
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
