package io.github.squat_team.agentsUtils;

import org.json.JSONStringer;

import io.github.squat_team.RestBot;
import io.github.squat_team.agentsUtils.BotManager.BotType;
import io.github.squat_team.model.PCMScenario;
import io.github.squat_team.model.ResponseMeasureType;

/**
 * Provides methods for setting up bots used in the case study.
 */
public class BotIntializer {

	/**
	 * Initializes 2 Performance bots and 2 Modifiability Bots. The standard setting of the STPlus case study.
	 */
	public static void initialize2P2MBots() {
		// TODO: LoadHelper.createModifiabilityScenarioS1(ResponseMeasureType.DECIMAL, 120, scenarioM1)
		createBot(, "TODO URI" , BotType.MODIFIABILITY);
		// TODO: LoadHelper.createModifiabilityScenarioS2(ResponseMeasureType.DECIMAL, 300, scenarioM2)
		createBot(, "TODO URI" , BotType.MODIFIABILITY);
		// TODO: LoadHelper.createPerformanceScenarioS1(ResponseMeasureType.DECIMAL, 30, scenarioP1)
		createBot(, "TODO URI" , BotType.PERFORMANCE);
		// TODO: LoadHelper.createPerformanceScenarioS1(ResponseMeasureType.DECIMAL, 40, scenarioP2)
		createBot(, "TODO URI" , BotType.PERFORMANCE);
	}

	/**
	 * Creates a bot.
	 * 
	 * @param scenario
	 *            the scenario the bot should have.
	 * @param remoteURI
	 *            uri of the docker bot.
	 * @param type
	 *            the type of the bot, e.g., performance, modifiability,...
	 */
	private static void createBot(PCMScenario scenario, String remoteURI, BotType type) {
		RestBot bot = new RestBot(remoteURI);
		bot.setScenario(scenario);
		BotManager.getInstance().addBot(bot, type);
	}

}
