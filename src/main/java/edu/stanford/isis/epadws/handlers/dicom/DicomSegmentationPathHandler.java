package edu.stanford.isis.epadws.handlers.dicom;

import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.processing.persistence.MySqlInstance;
import edu.stanford.isis.epadws.processing.persistence.MySqlQueries;

/**
 * Given an image identifier for an image in a DICOM study return a three-column CSV with the study, series and image
 * IDs.
 * 
 * @author kurtz
 */
public class DicomSegmentationPathHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final EPADConfig config = EPADConfig.getInstance();

	// TODO Convert result to JSON and clean up entire class. Add authentication etc.

	@Override
	public void handle(String base, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;
		String queryString = httpRequest.getQueryString();

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		if (queryString != null) {
			try {
				responseStream = httpResponse.getWriter();
				queryString = URLDecoder.decode(queryString, "UTF-8");
				queryString = queryString.trim();
				String imageUID = getImageUIDFromRequest(queryString);
				if (imageUID != null) {
					String[] studySeriesAndImageIDs = retrieveStudySeriesAndImageIDsFromEpadDatabase(imageUID);
					String separator = config.getParam("fieldSeparator");
					log.info("SegmentationPath query from ePAD for image " + imageUID);

					if (studySeriesAndImageIDs[0] != null && studySeriesAndImageIDs[1] != null
							&& studySeriesAndImageIDs[2] != null) {
						responseStream.println("StudyUID" + separator + "SeriesUID" + separator + "ImageUID");
						responseStream.println(studySeriesAndImageIDs[0] + separator + studySeriesAndImageIDs[1] + separator
								+ studySeriesAndImageIDs[2]);
						statusCode = HttpServletResponse.SC_OK;
					} else {
						log.warning("Could not find study for image with UID" + imageUID);
						statusCode = HttpServletResponse.SC_NOT_FOUND;
					}
				} else {
					log.warning("No image ID in request query!");
					statusCode = HttpServletResponse.SC_BAD_REQUEST;
				}
			} catch (Exception e) {
				log.warning("Internal server error handling series path request", e);
				statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			}
			responseStream.flush();
		} else {
			log.warning("No segmentation path query in request!");
			statusCode = HttpServletResponse.SC_BAD_REQUEST;
		}
		httpResponse.setStatus(statusCode);
	}

	private String[] retrieveStudySeriesAndImageIDsFromEpadDatabase(String imageUID)
	{
		MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();

		return dbQueries.retrieveStudySeriesAndImageIDsFromEpadDatabase(imageUID);
	}

	private String getImageUIDFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[0].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}
}
