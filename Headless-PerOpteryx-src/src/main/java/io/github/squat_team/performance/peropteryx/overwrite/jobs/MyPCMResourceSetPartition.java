package io.github.squat_team.performance.peropteryx.overwrite.jobs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.palladiosimulator.analyzer.workflow.blackboard.PCMResourceSetPartition;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

public class MyPCMResourceSetPartition extends PCMResourceSetPartition{

	ResourceSet rs = new MyResourceSetImpl();
	
	
	public ResourceSet getResourceSet() {
		return rs;
	}
	
    /**
     * @return Returns the PCM Resource Environment used by the stored PCM model instance
     */
    public ResourceEnvironment getResourceEnvironment() {
        return (ResourceEnvironment) getElement(ResourceenvironmentPackage.eINSTANCE.getResourceEnvironment()).get(0);
    }
	
	public void initialiseResourceSetEPackages(EPackage[] ePackages) {
		for (EPackage ePackage : ePackages) {
			rs.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
		}
	}
	
	public MyPCMResourceSetPartition(){
		super();
		super.rs = this.rs;
	}
	
	@Override
	public synchronized Resource loadModel(String modelURI) {
		Resource r;
		if (modelURI.indexOf("://") >= 0) { 
			r = rs.getResource(URI.createURI(modelURI), true);
		} else {
			r = rs.getResource(URI.createFileURI(modelURI), true);
		}
		EcoreUtil.resolveAll(r);
		return r;
		
	}	
	
	@Override
	public synchronized Resource loadModel(URI modelURI) {
		Resource r;
		r = rs.getResource(modelURI, true);
		EcoreUtil.resolveAll(r);
		return r;
		
	}
	
    /**
     * Helper to find root objects of a specified class.
     *
     * @param clazz The class to get elements for.
     * @return The list of found root elements. Empty list if none have been found.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends EObject> List<T> getElement(final EClass targetType) {
        final ArrayList<T> result = new ArrayList<T>();
        for (final Resource r : rs.getResources()) {
            if (r != null && r.getContents().size() > 0 && r.getContents().get(0).eClass() == targetType ) {
                result.add((T) r.getContents().get(0));
            }
        }
        if (result.size() == 0) {
            throw new RuntimeException("Failed to retrieve PCM model element "+targetType.getName());
        } else {
            return result;
        }
    }
	
}
