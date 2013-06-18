/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.server;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Use this class as the logger for the DicomProxy project, which logs
 * the data in the ./log/dicom-proxy.log file.
 *
 * @author amsnyder
 */
public class ProxyLogger {
    private static ProxyLogger ourInstance = new ProxyLogger();

    final Logger log;
    boolean useDebug = false;

    public static ProxyLogger getInstance() {
        return ourInstance;
    }

    private ProxyLogger() {

        log = Logger.getAnonymousLogger();

        //setup logging.
        try{

            FileHandler fh = new FileHandler("../log/dicom-proxy.log");
            fh.setFormatter( new LogFormatter() );
            log.addHandler(fh);

        }catch(Exception e){
            System.out.println("Failed to setup logging!"+e.getMessage());
        }

    }

    /**
     * Log a debug string.
     * @param message
     */
    public void debug(String message){
        if(useDebug){
            info(message);
        }
    }

    /**
     * To turn debugging on, set to true.
     * @param value true to turn on debug logging.
     */
    public void setDebug(boolean value){
        useDebug = value;
    }

    /**
     * Log a standard INFO level message.
     * @param message
     */
    public void info(String message){
        log.info(message);
    }

    /**
     * Log a warning with a message and the exception
     * @param message String to write to the log file.
     * @param t Throwable
     */
    public void warning(String message, Throwable t){
        log.log(Level.WARNING,message,t);
    }

    /**
     * Log a sever error with a message and a stack trace
     * @param message String to write to the log
     * @param t Throwable exception with Stack Trace.
     */
    public void sever(String message, Throwable t){
        log.log(Level.SEVERE,message,t);
        if(t!=null){
            t.printStackTrace();
        }
    }
}
