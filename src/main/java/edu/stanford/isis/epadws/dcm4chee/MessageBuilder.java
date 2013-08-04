/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.dcm4chee.demos;

/**
 * A replacement for log4j in the FirstDicomQueryTest code.
 *
 * @author amsnyder
 */
public class MessageBuilder {

    StringBuilder sb = new StringBuilder("");

    long startTime;

    public MessageBuilder(){
        startTime = System.currentTimeMillis();
    }

    /**
     * Call this method to clear the last result!!
     */
    public void clear(){
        sb = new StringBuilder("");
    }

    public void info(String message){
        append("Info: "+message);
    }

    public void info(String formatted, Object ... strings){

        String retVal = formatted;
        for(Object curr : strings){
            retVal = retVal.replaceFirst("\\{\\}",curr.toString());
        }
        info(retVal);
    }

    public void error(String message){
        append("ERROR: "+message);
    }

    public void error(String message, Exception e){
        error(message+" "+e.getMessage());
    }

    /**
     * All public methods need to call this method.
     * @param append
     */
    private void append(String append){
        long sec = (System.currentTimeMillis()-startTime)/1000;
        sb.append("\n"+sec+" sec. ,"+append);
    }

    @Override
    public String toString(){
        return sb.toString();
    }

}
