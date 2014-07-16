package edu.stanford.epad.epadws.queries;

import java.util.List;
import java.util.Set;

import edu.stanford.epad.common.dicom.DICOMFileDescription;
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
	EPADProjectList getProjectDescriptions(String username, String sessionID, EPADSearchFilter searchFilter);

	EPADProject getProjectDescription(ProjectReference projectReference, String username, String sessionID);

	EPADSubjectList getSubjectDescriptions(String projectID, String username, String sessionID,
			EPADSearchFilter searchFilter);

	EPADSubject getSubjectDescription(SubjectReference subjectReference, String username, String sessionID);

	EPADStudyList getStudyDescriptions(SubjectReference subjectReference, String username, String sessionID,
			EPADSearchFilter searchFilter);

	EPADStudy getStudyDescription(StudyReference studyReference, String username, String sessionID);

	EPADSeriesList getSeriesDescriptions(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter);

	EPADSeries getSeriesDescription(SeriesReference seriesReference, String username, String sessionID);

	EPADImageList getImageDescriptions(SeriesReference seriesReference, String sessionID, EPADSearchFilter searchFilter);

	EPADImage getImageDescription(ImageReference imageReference, String sessionID);

	EPADFrameList getFrameDescriptions(ImageReference imageReference);

	EPADFrame getFrameDescription(FrameReference frameReference, String sessionID);

	void createSubjectAndStudy(String projectID, String subjectID, String subjectName, String studyUID, String sessionID);

	int createProject(ProjectReference projectReference, String projectName, String projectDescription, String sessionID);

	int createSubject(SubjectReference subjectRefernece, String subjectName, String sessionID);

	int createStudy(StudyReference studyReference, String sessionID);

	int createStudyAIM(StudyReference studyReference, String aimID, String sessionID);

	int createSeries(SeriesReference seriesReference, String sessionID);

	int createSeriesAIM(SeriesReference seriesReference, String aimID, String sessionID);

	int createImageAIM(ImageReference imageReference, String aimID, String sessionID);

	int createFrameAIM(FrameReference frameReference, String aimID, String sessionID);

	int projectDelete(String projectID, String sessionID, String username);

	int subjectDelete(SubjectReference subjectReference, String sessionID, String username);

	int studyDelete(StudyReference studyReference, String sessionID, String username);

	int seriesDelete(SeriesReference seriesReference, String sessionID, String username);

	int studyAIMDelete(StudyReference studyReference, String aimID, String sessionID, String username);

	int seriesAIMDelete(SeriesReference seriesReference, String aimID, String sessionID, String username);

	int imageAIMDelete(ImageReference imageReference, String aimID, String sessionID, String username);

	int frameAIMDelete(FrameReference frameReference, String aimID, String sessionID, String username);

	EPADAIMList getProjectAIMDescriptions(ProjectReference projectReference, String username, String sessionID);

	EPADAIM getProjectAIMDescription(ProjectReference projectReference, String aimID, String username, String sessionID);

	EPADAIMList getSubjectAIMDescriptions(SubjectReference subjectReference, String username, String sessionID);

	EPADAIM getSubjectAIMDescription(SubjectReference subjectReference, String aimID, String username, String sessionID);

	EPADAIMList getStudyAIMDescriptions(StudyReference studyReference, String username, String sessionID);

	EPADAIM getStudyAIMDescription(StudyReference studyReference, String aimID, String username, String sessionID);

	EPADAIMList getSeriesAIMDescriptions(SeriesReference seriesReference, String username, String sessionID);

	EPADAIM getSeriesAIMDescription(SeriesReference seriesReference, String aimID, String username, String sessionID);

	EPADAIMList getImageAIMDescriptions(ImageReference imageReference, String username, String sessionID);

	EPADAIM getImageAIMDescription(ImageReference imageReference, String aimID, String username, String sessionID);

	EPADAIMList getFrameAIMDescriptions(FrameReference frameReference, String username, String sessionID);

	EPADAIM getFrameAIMDescription(FrameReference frameReference, String aimID, String username, String sessionID);

	/**
	 * See if new series have been uploaded to DCM4CHEE that ePAD does not know about.
	 */
	List<DCM4CHEESeries> getNewDcm4CheeSeries();

	/**
	 * Query both the ePAD and DCM4CHEE database to get information on a series.
	 */
	EPADDatabaseSeries getSeries(String studyUID, String seriesUID);

	// TODO This should be deleted shortly - used by seriesorderj call.

	/**
	 * For the specified series, return a list of DICOM image file descriptions for instances that have no corresponding
	 * PNG file specified in the ePAD database.
	 * <p>
	 * Each description is a map with keys: study_iuid, series_iuid, sop_iuid, inst_no, filepath, file_size.
	 */
	Set<DICOMFileDescription> getUnprocessedDICOMFilesInSeries(String seriesUID);

	void deleteStudyFromEPadAndDcm4CheeDatabases(String studyUID);

	void deleteStudiesFromEPadAndDcm4CheeDatabases(Set<String> studyUIDs);

	Set<String> getExamTypesForSubject(String sessionID, String projectID, String subjectID, EPADSearchFilter searchFilter);

	Set<String> getExamTypesForSubject(String subjectID);

	Set<String> getExamTypesForStudy(String studyUID);

	Set<String> getSeriesUIDsForSubject(String projectID, String subjectID, String sessionID,
			EPADSearchFilter searchFilter);
}
