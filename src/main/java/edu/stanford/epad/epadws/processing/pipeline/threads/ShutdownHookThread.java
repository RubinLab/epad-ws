/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.epad.epadws.processing.pipeline.threads;

import java.util.concurrent.atomic.AtomicBoolean;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.processing.pipeline.PipelineFactory;

/**
 * Run this code when shutting down the application.
 */
public class ShutdownHookThread extends Thread
{
	private static final EPADLogger logger = EPADLogger.getInstance();
	private static final AtomicBoolean hasRun = new AtomicBoolean(false);

	/**
	 * This is the shutdown thread.
	 */
	@Override
	public void run()
	{
		synchronized (hasRun) {
			logger.info("Shutdown hook called.");

			if (hasRun.get() == true) {
				return;
			}
			hasRun.set(true);

			ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
			shutdownSignal.shutdownNow();

			PipelineFactory.getInstance().shutdown();
			EpadDatabase.getInstance().shutdown();
		}
	}

	public void shutdown()
	{
		run();
	}
}
