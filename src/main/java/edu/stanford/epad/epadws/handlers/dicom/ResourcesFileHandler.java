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

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.service.SessionService;

/**
 * WADO Handler
 */
public class ResourcesFileHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error in resources route";
	private static final String MISSING_QUERY_MESSAGE = "No relativePath in resources request";
	private static final String INVALID_METHOD = "Only GET methods valid for the resources route";

	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on resources route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		ServletOutputStream responseStream = null;
		String origin = httpRequest.getHeader("Origin"); // CORS request should have Origin header
		int statusCode = HttpServletResponse.SC_OK;

		// Origin header indicates a possible CORS requests
		if (origin != null) {
			httpResponse.setHeader("Access-Control-Allow-Origin", origin);
			httpResponse.setHeader("Access-Control-Allow-Credentials", "true"); // Needed to allow cookies
		} else {
			httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		}
				
		if (request != null)					// In case handler is not called thru jetty
			request.setHandled(true);

		String method = httpRequest.getMethod();
		if ("GET".equalsIgnoreCase(method)) {
			try {
				String sessionID = SessionService.getJSessionIDFromRequest(httpRequest);
				if (sessionID == null || sessionID.length() == 0) {
					log.warning("JSESSIONID is Missing in client request");
					statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, httpResponse.getWriter(), log);
				} else {
					String username = httpRequest.getParameter("username");
					responseStream = httpResponse.getOutputStream();
		
					if (SessionService.hasValidSessionID(httpRequest)) {
						String relativePath = httpRequest.getParameter("relativePath");
						relativePath = URLDecoder.decode(relativePath, "UTF-8");
						if (relativePath != null) {
							File file = new File(EPADConfig.getEPADWebServerResourcesDir() +  relativePath);
							if (!file.exists()) {
								throw new Exception("File " + file.getAbsolutePath() + " does not exist");
							}
							String name = request.getParameter("name");
							if (name == null) name = file.getName();
						    EPADFileUtils.sendFile(request, httpResponse, file, name, false);
						} else {
							statusCode = HandlerUtil.badRequestResponse(MISSING_QUERY_MESSAGE, log);
							log.warning("Missing Resource Path");
						}
					} else {
						statusCode = HandlerUtil.invalidTokenResponse(INVALID_SESSION_TOKEN_MESSAGE, log);
					}
				}
			} catch (Throwable t) {
				statusCode = HandlerUtil.internalErrorResponse(INTERNAL_EXCEPTION_MESSAGE, log);
				log.warning("Error is Resources query", t);
			}
		} else {
			httpResponse.setHeader("Access-Control-Allow-Methods", "GET");
			statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_METHOD_NOT_ALLOWED, INVALID_METHOD, log);
		}
		httpResponse.setStatus(statusCode);
	}
}
