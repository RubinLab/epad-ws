package edu.stanford.isis.epadws.handlers.dicom;

import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.handlers.HandlerUtil;
import edu.stanford.isis.epadws.processing.pipeline.task.DicomSeriesDeleteTask;
import edu.stanford.isis.epadws.processing.pipeline.task.DicomStudyDeleteTask;
import edu.stanford.isis.epadws.xnat.XNATSessionOperations;

/**
 * Delete a study or a series.
 * 
 */
public class DicomDeleteHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String INTERNAL_ERROR_MESSAGE = "Internal error in DICOM delete route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on DICOM delete route";
	private static final String MISSING_QUERY_MESSAGE = "No query parameters specified  on DICOM delete route";
	private static final String BAD_QUERY_MESSAGE = "No study specified on DICOM delete route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();
			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String queryString = httpRequest.getQueryString();
				queryString = URLDecoder.decode(queryString, "UTF-8");
				log.info("DICOM delete handler query: " + queryString);

				if (queryString != null) {
					try {
						String studyUID = httpRequest.getParameter("studyuid");
						String seriesUID = httpRequest.getParameter("seriesuid");
						if (studyUID != null) {
							if (seriesUID == null)
								handleStudyDeleteRequest(studyUID);
							else
								handleSeriesDeleteRequest(studyUID, seriesUID);
							statusCode = HttpServletResponse.SC_OK;
						} else {
							statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_BAD_REQUEST, BAD_QUERY_MESSAGE,
									responseStream, log);
						}
					} catch (Throwable t) {
						statusCode = HandlerUtil.internalErrorResponse(INTERNAL_ERROR_MESSAGE, t, log);
					}
				} else {
					statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_QUERY_MESSAGE,
							responseStream, log);
				}
			} else {
				statusCode = HandlerUtil.invalidTokenResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	private void handleStudyDeleteRequest(String studyUID)
	{
		log.info("DeleteHandler(study) = " + studyUID);
		(new Thread(new DicomStudyDeleteTask(studyUID))).start();
	}

	private void handleSeriesDeleteRequest(String studyUID, String seriesUID)
	{
		log.info("DeleteHandler(series) = " + seriesUID);
		(new Thread(new DicomSeriesDeleteTask(studyUID, seriesUID))).start();
	}
}
