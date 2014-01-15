package edu.stanford.isis.epadws.handlers.event;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.JsonHelper;
import edu.stanford.isis.epadws.processing.events.EventTracker;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * 
 * 
 * 
 * @author martin
 */
public class ProjectEventHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final EventTracker eventTracker = EventTracker.getInstance();

	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error in event route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid in event route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("application/json");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
				String jsessionID = XNATUtil.getJSessionIDFromRequest(httpRequest);
				performEventHandle(responseStream, jsessionID);
				statusCode = HttpServletResponse.SC_OK;
			} else {
				log.info(INVALID_SESSION_TOKEN_MESSAGE);
				responseStream.append(JsonHelper.createJSONErrorResponse(INVALID_SESSION_TOKEN_MESSAGE));
				statusCode = HttpServletResponse.SC_UNAUTHORIZED;
			}
			responseStream.flush();
		} catch (Throwable t) {
			log.severe(INTERNAL_EXCEPTION_MESSAGE, t);
			if (responseStream != null)
				responseStream.append(JsonHelper.createJSONErrorResponse(INTERNAL_EXCEPTION_MESSAGE, t));
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		httpResponse.setStatus(statusCode);
	}

	private void performEventHandle(PrintWriter responseStream, String jsessionID)
	{
		responseStream.write(eventTracker.dumpProjectEvents(jsessionID));
	}
}