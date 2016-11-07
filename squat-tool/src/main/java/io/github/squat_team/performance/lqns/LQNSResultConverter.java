package io.github.squat_team.performance.lqns;

import io.github.squat_team.AbstractPCMBot;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.performance.PerformanceMetric;

public class LQNSResultConverter {

	public static PCMScenarioResult convert(PCMArchitectureInstance pcmInstance, LQNSResult lqnsResult,
			PerformanceMetric metric, AbstractPCMBot bot) {
		PCMScenarioResult scenarioResult = new PCMScenarioResult(bot);

		scenarioResult.setResult(createPCMResult(lqnsResult, metric));
		scenarioResult.setResultingArchitecture(pcmInstance);

		return scenarioResult;

	}

	private static PCMResult createPCMResult(LQNSResult lqnsResult, PerformanceMetric metric) {
		PCMResult pcmResult = new PCMResult(ResponseMeasureType.DECIMAL);

		switch (metric) {
		case MAX_CPU_UTILIZATION:
			pcmResult.setResponse(lqnsResult.getMaxUtilisation());
			break;
		case RESPONSE_TIME:
			pcmResult.setResponse(lqnsResult.getMeanValue());
			break;
		case THROUGHPUT:
			pcmResult.setResponse(lqnsResult.getThroughput());
			break;
		default:
			pcmResult.setResponse(lqnsResult.getMeanValue());
			break;
		}

		return pcmResult;
	}
}
