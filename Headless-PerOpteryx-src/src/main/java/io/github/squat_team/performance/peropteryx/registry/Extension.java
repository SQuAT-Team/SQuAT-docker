package io.github.squat_team.performance.peropteryx.registry;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;

public class Extension implements IExtension {

	@Override
	public IConfigurationElement[] getConfigurationElements() throws InvalidRegistryObjectException {
		IConfigurationElement[] confElements = new IConfigurationElement[1];
		confElements[0] = new LQNSolverConfigurationElement();
		return confElements;
	}

	@Override
	public String getNamespace() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNamespaceIdentifier() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContributor getContributor() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExtensionPointUniqueIdentifier() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel(String locale) throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSimpleIdentifier() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUniqueIdentifier() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

}
