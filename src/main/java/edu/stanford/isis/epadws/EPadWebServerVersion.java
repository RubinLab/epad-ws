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

	public static String getVersion()
	{
		return "1.2";
	}

	public static String getBuildDate()
	{
		return "September 2013"; // TODO Make part of build process.
	}
}
