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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.mindrot.jbcrypt.BCrypt;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.TaskStatus;
import edu.stanford.epad.dtos.internal.DCM4CHEESeries;
import edu.stanford.epad.epadws.aim.AIMDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.DatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.models.DisabledTemplate;
import edu.stanford.epad.epadws.models.EpadFile;
import edu.stanford.epad.epadws.models.EpadStatistics;
import edu.stanford.epad.epadws.models.EventLog;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.models.NonDicomSeries;
import edu.stanford.epad.epadws.models.Plugin;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectToFile;
import edu.stanford.epad.epadws.models.ProjectToPlugin;
import edu.stanford.epad.epadws.models.ProjectToPluginParameter;
import edu.stanford.epad.epadws.models.ProjectToSubject;
import edu.stanford.epad.epadws.models.ProjectToSubjectToStudy;
import edu.stanford.epad.epadws.models.ProjectToSubjectToUser;
import edu.stanford.epad.epadws.models.ProjectToUser;
import edu.stanford.epad.epadws.models.ProjectType;
import edu.stanford.epad.epadws.models.RemotePACQuery;
import edu.stanford.epad.epadws.models.ReviewerToReviewee;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.UserRole;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.models.WorkListToStudy;
import edu.stanford.epad.epadws.models.WorkListToSubject;
import edu.stanford.epad.epadws.models.dao.AbstractDAO;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;

/**
 * All Epad User/Project/Subject/Study related operations
 * 
* @author Dev Gude
 *
 */
public class DefaultEpadProjectOperations implements EpadProjectOperations {
	
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final DefaultEpadProjectOperations ourInstance = new DefaultEpadProjectOperations();
	
	// Simple Project/Subject/User cache - maybe replace it with Ehcache someday
	private static Map<String, Project> projectCache = new HashMap<String, Project>();
	private static Map<String, User> userCache = new HashMap<String, User>();
	private static Map<String, Subject> subjectCache = new HashMap<String, Subject>();

	private DefaultEpadProjectOperations()
	{
	}

	public static DefaultEpadProjectOperations getInstance()
	{
		return ourInstance;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#clearCache()
	 */
	@Override
	public void clearCache() {
		projectCache = new HashMap<String, Project>();
		userCache = new HashMap<String, User>();
		subjectCache = new HashMap<String, Subject>();
	}
	
	@Override
	public int getCacheSize() {
		return projectCache.keySet().size() +  userCache.keySet().size() +  subjectCache.keySet().size();
	}

	public static Collection<User> getUserCache() {
		return userCache.values();
	}
	/**
	 * @param username
	 * @return
	 * @throws Exception
	 */
	private User getUserFromCache(String username) throws Exception
	{
		User user = userCache.get(username);
		if (user != null)
		{
			user = (User) user.clone();
		}
		return user;
	}
	
	/**
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	private Project getProjectFromCache(String projectID) throws Exception
	{
		Project project = projectCache.get(projectID);
		if (project != null)
		{
			project = (Project) project.clone();
		}
		return project;
	}
	
	/**
	 * @param subjectUID
	 * @return
	 * @throws Exception
	 */
	private Subject getSubjectFromCache(String subjectUID) throws Exception
	{
		Subject subject = subjectCache.get(subjectUID);
		if (subject != null)
		{
			subject = (Subject) subject.clone();
		}
		return subject;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#createProject(java.lang.String, java.lang.String, java.lang.String, java.lang.String, edu.stanford.epad.epadws.models.ProjectType)
	 */
	@Override
	public Project createProject(String loggedInUser, String projectId, String projectName,
			String description, String defaultTemplate, ProjectType type) throws Exception {
		User user = getUser(loggedInUser);
		if (user != null && !user.isAdmin() && !user.hasPermission(User.CreateProjectPermission))
			throw new Exception("No permission to create project");
		Project project = new Project();
		project.setProjectId(projectId);
		project.setName(projectName);
		project.setDescription(description);
		project.setDefaultTemplate(defaultTemplate);
		project.setType(type.getName());
		project.setCreator(loggedInUser);
		project.save();
		projectCache.put(project.getProjectId(), project);
		return project;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#updateProject(java.lang.String, java.lang.String, java.lang.String, java.lang.String, edu.stanford.epad.epadws.models.ProjectType)
	 */
	@Override
	public Project updateProject(String loggedInUser, String projectId,
			String projectName, String description, String defaultTemplate, ProjectType type)
			throws Exception {
		Project project = getProject(projectId);
		if (projectName != null)
			project.setName(projectName);
		if (description != null)
			project.setDescription(description);
		if (type != null)
			project.setType(type.getName());
		if (defaultTemplate != null)
			project.setDefaultTemplate(defaultTemplate);
		project.save();
		projectCache.put(project.getProjectId(), project);
		return project;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#createUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public User createUser(String loggedInUser, String username, String firstName, String lastName, String email,
			String password, String colorpreference, List<String> addPermissions, List<String> removePermissions) throws Exception {
		User user = getUser(loggedInUser);
		if (user != null && !user.isAdmin() && !user.hasPermission(User.CreateUserPermission))
			throw new Exception("No permission to create user");
		user = new User();
		user.setUsername(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		String hashedPW = BCrypt.hashpw(password, BCrypt.gensalt());
		//log.info("Password:" + password + " hash:" + hashedPW);
		user.setPassword(hashedPW);
		user.setColorpreference(colorpreference);
		String[] defaultPerms = EPADConfig.getParamValue("DefaultUserPermissions", User.CreateProjectPermission).split(",");
		Set<String> perms = new HashSet<String>();
		for (String perm: defaultPerms)
			perms.add(perm);
		for (String perm: addPermissions)
			perms.add(perm);
		for (String perm: removePermissions)
			perms.remove(perm);
		user.setPermissions(toStringList(perms));
		user.setCreator(loggedInUser);
		user.setEnabled(true);
		user.save();
		userCache.put(user.getUsername(), user);
		return user;
	}

	private String toStringList(Collection<String> list)
	{
		if (list.isEmpty()) return "";
		String strList = "";
		for (String item: list)
			strList = strList + "," + item;
		return strList.substring(1);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#updateUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public User updateUser(String loggedInUserName, String username,
			String firstName, String lastName, String email, String newpassword, String oldpassword, String colorpreference, 
			List<String> addPermissions, List<String> removePermissions)
			throws Exception {
		User loggedInUser = getUser(loggedInUserName);
		if (loggedInUser != null && !loggedInUser.isAdmin() && !loggedInUser.hasPermission(User.CreateUserPermission) && !loggedInUserName.equals(username))
			throw new Exception("No permission to modify user");
		log.info("LoggedIn:" + loggedInUserName + " Modify user:" + username + " addPermissions:" + addPermissions + " removePermissions:" + removePermissions);
		if (addPermissions.size() > 0 && !loggedInUser.isAdmin())
			throw new Exception("Only admin can add permissions");
		User user = new User();
		user = (User) user.getObject("username = " + user.toSQL(username));
		if (loggedInUser != null && !loggedInUser.isAdmin() && !loggedInUserName.equals(username) && !loggedInUserName.equals(user.getCreator()))
			throw new Exception("No permission to modify user");
		if (firstName != null) user.setFirstName(firstName);
		if (lastName != null) user.setLastName(lastName);
		if (email != null) user.setEmail(email);
		if (newpassword != null) {
			if (loggedInUserName.equals("admin") || loggedInUser.isAdmin() || (user.getPassword().length() < 60 && oldpassword.equals(user.getPassword())) 
					|| BCrypt.checkpw(oldpassword, user.getPassword()))
			{
				String hashedPW = BCrypt.hashpw(newpassword, BCrypt.gensalt());
				//log.info("Password:" + newpassword + " hash:" + hashedPW);
				user.setPassword(hashedPW);
				user.setPasswordExpired(false);
				user.setPasswordUpdate(new Date());
			}
			else
				throw new Exception("Invalid old password");
		}
		if (colorpreference != null)
			user.setColorpreference(colorpreference);
		String[] oldPerms = user.getPermissions().split(",");
		Set<String> perms = new HashSet<String>();
		for (String perm: oldPerms)
			perms.add(perm);
		for (String perm: addPermissions)
			perms.add(perm);
		for (String perm: removePermissions)
			perms.remove(perm);
		log.info("Setting permissions:" + perms + ":" + toStringList(perms));
		user.setPermissions(toStringList(perms));
		user.save();
		userCache.put(user.getUsername(), user);
		return user;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#setAdmin(java.lang.String, java.lang.String)
	 */
	@Override
	public void setAdmin(String loggedInUserName, String username) throws Exception {
		User loggedInUser = getUser(loggedInUserName);
		User user = getUser(username);
		if (loggedInUser != null && !loggedInUser.isAdmin() && !loggedInUserName.equals(user.getCreator()))
			throw new Exception("No permission to modify user");
		user.setAdmin(true);
		user.save();
		userCache.put(user.getUsername(), user);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#resetAdmin(java.lang.String, java.lang.String)
	 */
	@Override
	public void resetAdmin(String loggedInUserName, String username) throws Exception {
		User loggedInUser = getUser(loggedInUserName);
		User user = getUser(username);
		if (loggedInUser != null && !loggedInUser.isAdmin() && !loggedInUserName.equals(user.getCreator()))
			throw new Exception("No permission to modify user");
		user.setAdmin(true);
		user.save();
		userCache.put(user.getUsername(), user);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#enableUser(java.lang.String, java.lang.String)
	 */
	@Override
	public void enableUser(String loggedInUserName, String username) throws Exception {
		User loggedInUser = getUser(loggedInUserName);
		User user = getUser(username);
		if (loggedInUser != null && !loggedInUser.isAdmin() && !loggedInUserName.equals(user.getCreator()))
			throw new Exception("No permission to modify user");
		user.setEnabled(true);
		user.save();
		userCache.put(user.getUsername(), user);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#disableUser(java.lang.String, java.lang.String)
	 */
	@Override
	public void disableUser(String loggedInUserName, String username) throws Exception {
		User loggedInUser = getUser(loggedInUserName);
		User user = getUser(username);
		if (loggedInUser != null && !loggedInUser.isAdmin() && !loggedInUserName.equals(user.getCreator()))
			throw new Exception("No permission to modify user");
		user.setEnabled(false);
		user.save();
		userCache.put(user.getUsername(), user);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#deleteUser(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteUser(String loggedInUser, String username) throws Exception {
		User requestor = getUser(loggedInUser);
		User user = getUser(username);
		if (!requestor.isAdmin() && !loggedInUser.equals(user.getCreator()))
			throw new Exception("No permissions to delete user");
		try {
			user.delete();
			userCache.remove(user.getUsername());
		} catch (Exception x) {
			if (x.getMessage() != null && x.getMessage().contains("constraint")) {
				throw new Exception("Error deleting user, this user is still a member of projects");
			} else
				throw x;
		}
	}

	static SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	@Override
	public void updateUserTaskStatus(String username, String type,
			String projectID, String target, String status, Date startTime, Date completeTime) {
		if (username == null || username.length() == 0) return;
		User user = null;
		try {
			user = getUser(username);
		} catch (Exception e) {
		}
		if (user != null) {
			TaskStatus tstat = user.getTaskStatus(type, target);
			if (tstat == null)
				tstat = new TaskStatus();
			if (tstat.projectID == null)
				tstat.projectID = projectID;
			tstat.username = username;
			tstat.status = status;
			if (startTime != null)
				tstat.starttime = dateformat.format(startTime);
			if (completeTime != null)
				tstat.completetime = dateformat.format(completeTime);
			tstat.type = type;
			tstat.target = target;
			tstat.statustime = dateformat.format(new Date());
			if (tstat.starttime == null)
				tstat.starttime = tstat.statustime;
			user.addTaskStatus(tstat);
		}
	}

	@Override
	public void updateUserTaskStatus(String username, String type,
			String target, String status, Date startTime,
			Date completeTime) {
		updateUserTaskStatus(username, type,
				null, target, status, startTime, completeTime);
	}

	@Override
	public EpadStatistics getUserStatistics(String loggedInUser,
			String username, boolean exceptionOnErr) throws Exception {
		User user = getUser(username);
		if (!loggedInUser.equals(username) && !loggedInUser.equals(user.getCreator()) && !isAdmin(loggedInUser))
		{
			if (exceptionOnErr)
				throw new Exception("No permission to get user " + username + " statistics");
			else
				return null;
		}
		try
		{
			EpadStatistics es = new EpadStatistics();
			int projects = this.getProjectsForUser(username).size();
			int users = new User().getCount("Creator == '" + username + "'")+1;
			int patients = new Subject().getCount("Creator == '" + username + "'");
			int studies = new Study().getCount("Creator == '" + username + "'");
			int files = new EpadFile().getCount("Creator == '" + username + "'");
			int templates = new EpadFile().getCount("Creator == '" + username + "' and filetype = '" + FileType.TEMPLATE.getName() + "'");
			int plugins = new Plugin().getCount("Creator == '" + username + "'");
			EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			int aims = epadDatabaseOperations.getNumberOfAIMs(AIMDatabaseOperations.aimcol_username + " = '" + username + "'");
			int dsos = epadDatabaseOperations.getNumberOfAIMs("DSOSeriesUID is not null or DSOSeriesUID != '' and " + AIMDatabaseOperations.aimcol_username + " = '" + username + "'");
			int pacQueries = new RemotePACQuery().getCount("");
			int wls = new WorkList().getCount("user_id =" + user.getId());
			String host = EPADConfig.xnatServer;
			es.setHost(host);
			es.setNumOfUsers(users);
			es.setNumOfProjects(projects);
			es.setNumOfPatients(patients);
			es.setNumOfStudies(studies);
			es.setNumOfAims(aims);
			es.setNumOfDSOs(dsos);
			es.setNumOfWorkLists(wls);
			es.setNumOfAutoQueries(pacQueries);
			es.setNumOfFiles(files);
			es.setNumOfTemplates(templates);
			es.setNumOfPlugins(plugins);
			es.setCreator("admin");
			return es;
		}
		catch (Exception x)
		{
			if (exceptionOnErr)
				throw x;
			else
				return null;
		}
	}

	@Override
	public void createEventLog(String username, String projectID,
			String subjectID, String studyUID, String seriesUID,
			String imageUID, String aimID, String function,
			String params) {
		createEventLog(username, projectID, subjectID, studyUID, seriesUID, imageUID, aimID, null, function, params, false);
	}

	@Override
	public void createEventLog(String username, String projectID,
			String subjectID, String studyUID, String seriesUID,
			String imageUID, String aimID, String filename, String function,
			String params, boolean error) {
		EventLog elog = new EventLog();
		elog.setUsername(username);
		elog.setProjectID(projectID);
		elog.setSubjectUID(subjectID);
		elog.setStudyUID(studyUID);
		elog.setSeriesUID(seriesUID);
		elog.setImageUID(imageUID);
		elog.setAimID(aimID);
		elog.setFilename(filename);
		elog.setFunction(function);
		elog.setParams(params);
		elog.setError(error);
		try {
			elog.save();
		} catch (Exception e) {
			log.warning("Error saving event log", e);
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#addUserToProject(java.lang.String, java.lang.String, java.lang.String, edu.stanford.epad.epadws.models.UserRole)
	 */
	@Override
	public void addUserToProject(String loggedInUser, String projectId,
			String username, UserRole role, String defaultTemplate) throws Exception {
		User user = getUser(username);
		Project project = getProject(projectId);
		ProjectToUser ptou = (ProjectToUser) new ProjectToUser().getObject("project_id = " + project.getId() + " and user_id=" + user.getId());
		if (ptou == null)
		{
			ptou = new ProjectToUser();
			ptou.setCreator(loggedInUser);
		}
		ptou.setProjectId(project.getId());
		ptou.setUserId(user.getId());
		if (role != null)
			ptou.setRole(role.getName());
		if (ptou.getRole() == null || ptou.getRole().length() == 0)
			ptou.setRole(UserRole.COLLABORATOR.getName());
		if (defaultTemplate != null && defaultTemplate.length() > 0)
			ptou.setDefaultTemplate(defaultTemplate);
		ptou.save();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#removeUserFromProject(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void removeUserFromProject(String loggedInUser, String projectId,
			String username) throws Exception {
		User user = getUser(username);
		Project project = getProject(projectId);
		ProjectToUser ptou = (ProjectToUser) new ProjectToUser().getObject("project_id = " + project.getId() + " and user_id=" + user.getId());
		if (ptou != null)
			ptou.delete();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#setUserRoleForProject(java.lang.String, java.lang.String, java.lang.String, edu.stanford.epad.epadws.models.UserRole)
	 */
	@Override
	public void setUserRoleForProject(String loggedInUser, String projectId,
			String username, UserRole role) throws Exception {
		User user = getUser(username);
		Project project = getProject(projectId);
		ProjectToUser ptou = new ProjectToUser();
		ptou = (ProjectToUser) ptou.getObject("project_id = " + project.getId() + " and user_id =" + user.getId());
		ptou.setRole(role.getName());
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#createSubject(java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.lang.String)
	 */
	@Override
	public Subject createSubject(String loggedInUser, String subjectUID,
			String name, Date dob, String gender) throws Exception {
		return createSubject(loggedInUser, subjectUID,
				name, dob, gender, false);
	}
	
	@Override
	public Subject createSubject(String loggedInUser, String subjectUID,
			String name, Date dob, String gender, boolean changeOwner) throws Exception {
		Subject subject = getSubject(subjectUID);
		if (subject == null) subject = new Subject();
		subject.setSubjectUID(subjectUID);
		if (name != null && name.trim().length() > 0) subject.setName(name);
		if (subject.getName() == null) subject.setName("");
		if (dob != null) subject.setDob(dob);
		if (gender != null && name.trim().length() > 0) subject.setGender(gender);
		if (subject.getId() == 0)
		{
			subject.setCreator(loggedInUser);
			this.createEventLog(loggedInUser, null, subjectUID, null, null, null, null, null, "Created Patient", null, false);
		}
		if (changeOwner)
			subject.setCreator(loggedInUser);
		subject.save();
		//subjectCache.put(subject.getSubjectUID(), subject);
		return subject;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#createStudy(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Study createStudy(String loggedInUser, String studyUID,
			String subjectUID, String description) throws Exception {
		return createStudy(loggedInUser, studyUID,
				subjectUID, description, null);
	}
	@Override
	public Study createStudy(String loggedInUser, String studyUID,
			String subjectUID, String description, Date studyDate) throws Exception {
		return createStudy(loggedInUser, studyUID,
				subjectUID, description, studyDate, false);
	}
	
	@Override
	public Study createStudy(String loggedInUser, String studyUID,
			String subjectUID, String description, Date studyDate, boolean changeOwner) throws Exception {
		Subject subject = getSubject(subjectUID);
		Study study = getStudy(studyUID);
		if (study == null)
		{
			study = new Study();
			study.setCreator(loggedInUser);
			this.createEventLog(loggedInUser, null, subjectUID, studyUID, null, null, null, null, "Created Study", null, false);
		}
		if (changeOwner)
			study.setCreator(loggedInUser);
		study.setStudyUID(studyUID);
		study.setSubjectId(subject.getId());
		if (description != null && description.length() > 0)
			study.setDescription(description);
		if (studyDate != null)
			study.setStudyDate(studyDate);
		study.save();
		return study;
	}

	@Override
	public NonDicomSeries createNonDicomSeries(String loggedInUser, String seriesUID,
			String studyUID, String description, Date seriesDate, String modality, String referencedSeries)
			throws Exception {
		Study study = getStudy(studyUID);
		if (study == null)
			throw new Exception("Study " + studyUID + " not found");
		NonDicomSeries series = getNonDicomSeries(seriesUID);
		if (series != null && series.getStudyId() != study.getId())
			throw new Exception("Series " + seriesUID + " already exists for another study");
		if (series == null)
		{
			DCM4CHEESeries dcm4cheeSeries = Dcm4CheeQueries.getSeries(seriesUID);
			if (dcm4cheeSeries != null)
				throw new Exception("Series already exists in DCM4CHEE");
			series = new NonDicomSeries();
			this.createEventLog(loggedInUser, null, null, studyUID, seriesUID, null, null, null, "Created Study", null, false);
		}
		series.setSeriesUID(seriesUID);
		series.setSeriesDate(seriesDate);
		series.setStudyId(study.getId());
		series.setDescription(description);
		series.setModality(modality);
		series.setReferencedSeries(referencedSeries);
		series.save();
		return series;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#addSubjectToProject(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addSubjectToProject(String loggedInUser, String subjectUID,
			String projectId) throws Exception {
		Subject subject = new Subject();
		subject = (Subject) subject.getObject("subjectuid = " + subject.toSQL(subjectUID));
		Project project = new Project();
		project = (Project) project.getObject("projectId = " + project.toSQL(projectId));
		List<ProjectToSubject> ptss = new ProjectToSubject().getObjects("subject_id=" + subject.getId());
		if (ptss.size() == 1 && subject.getCreator().equals("admin"))
		{
			subject.setCreator(loggedInUser);
			subject.save();
		}
		ProjectToSubject ptos = (ProjectToSubject) new ProjectToSubject().getObject("project_id =" + project.getId() + " and subject_id=" + subject.getId());
		if (ptos == null)
		{
			ptos = new ProjectToSubject();
			ptos.setProjectId(project.getId());
			ptos.setSubjectId(subject.getId());
			ptos.setCreator(loggedInUser);
			ptos.save();
		}
		List<Study> studies = this.getStudiesForSubject(subjectUID);
		for (Study study: studies)
		{
			addStudyToProject(loggedInUser, study.getStudyUID(), subjectUID, projectId);
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#addStudyToProject(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addStudyToProject(String loggedInUser, String studyUID,
			String subjectUID, String projectId) throws Exception {
		Study study = getStudy(studyUID);
		Subject subject = new Subject();
		if ((subjectUID == null || subjectUID.length() == 0) && study != null)
		{
			subject.setId(study.getSubjectId());
			subject = (Subject) subject.retrieve();
		}
		else	
		{
			subject = getSubject(subjectUID);
		}
		if (study == null)
		{
			study = new Study();
			study.setStudyUID(studyUID);
			study.setSubjectId(subject.getId());
			study.setCreator(loggedInUser);
			study.save();
		}
		
		Project project = getProject(projectId);
		ProjectToSubject ptos = new ProjectToSubject();
		ptos = (ProjectToSubject) ptos.getObject("project_id = " + project.getId() + " and subject_id =" + subject.getId());
		if (ptos == null)
		{
			ptos = new ProjectToSubject();
			ptos.setProjectId(project.getId());
			ptos.setSubjectId(subject.getId());
			ptos.setCreator(loggedInUser);
			ptos.save();
			this.createEventLog(loggedInUser, projectId, subjectUID, null, null, null, null, null, "Added Patient to Project", null, false);
		}
		List<ProjectToSubjectToStudy> psss = new ProjectToSubjectToStudy().getObjects("study_id=" + study.getId());
		if (psss.size() == 1 && study.getCreator().equals("admin"))
		{
			study.setCreator(loggedInUser);
			study.save();
		}
		ProjectToSubjectToStudy pss = (ProjectToSubjectToStudy) new ProjectToSubjectToStudy().getObject("proj_subj_id = " + ptos.getId() + " and study_id=" + study.getId());
		if (pss == null)
		{
			pss = new ProjectToSubjectToStudy();
			pss.setProjSubjId(ptos.getId());
			pss.setStudyId(study.getId());
			pss.setCreator(loggedInUser);
			pss.save();
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#removeSubjectFromProject(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void removeSubjectFromProject(String loggedInUser,
			String subjectUID, String projectId) throws Exception {
		Subject subject = getSubject(subjectUID);
		Project project = new Project();
		project = (Project) project.getObject("projectId = " + project.toSQL(projectId));
		ProjectToSubject ptos = new ProjectToSubject();
		ptos = (ProjectToSubject) ptos.getObject("project_id = " + project.getId() + " and subject_id =" + subject.getId());
		ProjectToSubjectToStudy pss = new ProjectToSubjectToStudy();
		pss.deleteObjects("proj_subj_id = " + ptos.getId());
		ptos.delete();
		new WorkListToSubject().deleteObjects("project_id = " + project.getId() + " and subject_id =" + subject.getId());
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#removeStudyFromProject(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void removeStudyFromProject(String loggedInUser, String studyUID,
			String projectId) throws Exception {
		Study study = getStudy(studyUID);
		Subject subject = new Subject();
		subject.setId(study.getSubjectId());
		subject = (Subject) subject.retrieve();
		Project project = new Project();
		project = (Project) project.getObject("projectId = " + project.toSQL(projectId));
		ProjectToSubject ptos = new ProjectToSubject();
		ptos = (ProjectToSubject) ptos.getObject("project_id = " + project.getId() + " and subject_id =" + subject.getId());
		ProjectToSubjectToStudy pss = new ProjectToSubjectToStudy();
		pss.deleteObjects("proj_subj_id = " + ptos.getId() + " and study_id =" + study.getId());
		new WorkListToStudy().deleteObjects("project_id = " + project.getId() + " and study_id =" + study.getId());
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#isSubjectInProject(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isSubjectInProject(String subjectUID, String projectId) throws Exception {
		Subject subject = getSubject(subjectUID);
		if (subject == null)
			throw new Exception("Subject not found, ID:" + subjectUID);
		Project project = getProject(projectId);
		if (project == null)
			throw new Exception("Project not found, ID:" + subjectUID);
		ProjectToSubject ptos = new ProjectToSubject();
		ptos = (ProjectToSubject) ptos.getObject("project_id = " + project.getId() + " and subject_id =" + subject.getId());
		if (ptos == null)
			return false;
		else
			return true;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#createFile(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.io.File, java.lang.String, java.lang.String, edu.stanford.epad.epadws.models.FileType)
	 */
	@Override
	public EpadFile createFile(String loggedInUser, String projectID,
			String subjectUID, String studyUID, String seriesUID, File file,
			String filename, String description, FileType fileType) throws Exception {
		User requestor = getUser(loggedInUser);
		EpadFile efile = new EpadFile();
		efile.setName(filename);
		efile.setDescription(description);
		if (projectID != null && projectID.length() > 0)
		{
			Project project = getProject(projectID);
			if (project == null)
				throw new Exception("Project " + projectID + " not found");
			efile.setProjectId(project.getId());
		}
		if (subjectUID != null && subjectUID.length() > 0)
		{
			Subject subject = getSubject(subjectUID);
			if (subject == null)
				throw new Exception("Patient " + subjectUID + " not found");
			efile.setSubjectId(subject.getId());
		}
		if (studyUID != null && studyUID.length() > 0)
		{
			Study study = getStudy(studyUID);
			if (study == null)
				throw new Exception("Study " + studyUID + " not found");
			efile.setStudyId(study.getId());
		}
		boolean exists = false;
		EpadFile oldFile = this.getEpadFile(projectID, subjectUID, studyUID, seriesUID, filename);
		if (oldFile != null)
		{
			if (!requestor.isAdmin() && !isOwner(loggedInUser, projectID) && !loggedInUser.equals(oldFile.getCreator()))
				throw new Exception("No permissions to overwrite file");
			efile = oldFile;
			exists = true;
		}
		else
		{
			efile.setSeriesUid(seriesUID);
			efile.setFilePath(EPADConfig.getEPADWebServerFilesDir() + efile.getRelativePath());
			efile.setCreator(loggedInUser);
		}
		if (fileType != null)
			efile.setFileType(fileType.getName());
		else
			efile.setFileType("");
		efile.setLength(file.length());
		if (description != null)
			efile.setDescription(description);
		efile.save();
		File parent = new File(efile.getFilePath());
		parent.mkdirs();
		String physicalName = "" + efile.getId() + efile.getExtension();
		FileUtils.copyFile(file, new File(parent, physicalName));
		if (exists)
			log.info("Modified file:" + efile.getName() + " in Project:" + efile.getProjectId());
		else
			log.info("Created file:" + efile.getName() + " in Project:" + efile.getProjectId());
		return efile;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getProject(java.lang.String)
	 */
	@Override
	public Project getProject(String projectId) throws Exception {
		Project project = projectCache.get(projectId);
		if (project != null)
			return project;
		project = new Project();
		project = (Project) project.getObject("projectId = " + project.toSQL(projectId));
		return project;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String username) throws Exception {
		User user = userCache.get(username);
		if (user == null)
		{
			user = new User();
			user = (User) user.getObject("username = " + user.toSQL(username));
			if (user == null) return user;
			userCache.put(user.getUsername(), user);
		}
		user.setProjectToRole(new HashMap<String, String>());
		List<ProjectToUser> ptous = new ProjectToUser().getObjects("user_id = " + user.getId());
		for (ProjectToUser ptou: ptous)
		{
			Project project = (Project)new Project(ptou.getProjectId()).retrieve();
			user.getProjectToRole().put(project.getProjectId(), ptou.getRole());
		}
		return user;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getUserByEmail(java.lang.String)
	 */
	@Override
	public User getUserByEmail(String email) throws Exception {
		User user = new User();
		List objects = user.getObjects("email = " + user.toSQL(email));
		if (objects.size() > 0)
			return (User) objects.get(0);
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getSubject(java.lang.String)
	 */
	@Override
	public Subject getSubject(String subjectUID) throws Exception {
		Subject subject = subjectCache.get(subjectUID);
		if (subject != null)
			return subject;
		subject = new Subject();
		subject = (Subject) subject.getObject("subjectuid = " + subject.toSQL(subjectUID));
		return subject;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getStudy(java.lang.String)
	 */
	@Override
	public Study getStudy(String studyUID) throws Exception {
		Study study = new Study();
		study = (Study) study.getObject("studyUID = " + study.toSQL(studyUID));
		return study;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getAllProjects()
	 */
	@Override
	public List<Project> getAllProjects() throws Exception {
		List objects = new Project().getObjects("1 = 1 order by projectId");
		List<Project> projects = new ArrayList<Project>();
		projects.addAll(objects);
		return projects;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getPublicProjects()
	 */
	@Override
	public List<Project> getPublicProjects() throws Exception {
		List objects = new Project().getObjects("type = '" + ProjectType.PUBLIC + "' order by projectId");
		List<Project> projects = new ArrayList<Project>();
		projects.addAll(objects);
		return projects;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getAllUsers()
	 */
	@Override
	public List<User> getAllUsers() throws Exception {
		List objects = new User().getObjects("1 = 1 order by username");
		List<User> users = new ArrayList<User>();
		users.addAll(objects);
		for (User user: users)
		{
			user.setProjectToRole(new HashMap<String, String>());
			List<ProjectToUser> ptous = new ProjectToUser().getObjects("user_id = " + user.getId());
			for (ProjectToUser ptou: ptous)
			{
				Project project = (Project)new Project(ptou.getProjectId()).retrieve();
				user.getProjectToRole().put(project.getProjectId(), ptou.getRole());
			}
		}
		return users;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getProjectsForUser(java.lang.String)
	 */
	@Override
	public List<Project> getProjectsForUser(String username) throws Exception {
		User user = getUser(username);
		List<Project> projects = this.getAllProjects();
		for (int i = 0; i < projects.size(); i++)
		{
			Project project = projects.get(i);
			List<ProjectToUser> p2us = new ProjectToUser().getObjects("user_id =" + user.getId() + " and project_id=" + project.getId());
			if (p2us.size() > 0)
			{
				ProjectToUser p2u = p2us.get(0);
				if (p2u.getDefaultTemplate() != null && p2u.getDefaultTemplate().length() > 0)
					project.setDefaultTemplate(p2u.getDefaultTemplate());
			}
			else if (username.equals("admin") || user.isAdmin() || project.getType().equals(ProjectType.PUBLIC.getName()))
			{
			}
			else
			{
				projects.remove(i);
				i--;
			}
		}
		return projects;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getProjectForUser(java.lang.String, java.lang.String)
	 */
	@Override
	public Project getProjectForUser(String username, String projectID) throws Exception {
		Project project = getProject(projectID);
		if (project == null) return null;
		User user = getUser(username);
		List<ProjectToUser> p2us = new ProjectToUser().getObjects("user_id =" + user.getId() + " and project_id=" + project.getId());
		if (p2us.size() > 0)
		{
			ProjectToUser p2u = p2us.get(0);
			if (p2u.getDefaultTemplate() != null && p2u.getDefaultTemplate().length() > 0)
				project.setDefaultTemplate(p2u.getDefaultTemplate());
			return project;
		}
		else if (username.equals("admin") || user.isAdmin() || project.getType().equals(ProjectType.PUBLIC.getName()))
			return project;
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getUsersForProject(java.lang.String)
	 */
	@Override
	public List<User> getUsersForProject(String projectId) throws Exception {
		Project project = getProject(projectId);
		if (project == null) return new ArrayList<User>();
		
		return getUsersByProjectId(project.getId());
	}

	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private List<User> getUsersByProjectId(long id)
			throws Exception {
		List objects = new User().getObjects("id in (select user_id from " 
													+ ProjectToUser.DBTABLE 
													+ " where project_id =" + id + ")");
		List<User> users = new ArrayList<User>();
		users.addAll(objects);
		
		return users;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getUsersWithRoleForProject(java.lang.String)
	 */
	@Override
	public List<User> getUsersWithRoleForProject(String projectId)
			throws Exception {
		Project project = getProject(projectId);
		if (project == null) return new ArrayList<User>();
		List ptous = new ProjectToUser().getObjects("project_id = " + project.getId());
		List<User> users = new ArrayList<User>();
		for (Object ptou: ptous)
		{
			long userId = ((ProjectToUser) ptou).getUserId();
			User user = new User();
			user.setId(userId);
			user = (User) user.retrieve();
			user.setRole(((ProjectToUser) ptou).getRole());
			users.add(user);
		}
		return users;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getSubjectsForProject(java.lang.String)
	 */
	@Override
	public List<Subject> getSubjectsForProject(String projectId)
			throws Exception {
		return getSubjectsForProject(projectId, null);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getSubjectsForProject(java.lang.String)
	 */
	@Override
	public List<Subject> getSubjectsForProject(String projectId, String sortBy)
			throws Exception {
		Project project = getProject(projectId);
		if (project == null) return new ArrayList<Subject>();
		
		return getSubjectsByProjectId(project.getId(), sortBy);
	}

	@Override
	public List<Subject> getUnassignSubjects() throws Exception {
		Project project = this.getProject(EPADConfig.xnatUploadProjectID);
		List psAll = new ProjectToSubject().getObjects("project_id = " + project.getId());
		Set<Long> allIds = new HashSet<Long>();
		for (Object obj: psAll)
		{
			long id = ((ProjectToSubject) obj).getSubjectId();
			allIds.add(id);
		}
		List psAsssigned = new ProjectToSubject().getObjects("project_id != " + project.getId());
		for (Object obj: psAsssigned)
		{
			long id = ((ProjectToSubject) obj).getSubjectId();
			if (!allIds.contains(id))
			{
				Subject subject = (Subject) new Subject().getObject("id = " + id);
				log.info("Adding " + subject.getSubjectUID() + " to " + EPADConfig.xnatUploadProjectID);
				this.addSubjectToProject("admin", subject.getSubjectUID(), EPADConfig.xnatUploadProjectID);
			}
		}
		for (Object obj: psAsssigned)
		{
			long id = ((ProjectToSubject) obj).getSubjectId();
			allIds.remove(id);
		}
		String inclause = "";
		String delim = "(";
		for (Long id: allIds)
		{
			inclause = inclause + delim + id;
			delim = ",";
		}
		List<Study> studies = new Study().getObjects("id not in (select distinct study_id from " + ProjectToSubjectToStudy.DBTABLE + ")");
		for (Study study: studies)
		{
			inclause = inclause + delim + study.getSubjectId();
			delim = ",";
		}
		if (inclause.length() == 0)
			return new ArrayList<Subject>();
		List objects = new Subject().getObjects("id in " + inclause + ") order by name");
		List<Subject> subjects = new ArrayList<Subject>();
		subjects.addAll(objects);
		
		return subjects;
	}

	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private List<Subject> getSubjectsByProjectId(long id, String sortBy)
			throws Exception {
		if (sortBy == null || sortBy.trim().length() == 0)
			sortBy = "name";
		else if (sortBy.equalsIgnoreCase("SubjectId"))
			sortBy = "subjectUID";
			
		List objects = new Subject().getObjects("id in (select subject_id from " 
													+ ProjectToSubject.DBTABLE 
													+ " where project_id =" + id + ") order by " + sortBy);
		List<Subject> subjects = new ArrayList<Subject>();
		subjects.addAll(objects);
		
		return subjects;
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getSubjectForProject(java.lang.String, java.lang.String)
	 */
	@Override
	public Subject getSubjectForProject(String projectId, String subjectUID)
			throws Exception {
		Project project = getProject(projectId);
		Subject subject = getSubject(subjectUID);
		if (subject == null) return null;
		if (new ProjectToSubject().getCount("subject_id=" + subject.getId() + " and project_id=" + project.getId()) > 0)
			return subject;
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getSubjectFromName(java.lang.String)
	 */
	@Override
	public Subject getSubjectFromName(String subjectName) throws Exception {
		Subject subject = new Subject();
		List<Subject> subjects = subject.getObjects("name =" +subject.toSQL(subjectName));
		if (subjects.size() > 0)
			return subjects.get(0);
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getSubjectFromNameForProject(java.lang.String, java.lang.String)
	 */
	@Override
	public Subject getSubjectFromNameForProject(String subjectName,
			String projectID) throws Exception {
		if (projectID == null || projectID.trim().length() == 0)
			return getSubjectFromName(subjectName);
		List<Subject> subjects = this.getSubjectsForProject(projectID, null);
		String xnatName = subjectName.replace('^',' ').trim();
		for (Subject subject: subjects)
		{
			if (subjectName.equals(subject.getName())) return subject;
			String xnatName2 = subject.getName().replace('^',' ').trim();
			if (xnatName.equals(xnatName2)) return subject;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getProjectsForSubject(java.lang.String)
	 */
	@Override
	public List<Project> getProjectsForSubject(String subjectUID)
			throws Exception {
		Subject subject = getSubject(subjectUID);
		List objects = new Project().getObjects("id in (select project_id from " 
													+ ProjectToSubject.DBTABLE 
													+ " where subject_id =" + subject.getId() + ")");
		List<Project> projects = new ArrayList<Project>();
		projects.addAll(objects);
		
		return projects;
	}

	@Override
	public List<Project> getProjectsForStudy(String studyUID) throws Exception {
		Study study = getStudy(studyUID);
		List<Project> projects = new ArrayList<Project>();
		List<AbstractDAO> psss = new ProjectToSubjectToStudy().getObjects("study_id=" + study.getId());
		if (psss.size() == 0) return projects;
		List objects = new Project().getObjects("id in (select project_id from " 
													+ ProjectToSubject.DBTABLE 
													+ " where id in (" + getIdList(psss) + "))");
		projects.addAll(objects);
		
		return projects;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getStudiesForProjectAndSubject(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Study> getStudiesForProjectAndSubject(String projectId,
			String subjectUID) throws Exception {
		Project project = getProject(projectId);
		Subject subject = getSubject(subjectUID);
		ProjectToSubject ptos = (ProjectToSubject) new ProjectToSubject().getObject("project_id = " + project.getId() + " and subject_id=" + subject.getId());
		List<Study> studies = new ArrayList<Study>();
		if (ptos == null)
			return studies;
		List objects = new Study().getObjects("id in (select study_id from " 
				+ ProjectToSubjectToStudy.DBTABLE 
				+ " where proj_subj_id =" + ptos.getId() + ")");
		studies.addAll(objects);

		return studies;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getFirstProjectForStudy(java.lang.String)
	 */
	@Override
	public Project getFirstProjectForStudy(String studyUID) throws Exception {
		Study study = getStudy(studyUID);
		List<ProjectToSubject> ptoss = new ProjectToSubject().getObjects("subject_id=" + study.getSubjectId());
		Map<Long, ProjectToSubject> ptosIdMap = new HashMap<Long, ProjectToSubject>();
		for (ProjectToSubject ptos: ptoss)
			ptosIdMap.put(ptos.getId(), ptos);
		List<ProjectToSubjectToStudy> psss = new ProjectToSubjectToStudy().getObjects("proj_subj_id in " + study.toSQL(ptosIdMap.keySet()) + " and study_id=" + study.getId());
		if (psss.size() > 0)
		{
			Project project = new Project();
			long ptosId = ((ProjectToSubjectToStudy)psss.get(0)).getProjSubjId();
			project.setId(ptosIdMap.get(ptosId).getId());
			project = (Project) project.retrieve();
			return project;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#isStudyInProjectAndSubject(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isStudyInProjectAndSubject(String projectId,
			String subjectUID, String studyUID) throws Exception {
		Project project = getProject(projectId);
		Subject subject = getSubject(subjectUID);
		Study study = getStudy(studyUID);
		ProjectToSubject ptos = (ProjectToSubject) new ProjectToSubject().getObject("project_id = " + project.getId() + " and subject_id=" + subject.getId());
		if (ptos == null)
			return false;
		return new ProjectToSubjectToStudy().getCount("proj_subj_id = " + ptos.getId() + " and study_id=" + study.getId()) > 0;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getAllStudiesForProject(java.lang.String)
	 */
	@Override
	public List<Study> getAllStudiesForProject(String projectId) throws Exception {
		Project project = getProject(projectId);
		List objects = new ProjectToSubject().getObjects("project_id = " + project.getId());
		List<Study> studies = new ArrayList<Study>();
		for (Object ptos: objects)
		{
			List sobjects = new Study().getObjects("id in (select study_id from " 
					+ ProjectToSubjectToStudy.DBTABLE 
					+ " where proj_subj_id =" + ((ProjectToSubject)ptos).getId() + ")");
			studies.addAll(sobjects);
			
		}
		return studies;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getStudiesForSubject(java.lang.String)
	 */
	@Override
	public List<Study> getStudiesForSubject(String subjectUID) throws Exception {
		Subject subject = getSubject(subjectUID);
		if (subject == null)
			return new ArrayList<Study>();
		List objects = new Study().getObjects("subject_id  =" + subject.getId());
		List<Study> studies = new ArrayList<Study>();
		studies.addAll(objects);		
		return studies;
	}

	@Override
	public Subject getSubjectForStudy(String studyUID) throws Exception {
		Study study = this.getStudy(studyUID);
		if (study == null)
			throw new Exception("Study not found, studyUID:" + studyUID);
		Subject subject = (Subject) this.getDBObject(Subject.class, study.getSubjectId());
		return subject;
	}

	@Override
	public List<NonDicomSeries> getNonDicomSeriesForStudy(String studyUID)
			throws Exception {
		Study study = getStudy(studyUID);
		if (study == null)
			throw new Exception("Study " + studyUID + " not found");
		List objects = new NonDicomSeries().getObjects("study_id  =" + study.getId());
		List<NonDicomSeries> serieses = new ArrayList<NonDicomSeries>();
		serieses.addAll(objects);		
		return serieses;
	}

	@Override
	public NonDicomSeries getNonDicomSeries(String seriesUID) throws Exception {
		NonDicomSeries series = new NonDicomSeries();
		series = (NonDicomSeries) series.getObject("seriesUID = " + series.toSQL(seriesUID));
		return series;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#setUserStatusForProjectAndSubject(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setUserStatusForProjectAndSubject(String loggedInUser,
			String projectID, String subjectUID, String status) throws Exception {
		Project project = getProject(projectID);
		Subject subject = getSubject(subjectUID);
		User user = getUser(loggedInUser);
		ProjectToSubject ptos = (ProjectToSubject)new ProjectToSubject().getObject("project_id=" + project.getId() + " and subject_id=" + subject.getId());
		ProjectToSubjectToUser psu = (ProjectToSubjectToUser)new ProjectToSubjectToUser().getObject("proj_subj_id=" + ptos.getId() + " and user_id=" + user.getId());
		if (psu == null)
		{
			psu = new ProjectToSubjectToUser();
			psu.setProjSubjId(ptos.getId());
			psu.setUserId(user.getId());
			psu.setCreator(loggedInUser);
		}
		psu.setStatus(status);
		psu.save();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getUserStatusForProjectAndSubject(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getUserStatusForProjectAndSubject(String loggedInUser,
			String projectID, String subjectUID) throws Exception {
		Project project = getProject(projectID);
		Subject subject = getSubject(subjectUID);
		User user = getUser(loggedInUser);
		ProjectToSubject ptos = (ProjectToSubject)new ProjectToSubject().getObject("project_id=" + project.getId() + " and subject_id=" + subject.getId());
		ProjectToSubjectToUser psu = (ProjectToSubjectToUser)new ProjectToSubjectToUser().getObject("proj_subj_id=" + ptos.getId() + " and user_id=" + user.getId());
		if (psu == null)
			return null;
		else
			return psu.getStatus();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getUserStatusForProjectSubjects(java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> getUserStatusForProjectSubjects(
			String loggedInUser, String projectID) throws Exception {
		Project project = getProject(projectID);
		User user = getUser(loggedInUser);
		List<Subject> subjects = getSubjectsByProjectId(project.getId(), null);
		Map<Long, String> subjectIdToUID = new HashMap<Long, String>();
		for (Subject subject: subjects)
		{
			subjectIdToUID.put(subject.getId(), subject.getSubjectUID());
		}
		List ptoss = new ProjectToSubject().getObjects("project_id=" + project.getId());
		List<Long> ptosIds = new ArrayList<Long>();
		Map<Long, Long> ptosIdToSubjectID = new HashMap<Long, Long>();
		for (Object ptos: ptoss)
		{
			ptosIds.add(((ProjectToSubject)ptos).getId());
			ptosIdToSubjectID.put(((ProjectToSubject)ptos).getId(), ((ProjectToSubject)ptos).getSubjectId());
		}
		List psus = new ProjectToSubjectToUser().getObjects("proj_subj_id in " + project.toSQL(ptosIds) + " and user_id=" + user.getId());
		Map<String, String> subjectStatus = new HashMap<String, String>();
		for (Object psu: psus)
		{
			String status = ((ProjectToSubjectToUser) psu).getStatus();
			Long subjectId = ptosIdToSubjectID.get(((ProjectToSubjectToUser) psu).getProjSubjId());
			String subjectUID = subjectIdToUID.get(subjectId);
			subjectStatus.put(subjectUID, status);
		}
		return subjectStatus;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getUserStatusForProjectSubjectIds(java.lang.String, java.lang.String)
	 */
	@Override
	public Map<Long, String> getUserStatusForProjectSubjectIds(
			String loggedInUser, String projectID) throws Exception {
		Project project = getProject(projectID);
		User user = getUser(loggedInUser);
		List ptoss = new ProjectToSubject().getObjects("project_id=" + project.getId());
		List<Long> ptosIds = new ArrayList<Long>();
		Map<Long, Long> ptosIdToSubjectID = new HashMap<Long, Long>();
		for (Object ptos: ptoss)
		{
			ptosIds.add(((ProjectToSubject)ptos).getId());
			ptosIdToSubjectID.put(((ProjectToSubject)ptos).getId(), ((ProjectToSubject)ptos).getSubjectId());
		}
		List psus = new ProjectToSubjectToUser().getObjects("proj_subj_id in " + project.toSQL(ptosIds) + " and user_id=" + user.getId());
		Map<Long, String> subjectIdStatus = new HashMap<Long, String>();
		for (Object psu: psus)
		{
			String status = ((ProjectToSubjectToUser) psu).getStatus();
			Long subjectId = ptosIdToSubjectID.get(((ProjectToSubjectToUser) psu).getProjSubjId());
			subjectIdStatus.put(subjectId, status);
		}
		return subjectIdStatus;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#hasAccessToProject(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasAccessToProject(String username, String projectID)
			throws Exception {
		Project project = getProject(projectID);
		if (project == null)
			throw new Exception("Project not found, ID:" + projectID);
		User user = getUser(username);
		if (user == null)
			throw new Exception("User not found, username:" + username);
		if (project.getType().equals(ProjectType.PUBLIC.getName()) ||  user.isAdmin())
			return true;
		if (isUserInProject(user.getId(), project.getId()))
			return true;
		else
			return false;
	}

	@Override
	public boolean hasAccessToProject(String username, long id)
			throws Exception {
		Project project = (Project) this.getDBObject(Project.class, id);
		if (project == null)
			throw new Exception("Project not found, ID:" + id);
		User user = getUser(username);
		if (user == null)
			throw new Exception("User not found, username:" + username);
		if (project.getType().equals(ProjectType.PUBLIC.getName()) ||  user.isAdmin())
			return true;
		if (isUserInProject(user.getId(), project.getId()))
			return true;
		else
			return false;
	}

	protected boolean isUserInProject(long userId, long projectId)
			throws Exception {
		ProjectToUser pu = (ProjectToUser) new ProjectToUser().getObject("project_id =" + projectId + " and user_id=" + userId);
		if (pu == null)
			return false;
		else
			return true;
	}

	@Override
	public boolean isAdmin(String username) throws Exception {
		User user = getUser(username);
		if (user != null)	
			return user.isAdmin();
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#isCollaborator(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isCollaborator(String username, String projectID) throws Exception {
		if (projectID == null || projectID.trim().length() == 0)
		{
			if (isAdmin(username)) 
				return false;
			else
				return true;
		}
		UserRole role = getUserProjectRole(username, projectID);
		if (role == null && isAdmin(username)) return false;
		if (role == null && projectID.equals(EPADConfig.xnatUploadProjectID)) return true;
		if (role == null)
			throw new Exception("User " + username  + " does not exist in project:" + projectID);
		if (role.equals(UserRole.COLLABORATOR))
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#isMember(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isMember(String username, String projectID)
			throws Exception {
		if (projectID == null || projectID.trim().length() == 0)
			return false;
		UserRole role = getUserProjectRole(username, projectID);
		if (role == null && isAdmin(username)) return true;
		if (role == null) return false;
		if (role.equals(UserRole.MEMBER))
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#isOwner(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isOwner(String username, String projectID)
			throws Exception {
		if (projectID == null || projectID.trim().length() == 0)
			return false;
		Project project = getProject(projectID);
		if (project.getCreator().equals(username))
			return true;
		UserRole role = getUserProjectRole(username, projectID);
		if (role == null && isAdmin(username)) return true;
		if (role == null) return false;
		if (role.equals(UserRole.OWNER))
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getUserProjectRole(java.lang.String, java.lang.String)
	 */
	@Override
	public UserRole getUserProjectRole(String username,
			String projectID) throws Exception {
		User user = getUser(username);
		Project project = getProject(projectID);
		ProjectToUser pu = (ProjectToUser) new ProjectToUser().getObject("project_id =" + project.getId() + " and user_id=" + user.getId());
		if (pu == null)
			return null;
		String role = pu.getRole();
		return UserRole.getRole(role);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getEpadFile(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public EpadFile getEpadFile(String projectID,
			String subjectUID, String studyUID, String seriesUID,
			String filename) throws Exception {
		String criteria = "";
		if (projectID != null && projectID.length() > 0)
		{
			Project project = getProject(projectID);
			criteria = criteria + "project_id = " + project.getId();
		}
		else
			criteria = criteria + "project_id is null";
		if (subjectUID != null && subjectUID.length() > 0)
		{
			Subject subject = getSubject(subjectUID);
			criteria = criteria + " and subject_id = " + subject.getId();
		}
		else
			criteria = criteria + " and subject_id is null";
		if (studyUID != null && studyUID.length() > 0)
		{
			Study study = getStudy(studyUID);
			criteria = criteria + " and study_id = " + study.getId();
		}
		else
			criteria = criteria + " and study_id is null";
		if (seriesUID != null && seriesUID.length() > 0)
		{
			criteria = criteria + " and series_uid = '" + seriesUID + "'";
		}
		else
			criteria = criteria + " and series_uid is null";
		
		criteria = criteria + " and name = " + new EpadFile().toSQL(filename);
		EpadFile file = (EpadFile) new EpadFile().getObject(criteria);
		return file;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getEpadFiles(java.lang.String, java.lang.String, java.lang.String, java.lang.String, FileType)
	 */
	@Override
	public List<EpadFile> getEpadFiles(String projectID,
			String subjectUID, String studyUID, String seriesUID,
			FileType fileType, boolean toplevelOnly) throws Exception {
		String criteria = "1 = 1";
		if (projectID != null && projectID.length() > 0)
		{
			Project project = getProject(projectID);
			if (project == null) return new ArrayList<EpadFile>();
			criteria = criteria + " and project_id = " + project.getId();
		}
		else if (toplevelOnly)
		{
			criteria = criteria + " and project_id is null";
		}

		if (subjectUID != null && subjectUID.length() > 0)
		{
			Subject subject = getSubject(subjectUID);
			criteria = criteria + " and subject_id = " + subject.getId();
		}
		else if (toplevelOnly)
		{
			criteria = criteria + " and subject_id is null";
		}

		if (studyUID != null && studyUID.length() > 0)
		{
			Study study = getStudy(studyUID);
			criteria = criteria + " and study_id = " + study.getId();
		}
		else if (toplevelOnly)
		{
			criteria = criteria + " and study_id is null";
		}

		if (seriesUID != null && seriesUID.length() > 0)
		{
			criteria = criteria + " and series_uid = '" + seriesUID + "'";
		}
		else if (toplevelOnly)
		{
			criteria = criteria + " and series_uid is null";
		}
		
		if (fileType != null)
		{
			criteria = criteria + " and filetype = '" + fileType.getName() + "'";
		}
		return new EpadFile().getObjects(criteria);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getProjectFiles(java.lang.String)
	 */
	@Override
	public List<EpadFile> getProjectFiles(String projectID, boolean toplevelOnly) throws Exception {
		Project project = getProject(projectID);
		String selectNull = "";
		if (toplevelOnly)
			selectNull = " and subject_id is null and study_id is null and series_uid is null";
		List objects = new EpadFile().getObjects("project_id = " + project.getId() + selectNull);
		List<EpadFile> efiles = new ArrayList<EpadFile>();
		efiles.addAll(objects);
		return efiles;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getSubjectFiles(java.lang.String, java.lang.String)
	 */
	@Override
	public List<EpadFile> getSubjectFiles(String projectID, String subjectUID, boolean toplevelOnly) throws Exception {
		Project project = getProject(projectID);
		Subject subject = getSubject(subjectUID);
		String criteria = "subject_id = " + subject.getId();
		if (project != null)
			criteria = criteria + " and project_id =" + project.getId();
		String selectNull = "";
		if (toplevelOnly)
			selectNull = " and study_id is null and series_uid is null";
		List objects = new EpadFile().getObjects(criteria + selectNull);
		List<EpadFile> efiles = new ArrayList<EpadFile>();
		efiles.addAll(objects);
		return efiles;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getStudyFiles(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<EpadFile> getStudyFiles(String projectID, String subjectUID, String studyUID, boolean toplevelOnly) throws Exception {
		Project project = getProject(projectID);
		Subject subject = getSubject(subjectUID);
		Study study = getStudy(studyUID);
		String criteria = "study_id = " + study.getId();
		if (subject != null)
			criteria = criteria + " and subject_id =" + subject.getId();
		if (project != null)
			criteria = criteria + " and project_id =" + project.getId();
		String selectNull = "";
		if (toplevelOnly)
			selectNull = "  and series_uid is null";
		List objects = new EpadFile().getObjects(criteria + selectNull);
		List<EpadFile> efiles = new ArrayList<EpadFile>();
		efiles.addAll(objects);
		return efiles;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getSeriesFiles(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<EpadFile> getSeriesFiles(String projectID, String subjectUID, String studyUID, String seriesUID) throws Exception {
		Project project = getProject(projectID);
		Subject subject = getSubject(subjectUID);
		Study study = getStudy(studyUID);
		String criteria = "series_uid = '" + seriesUID + "'";
		if (study != null)
			criteria = criteria + " and study_id = " + study.getId();
		if (subject != null)
			criteria = criteria + " and subject_id =" + subject.getId();
		if (project != null)
			criteria = criteria + " and project_id =" + project.getId();
		List objects = new EpadFile().getObjects(criteria);
		List<EpadFile> efiles = new ArrayList<EpadFile>();
		efiles.addAll(objects);
		return efiles;
	}

	@Override
	public void enableFile(String loggedInUser, String projectID,
			String subjectUID, String studyUID, String seriesUID,
			String filename) throws Exception {
		EpadFile efile = this.getEpadFile(projectID, subjectUID, studyUID, seriesUID, filename);
		if (efile == null)
			throw new Exception("File " + filename + " not found");
		User requestor = getUser(loggedInUser);
		if (!requestor.isAdmin() && !isOwner(loggedInUser, projectID) && !loggedInUser.equals(efile.getCreator()))
			throw new Exception("No permissions to disable template");
		efile.setEnabled(true);
		efile.save();
	}

	@Override
	public void disableFile(String loggedInUser, String projectID,
			String subjectUID, String studyUID, String seriesUID,
			String filename) throws Exception {
		EpadFile efile = this.getEpadFile(projectID, subjectUID, studyUID, seriesUID, filename);
		if (efile == null)
			throw new Exception("File " + filename + " not found");
		User requestor = getUser(loggedInUser);
		if (!requestor.isAdmin() && !isOwner(loggedInUser, projectID) && !loggedInUser.equals(efile.getCreator()))
			throw new Exception("No permissions to disable template");
		efile.setEnabled(false);
		efile.save();
	}

	@Override
	public void enableTemplate(String loggedInUser, String projectID,
			String subjectUID, String studyUID, String seriesUID,
			String templateName) throws Exception {
		Project project = getProject(projectID);
		if (project == null)
			throw new Exception("Project not found");
		new DisabledTemplate().deleteObjects("project_id = " + project.getId() + " and templatename=" + DisabledTemplate.toSQL(templateName));
	}

	@Override
	public void disableTemplate(String loggedInUser, String projectID,
			String subjectUID, String studyUID, String seriesUID,
			String templateName) throws Exception {
		Project project = getProject(projectID);
		if (project == null)
			throw new Exception("Project not found");
		DisabledTemplate dt = new DisabledTemplate();
		dt.setProjectId(project.getId());
		dt.setTemplateName(templateName);
		dt.setCreator(loggedInUser);
		dt.save();
	}

	@Override
	public EpadFile updateEpadFile(long fileID, String filename,
			String description, String fileType, String mimeType) throws Exception {
		EpadFile efile = new EpadFile();
		efile.setId(fileID);
		efile = (EpadFile) efile.retrieve();
		if (filename != null && filename.trim().length() > 0)
			efile.setName(filename);
		if (description != null && description.trim().length() > 0)
			efile.setDescription(description);
		if (fileType != null && fileType.trim().length() > 0)
			efile.setFileType(fileType);
		if (mimeType != null && mimeType.trim().length() > 0)
			efile.setFileType(mimeType);
		efile.save();
		return efile;
	}

	@Override
	public List<String> getDisabledTemplates(String projectID) throws Exception {
		Project project = getProject(projectID);
		if (project == null)
			throw new Exception("Project not found");
		List<DisabledTemplate> dts = new DisabledTemplate().getObjects("project_id=" + project.getId());
		List<String> templateNames = new ArrayList<String>();
		for (DisabledTemplate dt: dts)
			templateNames.add(dt.getTemplateName());
		return templateNames;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#deleteFile(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteFile(String loggedInUser, String projectID,
			String subjectUID, String studyUID, String seriesUID,
			String filename) throws Exception {
		log.info("Deleting File, projectID:" + projectID + "  subjectUID:" + subjectUID + "  studyUID:" + studyUID + "  seriesUID:" + seriesUID + "  filename:" + filename);
		EpadFile efile = getEpadFile(projectID, subjectUID, studyUID, seriesUID, filename);
		if (efile == null)
			throw new Exception("File not found");
		User requestor = getUser(loggedInUser);
		if (!requestor.isAdmin() && !isOwner(loggedInUser, projectID) && !loggedInUser.equals(efile.getCreator()))
			throw new Exception("No permissions to delete file");
		String path = efile.getFilePath();
		File file = new File(path);
		try {
			if (file.exists())
				file.delete();
		} catch (Exception x) {
			log.warning("Error deleting file:" + file.getAbsolutePath(), x);
		}
		new ProjectToFile().deleteObjects("file_id =" + efile.getId());
		efile.delete();
	}
	
	private void deleteFile(EpadFile efile) throws Exception {
		String path = efile.getFilePath();
		File file = new File(path);
		try {
			if (file.exists())
				file.delete();
		} catch (Exception x) {
			log.warning("Error deleting file:" + file.getAbsolutePath(), x);
		}
		new ProjectToFile().deleteObjects("file_id =" + efile.getId());
		efile.delete();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#deleteProject(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteProject(String username, String projectID)
			throws Exception {
		User requestor = getUser(username);
		if (!requestor.isAdmin() && !isOwner(username, projectID))
			throw new Exception("No permissions to delete project");
		log.info("Deleting project:" + projectID);
		Project project = getProject(projectID);
		if (project == null) return;
		new ProjectToUser().deleteObjects("project_id=" + project.getId());
		new EpadFile().deleteObjects("project_id=" + project.getId());
		new ProjectToPluginParameter().deleteObjects("project_id=" + project.getId() );
		new ProjectToPlugin().deleteObjects("project_id=" + project.getId() );
		new ProjectToSubjectToUser().deleteObjects("proj_subj_id in (select id from " + new ProjectToSubject().returnDBTABLE() + " where project_id=" + project.getId() + ")");
		new ProjectToSubjectToStudy().deleteObjects("proj_subj_id in (select id from " + new ProjectToSubject().returnDBTABLE() + " where project_id=" + project.getId() + ")");
		new ProjectToSubject().deleteObjects("project_id=" + project.getId());
		//ml
		new RemotePACQuery().deleteObjects("project_id=" + project.getId());
		new WorkListToStudy().deleteObjects("project_id=" + project.getId());
		new WorkListToSubject().deleteObjects("project_id=" + project.getId());
		new WorkList().deleteObjects("project_id=" + project.getId());
		try {
			project.delete();
			projectCache.remove(project.getProjectId());
		} catch (Exception x) {
			if (x.getMessage() != null && x.getMessage().contains("constraint")) {
				log.warning("Error deleting project", x);
				throw new Exception("Error deleting project, a PAC Query or Plugin may be referring to this project");
			} else
				throw x;
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#deleteSubject(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteSubject(String username, String subjectUID,
			String projectID) throws Exception {
		User requestor = getUser(username);
		if (!requestor.isAdmin() && !isOwner(username, projectID))
			throw new Exception("No permissions to delete subject");
		Subject subject = getSubject(subjectUID);
		Project project = getProject(projectID);
		if (subject == null) return;
		if (projectID.equals(EPADConfig.xnatUploadProjectID)) {
			List<ProjectToSubject> projSubjs = new ProjectToSubject().getObjects("project_id !=" + project.getId() + " and subject_id=" + subject.getId());
			if (projSubjs.size() > 0)
				throw new Exception("Patient exists in other projects");
		}
		ProjectToSubject projSubj = (ProjectToSubject) new ProjectToSubject().getObject("project_id =" + project.getId() + " and subject_id=" + subject.getId());
		if (projSubj != null)
		{
			new ProjectToSubjectToUser().deleteObjects("proj_subj_id =" + projSubj.getId());
			new ProjectToSubjectToStudy().deleteObjects("proj_subj_id =" + projSubj.getId());
			projSubj.delete();
		}
		new WorkListToSubject().deleteObjects("subject_id =" + subject.getId() + " and project_id =" + project.getId());			
		List projSubjs = new ProjectToSubject().getObjects("subject_id=" + subject.getId());
		// TODO: delete subject if not used any more
		if (projSubjs.size() == 0)
		{
			List<Study> studies = new Study().getObjects("subject_id = " + subject.getId());
			for (Study study: studies)
			{
				new WorkListToStudy().deleteObjects("study_id =" + study.getId());			
				study.delete();
			}
			new WorkListToSubject().deleteObjects("subject_id =" + subject.getId());			
			subject.delete();
			subjectCache.remove(subjectUID);
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#deleteStudy(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteStudy(String username, String studyUID,
			String subjectUID, String projectID) throws Exception {
		User requestor = getUser(username);
		if (!requestor.isAdmin() && !isOwner(username, projectID))
			throw new Exception("No permissions to delete study");
		Subject subject = getSubject(subjectUID);
		Project project = getProject(projectID);
		Study study = getStudy(studyUID);
		if (study == null) return;
		ProjectToSubject projSubj = (ProjectToSubject) new ProjectToSubject().getObject("project_id =" + project.getId() + " and subject_id=" + subject.getId());
		if (projSubj != null) {
			ProjectToSubjectToStudy projSubjStudy = (ProjectToSubjectToStudy) new ProjectToSubjectToStudy().getObject("proj_subj_id =" + projSubj.getId() + " and study_id=" + study.getId());
			if (projSubjStudy != null) projSubjStudy.delete();
		}
		new WorkListToStudy().deleteObjects("study_id =" + study.getId() + " and project_id =" + project.getId());			
		List<ProjectToSubjectToStudy> projSubjStudys = new ProjectToSubjectToStudy().getObjects("study_id=" + study.getId());
		if (projSubjStudys.size() == 0)
		{
			new WorkListToStudy().deleteObjects("study_id =" + study.getId());			
			study.delete();
			List<Study> studies = this.getStudiesForSubject(subjectUID);
			List<EpadFile> files = this.getEpadFiles(null, subjectUID, null, null, null, false);
			if (studies.size() == 0 && files.size() == 0)
				deleteSubject(username, subjectUID);
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#deleteSubject(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteSubject(String username, String subjectUID) throws Exception {
		log.info("Deleting subject:" + subjectUID);
		Subject subject = getSubject(subjectUID);
		if (subject == null) return;
		List<ProjectToSubject> objects = new ProjectToSubject().getObjects("subject_id=" + subject.getId());
		for (ProjectToSubject ptos: objects)
		{
			new ProjectToSubjectToUser().deleteObjects("proj_subj_id =" + ptos.getId());
			new ProjectToSubjectToStudy().deleteObjects("proj_subj_id =" + ptos.getId());
			ptos.delete();
		}
		List<Study> studies = new Study().getObjects("subject_id = " + subject.getId());
		for (Study study: studies)
		{
			new WorkListToStudy().deleteObjects("study_id =" + study.getId());			
			study.delete();
		}
		new EpadFile().deleteObjects("subject_id=" + subject.getId());
		new WorkListToSubject().deleteObjects("subject_id =" + subject.getId());			
		subject.delete();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#deleteStudy(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteStudy(String username, String studyUID) throws Exception {
		Study study = getStudy(studyUID);
		if (study == null) return;
		ProjectToSubjectToStudy projSubjStudy = (ProjectToSubjectToStudy) new ProjectToSubjectToStudy().getObject("study_id=" + study.getId());
		projSubjStudy.delete();
		new EpadFile().deleteObjects("study_id=" + study.getId());
		study.delete();
	}

	@Override
	public void deleteNonDicomSeries(String seriesUID) throws Exception {
		List<EpadFile> files = this.getEpadFiles(null, null, null, seriesUID, null, false);
		for (EpadFile file: files)
		{
			this.deleteFile(file);
		}
		NonDicomSeries nds = (NonDicomSeries) new NonDicomSeries().getObject("seriesUID = " + NonDicomSeries.toSQL(seriesUID));
		if (nds != null)
			nds.delete();
	}

	@Override
	public List<EventLog> getUseEventLogs(String username, int start, int count) throws Exception {
		List<EventLog> events = new EventLog().getObjects("username like '" + username + "' order by createdtime desc", start, count);
		return events;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getReviewers(java.lang.String)
	 */
	@Override
	public List<User> getReviewers(String username) throws Exception {
		List<User> users = new ArrayList<User>();
		List<ReviewerToReviewee> rtrs = new ReviewerToReviewee().getObjects("reviewee = " + DatabaseUtils.toSQL(username));
		for (ReviewerToReviewee rtr: rtrs)
		{
			User reviewer = getUser(rtr.getReviewer());
			users.add(reviewer);
		}
		return users;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getReviewees(java.lang.String)
	 */
	@Override
	public List<User> getReviewees(String username) throws Exception {
		List<User> users = new ArrayList<User>();
		List<ReviewerToReviewee> rtrs = new ReviewerToReviewee().getObjects("reviewer = " + DatabaseUtils.toSQL(username));
		for (ReviewerToReviewee rtr: rtrs)
		{
			User reviewee = getUser(rtr.getReviewee());
			users.add(reviewee);
		}
		return users;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#addReviewer(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addReviewer(String loggedInUser, String username,
			String reviewer) throws Exception {
		if (username.equals(reviewer))
			throw new Exception("Reviewer and reviewee " + username + " are the same");
		List<ReviewerToReviewee> rtrs = new ReviewerToReviewee().getObjects("reviewee = " + DatabaseUtils.toSQL(username) + " and reviewer=" + DatabaseUtils.toSQL(reviewer));
		if (rtrs.size() == 0)
		{
			ReviewerToReviewee rtr = new ReviewerToReviewee();
			rtr.setReviewee(username);
			rtr.setReviewer(reviewer);
			rtr.save();
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#addReviewee(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addReviewee(String loggedInUser, String username,
			String reviewee) throws Exception {
		if (username.equals(reviewee))
			throw new Exception("Reviewer and reviewee " + username + " are the same");
		List<ReviewerToReviewee> rtrs = new ReviewerToReviewee().getObjects("reviewer = " + DatabaseUtils.toSQL(username) + " and reviewee=" + DatabaseUtils.toSQL(reviewee));
		if (rtrs.size() == 0)
		{
			ReviewerToReviewee rtr = new ReviewerToReviewee();
			rtr.setReviewee(reviewee);
			rtr.setReviewer(username);
			rtr.save();
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#removeReviewer(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void removeReviewer(String loggedInUser, String username,
			String reviewer) throws Exception {
		List<ReviewerToReviewee> rtrs = new ReviewerToReviewee().getObjects("reviewee = " + DatabaseUtils.toSQL(username) + " and reviewer=" + DatabaseUtils.toSQL(reviewer));
		for (ReviewerToReviewee rtr: rtrs)
		{
			rtr.delete();
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#removeReviewee(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void removeReviewee(String loggedInUser, String username,
			String reviewee) throws Exception {
		List<ReviewerToReviewee> rtrs = new ReviewerToReviewee().getObjects("reviewer = " + DatabaseUtils.toSQL(username) + " and reviewee=" + DatabaseUtils.toSQL(reviewee));
		for (ReviewerToReviewee rtr: rtrs)
		{
			rtr.delete();
		}
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#linkFileToProject(edu.stanford.epad.epadws.models.Project, edu.stanford.epad.epadws.models.EpadFile)
	 */
	@Override
	public void linkFileToProject(String loggedInUser, Project project, EpadFile file) throws Exception {
		User user = getUser(loggedInUser);
		if (user != null && !user.isAdmin() && !isOwner(loggedInUser, project.getProjectId()))
			throw new Exception("No permission to add template to project");
		ProjectToFile ptof = (ProjectToFile) new ProjectToFile().getObject("project_id =" + project.getId() + " and file_id =" + file.getId());
		if (ptof == null) {
			ptof = new ProjectToFile();
			ptof.setProjectId(project.getId());
			ptof.setFileId(file.getId());
			ptof.setCreator(loggedInUser);
			ptof.save();
		}		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#unlinkFileFromProject(edu.stanford.epad.epadws.models.Project, edu.stanford.epad.epadws.models.EpadFile)
	 */
	@Override
	public void unlinkFileFromProject(String loggedInUser, Project project, EpadFile file) throws Exception {
		User user = getUser(loggedInUser);
		if (user != null && !user.isAdmin() && !isOwner(loggedInUser, project.getProjectId()))
			throw new Exception("No permission to remove template from project");
		ProjectToFile ptof = (ProjectToFile) new ProjectToFile().getObject("project_id =" + project.getId() + " and file_id =" + file.getId());
		if (ptof != null) {
			ptof.delete();
		}		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getLinkedFiles(edu.stanford.epad.epadws.models.Project)
	 */
	@Override
	public List<EpadFile> getLinkedFiles(Project project) throws Exception {
		List objects = new EpadFile().getObjects("id in (select file_id from " 
				+ ProjectToFile.DBTABLE 
				+ " where project_id =" + project.getId() + ")");
		List<EpadFile> files = new ArrayList<EpadFile>();
		files.addAll(objects);
		return files;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#getDBObject(java.lang.Class, long)
	 */
	@Override
	public AbstractDAO getDBObject(Class dbClass, long id) throws Exception {
		AbstractDAO object = (AbstractDAO) dbClass.newInstance();
		return object.getObject("id = " + id);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.epad.epadws.service.EpadProjectOperations#sort(java.util.List, java.lang.String, boolean)
	 */
	@Override
	public List sort(List<AbstractDAO> objects, String field, boolean ascending) {
		if (objects.size() == 0) return new ArrayList();
		log.debug("Sort database objects:" + objects.get(0).returnDBTABLE());
		for (int i = 0; i < objects.size(); i++)
		{
			AbstractDAO obj1 = objects.get(i);
			for (int j = i+1; j < objects.size(); j++)
			{
				AbstractDAO obj2 = objects.get(j);
				Object value1 = obj1._fieldValue(field);
				Object value2 = obj2._fieldValue(field);
				if (!ascending)
				{
					Object value = value1;
					value1 = value2;
					value2 = value;
				}
				boolean swich = false;
				if (value2 == null && value1 != null) 
				{
					continue;
				}
				else if (value1 == null && value2 != null)
				{
					swich = true;
				}
				else
				{
					if (value1 instanceof String && ((String) value1).compareToIgnoreCase((String) value2) > 0)
					{
						swich = true;
					}
					else if (value1 instanceof Date && ((Date) value1).compareTo((Date) value2) > 0)
					{
						swich = true;
					}
					else if (value1 instanceof Integer && ((Integer) value1).compareTo((Integer) value2) > 0)
					{
						swich = true;
					}
					else if (value1 instanceof Long && ((Long) value1).compareTo((Long) value2) > 0)
					{
						swich = true;
					}
					else if (value1 instanceof Double && ((Double) value1).compareTo((Double) value2) > 0)
					{
						swich = true;
					}
					else if (value1 instanceof Boolean && ((Boolean) value1).compareTo((Boolean) value2) > 0)
					{
						swich = true;
					}
				}
				if (swich)
				{
					objects.set(i, obj2);
					objects.set(j, obj1);
					obj1 = obj2;
				}
			}
		}
		return objects;
	}
	
	private String getIdList(List<AbstractDAO> objects)
	{
		if (objects == null) return "";
		String list = "";
		for (AbstractDAO object: objects)
		{
			list = "," + object.getId();
		}
		if (list.length() > 0)
			return list.substring(1);
		else
			return list;
	}

}
