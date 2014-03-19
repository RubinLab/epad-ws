package edu.stanford.isis.epadws.handlers.search;

import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.XNATProject;
import edu.stanford.epad.dtos.XNATProjectList;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.handlers.HandlerUtil;
import edu.stanford.isis.epadws.queries.AIMQueries;
import edu.stanford.isis.epadws.queries.XNATQueries;
import edu.stanford.isis.epadws.xnat.XNATSessionOperations;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * @author martin
 */
public class EPADSearchHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String PROJECTS_TEMPLATE = "/projects/";
	// private static final String PROJECT_TEMPLATE = PROJECTS_TEMPLATE + "{project}";
	// private static final String SUBJECTS_TEMPLATE = PROJECT_TEMPLATE + "/subjects/";
	// private static final String SUBJECT_TEMPLATE = SUBJECTS_TEMPLATE + "{subject}";
	// private static final String STUDIES_TEMPLATE = SUBJECT_TEMPLATE + "/studies/";
	// private static final String STUDY_TEMPLATE = STUDIES_TEMPLATE + "/studies/{study}";
	// private static final String SERIES_TEMPLATE = STUDY_TEMPLATE + "/series/";
	// private static final String SERIES_ID_TEMPLATE = SERIES_TEMPLATE + "/series/{series}";

	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error running query on search route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on search route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("application/json");
		request.setHandled(true);

		log.info("ePADSearch path=" + httpRequest.getPathInfo() + ", query=" + httpRequest.getQueryString());

		try {
			responseStream = httpResponse.getWriter();

			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String jsessionID = XNATUtil.getJSessionIDFromRequest(httpRequest);
				String username = httpRequest.getParameter("username");
				log.info("username " + username);
				String pathInfo = httpRequest.getPathInfo();
				if (HandlerUtil.matchesTemplate(PROJECTS_TEMPLATE, pathInfo)) {
					EPADProjectList projectList = performAllProjectsQuery(jsessionID, username);
					responseStream.append(projectList.toJSON());
				} else {
					// TODO
					log.warning("Not implemented");
					throw new RuntimeException("Not implemented");
				}
				responseStream.flush();
				statusCode = HttpServletResponse.SC_OK;
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_EXCEPTION_MESSAGE, t, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	// TODO Think about getting a list of users for each project from XNAT and using that user list to query
	// for the AIM annotations
	private EPADProjectList performAllProjectsQuery(String sessionID, String username)
	{
		EPADProjectList epadProjectList = new EPADProjectList();
		XNATProjectList xnatProjectList = XNATQueries.allProjects(sessionID);

		for (XNATProject xnatProject : xnatProjectList.ResultSet.Result) {
			String secondaryID = xnatProject.secondary_ID;
			String piLastName = xnatProject.pi_lastname;
			String description = xnatProject.description;
			String name = xnatProject.name;
			String id = xnatProject.ID;
			String piFirstName = xnatProject.pi_firstname;
			String uri = xnatProject.URI;
			int numberOfSubjects = XNATQueries.numberOfSubjectsForProject(sessionID, xnatProject.ID);
			int numberOfStudies = numberOfStudiesForProject(sessionID, xnatProject.ID);
			int numberOfAnnotations = isEmptyUsername(username) ? 0 : numberOfAIMAnnotationsForProject(sessionID,
					xnatProject.ID, username);
			EPADProject epadProject = new EPADProject(secondaryID, piLastName, description, name, id, piFirstName, uri,
					numberOfSubjects, numberOfStudies, numberOfAnnotations);

			epadProjectList.addEPADProject(epadProject);
		}
		return epadProjectList;
	}

	private boolean isEmptyUsername(String username)
	{
		return username == null || username.length() == 0;
	}

	private int numberOfAIMAnnotationsForProject(String sessionID, String projectID, String username)
	{
		Set<String> subjectIDs = XNATQueries.subjectIDsForProject(sessionID, projectID);
		int totalAIMAnnotations = 0;

		for (String subjectID : subjectIDs) {
			totalAIMAnnotations += AIMQueries.getNumberOfAIMImageAnnotationsForPatientId(subjectID, username);
		}

		return totalAIMAnnotations;
	}

	private int numberOfStudiesForProject(String sessionID, String projectID)
	{
		return XNATQueries.numberOfDICOMExperimentsForProject(sessionID, projectID);
	}
}
