package io.github.squat_team;

import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMTactic;

public class ArchitectureTransformer {
	
	public ArchitectureTransformer() {
		// TODO Auto-generated constructor stub
	}
	
	public static PCMArchitectureInstance transform(PCMArchitectureInstance currentArchitecture, PCMArchitectureInstance initialArchitecture, PCMArchitectureInstance changedArchitecture) {
		PCMArchitectureInstance nextArchitecture = (PCMArchitectureInstance) currentArchitecture.clone();
		//Compute changes between architectures with EMF Compare or something like that
		//The changes would be from initialArchitecture and changedArchitecture
		//Then we would need to apply the changes to nextArchitecture
		return nextArchitecture;
	}
	
	public static PCMArchitectureInstance transform(PCMArchitectureInstance currentArchitecture, PCMTactic tactic) {
		PCMArchitectureInstance nextArchitecture = (PCMArchitectureInstance) currentArchitecture.clone();
		//Use henshin to apply the changes to the architecture...
		return nextArchitecture;
	}

}
