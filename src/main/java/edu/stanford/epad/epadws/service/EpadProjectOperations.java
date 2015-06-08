package edu.stanford.epad.epadws.service;

//Copyright (c) 2014 The Board of Trustees of the Leland Stanford Junior University
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

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.stanford.epad.dtos.EPADUserList;
import edu.stanford.epad.epadws.models.EpadFile;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.models.NonDicomSeries;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectType;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.User.EventLog;
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
			String email, String password, List<String> addPermissions, List<String> removePermissions) throws Exception;
	
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
			String password, String oldpassword, List<String> addPermissions, List<String> removePermissions) throws Exception;
	
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
	void addUserToProject(String loggedInUser, String projectId, String username, UserRole role) throws Exception;
	
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
	void userErrorLog(String username, String message);
	void userWarningLog(String username, String message);
	void userInfoLog(String username, String message);
	
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
	
	/**
	 * Create Study record in database
	 * @param loggedInUser
	 * @param studyUID
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	NonDicomSeries createNonDicomSeries(String loggedInUser, String seriesUID, String studyUID, String description, Date seriesDate) throws Exception;
	
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
	
	void enableFile(String loggedInUser, String projectID, String subjectUID, String studyUID, String seriesUID, String filename) throws Exception;
	
	void disableFile(String loggedInUser, String projectID, String subjectUID, String studyUID, String seriesUID, String filename) throws Exception;
	
	void enableTemplate(String loggedInUser, String projectID, String subjectUID, String studyUID, String seriesUID, String templateName) throws Exception;
	
	void disableTemplate(String loggedInUser, String projectID, String subjectUID, String studyUID, String seriesUID, String templateName) throws Exception;
	
	EpadFile updateEpadFile(long fileID, String filename, String description, String fileType, String mimeType) throws Exception;	
	
	List<String> getDisabledTemplates(String projectID) throws Exception;	

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
	List<EventLog> getUserLogs(String username);

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
}
