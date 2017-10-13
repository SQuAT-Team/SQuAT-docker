package io.github.squat_team.model;

import org.json.JSONObject;

import io.github.squat_team.agentsUtils.BotManager.BotType;

public class RestScenarioResult extends PCMScenarioResult {

    /** The resulting architecture */
    private final RestArchitecture architecture;

    /** The type of the bot that created this result */
    private final BotType originatingBotType;

    /**
     * Create a new RestScenarioResult without unwrapping the result for faster
     * forwarding
     *
     * @param botType the type of bot that created this result
     * @param name the name of the architecture
     * @param architectureInstance the architecture
     * @param result the result
     */
    public RestScenarioResult(BotType botType, String name, 
            JSONObject architectureInstance, PCMResult result) {
        this(botType, name, architectureInstance, result, null, null, null, null);
    }

    /**
     * Create a new RestScenarioResult without unwrapping the result for faster
     * forwarding
     *
     * @param botType the type of bot that created this result
     * @param name the name of the architecture
     * @param architectureInstance the architecture
     * @param result the result
     * @param cost the cost file
     * @param insinter the insinter file
     * @param splitrespn the splitrespn file
     * @param wrapper the wrapper file
     */
    public RestScenarioResult(BotType botType, String name, 
            JSONObject architectureInstance, PCMResult result, JSONObject cost, 
            JSONObject insinter, JSONObject splitrespn, JSONObject wrapper) {
        super(null);
        this.setResult(result);
        this.originatingBotType = Objects.requireNonNull(botType);
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

    /**
     * @return the originating bot type
     */
    public BotType getOrigininatingBotType() {
        return this.originatingBotType;
    }
}
