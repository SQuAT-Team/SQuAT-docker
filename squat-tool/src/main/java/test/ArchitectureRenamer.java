package test;

import java.io.File;
import java.util.List;

import org.eclipse.emf.common.util.URI;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.PCMScenarioResult;
import io.github.squat_team.util.PCMHelper;
import io.github.squat_team.util.PCMWorkingCopyCreator;

/**
 * The architectures must have a unique name over the whole SQuAT-Run, but this
 * is not true for the performance bot results. This modifies the name of the
 * results and uses the {@link PCMWorkingCopyCreator} to keep the model
 * consistent (internal dependencies have to be updated, too).
 */
public class ArchitectureRenamer {

	public static void rename(List<PCMScenarioResult> searchForAlternativeResults) {
		for (PCMScenarioResult result : searchForAlternativeResults) {
			PCMArchitectureInstance architectureInstance = result.getResultingArchitecture();
			URI uri = architectureInstance.getUsageModel().eResource().getURI();
			File modelFile = new File(uri.toFileString());
			String newModelName = modelFile.getParentFile().getParentFile().getName() + "-"
					+ modelFile.getParentFile().getName();
			/*
			 * Working Copy will be in the same directory, previously:
			 * TestConstants.MAIN_STORAGE_PATH
			 */
			PCMWorkingCopyCreator copyCreator = new PCMWorkingCopyCreator(newModelName, modelFile.getParentFile());
			PCMArchitectureInstance architectureInstanceRenamed = copyCreator.createWorkingCopy(architectureInstance);
			File modelFileRenamed = new File(
					architectureInstanceRenamed.getUsageModel().eResource().getURI().toFileString());

			// It is not good practice to transform it first to ArchitecturalVersion and
			// then to the architecture, but it is the easiest way.
			ArchitecturalVersion newAlternative = new ArchitecturalVersion(
					modelFileRenamed.getName().substring(0, modelFileRenamed.getName().lastIndexOf('.')),
					modelFileRenamed.getParentFile().getName(), ArchitecturalVersion.PERFORMANCE);
			result.setResultingArchitecture(PCMHelper.createArchitecture(newAlternative));
		}
	}

}
