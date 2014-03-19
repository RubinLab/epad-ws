package edu.stanford.isis.epadws.handlers.search;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.XNATProject;
import edu.stanford.epad.dtos.XNATProjectList;
import edu.stanford.epad.dtos.XNATSubjectList;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.handlers.HandlerUtil;
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

		try {
			responseStream = httpResponse.getWriter();

			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String jsessionID = XNATUtil.getJSessionIDFromRequest(httpRequest);
				String pathInfo = httpRequest.getPathInfo();
				if (HandlerUtil.matchesTemplate(PROJECTS_TEMPLATE, pathInfo)) {
					EPADProjectList projectList = performAllProjectsQuery(jsessionID);
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

	private EPADProjectList performAllProjectsQuery(String sessionID)
	{
		EPADProjectList epadProjectList = new EPADProjectList();
		XNATProjectList xnatProjectList = XNATQueries.allProjects(sessionID);

		for (XNATProject xnatProject : xnatProjectList.ResultSet.Result) {
			String secondaryID = xnatProject.secondaryID;
			String piLastName = xnatProject.piLastName;
			String description = xnatProject.description;
			String name = xnatProject.name;
			String id = xnatProject.id;
			String piFirstName = xnatProject.piFirstName;
			String uri = xnatProject.uri;
			int numberOfSubjects = numberOfSubjectsForProject(sessionID, xnatProject.id);
			int numberOfAnnotations = 0;
			EPADProject epadProject = new EPADProject(secondaryID, piLastName, description, name, id, piFirstName, uri,
					numberOfSubjects, numberOfAnnotations);

			epadProjectList.addEPADProject(epadProject);
		}
		return epadProjectList;
	}

	private int numberOfSubjectsForProject(String sessionID, String projectID)
	{
		XNATSubjectList xnatSubjectList = XNATQueries.allSubjectsForProject(sessionID, projectID);

		return xnatSubjectList.ResultSet.totalRecords;
	}
}
