package io.github.squat_team.performance.peropteryx.configuration;

import java.util.Map;

/**
 * Config parameters to locate certain PCM models.
 */
public class PcmInstanceConfig extends AbstractConfig {
    private String usageModel = "";
    private String alloationModel = "";
    
    public PcmInstanceConfig() {
        super();
    }

	@Override
	public void initializeDefault() {
		// nothing
	}

	@Override
	public Map<String, Object> copyValuesTo(Map<String, Object> attr) {
		attr.put("allocationFile", this.getAllocationModel());
		attr.put("usageFile", this.getUsageModel());
		return attr;
	}
    
    public String getUsageModel() {
        return this.usageModel;
    }

    public void setUsageModel(final String usageModel) {
        this.usageModel = usageModel;
    }

    public String getAllocationModel() {
        return this.alloationModel;
    }

    public void setAllocationModel(final String allocationModel) {
        this.alloationModel = allocationModel;
    }

	@Override
	public boolean validate() {
		return validatePath(alloationModel) && validatePath(usageModel);
	}
}
