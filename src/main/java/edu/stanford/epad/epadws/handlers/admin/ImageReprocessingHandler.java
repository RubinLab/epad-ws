package edu.stanford.epad.epadws.handlers.admin;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.service.SessionService;

/**
 * @author martin
 */
public class ImageReprocessingHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String FORBIDDEN = "Forbidden method - only GET supported on reload route";
	private static final String INTERNAL_ERROR_MESSAGE = "Internal server error on reload route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid for reload route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/plain;charset=UTF-8");
		if (request != null)					// In case handler is not called thru jetty
			request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (SessionService.hasValidSessionID(httpRequest)) {
				String method = httpRequest.getMethod();
				if ("GET".equalsIgnoreCase(method)) {
					forceImageReload();
					statusCode = HttpServletResponse.SC_OK;
				} else {
					statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_FORBIDDEN, FORBIDDEN, responseStream, log);
				}
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	private void forceImageReload()
	{
		final EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();

		log.info("Forcing reprocessing of all DICOM images");
		epadDatabaseOperations.forceDICOMReprocessing();
	}
}
