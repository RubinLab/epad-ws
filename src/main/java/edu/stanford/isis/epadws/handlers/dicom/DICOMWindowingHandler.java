package edu.stanford.isis.epadws.handlers.dicom;

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

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SourceImage;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.EPADTools;
import edu.stanford.isis.epadws.handlers.HandlerUtil;
import edu.stanford.isis.epadws.xnat.XNATSessionOperations;

/**
 * Generate window width and center for a series or study in one quick step.
 */
public class DICOMWindowingHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final EPADConfig config = EPADConfig.getInstance();

	private static final String WADO_ERROR_MESSAGE = "Warining: WADO error in DICOM windowing route";
	private static final String INTERNAL_ERROR_MESSAGE = "Warning: internal error in DICOM windowing route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on DICOM windowing route";
	private static final String MISSING_QUERY_MESSAGE = "No query in DICOM windowing request";
	private static final String BADLY_FORMED_QUERY_MESSAGE = "Invalid query paramaters specified in DICOM windowing request";

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
				String queryString = httpRequest.getQueryString();
				queryString = URLDecoder.decode(queryString, "UTF-8");

				if (queryString != null) {
					queryString = queryString.trim();
					String studyIdKey = getStudyUIDFromRequest(queryString);
					String seriesIdKey = getSeriesUIDFromRequest(queryString);
					String imageIdKey = getInstanceUIDFromRequest(queryString);

					if (studyIdKey != null && seriesIdKey != null && imageIdKey != null) {
						if (handleDICOMWindowing(responseStream, studyIdKey, seriesIdKey, imageIdKey))
							statusCode = HttpServletResponse.SC_OK;
						else {
							statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
									WADO_ERROR_MESSAGE, log);
						}
					} else {
						statusCode = HandlerUtil.infoResponse(HttpServletResponse.SC_BAD_REQUEST, BADLY_FORMED_QUERY_MESSAGE, log);
					}
				} else {
					statusCode = HandlerUtil.infoResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_QUERY_MESSAGE, log);
				}
				responseStream.flush();
			} else {
				statusCode = HandlerUtil.invalidTokenResponse(INVALID_SESSION_TOKEN_MESSAGE, log);
			}
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_ERROR_MESSAGE, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	private boolean handleDICOMWindowing(PrintWriter responseStream, String studyIdKey, String seriesIdKey,
			String imageIdKey)
	{
		boolean success;

		try {
			File tempDicom = File.createTempFile(imageIdKey, ".tmp");

			EPADTools.downloadDICOMFileFromWADO(tempDicom, studyIdKey, seriesIdKey, imageIdKey);

			SourceImage srcDicomImage = new SourceImage(tempDicom.getAbsolutePath());
			double windowWidth = 0.0;
			double windowCenter = 0.0;

			if (srcDicomImage != null) {
				Opener opener = new Opener();
				String imageFilePath = tempDicom.getAbsolutePath();
				ImagePlus imp = opener.openImage(imageFilePath);// ImageProcessor ip = imp.getProcessor();
				double min = imp.getDisplayRangeMin();
				double max = imp.getDisplayRangeMax();
				Calibration cal = imp.getCalibration();
				// int digits = (ip instanceof FloatProcessor) || cal.calibrated() ? 2 : 0;
				double minValue = cal.getCValue(min);
				double maxValue = cal.getCValue(max);
				windowWidth = (maxValue - minValue);
				windowCenter = (minValue + windowWidth / 2.0);
			}
			String separator = config.getStringPropertyValue("fieldSeparator");
			responseStream.println("windowWidth" + separator + "windowCenter");
			responseStream.println(windowWidth + separator + windowCenter);
			success = true;
		} catch (DicomException e) {
			log.warning("DICOM windowing handler: error reading DICOM image ", e);
			success = false;
		} catch (IOException e) {
			log.warning("DICOM windowing handler: error getting DICOM image from WADO", e);
			success = false;
		}
		return success;
	}

	// TODO Clean up this mess
	private static String getStudyUIDFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[0].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getSeriesUIDFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[1].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getInstanceUIDFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[2].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}
}
