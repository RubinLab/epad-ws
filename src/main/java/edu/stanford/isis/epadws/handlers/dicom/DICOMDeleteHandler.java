package edu.stanford.isis.epadws.handlers.dicom;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.JsonHelper;
import edu.stanford.isis.epadws.processing.pipeline.task.DicomDeleteTask;
import edu.stanford.isis.epadws.xnat.XNATOperations;

/**
 * Delete a study or a series.
 * 
 */
public class DICOMDeleteHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String INTERNAL_ERROR_MESSAGE = "Internal error in DICOM delete route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on DICOM delete route";
	private static final String MISSING_QUERY_MESSAGE = "No query parameters specified  on DICOM delete route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException
	{
		PrintWriter responseStream = httpResponse.getWriter();
		int responseCode;

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		if (XNATOperations.hasValidXNATSessionID(httpRequest)) {
			String queryString = httpRequest.getQueryString();
			queryString = URLDecoder.decode(queryString, "UTF-8");
			log.info("DICOM delete handler query: " + queryString);

			if (queryString != null) {
				queryString = queryString.trim();
				try {
					if (isSeriesRequest(queryString)) {
						handleDICOMSeriesDeleteRequest(queryString);
					} else {
						handleDICOMStudyDeleteRequest(queryString);
					}
					responseCode = HttpServletResponse.SC_OK;
				} catch (Throwable t) {
					log.severe(INTERNAL_ERROR_MESSAGE, t);
					responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				}
			} else {
				log.info(MISSING_QUERY_MESSAGE);
				responseStream.append(MISSING_QUERY_MESSAGE);
				responseCode = HttpServletResponse.SC_BAD_REQUEST;
			}
		} else {
			log.info(INVALID_SESSION_TOKEN_MESSAGE);
			responseStream.append(JsonHelper.createJSONErrorResponse(INVALID_SESSION_TOKEN_MESSAGE));
			responseCode = HttpServletResponse.SC_UNAUTHORIZED;
		}
		httpResponse.setStatus(responseCode);
	}

	private void handleDICOMStudyDeleteRequest(String queryString)
	{
		log.info(queryString);
		String[] parts = queryString.split("&");
		String studyUID = parts[1].trim();
		parts = studyUID.split("=");
		studyUID = parts[1].trim();

		log.info("DICOM delete handler (study) = " + studyUID);
		(new Thread(new DicomDeleteTask(studyUID, true))).start();
	}

	private void handleDICOMSeriesDeleteRequest(String queryString)
	{
		log.info(queryString);
		String[] parts = queryString.split("&");
		String seriesUID = parts[1].trim();
		parts = seriesUID.split("=");
		seriesUID = parts[1].trim();

		log.info("DicomDeleteHandler(series) = " + seriesUID);
		(new Thread(new DicomDeleteTask(seriesUID, false))).start();
	}

	/**
	 * Look for deletetype=series in the request to determine if it is a series request.
	 * 
	 * @param queryString String
	 * @return boolean
	 */
	private static boolean isSeriesRequest(String queryString)
	{
		String check = queryString.toLowerCase().trim();
		boolean isSeries = check.indexOf("eletetype=series") > 0;

		return isSeries;
	}
}
