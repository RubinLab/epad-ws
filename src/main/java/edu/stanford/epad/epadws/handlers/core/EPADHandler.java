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
	public static final String INTERNAL_ERROR_MESSAGE = "Internal error";
	public static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";
	public static final String FORBIDDEN_MESSAGE = "Forbidden method - only GET, DELETE, PUT, and POST allowed!";
	public static final String NO_USERNAME_MESSAGE = "Must have username parameter for requests!";

	private static final EPADLogger log = EPADLogger.getInstance();

	/*
	 * Main class for handling rest calls using the epad v2 api.
	 * 
	 * Note: These long if/then/else statements looks terrible, they need to be replaced by something like jersey with annotations
	 * But there seems to be some problem using jersey with embedded jetty and multiple handlers - still need to solve that
	 *
	 * Note: This class (including the other handlers called) will soon become obsolete and be replaced by Spring Controllers
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
						statusCode = HandlerUtil.badRequestResponse("Incorrect username in request:" + username + " session belongs to " + sessionUser, log);
						httpResponse.setStatus(statusCode);
						return;
						//throw new Exception("Incorrect username in request:" + username + " session belongs to " + sessionUser);
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
