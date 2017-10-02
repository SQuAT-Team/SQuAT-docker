package io.github.squat_team.modifiability;

import java.util.HashMap;
import java.util.Map;

public class ModifiabilityInstruction {
	public ModifiabilityOperation operation;
	public ModifiabilityElement element;
	public Map<String, String> parameters = new HashMap<String, String>();
}
