package edu.stanford.epad.epadws.handlers.admin;

import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.internal.XNATExperiment;
import edu.stanford.epad.dtos.internal.XNATExperimentList;
import edu.stanford.epad.dtos.internal.XNATProject;
import edu.stanford.epad.dtos.internal.XNATProjectList;
import edu.stanford.epad.dtos.internal.XNATSubject;
import edu.stanford.epad.dtos.internal.XNATSubjectList;
import edu.stanford.epad.dtos.internal.XNATUser;
import edu.stanford.epad.dtos.internal.XNATUserList;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectType;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.UserRole;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.queries.EpadProjectOperations;
import edu.stanford.epad.epadws.queries.XNATQueries;
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

	EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
	EpadOperations epadOperations = DefaultEpadOperations.getInstance();
	
	@Override
	public void handle(String str, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/plain;charset=UTF-8");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String sessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
				String username = httpRequest.getParameter("username");
				String method = httpRequest.getMethod();
				if ("GET".equalsIgnoreCase(method)) {
					try {
						String response = "";
						XNATUserList xusers = XNATQueries.getAllUsers();
						for (XNATUser xuser: xusers.ResultSet.Result)
						{
							if (projectOperations.getUser(xuser.login) == null)
							{
								String passw = generatePassword(xuser.login);
								// TODO: mail password to user
								User user = projectOperations.createUser(username, xuser.login, xuser.firstname, xuser.lastname, passw);
								if (xuser.login.equals("admin"))
								{
									user.setAdmin(true);
									user.save();
								}
								response = response + "\nCreated user " + user.getUsername();
							}		
						}
						XNATProjectList xnatProjectList = XNATQueries.allProjects(sessionID);
						for (XNATProject xproject: xnatProjectList.ResultSet.Result)
						{						
							Project project = projectOperations.getProject(xproject.ID);
							if (project == null)
							{
								String type = XNATQueries.getProjectType(xproject.ID, sessionID);
								log.info("Project " + xproject.name + " is " + type);
								ProjectType pt = ProjectType.PRIVATE;
								if (type.toLowerCase().startsWith("public"))
									pt = ProjectType.PUBLIC;
								project = projectOperations.createProject(username, xproject.ID, xproject.name, xproject.description, pt);
								response = response + "\nCreated project " + xproject.name;
							}
							EPADProject eproject = epadOperations.getProjectDescription(new ProjectReference(xproject.ID), username, sessionID);
							for (String login: eproject.loginToRole.keySet())
							{
								log.info("Getting projects for " + login);
								String role = eproject.loginToRole.get(login);
								List<Project> projects = projectOperations.getProjectsForUser(login);
								boolean found = false;
								for (Project p: projects)
								{
									if (p.getProjectId().equals(xproject.ID))
									{
										found = true;
										break;
									}
								}
								if (!found)
								{
									UserRole urole = UserRole.COLLABORATOR;
									if (role.startsWith("Owner"))
										urole = UserRole.OWNER;
									if (role.startsWith("Member"))
										urole = UserRole.MEMBER;
									projectOperations.addUserToProject(username, eproject.id, login, urole);
									if (role.startsWith("Owner") && project.getCreator().equals(username))
									{
										project.setCreator(login);
										project.save();
									}
									response = response + "\nAdded user " + login + " to project " + eproject.id + " as " + urole;
								}
							}
							XNATSubjectList xsubjects = XNATQueries.getSubjectsForProject(sessionID, xproject.ID);
							List<Subject> psubjects = projectOperations.getSubjectsForProject(xproject.ID);
							for (XNATSubject xsubject: xsubjects.ResultSet.Result)
							{
								Subject subject = projectOperations.getSubject(xsubject.label);
								if (subject == null)
								{
									subject = projectOperations.createSubject(username, xsubject.label, xsubject.src, null, null);
									response = response + "\nCreated subject " + xsubject.src;
								}
								XNATExperimentList experiments = XNATQueries.getDICOMExperiments(sessionID, xproject.ID, xsubject.ID);
								for (XNATExperiment experiment: experiments.ResultSet.Result)
								{
									Study study = projectOperations.getStudy(experiment.label.replace('_', '.'));
									if (study == null)
									{
										study = projectOperations.createStudy(username, experiment.label.replace('_', '.'), subject.getSubjectUID());
										response = response + "\nCreated study " + study.getStudyUID() + " for subject " + subject.getName();
									}
								}
								if (!psubjects.contains(subject))
								{
									projectOperations.addSubjectToProject(username, subject.getSubjectUID(), project.getProjectId());
									response = response + "\nAdded Subject " + subject.getName() + ":" + subject.getSubjectUID() + " to project " + project.getProjectId();
								}
								Set<String> studyUIDs = XNATQueries.getStudyUIDsForSubject(sessionID, project.getProjectId(), xsubject.ID);
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
							}
						}
						log.info(response);
						responseStream.write(response);
						statusCode = HttpServletResponse.SC_OK;
					} catch (Exception e) {
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
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	private String generatePassword(String login)
	{
		return login;
	}
}
