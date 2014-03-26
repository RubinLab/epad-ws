/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.epad.epadws.processing.pipeline.threads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import edu.stanford.epad.common.dicom.DicomSeriesUID;
import edu.stanford.epad.common.dicom.DicomStudyUID;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.processing.pipeline.PipelineFactory;

/**
 * Keeps a detailed list of the current work queues and the status of the /resources/dicom directory.
 * 
 * List of queues. a) Thumbnail creation b) Data download c) ResourceWatcher
 * 
 * @author amsnyder
 */
public class EPADWorkers
{
	private static EPADWorkers ourInstance = new EPADWorkers();
	private static EPADLogger logger = EPADLogger.getInstance();

	private final BlockingQueue<DicomSeriesUID> thumbnailQueue = new LinkedBlockingQueue<DicomSeriesUID>();
	private final PipelineFactory pipelineFactory;

	public static EPADWorkers getInstance()
	{
		return ourInstance;
	}

	private EPADWorkers()
	{
		pipelineFactory = PipelineFactory.getInstance();
		pipelineFactory.buildAndStart();
	}

	/**
	 * Add a studyId to a stack that indicates it should be loaded in the future.
	 * 
	 * @param studyId Dicom StudyUID that we might want to download
	 * @param remoteAddr IP address to identify who is interested
	 */
	public void showInterestInStudy(DicomStudyUID studyId, String remoteAddr)
	{
		logger.info("Show Interest In Study NOT IMPLEMENTED for: " + studyId + ", " + remoteAddr);
	}

	/**
	 * Add a seriesId to the thumbnail generator queue.
	 * 
	 * @param seriesUID
	 */
	public void createThumbnailForSeries(DicomSeriesUID seriesUID)
	{
		thumbnailQueue.offer(seriesUID);
	}
}
