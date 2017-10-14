package test;

import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.RestArchitecture;
import io.github.squat_team.performance.peropteryx.PerOpteryxPCMBot;

public class ExecutionContext {

	/** The bot */
	private final PerOpteryxPCMBot bot;

	/** The architecture to analyze */
	private final PCMArchitectureInstance architectureInstance;

	/** */
	private final RestArchitecture restArchitecture;

	/**
	 *
	 * @param bot
	 * @param architectureInstance
	 */
	public ExecutionContext(PerOpteryxPCMBot bot, PCMArchitectureInstance architectureInstance,
			RestArchitecture restArchitecture) {
		this.bot = bot;
		this.architectureInstance = architectureInstance;
		this.restArchitecture = restArchitecture;
	}

	/**
	 * @return the scenario
	 */
	public PerOpteryxPCMBot getBot() {
		return this.bot;
	}

	/**
	 * @return the architecture instance
	 */
	public PCMArchitectureInstance getArchitectureInstance() {
		return this.architectureInstance;
	}

	/**
	 * @return the rest architecture
	 */
	public RestArchitecture getRestArchitecture() {
		return this.restArchitecture;
	}
}
