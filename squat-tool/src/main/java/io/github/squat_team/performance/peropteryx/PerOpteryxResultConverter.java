package io.github.squat_team.performance.peropteryx;

import java.util.ArrayList;
import java.util.List;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import io.github.squat_team.AbstractPCMBot;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMResult;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.model.ResponseMeasureType;
import io.github.squat_team.performance.peropteryx.export.PerOpteryxPCMResult;
import io.github.squat_team.util.SQuATHelper;

public class PerOpteryxResultConverter {

	public static List<PCMScenarioResult> convert(List<PerOpteryxPCMResult> peropteryxResults, AbstractPCMBot bot) {
		List<PCMScenarioResult> results = new ArrayList<PCMScenarioResult>();
		for (PerOpteryxPCMResult peropteryxResult : peropteryxResults) {
			results.add(convert(peropteryxResult, bot));
		}
		return results;
	}

	public static PCMScenarioResult convert(PerOpteryxPCMResult peropteryxResult, AbstractPCMBot bot) {
		PCMScenarioResult scenarioResult = new PCMScenarioResult(bot);
		scenarioResult.setResult(createPCMResult(peropteryxResult));
		scenarioResult.setResultingArchitecture(createArchitectureInstance(peropteryxResult));
		return scenarioResult;
	}

	private static PCMResult createPCMResult(PerOpteryxPCMResult peropteryxResult) {
		PCMResult pcmResult = new PCMResult(ResponseMeasureType.DECIMAL);
		pcmResult.setResponse(peropteryxResult.getValue());
		return pcmResult;
	}

	private static PCMArchitectureInstance createArchitectureInstance(PerOpteryxPCMResult peropteryxResult) {
		Allocation allocation = SQuATHelper.loadAllocationModel(formatePath(peropteryxResult.getAllocationPath()));
		Repository repository = SQuATHelper.loadRepositoryModel(formatePath(peropteryxResult.getRepositoryPath()));
		org.palladiosimulator.pcm.system.System system = SQuATHelper.loadSystemModel(formatePath(peropteryxResult.getSystemPath()));
		ResourceEnvironment resourceEnvironment = SQuATHelper
				.loadResourceEnvironmentModel(formatePath(peropteryxResult.getResourceEnvironmentPath()));
		UsageModel usageModel = SQuATHelper.loadUsageModel(formatePath(peropteryxResult.getUsagemodelPath()));

		PCMArchitectureInstance pcmArchitectureInstance = new PCMArchitectureInstance("PerOpteryx Candidate",
				repository, system, allocation, resourceEnvironment, usageModel);
		return pcmArchitectureInstance;
	}
	
	private static String formatePath(String path){
		String newPath;
		if(!path.contains("file:/")){
			newPath = "file:/"+path;
		}else{
			newPath = path;
		}
		return newPath;
	}
}
