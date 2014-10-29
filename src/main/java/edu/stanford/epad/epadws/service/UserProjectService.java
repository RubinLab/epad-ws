package edu.stanford.epad.epadws.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.queries.XNATQueries;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

public class UserProjectService {

	private static final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();	
	
	public static boolean isCollaborator(String sessionID, String username, String projectID) throws Exception {
		if (EPADConfig.UseEPADUsersProjects) {
			return projectOperations.isCollaborator(username, projectID);
		} else {
			return XNATQueries.isCollaborator(sessionID, username, projectID);
		}				
	}
	
	public static boolean isOwner(String sessionID, String username, String projectID) throws Exception {
		if (EPADConfig.UseEPADUsersProjects) {
			return projectOperations.isOwner(username, projectID);
		} else {
			return XNATQueries.isOwner(sessionID, username, projectID);
		}				
	}
	
	public static boolean isMember(String sessionID, String username, String projectID) throws Exception {
		if (EPADConfig.UseEPADUsersProjects) {
			return projectOperations.isMember(username, projectID);
		} else {
			return XNATQueries.isMember(sessionID, username, projectID);
		}				
	}
	
	public static Set<String> getAllProjectIDs() throws Exception {
		if (EPADConfig.UseEPADUsersProjects) {
			List<Project> projects = projectOperations.getAllProjects();
			Set<String> projectIDs = new HashSet<String>();
			for (Project project: projects)
				projectIDs.add(project.getProjectId());
			return projectIDs;
		} else {
			String adminSessionID = XNATSessionOperations.getXNATAdminSessionID();
			return XNATQueries.allProjectIDs(adminSessionID);
		}				
	}
	
	public static Set<String> getAllStudyUIDsForProject(String projectID) throws Exception {
		if (EPADConfig.UseEPADUsersProjects) {
			List<Study> studies = projectOperations.getAllStudiesForProject(projectID);
			Set<String> studyIDs = new HashSet<String>();
			for (Study study: studies)
				studyIDs.add(study.getStudyUID());
			return studyIDs;
		} else {
			String adminSessionID = XNATSessionOperations.getXNATAdminSessionID();
			return XNATQueries.getAllStudyUIDsForProject(projectID, adminSessionID);
		}				
	}
	
	public static Set<String> getStudyUIDsForSubject(String sessionID, String projectID, String patientID) throws Exception {
		if (EPADConfig.UseEPADUsersProjects) {
			List<Study> studies = projectOperations.getStudiesForProjectAndSubject(projectID, patientID);
			Set<String> studyIDs = new HashSet<String>();
			for (Study study: studies)
				studyIDs.add(study.getStudyUID());
			return studyIDs;
		} else {
			return XNATQueries.getStudyUIDsForSubject(sessionID, projectID, patientID);
		}				
	}
	
	public static String getFirstProjectForStudy(String studyUID) throws Exception {
		if (EPADConfig.UseEPADUsersProjects) {
			Project project = projectOperations.getFirstProjectForStudy(studyUID);
			return project.getProjectId();
		} else {
			String adminSessionID = XNATSessionOperations.getXNATAdminSessionID();
			return XNATQueries.getFirstProjectForStudy(adminSessionID, studyUID);	
		}				
		
	}
}
