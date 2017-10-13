package io.github.squat_team.model;

import org.json.JSONObject;

public class RestArchitecture {

    /** The name of the architecture */
    private final String name;

    /** The architecture */
    private final JSONObject architectureInstance;

    /** The cost */
    private final JSONObject cost;

    /** The insintenr */
    private final JSONObject insinter;

    /** The splitRespn */
    private final JSONObject splitrespn;

    /** The Wrapper */
    private final JSONObject wrapper;

    /**
     * Create a new RestScenarioResult without unwrapping the result for faster
     * forwarding
     *
     * @param name the name of the architecture
     * @param architectureInstance the architecture
     */
    public RestArchitecture(String name, JSONObject architectureInstance) {
        this(name, architectureInstance, null, null, null, null);
    }

    /**
     * Create a new RestScenarioResult without unwrapping the result for faster
     * forwarding
     *
     * @param name the name of the architecture
     * @param architectureInstance the architecture
     * @param cost the cost file
     * @param insinter the insinter file
     * @param splitrespn the splitrespn file
     * @param wrapper the wrapper file
     */
    public RestArchitecture(String name, JSONObject architectureInstance, 
            JSONObject cost, JSONObject insinter, JSONObject splitrespn, 
            JSONObject wrapper) {
        this.name = name;
        this.architectureInstance = architectureInstance;
        this.cost = cost;
        this.insinter = insinter;
        this.splitrespn = splitrespn;
        this.wrapper = wrapper;
    }

    /**
     * @return the name of the architecture
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the architecture
     */
    public JSONObject getRestArchitecture() {
        return this.architectureInstance;
    }

    /**
     * @return the cost file
     */
    public JSONObject getCost() {
        return this.cost;
    }

    /**
     * @return the insinter file
     */
    public JSONObject getInsinter() {
        return this.insinter;
    }

    /**
     * @return the splitrespn file
     */
    public JSONObject getSplitrespn() {
        return this.splitrespn;
    }

    /**
     * @return the wrapper file
     */
    public JSONObject getWrapper() {
        return this.wrapper;
    }
}