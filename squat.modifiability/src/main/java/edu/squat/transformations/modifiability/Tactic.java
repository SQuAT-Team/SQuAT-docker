package edu.squat.transformations.modifiability;

import org.eclipse.emf.henshin.interpreter.EGraph;
import org.eclipse.emf.henshin.interpreter.Match;
import org.palladiosimulator.pcm.repository.BasicComponent;

public class Tactic {
	public BasicComponent seed;
	public String seedName;
	public Match initialMatch;
	public EGraph initialArchitecture;
	public EGraph resultingArchitecture;
}
