package io.github.squat_team.util;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.SystemPackage;

public class PCMLoader {
	private static String dirPath = "file:////Users/alejandrorago/Documents/Implementacion/Repositorios/kamp-test/MediaStore3-Nightly/Model/MediaStore3_Model/";
	private static String repositoryFile = dirPath + "ms.repository";
	private static String resourceEnvironmentFile = dirPath + "ms.resourceenvironment";
	private static String baseSystemFile = dirPath + "ms_base.system";
	private static String baseAllocationFile = dirPath + "ms_base.allocation";
	
	static {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		EPackage.Registry.INSTANCE.put(RepositoryPackage.eNS_URI, RepositoryPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(ResourceenvironmentPackage.eNS_URI, ResourceenvironmentPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(SystemPackage.eNS_URI, SystemPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(AllocationPackage.eNS_URI, AllocationPackage.eINSTANCE);
	}
	
	public void testRepositoryModel() {
		Repository repository = PCMHelper.loadRepositoryModel(repositoryFile);
		System.out.println();
		PCMHelper.printRepositoryModel(repository);
	}
	
	public void testResourceEnvironmentModel() {
		ResourceEnvironment resourceEnvironment = PCMHelper.loadResourceEnvironmentModel(resourceEnvironmentFile);
		System.out.println();
		PCMHelper.printResourceEnvironmentModel(resourceEnvironment);
	}

	public void testSystemModel() {
		org.palladiosimulator.pcm.system.System baseSystem = PCMHelper.loadSystemModel(baseSystemFile);
		System.out.println();
		PCMHelper.printSystemModel(baseSystem);
	}
	
	public void testAllocationModel() {
		Allocation allocation = PCMHelper.loadAllocationModel(baseAllocationFile);
		System.out.println();
		PCMHelper.printAllocationModel(allocation);
	}
	
	public static void main(String[] args) {
		System.out.println("---PCM Model Loading---");
		PCMLoader loader = new PCMLoader();
		loader.testRepositoryModel();
		loader.testSystemModel();
		loader.testAllocationModel();
		loader.testResourceEnvironmentModel();
	}
}
