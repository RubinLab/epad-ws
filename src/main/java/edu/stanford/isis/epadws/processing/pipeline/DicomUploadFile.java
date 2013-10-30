/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.pipeline;

import java.io.File;

import edu.stanford.isis.epad.common.util.FileKey;

/**
 * Represents a new file detected.
 * 
 * @author amsnyder
 */
public class DicomUploadFile
{
	private final FileKey key;
	private final String filePath;
	private final long lastUpdated;
	private final long size;
	private final long timestamp;

	public DicomUploadFile(File file)
	{
		key = new FileKey(file);

		filePath = file.getAbsolutePath();
		lastUpdated = file.lastModified();
		size = file.length();

		this.timestamp = System.currentTimeMillis();
	}

	public String getFilePath()
	{
		return filePath;
	}

	public FileKey getKey()
	{
		return key;
	}

	/**
	 * The time this file was checked.
	 * 
	 * @return
	 */
	public long getTimestamp()
	{
		return timestamp;
	}

	public long getSize()
	{
		return size;
	}

	public long getLastUpdated()
	{
		return lastUpdated;
	}
}
