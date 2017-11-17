package io.github.squat_team.util;


import java.io.File;

import org.eclipse.emf.common.util.URI;
import io.github.squat_team.model.PCMArchitectureInstance;

public class PCMFileFinder {
	private File pcmFile;
	private String extension;

	public PCMFileFinder(PCMArchitectureInstance pcmInstance) {
		// take one of the pcm files.
		URI uri;
		if (pcmInstance.getRepository() != null) {
			uri = pcmInstance.getRepository().eResource().getURI();
		} else if (pcmInstance.getAllocation() != null) {
			uri = pcmInstance.getAllocation().eResource().getURI();
		} else if (pcmInstance.getUsageModel() != null) {
			uri = pcmInstance.getUsageModel().eResource().getURI();
		} else if (pcmInstance.getResourceEnvironment() != null) {
			uri = pcmInstance.getResourceEnvironment().eResource().getURI();
		} else if (pcmInstance.getSystem() != null) {
			uri = pcmInstance.getSystem().eResource().getURI();
		} else {
			throw new IllegalArgumentException("PCM Instance should not be empty!");
		}
		this.pcmFile = new File(uri.toFileString());
		this.extension = uri.fileExtension();
	}

	public String getName() {
		String pcmFileName = this.pcmFile.getName();
		String pcmFileExtension = this.extension;
		int pcmFileExtensionLength = pcmFileExtension != null ? pcmFileExtension.length() : 0;
		if (pcmFileExtensionLength > 0) {
			pcmFileExtension = "." + pcmFileExtension;
		}
		return pcmFileName.substring(0, pcmFileName.length() - pcmFileExtensionLength);
	}

	public String getPath() {
		return this.pcmFile.getParentFile().toString();
	}
	
	public String getExtension(){
		return this.extension;
	}

}
