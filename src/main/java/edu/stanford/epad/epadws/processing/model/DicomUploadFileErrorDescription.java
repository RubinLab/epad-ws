/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.epad.epadws.processing.model;

import edu.stanford.epad.common.util.FileKey;

public class DicomUploadFileErrorDescription
{
	private final FileKey fileKey;
	private final String errorMessage;
	private final Exception exception;

	public DicomUploadFileErrorDescription(FileKey fileKey, String errorMessage, Exception e)
	{
		this.fileKey = fileKey;
		this.errorMessage = errorMessage;
		this.exception = e;
	}

	public FileKey getFileKey()
	{
		return fileKey;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public Exception getException()
	{
		return exception;
	}
}
