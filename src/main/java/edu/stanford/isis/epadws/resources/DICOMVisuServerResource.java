package edu.stanford.isis.epadws.resources;

import ij.ImagePlus;
import ij.io.Opener;
import ij.measure.Calibration;

import java.io.File;
import java.io.IOException;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.resource.Get;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SourceImage;

import edu.stanford.isis.epad.common.util.EPADTools;

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
		EPADTools.downloadDICOMFileFromWADO(tempDicom, studyIdKey, seriesIdKey, imageIdKey);
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

		String separator = config.getStringPropertyValue("fieldSeparator"); // TODO Constants for these names
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
}
