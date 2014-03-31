package edu.stanford.epad.epadws.handlers.search;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.queries.DefaultEpadQueries;
import edu.stanford.epad.epadws.queries.EpadQueries;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * @author martin
 */
public class EPADSearchHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String PROJECTS_TEMPLATE = "/projects/";
	private static final String PROJECT_TEMPLATE = PROJECTS_TEMPLATE + "{project}";
	private static final String SUBJECTS_TEMPLATE = PROJECT_TEMPLATE + "/subjects/";
	private static final String SUBJECT_TEMPLATE = SUBJECTS_TEMPLATE + "{subject}";
	private static final String STUDIES_TEMPLATE = SUBJECT_TEMPLATE + "/studies/";
	private static final String STUDY_TEMPLATE = STUDIES_TEMPLATE + "/studies/{study}";
	private static final String SERIES_TEMPLATE = STUDY_TEMPLATE + "/series/";
	// private static final String SERIES_ID_TEMPLATE = SERIES_TEMPLATE + "/series/{series}";

	private static final String BAD_REQUEST_MESSAGE = "Bad request on search route";
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
				EpadQueries epadQueries = DefaultEpadQueries.getInstance();
				String jsessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
				String username = httpRequest.getParameter("username");
				String pathInfo = httpRequest.getPathInfo();

				if (HandlerUtil.matchesTemplate(PROJECTS_TEMPLATE, pathInfo)) {
					EPADProjectList projectList = epadQueries.getAllProjectsForUser(jsessionID, username);
					responseStream.append(projectList.toJSON());
				} else if (HandlerUtil.matchesTemplate(SUBJECTS_TEMPLATE, pathInfo)) {
					Map<String, String> templateMap = HandlerUtil.getTemplateMap(SUBJECTS_TEMPLATE, pathInfo);
					String projectID = HandlerUtil.getParameter(templateMap, "project");
					EPADSubjectList subjectList = epadQueries.getAllSubjectsForProject(jsessionID, projectID);
					responseStream.append(subjectList.toJSON());
				} else if (HandlerUtil.matchesTemplate(STUDIES_TEMPLATE, pathInfo)) {
					Map<String, String> templateMap = HandlerUtil.getTemplateMap(STUDIES_TEMPLATE, pathInfo);
					String projectID = HandlerUtil.getParameter(templateMap, "project");
					String subjectID = HandlerUtil.getParameter(templateMap, "subject");
					EPADStudyList studyList = epadQueries.getAllStudiesForSubject(jsessionID, projectID, subjectID);
					responseStream.append(studyList.toJSON());
				} else if (HandlerUtil.matchesTemplate(SERIES_TEMPLATE, pathInfo)) {
					Map<String, String> templateMap = HandlerUtil.getTemplateMap(SERIES_TEMPLATE, pathInfo);
					String projectID = HandlerUtil.getParameter(templateMap, "project");
					String subjectID = HandlerUtil.getParameter(templateMap, "subject");
					String studyUID = HandlerUtil.getParameter(templateMap, "study");
					EPADSeriesList seriesList = epadQueries.getAllSeriesForStudy(jsessionID, projectID, subjectID, studyUID);
					responseStream.append(seriesList.toJSON());
				} else {
					statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, BAD_REQUEST_MESSAGE, log);
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
}
