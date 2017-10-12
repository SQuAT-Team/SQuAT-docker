package test;

import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.performance.peropteryx.PerOpteryxPCMBot;

public class ExecutionContext {

    /** The bot */
    private final PerOpteryxPCMBot bot;

    /** The architecture to analyze */
    private final PCMArchitectureInstance architectureInstance;

    /**
     *
     * @param bot
     * @param architectureInstance
     */
    public ExecutionContext(PerOpteryxPCMBot bot, 
            PCMArchitectureInstance architectureInstance) {
        this.bot = bot;
        this.architectureInstance = architectureInstance;
    }

    /**
     * @return the scenario
     */
    public PerOpteryxPCMBot getBot() {
        return this.bot;
    }

    /**
     * @return the architecture instance
     */
    public PCMArchitectureInstance getArchitectureInstance() {
        return this.architectureInstance;
    }
}
