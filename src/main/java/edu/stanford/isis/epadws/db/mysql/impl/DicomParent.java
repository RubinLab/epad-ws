package edu.stanford.isis.epadws.db.mysql.impl;

/**
 * Holds a Dicom UID and the type.
 *
 * @author amsnyder
 */
public class DicomParent {

    final String dicomUID;
    final DicomParentType type;

    public DicomParent(String dicomUID, DicomParentType type){
        this.dicomUID = dicomUID;
        this.type = type;
    }

    public String getDicomUID(){
        return dicomUID;
    }

    public DicomParentType getType(){
        return type;
    }

}
