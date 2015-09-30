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
package edu.stanford.epad.epadws.controllers;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADUser;
import edu.stanford.epad.dtos.EPADUserList;
import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.dtos.RemotePACEntity;
import edu.stanford.epad.dtos.RemotePACEntityList;
import edu.stanford.epad.dtos.RemotePACList;
import edu.stanford.epad.dtos.RemotePACQueryConfig;
import edu.stanford.epad.dtos.RemotePACQueryConfigList;
import edu.stanford.epad.epadws.controllers.exceptions.NotFoundException;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.core.UsersRouteTemplates;
import edu.stanford.epad.epadws.models.RemotePACQuery;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.security.EPADSession;
import edu.stanford.epad.epadws.service.DefaultWorkListOperations;
import edu.stanford.epad.epadws.service.EpadWorkListOperations;
import edu.stanford.epad.epadws.service.RemotePACService;
import edu.stanford.epad.epadws.service.SessionService;

@RestController
@RequestMapping("/users")
public class UserController {
	private static final EPADLogger log = EPADLogger.getInstance();
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public EPADUserList getEPADUsers( 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADUserList userlist = epadOperations.getUserDescriptions(username, sessionID);
		return userlist;
	}
 
	@RequestMapping(value = "/{user}", method = RequestMethod.GET)
	public EPADUser getEPADUser( 
											@PathVariable String user,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADUser euser = epadOperations.getUserDescription(username, user, sessionID);
		if (euser == null)
			throw new NotFoundException("User " + user + " not found");
		return euser;
	}
	 
	@RequestMapping(value = "/{user}/sessions/", method = RequestMethod.GET)
	public Collection<EPADSession> getEPADUserSessions( 
											@PathVariable String user,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADUser euser = epadOperations.getUserDescription(username, user, sessionID);
		if (euser == null)
			throw new NotFoundException("User " + user + " not found");
		Collection<EPADSession> sessions = epadOperations.getCurrentSessions(user);
		return sessions;
	}
	 
	@RequestMapping(value = "/{user}/reviewers/", method = RequestMethod.GET)
	public EPADUserList getUserReviewers( 
											@PathVariable String user,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		return epadOperations.getReviewers(username, user, sessionID);
	}
	 
	@RequestMapping(value = "/{user}/reviewees/", method = RequestMethod.GET)
	public EPADUserList getUserReviewees( 
											@PathVariable String user,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		return epadOperations.getReviewees(username, user, sessionID);
	}
	 
	@RequestMapping(value = "/{user}/{username}", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createUser( 
											@PathVariable String username,
											@RequestParam(value="firstname") String firstname,
											@RequestParam(value="lastname") String lastname,
											@RequestParam(value="email") String email,
											@RequestParam(value="password") String password,
											@RequestParam(value="oldpassword") String oldpassword,
											@RequestParam(value="colorpreference") String colorpreference,
											@RequestParam(value="addPermissions") String[] addPermissions,
											@RequestParam(value="removePermissions") String[] removePermissions,
											@RequestParam(value="enable") String enable,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String loggedInUser = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.createOrModifyUser(loggedInUser, username, firstname, lastname, email, password, oldpassword, colorpreference, addPermissions, removePermissions);
		if ("true".equalsIgnoreCase(enable))
			epadOperations.enableUser(loggedInUser, username);
		else if ("false".equalsIgnoreCase(enable))
			epadOperations.disableUser(loggedInUser, username);
	}
	
	 
	@RequestMapping(value = "/{user}/{username}", method = RequestMethod.DELETE)
	public void deleteUser( 
			@PathVariable String username,
			HttpServletRequest request, 
	        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String loggedInUser = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.deleteUser(loggedInUser, username);
	}
	
	@RequestMapping(value = "/{reader}/worklists/{workListID:.+}", method = RequestMethod.PUT)
	public void createUserWorkList( 
										@PathVariable String projectID,
										@PathVariable String reader,
										@PathVariable String workListID,
										@RequestParam(value="name", required=false) String name,
										@RequestParam(value="description", required=false) String description,
										@RequestParam(value="dueDate", required=false) String dueDate,
										@RequestParam(value="status", required=false) String wlstatus,
										@RequestParam(value="started", required=false) Boolean started,
										@RequestParam(value="completed", required=false) Boolean completed,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadWorkListOperations worklistOperations = DefaultWorkListOperations.getInstance();
		WorkList worklist = worklistOperations.getWorkList(workListID);
		if (worklist == null)
		{
			worklist = worklistOperations.createWorkList(username, reader, workListID, name, description, null, getDate(dueDate));
		}
		else
		{
			if (description != null || dueDate != null)
				worklistOperations.updateWorkList(username, reader, workListID, name, description, null, getDate(dueDate));
		}
		if (wlstatus != null || started != null || completed != null)
		{
			log.info("WorklistID:" + workListID + " status:" + wlstatus + " started:" + started + " completed:" + completed);
			worklistOperations.setWorkListStatus(reader, workListID, wlstatus, started, completed);
		}
	}
	
	@RequestMapping(value = "/{reader}/worklists/", method = {RequestMethod.POST,RequestMethod.PUT})
	public void createUserWorkList( 
										@PathVariable String projectID,
										@PathVariable String reader,
										@RequestParam(value="description", required=false) String description,
										@RequestParam(value="name", required=false) String name,
										@RequestParam(value="dueDate", required=false) String dueDate,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadWorkListOperations worklistOperations = DefaultWorkListOperations.getInstance();
		worklistOperations.createWorkList(username, reader, null, name, description, null, getDate(dueDate));
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
	
	
//	
//} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_WORKLIST, pathInfo)) {
//	Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_WORKLIST, pathInfo);
//	String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
//	String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
//	String wlstatus = httpRequest.getParameter("status");
//	boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
//	boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
//	worklistOperations.setWorkListStatus(reader, workListID, wlstatus, started, completed);
//	statusCode = HttpServletResponse.SC_OK;
//	
//} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_SUBJECT, pathInfo)) {
//	Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_SUBJECT, pathInfo);
//	String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
//	String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subjectID");
//	String wlstatus = httpRequest.getParameter("status");
//	boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
//	boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
//	Set<WorkList> wls = worklistOperations.getWorkListsForUserBySubject(username, subjectID);
//	for (WorkList wl: wls)
//		worklistOperations.setWorkListSubjectStatus(reader, wl.getWorkListID(), subjectID, wlstatus, started, completed);
//	statusCode = HttpServletResponse.SC_OK;
//
//} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_STUDY, pathInfo)) {
//	Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_STUDY, pathInfo);
//	String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
//	String studyUID = HandlerUtil.getTemplateParameter(templateMap, "studyUID");
//	String wlstatus = httpRequest.getParameter("status");
//	boolean started = "true".equalsIgnoreCase(httpRequest.getParameter("started"));
//	boolean completed = "true".equalsIgnoreCase(httpRequest.getParameter("completed"));
//	Set<WorkList> wls = worklistOperations.getWorkListsForUserByStudy(username, studyUID);
//	for (WorkList wl: wls)
//		worklistOperations.setWorkListSubjectStatus(reader, wl.getWorkListID(), studyUID, wlstatus, started, completed);
//	statusCode = HttpServletResponse.SC_OK;
//
//} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_REVIEWEE, pathInfo)) {
//	Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_REVIEWEE, pathInfo);
//	String reviewer = HandlerUtil.getTemplateParameter(templateMap, "username");
//	String reviewee = HandlerUtil.getTemplateParameter(templateMap, "reviewee");
//	epadOperations.addReviewee(username, reviewer, reviewee);
//	statusCode = HttpServletResponse.SC_OK;
//
//} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_REVIEWER, pathInfo)) {
//	Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_REVIEWEE, pathInfo);
//	String reviewee = HandlerUtil.getTemplateParameter(templateMap, "username");
//	String reviewer = HandlerUtil.getTemplateParameter(templateMap, "reviewer");
//	epadOperations.addReviewer(username, reviewee, reviewer);
//	statusCode = HttpServletResponse.SC_OK;


}
