package edu.stanford.isis.epadws.resources.server;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.resource.Get;

import edu.stanford.isis.epadws.processing.pipeline.DicomDeleteTask;

/**
 * This is not RESTful. For moment, we are just replicating the pre-Restlet code.
 * <p>
 * TODO Refactor to make RESTful. To test:
 * 
 * <pre>
 * curl -X GET "http://localhost:8080/dicomdelete?seriesuid=23"
 * curl -X GET "http://localhost:8080/dicomdelete?studyuid=332"
 * </pre>
 */
public class DICOMDeleteServerResource extends BaseServerResource
{
	private static final String SUCCESS_MESSAGE = "DICOM image deleted";
	private static final String FAILURE_MESSAGE = "Error deleting DICOM image";
	private static final String NO_QUERY_ERROR_MESSAGE = "No DICOM series or study present in request";

	public DICOMDeleteServerResource()
	{
		setNegotiated(false); // Disable content negotiation
	}

	@Override
	protected void doCatch(Throwable throwable)
	{
		log.warning("An exception was thrown in the DICOM delete resource.", throwable);
	}

	@Get("text")
	// TODO Should be DELETE
	public String deleteImage()
	{
		String queryString = getQuery().getQueryString(CharacterSet.UTF_8);
		log.info("DICOM delete query from ePAD : " + queryString);

		if (queryString != null && queryString.length() != 0) {
			queryString = queryString.trim();

			try {
				if (isDICOMSeriesRequest(queryString)) {
					handleDICOMSeriesRequest(queryString);
				} else {
					handleDICOMStudyRequest(queryString);
				}
				setStatus(Status.SUCCESS_OK);
				return SUCCESS_MESSAGE;
			} catch (Exception e) {
				log.warning(FAILURE_MESSAGE, e);
				setStatus(Status.SERVER_ERROR_INTERNAL);
				return FAILURE_MESSAGE + ": " + e.getMessage();
			}
		} else {
			log.info(NO_QUERY_ERROR_MESSAGE);
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return NO_QUERY_ERROR_MESSAGE;
		}
	}

	private void handleDICOMStudyRequest(String queryString) throws Exception
	{
		log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[1].trim();
		parts = value.split("=");
		value = parts[1].trim();

		log.info("DicomDeleteHandler(study) = " + value);
		(new Thread(new DicomDeleteTask(value, true))).start();
	}

	private void handleDICOMSeriesRequest(String queryString) throws Exception
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
	private static boolean isDICOMSeriesRequest(String queryString)
	{
		String check = queryString.toLowerCase().trim();
		boolean isSeries = check.indexOf("eletetype=series") > 0;

		log.info(" isSeries=" + isSeries + " for: " + queryString);
		return isSeries;
	}

}
