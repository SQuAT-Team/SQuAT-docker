package io.github.squat_team.modifiability;

import java.util.ArrayList;
import java.util.List;

import io.github.squat_team.model.OptimizationType;
import io.github.squat_team.model.PCMScenario;

public class ModifiabilityPCMScenario extends PCMScenario {
	//This should represent the changes made to the repository model
	//New components, interfaces, required and provided roles, operations, datatypes, parameters, among others
	//It would also be possible to alter existing components by marking them
	//And to delete some unwanted components after the change is carried out
	private List<ModifiabilityInstruction> changes;
	//We should probably read the changes from a CSV file...it should not be that difficult to implement...
	
	public ModifiabilityPCMScenario(OptimizationType type) {
		super(type);
		this.changes = new ArrayList<ModifiabilityInstruction>(); 
	}
	
	public void addChange(ModifiabilityInstruction change) {
		changes.add(change);
	}
	
	public void removeChange(ModifiabilityInstruction change) {
		changes.remove(change);
	}
	
	public List<ModifiabilityInstruction> getChanges() {
		return changes;
	}
}
