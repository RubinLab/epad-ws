/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.pipeline.threads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import edu.stanford.isis.epad.common.dicom.DicomSeriesUID;
import edu.stanford.isis.epad.common.dicom.DicomStudyUID;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.processing.pipeline.PipelineFactory;

/**
 * Keeps a detailed list of the current work queues and the status of the /resources/dicom directory.
 * 
 * List of queues. a) Thumbnail creation b) Data download c) ResourceWatcher
 * 
 * @author amsnyder
 */
public class ProxyWorkers
{
	private static ProxyWorkers ourInstance = new ProxyWorkers();
	private static EPADLogger logger = EPADLogger.getInstance();

	private final BlockingQueue<DicomSeriesUID> thumbnailQueue = new LinkedBlockingQueue<DicomSeriesUID>();
	private final Executor thumbnailExec = Executors.newFixedThreadPool(1);
	private final PipelineFactory pipelineFactory;

	private Runnable thumbnailProcess;

	public static ProxyWorkers getInstance()
	{
		return ourInstance;
	}

	private ProxyWorkers()
	{
		thumbnailProcess = new ThumbnailGeneratorThread(thumbnailQueue);
		thumbnailExec.execute(thumbnailProcess);
		pipelineFactory = PipelineFactory.getInstance();
		pipelineFactory.buildAndStart();
	}

	/**
   *
   */
	@SuppressWarnings("unused")
	private void startThreads()
	{
		thumbnailProcess = new ThumbnailGeneratorThread(thumbnailQueue);
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
