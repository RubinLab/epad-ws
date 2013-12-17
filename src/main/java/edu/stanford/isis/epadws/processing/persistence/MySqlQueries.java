package edu.stanford.isis.epadws.processing.persistence;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import edu.stanford.isis.epadws.handlers.coordination.CoordinationHandler;
import edu.stanford.isis.epadws.handlers.coordination.Term;
import edu.stanford.isis.epadws.processing.model.PngProcessingStatus;

public interface MySqlQueries
{
	List<Map<String, String>> doStudySearchInDcm4Chee(String type, String searchString);

	List<Map<String, String>> findAllSeriesInStudyInDcm4Chee(String studyUID);

	void doDeleteDicomStudyInEPadDatabase(String uid);

	void doDeleteSeriesInEPadDatabase(String uid);

	/**
	 * Get all the studies with the study status of zero.
	 * 
	 * @return a list of studyUIDs.
	 */
	List<String> getNewSeriesInDcm4Chee();

	List<Map<String, String>> getSeriesForStatusInEPadDatabase(int statusCode);

	String[] retrieveStudySeriesAndImageIDsFromEpadDatabase(String imageUID);

	Map<String, String> getSeriesByIdInEPadDatabase(String seriesIUID);

	Map<String, String> getPatientForStudyFromDcm4Chee(String studyIUID);

	List<String> getStudyIDsForPatientFromDcm4Chee(String patientID);

	Map<String, String> getParentStudyForSeries(String seriesIUID);

	String getStudyUIDForSeries(String seriesUID);

	List<Map<String, String>> getDICOMImageFileDescriptionsForSeries(String seriesIUID);

	Blob getImageBlobDataForSeries(String seriesIUID);

	void updateSeriesStatusCodeEx(int newStatusCode, String seriesIUID);

	/**
	 * For the specified series, return a list of DICOM image file descriptions for instances that have no corresponding
	 * PNG file specified in the ePAD database.
	 * <p>
	 * Each description is a map with keys: sop_iuid, inst_no, series_iuid, filepath, file_size.
	 */
	List<Map<String, String>> getUnprocessedDicomImageFileDescriptionsForSeries(String seriesIUID);

	void insertEpadFile(Map<String, String> data);

	boolean hasEpadFile(String filePath);

	void updateEpadFile(String filePath, PngProcessingStatus newStatus, int fileSize, String errorMsg);

	String selectEpadFilePathLike(String sopInstanceUID);

	List<String> selectEpadFilePath();

	List<Map<String, String>> getDicomSeriesOrder(String seriesUID);

	int getInstanceKeyForInstance(String sopInstanceUID);

	InputStream getPatientAttrsAsStream(String patId);

	InputStream getStudyAttrsAsStream(String studyIUID);

	InputStream getSeriesAttrsAsStream(String seriesUID);

	void insertEvent(String userName, String event_status, String aim_uid, String aim_name, String patient_id,
			String patient_name, String template_id, String template_name, String plugin_name);

	List<Map<String, String>> getEventsForUser(String username);

	/**
	 * Return the key of a {@link Term}.
	 * 
	 * @param term
	 * @return The key of the term or -1 if it is not recorded
	 * @see CoordinationHandler
	 */
	int getKeyForTerm(Term term) throws SQLException;

	/**
	 * 
	 * @param termKeys
	 * @return The {@link Term} representing the coordination or null if it does not exist.
	 * @see CoordinationHandler
	 */
	Term getCoordinationTerm(List<Integer> termKeys) throws SQLException;

	/**
	 * Insert a term and return its new ID.
	 * 
	 * @param term
	 * @return The new key of the term
	 * @see CoordinationHandler
	 */
	int insertTerm(Term term) throws SQLException;

	/**
	 * Record a coordination term.
	 * 
	 * @param coordinationTermID
	 * @param schemaName
	 * @param schemaVersion
	 * @param description
	 * @param termIDs
	 * @return A {@link Term} representing the new coordination
	 * @see CoordinationHandler
	 */
	Term insertCoordinationTerm(String termIDPrefix, String schemaName, String schemaVersion, String description,
			List<Integer> termKeys) throws SQLException;
}
