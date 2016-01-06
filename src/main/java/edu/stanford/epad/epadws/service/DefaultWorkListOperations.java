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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.stanford.epad.common.util.EPADConfig;
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
	public WorkList createWorkList(String loggedInUser, String username, String workListID, String name, String description, Date startDate, Date dueDate)
			throws Exception {
		User loggedIn = projectOperations.getUser(loggedInUser);
		if (loggedIn != null && !loggedIn.isAdmin() && !loggedInUser.equals(username) && !loggedIn.hasPermission(User.CreateWorkListPermission))
			throw new Exception("No permission to create worklist");
		User user = projectOperations.getUser(username);
		if (user == null)
			throw new Exception("User not found for username:" + username);
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
			workListID = username + "_" + i;
			while (getWorkList(workListID) != null) {
				i++;
				workListID = username + "_" + i;
			}
			workList.setWorkListID(workListID);
		}
		if (description != null && description.trim().length() > 0)
			workList.setDescription(description);
		workList.setName(name);
		if (startDate != null)
			workList.setStartDate(startDate);
		if (dueDate != null)
			workList.setDueDate(dueDate);
		workList.setUserId(user.getId());
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
			String workListID, String name, String description, Date startDate, Date dueDate)
			throws Exception {
		WorkList workList = getWorkList(workListID);
		if (name != null)
			workList.setName(name);
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
	public synchronized void addSubjectToWorkList(String loggedInUser, String projectID, String subjectUID,
			String workListID) throws Exception {
		log.debug("Adding subject " + subjectUID + " to " + workListID + " for " + projectID);
		Subject subject = projectOperations.getSubject(subjectUID);
		WorkList workList = new WorkList();
		workList = (WorkList) workList.getObject("worklistid = " + workList.toSQL(workListID));
		Project project = projectOperations.getProject(projectID);
		if (!projectOperations.isSubjectInProject(subjectUID, project.getProjectId()))
			throw new Exception("Subject " + subjectUID + " no longer exists in Project " + projectID);
		List<WorkListToSubject> wtoss = new WorkListToSubject().getObjects("worklist_id =" + workList.getId() + " order by sortorder");
		long max = 0;
		if (wtoss.size() > 0)
			max = wtoss.get(wtoss.size()-1).getSortOrder();
		WorkListToSubject wtos = (WorkListToSubject) new WorkListToSubject().getObject("worklist_id =" + workList.getId() + " and subject_id=" + subject.getId() + " and project_id=" + project.getId());
		if (wtos != null)
		{
			wtos.delete();
			wtos.setId(0);
		}
		else
			wtos = new WorkListToSubject();
		wtos.setWorkListId(workList.getId());
		wtos.setSubjectId(subject.getId());
		wtos.setProjectId(project.getId());
		wtos.setCreator(loggedInUser);
		wtos.save();
		wtos.setSortOrder(++max);
		wtos.save();

	}
	
	//ml get sortorder from url
	public synchronized void addSubjectToWorkList(String loggedInUser, String projectID, String subjectUID,
			String workListID,String sortOrder) throws Exception {
		log.debug("Adding subject " + subjectUID + " to " + workListID + " for " + projectID);
		Subject subject = projectOperations.getSubject(subjectUID);
		WorkList workList = new WorkList();
		workList = (WorkList) workList.getObject("worklistid = " + workList.toSQL(workListID));
		Project project = projectOperations.getProject(projectID);
		if (!projectOperations.isSubjectInProject(subjectUID, project.getProjectId()))
			throw new Exception("Subject " + subjectUID + " no longer exists in Project " + projectID);
		
		WorkListToSubject wtos = (WorkListToSubject) new WorkListToSubject().getObject("worklist_id =" + workList.getId() + " and subject_id=" + subject.getId() + " and project_id=" + project.getId());
		if (wtos == null)
		{
			wtos = new WorkListToSubject();
			wtos.setWorkListId(workList.getId());
			wtos.setSubjectId(subject.getId());
			wtos.setProjectId(project.getId());
			wtos.setCreator(loggedInUser);
		}
		
		wtos.setSortOrder(Long.valueOf(sortOrder));
		wtos.save();

	}
	
	@Override
	public void removeSubjectFromWorkList(String loggedInUser,
			String projectID, String subjectUID, String workListID)
			throws Exception {
		Subject subject = projectOperations.getSubject(subjectUID);
		WorkList workList = new WorkList();
		workList = (WorkList) workList.getObject("worklistid = " + workList.toSQL(workListID));
		Project project = projectOperations.getProject(projectID);
		WorkListToSubject wtos = (WorkListToSubject) new WorkListToSubject().getObject("worklist_id =" + workList.getId() + " and subject_id=" + subject.getId() + " and project_id=" + project.getId());
		if (wtos != null)
			wtos.delete();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#addStudyToWorkList(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public WorkListToStudy addStudyToWorkList(String loggedInUser, String projectID, String studyUID,
			String workListID) throws Exception {
		Study study = projectOperations.getStudy(studyUID);
		WorkList workList = new WorkList();
		workList = (WorkList) workList.getObject("worklistid = " + workList.toSQL(workListID));
		Project project = projectOperations.getProject(projectID);
		Subject subject = (Subject) projectOperations.getDBObject(Subject.class, study.getSubjectId());
		if (!projectOperations.isSubjectInProject(subject.getSubjectUID(), project.getProjectId()))
			throw new Exception("Study " + studyUID + " no longer exists in Project " + projectID);
		List<WorkListToStudy> wtoss = new WorkListToStudy().getObjects("worklist_id =" + workList.getId() + " order by sortorder");
		long max = 0;
		if (wtoss.size() > 0)
			max = wtoss.get(wtoss.size()-1).getSortOrder();
		WorkListToStudy wtos = (WorkListToStudy) new WorkListToStudy().getObject("worklist_id =" + workList.getId() + " and study_id=" + study.getId() + " and project_id=" + project.getId());
		if (wtos != null)
		{
			wtos.delete();
			wtos.setId(0);
		}
		else
			wtos = new WorkListToStudy();
		wtos.setWorkListId(workList.getId());
		wtos.setStudyId(study.getId());
		wtos.setProjectId(project.getId());
		wtos.setCreator(loggedInUser);
		wtos.save();
		wtos.setSortOrder(++max);
		wtos.save();
		return wtos;
	}

	//{"Subjects":["7","LIDC-IDRI-0314","AAA 20120823","91659230232099800175185744868500866896","282712935615235796400856228568961224210","99999999","DJR-VA-6770","212845382164361112207915959222509827626"]} 
	@Override
	public void addSubjectsToWorkList(String loggedInUser, String projectID,
			JSONObject json, String workListID) throws Exception {
		log.debug("JSONObject:" + json);
		JSONArray subjectIDs = (JSONArray) json.get("Subjects");
		for (int i = 0; i < subjectIDs.length(); i++)
		{
			String subjectID = subjectIDs.getString(i);
			addSubjectToWorkList(loggedInUser, projectID, subjectID, workListID);
		}		
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
		new WorkListToStudy().deleteObjects("worklist_id = " + workList.getId() + " and study_id =" + study.getId());		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#getWorkList(java.lang.String)
	 */
	@Override
	public WorkList getWorkList(String workListID) throws Exception {
		WorkList workList = new WorkList();
		return (WorkList) workList.getObject("worklistid = " + workList.toSQL(workListID));
	}

	@Override
	public User getUserForWorkList(String workListID) throws Exception {
		WorkList workList = new WorkList();
		workList = (WorkList) workList.getObject("worklistid = " + workList.toSQL(workListID));
		return (User) projectOperations.getDBObject(User.class, workList.getUserId());
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
	public List<WorkList> getWorkListsByUser(String username) throws Exception {
		User user = projectOperations.getUser(username);
		if (user == null)
			throw new Exception("User not found, username:" + username);
		List objects = new WorkList().getObjects("creator ='" + username + "' order by worklistid");
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
	public List<Subject> getSubjectsForWorkList(String workListID)
			throws Exception {
		WorkList workList = getWorkList(workListID);
		List objects = new WorkListToSubject().getObjects("worklist_id =" + workList.getId() + " order by sortorder");
		List<Subject> subjects = new ArrayList<Subject>();
		for (Object obj: objects)
		{
			WorkListToSubject wls = (WorkListToSubject) obj;
			if (wls.getStatus() == null) wls.setStatus("");
			Subject subject = (Subject) projectOperations.getDBObject(Subject.class, wls.getSubjectId());
			Project project = (Project) projectOperations.getDBObject(Project.class, wls.getProjectId());
			subject.setProjectID(project.getProjectId());
			subject.setStatus(wls.getStatus() + ":" + wls.getStartDate() != null ?"started":"not started" + ":" + wls.getCompleteDate() != null?"completed":"not completed");
			subjects.add(subject);
		}
		return subjects;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#getWorkListsForSubject(java.lang.String)
	 */
	@Override
	public List<WorkList> getWorkListsForSubject(String subjectUID)
			throws Exception {
		Subject subject = projectOperations.getSubject(subjectUID);
		List objects = new WorkList().getObjects("id in (select worklist_id from " 
				+ WorkListToSubject.DBTABLE 
				+ " where subject_id =" + subject.getId() + ")");
		List<WorkList> workLists = new ArrayList<WorkList>();
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
	public List<WorkList> getWorkListsForUserBySubject(String username, String subjectUID) throws Exception {
		User user = projectOperations.getUser(username);
		Subject subject = projectOperations.getSubject(subjectUID);
		List objects = new WorkList().getObjects("user_id = " + user.getId() + " and id in (select worklist_id from " 
				+ WorkListToSubject.DBTABLE 
				+ " where subject_id =" + subject.getId() + ")");
		List<WorkList> workLists = new ArrayList<WorkList>();
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
	public List<WorkList> getWorkListsForUserByStudy(String username, String studyUID) throws Exception {
		User user = projectOperations.getUser(username);
		Study study = projectOperations.getStudy(studyUID);
		List objects = new WorkList().getObjects("user_id = " + user.getId() + " and id in (select worklist_id from " 
				+ WorkListToStudy.DBTABLE 
				+ " where study_id =" + study.getId() + ")");
		List<WorkList> workLists = new ArrayList<WorkList>();
		workLists.addAll(objects);
		return workLists;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadWorkListOperations#getStudiesForWorkList(java.lang.String)
	 */
	@Override
	public List<Study> getStudiesForWorkList(String workListID)
			throws Exception {
		WorkList workList = getWorkList(workListID);
		List objects = new WorkListToStudy().getObjects("worklist_id =" + workList.getId() + " order by sortorder");
		List<Study> studys = new ArrayList<Study>();
		for (Object obj: objects)
		{
			WorkListToStudy wls = (WorkListToStudy) obj;
			if (wls.getStatus() == null) wls.setStatus("");
			Study study = (Study) projectOperations.getDBObject(Study.class, wls.getStudyId());
			Project project = (Project) projectOperations.getDBObject(Project.class, wls.getProjectId());
			study.setProjectID(project.getProjectId());
			study.setStatus(wls.getStatus() + ":" + wls.getStartDate() != null ?"started":"not started" + ":" + wls.getCompleteDate() != null?"completed":"not completed");
			studys.add(study);
		}
		return studys;
	}

	@Override
	public List<WorkListToStudy> getWorkListStudies(String workListID)
			throws Exception {
		WorkList workList = getWorkList(workListID);
		log.info("Getting studies for workListID:" + workListID);
		List<WorkListToStudy> wltss = new WorkListToStudy().getObjects("worklist_id =" + workList.getId() + " order by sortorder");
		return wltss;
	}

	@Override
	public List<WorkListToSubject> getWorkListSubjects(String workListID)
			throws Exception {
		WorkList workList = getWorkList(workListID);
		log.info("Getting subjects for workListID:" + workListID);
		List<WorkListToSubject> wltss = new WorkListToSubject().getObjects("worklist_id =" + workList.getId() + " order by sortorder");
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
		if (loggedIn != null && !loggedIn.isAdmin() && !username.equals(worklist.getCreator()))
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

		User loggedIn = projectOperations.getUser(username);
		if (loggedIn != null && !loggedIn.isAdmin() && !username.equals(worklist.getCreator()) && loggedIn.getId() != worklist.getUserId())
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
	public void setWorkListSubjectStatus(String username, String workListID, String projectID,
			String subjectID, String status, Boolean started, Boolean completed)
			throws Exception {
		WorkList worklist = this.getWorkList(workListID);
		if (worklist == null)
			throw new Exception("Worklist not found, ID =" + workListID);
		User loggedIn = projectOperations.getUser(username);
		if (loggedIn != null && !loggedIn.isAdmin() && !username.equals(worklist.getCreator()) && loggedIn.getId() != worklist.getUserId())
			throw new Exception("No permission to set worklist status");
		Subject subject = projectOperations.getSubject(subjectID);
		if (subject == null)
			throw new Exception("Subject not found " + subjectID);
		Project project = projectOperations.getProject(projectID);
		if (project == null)
			throw new Exception("Project not found " + projectID);
		WorkListToSubject wls = (WorkListToSubject) new WorkListToSubject().getObject("worklist_id=" + worklist.getId() + " and subject_id=" + subject.getId() + " and project_id=" + project.getId());
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
	public WorkListToSubject getWorkListSubjectStatus(String workListID, String projectID,
			String subjectID) throws Exception {
		WorkList worklist = this.getWorkList(workListID);
		if (worklist == null)
			throw new Exception("Worklist not found, ID =" + workListID);
		Subject subject = projectOperations.getSubject(subjectID);
		if (subject == null)
			throw new Exception("Subject not found " + subjectID);
		Project project = projectOperations.getProject(projectID);
		if (project == null)
			throw new Exception("Project not found " + projectID);
		WorkListToSubject wls = (WorkListToSubject) new WorkListToSubject().getObject("worklist_id=" + worklist.getId() + " and subject_id=" + subject.getId() + " and project_id=" + project.getId());
		return wls;
	}

	@Override
	public void setWorkListStudyStatus(String username, String workListID,
			String studyUID, String status, Boolean started, Boolean completed)
			throws Exception {
		WorkList worklist = this.getWorkList(workListID);
		if (worklist == null)
			throw new Exception("Worklist not found, ID =" + workListID);
		User loggedIn = projectOperations.getUser(username);
		if (loggedIn != null && !loggedIn.isAdmin() && !username.equals(worklist.getCreator()) && loggedIn.getId() != worklist.getUserId())
			throw new Exception("No permission to set worklist status");
		Study study = projectOperations.getStudy(studyUID);
		if (study == null)
			throw new Exception("Study not found " + studyUID);
		WorkListToStudy wls = (WorkListToStudy) new WorkListToStudy().getObject("worklist_id=" + worklist.getId() + " and study_id=" + study.getId());
		if (wls == null) {
			wls = this.addStudyToWorkList(null, EPADConfig.xnatUploadProjectID, studyUID, workListID);
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
		Study study = projectOperations.getStudy(studyUID);
		WorkListToStudy wls = (WorkListToStudy) new WorkListToStudy().getObject("worklist_id=" + worklist.getId() + " and study_id=" + study.getId());
		return wls;	
	}

}
