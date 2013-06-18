package edu.stanford.isis.dicomproxy.db.mysql.impl;

/**
 * @author alansnyder
 */
public enum PngStatus {

    NO_DICOM(1),
    IN_PIPELINE(2),
    DONE(3),
    ERROR(4);

    int statusCode;

    PngStatus(int status){
        this.statusCode=status;
    }

    public int getCode(){
        return statusCode;
    }

}
