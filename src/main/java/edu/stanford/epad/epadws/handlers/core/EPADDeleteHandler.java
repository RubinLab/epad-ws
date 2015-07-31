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

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADMessage;
import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultWorkListOperations;
import edu.stanford.epad.epadws.service.EpadWorkListOperations;
import edu.stanford.epad.epadws.service.RemotePACService;
import edu.stanford.epad.epadws.service.TCIAService;

/**
 * @author martin
 */
public class EPADDeleteHandler
{
	private static final String BAD_DELETE_MESSAGE = "Invalid DELETE request!";

	private static final EPADLogger log = EPADLogger.getInstance();

	/*
	 * Class for handling Delete calls using the epad v2 api.
	 * 
	 * Note: These long if/then/else statements looks terrible, they need to be replaced by something like jersey with annotations
	 * But there seems to be some problem using jersey with embedded jetty and multiple handlers - still need to solve that
	 * 
	 * Note: This class will soon become obsolete and be replaced by Spring Controllers
	 */
	protected static int handleDelete(HttpServletRequest httpRequest, PrintWriter responseStream, String username, String sessionID)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EpadWorkListOperations worklistOperations = DefaultWorkListOperations.getInstance();
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;

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
				
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_WORKLIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_WORKLIST, pathInfo);
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "workListID");
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				WorkList wl = worklistOperations.getWorkList(workListID);
				if (wl == null)
					throw new Exception("Worklist not found for id " + workListID);
				User user = worklistOperations.getUserForWorkList(workListID);
				if (!user.getUsername().equals(reader))
					throw new Exception("User " +  reader + " does not match user for worklist "+ workListID);
				worklistOperations.deleteWorkList(username, wl.getWorkListID());;		
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER, pathInfo);
				String target_username = HandlerUtil.getTemplateParameter(templateMap, "username");
				epadOperations.deleteUser(username, target_username);
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_REVIEWEE, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_REVIEWEE, pathInfo);
				String reviewer = HandlerUtil.getTemplateParameter(templateMap, "username");
				String reviewee = HandlerUtil.getTemplateParameter(templateMap, "reviewee");
				epadOperations.removeReviewee(username, reviewer, reviewee);
				statusCode = HttpServletResponse.SC_OK;
			
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_REVIEWER, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_REVIEWEE, pathInfo);
				String reviewee = HandlerUtil.getTemplateParameter(templateMap, "username");
				String reviewer = HandlerUtil.getTemplateParameter(templateMap, "reviewer");
				epadOperations.removeReviewee(username, reviewee, reviewer);
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				if (pacid.startsWith(TCIAService.TCIA_PREFIX))
					throw new Exception("A TCIA Collection can not be deleted");
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacid);
				if (pac == null)
					throw new Exception("Remote PAC not found");
				RemotePACService.getInstance().removeRemotePAC(username, pac);
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_QUERY, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_QUERY, pathInfo);
				String pacID = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				if (pacID.equalsIgnoreCase("null"))
					throw new Exception("PAC ID in rest call is null:" + pathInfo);
				String subjectUID = HandlerUtil.getTemplateParameter(templateMap, "subjectid");
				if (subjectUID == null)
					throw new Exception("Missing Patient ID parameter");
				RemotePACService.getInstance().removeRemotePACQuery(username, pacID, subjectUID);
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_FILE, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_FILE, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.PROJECT_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				epadOperations.deleteFile(username, projectReference, filename);
				statusCode = HttpServletResponse.SC_OK;
						
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_FILE, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_FILE, pathInfo);
				if (subjectReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.SUBJECT_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				epadOperations.deleteFile(username, subjectReference, filename);
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_FILE, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_FILE, pathInfo);
				if (studyReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.STUDY_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				epadOperations.deleteFile(username, studyReference, filename);
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_FILE, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_FILE, pathInfo);
				if (seriesReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.SERIES_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				epadOperations.deleteFile(username, seriesReference, filename);
				statusCode = HttpServletResponse.SC_OK;
			} else {
				statusCode = HandlerUtil.badRequestJSONResponse(BAD_DELETE_MESSAGE + ":" + pathInfo, responseStream, log);
			}
		} catch (Exception x) {
			log.warning("Error handleget:", x);
			responseStream.append(new EPADMessage(x.getMessage()).toJSON());
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return statusCode;
	}

}
