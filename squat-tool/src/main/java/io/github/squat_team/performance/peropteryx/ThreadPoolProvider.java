package io.github.squat_team.performance.peropteryx;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolProvider {
	public static final ExecutorService POOL = Executors.newFixedThreadPool(4);
	public static final ExecutorService BOT_POOL = Executors.newFixedThreadPool(32);
}
