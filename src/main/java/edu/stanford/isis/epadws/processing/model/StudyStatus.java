package edu.stanford.isis.epadws.processing.model;

/**
 * The study-status table indicates the status of
 *
 *
 * @author alansnyder
 * Date: 10/3/12
 */
public enum StudyStatus {

    NEW(0),
    PNG_IN_PROCESS(1),
    PNG_COMPLETE(2);

    int value;

    StudyStatus(int value){
        this.value = value;
    }

}
