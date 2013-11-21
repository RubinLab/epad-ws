package edu.stanford.isis.epadws.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.resource.Get;

import edu.stanford.isis.epad.common.util.EPADTools;
import edu.stanford.isis.epadws.processing.pipeline.task.DicomHeadersTask;

public class DICOMHeadersServerResource extends BaseServerResource
{
	private static final String SUCCESS_MESSAGE = "Request succeeded";
	private static final String FILE_NOT_FOUND_MESSAGE = "File not found";
	private static final String IO_EXCEPTION_MESSAGE = "IO exception";
	private static final String BAD_DICOM_REFERENCE_MESSAGE = "Bad DICOM reference";
	private static final String BAD_REQUEST_MESSAGE = "Bad request - no query";

	public DICOMHeadersServerResource()
	{
		setNegotiated(false); // Disable content negotiation
	}

	@Override
	protected void doCatch(Throwable throwable)
	{
		log.warning("An exception was thrown in the DICOM headers resource.", throwable);
	}

	@Get("text")
	public String query()
	{
		String queryString = getQuery().getQueryString(CharacterSet.UTF_8);
		log.info("DICOM header query from ePAD : " + queryString);

		if (queryString != null) {
			queryString = queryString.trim();
			String studyIdKey = getStudyUIDFromRequest(queryString);
			String seriesIdKey = getSeriesUIDFromRequest(queryString);
			String imageIdKey = getInstanceUIDFromRequest(queryString);

			if (studyIdKey != null && seriesIdKey != null && imageIdKey != null) {
				try { // Get the WADO and the tag file
					String out = executeDICOMHeadersTask(studyIdKey, seriesIdKey, imageIdKey);
					log.info(SUCCESS_MESSAGE);
					setStatus(Status.SUCCESS_OK);
					return out;
				} catch (FileNotFoundException e) {
					log.warning(FILE_NOT_FOUND_MESSAGE, e);
					setStatus(Status.SERVER_ERROR_INTERNAL);
					return FILE_NOT_FOUND_MESSAGE + ": " + e.getMessage();
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

	private String executeDICOMHeadersTask(String studyIdKey, String seriesIdKey, String imageIdKey) throws IOException,
			FileNotFoundException
	{
		File tempDicom = File.createTempFile(imageIdKey, ".tmp");
		EPADTools.downloadDICOMFileFromWADO(tempDicom, studyIdKey, seriesIdKey, imageIdKey);
		File tempTag = File.createTempFile(imageIdKey, "_tag.tmp");
		StringBuilder out = new StringBuilder();

		ExecutorService taskExecutor = Executors.newFixedThreadPool(4); // Generation of the tag file
		taskExecutor.execute(new DicomHeadersTask(tempDicom, tempTag));
		taskExecutor.shutdown();
		try {
			taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			BufferedReader in = new BufferedReader(new FileReader(tempTag.getAbsolutePath()));
			try {
				String line;
				while ((line = in.readLine()) != null) {
					out.append(line);
				}
			} finally {
				in.close();
			}
		} catch (InterruptedException e) {
		}
		return out.toString();
	}

	private String getStudyUIDFromRequest(String queryString)
	{
		log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[0].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private String getSeriesUIDFromRequest(String queryString)
	{
		log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[1].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private String getInstanceUIDFromRequest(String queryString)
	{
		log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[2].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}
}
