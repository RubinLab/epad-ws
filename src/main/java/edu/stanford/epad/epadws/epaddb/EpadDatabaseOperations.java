package edu.stanford.epad.epadws.epaddb;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.handlers.coordination.CoordinationHandler;
import edu.stanford.epad.epadws.handlers.coordination.Term;

/**
 * Defines all operations on ePAD's database
 * 
 * 
 * @author martin
 */
public interface EpadDatabaseOperations
{
	void deleteStudy(String studyUID);

	void deleteSeries(String seriesUID);

	void updateOrInsertSeries(String seriesUID, SeriesProcessingStatus seriesProcessingStatus);

	void insertEpadEvent(String sessionID, String event_status, String aim_uid, String aim_name, String patient_id,
			String patient_name, String template_id, String template_name, String plugin_name);

	SeriesProcessingStatus getSeriesProcessingStatus(String seriesUID);

	List<Map<String, String>> getEpadEventsForSessionID(String sessionID);

	void forceDICOMReprocessing();

	// Database recording of PNG files generated from DICOM images

	void insertEpadFileRecord(Map<String, String> fileRecord);

	boolean hasEpadFileRecord(String filePath);

	/**
	 * Update the processing status, file size and error message of an ePAD file record.
	 */
	void updateEpadFileRecord(String filePath, SeriesProcessingStatus newStatus, int fileSize, String errorMsg);

	String selectEpadFilePathLike(String sopInstanceUID);

	List<String> selectEpadFilePath();

	/**
	 * Returns a triple containing studyUID, seriesUID, and imageUID for the specified imageUID.
	 */
	String[] retrieveDicomStudySeriesAndImageUIDs(String imageUID);

	Set<String> getAllSeriesUIDsFromEPadDatabase();

	List<String> getFinishedDICOMImageInstanceUIDsForSeriesFromEPadDatabase(String seriesIUID);

	// Coordination methods; will disappear with AIM 4

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
