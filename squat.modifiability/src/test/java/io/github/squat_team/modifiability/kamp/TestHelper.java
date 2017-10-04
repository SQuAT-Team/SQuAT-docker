package io.github.squat_team.modifiability.kamp;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.util.SQuATHelper;

/**
 * TODO: Move this to squat.utils package.
 */
public class TestHelper {
	public static PCMArchitectureInstance createArchitecture(ArchitecturalVersion architecturalVersion) {
		// create Instance
		Allocation allocation;
		org.palladiosimulator.pcm.system.System system;
		ResourceEnvironment resourceenvironment;
		Repository repository;
		UsageModel usageModel;

		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			// if is windows...
			allocation = SQuATHelper.loadAllocationModel("file:\\"+architecturalVersion.getAbsolutePath() + File.separator
					+ architecturalVersion.getAllocationFilename());
			system = SQuATHelper.loadSystemModel("file:\\"+
					architecturalVersion.getAbsolutePath() + File.separator + architecturalVersion.getSystemFilename());
			resourceenvironment = SQuATHelper.loadResourceEnvironmentModel("file:\\"+architecturalVersion.getAbsolutePath()
					+ File.separator + architecturalVersion.getResourceEnvironmentFilename());
			repository = SQuATHelper.loadRepositoryModel("file:\\"+architecturalVersion.getAbsolutePath() + File.separator
					+ architecturalVersion.getRepositoryFilename());
			usageModel = SQuATHelper.loadUsageModel("file:\\"+
					architecturalVersion.getAbsolutePath() + File.separator + architecturalVersion.getUsageFilename());
		} else {
			allocation = SQuATHelper.loadAllocationModel("file:/" + architecturalVersion.getAbsolutePath()
					+ File.separator + architecturalVersion.getAllocationFilename());
			system = SQuATHelper.loadSystemModel("file:/" + architecturalVersion.getAbsolutePath() + File.separator
					+ architecturalVersion.getSystemFilename());
			resourceenvironment = SQuATHelper
					.loadResourceEnvironmentModel("file:/" + architecturalVersion.getAbsolutePath() + File.separator
							+ architecturalVersion.getResourceEnvironmentFilename());
			repository = SQuATHelper.loadRepositoryModel("file:/" + architecturalVersion.getAbsolutePath()
					+ File.separator + architecturalVersion.getRepositoryFilename());
			usageModel = SQuATHelper.loadUsageModel("file:/" + architecturalVersion.getAbsolutePath() + File.separator
					+ architecturalVersion.getUsageFilename());
		}

		PCMArchitectureInstance architecture = new PCMArchitectureInstance(architecturalVersion.getFileName(),
				repository, system, allocation, resourceenvironment, usageModel);
		if (architecturalVersion.getFullPathToAlternativeRepository() != null) {
			if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
				Repository repositoryAlternatives = SQuATHelper
						.loadRepositoryModel("file:\\"+architecturalVersion.getFullPathToAlternativeRepository());
				architecture.setRepositoryWithAlternatives(repositoryAlternatives);
			}else{
			Repository repositoryAlternatives = SQuATHelper
					.loadRepositoryModel("file:/" + architecturalVersion.getFullPathToAlternativeRepository());
			architecture.setRepositoryWithAlternatives(repositoryAlternatives);
			}
		}

		return architecture;
	}
}
