package io.github.squat_team.performance.peropteryx.environment;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.*;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Custom implementation of the launch configuration as the one's provided by
 * Palladio are too restrictive.
 */
public class Pcm2LqnLaunchConfiguration implements ILaunchConfiguration {

	private Map<String, Object> attr = new HashMap<>();

	private Logger log = Logger.getLogger(Pcm2LqnLaunchConfiguration.class.getName());
	
	public Pcm2LqnLaunchConfiguration(Map<String, Object> attr) {
		this.attr.putAll(attr);
		log.info("Using ILaunchConfiguration from " + this.getClass().getName() + " with attributes:");
		log.info(this.toString());
		}

	protected Map<String, Object> getAttributeObject(){
		return attr;
	}

	@Override
	public String toString() {
		return this.attr.entrySet().stream().map((e) -> {
			return String.format("|- %s: %s", e.getKey(), e.getValue().toString());
		}).collect(Collectors.joining("\n"));
	}

	@Override
	public boolean contentsEqual(ILaunchConfiguration iLaunchConfiguration) {
		return iLaunchConfiguration.contentsEqual(this);
	}

	@Override
	public ILaunchConfigurationWorkingCopy copy(String s) throws CoreException {
		throw new RuntimeException("NYI");
	}

	@Override
	public void delete() throws CoreException {
		throw new RuntimeException("NYI");
	}

	@Override
	public boolean exists() {
		return false;
	}

	private <T> T get(String key, T defaultValue) {
		Object val = this.attr.get(key);
		if (null == val) {
			return defaultValue;
		} else {
			return (T) val;
		}
	}

	@Override
	public boolean getAttribute(String s, boolean b) throws CoreException {
		return this.get(s, b);
	}

	@Override
	public int getAttribute(String s, int i) throws CoreException {
		return this.get(s, i);
	}

	@Override
	public List<String> getAttribute(String s, List<String> list) throws CoreException {
		return this.get(s, list);
	}

	@Override
	public Set<String> getAttribute(String s, Set<String> set) throws CoreException {
		return this.get(s, set);
	}

	@Override
	public Map<String, String> getAttribute(String s, Map<String, String> map) throws CoreException {
		return this.get(s, map);
	}

	@Override
	public String getAttribute(String s, String s1) throws CoreException {
		return this.get(s, s1);
	}

	@Override
	public Map<String, Object> getAttributes() throws CoreException {
		return this.attr;
	}

	@Override
	public String getCategory() throws CoreException {
		throw new RuntimeException("NYI");
	}

	@Override
	public IFile getFile() {
		throw new RuntimeException("NYI");
	}

	@Override
	public IPath getLocation() {
		throw new RuntimeException("NYI");
	}

	@Override
	public IResource[] getMappedResources() throws CoreException {
		throw new RuntimeException("NYI");
	}

	@Override
	public String getMemento() throws CoreException {
		return "This is an empty memento of the LaunchConfiguration";//throw new RuntimeException("NYI");
	}

	@Override
	public String getName() {
		return "default";
		// throw new RuntimeException("NYI");
	}

	@Override
	public Set<String> getModes() throws CoreException {
		throw new RuntimeException("NYI");
	}

	@Override
	public ILaunchDelegate getPreferredDelegate(Set<String> set) throws CoreException {
		throw new RuntimeException("NYI");
	}

	@Override
	public ILaunchConfigurationType getType() throws CoreException {
		throw new RuntimeException("NYI");
	}

	@Override
	public ILaunchConfigurationWorkingCopy getWorkingCopy() throws CoreException {
		return new LaunchConfigurationWorkingCopy(this);
	}

	@Override
	public boolean hasAttribute(String s) throws CoreException {
		return this.attr.containsKey(s);
	}

	@Override
	public boolean isLocal() {
		throw new RuntimeException("NYI");
	}

	@Override
	public boolean isMigrationCandidate() throws CoreException {
		throw new RuntimeException("NYI");
	}

	@Override
	public boolean isWorkingCopy() {
		throw new RuntimeException("NYI");
	}

	@Override
	public ILaunch launch(String s, IProgressMonitor iProgressMonitor) throws CoreException {
		throw new RuntimeException("NYI");
	}

	@Override
	public ILaunch launch(String s, IProgressMonitor iProgressMonitor, boolean b) throws CoreException {
		throw new RuntimeException("NYI");
	}

	@Override
	public ILaunch launch(String s, IProgressMonitor iProgressMonitor, boolean b, boolean b1) throws CoreException {
		throw new RuntimeException("NYI");
	}

	@Override
	public void migrate() throws CoreException {
		throw new RuntimeException("NYI");
	}

	@Override
	public boolean supportsMode(String s) throws CoreException {
		throw new RuntimeException("NYI");
	}

	@Override
	public boolean isReadOnly() {
		throw new RuntimeException("NYI");
	}

	@Override
	public Object getAdapter(Class aClass) {
		throw new RuntimeException("NYI");
	}
}
