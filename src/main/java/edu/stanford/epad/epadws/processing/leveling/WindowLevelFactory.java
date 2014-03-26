/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.epad.epadws.processing.leveling;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.common.dicom.WindowLevelManager;
import edu.stanford.epad.common.util.EPADLogger;

public class WindowLevelFactory
{
	private static final EPADLogger log = EPADLogger.getInstance();

	/**
	 * This is a different queueing service from the upload process.
	 */
	private static final BlockingQueue<File> windowLevelQueue = new ArrayBlockingQueue<File>(5000);

	private final ExecutorService windowLevelService = Executors.newFixedThreadPool(20);
	private final JPEGLevelingProcess jpegLevelingProcess;
	private final ExecutorService processService = Executors.newFixedThreadPool(1);
	private final ScheduledExecutorService statusCheckService = Executors.newScheduledThreadPool(1);

	private String lastQueueSize = "";
	private long lastUpdate = -1;

	private static final WindowLevelManager windowLevelManager = WindowLevelManager.getInstance();
	private static final WindowLevelFactory ourInstance = new WindowLevelFactory();

	public static WindowLevelFactory getInstance()
	{
		return ourInstance;
	}

	private WindowLevelFactory()
	{ // The process has an input queue but doesn't have an output queue.
		jpegLevelingProcess = new JPEGLevelingProcess(windowLevelService, windowLevelQueue, null);

		statusCheckService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run()
			{
				printPipelineStats();
			}// run
		}, 5, 1, TimeUnit.SECONDS);
	}

	public void setLevel(int windowWidth, int windowLevel, String key)
	{
		windowLevelManager.updateSetting(windowWidth, windowLevel, key); // we are ignoring key for now.
	}

	/**
	 * 
	 * @param file File
	 * @param seriesToLevel String
	 */
	public void submitDicomFile(File file, String seriesToLevel) throws InterruptedException
	{
		windowLevelQueue.offer(file, 200, TimeUnit.MILLISECONDS);
	}

	public void buildAndStart()
	{
		log.info("Starting on-the-fly server-side window level queues.");

		processService.submit(jpegLevelingProcess);
	}

	public void shutdown()
	{
		log.info("Stopping on-the-fly server-side window level queues.");

		processService.shutdown();
		statusCheckService.shutdown();
	}

	/**
	 * Just for printing status queue.
	 */
	public void printPipelineStats()
	{
		if (queueSizeHasChanged() || updateTimeExceeded()) {
			lastUpdate = System.currentTimeMillis();
		}
	}

	/**
	 * Just for printing status queue.
	 * 
	 * @return boolean
	 */
	private boolean queueSizeHasChanged()
	{
		String currQueueSize = "" + windowLevelQueue.size();
		if (!lastQueueSize.equals(currQueueSize)) {
			lastQueueSize = currQueueSize;
			return true;
		}
		return false;
	}

	private boolean updateTimeExceeded()
	{
		return ((System.currentTimeMillis() - lastUpdate) > (1000 * 60));
	}
}
