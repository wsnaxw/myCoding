package com.martiiin.threadPool;

import java.util.List;

/**
 * 自定义的线程池实现
 * @author MarTiiin
 *
 */
public interface MyThreadPool {
	/**
	 * 执行多线程任务
	 * @param task
	 */
	void excute(Runnable task);
	void excute(Runnable[] tasks);
	void excute(List<Runnable> tasks);
	/**
	 * 获取已完成任务数量
	 * @return
	 */
	int getFinishedTaskNum();
	
	/**
	 * 获取任务数量
	 * @return
	 */
	int getThreadTaskNum();
	/**
	 * 获取线程数量
	 * @return
	 */
	int getThreadWorkerNum();
	/**
	 * 销毁线程池
	 */
	void destroy();

}
