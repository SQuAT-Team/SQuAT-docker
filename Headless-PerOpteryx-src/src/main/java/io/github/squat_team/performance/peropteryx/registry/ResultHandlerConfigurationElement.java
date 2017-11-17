package io.github.squat_team.performance.peropteryx.registry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;

import io.github.squat_team.performance.peropteryx.export.PCMResultsHandler;
import io.github.squat_team.performance.peropteryx.start.HeadlessPerOpteryxConstants;

public class ResultHandlerConfigurationElement implements IConfigurationElement {

	@Override
	public Object createExecutableExtension(String propertyName) throws CoreException {
		return new PCMResultsHandler();
	}

	@Override
	public String getAttribute(String name) throws InvalidRegistryObjectException {
		if (name.equals("name")){
			return HeadlessPerOpteryxConstants.LQN_SOLVER_METHOD_NAME;
		}
		return null;
	}

	@Override
	public String getAttribute(String attrName, String locale) throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttributeAsIs(String name) throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAttributeNames() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IConfigurationElement[] getChildren() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IConfigurationElement[] getChildren(String name) throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExtension getDeclaringExtension() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() throws InvalidRegistryObjectException {
		return "analysis";
	}

	@Override
	public Object getParent() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue(String locale) throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValueAsIs() throws InvalidRegistryObjectException {
		// TODO Auto-generated method stub
		return null;
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
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

}
