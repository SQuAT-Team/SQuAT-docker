package io.github.squat_team.model;

import java.util.Map;

public abstract class PCMTactic {
	
	abstract public void setup(Map<String, String> options);
	abstract public void apply();
	
}
