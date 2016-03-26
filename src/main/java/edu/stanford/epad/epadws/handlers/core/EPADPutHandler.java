/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
package edu.stanford.epad.epadws.handlers.core;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADMessage;
import edu.stanford.epad.dtos.EPADPlugin;
import edu.stanford.epad.dtos.EPADPluginList;
import edu.stanford.epad.dtos.EPADPluginParameterList;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.models.EpadFile;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.RemotePACQuery;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.models.WorkListToStudy;
import edu.stanford.epad.epadws.models.WorkListToSubject;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.DefaultWorkListOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadWorkListOperations;
import edu.stanford.epad.epadws.service.PluginOperations;
import edu.stanford.epad.epadws.service.RemotePACService;
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
	 * Note: This class will soon become obsolete and be replaced by Spring Controllers
	 */

	protected static int handlePut(HttpServletRequest httpRequest, HttpServletResponse httpResponse, PrintWriter responseStream,
			String username, String sessionID)
	{
		PluginOperations pluginOperations=PluginOperations.getInstance();
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
				String uploadDir = EPADConfig.getEPADWebServerFileUploadDir() + "temp" + Long.toString(System.currentTimeMillis());
				paramData = HandlerUtil.parsePostedData(uploadDir, httpRequest, responseStream);
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
				String defaultTags = httpRequest.getParameter("defaultTags");
				if (defaultTags != null) {
					epadOperations.updateSeriesTags(username, seriesReference, defaultTags, sessionID);
				} else {
					// Create non-dicom series
					EPADSeries series  = epadOperations.createSeries(username, seriesReference, description, getDate(seriesDate), modality, referencedSeries, sessionID);
				}
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
				
				//ml 
				String[] aims = httpRequest.getParameterValues("aims");
				
				String searchValue = aimSearchType != null ? httpRequest.getParameter(aimSearchType.getName()) : null;
				String templateName = httpRequest.getParameter("templateName");
				if (templateName == null)
					templateName = httpRequest.getParameter("pluginID");
				log.info("PUT request for AIMs from user " + username + "; query type is " + aimSearchType + ", value "
						+ searchValue + ", project " + projectReference.projectID);
				
				if (aimSearchType.equals(AIMSearchType.ANNOTATION_UID)) {
					String[] aimIDs = searchValue.split(",");
					AIMUtil.runPlugIn(aimIDs, templateName, projectReference.projectID, sessionID);
				}else if (aims!=null && aims.length!=0) { //ml
					AIMUtil.runPlugIn(aims, templateName, projectReference.projectID, sessionID);
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
	
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_WORKLISTS, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_WORKLISTS, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String name = httpRequest.getParameter("name");
				String description = httpRequest.getParameter("description");
				String dueDate = httpRequest.getParameter("dueDate");
				worklistOperations.createWorkList(username, reader, null, name, description, null, getDate(dueDate));
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_WORKLIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_WORKLIST, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				String name = httpRequest.getParameter("name");
				String description = httpRequest.getParameter("description");
				String dueDate = httpRequest.getParameter("dueDate");
				WorkList worklist = worklistOperations.getWorkList(workListID);
				if (worklist == null)
				{
					worklist = worklistOperations.createWorkList(username, reader, workListID, name, description, null, getDate(dueDate));
				}
				else
				{
					//ml removed for changing user
//					if (description != null || dueDate != null || name != null)
						worklistOperations.updateWorkList(username, reader, workListID, name, description, null, getDate(dueDate));
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
				
			
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_PROJECT_SUBJECT, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_PROJECT_SUBJECT, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				String projectID = HandlerUtil.getTemplateParameter(templateMap, "projectID");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subjectID");
				String wlstatus = httpRequest.getParameter("status");
				boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				WorkList wl = worklistOperations.getWorkList(workListID);
				if (wl == null)
					throw new Exception("Worklist not found for user " + reader);
				User user = worklistOperations.getUserForWorkList(workListID);
				//ml user admin check  added
				if (!user.isAdmin() || !user.getUsername().equals(reader))
					throw new Exception("User " +  reader + " does not match user for worklist "+ workListID);
				log.debug("Worklist parameters, wlstatus:" + wlstatus + " started:" + started + " completed:" + completed);
				if (wlstatus == null && !started && !completed){
					//ml get sortorder from param
					String sortOrder = httpRequest.getParameter("sortorder");
					if (sortOrder!=null)
					//worklistOperations.addSubjectToWorkList(username, projectID, subjectID, workListID); // Just change sort order
						worklistOperations.addSubjectToWorkList(username, projectID, subjectID, workListID,sortOrder);
				}
				else
					worklistOperations.setWorkListSubjectStatus(reader, wl.getWorkListID(), projectID, subjectID, wlstatus, started, completed);
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_PROJECT_STUDY, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_STUDY, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String studyUID = HandlerUtil.getTemplateParameter(templateMap, "studyUID");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				String projectID = HandlerUtil.getTemplateParameter(templateMap, "projectID");
				String wlstatus = httpRequest.getParameter("status");
				Boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				Boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				if (httpRequest.getParameter("started") == null)
					started = null;
				if (httpRequest.getParameter("completed") == null)
					completed = null;
				log.info("Project:" + projectID + " reader:" + reader + " workListID:" + workListID +" studyUID:" + studyUID);
				WorkList wl = worklistOperations.getWorkList(workListID);
				if (wl == null)
					throw new Exception("Worklist not found for user " + reader);
				WorkListToStudy wls = worklistOperations.getWorkListStudyStatus(workListID, studyUID);
				if (wls == null)
					worklistOperations.addStudyToWorkList(username, projectID, studyUID, workListID);
				if (wlstatus != null || started != null || completed != null)
				{
					log.info("WorklistID:" + workListID + " status:" + wlstatus + " started:" + started + " completed:" + completed);
					worklistOperations.setWorkListStudyStatus(reader, wl.getWorkListID(), studyUID, wlstatus, started, completed);
				}
				else
					worklistOperations.addStudyToWorkList(username, projectID, studyUID, workListID);
					
				statusCode = HttpServletResponse.SC_OK;
				
				//ml seems duplicate
//			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_PROJECT_SUBJECT, pathInfo)) {
//				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_PROJECT_SUBJECT, pathInfo);
//				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
//				String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
//				String projectID = HandlerUtil.getTemplateParameter(templateMap, "projectID");
//				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subjectID");
//				String wlstatus = httpRequest.getParameter("status");
//				boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
//				boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
//				WorkList wl = worklistOperations.getWorkList(workListID);
//				if (wl == null)
//					throw new Exception("Worklist not found for user " + reader);
//				User user = worklistOperations.getUserForWorkList(workListID);
//				//ml user admin check
//				if (!user.isAdmin() || !user.getUsername().equals(reader))
//					throw new Exception("User " +  reader + " does not match user for worklist "+ workListID);
//				WorkListToSubject wls = worklistOperations.getWorkListSubjectStatus(workListID, projectID, subjectID);
//				if (wls == null)
//					worklistOperations.addSubjectToWorkList(username, projectID, subjectID, workListID);
//				worklistOperations.setWorkListSubjectStatus(reader, wl.getWorkListID(), projectID, subjectID, wlstatus, started, completed);
//				statusCode = HttpServletResponse.SC_OK;
//	
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_PROJECT_STUDY, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_PROJECT_STUDY, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String studyUID = HandlerUtil.getTemplateParameter(templateMap, "studyUID");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				String projectID = HandlerUtil.getTemplateParameter(templateMap, "projectID");
				String wlstatus = httpRequest.getParameter("status");
				Boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
				Boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
				if (httpRequest.getParameter("started") == null)
					started = null;
				if (httpRequest.getParameter("completed") == null)
					completed = null;
				log.info("Project:" + projectID + " reader:" + reader + " workListID:" + workListID +" studyUID:" + studyUID);
				WorkList wl = worklistOperations.getWorkList(workListID);
				if (wl == null)
					throw new Exception("Worklist not found for user " + reader);
				WorkListToStudy wls = worklistOperations.getWorkListStudyStatus(workListID, studyUID);
				if (wls == null)
					worklistOperations.addStudyToWorkList(username, projectID, studyUID, workListID);
				if (wlstatus != null || started != null || completed != null)
				{
					log.info("WorklistID:" + workListID + " status:" + wlstatus + " started:" + started + " completed:" + completed);
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
				String colorpreference = httpRequest.getParameter("colorpreference");
				String admin = httpRequest.getParameter("admin");
				//log.info(" firstname:" + firstname + " lastname:" + lastname + " new password:" + password + " old password:" + oldpassword); 
				String[] addPermissions = httpRequest.getParameterValues("addPermission");
				String[] removePermissions = httpRequest.getParameterValues("removePermission");
				String enable = httpRequest.getParameter("enable");
				if (colorpreference == null && enable == null && firstname == null && lastname == null && email == null && addPermissions == null && removePermissions == null && password == null && oldpassword == null && admin == null)
					throw new Exception("BAD Request - all parameters are null");
				epadOperations.createOrModifyUser(username, target_username, firstname, lastname, email, password, oldpassword, colorpreference, addPermissions, removePermissions);
				if ("true".equalsIgnoreCase(enable))
					epadOperations.enableUser(username, target_username);
				else if ("false".equalsIgnoreCase(enable))
					epadOperations.disableUser(username, target_username);
				if ("true".equalsIgnoreCase(admin))
					epadOperations.setAdmin(username, target_username);
				else if ("false".equalsIgnoreCase(admin))
					epadOperations.resetAdmin(username, target_username);
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
				
			} else if (HandlerUtil.matchesTemplate(TemplatesRouteTemplates.TEMPLATE, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(TemplatesRouteTemplates.TEMPLATE, pathInfo);
				String templatename = HandlerUtil.getTemplateParameter(templateMap, "name");
				String enable = httpRequest.getParameter("enable");
				if (enable != null)
				{
					if ("true".equalsIgnoreCase(enable))
					{
						projectOperations.enableTemplate(username, EPADConfig.xnatUploadProjectID, null, null, null, templatename);
					}
					else if ("false".equalsIgnoreCase(enable))
					{	
						projectOperations.disableTemplate(username, EPADConfig.xnatUploadProjectID, null, null, null, templatename);
					}
				}
				
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
						projectOperations.enableFile(username, EPADConfig.xnatUploadProjectID, null, null, null, templatename);
					}
					else if (efile != null && "false".equalsIgnoreCase(enable))
					{	
						projectOperations.disableFile(username, EPADConfig.xnatUploadProjectID, null, null, null, templatename);
					}
					if (efile == null && "true".equalsIgnoreCase(enable))
					{
						projectOperations.enableTemplate(username, reference.projectID, null, null, null, templatename);
					}
					else if (efile == null && "false".equalsIgnoreCase(enable))
					{	
						projectOperations.disableTemplate(username, reference.projectID, null, null, null, templatename);
					}
				}
				else if (httpRequest.getParameter("addToProject") != null)
				{
					Project project = projectOperations.getProject(httpRequest.getParameter("addToProject"));
					if (project == null)
						throw new Exception("Project " + httpRequest.getParameter("addToProject") + " not found");
					EpadFile efile = projectOperations.getEpadFile(reference.projectID, null, null, null, templatename);
					if (efile != null)
					{
						projectOperations.linkFileToProject(username, project, efile);
					}
				}
				else if (httpRequest.getParameter("removeFromProject") != null)
				{
					Project project = projectOperations.getProject(httpRequest.getParameter("addToProject"));
					if (project == null)
						throw new Exception("Project " + httpRequest.getParameter("addToProject") + " not found");
					EpadFile efile = projectOperations.getEpadFile(reference.projectID, null, null, null, templatename);
					if (efile != null)
					{
						projectOperations.unlinkFileFromProject(username, project, efile);
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
				String color = httpRequest.getParameter("color");
				if (action == null && color == null)
					throw new Exception("Invalid action specified, should be Undo or Redo or Color");
				AIMReference aimReference = AIMReference.extract(AimsRouteTemplates.AIM, pathInfo);
				EPADAIM aim = epadOperations.getAIMDescription(aimReference.aimID, username, sessionID);
				if (aim == null)
					throw new Exception("Annotation not found, aimID:" + aimReference.aimID);
				if (action.equalsIgnoreCase("undo"))
					AIMUtil.undoLastAIM(aim);
				else if (action.equalsIgnoreCase("redo"))
					AIMUtil.redoLastAIM(aim);
				else if (color != null && color.length() > 0)
					EpadDatabase.getInstance().getEPADDatabaseOperations().updateAIMColor(aim.aimID, color);
				else
					throw new Exception("Invalid action " + action + " specified, should be Undo or Redo");
					
			
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_SENDNEWPASSWORD, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_SENDNEWPASSWORD, pathInfo);
				String account = HandlerUtil.getTemplateParameter(templateMap, "username");					
				if (username == null || username.length() == 0)
					username = account;
				UserProjectService.sendNewPassword(username, account);
			
			} else if (HandlerUtil.matchesTemplate(PluginRouteTemplates.PLUGIN, pathInfo)) { //ML
				PluginReference pluginReference = PluginReference.extract(PluginRouteTemplates.PLUGIN, pathInfo);
				String name = httpRequest.getParameter("name");
				String description = httpRequest.getParameter("description");
				String javaclass = httpRequest.getParameter("class");
				String enabled = httpRequest.getParameter("enable");
				String modality = httpRequest.getParameter("modality");
				String developer = httpRequest.getParameter("developer");
				String documentation = httpRequest.getParameter("documentation");
				String rate = httpRequest.getParameter("rate");
				boolean isUpdate = pluginOperations.doesPluginExist(pluginReference.pluginID, username, sessionID);
				if (isUpdate) {
					pluginOperations.updatePlugin(username, pluginReference.pluginID, name, description, javaclass, enabled, modality, developer,documentation,rate,sessionID);
					return HttpServletResponse.SC_OK;
				} else {
					pluginOperations.createPlugin(username, pluginReference.pluginID, name, description, javaclass, enabled, modality, developer,documentation,rate, sessionID);
					return HttpServletResponse.SC_OK;
				}	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PLUGIN, pathInfo)) { //ML
				ProjectPluginReference reference = ProjectPluginReference.extract(ProjectsRouteTemplates.PLUGIN, pathInfo);
				String enabled = httpRequest.getParameter("enable");
				pluginOperations.setProjectPluginEnable(username,reference.projectId,reference.pluginId,enabled,sessionID);
				return HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PARAMETER_LIST, pathInfo)) { //ML
				ProjectPluginReference reference = ProjectPluginReference.extract(ProjectsRouteTemplates.PARAMETER_LIST, pathInfo);
							
				String[] paramNames = httpRequest.getParameterValues("param");
				String[] paramValues = httpRequest.getParameterValues("val");
				boolean isSuccess=pluginOperations.addParameters(username,reference.projectId,reference.pluginId,paramNames,paramValues);
				if (!isSuccess)
					throw new Exception();
				return HttpServletResponse.SC_OK;

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
			log.info("ID:" + Thread.currentThread().getId() + " Error message to client:" + status);
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
