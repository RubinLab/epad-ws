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

}
