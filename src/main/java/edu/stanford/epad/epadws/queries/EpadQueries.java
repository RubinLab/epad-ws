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
	EPADProjectList performAllProjectsQuery(String sessionID, String username);

	EPADSubjectList performSubjectsQuery(String sessionID, String projectID);

	EPADStudyList performStudiesQuery(String sessionID, String projectID, String subjectID);

	EPADSeriesList performSeriesQuery(String sessionID, String projectID, String subjectID, String studyUID);

	List<DCM4CHEESeries> getNewDcm4CheeSeriesWithStatus(int statusCode);

	EPADDatabaseSeries peformEPADSeriesQuery(String seriesUID);

	/**
	 * For the specified series, return a list of DICOM image file descriptions for instances that have no corresponding
	 * PNG file specified in the ePAD database.
	 * <p>
	 * Each description is a map with keys: sop_iuid, inst_no, series_iuid, filepath, file_size.
	 */
	List<Map<String, String>> getUnprocessedDicomImageFileDescriptionsForSeries(String seriesIUID);

	Set<String> seriesUIDsForSubject(String sessionID, String projectID, String subjectID);

	Set<String> examTypesForSubject(String sessionID, String projectID, String subjectID);

	Set<String> examTypesForStudy(String sessionID, String projectID, String subjectID, String studyUID);

}
