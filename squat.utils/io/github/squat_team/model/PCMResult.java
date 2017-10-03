package io.github.squat_team.model;

@SuppressWarnings("rawtypes")
public class PCMResult {
	private Comparable response;
	private ResponseMeasureType responseMeasureType;
	
	public PCMResult(ResponseMeasureType responseMeasureType) {
		super();
		this.responseMeasureType = responseMeasureType;
	}

	public Comparable getResponse() {
		return response;
	}

	public void setResponse(Comparable response) {
		this.response = response;
	}

	public ResponseMeasureType getResponseMeasureType() {
		return responseMeasureType;
	}

	public void setResponseMeasureType(ResponseMeasureType responseMeasureType) {
		this.responseMeasureType = responseMeasureType;
	}
}
