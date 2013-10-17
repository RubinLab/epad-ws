package edu.stanford.isis.epadws.handlers.dicom;

import ij.ImagePlus;
import ij.io.Opener;
import ij.measure.Calibration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SourceImage;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.util.WadoUrlBuilder;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * Generate window width and center for a series or study in one quick step.
 * 
 * @author amsnyder
 */
public class DICOMVisuHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();
	private static final ProxyConfig config = ProxyConfig.getInstance();

	private static final String WADO_ERROR_MESSAGE = "WADO error";
	private static final String INTERNAL_ERROR_MESSAGE = "Internal error";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";
	private static final String MISSING_QUERY_MESSAGE = "No query in request";
	private static final String BADLY_FORMED_QUERY_MESSAGE = "Invalid query paramaters specified";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
				String queryString = httpRequest.getQueryString();
				queryString = URLDecoder.decode(queryString, "UTF-8");

				if (queryString != null) {
					queryString = queryString.trim();
					String studyIdKey = getStudyUIDFromRequest(queryString);
					String seriesIdKey = getSeriesUIDFromRequest(queryString);
					String imageIdKey = getInstanceUIDFromRequest(queryString);

					if (studyIdKey != null && seriesIdKey != null && imageIdKey != null) {
						if (handleDICOMVisu(responseStream, studyIdKey, seriesIdKey, imageIdKey))
							statusCode = HttpServletResponse.SC_OK;
						else {
							log.warning(WADO_ERROR_MESSAGE);
							responseStream.print(INTERNAL_ERROR_MESSAGE);
							statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
						}
					} else {
						log.info(BADLY_FORMED_QUERY_MESSAGE);
						responseStream.append(BADLY_FORMED_QUERY_MESSAGE);
						statusCode = HttpServletResponse.SC_BAD_REQUEST;
					}
				} else {
					log.info(MISSING_QUERY_MESSAGE);
					responseStream.append(MISSING_QUERY_MESSAGE);
					statusCode = HttpServletResponse.SC_BAD_REQUEST;
				}
			} else {
				log.info(INVALID_SESSION_TOKEN_MESSAGE);
				responseStream.append(INVALID_SESSION_TOKEN_MESSAGE);
				statusCode = HttpServletResponse.SC_UNAUTHORIZED;
			}
		} catch (Throwable t) {
			log.warning(INTERNAL_ERROR_MESSAGE, t);
			responseStream.print(INTERNAL_ERROR_MESSAGE + ": " + t.getMessage());
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			if (responseStream != null) {
				responseStream.flush();
				responseStream.close();
			}
		}
		httpResponse.setStatus(statusCode);
	}

	private boolean handleDICOMVisu(PrintWriter responseStream, String studyIdKey, String seriesIdKey, String imageIdKey)
	{
		boolean success;

		try {
			File tempDicom = File.createTempFile(imageIdKey, ".tmp");

			feedFileWithDicomFromWado(tempDicom, studyIdKey, seriesIdKey, imageIdKey);

			SourceImage srcDicomImage = new SourceImage(tempDicom.getAbsolutePath());
			double windowWidth = 0.0;
			double windowCenter = 0.0;

			if (srcDicomImage != null) {
				log.info("DICOM image is valid");
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
			String separator = config.getParam("fieldSeparator");
			responseStream.println("windowWidth" + separator + "windowCenter");
			responseStream.println(windowWidth + separator + windowCenter);
			log.info("windowWidth" + separator + "windowCenter");
			log.info(windowWidth + separator + windowCenter);
			success = true;
		} catch (DicomException e) {
			log.warning("Error reading DICOM image ", e);
			success = false;
		} catch (IOException e) {
			log.warning("Error getting DICOM image from WADO", e);
			success = false;
		}
		return success;
	}

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

	private void feedFileWithDicomFromWado(File temp, String studyIdKey, String seriesIdKey, String imageIdKey)
	{

		// we use wado to get the dicom image
		String host = config.getParam("NameServer");
		int port = config.getIntParam("DicomServerWadoPort");
		String base = config.getParam("WadoUrlExtension");

		WadoUrlBuilder wadoUrlBuilder = new WadoUrlBuilder(host, port, base, WadoUrlBuilder.ContentType.FILE);

		// GET WADO call result.
		wadoUrlBuilder.setStudyUID(studyIdKey);
		wadoUrlBuilder.setSeriesUID(seriesIdKey);
		wadoUrlBuilder.setObjectUID(imageIdKey);

		try {
			String wadoUrl = wadoUrlBuilder.build();
			log.info("Build wadoUrl = " + wadoUrl);

			// --Get the Dicom file from the server
			HttpClient client = new HttpClient();

			GetMethod method = new GetMethod(wadoUrl);
			// Execute the GET method
			int statusCode = client.executeMethod(method);

			if (statusCode != -1) {
				// Get the result as stream
				InputStream res = method.getResponseBodyAsStream();
				// write the inputStream to a FileOutputStream
				OutputStream out = new FileOutputStream(temp);

				int read = 0;
				byte[] bytes = new byte[4096];

				while ((read = res.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				res.close();
				out.flush();
				out.close();
			}

		} catch (UnsupportedEncodingException e) {
			log.warning("Not able to build wado url for : " + temp.getName(), e);
		} catch (HttpException e) {
			log.warning("Not able to get the wado image : " + temp.getName(), e);
		} catch (IOException e) {
			log.warning("Not able to write the temp dicom image : " + temp.getName(), e);
		}
	}

}
