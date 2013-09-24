package edu.stanford.isis.epadws.resources.server;

import ij.ImagePlus;
import ij.io.Opener;
import ij.measure.Calibration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.resource.Get;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SourceImage;

import edu.stanford.isis.epad.common.util.WadoUrlBuilder;

public class DICOMVisuServerResource extends BaseServerResource
{
	private static final String SUCCESS_MESSAGE = "Request succeeded.";
	private static final String BAD_DICOM_REFERENCE_MESSAGE = "Bad DICOM reference: ";
	private static final String BAD_REQUEST_MESSAGE = "Bad request - no query";
	private static final String DICOM_EXCEPTION_MESSAGE = "DICOM exception: ";
	private static final String IO_EXCEPTION_MESSAGE = "IO exception: ";

	public DICOMVisuServerResource()
	{
		setNegotiated(false); // Disable content negotiation
	}

	@Override
	protected void doCatch(Throwable throwable)
	{
		log.warning("An exception was thrown in the DICOM VISU resource.", throwable);
	}

	@Get("text")
	public String query()
	{
		String queryString = getQuery().getQueryString(CharacterSet.UTF_8);
		log.info("Received DICOM VISU query from ePAD : " + queryString);

		if (queryString != null) {
			queryString = queryString.trim();
			String studyIdKey = getStudyUIDFromRequest(queryString);
			String seriesIdKey = getSeriesUIDFromRequest(queryString);
			String imageIdKey = getInstanceUIDFromRequest(queryString);

			if (studyIdKey != null && seriesIdKey != null && imageIdKey != null) { // Get the WADO and the tag file
				try {
					String out = calculate(studyIdKey, seriesIdKey, imageIdKey);

					log.info(SUCCESS_MESSAGE);
					setStatus(Status.SUCCESS_OK);
					return out.toString();
				} catch (DicomException e) {
					log.warning(DICOM_EXCEPTION_MESSAGE, e);
					setStatus(Status.SERVER_ERROR_INTERNAL);
					return DICOM_EXCEPTION_MESSAGE + ": " + e.getMessage();
				} catch (IOException e) {
					log.warning(IO_EXCEPTION_MESSAGE, e);
					setStatus(Status.SERVER_ERROR_INTERNAL);
					return IO_EXCEPTION_MESSAGE + ": " + e.getMessage();
				}
			} else {
				log.info(BAD_DICOM_REFERENCE_MESSAGE);
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return BAD_DICOM_REFERENCE_MESSAGE;
			}
		} else {
			log.info(BAD_REQUEST_MESSAGE);
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return BAD_REQUEST_MESSAGE;
		}
	}

	private String calculate(String studyIdKey, String seriesIdKey, String imageIdKey) throws IOException, DicomException
	{
		StringBuilder out = new StringBuilder();
		File tempDicom = File.createTempFile(imageIdKey, ".tmp");
		feedFileWithDicomFromWado(tempDicom, studyIdKey, seriesIdKey, imageIdKey);
		SourceImage srcDicomImage = new SourceImage(tempDicom.getAbsolutePath());
		double windowWidth = 0.0;
		double windowCenter = 0.0;

		if (srcDicomImage != null) {
			log.info("Dicom image is valid");
			/*
			 * ImageEnhancer ie=new ImageEnhancer(srcDicomImage); ie.findVisuParametersImage();
			 * 
			 * windowWidth=ie.getWindowWidth(); windowCenter=ie.getWindowCenter();
			 */

			Opener opener = new Opener();
			String imageFilePath = tempDicom.getAbsolutePath();
			ImagePlus imp = opener.openImage(imageFilePath);
			// ImageProcessor ip = imp.getProcessor();

			double min = imp.getDisplayRangeMin();
			double max = imp.getDisplayRangeMax();
			Calibration cal = imp.getCalibration();
			// int digits = (ip instanceof FloatProcessor) || cal.calibrated() ? 2 : 0;
			double minValue = cal.getCValue(min);
			double maxValue = cal.getCValue(max);

			windowWidth = (maxValue - minValue);
			windowCenter = (minValue + windowWidth / 2.0);
		}

		String separator = config.getParam("fieldSeparator"); // TODO Constants for these names
		out.append("windowWidth" + separator + "windowCenten");
		out.append(windowWidth + separator + windowCenter + "\n");

		return out.toString();
	}

	private static String getStudyUIDFromRequest(String queryString)
	{
		log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[0].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getSeriesUIDFromRequest(String queryString)
	{
		log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[1].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getInstanceUIDFromRequest(String queryString)
	{
		log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[2].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private void feedFileWithDicomFromWado(File temp, String studyIdKey, String seriesIdKey, String imageIdKey)
	{ // We use WADO to get the DICOM image
		String host = config.getParam("NameServer");
		int port = config.getIntParam("DicomServerWadoPort");
		String base = config.getParam("WadoUrlExtension");

		WadoUrlBuilder wadoUrlBuilder = new WadoUrlBuilder(host, port, base, WadoUrlBuilder.ContentType.FILE);

		wadoUrlBuilder.setStudyUID(studyIdKey);
		wadoUrlBuilder.setSeriesUID(seriesIdKey);
		wadoUrlBuilder.setObjectUID(imageIdKey);

		try {
			String wadoUrl = wadoUrlBuilder.build();
			log.info("WADO URL = " + wadoUrl);

			// --Get the Dicom file from the server
			HttpClient client = new HttpClient();

			GetMethod method = new GetMethod(wadoUrl);
			// Execute the GET method
			int statusCode = client.executeMethod(method);

			if (statusCode != -1) {
				// Get the result as a stream
				InputStream res = method.getResponseBodyAsStream();
				// Write the inputStream to a FileOutputStream
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
