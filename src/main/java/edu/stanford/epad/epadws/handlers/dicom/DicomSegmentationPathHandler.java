package edu.stanford.epad.epadws.handlers.dicom;

import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.HandlerUtil;

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
	private static final String NO_STUDY_FOUND_MESSAGE = "Could not find study for image with UID ";
	private static final String MISSING_IMAGE_ID_MESSAGE = "No image ID on segmentation path route";
	private static final String MISSING_QUERY_MESSAGE = "Missing query on segmentation path route";
	private static final String INTERNAL_ERROR_MESSAGE = "Internal server error on segmentation path request";

	// TODO Convert result to JSON and clean up entire class.

	private boolean dummy()
	{
		return true;
	}

	@Override
	public void handle(String base, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		try {
			// if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
			if (dummy()) { // TODO
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
									NO_STUDY_FOUND_MESSAGE + imageUID, log);
						}
					} else {
						statusCode = HandlerUtil.infoResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_IMAGE_ID_MESSAGE, log);
					}
				} else {
					statusCode = HandlerUtil.infoResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_QUERY_MESSAGE, log);
				}
				responseStream.flush();
			} else {
				statusCode = HandlerUtil.invalidTokenResponse(INVALID_SESSION_TOKEN_MESSAGE, log);
			}
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_ERROR_MESSAGE, t, log);
		}

		httpResponse.setStatus(statusCode);
	}

	private String[] retrieveStudySeriesAndImageIDsFromEpadDatabase(String imageUID)
	{
		EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();

		return databaseOperations.retrieveDicomStudySeriesAndImageUIDs(imageUID);
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
