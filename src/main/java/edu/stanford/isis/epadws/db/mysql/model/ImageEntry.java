package edu.stanford.isis.epadws.db.mysql.model;

/**
 * instanceNum
 * SOPInstanceUID
 * PNG file path
 * header file path
 *
 * @author amsnyder
 */
public class ImageEntry {

    final int instanceNum;
    final String sopInstanceUID;
    String pngFilePath=null;
    String dicomHeaderFilePath=null;

    ProcessingState state = ProcessingState.NEW;
    String errorMessage = null;

    public ImageEntry(int instanceNum, String sopInstanceUID){
        this.instanceNum = instanceNum;
        this.sopInstanceUID = sopInstanceUID;
    }

    public int getInstanceNum(){
        return instanceNum;
    }

    public String getSopInstanceUID(){
        return sopInstanceUID;
    }

    public ProcessingState getState(){
        return state;
    }

    public void setState(ProcessingState newState){
        state = newState;
    }


    public boolean hasPngFile(){
        return pngFilePath!=null;
    }

    public void setPngFilePath(String pngFilePath){
        this.pngFilePath=pngFilePath;
    }

    public String getPngFilePath(){
        return pngFilePath;
    }

    public boolean hasHeaderFile(){
        return dicomHeaderFilePath!=null;
    }

    public void setDicomHeaderFilePath(String dicomHeaderFilePath){
        this.dicomHeaderFilePath=dicomHeaderFilePath;
    }

    public String getDicomHeaderFilePath(){
        return dicomHeaderFilePath;
    }

    public boolean hasError(){
        return errorMessage!=null;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    /**
     * Processing is complete when the PNG file and the dicom-header file is complete.
     * @return boolean
     */
    public boolean isComplete(){
        if( hasHeaderFile() && hasPngFile() ){
            return true;
        }
        return false;
    }

}
