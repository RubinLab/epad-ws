/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.leveling;

import edu.stanford.isis.epad.common.dicom.DicomWindowSetting;
import edu.stanford.isis.epad.common.dicom.WindowLevelManager;
import edu.stanford.isis.epadws.processing.pipeline.process.AbstractPipelineProcess;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 *
 */
public class JPEGLevelingProcess extends AbstractPipelineProcess<File>
{

    public JPEGLevelingProcess(ExecutorService es, BlockingQueue<File> inQueue, BlockingQueue<File> outQueue){
        super(es,inQueue,outQueue);
    }

    @Override
    public Callable<File> getTask(File submitFile) {
        JPEGLevelingTask task = new JPEGLevelingTask(submitFile);

        DicomWindowSetting level = WindowLevelManager.getInstance().getSetting();
        task.setLevel(level.getWidth(), level.getLevel());

        return task;
    }

    /**
     * Code specific to this process.
     *
     * @throws InterruptedException on interrupt
     * @throws java.util.concurrent.ExecutionException
     *                              on task exception
     */
    @Override
    public void process() throws InterruptedException, ExecutionException {
        submitTask();

        forwardCompletedTasks();
    }

    /**
     * Return a short name for this stage in the process.
     *
     * @return String
     */
    @Override
    public String getProcessName() {
        return "JPEGLeveling";
    }

}
