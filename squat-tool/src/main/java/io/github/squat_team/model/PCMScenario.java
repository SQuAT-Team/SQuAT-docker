package io.github.squat_team.model;

public abstract class PCMScenario {
	//To compare we first need to know if we are going to minimize or maximize something (e.g., minimize number of changes, maximize throughput)
	protected OptimizationType type;
	//This contains the expected response value and measure...this could or should be more complex, let us talk about it later
	//A negotiation would imply to make concessions in the response
	protected PCMResult expectedResult;
	
	private String source, stimulus, environment, artifact, response, responseMeasure;

	public PCMScenario(OptimizationType type) {
		super();
		this.type = type;
	}

	public OptimizationType getType() {
		return type;
	}

	public void setType(OptimizationType type) {
		this.type = type;
	}
	
	public PCMResult getExpectedResult() {
		return expectedResult;
	}
	
	public void setExpectedResponse(PCMResult expectedResult) {
		this.expectedResult = expectedResult;
	}
}