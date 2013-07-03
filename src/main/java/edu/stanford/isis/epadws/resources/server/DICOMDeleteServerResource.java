package edu.stanford.isis.epadws.resources.server;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.resource.Get;

import edu.stanford.isis.epadws.db.mysql.pipeline.DicomDeleteTask;

/**
 * This is not RESTful. For moment, we are just replicating the pre-Restlet code. TODO Refactor to make RESTful.
 */
public class DICOMDeleteServerResource extends BaseServerResource
{
	private static final String SUCCESS_MESSAGE = "Image deleted";
	private static final String NO_QUERY_ERROR_MESSAGE = "No query present in request";

	public DICOMDeleteServerResource()
	{
		setNegotiated(false); // Disable content negotiation
	}

	@Override
	protected void doCatch(Throwable throwable)
	{
		log.warning("An exception was thrown in the DICOM delete resource.", throwable);
	}

	@Get
	// TODO Should be DELETE
	public String deleteImage()
	{
		String queryString = getQuery().getQueryString(CharacterSet.UTF_8);
		log.info("Delete query from ePad : " + queryString);

		if (queryString != null) {
			queryString = queryString.trim();
			if (isSeriesRequest(queryString)) {
				handleSeriesRequest(queryString);
			} else {
				handleStudyRequest(queryString);
			}
			setStatus(Status.SUCCESS_OK);
			return SUCCESS_MESSAGE;
		} else {
			log.info(NO_QUERY_ERROR_MESSAGE);
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return NO_QUERY_ERROR_MESSAGE;
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
