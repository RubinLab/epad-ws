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
import java.util.Date;
import java.util.HashMap;
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
import edu.stanford.epad.epadws.xnat.XNATSessionOperations.XNATSessionResponse;

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
	private static final int SESSION_LIFESPAN = 3600;  // 1 hour in secs 
	
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
		try {
			EPADSession session = EPADSessionOperations.createNewEPADSession(username, password);
			EPADSessionResponse response = new EPADSessionResponse(HttpServletResponse.SC_OK, session.getSessionId(), "");
			log.info("Session ID " + response.response + " generated for user " + username);
			return response;
		} catch (Exception x) {
			EPADSessionResponse response = new EPADSessionResponse(HttpServletResponse.SC_UNAUTHORIZED, null, x.getMessage());
			return response;
		}
	}

	public static int invalidateSessionID(HttpServletRequest httpRequest)
	{
		String jsessionID = getJSessionIDFromRequest(httpRequest);
		if (hasValidSessionID(jsessionID))
		{
			currentSessions.remove(jsessionID);
			return HttpServletResponse.SC_OK;
		}
		else
			return HttpServletResponse.SC_BAD_REQUEST;
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
			log.warning("No JSESESSIONID cookie present in request " + servletRequest.getRequestURL());
		}
		else
		{
			int comma = jSessionID.indexOf(",");
			if (comma != -1)
				jSessionID = jSessionID.substring(0, comma);
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

	private static EPADSession createNewEPADSession(String username, String password) throws Exception
	{
		User user = projectOperations.getUser(username);
		if (user == null)
			throw new Exception("User " + username + " not found");
		if (!user.isEnabled())
			throw new Exception("User " + username + " is disabled");
		if (user.getPassword().length() >= 60 && BCrypt.checkpw(password, user.getPassword()))
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
				log.debug("Password:" + user.getPassword() + " Entered:" + password);
				if (password.equals(user.getPassword()))
				{
					String sessionId = idGenerator.generateId(16);
					EPADSession session = new EPADSession(sessionId, username, SESSION_LIFESPAN);
					currentSessions.put(sessionId, session);
					return session;
				}
			}
			throw new Exception("Error creating new session, invalid password");
		}
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

}

