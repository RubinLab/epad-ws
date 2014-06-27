package edu.stanford.epad.epadws.epaddb;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.handlers.coordination.CoordinationHandler;
import edu.stanford.epad.epadws.handlers.coordination.Term;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;

/**
 * Defines all operations on ePAD's database
 * 
 * 
 * @author martin
 */
public interface EpadDatabaseOperations
{
	EPADAIM getAIM(ProjectReference projectReference, String aimID);

	EPADAIM getAIM(SubjectReference subjectReference, String aimID);

	EPADAIM getAIM(StudyReference studyReference, String aimID);

	EPADAIM getAIM(SeriesReference seriesReference, String aimID);

	EPADAIM getAIM(ImageReference imageReference, String aimID);

	EPADAIM getAIM(FrameReference frameReference, String aimID);

	Set<EPADAIM> getAIMs(ProjectReference projectReference);

	Set<EPADAIM> getAIMs(SubjectReference subjectReference);

	Set<EPADAIM> getAIMs(StudyReference studyReference);

	Set<EPADAIM> getAIMs(SeriesReference seriesReference);

	Set<EPADAIM> getAIMs(ImageReference imageReference);

	Set<EPADAIM> getAIMs(FrameReference frameReference);

	int getNumberOfAIMs(ProjectReference reference);

	int getNumberOfAIMs(SubjectReference reference);

	int getNumberOfAIMs(StudyReference reference);

	int getNumberOfAIMs(SeriesReference reference);

	int getNumberOfAIMs(ImageReference reference);

	int getNumberOfAIMs(FrameReference reference);

	void addAIM(StudyReference reference, String aimID);

	void addAIM(SeriesReference reference, String aimID);

	void addAIM(ImageReference reference, String aimID);

	void addAIM(FrameReference reference, String aimID);

	void deleteAIM(StudyReference reference, String aimID);

	void deleteAIM(SeriesReference reference, String aimID);

	void deleteAIM(ImageReference reference, String aimID);

	void deleteAIM(FrameReference reference, String aimID);

	Set<String> getAllSeriesUIDsFromEPadDatabase();

	List<String> getAllImageUIDsInSeries(String seriesUID);

	void deleteStudy(String studyUID);

	void deleteSeries(String seriesUID);

	SeriesProcessingStatus getSeriesProcessingStatus(String seriesUID);

	void insertEpadEvent(String sessionID, String eventStatus, String aimUID, String aimName, String patientID,
			String patientName, String templateID, String templateName, String pluginName);

	Timestamp getSeriesProcessingDate(String seriesUID);

	void updateOrInsertSeries(String seriesUID, SeriesProcessingStatus seriesProcessingStatus);

	List<Map<String, String>> getEpadEventsForSessionID(String sessionID);

	void forceDICOMReprocessing();

	// Database recording of PNG files generated from DICOM images

	void insertEpadFileRecord(Map<String, String> fileRecord);

	boolean hasEpadFileRecord(String filePath);

	/**
	 * Update the processing status, file size and error message of an ePAD file record.
	 */
	void updateEpadFileRecord(String filePath, PNGFileProcessingStatus newStatus, int fileSize, String errorMsg);

	String getEpadFilePathLike(String sopInstanceUID);

	List<String> getAllEPadFilePathsWithErrors();

	List<String> getAllEPadInPipelineFilePaths();

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
