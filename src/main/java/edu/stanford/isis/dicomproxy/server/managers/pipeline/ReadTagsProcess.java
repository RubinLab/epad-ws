/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.server.managers.pipeline;


import java.io.File;
import java.util.concurrent.*;

public class ReadTagsProcess extends AbstractPipelineProcess<File>{

    public ReadTagsProcess(ExecutorService tagExec, BlockingQueue<File> tagQueue, BlockingQueue<File> moverQueue){
        super(tagExec,tagQueue,moverQueue);
    }

    @Override
    public Callable<File> getTask(File submitFile) {
        return new ReadTagsTask(submitFile);
    }

    @Override
    public void process()
        throws InterruptedException, ExecutionException
    {
        submitTask();

        forwardCompletedTasks();
    }

    @Override
    public String getProcessName() {
        return "Tags";
    }

}
