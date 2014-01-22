package edu.stanford.isis.epadws.handlers.dicom;

import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.handlers.HandlerUtil;
import edu.stanford.isis.epadws.persistence.Database;
import edu.stanford.isis.epadws.persistence.DatabaseOperations;
import edu.stanford.isis.epadws.xnat.XNATSessionOperations;

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

	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on DICOM segmentation path route";

	// TODO Convert result to JSON and clean up entire class. Add authentication etc.

	@Override
	public void handle(String base, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		try {
			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String queryString = httpRequest.getQueryString();

				if (queryString != null) {
					responseStream = httpResponse.getWriter();
					queryString = URLDecoder.decode(queryString, "UTF-8");
					queryString = queryString.trim();
					String imageUID = getImageUIDFromRequest(queryString);
					if (imageUID != null) {
						String[] studySeriesAndImageIDs = retrieveStudySeriesAndImageIDsFromEpadDatabase(imageUID);
						String separator = config.getStringPropertyValue("fieldSeparator");
						log.info("SegmentationPath query from ePAD for image " + imageUID);

						if (studySeriesAndImageIDs[0] != null && studySeriesAndImageIDs[1] != null
								&& studySeriesAndImageIDs[2] != null) {
							responseStream.println("StudyUID" + separator + "SeriesUID" + separator + "ImageUID");
							responseStream.println(studySeriesAndImageIDs[0] + separator + studySeriesAndImageIDs[1] + separator
									+ studySeriesAndImageIDs[2]);
							statusCode = HttpServletResponse.SC_OK;
						} else {
							statusCode = HandlerUtil.infoResponse(HttpServletResponse.SC_NOT_FOUND,
									"Could not find study for image with UID" + imageUID, log);
						}
					} else {
						statusCode = HandlerUtil.infoResponse(HttpServletResponse.SC_BAD_REQUEST, "No image ID inquery!", log);
					}
				} else {
					statusCode = HandlerUtil.infoResponse(HttpServletResponse.SC_BAD_REQUEST, "No query in request!", log);
				}
				responseStream.flush();
			} else {
				statusCode = HandlerUtil.invalidTokenResponse(INVALID_SESSION_TOKEN_MESSAGE, log);
			}
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorResponse("Warning: internal server error on series path request", t, log);
		}

		httpResponse.setStatus(statusCode);
	}

	private String[] retrieveStudySeriesAndImageIDsFromEpadDatabase(String imageUID)
	{
		DatabaseOperations databaseOperations = Database.getInstance().getDatabaseOperations();

		return databaseOperations.retrieveStudySeriesAndImageIDs(imageUID);
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