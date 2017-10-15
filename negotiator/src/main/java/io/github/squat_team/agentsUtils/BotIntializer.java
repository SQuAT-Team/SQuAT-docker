package io.github.squat_team.agentsUtils;

import org.json.JSONObject;

import io.github.squat_team.RestBot;
import io.github.squat_team.agentsUtils.BotManager.BotType;
import io.github.squat_team.model.ResponseMeasureType;

/**
 * Provides methods for setting up bots used in the case study.
 */
public class BotIntializer {

	// TODO: PA! Set the Docker URI.
	public static final String PERF_BOT_URI = "http://performance-bot:8080";
	public static final String MOD_BOT_URI = "http://modifiability-bot:8081";

	/**
	 * Initializes only 1 Performance Bot and 1 Modifiability Bot.
	 */
	public static void initialize1P1MBots() {
		createBot("M1", LoadHelper.createModifiabilityScenarioS1(ResponseMeasureType.DECIMAL, 120.0), MOD_BOT_URI,
				BotType.MODIFIABILITY);
		createBot("P2", LoadHelper.createPerformanceScenarioS2(ResponseMeasureType.DECIMAL, 40.0), PERF_BOT_URI,
				BotType.PERFORMANCE);
	}

	/**
	 * Initializes 2 Performance bots and 2 Modifiability Bots. The standard setting
	 * of the STPlus case study.
	 */
	public static void initialize2P2MBots() {
		createBot("M1", LoadHelper.createModifiabilityScenarioS1(ResponseMeasureType.DECIMAL, 120.0), MOD_BOT_URI,
				BotType.MODIFIABILITY);
		createBot("M2", LoadHelper.createModifiabilityScenarioS2(ResponseMeasureType.DECIMAL, 300.0), MOD_BOT_URI,
				BotType.MODIFIABILITY);
		createBot("P1", LoadHelper.createPerformanceScenarioS1(ResponseMeasureType.DECIMAL, 30.0), PERF_BOT_URI,
				BotType.PERFORMANCE);
		createBot("P2", LoadHelper.createPerformanceScenarioS2(ResponseMeasureType.DECIMAL, 40.0), PERF_BOT_URI,
				BotType.PERFORMANCE);
	}

	/**
	 * Initializes 2 Performance bots and 2 Modifiability Bots. The standard setting
	 * of the STPlus case study.
	 */
	public static void initialize3P3MBots() {
		initialize2P2MBots();
		createBot("M3", LoadHelper.createModifiabilityScenarioS3(ResponseMeasureType.DECIMAL, 98.0), MOD_BOT_URI,
				BotType.MODIFIABILITY);
		createBot("P3", LoadHelper.createPerformanceScenarioS3(ResponseMeasureType.DECIMAL, 40.0), PERF_BOT_URI,
				BotType.PERFORMANCE);
	}

	/**
	 * Initializes 2 Performance bots and 2 Modifiability Bots. The standard setting
	 * of the STPlus case study.
	 */
	public static void initialize4P4MBots() {
		initialize3P3MBots();
		createBot("M4", LoadHelper.createModifiabilityScenarioS4(ResponseMeasureType.DECIMAL, 199.5), MOD_BOT_URI,
				BotType.MODIFIABILITY);
		createBot("P4", LoadHelper.createPerformanceScenarioS4(ResponseMeasureType.DECIMAL, 45.0), PERF_BOT_URI,
				BotType.PERFORMANCE);
	}

	/**
	 * Creates a bot.
	 * 
	 * @param name
	 *            the name that the bot should have.
	 * @param scenario
	 *            the scenario the bot should have.
	 * @param remoteURI
	 *            uri of the docker bot.
	 * @param type
	 *            the type of the bot, e.g., performance, modifiability,...
	 */
	private static void createBot(String name, JSONObject scenario, String remoteURI, BotType type) {
		RestBot bot = new RestBot(name, type, remoteURI, scenario);
		BotManager.getInstance().addBot(bot);
	}

}
