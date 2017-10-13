package io.github.squat_team.agentsUtils.transformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.json.JSONObject;

import io.github.squat_team.RestBot;
import io.github.squat_team.agentsUtils.BotManager;
import io.github.squat_team.agentsUtils.BotManager.BotType;
import io.github.squat_team.model.RestArchitecture;
import io.github.squat_team.model.RestScenarioResult;

// TODO: PA! Sequential vs Parallel calls
public class ArchitecturalTransformationsFactory {

	/** The initial architecture */
	private final RestArchitecture initialArchitecture;

	/** List of Scenario */
	private final Map<Integer, List<RestScenarioResult>> resultPerLevel;

	/**
	 * Create a new Transformation factory with the given initial architecture
	 *
	 * @param initialArchitecture
	 *            the initial architecture
	 */
	public ArchitecturalTransformationsFactory(RestArchitecture initialArchitecture) {
		this.initialArchitecture = initialArchitecture;
		this.resultPerLevel = new HashMap<>();
	}

	/**
	 * Get a list with all architectures that were created by any bot during the
	 * first n levels.
	 *
	 * @param n
	 *            the number of levels to contain in the list
	 * @return list with all {@link io.github.squat_team.model.RestArchitecture}
	 *         created within the first and n-th level
	 */
	public List<RestArchitecture> getArchitecturalTransformationsUntilLevel(int n) {
		if (resultPerLevel.get(n) == null)
			createArchitecturalTransformationsForLevel(n);

		// The results is the architectures created for this level plus the
		// architectures created for previous levels
		List<RestArchitecture> ret = new ArrayList<>();
		for (int i = 1; i <= n; i++) {
			resultPerLevel.get(i).stream().map(RestScenarioResult::getArchitecture).forEach(ret::add);
		}
		return ret;
	}

	/**
	 * @return the initial architecture as {@link JSONObject}
	 */
	public RestArchitecture getInitialArchitecture() {
		return this.initialArchitecture;
	}

	/**
	 * Search for alternatives using the Modifiability Bot
	 *
	 * @param arch
	 *            the architecture to perform the search on
	 * @param results
	 *            the list to add the results to
	 */
	private void performModBot(RestArchitecture arch, List<RestScenarioResult> results) {
		RestBot modBot = BotManager.getInstance().getBots(BotType.MODIFIABILITY).get(0);
		if (modBot != null)
			modBot.searchForAlternatives(arch).forEach(results::add);
	}

	/**
	 * Search for alternatives using the Performance Bot
	 *
	 * @param arch
	 *            the architecture to perform the search on
	 * @param results
	 *            the list to add the results to
	 */
	private void performPerfBot(RestArchitecture arch, List<RestScenarioResult> results) {
		BotManager.getInstance().getBots(BotType.PERFORMANCE).stream().map(b -> b.searchForAlternatives(arch))
				.forEach(results::addAll);
	}

	/**
	 * Perform an analysis on the previously generated results given in the list
	 *
	 * @param results
	 *            the list to add the results to
	 * @param previousLevelResults
	 *            the list of results generated in the previous level
	 */
	private void createFromPreviousResults(List<RestScenarioResult> results,
			List<RestScenarioResult> previousLevelResults) {
		for (RestScenarioResult previousResult : previousLevelResults) {
			RestArchitecture arch = previousResult.getArchitecture();
			if (previousResult.getOrigininatingBotType() == BotType.PERFORMANCE) {
				this.performPerfBot(arch, results);
			} else {
				this.performModBot(arch, results);
			}
		}
	}

	/**
	 * Search for the given level based in the previous results. Note, if the
	 * previous level (for level index bigger than 1) does not exist nothing will be
	 * done
	 *
	 * @param level
	 *            the level to search for
	 */
	private void createArchitecturalTransformationsForLevel(int level) {
		// If it already exists, exit doing nothing
		if (this.resultPerLevel.containsKey(level))
			return;

		// Create the list and add to map
		final List<RestScenarioResult> results = new ArrayList<>();
		this.resultPerLevel.put(level, results);

		// If we need to create the first level, we need to start with the
		// initial architecture
		if (level == 1) {
			this.performPerfBot(this.initialArchitecture, results);
			this.performModBot(this.initialArchitecture, results);
		} else if (this.resultPerLevel.containsKey(level - 1)) {
			List<RestScenarioResult> previousLevelResults = this.resultPerLevel.get(level - 1);
			this.createFromPreviousResults(results, previousLevelResults);
		}
	}

	/**
	 * Returns the architectures of alternatives from a specific level.
	 * 
	 * @param level
	 *            the (depth) level of the optimization the results come from.
	 * @return all architecture alternatives detected at the specified level.
	 */
	public List<RestArchitecture> transformationsForLevel(int level) {
		List<RestArchitecture> results = new ArrayList<>();
		for (RestScenarioResult currentResult : resultPerLevel.get(level)) {
			results.add(currentResult.getArchitecture());
		}
		return results;
	}
}
