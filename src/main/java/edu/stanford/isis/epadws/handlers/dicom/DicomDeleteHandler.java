package edu.stanford.isis.epadws.handlers.dicom;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epadws.processing.pipeline.DicomDeleteTask;
import edu.stanford.isis.epadws.resources.server.DICOMDeleteServerResource;

/**
 * Now handled by Restlet resource {@link DICOMDeleteServerResource}.
 * 
 * @author kurtz
 * 
 * @see DICOMDeleteServerResource
 */
public class DicomDeleteHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();

	public DicomDeleteHandler()
	{
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		httpResponse.setContentType("text/plain");
		httpResponse.setStatus(HttpServletResponse.SC_OK);
		request.setHandled(true);

		String queryString = httpRequest.getQueryString();
		queryString = URLDecoder.decode(queryString, "UTF-8");
		log.info("Delete query from ePad : " + queryString);

		if (queryString != null) {

			queryString = queryString.trim();
			if (isSeriesRequest(queryString)) {
				handleSeriesRequest(queryString);
			} else {
				handleStudyRequest(queryString);
			}
		} else {
			log.info("NO delete Query from request.");
		}
	}

	private void handleStudyRequest(String queryString)
	{
		try {
			log.info(queryString);
			String[] parts = queryString.split("&");
			String value = parts[1].trim();
			parts = value.split("=");
			value = parts[1].trim();

			log.info("DicomDeleteHandler(study) = " + value);
			(new Thread(new DicomDeleteTask(value, true))).start();

		} catch (Exception e) {
			log.warning("handleStudyRequest (mysql) had..", e);
		}
	}

	private void handleSeriesRequest(String queryString)
	{
		try {
			log.info(queryString);
			String[] parts = queryString.split("&");
			String value = parts[1].trim();
			parts = value.split("=");
			value = parts[1].trim();

			log.info("DicomDeleteHandler(series) = " + value);
			(new Thread(new DicomDeleteTask(value, false))).start();

		} catch (Exception e) {
			log.warning("handleSeriesRequest (mysql) had..", e);
		}
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

		log.info(" isSeries=" + isSeries + " for: " + queryString);
		return isSeries;
	}

}
