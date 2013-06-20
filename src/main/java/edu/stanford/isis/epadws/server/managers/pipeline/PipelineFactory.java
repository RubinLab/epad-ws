/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server.managers.pipeline;

import edu.stanford.isis.epadws.server.ProxyLogger;

import java.io.File;
import java.util.concurrent.*;

/**
 * A Singleton class to get queues and objects needed by pipeline classes.
 */
public class PipelineFactory {

    private static final ProxyLogger log = ProxyLogger.getInstance();

    private static final BlockingQueue<File> unzipQueue = new ArrayBlockingQueue<File>(500);
    private static final BlockingQueue<File> taggerQueue = new ArrayBlockingQueue<File>(500);
    private static final BlockingQueue<File> moverQueue = new ArrayBlockingQueue<File>(500);
    private static final BlockingQueue<File> orderQueue = new ArrayBlockingQueue<File>(500);
    private static final BlockingQueue<File> thumbnailQueue = new ArrayBlockingQueue<File>(500);


    final ExecutorService uploadDirWatchExec = Executors.newSingleThreadExecutor();
    final ScheduledExecutorService pipelineStatusWatchExec = Executors.newScheduledThreadPool(1);
    final ExecutorService unzipService = Executors.newFixedThreadPool(10);
    final ExecutorService taggerService = Executors.newFixedThreadPool(20);
    final ExecutorService moverService = Executors.newFixedThreadPool(10);
    final ExecutorService orderService = Executors.newFixedThreadPool(3);
    final ExecutorService thumbnailService = Executors.newFixedThreadPool(15);

    final ExecutorService processService = Executors.newFixedThreadPool(5);
    final ScheduledExecutorService statusCheckService = Executors.newScheduledThreadPool(1);
    /**
     * There is only one pipeline service, and this contains a reference to the
     * pipeline service.
     */
    private static PipelineFactory ourInstance = new PipelineFactory();

    final UploadDirWatcher uploadDirWatcher;

    final UnzipProcess unzipProcess;
    final ReadTagsProcess readTagsProcess;
    final MoverProcess moverProcess;
    final OrderFileRunnable orderFileRunnable;
    final ThumbnailProcess thumbnailProcess;





    public static PipelineFactory getInstance() {
        return ourInstance;
    }

    private PipelineFactory() {

        ThumbnailManager.getFirstInstance(thumbnailQueue);

        uploadDirWatcher = new UploadDirWatcher(unzipQueue);

        //start the unzip process
        unzipProcess = new UnzipProcess(unzipService,unzipQueue,taggerQueue);

        //start the read tags process
        readTagsProcess = new ReadTagsProcess(taggerService,taggerQueue,moverQueue);

        //start the mover process
        moverProcess = new MoverProcess(moverService,moverQueue,orderQueue);

        //start the order queue
        orderFileRunnable = new OrderFileRunnable(orderService,orderQueue);

        //thumbnail queue just consumes data.
        thumbnailProcess = new ThumbnailProcess(thumbnailService,thumbnailQueue,null);

        //print every 60 seconds.
        statusCheckService.scheduleAtFixedRate(new Runnable() {
            private String lastQueueSize = "00000000000";
            private long lastUpdate = -1;

            @Override
            public void run() {
                if( queueSizeHasChanged() || updateTimeExceeded() ){
                    lastUpdate = System.currentTimeMillis();
                    printPipelineStats();
                }
            }//run

            /**
             * True if the queue have changed. This is done by building a string.
             * @return boolean true if a queue has changed.
             */
            private boolean queueSizeHasChanged(){
                StringBuilder sb = new StringBuilder();
                sb.append(unzipQueue.size());
                sb.append(taggerQueue.size());
                sb.append(moverQueue.size());
                sb.append(orderQueue.size());
                sb.append(thumbnailQueue.size());
                sb.append(unzipProcess.getTasksInProgressCount());
                sb.append(readTagsProcess.getTasksInProgressCount());
                sb.append(moverProcess.getTasksInProgressCount());
                sb.append(thumbnailProcess.getTasksInProgressCount());
                sb.append(uploadDirWatcher.getErrorFileCount());

                String currQueue = sb.toString();

                if(!currQueue.equals(lastQueueSize)){
                    lastQueueSize=currQueue;
                    return true;
                }
                return false;
            }

            private boolean updateTimeExceeded(){
                //true if more than one minute.
                return ((System.currentTimeMillis()-lastUpdate)>(1000*60));
            }
        },5,1,TimeUnit.SECONDS);

        pipelineStatusWatchExec.scheduleAtFixedRate(new PipelineStatusWatcher(),5,5,TimeUnit.SECONDS);

    }


    /**
     * Build the pipeline and start the processes.
     */
    public void buildAndStart(){

        processService.submit(thumbnailProcess);
        processService.submit(orderFileRunnable);
        processService.submit(moverProcess);
        processService.submit(readTagsProcess);
        processService.submit(unzipProcess);

        uploadDirWatchExec.execute(uploadDirWatcher);
    }
    /**
     * Shut down the pipeline service
     */
    public void shutdown(){
        pipelineStatusWatchExec.shutdown();
        statusCheckService.shutdown();

        uploadDirWatchExec.shutdown();

        //?? Do we need  this.
        //processService.shutdown();  //ToDo: uncomment and test.
    }

    public void printPipelineStats()
    {
        log.info(getPipelineStats());
    }

    public String getPipelineStats(){
        StringBuilder sb = new StringBuilder("Pipeline Stats: ");
        sb.append("Queue sizes - ");
        sb.append(" quz=").append(unzipQueue.size());
        sb.append(" qtg=").append(taggerQueue.size());
        sb.append(" qmv=").append(moverQueue.size());
        sb.append(" qor=").append(orderQueue.size());
        sb.append(" qth=").append(thumbnailQueue.size());
        sb.append(" Process map sizes - ");
        sb.append(" tuz=").append(unzipProcess.getTasksInProgressCount());
        sb.append(" ttg=").append(readTagsProcess.getTasksInProgressCount());
        sb.append(" tmv=").append(moverProcess.getTasksInProgressCount());
        sb.append(" tth=").append(thumbnailProcess.getTasksInProgressCount());
        sb.append("; error file count=").append(uploadDirWatcher.getErrorFileCount());

        return sb.toString();
    }

    /**
     * Just list the total number of files in the
     * @return int
     */
    public int getActivityLevel()
    {
        int itemsInQueue=0;
        itemsInQueue+=unzipQueue.size();
        itemsInQueue+=taggerQueue.size();
        itemsInQueue+=moverQueue.size();
        itemsInQueue+=orderQueue.size();
        itemsInQueue+=thumbnailQueue.size();
        itemsInQueue+=unzipProcess.getTasksInProgressCount();
        itemsInQueue+=readTagsProcess.getTasksInProgressCount();
        itemsInQueue+=moverProcess.getTasksInProgressCount();
        itemsInQueue+=thumbnailProcess.getTasksInProgressCount();

        return itemsInQueue;
    }

    /**
     * Lists the total number of files that error-ed out.
     * @return int
     */
    public int getErrorFileCount(){
        return uploadDirWatcher.getErrorFileCount();
    }

}
