package io.github.squat_team.performance.peropteryx.start;

public class OptimizationInfo {
	private static int iterationCount = 0;
	
	public static void nextIteration(){
		iterationCount++;
	}
	
	public static int getIterations(){
		return iterationCount;
	}
}
