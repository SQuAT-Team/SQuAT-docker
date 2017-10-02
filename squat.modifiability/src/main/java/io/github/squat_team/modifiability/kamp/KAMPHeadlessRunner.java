package io.github.squat_team.modifiability.kamp;

import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import de.uka.ipd.sdq.componentInternalDependencies.ComponentInternalDependenciesPackage;
import de.uka.ipd.sdq.internalmodificationmark.InternalmodificationmarkPackage;
import edu.kit.ipd.sdq.kamp.core.Activity;
import edu.kit.ipd.sdq.kamp.core.ArchitectureVersion;
import edu.kit.ipd.sdq.kamp.core.ChangePropagationAnalysis;
import edu.kit.ipd.sdq.kamp.core.derivation.DifferenceCalculation;

public class KAMPHeadlessRunner {
	static {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		//KAMP Packages
		EPackage.Registry.INSTANCE.put(ComponentInternalDependenciesPackage.eNS_URI, ComponentInternalDependenciesPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(de.uka.ipd.sdq.fieldOfActivityAnnotations.FieldOfActivityAnnotationsPackage.eNS_URI, de.uka.ipd.sdq.fieldOfActivityAnnotations.FieldOfActivityAnnotationsPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(InternalmodificationmarkPackage.eNS_URI, InternalmodificationmarkPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.FieldofactivityannotationsPackage.eNS_URI, edu.kit.ipd.sdq.kamp.model.fieldofactivityannotations.FieldofactivityannotationsPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(edu.kit.ipd.sdq.kamp.model.modificationmarks.modificationmarksPackage.eNS_URI, edu.kit.ipd.sdq.kamp.model.modificationmarks.modificationmarksPackage.eINSTANCE);
	}
	
	public static List<Activity> runAnalysis(ArchitectureVersion baseAV, ArchitectureVersion changedAV) {
		ChangePropagationAnalysis cia = new ChangePropagationAnalysis();
		cia.runChangePropagationAnalysis(changedAV);
		List<Activity> activityList = DifferenceCalculation.deriveWorkplan(baseAV, changedAV);
		return activityList;
	}
}
