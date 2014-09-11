package edu.stanford.epad.epadws.epaddb;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.aim.AIMSearchType;
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
	String getPNGLocation(String studyUID, String seriesUID, String imageUID);

	String getPNGLocation(ImageReference imageReference);

	String getPNGLocation(FrameReference frameReference);

	String getJPGLocation(FrameReference frameReference);

	Set<String> getAllSeriesUIDsFromEPadDatabase();

	Set<String> getImageUIDsInSeries(String seriesUID);

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

	void insertEpadFileRow(Map<String, String> fileRecord);

	void updateEpadFileRow(String filePath, PNGFileProcessingStatus newStatus, long fileSize, String errorMsg);

	boolean hasEpadFileRow(String filePath);

	List<String> getAllEPadFilePathsWithErrors();

	List<String> getAllEPadInPipelineFilePaths();
	
	void checkAndRefreshAnnotationsTable();

	EPADAIM getAIM(String aimID);

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
	
	Set<EPADAIM> getAIMs(String projectID, AIMSearchType aimSearchType, String value, int start, int count);

	int getNumberOfAIMs(ProjectReference reference);

	int getNumberOfAIMs(SubjectReference reference);

	int getNumberOfAIMs(StudyReference reference);

	int getNumberOfAIMs(SeriesReference reference);

	int getNumberOfAIMs(ImageReference reference);

	int getNumberOfAIMs(FrameReference reference);

	int getNumberOfAIMsForPatients(String projectID, Set<String> patientIDs);

	int getNumberOfAIMsForSeriesSet(String projectID, Set<String> seriesIDs, String username);

	int getNumberOfAIMsForSeries(String projectID, String seriesID, String username);

	void addAIM(String userName, ProjectReference reference, String aimID);

	void addAIM(String userName, StudyReference reference, String aimID);

	void addAIM(String userName, SeriesReference reference, String aimID);

	void addAIM(String userName, ImageReference reference, String aimID);

	void addAIM(String userName, FrameReference reference, String aimID);

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
