package com.meicai.util.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by qzf on 2015/2/9.
 */

public class ThreadManager {

	/**
	 * 用来请求网络
	 */
	private static ThreadPoolExecutor longExecutor;
	private static ThreadPoolExecutor shortExecutor;
	/**
	 * 线程池的锁
	 */
	private static Object longLock = new Object();
	private static Object shortLock = new Object();

	/**
	 * 执行一个长的任务
	 *
	 * @param runnable
	 */
	public static void executeLong(Runnable runnable) {
		synchronized (longLock) {
			if (longExecutor == null) {
				ThreadPoolProxy proxy = new ThreadPoolProxy(5, 5, 100);
				longExecutor = proxy.createPool();
			}
		}
		longExecutor.execute(runnable);
	}

	/**
	 * 取消长的任务
	 *
	 * @param runnable
	 */
	public static void cancelLong(Runnable runnable) {
		if (longExecutor == null || longExecutor.isShutdown() || longExecutor.isTerminated()) {
			return;
		} else {
			longExecutor.getQueue().remove(runnable);
		}
	}

	public static void executeShort(Runnable runnable) {
		synchronized (shortLock) {
			if (shortExecutor == null) {
				ThreadPoolProxy proxy = new ThreadPoolProxy(3, 3, 5);
				shortExecutor = proxy.createPool();
			}
		}
		shortExecutor.execute(runnable);
	}

	/**
	 * 线程池的代理对象
	 *
	 */
	private static class ThreadPoolProxy {
		private int coreNum;
		private int maxNum;
		private long keepTime;
		private ThreadPoolExecutor pool;

		public ThreadPoolProxy(int coreNum, int maxNum, long keepTime) {
			this.coreNum = coreNum;
			this.maxNum = maxNum;
			this.keepTime = keepTime;
		}

		public ThreadPoolExecutor createPool() {
			if (pool == null) {
				/*
				 * 1 线程池的任务数量
				 * 2 如果队列放满了 额外创建的线程
				 * 3 没有任务的还能活多久
				 * 4 存活时间的单位
				 * 5 线程池满了队列   可以指定上限
				 * 6 创建线程池的工厂
				 * 7 处理异常的Handler (固定写法 )
				 *
				 */
				pool = new ThreadPoolExecutor(coreNum, maxNum, keepTime,
						TimeUnit.MICROSECONDS,
						new LinkedBlockingQueue<Runnable>(10),
						Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
			}
			return pool;
		}
	}
}
