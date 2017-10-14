package test;

import java.util.Objects;

import io.github.squat_team.model.PCMArchitectureInstance;
import io.github.squat_team.model.RestArchitecture;
import io.github.squat_team.modifiability.kamp.KAMPPCMBot;

public class ExecutionContext {

    /** The bot */
    private final KAMPPCMBot bot;

    /** The architecture to analyze */
    private final PCMArchitectureInstance architectureInstance;

    /** */
    private final RestArchitecture restArchitecture;

    /** 
     *
     * @param bot
     * @param architectureInstance
     * @param restArchitecture
     */
    public ExecutionContext(KAMPPCMBot bot, 
            PCMArchitectureInstance architectureInstance,
            RestArchitecture restArchitecture) {
        this.bot = Objects.requireNonNull(bot);
        this.architectureInstance = Objects.requireNonNull(architectureInstance);
        this.restArchitecture = restArchitecture;
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

    /**
     * @return the rest architecture
     */
    public RestArchitecture getRestArchitecture() {
        return this.restArchitecture;
    }
}
