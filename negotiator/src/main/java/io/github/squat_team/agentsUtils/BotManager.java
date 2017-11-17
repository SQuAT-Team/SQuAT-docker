package io.github.squat_team.agentsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.squat_team.RestBot;

/**
 * Knows all the active bots.
 */
public class BotManager {

	public enum BotType {
		PERFORMANCE, MODIFIABILITY
	}

	private static final BotManager INSTANCE = new BotManager();
	private Map<BotType, List<RestBot>> bots = new HashMap<>();

	private BotManager() {
		// SINGLETON
	}

	public static BotManager getInstance() {
		return INSTANCE;
	}

	/**
	 * Adds a bot as active bot to the manager.
	 * 
	 * @param bot the bot to add.
	 */
	public void addBot(RestBot bot) {
		BotType type = bot.getBotType();
		List<RestBot> botsOfType = bots.get(type);
		if (botsOfType == null) {
			botsOfType = new ArrayList<RestBot>();
			bots.put(type, botsOfType);
		}
		botsOfType.add(bot);
	}

	/**
	 * Gets the active bots of the specified type.
	 * 
	 * @param type
	 *            bots of this type will be considered.
	 * @return all bots of the specified type.
	 */
	public List<RestBot> getBots(BotType type) {
		return bots.get(type);
	}

	/**
	 * Gets all the active bots.
	 * 
	 * @return all bots.
	 */
	public List<RestBot> getAllBots() {
		List<RestBot> allBots = new ArrayList<>();
		for (List<RestBot> typeBots : bots.values()) {
			allBots.addAll(typeBots);
		}
		return allBots;
	}

}
