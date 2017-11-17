package io.github.squat_team.performance.peropteryx.export;

/**
 * This class carries the paths to a PCM model and the result of its
 * evaluation.
 */
public class PerOpteryxPCMResult implements Comparable<PerOpteryxPCMResult> {

	private Double value;
	private String folderPath;
	private boolean filesExist;

	public PerOpteryxPCMResult(Double value, String folderPath) {
		this.value = value;
		this.folderPath = folderPath;
		this.filesExist = !(folderPath == null);
	}

	public Double getValue() {
		return this.value;
	}

	public boolean filesExist() {
		return filesExist;
	}

	public String getAllocationPath() {
		return folderPath + PCMFileExporter.FILE_PREFIX + ".allocation";
	}

	public String getRepositoryPath() {
		return folderPath + PCMFileExporter.FILE_PREFIX + ".repository";
	}

	public String getResourceTypePath() {
		return folderPath + PCMFileExporter.FILE_PREFIX + ".resourcetype";
	}

	public String getResourceEnvironmentPath() {
		return folderPath + PCMFileExporter.FILE_PREFIX + ".resourceenvironment";
	}

	public String getSystemPath() {
		return folderPath + PCMFileExporter.FILE_PREFIX + ".system";
	}

	public String getUsagemodelPath() {
		return folderPath + PCMFileExporter.FILE_PREFIX + ".usagemodel";
	}

	@Override
	public int compareTo(PerOpteryxPCMResult o) {
		if(o.getValue() > this.getValue()){
			return -1;
		}else if(o.getValue() == this.getValue()){
			return 0;
		}else{
			return 1;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PerOpteryxPCMResult) {
			PerOpteryxPCMResult result = (PerOpteryxPCMResult) o;
			return result.getValue().doubleValue() == this.getValue().doubleValue();
		}
		return false;
	}
}
