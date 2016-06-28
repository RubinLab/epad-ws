/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
package edu.stanford.epad.epadws.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.stanford.epad.dtos.AnnotationStatus;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.models.EpadFile;
import edu.stanford.epad.epadws.models.EpadStatistics;
import edu.stanford.epad.epadws.models.EventLog;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.models.NonDicomSeries;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectType;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.UserRole;
import edu.stanford.epad.epadws.models.dao.AbstractDAO;

/**
 * All Epad User/Project/Subject/Study related operations to replace XNAT functionality
 * 
 * @author Dev Gude
 *
 */
public interface EpadProjectOperations {

	/**
	 * 
	 */
	void clearCache();
	int getCacheSize();
	
	/**
	 * Creates a project record in database
	 * 
	 * @param loggedInUser
	 * @param projectId
	 * @param projectName
	 * @param description
	 * @param type
	 * @return
	 * @throws Exception
	 */
	Project createProject(String loggedInUser, String projectId, String projectName, String description, String defaultTemplate, ProjectType type) throws Exception;
	
	/**
	 * Updates project fields in database (all arguments except projectId are optional)
	 * @param loggedInUser
	 * @param projectId
	 * @param projectName
	 * @param description
	 * @param type
	 * @return
	 * @throws Exception
	 */
	Project updateProject(String loggedInUser, String projectId, String projectName, String description, String defaultTemplate, ProjectType type) throws Exception;
	
	/**
	 * Creates a user record in database
	 * 
	 * @param loggedInUser
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 * @return
	 * @throws Exception
	 */
	User createUser(String loggedInUser, String username, String firstName, String lastName, 
			String email, String password, String colorpreference, List<String> addPermissions, List<String> removePermissions) throws Exception;
	
	/**
	 * Updates a user record in database, fields can be null except username, password and oldpassword should match 
	 *
	 * @param loggedInUser
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 * @param oldpassword
	 * @return
	 * @throws Exception
	 */
	User updateUser(String loggedInUser, String username, String firstName, String lastName, String email, 
			String password, String oldpassword, String colorpreference, List<String> addPermissions, List<String> removePermissions) throws Exception;
	
	/**
	 * Set admin flag in user record
	 * @param loggedInUser
	 * @param username
	 * @throws Exception
	 */
	void setAdmin(String loggedInUser, String username) throws Exception;
	
	/**
	 * Reset admin flag in user record
	 * @param loggedInUser
	 * @param username
	 * @throws Exception
	 */
	void resetAdmin(String loggedInUser, String username) throws Exception;
	
	/**
	 * Set enabled flag in user record
	 * @param loggedInUser
	 * @param username
	 * @throws Exception
	 */
	void enableUser(String loggedInUser, String username) throws Exception;
	
	/**
	 * Reset enabled flag in user record
	 * @param loggedInUser
	 * @param username
	 * @throws Exception
	 */
	void disableUser(String loggedInUser, String username) throws Exception;
	
	/**
	 * Delete user record. Exception on foreign key violation (if related records exist)
	 * @param loggedInUser
	 * @param username
	 * @throws Exception
	 */
	void deleteUser(String loggedInUser, String username) throws Exception;
	
	/**
	 * Add User to project
	 * @param loggedInUser
	 * @param projectId
	 * @param username
	 * @param role - Owner, Member, Collaborator
	 * @throws Exception
	 */
	void addUserToProject(String loggedInUser, String projectId, String username, UserRole role, String defaultTemplate) throws Exception;
	
	/**
	 * Remove user from project
	 * @param loggedInUser
	 * @param projectId
	 * @param username
	 * @throws Exception
	 */
	void removeUserFromProject(String loggedInUser, String projectId, String username) throws Exception;
	
	/**
	 * Set user role in project
	 * @param loggedInUser
	 * @param projectId
	 * @param username
	 * @param role - Owner, Member, Collaborator
	 * @throws Exception
	 */
	void setUserRoleForProject(String loggedInUser, String projectId, String username, UserRole role) throws Exception;
	
	/*
	 * Add logs to user object
	 */
	void updateUserTaskStatus(String username, String type, String target, String status, Date startTime, Date completeTime);
	void updateUserTaskStatus(String username, String type, String projectID, String target, String status, Date startTime, Date completeTime);
	
	void createEventLog(String username, String projectID, String subjectID, String studyUID, String seriesUID, String imageUID, String aimID, String function, String params);
	void createEventLog(String username, String projectID, String subjectID, String studyUID, String seriesUID, String imageUID, String aimID, String filename, String function, String params, boolean error);
	
	EpadStatistics getUserStatistics(String loggediInUser, String username, boolean exceptionOnErr) throws Exception;
	
	/**
	 * Create Subject record in database
	 * @param loggedInUser
	 * @param subjectUID
	 * @param name
	 * @param dob
	 * @param gender
	 * @return
	 * @throws Exception
	 */
	Subject createSubject(String loggedInUser, String subjectUID, String name, Date dob, String gender) throws Exception;
	Subject createSubject(String loggedInUser, String subjectUID, String name, Date dob, String gender, boolean changeOwner) throws Exception;
	
	/**
	 * Create Study record in database
	 * @param loggedInUser
	 * @param studyUID
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	Study createStudy(String loggedInUser, String studyUID, String subjectUID, String description) throws Exception;
	Study createStudy(String loggedInUser, String studyUID, String subjectUID, String description, Date studyDate) throws Exception;
	Study createStudy(String loggedInUser, String studyUID, String subjectUID, String description, Date studyDate, boolean changeOwner) throws Exception;
	
	/**
	 * Create Study record in database
	 * @param loggedInUser
	 * @param studyUID
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	NonDicomSeries createNonDicomSeries(String loggedInUser, String seriesUID, String studyUID, String description, Date seriesDate, String modality, String referencedSeries) throws Exception;
	
	/**
	 * Add Subject/Studies to Project
	 * @param loggedInUser
	 * @param subjectUID
	 * @param projectId
	 * @throws Exception
	 */
	void addSubjectToProject(String loggedInUser, String subjectUID, String projectId) throws Exception;
	
	/**
	 * Add Study to Project
	 * @param loggedInUser
	 * @param studyUID
	 * @param subjectUID
	 * @param projectId
	 * @throws Exception
	 */
	void addStudyToProject(String loggedInUser, String studyUID, String subjectUID, String projectId) throws Exception;
	
	/**
	 * Remove Subject/Studies from project
	 * @param loggedInUser
	 * @param subjectUID
	 * @param projectId
	 * @throws Exception
	 */
	void removeSubjectFromProject(String loggedInUser, String subjectUID, String projectId) throws Exception;
	
	/**
	 * Remove Study from Project
	 * @param loggedInUser
	 * @param studyUID
	 * @param projectId
	 * @throws Exception
	 */
	void removeStudyFromProject(String loggedInUser, String studyUID, String projectId) throws Exception;
	
	/**
	 * Check if Subject is in Project
	 * @param subjectUID
	 * @param projectId
	 * @throws Exception
	 */
	boolean isSubjectInProject(String subjectUID, String projectId) throws Exception;

	/**
	 * Create File Record
	 * @param loggedInUser
	 * @param projectID
	 * @param subjectUID
	 * @param studyUID
	 * @param seriesUID
	 * @param file
	 * @param filename
	 * @param description
	 * @param type
	 * @return
	 * @throws Exception
	 */
	EpadFile createFile(String loggedInUser, String projectID, String subjectUID, String studyUID, String seriesUID, File file, String filename, String description, FileType type) throws Exception;
	
	/**
	 * Get Project from database
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	Project getProject(String projectId) throws Exception;
	
	/**
	 * Get User from database
	 * @param username
	 * @return
	 * @throws Exception
	 */
	User getUser(String username) throws Exception;
	
	/**
	 * Get User from database by email
	 * @param email
	 * @return
	 * @throws Exception
	 */
	User getUserByEmail(String email) throws Exception;
	
	/**
	 * Get Subject from database
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	Subject getSubject(String subjectUID) throws Exception;
	
	/**
	 * Get Study from database
	 * @param studyUID
	 * @return
	 * @throws Exception
	 */
	Study getStudy(String studyUID) throws Exception;
	
	/**
	 * Get All Project records
	 * @return
	 * @throws Exception
	 */
	List<Project> getAllProjects() throws Exception;
	
	/**
	 * Get all user records with projects/roles
	 * @return
	 * @throws Exception
	 */
	List<User> getAllUsers() throws Exception;
	
	/**
	 * Get public projects
	 * @return
	 * @throws Exception
	 */
	List<Project> getPublicProjects() throws Exception;
	
	/**
	 * Get Projects for a user
	 * @param username
	 * @return
	 * @throws Exception
	 */
	List<Project> getProjectsForUser(String username) throws Exception;
	
	/**
	 * Get single project for a user
	 * @param username
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	Project getProjectForUser(String username, String projectID) throws Exception;
	
	/**
	 * Get Users for a project
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	List<User> getUsersForProject(String projectId) throws Exception;
	
	/**
	 * Get Users with role for a project
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	List<User> getUsersWithRoleForProject(String projectId) throws Exception;
	
	/**
	 * Get Subjects for a project
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	List<Subject> getSubjectsForProject(String projectId) throws Exception;
	List<Subject> getSubjectsForProject(String projectId, String sortBy) throws Exception;
	List<Subject> getUnassignSubjects() throws Exception;
	/**
	 * Get Subject for project
	 * @param projectId
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	Subject getSubjectForProject(String projectId, String subjectUID) throws Exception;
	
	/**
	 * Get projects for a subject
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	List<Project> getProjectsForSubject(String subjectUID) throws Exception;
	
	/**
	 * Get projects for a study
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	List<Project> getProjectsForStudy(String studyUID) throws Exception;
	
	/**
	 * Get Subject/Patient by name
	 * @param subjectName
	 * @return
	 * @throws Exception
	 */
	Subject getSubjectFromName(String subjectName) throws Exception;
	
	/**
	 * Get Subject for a project by name
	 * @param subjectName
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	Subject getSubjectFromNameForProject(String subjectName, String projectID) throws Exception;
	
	/**
	 * Get First Project for  a Study
	 * @param studyUID
	 * @return
	 * @throws Exception
	 */
	Project getFirstProjectForStudy(String studyUID) throws Exception;
	
	/**
	 * Get Studies for Project/Subject
	 * @param projectId
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	List<Study> getStudiesForProjectAndSubject(String projectId, String subjectUID) throws Exception;
	
	/**
	 * Check if Study in Project
	 * @param projectId
	 * @param subjectUID
	 * @param studyUID
	 * @return
	 * @throws Exception
	 */
	boolean isStudyInProjectAndSubject(String projectId, String subjectUID, String studyUID) throws Exception;
	
	/**
	 * Get all studies for project
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	List<Study> getAllStudiesForProject(String projectId) throws Exception;
	
	/**
	 * Get Studies for a subject
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	List<Study> getStudiesForSubject(String subjectUID) throws Exception;
	Subject getSubjectForStudy(String studyUID) throws Exception;
	
	/**
	 * Get Non-dicom series for a study
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	List<NonDicomSeries> getNonDicomSeriesForStudy(String studyUID) throws Exception;
	
	/**
	 * Get Non-dicom series
	 * @param seriesUID
	 * @return
	 * @throws Exception
	 */
	NonDicomSeries getNonDicomSeries(String seriesUID) throws Exception;

	/**
	 * Set user status for project and subject
	 * @param loggedInUser
	 * @param projectID
	 * @param subjectUID
	 * @param status
	 * @throws Exception
	 */
	void setUserStatusForProjectAndSubject(String loggedInUser, String projectID, String subjectUID, String status) throws Exception;
	
	/**
	 * Get user status for project and subject
	 * @param loggedInUser
	 * @param projectID
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	String getUserStatusForProjectAndSubject(String loggedInUser, String projectID, String subjectUID) throws Exception;
	
	/**
	 * Get map for subject/status for a user/project
	 * @param loggedInUser
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	Map<String, String> getUserStatusForProjectSubjects(String loggedInUser, String projectID) throws Exception;
	
	/**
	 * Get map for subjectId/status for a user/project
	 * @param loggedInUser
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	Map<Long, String> getUserStatusForProjectSubjectIds(String loggedInUser, String projectID) throws Exception;

	/**
	 * Get File for project/subject/study/series by filename 
	 * @param projectID
	 * @param subjectUID
	 * @param studyUID
	 * @param seriesUID
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	EpadFile getEpadFile(String projectID, String subjectUID, String studyUID, String seriesUID, String filename) throws Exception;	

	/**
	 * Get Files for project/subject/study/series by filetype 
	 * @param projectID
	 * @param subjectUID
	 * @param studyUID
	 * @param seriesUID
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	List<EpadFile> getEpadFiles(String projectID, String subjectUID, String studyUID, String seriesUID, FileType fileType, boolean toplevelOnly) throws Exception;	
	
	/**
	 * Get files for project
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	List<EpadFile> getProjectFiles(String projectID, boolean toplevelOnly) throws Exception;
	
	/**
	 * Get files for project/subject
	 * @param projectID
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	List<EpadFile> getSubjectFiles(String projectID, String subjectUID, boolean toplevelOnly) throws Exception;

	/**
	 * Get files for project/subject/study
	 * @param projectID
	 * @param subjectUID
	 * @param studyUID
	 * @return
	 * @throws Exception
	 */
	List<EpadFile> getStudyFiles(String projectID, String subjectUID, String studyUID, boolean toplevelOnly) throws Exception;
	
	/**
	 * Get files for project/subject/study/series
	 * @param projectID
	 * @param subjectUID
	 * @param studyUID
	 * @param seriesUID
	 * @return
	 * @throws Exception
	 */
	List<EpadFile> getSeriesFiles(String projectID, String subjectUID, String studyUID, String seriesUID) throws Exception;	
	
	/**
	 * @param loggedInUser
	 * @param projectID
	 * @param subjectUID
	 * @param studyUID
	 * @param seriesUID
	 * @param filename
	 * @throws Exception
	 */
	void enableFile(String loggedInUser, String projectID, String subjectUID, String studyUID, String seriesUID, String filename) throws Exception;
	
	/**
	 * @param loggedInUser
	 * @param projectID
	 * @param subjectUID
	 * @param studyUID
	 * @param seriesUID
	 * @param filename
	 * @throws Exception
	 */
	void disableFile(String loggedInUser, String projectID, String subjectUID, String studyUID, String seriesUID, String filename) throws Exception;
	
	/**
	 * @param loggedInUser
	 * @param projectID
	 * @param subjectUID
	 * @param studyUID
	 * @param seriesUID
	 * @param templateName
	 * @throws Exception
	 */
	void enableTemplate(String loggedInUser, String projectID, String subjectUID, String studyUID, String seriesUID, String templateName) throws Exception;
	
	/**
	 * @param loggedInUser
	 * @param projectID
	 * @param subjectUID
	 * @param studyUID
	 * @param seriesUID
	 * @param templateName
	 * @throws Exception
	 */
	void disableTemplate(String loggedInUser, String projectID, String subjectUID, String studyUID, String seriesUID, String templateName) throws Exception;
	
	/**
	 * @param fileID
	 * @param filename
	 * @param description
	 * @param fileType
	 * @param mimeType
	 * @return
	 * @throws Exception
	 */
	EpadFile updateEpadFile(long fileID, String filename, String description, String fileType, String mimeType) throws Exception;	
	
	/**
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	List<String> getDisabledTemplates(String projectID) throws Exception;	

	/**
	 * @param project
	 * @param file
	 */
	void linkFileToProject(String loggedInUser, Project project, EpadFile file) throws Exception;
	
	/**
	 * @param project
	 * @param file
	 */
	void unlinkFileFromProject(String loggedInUser, Project project, EpadFile file) throws Exception;
	
	/**
	 * @param project
	 * @return
	 * @throws Exception
	 */
	List<EpadFile> getLinkedFiles(Project project) throws Exception;
	
	/**
	 * @param username
	 * @return
	 * @throws Exception
	 */
	boolean isAdmin(String username) throws Exception;
	
	/**
	 * Check if user is in project
	 * @param username
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	boolean hasAccessToProject(String username, String projectID) throws Exception;
	
	/**
	 * Check if user is in project
	 * @param username
	 * @param project key
	 * @return
	 * @throws Exception
	 */
	boolean hasAccessToProject(String username, long id) throws Exception;
	
	/**
	 * Check if collaborator
	 * @param username
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	boolean isCollaborator(String username, String projectID) throws Exception;
	
	/**
	 * Check if member
	 * @param username
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	boolean isMember(String username, String projectID) throws Exception;

	/**
	 * Check if owner
	 * @param username
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	boolean isOwner(String username, String projectID) throws Exception;

	/**
	 * Get project role
	 * @param username
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	UserRole getUserProjectRole(String username, String projectID) throws Exception;

	/**
	 * Delete project
	 * @param username
	 * @param projectID
	 * @throws Exception
	 */
	void deleteProject(String username, String projectID) throws Exception;
	
	/**
	 * Delete subject from project
	 * @param username
	 * @param subjectUID
	 * @param projectID
	 * @throws Exception
	 */
	void deleteSubject(String username, String subjectUID, String projectID) throws Exception;
	
	/**
	 * Delete study from project
	 * @param username
	 * @param studyUID
	 * @param subjectUID
	 * @param projectID
	 * @throws Exception
	 */
	void deleteStudy(String username, String studyUID, String subjectUID, String projectID) throws Exception;
	
	/**
	 * Delete subject
	 * @param username
	 * @param subjectUID
	 * @throws Exception
	 */
	void deleteSubject(String username, String subjectUID) throws Exception;
	
	/**
	 * Delete study
	 * @param username
	 * @param studyUID
	 * @throws Exception
	 */
	void deleteStudy(String username, String studyUID) throws Exception;
	
	/**
	 * Delete Non-dicom series
	 * @param seriesUID
	 * @return
	 * @throws Exception
	 */
	void deleteNonDicomSeries(String seriesUID) throws Exception;
	
	/**
	 * Delete file
	 * @param loggedInUser
	 * @param projectID
	 * @param subjectUID
	 * @param studyUID
	 * @param seriesUID
	 * @param filename
	 * @throws Exception
	 */
	void deleteFile(String loggedInUser, String projectID, String subjectUID, String studyUID, String seriesUID, String filename) throws Exception;	
		
	/**
	 * Get event logs for this user
	 * @param username
	 * @return
	 */
	List<EventLog> getUseEventLogs(String username, int start, int count) throws Exception;

	/**
	 * Get reviewers for this user
	 * @param username
	 * @return
	 * @throws Exception
	 */
	List<User> getReviewers(String username) throws Exception;

	/**
	 * Get reviewees for this user
	 * @param username
	 * @return
	 * @throws Exception
	 */
	List<User> getReviewees(String username) throws Exception;
	
	/**
	 * Add reviewer to this user
	 * @param loggedInUser
	 * @param username
	 * @param reviewer
	 * @throws Exception
	 */
	void addReviewer(String loggedInUser, String username, String reviewer) throws Exception;
	
	/**
	 * Add reviewee to this user
	 * @param loggedInUser
	 * @param username
	 * @param reviewee
	 * @throws Exception
	 */
	void addReviewee(String loggedInUser, String username, String reviewee) throws Exception;
	
	/**
	 * Remove reviewer from this user
	 * @param loggedInUser
	 * @param username
	 * @param reviewer
	 * @throws Exception
	 */
	void removeReviewer(String loggedInUser, String username, String reviewer) throws Exception;
	
	/**
	 * Remove reviewee from this user
	 * @param loggedInUser
	 * @param username
	 * @param reviewee
	 * @throws Exception
	 */
	void removeReviewee(String loggedInUser, String username, String reviewee) throws Exception;	
	
	/**
	 * Get database object by primary key
	 * @param dbclass
	 * @param id
	 * @return
	 * @throws Exception
	 */
	AbstractDAO getDBObject(Class dbclass, long id) throws Exception;
	
	/**
	 * Sort db object list
	 * @param objects
	 * @param field
	 * @param ascending
	 * @return sorted list
	 * @throws Exception
	 */
	List sort(List<AbstractDAO> objects, String field, boolean ascending);
	
	/**
	 * get user count for a specific project with project uid
	 * @param projectId
	 * @return count
	 * @throws Exception
	 */
	long getUserCountProject(String projectId) throws Exception;
	
	/**
	 * get user count for a specific project with project id
	 * @param id
	 * @return count
	 * @throws Exception
	 */
	long getUserCountForProject(long id) throws Exception;
	
	/**
	 * Gets all the parameters and returns the annotation status for that specific user and series
	 * @param projectuid
	 * @param subjectuid
	 * @param studyuid
	 * @param seriesuid
	 * @param username
	 * @param numberOfSeries for cumulative status
	 * @return annotation status see AnnotationStatus class for values or null
	 * @author emelalkim
	 */
	AnnotationStatus getAnnotationStatusForUser(String projectUID, String subjectUID, String studyUID,
			String series_uid, String username, int numberOfSeries);
	
	
	/**
	 * Gets the number of users that are in the status annotating the specific series for the project (using model)
	 * @param projectUID
	 * @param subjectUID
	 * @param studyUID
	 * @param series_uid
	 * @param status annotation status
	 * @return count or 0
	 * @author emelalkim
	 */
	int getAnnotationStatusUserCount(String projectUID, String subjectUID, String studyUID, String series_uid,
			AnnotationStatus status);
	/**
	 * update annotation status for user
	 * if annotation status can be converted to an integer writes it
	 * if not checks if it is DONE
	 * if not writes error
	 * see AnnotationStatus class for the code values
	 * @param username
	 * @param seriesReference
	 * @param annotationStatus
	 * @param sessionID
	 * @throws Exception
	 */
	void updateAnnotationStatus(String username, SeriesReference seriesReference, String annotationStatus,
			String sessionID) throws Exception;
	
	/**
	 * updates the annotation status for whole study
	 * @param username
	 * @param studyReference
	 * @param annotationStatus
	 * @param sessionID
	 * @throws Exception
	 */
	void updateAnnotationStatus(String username, StudyReference studyReference, String annotationStatus,
			String sessionID) throws Exception;
	
	/**
	 * updates the annotation status for whole subject
	 * @param username
	 * @param subjectReference
	 * @param annotationStatus
	 * @param sessionID
	 * @throws Exception
	 */
	void updateAnnotationStatus(String username, SubjectReference subjectReference, String annotationStatus,
			String sessionID) throws Exception;
	
	/**
	 * updates the annotation status for whole project
	 * @param username
	 * @param projectReference
	 * @param annotationStatus
	 * @param sessionID
	 * @throws Exception
	 */
	void updateAnnotationStatus(String username, ProjectReference projectReference, String annotationStatus,
			String sessionID) throws Exception;
	
	
}
