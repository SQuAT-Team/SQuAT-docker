package io.github.squat_team.utility;

import io.github.squat_team.AbstractPCMBot;
import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;

public class PCMScenarioSatisfaction {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int compute(PCMScenarioResult scenarioResult) throws Exception {
		AbstractPCMBot originatingBot = scenarioResult.getOriginatingBot();
		PCMResult result = scenarioResult.getResult();

		if(result == null || scenarioResult.getResultingArchitecture() == null)
			throw new Exception("Results haven't been calculated yet");
		
		PCMScenario scenario = originatingBot.getScenario();
		PCMResult expectedResult = scenario.getExpectedResult();
		
		ResponseMeasureType scenarioResponseMeasureType = expectedResult.getResponseMeasureType();
		ResponseMeasureType resultResponseMeasureType = result.getResponseMeasureType();
		
		if(scenarioResponseMeasureType.equals(resultResponseMeasureType)) {
			Comparable expectedResponse = expectedResult.getResponse();
			Comparable response = result.getResponse();
			if(scenario.getType().equals(OptimizationType.MINIMIZATION))
				return expectedResponse.compareTo(response);
			else 
			//if(scenario.getType().equals(OptimizationType.MAXIMIZATION)) {
				return response.compareTo(expectedResponse);
		}
		else
			throw new Exception("Incompatible response measure types");
	}
}
