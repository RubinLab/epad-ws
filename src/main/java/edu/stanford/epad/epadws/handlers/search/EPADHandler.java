package edu.stanford.epad.epadws.handlers.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.xnat.XNATCreationOperations;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * @author martin
 */
public class EPADHandler extends AbstractHandler
{
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

	private static final EPADLogger log = EPADLogger.getInstance();

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("application/json");
		request.setHandled(true);

		log.info(httpRequest.getMethod() + " request " + httpRequest.getPathInfo() + " with parameters "
				+ httpRequest.getQueryString());

		try {
			responseStream = httpResponse.getWriter();

			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String method = httpRequest.getMethod();
				String username = httpRequest.getParameter("username");
				if (username != null) {
					if ("GET".equalsIgnoreCase(method)) {
						statusCode = handleQuery(httpRequest, responseStream, username);
					} else if ("DELETE".equalsIgnoreCase(method)) {
						statusCode = handleDelete(httpRequest, responseStream, username);
					} else if ("POST".equalsIgnoreCase(method)) {
						statusCode = handlePost(httpRequest, responseStream, username);
					} else {
						statusCode = HandlerUtil.badRequestJSONResponse(FORBIDDEN_MESSAGE, responseStream, log);
					}
				} else
					statusCode = HandlerUtil.badRequestJSONResponse(NO_USERNAME_MESSAGE, responseStream, log);
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
				if (projectParametersAreValid(projectID)) {
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
				if (subjectParametersAreValid(projectID, subjectID)) {
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
				if (studyParametersAreValid(projectID, subjectID, studyUID)) {
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
				if (seriesParametersAreValid(projectID, subjectID, studyUID, seriesUID)) {
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
				if (frameEditParametersAreValid(projectID, subjectID, studyUID, seriesUID, imageID)) {
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

	private int handleDelete(HttpServletRequest httpRequest, PrintWriter responseStream, String username)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		String sessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;

		if (HandlerUtil.matchesTemplate(PROJECT_TEMPLATE, pathInfo)) {
			Map<String, String> templateMap = HandlerUtil.getTemplateMap(PROJECT_TEMPLATE, pathInfo);
			String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");

			statusCode = epadOperations.projectDelete(sessionID, username, projectID);
		} else if (HandlerUtil.matchesTemplate(SUBJECT_TEMPLATE, pathInfo)) {
			Map<String, String> templateMap = HandlerUtil.getTemplateMap(SUBJECT_TEMPLATE, pathInfo);
			String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
			String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");

			statusCode = epadOperations.patientDelete(sessionID, username, projectID, subjectID);
		} else if (HandlerUtil.matchesTemplate(STUDY_TEMPLATE, pathInfo)) {
			Map<String, String> templateMap = HandlerUtil.getTemplateMap(STUDY_TEMPLATE, pathInfo);
			String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
			String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");
			String studyUID = HandlerUtil.getTemplateParameter(templateMap, "study");

			statusCode = epadOperations.studyDelete(sessionID, username, projectID, subjectID, studyUID);
		} else {
			statusCode = HandlerUtil.badRequestJSONResponse(BAD_DELETE_MESSAGE, responseStream, log);
		}
		return statusCode;
	}

	private int handlePost(HttpServletRequest httpRequest, PrintWriter responseStream, String username)
	{
		String sessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;

		if (HandlerUtil.matchesTemplate(PROJECT_LIST_TEMPLATE, pathInfo)) {
			String projectID = httpRequest.getParameter("projectID");
			String projectName = httpRequest.getParameter("projectName");
			String projectDescription = httpRequest.getParameter("projectDescription");

			if (projectCreationParametersAreValid(projectID, projectName, projectDescription, username)) {
				statusCode = XNATCreationOperations.createXNATProject(projectID, projectName, projectDescription, sessionID);
			} else
				statusCode = HandlerUtil.badRequestJSONResponse(BAD_POST_MESSAGE, responseStream, log);

		} else if (HandlerUtil.matchesTemplate(SUBJECT_LIST_TEMPLATE, pathInfo)) {
			Map<String, String> templateMap = HandlerUtil.getTemplateMap(SUBJECT_LIST_TEMPLATE, pathInfo);
			String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
			String subjectID = httpRequest.getParameter("subjectID");
			String subjectName = httpRequest.getParameter("subjectName");

			if (subjectCreationParametersAreValid(projectID, subjectID, subjectName, username)) {
				statusCode = XNATCreationOperations.createXNATSubject(projectID, subjectID, subjectName, sessionID);
			} else
				statusCode = HandlerUtil.badRequestJSONResponse(BAD_POST_MESSAGE, responseStream, log);
		} else if (HandlerUtil.matchesTemplate(STUDY_LIST_TEMPLATE, pathInfo)) {
			Map<String, String> templateMap = HandlerUtil.getTemplateMap(STUDY_LIST_TEMPLATE, pathInfo);
			String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
			String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");
			String studyUID = httpRequest.getParameter("studyUID");

			if (studyCreationParametersAreValid(projectID, subjectID, studyUID, username)) {
				statusCode = XNATCreationOperations.createXNATDICOMStudyExperiment(projectID, subjectID, studyUID, sessionID);
			} else
				statusCode = HandlerUtil.badRequestJSONResponse(BAD_POST_MESSAGE, responseStream, log);

		} else if (HandlerUtil.matchesTemplate(FRAME_LIST_TEMPLATE, pathInfo)) {
			Map<String, String> templateMap = HandlerUtil.getTemplateMap(FRAME_LIST_TEMPLATE, pathInfo);
			String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
			String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");
			String studyUID = HandlerUtil.getTemplateParameter(templateMap, "study");
			String seriesUID = HandlerUtil.getTemplateParameter(templateMap, "series");
			String imageUID = HandlerUtil.getTemplateParameter(templateMap, "image");
			if (frameEditParametersAreValid(projectID, subjectID, studyUID, seriesUID, imageUID)) {
				if (handleDSOFramesEdit(projectID, subjectID, studyUID, seriesUID, imageUID, httpRequest, responseStream))
					statusCode = HttpServletResponse.SC_CREATED;
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

	private boolean projectParametersAreValid(String projectID)
	{
		if (projectID == null) {
			log.warning("Missing project ID parameter");
			return false;
		} else
			return true;
	}

	private boolean subjectParametersAreValid(String projectID, String subjectID)
	{
		if (projectParametersAreValid(projectID)) {
			if (subjectID == null) {
				log.warning("Missing subject ID parameter");
				return false;
			} else
				return true;
		} else
			return false;
	}

	private boolean studyParametersAreValid(String projectID, String subjectID, String studyUID)
	{
		if (subjectParametersAreValid(projectID, subjectID)) {
			if (studyUID == null) {
				log.warning("Missing study UID parameter");
				return false;
			} else
				return true;
		} else
			return false;
	}

	private boolean seriesParametersAreValid(String projectID, String subjectID, String studyUID, String seriesUID)
	{
		if (studyParametersAreValid(projectID, subjectID, studyUID)) {
			if (seriesUID == null) {
				log.warning("Missing series UID parameter");
				return false;
			} else
				return true;
		} else
			return false;
	}

	private boolean frameEditParametersAreValid(String projectID, String subjectID, String studyUID, String seriesUID,
			String imageUID)
	{
		if (seriesParametersAreValid(projectID, subjectID, studyUID, seriesUID)) {
			if (imageUID == null) {
				log.warning("Missing image UID parameter");
				return false;
			} else
				return true;
		} else
			return false;
	}

	private boolean projectCreationParametersAreValid(String projectID, String projectName, String projectDescription,
			String username)
	{
		if (projectParametersAreValid(projectID)) {
			if (projectName == null) {
				log.warning("Missing projectName parameter from user " + username);
				return false;
			} else if (projectDescription == null) {
				log.warning("Missing projectDescription parameter from user " + username);
				return false;
			} else
				return true;
		} else
			return false;
	}

	private boolean subjectCreationParametersAreValid(String projectID, String subjectID, String subjectName,
			String username)
	{
		if (subjectParametersAreValid(projectID, subjectID)) {
			if (subjectName == null) {
				log.warning("Missing subjectName parameter from user " + username);
				return false;
			} else
				return true;
		} else
			return false;
	}

	private boolean studyCreationParametersAreValid(String projectID, String subjectID, String studyUID, String username)
	{
		return studyParametersAreValid(projectID, subjectID, studyUID);
	}

	private DSOEditResult createEditedDSO(DSOEditRequest dsoEditRequest, List<File> editFramesMaskFiles)
	{
		String projectID = dsoEditRequest.projectID;
		String patientID = dsoEditRequest.patientID;
		String dsoStudyUID = dsoEditRequest.studyUID;
		String dsoSeriesUID = "";
		String dsoImageUID = "";
		String aimID = "";
		// List<File> dsoFrames = new ArrayList<>();

		try {
			List<File> existingDSOFrameFiles = DSOUtil.getDSOFrameFiles(dsoStudyUID, dsoSeriesUID, dsoImageUID);
			List<File> finalDSOFrames = new ArrayList<>(existingDSOFrameFiles);
			int frameMaskFilesIndex = 0;
			for (Integer frameNumber : dsoEditRequest.editedFrameNumbers) {
				if (frameNumber >= 0 || frameNumber < existingDSOFrameFiles.size()) {
					finalDSOFrames.set(frameNumber, editFramesMaskFiles.get(frameMaskFilesIndex++));
				} else {
					log.warning("Frame number " + frameNumber + " is out of range for DSO image " + dsoImageUID + " in series "
							+ dsoSeriesUID);
					return null;
				}
			}
			File dsoFile = DSOUtil.createDSO(dsoStudyUID, dsoSeriesUID, dsoImageUID, finalDSOFrames);
			AIMUtil.generateAIMFileForDSO(dsoFile);
			return new DSOEditResult(projectID, patientID, dsoStudyUID, dsoSeriesUID, dsoImageUID, aimID);

		} catch (Exception e) {
			log.warning("Error generating AIM file for DSO image " + dsoImageUID + " in series " + dsoSeriesUID, e);
			return null;
		}
	}
}
