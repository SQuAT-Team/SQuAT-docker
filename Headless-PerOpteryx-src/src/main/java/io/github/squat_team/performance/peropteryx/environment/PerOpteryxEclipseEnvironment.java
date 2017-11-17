package io.github.squat_team.performance.peropteryx.environment;

import java.util.logging.Logger;

import org.eclipse.core.internal.registry.RegistryProviderFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.resource.Resource;

import de.uka.ipd.sdq.pcm.designdecision.util.designdecisionResourceFactoryImpl;
import io.github.squat_team.performance.peropteryx.registry.RegistryProvider;

public enum PerOpteryxEclipseEnvironment {
	INSTANCE;

    private static final Logger log = Logger.getLogger(PalladioEclipseEnvironment.class.getName());

    private boolean isSetup = false;

    PerOpteryxEclipseEnvironment() {
    }
    
    /**
     * Sets up the PerOpteryx Eclipse environment by registering certain PerOpteryx and Eclipse functionality in the right
     * places. This method is thread-safe and will only perform the setup once, even when called multiple times.
     */
    public synchronized void setup() {
        if (this.isSetup) {
            return;
        }
        this.isSetup = true;

        log.info("Starting to set up the PerOpteryx Eclipse environment.");
        registerFactories();
        initializeFakeRegistry();
        log.info("Finished setting up the PerOpteryx Eclipse environment.");
    }
    
    /**
     * Determines whether setup of the environment has already been executed.
     */
    public boolean isSetup() {
        return this.isSetup;
    }
    
    private void registerFactories() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("designdecision",
				new designdecisionResourceFactoryImpl());
    }
    
	private void initializeFakeRegistry() {
		RegistryProvider regprovider = new RegistryProvider();
		try {
			RegistryProviderFactory.setDefault(regprovider);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		// called by: Platform.getExtensionRegistry()
	}
    
}
