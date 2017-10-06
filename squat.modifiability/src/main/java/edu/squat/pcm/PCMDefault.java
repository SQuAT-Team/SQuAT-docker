package edu.squat.pcm;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.CorePackage;
import org.palladiosimulator.pcm.core.entity.EntityPackage;
import org.palladiosimulator.pcm.reliability.FailureType;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.resourcetype.CommunicationLinkResourceType;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourceInterface;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.ResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourcetypePackage;
import org.palladiosimulator.pcm.resourcetype.SchedulingPolicy;
import org.palladiosimulator.pcm.seff.SeffPackage;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

import de.uka.ipd.sdq.identifier.IdentifierPackage;
import de.uka.ipd.sdq.stoex.StoexPackage;

public class PCMDefault {
	private static ResourceSet resourceSet = new ResourceSetImpl();
	private static ResourceRepository repositoryResource = null;
	private static Repository repositoryPrimitiveTypes = null;
	private static Repository repositoryFailureTypes = null;

	static {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		// PCM Packages
		EPackage.Registry.INSTANCE.put(IdentifierPackage.eNS_URI, IdentifierPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(StoexPackage.eNS_URI, StoexPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(CorePackage.eNS_URI, CorePackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(EntityPackage.eNS_URI, EntityPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(RepositoryPackage.eNS_URI, RepositoryPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(SystemPackage.eNS_URI, SystemPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(ResourceenvironmentPackage.eNS_URI, ResourceenvironmentPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(AllocationPackage.eNS_URI, AllocationPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(UsagemodelPackage.eNS_URI, UsagemodelPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(SeffPackage.eNS_URI, SeffPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(ResourcetypePackage.eNS_URI, ResourcetypePackage.eINSTANCE);
	}

	static {
		Path currentPath = Paths.get("").toAbsolutePath();
		Path workspacePath = currentPath.getParent();
		String uriPath = workspacePath.normalize().toString();
		// URI platformURI =
		// URI.createPlatformPluginURI("org.palladiosimulator.pcm.resources",
		// true).appendSegment("defaultModels").appendSegment("");
		// URI physicalURI =
		// URI.createFileURI(uriPath).appendSegment("PCM").appendSegment("org.palladiosimulator.pcm.resources").appendSegment("defaultModels").appendSegment("");

		// URI physicalURI =
		// URI.createFileURI(uriPath).appendSegment("squat.modifiability").appendSegment("pcm").appendSegment("defaultModels").appendSegment("");
		// for docker:
		URI physicalURI = URI.createFileURI("").appendSegment("home").appendSegment("pcm")
				.appendSegment("defaultModels").appendSegment("");
		URI pcmModels = URI.createURI("pathmap://PCM_MODELS/");
		URIConverter.URI_MAP.put(pcmModels, physicalURI);
		// URIConverter.URI_MAP.put(URI.createURI("pathmap://PCM_MODELS/"),URI.createURI("platform:/plugin/org.palladiosimulator.pcm.resources/defaultModels/"));
		// URIConverter.URI_MAP.put(URI.createURI("pathmap://PCM_TRANSFORMATIONS/EVENTS/"),URI.createURI("platform:/plugin/org.palladiosimulator.pcm.resources/transformations/events/"));
	}

	public static ResourceRepository loadResourceRepository() {
		if (repositoryResource == null) {
			Resource resource = resourceSet.getResource(URI.createURI("pathmap://PCM_MODELS/Palladio.resourcetype"),
					true);
			repositoryResource = (ResourceRepository) resource.getContents().get(0);
		}
		return repositoryResource;
	}

	public static EList<ResourceInterface> loadResourceInterfaces() {
		return PCMDefault.loadResourceRepository().getResourceInterfaces__ResourceRepository();
	}

	public static EList<SchedulingPolicy> loadSchedulingPolicies() {
		return PCMDefault.loadResourceRepository().getSchedulingPolicies__ResourceRepository();
	}

	public static EList<ResourceType> loadAvailableResourceTypes() {
		return PCMDefault.loadResourceRepository().getAvailableResourceTypes_ResourceRepository();
	}

	public static EList<DataType> loadPrimitiveTypes() {
		if (repositoryPrimitiveTypes == null) {
			Resource resource = resourceSet.getResource(URI.createURI("pathmap://PCM_MODELS/PrimitiveTypes.repository"),
					true);
			repositoryPrimitiveTypes = (Repository) resource.getContents().get(0);
		}
		return repositoryPrimitiveTypes.getDataTypes__Repository();
	}

	public static EList<FailureType> loadFailureTypes() {
		if (repositoryPrimitiveTypes == null) {
			Resource resource = resourceSet.getResource(URI.createURI("pathmap://PCM_MODELS/FailureTypes.repository"),
					true);
			repositoryFailureTypes = (Repository) resource.getContents().get(0);
		}
		return repositoryFailureTypes.getFailureTypes__Repository();
	}

	public static ProcessingResourceType getCPU() {
		return (ProcessingResourceType) PCMDefault.loadAvailableResourceTypes().get(0);
	}

	public static CommunicationLinkResourceType getLAN() {
		return (CommunicationLinkResourceType) PCMDefault.loadAvailableResourceTypes().get(1);
	}

	public static ProcessingResourceType getHDD() {
		return (ProcessingResourceType) PCMDefault.loadAvailableResourceTypes().get(2);
	}

	public static ProcessingResourceType getDELAY() {
		return (ProcessingResourceType) PCMDefault.loadAvailableResourceTypes().get(3);
	}

	public static void main(String[] args) {
		EList<ResourceType> resources = PCMDefault.loadAvailableResourceTypes();
		for (ResourceType resourceType : resources)
			System.out.println(resourceType.getEntityName());
	}
}