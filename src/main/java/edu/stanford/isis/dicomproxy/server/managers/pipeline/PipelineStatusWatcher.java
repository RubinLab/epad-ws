/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.server.managers.pipeline;

import edu.stanford.isis.dicomproxy.common.ProxyFileUtils;
import edu.stanford.isis.dicomproxy.server.ProxyLogger;
import edu.stanford.isis.dicomproxy.server.ShutdownSignal;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Count the number of dicom files in the upload/temp directory. Write the results
 * to the /resources/dicom/pipeline.status file and also update the PipelineStatus
 * class which is a single place for keeping all this data.
 */
public class PipelineStatusWatcher implements Runnable {

    ProxyLogger log = ProxyLogger.getInstance();

    public PipelineStatusWatcher(){}

    public static void countDicomFileInUploadDir(){

        int nDcmFiles = ProxyFileUtils.countFilesWithEnding("../resources/upload", ".dcm");
        StringBuilder sb = new StringBuilder();
        sb.append("files remaining: ").append(nDcmFiles).append("\n");
        sb.append("last update:").append(System.currentTimeMillis());

        File pipelineStatusFile = new File("../resources/dicom/pipeline.status");
        ProxyFileUtils.overwrite(pipelineStatusFile,sb.toString());
    }//call


    @Override
    public void run() {
        try{
        ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
        while(true){
            if(shutdownSignal.hasShutdown()){
                    return;
            }

            countDicomFileInUploadDir();

            if(shutdownSignal.hasShutdown()){
                    return;
            }
            long checkInterval = getCheckInterval();
            TimeUnit.MILLISECONDS.sleep(checkInterval);
        }//while

        }catch(Exception e){
            log.sever("Exiting PipelineStatusWatcher thread!!",e);
        }finally{
            log.info("Done PipelineStatusWatcher runnable.");
        }
    }//run

    /**
     * Calculate the check interval. When a temp file is in the upload section
     * it should be shorter.
     * @return long time to sleep in milliseconds. Values between one and 5 seconds should be considered.
     */
    private static long getCheckInterval(){
        return 1000;
    }


}
