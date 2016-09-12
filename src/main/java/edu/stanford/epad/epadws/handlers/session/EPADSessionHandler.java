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
package edu.stanford.epad.epadws.handlers.session;

import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.security.EPADSessionOperations.EPADSessionResponse;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.SessionService;

/**
 * Handler for EPAD-based session management.
 * <p>
 * To create a session key:
 * <p>
 * <code>curl -v -u [username:password] -X POST http://[host:port]/epad/session/ </code>
 * <p>
 * Returns a session key.
 * <p>
 * To deactivate that key:
 * <p>
 * <code>curl -v -b JSESSIONID=[session_key] -X DELETE http://[host:port]/epad/session/</code>
 * 
 * @author dev
 */
public class EPADSessionHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();

	private static final String MISSING_USER = "Missing user name";
	private static final String INVALID_METHOD_MESSAGE = "Only POST and DELETE methods valid for this route";
	private static final String LOGIN_EXCEPTION_MESSAGE = "Warning: internal login error";
	private static final String LOGOUT_EXCEPTION_MESSAGE = "Warning: internal logout error";
	private static final String UNEXPECTED_XNAT_RESPONSE_MESSAGE = "Warning: unexpected response code from XNAT";
	private static final String UNAUTHORIZED_USER_XNAT_RESPONSE_MESSAGE = "Invalid username or password";
	private static final String DISABLED_USER = "User has been disabled";
	private static final String JSESSIONID_COOKIE = "JSESSIONID";
	private static final String LOGGEDINUSER_COOKIE = "ePADLoggedinUser";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		String origin = httpRequest.getHeader("Origin");
		int statusCode;

		if (request != null)
			request.setHandled(true);
		httpResponse.setContentType("text/plain");

		String method = httpRequest.getMethod();
		log.info("Session request from client " + method + ", s:" + s + ", origin:" + origin + ", params:" + httpRequest.getQueryString());
		if ("POST".equalsIgnoreCase(method)) {
			String username = SessionService.extractUserNameFromAuthorizationHeader(httpRequest);
			boolean formpost = false;
			if (username == null || username.length() == 0)
			{
				username = httpRequest.getParameter("username");
				formpost = true;
			}
			String host = httpRequest.getParameter("hostname");
			String ip = httpRequest.getParameter("hostip");
			if (ip == null && host == null)
			{
				ip = httpRequest.getRemoteAddr();
				host = httpRequest.getRemoteHost();
			}
			if (username.length() != 0) {
				log.info("Login Request, User:" + username  + " hostname:" + host +" ip:" + ip + " origin:" + origin);
				log.info("From httpRequest hostname:" + httpRequest.getRemoteHost() +" ip:" + httpRequest.getRemoteAddr());
				try {
					EPADSessionResponse sessionResponse = SessionService.authenticateUser(httpRequest);
					if (sessionResponse.statusCode == HttpServletResponse.SC_OK) {
						projectOperations.createEventLog(username, null, null, null, null, null, null, null, "User Logged In",  ip + " " + host, false);
						String jsessionID = sessionResponse.response;
						log.info("Successful login to EPAD; SESSIONID=" + jsessionID + " host:" + host + " ip:" + ip + " host from request:" + httpRequest.getRemoteHost() + "-" + httpRequest.getRemoteAddr());
						EPADSessionOperations.setSessionHost(jsessionID, host, ip);
				    	if (formpost)
				    	{
				            Cookie userName = new Cookie(LOGGEDINUSER_COOKIE, username);
				            userName.setMaxAge(8*3600);
				            //userName.setPath("/epad/; Secure; HttpOnly");
				            userName.setPath(httpRequest.getContextPath().replace("session/", "").replace("session", ""));
				            httpResponse.addCookie(userName);
							//log.info("Setting HttpOnly, Secure cookie =" + jsessionID);
				            Cookie sessionCookie = new Cookie(JSESSIONID_COOKIE, jsessionID);
				            sessionCookie.setMaxAge(8*3600);
				            
				            //sessionCookie.setPath("/epad/; Secure; HttpOnly");
				            String contextPath = httpRequest.getContextPath().replace("session/", "").replace("session", "");
				            sessionCookie.setPath(contextPath);
				            log.info("WS Secure cookie =" + jsessionID + " path = "+ sessionCookie.getPath());
				            
				            httpResponse.addCookie(sessionCookie);
				            String home = contextPath;
				            if (!home.endsWith("/")) home = home + "/";
				            home =  home  + EPADConfig.getParamValue("HomePage", "Web_pad.html");
				            String redirectUrl = httpRequest.getParameter("redirectUrl");
				            if (redirectUrl != null)
					    		httpResponse.sendRedirect(redirectUrl);
				            else
				    			httpResponse.sendRedirect(home);
				    		return;
				    	}

						httpResponse.setContentType("text/plain");
						PrintWriter responseStream = httpResponse.getWriter();
						responseStream.append(jsessionID);
						if ("true".equalsIgnoreCase(EPADConfig.getParamValue("SeparateWebServicesApp")) 
								|| "true".equals(httpRequest.getParameter("setCookies")))
						{
				            Cookie userName = new Cookie(LOGGEDINUSER_COOKIE, username);
				            userName.setMaxAge(8*3600);
				            userName.setPath(httpRequest.getContextPath().replace("session/", "").replace("session", ""));
				            httpResponse.addCookie(userName);
							log.info("Setting JSESSIONID Cookie");
				            Cookie sessionCookie = new Cookie(JSESSIONID_COOKIE, jsessionID);
				            sessionCookie.setMaxAge(8*3600);
				            sessionCookie.setPath(httpRequest.getContextPath().replace("session/", "").replace("session", ""));
				            httpResponse.addCookie(sessionCookie);
//							httpResponse.addHeader("Set-Cookie", "JSESSIONID=" + jsessionID);
						}
						// Origin header indicates a possible CORS requests
						if (origin != null) {
							httpResponse.setHeader("Access-Control-Allow-Origin", origin);
							httpResponse.setHeader("Access-Control-Allow-Credentials", "true"); // Needed to allow cookies
						} else {
							httpResponse.setHeader("Access-Control-Allow-Origin", "*");
						}
						log.info("Successful login to EPAD; JSESSIONID=" + jsessionID);
						statusCode = HttpServletResponse.SC_OK;
				    	
					} else if (sessionResponse.statusCode == HttpServletResponse.SC_UNAUTHORIZED) {
						projectOperations.createEventLog(username, null, null, null, null, null, null, null, "User Logged Failed",  ip + " " + host, true);
						PrintWriter responseStream = httpResponse.getWriter();
						if (sessionResponse.message != null && sessionResponse.message.contains("disabled"))
							statusCode = HandlerUtil.invalidTokenResponse(DISABLED_USER, responseStream, log);
						else
						statusCode = HandlerUtil.invalidTokenResponse(UNAUTHORIZED_USER_XNAT_RESPONSE_MESSAGE, responseStream, log);
					} else {
						PrintWriter responseStream = httpResponse.getWriter();
						statusCode = HandlerUtil.warningResponse(sessionResponse.statusCode, UNEXPECTED_XNAT_RESPONSE_MESSAGE
								+ ";statusCode = " + sessionResponse.statusCode, responseStream, log);
					}
				} catch (Throwable t) {
					statusCode = HandlerUtil.internalErrorResponse(LOGIN_EXCEPTION_MESSAGE, t, log);
				}
			} else {
				statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_USER, log);
			}
		} else if ("DELETE".equalsIgnoreCase(method)) {
			log.info("Logout request, sessionId:" + SessionService.getJSessionIDFromRequest(httpRequest)  + " origin:" + origin);
			try {
				String jsessionID = SessionService.getJSessionIDFromRequest(httpRequest);
				String username = null;
				if (jsessionID != null)
					username = EPADSessionOperations.getSessionUser(jsessionID);
				statusCode = SessionService.invalidateSessionID(httpRequest);
				// Origin header indicates a possible CORS requests
				if (origin != null) {
					httpResponse.setHeader("Access-Control-Allow-Origin", origin);
					httpResponse.setHeader("Access-Control-Allow-Credentials", "true"); // Needed to allow cookies
				} else {
					httpResponse.setHeader("Access-Control-Allow-Origin", "*");
				}
				httpResponse.setHeader("Access-Control-Allow-Methods", "POST, DELETE, OPTIONS");
				//httpResponse.addHeader("Access-Control-Allow-Origin", "*");
				if ("true".equalsIgnoreCase(EPADConfig.getParamValue("SeparateWebServicesApp")))
				{
		            Cookie sessionCookie = new Cookie(JSESSIONID_COOKIE, "");
		            sessionCookie.setMaxAge(0);
		            sessionCookie.setPath(httpRequest.getContextPath() + "/");
		            httpResponse.addCookie(sessionCookie);
		            sessionCookie = new Cookie(JSESSIONID_COOKIE, "");
		            sessionCookie.setMaxAge(0);
		            sessionCookie.setPath(httpRequest.getContextPath());
		            httpResponse.addCookie(sessionCookie);
		            log.info("WS Secure seperatews cookie =" + jsessionID + " path = "+ sessionCookie.getPath());
		            
				}
					if (username != null)
						projectOperations.createEventLog(username, null, null, null, null, null, null, null, "User Logged Out", null, false);
				log.info("Delete session returns status code " + statusCode);
				statusCode = HttpServletResponse.SC_OK;
			} catch (Throwable t) {
				statusCode = HandlerUtil.internalErrorResponse(LOGOUT_EXCEPTION_MESSAGE, t, log);
			}
		} else if ("GET".equalsIgnoreCase(method)) {
			log.info("GET request, sessionId:" + SessionService.getJSessionIDFromRequest(httpRequest));
			try {
				statusCode = HttpServletResponse.SC_OK;
			} catch (Throwable t) {
				statusCode = HandlerUtil.internalErrorResponse(LOGOUT_EXCEPTION_MESSAGE, t, log);
			}
		} else if ("OPTIONS".equalsIgnoreCase(method)) {
			log.info("CORS preflight OPTIONS request to session route" + " origin:" + origin);
			httpResponse.setHeader("Access-Control-Allow-Origin", origin);
//			httpResponse.setHeader("Access-Control-Allow-Origin", "*");
			//httpResponse.addHeader("Access-Control-Allow-Origin", "*");
			httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
			httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization");
			httpResponse.setHeader("Access-Control-Allow-Methods", "POST, DELETE, OPTIONS");
			statusCode = HttpServletResponse.SC_OK;
		} else {
			log.info("Request, Method:" + method  + " origin:" + origin);
			httpResponse.setHeader("Access-Control-Allow-Origin", origin);
			httpResponse.setHeader("Access-Control-Allow-Methods", "POST, DELETE, OPTIONS");
			statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_METHOD_NOT_ALLOWED, INVALID_METHOD_MESSAGE
					+ "; got " + method, log);
		}
		log.info("Status returned to client:" + statusCode);
		httpResponse.setStatus(statusCode);
	}
}