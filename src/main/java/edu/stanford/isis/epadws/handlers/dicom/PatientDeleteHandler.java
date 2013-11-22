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
import edu.stanford.isis.epadws.processing.pipeline.task.PatientDeleteTask;
import edu.stanford.isis.epadws.xnat.XNATUtil;

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
	private static final String MISSING_QUERY_MESSAGE = "No query parameters specified in patient delete route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException // TODO Get rid of
	{
		PrintWriter responseStream = httpResponse.getWriter();
		int responseCode;

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
			String queryString = httpRequest.getQueryString();
			queryString = URLDecoder.decode(queryString, "UTF-8");
			log.info("Patient delete handler query: " + queryString);

			if (queryString != null) {
				queryString = queryString.trim();
				try {
					handlePatientDeleteRequest(queryString);
					responseCode = HttpServletResponse.SC_OK;
				} catch (Throwable t) {
					log.warning(INTERNAL_ERROR_MESSAGE, t);
					responseStream.print(INTERNAL_ERROR_MESSAGE + ": " + t.getMessage());
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

	private void handlePatientDeleteRequest(String queryString)
	{
		log.info(queryString);
		String[] parts = queryString.split("&");
		String patientID = parts[1].trim();
		parts = patientID.split("=");
		patientID = parts[1].trim();

		log.info("Deleting patient = " + patientID);

		(new Thread(new PatientDeleteTask(patientID))).start();
	}
}
