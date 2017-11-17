package io.github.squat_team.performance.peropteryx.configuration;

import java.util.Map;

public class LQNSConfig extends AbstractConfiguration {
	
	private String lqnsOutputDir = "";
	
	@Override
	public void initializeDefault() {
		defaultAttr.put("solver", "LQNS (Layered Queueing Network Solver)");
		
		defaultAttr.put("convValue", "0.001");
		defaultAttr.put("itLimit", "50");
		defaultAttr.put("printInt", "10");
		defaultAttr.put("underCoeff", "0.5");

		defaultAttr.put("LQNS Stop On Message Loss", true);
		defaultAttr.put("infiniteTaskMultiplicity", true);

		defaultAttr.put("output", "XML Output");

		defaultAttr.put("lqnPragmas", "");

		
		defaultAttr.put("accuracyQualityAnnotationFile", "");
		defaultAttr.put("blocks", "");
		defaultAttr.put("clear", true);
		defaultAttr.put("maxDomain", 256);
		defaultAttr.put("outpath", "de.uka.ipd.sdq.temporary");
		defaultAttr.put("psQuantum", "0.001");
		defaultAttr.put("runTime", "");
		defaultAttr.put("samplingDist", "1.0");
		defaultAttr.put("simulateAccuracy", false);
	}

	@Override
	public Map<String, Object> copyValuesTo(Map<String, Object> attr) {
		attr.putAll(defaultAttr);
		attr.put("lqnsOutputDir", this.lqnsOutputDir);//
		return attr;	}

	@Override
	public boolean validate() {
		return validatePath(lqnsOutputDir);
	}

	public String getLqnsOutputDir() {
		return lqnsOutputDir;
	}

	public void setLqnsOutputDir(String lqnsOutputDir) {
		this.lqnsOutputDir = lqnsOutputDir;
	}

}
