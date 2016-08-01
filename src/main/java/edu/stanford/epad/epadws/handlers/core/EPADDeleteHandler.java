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

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADMessage;
import edu.stanford.epad.dtos.EPADPlugin;
import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.epadws.dcm4chee.Dcm4cheeServer;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.models.Plugin;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.models.WorkListToStudy;
import edu.stanford.epad.epadws.models.WorkListToSubject;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultWorkListOperations;
import edu.stanford.epad.epadws.service.EpadWorkListOperations;
import edu.stanford.epad.epadws.service.PluginOperations;
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
		PluginOperations pluginOperations= PluginOperations.getInstance();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EpadWorkListOperations worklistOperations = DefaultWorkListOperations.getInstance();
		String pathInfo = httpRequest.getPathInfo();
		int statusCode;

		//ml system delete
		boolean all = !"false".equalsIgnoreCase(httpRequest.getParameter("all")); 
		boolean deleteDSO = !"false".equalsIgnoreCase(httpRequest.getParameter("deleteDSO")); 
		boolean deleteAims = !"false".equalsIgnoreCase(httpRequest.getParameter("deleteAims"));
		try
		{
			if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT, pathInfo);
				statusCode = epadOperations.projectDelete(projectReference.projectID, sessionID, username);
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT, pathInfo);
//				statusCode = epadOperations.subjectDelete(subjectReference, sessionID, username);
				//ml
				statusCode = epadOperations.subjectDelete(subjectReference, sessionID, username, all);
				
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY, pathInfo);
				//String err = epadOperations.studyDelete(studyReference, sessionID, deleteAims, username);
				//ml
				String err = epadOperations.studyDelete(studyReference, sessionID, deleteAims, username, all);
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
//				String err = epadOperations.seriesDelete(seriesReference, sessionID, deleteAims, username);
				//ml all
				String err = epadOperations.seriesDelete(seriesReference, sessionID, deleteAims, username, all);
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
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				WorkList wl = worklistOperations.getWorkList(workListID);
				if (wl == null)
					throw new Exception("Worklist not found for id " + workListID);
				User user = worklistOperations.getUserForWorkList(workListID);
				if (!user.getUsername().equals(reader))
					throw new Exception("User " +  reader + " does not match user for worklist "+ workListID);
				worklistOperations.deleteWorkList(username, wl.getWorkListID());;		
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_SUBJECT, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_SUBJECT, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subjectID");
				WorkList wl = worklistOperations.getWorkList(workListID);
				if (wl == null)
					throw new Exception("Worklist not found for user " + reader);
				User user = worklistOperations.getUserForWorkList(workListID);
				if (!user.getUsername().equals(reader))
					throw new Exception("User " +  reader + " does not match user for worklist "+ workListID);
				worklistOperations.removeSubjectFromWorkList(username, subjectID, workListID);
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_PROJECT_SUBJECT, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_PROJECT_SUBJECT, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				String projectID = HandlerUtil.getTemplateParameter(templateMap, "projectID");
				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subjectID");
				WorkList wl = worklistOperations.getWorkList(workListID);
				if (wl == null)
					throw new Exception("Worklist not found for user " + reader);
				User user = worklistOperations.getUserForWorkList(workListID);
				if (!user.getUsername().equals(reader))
					throw new Exception("User " +  reader + " does not match user for worklist "+ workListID);
				worklistOperations.removeSubjectFromWorkList(username, projectID, subjectID, workListID);
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_PROJECT_STUDY, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_PROJECT_STUDY, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String studyUID = HandlerUtil.getTemplateParameter(templateMap, "studyUID");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				String projectID = HandlerUtil.getTemplateParameter(templateMap, "projectID");
				worklistOperations.removeStudyFromWorkList(username, studyUID, workListID);
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
				//cavit
				Dcm4cheeServer instanceDcm4cheeServer = new Dcm4cheeServer();
				instanceDcm4cheeServer.connect(EPADConfig.jmxUserName, EPADConfig.jmxUserPass, EPADConfig.dcm4CheeServer,(short) EPADConfig.dcm4cheeServerWadoPort);
				instanceDcm4cheeServer.deleteAetitle(pac.aeTitle);
				//throw new Exception("editing connection :" + pac.hostname);
				//cavit
				
				
				statusCode = HttpServletResponse.SC_OK;
				//throw new Exception("pac removed" + pac.aeTitle);
				
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
			
			} else if (HandlerUtil.matchesTemplate(PluginRouteTemplates.PLUGIN, pathInfo)) {
				PluginReference pluginReference = PluginReference.extract(PluginRouteTemplates.PLUGIN, pathInfo);
				Plugin plugin=pluginOperations.getPlugin(pluginReference.pluginID);
				if (plugin == null) 
					throw new Exception("Plugin not found for id " + pluginReference.pluginID);
				pluginOperations.deletePlugin(username, pluginReference.pluginID);	
				statusCode = HttpServletResponse.SC_OK;
			
			} else {
				statusCode = HandlerUtil.badRequestJSONResponse(BAD_DELETE_MESSAGE + ":" + pathInfo, responseStream, log);
			}
		} catch (Exception x) {
			log.warning("Error handleget:", x);
			responseStream.append(new EPADMessage(x.getMessage()).toJSON());
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			log.info("ID:" + Thread.currentThread().getId() + " Error message to client:" + x.getMessage());
		}
		return statusCode;
	}

}
