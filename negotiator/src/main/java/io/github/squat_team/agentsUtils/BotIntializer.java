package io.github.squat_team.agentsUtils;

import org.json.JSONObject;
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
		createBot(LoadHelper.createModifiabilityScenarioS1(ResponseMeasureType.DECIMAL, 120.0), 
				"TODO URI" , BotType.MODIFIABILITY);
		createBot(LoadHelper.createModifiabilityScenarioS2(ResponseMeasureType.DECIMAL, 300.0),
				"TODO URI" , BotType.MODIFIABILITY);
		createBot(LoadHelper.createScenarioOfWorkload(ResponseMeasureType.DECIMAL, 30.0),
				"TODO URI" , BotType.PERFORMANCE);
		createBot(LoadHelper.createScenarioOfCPU(ResponseMeasureType.DECIMAL, 40),
				"TODO URI" , BotType.PERFORMANCE);
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
	private static void createBot(JSONObject scenario, String remoteURI, BotType type) {
		RestBot bot = new RestBot(remoteURI);
		bot.setScenario(scenario);
		BotManager.getInstance().addBot(bot, type);
	}

}
