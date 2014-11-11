package edu.stanford.epad.epadws.handlers.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADError;
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
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.queries.XNATQueries;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * @author martin
 */
public class EPADHandler extends AbstractHandler
{
	private static final String INTERNAL_ERROR_MESSAGE = "Internal error";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";
	private static final String BAD_GET_MESSAGE = "Invalid GET request - parameters are invalid";
	private static final String BAD_DELETE_MESSAGE = "Invalid DELETE request!";
	private static final String BAD_POST_MESSAGE = "Invalid POST request!";
	private static final String BAD_PUT_MESSAGE = "Invalid PUT request!";
	private static final String FORBIDDEN_MESSAGE = "Forbidden method - only GET, DELETE, PUT, and POST allowed!";
	private static final String NO_USERNAME_MESSAGE = "Must have username parameter for requests!";

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
			String method = httpRequest.getMethod();
			File aimFile = null;
			if ("PUT".equalsIgnoreCase(method)) {
				// Note: This needs to be done, before anything else otherwise upload won't work
				aimFile = this.getUploadedAIMFile(httpRequest);
			}

			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String username = httpRequest.getParameter("username");
				log.info("Request from client:" + method + " user:" + username);
				if (username != null) {
					if ("GET".equalsIgnoreCase(method)) {
						statusCode = handleGet(httpRequest, responseStream, username);
					} else if ("DELETE".equalsIgnoreCase(method)) {
						statusCode = handleDelete(httpRequest, responseStream, username);
					} else if ("PUT".equalsIgnoreCase(method)) {
						statusCode = handlePut(httpRequest, httpResponse, responseStream, username, aimFile);
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
			e.printStackTrace();
			log.warning("Error in handle request:", e);
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
		log.info("GET Request from client:" + pathInfo + " user:" + username + " sessionID:" + sessionID);
		try {
			if (sessionID == null)
				throw new Exception("Invalid sessionID for user:" + username);
			EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(httpRequest);
			int start = getInt(httpRequest.getParameter("start"));
			if (start == 0) start = 1;
			int count = getInt(httpRequest.getParameter("count"));
			if (count == 0) count = 5000;
			long starttime = System.currentTimeMillis();
			if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_LIST, pathInfo)) {
				EPADProjectList projectList = epadOperations.getProjectDescriptions(username, sessionID, searchFilter);
				responseStream.append(projectList.toJSON());

				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT, pathInfo);
				EPADProject project = epadOperations.getProjectDescription(projectReference, username, sessionID);
				if (project != null) {
					log.info("Project aim count:" + project.numberOfAnnotations);
					responseStream.append(project.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HttpServletResponse.SC_NOT_FOUND;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_LIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.SUBJECT_LIST, pathInfo);
				EPADSubjectList subjectList = epadOperations.getSubjectDescriptions(projectReference.projectID, username,
						sessionID, searchFilter);
				long endtime = System.currentTimeMillis();
				log.info("Returning " + subjectList.ResultSet.totalRecords + " subjects to client, took " + (endtime-starttime) + " msecs");
				responseStream.append(subjectList.toJSON());
				long resptime = System.currentTimeMillis();
				log.info("Time taken for write http response:" + (resptime-endtime) + " msecs");
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT, pathInfo);
				EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
				if (subject != null) {
					log.info("subject aim count:" + subject.numberOfAnnotations);
					responseStream.append(subject.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HttpServletResponse.SC_NOT_FOUND;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.STUDY_LIST, pathInfo);
				EPADStudyList studyList = epadOperations.getStudyDescriptions(subjectReference, username, sessionID,
						searchFilter);
				responseStream.append(studyList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY, pathInfo);
				EPADStudy study = epadOperations.getStudyDescription(studyReference, username, sessionID);
				if (study != null) {
					responseStream.append(study.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HttpServletResponse.SC_NOT_FOUND;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_LIST, pathInfo)) {
				boolean filterDSO = "true".equalsIgnoreCase(httpRequest.getParameter("filterDSO"));
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.SERIES_LIST, pathInfo);
				EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID,
						searchFilter, filterDSO);
				responseStream.append(seriesList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES, pathInfo);
				EPADSeries series = epadOperations.getSeriesDescription(seriesReference, username, sessionID);
				if (series != null) {
					responseStream.append(series.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HttpServletResponse.SC_NOT_FOUND;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.IMAGE_LIST, pathInfo);
				EPADImageList imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, searchFilter);
				responseStream.append(imageList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.IMAGE, pathInfo);
				EPADImage image = epadOperations.getImageDescription(imageReference, sessionID);
				if (image != null) {
					responseStream.append(image.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HttpServletResponse.SC_NOT_FOUND;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME_LIST, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.FRAME_LIST, pathInfo);
				EPADFrameList frameList = epadOperations.getFrameDescriptions(imageReference);
				responseStream.append(frameList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(ProjectsRouteTemplates.FRAME, pathInfo);
				EPADFrame frame = epadOperations.getFrameDescription(frameReference, sessionID);
				if (frame != null) {
					responseStream.append(frame.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HttpServletResponse.SC_NOT_FOUND;
				
			} else if (HandlerUtil.matchesTemplate(SubjectsRouteTemplates.SUBJECT, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(SubjectsRouteTemplates.SUBJECT, pathInfo);
				EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
				if (subject != null) {
					responseStream.append(subject.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HttpServletResponse.SC_NOT_FOUND;

			} else if (HandlerUtil.matchesTemplate(SubjectsRouteTemplates.SUBJECT_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(SubjectsRouteTemplates.SUBJECT_LIST, pathInfo);
				EPADStudyList studyList = epadOperations.getStudyDescriptions(subjectReference, username, sessionID,
						searchFilter);
				responseStream.append(studyList.toJSON());
				statusCode = HttpServletResponse.SC_OK;


				/**
				 * Studies routes. These short cuts are used when the invoker does not have a project or subject ID.
				 */
			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.SERIES, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(StudiesRouteTemplates.SERIES, pathInfo);
				EPADSeries series = epadOperations.getSeriesDescription(seriesReference, username, sessionID);
				responseStream.append(series.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.IMAGE_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(StudiesRouteTemplates.IMAGE_LIST, pathInfo);
				EPADImageList imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, searchFilter);
				responseStream.append(imageList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.IMAGE, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(StudiesRouteTemplates.IMAGE, pathInfo);
				EPADImage image = epadOperations.getImageDescription(imageReference, sessionID);
				if (image != null) {
					responseStream.append(image.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					statusCode = HttpServletResponse.SC_NOT_FOUND;
				
			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.FRAME_LIST, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(StudiesRouteTemplates.FRAME_LIST, pathInfo);
				EPADFrameList frameList = epadOperations.getFrameDescriptions(imageReference);
				responseStream.append(frameList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

				/**
				 * New AIM-related routes.
				 */

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_AIM_LIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_AIM_LIST, pathInfo);
				AIMSearchType aimSearchType = AIMUtil.getAIMSearchType(httpRequest);
				String searchValue = aimSearchType != null ? httpRequest.getParameter(aimSearchType.getName()) : null;
				String projectID = projectReference.projectID;
				log.info("GET request for AIMs from user " + username + "; query type is " + aimSearchType + ", value "
						+ searchValue + ", project " + projectID);
				EPADAIMList aims = null;
				if (aimSearchType != null)
					aims = epadOperations.getAIMDescriptions(projectID, aimSearchType, searchValue, username, sessionID, start, count);
				else
					aims = epadOperations.getProjectAIMDescriptions(projectReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{
					aims = AIMUtil.queryAIMImageAnnotationSummaries(aims, username, start, count, sessionID);					
					long starttime2 = System.currentTimeMillis();
					responseStream.append(aims.toJSON());
					long resptime = System.currentTimeMillis();
					log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							AIMUtil.getUIDCsvList(sessionID, aims, username), username, start, count);					
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_AIM, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
				EPADAIM aim = epadOperations
						.getProjectAIMDescription(projectReference, aimReference.aimID, username, sessionID);
				if (!XNATQueries.isCollaborator(sessionID, username, aim.projectID))
					username = null;
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getSubjectAIMDescriptions(subjectReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummaries(aims, username, start, count, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							AIMUtil.getUIDCsvList(sessionID, aims, username), username, start, count);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo);
				EPADAIM aim = epadOperations
						.getSubjectAIMDescription(subjectReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_AIM_LIST, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getStudyAIMDescriptions(studyReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummaries(aims, username, start, count, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							AIMUtil.getUIDCsvList(sessionID, aims, username), username, start, count);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_AIM, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.STUDY_AIM, pathInfo);
				EPADAIM aim = epadOperations.getStudyAIMDescription(studyReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_AIM_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummaries(aims, username, start, count, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							AIMUtil.getUIDCsvList(sessionID, aims, username), username, start, count);					
				}

				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_AIM, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
				EPADAIM aim = epadOperations.getSeriesAIMDescription(seriesReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE_AIM_LIST, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.IMAGE_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getImageAIMDescriptions(imageReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummaries(aims, username, start, count, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							AIMUtil.getUIDCsvList(sessionID, aims, username), username, start, count);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE_AIM, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.IMAGE_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
				EPADAIM aim = epadOperations.getImageAIMDescription(imageReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME_AIM_LIST, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(ProjectsRouteTemplates.FRAME_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getFrameAIMDescriptions(frameReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummaries(aims, username, start, count, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							AIMUtil.getUIDCsvList(sessionID, aims, username), username, start, count);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME_AIM, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(ProjectsRouteTemplates.FRAME_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.FRAME_AIM, pathInfo);
				EPADAIM aim = epadOperations.getFrameAIMDescription(frameReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.STUDY_AIM_LIST, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(StudiesRouteTemplates.STUDY_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getStudyAIMDescriptions(studyReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummaries(aims, username, start, count, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							AIMUtil.getUIDCsvList(sessionID, aims, username), username, start, count);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.STUDY_AIM, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(StudiesRouteTemplates.STUDY_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(StudiesRouteTemplates.STUDY_AIM, pathInfo);
				EPADAIM aim = epadOperations.getStudyAIMDescription(studyReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.SERIES_AIM_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(StudiesRouteTemplates.SERIES_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummaries(aims, username, start, count, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							AIMUtil.getUIDCsvList(sessionID, aims, username), username, start, count);					
				}

				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.SERIES_AIM, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(StudiesRouteTemplates.SERIES_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(StudiesRouteTemplates.SERIES_AIM, pathInfo);
				EPADAIM aim = epadOperations.getSeriesAIMDescription(seriesReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.IMAGE_AIM_LIST, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(StudiesRouteTemplates.IMAGE_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getImageAIMDescriptions(imageReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummaries(aims, username, start, count, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							AIMUtil.getUIDCsvList(sessionID, aims, username), username, start, count);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.IMAGE_AIM, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(StudiesRouteTemplates.IMAGE_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(StudiesRouteTemplates.SERIES_AIM, pathInfo);
				EPADAIM aim = epadOperations.getImageAIMDescription(imageReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.FRAME_AIM_LIST, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(StudiesRouteTemplates.FRAME_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getFrameAIMDescriptions(frameReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummaries(aims, username, start, count, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							AIMUtil.getUIDCsvList(sessionID, aims, username), username, start, count);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.FRAME_AIM, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(StudiesRouteTemplates.FRAME_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(StudiesRouteTemplates.FRAME_AIM, pathInfo);
				EPADAIM aim = epadOperations.getFrameAIMDescription(frameReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(SubjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(SubjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getSubjectAIMDescriptions(subjectReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummaries(aims, username, start, count, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							AIMUtil.getUIDCsvList(sessionID, aims, username), username, start, count);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(SubjectsRouteTemplates.SUBJECT_AIM, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(SubjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo);
				AIMReference aimReference = AIMReference.extract(SubjectsRouteTemplates.SUBJECT_AIM, pathInfo);
				EPADAIM aim = epadOperations.getSubjectAIMDescription(subjectReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(AimsRouteTemplates.AIMS_LIST, pathInfo)) {
				AIMSearchType aimSearchType = AIMUtil.getAIMSearchType(httpRequest);
				String searchValue = aimSearchType != null ? httpRequest.getParameter(aimSearchType.getName()) : null;
				String projectID = httpRequest.getParameter("projectID");
				log.info("GET request for AIMs from user " + username + "; query type is " + aimSearchType + ", value "
						+ searchValue + ", project " + projectID);
				EPADAIMList aims = epadOperations.getAIMDescriptions(projectID, aimSearchType, searchValue, username, sessionID, start, count);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummaries(aims, username, start, count, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else
				{
					if (aimSearchType == null && username.equals("admin"))
					{
						AIMUtil.queryAIMImageAnnotations(responseStream, projectID, AIMSearchType.ANNOTATION_UID,
								"all", "", start, count);
					}
					else
					{
						AIMUtil.queryAIMImageAnnotations(responseStream, projectID, AIMSearchType.ANNOTATION_UID,
								AIMUtil.getUIDCsvList(sessionID, aims, username), username, start, count);
					}
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(AimsRouteTemplates.AIM, pathInfo)) {
				AIMReference aimReference = AIMReference.extract(AimsRouteTemplates.AIM, pathInfo);
				EPADAIM aim = epadOperations.getAIMDescription(aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else
				statusCode = HandlerUtil.badRequestJSONResponse(BAD_GET_MESSAGE, responseStream, log);
		} catch (Throwable t) {
			t.printStackTrace();
			log.warning("Error handleget:", t);
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
		}
		return statusCode;
	}

	private int handlePut(HttpServletRequest httpRequest, HttpServletResponse httpResponse, PrintWriter responseStream,
			String username, File aimFile)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		String sessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;
		log.info("PUT Request from client:" + pathInfo + " user:" + username + " sessionID:" + sessionID);

		if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT, pathInfo)) {
			ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT, pathInfo);
			String projectName = httpRequest.getParameter("projectName");
			String projectDescription = httpRequest.getParameter("projectDescription");
			statusCode = epadOperations.createProject(projectReference, projectName, projectDescription, sessionID);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT, pathInfo)) {
			SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT, pathInfo);
			String subjectName = httpRequest.getParameter("subjectName");
			statusCode = epadOperations.createSubject(subjectReference, subjectName, sessionID);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_STATUS, pathInfo)) {
			SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_STATUS, pathInfo);
			String status = epadOperations.setSubjectStatus(subjectReference, sessionID, username);
			if (status == null || status.length() == 0)
				statusCode = HttpServletResponse.SC_OK;
			else
				statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;				

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo)) {
			SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo);
			statusCode = epadOperations.createSubjectAIM(username, subjectReference, aimReference.aimID, aimFile, sessionID);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY, pathInfo)) {
			StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY, pathInfo);
			statusCode = epadOperations.createStudy(studyReference, sessionID);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_AIM, pathInfo)) {
			StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.STUDY_AIM, pathInfo);
			statusCode = epadOperations.createStudyAIM(username, studyReference, aimReference.aimID, aimFile, sessionID);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES, pathInfo)) {
			SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES, pathInfo);
			statusCode = epadOperations.createSeries(seriesReference, sessionID);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_AIM, pathInfo)) {
			SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
			statusCode = epadOperations.createSeriesAIM(username, seriesReference, aimReference.aimID, aimFile, sessionID);
		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE, pathInfo)) {
			statusCode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
			httpResponse.addHeader("Allow", "GET, DELETE");

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE_AIM, pathInfo)) {
			ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.IMAGE_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.IMAGE_AIM, pathInfo);
			log.info("Images AIM PUT");
			statusCode = epadOperations.createImageAIM(username, imageReference, aimReference.aimID, aimFile, sessionID);
		
		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_AIM, pathInfo)) {
			ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
			log.info("Projects AIM PUT");
			statusCode = epadOperations.createProjectAIM(username, projectReference, aimReference.aimID, aimFile, sessionID);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME, pathInfo)) {
			statusCode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
			httpResponse.addHeader("Allow", "GET, DELETE");

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME_AIM, pathInfo)) {
			FrameReference frameReference = FrameReference.extract(ProjectsRouteTemplates.FRAME_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.FRAME_AIM, pathInfo);
			statusCode = epadOperations.createFrameAIM(username, frameReference, aimReference.aimID, aimFile, sessionID);

		} else {
			statusCode = HandlerUtil.badRequestJSONResponse(BAD_PUT_MESSAGE, responseStream, log);
		}
		return statusCode;
	}
	
	private File getUploadedAIMFile(HttpServletRequest httpRequest)
	{
		String annotationsUploadDirPath = EPADConfig.getEPADWebServerAnnotationsUploadDir();
		String tempXMLFileName = "temp-" + System.currentTimeMillis() + ".xml";
		File aimFile = new File(annotationsUploadDirPath + tempXMLFileName);
		try
		{
			// opens input stream of the request for reading data
			InputStream inputStream = httpRequest.getInputStream();
			
			// opens an output stream for writing file
			FileOutputStream outputStream = new FileOutputStream(aimFile);
			
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			log.info("Receiving data...");
			int len = 0;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				len = len + bytesRead;
				outputStream.write(buffer, 0, bytesRead);
			}
			
			log.info("Data received, len:" + len);
			outputStream.close();
			inputStream.close();
			log.info("Created AIMFile:" + aimFile.getAbsolutePath());
			if (len > 0)
			{
				log.info("PUT Data:" + readFile(aimFile));
			}
			return aimFile;
		}
		catch (Exception x)
		{
			log.warning("Error receiving Annotations file", x);
		}
		return null;
	}
	
    private String readFile(File aimFile) throws Exception
    {
        BufferedReader in = new BufferedReader(new FileReader(aimFile));
        StringBuilder sb = new StringBuilder();
        String line;
        try
        {
            while ((line = in.readLine()) != null)
            {
            	sb.append(line + "\n");
            }
        }
        finally
        {
            in.close();
        }
        return sb.toString();
    }

	private int handlePost(HttpServletRequest httpRequest, PrintWriter responseStream, String username)
	{
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;

		log.info("POST Request from client:" + pathInfo + " user:" + username);
		if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME_LIST, pathInfo)) {
			ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.FRAME_LIST, pathInfo);
			String type = httpRequest.getParameter("type");
			if ("new".equalsIgnoreCase(type))
			{
				boolean status = DSOUtil.handleCreateDSO(imageReference.projectID, imageReference.subjectID, imageReference.studyUID,
						imageReference.seriesUID, httpRequest, responseStream);
				if (status)
					statusCode = HttpServletResponse.SC_CREATED;
				else
					statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			}
			else if (!DSOUtil.handleDSOFramesEdit(imageReference.projectID, imageReference.subjectID, imageReference.studyUID,
					imageReference.seriesUID, imageReference.imageUID, httpRequest, responseStream))
			{
					statusCode = HttpServletResponse.SC_CREATED;
			}
			else
			{
				log.info("Error return from handleDSOFramesEdit");
				statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			}
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

		log.info("DELETE Request from client:" + pathInfo + " user:" + username + " sessionID:" + sessionID);
		boolean deleteDSO = "true".equalsIgnoreCase(httpRequest.getParameter("deleteDSO"));
		boolean deleteAims = "true".equalsIgnoreCase(httpRequest.getParameter("deleteAims"));
		if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT, pathInfo)) {
			ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT, pathInfo);
			statusCode = epadOperations.projectDelete(projectReference.projectID, sessionID, username);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT, pathInfo)) {
			SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT, pathInfo);
			statusCode = epadOperations.subjectDelete(subjectReference, sessionID, username);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY, pathInfo)) {
			StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY, pathInfo);
			String err = epadOperations.studyDelete(studyReference, sessionID, deleteAims, username);
			if (err == null || err.trim().length() == 0)
			{
				statusCode = HttpServletResponse.SC_OK;
			}
			else
			{
				responseStream.append(new EPADError(err).toJSON());
				statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			}

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES, pathInfo)) {
			SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES, pathInfo);
			String err = epadOperations.seriesDelete(seriesReference, sessionID, deleteAims, username);
			if (err == null || err.trim().length() == 0)
			{
				statusCode = HttpServletResponse.SC_OK;
			}
			else
			{
				responseStream.append(new EPADError(err).toJSON());
				statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			}

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_AIM, pathInfo)) {
			ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
			statusCode = epadOperations.projectAIMDelete(projectReference, aimReference.aimID, sessionID, deleteDSO, username);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo)) {
			SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo);
			statusCode = epadOperations.subjectAIMDelete(subjectReference, aimReference.aimID, sessionID, deleteDSO, username);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_AIM, pathInfo)) {
			StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.STUDY_AIM, pathInfo);
			statusCode = epadOperations.studyAIMDelete(studyReference, aimReference.aimID, sessionID, deleteDSO, username);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_AIM, pathInfo)) {
			SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
			statusCode = epadOperations.seriesAIMDelete(seriesReference, aimReference.aimID, sessionID, deleteDSO, username);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE_AIM, pathInfo)) {
			ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.IMAGE_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.IMAGE_AIM, pathInfo);
			statusCode = epadOperations.imageAIMDelete(imageReference, aimReference.aimID, sessionID, deleteDSO, username);

		} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME_AIM, pathInfo)) {
			FrameReference frameReference = FrameReference.extract(ProjectsRouteTemplates.FRAME_AIM, pathInfo);
			AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.FRAME_AIM, pathInfo);
			statusCode = epadOperations.frameAIMDelete(frameReference, aimReference.aimID, sessionID, deleteDSO, username);

		} else if (HandlerUtil.matchesTemplate(AimsRouteTemplates.AIM, pathInfo)) {
			AIMReference aimReference = AIMReference.extract(AimsRouteTemplates.AIM, pathInfo);
			statusCode = epadOperations.aimDelete(aimReference.aimID, sessionID, deleteDSO, username);
		} else {
			statusCode = HandlerUtil.badRequestJSONResponse(BAD_DELETE_MESSAGE, responseStream, log);
		}
		return statusCode;
	}
	
	private boolean returnSummary(HttpServletRequest httpRequest)
	{
		String summary = httpRequest.getParameter("format");
		if (summary != null && summary.trim().equalsIgnoreCase("summary"))
			return true;
		else
			return false;
	}
	
	private int getInt(String value)
	{
		try {
			return new Integer(value.trim()).intValue();
		} catch (Exception x) {
			return 0;
		}
	}

}
