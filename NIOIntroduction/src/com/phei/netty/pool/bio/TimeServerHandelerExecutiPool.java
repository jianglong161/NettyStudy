package com.phei.netty.pool.bio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Î±Òì²½µÄTimeServerHandelerExecutiPool
 * @author Still2Almost
 *
 */
public class TimeServerHandelerExecutiPool {
	private ExecutorService executor;
	public TimeServerHandelerExecutiPool(int maxPoolSize, int queueSize) {
		executor = new ThreadPoolExecutor(
				Runtime.getRuntime().availableProcessors(),
				maxPoolSize,
				120L,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(queueSize));
		
	}
	public void execute(Runnable task){
		executor.execute(task);
	}
}
