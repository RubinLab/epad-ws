package edu.stanford.epad.epadws.queries;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.dtos.EPADDatabaseSeries;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.dtos.internal.DCM4CHEESeries;
import edu.stanford.epad.epadws.handlers.search.EPADSearchFilter;

/**
 * 
 * @author martin
 */
public interface EpadOperations
{
	EPADProjectList getAllProjectsForUser(String username, String sessionID, EPADSearchFilter searchFilter);

	EPADSubjectList getAllSubjectsForProject(String projectID, String username, String sessionID,
			EPADSearchFilter searchFilter);

	EPADStudyList getAllStudiesForPatient(String projectID, String subjectID, String username, String sessionID,
			EPADSearchFilter searchFilter);

	EPADSeriesList getAllSeriesForStudy(String projectID, String subjectID, String studyUID, String username,
			String sessionID, EPADSearchFilter searchFilter);

	EPADImageList getAllImagesForSeries(String projectID, String subjectID, String studyUID, String seriesUID,
			String sessionID, EPADSearchFilter searchFilter);

	EPADImage getImage(String projectID, String subjectID, String studyUID, String seriesUID, String imageID,
			String sessionID, EPADSearchFilter searchFilter);

	Set<String> getExamTypesForPatient(String sessionID, String projectID, String subjectID, EPADSearchFilter searchFilter);

	Set<String> getExamTypesForPatient(String subjectID);

	Set<String> getExamTypesForStudy(String studyUID);

	Set<String> getSeriesUIDsForPatient(String projectID, String subjectID, String sessionID,
			EPADSearchFilter searchFilter);

	/**
	 * See if new series have been uploaded to DCM4CHEE that ePAD does not know about.
	 */
	List<DCM4CHEESeries> getNewDcm4CheeSeries();

	/**
	 * Query both the ePAD and DCM4CHEE database to get information on a series.
	 */
	EPADDatabaseSeries getSeries(String seriesUID);

	/**
	 * For the specified series, return a list of DICOM image file descriptions for instances that have no corresponding
	 * PNG file specified in the ePAD database.
	 * <p>
	 * Each description is a map with keys: sop_iuid, inst_no, series_iuid, filepath, file_size.
	 */
	List<Map<String, String>> getUnprocessedDicomImageFileDescriptionsForSeries(String seriesUID);

	void scheduleProjectDelete(String sessionID, String username, String projectID);

	void schedulePatientDelete(String sessionID, String username, String projectID, String patientID);

	void scheduleStudyDelete(String sessionID, String username, String projectID, String patientID, String studyUID);

	void deleteStudyFromEPadAndDcm4CheeDatabases(String studyUID);

	void deleteStudiesFromEPadAndDcm4CheeDatabases(Set<String> studyUIDs);
}
