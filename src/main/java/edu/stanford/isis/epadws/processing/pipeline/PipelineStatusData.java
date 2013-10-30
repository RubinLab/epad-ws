/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.pipeline;

public class PipelineStatusData
{
	private static PipelineStatusData ourInstance = new PipelineStatusData();

	public static PipelineStatusData getInstance()
	{
		return ourInstance;
	}

	private PipelineStatusData()
	{
	}

}
