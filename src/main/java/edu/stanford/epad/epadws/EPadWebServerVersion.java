/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.epad.epadws;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

public class EPadWebServerVersion
{
	static String version = null;
	static String buildDate = null;
	public EPadWebServerVersion()
	{
		if (version == null)
		{
			version = "1.5.1";
			buildDate = "";
			InputStream is = null;
			try {
				is = this.getClass().getClassLoader().getResourceAsStream("version.txt");
				Properties properties = new Properties();
				properties.load(is);
				version = properties.getProperty("version");
				buildDate = properties.getProperty("build.date");
			} catch (Exception x){
				
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
	}
	public String getVersion() {
		return version;
	}
	public String getBuildDate() {
		return buildDate;
	}
}
