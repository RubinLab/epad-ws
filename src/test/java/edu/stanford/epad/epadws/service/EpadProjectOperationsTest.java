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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectType;

public class EpadProjectOperationsTest {

    @BeforeClass
    public static void oneTimeSetUp() {
    	if (EpadDatabase.getInstance().getStartupTime() == -1)
    		EpadDatabase.getInstance().startup("1");
    }
 
    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    	System.out.println("@AfterClass - oneTimeTearDown");
    }
 
    @Before
    public void setUp() {
        System.out.println("@Before - setUp");
    }
 
    @After
    public void tearDown() {
         System.out.println("@After - tearDown");
    }
    
    @Test
    public void createDeleteProjectTest() {
    	Project project;
		try {
			project = DefaultEpadProjectOperations.getInstance().createProject("admin", "Test" + System.currentTimeMillis(), "Test" + System.currentTimeMillis(), "Test", ProjectType.PRIVATE);
	    	Assert.assertNotNull(project);
	    	DefaultEpadProjectOperations.getInstance().deleteProject("admin", project.getProjectId());
	    	project = DefaultEpadProjectOperations.getInstance().getProject(project.getProjectId());
	    	Assert.assertNull(project);
		} catch (Exception e) {
			Assert.fail();			
		}
    }

//  Need to create Tests for these
//	Project updateProject(String loggedInUser, String projectId, String projectName, String description, ProjectType type) throws Exception;
//	User createUser(String loggedInUser, String username, String firstName, String lastName, String email, String password) throws Exception;
//	User updateUser(String loggedInUser, String username, String firstName, String lastName, String email, String password, String oldpassword) throws Exception;
//	void enableUser(String loggedInUser, String username) throws Exception;
//	void disableUser(String loggedInUser, String username) throws Exception;
//	
//	void addUserToProject(String loggedInUser, String projectId, String username, UserRole role) throws Exception;
//	void removeUserFromProject(String loggedInUser, String projectId, String username) throws Exception;
//	void setUserRoleForProject(String loggedInUser, String projectId, String username, UserRole role) throws Exception;
//	void userErrorLog(String username, String message);
//	void userWarningLog(String username, String message);
//	void userInfoLog(String username, String message);
//	
//	Subject createSubject(String loggedInUser, String subjectUID, String name, Date dob, String gender) throws Exception;
//	Study createStudy(String loggedInUser, String studyUID, String subjectUID) throws Exception;
//	void addSubjectToProject(String loggedInUser, String subjectUID, String projectId) throws Exception;
//	void addStudyToProject(String loggedInUser, String studyUID, String subjectUID, String projectId) throws Exception;
//	
//	void removeSubjectFromProject(String loggedInUser, String subjectUID, String projectId) throws Exception;
//	void removeStudyFromProject(String loggedInUser, String studyUID, String projectId) throws Exception;
//
//	EpadFile createFile(String loggedInUser, String projectID, String subjectUID, String studyUID, String seriesUID, File file, String filename, String description) throws Exception;
//	
//	Project getProject(String projectId) throws Exception;
//	
//	User getUser(String username) throws Exception;
//	User getUserByEmail(String email) throws Exception;
//	
//	Subject getSubject(String subjectUID) throws Exception;
//	Study getStudy(String studyUID) throws Exception;
//	
//	List<Project> getAllProjects() throws Exception;
//	List<User> getAllUsers() throws Exception;
//	List<Project> getPublicProjects() throws Exception;
//	List<Project> getProjectsForUser(String username) throws Exception;
//	Project getProjectForUser(String username, String projectID) throws Exception;
//	List<User> getUsersForProject(String projectId) throws Exception;
//	List<User> getUsersWithRoleForProject(String projectId) throws Exception;
//	List<Subject> getSubjectsForProject(String projectId) throws Exception;
//	Subject getSubjectForProject(String projectId, String subjectUID) throws Exception;
//	List<Project> getProjectsForSubject(String subjectUID) throws Exception;
//	Subject getSubjectFromName(String subjectName) throws Exception;
//	Subject getSubjectFromNameForProject(String subjectName, String projectID) throws Exception;
//	Project getFirstProjectForStudy(String studyUID) throws Exception;
//	
//	List<Study> getStudiesForProjectAndSubject(String projectId, String subjectUID) throws Exception;
//	boolean isStudyInProjectAndSubject(String projectId, String subjectUID, String studyUID) throws Exception;
//	List<Study> getAllStudiesForProject(String projectId) throws Exception;
//	List<Study> getStudiesForSubject(String subjectUID) throws Exception;
//
//	void setUserStatusForProjectAndSubject(String loggedInUser, String projectID, String subjectUID, String status) throws Exception;
//	String getUserStatusForProjectAndSubject(String loggedInUser, String projectID, String subjectUID) throws Exception;
//	Map<String, String> getUserStatusForProjectSubjects(String loggedInUser, String projectID) throws Exception;
//	Map<Long, String> getUserStatusForProjectSubjectIds(String loggedInUser, String projectID) throws Exception;
//
//	EpadFile getEpadFile(String projectID, String subjectUID, String studyUID, String seriesUID, String filename) throws Exception;	
//	List<EpadFile> getProjectFiles(String projectID) throws Exception;
//	List<EpadFile> getSubjectFiles(String projectID, String subjectUID) throws Exception;
//	List<EpadFile> getStudyFiles(String projectID, String subjectUID, String studyUID) throws Exception;
//	List<EpadFile> getSeriesFiles(String projectID, String subjectUID, String studyUID, String seriesUID) throws Exception;
//	
//	boolean isCollaborator(String username, String projectID) throws Exception;
//	boolean isMember(String username, String projectID) throws Exception;
//	boolean isOwner(String username, String projectID) throws Exception;
//	UserRole getUserProjectRole(String username, String projectID) throws Exception;
//
//	void deleteProject(String username, String projectID) throws Exception;
//	void deleteSubject(String username, String subjectUID, String projectID) throws Exception;
//	void deleteStudy(String username, String studyUID, String subjectUID, String projectID) throws Exception;
//	void deleteSubject(String username, String subjectUID) throws Exception;
//	void deleteStudy(String username, String studyUID) throws Exception;
//	
//	void deleteFile(String loggedInUser, String projectID, String subjectUID, String studyUID, String seriesUID, String filename) throws Exception;	
//	
//	List<EventLog> getUserLogs(String username);
//	
//	AbstractDAO getDBObject(Class dbclass, long id) throws Exception;
}
