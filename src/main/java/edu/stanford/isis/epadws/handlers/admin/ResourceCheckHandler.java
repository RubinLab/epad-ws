package edu.stanford.isis.epadws.handlers.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyLogger;

/**
 * Provide access control.
 * <p>
 * Also allows CORS requests to support GWT Dev Mode where drawing on retrieved canvas is required.
 * 
 * @author martin
 * 
 */
public class ResourceCheckHandler extends AbstractHandler
{
	private final ProxyLogger log = ProxyLogger.getInstance();

	private static final String INTERNAL_ERROR_MESSAGE = "Internal server error";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		String origin = httpRequest.getHeader("Origin"); // CORS request should have Origin header

		// Origin header indicates a possible CORS requests, which we support to allow drawing on canvas in GWT Dev Mode.
		if (origin != null) {
			httpResponse.setHeader("Access-Control-Allow-Origin", origin);
			httpResponse.setHeader("Access-Control-Allow-Credentials", "true"); // Needed to allow cookies.
		}

		request.setHandled(false);

		// try {
		// if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
		// request.setHandled(false);
		// } else {
		// log.info(INVALID_SESSION_TOKEN_MESSAGE);
		// httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		// request.setHandled(true);
		// }
		// } catch (Throwable t) {
		// log.warning(INTERNAL_ERROR_MESSAGE, t);
		// httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		// request.setHandled(true);
		// }
	}
}
