package edu.stanford.epad.epadws.queries;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADDatabaseSeries;
import edu.stanford.epad.dtos.EPADFrame;
import edu.stanford.epad.dtos.EPADFrameList;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.dtos.internal.DCM4CHEESeries;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;

/**
 * 
 * @author martin
 */
public interface EpadOperations
{
	EPADProjectList getAllProjectDescriptionsForUser(String username, String sessionID, EPADSearchFilter searchFilter);

	// TODO
	EPADProject getProjectDescription(String projectID, String username, String sessionID);

	EPADSubjectList getAllSubjectDescriptionsForProject(String projectID, String username, String sessionID,
			EPADSearchFilter searchFilter);

	// TODO
	EPADSubject getSubjectDescription(String projectID, String subjectID, String username, String sessionID);

	EPADStudyList getAllStudyDescriptionsForSubject(String projectID, String subjectID, String username,
			String sessionID, EPADSearchFilter searchFilter);

	// TODO
	EPADStudy getStudyDescription(String projectID, String subjectID, String studyUID, String username, String sessionID);

	EPADSeriesList getAllSeriesDescriptionsForStudy(String projectID, String subjectID, String studyUID, String username,
			String sessionID, EPADSearchFilter searchFilter);

	// TODO
	EPADSeries getSeriesDescription(String projectID, String subjectID, String studyUID, String seriesUID,
			String username, String sessionID);

	EPADImageList getAllImageDescriptionsForSeries(String projectID, String subjectID, String studyUID, String seriesUID,
			String sessionID, EPADSearchFilter searchFilter);

	EPADImage getImageDescription(String projectID, String subjectID, String studyUID, String seriesUID, String imageUID,
			String sessionID);

	// TODO
	EPADFrameList getAllFrameDescriptionsForImage(String projectID, String subjectID, String studyUID, String seriesUID,
			String imageUID, String sessionID, EPADSearchFilter searchFilter);

	// TODO
	EPADFrame getFrameDescription(String projectID, String subjectID, String studyUID, String seriesUID, String imageUID,
			int frameNumber, String sessionID);

	Set<String> getExamTypesForSubject(String sessionID, String projectID, String subjectID, EPADSearchFilter searchFilter);

	Set<String> getExamTypesForSubject(String subjectID);

	Set<String> getExamTypesForStudy(String studyUID);

	Set<String> getSeriesUIDsForSubject(String projectID, String subjectID, String sessionID,
			EPADSearchFilter searchFilter);

	int projectDelete(String projectID, String sessionID, String username);

	int patientDelete(String projectID, String patientID, String sessionID, String username);

	int studyDelete(String projectID, String patientID, String studyUID, String sessionID, String username);

	// AIM

	// TODO
	EPADAIMList getProjectAIMDescriptions(String projectID, String username, String sessionID);

	// TODO
	EPADAIM getProjectAIMDescription(String projectID, String aimID, String username, String sessionID);

	// TODO
	EPADAIMList getSubjectAIMDescriptions(String projectID, String subjectID, String username, String sessionID);

	// TODO
	EPADAIM getSubjectAIMDescription(String projectID, String subjectID, String aimID, String username, String sessionID);

	// TODO
	EPADAIMList getStudyAIMDescriptions(String projectID, String subjectID, String studyUID, String username,
			String sessionID);

	// TODO
	EPADAIM getStudyAIMDescription(String projectID, String subjectID, String studyUID, String aimID, String username,
			String sessionID);

	// TODO
	EPADAIMList getSeriesAIMDescriptions(String projectID, String subjectID, String studyUID, String seriesUID,
			String username, String sessionID);

	// TODO
	EPADAIM getSeriesAIMDescription(String projectID, String subjectID, String studyUID, String seriesUID, String aimID,
			String username, String sessionID);

	// TODO
	EPADAIMList getImageAIMDescriptions(String projectID, String subjectID, String studyUID, String seriesUID,
			String imageUID, String username, String sessionID);

	// TODO
	EPADAIM getImageAIMDescription(String projectID, String subjectID, String studyUID, String seriesUID,
			String imageUID, String aimID, String username, String sessionID);

	// TODO
	EPADAIMList getFrameAIMDescriptions(String projectID, String subjectID, String studyUID, String seriesUID,
			String imageUID, int frameNumber, String username, String sessionID);

	// TODO
	EPADAIM getFrameAIMDescription(String projectID, String subjectID, String studyUID, String seriesUID,
			String imageUID, int frameNumber, String aimID, String username, String sessionID);

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

	void deleteStudyFromEPadAndDcm4CheeDatabases(String studyUID);

	void deleteStudiesFromEPadAndDcm4CheeDatabases(Set<String> studyUIDs);
}
