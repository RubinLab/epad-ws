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
package edu.stanford.epad.epadws.handlers.admin;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.MailUtil;
import edu.stanford.epad.dtos.internal.XNATExperiment;
import edu.stanford.epad.dtos.internal.XNATExperimentList;
import edu.stanford.epad.dtos.internal.XNATProject;
import edu.stanford.epad.dtos.internal.XNATProjectList;
import edu.stanford.epad.dtos.internal.XNATSubject;
import edu.stanford.epad.dtos.internal.XNATSubjectList;
import edu.stanford.epad.dtos.internal.XNATUser;
import edu.stanford.epad.dtos.internal.XNATUserList;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectType;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.UserRole;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.queries.XNATQueries;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.SessionService;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * @author martin
 */
public class XNATSyncHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String FORBIDDEN = "Forbidden method - only GET supported on XNAT sync route";
	private static final String INTERNAL_ERROR_MESSAGE = "Internal server error on XNAT sync route";
	private static final String INTERNAL_IO_ERROR_MESSAGE = "Internal server IO error on XNAT sync route";
	private static final String INTERNAL_SQL_ERROR_MESSAGE = "Internal server SQL error on XNAT sync route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid for XNAT sync route";

	private final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
	private final EpadOperations epadOperations = DefaultEpadOperations.getInstance();
	
	@Override
	public void handle(String str, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/plain;charset=UTF-8");
		request.setHandled(true);

		String response = "";
		try {
			responseStream = httpResponse.getWriter();
			responseStream.write(response);
			responseStream.flush();
			
			if (SessionService.hasValidSessionID(httpRequest)) {
				String sessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
				// We may be logged into EPAD Session, get a new session ID from XNAT
				if (!XNATSessionOperations.hasValidXNATSessionID(sessionID))
					sessionID = XNATSessionOperations.getXNATAdminSessionID();
				String username = httpRequest.getParameter("username");
				if (username == null) username = "admin";
				String method = httpRequest.getMethod();
				if ("GET".equalsIgnoreCase(method)) {
					try {
						response = syncXNATtoEpad(username, sessionID);
						log.info("Output Log:" + response);
						responseStream.write(response);
						statusCode = HttpServletResponse.SC_OK;
					} catch (Exception e) {
						log.warning("Error in XNAT Sync", e);
						statusCode = HandlerUtil.internalErrorResponse(INTERNAL_ERROR_MESSAGE, e, responseStream, log);
					} 
				} else {
					statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_FORBIDDEN, FORBIDDEN, responseStream, log);
				}
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
			responseStream.flush();
		} catch (Throwable t) {
			responseStream.write(response);
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}
	
	public static String syncXNATtoEpad(String username, String sessionID) throws Exception {
		
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		String response = "";
		log.info("Starting sync with XNAT Database");
		XNATUserList xusers = XNATQueries.getAllUsers();
		for (XNATUser xuser: xusers.ResultSet.Result)
		{
			log.info("Processing user:" + xuser.login);
			response = response + "\nProcessing user:" + xuser.login;
			String password = generatePassword(xuser.login);
			User user = projectOperations.getUser(xuser.login);
			if (user == null)
			{
				user = projectOperations.createUser(username, xuser.login, xuser.firstname, xuser.lastname, xuser.email, password, new ArrayList<String>(), new ArrayList<String>());
				// TODO: mail password to user
				log.info("Created user:" + xuser.login + " password:" + password);
				response = response + "\nCreated user: " + user.getUsername() + " password:" + password;
				if (user.getEmail() != null && user.getEmail().trim().length() > 0)
					new MailUtil().send(user.getEmail(), "noreply@" + EPADConfig.xnatServer, "Password Reset", "Hello " + user.getFullName() 
						+ "\nYour password on EPAD has been reset to your username.\nPlease login and change it.\nThank you.");
			}
			else if (user.getUsername().equals("admin"))
			{
				try {
					// Update password, but ignore errors
					user = projectOperations.updateUser(username, xuser.login, null, null, xuser.email, password, "admin", new ArrayList<String>(), new ArrayList<String>());
					log.info("Updated admin password");
				} catch (Exception x) {}
			}
			if (xuser.login.equals("admin"))
			{
				user.setAdmin(true);
				user.save();
			}

		}
		log.info("Getting all projects");
		if (sessionID == null || sessionID.length() == 0 || !XNATSessionOperations.hasValidXNATSessionID(sessionID))
			sessionID = XNATSessionOperations.getXNATAdminSessionID();
		XNATProjectList xnatProjectList = XNATQueries.allProjects(sessionID);
		for (XNATProject xproject: xnatProjectList.ResultSet.Result)
		{						
			log.info("Processing project:" + xproject.name);
			response = response + "\nProcessing project:" + xproject.name;
			Project project = projectOperations.getProject(xproject.ID);
			if (project == null)
			{
				String type = XNATQueries.getProjectType(xproject.ID, sessionID);
				log.info("Project " + xproject.name + " is " + type);
				ProjectType pt = ProjectType.PRIVATE;
				if (type.toLowerCase().startsWith("public"))
					pt = ProjectType.PUBLIC;
				try
				{
					project = projectOperations.createProject(username, xproject.ID, xproject.name, xproject.description, null, pt);
				} catch (Exception x) {
					log.warning("Error creating project:" +  xproject.ID, x);
					continue;
				}
				log.info("Created project:" + xproject.name);
				response = response + "\nCreated project " + xproject.name;
			}
			log.info("Getting project desc:" + xproject.ID);
			XNATUserList xnatUsers = XNATQueries.getUsersForProject(xproject.ID);
			Map<String,String> userRoles = xnatUsers.getRoles();
			log.info("Project users:" + userRoles);
			for (String euser: userRoles.keySet())
			{
				log.info("Getting projects for " + euser);
				String role = userRoles.get(euser);
				List<Project> projects = projectOperations.getProjectsForUser(euser);
				boolean found = false;
				for (Project p: projects)
				{
					if (p.getProjectId().equals(xproject.ID))
					{
						found = true;
						break;
					}
				}
				if (found && euser.equals("admin"))
				{
					List<User> epadUsers = projectOperations.getUsersForProject(xproject.ID);
					found = false;
					for (User u: epadUsers)
					{
						if (u.getUsername().equals("admin"))
							found = true;
					}
				}
				if (!found)
				{
					UserRole urole = UserRole.COLLABORATOR;
					if (role.startsWith("Owner"))
						urole = UserRole.OWNER;
					if (role.startsWith("Member"))
						urole = UserRole.MEMBER;
					projectOperations.addUserToProject(username, xproject.ID, euser, urole);
					if (role.startsWith("Owner") && project.getCreator().equals(username))
					{
						project.setCreator(euser);
						project.save();
					}
					response = response + "\nAdded user " + euser + " to project " + xproject.ID + " as " + urole;
				}
			}
			XNATSubjectList xsubjects = XNATQueries.getSubjectsForProject(sessionID, xproject.ID);
			List<Subject> psubjects = projectOperations.getSubjectsForProject(xproject.ID);
			for (XNATSubject xsubject: xsubjects.ResultSet.Result)
			{
				log.info("Processing subject:" + xsubject.label);
				Subject subject = projectOperations.getSubject(xsubject.label);
				if (subject == null)
				{
					String subname = trimTrailing(xsubject.src);
					subject = projectOperations.createSubject(username, xsubject.label, xsubject.src, null, null);
					response = response + "\nCreated subject " + xsubject.src;
				}
				XNATExperimentList experiments = XNATQueries.getDICOMExperiments(sessionID, xproject.ID, xsubject.ID);
				for (XNATExperiment experiment: experiments.ResultSet.Result)
				{
					log.info("Processing study:" + experiment.label.replace('_', '.'));
					Study study = projectOperations.getStudy(experiment.label.replace('_', '.'));
					if (study == null)
					{
						study = projectOperations.createStudy(username, experiment.label.replace('_', '.'), subject.getSubjectUID(),"");
						response = response + "\nCreated study " + study.getStudyUID() + " for subject " + subject.getName();
					}
				}
				if (!psubjects.contains(subject))
				{
					projectOperations.addSubjectToProject(username, subject.getSubjectUID(), project.getProjectId());
					response = response + "\nAdded Subject " + subject.getName() + ":" + subject.getSubjectUID() + " to project " + project.getProjectId();
				}
				log.info("Getting XNAT studies for project:" + project.getProjectId());
				Set<String> studyUIDs = XNATQueries.getStudyUIDsForSubject(sessionID, project.getProjectId(), xsubject.ID);
				log.info("Getting EPAD studies for project:" + project.getProjectId());
				List<Study> studies = projectOperations.getStudiesForProjectAndSubject(project.getProjectId(), subject.getSubjectUID());
				for (String studyUID: studyUIDs)
				{
					boolean found = false;
					for (Study s: studies)
					{
						if (s.getStudyUID().equals(studyUID.replace('_', '.')))
						{
							found = true;
							break;
						}
					}
					if (!found)
					{
						projectOperations.addStudyToProject(username, studyUID.replace('_', '.'), subject.getSubjectUID(), project.getProjectId());
						response = response + "\nAdded Study " + studyUID.replace('_', '.') + " to project " + project.getProjectId();
					}
				}
				log.info("Done with subject:" + xsubject.label);
			}
			log.info("Done with project:" + project.getProjectId());
		}
		log.info("Done with all projects");
		response = response + "\nSyncing with XNAT Completed";
		return response;
	}

	private static String generatePassword(String login)
	{
		if (login.equals(EPADConfig.xnatUploadProjectUser))
			return EPADConfig.xnatUploadProjectPassword;
		if (login.equals("guest"))
			return "guest";
		return login;  // Keep password same as login
//		return "" + new IdGenerator().generateId(2) + login + new IdGenerator().generateId(2);
	}
	
	private static String trimTrailing(String xnatName)
	{
		while (xnatName.endsWith("^"))
			xnatName = xnatName.substring(0, xnatName.length()-1);
		String name = xnatName.trim();
		return name;
	}
}
