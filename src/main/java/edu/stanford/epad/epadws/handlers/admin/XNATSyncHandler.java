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
package edu.stanford.epad.epadws.handlers.admin;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.MailUtil;
import edu.stanford.epad.dtos.internal.XNATExperiment;
import edu.stanford.epad.dtos.internal.XNATExperimentList;
import edu.stanford.epad.dtos.internal.XNATProject;
import edu.stanford.epad.dtos.internal.XNATProjectList;
import edu.stanford.epad.dtos.internal.XNATSubject;
import edu.stanford.epad.dtos.internal.XNATSubjectList;
import edu.stanford.epad.dtos.internal.XNATUser;
import edu.stanford.epad.dtos.internal.XNATUserList;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectType;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.UserRole;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.queries.XNATQueries;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.SessionService;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * @author martin
 */
public class XNATSyncHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String FORBIDDEN = "Forbidden method - only GET supported on XNAT sync route";
	private static final String INTERNAL_ERROR_MESSAGE = "Internal server error on XNAT sync route";
	private static final String INTERNAL_IO_ERROR_MESSAGE = "Internal server IO error on XNAT sync route";
	private static final String INTERNAL_SQL_ERROR_MESSAGE = "Internal server SQL error on XNAT sync route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid for XNAT sync route";

	private final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
	private final EpadOperations epadOperations = DefaultEpadOperations.getInstance();
	
	@Override
	public void handle(String str, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/plain;charset=UTF-8");
		request.setHandled(true);

		String response = "";
		try {
			responseStream = httpResponse.getWriter();
			responseStream.write(response);
			responseStream.flush();
			
			if (SessionService.hasValidSessionID(httpRequest)) {
				String sessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
				// We may be logged into EPAD Session, get a new session ID from XNAT
				if (!XNATSessionOperations.hasValidXNATSessionID(sessionID))
					sessionID = XNATSessionOperations.getXNATAdminSessionID();
				String username = httpRequest.getParameter("username");
				if (username == null) username = "admin";
				String method = httpRequest.getMethod();
				if ("GET".equalsIgnoreCase(method)) {
					try {
						response = syncXNATtoEpad(username, sessionID);
						log.info("Output Log:" + response);
						responseStream.write(response);
						statusCode = HttpServletResponse.SC_OK;
					} catch (Exception e) {
						log.warning("Error in XNAT Sync", e);
						statusCode = HandlerUtil.internalErrorResponse(INTERNAL_ERROR_MESSAGE, e, responseStream, log);
					} 
				} else {
					statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_FORBIDDEN, FORBIDDEN, responseStream, log);
				}
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
			responseStream.flush();
		} catch (Throwable t) {
			responseStream.write(response);
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}
	
	public static String syncXNATtoEpad(String username, String sessionID) throws Exception {
		
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		String response = "";
		log.info("Starting sync with XNAT Database");
		XNATUserList xusers = XNATQueries.getAllUsers();
		for (XNATUser xuser: xusers.ResultSet.Result)
		{
			log.info("Processing user:" + xuser.login);
			response = response + "\nProcessing user:" + xuser.login;
			String password = generatePassword(xuser.login);
			User user = projectOperations.getUser(xuser.login);
			if (user == null)
			{
				user = projectOperations.createUser(username, xuser.login, xuser.firstname, xuser.lastname, xuser.email, password, null, new ArrayList<String>(), new ArrayList<String>());
				// TODO: mail password to user
				log.info("Created user:" + xuser.login + " password:" + password);
				response = response + "\nCreated user: " + user.getUsername() + " password:" + password;
				if (user.getEmail() != null && user.getEmail().trim().length() > 0)
				{
					try {
						new MailUtil().send(user.getEmail(), "noreply@" + EPADConfig.xnatServer, "Password Reset", "Hello " + user.getFullName() 
							+ "\nYour password on EPAD has been reset to your username.\nPlease login and change it.\nThank you.");
					} catch (Exception x) {}
				}
			}
			else if (user.getUsername().equals("admin"))
			{
				try {
					// Update password, but ignore errors
					user = projectOperations.updateUser(username, xuser.login, null, null, xuser.email, password, "admin", null, new ArrayList<String>(), new ArrayList<String>());
					log.info("Updated admin password");
				} catch (Exception x) {}
			}
			if (xuser.login.equals("admin"))
			{
				user.setAdmin(true);
				user.save();
			}

		}
		log.info("Getting all projects");
		if (sessionID == null || sessionID.length() == 0 || !XNATSessionOperations.hasValidXNATSessionID(sessionID))
			sessionID = XNATSessionOperations.getXNATAdminSessionID();
		XNATProjectList xnatProjectList = XNATQueries.allProjects(sessionID);
		for (XNATProject xproject: xnatProjectList.ResultSet.Result)
		{						
			log.info("Processing project:" + xproject.name);
			response = response + "\nProcessing project:" + xproject.name;
			Project project = projectOperations.getProject(xproject.ID);
			if (project == null)
			{
				String type = XNATQueries.getProjectType(xproject.ID, sessionID);
				log.info("Project " + xproject.name + " is " + type);
				ProjectType pt = ProjectType.PRIVATE;
				if (type.toLowerCase().startsWith("public"))
					pt = ProjectType.PUBLIC;
				try
				{
					project = projectOperations.createProject(username, xproject.ID, xproject.name, xproject.description, null, pt);
				} catch (Exception x) {
					log.warning("Error creating project:" +  xproject.ID, x);
					continue;
				}
				log.info("Created project:" + xproject.name);
				response = response + "\nCreated project " + xproject.name;
			}
			log.info("Getting project desc:" + xproject.ID);
			XNATUserList xnatUsers = XNATQueries.getUsersForProject(xproject.ID);
			Map<String,String> userRoles = xnatUsers.getRoles();
			log.info("Project users:" + userRoles);
			for (String euser: userRoles.keySet())
			{
				log.info("Getting projects for " + euser);
				String role = userRoles.get(euser);
				List<Project> projects = projectOperations.getProjectsForUser(euser);
				boolean found = false;
				for (Project p: projects)
				{
					if (p.getProjectId().equals(xproject.ID))
					{
						found = true;
						break;
					}
				}
				if (found && euser.equals("admin"))
				{
					List<User> epadUsers = projectOperations.getUsersForProject(xproject.ID);
					found = false;
					for (User u: epadUsers)
					{
						if (u.getUsername().equals("admin"))
							found = true;
					}
				}
				if (!found)
				{
					UserRole urole = UserRole.COLLABORATOR;
					if (role.startsWith("Owner"))
						urole = UserRole.OWNER;
					if (role.startsWith("Member"))
						urole = UserRole.MEMBER;
					projectOperations.addUserToProject(username, xproject.ID, euser, urole, "");
					if (role.startsWith("Owner") && project.getCreator().equals(username))
					{
						project.setCreator(euser);
						project.save();
					}
					response = response + "\nAdded user " + euser + " to project " + xproject.ID + " as " + urole;
				}
			}
			XNATSubjectList xsubjects = XNATQueries.getSubjectsForProject(sessionID, xproject.ID);
			List<Subject> psubjects = projectOperations.getSubjectsForProject(xproject.ID);
			for (XNATSubject xsubject: xsubjects.ResultSet.Result)
			{
				log.info("Processing subject:" + xsubject.label);
				Subject subject = projectOperations.getSubject(xsubject.label);
				if (subject == null)
				{
					String subname = trimTrailing(xsubject.src);
					subject = projectOperations.createSubject(username, xsubject.label, xsubject.src, null, null);
					response = response + "\nCreated subject " + xsubject.src;
				}
				XNATExperimentList experiments = XNATQueries.getDICOMExperiments(sessionID, xproject.ID, xsubject.ID);
				for (XNATExperiment experiment: experiments.ResultSet.Result)
				{
					log.info("Processing study:" + experiment.label.replace('_', '.'));
					Study study = projectOperations.getStudy(experiment.label.replace('_', '.'));
					if (study == null)
					{
						study = projectOperations.createStudy(username, experiment.label.replace('_', '.'), subject.getSubjectUID(),"");
						response = response + "\nCreated study " + study.getStudyUID() + " for subject " + subject.getName();
					}
				}
				if (!psubjects.contains(subject))
				{
					projectOperations.addSubjectToProject(username, subject.getSubjectUID(), project.getProjectId());
					response = response + "\nAdded Subject " + subject.getName() + ":" + subject.getSubjectUID() + " to project " + project.getProjectId();
				}
				log.info("Getting XNAT studies for project:" + project.getProjectId());
				Set<String> studyUIDs = XNATQueries.getStudyUIDsForSubject(sessionID, project.getProjectId(), xsubject.ID);
				log.info("Getting EPAD studies for project:" + project.getProjectId());
				List<Study> studies = projectOperations.getStudiesForProjectAndSubject(project.getProjectId(), subject.getSubjectUID());
				for (String studyUID: studyUIDs)
				{
					boolean found = false;
					for (Study s: studies)
					{
						if (s.getStudyUID().equals(studyUID.replace('_', '.')))
						{
							found = true;
							break;
						}
					}
					if (!found)
					{
						projectOperations.addStudyToProject(username, studyUID.replace('_', '.'), subject.getSubjectUID(), project.getProjectId());
						response = response + "\nAdded Study " + studyUID.replace('_', '.') + " to project " + project.getProjectId();
					}
				}
				log.info("Done with subject:" + xsubject.label);
			}
			log.info("Done with project:" + project.getProjectId());
		}
		log.info("Done with all projects");
		response = response + "\nSyncing with XNAT Completed";
		return response;
	}

	private static String generatePassword(String login)
	{
		if (login.equals(EPADConfig.xnatUploadProjectUser))
			return EPADConfig.xnatUploadProjectPassword;
		if (login.equals("guest"))
			return "guest";
		return login;  // Keep password same as login
//		return "" + new IdGenerator().generateId(2) + login + new IdGenerator().generateId(2);
	}
	
	private static String trimTrailing(String xnatName)
	{
		while (xnatName.endsWith("^"))
			xnatName = xnatName.substring(0, xnatName.length()-1);
		String name = xnatName.trim();
		return name;
	}
}
