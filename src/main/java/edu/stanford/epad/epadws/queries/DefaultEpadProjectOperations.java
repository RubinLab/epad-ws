package edu.stanford.epad.epadws.queries;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectToSubject;
import edu.stanford.epad.epadws.models.ProjectToSubjectToStudy;
import edu.stanford.epad.epadws.models.ProjectToSubjectToUser;
import edu.stanford.epad.epadws.models.ProjectToUser;
import edu.stanford.epad.epadws.models.ProjectType;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.UserRole;

public class DefaultEpadProjectOperations implements EpadProjectOperations {
	
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final DefaultEpadProjectOperations ourInstance = new DefaultEpadProjectOperations();

	private DefaultEpadProjectOperations()
	{
	}

	public static DefaultEpadProjectOperations getInstance()
	{
		return ourInstance;
	}

	@Override
	public Project createProject(String loggedInUser, String projectId, String projectName,
			String description, ProjectType type) throws Exception {
		Project project = new Project();
		project.setProjectId(projectId);
		project.setName(projectName);
		project.setDescription(description);
		project.setType(type.getName());
		project.setCreator(loggedInUser);
		project.save();
		return project;
	}

	@Override
	public User createUser(String loggedInUser, String username, String firstName, String lastName,
			String password) throws Exception {
		User user = new User();
		user.setUsername(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		String hashedPW = BCrypt.hashpw(password, BCrypt.gensalt());
		user.setPassword(hashedPW);
		user.setCreator(loggedInUser);
		user.save();
		return user;
	}

	@Override
	public User updateUser(String loggedInUser, String username,
			String firstName, String lastName, String password)
			throws Exception {
		User user = new User();
		user = (User) user.getObject("username = " + user.toSQL(username));
		if (firstName != null) user.setFirstName(firstName);
		if (lastName != null) user.setLastName(lastName);
		if (password != null) {
			String hashedPW = BCrypt.hashpw(password, BCrypt.gensalt());
			user.setPassword(hashedPW);
		}
		user.save();
		return null;
	}

	@Override
	public void addUserToProject(String loggedInUser, String projectId,
			String username, UserRole role) throws Exception {
		User user = new User();
		user = (User) user.getObject("username = " + user.toSQL(username));
		Project project = new Project();
		project = (Project) project.getObject("projectId = " + project.toSQL(projectId));
		ProjectToUser ptou = new ProjectToUser();
		ptou.setProjectId(project.getId());
		ptou.setUserId(user.getId());
		ptou.setRole(role.getName());
		ptou.setCreator(loggedInUser);
		ptou.save();
	}

	@Override
	public void setUserRoleForProject(String loggedInUser, String projectId,
			String username, UserRole role) throws Exception {
		User user = new User();
		user = (User) user.getObject("username = " + user.toSQL(username));
		Project project = new Project();
		project = (Project) project.getObject("projectId = " + project.toSQL(projectId));
		ProjectToUser ptou = new ProjectToUser();
		ptou = (ProjectToUser) ptou.getObject("project_id = " + project.getId() + " and user_id =" + user.getId());
		ptou.setRole(role.getName());
	}

	@Override
	public Subject createSubject(String loggedInUser, String subjectUID,
			String name, Date dob, String gender) throws Exception {
		Subject subject = new Subject();
		subject.setSubjectUID(subjectUID);
		subject.setName(name);
		subject.setDob(dob);
		subject.setGender(gender);
		subject.setCreator(loggedInUser);
		subject.save();
		return subject;
	}

	@Override
	public Study createStudy(String loggedInUser, String studyUID,
			String subjectUID) throws Exception {
		Subject subject = new Subject();
		subject = (Subject) subject.getObject("subjectuid = " + subject.toSQL(subjectUID));
		Study study = new Study();
		study.setStudyUID(studyUID);
		study.setSubjectId(subject.getId());
		study.setCreator(loggedInUser);
		study.save();
		return study;
	}

	@Override
	public void addSubjectToProject(String loggedInUser, String subjectUID,
			String projectId) throws Exception {
		Subject subject = new Subject();
		subject = (Subject) subject.getObject("subjectuid = " + subject.toSQL(subjectUID));
		Project project = new Project();
		project = (Project) project.getObject("projectId = " + project.toSQL(projectId));
		ProjectToSubject ptos = new ProjectToSubject();
		ptos.setProjectId(project.getId());
		ptos.setSubjectId(subject.getId());
		ptos.setCreator(loggedInUser);
		ptos.save();
	}

	@Override
	public void addStudyToProject(String loggedInUser, String studyUID,
			String subjectUID, String projectId) throws Exception {
		Study study = getStudy(studyUID);
		Subject subject = new Subject();
		if ((subjectUID == null || subjectUID.length() == 0) && study != null)
		{
			subject.setId(study.getSubjectId());
			subject.retrieve();
		}
		else	
		{
			subject = (Subject) subject.getObject("subjectuid = " + subject.toSQL(subjectUID));
		}
		Project project = new Project();
		project = (Project) project.getObject("projectId = " + project.toSQL(projectId));
		ProjectToSubject ptos = new ProjectToSubject();
		ptos = (ProjectToSubject) ptos.getObject("project_id = " + project.getId() + " and subject_id =" + subject.getId());
		if (study == null)
		{
			study = new Study();
			study.setStudyUID(studyUID);
			study.setSubjectId(subject.getId());
			study.setCreator(loggedInUser);
			study.save();
		}
		ProjectToSubjectToStudy pss = new ProjectToSubjectToStudy();
		pss.setProjSubjId(ptos.getId());
		pss.setStudyId(study.getId());
		pss.setCreator(loggedInUser);
		pss.save();
	}

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
	}

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
	}

	@Override
	public Project getProject(String projectId) throws Exception {
		Project project = new Project();
		project = (Project) project.getObject("projectId = " + project.toSQL(projectId));
		return project;
	}

	@Override
	public User getUser(String username) throws Exception {
		User user = new User();
		user = (User) user.getObject("username = " + user.toSQL(username));
		return user;
	}

	@Override
	public Subject getSubject(String subjectUID) throws Exception {
		Subject subject = new Subject();
		subject = (Subject) subject.getObject("subjectuid = " + subject.toSQL(subjectUID));
		return subject;
	}

	@Override
	public Study getStudy(String studyUID) throws Exception {
		Study study = new Study();
		study = (Study) study.getObject("studyUID = " + study.toSQL(studyUID));
		return study;
	}

	@Override
	public List<Project> getAllProjects() throws Exception {
		List objects = new Project().getObjects("1 = 1 order by projectId");
		List<Project> projects = new ArrayList<Project>();
		projects.addAll(objects);
		return projects;
	}

	@Override
	public List<Project> getProjectsForUser(String username) throws Exception {
		User user = getUser(username);
		List objects = new Project().getObjects("id in (select project_id from " 
													+ ProjectToUser.DBTABLE 
													+ " where user_id =" + user.getId() + ") order by projectId");
		List<Project> projects = new ArrayList<Project>();
		projects.addAll(objects);		
		return projects;
	}

	@Override
	public List<Subject> getSubjectsForProject(String projectId)
			throws Exception {
		Project project = getProject(projectId);
		
		return getSubjectsByProjectId(project.getId());
	}


	private List<Subject> getSubjectsByProjectId(long id)
			throws Exception {
		List objects = new Subject().getObjects("id in (select subject_id from " 
													+ ProjectToSubject.DBTABLE 
													+ " where project_id =" + id + ")");
		List<Subject> subjects = new ArrayList<Subject>();
		subjects.addAll(objects);
		
		return subjects;
	}

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
	public List<Study> getStudiesForProjectAndSubject(String projectId,
			String subjectUID) throws Exception {
		Project project = getProject(projectId);
		Subject subject = getSubject(subjectUID);
		ProjectToSubject ptos = (ProjectToSubject) new ProjectToSubject().getObject("project_id = " + project.getId() + " and subject_id=" + subject.getId());
		List<Study> studies = new ArrayList<Study>();
		List objects = new Study().getObjects("id in (select study_id from " 
				+ ProjectToSubjectToStudy.DBTABLE 
				+ " where proj_subj_id =" + ptos.getId() + ")");
		studies.addAll(objects);

		return studies;
	}

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

	@Override
	public List<Study> getStudiesForSubject(String subjectUID) throws Exception {
		Subject subject = getSubject(subjectUID);
		List objects = new Study().getObjects("subject_id  =" + subject.getId());
		List<Study> studies = new ArrayList<Study>();
		studies.addAll(objects);		
		return studies;
	}

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

	@Override
	public Map<String, String> getUserStatusForProjectSubjects(
			String loggedInUser, String projectID) throws Exception {
		Project project = getProject(projectID);
		User user = getUser(loggedInUser);
		List<Subject> subjects = getSubjectsByProjectId(project.getId());
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

	@Override
	public boolean isCollaborator(String sessionID, String username,
			String projectID) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMember(String sessionID, String username, String projectID)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOwner(String sessionID, String username, String projectID)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserRole getUserProjectRole(String sessionID, String username,
			String projectID) throws Exception {
		User user = getUser(username);
		Project project = getProject(projectID);
		ProjectToUser pu = new ProjectToUser("project_id =" + project.getId() + " and user_id=" + user.getId());
		
		return null;
	}

}
