package io.github.squat_team;

import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;

public class HenshinResourceSetManager {
	private static HenshinResourceSetManager instance;
	private HenshinResourceSet resourceSet;
	private String currentAbsolutePath;
	private HenshinResourceSetManager() {
		resourceSet=null;
	}
	public static HenshinResourceSetManager getInstance(){
		if(instance==null)
			instance=new HenshinResourceSetManager();
		
		return instance;
	}
	public HenshinResourceSet getHenshinResourceSet(String absolutePath){
		if(resourceSet==null || (!currentAbsolutePath.equals(absolutePath))){
			resourceSet = new HenshinResourceSet(absolutePath);
			currentAbsolutePath=absolutePath;
		}
		
		return resourceSet;
	}
}
