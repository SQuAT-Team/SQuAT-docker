package io.github.squat_team.util;


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
		org.palladiosimulator.pcm.system.System baseSystem = (org.palladiosimulator.pcm.system.System) PCMHelper.load(systemFile);
		return baseSystem;
	}
	
	public static Allocation loadAllocationModel(String allocationFile) {
		Allocation allocation = (Allocation) PCMHelper.load(allocationFile);
		return allocation;
	}
	
	public static void printRepositoryModel(Repository repository) {
		System.out.println("---Repository---");
		for(RepositoryComponent componentType : repository.getComponents__Repository())
			System.out.println(componentType.getEntityName());
		for(Interface interfaceType : repository.getInterfaces__Repository())
			System.out.println(interfaceType.getEntityName());
	}

	public static void printResourceEnvironmentModel(ResourceEnvironment resourceEnvironment) {
		System.out.println("---Resource Environment---");
		for(LinkingResource linkingResource : resourceEnvironment.getLinkingResources__ResourceEnvironment())
			System.out.println(linkingResource.getEntityName());
		for(ResourceContainer resourceContainer : resourceEnvironment.getResourceContainer_ResourceEnvironment())
			System.out.println(resourceContainer.getEntityName());
	}
	
	public static void printSystemModel(org.palladiosimulator.pcm.system.System baseSystem) {
		System.out.println("---System---");
		for(AssemblyContext context : baseSystem.getAssemblyContexts__ComposedStructure()) {
			System.out.println(context.getEntityName());
		}
	}
	
	public static void printAllocationModel(Allocation allocation) {
		System.out.println("---Allocation---");
		for(AllocationContext allocationContext : allocation.getAllocationContexts_Allocation())
			System.out.println(allocationContext.getEntityName());
	}
}
