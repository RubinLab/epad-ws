//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.queries;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADEventLogList;
import edu.stanford.epad.dtos.EPADFile;
import edu.stanford.epad.dtos.EPADFileList;
import edu.stanford.epad.dtos.EPADFrame;
import edu.stanford.epad.dtos.EPADFrameList;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADObjectList;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.dtos.EPADTemplateContainerList;
import edu.stanford.epad.dtos.EPADUser;
import edu.stanford.epad.dtos.EPADUserList;
import edu.stanford.epad.dtos.EPADWorklist;
import edu.stanford.epad.dtos.EPADWorklistList;
import edu.stanford.epad.dtos.EPADWorklistStudyList;
import edu.stanford.epad.dtos.EPADWorklistSubjectList;
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
	/**
	 * Get list of projects accessible to user
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param annotationCount
	 * @return
	 * @throws Exception
	 */
	EPADProjectList getProjectDescriptions(String username, String sessionID, EPADSearchFilter searchFilter, boolean annotationCount) throws Exception;

	/**
	 * Get project description
	 * @param projectReference
	 * @param username
	 * @param sessionID
	 * @param annotationCount
	 * @return
	 * @throws Exception
	 */
	EPADProject getProjectDescription(ProjectReference projectReference, String username, String sessionID, boolean annotationCount) throws Exception;

	/**
	 * Get subject descriptions for a project 
	 * @param projectID
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param start
	 * @param count
	 * @param sortField
	 * @return
	 * @throws Exception
	 */
	EPADSubjectList getSubjectDescriptions(String projectID, String username, String sessionID,
			EPADSearchFilter searchFilter, int start, int count, String sortField) throws Exception;

	/**
	 * Get Unassigned subject descriptions
	 * @param projectID
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param start
	 * @param count
	 * @param sortField
	 * @return
	 * @throws Exception
	 */
	EPADSubjectList getUnassignedSubjectDescriptions(String username, String sessionID, EPADSearchFilter searchFilter) throws Exception;

	/**
	 * Get subject description
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADSubject getSubjectDescription(SubjectReference subjectReference, String username, String sessionID) throws Exception;

	/**
	 * Get study descriptions for a subject 
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @return
	 * @throws Exception
	 */
	EPADStudyList getStudyDescriptions(SubjectReference subjectReference, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception;

	/**
	 * Get study description
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADStudy getStudyDescription(StudyReference studyReference, String username, String sessionID) throws Exception;
	
	/**
	 * Get series descriptions for a study
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param filterDSOs
	 * @return
	 * @throws Exception
	 */
	EPADSeriesList getSeriesDescriptions(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean filterDSOs) throws Exception;

	/**
	 * Get series description
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADSeries getSeriesDescription(SeriesReference seriesReference, String username, String sessionID);
	
	/**
	 * Get image descriptions for a series
	 * @param seriesReference
	 * @param sessionID
	 * @param searchFilter
	 * @return
	 */
	EPADImageList getImageDescriptions(SeriesReference seriesReference, String sessionID, EPADSearchFilter searchFilter);

	/**
	 * Get image description
	 * @param imageReference
	 * @param sessionID
	 * @return
	 */
	EPADImage getImageDescription(ImageReference imageReference, String sessionID);

	/**
	 * Get frame descriptions for an image
	 * @param imageReference
	 * @return
	 */
	EPADFrameList getFrameDescriptions(ImageReference imageReference);

	/**
	 * Get frame description
	 * @param frameReference
	 * @param sessionID
	 * @return
	 */
	EPADFrame getFrameDescription(FrameReference frameReference, String sessionID);

	/**
	 * Get descriptions for users in a project
	 * @param username
	 * @param projectReference
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADUserList getUserDescriptions(String username, ProjectReference projectReference, String sessionID) throws Exception;

	/**
	 * Add a user to a project or set default template for a user
	 * @param loggedInusername
	 * @param projectReference
	 * @param username
	 * @param role
	 * @param defaultTemplate
	 * @param sessionID
	 * @throws Exception
	 */
	void addUserToProject(String loggedInusername, ProjectReference projectReference, String username, String role, String defaultTemplate, String sessionID) throws Exception;

	/**
	 * Remove a user from a project
	 * @param loggedInusername
	 * @param projectReference
	 * @param username
	 * @param sessionID
	 * @throws Exception
	 */
	void removeUserFromProject(String loggedInusername, ProjectReference projectReference, String username, String sessionID) throws Exception;

	/**
	 * Get description for all users
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADUserList getUserDescriptions(String username, String sessionID) throws Exception;

	/**
	 * Get description for a user
	 * @param loggedInusername
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADUser getUserDescription(String loggedInusername, String username, String sessionID) throws Exception;

	/**
	 * Get users who can review annotations for a reader
	 * @param loggedInusername
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADUserList getReviewers(String loggedInusername, String username, String sessionID) throws Exception;

	/**
	 * Get readers whose annotations can be reviewed by this user
	 * @param loggedInusername
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADUserList getReviewees(String loggedInusername, String username, String sessionID) throws Exception;
	
	/**
	 * Create a subject and study (without a dicom)
	 * @param username
	 * @param projectID
	 * @param subjectID
	 * @param subjectName
	 * @param studyUID
	 * @param sessionID
	 * @throws Exception
	 */
	void createSubjectAndStudy(String username, String projectID, String subjectID, String subjectName, String studyUID, String sessionID) throws Exception;

	/**
	 * Create a project
	 * @param username
	 * @param projectReference
	 * @param projectName
	 * @param projectDescription
	 * @param defaultTemplate
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createProject(String username, ProjectReference projectReference, String projectName, String projectDescription, String defaultTemplate, String sessionID) throws Exception;

	/**
	 * update a project
	 * @param username
	 * @param projectReference
	 * @param projectName
	 * @param projectDescription
	 * @param defaultTemplate
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int updateProject(String username, ProjectReference projectReference, String projectName, String projectDescription, String defaultTemplate, String sessionID) throws Exception;

	/**
	 * create a subject
	 * @param username
	 * @param subjectRefernece
	 * @param subjectName
	 * @param dob
	 * @param gender
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createSubject(String username, SubjectReference subjectRefernece, String subjectName, Date dob, String gender, String sessionID) throws Exception;

	/**
	 * create a study
	 * @param username
	 * @param studyReference
	 * @param description
	 * @param studyDate
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createStudy(String username, StudyReference studyReference, String description, Date studyDate, String sessionID) throws Exception;

	/**
	 * update a subject
	 * @param username
	 * @param subjectRefernece
	 * @param subjectName
	 * @param dob
	 * @param gender
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int updateSubject(String username, SubjectReference subjectRefernece, String subjectName, Date dob, String gender, String sessionID) throws Exception;

	/**
	 * Upload a project file
	 * @param username
	 * @param projectReference
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createFile(String username, ProjectReference projectReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	/**
	 * Upload a subject file
	 * @param username
	 * @param subjectReference
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createFile(String username, SubjectReference subjectReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	/**
	 * Upload a study file
	 * @param username
	 * @param studyReference
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createFile(String username, StudyReference studyReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	/**
	 * Upload a series file
	 * @param username
	 * @param seriesReference
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createFile(String username, SeriesReference seriesReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	/**
	 * Upload a series file and convert to dicom (does not quite work)
	 * @param username
	 * @param seriesReference
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @param convertToDICOM
	 * @param modality
	 * @param instanceNumber
	 * @return
	 * @throws Exception
	 */
	int createFile(String username, SeriesReference seriesReference, File uploadedFile, String description, String fileType, String sessionID, boolean convertToDICOM, String modality, String instanceNumber) throws Exception;

	/**
	 * Upload a image file
	 * @param username
	 * @param imageReference
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createFile(String username, ImageReference imageReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	/**
	 * Upload a file
	 * @param username
	 * @param projectID
	 * @param subjectID
	 * @param studyID
	 * @param seriesID
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @throws Exception
	 */
	void createFile(String username, String projectID, String subjectID, String studyID, String seriesID,
			File uploadedFile, String description, String fileType, String sessionID) throws Exception;
	
	/**
	 * Upload a dicom
	 * @param username
	 * @param projectID
	 * @param dicomFile
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createImage(String username, String projectID, File dicomFile, String sessionID) throws Exception;
	
	/**
	 * @param username
	 * @param templateFile
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createSystemTemplate(String username, File templateFile, String sessionID) throws Exception;	
	
	/**
	 * Get project files description
	 * @param projectReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param toplevelOnly
	 * @return
	 * @throws Exception
	 */
	EPADFileList getFileDescriptions(ProjectReference projectReference, String username, String sessionID, EPADSearchFilter searchFilter, boolean toplevelOnly) throws Exception;

	/**
	 * Get project files description
	 * @param projectReference
	 * @param filename
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADFile getFileDescription(ProjectReference projectReference, String filename, String username, String sessionID) throws Exception;

	/**
	 * Get subject files description
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param toplevelOnly
	 * @return
	 * @throws Exception
	 */
	EPADFileList getFileDescriptions(SubjectReference subjectReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean toplevelOnly) throws Exception;

	/**
	 * Get subject files description
	 * @param subjectReference
	 * @param filename
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADFile getFileDescription(SubjectReference subjectReference, String filename, String username, String sessionID) throws Exception;

	/**
	 * Get study file description
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param toplevelOnly
	 * @return
	 * @throws Exception
	 */
	EPADFileList getFileDescriptions(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean toplevelOnly) throws Exception;
	/**
	 * Get study files description
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param toplevelOnly
	 * @return
	 * @throws Exception
	 */
	List<EPADFile> getEPADFiles(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean toplevelOnly) throws Exception;

	/**
	 * Get study file description
	 * @param studyReference
	 * @param filename
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADFile getFileDescription(StudyReference studyReference, String filename, String username, String sessionID) throws Exception;

	/**
	 * Get series files description
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @return
	 * @throws Exception
	 */
	EPADFileList getFileDescriptions(SeriesReference seriesReference, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception;

	/**
	 * Get series files description
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @return
	 * @throws Exception
	 */
	List<EPADFile> getEPADFiles(SeriesReference seriesReference, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception;

	/**
	 * Get templates
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADTemplateContainerList getTemplateDescriptions(String username, String sessionID) throws Exception;

	/**
	 * Get system-wide templates
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADTemplateContainerList getSystemTemplateDescriptions(String username, String sessionID) throws Exception;

	/**
	 * Get project templates
	 * @param projectID
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADTemplateContainerList getTemplateDescriptions(String projectID, String username, String sessionID) throws Exception;
	
	/**
	 * @param seriesReference
	 * @param filename
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADFile getFileDescription(SeriesReference seriesReference, String filename, String username, String sessionID) throws Exception;

	/**
	 * create Subject AIM
	 * @param username
	 * @param subjectRefernece
	 * @param aimID
	 * @param aimFile
	 * @param sessionID
	 * @return
	 */
	String createSubjectAIM(String username, SubjectReference subjectRefernece, String aimID, File aimFile, String sessionID);
	
	/**
	 * create Study AIM
	 * @param username
	 * @param studyReference
	 * @param aimID
	 * @param aimFile
	 * @param sessionID
	 * @return
	 */
	String createStudyAIM(String username, StudyReference studyReference, String aimID, File aimFile, String sessionID);

	/**
	 * create Series
	 * @param username
	 * @param seriesReference
	 * @param description
	 * @param seriesDate
	 * @param modality
	 * @param referencedSeries
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADSeries createSeries(String username, SeriesReference seriesReference, String description, Date seriesDate, String modality, String referencedSeries, String sessionID) throws Exception;

	/**
	 * create Project AIM (does not make senss, just for completeness)
	 * @param username
	 * @param projectReference
	 * @param aimID
	 * @param aimFile
	 * @param sessionID
	 * @return
	 */
	String createProjectAIM(String username, ProjectReference projectReference, String aimID, File aimFile, String sessionID);

	/**
	 * create Series AIM
	 * @param username
	 * @param seriesReference
	 * @param aimID
	 * @param aimFile
	 * @param sessionID
	 * @return
	 */
	String createSeriesAIM(String username, SeriesReference seriesReference, String aimID, File aimFile, String sessionID);

	/**
	 * create Image AIM
	 * @param username
	 * @param imageReference
	 * @param aimID
	 * @param aimFile
	 * @param sessionID
	 * @return
	 */
	String createImageAIM(String username, ImageReference imageReference, String aimID, File aimFile, String sessionID);

	/**
	 * create Frame AIM
	 * @param username
	 * @param frameReference
	 * @param aimID
	 * @param aimFile
	 * @param sessionID
	 * @return
	 */
	String createFrameAIM(String username, FrameReference frameReference, String aimID, File aimFile, String sessionID);

	/**
	 * Set Subject Status per user
	 * @param subjectReference
	 * @param sessionID
	 * @param username
	 * @return
	 * @throws Exception
	 */
	String setSubjectStatus(SubjectReference subjectReference, String sessionID, String username) throws Exception;
	
	/**
	 * create/modify user
	 * @param loggedInUser
	 * @param username
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param password
	 * @param oldpassword
	 * @param addPermission
	 * @param removePermissions
	 * @throws Exception
	 */
	void createOrModifyUser(String loggedInUser, String username, String firstname, String lastname, String email, 
			String password, String oldpassword, String[]addPermission, String[] removePermissions) throws Exception;
	
	/**
	 * enable user
	 * @param loggedInUser
	 * @param username
	 * @throws Exception
	 */
	void enableUser(String loggedInUser, String username) throws Exception;
	
	/**
	 * disable user
	 * @param loggedInUser
	 * @param username
	 * @throws Exception
	 */
	void disableUser(String loggedInUser, String username) throws Exception;
	
	/**
	 * delete user
	 * @param loggedInUser
	 * @param username
	 * @throws Exception
	 */
	void deleteUser(String loggedInUser, String username) throws Exception;
	
	/**
	 * Add reviewer for a user
	 * @param loggedInUser
	 * @param username
	 * @param reviewer
	 * @throws Exception
	 */
	void addReviewer(String loggedInUser, String username, String reviewer) throws Exception;
	
	/**
	 * Add reviewee for a user
	 * @param loggedInUser
	 * @param username
	 * @param reviewee
	 * @throws Exception
	 */
	void addReviewee(String loggedInUser, String username, String reviewee) throws Exception;
	
	/**
	 * Remove reviewer for a user
	 * @param loggedInUser
	 * @param username
	 * @param reviewer
	 * @throws Exception
	 */
	void removeReviewer(String loggedInUser, String username, String reviewer) throws Exception;
	
	/**
	 * @param loggedInUser
	 * @param username
	 * @param reviewee
	 * @throws Exception
	 */
	void removeReviewee(String loggedInUser, String username, String reviewee) throws Exception;
	
	/**
	 * @param projectID
	 * @param sessionID
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int projectDelete(String projectID, String sessionID, String username) throws Exception;

	/**
	 * @param subjectReference
	 * @param sessionID
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int subjectDelete(SubjectReference subjectReference, String sessionID, String username) throws Exception;

	/**
	 * @param studyReference
	 * @param sessionID
	 * @param deleteAims
	 * @param username
	 * @return
	 * @throws Exception
	 */
	String studyDelete(StudyReference studyReference, String sessionID, boolean deleteAims, String username) throws Exception;

	/**
	 * Delete series
	 * @param seriesReference
	 * @param sessionID
	 * @param deleteAims
	 * @param username
	 * @return
	 */
	String seriesDelete(SeriesReference seriesReference, String sessionID, boolean deleteAims, String username) throws Exception;
	
	/**
	 * Delete series
	 * @param seriesReference
	 * @param deleteAims
	 * @return
	 */
	String deleteSeries(SeriesReference seriesReference, boolean deleteAims);

	/**
	 * Delete project aim
	 * @param projectReference
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int projectAIMDelete(ProjectReference projectReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	/**
	 * Delete subject aim
	 * @param subjectReference
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int subjectAIMDelete(SubjectReference subjectReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	/**
	 * Delete study aim
	 * @param studyReference
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int studyAIMDelete(StudyReference studyReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	/**
	 * Delete series aim
	 * @param seriesReference
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int seriesAIMDelete(SeriesReference seriesReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	/**
	 * Delete image aim
	 * @param imageReference
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int imageAIMDelete(ImageReference imageReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	/**
	 * Delete frame aim
	 * @param frameReference
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int frameAIMDelete(FrameReference frameReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	/**
	 * Delete aim
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int aimDelete(String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;
	
	/**
	 * Delete all series aim
	 * @param seriedUID
	 * @param deleteDSOs
	 */
	void deleteAllSeriesAims(String seriedUID, boolean deleteDSOs);
	
	/**
	 * Delete all study aim
	 * @param studyUID
	 * @param deleteDSOs
	 */
	void deleteAllStudyAims(String studyUID, boolean deleteDSOs);

	/**
	 * Delete all project aim
	 * @param projectID
	 * @param subjectID
	 * @param studyUID
	 * @param seriesUID
	 * @param deleteDSOs
	 */
	void deleteAllAims(String projectID, String subjectID, String studyUID, String seriesUID, boolean deleteDSOs);

	/**
	 * Delete subject file
	 * @param username
	 * @param subjectReference
	 * @param fileName
	 * @throws Exception
	 */
	void deleteFile(String username, SubjectReference subjectReference, String fileName) throws Exception;

	/**
	 * Delete project file
	 * @param username
	 * @param projectReference
	 * @param fileName
	 * @throws Exception
	 */
	void deleteFile(String username, ProjectReference projectReference, String fileName) throws Exception;

	/**
	 * Delete study file
	 * @param username
	 * @param studyReference
	 * @param fileName
	 * @throws Exception
	 */
	void deleteFile(String username, StudyReference studyReference, String fileName) throws Exception;

	/**
	 * Delete series file
	 * @param username
	 * @param seriesReference
	 * @param fileName
	 * @throws Exception
	 */
	void deleteFile(String username, SeriesReference seriesReference, String fileName) throws Exception;

	/**
	 * Delete file
	 * @param username
	 * @param fileName
	 * @throws Exception
	 */
	public void deleteFile(String username, String fileName) throws Exception;
	
	/**
	 * Get project aims
	 * @param projectReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getProjectAIMDescriptions(ProjectReference projectReference, String username, String sessionID);

	/**
	 * @param projectReference
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getProjectAIMDescription(ProjectReference projectReference, String aimID, String username, String sessionID);

	/**
	 * Get subject aims
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getSubjectAIMDescriptions(SubjectReference subjectReference, String username, String sessionID);

	/**
	 * @param subjectReference
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getSubjectAIMDescription(SubjectReference subjectReference, String aimID, String username, String sessionID);

	/**
	 * Get study aims
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getStudyAIMDescriptions(StudyReference studyReference, String username, String sessionID);

	/**
	 * @param studyReference
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getStudyAIMDescription(StudyReference studyReference, String aimID, String username, String sessionID);

	/**
	 * Get series aims
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getSeriesAIMDescriptions(SeriesReference seriesReference, String username, String sessionID);

	/**
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @param includeStudyAims
	 * @return
	 */
	EPADAIMList getSeriesAIMDescriptions(SeriesReference seriesReference, String username, String sessionID, boolean includeStudyAims);

	/**
	 * @param seriesReference
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getSeriesAIMDescription(SeriesReference seriesReference, String aimID, String username, String sessionID);

	/**
	 * Get image aims
	 * @param imageReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getImageAIMDescriptions(ImageReference imageReference, String username, String sessionID);

	/**
	 * @param imageReference
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getImageAIMDescription(ImageReference imageReference, String aimID, String username, String sessionID);

	/**
	 * Get frame aims
	 * @param frameReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getFrameAIMDescriptions(FrameReference frameReference, String username, String sessionID);

	/**
	 * @param frameReference
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getFrameAIMDescription(FrameReference frameReference, String aimID, String username, String sessionID);

	/**
	 * Search for aims
	 * @param projectID
	 * @param aimSearchType
	 * @param searchValue
	 * @param username
	 * @param sessionID
	 * @param start
	 * @param count
	 * @return
	 */
	EPADAIMList getAIMDescriptions(String projectID, AIMSearchType aimSearchType, String searchValue, String username, String sessionID, int start, int count);
	
	/**
	 * Get aim by user
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getAIMDescriptionsForUser(String username, String sessionID);
	
	/**
	 * Get aim by aimID
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getAIMDescription(String aimID, String username, String sessionID);
	
	/**
	 * Get worklists for user
	 * @param username
	 * @return
	 * @throws Exception
	 */
	EPADWorklistList getWorkListsForUser(String loggedInUser, String username) throws Exception;
	
	/**
	 * Get studies for worklist
	 * @param username
	 * @param workListID
	 * @return
	 * @throws Exception
	 */
	EPADWorklistStudyList getWorkListStudies(String loggedInUser, String username, String workListID) throws Exception;
	
	/**
	 * Get subjects for worklist
	 * @param username
	 * @param workListID
	 * @return
	 * @throws Exception
	 */
	EPADWorklistSubjectList getWorkListSubjects(String loggedInUser, String username, String workListID) throws Exception;
	
	//EPADWorklistStudyList getWorkListSubjectStudies(ProjectReference projectReference, String username, String subjectID, String workListID) throws Exception;
	
	//EPADWorklist getWorkList(ProjectReference projectReference, String username) throws Exception;
	
	/**
	 * Get Worklist by ID
	 * @param username
	 * @param workListID
	 * @return
	 * @throws Exception
	 */
	EPADWorklist getWorkListByID(String loggedInUser, String username, String workListID) throws Exception;

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
	 * @param seriesUID
	 * @return
	 */
	Set<DICOMFileDescription> getUnprocessedDICOMFilesInSeries(String seriesUID);

	/**
	 * @param seriesUID
	 * @param imagUID
	 * @return
	 */
	Set<DICOMFileDescription> getDICOMFilesInSeries(String seriesUID, String imagUID);

	/**
	 * @param studyUID
	 * @param deleteAims
	 */
	void deleteStudyFromEPadAndDcm4CheeDatabases(String studyUID, boolean deleteAims);

	/**
	 * @param studyUIDs
	 */
	void deleteStudiesFromEPadAndDcm4CheeDatabases(Set<String> studyUIDs);

	/**
	 * @param sessionID
	 * @param projectID
	 * @param subjectID
	 * @param searchFilter
	 * @return
	 * @throws Exception
	 */
	Set<String> getExamTypesForSubject(String sessionID, String projectID, String subjectID, EPADSearchFilter searchFilter) throws Exception;

	/**
	 * @param subjectID
	 * @return
	 */
	Set<String> getExamTypesForSubject(String subjectID);

	/**
	 * @param studyUID
	 * @return
	 */
	Set<String> getExamTypesForStudy(String studyUID);

	/**
	 * @param projectID
	 * @param subjectID
	 * @param sessionID
	 * @param searchFilter
	 * @return
	 */
	Set<String> getSeriesUIDsForSubject(String projectID, String subjectID, String sessionID,
			EPADSearchFilter searchFilter);
	
	/**
	 * @param username
	 * @return
	 * @throws Exception
	 */
	Collection<EPADSession> getCurrentSessions(String username) throws Exception;
	
	/**
	 * @param username
	 * @return
	 * @throws Exception
	 */
	EPADEventLogList getEventLogs(String loggedInUser, String username, int start, int count) throws Exception;
	EPADObjectList getTaskStatuses(String loggedInUser, String username) throws Exception;
}
