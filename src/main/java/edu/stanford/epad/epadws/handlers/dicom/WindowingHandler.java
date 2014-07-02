package edu.stanford.epad.epadws.handlers.dicom;

import ij.ImagePlus;
import ij.io.Opener;
import ij.measure.Calibration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.dicom.DCM4CHEEUtil;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * Generate window width and center for a series or study in one quick step.
 */
public class WindowingHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final EPADConfig config = EPADConfig.getInstance();

	private static final String WADO_ERROR_MESSAGE = "WADO error in DICOM windowing route";
	private static final String INTERNAL_ERROR_MESSAGE = "Internal error in DICOM windowing route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on DICOM windowing route";
	private static final String INVALID_METHOD = "Only GET methods valid for the windowing route";
	private static final String MISSING_QUERY_MESSAGE = "No query in DICOM windowing request";
	private static final String BADLY_FORMED_QUERY_MESSAGE = "Invalid query parameters specified in DICOM windowing request";

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
				String method = httpRequest.getMethod();
				String queryString = httpRequest.getQueryString();
				queryString = URLDecoder.decode(queryString, "UTF-8");

				if ("GET".equalsIgnoreCase(method)) {
					if (queryString != null) {
						queryString = queryString.trim();
						String studyUID = httpRequest.getParameter("studyuid");
						String seriesUID = httpRequest.getParameter("seriesuid");
						String imageUID = httpRequest.getParameter("instanceuid");

						if (studyUID != null && seriesUID != null && imageUID != null) {
							if (handleDICOMWindowing(responseStream, studyUID, seriesUID, imageUID))
								statusCode = HttpServletResponse.SC_OK;
							else {
								statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
										WADO_ERROR_MESSAGE, log);
							}
						} else {
							statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_BAD_REQUEST, BADLY_FORMED_QUERY_MESSAGE,
									log);
						}
					} else {
						statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_QUERY_MESSAGE, log);
					}
				} else {
					httpResponse.setHeader("Access-Control-Allow-Methods", "GET");
					statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_METHOD_NOT_ALLOWED, INVALID_METHOD, log);
				}
				responseStream.flush();
			} else {
				statusCode = HandlerUtil.invalidTokenResponse(INVALID_SESSION_TOKEN_MESSAGE, log);
			}
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	private boolean handleDICOMWindowing(PrintWriter responseStream, String studyUID, String seriesUID, String imageUID)
	{
		boolean dicomFileDownloaded = false;
		boolean success = false;
		File tempDicomFile = null;

		try {
			tempDicomFile = File.createTempFile(imageUID, ".tmp");

			DCM4CHEEUtil.downloadDICOMFileFromWADO(studyUID, seriesUID, imageUID, tempDicomFile);
			dicomFileDownloaded = true;
		} catch (IOException e) {
			log.warning("Error getting DICOM file from WADO for image " + imageUID + " in series " + seriesUID, e);
		}

		if (dicomFileDownloaded) {
			try {
				double windowWidth = 1.0;
				double windowCenter = 0.0;

				Opener opener = new Opener();
				String imageFilePath = tempDicomFile.getAbsolutePath();
				ImagePlus imp = opener.openImage(imageFilePath);// ImageProcessor ip = imp.getProcessor();

				if (imp != null) { // ImageJ failed to open DICOM file
					double min = imp.getDisplayRangeMin();
					double max = imp.getDisplayRangeMax();
					Calibration cal = imp.getCalibration();
					// int digits = (ip instanceof FloatProcessor) || cal.calibrated() ? 2 : 0;

					double minValue = cal.getCValue(min);
					double maxValue = cal.getCValue(max);
					windowWidth = (maxValue - minValue);
					windowCenter = (minValue + windowWidth / 2.0);

					log.info("Image " + imageUID + " in series " + seriesUID + " has a calculated window width of " + windowWidth
							+ " and window center of " + windowCenter);
				} else {
					log.warning("ImageJ failed to load DICOM file for image " + imageUID + " in series " + seriesUID
							+ " to calculate windowing");
				}
				// This is Pixelmed variant (though does not seem to be correct).
				// SourceImage srcDicomImage = new SourceImage(tempDicomFile.getAbsolutePath());
				// ImageEnhancer imageEnhancer = new ImageEnhancer(srcDicomImage);
				// imageEnhancer.findVisuParametersImage();
				// windowWidth = imageEnhancer.getWindowWidth();
				// windowCenter = imageEnhancer.getWindowCenter();

				String separator = config.getStringPropertyValue("fieldSeparator");
				responseStream.println("windowWidth" + separator + "windowCenter");
				responseStream.println(windowWidth + separator + windowCenter);
				success = true;
			} catch (Exception e) {
				log.warning("Exception calculating windowing for image " + imageUID + " in series " + seriesUID, e);
			}
		}
		return success;
	}
}
