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
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;

/**
 * 
 * @author martin
 */
public interface EpadOperations
{
	EPADProjectList getAllProjectDescriptionsForUser(String username, String sessionID, EPADSearchFilter searchFilter);

	EPADStudyList getAllStudyDescriptionsForSubject(SubjectReference subjectReference, String username, String sessionID,
			EPADSearchFilter searchFilter);

	EPADSubjectList getAllSubjectDescriptionsForProject(String projectID, String username, String sessionID,
			EPADSearchFilter searchFilter);

	EPADSeriesList getAllSeriesDescriptionsForStudy(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter);

	EPADImageList getAllImageDescriptionsForSeries(SeriesReference seriesReference, String sessionID,
			EPADSearchFilter searchFilter);

	// TODO
	EPADFrameList getAllFrameDescriptionsForImage(ImageReference imageReference, String sessionID,
			EPADSearchFilter searchFilter);

	// TODO
	EPADProject getProjectDescription(String projectID, String username, String sessionID);

	// TODO
	EPADSubject getSubjectDescription(SubjectReference subjectReference, String username, String sessionID);

	// TODO
	EPADStudy getStudyDescription(StudyReference studyReference, String username, String sessionID);

	// TODO
	EPADSeries getSeriesDescription(SeriesReference seriesReference, String username, String sessionID);

	EPADImage getImageDescription(ImageReference imageReference, String sessionID);

	// TODO
	EPADFrame getFrameDescription(FrameReference frameReference, String sessionID);

	// Creation

	int createProject(ProjectReference projectReference, String projectName, String projectDescription, String sessionID);

	int createSubject(SubjectReference subjectRefernece, String subjectName, String sessionID);

	int createStudy(StudyReference studyReference, String sessionID);

	// TODO
	int createSeries(SeriesReference seriesReference, String sessionID);

	// Deletion

	int projectDelete(String projectID, String sessionID, String username);

	int subjectDelete(SubjectReference subjectReference, String sessionID, String username);

	int studyDelete(StudyReference studyReference, String sessionID, String username);

	// TODO
	int seriesDelete(SeriesReference seriesReference, String sessionID, String username);

	// AIM

	// TODO
	int createStudyAIM(StudyReference studyReference, String aimID, String sessionID);

	// TODO
	int createSeriesAIM(SeriesReference seriesReference, String aimID, String sessionID);

	// TODO
	int createImageAIM(ImageReference imageReference, String aimID, String sessionID);

	// TODO
	int createFrameAIM(FrameReference frameReference, String aimID, String sessionID);

	// TODO
	int studyAIMDelete(StudyReference studyReference, String aimID, String sessionID, String username);

	// TODO
	int seriesAIMDelete(SeriesReference seriesReference, String aimID, String sessionID, String username);

	// TODO
	int imageAIMDelete(ImageReference imageReference, String aimID, String sessionID, String username);

	// TODO
	int frameAIMDelete(FrameReference frameReference, String aimID, String sessionID, String username);

	// TODO
	EPADAIMList getProjectAIMDescriptions(String projectID, String username, String sessionID);

	// TODO
	EPADAIM getProjectAIMDescription(String projectID, String aimID, String username, String sessionID);

	// TODO
	EPADAIMList getSubjectAIMDescriptions(SubjectReference subjectReference, String username, String sessionID);

	// TODO
	EPADAIM getSubjectAIMDescription(SubjectReference subjectReference, String aimID, String username, String sessionID);

	// TODO
	EPADAIMList getStudyAIMDescriptions(StudyReference studyReference, String username, String sessionID);

	// TODO
	EPADAIM getStudyAIMDescription(StudyReference studyReference, String aimID, String username, String sessionID);

	// TODO
	EPADAIMList getSeriesAIMDescriptions(SeriesReference seriesReference, String username, String sessionID);

	// TODO
	EPADAIM getSeriesAIMDescription(SeriesReference seriesReference, String aimID, String username, String sessionID);

	// TODO
	EPADAIMList getImageAIMDescriptions(ImageReference imageReference, String username, String sessionID);

	// TODO
	EPADAIM getImageAIMDescription(ImageReference imageReference, String aimID, String username, String sessionID);

	// TODO
	EPADAIMList getFrameAIMDescriptions(FrameReference frameReference, String username, String sessionID);

	// TODO
	EPADAIM getFrameAIMDescription(FrameReference frameReference, String aimID, String username, String sessionID);

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

	Set<String> getExamTypesForSubject(String sessionID, String projectID, String subjectID, EPADSearchFilter searchFilter);

	Set<String> getExamTypesForSubject(String subjectID);

	Set<String> getExamTypesForStudy(String studyUID);

	Set<String> getSeriesUIDsForSubject(String projectID, String subjectID, String sessionID,
			EPADSearchFilter searchFilter);
}
