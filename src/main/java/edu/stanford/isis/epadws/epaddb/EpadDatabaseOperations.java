package edu.stanford.isis.epadws.epaddb;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import edu.stanford.isis.epadws.handlers.coordination.CoordinationHandler;
import edu.stanford.isis.epadws.handlers.coordination.Term;
import edu.stanford.isis.epadws.processing.model.PngProcessingStatus;

/**
 * Defines all operations on ePAD's database
 * 
 * 
 * @author martin
 */
public interface EpadDatabaseOperations
{
	List<Map<String, String>> getDicomSeriesForStatus(int statusCode);

	String[] retrieveDicomStudySeriesAndImageIDs(String imageUID);

	Map<String, String> getDicomSeriesById(String seriesIUID);

	void deleteDicomStudy(String uid);

	void deleteDicomSeries(String uid);

	void updateDicomSeriesStatusCode(int newStatusCode, String seriesIUID);

	void insertEpadFile(Map<String, String> data);

	boolean hasEpadFile(String filePath);

	void updateEpadFile(String filePath, PngProcessingStatus newStatus, int fileSize, String errorMsg);

	String selectEpadFilePathLike(String sopInstanceUID);

	List<String> selectEpadFilePath();

	void insertEpadEvent(String sessionID, String event_status, String aim_uid, String aim_name, String patient_id,
			String patient_name, String template_id, String template_name, String plugin_name);

	List<Map<String, String>> getEpadEventsForSessionID(String sessionID);

	void forceDICOMReprocessing();

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
