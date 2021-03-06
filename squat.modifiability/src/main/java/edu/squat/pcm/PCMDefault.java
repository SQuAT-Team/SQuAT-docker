package edu.squat.pcm;

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
import io.github.squat_team.modifiability.kamp.KAMPPCMBot;

public class PCMDefault {
	private static ResourceSet resourceSet = new ResourceSetImpl();
	private static ResourceRepository repositoryResource = null;
	private static Repository repositoryPrimitiveTypes = null;
	private static Repository repositoryFailureTypes = null;

	private static final URI PCM_MODEL_URI = URI.createURI("pathmap://PCM_MODELS/");
	private static final URI PCM_PHYSICAL_MODEL_URI = URI.createFileURI("").appendSegment("pcm")
			.appendSegment("defaultModels").appendSegment("");

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

	/**
	 * Registers the default model directory.
	 */
	private static void register() {
		URIConverter.URI_MAP.put(PCM_MODEL_URI, PCM_PHYSICAL_MODEL_URI);
	}

	/**
	 * Deregisters the default model directory.<br>
	 * Without this function multiple calls to
	 * {@link KAMPPCMBot#searchForAlternatives(io.github.squat_team.model.PCMArchitectureInstance)}
	 * with models in different directories does not work.
	 */
	private static void deregister() {
		URIConverter.URI_MAP.remove(PCM_MODEL_URI);
	}

	public static ResourceRepository loadResourceRepository() {
		register();
		if (repositoryResource == null) {
			Resource resource = resourceSet.getResource(URI.createURI("pathmap://PCM_MODELS/Palladio.resourcetype"),
					true);
			repositoryResource = (ResourceRepository) resource.getContents().get(0);
		}
		deregister();
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
		register();
		if (repositoryPrimitiveTypes == null) {
			Resource resource = resourceSet.getResource(URI.createURI("pathmap://PCM_MODELS/PrimitiveTypes.repository"),
					true);
			repositoryPrimitiveTypes = (Repository) resource.getContents().get(0);
		}
		deregister();
		return repositoryPrimitiveTypes.getDataTypes__Repository();
	}

	public static EList<FailureType> loadFailureTypes() {
		register();
		if (repositoryPrimitiveTypes == null) {
			Resource resource = resourceSet.getResource(URI.createURI("pathmap://PCM_MODELS/FailureTypes.repository"),
					true);
			repositoryFailureTypes = (Repository) resource.getContents().get(0);
		}
		deregister();
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