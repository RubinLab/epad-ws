package edu.stanford.epad.epadws.queries;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.dtos.DCM4CHEESeries;
import edu.stanford.epad.dtos.EPADDatabaseSeries;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubjectList;

/**
 * 
 * @author martin
 */
public interface EpadQueries
{
	EPADProjectList getAllProjectsForUser(String sessionID, String username);

	EPADSubjectList getAllSubjectsForProject(String sessionID, String projectID);

	EPADStudyList getAllStudiesForSubject(String sessionID, String projectID, String subjectID);

	EPADSeriesList getAllSeriesForStudy(String sessionID, String projectID, String subjectID, String studyUID);

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
	List<Map<String, String>> getUnprocessedDicomImageFileDescriptionsForSeries(String seriesIUID);

	Set<String> getSeriesUIDsForSubject(String sessionID, String projectID, String subjectID);

	Set<String> getExamTypesForSubject(String sessionID, String projectID, String subjectID);

	Set<String> getExamTypesForStudy(String sessionID, String projectID, String subjectID, String studyUID);

}
