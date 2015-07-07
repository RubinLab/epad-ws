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
package edu.stanford.epad.epadws.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADMessage;
import edu.stanford.epad.epadws.Main;
import edu.stanford.epad.epadws.handlers.core.EPADHandler;
import edu.stanford.epad.epadws.service.SessionService;

public class WebAuthFilter implements Filter {

	private static final EPADLogger log = EPADLogger.getInstance();
	private static final String WEBAUTH_HEADER = "X-WEBAUTH-USER";
	private static final String JSESSIONID_COOKIE = "JSESSIONID";
	private static final String LOGGEDINUSER_COOKIE = "ePADLoggedinUser";
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String webAuthUser = httpRequest.getHeader(WEBAUTH_HEADER);
	    if (webAuthUser != null && webAuthUser.length() > 0 && EPADConfig.webAuthPassword != null)
		{
			String sessionID = SessionService.getJSessionIDFromRequest(httpRequest);
			if (SessionService.hasValidSessionID(sessionID)) {
				String sessionUser = EPADSessionOperations.getSessionUser(sessionID);
				if (!sessionUser.equals(webAuthUser))
					sessionID = null;
			} else {
				sessionID = null;
			}
			if (sessionID == null) {
				try {
					// Note: On the client, this call should be SessionResource.createSession(List<String>() with username, password)
					log.info("WebAuth login request from user:" + webAuthUser  + " host: " + httpRequest.getRemoteHost() +":" + httpRequest.getRemoteAddr());
					sessionID = EPADSessionOperations.authenticateWebAuthUser(webAuthUser, EPADConfig.webAuthPassword);
		            EPADSessionOperations.setSessionHost(sessionID, httpRequest.getRemoteHost(), httpRequest.getRemoteAddr());
					Cookie userName = new Cookie(LOGGEDINUSER_COOKIE, webAuthUser);
		            userName.setMaxAge(-1);
		            userName.setPath(httpRequest.getContextPath());
		            httpResponse.addCookie(userName);
		            Cookie sessionCookie = new Cookie(JSESSIONID_COOKIE, sessionID);
		            sessionCookie.setMaxAge(-1);
		            sessionCookie.setPath(httpRequest.getContextPath());
		            httpResponse.addCookie(sessionCookie);
		            if (httpRequest.getRequestURL().toString().indexOf("Web_pad") == -1)
		            	httpResponse.sendRedirect("Web_pad.html");
		            return;
				} catch (Exception e) {
					log.warning("Error logging in WebAuth User", e);
				}
			}
		} else {
			String sessionID = SessionService.getJSessionIDFromRequest(httpRequest);
			String method = httpRequest.getMethod();
			boolean isValid = SessionService.hasValidSessionID(sessionID);
			if (!isValid && httpRequest.getRequestURL().toString().indexOf("login.jsp") == -1 
					&& !httpRequest.getRequestURL().toString().contains("/session") 
					&& !httpRequest.getRequestURL().toString().contains("/eventresource") 
					&& !httpRequest.getHeader("User-Agent").contains("HttpClient") 
					&& !httpRequest.getHeader("user-agent").contains("HttpClient") 
					&& !httpRequest.getRequestURL().toString().contains("/v2") && Main.separateWebServicesApp) {
            	log.info("Redirecting url:" + httpRequest.getRequestURL() + " to login.jsp");
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            	return;
			}
			if (httpRequest.getRequestURL().indexOf("/v2/") != -1 || httpRequest.getRequestURL().indexOf("/plugin/") != -1)				
				log.info(httpRequest.getMethod() + " Request from client:" + httpRequest.getRequestURL());
			if (!isValid && !httpRequest.getRequestURL().toString().contains("WADO") 
					&& !httpRequest.getRequestURL().toString().contains("/session") 
					&& !httpRequest.getRequestURL().toString().contains("login.jsp"))
			{
				if ("OPTIONS".equalsIgnoreCase(method)) {			
					String origin = httpRequest.getHeader("Origin");
					httpResponse.setHeader("Access-Control-Allow-Origin", origin);
					httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
					httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization");
					httpResponse.setHeader("Access-Control-Allow-Methods", "POST, DELETE, PUT, GET, OPTIONS");
					httpResponse.setStatus(HttpServletResponse.SC_OK);
					return;
				} else {
					PrintWriter responseStream = httpResponse.getWriter();
					responseStream.append(new EPADMessage(EPADHandler.INVALID_SESSION_TOKEN_MESSAGE).toJSON());
					httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}
			}
		}
	    if (httpRequest.getRequestURL().indexOf("WEB-INF") != -1) {
	    	// How come, this weird jetty allows this?
			PrintWriter pw = httpResponse.getWriter();
			httpResponse.setContentType("text/html");
			pw.append("<html> <head> <meta http-equiv='Content-Type' content='text/html;charset=ISO-8859-1'/>" 
					+ "<title>Error 403 Forbidden</title>"
					+ "</head> <body> <h2>HTTP ERROR: 403 Forbidden</h2>"
					+ "</body></html>");
			httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
	    }
        filterChain.doFilter(request, response);
		return;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
