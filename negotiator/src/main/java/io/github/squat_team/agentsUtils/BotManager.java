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

	public enum BotType{
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
	
	public void addBot(RestBot bot, BotType type) {
		List<RestBot> botsOfType = bots.get(type);
		if(botsOfType == null) {
			botsOfType = new ArrayList<RestBot>();
			bots.put(type, botsOfType);
		}
		botsOfType.add(bot);
	}
	
	public List<RestBot> getBots(BotType type){
		return bots.get(type);
	}
	
	
}
