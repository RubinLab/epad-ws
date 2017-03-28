/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
package edu.stanford.epad.epadws.security;

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
			return hasValidSessionID(jsessionID, httpRequest);
	}

	public static boolean hasValidSessionID(String jsessionID)
	{
		return hasValidSessionID(jsessionID, null);
	}

	public static boolean hasValidSessionID(String jsessionID, HttpServletRequest httpRequest)
	{
		EPADSession session = currentSessions.get(jsessionID);
		if (session != null)
		{
			session.setLastActivity(new Date());
			if (httpRequest != null)
			{
				String url = httpRequest.getRequestURL().toString();
				if (url.indexOf("eventresource") == -1)	
					session.setLastRequest(url);
			}
			session.setLifespan(EPADSessionOperations.SESSION_LIFESPAN);
			return true;
		}
		//log.warning("SessionId:" + jsessionID + " not found in active sessions");
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
//			log.warning("No JSESSIONID cookie present in request " + servletRequest.getRequestURL());
			//secondary authentication mechanism
			String auth = servletRequest.getHeader("Authorization");
			String clientKey = null;
			if (auth != null)
			{
				String[] tokens = auth.split(",");
				for (String t: tokens)
				{
					if (t.length() == 0) continue;
					String[] parts = t.split("=");
					if (parts.length != 2)
					{
						log.warning("Invalid Authorization Token");
						continue;
					}
					if (parts[0].equalsIgnoreCase("JSESSIONID"))
						jSessionID = parts[1];
					else if (parts[0].equalsIgnoreCase("CLIENT_KEY"))
						clientKey = parts[1];
				}
				if (!EPADConfig.getParamValue("CLIENT_KEY", "bb33647e-140e-11e7-93ae-92361f002671").equals(clientKey))
				{
					log.warning("Invalid Authorization Client Key:" + clientKey);
					jSessionID = null;
				}
			}
			else
				log.warning("No JSESSIONID cookie present in request " + servletRequest.getRequestURL() + " method:" + servletRequest.getMethod());

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

