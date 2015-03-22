package edu.stanford.epad.epadws.handlers.event;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.processing.events.EventTracker;
import edu.stanford.epad.epadws.service.SessionService;

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
		if (request != null)					// In case handler is not called thru jetty
			request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (SessionService.hasValidSessionID(httpRequest)) {
				String jsessionID = SessionService.getJSessionIDFromRequest(httpRequest);
				performEventHandle(responseStream, jsessionID);
				statusCode = HttpServletResponse.SC_OK;
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
			responseStream.flush();
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_EXCEPTION_MESSAGE, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	private void performEventHandle(PrintWriter responseStream, String jsessionID)
	{
		responseStream.write(eventTracker.dumpProjectEvents(jsessionID));
	}
}