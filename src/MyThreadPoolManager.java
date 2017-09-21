package com.martiiin.threadPool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MyThreadPoolManager implements MyThreadPool {

	private static int workerNumber = 5;

	private Worker[] worker;

	private static volatile int taskNums = 0;

	private static List<Runnable> taskQueue = new LinkedList<Runnable>();

	private AtomicLong atom = new AtomicLong();

	private MyThreadPoolManager() {
		this(workerNumber);
	}

	private MyThreadPoolManager(int workerNumber) {
		if (workerNumber > 0) {
			MyThreadPoolManager.workerNumber = workerNumber;
		}

		this.worker = new Worker[MyThreadPoolManager.workerNumber];
		for (int i = 0; i < MyThreadPoolManager.workerNumber; i++) {
			worker[i] = new Worker();
			Thread r = new Thread(worker[i], "worker" + atom.incrementAndGet());
			r.start();
		}
	}

	public static MyThreadPool instanceMyThreadPool() {
		return new MyThreadPoolManager();
	}

	public static MyThreadPool instanceMyThreadPool(int poolSize) {
		return new MyThreadPoolManager(poolSize);
	}

	@Override
	public void excute(Runnable task) {
		synchronized (this) {
			taskQueue.add(task);
			this.notifyAll();
		}

	}

	@Override
	public void excute(Runnable[] tasks) {
		synchronized (this) {
			for (Runnable task : tasks) {
				taskQueue.add(task);
			}
			this.notifyAll();
		}
	}

	@Override
	public void excute(List<Runnable> tasks) {
		synchronized (this) {
			for (Runnable task : tasks) {
				taskQueue.add(task);
			}
			this.notifyAll();
		}
	}

	@Override
	public int getThreadTaskNum() {
		return taskQueue.size();
	}

	@Override
	public int getThreadWorkerNum() {
		// TODO Auto-generated method stub
		return worker != null ? worker.length : 0;
	}
	
	@Override
	public int getFinishedTaskNum() {
		// TODO Auto-generated method stub
		return taskNums;
	}

	@Override
	public void destroy() {
		while (taskQueue.size() > 0) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (Worker a : worker) {
			a.stopWork();
			a = null;
		}
		worker = null;
	}

	

	private class Worker implements Runnable {

		private boolean isWork = true;

		@Override
		public void run() {
			Runnable r = null;
			while (isWork) {
				synchronized (taskQueue) {
					while (isWork && taskQueue.isEmpty()) {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (!taskQueue.isEmpty()) {
						r = taskQueue.remove(0);
						r.run();
						taskNums++;
						r = null;
					}
				}

			}
		}

		private void stopWork() {
			isWork = false;
		}

	}
}
