package edu.stanford.isis.dicomproxy.db.mysql;

import edu.stanford.isis.dicomproxy.db.mysql.impl.PngStatus;

import java.io.InputStream;
import java.sql.Blob;
import java.util.List;
import java.util.Map;

/**
 * @author amsnyder
 */
public interface MySqlQueries {

    List<Map<String,String>> doStudySearch(String type, String searchString);
    void doDeleteStudy(String uid);
    void doDeleteSeries(String uid);

    List<Map<String,String>> doSeriesSearch(String studyUID);

    /**
     * Get all the studies with the study-stats of zero.
     * @return a list of studyUIDs.
     */
    List<String> getNewStudies();
    List<String> getNewSeries();    //@Deprecated.
    List<String> getEPadDdSeriesForStatus(int statusCode);  //calls epaddb and replaces above.

    List<Map<String,String>> getStudiesForStatus(int statusCode);
    List<Map<String,String>> getSeriesForStatus(int statusCode); //@Deprecated
    List<Map<String,String>> getSeriesForStatusEx(int statusCode); //replaces call above.
    Map<String,String> getSeriesById(String seriesIUID);


    Map<String,String> getPatientForStudy(String studyIUID);
    Map<String,String> getParentStudyForSeries(String seriesIUID);
    Map<String,String> getParentSeriesForImage(String sopInstanceUID);

    String getStudyUIDForSeries(String seriesUID);
    String getSeriesUIDForImage(String sopInstanceUID);

    List<String> getSopInstanceUidsForSeries(String seriesIUID);
    List<Map<String,String>> getImageFilesForSeries(String seriesIUID);

    Blob getImageBlobDataForSeries(String seriesIUID);

    void updateStudiesStatusCode(int newStatusCode, String studyIUID);
    void updateSeriesStatusCode(int newStatusCode, String seriesIUID);
    void updateSeriesStatusCodeEx(int newStatusCode, String seriesIUID);


    List<Map<String,String>> getUnprocessedPngFilesSeries(String seriesIUID);


    void insertEpadFile(Map<String,String> data);

    boolean hasEpadFile(String filePath);

    void updateEpadFile(String filePath, PngStatus newStatus, int fileSize , String errorMsg);

    int countEpadFilesLike(String likePath);
    
    String selectEpadFilePathLike(String sopInstanceUID);

    float getPercentComplete(String seriesUID);

    List<Map<String,String>> getOrderFile(String seriesUID);

    int getStudyKey(String studyUID);
    int getSeriesKey(String seriesUID);
    int getInstanceKey(String sopInstanceUID);

    String getSeriesAttrs(String seriesUID);
    String getInstanceAttrs(String sopInstanceUID);

    byte[] getSeriesAttrsAsBytes(String seriesUID);
    byte[] getInstanceAttrsAsBytes(String sopInstanceUID);

    InputStream getPatientAttrsAsStream(String patId);
    InputStream getStudyAttrsAsStream(String studyIUID);
    InputStream getSeriesAttrsAsStream(String seriesUID);
    InputStream getInstanceAttrsAsStream(String sopInstanceUID);
    
	List<String> getAllUsers();
	void insertUserInDb(String username, String email, String password, String expirationdate, String userrole);
	
	String getUserFK(String username);
	void insertEventInDb(String userName, String event_status,String aim_uid, String aim_name,String patient_id,String patient_name,String template_id,String template_name,String plugin_name);
	List<Map<String,String>> getEventsForUser(String username);
}
