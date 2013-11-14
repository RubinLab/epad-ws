package edu.stanford.isis.epadws.handlers.xnat;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.xnat.XNATUtil;
import edu.stanford.isis.epadws.xnat.XNATUtil.XNATSessionResponse;

/**
 * Handler for XNAT-based session management.
 * <p>
 * To create a session key:
 * <p>
 * <code>curl -v -u [username:password] -X POST http://[host:port]/session/ </code>
 * <p>
 * Returns a session key.
 * <p>
 * To deactivate that key:
 * <p>
 * <code>curl -v -b JSESSIONID=[session_key] -X DELETE http://[host:port]/session/</code>
 * 
 * @author martin
 */
public class XNATSessionHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String MISSING_USERNAME_MESSAGE = "Missing user name";
	private static final String INVALID_METHOD_MESSAGE = "Only POST and DELETE methods valid for this route";
	private static final String LOGIN_EXCEPTION_MESSAGE = "Internal login error";
	private static final String LOGOUT_EXCEPTION_MESSAGE = "Internal logout error";
	private static final String UNEXPECTED_XNAT_RESPONSE_MESSAGE = "Unexpected response code from XNAT";
	private static final String UNAUTHORIZED_USER_XNAT_RESPONSE_MESSAGE = "Invalid username or password";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException
	{
		PrintWriter responseStream = httpResponse.getWriter();
		String origin = httpRequest.getHeader("Origin");
		int statusCode;

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		String method = httpRequest.getMethod();
		if ("POST".equalsIgnoreCase(method)) {
			String username = XNATUtil.extractUserNameFromAuthorizationHeader(httpRequest);
			if (username.length() != 0) {
				log.info("XNATSessionHandler, login request from user " + username);
				try {
					XNATSessionResponse xnatSessionResponse = XNATUtil.invokeXNATSessionIDService(httpRequest);
					if (xnatSessionResponse.statusCode == HttpServletResponse.SC_OK) {
						String jsessionID = xnatSessionResponse.response;
						responseStream.append(jsessionID);
						httpResponse.addHeader("Set-Cookie", "JSESSIONID=" + jsessionID + ";path=/");
						httpResponse.addHeader("Set-Cookie", "ePADLoggedinUser=" + username + ";path=/");
						httpResponse.addHeader("Access-Control-Allow-Origin", origin);
						httpResponse.addHeader("Access-Control-Allow-Credentials", "true");
						statusCode = HttpServletResponse.SC_OK;
					} else if (xnatSessionResponse.statusCode == HttpServletResponse.SC_UNAUTHORIZED) {
						log.info(UNAUTHORIZED_USER_XNAT_RESPONSE_MESSAGE);
						responseStream.append(UNAUTHORIZED_USER_XNAT_RESPONSE_MESSAGE);
						statusCode = HttpServletResponse.SC_UNAUTHORIZED;
					} else {
						log.info(UNEXPECTED_XNAT_RESPONSE_MESSAGE + ";statusCode = " + xnatSessionResponse.statusCode);
						responseStream.append(UNEXPECTED_XNAT_RESPONSE_MESSAGE + ";statusCode = " + xnatSessionResponse.statusCode);
						statusCode = xnatSessionResponse.statusCode;
					}
				} catch (Throwable t) {
					log.warning(LOGIN_EXCEPTION_MESSAGE, t);
					responseStream.append(LOGIN_EXCEPTION_MESSAGE + ": " + t.getMessage());
					statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				}
			} else {
				log.info(MISSING_USERNAME_MESSAGE);
				responseStream.append(MISSING_USERNAME_MESSAGE);
				statusCode = HttpServletResponse.SC_BAD_REQUEST;
			}
		} else if ("DELETE".equalsIgnoreCase(method)) {
			log.info("XNATSessionHandler, logout request");
			try {
				int xnatStatusCode = XNATUtil.invalidateXNATSessionID(httpRequest);
				log.info("XNAT delete session returns status code " + xnatStatusCode);
				statusCode = xnatStatusCode;
			} catch (Throwable t) {
				log.warning(LOGOUT_EXCEPTION_MESSAGE, t);
				responseStream.append(LOGOUT_EXCEPTION_MESSAGE + ": " + t.getMessage());
				statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			}
		} else if ("OPTIONS".equalsIgnoreCase(method)) {
			log.info("XNATSessionHandler, CORS preflight OPTIONS request");
			httpResponse.setHeader("Access-Control-Allow-Origin", origin);
			httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
			httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization");
			statusCode = HttpServletResponse.SC_OK;
		} else {
			log.info(INVALID_METHOD_MESSAGE + "; got " + method);
			responseStream.append(INVALID_METHOD_MESSAGE + "; got " + method);
			httpResponse.setHeader("Access-Control-Allow-Methods", "POST, DELETE, OPTIONS");
			statusCode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
		}
		httpResponse.setStatus(statusCode);
	}
}