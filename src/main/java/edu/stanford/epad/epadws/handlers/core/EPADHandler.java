package edu.stanford.epad.epadws.handlers.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADFrame;
import edu.stanford.epad.dtos.EPADFrameList;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * @author martin
 */
public class EPADHandler extends AbstractHandler
{
	private static final String INTERNAL_ERROR_MESSAGE = "Internal error running query on projects route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on projects route";
	private static final String BAD_GET_MESSAGE = "Invalid GET request - parameters are invalid";
	private static final String BAD_DELETE_MESSAGE = "Invalid DELETE request!";
	private static final String BAD_POST_MESSAGE = "Invalid POST request!";
	private static final String BAD_PUT_MESSAGE = "Invalid PUT request!";
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
						statusCode = handleGet(httpRequest, responseStream, username);
					} else if ("DELETE".equalsIgnoreCase(method)) {
						statusCode = handleDelete(httpRequest, responseStream, username);
					} else if ("PUT".equalsIgnoreCase(method)) {
						statusCode = handlePut(httpRequest, httpResponse, responseStream, username);
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

	private int handleGet(HttpServletRequest httpRequest, PrintWriter responseStream, String username)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		String sessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;

		try {
			EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(httpRequest);

			if (HandlerUtil.matchesTemplate(RouteTemplates.PROJECT_LIST, pathInfo)) {
				EPADProjectList projectList = epadOperations
						.getAllProjectDescriptionsForUser(username, sessionID, searchFilter);
				responseStream.append(projectList.toJSON());

				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(RouteTemplates.PROJECT, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(RouteTemplates.PROJECT, pathInfo);
				EPADProject project = epadOperations.getProjectDescription(projectReference, username, sessionID);
				responseStream.append(project.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.PROJECT_AIM_LIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(RouteTemplates.PROJECT_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getProjectAIMDescriptions(projectReference, username, sessionID);
				responseStream.append(aims.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.PROJECT_AIM, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(RouteTemplates.PROJECT_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(RouteTemplates.PROJECT_AIM, pathInfo);
				EPADAIM aim = epadOperations
						.getProjectAIMDescription(projectReference, aimReference.aimID, username, sessionID);
				responseStream.append(aim.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.SUBJECT_LIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(RouteTemplates.SUBJECT_LIST, pathInfo);
				EPADSubjectList subjectList = epadOperations.getAllSubjectDescriptionsForProject(projectReference.projectID,
						username, sessionID, searchFilter);
				responseStream.append(subjectList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.SUBJECT, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(RouteTemplates.SUBJECT, pathInfo);
				EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
				responseStream.append(subject.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.SUBJECT_AIM_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(RouteTemplates.SUBJECT_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getSubjectAIMDescriptions(subjectReference, username, sessionID);
				responseStream.append(aims.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.SUBJECT_AIM, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(RouteTemplates.SUBJECT_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(RouteTemplates.SUBJECT_AIM, pathInfo);
				EPADAIM aim = epadOperations
						.getSubjectAIMDescription(subjectReference, aimReference.aimID, username, sessionID);
				responseStream.append(aim.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.STUDY_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(RouteTemplates.STUDY_LIST, pathInfo);
				EPADStudyList studyList = epadOperations.getAllStudyDescriptionsForSubject(subjectReference, username,
						sessionID, searchFilter);
				responseStream.append(studyList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.STUDY, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(RouteTemplates.STUDY, pathInfo);
				EPADStudy study = epadOperations.getStudyDescription(studyReference, username, sessionID);
				responseStream.append(study.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.STUDY_AIM_LIST, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(RouteTemplates.STUDY_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getStudyAIMDescriptions(studyReference, username, sessionID);
				responseStream.append(aims.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.STUDY_AIM, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(RouteTemplates.STUDY_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(RouteTemplates.STUDY_AIM, pathInfo);
				EPADAIM aim = epadOperations.getStudyAIMDescription(studyReference, aimReference.aimID, username, sessionID);
				responseStream.append(aim.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.SERIES_LIST, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(RouteTemplates.SERIES_LIST, pathInfo);
				EPADSeriesList seriesList = epadOperations.getAllSeriesDescriptionsForStudy(studyReference, username,
						sessionID, searchFilter);
				responseStream.append(seriesList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.SERIES, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(RouteTemplates.STUDY, pathInfo);
				EPADSeries series = epadOperations.getSeriesDescription(seriesReference, username, sessionID);
				responseStream.append(series.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.SERIES_AIM_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(RouteTemplates.SERIES_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
				responseStream.append(aims.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.SERIES_AIM, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(RouteTemplates.SERIES_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(RouteTemplates.SERIES_AIM, pathInfo);
				EPADAIM aim = epadOperations.getSeriesAIMDescription(seriesReference, aimReference.aimID, username, sessionID);
				responseStream.append(aim.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.IMAGE_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(RouteTemplates.IMAGE_LIST, pathInfo);
				EPADImageList imageList = epadOperations.getAllImageForSeries(seriesReference, sessionID,
						searchFilter);
				responseStream.append(imageList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.IMAGE, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(RouteTemplates.IMAGE, pathInfo);
				// String format = httpRequest.getParameter("format");
				EPADImage image = epadOperations.getImageDescription(imageReference, sessionID);
				responseStream.append(image.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.IMAGE_AIM_LIST, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(RouteTemplates.IMAGE_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getImageAIMDescriptions(imageReference, username, sessionID);
				responseStream.append(aims.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.IMAGE_AIM, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(RouteTemplates.IMAGE_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(RouteTemplates.SERIES_AIM, pathInfo);
				EPADAIM aim = epadOperations.getImageAIMDescription(imageReference, aimReference.aimID, username, sessionID);
				responseStream.append(aim.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.FRAME_LIST, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(RouteTemplates.FRAME_LIST, pathInfo);
				EPADFrameList frameList = epadOperations.getAllFrameDescriptionsForImage(imageReference, sessionID,
						searchFilter);
				responseStream.append(frameList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.FRAME, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(RouteTemplates.FRAME, pathInfo);
				// String format = httpRequest.getParameter("format");
				EPADFrame frame = epadOperations.getFrameDescription(frameReference, sessionID);

				responseStream.append(frame.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.FRAME_AIM_LIST, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(RouteTemplates.FRAME_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getFrameAIMDescriptions(frameReference, username, sessionID);
				responseStream.append(aims.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(RouteTemplates.FRAME_AIM, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(RouteTemplates.FRAME_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(RouteTemplates.FRAME_AIM, pathInfo);
				EPADAIM aim = epadOperations.getFrameAIMDescription(frameReference, aimReference.aimID, username, sessionID);
				responseStream.append(aim.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else
				statusCode = HandlerUtil.badRequestJSONResponse(BAD_GET_MESSAGE, responseStream, log);
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
		}
		return statusCode;
	}

	private int handlePut(HttpServletRequest httpRequest, HttpServletResponse httpResponse, PrintWriter responseStream,
			String username)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		String sessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;

		if (HandlerUtil.matchesTemplate(RouteTemplates.PROJECT, pathInfo)) {
			ProjectReference projectReference = ProjectReference.extract(RouteTemplates.PROJECT, pathInfo);
			String projectName = httpRequest.getParameter("projectName");
			String projectDescription = httpRequest.getParameter("projectDescription");
			statusCode = epadOperations.createProject(projectReference, projectName, projectDescription, sessionID);

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.SUBJECT, pathInfo)) {
			SubjectReference subjectReference = SubjectReference.extract(RouteTemplates.SUBJECT, pathInfo);
			String subjectName = httpRequest.getParameter("subjectName");
			statusCode = epadOperations.createSubject(subjectReference, subjectName, sessionID);

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.STUDY, pathInfo)) {
			StudyReference studyReference = StudyReference.extract(RouteTemplates.STUDY, pathInfo);
			statusCode = epadOperations.createStudy(studyReference, sessionID);

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.STUDY_AIM, pathInfo)) {
			StudyReference studyReference = StudyReference.extract(RouteTemplates.STUDY_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(RouteTemplates.STUDY_AIM, pathInfo);
			statusCode = epadOperations.createStudyAIM(studyReference, aimReference.aimID, sessionID);

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.SERIES, pathInfo)) {
			SeriesReference seriesReference = SeriesReference.extract(RouteTemplates.SERIES, pathInfo);
			statusCode = epadOperations.createSeries(seriesReference, sessionID);

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.SERIES_AIM, pathInfo)) {
			SeriesReference seriesReference = SeriesReference.extract(RouteTemplates.SERIES_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(RouteTemplates.SERIES_AIM, pathInfo);
			statusCode = epadOperations.createSeriesAIM(seriesReference, aimReference.aimID, sessionID);
		} else if (HandlerUtil.matchesTemplate(RouteTemplates.IMAGE, pathInfo)) {
			statusCode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
			httpResponse.addHeader("Allow", "GET, DELETE");

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.IMAGE_AIM, pathInfo)) {
			ImageReference imageReference = ImageReference.extract(RouteTemplates.IMAGE_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(RouteTemplates.IMAGE_AIM, pathInfo);
			statusCode = epadOperations.createImageAIM(imageReference, aimReference.aimID, sessionID);

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.FRAME, pathInfo)) {
			statusCode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
			httpResponse.addHeader("Allow", "GET, DELETE");

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.FRAME_AIM, pathInfo)) {
			FrameReference frameReference = FrameReference.extract(RouteTemplates.FRAME_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(RouteTemplates.FRAME_AIM, pathInfo);
			statusCode = epadOperations.createFrameAIM(frameReference, aimReference.aimID, sessionID);

		} else {
			statusCode = HandlerUtil.badRequestJSONResponse(BAD_PUT_MESSAGE, responseStream, log);
		}
		return statusCode;
	}

	private int handlePost(HttpServletRequest httpRequest, PrintWriter responseStream, String username)
	{
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;

		if (HandlerUtil.matchesTemplate(RouteTemplates.PROJECT_LIST, pathInfo)) {
			statusCode = HandlerUtil.badRequestJSONResponse(BAD_POST_MESSAGE, responseStream, log);
		} else if (HandlerUtil.matchesTemplate(RouteTemplates.SUBJECT_LIST, pathInfo)) {
			statusCode = HandlerUtil.badRequestJSONResponse(BAD_POST_MESSAGE, responseStream, log);
		} else if (HandlerUtil.matchesTemplate(RouteTemplates.STUDY_LIST, pathInfo)) {
			statusCode = HandlerUtil.badRequestJSONResponse(BAD_POST_MESSAGE, responseStream, log);
		} else if (HandlerUtil.matchesTemplate(RouteTemplates.FRAME_LIST, pathInfo)) {
			ImageReference imageReference = ImageReference.extract(RouteTemplates.FRAME_LIST, pathInfo);
			if (handleDSOFramesEdit(imageReference.projectID, imageReference.subjectID, imageReference.studyUID,
					imageReference.seriesUID, imageReference.imageUID, httpRequest, responseStream))
				statusCode = HttpServletResponse.SC_CREATED;
			else
				statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} else {
			statusCode = HandlerUtil.badRequestJSONResponse(BAD_POST_MESSAGE, responseStream, log);
		}
		return statusCode;
	}

	private int handleDelete(HttpServletRequest httpRequest, PrintWriter responseStream, String username)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		String sessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;

		if (HandlerUtil.matchesTemplate(RouteTemplates.PROJECT, pathInfo)) {
			ProjectReference projectReference = ProjectReference.extract(RouteTemplates.PROJECT, pathInfo);
			statusCode = epadOperations.projectDelete(projectReference.projectID, sessionID, username);

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.SUBJECT, pathInfo)) {
			SubjectReference subjectReference = SubjectReference.extract(RouteTemplates.SUBJECT, pathInfo);
			statusCode = epadOperations.subjectDelete(subjectReference, sessionID, username);

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.STUDY, pathInfo)) {
			StudyReference studyReference = StudyReference.extract(RouteTemplates.STUDY, pathInfo);
			statusCode = epadOperations.studyDelete(studyReference, sessionID, username);

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.STUDY_AIM, pathInfo)) {
			StudyReference studyReference = StudyReference.extract(RouteTemplates.STUDY_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(RouteTemplates.STUDY_AIM, pathInfo);
			statusCode = epadOperations.studyAIMDelete(studyReference, aimReference.aimID, sessionID, username);

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.SERIES, pathInfo)) {
			SeriesReference seriesReference = SeriesReference.extract(RouteTemplates.SERIES, pathInfo);
			statusCode = epadOperations.seriesDelete(seriesReference, sessionID, username);

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.SERIES_AIM, pathInfo)) {
			SeriesReference seriesReference = SeriesReference.extract(RouteTemplates.SERIES_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(RouteTemplates.SERIES_AIM, pathInfo);
			statusCode = epadOperations.seriesAIMDelete(seriesReference, aimReference.aimID, sessionID, username);

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.IMAGE_AIM, pathInfo)) {
			ImageReference imageReference = ImageReference.extract(RouteTemplates.IMAGE_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(RouteTemplates.IMAGE_AIM, pathInfo);
			statusCode = epadOperations.imageAIMDelete(imageReference, aimReference.aimID, sessionID, username);

		} else if (HandlerUtil.matchesTemplate(RouteTemplates.FRAME_AIM, pathInfo)) {
			FrameReference frameReference = FrameReference.extract(RouteTemplates.FRAME_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(RouteTemplates.FRAME_AIM, pathInfo);
			statusCode = epadOperations.frameAIMDelete(frameReference, aimReference.aimID, sessionID, username);

		} else {
			statusCode = HandlerUtil.badRequestJSONResponse(BAD_DELETE_MESSAGE, responseStream, log);
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
