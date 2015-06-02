package edu.stanford.epad.epadws.controllers;

import java.util.Collection;
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
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.security.EPADSession;
import edu.stanford.epad.epadws.service.RemotePACService;
import edu.stanford.epad.epadws.service.SessionService;

@RestController
@RequestMapping("/users")
public class UserController {
	private static final EPADLogger log = EPADLogger.getInstance();
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public EPADUserList getEPADUsers(@RequestParam(value="username") String username, 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADUserList userlist = epadOperations.getUserDescriptions(username, sessionID);
		return userlist;
	}
 
	@RequestMapping(value = "/{user}", method = RequestMethod.GET)
	public EPADUser getEPADUser(@RequestParam(value="username") String username, 
											@PathVariable String user,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADUser euser = epadOperations.getUserDescription(username, user, sessionID);
		if (euser == null)
			throw new NotFoundException("User " + user + " not found");
		return euser;
	}
	 
	@RequestMapping(value = "/{user}/sessions/", method = RequestMethod.GET)
	public Collection<EPADSession> getEPADUserSessions(@RequestParam(value="username") String username, 
											@PathVariable String user,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADUser euser = epadOperations.getUserDescription(username, user, sessionID);
		if (euser == null)
			throw new NotFoundException("User " + user + " not found");
		Collection<EPADSession> sessions = epadOperations.getCurrentSessions(user);
		return sessions;
	}
	 
	@RequestMapping(value = "/{user}/reviewers/", method = RequestMethod.GET)
	public EPADUserList getUserReviewers(@RequestParam(value="username") String username, 
											@PathVariable String user,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		return epadOperations.getReviewers(username, user, sessionID);
	}
	 
	@RequestMapping(value = "/{user}/reviewees/", method = RequestMethod.GET)
	public EPADUserList getUserReviewees(@RequestParam(value="username") String username, 
											@PathVariable String user,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		return epadOperations.getReviewees(username, user, sessionID);
	}
	 
	@RequestMapping(value = "/{user}/{username}", method = RequestMethod.PUT)
	public void createUSer(@RequestParam(value="username") String loggedInUser, 
											@PathVariable String username,
											@RequestParam(value="firstname") String firstname,
											@RequestParam(value="lastname") String lastname,
											@RequestParam(value="email") String email,
											@RequestParam(value="password") String password,
											@RequestParam(value="oldpassword") String oldpassword,
											@RequestParam(value="addPermissions") String[] addPermissions,
											@RequestParam(value="removePermissions") String[] removePermissions,
											@RequestParam(value="enable") String enable,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.createOrModifyUser(loggedInUser, username, firstname, lastname, email, password, oldpassword, addPermissions, removePermissions);
		if ("true".equalsIgnoreCase(enable))
			epadOperations.enableUser(loggedInUser, username);
		else if ("false".equalsIgnoreCase(enable))
			epadOperations.disableUser(loggedInUser, username);
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
