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

import org.json.JSONObject;

import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.models.WorkListToStudy;
import edu.stanford.epad.epadws.models.WorkListToSubject;

/**
 * All Epad User/WorkList related operations
 * 
 * @author Dev Gude
 *
 */
public interface EpadWorkListOperations {

	/**
	 * 
	 */
	void clearCache();
	
	/**
	 * Creates a worklist record in database
	 * 
	 * @param loggedInUser
	 * @param username - user for whom the worklist is created
	 * @param projectID
	 * @param workListID
	 * @param name
	 * @param description
	 * @param startDate
	 * @param dueDate
	 * @return
	 * @throws Exception
	 */
	WorkList createWorkList(String loggedInUser, String username, String workListID, String name, String description, Date startDate, Date dueDate) throws Exception;
	
	/**
	 * Updates worklist fields in database (all arguments except workListID are optional) 
	 * 
	 * @param loggedInUser
	 * @param username - user to whom the worklist is being changed or null
	 * @param workListID
	 * @param name
	 * @param description
	 * @param startDate
	 * @param dueDate
	 * @return
	 * @throws Exception
	 */
	WorkList updateWorkList(String loggedInUser, String username, String workListID, String name, String description, Date startDate, Date dueDate) throws Exception;
	
	/**
	 * Add Subject to WorkList
	 * @param loggedInUser
	 * @param subjectUID
	 * @param workListID
	 * @throws Exception
	 */
	void addSubjectToWorkList(String loggedInUser, String projectID, String subjectUID, String workListID) throws Exception;
	void addSubjectsToWorkList(String loggedInUser, String projectID, JSONObject json, String workListID) throws Exception;
	void removeSubjectFromWorkList(String loggedInUser, String projectID, String subjectUID, String workListID) throws Exception;
	/**
	 * Add Study to WorkList
	 * @param loggedInUser
	 * @param studyUID
	 * @param projectId
	 * @throws Exception
	 */
	WorkListToStudy addStudyToWorkList(String loggedInUser, String projectID, String studyUID, String workListID) throws Exception;
	
	/**
	 * Remove Subject from WorkList
	 * @param loggedInUser
	 * @param subjectUID
	 * @param workListID
	 * @throws Exception
	 */
	void removeSubjectFromWorkList(String loggedInUser, String subjectUID, String workListID) throws Exception;
	
	/**
	 * Remove Study from WorkList
	 * @param loggedInUser
	 * @param studyUID
	 * @param workListID
	 * @throws Exception
	 */
	void removeStudyFromWorkList(String loggedInUser, String studyUID, String workListID) throws Exception;
	
	/**
	 * Get WorkList from database
	 * @param workListID
	 * @return
	 * @throws Exception
	 */
	WorkList getWorkList(String workListID) throws Exception;
	
	/**
	 * Get User for Worklist
	 * @param workListID
	 * @return
	 * @throws Exception
	 */
	User getUserForWorkList(String workListID) throws Exception;
	
	/**
	 * Get All WorkList records
	 * @return
	 * @throws Exception
	 */
	List<WorkList> getAllWorkLists() throws Exception;
	
	/**
	 * Get WorkLists for a user
	 * @param username
	 * @return
	 * @throws Exception
	 */
	List<WorkList> getWorkListsForUser(String username) throws Exception;
	
	/**
	 * Get WorkList for a user and project
	 * @param username
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	WorkList getWorkListForUserByProject(String username, String projectID) throws Exception;
	List<WorkList> getWorkListsForUserByProject(String username, String projectID) throws Exception;
	
	/**
	 * Get Subjects for a WorkList
	 * @param workListID
	 * @return
	 * @throws Exception
	 */
	List<Subject> getSubjectsForWorkList(String workListID) throws Exception;
	
	/**
	 * Get WorkLists for a subject
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	List<WorkList> getWorkListsForSubject(String subjectUID) throws Exception;
	
	/**
	 * Get WorkLists for a subject
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	List<WorkList> getWorkListsForUserBySubject(String username, String subjectUID) throws Exception;
	
	/**
	 * Get WorkLists for a study
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	List<WorkList> getWorkListsForUserByStudy(String username, String studyUID) throws Exception;
	
	/**
	 * Get Studies for WorkList
	 * @param workListID
	 * @return
	 * @throws Exception
	 */
	List<Study> getStudiesForWorkList(String workListID) throws Exception;
	
	/**
	 * Get Studies for WorkList
	 * @param workListID
	 * @return
	 * @throws Exception
	 */
	List<WorkListToStudy> getWorkListStudies(String workListID) throws Exception;
	
	/**
	 * Get Subjects for WorkList
	 * @param workListID
	 * @return
	 * @throws Exception
	 */
	List<WorkListToSubject> getWorkListSubjects(String workListID) throws Exception;

	/**
	 * Delete WorkList
	 * @param username
	 * @param workListID
	 * @throws Exception
	 */
	void deleteWorkList(String username, String workListID) throws Exception;

	/**
	 * Set WorkList status
	 * @param username
	 * @param workListID
	 * @throws Exception
	 */
	void setWorkListStatus(String username, String workListID, String status, Boolean started, Boolean completed) throws Exception;

	/**
	 * Set WorkList Subject status
	 * @param username
	 * @param workListID
	 * @throws Exception
	 */
	void setWorkListSubjectStatus(String username, String workListID, String projectID, String subjectID, String status, Boolean started, Boolean completed) throws Exception;
	WorkListToSubject getWorkListSubjectStatus(String workListID, String projectID, String subjectID) throws Exception;

	/**
	 * Set WorkList Study status
	 * @param username
	 * @param workListID
	 * @throws Exception
	 */
	void setWorkListStudyStatus(String username, String workListID, String studyUID, String status, Boolean started, Boolean completed) throws Exception;
	WorkListToStudy getWorkListStudyStatus(String workListID, String studyUID) throws Exception;
}
