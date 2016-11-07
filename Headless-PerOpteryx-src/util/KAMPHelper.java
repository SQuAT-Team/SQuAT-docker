package io.github.squat_team.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import de.uka.ipd.sdq.componentInternalDependencies.ComponentInternalDependencyRepository;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.system.System;
import edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.FieldOfActivityAnnotationRepository;
import edu.kit.ipd.sdq.kamp.model.modificationmarks.ModificationRepository;

import edu.kit.ipd.sdq.kamp.core.Activity;
import edu.kit.ipd.sdq.kamp.core.ArchitectureVersion;

public class KAMPHelper {
	
	public static ArchitectureVersion createArchitectureVersionClone(ArchitectureVersion base, String cloneName) {
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
	
	public static void printActivities(List<Activity> activityList, String prefix) {
		if (prefix==null)
			prefix="";
		
		for (Activity activity : activityList) {
			java.lang.System.out.println(prefix + " " + activity.getBasicActivity() + " " + activity.getElementType() + " " + activity.getElementName());
			if (!activity.getSubactivities().isEmpty()) {
				printActivities(activity.getSubactivities(), prefix + "=");
			}
			if (!activity.getFollowupActivities().isEmpty()) {
				printActivities(activity.getFollowupActivities(), prefix+"->");
			}
		}
	}
}
