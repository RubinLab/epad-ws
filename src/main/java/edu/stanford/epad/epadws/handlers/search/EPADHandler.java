package edu.stanford.epad.epadws.handlers.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * @author martin
 */
public class EPADHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String PROJECT_LIST_TEMPLATE = "/projects/";
	private static final String PROJECT_TEMPLATE = PROJECT_LIST_TEMPLATE + "{project}";
	private static final String SUBJECT_LIST_TEMPLATE = PROJECT_TEMPLATE + "/subjects/";
	private static final String SUBJECT_TEMPLATE = SUBJECT_LIST_TEMPLATE + "{subject}";
	private static final String STUDY_LIST_TEMPLATE = SUBJECT_TEMPLATE + "/studies/";
	private static final String STUDY_TEMPLATE = STUDY_LIST_TEMPLATE + "{study}";
	private static final String SERIES_LIST_TEMPLATE = STUDY_TEMPLATE + "/series/";
	private static final String SERIES_TEMPLATE = SERIES_LIST_TEMPLATE + "{series}";
	private static final String IMAGE_LIST_TEMPLATE = SERIES_TEMPLATE + "/images/";
	private static final String IMAGE_TEMPLATE = IMAGE_LIST_TEMPLATE + "{image}";

	private static final String INTERNAL_ERROR_MESSAGE = "Internal error running query on projects route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on projects route";
	private static final String BAD_GET_MESSAGE = "Invalid GET request.";
	private static final String BAD_DELETE_MESSAGE = "Invalid DELETE request!";
	private static final String FORBIDDEN_MESSAGE = "Forbidden method - only GET and DELETE supported on projects route!";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("application/json");
		request.setHandled(true);

		log.info("Project path=" + httpRequest.getPathInfo() + ", query=" + httpRequest.getQueryString());

		try {
			responseStream = httpResponse.getWriter();

			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String method = httpRequest.getMethod();

				if ("GET".equalsIgnoreCase(method)) {
					statusCode = handleQuery(httpRequest, responseStream);
				} else if ("DELETE".equalsIgnoreCase(method)) {
					statusCode = handleDelete(httpRequest, responseStream);
				} else {
					statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, FORBIDDEN_MESSAGE,
							responseStream, log);
				}
				responseStream.flush();
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
		} catch (IOException e) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, e, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	private int handleQuery(HttpServletRequest httpRequest, PrintWriter responseStream)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		String jsessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
		String username = httpRequest.getParameter("username");
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;

		try {
			EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(httpRequest);

			if (HandlerUtil.matchesTemplate(PROJECT_LIST_TEMPLATE, pathInfo)) {
				EPADProjectList projectList = epadOperations.getAllProjectsForUser(username, jsessionID, searchFilter);
				responseStream.append(projectList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(SUBJECT_LIST_TEMPLATE, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(SUBJECT_LIST_TEMPLATE, pathInfo);
				String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
				if (parametersAreValid(projectID)) {
					EPADSubjectList subjectList = epadOperations.getAllSubjectsForProject(projectID, jsessionID, searchFilter);
					responseStream.append(subjectList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, BAD_GET_MESSAGE,
							responseStream, log);

			} else if (HandlerUtil.matchesTemplate(STUDY_LIST_TEMPLATE, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(STUDY_LIST_TEMPLATE, pathInfo);
				String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");
				if (parametersAreValid(projectID, subjectID)) {
					EPADStudyList studyList = epadOperations.getAllStudiesForPatient(projectID, subjectID, jsessionID,
							searchFilter);
					responseStream.append(studyList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, BAD_GET_MESSAGE,
							responseStream, log);

			} else if (HandlerUtil.matchesTemplate(SERIES_LIST_TEMPLATE, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(SERIES_LIST_TEMPLATE, pathInfo);
				String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");
				String studyUID = HandlerUtil.getTemplateParameter(templateMap, "study");
				if (parametersAreValid(projectID, subjectID, studyUID)) {
					EPADSeriesList seriesList = epadOperations.getAllSeriesForStudy(projectID, subjectID, studyUID, jsessionID,
							searchFilter);
					responseStream.append(seriesList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, BAD_GET_MESSAGE,
							responseStream, log);

			} else if (HandlerUtil.matchesTemplate(IMAGE_LIST_TEMPLATE, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(IMAGE_LIST_TEMPLATE, pathInfo);
				String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");
				String studyUID = HandlerUtil.getTemplateParameter(templateMap, "study");
				String seriesUID = HandlerUtil.getTemplateParameter(templateMap, "series");
				if (parametersAreValid(projectID, subjectID, studyUID, seriesUID)) {
					EPADImageList imageList = epadOperations.getAllImagesForSeries(projectID, subjectID, studyUID, seriesUID,
							jsessionID, searchFilter);
					responseStream.append(imageList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, BAD_GET_MESSAGE,
							responseStream, log);
			} else if (HandlerUtil.matchesTemplate(IMAGE_TEMPLATE, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(IMAGE_LIST_TEMPLATE, pathInfo);
				String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");
				String studyUID = HandlerUtil.getTemplateParameter(templateMap, "study");
				String seriesUID = HandlerUtil.getTemplateParameter(templateMap, "series");
				String imageID = HandlerUtil.getTemplateParameter(templateMap, "image");
				if (parametersAreValid(projectID, subjectID, studyUID, seriesUID, imageID)) {
					EPADImage image = epadOperations.getImage(projectID, subjectID, studyUID, seriesUID, imageID, jsessionID,
							searchFilter);
					responseStream.append(image.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, BAD_GET_MESSAGE,
							responseStream, log);
			} else
				statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, BAD_GET_MESSAGE,
						responseStream, log);
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
		}
		return statusCode;
	}

	private int handleDelete(HttpServletRequest httpRequest, PrintWriter responseStream)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		String sessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;

		if (HandlerUtil.matchesTemplate(PROJECT_TEMPLATE, pathInfo)) {
			Map<String, String> templateMap = HandlerUtil.getTemplateMap(PROJECT_TEMPLATE, pathInfo);
			String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");

			epadOperations.scheduleProjectDelete(sessionID, projectID);

			statusCode = HttpServletResponse.SC_ACCEPTED;
		} else if (HandlerUtil.matchesTemplate(SUBJECT_TEMPLATE, pathInfo)) {
			Map<String, String> templateMap = HandlerUtil.getTemplateMap(SUBJECT_TEMPLATE, pathInfo);
			String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
			String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");

			epadOperations.schedulePatientDelete(sessionID, projectID, subjectID);

			statusCode = HttpServletResponse.SC_ACCEPTED;

		} else if (HandlerUtil.matchesTemplate(STUDY_TEMPLATE, pathInfo)) {
			Map<String, String> templateMap = HandlerUtil.getTemplateMap(STUDY_TEMPLATE, pathInfo);
			String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
			String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");
			String studyUID = HandlerUtil.getTemplateParameter(templateMap, "study");

			epadOperations.scheduleStudyDelete(sessionID, projectID, subjectID, studyUID);

			statusCode = HttpServletResponse.SC_ACCEPTED;
		} else {
			statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, BAD_DELETE_MESSAGE,
					responseStream, log);
		}
		return statusCode;
	}

	private boolean parametersAreValid(String projectID)
	{
		if (projectID == null) {
			log.warning("Missing project ID parameter");
			return false;
		} else
			return true;
	}

	private boolean parametersAreValid(String projectID, String subjectID)
	{
		if (parametersAreValid(projectID)) {
			if (subjectID == null) {
				log.warning("Missing subject ID parameter");
				return false;
			} else
				return true;
		} else
			return false;
	}

	private boolean parametersAreValid(String projectID, String subjectID, String studyUID)
	{
		if (parametersAreValid(projectID, subjectID)) {
			if (studyUID == null) {
				log.warning("Missing study UID parameter");
				return false;
			} else
				return true;
		} else
			return false;
	}

	private boolean parametersAreValid(String projectID, String subjectID, String studyUID, String seriesUID)
	{
		if (parametersAreValid(projectID, subjectID, studyUID)) {
			if (seriesUID == null) {
				log.warning("Missing series UID parameter");
				return false;
			} else
				return true;
		} else
			return false;
	}

	private boolean parametersAreValid(String projectID, String subjectID, String studyUID, String seriesUID,
			String imageID)
	{
		if (parametersAreValid(projectID, subjectID, studyUID, seriesUID)) {
			if (imageID == null) {
				log.warning("Missing image ID parameter");
				return false;
			} else
				return true;
		} else
			return false;
	}
}
