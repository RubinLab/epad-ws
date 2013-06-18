package edu.stanford.isis.dicomproxy.db.mysql.model;

/**
 * @author amsnyder
 */
public enum ProcessingState
{
    NEW,
    IN_PIPELINE,
    ERROR,
    COMPLETE;
}
