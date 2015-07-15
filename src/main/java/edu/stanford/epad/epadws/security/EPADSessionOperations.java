package edu.stanford.epad.epadws.security;

//Copyright (c) 2014 The Board of Trustees of the Leland Stanford Junior University
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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.mindrot.jbcrypt.BCrypt;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;

/**
 * EPAD session management methods
 * 
 * @author dev (based on XNATSessionOperations)
 * 
 */
public class EPADSessionOperations
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final String LOGIN_EXCEPTION_MESSAGE = "Internal login error";
	private static Map<String, EPADSession> currentSessions = new HashMap<String, EPADSession>();
	private static EPADSession adminSession = null;
	private static final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
	private static final IdGenerator idGenerator = new IdGenerator();
	private static final int SESSION_LIFESPAN = 60;  // 1 hour in mins 
	
	public static class EPADSessionResponse
	{
		public final int statusCode;
		public final String response;
		public final String message;

		public EPADSessionResponse(int responseCode, String response, String message)
		{
			this.statusCode = responseCode;
			this.response = response;
			this.message = message;
		}
	}
	
	public static String getAdminSessionID() throws Exception
	{
		if (adminSession != null && adminSession.isValid())
		{
			return adminSession.getSessionId();
		}

		String xnatUploadProjectUser = EPADConfig.xnatUploadProjectUser;
		String xnatUploadProjectPassword = EPADConfig.xnatUploadProjectPassword;

		log.info("Getting EPAD Admin Session");
		EPADSession session = EPADSessionOperations.createNewEPADSession(xnatUploadProjectUser,
				xnatUploadProjectPassword);
		if (session != null)
		{
			adminSession = session;
			return session.getSessionId();
		}
		else
			throw new Exception("Error getting admin session");
	}

	public static EPADSessionResponse authenticateUser(HttpServletRequest httpRequest)
	{
		String username = extractUserNameFromAuthorizationHeader(httpRequest);
		String password = extractPasswordFromAuthorizationHeader(httpRequest);
		if (username == null || username.length() == 0) {
			username = httpRequest.getParameter("username");
			password = httpRequest.getParameter("password");
		}
		EPADSession session = null;
		try {
			if (username != null && password == null && httpRequest.getParameter("adminuser") != null) {
				session = EPADSessionOperations.createProxySession(username, httpRequest.getParameter("adminuser"), httpRequest.getParameter("adminpassword"));				
			} else  if (username == null && httpRequest.getAuthType().equals("WebAuth") && httpRequest.getRemoteUser() != null) {
				if (password != null && password.equals(EPADConfig.webAuthPassword))
					session = EPADSessionOperations.createPreAuthenticatedSession(httpRequest.getRemoteUser());				
			} else {
				session = EPADSessionOperations.createNewEPADSession(username, password);
			}
			EPADSessionResponse response = new EPADSessionResponse(HttpServletResponse.SC_OK, session.getSessionId(), "");
			log.info("Session ID " + response.response + " generated for user " + username);
			return response;
		} catch (Exception x) {
			EPADSessionResponse response = new EPADSessionResponse(HttpServletResponse.SC_UNAUTHORIZED, null, x.getMessage());
			return response;
		}
	}
	
	public static String authenticateWebAuthUser(String username, String password) throws Exception
	{
		if (password != null && password.trim().length() > 0) {
			if (password.equals(EPADConfig.webAuthPassword)) {
				EPADSession session = EPADSessionOperations.createPreAuthenticatedSession(username);
				return session.getSessionId();
			}
		}
		return null;
	}

	public static int invalidateSessionID(HttpServletRequest httpRequest)
	{
		String jsessionID = getJSessionIDFromRequest(httpRequest);
		if (jsessionID != null)
		{
			invalidateSessionID(jsessionID);
			return HttpServletResponse.SC_OK;
		}
		else
			return HttpServletResponse.SC_BAD_REQUEST;
	}

	public static void invalidateSessionID(String jsessionID)
	{
		EPADSession session = currentSessions.remove(jsessionID);
		if (session != null)
			session.setValid(false);
	}

	public static boolean hasValidSessionID(HttpServletRequest httpRequest)
	{
		String jsessionID = EPADSessionOperations.getJSessionIDFromRequest(httpRequest);

		if (jsessionID == null) // The getJSessionIDFromRequest method logs warning in this case.
			return false;
		else
			return hasValidSessionID(jsessionID);
	}

	public static boolean hasValidSessionID(String jsessionID)
	{
		EPADSession session = currentSessions.get(jsessionID);
		if (session != null)
		{
			session.setLastActivity(new Date());
			session.setLifespan(EPADSessionOperations.SESSION_LIFESPAN);
			return true;
		}
		log.warning("SessionId:" + jsessionID + " not found in active sessions");
		return false;
	}

	public static String getJSessionIDFromRequest(HttpServletRequest servletRequest)
	{
		String jSessionID = null;

		Cookie[] cookies = servletRequest.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("JSESSIONID".equalsIgnoreCase(cookie.getName())) {
					jSessionID = cookie.getValue();
					break;
				}
			}
		}
		if (jSessionID == null)
		{
			log.warning("No JSESSIONID cookie present in request " + servletRequest.getRequestURL());
		}
		else
		{
			int comma = jSessionID.indexOf(",");
			if (comma != -1)
			{
				log.warning("Multiple cookies:" + jSessionID);
				jSessionID = jSessionID.substring(0, comma);
			}
		}
		return jSessionID;
	}

	public static String extractUserNameFromAuthorizationHeader(HttpServletRequest httpRequest)
	{
		String credentials = extractCredentialsFromAuthorizationHeader(httpRequest);
		String[] values = credentials.split(":", 2);

		if (values.length != 0 && values[0] != null)
			return values[0];
		else
			return "";
	}

	public static void checkSessionTimeout()
	{
		List<String> expiredSessions = new ArrayList<String>();
		for (String sessionID: currentSessions.keySet())
		{
			EPADSession session = currentSessions.get(sessionID);
			int lifespan = session.getLifespan();
			lifespan--;
			session.setLifespan(lifespan);
			if (lifespan == 0)
			{
				session.setValid(false);
				expiredSessions.add(sessionID);
			}
		}
		for (String sessionID: expiredSessions)
		{
			currentSessions.remove(sessionID);
		}
	}
	
	public static void setSessionHost(String sessionID, String hostName, String hostAddr)
	{
		EPADSession session = currentSessions.get(sessionID);
		if (session != null)
		{
			session.setRemoteHost(hostName);
			session.setRemoteAddr(hostAddr);
		}
	}
	
	public static String getSessionHost(String sessionID)
	{
		EPADSession session = currentSessions.get(sessionID);
		if (session != null)
		{
			if (session.getRemoteHost() == null)
				return session.getRemoteAddr();
			else if (session.getRemoteAddr() == null)
				return session.getRemoteHost();
			else if (session.getRemoteAddr().equals(session.getRemoteHost()))
				return session.getRemoteHost();
			else
				return session.getRemoteHost() + ":" + session.getRemoteAddr();
		}
		else
			return null;
	}
	
	private static EPADSession createNewEPADSession(String username, String password) throws Exception
	{
		User user = projectOperations.getUser(username);
		if (user == null)
			throw new Exception("User " + username + " not found");
		if (!user.isEnabled())
			throw new Exception("User " + username + " is disabled");
		String webAuthPassword = EPADConfig.webAuthPassword;
		if (user.getPassword().length() >= 60 && BCrypt.checkpw(password, user.getPassword()))
		{
			String sessionId = idGenerator.generateId(16);
			EPADSession session = new EPADSession(sessionId, username, SESSION_LIFESPAN);
			currentSessions.put(sessionId, session);
			return session;
		}
		else if (webAuthPassword != null && webAuthPassword.length() > 0 
				&& user.getPassword().length() >= 60 && BCrypt.checkpw(password, webAuthPassword))
		{
			String sessionId = idGenerator.generateId(16);
			EPADSession session = new EPADSession(sessionId, username, SESSION_LIFESPAN);
			currentSessions.put(sessionId, session);
			return session;
		}
		else
		{
			log.debug("Invalid Password:" + user.getPassword() + " Entered:" + password);
			if (user.getPassword().length() < 60) // clear password set by admin manually on rare occasions
			{
				if (password.equals(user.getPassword()))
				{
					if (!user.isPasswordExpired())
					{
						user.setPasswordExpired(true);
						user.save();
					}
					String sessionId = idGenerator.generateId(16);
					EPADSession session = new EPADSession(sessionId, username, SESSION_LIFESPAN);
					currentSessions.put(sessionId, session);
					return session;
				}
			}
			throw new Exception("Error creating new session, invalid password");
		}
	}
	
	private static EPADSession createProxySession(String username, String adminuser, String adminpassword) throws Exception
	{
		User user = projectOperations.getUser(username);
		if (user == null)
			throw new Exception("User " + username + " not found");
		if (!user.isEnabled())
			throw new Exception("User " + username + " is disabled");
		User admin = projectOperations.getUser(adminuser);
		if (admin == null)
			throw new Exception("User " + admin + " not found");
		if (!admin.isEnabled())
			throw new Exception("User " + admin + " is disabled");
		if (user.getPassword().length() >= 60 && BCrypt.checkpw(adminpassword, admin.getPassword()))
		{
			String sessionId = idGenerator.generateId(16);
			EPADSession session = new EPADSession(sessionId, username, SESSION_LIFESPAN);
			currentSessions.put(sessionId, session);
			return session;
		}
		else
		{
			if (user.getPassword().length() < 60) // clear password set by admin manually on rare occasions
			{
				if (adminpassword.equals(admin.getPassword()))
				{
					if (!admin.isPasswordExpired())
					{
						admin.setPasswordExpired(true);
						admin.save();
					}
					String sessionId = idGenerator.generateId(16);
					EPADSession session = new EPADSession(sessionId, username, SESSION_LIFESPAN);
					currentSessions.put(sessionId, session);
					return session;
				}
			}
			throw new Exception("Error creating new session, invalid admin password");
		}
	}
	
	private static EPADSession createPreAuthenticatedSession(String username) throws Exception
	{
		User user = projectOperations.getUser(username);
		if (user == null)
			throw new Exception("User " + username + " not found");
		String sessionId = idGenerator.generateId(16);
		EPADSession session = new EPADSession(sessionId, username, SESSION_LIFESPAN);
		currentSessions.put(sessionId, session);
		return session;
	}

	private static String extractPasswordFromAuthorizationHeader(HttpServletRequest request)
	{
		String credentials = extractCredentialsFromAuthorizationHeader(request);
		String[] values = credentials.split(":", 2);
		if (values.length > 1 && values[1] != null)
			return values[1];
		else
			return "";
	}

	private static String[] extractUsernamePasswordFromAuthorizationHeader(HttpServletRequest request)
	{
		String credentials = extractCredentialsFromAuthorizationHeader(request);
		String[] values = credentials.split(":", 2);
		if (values.length > 1)
			return values;
		else
			return null;
	}

	private static String extractCredentialsFromAuthorizationHeader(HttpServletRequest request)
	{
		String authorizationHeader = request.getHeader("Authorization");
		String credentials = "";

		if (authorizationHeader != null && authorizationHeader.startsWith("Basic")) {
			String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
			credentials = new String(Base64.decodeBase64(base64Credentials), Charset.forName("UTF-8"));
		}
		return credentials;
	}

	public static Map<String, EPADSession> getCurrentSessions() {
		return currentSessions;
	}
	
	public static String getSessionUser(String sessionID)
	{
		EPADSession session = currentSessions.get(sessionID);
		if (session != null)
			return session.getUsername();
		else
			return null;
	}
}

