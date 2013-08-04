/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws;

public class EPadWebServerVersion
{
	private EPadWebServerVersion()
	{
	}

	@SuppressWarnings("unused")
	private static final String VERSION = "@(#)00.00.00@";

	@SuppressWarnings("unused")
	private static final String BUILD_DATE = "";// ToDo: create an Ant task for this.

	public static String getVersion()
	{
		return "1.2";
		// return VERSION;
	}

	public static String getBuildDate()
	{
		return "Nov 9, 2012 13:00"; // ToDo: make part of ANT build process.
		// return BUILD_DATE;
	}

}
