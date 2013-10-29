/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Signal a shutdown event.
 * 
 * @author amsnyder
 */
public class ShutdownSignal
{
	private static ShutdownSignal ourInstance = new ShutdownSignal();

	private final AtomicBoolean isRunning;

	public static ShutdownSignal getInstance()
	{
		return ourInstance;
	}

	private ShutdownSignal()
	{
		isRunning = new AtomicBoolean(true);
	}

	/**
	 * Call when time to shutdown application.
	 */
	public void shutdownNow()
	{
		isRunning.set(false);
	}

	/**
	 * 
	 * @return boolean. True if application is running. False if shutting down.
	 */
	public boolean hasShutdown()
	{
		return !isRunning.get();
	}
}
