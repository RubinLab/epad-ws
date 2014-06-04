/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.epad.epadws.processing.pipeline;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.processing.pipeline.process.UnzipProcess;
import edu.stanford.epad.epadws.processing.pipeline.watcher.EPADUploadZIPWatcher;
import edu.stanford.epad.epadws.processing.pipeline.watcher.PipelineStatusWatcher;

/**
 * A singleton class to get queues and objects needed by pipeline classes.
 */
public class PipelineFactory
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final BlockingQueue<File> unzipQueue = new ArrayBlockingQueue<File>(500);
	private static final BlockingQueue<File> taggerQueue = new ArrayBlockingQueue<File>(500);
	private static final BlockingQueue<File> moverQueue = new ArrayBlockingQueue<File>(500);
	private static final BlockingQueue<File> orderQueue = new ArrayBlockingQueue<File>(500);
	private static final BlockingQueue<File> thumbnailQueue = new ArrayBlockingQueue<File>(500);

	private final ExecutorService uploadDirWatchExec = Executors.newSingleThreadExecutor();
	private final ScheduledExecutorService pipelineStatusWatchExec = Executors.newScheduledThreadPool(1);
	private final ExecutorService unzipService = Executors.newFixedThreadPool(10);
	private final ExecutorService processService = Executors.newFixedThreadPool(5);
	private final ScheduledExecutorService statusCheckService = Executors.newScheduledThreadPool(1);

	/**
	 * There is only one pipeline service, and this contains a reference to the pipeline service.
	 */
	private static final PipelineFactory ourInstance = new PipelineFactory();

	private final EPADUploadZIPWatcher uploadDirWatcher;
	private final UnzipProcess unzipProcess;

	public static PipelineFactory getInstance()
	{
		return ourInstance;
	}

	private PipelineFactory()
	{
		uploadDirWatcher = new EPADUploadZIPWatcher(unzipQueue);
		unzipProcess = new UnzipProcess(unzipService, unzipQueue, taggerQueue);

		statusCheckService.scheduleAtFixedRate(new Runnable() {
			private String lastQueueSize = "00000000000";
			private long lastUpdate = -1;

			@Override
			public void run()
			{
				if (queueSizeHasChanged() || updateTimeExceeded()) {
					lastUpdate = System.currentTimeMillis();
					printPipelineStats();
				}
			}

			/**
			 * True if the queue have changed. This is done by building a string.
			 * 
			 * @return boolean true if a queue has changed.
			 */
			private boolean queueSizeHasChanged()
			{
				StringBuilder sb = new StringBuilder();
				sb.append(unzipQueue.size());
				sb.append(taggerQueue.size());
				sb.append(moverQueue.size());
				sb.append(orderQueue.size());
				sb.append(thumbnailQueue.size());
				sb.append(unzipProcess.getTasksInProgressCount());
				sb.append(uploadDirWatcher.getErrorFileCount());

				String currQueue = sb.toString();

				if (!currQueue.equals(lastQueueSize)) {
					lastQueueSize = currQueue;
					return true;
				}
				return false;
			}

			private boolean updateTimeExceeded()
			{
				return ((System.currentTimeMillis() - lastUpdate) > (1000 * 60));
			}
		}, 5, 1, TimeUnit.SECONDS);

		pipelineStatusWatchExec.scheduleAtFixedRate(new PipelineStatusWatcher(), 5, 5, TimeUnit.SECONDS);
	}

	/**
	 * Build the pipeline and start the processes.
	 */
	public void buildAndStart()
	{
		processService.submit(unzipProcess);
		uploadDirWatchExec.execute(uploadDirWatcher);
	}

	/**
	 * Shut down the pipeline service
	 */
	public void shutdown()
	{
		pipelineStatusWatchExec.shutdown();
		statusCheckService.shutdown();
		uploadDirWatchExec.shutdown();
	}

	public void printPipelineStats()
	{
		log.info(getPipelineStats());
	}

	public String getPipelineStats()
	{
		StringBuilder sb = new StringBuilder("Pipeline Stats: ");
		sb.append("Queue sizes - ");
		sb.append(" quz=").append(unzipQueue.size());
		sb.append(" qtg=").append(taggerQueue.size());
		sb.append(" qmv=").append(moverQueue.size());
		sb.append(" qor=").append(orderQueue.size());
		sb.append(" qth=").append(thumbnailQueue.size());
		sb.append(" Process map sizes - ");
		sb.append(" tuz=").append(unzipProcess.getTasksInProgressCount());
		sb.append("; error file count=").append(uploadDirWatcher.getErrorFileCount());

		return sb.toString();
	}

	/**
	 * Just list the total number of files in the
	 * 
	 * @return int
	 */
	public int getActivityLevel()
	{
		int itemsInQueue = 0;
		itemsInQueue += unzipQueue.size();
		itemsInQueue += taggerQueue.size();
		itemsInQueue += moverQueue.size();
		itemsInQueue += orderQueue.size();
		itemsInQueue += thumbnailQueue.size();
		itemsInQueue += unzipProcess.getTasksInProgressCount();

		return itemsInQueue;
	}

	/**
	 * Lists the total number of files that error-ed out.
	 * 
	 * @return int
	 */
	public int getErrorFileCount()
	{
		return uploadDirWatcher.getErrorFileCount();
	}
}
