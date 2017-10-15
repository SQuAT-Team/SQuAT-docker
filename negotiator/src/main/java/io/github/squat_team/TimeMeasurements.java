package io.github.squat_team;

import java.util.HashMap;
import java.util.Map;

public class TimeMeasurements {
	private static long totalTime = 0;
	private static long negotiationTime = 0;
	private static Map<String, Long> timeOfBot = new HashMap<>();

	private static long startedTotalTime = 0;
	private static long startedNegotiationTime = 0;

	public static void startTotalTimeMeasurement() {
		startedTotalTime = System.currentTimeMillis();
	}

	public static void endTotalTimeMeasurement() {
		totalTime = System.currentTimeMillis() - startedTotalTime;
	}

	public static void continueNegotiationTimeMeasurement() {
		startedNegotiationTime = System.currentTimeMillis();
	}

	public static void pauseNegotiationTimeMeasurement() {
		long timePart = System.currentTimeMillis() - startedNegotiationTime;
		startedNegotiationTime = 0;
		negotiationTime += timePart;
	}

	public static long getTotalTime() {
		return totalTime;
	}

	public static long getNegotiationTime() {
		return negotiationTime;
	}
}
