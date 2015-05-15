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
import edu.stanford.epad.dtos.EPADTemplateList;
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
public class EPADPostHandler
{
	private static final String BAD_POST_MESSAGE = "Invalid POST request!";

	private static final EPADLogger log = EPADLogger.getInstance();

	/*
	 * Main class for handling POST rest calls using the epad v2 api.
	 * 
	 * Note: These long if/then/else statements looks terrible, they need to be replaced by something like jersey with annotations
	 * But there seems to be some problem using jersey with embedded jetty and multiple handlers - still need to solve that
	 * 
	 */

	protected static int handlePost(HttpServletRequest httpRequest, PrintWriter responseStream, String username, String sessionID)
	{
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;
		File uploadedFile = null;
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EpadWorkListOperations worklistOperations = DefaultWorkListOperations.getInstance();
	    String requestContentType = httpRequest.getContentType();
		try {
			log.info("POST Request, contentType:" + requestContentType);
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
					paramData = HandlerUtil.parsePostedData(httpRequest, responseStream);
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
						log.warning("No files found in post");
					if (numberOfFiles == 1) {
						String description = httpRequest.getParameter("description");
						if (description == null) description = (String) paramData.get("description");
						String fileType = httpRequest.getParameter("fileType");
						if (fileType == null) description = (String) paramData.get("fileType");
						statusCode = epadOperations.createFile(username, projectReference, uploadedFile, description, fileType, sessionID);					
					} else if (numberOfFiles > 1) {
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
						log.warning("No files found in post");
					if (numberOfFiles == 1) {
						String description = httpRequest.getParameter("description");
						if (description == null) description = (String) paramData.get("description");
						String fileType = httpRequest.getParameter("fileType");
						if (fileType == null) description = (String) paramData.get("fileType");
						statusCode = epadOperations.createFile(username, subjectReference, uploadedFile, description, fileType, sessionID);					
					} else if (numberOfFiles > 1) {
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
						log.warning("No files found in post");
					if (numberOfFiles == 1) {
						String description = httpRequest.getParameter("description");
						if (description == null) description = (String) paramData.get("description");
						String fileType = httpRequest.getParameter("fileType");
						if (fileType == null) description = (String) paramData.get("fileType");
						statusCode = epadOperations.createFile(username, studyReference, uploadedFile, description, fileType, sessionID);					
					} else if (numberOfFiles > 1) {
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
						log.warning("No files found in post");
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
					} else if (numberOfFiles > 1) {
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
					String enable = httpRequest.getParameter("enable");
					//log.info(" email:" + email +" firstname:" + firstname + " lastname:" + lastname + " new password:" + password + " old password:" + oldpassword); 
					String[] addPermissions = httpRequest.getParameterValues("addPermission");
					String[] removePermissions = httpRequest.getParameterValues("removePermission");
					if (enable == null && firstname == null && lastname == null && email == null && addPermissions == null && removePermissions == null && password == null && oldpassword == null)
						throw new Exception("BAD Request - all parameters are null");
					epadOperations.createOrModifyUser(username, target_username, firstname, lastname, email, password, oldpassword, addPermissions, removePermissions);
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
