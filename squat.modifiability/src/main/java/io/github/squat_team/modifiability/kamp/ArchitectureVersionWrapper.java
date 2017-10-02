package io.github.squat_team.modifiability.kamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.system.System;

import de.uka.ipd.sdq.componentInternalDependencies.ComponentInternalDependencyRepository;
import edu.kit.ipd.sdq.kamp.core.ArchitectureModelFactoryFacade;
import edu.kit.ipd.sdq.kamp.core.ArchitectureVersion;
import edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.FieldOfActivityAnnotationRepository;
import edu.kit.ipd.sdq.kamp.model.modificationmarks.ModificationRepository;
import io.github.squat_team.model.PCMArchitectureInstance;

//Transformations will cause trouble when partial updates of the models are made.
//We should keep all the models in sync. 
public class ArchitectureVersionWrapper {
	private PCMArchitectureInstance instance;
	
	public ArchitectureVersionWrapper(PCMArchitectureInstance currentArchitecture) {
		this.instance = (PCMArchitectureInstance) currentArchitecture.clone();
	}
	
	public ArchitectureVersion transformToKAMP() {
		//Setting up KAMP additional models (empty)
		FieldOfActivityAnnotationRepository fieldOfActivityRepository = ArchitectureModelFactoryFacade.createFieldOfActivityAnnotationsRepository();
		ModificationRepository internalModificationMarkRepository = ArchitectureModelFactoryFacade.createModificationMarkRepository();
		ComponentInternalDependencyRepository componentInternalDependencyRepository = ArchitectureModelFactoryFacade.createComponentInternalDependencyRepository();
		//Converting the model to KAMP's internal representation
		ArchitectureVersion architectureVersion = new ArchitectureVersion(
				instance.getName(), 
				instance.getRepository(), 
				instance.getSystem(), 
				fieldOfActivityRepository, 
				internalModificationMarkRepository, 
				componentInternalDependencyRepository);
		return architectureVersion; 
	}
	
	public PCMArchitectureInstance transformToSQuAT(ArchitectureVersion architectureVersion) {
		instance.setName(architectureVersion.getName());
		instance.setRepository(architectureVersion.getRepository());
		instance.setSystem(architectureVersion.getSystem());
		return (PCMArchitectureInstance) instance.clone(); 
	}
	
	public static ArchitectureVersion createClone(ArchitectureVersion base, String cloneName) {
		Collection<EObject> collection = new ArrayList<EObject>();
		collection.add(base.getRepository());
		collection.add(base.getSystem());
		collection.add(base.getFieldOfActivityRepository());
		collection.add(base.getModificationMarkRepository());
		collection.add(base.getComponentInternalDependencyRepository());
		Collection<EObject> clonedCollection = EcoreUtil.copyAll(collection);
		List<EObject> clonedList = new ArrayList<EObject>(clonedCollection);
		ArchitectureVersion clone = new ArchitectureVersion(
				cloneName, 
				(Repository) clonedList.get(0), 
				(System) clonedList.get(1), 
				(FieldOfActivityAnnotationRepository) clonedList.get(2), 
				(ModificationRepository) clonedList.get(3), 
				(ComponentInternalDependencyRepository) clonedList.get(4)
		);
		return clone;
	}
}
