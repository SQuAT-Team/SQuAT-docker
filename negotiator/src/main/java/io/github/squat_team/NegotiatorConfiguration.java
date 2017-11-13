package io.github.squat_team;

public class NegotiatorConfiguration {
	// TODO: PA! Set correct paths to model.
	public static final String INITIAL_ARCHITECTURE_NAME = "default"; // cocome-cloud
	public static final String INITIAL_ARCHITECTURE_PATH = "/home/pcm"; // models/cocomeWithoutPickUpStoreAndServiceAdapter
	public static final String INITIAL_ARCHITECTURE_ALTERNATIVE_REPOSITORY_FULL_PATH = "/home/model/alternativeRepository.repository"; // /Users/santiagovidal/Documents/Programacion/kamp-test/squat-tool/models/cocomeWithoutPickUpStoreAndServiceAdapter/alternativescocome-cloud.repository

	private static float FAILURE_RESPONSE_VALUE = 9999f;
	private static boolean RUN_SEQUENTIAL = true;
	private static boolean AUTO_ACCEPT = true;
	private static int AUTO_ACCEPT_LEVEL = 1;

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
	 * The level of the analysis that should be reached before agreement gets auto
	 * accepted.
	 * 
	 * @return the level.
	 */
	public static int autoAcceptLevel() {
		return AUTO_ACCEPT_LEVEL;
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
		AUTO_ACCEPT = autoAccept;
	}

	/**
	 * This should not be done during a run...
	 * 
	 * @param level
	 *            level that should be reached before agreement is auto accepted.
	 */
	public static void setAutoAcceptLevel(int level) {
		AUTO_ACCEPT_LEVEL = level;
	}

	/**
	 * Get the value that should be set, if the analysis fails.
	 * 
	 * @return the value.
	 */
	public static float getFailureResponseValue() {
		return FAILURE_RESPONSE_VALUE;
	}

	/**
	 * This should not be done during a run...
	 * 
	 * @param value
	 *            The value that should be set, if a analysis failed.
	 */
	public static void setFailureResponseValue(float value) {
		FAILURE_RESPONSE_VALUE = value;
	}

	public static void printConfig() {
		System.out.println("NEGOTIATOR CONFIGURATION:");
		System.out.println("Run Sequential: " + RUN_SEQUENTIAL);
		System.out.println("Auto Accept: " + AUTO_ACCEPT);
		System.out.println("Auto Accept Level: " + AUTO_ACCEPT_LEVEL);

	}
}
