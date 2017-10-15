package io.github.squat_team;

import java.util.HashMap;
import java.util.Map;

/**
 * Controls the time measurements for experiments. The total execution time, the
 * negotiation time and the time for each bot is measured.
 */
public class TimeMeasurements {
	// the measured times
	private static long totalTime = 0;
	private static long negotiationTime = 0;
	private static Map<String, Long> timeOfBots = new HashMap<>();

	// intermediate results
	private static long startedTotalTime = 0;
	private static long startedNegotiationTime = 0;

	/**
	 * Starts the measurement for the total execution time. Expects a
	 * {@link #endTotalTimeMeasurement()} before it can be called again.
	 */
	public static void startTotalTimeMeasurement() {
		startedTotalTime = System.currentTimeMillis();
	}

	/**
	 * Ends the measurement for the total execution time.
	 */
	public static void endTotalTimeMeasurement() {
		totalTime = System.currentTimeMillis() - startedTotalTime;
		startedTotalTime = 0;
	}

	/**
	 * Starts/Continues the negotiation time measurement. If it is called first, the
	 * measurement is started. Expects a {@link #pauseNegotiationTimeMeasurement()}
	 * before it can be called again.
	 */
	public static void continueNegotiationTimeMeasurement() {
		startedNegotiationTime = System.currentTimeMillis();
	}

	/**
	 * Pauses/Ends the negotiation time measurement.
	 */
	public static void pauseNegotiationTimeMeasurement() {
		long timePart = System.currentTimeMillis() - startedNegotiationTime;
		startedNegotiationTime = 0;
		negotiationTime += timePart;
	}

	/**
	 * Gets the total execution time.
	 * 
	 * @return total execution time
	 */
	public static long getTotalTime() {
		return totalTime;
	}

	/**
	 * Gets the negotiation time.
	 * 
	 * @return negotiation time.
	 */
	public static long getNegotiationTime() {
		return negotiationTime;
	}

	/**
	 * Gets the bot times.
	 * 
	 * @return a list of all bot names and their associated time.
	 */
	public static Map<String, Long> getBotTimes() {
		return timeOfBots;
	}

	/**
	 * Adds the time for a bot.
	 * 
	 * @param botName
	 *            the name of the bot.
	 * @param time
	 *            the time that should be added for this bot.
	 */
	public static synchronized void addTimeForBot(String botName, long time) {
		Long previousTime = timeOfBots.get(botName);
		if (previousTime == null) {
			timeOfBots.put(botName, time);
		} else {
			timeOfBots.put(botName, previousTime + time);
		}
	}

	/**
	 * Resets the measurements.
	 */
	public static void reset() {
		totalTime = 0;
		negotiationTime = 0;
		startedTotalTime = 0;
		startedNegotiationTime = 0;
		timeOfBots.clear();
	}

	/**
	 * Prints the measured times.
	 */
	public static void printTimes() {
		System.out.println("TIME MEASUREMENTS:");
		System.out.println("Total Runtime: " + totalTime);
		if (NegotiatorConfiguration.sequential()) {
			Long sequentialTime = negotiationTime;
			System.out.println("Total Negotiatior Runtime: " + negotiationTime);
			for (String botName : timeOfBots.keySet()) {
				long botTime = timeOfBots.get(botName);
				System.out.println("Total Bot " + botName + " Runtime: " + timeOfBots.get(botName));
				sequentialTime += botTime;
			}
			double withoutRestRatio = Double.valueOf(sequentialTime) / Double.valueOf(totalTime);
			System.out.println("Total Sequential Runtime Without REST: " + sequentialTime + " ("
					+ (100 * withoutRestRatio) + "%)");
		}
	}
}
