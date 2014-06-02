package edu.stanford.epad.epadws.handlers.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.DSOEditRequest;
import edu.stanford.epad.dtos.DSOEditResult;
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
	private static final String FRAME_LIST_TEMPLATE = IMAGE_TEMPLATE + "/frames/";

	private static final String INTERNAL_ERROR_MESSAGE = "Internal error running query on projects route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on projects route";
	private static final String BAD_GET_MESSAGE = "Invalid GET request - parameters are invalid";
	private static final String BAD_DELETE_MESSAGE = "Invalid DELETE request!";
	private static final String BAD_POST_MESSAGE = "Invalid POST request!";
	private static final String FORBIDDEN_MESSAGE = "Forbidden method - only GET, DELETE, and POST allowed!";
	private static final String NO_USERNAME_MESSAGE = "Must have username parameter for queries!";

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
					String username = httpRequest.getParameter("username");
					if (username != null)
						statusCode = handleQuery(httpRequest, responseStream, username);
					else
						statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, NO_USERNAME_MESSAGE,
								responseStream, log);
				} else if ("DELETE".equalsIgnoreCase(method)) {
					statusCode = handleDelete(httpRequest, responseStream);
				} else if ("POST".equalsIgnoreCase(method)) {
					statusCode = handlePost(httpRequest, responseStream);
				} else {
					statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, FORBIDDEN_MESSAGE,
							responseStream, log);
				}
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
		} catch (Exception e) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, e, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	// TODO Too long.
	private int handleQuery(HttpServletRequest httpRequest, PrintWriter responseStream, String username)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		String jsessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
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
					EPADSubjectList subjectList = epadOperations.getAllSubjectsForProject(projectID, username, jsessionID,
							searchFilter);
					responseStream.append(subjectList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HandlerUtil.badRequestJSONResponse(BAD_GET_MESSAGE, responseStream, log);

			} else if (HandlerUtil.matchesTemplate(STUDY_LIST_TEMPLATE, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(STUDY_LIST_TEMPLATE, pathInfo);
				String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");
				if (parametersAreValid(projectID, subjectID)) {
					EPADStudyList studyList = epadOperations.getAllStudiesForPatient(projectID, subjectID, username, jsessionID,
							searchFilter);
					responseStream.append(studyList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HandlerUtil.badRequestJSONResponse(BAD_GET_MESSAGE, responseStream, log);

			} else if (HandlerUtil.matchesTemplate(SERIES_LIST_TEMPLATE, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(SERIES_LIST_TEMPLATE, pathInfo);
				String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");
				String studyUID = HandlerUtil.getTemplateParameter(templateMap, "study");
				if (parametersAreValid(projectID, subjectID, studyUID)) {
					EPADSeriesList seriesList = epadOperations.getAllSeriesForStudy(projectID, subjectID, studyUID, username,
							jsessionID, searchFilter);
					responseStream.append(seriesList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HandlerUtil.badRequestJSONResponse(BAD_GET_MESSAGE, responseStream, log);

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
					statusCode = HandlerUtil.badRequestJSONResponse(BAD_GET_MESSAGE, responseStream, log);
			} else if (HandlerUtil.matchesTemplate(IMAGE_TEMPLATE, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(IMAGE_TEMPLATE, pathInfo);
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
					statusCode = HandlerUtil.badRequestJSONResponse(BAD_GET_MESSAGE, responseStream, log);
			} else
				statusCode = HandlerUtil.badRequestJSONResponse(BAD_GET_MESSAGE, responseStream, log);
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
			statusCode = HandlerUtil.badRequestJSONResponse(BAD_DELETE_MESSAGE, responseStream, log);
		}
		return statusCode;
	}

	private int handlePost(HttpServletRequest httpRequest, PrintWriter responseStream)
	{
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;

		if (HandlerUtil.matchesTemplate(FRAME_LIST_TEMPLATE, pathInfo)) {
			Map<String, String> templateMap = HandlerUtil.getTemplateMap(FRAME_LIST_TEMPLATE, pathInfo);
			String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
			String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");
			String studyUID = HandlerUtil.getTemplateParameter(templateMap, "study");
			String seriesUID = HandlerUtil.getTemplateParameter(templateMap, "series");
			String imageUID = HandlerUtil.getTemplateParameter(templateMap, "image");
			if (parametersAreValid(projectID, subjectID, studyUID, seriesUID, imageUID)) {
				if (handleDSOFramesEdit(projectID, subjectID, studyUID, seriesUID, imageUID, httpRequest, responseStream))
					statusCode = HttpServletResponse.SC_OK;
				else
					statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			} else
				statusCode = HandlerUtil.badRequestJSONResponse(BAD_POST_MESSAGE, responseStream, log);
		} else {
			statusCode = HandlerUtil.badRequestJSONResponse(BAD_POST_MESSAGE, responseStream, log);
		}
		return statusCode;
	}

	private boolean handleDSOFramesEdit(String projectID, String patientID, String studyUID, String seriesUID,
			String imageUID, HttpServletRequest httpRequest, PrintWriter responseStream)
	{ // See http://www.tutorialspoint.com/servlets/servlets-file-uploading.htm
		boolean uploadError = false;

		log.info("Received DSO edit request for series " + seriesUID);

		try {
			ServletFileUpload servletFileUpload = new ServletFileUpload();
			FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(httpRequest);

			if (fileItemIterator.hasNext()) {
				DSOEditRequest dsoEditRequest = extractDSOEditRequest(fileItemIterator);

				if (dsoEditRequest != null) {
					List<File> editedFramesPNGMaskFiles = HandlerUtil.extractFiles(fileItemIterator, "DSOEditedFrame", "PNG");
					if (editedFramesPNGMaskFiles.isEmpty()) {
						log.warning("No PNG masks supplied in DSO edit request for series " + seriesUID);
						uploadError = true;
					} else {
						DSOEditResult dsoEditResult = createEditedDSO(dsoEditRequest, editedFramesPNGMaskFiles);
						if (dsoEditResult != null)
							responseStream.append(dsoEditResult.toJSON());
						else
							uploadError = true;
					}
				} else {
					log.warning("Invalid JSON header in DSO edit request for series " + seriesUID);
					uploadError = true;
				}
			} else {
				log.warning("Missing body in DSO edit request for series " + seriesUID);
				uploadError = true;
			}
		} catch (IOException e) {
			log.warning("IO exception handling DSO edits for series " + seriesUID, e);
			uploadError = true;
		} catch (FileUploadException e) {
			log.warning("File upload exception handling DSO edits for series " + seriesUID, e);
			uploadError = true;
		}
		return uploadError;
	}

	private DSOEditRequest extractDSOEditRequest(FileItemIterator fileItemIterator) throws FileUploadException,
			IOException, UnsupportedEncodingException
	{
		DSOEditRequest dsoEditRequest = null;
		Gson gson = new Gson();
		FileItemStream headerJSONItemStream = fileItemIterator.next();
		InputStream headerJSONStream = headerJSONItemStream.openStream();
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			isr = new InputStreamReader(headerJSONStream, "UTF-8");
			br = new BufferedReader(isr);

			dsoEditRequest = gson.fromJson(br, DSOEditRequest.class);
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
		}
		return dsoEditRequest;
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
			String imageUID)
	{
		if (parametersAreValid(projectID, subjectID, studyUID, seriesUID)) {
			if (imageUID == null) {
				log.warning("Missing image UID parameter");
				return false;
			} else
				return true;
		} else
			return false;
	}

	private DSOEditResult createEditedDSO(DSOEditRequest dsoEditRequest, List<File> editFramesMaskFiles)
	{
		String projectID = dsoEditRequest.projectID;
		String patientID = dsoEditRequest.patientID;
		String dsoStudyUID = dsoEditRequest.studyUID;
		String dsoSeriesUID = "";
		String dsoImageUID = "";
		String aimID = "";
		// Find existing PNG mask files for DSO
		// Create the DSO, supplying all mask files
		// Create the AIM
		return new DSOEditResult(projectID, patientID, dsoStudyUID, dsoSeriesUID, dsoImageUID, aimID);
	}
}
