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

import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectType;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.User.EventLog;
import edu.stanford.epad.epadws.models.UserRole;

public interface EpadProjectOperations {

	void clearCache();
	
	Project createProject(String loggedInUser, String projectId, String projectName, String description, ProjectType type) throws Exception;
	Project updateProject(String loggedInUser, String projectId, String projectName, String description, ProjectType type) throws Exception;
	User createUser(String loggedInUser, String username, String firstName, String lastName, String email, String password) throws Exception;
	User updateUser(String loggedInUser, String username, String firstName, String lastName, String email, String password, String oldpassword) throws Exception;
	void enableUser(String loggedInUser, String username) throws Exception;
	void disableUser(String loggedInUser, String username) throws Exception;
	
	void addUserToProject(String loggedInUser, String projectId, String username, UserRole role) throws Exception;
	void removeUserFromProject(String loggedInUser, String projectId, String username) throws Exception;
	void setUserRoleForProject(String loggedInUser, String projectId, String username, UserRole role) throws Exception;
	
	Subject createSubject(String loggedInUser, String subjectUID, String name, Date dob, String gender) throws Exception;
	Study createStudy(String loggedInUser, String studyUID, String subjectUID) throws Exception;
	void addSubjectToProject(String loggedInUser, String subjectUID, String projectId) throws Exception;
	void addStudyToProject(String loggedInUser, String studyUID, String subjectUID, String projectId) throws Exception;
	
	void removeSubjectFromProject(String loggedInUser, String subjectUID, String projectId) throws Exception;
	void removeStudyFromProject(String loggedInUser, String studyUID, String projectId) throws Exception;
	
	Project getProject(String projectId) throws Exception;
	
	User getUser(String username) throws Exception;
	User getUserByEmail(String email) throws Exception;
	
	Subject getSubject(String subjectUID) throws Exception;
	Study getStudy(String studyUID) throws Exception;
	
	List<Project> getAllProjects() throws Exception;
	List<User> getAllUsers() throws Exception;
	List<Project> getPublicProjects() throws Exception;
	List<Project> getProjectsForUser(String username) throws Exception;
	Project getProjectForUser(String username, String projectID) throws Exception;
	List<User> getUsersForProject(String projectId) throws Exception;
	List<User> getUsersWithRoleForProject(String projectId) throws Exception;
	List<Subject> getSubjectsForProject(String projectId) throws Exception;
	Subject getSubjectForProject(String projectId, String subjectUID) throws Exception;
	List<Project> getProjectsForSubject(String subjectUID) throws Exception;
	Subject getSubjectFromName(String subjectName) throws Exception;
	Subject getSubjectFromNameForProject(String subjectName, String projectID) throws Exception;
	Project getFirstProjectForStudy(String studyUID) throws Exception;
	
	List<Study> getStudiesForProjectAndSubject(String projectId, String subjectUID) throws Exception;
	boolean isStudyInProjectAndSubject(String projectId, String subjectUID, String studyUID) throws Exception;
	List<Study> getAllStudiesForProject(String projectId) throws Exception;
	List<Study> getStudiesForSubject(String subjectUID) throws Exception;

	void setUserStatusForProjectAndSubject(String loggedInUser, String projectID, String subjectUID, String status) throws Exception;
	String getUserStatusForProjectAndSubject(String loggedInUser, String projectID, String subjectUID) throws Exception;
	Map<String, String> getUserStatusForProjectSubjects(String loggedInUser, String projectID) throws Exception;
	Map<Long, String> getUserStatusForProjectSubjectIds(String loggedInUser, String projectID) throws Exception;
	
	boolean isCollaborator(String username, String projectID) throws Exception;
	boolean isMember(String username, String projectID) throws Exception;
	boolean isOwner(String username, String projectID) throws Exception;
	UserRole getUserProjectRole(String username, String projectID) throws Exception;

	void deleteProject(String username, String projectID) throws Exception;
	void deleteSubject(String username, String subjectUID, String projectID) throws Exception;
	void deleteStudy(String username, String studyUID, String subjectUID, String projectID) throws Exception;
	void deleteSubject(String username, String subjectUID) throws Exception;
	void deleteStudy(String username, String studyUID) throws Exception;
	
	List<EventLog> getUserLogs(String username);
	void addUserLog(String username, EventLog eventLog);
}
