package io.github.squat_team.agentsUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.github.squat_team.RestBot;
import io.github.squat_team.model.RestArchitecture;

public interface ILoadHelper {
	/**
	 * Creates a {@link SillyBot} for each active {@link RestBot} and analyzes the alternatives.
	 * 
	 * @param initialArchitecture
	 *            the architecture at the beginning.
	 * @return the created bots.
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public List<SillyBot> generateSillyBotsAndAnalyze(List<RestArchitecture> architecturalAlternatives,
			RestArchitecture initialArchitecture) throws InterruptedException, ExecutionException;
}
