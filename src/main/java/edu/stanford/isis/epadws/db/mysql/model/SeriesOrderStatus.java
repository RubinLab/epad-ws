package edu.stanford.isis.epadws.db.mysql.model;

import edu.stanford.isis.epadws.server.ProxyLogger;

/**
 * Status of the SeriesOrder class.
 *
 * When a new series is detected, it either runs to completion or until it is
 * idle for a set amount of time.
 *
 *
 * @author amsnyder
 */
public class SeriesOrderStatus {

    ProxyLogger logger = ProxyLogger.getInstance();

    private static final long MAX_IDLE_TIME = 30000;

    long lastActivityTimeStamp;

    final SeriesOrder seriesOrder;

    ProcessingState state = ProcessingState.NEW;


    public SeriesOrderStatus(SeriesOrder seriesOrder){
        if(seriesOrder==null){throw new IllegalArgumentException("seriesOrder cannot be null.");}
        this.seriesOrder = seriesOrder;
        lastActivityTimeStamp = System.currentTimeMillis();
    }

    public SeriesOrder getSeriesOrder(){
        return seriesOrder;
    }

    public void setState(ProcessingState pState){
        state=pState;
    }

    public ProcessingState getState(){
        return state;
    }

    /**
     * We are done if the series has been idle for a while, or if it is complete.
     *
     * @return boolean
     */
    public boolean isDone(){

        //Has the series been idle for too long?
        long currTime = System.currentTimeMillis();
        if(currTime> lastActivityTimeStamp+MAX_IDLE_TIME){
            //log this series as being done.
            logger.info("Series: "+seriesOrder.seriesUID+" is idle. Downloaded "+seriesOrder.getFinishedCount()+" of "+seriesOrder.size()+" images.");
            return true;
        }

        //log for instances that


        //is it now complete?
        if( seriesOrder.isComplete() ){
            logger.info("Series: "+seriesOrder.seriesUID+" is complete. #images="+seriesOrder.size());
            return true;
        }
        return false;
    }

    public void registerActivity(){
        lastActivityTimeStamp = System.currentTimeMillis();
    }

}
