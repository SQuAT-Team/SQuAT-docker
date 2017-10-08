package test;

import java.util.Objects;

import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.modifiability.kamp.KAMPPCMBot;

public class ExecutionContext {

    /** The bot */
    private final KAMPPCMBot bot;

    /** The architecture to analyze */
    private final PCMArchitectureInstance architectureInstance;

    /** 
     *
     * @param bot
     * @param architectureInstance
     */
    public ExecutionContext(KAMPPCMBot bot, 
            PCMArchitectureInstance architectureInstance) {
        this.bot = Objects.requireNonNull(bot);
        this.architectureInstance = Objects.requireNonNull(architectureInstance);
    }

    /**
     * @return the scenario
     */
    public KAMPPCMBot getBot() {
        return this.bot;
    }

    /**
     * @return the architecture instance
     */
    public PCMArchitectureInstance getArchitectureInstance() {
        return this.architectureInstance;
    }
}
