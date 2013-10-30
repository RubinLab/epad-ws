/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.pipeline.threads;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.pipeline.PipelineFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Run this code when shutting down the application.
 */
public class ShutdownHookThread extends Thread{

    private static final EPADLogger logger = EPADLogger.getInstance();

    private static final AtomicBoolean hasRun = new AtomicBoolean(false);

    public ShutdownHookThread(){

    }

    /**
     * This is the shutdown thread.
     */
    public void run(){

        synchronized (hasRun) {
            logger.info("Shutdown hook called.");

            if(hasRun.get()==true){
                //don't call this twice.
                return;
            }
            hasRun.set(true);

            //signal all threads to shutdown.
            ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
            shutdownSignal.shutdownNow();

            //stop any schedule services in pipeline.
            PipelineFactory.getInstance().shutdown();

            //shutdown the database.
            MySqlInstance.getInstance().shutdown();

        }

    }//run

    public void shutdown(){
        run();
    }

}
