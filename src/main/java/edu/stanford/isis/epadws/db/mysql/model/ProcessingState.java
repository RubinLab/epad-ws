package edu.stanford.isis.epadws.db.mysql.model;

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
