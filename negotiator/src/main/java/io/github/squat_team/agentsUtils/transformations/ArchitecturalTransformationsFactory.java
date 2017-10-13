package io.github.squat_team.agentsUtils.transformations;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.NegotiatorConfiguration;

public class ArchitecturalTransformationsFactory {

	private Hashtable<Integer, List<ArchitecturalVersion>> architecturesByLevel;
	private ModifiabilityTransformationsFactory modifiabilityTrans;
	private PerformanceTransformationFactory performanceTrans;
	// private ArchitecturalVersion initialArchitecture1;
	private ArchitecturalVersion initialArchitecture;

	public ArchitecturalTransformationsFactory() {
		modifiabilityTrans = new ModifiabilityTransformationsFactory();
		// initialArchitecture2=new ArchitecturalVersion("default","models","");
		// initialArchitecture2.setFullPathToAlternativeRepository("/Users/santiagovidal/Documents/Programacion/kamp-test/squat-tool/models/alternativeRepository.repository");
		initialArchitecture = new ArchitecturalVersion(NegotiatorConfiguration.INITIAL_ARCHITECTURE_NAME,
				NegotiatorConfiguration.INITIAL_ARCHITECTURE_PATH, "");
		initialArchitecture.setFullPathToAlternativeRepository(
				NegotiatorConfiguration.INITIAL_ARCHITECTURE_ALTERNATIVE_REPOSITORY_FULL_PATH);
		performanceTrans = new PerformanceTransformationFactory();
		architecturesByLevel = new Hashtable<>();
	}

	public List<ArchitecturalVersion> getArchitecturalTransformationsUntilLevel(int level) {
		if (architecturesByLevel.get(level) == null)
			createArchitecturalTransformationsForLevel(level);

		// The results is the architectures created for this level plus the
		// architectures created for previous levels
		List<ArchitecturalVersion> ret = new ArrayList<ArchitecturalVersion>();
		for (int i = 1; i <= level; i++) {
			ret.addAll(architecturesByLevel.get(i));
		}
		return ret;
	}

	private void createArchitecturalTransformationsForLevel(int level) {
		List<ArchitecturalVersion> transformationForLevel = new ArrayList<ArchitecturalVersion>();
		architecturesByLevel.put(level, transformationForLevel);
		if (level == 1) {
			// Applied transformations to initial architecture and save it on the hashtable
			transformationForLevel
					.addAll(generateArchitecturalVersionsUsingModifiabilityTransformations(initialArchitecture));
			transformationForLevel
					.addAll(generateArchitecturalVersionsUsingPerformanceTransformations(initialArchitecture));
		} else {
			List<ArchitecturalVersion> architecturesPreviousLevel = architecturesByLevel.get(level - 1);
			for (Iterator<ArchitecturalVersion> iterator = architecturesPreviousLevel.iterator(); iterator.hasNext();) {
				ArchitecturalVersion architecturalVersion = (ArchitecturalVersion) iterator.next();
				// if the architecture was modified last time by performance now is going to be
				// modified for modifiability.
				if (architecturalVersion.lastModifiedByModifiability()) {
					transformationForLevel
							.addAll(generateArchitecturalVersionsUsingPerformanceTransformations(architecturalVersion));
				} else {
					transformationForLevel.addAll(
							generateArchitecturalVersionsUsingModifiabilityTransformations(architecturalVersion));
				}
			}
		}
	}

	private List<ArchitecturalVersion> generateArchitecturalVersionsUsingModifiabilityTransformations(
			ArchitecturalVersion architecturalVersion) {
		return modifiabilityTrans.runModifiabilityTransformationsInAModel(architecturalVersion);
	}

	private List<ArchitecturalVersion> generateArchitecturalVersionsUsingPerformanceTransformations(
			ArchitecturalVersion architecturalVersion) {
		return performanceTrans.generateArchitecturalVersionsUsingPerformanceTransformations(architecturalVersion);
	}

	public List<ArchitecturalVersion> transformationsForLevel(int level) {
		return architecturesByLevel.get(level);
	}

	public ArchitecturalVersion getInitialArchitecture() {
		return initialArchitecture;
	}
}
