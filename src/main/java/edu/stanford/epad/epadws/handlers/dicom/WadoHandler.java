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
package edu.stanford.epad.epadws.handlers.dicom;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.service.SessionService;

/**
 * WADO Handler
 */
public class WadoHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error in WADO route";
	private static final String MISSING_QUERY_MESSAGE = "No query in WADO request";
	private static final String INVALID_METHOD = "Only GET methods valid for the WADO route";

	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on WADO route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		ServletOutputStream responseStream = null;
		String origin = httpRequest.getHeader("Origin"); // CORS request should have Origin header
		int statusCode;

		// Origin header indicates a possible CORS requests
		if (origin != null) {
			httpResponse.setHeader("Access-Control-Allow-Origin", origin);
			httpResponse.setHeader("Access-Control-Allow-Credentials", "true"); // Needed to allow cookies
		} else {
			httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		}
		
		if (httpRequest.getQueryString().indexOf("dicom") != -1)
			httpResponse.setContentType("application/octet-stream");
		else
			httpResponse.setContentType("image/jpeg");
		
		if (request != null)					// In case handler is not called thru jetty
			request.setHandled(true);

		String method = httpRequest.getMethod();
		if ("GET".equalsIgnoreCase(method)) {
			try {
				String sessionID = SessionService.getJSessionIDFromRequest(httpRequest);
				if (sessionID == null || sessionID.length() == 0) {
					log.warning("JSESSIONID is Missing in client request");
					//statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, httpResponse.getWriter(), log);
				}
					String username = httpRequest.getParameter("username");
					responseStream = httpResponse.getOutputStream();
	
					// if (XNATOperations.hasValidXNATSessionID(httpRequest)) {
					if (dummy()) { // TODO Re-enable authentication
						String queryString = httpRequest.getQueryString();
						queryString = URLDecoder.decode(queryString, "UTF-8");
						if (queryString != null) {
							statusCode = performWADOQuery(queryString, responseStream, username, sessionID);
						} else {
							statusCode = HandlerUtil.badRequestResponse(MISSING_QUERY_MESSAGE, log);
							log.warning("Missing Wado query");
						}
					} else {
						statusCode = HandlerUtil.invalidTokenResponse(INVALID_SESSION_TOKEN_MESSAGE, log);
					}
				//}
			} catch (Throwable t) {
				statusCode = HandlerUtil.internalErrorResponse(INTERNAL_EXCEPTION_MESSAGE, log);
				log.warning("Error is Wado query", t);
			}
		} else {
			httpResponse.setHeader("Access-Control-Allow-Methods", "GET");
			statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_METHOD_NOT_ALLOWED, INVALID_METHOD, log);
		}
		httpResponse.setStatus(statusCode);
	}

	private boolean dummy()
	{
		return true;
	}

	private int performWADOQuery(String queryString, ServletOutputStream outputStream, String username, String sessionID)

	{
		String wadoHost = EPADConfig.dcm4CheeServer;
		int wadoPort = EPADConfig.dcm4cheeServerWadoPort;
		String wadoBase = EPADConfig.wadoURLExtension;
		if (queryString.toLowerCase().indexOf("dicom") != -1)
		{
			log.info("User:" + username  + " host:" + EPADSessionOperations.getSessionHost(sessionID) 
					+ " Wado Request to download dicom:" + queryString);
		}
		String wadoURL = buildWADOURL(wadoHost, wadoPort, wadoBase, queryString);
		int statusCode;
		try {
			statusCode = HandlerUtil.streamGetResponse(wadoURL, outputStream, log);
			if (statusCode != HttpServletResponse.SC_OK)
				log.warning("Unexpected response " + statusCode + " to WADO request " + wadoURL);
		} catch (HttpException e) {
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_EXCEPTION_MESSAGE, log);
		} catch (IOException e) {
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_EXCEPTION_MESSAGE, log);
		}
		return statusCode;
	}

	private String buildWADOURL(String host, int port, String base, String queryString)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("http://").append(host);
		sb.append(":").append(port);
		sb.append(base);
		sb.append(queryString);
		return sb.toString();
	}
}
