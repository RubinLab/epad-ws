package edu.stanford.isis.epadws.handlers.dicom;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.util.JsonHelper;
import edu.stanford.isis.epadws.processing.pipeline.DicomDeleteTask;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * @author kurtz
 * 
 */
public class DICOMDeleteHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();

	private static final String INTERNAL_ERROR_MESSAGE = "Internal error on delete";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";
	private static final String MISSING_QUERY_MESSAGE = "No query paramaters specified";

	public DICOMDeleteHandler()
	{
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		PrintWriter out = httpResponse.getWriter();

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
			String queryString = httpRequest.getQueryString();
			queryString = URLDecoder.decode(queryString, "UTF-8");
			log.info("DICOMDeleteHandler query: " + queryString);

			if (queryString != null) {
				queryString = queryString.trim();
				try {
					if (isSeriesRequest(queryString)) {
						handleDICOMSeriesDeleteRequest(queryString);
					} else {
						handleDICOMStudyDeleteRequest(queryString);
					}
					httpResponse.setStatus(HttpServletResponse.SC_OK);
				} catch (Exception e) {
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					log.warning(INTERNAL_ERROR_MESSAGE, e);
					out.print(INTERNAL_ERROR_MESSAGE + ": " + e.getMessage());
				} catch (Error e) {
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					log.warning(INTERNAL_ERROR_MESSAGE, e);
					out.print(INTERNAL_ERROR_MESSAGE + ": " + e.getMessage());
				}
			} else {
				log.info(MISSING_QUERY_MESSAGE);
				out.append(MISSING_QUERY_MESSAGE);
				httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} else {
			log.info(INVALID_SESSION_TOKEN_MESSAGE);
			out.append(JsonHelper.createJSONErrorResponse(INVALID_SESSION_TOKEN_MESSAGE));
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		out.flush();
		out.close();
	}

	private void handleDICOMStudyDeleteRequest(String queryString)
	{
		log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[1].trim();
		parts = value.split("=");
		value = parts[1].trim();

		log.info("DicomDeleteHandler(study) = " + value);
		(new Thread(new DicomDeleteTask(value, true))).start();
	}

	private void handleDICOMSeriesDeleteRequest(String queryString)
	{
		log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[1].trim();
		parts = value.split("=");
		value = parts[1].trim();

		log.info("DicomDeleteHandler(series) = " + value);
		(new Thread(new DicomDeleteTask(value, false))).start();
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
