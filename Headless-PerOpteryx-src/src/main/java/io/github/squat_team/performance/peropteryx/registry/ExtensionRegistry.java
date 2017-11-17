package io.github.squat_team.performance.peropteryx.registry;

import java.io.InputStream;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.IRegistryEventListener;

import io.github.squat_team.performance.peropteryx.start.HeadlessPerOpteryxConstants;

public class ExtensionRegistry implements IExtensionRegistry {
	IExtensionPoint lqnExtensionPoint = new ExtensionPoint();
	
	@Override
	public void addRegistryChangeListener(IRegistryChangeListener listener, String namespace) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRegistryChangeListener(IRegistryChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IConfigurationElement[] getConfigurationElementsFor(String extensionPointId) {
		if(extensionPointId.equals(HeadlessPerOpteryxConstants.IRESTULSTHANDLER_ID)){
			IConfigurationElement[] resultHandlers = new ResultHandlerConfigurationElement[1];
			resultHandlers[0] = new ResultHandlerConfigurationElement();
			return resultHandlers;
		}
		return null;
	}

	@Override
	public IConfigurationElement[] getConfigurationElementsFor(String namespace, String extensionPointName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IConfigurationElement[] getConfigurationElementsFor(String namespace, String extensionPointName,
			String extensionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExtension getExtension(String extensionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExtension getExtension(String extensionPointId, String extensionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExtension getExtension(String namespace, String extensionPointName, String extensionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExtensionPoint getExtensionPoint(String extensionPointId) {
		if (extensionPointId.equals("de.uka.ipd.sdq.dsexplore.analysis")){
			return lqnExtensionPoint;}
		return null;
	}

	@Override
	public IExtensionPoint getExtensionPoint(String namespace, String extensionPointName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExtensionPoint[] getExtensionPoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExtensionPoint[] getExtensionPoints(String namespace) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExtensionPoint[] getExtensionPoints(IContributor contributor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExtension[] getExtensions(String namespace) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExtension[] getExtensions(IContributor contributor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getNamespaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeRegistryChangeListener(IRegistryChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addContribution(InputStream is, IContributor contributor, boolean persist, String name,
			ResourceBundle translationBundle, Object token) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeExtension(IExtension extension, Object token) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeExtensionPoint(IExtensionPoint extensionPoint, Object token) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stop(Object token) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(IRegistryEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(IRegistryEventListener listener, String extensionPointId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(IRegistryEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isMultiLanguage() {
		// TODO Auto-generated method stub
		return false;
	}

}
