package edu.stanford.epad.epadws.queries;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADFile;
import edu.stanford.epad.dtos.EPADFileList;
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
import edu.stanford.epad.dtos.EPADUser;
import edu.stanford.epad.dtos.EPADUserList;
import edu.stanford.epad.dtos.internal.DCM4CHEESeries;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.security.EPADSession;

/**
 * 
 * @author martin
 */
public interface EpadOperations
{
	EPADProjectList getProjectDescriptions(String username, String sessionID, EPADSearchFilter searchFilter, boolean annotationCount) throws Exception;

	EPADProject getProjectDescription(ProjectReference projectReference, String username, String sessionID) throws Exception;

	EPADSubjectList getSubjectDescriptions(String projectID, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception;

	EPADSubject getSubjectDescription(SubjectReference subjectReference, String username, String sessionID) throws Exception;

	EPADStudyList getStudyDescriptions(SubjectReference subjectReference, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception;

	EPADStudy getStudyDescription(StudyReference studyReference, String username, String sessionID) throws Exception;

	EPADSeriesList getSeriesDescriptions(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean filterDSOs) throws Exception;

	EPADSeries getSeriesDescription(SeriesReference seriesReference, String username, String sessionID);

	EPADImageList getImageDescriptions(SeriesReference seriesReference, String sessionID, EPADSearchFilter searchFilter);

	EPADImage getImageDescription(ImageReference imageReference, String sessionID);

	EPADFrameList getFrameDescriptions(ImageReference imageReference);

	EPADFrame getFrameDescription(FrameReference frameReference, String sessionID);

	EPADUserList getUserDescriptions(String username, ProjectReference projectReference, String sessionID) throws Exception;

	void addUserToProject(String loggedInusername, ProjectReference projectReference, String username, String role, String sessionID) throws Exception;

	void removeUserFromProject(String loggedInusername, ProjectReference projectReference, String username, String sessionID) throws Exception;

	EPADUserList getUserDescriptions(String username, String sessionID) throws Exception;

	EPADUser getUserDescription(String loggedInusername, String username, String sessionID) throws Exception;
	
	void createSubjectAndStudy(String username, String projectID, String subjectID, String subjectName, String studyUID, String sessionID) throws Exception;

	int createProject(String username, ProjectReference projectReference, String projectName, String projectDescription, String sessionID) throws Exception;

	int updateProject(String username, ProjectReference projectReference, String projectName, String projectDescription, String sessionID) throws Exception;

	int createSubject(String username, SubjectReference subjectRefernece, String subjectName, Date dob, String gender, String sessionID) throws Exception;

	int createStudy(String username, StudyReference studyReference, String description, String sessionID) throws Exception;

	int updateSubject(String username, SubjectReference subjectRefernece, String subjectName, Date dob, String gender, String sessionID) throws Exception;

	int createFile(String username, ProjectReference projectReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	int createFile(String username, SubjectReference subjectReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	int createFile(String username, StudyReference studyReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	int createFile(String username, SeriesReference seriesReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	int createFile(String username, SeriesReference seriesReference, File uploadedFile, String description, String fileType, String sessionID, boolean convertToDICOM, String modality, String instanceNumber) throws Exception;

	int createFile(String username, ImageReference imageReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	int createImage(String username, String projectID, File dicomFile, String sessionID) throws Exception;
	
	EPADFileList getFileDescriptions(ProjectReference projectReference, String username, String sessionID, EPADSearchFilter searchFilter) throws Exception;

	EPADFile getFileDescription(ProjectReference projectReference, String filename, String username, String sessionID) throws Exception;

	EPADFileList getFileDescriptions(SubjectReference subjectReference, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception;

	EPADFile getFileDescription(SubjectReference subjectReference, String filename, String username, String sessionID) throws Exception;

	EPADFileList getFileDescriptions(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception;
	List<EPADFile> getEPADFiles(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception;

	EPADFile getFileDescription(StudyReference studyReference, String filename, String username, String sessionID) throws Exception;

	EPADFileList getFileDescriptions(SeriesReference seriesReference, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception;

	List<EPADFile> getEPADFiles(SeriesReference seriesReference, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception;
	
	EPADFile getFileDescription(SeriesReference seriesReference, String filename, String username, String sessionID) throws Exception;

	String createSubjectAIM(String username, SubjectReference subjectRefernece, String aimID, File aimFile, String sessionID);
	
	String createStudyAIM(String username, StudyReference studyReference, String aimID, File aimFile, String sessionID);

	EPADSeries createSeries(String username, SeriesReference seriesReference, String description, String sessionID) throws Exception;

	String createProjectAIM(String username, ProjectReference projectReference, String aimID, File aimFile, String sessionID);

	String createSeriesAIM(String username, SeriesReference seriesReference, String aimID, File aimFile, String sessionID);

	String createImageAIM(String username, ImageReference imageReference, String aimID, File aimFile, String sessionID);

	String createFrameAIM(String username, FrameReference frameReference, String aimID, File aimFile, String sessionID);

	String setSubjectStatus(SubjectReference subjectReference, String sessionID, String username) throws Exception;
	
	void createOrModifyUser(String loggedInUser, String username, String firstname, String lastname, String email, 
			String password, String oldpassword, String[]addPermission, String[] removePermissions) throws Exception;
	
	void enableUser(String loggedInUser, String username) throws Exception;
	
	void disableUser(String loggedInUser, String username) throws Exception;
	
	void deleteUser(String loggedInUser, String username) throws Exception;
	
	int projectDelete(String projectID, String sessionID, String username) throws Exception;

	int subjectDelete(SubjectReference subjectReference, String sessionID, String username) throws Exception;

	String studyDelete(StudyReference studyReference, String sessionID, boolean deleteAims, String username) throws Exception;

	String seriesDelete(SeriesReference seriesReference, String sessionID, boolean deleteAims, String username);
	
	String deleteSeries(SeriesReference seriesReference, boolean deleteAims);

	int projectAIMDelete(ProjectReference projectReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	int subjectAIMDelete(SubjectReference subjectReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	int studyAIMDelete(StudyReference studyReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	int seriesAIMDelete(SeriesReference seriesReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	int imageAIMDelete(ImageReference imageReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	int frameAIMDelete(FrameReference frameReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	int aimDelete(String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;
	
	void deleteAllSeriesAims(String seriedUID, boolean deleteDSOs);
	
	void deleteAllStudyAims(String studyUID, boolean deleteDSOs);

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

	EPADAIMList getAIMDescriptions(String projectID, AIMSearchType aimSearchType, String searchValue, String username, String sessionID, int start, int count);
	
	EPADAIM getAIMDescription(String aimID, String username, String sessionID);
	/**
	 * See if new series have been uploaded to DCM4CHEE that ePAD does not know about.
	 */
	List<DCM4CHEESeries> getNewDcm4CheeSeries();
	/**
	 * See if any series have been deleted from DCM4CHEE that are still in ePAD.
	 */
	Set<String> getDeletedDcm4CheeSeries();

	/**
	 * For the specified series, return a list of DICOM image file descriptions for instances that have no corresponding
	 * PNG file specified in the ePAD database.
	 * <p>
	 * Each description is a map with keys: study_iuid, series_iuid, sop_iuid, inst_no, filepath, file_size.
	 */
	Set<DICOMFileDescription> getUnprocessedDICOMFilesInSeries(String seriesUID);

	Set<DICOMFileDescription> getDICOMFilesInSeries(String seriesUID, String imagUID);

	void deleteStudyFromEPadAndDcm4CheeDatabases(String studyUID, boolean deleteAims);

	void deleteStudiesFromEPadAndDcm4CheeDatabases(Set<String> studyUIDs);

	Set<String> getExamTypesForSubject(String sessionID, String projectID, String subjectID, EPADSearchFilter searchFilter) throws Exception;

	Set<String> getExamTypesForSubject(String subjectID);

	Set<String> getExamTypesForStudy(String studyUID);

	Set<String> getSeriesUIDsForSubject(String projectID, String subjectID, String sessionID,
			EPADSearchFilter searchFilter);
	
	Collection<EPADSession> getCurrentSessions(String username) throws Exception;
}
