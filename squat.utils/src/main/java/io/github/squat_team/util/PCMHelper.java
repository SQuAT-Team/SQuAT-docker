package io.github.squat_team.util;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import edu.squat.transformations.ArchitecturalVersion;
import io.github.squat_team.model.PCMArchitectureInstance;

public class PCMHelper {

	private static EObject load(String inputString) {
		URI resourceURI = URI.createURI(inputString);
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.getResource(resourceURI, true);
		EObject content = resource.getContents().get(0);
		return content;
	}

	public static Repository loadRepositoryModel(String repositoryFile) {
		Repository repository = (Repository) PCMHelper.load(repositoryFile);
		return repository;
	}

	public static ResourceEnvironment loadResourceEnvironmentModel(String resourceEnvironmentFile) {
		ResourceEnvironment resourceEnvironment = (ResourceEnvironment) PCMHelper.load(resourceEnvironmentFile);
		return resourceEnvironment;
	}

	public static org.palladiosimulator.pcm.system.System loadSystemModel(String systemFile) {
		org.palladiosimulator.pcm.system.System baseSystem = (org.palladiosimulator.pcm.system.System) PCMHelper
				.load(systemFile);
		return baseSystem;
	}

	public static Allocation loadAllocationModel(String allocationFile) {
		Allocation allocation = (Allocation) PCMHelper.load(allocationFile);
		return allocation;
	}

	public static UsageModel loadUsageModel(String usageFile) {
		UsageModel usage = (UsageModel) PCMHelper.load(usageFile);
		return usage;
	}

	public static void printRepositoryModel(Repository repository) {
		System.out.println("---Repository---");
		for (RepositoryComponent componentType : repository.getComponents__Repository())
			System.out.println(componentType.getEntityName());
		for (Interface interfaceType : repository.getInterfaces__Repository())
			System.out.println(interfaceType.getEntityName());
	}

	public static void printResourceEnvironmentModel(ResourceEnvironment resourceEnvironment) {
		System.out.println("---Resource Environment---");
		for (LinkingResource linkingResource : resourceEnvironment.getLinkingResources__ResourceEnvironment())
			System.out.println(linkingResource.getEntityName());
		for (ResourceContainer resourceContainer : resourceEnvironment.getResourceContainer_ResourceEnvironment())
			System.out.println(resourceContainer.getEntityName());
	}

	public static void printSystemModel(org.palladiosimulator.pcm.system.System baseSystem) {
		System.out.println("---System---");
		for (AssemblyContext context : baseSystem.getAssemblyContexts__ComposedStructure()) {
			System.out.println(context.getEntityName());
		}
	}

	public static void printAllocationModel(Allocation allocation) {
		System.out.println("---Allocation---");
		for (AllocationContext allocationContext : allocation.getAllocationContexts_Allocation())
			System.out.println(allocationContext.getEntityName());
	}

	/**
	 * Loads a specific model as {@link PCMArchitectureInstance}. This method does
	 * not load the alternative repository. Note that this method may only work for
	 * Unix OS.
	 * 
	 * @param architecturalVersion
	 *            the architecture to load.
	 * @return the loaded model.
	 */
	public static PCMArchitectureInstance loadSpecificModel(ArchitecturalVersion architecturalVersion) {
		Repository repository = SQuATHelper.loadRepositoryModel("file:/" + architecturalVersion.getPath()
				+ File.separator + architecturalVersion.getRepositoryFilename());
		ResourceEnvironment resourceEnvironment = SQuATHelper
				.loadResourceEnvironmentModel("file:/" + architecturalVersion.getPath() + File.separator
						+ architecturalVersion.getResourceEnvironmentFilename());
		org.palladiosimulator.pcm.system.System system = SQuATHelper.loadSystemModel(
				"file:/" + architecturalVersion.getPath() + File.separator + architecturalVersion.getSystemFilename());
		Allocation allocation = SQuATHelper.loadAllocationModel("file:/" + architecturalVersion.getPath()
				+ File.separator + architecturalVersion.getAllocationFilename());
		UsageModel usageModel = SQuATHelper.loadUsageModel(
				"file:/" + architecturalVersion.getPath() + File.separator + architecturalVersion.getUsageFilename());
		PCMArchitectureInstance instance = new PCMArchitectureInstance(architecturalVersion.getFileName(), repository,
				system, allocation, resourceEnvironment, usageModel);
		return instance;
	}

	/**
	 * Loads the architecture including the alternative repository. This method
	 * should be called, if the merging/splitting of the repository is not done yet.
	 * Otherwise {@link #loadSpecificModel(ArchitecturalVersion)} should be used.
	 * 
	 * @param architecturalVersion
	 *            the architecture to load.
	 * @return the loaded model.
	 */
	public static PCMArchitectureInstance createArchitecture(ArchitecturalVersion architecturalVersion) {
		Allocation allocation = SQuATHelper.loadAllocationModel("file:/" + architecturalVersion.getAbsolutePath()
				+ File.separator + architecturalVersion.getAllocationFilename());
		org.palladiosimulator.pcm.system.System system = SQuATHelper.loadSystemModel("file:/"
				+ architecturalVersion.getAbsolutePath() + File.separator + architecturalVersion.getSystemFilename());
		ResourceEnvironment resourceenvironment = SQuATHelper
				.loadResourceEnvironmentModel("file:/" + architecturalVersion.getAbsolutePath() + File.separator
						+ architecturalVersion.getResourceEnvironmentFilename());
		Repository repository = SQuATHelper.loadRepositoryModel("file:/" + architecturalVersion.getAbsolutePath()
				+ File.separator + architecturalVersion.getRepositoryFilename());
		UsageModel usageModel = SQuATHelper.loadUsageModel("file:/" + architecturalVersion.getAbsolutePath()
				+ File.separator + architecturalVersion.getUsageFilename());

		PCMArchitectureInstance architecture = new PCMArchitectureInstance(architecturalVersion.getFileName(),
				repository, system, allocation, resourceenvironment, usageModel);
		if (architecturalVersion.getFullPathToAlternativeRepository() != null) {
			Repository repositoryAlternatives = SQuATHelper
					.loadRepositoryModel("file:/" + architecturalVersion.getFullPathToAlternativeRepository());
			architecture.setRepositoryWithAlternatives(repositoryAlternatives);
		}

		return architecture;
	}

	/**
	 * Converts the {@link PCMArchitectureInstance} to a
	 * {@link ArchitecturalVersion}. This method is assumed to only work on Linux.
	 * 
	 * @param architecture
	 *            the architecture to convert.
	 * @return the converted architecture.
	 */
	public static ArchitecturalVersion createArchitecture(PCMArchitectureInstance architecture) {
		File repositoryFile = new File(architecture.getRepository().eResource().getURI().toFileString());
		String fileName = repositoryFile.getName().replace(".repository", "");
		ArchitecturalVersion result = new ArchitecturalVersion(fileName, repositoryFile.getParent(), "");

		File alternativeRepositoryFile = new File(
				architecture.getRepositoryWithAlternatives().eResource().getURI().toFileString());
		result.setFullPathToAlternativeRepository(alternativeRepositoryFile.getAbsolutePath());
		return result;
	}
}
