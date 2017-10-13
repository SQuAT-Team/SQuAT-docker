package io.github.squat_team.model;

import org.json.JSONObject;

public class RestScenarioResult extends PCMScenarioResult {

    /** */
    private final RestArchitecture architecture;

    /**
     * Create a new RestScenarioResult without unwrapping the result for faster
     * forwarding
     *
     * @param name the name of the architecture
     * @param architectureInstance the architecture
     * @param result the result
     */
    public RestScenarioResult(String name, JSONObject architectureInstance, PCMResult result) {
        this(name, architectureInstance, result, null, null, null, null);
    }

    /**
     * Create a new RestScenarioResult without unwrapping the result for faster
     * forwarding
     *
     * @param name the name of the architecture
     * @param architectureInstance the architecture
     * @param result the result
     * @param cost the cost file
     * @param insinter the insinter file
     * @param splitrespn the splitrespn file
     * @param wrapper the wrapper file
     */
    public RestScenarioResult(String name, JSONObject architectureInstance, 
            PCMResult result, JSONObject cost, JSONObject insinter, 
            JSONObject splitrespn, JSONObject wrapper) {
        super(null);
        this.setResult(result);
        this.architecture = new RestArchitecture(name, architectureInstance, 
            cost, insinter, splitrespn, wrapper);
    }

    /**
     * @return the architecture
     */
    public JSONObject getRestArchitecture() {
        return this.architecture.getRestArchitecture();
    }

    /**
     * @return the cost file
     */
    public JSONObject getCost() {
        return this.architecture.getCost();
    }

    /**
     * @return the insinter file
     */
    public JSONObject getInsinter() {
        return this.architecture.getInsinter();
    }

    /**
     * @return the splitrespn file
     */
    public JSONObject getSplitrespn() {
        return this.architecture.getSplitrespn();
    }

    /**
     * @return the wrapper file
     */
    public JSONObject getWrapper() {
        return this.architecture.getWrapper();
    }

    /**
     * @return the architecture
     */
    public RestArchitecture getArchitecture() {
        return this.architecture;
    }
}
