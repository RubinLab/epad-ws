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
package edu.stanford.epad.epadws.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectToSubject;
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
public class DefaultWorkListOperations implements EpadWorkListOperations {

	private static final EPADLogger log = EPADLogger.getInstance();

	private static final DefaultWorkListOperations ourInstance = new DefaultWorkListOperations();
	private final DefaultEpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
	
	// Simple cache - maybe replace it with Ehcache someday
	private static Map<String, WorkList> workListCache = new HashMap<String, WorkList>();

	private DefaultWorkListOperations()
	{
	}

	public static DefaultWorkListOperations getInstance()
	{
		return ourInstance;
	}

	@Override
	public void clearCache() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#createWorkList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public WorkList createWorkList(String loggedInUser, String username, String projectID,
			String workListID, String description, Date startDate, Date dueDate)
			throws Exception {
		User loggedIn = projectOperations.getUser(loggedInUser);
		Project project = projectOperations.getProject(projectID);
		if (project == null)
			throw new Exception("Project not found, ID:" + projectID);
		if (loggedIn != null && !loggedIn.isAdmin() && !projectOperations.isOwner(loggedInUser, projectID) && !loggedIn.hasPermission(User.CreateWorkListPermission))
			throw new Exception("No permission to create worklist");
		User user = projectOperations.getUser(username);
		if (user == null)
			throw new Exception("User not found for username:" + username);
		if (!projectOperations.isUserInProject(user.getId(), project.getId()));
		WorkList workList = null;
		if (workListID != null && workListID.trim().length() > 0)
		{
			workList = this.getWorkList(workListID);
		}
		if (workList == null) workList = new WorkList();
		if (workListID != null && workListID.trim().length() > 0)
			workList.setWorkListID(workListID);
		if (workList.getWorkListID() == null)
		{
			int i = 0;
			workListID = projectID + "_" + username + "_" + i;
			while (getWorkList(workListID) != null) {
				i++;
				workListID = projectID + "_" + username + "_" + i;
			}
			workList.setWorkListID(workListID);
		}
		if (description != null && description.trim().length() > 0)
			workList.setDescription(description);
		if (startDate != null)
			workList.setStartDate(startDate);
		if (dueDate != null)
			workList.setDueDate(dueDate);
		workList.setUserId(user.getId());
		workList.setProjectId(project.getId());
		if (workList.getId() == 0)
			workList.setCreator(loggedInUser);
		workList.save();
		workListCache.put(workList.getWorkListID(), workList);
		return workList;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#updateWorkList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public WorkList updateWorkList(String loggedInUser, String username,
			String workListID, String description, Date startDate, Date dueDate)
			throws Exception {
		WorkList workList = getWorkList(workListID);
		if (description != null)
			workList.setDescription(description);
		if (username != null)
		{
			User user = projectOperations.getUser(username);
			if (user == null)
				throw new Exception("User not found for username:" + username);
			workList.setUserId(user.getId());
		}
		if (startDate != null)
			workList.setStartDate(startDate);
		if (dueDate != null)
			workList.setDueDate(dueDate);
		workList.save();
		workListCache.put(workList.getWorkListID(), workList);
		return workList;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#addSubjectToWorkList(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addSubjectToWorkList(String loggedInUser, String subjectUID,
			String workListID) throws Exception {
		Subject subject = projectOperations.getSubject(subjectUID);
		WorkList workList = new WorkList();
		workList = (WorkList) workList.getObject("worklistid = " + workList.toSQL(workListID));
		Project project = new Project(workList.getProjectId());
		project.retrieve();
		if (!projectOperations.isSubjectInProject(subjectUID, project.getProjectId()))
			throw new Exception("Subject does not belong to Project");
		WorkListToSubject wtos = (WorkListToSubject) new WorkListToSubject().getObject("worklist_id =" + workList.getId() + " and subject_id=" + subject.getId());
		if (wtos == null)
		{
			wtos = new WorkListToSubject();
			wtos.setWorkListId(workList.getId());
			wtos.setSubjectId(subject.getId());
			wtos.setCreator(loggedInUser);
			wtos.save();
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#addStudyToWorkList(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public WorkListToStudy addStudyToWorkList(String loggedInUser, String studyUID,
			String workListID) throws Exception {
		Study study = projectOperations.getStudy(studyUID);
		WorkList workList = new WorkList();
		workList = (WorkList) workList.getObject("worklistid = " + workList.toSQL(workListID));
		WorkListToStudy wtos = (WorkListToStudy) new WorkListToStudy().getObject("worklist_id =" + workList.getId() + " and study_id=" + study.getId());
		if (wtos == null)
		{
			wtos = new WorkListToStudy();
			wtos.setWorkListId(workList.getId());
			wtos.setStudyId(study.getId());
			wtos.setCreator(loggedInUser);
			wtos.save();
		}
		return wtos;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#removeSubjectFromWorkList(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void removeSubjectFromWorkList(String loggedInUser,
			String subjectUID, String workListID) throws Exception {
		Subject subject = projectOperations.getSubject(subjectUID);
		WorkList workList = new WorkList();
		workList = (WorkList) workList.getObject("worklistid = " + workList.toSQL(workListID));
		new WorkListToSubject().deleteObjects("worklist_id = " + workList.getId() + " and subject_id =" + subject.getId());		ProjectToSubject ptos = new ProjectToSubject();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#removeStudyFromWorkList(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void removeStudyFromWorkList(String loggedInUser, String studyUID,
			String workListID) throws Exception {
		Study study = projectOperations.getStudy(studyUID);
		WorkList workList = new WorkList();
		workList = (WorkList) workList.getObject("worklistid = " + workList.toSQL(workListID));
		new WorkListToStudy().deleteObjects("worklist_id = " + workList.getId() + " and study_id =" + study.getId());		ProjectToSubject ptos = new ProjectToSubject();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#getWorkList(java.lang.String)
	 */
	@Override
	public WorkList getWorkList(String workListID) throws Exception {
		WorkList workList = new WorkList();
		return (WorkList) workList.getObject("worklistid = " + workList.toSQL(workListID));
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#getAllWorkLists()
	 */
	@Override
	public List<WorkList> getAllWorkLists() throws Exception {
		List objects = new WorkList().getObjects("1 = 1 order by worklistid");
		List<WorkList> worklists = new ArrayList<WorkList>();
		worklists.addAll(objects);
		return worklists;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#getWorkListsForUser(java.lang.String)
	 */
	@Override
	public List<WorkList> getWorkListsForUser(String username) throws Exception {
		User user = projectOperations.getUser(username);
		if (user == null)
			throw new Exception("User not found, username:" + username);
		List objects = new WorkList().getObjects("user_id =" + user.getId() + " order by worklistid");
		List<WorkList> worklists = new ArrayList<WorkList>();
		worklists.addAll(objects);
		return worklists;
	}

	@Override
	public List<WorkList> getWorkListsForProject(String projectID)
			throws Exception {
		Project project = projectOperations.getProject(projectID);
		if (project == null)
			throw new Exception("Project not found, projectID:" + projectID);
		List objects = new WorkList().getObjects("project_id =" + project.getId() + " order by worklistid");
		List<WorkList> worklists = new ArrayList<WorkList>();
		worklists.addAll(objects);
		return worklists;
	}

	@Override
	public WorkList getWorkListForUserByProject(String username, String projectID) throws Exception {
		User user = projectOperations.getUser(username);
		if (user == null)
			throw new Exception("User not found for username:" + username);
		Project project = projectOperations.getProject(projectID);
		if (project == null)
			throw new Exception("Project not found, ID:" + projectID);
		return (WorkList) new WorkList().getObject("user_id =" + user.getId() + " and project_id=" + project.getId() + " order by worklistid");
	}

	@Override
	public List<WorkList> getWorkListsForUserByProject(String username, String projectID) throws Exception {
		User user = projectOperations.getUser(username);
		if (user == null)
			throw new Exception("User not found for username:" + username);
		Project project = projectOperations.getProject(projectID);
		if (project == null)
			throw new Exception("Project not found, ID:" + projectID);
		return new WorkList().getObjects("user_id =" + user.getId() + " and project_id=" + project.getId() + " order by worklistid");
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#getSubjectsForWorkList(java.lang.String)
	 */
	@Override
	public Set<Subject> getSubjectsForWorkList(String workListID)
			throws Exception {
		WorkList workList = getWorkList(workListID);
		List objects = new Subject().getObjects("id in (select subject_id from " 
				+ WorkListToSubject.DBTABLE 
				+ " where worklist_id =" + workList.getId() + ")");
		Set<Subject> subjects = new HashSet<Subject>();
		subjects.addAll(objects);
//		objects = new Study().getObjects("id in (select study_id from " 
//				+ WorkListToStudy.DBTABLE 
//				+ " where worklist_id =" + workList.getId() + ")");
//		if (objects.size() == 0)
//			return subjects;
//		String inclause = "";
//		for (Object obj: objects)
//		{
//			inclause = inclause + "," + ((Study) obj).getSubjectId();
//		}
//		objects = new Subject().getObjects("id in (" + inclause.substring(1) + ")");
//		subjects.addAll(objects);
		return subjects;
	}

	@Override
	public Set<Subject> getSubjectsForWorkListWithStatus(String workListID)
			throws Exception {
		WorkList workList = getWorkList(workListID);
		List<WorkListToSubject> wltss = new WorkListToSubject().getObjects("worklist_id =" + workList.getId());
		Set<Subject> subjects = new HashSet<Subject>();
		for (WorkListToSubject wlts: wltss)
		{
			if (wlts.getStatus() == null) wlts.setStatus("");
			Subject subject = (Subject) projectOperations.getDBObject(Subject.class, wlts.getSubjectId());
			subject.setStatus(wlts.getStatus() + ":" + wlts.getStartDate() != null ?"started":"not started" + ":" + wlts.getCompleteDate() != null?"completed":"not completed");
			subjects.add(subject);
		}
		return subjects;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#getWorkListsForSubject(java.lang.String)
	 */
	@Override
	public Set<WorkList> getWorkListsForSubject(String subjectUID)
			throws Exception {
		Subject subject = projectOperations.getSubject(subjectUID);
		List objects = new WorkList().getObjects("id in (select worklist_id from " 
				+ WorkListToSubject.DBTABLE 
				+ " where subject_id =" + subject.getId() + ")");
		Set<WorkList> workLists = new HashSet<WorkList>();
		workLists.addAll(objects);
		List<Study> studies = projectOperations.getStudiesForSubject(subjectUID);
		String inclause = "";
		for (Study study: studies)
		{
			inclause = inclause + "," + study.getId();
		}
		objects = new WorkList().getObjects("id in (select worklist_id from " 
				+ WorkListToStudy.DBTABLE 
				+ " where study_id in (" + inclause + "))");
		
		return workLists;
	}

	@Override
	public Set<WorkList> getWorkListsForUserBySubject(String username, String subjectUID) throws Exception {
		User user = projectOperations.getUser(username);
		Subject subject = projectOperations.getSubject(subjectUID);
		List objects = new WorkList().getObjects("user_id = " + user.getId() + " and id in (select worklist_id from " 
				+ WorkListToSubject.DBTABLE 
				+ " where subject_id =" + subject.getId() + ")");
		Set<WorkList> workLists = new HashSet<WorkList>();
		workLists.addAll(objects);
		List<Study> studies = projectOperations.getStudiesForSubject(subjectUID);
		String inclause = "";
		for (Study study: studies)
		{
			inclause = inclause + "," + study.getId();
		}
		objects = new WorkList().getObjects("id in (select worklist_id from " 
				+ WorkListToStudy.DBTABLE 
				+ " where study_id in (" + inclause + "))");
		
		return workLists;
	}

	@Override
	public Set<WorkList> getWorkListsForUserByStudy(String username, String studyUID) throws Exception {
		User user = projectOperations.getUser(username);
		Study study = projectOperations.getStudy(studyUID);
		List objects = new WorkList().getObjects("user_id = " + user.getId() + " and id in (select worklist_id from " 
				+ WorkListToStudy.DBTABLE 
				+ " where study_id =" + study.getId() + ")");
		Set<WorkList> workLists = new HashSet<WorkList>();
		workLists.addAll(objects);
		return workLists;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#getStudiesForWorkList(java.lang.String)
	 */
	@Override
	public Set<Study> getStudiesForWorkList(String workListID)
			throws Exception {
		WorkList workList = getWorkList(workListID);
		List objects = new Study().getObjects("id in (select study_id from " 
				+ WorkListToStudy.DBTABLE 
				+ " where worklist_id =" + workList.getId() + ")");
		Set<Study> studies = new HashSet<Study>();
		studies.addAll(objects);
		return studies;
	}

	@Override
	public Set<Study> getStudiesForWorkListWithStatus(String workListID)
			throws Exception {
		WorkList workList = getWorkList(workListID);
		List<WorkListToStudy> wltss = new WorkListToStudy().getObjects("worklist_id =" + workList.getId());
		Set<Study> studies = new HashSet<Study>();
		for (WorkListToStudy wlts: wltss)
		{
			if (wlts.getStatus() == null) wlts.setStatus("");
			Study study = (Study) projectOperations.getDBObject(Study.class, wlts.getStudyId());
			study.setStatus(wlts.getStatus() + ":" + wlts.getStartDate() != null ?"started":"not started" + ":" + wlts.getCompleteDate() != null?"completed":"not completed");
			studies.add(study);
		}
		return studies;
	}

	@Override
	public List<WorkListToStudy> getWorkListStudies(String workListID)
			throws Exception {
		WorkList workList = getWorkList(workListID);
		log.info("Getting studies for workListID:" + workListID);
		List<WorkListToStudy> wltss = new WorkListToStudy().getObjects("worklist_id =" + workList.getId());
		return wltss;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#deleteWorkList(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteWorkList(String username, String workListID)
			throws Exception {
		WorkList worklist = this.getWorkList(workListID);
		if (worklist == null)
			throw new Exception("Worklist not found, ID =" + workListID);
		User loggedIn = projectOperations.getUser(username);
		Project project = (Project) projectOperations.getDBObject(Project.class, worklist.getProjectId());
		if (loggedIn != null && !loggedIn.isAdmin() && !projectOperations.isOwner(username, project.getProjectId()) 
				&& !username.equals(worklist.getCreator()))
			throw new Exception("No permission to delete worklist");
		WorkListToStudy wls = new WorkListToStudy();
		wls.deleteObjects("worklist_id = " + worklist.getId());
		WorkListToSubject wlp = new WorkListToSubject();
		wlp.deleteObjects("worklist_id = " + worklist.getId());
		worklist.delete();
	}

	@Override
	public void setWorkListStatus(String username, String workListID,
			String status, Boolean started, Boolean completed) throws Exception {
		WorkList worklist = this.getWorkList(workListID);
		if (worklist == null)
			throw new Exception("Worklist not found, ID =" + workListID);
		Project project = (Project) projectOperations.getDBObject(Project.class, worklist.getProjectId());
		User loggedIn = projectOperations.getUser(username);
		if (loggedIn != null && !loggedIn.isAdmin() && !projectOperations.isOwner(username, project.getProjectId()) 
				&& !username.equals(worklist.getCreator()) && loggedIn.getId() != worklist.getUserId())
			throw new Exception("No permission to set worklist status");		
		if (status != null && status.trim().length() > 0)
			worklist.setStatus(status);
		if (started != null && started && worklist.getStartDate() == null)
			worklist.setStartDate(new Date());
		if (started != null && !started)
			worklist.setStartDate(null);
		if (completed != null && completed && worklist.getCompleteDate() == null)
			worklist.setCompleteDate(new Date());
		if (completed != null && !completed)
			worklist.setCompleteDate(null);
		worklist.save();
	}

	@Override
	public void setWorkListSubjectStatus(String username, String workListID,
			String subjectID, String status, Boolean started, Boolean completed)
			throws Exception {
		WorkList worklist = this.getWorkList(workListID);
		if (worklist == null)
			throw new Exception("Worklist not found, ID =" + workListID);
		Project project = (Project) projectOperations.getDBObject(Project.class, worklist.getProjectId());
		User loggedIn = projectOperations.getUser(username);
		if (loggedIn != null && !loggedIn.isAdmin() && !projectOperations.isOwner(username, project.getProjectId()) 
				&& !username.equals(worklist.getCreator()) && loggedIn.getId() != worklist.getUserId())
			throw new Exception("No permission to set worklist status");
		Subject subject = projectOperations.getSubject(subjectID);
		WorkListToSubject wls = (WorkListToSubject) new WorkListToSubject().getObject("worklist_id=" + worklist.getId() + " and subject_id=" + subject.getId());
		if (wls == null)  return;
		if (status != null && status.trim().length() > 0)
			wls.setStatus(status);
		if (started != null && started && wls.getStartDate() == null)
			wls.setStartDate(new Date());
		if (started != null && !started)
			wls.setStartDate(null);
		if (completed != null && completed && wls.getCompleteDate() == null)
			wls.setCompleteDate(new Date());
		if (completed != null && !completed)
			wls.setCompleteDate(null);
		wls.save();
	}

	@Override
	public WorkListToSubject getWorkListSubjectStatus(String workListID,
			String subjectID) throws Exception {
		WorkList worklist = this.getWorkList(workListID);
		if (worklist == null)
			throw new Exception("Worklist not found, ID =" + workListID);
		Project project = (Project) projectOperations.getDBObject(Project.class, worklist.getProjectId());
		Subject subject = projectOperations.getSubject(subjectID);
		WorkListToSubject wls = (WorkListToSubject) new WorkListToSubject().getObject("worklist_id=" + worklist.getId() + " and subject_id=" + subject.getId());
		return wls;
	}

	@Override
	public void setWorkListStudyStatus(String username, String workListID,
			String studyUID, String status, Boolean started, Boolean completed)
			throws Exception {
		WorkList worklist = this.getWorkList(workListID);
		if (worklist == null)
			throw new Exception("Worklist not found, ID =" + workListID);
		Project project = (Project) projectOperations.getDBObject(Project.class, worklist.getProjectId());
		User loggedIn = projectOperations.getUser(username);
		if (loggedIn != null && !loggedIn.isAdmin() && !projectOperations.isOwner(username, project.getProjectId()) 
				&& !username.equals(worklist.getCreator()) && loggedIn.getId() != worklist.getUserId())
			throw new Exception("No permission to set worklist status");
		Study study = projectOperations.getStudy(studyUID);
		WorkListToStudy wls = (WorkListToStudy) new WorkListToStudy().getObject("worklist_id=" + worklist.getId() + " and study_id=" + study.getId());
		if (wls == null) {
			wls = this.addStudyToWorkList(null, studyUID, workListID);
		}
		if (status != null && status.trim().length() > 0)
			wls.setStatus(status);
		if (started != null && started && wls.getStartDate() == null)
			wls.setStartDate(new Date());
		if (started != null && !started)
			wls.setStartDate(null);
		if (completed != null && completed && wls.getCompleteDate() == null)
			wls.setCompleteDate(new Date());
		if (completed != null && !completed)
			wls.setCompleteDate(null);
		wls.save();
	
	}

	@Override
	public WorkListToStudy getWorkListStudyStatus(String workListID, String studyUID)
			throws Exception {
		WorkList worklist = this.getWorkList(workListID);
		if (worklist == null)
			throw new Exception("Worklist not found, ID =" + workListID);
		Project project = (Project) projectOperations.getDBObject(Project.class, worklist.getProjectId());
		Study study = projectOperations.getStudy(studyUID);
		WorkListToStudy wls = (WorkListToStudy) new WorkListToStudy().getObject("worklist_id=" + worklist.getId() + " and study_id=" + study.getId());
		return wls;	
	}

}
