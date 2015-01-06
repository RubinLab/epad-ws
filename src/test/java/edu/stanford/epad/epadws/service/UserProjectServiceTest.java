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

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;

public class UserProjectServiceTest {

    @BeforeClass
    public static void oneTimeSetUp() {
        // one-time initialization code   
    	System.out.println("@BeforeClass - oneTimeSetUp");
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
    public void isCollaboratorTest() {
		try {
	    	boolean collaborator = UserProjectService.isCollaborator("", "admin", "unassigned");
	    	Assert.assertFalse(collaborator);
		} catch (Exception e) {
			Assert.fail();
		}
    }
//	
//  Need to create Tests for these
//	public static boolean isCollaborator(String sessionID, String username, String projectID)
//	
//	public static boolean isOwner(String sessionID, String username, String projectID) 
//	
//	public static boolean isMember(String sessionID, String username, String projectID) 
//	
//	public static Set<String> getAllProjectIDs() 
//	
//	public static Set<String> getAllStudyUIDsForProject(String projectID)
//	
//	public static Set<String> getStudyUIDsForSubject(String projectID, String patientID) 
//	
//	public static String getFirstProjectForStudy(String studyUID) 
//	
//	public static Set<String> getSubjectIDsForProject(String projectID)
//
//	public static String createProjectEntitiesFromDICOMFilesInUploadDirectory(File dicomUploadDirectory)
//	
//	public static int createProjectEntitiesFromDICOMFilesInUploadDirectory(File dicomUploadDirectory, String projectID, String sessionID, String username) throws Exception
//	
//	public static void createProjectEntitiesFromDICOMFile(File dicomFile, String projectID, String sessionID, String username) throws Exception
//
//	public static void addSubjectAndStudyToProject(String subjectID, String subjectName, String studyUID, String projectID, String sessionID, String username) {
//	
//	
}
