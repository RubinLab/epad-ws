package edu.stanford.isis.epadws.resources.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.resource.Get;

import edu.stanford.isis.epadws.common.WadoUrlBuilder;
import edu.stanford.isis.epadws.db.mysql.pipeline.DicomHeadersTask;

public class DICOMHeadersServerResource extends BaseServerResource
{
	private static final String SUCCESS_MESSAGE = "Request succeeded.";
	private static final String FILE_NOT_FOUND_MESSAGE = "File not found: ";
	private static final String IO_EXCEPTION_MESSAGE = "IO exception: ";
	private static final String BAD_DICOM_REFERENCE_MESSAGE = "Bad DICOM reference: ";
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
					StringBuilder out = new StringBuilder();
					executeDICOMHeadersTask(studyIdKey, seriesIdKey, imageIdKey, out);
					log.info(SUCCESS_MESSAGE);
					setStatus(Status.SUCCESS_OK);
					return out.toString();
				} catch (FileNotFoundException e) {
					log.warning(FILE_NOT_FOUND_MESSAGE, e);
					setStatus(Status.SERVER_ERROR_INTERNAL);
					return FILE_NOT_FOUND_MESSAGE + e.getMessage();
				} catch (IOException e) {
					log.warning(IO_EXCEPTION_MESSAGE, e);
					setStatus(Status.SERVER_ERROR_INTERNAL);
					return IO_EXCEPTION_MESSAGE + e.getMessage();
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

	private void executeDICOMHeadersTask(String studyIdKey, String seriesIdKey, String imageIdKey, StringBuilder out)
			throws IOException, FileNotFoundException
	{
		File tempDicom = File.createTempFile(imageIdKey, ".tmp");
		feedFileWithDicomFromWado(tempDicom, studyIdKey, seriesIdKey, imageIdKey);
		File tempTag = File.createTempFile(imageIdKey, "_tag.tmp");

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

	private void feedFileWithDicomFromWado(File temp, String studyIdKey, String seriesIdKey, String imageIdKey)
	{ // We use WADO to get the DICOM image
		String host = config.getParam("NameServer"); // TODO Constants for these parameter names
		int port = config.getIntParam("DicomServerWadoPort");
		String base = config.getParam("WadoUrlExtension");

		WadoUrlBuilder wadoUrlBuilder = new WadoUrlBuilder(host, port, base, WadoUrlBuilder.Type.FILE);

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
