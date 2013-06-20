/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import edu.stanford.isis.epadws.common.DicomSeriesUID;
import edu.stanford.isis.epadws.common.DicomStudyUID;
import edu.stanford.isis.epadws.server.managers.pipeline.PipelineFactory;
import edu.stanford.isis.epadws.server.threads.ThumbnailGenerator;

/**
 * Keeps a detailed list of the current work queues and the
 * status of the /resources/dicom  directory.
 *
 * List of queues.
 * a) Thumbnail creation
 * b) Data download
 * c) ResourceWatcher
 *
 * @author amsnyder
 */
public class ProxyWorkers {
    private static ProxyWorkers ourInstance = new ProxyWorkers();

    private static ProxyLogger logger = ProxyLogger.getInstance();

    BlockingQueue<DicomSeriesUID> thumbnailQueue = new LinkedBlockingQueue<DicomSeriesUID>();
    Runnable thumbnailProcess;
    Executor thumbnailExec = Executors.newFixedThreadPool(1);

    PipelineFactory pipelineFactory;

    public static ProxyWorkers getInstance() {
        return ourInstance;
    }

    private ProxyWorkers() {

        thumbnailProcess = new ThumbnailGenerator(thumbnailQueue);
        thumbnailExec.execute(thumbnailProcess);

        pipelineFactory = PipelineFactory.getInstance();
        pipelineFactory.buildAndStart();

    }

    /**
     *
     */
    @SuppressWarnings("unused")
	private void startThreads(){
        thumbnailProcess = new ThumbnailGenerator(thumbnailQueue);
    }

    /**
     * Add a studyId to a stack that indicates it should be loaded in the future.
     * @param studyId    Dicom StudyUID that we might want to download
     * @param remoteAddr IP address to identify who is interested
     */
    public void showInterestInStudy(DicomStudyUID studyId, String remoteAddr){

        logger.info("Show Interest In Study NOT IMPLEMENTED for: "+studyId+", "+remoteAddr);

    }

    /**
     * Add a seriesId to the thumbnail generator queue.
     * @param seriesUID
     */
    public void createThumbnailForSeries(DicomSeriesUID seriesUID){
        thumbnailQueue.offer(seriesUID);
    }

}
