package edu.stanford.epad.epadws.handlers.dicom;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.DICOMElementList;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * Download headers for a series or study in one quick step.
 */
public class DICOMHeadersHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String INTERNAL_ERROR_MESSAGE = "Internal error on DICOM headers route";
	private static final String NOTHING_FOUND_MESSAGE = "No DICOM headers found!";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on DICOM headers route";
	private static final String MISSING_QUERY_MESSAGE = "No query paramaters specified  on DICOM headers route";
	private static final String BADLY_FORMED_QUERY_MESSAGE = "Invalid query paramaters specified  on DICOM headers route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("application/json");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String queryString = httpRequest.getQueryString();
				if (queryString != null) {
					String studyUID = httpRequest.getParameter("studyuid");
					String seriesUID = httpRequest.getParameter("seriesuid");
					String imageUID = httpRequest.getParameter("instanceuid");

					log.info("Retrieving DICOM headers for image " + imageUID + " in series " + seriesUID);

					if (studyUID != null && seriesUID != null && imageUID != null) {
						DICOMElementList dicomElementList = Dcm4CheeQueries.getDICOMElementsFromWADO(studyUID, seriesUID, imageUID);
						if (dicomElementList.ResultSet.totalRecords != 0) {
							responseStream.append(dicomElementList.toJSON());
							statusCode = HttpServletResponse.SC_OK;
						} else {
							statusCode = HandlerUtil.internalErrorJSONResponse(NOTHING_FOUND_MESSAGE, responseStream, log);
						}
					} else {
						statusCode = HandlerUtil.infoJSONResponse(HttpServletResponse.SC_BAD_REQUEST, BADLY_FORMED_QUERY_MESSAGE,
								responseStream, log);
					}
				} else {
					statusCode = HandlerUtil.infoJSONResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_QUERY_MESSAGE,
							responseStream, log);
				}
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
			responseStream.flush();
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}
}
