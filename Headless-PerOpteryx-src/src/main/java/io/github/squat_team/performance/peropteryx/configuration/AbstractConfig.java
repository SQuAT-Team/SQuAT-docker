package io.github.squat_team.performance.peropteryx.configuration;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfig {
	
	protected Map<String, Object> defaultAttr;

	
	public AbstractConfig(){
		defaultAttr = new HashMap<String, Object>();
		initializeDefault();
	}
	
	protected abstract void initializeDefault();
	
	protected abstract Map<String, Object> copyValuesTo(Map<String, Object> attr);
	
	protected abstract boolean validate();
	
	protected boolean validatePercentage(Double value){
		return value >= 0.0 && value <= 1.0;
	}
	
	protected boolean validateInteger(Integer value){
		return value >= 0;
	}
	
	protected boolean validatePath(String path) {
		return !path.isEmpty();
	}
	
	
}
