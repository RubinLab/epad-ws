package edu.stanford.epad.epadws.handlers.core;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.service.SessionService;

/**
 * @author martin
 */
public class EPADHandler extends AbstractHandler
{
	private static final String INTERNAL_ERROR_MESSAGE = "Internal error";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";
	private static final String FORBIDDEN_MESSAGE = "Forbidden method - only GET, DELETE, PUT, and POST allowed!";
	private static final String NO_USERNAME_MESSAGE = "Must have username parameter for requests!";

	private static final EPADLogger log = EPADLogger.getInstance();

	/*
	 * Main class for handling rest calls using the epad v2 api.
	 * 
	 * Note: These long if/then/else statements looks terrible, they need to be replaced by something like jersey with annotations
	 * But there seems to be some problem using jersey with embedded jetty and multiple handlers - still need to solve that
	 * 
	 */
	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("application/json");
		request.setHandled(true);

		try {
			if (!"stream".equals(httpRequest.getParameter("format")))
				responseStream = httpResponse.getWriter();
			String method = httpRequest.getMethod();

			String sessionID = SessionService.getJSessionIDFromRequest(httpRequest);
			boolean releaseSession = false;
			if (sessionID == null)
			{
				sessionID = SessionService.getJSessionIDFromRequest(httpRequest, true);
				releaseSession = true;
			}

			String username = httpRequest.getParameter("username");
			if (username == null && sessionID != null)
				username = EPADSessionOperations.getSessionUser(sessionID);
			log.info("User:" + username  + " host:" + EPADSessionOperations.getSessionHost(sessionID) + " method:" + httpRequest.getMethod() 
					+ ", url: " + httpRequest.getPathInfo() + ", parameters: "
					+ httpRequest.getQueryString() + " sessionId:" + sessionID);
			if (SessionService.hasValidSessionID(sessionID)) {
				if (EPADConfig.UseEPADUsersProjects) {
					String sessionUser = EPADSessionOperations.getSessionUser(sessionID);
					if (username != null && !username.equals(sessionUser))
					{
						throw new Exception("Incorrect username in request:" + username + " session belongs to " + sessionUser);
					}
					else
						username = sessionUser;
				}
				if (username != null) {
					if ("GET".equalsIgnoreCase(method)) {
						statusCode = EPADGetHandler.handleGet(httpRequest, httpResponse, responseStream, username, sessionID);
					} else if ("DELETE".equalsIgnoreCase(method)) {
						statusCode = EPADDeleteHandler.handleDelete(httpRequest, responseStream, username, sessionID);
					} else if ("PUT".equalsIgnoreCase(method)) {
						statusCode = EPADPutHandler.handlePut(httpRequest, httpResponse, responseStream, username, sessionID);
					} else if ("POST".equalsIgnoreCase(method)) {
						statusCode = EPADPostHandler.handlePost(httpRequest, responseStream, username, sessionID);
					} else {
						statusCode = HandlerUtil.badRequestJSONResponse(FORBIDDEN_MESSAGE, responseStream, log);
					}
				} else
					statusCode = HandlerUtil.badRequestJSONResponse(NO_USERNAME_MESSAGE, responseStream, log);
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
			if (releaseSession) {
				EPADSessionOperations.invalidateSessionID(sessionID);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warning("Error in handle request:", e);
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, e, responseStream, log);
		}
		log.info("Status returned to client:" + statusCode);
		httpResponse.setStatus(statusCode);
	}
}
