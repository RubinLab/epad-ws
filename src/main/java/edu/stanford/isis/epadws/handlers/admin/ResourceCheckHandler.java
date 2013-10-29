package edu.stanford.isis.epadws.handlers.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epadws.xnat.XNATUtil;

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

		if (origin == null) { // CORS requests should not have an empty Origin header.
			log.warning("Empty Origin header on request: " + httpRequest.getRequestURI());
		} else {
			httpResponse.setHeader("Access-Control-Allow-Origin", origin);
		}
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true"); // Needed to allow cookies.

		// Only required on pre-flight CORS requests.
		httpResponse.setHeader("Access-Control-Allow-Headers",
				"Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
		httpResponse.setHeader("Access-Control-Allow-Methods", "GET");

		try {
			if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
				request.setHandled(false);
			} else {
				log.info(INVALID_SESSION_TOKEN_MESSAGE);
				httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				request.setHandled(true);
			}
		} catch (Throwable t) {
			log.warning(INTERNAL_ERROR_MESSAGE, t);
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			request.setHandled(true);
		}

	}
}
