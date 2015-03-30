package edu.stanford.epad.epadws.handlers.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADFile;
import edu.stanford.epad.dtos.EPADFileList;
import edu.stanford.epad.dtos.EPADFrame;
import edu.stanford.epad.dtos.EPADFrameList;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADMessage;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.dtos.EPADUser;
import edu.stanford.epad.dtos.EPADUserList;
import edu.stanford.epad.dtos.EPADWorklist;
import edu.stanford.epad.dtos.EPADWorklistList;
import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.dtos.RemotePACEntity;
import edu.stanford.epad.dtos.RemotePACEntityList;
import edu.stanford.epad.dtos.RemotePACList;
import edu.stanford.epad.dtos.RemotePACQueryConfigList;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.models.RemotePACQuery;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.security.EPADSession;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.service.DefaultWorkListOperations;
import edu.stanford.epad.epadws.service.EpadWorkListOperations;
import edu.stanford.epad.epadws.service.RemotePACService;
import edu.stanford.epad.epadws.service.SessionService;
import edu.stanford.epad.epadws.service.UserProjectService;

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

	/*
	 * Main class for handling rest calls using the epad v2 api.
	 * 
	 * Note: These long if/then/else statements looks terrible, they need to be replaced by something like jersey with annotations
	 * But there seems to be some problem using jersey with embedded jetty and multiple handlers - still need to solve that
	 * 
	 */
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

			String sessionID = SessionService.getJSessionIDFromRequest(httpRequest);
			boolean releaseSession = false;
			if (sessionID == null)
			{
				sessionID = SessionService.getJSessionIDFromRequest(httpRequest, true);
				releaseSession = true;
			}
			if (SessionService.hasValidSessionID(sessionID)) {
				String username = httpRequest.getParameter("username");
				if (EPADConfig.UseEPADUsersProjects) {
					String sessionUser = EPADSessionOperations.getSessionUser(sessionID);
					if (username != null && !username.equals(sessionUser))
					{
						throw new Exception("Incorrect username in request:" + username + " session belongs to " + sessionUser);
					}
					else
						username = sessionUser;
				}
				if (username != null) {
					if ("GET".equalsIgnoreCase(method)) {
						statusCode = handleGet(httpRequest, httpResponse, responseStream, username, sessionID);
					} else if ("DELETE".equalsIgnoreCase(method)) {
						statusCode = handleDelete(httpRequest, responseStream, username, sessionID);
					} else if ("PUT".equalsIgnoreCase(method)) {
						statusCode = handlePut(httpRequest, httpResponse, responseStream, username, sessionID);
					} else if ("POST".equalsIgnoreCase(method)) {
						statusCode = handlePost(httpRequest, responseStream, username, sessionID);
					} else {
						statusCode = HandlerUtil.badRequestJSONResponse(FORBIDDEN_MESSAGE, responseStream, log);
					}
				} else
					statusCode = HandlerUtil.badRequestJSONResponse(NO_USERNAME_MESSAGE, responseStream, log);
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
			if (releaseSession) {
				EPADSessionOperations.invalidateSessionID(sessionID);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warning("Error in handle request:", e);
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, e, responseStream, log);
		}
		log.info("Status returned to client:" + statusCode);
		httpResponse.setStatus(statusCode);
	}

	private int handleGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse, PrintWriter responseStream, String username, String sessionID)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
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
				boolean annotationCount = false;
				if ("true".equalsIgnoreCase(httpRequest.getParameter("annotationCount")))
					annotationCount = true;
				EPADProjectList projectList = epadOperations.getProjectDescriptions(username, sessionID, searchFilter, annotationCount);
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
				} else {
					log.info("Subject " + subjectReference.subjectID + " not found");
					statusCode = HttpServletResponse.SC_NOT_FOUND;
				}

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.STUDY_LIST, pathInfo);
				if (subjectReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADStudyList studyList = epadOperations.getStudyDescriptions(subjectReference, username, sessionID,
						searchFilter);
				log.info("Returning " + studyList.ResultSet.totalRecords + " studies");
				responseStream.append(studyList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY, pathInfo);
				EPADStudy study = epadOperations.getStudyDescription(studyReference, username, sessionID);
				if (study != null) {
					responseStream.append(study.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else {
					log.info("Study " + studyReference.studyUID + " not found");
					statusCode = HttpServletResponse.SC_NOT_FOUND;
				}
				
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
				} else {
					log.info("Series " + seriesReference.seriesUID + " not found");
					statusCode = HttpServletResponse.SC_NOT_FOUND;
				}
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIESFILE_LIST, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.SERIESFILE_LIST, pathInfo);
				EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID,
						searchFilter, true);
				EPADFileList fileList = epadOperations.getFileDescriptions(studyReference, username, sessionID, searchFilter);
				List objects = new ArrayList();
				objects.add(seriesList);
				objects.add(fileList);
				responseStream.append(new Gson().toJson(objects));
//				List<EPADFile> files = epadOperations.getEPADFiles(studyReference, username, sessionID, searchFilter);
//				for (EPADSeries series: seriesList.ResultSet.Result)
//				{
//					EPADFile efile = new EPADFile(studyReference.projectID, studyReference.subjectID, series.patientID, studyReference.studyUID, series.seriesUID,
//							series.seriesUID, 0, FileType.SERIES.getName(), new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(series.seriesDate), "");
//					files.add(0, efile);
//				}
//				responseStream.append(new EPADFileList(files).toJSON());
				statusCode = HttpServletResponse.SC_OK;


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
				} else {
					log.info("Image " + imageReference.imageUID + " not found");
					statusCode = HttpServletResponse.SC_NOT_FOUND;
				}
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGEFILE_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.IMAGEFILE_LIST, pathInfo);
				EPADImageList imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, searchFilter);
				EPADFileList fileList = epadOperations.getFileDescriptions(seriesReference, username, sessionID, searchFilter);
				List objects = new ArrayList();
				objects.add(imageList);
				objects.add(fileList);
				responseStream.append(new Gson().toJson(objects));
//				List<EPADFile> files = epadOperations.getEPADFiles(seriesReference, username, sessionID, searchFilter);
//				for (EPADImage image: imageList.ResultSet.Result)
//				{
//					EPADFile efile = new EPADFile(seriesReference.projectID, seriesReference.subjectID, image.patientID, seriesReference.studyUID, seriesReference.seriesUID,
//							image.imageUID, 0, FileType.DICOM.getName(), new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(image.imageDate), image.lossyImage);
//					files.add(0, efile);
//				}
//				responseStream.append(new EPADFileList(files).toJSON());
				statusCode = HttpServletResponse.SC_OK;

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
				} else {
					log.info("Image " + frameReference.imageUID + " frame " + frameReference.frameNumber + " not found");
					statusCode = HttpServletResponse.SC_NOT_FOUND;
				}
				
			} else if (HandlerUtil.matchesTemplate(SubjectsRouteTemplates.SUBJECT, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(SubjectsRouteTemplates.SUBJECT, pathInfo);
				EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
				if (subject != null) {
					responseStream.append(subject.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else {
					log.info("Subject " + subjectReference.subjectID + " not found");
					statusCode = HttpServletResponse.SC_NOT_FOUND;
				}

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
				} else {
					log.info("Image " + imageReference.imageUID + " not found");
					statusCode = HttpServletResponse.SC_NOT_FOUND;
				}
				
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
					if (AIMSearchType.JSON_QUERY.equals(aimSearchType) || AIMSearchType.AIM_QUERY.equals(aimSearchType))
						aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, aimSearchType, searchValue, username, sessionID);
					else
						aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					long starttime2 = System.currentTimeMillis();
					responseStream.append(aims.toJSON());
					long resptime = System.currentTimeMillis();
					log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
				}
				else if (returnJson(httpRequest))
				{
					if (AIMSearchType.JSON_QUERY.equals(aimSearchType) || AIMSearchType.AIM_QUERY.equals(aimSearchType))
						AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, aimSearchType, searchValue, username, sessionID, true);					
					else
						AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					if (AIMSearchType.AIM_QUERY.equals(aimSearchType) || AIMSearchType.JSON_QUERY.equals(aimSearchType))
						AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, aimSearchType, searchValue, username, sessionID, false);					
					else
						AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_AIM, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
				EPADAIM aim = epadOperations
						.getProjectAIMDescription(projectReference, aimReference.aimID, username, sessionID);
				if (!UserProjectService.isCollaborator(sessionID, username, aim.projectID))
					username = null;
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, projectReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo);
				if (subjectReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADAIMList aims = epadOperations.getSubjectAIMDescriptions(subjectReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
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
					AIMUtil.queryAIMImageAnnotations(responseStream, subjectReference.projectID, AIMSearchType.ANNOTATION_UID,
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
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
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
					AIMUtil.queryAIMImageAnnotations(responseStream, studyReference.projectID, AIMSearchType.ANNOTATION_UID,
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
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
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
					AIMUtil.queryAIMImageAnnotations(responseStream, seriesReference.projectID, AIMSearchType.ANNOTATION_UID,
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
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
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
					AIMUtil.queryAIMImageAnnotations(responseStream, imageReference.projectID, AIMSearchType.ANNOTATION_UID,
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
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
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
					AIMUtil.queryAIMImageAnnotations(responseStream, frameReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_LIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_LIST, pathInfo);
				EPADUserList users = epadOperations.getUserDescriptions(username, projectReference, sessionID);
				responseStream.append(users.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.WORKLISTS, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.WORKLISTS, pathInfo);
				EPADWorklistList wll = epadOperations.getWorkLists(projectReference);
				responseStream.append(wll.toJSON());
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.WORKLIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.WORKLIST, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_WORKLIST, pathInfo);
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "workListID");
				EPADWorklist wl = epadOperations.getWorkListByID(projectReference, workListID);
				responseStream.append(wl.toJSON());
				statusCode = HttpServletResponse.SC_OK;
			
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_WORKLISTS, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_WORKLISTS, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_WORKLISTS, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				EPADWorklist wl = epadOperations.getWorkList(projectReference, reader);
				responseStream.append(wl.toJSON());
				statusCode = HttpServletResponse.SC_OK;


			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.STUDY_AIM_LIST, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(StudiesRouteTemplates.STUDY_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getStudyAIMDescriptions(studyReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
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
					AIMUtil.queryAIMImageAnnotations(responseStream, studyReference.projectID, AIMSearchType.ANNOTATION_UID,
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
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
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
					AIMUtil.queryAIMImageAnnotations(responseStream, seriesReference.projectID, AIMSearchType.ANNOTATION_UID,
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
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
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
					AIMUtil.queryAIMImageAnnotations(responseStream, imageReference.projectID, AIMSearchType.ANNOTATION_UID,
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
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
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
					AIMUtil.queryAIMImageAnnotations(responseStream, frameReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(SubjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(SubjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo);
				if (subjectReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADAIMList aims = epadOperations.getSubjectAIMDescriptions(subjectReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
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
					AIMUtil.queryAIMImageAnnotations(responseStream, subjectReference.projectID, AIMSearchType.ANNOTATION_UID,
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
					if (AIMSearchType.AIM_QUERY.equals(aimSearchType) || AIMSearchType.JSON_QUERY.equals(aimSearchType))
						aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, aimSearchType, searchValue, username, sessionID);
					else
						aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					if (AIMSearchType.JSON_QUERY.equals(aimSearchType))
						AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, aimSearchType, searchValue, username, sessionID, true);					
					else
						AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					if (AIMSearchType.AIM_QUERY.equals(aimSearchType))
						AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, aimSearchType, searchValue, username, sessionID, false);					
					else
						AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
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
					AIMUtil.queryAIMImageAnnotations(responseStream, null, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_LIST, pathInfo)) {
				EPADUserList userlist = epadOperations.getUserDescriptions(username, sessionID);
				responseStream.append(userlist.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER, pathInfo);
				String return_username = HandlerUtil.getTemplateParameter(templateMap, "username");
				EPADUser user = epadOperations.getUserDescription(username, return_username, sessionID);
				responseStream.append(user.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_SESSIONS, pathInfo)) {
				Collection<EPADSession> sessions = epadOperations.getCurrentSessions(username);
				responseStream.append(new Gson().toJson(sessions));
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_FILE_LIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_FILE_LIST, pathInfo);
				EPADFileList files = epadOperations.getFileDescriptions(projectReference, username, sessionID, searchFilter);
				responseStream.append(files.toJSON());
				statusCode = HttpServletResponse.SC_OK;
						
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_FILE_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_FILE_LIST, pathInfo);
				if (subjectReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADFileList files = epadOperations.getFileDescriptions(subjectReference, username, sessionID, searchFilter);
				responseStream.append(files.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_FILE_LIST, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_FILE_LIST, pathInfo);
				EPADFileList files = epadOperations.getFileDescriptions(studyReference, username, sessionID, searchFilter);
				responseStream.append(files.toJSON());
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_FILE_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_FILE_LIST, pathInfo);
				EPADFileList files = epadOperations.getFileDescriptions(seriesReference, username, sessionID, searchFilter);
				responseStream.append(files.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_FILE, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_FILE, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.PROJECT_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				EPADFile file = epadOperations.getFileDescription(projectReference, filename, username, sessionID);
				if (returnSummary(httpRequest)){
					responseStream.append(file.toJSON());
				} else {
					EPADFileUtils.downloadFile(httpRequest, httpResponse, new File(EPADConfig.getEPADWebServerResourcesDir()+file.path), file.fileName); 					
				}					
				statusCode = HttpServletResponse.SC_OK;
						
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_FILE, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_FILE, pathInfo);
				if (subjectReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.SUBJECT_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				EPADFile file = epadOperations.getFileDescription(subjectReference, filename, username, sessionID);
				if (returnSummary(httpRequest)){
					responseStream.append(file.toJSON());
				} else {
					EPADFileUtils.downloadFile(httpRequest, httpResponse, new File(EPADConfig.getEPADWebServerResourcesDir()+file.path), file.fileName); 					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_FILE, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_FILE, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.STUDY_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				EPADFile file = epadOperations.getFileDescription(studyReference, filename, username, sessionID);
				if (returnSummary(httpRequest)){
					responseStream.append(file.toJSON());
				} else {
					EPADFileUtils.downloadFile(httpRequest, httpResponse, new File(EPADConfig.getEPADWebServerResourcesDir()+file.path), file.fileName); 					
				}
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_FILE, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_FILE, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.SERIES_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				EPADFile file = epadOperations.getFileDescription(seriesReference, filename, username, sessionID);
				if (returnSummary(httpRequest)){
					responseStream.append(file.toJSON());
				} else {
					EPADFileUtils.downloadFile(httpRequest, httpResponse, new File(EPADConfig.getEPADWebServerResourcesDir()+file.path), file.fileName); 					
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PACS_LIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PACS_LIST, pathInfo);
				List<RemotePAC> pacs = RemotePACService.getInstance().getRemotePACs();
				RemotePACList pacList = new RemotePACList();
				for (RemotePAC pac: pacs)
					pacList.addRemotePAC(pac);
				responseStream.append(pacList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacid);
				if (pac != null)
				{
					responseStream.append(new Gson().toJson(pac));
					statusCode = HttpServletResponse.SC_OK;
				}
				else
					statusCode = HttpServletResponse.SC_NOT_FOUND;

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_ENTITY_LIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_ENTITY_LIST, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				String patientNameFilter = httpRequest.getParameter("patientNameFilter");
				if (patientNameFilter == null) patientNameFilter = "";
				String patientIDFilter = httpRequest.getParameter("patientIDFilter");
				if (patientIDFilter == null) patientIDFilter = "";
				String studyIDFilter = httpRequest.getParameter("studyIDFilter");
				if (studyIDFilter == null) studyIDFilter = "";
				String studyDateFilter = httpRequest.getParameter("studyDateFilter");
				if (studyDateFilter == null) studyDateFilter = "";
				String[] tagGroup = httpRequest.getParameterValues("tagGroup");
				String[] tagElement = httpRequest.getParameterValues("tagElement");
				String[] tagValue = httpRequest.getParameterValues("tagValue");
				String[] tagType = httpRequest.getParameterValues("tagType");
				boolean studiesOnly = !"true".equalsIgnoreCase(httpRequest.getParameter("series"));
				if (tagGroup != null && httpRequest.getParameter("series") == null)
					studiesOnly = false;
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacid);
				if (pac != null)
				{
					List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, patientNameFilter, patientIDFilter, 
							studyIDFilter, studyDateFilter, 
							tagGroup, tagElement, tagValue, tagType, false, studiesOnly);
					RemotePACEntityList entityList = new RemotePACEntityList();
					for (RemotePACEntity entity: entities)
						entityList.addRemotePACEntity(entity);
					responseStream.append(entityList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				}
				else
					statusCode = HttpServletResponse.SC_NOT_FOUND;

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_SUBJECT_LIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_SUBJECT_LIST, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				String patientNameFilter = httpRequest.getParameter("patientNameFilter");
				if (patientNameFilter == null) patientNameFilter = "";
				String patientIDFilter = httpRequest.getParameter("patientIDFilter");
				if (patientIDFilter == null) patientIDFilter = "";
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacid);
				if (pac != null)
				{
					List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, patientNameFilter, patientIDFilter, "", "", true, true);
					RemotePACEntityList entityList = new RemotePACEntityList();
					for (RemotePACEntity entity: entities)
						entityList.addRemotePACEntity(entity);
					responseStream.append(entityList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				}
				else
					statusCode = HttpServletResponse.SC_NOT_FOUND;

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_STUDY_LIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_STUDY_LIST, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				String subjectid = HandlerUtil.getTemplateParameter(templateMap, "subjectid");
				String studyDateFilter = httpRequest.getParameter("studyDateFilter");
				if (studyDateFilter == null) studyDateFilter = "";
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacid);
				if (pac != null)
				{
					List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, "", subjectid, "", studyDateFilter, false, true);
					RemotePACEntityList entityList = new RemotePACEntityList();
					for (RemotePACEntity entity: entities)
						entityList.addRemotePACEntity(entity);
					responseStream.append(entityList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				}
				else
					statusCode = HttpServletResponse.SC_NOT_FOUND;
				
			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_SERIES_LIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_SERIES_LIST, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				String subjectid = HandlerUtil.getTemplateParameter(templateMap, "subjectid");
				String studyid = HandlerUtil.getTemplateParameter(templateMap, "studyid");
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacid);
				if (pac != null)
				{
					List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, "", subjectid, studyid, "", false, false);
					RemotePACEntityList entityList = new RemotePACEntityList();
					for (RemotePACEntity entity: entities)
						entityList.addRemotePACEntity(entity);
					responseStream.append(entityList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				}
				else
					statusCode = HttpServletResponse.SC_NOT_FOUND;

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_ENTITY, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_ENTITY, pathInfo);
				String pacID = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				String entityID = HandlerUtil.getTemplateParameter(templateMap, "entityid");
				String projectID = httpRequest.getParameter("projectID");
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
				if (pac != null)
				{
					RemotePACService.getInstance().retrieveRemoteData(pac, entityID, projectID, username, sessionID);
					statusCode = HttpServletResponse.SC_OK;
				}
				else
					statusCode = HttpServletResponse.SC_NOT_FOUND;

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_QUERY_LIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_QUERY_LIST, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				RemotePACService rps = RemotePACService.getInstance();
				List<RemotePACQuery> remoteQueries = rps.getRemotePACQueries(pacid);
				RemotePACQueryConfigList queryList = new RemotePACQueryConfigList();
				for (RemotePACQuery query: remoteQueries)
				{
					queryList.addRemotePACQueryConfig(rps.getConfig(query));
				}
				responseStream.append(queryList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_QUERY, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_QUERY, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				String subject = HandlerUtil.getTemplateParameter(templateMap, "subjectid");
				RemotePACQuery remoteQuery = RemotePACService.getInstance().getRemotePACQuery(pacid, subject);
				if (remoteQuery != null)
				{
					responseStream.append(new Gson().toJson( RemotePACService.getInstance().getConfig(remoteQuery)));
					statusCode = HttpServletResponse.SC_OK;
				}
				else
					statusCode = HttpServletResponse.SC_NOT_FOUND;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.TEMPLATE_LIST, pathInfo)) {
				ProjectReference reference = ProjectReference.extract(ProjectsRouteTemplates.TEMPLATE_LIST, pathInfo);
				EPADFileList templates = epadOperations.getTemplateDescriptions(reference.projectID, username, sessionID);
				responseStream.append(templates.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(TemplatesRouteTemplates.TEMPLATE_LIST, pathInfo)) {
				EPADFileList templates = epadOperations.getTemplateDescriptions(username, sessionID);
				responseStream.append(templates.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else
				statusCode = HandlerUtil.badRequestJSONResponse(BAD_GET_MESSAGE + ":" + pathInfo, responseStream, log);
		} catch (Throwable t) {
			t.printStackTrace();
			log.warning("Error handleget:", t);
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
		}
		return statusCode;
	}

	private int handlePut(HttpServletRequest httpRequest, HttpServletResponse httpResponse, PrintWriter responseStream,
			String username, String sessionID)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EpadWorkListOperations worklistOperations = DefaultWorkListOperations.getInstance();
		String pathInfo = httpRequest.getPathInfo();
		int statusCode = HttpServletResponse.SC_OK;
		String status = null;
	    String requestContentType = httpRequest.getContentType();
		log.info("PUT Request from client:" + pathInfo + " user:" + username + " sessionID:" + sessionID + " contentType:" + requestContentType);
		File uploadedFile = null;
		try
		{
			Map<String, Object> paramData = null;
			// Note: This needs to be done, before anything else otherwise upload won't work
			if (requestContentType == null || !requestContentType.startsWith("multipart/form-data"))
			{
				uploadedFile = this.getUploadedFile(httpRequest);
			}
			else
			{
				paramData = parsePostedData(httpRequest, responseStream);
				for (String param: paramData.keySet())
				{
					if (paramData.get(param) instanceof File)
					{
						uploadedFile = (File) paramData.get(param);
						break;
					}
				}
			}
			if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT, pathInfo);
				String projectName = httpRequest.getParameter("projectName");
				String projectDescription = httpRequest.getParameter("projectDescription");
				EPADProject project = epadOperations.getProjectDescription(projectReference, username, sessionID);
				if (project != null) {
					statusCode = epadOperations.updateProject(username, projectReference, projectName, projectDescription, sessionID);
				} else {
					statusCode = epadOperations.createProject(username, projectReference, projectName, projectDescription, sessionID);
				}
				project = epadOperations.getProjectDescription(projectReference, username, sessionID);
				responseStream.append(project.toJSON());
				if (uploadedFile != null && false) {
					log.info("Saving uploaded file:" + uploadedFile.getName());
					String description = httpRequest.getParameter("description");
					String fileType = httpRequest.getParameter("fileType");
					statusCode = epadOperations.createFile(username, projectReference, uploadedFile, description, fileType, sessionID);					
				}
					
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT, pathInfo);
				String subjectName = httpRequest.getParameter("subjectName");
				String gender = httpRequest.getParameter("gender");
				String dob = httpRequest.getParameter("dob");
				EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
				if (subject != null) {
					statusCode = epadOperations.updateSubject(username, subjectReference, subjectName, getDate(dob), gender, sessionID);
				} else {
					statusCode = epadOperations.createSubject(username, subjectReference, subjectName, getDate(dob), gender, sessionID);
				}
				if (uploadedFile != null && false) {
					String description = httpRequest.getParameter("description");
					String fileType = httpRequest.getParameter("fileType");
					statusCode = epadOperations.createFile(username, subjectReference, uploadedFile, description, fileType, sessionID);					
				}

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_STATUS, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_STATUS, pathInfo);
				status = epadOperations.setSubjectStatus(subjectReference, sessionID, username);
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo);
				status = epadOperations.createSubjectAIM(username, subjectReference, aimReference.aimID, uploadedFile, sessionID);
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY, pathInfo);
				String description = httpRequest.getParameter("description");
				String studyDate = httpRequest.getParameter("studyDate");
				statusCode = epadOperations.createStudy(username, studyReference, description, getDate(studyDate), sessionID);
				if (uploadedFile != null && false) {
					String fileType = httpRequest.getParameter("fileType");
					statusCode = epadOperations.createFile(username, studyReference, uploadedFile, description, fileType, sessionID);					
				}
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_AIM, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.STUDY_AIM, pathInfo);
				status = epadOperations.createStudyAIM(username, studyReference, aimReference.aimID, uploadedFile, sessionID);
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES, pathInfo);
				String description = httpRequest.getParameter("description");
				String seriesDate = httpRequest.getParameter("seriesDate");
				EPADSeries series  = epadOperations.createSeries(username, seriesReference, description, getDate(seriesDate), sessionID);
				if (uploadedFile != null && false) {
					String fileType = httpRequest.getParameter("fileType");
					statusCode = epadOperations.createFile(username, seriesReference, uploadedFile, description, fileType, sessionID);					
				}
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_AIM, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
				status = epadOperations.createSeriesAIM(username, seriesReference, aimReference.aimID, uploadedFile, sessionID);
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE, pathInfo)) {
				statusCode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
				httpResponse.addHeader("Allow", "GET, DELETE");
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE_AIM, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.IMAGE_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.IMAGE_AIM, pathInfo);
				log.info("Images AIM PUT");
				status = epadOperations.createImageAIM(username, imageReference, aimReference.aimID, uploadedFile, sessionID);
			
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_AIM, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
				log.info("Projects AIM PUT");
				status = epadOperations.createProjectAIM(username, projectReference, aimReference.aimID, uploadedFile, sessionID);
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_AIM_LIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_AIM_LIST, pathInfo);
				AIMSearchType aimSearchType = AIMUtil.getAIMSearchType(httpRequest);
				String searchValue = aimSearchType != null ? httpRequest.getParameter(aimSearchType.getName()) : null;
				String templateName = httpRequest.getParameter("templateName");
				log.info("PUT request for AIMs from user " + username + "; query type is " + aimSearchType + ", value "
						+ searchValue + ", project " + projectReference.projectID);
				if (aimSearchType.equals(AIMSearchType.ANNOTATION_UID)) {
					String[] aimIDs = searchValue.split(",");
					AIMUtil.runPlugIn(aimIDs, templateName, projectReference.projectID, sessionID);
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME, pathInfo)) {
				statusCode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
				httpResponse.addHeader("Allow", "GET, DELETE");
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME_AIM, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(ProjectsRouteTemplates.FRAME_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.FRAME_AIM, pathInfo);
				status = epadOperations.createFrameAIM(username, frameReference, aimReference.aimID, uploadedFile, sessionID);
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER, pathInfo);
				String add_username = HandlerUtil.getTemplateParameter(templateMap, "username");
				String role = httpRequest.getParameter("role");
				epadOperations.addUserToProject(username, projectReference, add_username, role, sessionID);
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_WORKLIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_WORKLIST, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_WORKLIST, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "workListID");
				String description = httpRequest.getParameter("description");
				String dueDate = httpRequest.getParameter("dueDate");
				worklistOperations.createWorkList(username, reader, projectReference.projectID, workListID, description, null, getDate(dueDate));
				statusCode = HttpServletResponse.SC_OK;
			
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_SUBJECT, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_SUBJECT, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_SUBJECT, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subjectID");
				String wlstatus = httpRequest.getParameter("status");
				boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				WorkList wl = worklistOperations.getWorkListForUserByProject(username, projectReference.projectID);
				if (wl == null)
					throw new Exception("Worklist not found for user " + reader + " and project " + projectReference.projectID);
				worklistOperations.setWorkListSubjectStatus(reader, wl.getWorkListID(), subjectID, wlstatus, started, completed);
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_STUDY, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_STUDY, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_STUDY, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String studyUID = HandlerUtil.getTemplateParameter(templateMap, "studyUID");
				String wlstatus = httpRequest.getParameter("status");
				boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				WorkList wl = worklistOperations.getWorkListForUserByProject(username, projectReference.projectID);
				if (wl == null)
					throw new Exception("Worklist not found for user " + reader + " and project " + projectReference.projectID);
				worklistOperations.setWorkListStudyStatus(reader, wl.getWorkListID(), studyUID, wlstatus, started, completed);
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER, pathInfo);
				String target_username = HandlerUtil.getTemplateParameter(templateMap, "username");
				String firstname = httpRequest.getParameter("firstname");
				String lastname = httpRequest.getParameter("lastname");
				String email = httpRequest.getParameter("email");
				String password = httpRequest.getParameter("password");
				String oldpassword = httpRequest.getParameter("oldpassword");
				String[] addPermissions = httpRequest.getParameterValues("addPermission");
				String[] removePermissions = httpRequest.getParameterValues("removePermission");
				epadOperations.createOrModifyUser(username, target_username, firstname, lastname, email, password, oldpassword, addPermissions, removePermissions);
				String enable = httpRequest.getParameter("enable");
				if ("true".equalsIgnoreCase(enable))
					epadOperations.enableUser(username, target_username);
				else if ("false".equalsIgnoreCase(enable))
					epadOperations.disableUser(username, target_username);
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_WORKLIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_WORKLIST, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				String wlstatus = httpRequest.getParameter("status");
				boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				worklistOperations.setWorkListStatus(reader, workListID, wlstatus, started, completed);
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_SUBJECT, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_SUBJECT, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subjectID");
				String wlstatus = httpRequest.getParameter("status");
				boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				Set<WorkList> wls = worklistOperations.getWorkListsForUserBySubject(username, subjectID);
				for (WorkList wl: wls)
					worklistOperations.setWorkListSubjectStatus(reader, wl.getWorkListID(), subjectID, wlstatus, started, completed);
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_STUDY, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_STUDY, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String studyUID = HandlerUtil.getTemplateParameter(templateMap, "studyUID");
				String wlstatus = httpRequest.getParameter("status");
				boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				Set<WorkList> wls = worklistOperations.getWorkListsForUserByStudy(username, studyUID);
				for (WorkList wl: wls)
					worklistOperations.setWorkListSubjectStatus(reader, wl.getWorkListID(), studyUID, wlstatus, started, completed);
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacid);
				String aeTitle = httpRequest.getParameter("aeTitle");
				String hostname = httpRequest.getParameter("hostname");
				int port = getInt(httpRequest.getParameter("port"));
				String primaryDeviceType = httpRequest.getParameter("deviceType");
				String queryModel = httpRequest.getParameter("queryModel");
				if (aeTitle == null && pac == null)
					throw new Exception("Missing aeTitle parameter in PAC Put");
				else if (aeTitle == null)
					aeTitle = pac.aeTitle;
				if (hostname == null && pac == null)
					throw new Exception("Missing hostname parameter in PAC Put");
				else if (hostname == null)
					hostname = pac.hostname;
				if (port == 0 && pac == null)
					throw new Exception("Missing port parameter in PAC Put");
				else if (port == 0)
					port = pac.port;
				if (primaryDeviceType == null && pac == null)
					primaryDeviceType = "WSD";
				else if (primaryDeviceType == null)
					primaryDeviceType = pac.primaryDeviceType;
				if (pac == null)
				{
					pac = new RemotePAC(pacid, aeTitle, hostname, port, queryModel, primaryDeviceType);
					RemotePACService.getInstance().addRemotePAC(username, pac);
				}
				else
				{
					pac = new RemotePAC(pacid, aeTitle, hostname, port, queryModel, primaryDeviceType);
					RemotePACService.getInstance().modifyRemotePAC(username, pac);
				}
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_QUERY, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_QUERY, pathInfo);
				String pacID = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				if (pacID == null)
					throw new Exception("Missing Pac ID parameter");
				String subjectUID = HandlerUtil.getTemplateParameter(templateMap, "subjectid");
				if (subjectUID == null)
					throw new Exception("Missing Patient ID parameter");
				String projectID = httpRequest.getParameter("projectID");
				String subjectName = httpRequest.getParameter("subjectName");
				if (subjectName == null)
					subjectName = httpRequest.getParameter("patientName");
				String modality = httpRequest.getParameter("modality");
				String studyDate = httpRequest.getParameter("studyDate");
				String period = httpRequest.getParameter("period");
				String enable = httpRequest.getParameter("enable");
				if (enable != null)
				{
					RemotePACQuery query = RemotePACService.getInstance().getRemotePACQuery(pacID, subjectUID);
					if ("true".equalsIgnoreCase(enable) && query != null)
					{
						RemotePACService.getInstance().enableRemotePACQuery(username, pacID, subjectUID);
					}
					else if ("false".equalsIgnoreCase(enable))
					{	
						if (query == null)
							throw new Exception("Remote PAC and Patient not configured for periodic query");
						RemotePACService.getInstance().disableRemotePACQuery(username, pacID, subjectUID);
					}
				}
				else
				{
					RemotePACService.getInstance().createRemotePACQuery(username, pacID, subjectUID, subjectName, modality, studyDate, period, projectID);
				}
				statusCode = HttpServletResponse.SC_OK;
				
			} else {
				statusCode = HandlerUtil.badRequestJSONResponse(BAD_PUT_MESSAGE + ":" + pathInfo, responseStream, log);
			}
		}
		catch (Exception x) {
			log.warning("Error handling put", x);
			status = x.getMessage();
		}
		finally {
			if (uploadedFile != null)
			{
				if (uploadedFile.getParentFile().exists())
				{
					log.info("Deleting upload directory " + uploadedFile.getParentFile().getAbsolutePath());
					EPADFileUtils.deleteDirectoryAndContents(uploadedFile.getParentFile());
				}
			}
		}
		if (status == null || status.length() == 0)
		{
			return statusCode;
		}
		else
		{
			responseStream.write(new EPADMessage(status).toJSON());
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;				
		}
	}

	private int handlePost(HttpServletRequest httpRequest, PrintWriter responseStream, String username, String sessionID)
	{
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;
		File uploadedFile = null;
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EpadWorkListOperations worklistOperations = DefaultWorkListOperations.getInstance();
	    String requestContentType = httpRequest.getContentType();
		try {
			log.info("POST Request from client:" + pathInfo + " user:" + username + " contentType:" + requestContentType);
			if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME_LIST, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.FRAME_LIST, pathInfo);
				String type = httpRequest.getParameter("type");
				if ("new".equalsIgnoreCase(type))
				{
					boolean errstatus = DSOUtil.handleCreateDSO(imageReference.projectID, imageReference.subjectID, imageReference.studyUID,
							imageReference.seriesUID, httpRequest, responseStream);
					if (!errstatus)
						statusCode = HttpServletResponse.SC_CREATED;
					else
						statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				}
				else { 
					
					boolean errstatus = DSOUtil.handleDSOFramesEdit(imageReference.projectID, imageReference.subjectID, imageReference.studyUID,
						imageReference.seriesUID, imageReference.imageUID, httpRequest, responseStream);
					if (!errstatus)
					{
						statusCode = HttpServletResponse.SC_CREATED;
					}
					else
					{
						log.warning("Error editing DSO");
						statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
					}
				}
			} else {
				Map<String, Object> paramData = null;
				int numberOfFiles = 0;
				if (requestContentType != null && requestContentType.startsWith("multipart/form-data"))
				{
					paramData = parsePostedData(httpRequest, responseStream);
					for (String param: paramData.keySet())
					{
						if (paramData.get(param) instanceof File)
						{
							if (uploadedFile == null)
								uploadedFile = (File) paramData.get(param);
							numberOfFiles++;
						}
					}
				}
				statusCode = HttpServletResponse.SC_BAD_REQUEST;
				if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_FILE_LIST, pathInfo)) {
					ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_FILE_LIST, pathInfo);
					if (requestContentType == null || !requestContentType.startsWith("multipart/form-data"))
						throw new Exception("Invalid Content Type, should be multipart/form-data");
					if (numberOfFiles == 0)
						throw new Exception("No files found in post");
					if (numberOfFiles == 1) {
						String description = httpRequest.getParameter("description");
						if (description == null) description = (String) paramData.get("description");
						String fileType = httpRequest.getParameter("fileType");
						if (fileType == null) description = (String) paramData.get("fileType");
						statusCode = epadOperations.createFile(username, projectReference, uploadedFile, description, fileType, sessionID);					
					} else {
						List<String> descriptions = (List<String>) paramData.get("description_List");
						List<String> fileTypes = (List<String>) paramData.get("fileType_List");
						int i = 0;
						for (String param: paramData.keySet())
						{
							if (paramData.get(param) instanceof File)
							{
								String description = httpRequest.getParameter("description");
								if (descriptions != null && descriptions.size() > i)
									description = descriptions.get(i);
								String fileType = httpRequest.getParameter("fileType");
								if (fileTypes != null && fileTypes.size() > i)
										fileType = fileTypes.get(i);
								statusCode = epadOperations.createFile(username, projectReference, (File)paramData.get(param), description, fileType, sessionID);
								i++;
							}
						}
					}
						
				} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_FILE_LIST, pathInfo)) {
					SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_FILE_LIST, pathInfo);
					if (requestContentType == null || !requestContentType.startsWith("multipart/form-data"))
						throw new Exception("Invalid Content Type, should be multipart/form-data");
					if (numberOfFiles == 0)
						throw new Exception("No files found in post");
					if (numberOfFiles == 1) {
						String description = httpRequest.getParameter("description");
						if (description == null) description = (String) paramData.get("description");
						String fileType = httpRequest.getParameter("fileType");
						if (fileType == null) description = (String) paramData.get("fileType");
						statusCode = epadOperations.createFile(username, subjectReference, uploadedFile, description, fileType, sessionID);					
					} else {
						List<String> descriptions = (List<String>) paramData.get("description_List");
						List<String> fileTypes = (List<String>) paramData.get("fileType_List");
						int i = 0;
						for (String param: paramData.keySet())
						{
							if (paramData.get(param) instanceof File)
							{
								String description = httpRequest.getParameter("description");
								if (descriptions != null && descriptions.size() > i)
									description = descriptions.get(i);
								String fileType = httpRequest.getParameter("fileType");
								if (fileTypes != null && fileTypes.size() > i)
										fileType = fileTypes.get(i);
								statusCode = epadOperations.createFile(username, subjectReference, (File)paramData.get(param), description, fileType, sessionID);
								i++;
							}
						}
					}

				} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_FILE_LIST, pathInfo)) {
					StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_FILE_LIST, pathInfo);
					if (requestContentType == null || !requestContentType.startsWith("multipart/form-data"))
						throw new Exception("Invalid Content Type, should be multipart/form-data");
					if (numberOfFiles == 0)
						throw new Exception("No files found in post");
					if (numberOfFiles == 1) {
						String description = httpRequest.getParameter("description");
						if (description == null) description = (String) paramData.get("description");
						String fileType = httpRequest.getParameter("fileType");
						if (fileType == null) description = (String) paramData.get("fileType");
						statusCode = epadOperations.createFile(username, studyReference, uploadedFile, description, fileType, sessionID);					
					} else {
						List<String> descriptions = (List<String>) paramData.get("description_List");
						List<String> fileTypes = (List<String>) paramData.get("fileType_List");
						int i = 0;
						for (String param: paramData.keySet())
						{
							if (paramData.get(param) instanceof File)
							{
								String description = httpRequest.getParameter("description");
								if (descriptions != null && descriptions.size() > i)
									description = descriptions.get(i);
								String fileType = httpRequest.getParameter("fileType");
								if (fileTypes != null && fileTypes.size() > i)
										fileType = fileTypes.get(i);
								statusCode = epadOperations.createFile(username, studyReference, (File)paramData.get(param), description, fileType, sessionID);
								i++;
							}
						}
					}
		
				} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_FILE_LIST, pathInfo)) {
					SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_FILE_LIST, pathInfo);
					if (requestContentType == null || !requestContentType.startsWith("multipart/form-data"))
						throw new Exception("Invalid Content Type, should be multipart/form-data");
					if (numberOfFiles == 0)
						throw new Exception("No files found in post");
					boolean convertToDicom = "true".equalsIgnoreCase(httpRequest.getParameter("convertToDICOM"));
					if (!convertToDicom) convertToDicom = "true".equalsIgnoreCase((String) paramData.get("convertToDICOM"));
					String modality = httpRequest.getParameter("modality");
					if (modality == null) modality = (String) paramData.get("modality");
					if (numberOfFiles == 1) {
						String description = httpRequest.getParameter("description");
						if (description == null) description = (String) paramData.get("description");
						String fileType = httpRequest.getParameter("fileType");
						if (fileType == null) fileType = (String) paramData.get("fileType");
						String instanceNumber = httpRequest.getParameter("instanceNumber");
						if (instanceNumber == null) instanceNumber = (String) paramData.get("instanceNumber");
						if (instanceNumber == null) instanceNumber = "1";
						statusCode = epadOperations.createFile(username, seriesReference, uploadedFile, description, fileType, sessionID, convertToDicom, modality, instanceNumber);					
					} else {
						List<String> descriptions = (List<String>) paramData.get("description_List");
						List<String> fileTypes = (List<String>) paramData.get("fileType_List");
						List<String> instanceNumbers = (List<String>) paramData.get("instanceNumber_List");
						int i = 0;
						for (String param: paramData.keySet())
						{
							if (paramData.get(param) instanceof File)
							{
								String description = httpRequest.getParameter("description");
								if (descriptions != null && descriptions.size() > i)
									description = descriptions.get(i);
								String fileType = httpRequest.getParameter("fileType");
								if (fileTypes != null && fileTypes.size() > i)
										fileType = fileTypes.get(i);
								String instanceNumber = "1";
								if (instanceNumbers != null && instanceNumbers.size() > i)
									instanceNumber = instanceNumbers.get(i);
								statusCode = epadOperations.createFile(username, seriesReference, (File)paramData.get(param), description, fileType, sessionID, convertToDicom, modality, instanceNumber);
								i++;
							}
						}
					}
					
				} else if (HandlerUtil.matchesTemplate(TemplatesRouteTemplates.TEMPLATE_LIST, pathInfo)) {
					if (requestContentType == null || !requestContentType.startsWith("multipart/form-data"))
						throw new Exception("Invalid Content Type, should be multipart/form-data");
					if (numberOfFiles == 0)
						throw new Exception("No files found in post");
					int i = 0;
					for (String param: paramData.keySet())
					{
						if (paramData.get(param) instanceof File)
						{
							statusCode = epadOperations.createSystemTemplate(username, (File)paramData.get(param), sessionID);
							i++;
						}
					}
					
				} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER, pathInfo)) {
					Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER, pathInfo);
					String target_username = HandlerUtil.getTemplateParameter(templateMap, "username");
					String firstname = httpRequest.getParameter("firstname");
					String lastname = httpRequest.getParameter("lastname");
					String email = httpRequest.getParameter("email");
					String password = httpRequest.getParameter("password");
					String oldpassword = httpRequest.getParameter("oldpassword");
					String[] addPermissions = httpRequest.getParameterValues("addPermission");
					String[] removePermissions = httpRequest.getParameterValues("removePermission");
					epadOperations.createOrModifyUser(username, target_username, firstname, lastname, email, password, oldpassword, addPermissions, removePermissions);
					String enable = httpRequest.getParameter("enable");
					if ("true".equalsIgnoreCase(enable))
						epadOperations.enableUser(username, target_username);
					else if ("false".equalsIgnoreCase(enable))
						epadOperations.disableUser(username, target_username);
					statusCode = HttpServletResponse.SC_OK;
				
				} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT, pathInfo)) {
					ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT, pathInfo);
					String projectName = httpRequest.getParameter("projectName");
					String projectDescription = httpRequest.getParameter("projectDescription");
					EPADProject project = epadOperations.getProjectDescription(projectReference, username, sessionID);
					if (project != null) {
						throw new Exception("Project " + project.id +  " already exists");
					} else {
						statusCode = epadOperations.createProject(username, projectReference, projectName, projectDescription, sessionID);
					}							
				} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT, pathInfo)) {
					SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT, pathInfo);
					String subjectName = httpRequest.getParameter("subjectName");
					String gender = httpRequest.getParameter("gender");
					String dob = httpRequest.getParameter("dob");
					EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
					if (subject != null) {
						throw new Exception("Subject " + subject.subjectID +  " already exists");
					} else {
						statusCode = epadOperations.createSubject(username, subjectReference, subjectName, getDate(dob), gender, sessionID);
					}
					
				} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_WORKLIST, pathInfo)) {
					ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_WORKLIST, pathInfo);
					Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_WORKLIST, pathInfo);
					String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
					String workListID = HandlerUtil.getTemplateParameter(templateMap, "workListID");
					String description = httpRequest.getParameter("description");
					String dueDate = httpRequest.getParameter("dueDate");
					worklistOperations.createWorkList(username, reader, projectReference.projectID, workListID, description, null, getDate(dueDate));
					statusCode = HttpServletResponse.SC_OK;
				
				} else {
					statusCode = HandlerUtil.badRequestJSONResponse(BAD_POST_MESSAGE + ":" + pathInfo, responseStream, log);
				}		
			}
			return statusCode;
		} catch (Exception x) {
			log.warning("Error handling post", x);
			responseStream.write(new EPADMessage(x.getMessage()).toJSON());
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;							
		}
		finally {
			if (uploadedFile != null)
			{
				if (uploadedFile.getParentFile().exists())
				{
					log.info("Deleting upload directory " + uploadedFile.getParentFile().getAbsolutePath());
					EPADFileUtils.deleteDirectoryAndContents(uploadedFile.getParentFile());
				}
			}
		}
	}

	private int handleDelete(HttpServletRequest httpRequest, PrintWriter responseStream, String username, String sessionID)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EpadWorkListOperations worklistOperations = DefaultWorkListOperations.getInstance();
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;

		log.info("DELETE Request from client:" + pathInfo + " user:" + username + " sessionID:" + sessionID);
		boolean deleteDSO = "true".equalsIgnoreCase(httpRequest.getParameter("deleteDSO"));
		boolean deleteAims = "true".equalsIgnoreCase(httpRequest.getParameter("deleteAims"));
		try
		{
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
					responseStream.append(new EPADMessage(err).toJSON());
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
					responseStream.append(new EPADMessage(err).toJSON());
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
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER, pathInfo);
				String delete_username = HandlerUtil.getTemplateParameter(templateMap, "username");
				epadOperations.removeUserFromProject(username, projectReference, delete_username, sessionID);
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.WORKLISTS, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.WORKLISTS, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.WORKLISTS, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				WorkList wl = worklistOperations.getWorkListForUserByProject(username, projectReference.projectID);
				if (wl == null)
					throw new Exception("Worklist not found for user " + reader + " and project " + projectReference.projectID);
				worklistOperations.deleteWorkList(username, wl.getWorkListID());;		
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_WORKLIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_WORKLIST, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String worklistID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				worklistOperations.deleteWorkList(username, worklistID);;		
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER, pathInfo);
				String target_username = HandlerUtil.getTemplateParameter(templateMap, "username");
				epadOperations.deleteUser(username, target_username);
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_WORKLIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(UsersRouteTemplates.USER_WORKLIST, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_WORKLIST, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String worklistID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				worklistOperations.deleteWorkList(username, worklistID);;		
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacid);
				RemotePACService.getInstance().removeRemotePAC(username, pac);
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_QUERY, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_QUERY, pathInfo);
				String pacID = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				String subjectUID = HandlerUtil.getTemplateParameter(templateMap, "subjectid");
				if (subjectUID == null)
					throw new Exception("Missing Patient ID parameter");
				RemotePACService.getInstance().removeRemotePACQuery(username, pacID, subjectUID);
				statusCode = HttpServletResponse.SC_OK;
				
			} else {
				statusCode = HandlerUtil.badRequestJSONResponse(BAD_DELETE_MESSAGE + ":" + pathInfo, responseStream, log);
			}
		} catch (Exception x) {
			responseStream.append(new EPADMessage(x.getMessage()).toJSON());
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return statusCode;
	}
	
	private File getUploadedFile(HttpServletRequest httpRequest)
	{
		String uploadDirPath = EPADConfig.getEPADWebServerFileUploadDir() + "temp" + Long.toString(System.currentTimeMillis());
		File uploadDir = new File(uploadDirPath);
		uploadDir.mkdirs();
		String fileName = httpRequest.getParameter("fileName");
		String tempXMLFileName = "temp" + System.currentTimeMillis() + "-annotation.xml";
		if (fileName != null)
			tempXMLFileName = "temp" + System.currentTimeMillis() + "-" + fileName;
		File uploadedFile = new File(uploadDir, tempXMLFileName);
		try
		{
			// opens input stream of the request for reading data
			InputStream inputStream = httpRequest.getInputStream();
			
			// opens an output stream for writing file
			FileOutputStream outputStream = new FileOutputStream(uploadedFile);
			
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
			if (len == 0)
			{
				try {
					uploadedFile.delete();
					uploadDir.delete();
				} catch (Exception x) {}
				uploadedFile = null;
			}
			else
				log.debug("Created File:" + uploadedFile.getAbsolutePath());
			if (len > 0 && (tempXMLFileName.endsWith(".xml") || tempXMLFileName.endsWith(".txt")))
			{
				log.debug("PUT Data:" + readFile(uploadedFile));
			}
//			if (fileType != null)
//			{
//				File changeFileExt = new File(uploadedFile.getParentFile(), tempXMLFileName.substring(0, tempXMLFileName.length()-3) + fileType);
//				uploadedFile.renameTo(changeFileExt);
//				uploadedFile = changeFileExt;
//			}
			return uploadedFile;
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

	private Map<String, Object> parsePostedData(HttpServletRequest httpRequest, PrintWriter responseStream) throws Exception
	{
		String uploadDirPath = EPADConfig.getEPADWebServerFileUploadDir() + "temp" + Long.toString(System.currentTimeMillis());
		File uploadDir = new File(uploadDirPath);
		uploadDir.mkdirs();
		
		Map<String, Object> params = new HashMap<String, Object>();
	    // Create a factory for disk-based file items
	    DiskFileItemFactory factory = new DiskFileItemFactory();
	    // Create a new file upload handler
	    ServletFileUpload upload = new ServletFileUpload(factory);
	    List<FileItem> items = upload.parseRequest(httpRequest);
		Iterator<FileItem> fileItemIterator = items.iterator();
		int fileCount = 0;
		while (fileItemIterator.hasNext()) {
			FileItem fileItem = fileItemIterator.next();
		    if (fileItem.isFormField()) {
		    	if (params.get(fileItem.getFieldName()) == null)
		    		params.put(fileItem.getFieldName(), fileItem.getString());
			    List values = (List) params.get(fileItem.getFieldName() + "_List");
			    if (values == null) {
			    	values = new ArrayList();
			    	params.put(fileItem.getFieldName() + "_List", values);
			    }
			    values.add(fileItem.getString());
		    } else {
				fileCount++;		    	
				String fieldName = fileItem.getFieldName();
				String fileName = fileItem.getName();
				log.debug("Uploading file number " + fileCount);
				log.debug("FieldName: " + fieldName);
				log.debug("File Name: " + fileName);
				log.debug("ContentType: " + fileItem.getContentType());
				log.debug("Size (Bytes): " + fileItem.getSize());
				if (fileItem.getSize() != 0)
				{
			        try {
						String tempFileName = "temp" + System.currentTimeMillis() + "-" + fileName;
						File file = new File(uploadDirPath + "/" + tempFileName);
						log.debug("FileName: " + file.getAbsolutePath());
		                // write the file
						fileItem.write(file);
				    	if (params.get(fileItem.getFieldName()) == null)
				    		params.put(fileItem.getFieldName(), file);
					    List values = (List) params.get(fileItem.getFieldName() + "_List");
					    if (values == null) {
					    	values = new ArrayList();
					    	params.put(fileItem.getFieldName() + "_List", values);
					    }
					    values.add(file);
					} catch (Exception e) {
						e.printStackTrace();
						log.warning("Error receiving file:" + e);
						responseStream.print("error reading (" + fileCount + "): " + fileItem.getName());
						continue;
					}
				}
		    }
		}
		return params;
	}

	private boolean returnSummary(HttpServletRequest httpRequest)
	{
		String summary = httpRequest.getParameter("format");
		if (summary != null && summary.trim().equalsIgnoreCase("summary"))
			return true;
		else
			return false;
	}

	private boolean returnJson(HttpServletRequest httpRequest)
	{
		String summary = httpRequest.getParameter("format");
		if (summary != null && summary.trim().equalsIgnoreCase("json"))
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
	
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
	private Date getDate(String dateStr)
	{
		try
		{
			return dateformat.parse(dateStr);
		}
		catch (Exception x)
		{
			return null;
		}
	}

}
