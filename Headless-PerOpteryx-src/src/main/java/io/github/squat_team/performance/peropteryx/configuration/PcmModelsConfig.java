package io.github.squat_team.performance.peropteryx.configuration;

import java.util.Map;

public class PcmModelsConfig extends AbstractConfig {

	private String eventMiddlewareRepositoryFile = "pathmap://PCM_MODELS/default_event_middleware.repository";
	private String mwRepositoryFile = "pathmap://PCM_MODELS/Glassfish.repository";
	private String rmiRepositoryFile = "pathmap://PCM_MODELS/Glassfish.repository";
	private String pathmapFolder = "";
	
	@Override
	public void initializeDefault() {
		// nothing
	}

	@Override
	public Map<String, Object> copyValuesTo(Map<String, Object> attr) {
		attr.putAll(defaultAttr);
		
		attr.put("eventMiddlewareRepositoryFile", eventMiddlewareRepositoryFile);
		attr.put("mwRepositoryFile", mwRepositoryFile);
		attr.put("rmiRepositoryFile", rmiRepositoryFile);
		
		return attr;
	}

	@Override
	public boolean validate() {
		return validatePath(eventMiddlewareRepositoryFile) && validatePath(mwRepositoryFile) && validatePath(rmiRepositoryFile) && validatePath(pathmapFolder);
	}

	public String getEventMiddlewareRepositoryFile() {
		return eventMiddlewareRepositoryFile;
	}

	public void setEventMiddlewareRepositoryFile(String eventMiddlewareRepositoryFile) {
		this.eventMiddlewareRepositoryFile = eventMiddlewareRepositoryFile;
	}

	public String getMwRepositoryFile() {
		return mwRepositoryFile;
	}

	public void setMwRepositoryFile(String mwRepositoryFile) {
		this.mwRepositoryFile = mwRepositoryFile;
	}

	public String getRmiRepositoryFile() {
		return rmiRepositoryFile;
	}

	public void setRmiRepositoryFile(String rmiRepositoryFile) {
		this.rmiRepositoryFile = rmiRepositoryFile;
	}

	public String getPathmapFolder() {
		return pathmapFolder;
	}

	public void setPathmapFolder(String pathmapFolder) {
		this.pathmapFolder = pathmapFolder;
	}

}
