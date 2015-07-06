//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
import edu.stanford.epad.dtos.DicomTagList;
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
import edu.stanford.epad.dtos.EPADTemplateContainerList;
import edu.stanford.epad.dtos.EPADUsage;
import edu.stanford.epad.dtos.EPADUsageList;
import edu.stanford.epad.dtos.EPADUser;
import edu.stanford.epad.dtos.EPADUserList;
import edu.stanford.epad.dtos.EPADWorklist;
import edu.stanford.epad.dtos.EPADWorklistList;
import edu.stanford.epad.dtos.EPADWorklistStudyList;
import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.dtos.RemotePACEntity;
import edu.stanford.epad.dtos.RemotePACEntityList;
import edu.stanford.epad.dtos.RemotePACList;
import edu.stanford.epad.dtos.RemotePACQueryConfigList;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.models.EpadFile;
import edu.stanford.epad.epadws.models.EpadStatistics;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.models.RemotePACQuery;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.models.WorkListToStudy;
import edu.stanford.epad.epadws.models.WorkListToSubject;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.security.EPADSession;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.DefaultWorkListOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadWorkListOperations;
import edu.stanford.epad.epadws.service.RemotePACService;
import edu.stanford.epad.epadws.service.SessionService;
import edu.stanford.epad.epadws.service.TCIAService;
import edu.stanford.epad.epadws.service.UserProjectService;

/**
 * @author martin
 */
public class EPADPutHandler
{
	private static final String BAD_PUT_MESSAGE = "Invalid PUT request!";

	private static final EPADLogger log = EPADLogger.getInstance();

	/*
	 * Main class for handling rest calls using the epad v2 api.
	 * 
	 * Note: These long if/then/else statements looks terrible, they need to be replaced by something like jersey with annotations
	 * But there seems to be some problem using jersey with embedded jetty and multiple handlers - still need to solve that
	 * 
	 */

	protected static int handlePut(HttpServletRequest httpRequest, HttpServletResponse httpResponse, PrintWriter responseStream,
			String username, String sessionID)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		EpadWorkListOperations worklistOperations = DefaultWorkListOperations.getInstance();
		String pathInfo = httpRequest.getPathInfo();
		int statusCode = HttpServletResponse.SC_OK;
		String status = null;
	    String requestContentType = httpRequest.getContentType();
		log.info("PUT Request, contentType:" + requestContentType);
		File uploadedFile = null;
		try
		{
			Map<String, Object> paramData = null;
			// Note: This needs to be done, before anything else otherwise upload won't work
			if (requestContentType == null || !requestContentType.startsWith("multipart/form-data"))
			{
				uploadedFile = HandlerUtil.getUploadedFile(httpRequest);
			}
			else
			{
				paramData = HandlerUtil.parsePostedData(httpRequest, responseStream);
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
				if (projectDescription == null)
					projectDescription = httpRequest.getParameter("description");
				String defaultTemplate = httpRequest.getParameter("defaultTemplate");
				EPADProject project = epadOperations.getProjectDescription(projectReference, username, sessionID, false);
				if (project != null) {
					statusCode = epadOperations.updateProject(username, projectReference, projectName, projectDescription, defaultTemplate, sessionID);
				} else {
					statusCode = epadOperations.createProject(username, projectReference, projectName, projectDescription, defaultTemplate, sessionID);
				}
				project = epadOperations.getProjectDescription(projectReference, username, sessionID, false);
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
				if ("true".equalsIgnoreCase(httpRequest.getParameter("disablePluginInvocation")))
					sessionID = null; // This will prevent plugin invocation
				status = epadOperations.createSubjectAIM(username, subjectReference, aimReference.aimID, uploadedFile, sessionID);
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY, pathInfo);
				String description = httpRequest.getParameter("description");
				if (description == null)
					description = httpRequest.getParameter("studyDescription");
				String studyDate = httpRequest.getParameter("studyDate");
				statusCode = epadOperations.createStudy(username, studyReference, description, getDate(studyDate), sessionID);
				if (uploadedFile != null && false) {
					String fileType = httpRequest.getParameter("fileType");
					statusCode = epadOperations.createFile(username, studyReference, uploadedFile, description, fileType, sessionID);					
				}
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_AIM, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.STUDY_AIM, pathInfo);
				if ("true".equalsIgnoreCase(httpRequest.getParameter("disablePluginInvocation")))
					sessionID = null; // This will prevent plugin invocation
				status = epadOperations.createStudyAIM(username, studyReference, aimReference.aimID, uploadedFile, sessionID);
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES, pathInfo);
				String description = httpRequest.getParameter("description");
				String modality = httpRequest.getParameter("modality");
				String seriesDate = httpRequest.getParameter("seriesDate");
				String referencedSeries = httpRequest.getParameter("referencedSeries");
				if (referencedSeries == null)
					referencedSeries = httpRequest.getParameter("referencedSeriesUID");
				EPADSeries series  = epadOperations.createSeries(username, seriesReference, description, getDate(seriesDate), modality, referencedSeries, sessionID);
				if (uploadedFile != null && false) {
					String fileType = httpRequest.getParameter("fileType");
					statusCode = epadOperations.createFile(username, seriesReference, uploadedFile, description, fileType, sessionID);					
				}
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_AIM, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
				if ("true".equalsIgnoreCase(httpRequest.getParameter("disablePluginInvocation")))
					sessionID = null; // This will prevent plugin invocation
				status = epadOperations.createSeriesAIM(username, seriesReference, aimReference.aimID, uploadedFile, sessionID);
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE, pathInfo)) {
				statusCode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
				httpResponse.addHeader("Allow", "GET, DELETE");
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE_AIM, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.IMAGE_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.IMAGE_AIM, pathInfo);
				if ("true".equalsIgnoreCase(httpRequest.getParameter("disablePluginInvocation")))
					sessionID = null; // This will prevent plugin invocation
				status = epadOperations.createImageAIM(username, imageReference, aimReference.aimID, uploadedFile, sessionID);
			
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_AIM, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
				if ("true".equalsIgnoreCase(httpRequest.getParameter("disablePluginInvocation")))
					sessionID = null; // This will prevent plugin invocation
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
				if ("true".equalsIgnoreCase(httpRequest.getParameter("disablePluginInvocation")))
					sessionID = null; // This will prevent plugin invocation
				status = epadOperations.createFrameAIM(username, frameReference, aimReference.aimID, uploadedFile, sessionID);
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER, pathInfo);
				String add_username = HandlerUtil.getTemplateParameter(templateMap, "username");
				String role = httpRequest.getParameter("role");
				String defaultTemplate = httpRequest.getParameter("defaultTemplate");
				epadOperations.addUserToProject(username, projectReference, add_username, role, defaultTemplate, sessionID);
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_WORKLISTS, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_WORKLISTS, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_WORKLISTS, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String description = httpRequest.getParameter("description");
				String dueDate = httpRequest.getParameter("dueDate");
				worklistOperations.createWorkList(username, reader, projectReference.projectID, null, description, null, getDate(dueDate));
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_WORKLIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_WORKLIST, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_WORKLIST, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "workListID");
				String description = httpRequest.getParameter("description");
				String dueDate = httpRequest.getParameter("dueDate");
				WorkList worklist = worklistOperations.getWorkList(workListID);
				if (worklist == null)
				{
					worklist = worklistOperations.createWorkList(username, reader, projectReference.projectID, workListID, description, null, getDate(dueDate));
				}
				else
				{
					if (description != null || dueDate != null)
						worklistOperations.updateWorkList(username, reader, workListID, description, null, getDate(dueDate));
				}
				String wlstatus = httpRequest.getParameter("status");
				Boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				Boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				if (httpRequest.getParameter("started") == null)
					started = null;
				if (httpRequest.getParameter("completed") == null)
					completed = null;
				if (wlstatus != null || started != null || completed != null)
				{
					log.info("WorklistID:" + workListID + " status:" + wlstatus + " started:" + started + " completed:" + completed);
					worklistOperations.setWorkListStatus(reader, workListID, wlstatus, started, completed);
				}
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
			
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_WORKLIST_SUBJECT, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_WORKLIST_SUBJECT, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_WORKLIST_SUBJECT, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subjectID");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "workListID");
				String wlstatus = httpRequest.getParameter("status");
				boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				log.info("Project:" + projectReference.projectID + " reader:" + reader + " workListID:" + workListID +" subjectID:" + subjectID);
				WorkList wl = worklistOperations.getWorkList(workListID);
				if (wl == null)
					wl = worklistOperations.getWorkListForUserByProject(username, projectReference.projectID);
				if (wl == null)
					throw new Exception("Worklist not found for user " + reader + " and project " + projectReference.projectID);
				WorkListToSubject wls = worklistOperations.getWorkListSubjectStatus(wl.getWorkListID(), subjectID);
				if (wls == null)
					worklistOperations.addSubjectToWorkList(username, subjectID, wl.getWorkListID());
				worklistOperations.setWorkListSubjectStatus(reader, wl.getWorkListID(), subjectID, wlstatus, started, completed);
				statusCode = HttpServletResponse.SC_OK;
	
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_STUDY, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_STUDY, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_STUDY, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String studyUID = HandlerUtil.getTemplateParameter(templateMap, "studyUID");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "workListID");
				String wlstatus = httpRequest.getParameter("status");
				Boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				Boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				if (httpRequest.getParameter("started") == null)
					started = null;
				if (httpRequest.getParameter("completed") == null)
					completed = null;
				log.info("Project:" + projectReference.projectID + " reader:" + reader + " workListID:" + workListID +" studyUID:" + studyUID);
				WorkList wl = worklistOperations.getWorkListForUserByProject(username, projectReference.projectID);
				if (wl == null)
					wl = worklistOperations.getWorkList(workListID);
				if (wl == null)
					throw new Exception("Worklist not found for user " + reader + " and project " + projectReference.projectID);
				WorkListToStudy wls = worklistOperations.getWorkListStudyStatus(workListID, studyUID);
				if (wls == null)
					worklistOperations.addStudyToWorkList(username, studyUID, workListID);
				if (wlstatus != null || started != null || completed != null)
				{
					log.info("WorklistID:" + workListID + " status:" + wlstatus + " started:" + started + " completed:" + completed);
					worklistOperations.setWorkListStudyStatus(reader, wl.getWorkListID(), studyUID, wlstatus, started, completed);
				}
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_WORKLIST_STUDY, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_WORKLIST_STUDY, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_WORKLIST_STUDY, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String studyUID = HandlerUtil.getTemplateParameter(templateMap, "studyUID");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "workListID");
				log.info("Project:" + projectReference.projectID + " reader:" + reader + " workListID:" + workListID +" studyUID:" + studyUID);
				String wlstatus = httpRequest.getParameter("status");
				Boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				Boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				if (httpRequest.getParameter("started") == null)
					started = null;
				if (httpRequest.getParameter("completed") == null)
					completed = null;
				WorkList wl = worklistOperations.getWorkList(workListID);
				if (wl == null)
					wl = worklistOperations.getWorkListForUserByProject(username, projectReference.projectID);
				if (wl == null)
					throw new Exception("Worklist not found for user " + reader + " and project " + projectReference.projectID);
				WorkListToStudy wls = worklistOperations.getWorkListStudyStatus(workListID, studyUID);
				if (wls == null)
					worklistOperations.addStudyToWorkList(username, studyUID, workListID);
				if (wlstatus != null || started != null || completed != null)
				{
					log.info("WorklistID:" + workListID + " status:" + wlstatus + " started:" + started + " completed:" + completed);
					worklistOperations.setWorkListStudyStatus(reader, wl.getWorkListID(), studyUID, wlstatus, started, completed);
				}
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_WORKLIST_SUBJECT_STUDY, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_WORKLIST_SUBJECT_STUDY, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_WORKLIST_SUBJECT_STUDY, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String studyUID = HandlerUtil.getTemplateParameter(templateMap, "studyUID");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subjectID");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "workListID");
				log.info("Project:" + projectReference.projectID + " reader:" + reader + " workListID:" + workListID +" subjectID:" + subjectID+" studyUID:" + studyUID);
				String wlstatus = httpRequest.getParameter("status");
				Boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				Boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				if (httpRequest.getParameter("started") == null)
					started = null;
				if (httpRequest.getParameter("completed") == null)
					completed = null;
				WorkList wl = worklistOperations.getWorkList(workListID);
				if (wl == null)
					wl = worklistOperations.getWorkListForUserByProject(username, projectReference.projectID);
				if (wl == null)
					throw new Exception("Worklist not found for user " + reader + " and project " + projectReference.projectID);
				Subject subject = projectOperations.getSubjectForStudy(studyUID);
				if (!subject.getSubjectUID().equals(subjectID))
					throw new Exception("Invalid subject " + subjectID + " for study " + studyUID);
				WorkListToStudy wls = worklistOperations.getWorkListStudyStatus(workListID, studyUID);
				if (wls == null)
					worklistOperations.addStudyToWorkList(username, studyUID, workListID);
				if (wlstatus != null || started != null || completed != null)
				{
					worklistOperations.setWorkListStudyStatus(reader, wl.getWorkListID(), studyUID, wlstatus, started, completed);
				}
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER, pathInfo);
				String target_username = HandlerUtil.getTemplateParameter(templateMap, "username");
				String firstname = httpRequest.getParameter("firstname");
				String lastname = httpRequest.getParameter("lastname");
				String email = httpRequest.getParameter("email");
				String password = httpRequest.getParameter("password");
				String oldpassword = httpRequest.getParameter("oldpassword");
				//log.info(" firstname:" + firstname + " lastname:" + lastname + " new password:" + password + " old password:" + oldpassword); 
				String[] addPermissions = httpRequest.getParameterValues("addPermission");
				String[] removePermissions = httpRequest.getParameterValues("removePermission");
				String enable = httpRequest.getParameter("enable");
				if (enable == null && firstname == null && lastname == null && email == null && addPermissions == null && removePermissions == null && password == null && oldpassword == null)
					throw new Exception("BAD Request - all parameters are null");
				epadOperations.createOrModifyUser(username, target_username, firstname, lastname, email, password, oldpassword, addPermissions, removePermissions);
				if ("true".equalsIgnoreCase(enable))
					epadOperations.enableUser(username, target_username);
				else if ("false".equalsIgnoreCase(enable))
					epadOperations.disableUser(username, target_username);
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_SUBJECT, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_SUBJECT, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subjectID");
				String wlstatus = httpRequest.getParameter("status");
				Boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				Boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				if (httpRequest.getParameter("started") == null)
					started = null;
				if (httpRequest.getParameter("completed") == null)
					completed = null;
				Set<WorkList> wls = worklistOperations.getWorkListsForUserBySubject(username, subjectID);
				for (WorkList wl: wls)
					worklistOperations.setWorkListSubjectStatus(reader, wl.getWorkListID(), subjectID, wlstatus, started, completed);
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_STUDY, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_STUDY, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String studyUID = HandlerUtil.getTemplateParameter(templateMap, "studyUID");
				String wlstatus = httpRequest.getParameter("status");
				Boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				Boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				if (httpRequest.getParameter("started") == null)
					started = null;
				if (httpRequest.getParameter("completed") == null)
					completed = null;
				Set<WorkList> wls = worklistOperations.getWorkListsForUserByStudy(username, studyUID);
				for (WorkList wl: wls)
					worklistOperations.setWorkListSubjectStatus(reader, wl.getWorkListID(), studyUID, wlstatus, started, completed);
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_REVIEWEE, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_REVIEWEE, pathInfo);
				String reviewer = HandlerUtil.getTemplateParameter(templateMap, "username");
				String reviewee = HandlerUtil.getTemplateParameter(templateMap, "reviewee");
				epadOperations.addReviewee(username, reviewer, reviewee);
				statusCode = HttpServletResponse.SC_OK;
			
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_REVIEWER, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_REVIEWEE, pathInfo);
				String reviewee = HandlerUtil.getTemplateParameter(templateMap, "username");
				String reviewer = HandlerUtil.getTemplateParameter(templateMap, "reviewer");
				epadOperations.addReviewer(username, reviewee, reviewer);
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
					if (pacid.startsWith(TCIAService.TCIA_PREFIX))
						throw new Exception("TCIA Collections can not be added or edited");
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
				if (pacID.equalsIgnoreCase("null"))
					throw new Exception("PAC ID in rest call is null:" + pathInfo);
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
				
			} else if (HandlerUtil.matchesTemplate(TemplatesRouteTemplates.TEMPLATE_LIST, pathInfo)) {
				if (uploadedFile == null)
					throw new Exception("No file uploaded");
				statusCode = epadOperations.createSystemTemplate(username, uploadedFile, sessionID);
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.TEMPLATE, pathInfo)) {
				ProjectReference reference = ProjectReference.extract(ProjectsRouteTemplates.TEMPLATE, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.TEMPLATE, pathInfo);
				String templatename = HandlerUtil.getTemplateParameter(templateMap, "templatename");
				String enable = httpRequest.getParameter("enable");
				if (enable != null)
				{
					EpadFile efile = projectOperations.getEpadFile(reference.projectID, null, null, null, templatename);
					if (efile != null && "true".equalsIgnoreCase(enable))
					{
						projectOperations.enableFile(username, reference.projectID, null, null, null, templatename);
					}
					else if (efile != null && "false".equalsIgnoreCase(enable))
					{	
						projectOperations.disableFile(username, reference.projectID, null, null, null, templatename);
					}
					efile = projectOperations.getEpadFile(EPADConfig.xnatUploadProjectID, null, null, null, templatename);
					if (efile != null && "true".equalsIgnoreCase(enable))
					{
						projectOperations.enableTemplate(username, reference.projectID, null, null, null, templatename);
					}
					else if (efile != null && "false".equalsIgnoreCase(enable))
					{	
						projectOperations.disableTemplate(username, reference.projectID, null, null, null, templatename);
					}
				}
				else
				{
					if (uploadedFile != null)
					{
						statusCode = epadOperations.createFile(username, reference, uploadedFile, "", FileType.TEMPLATE.getName(), sessionID);
					}
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_FILE, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_FILE, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.PROJECT_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				String description = httpRequest.getParameter("description");
				String fileType = httpRequest.getParameter("fileType");
				String mimeType = httpRequest.getParameter("mimeType");
				String name = httpRequest.getParameter("name");
				EpadFile file = projectOperations.getEpadFile(projectReference.projectID, null, null, null, filename);
				if (file != null)
				{
					projectOperations.updateEpadFile(file.getId(), name, description, fileType, mimeType);
				}
				else
					throw new Exception("File not found");
				statusCode = HttpServletResponse.SC_OK;
						
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_FILE, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_FILE, pathInfo);
				if (subjectReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.SUBJECT_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				String description = httpRequest.getParameter("description");
				String fileType = httpRequest.getParameter("fileType");
				String mimeType = httpRequest.getParameter("mimeType");
				String name = httpRequest.getParameter("name");
				EpadFile file = projectOperations.getEpadFile(subjectReference.projectID, subjectReference.subjectID, null, null, filename);
				if (file != null)
				{
					projectOperations.updateEpadFile(file.getId(), name, description, fileType, mimeType);
				}
				else
					throw new Exception("File not found");
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_FILE, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_FILE, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.STUDY_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				String description = httpRequest.getParameter("description");
				String fileType = httpRequest.getParameter("fileType");
				String mimeType = httpRequest.getParameter("mimeType");
				String name = httpRequest.getParameter("name");
				EpadFile file = projectOperations.getEpadFile(studyReference.projectID, studyReference.subjectID, studyReference.studyUID, null, filename);
				if (file != null)
				{
					projectOperations.updateEpadFile(file.getId(), name, description, fileType, mimeType);
				}
				else
					throw new Exception("File not found");
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_FILE, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_FILE, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.SERIES_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				String description = httpRequest.getParameter("description");
				String fileType = httpRequest.getParameter("fileType");
				String mimeType = httpRequest.getParameter("mimeType");
				String name = httpRequest.getParameter("name");
				EpadFile file = projectOperations.getEpadFile(seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID, seriesReference.seriesUID, filename);
				if (file != null)
				{
					projectOperations.updateEpadFile(file.getId(), name, description, fileType, mimeType);
				}
				else
					throw new Exception("File not found");
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(AimsRouteTemplates.AIM, pathInfo)) {
				String action = httpRequest.getParameter("action");
				if (action == null || action.trim().length() == 0)
					throw new Exception("Invalid action specified, should be Undo or Redo");
				AIMReference aimReference = AIMReference.extract(AimsRouteTemplates.AIM, pathInfo);
				EPADAIM aim = epadOperations.getAIMDescription(aimReference.aimID, username, sessionID);
				if (aim == null)
					throw new Exception("Annotation not found, aimID:" + aimReference.aimID);
				if (action.equalsIgnoreCase("undo"))
					AIMUtil.undoLastAIM(aim);
				else if (action.equalsIgnoreCase("redo"))
					AIMUtil.redoLastAIM(aim);
				else
					throw new Exception("Invalid action " + action + " specified, should be Undo or Redo");
					
			
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
	
	private static int getInt(String value)
	{
		try {
			return new Integer(value.trim()).intValue();
		} catch (Exception x) {
			return 0;
		}
	}
	
	static SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
	private static Date getDate(String dateStr)
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
