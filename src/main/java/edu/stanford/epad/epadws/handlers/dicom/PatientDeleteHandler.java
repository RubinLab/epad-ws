package edu.stanford.epad.epadws.handlers.dicom;

import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * Delete a patient. Deletes the studies associated with that patient from the ePAD and DCM4CHEE databases.
 * <p>
 * Deletes all studies and series associated with a patient so should only be used if that patient is not present in any
 * XNAT study.
 * 
 * @author martin
 */
public class PatientDeleteHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String INTERNAL_ERROR_MESSAGE = "Internal error on patient delete route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on patient delete route";
	private static final String MISSING_QUERY = "No query parameters specified in patient delete route";
	private static final String BAD_QUERY = "Both project ID and patient ID must be specified in patient delete route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream;
		int responseCode;

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
			try {
				String queryString = httpRequest.getQueryString();
				String sessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);

				queryString = URLDecoder.decode(queryString, "UTF-8");
				log.info("Patient delete handler query: " + queryString);
				responseStream = httpResponse.getWriter();

				if (queryString != null) {
					EpadOperations epadOperations = DefaultEpadOperations.getInstance();
					String projectID = httpRequest.getParameter("projectID");
					String patientID = httpRequest.getParameter("patientID");

					if (projectID != null && patientID != null) {
						epadOperations.schedulePatientDelete(sessionID, projectID, patientID);
						responseCode = HttpServletResponse.SC_OK;
					} else {
						responseCode = HandlerUtil.infoResponse(HttpServletResponse.SC_BAD_REQUEST, BAD_QUERY, responseStream, log);
					}
				} else {
					responseCode = HandlerUtil.infoResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_QUERY, responseStream,
							log);
				}
				responseStream.flush();
			} catch (Throwable t) {
				responseCode = HandlerUtil.internalErrorResponse(INTERNAL_ERROR_MESSAGE, t, log);
			}
		} else {
			responseCode = HandlerUtil.invalidTokenResponse(INVALID_SESSION_TOKEN_MESSAGE, log);
		}
		httpResponse.setStatus(responseCode);
	}
}
