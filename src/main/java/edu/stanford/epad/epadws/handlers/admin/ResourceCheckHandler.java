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
package edu.stanford.epad.epadws.handlers.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.HandlerUtil;

/**
 * Provide access control.
 * <p>
 * Also allows CORS requests to support cross origin modification of retrieved resources.
 * 
 * @author martin
 * 
 */
public class ResourceCheckHandler extends AbstractHandler
{
	private final EPADLogger log = EPADLogger.getInstance();

	private static final String INTERNAL_ERROR_MESSAGE = "Internal server error on resource route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on resource route";

	@Override
	public void handle(String base, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		String origin = httpRequest.getHeader("Origin"); // CORS request should have Origin header
		int statusCode;

		// Origin header indicates a possible CORS requests, which we support to allow drawing on canvas in GWT Dev Mode.
		if (origin != null) {
			httpResponse.setHeader("Access-Control-Allow-Origin", origin);
			httpResponse.setHeader("Access-Control-Allow-Credentials", "true"); // Needed to allow cookies.
		} else {
			httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		}

		try {
			// if (XNATOperations.hasValidXNATSessionID(httpRequest)) {
			if (dummy()) { // TODO Turn off authorization for the moment
				request.setHandled(false);
				statusCode = HttpServletResponse.SC_OK;
			} else {
				statusCode = HandlerUtil.invalidTokenResponse(INVALID_SESSION_TOKEN_MESSAGE, log);
				request.setHandled(true);
			}
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_ERROR_MESSAGE, t, log);
			request.setHandled(true);
		}
		httpResponse.setStatus(statusCode);
	}

	private boolean dummy()
	{
		return true;
	}
}
