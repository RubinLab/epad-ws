package edu.stanford.isis.epadws.handlers.admin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * 
 * <code> curl -v -u admin:admin -X POST http://<host>:<port>/login/ </code>
 * 
 * @author martin
 */
public class LoginHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();

	private static final String MISSING_USERNAME_MESSAGE = "Missing user name";
	private static final String INVALID_METHOD_MESSAGE = "Only POST method is valid for this route";
	private static final String LOGIN_EXCEPTION_MESSAGE = "Internal login error";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		PrintWriter out = httpResponse.getWriter();
		String username = XNATUtil.extractUserNameFromAuthorizationHeader(httpRequest);
		String result = "";

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		log.info("Login request from ePad : ");

		String method = httpRequest.getMethod();
		if ("POST".equalsIgnoreCase(method)) {

			if (username.length() != 0) {
				try {
					result = XNATUtil.invokeXNATSessionIDService(httpRequest, httpResponse);
				} catch (IOException e) {
					log.warning(LOGIN_EXCEPTION_MESSAGE, e);
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					result = LOGIN_EXCEPTION_MESSAGE + ": " + e.getMessage();
				} catch (Exception e) {
					log.warning(LOGIN_EXCEPTION_MESSAGE, e);
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					result = LOGIN_EXCEPTION_MESSAGE + ": " + e.getMessage();
				}
			} else {
				log.info(MISSING_USERNAME_MESSAGE);
				httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				result = MISSING_USERNAME_MESSAGE;
			}
		} else {
			log.info(INVALID_METHOD_MESSAGE);
			httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			httpResponse.setHeader("Access-Control-Allow-Methods", "POST");
			result = INVALID_METHOD_MESSAGE;
		}
		out.append(result);
		out.flush();
	}
}