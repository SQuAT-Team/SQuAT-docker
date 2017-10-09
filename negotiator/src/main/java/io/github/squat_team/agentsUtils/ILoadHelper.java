package io.github.squat_team.agentsUtils;

import java.util.List;

import edu.squat.transformations.ArchitecturalVersion;

public interface ILoadHelper {
	public List<SillyBot> loadBotsForArchitecturalAlternatives(List<ArchitecturalVersion> architecturalAlternatives,
			ArchitecturalVersion initialArchitecture);
}
