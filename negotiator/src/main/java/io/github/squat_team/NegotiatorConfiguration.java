package io.github.squat_team;

public class NegotiatorConfiguration {
	// TODO: PA! Set correct paths to model.
	public static final String INITIAL_ARCHITECTURE_NAME = "default"; // cocome-cloud
	public static final String INITIAL_ARCHITECTURE_PATH = "/home/pcm"; // models/cocomeWithoutPickUpStoreAndServiceAdapter
	public static final String INITIAL_ARCHITECTURE_ALTERNATIVE_REPOSITORY_FULL_PATH = "/home/model/alternativeRepository.repository"; // /Users/santiagovidal/Documents/Programacion/kamp-test/squat-tool/models/cocomeWithoutPickUpStoreAndServiceAdapter/alternativescocome-cloud.repository

	private static boolean RUN_SEQUENTIAL = false;
	private static boolean AUTO_ACCEPT = true;

	/**
	 * Checks whether the bots should work sequential.
	 * 
	 * @return true if bots should be executed in sequence. False if they should be
	 *         able to run parallel.
	 */
	public static boolean sequential() {
		return RUN_SEQUENTIAL;
	}

	/**
	 * Checks whether the negotiator should accept the first agreement.
	 * 
	 * @return true if negotiator should accept first agreement without user
	 *         interaction.
	 */
	public static boolean autoAccept() {
		return AUTO_ACCEPT;
	}

	/**
	 * This should not be done during a run...
	 * 
	 * @param runSequential
	 *            true if bots should be executed in sequence. False if they should
	 *            be able to run parallel.
	 */
	public static void setSequential(boolean runSequential) {
		RUN_SEQUENTIAL = runSequential;
	}

	/**
	 * This should not be done during a run...
	 * 
	 * @param autoAccept
	 *            true if negotiator should accept first agreement without user
	 *            interaction.
	 */
	public static void setAutoAccept(boolean autoAccept) {
		RUN_SEQUENTIAL = AUTO_ACCEPT;
	}
}
