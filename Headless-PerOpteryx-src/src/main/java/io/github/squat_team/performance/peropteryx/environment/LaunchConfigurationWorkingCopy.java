package io.github.squat_team.performance.peropteryx.environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchDelegate;

public class LaunchConfigurationWorkingCopy implements ILaunchConfigurationWorkingCopy{
	Pcm2LqnLaunchConfiguration parentConfiguration;
	Map<String, Object> tempMap = new HashMap<String, Object>();
	
	public LaunchConfigurationWorkingCopy(Pcm2LqnLaunchConfiguration parentConfiguration) {
		this.parentConfiguration = parentConfiguration;
	}
	
	@Override
	public boolean contentsEqual(ILaunchConfiguration configuration) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ILaunchConfigurationWorkingCopy copy(String name) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete() throws CoreException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getAttribute(String attributeName, boolean defaultValue) throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getAttribute(String attributeName, int defaultValue) throws CoreException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getAttribute(String attributeName, List<String> defaultValue) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getAttribute(String attributeName, Set<String> defaultValue) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getAttribute(String attributeName, Map<String, String> defaultValue)
			throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttribute(String attributeName, String defaultValue) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getAttributes() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCategory() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFile getFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPath getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResource[] getMappedResources() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMemento() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getModes() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILaunchDelegate getPreferredDelegate(Set<String> modes) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILaunchConfigurationType getType() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILaunchConfigurationWorkingCopy getWorkingCopy() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAttribute(String attributeName) throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLocal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMigrationCandidate() throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWorkingCopy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ILaunch launch(String mode, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILaunch launch(String mode, IProgressMonitor monitor, boolean build) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILaunch launch(String mode, IProgressMonitor monitor, boolean build, boolean register) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void migrate() throws CoreException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean supportsMode(String mode) throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ILaunchConfiguration doSave() throws CoreException {
		parentConfiguration.getAttributeObject().putAll(tempMap);
		tempMap.clear();
		return parentConfiguration;
	}

	@Override
	public void setAttribute(String attributeName, int value) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setAttribute(String attributeName, String value) {
		tempMap.put(attributeName, value);
	}

	@Override
	public void setAttribute(String attributeName, List<String> value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttribute(String attributeName, Map<String, String> value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttribute(String attributeName, Set<String> value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttribute(String attributeName, boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ILaunchConfiguration getOriginal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rename(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContainer(IContainer container) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttributes(Map<String, ? extends Object> attributes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMappedResources(IResource[] resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setModes(Set<String> modes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPreferredLaunchDelegate(Set<String> modes, String delegateId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addModes(Set<String> modes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeModes(Set<String> modes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object removeAttribute(String attributeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILaunchConfigurationWorkingCopy getParent() {
		// TODO Auto-generated method stub
		return null;
	}

}
