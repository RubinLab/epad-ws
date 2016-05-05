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

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADMessage;
import edu.stanford.epad.dtos.EPADPlugin;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.models.ProjectType;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.DefaultWorkListOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadWorkListOperations;
import edu.stanford.epad.epadws.service.PluginOperations;

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
	 * Note: This class will soon become obsolete and be replaced by Spring Controllers
	 */

	protected static int handlePost(HttpServletRequest httpRequest, PrintWriter responseStream, String username, String sessionID)
	{
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;
		File uploadedFile = null;
		PluginOperations pluginOperations=PluginOperations.getInstance();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EpadWorkListOperations worklistOperations = DefaultWorkListOperations.getInstance();
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
	    String requestContentType = httpRequest.getContentType();
		try {
			log.info("POST Request, contentType:" + requestContentType);
			if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME_LIST, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.FRAME_LIST, pathInfo);
				String type = httpRequest.getParameter("type");
				if ("new".equalsIgnoreCase(type))
				{
					boolean errstatus = DSOUtil.handleCreateDSO(imageReference.projectID, imageReference.subjectID, imageReference.studyUID,
							imageReference.seriesUID, httpRequest, responseStream, username);
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
					String uploadDir = EPADConfig.getEPADWebServerFileUploadDir() + "temp" + Long.toString(System.currentTimeMillis());
					paramData = HandlerUtil.parsePostedData(uploadDir, httpRequest, responseStream);
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
								log.info("Creating file " + i++ + " type:" + fileType);
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
					String type = httpRequest.getParameter("type");
					String colorpreference = httpRequest.getParameter("colorpreference");
					String admin = httpRequest.getParameter("admin");
					//log.info(" email:" + email +" firstname:" + firstname + " lastname:" + lastname + " new password:" + password + " old password:" + oldpassword); 
					String[] addPermissions = httpRequest.getParameterValues("addPermission");
					String[] removePermissions = httpRequest.getParameterValues("removePermission");
					if (colorpreference == null && enable == null && firstname == null && lastname == null && email == null && addPermissions == null && removePermissions == null && password == null && oldpassword == null && admin == null)
						throw new Exception("BAD Request - all parameters are null");
					if ("new".equals(type) && projectOperations.getUser(target_username) != null)
						throw new Exception("User " +  target_username + " already exists");
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
				
				} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT, pathInfo)) {
					ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT, pathInfo);
					String projectName = httpRequest.getParameter("projectName");
					String projectDescription = httpRequest.getParameter("projectDescription");
					String defaultTemplate = httpRequest.getParameter("defaultTemplate");
					ProjectType type=ProjectType.PRIVATE;
					try {
						type= ProjectType.valueOf(httpRequest.getParameter("type"));
					}catch (IllegalArgumentException e){}
					
					EPADProject project = epadOperations.getProjectDescription(projectReference, username, sessionID, false);
					if (project != null) {
						throw new Exception("Project " + project.id +  " already exists");
					} else {
						statusCode = epadOperations.createProject(username, projectReference, projectName, projectDescription, defaultTemplate, sessionID, type);
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
					
				} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_WORKLISTS, pathInfo)) {
					Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_WORKLISTS, pathInfo);
					String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
					String projectID = HandlerUtil.getTemplateParameter(templateMap, "projectID");
					String description = httpRequest.getParameter("description");
					String name = httpRequest.getParameter("name");
					String dueDate = httpRequest.getParameter("dueDate");
					worklistOperations.createWorkList(username, reader, null, name, description, null, getDate(dueDate));
					statusCode = HttpServletResponse.SC_OK;
				
				} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_WORKLIST, pathInfo)) {
					Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_WORKLIST, pathInfo);
					String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
					String projectID = HandlerUtil.getTemplateParameter(templateMap, "projectID");
					String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
					String name = httpRequest.getParameter("name");
					String description = httpRequest.getParameter("description");
					String dueDate = httpRequest.getParameter("dueDate");
					worklistOperations.createWorkList(username, reader, workListID, name, description, null, getDate(dueDate));
					statusCode = HttpServletResponse.SC_OK;
				} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_PROJECT_SUBJECTS, pathInfo)) {
					Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_PROJECT_SUBJECTS, pathInfo);
					String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
					String projectID = HandlerUtil.getTemplateParameter(templateMap, "projectID");
					String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
					worklistOperations.addSubjectsToWorkList(username, projectID, HandlerUtil.getPostedJson(httpRequest), workListID);
					statusCode = HttpServletResponse.SC_OK;
								
				} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_AIM_LIST, pathInfo)) {
					ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_AIM_LIST, pathInfo);
					AIMSearchType aimSearchType = AIMUtil.getAIMSearchType(httpRequest);
					
					JSONObject aims = HandlerUtil.getPostedJson(httpRequest);
				    JSONArray aimIDsJson = (JSONArray) aims.get("aims");
				    String[] aimIDsStr= new String[aimIDsJson.length()];
					for (int i = 0; i < aimIDsJson.length(); i++)
					{
						aimIDsStr[i] = aimIDsJson.getString(i);
					}
					log.info("aims array  "+ aimIDsStr);
					
					String searchValue = aimSearchType != null ? httpRequest.getParameter(aimSearchType.getName()) : null;
					String templateName = httpRequest.getParameter("templateName");
					if (templateName == null)
						templateName = httpRequest.getParameter("pluginID");
					log.info("POST request for AIMs from user " + username + "; query type is " + aimSearchType + ", value "
							+ searchValue + ", project " + projectReference.projectID + " template/plugin:" +templateName);
					String inParallel = httpRequest.getParameter("inParallel");
					boolean isInParallel=!("false".equalsIgnoreCase(inParallel));
					
					if (aimSearchType!=null && aimSearchType.equals(AIMSearchType.ANNOTATION_UID)) {
						String[] aimIDs = searchValue.split(",");
						AIMUtil.runPlugIn(aimIDs, templateName, projectReference.projectID, sessionID, isInParallel);
					}else if (aims!=null && aimIDsStr.length!=0) { //ml
						AIMUtil.runPlugIn(aimIDsStr, templateName, projectReference.projectID, sessionID, isInParallel);
						
					}
					
					statusCode = HttpServletResponse.SC_OK;

				} else if (HandlerUtil.matchesTemplate(PluginRouteTemplates.PLUGIN_LIST, pathInfo)) { //ML
					String pluginId = httpRequest.getParameter("pluginId");
					String name = httpRequest.getParameter("name");
					String description = httpRequest.getParameter("description");
					String javaclass = httpRequest.getParameter("class");
					String enabled = httpRequest.getParameter("enabled");
					String modality = httpRequest.getParameter("modality");
					String developer = httpRequest.getParameter("developer");
					String documentation = httpRequest.getParameter("documentation");
					String rate = httpRequest.getParameter("rate");
					boolean isUpdate = pluginOperations.doesPluginExist(pluginId, username, sessionID);
					if (isUpdate) {
						throw new Exception("Plugin " + pluginId +  " already exists");
					} else {
						pluginOperations.createPlugin(username, pluginId, name, description, javaclass, enabled, modality,developer,documentation,rate, sessionID);
						return HttpServletResponse.SC_OK;
					}	
					
				} else if (HandlerUtil.matchesTemplate(PluginRouteTemplates.PLUGIN, pathInfo)) { //ML
					PluginReference pluginReference = PluginReference.extract(PluginRouteTemplates.PLUGIN, pathInfo);
					String name = httpRequest.getParameter("name");
					String description = httpRequest.getParameter("description");
					String javaclass = httpRequest.getParameter("class");
					String enabled = httpRequest.getParameter("enabled");
					String modality = httpRequest.getParameter("modality");
					String developer = httpRequest.getParameter("developer");
					String documentation = httpRequest.getParameter("documentation");
					String rate = httpRequest.getParameter("rate");
					String processMultipleAims = httpRequest.getParameter("processMultipleAims");
					boolean isProcessMultipleAims = ("true".equalsIgnoreCase(processMultipleAims));

					boolean isUpdate = pluginOperations.doesPluginExist(pluginReference.pluginID, username, sessionID);
					if (isUpdate) {
						throw new Exception("Plugin " + pluginReference.pluginID +  " already exists");
					} else {
						pluginOperations.createPlugin(username, pluginReference.pluginID, name, description, javaclass, enabled, modality,developer,documentation,rate, sessionID, isProcessMultipleAims);
						return HttpServletResponse.SC_OK;
					}	

				} else {
					statusCode = HandlerUtil.badRequestJSONResponse(BAD_POST_MESSAGE + ":" + pathInfo, responseStream, log);
				}		
			}
			return statusCode;
		} catch (Exception x) {
			log.warning("Error handling post", x);
			responseStream.write(new EPADMessage(x.getMessage()).toJSON());
			log.info("ID:" + Thread.currentThread().getId() + " Error message to client:" + x.getMessage());
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
