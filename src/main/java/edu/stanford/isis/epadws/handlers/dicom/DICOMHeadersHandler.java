package edu.stanford.isis.epadws.handlers.dicom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;

import edu.stanford.isis.epad.common.dicom.DICOMElementResult;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.EPADTools;
import edu.stanford.isis.epad.common.util.JsonHelper;
import edu.stanford.isis.epadws.processing.pipeline.task.DicomHeadersTask;
import edu.stanford.isis.epadws.xnat.XNATOperations;

/**
 * Download headers for a series or study in one quick step.
 */
public class DICOMHeadersHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String INTERNAL_ERROR_MESSAGE = "Internal error on DICOM headers route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on DICOM headers route";
	private static final String MISSING_QUERY_MESSAGE = "No query paramaters specified  on DICOM headers route";
	private static final String BADLY_FORMED_QUERY_MESSAGE = "Invalid query paramaters specified  on DICOM headers route";
	private static final String WADO_INVOCATION_ERROR_MESSAGE = "Error retrieving header from WADO on DICOM headers route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("application/json");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (XNATOperations.hasValidXNATSessionID(httpRequest)) {
				String queryString = httpRequest.getQueryString();
				queryString = URLDecoder.decode(queryString, "UTF-8");
				if (queryString != null) {
					statusCode = performDICOMHeaderRequest(responseStream, queryString.trim());
				} else {
					log.info(MISSING_QUERY_MESSAGE);
					responseStream.append(JsonHelper.createJSONErrorResponse(MISSING_QUERY_MESSAGE));
					statusCode = HttpServletResponse.SC_BAD_REQUEST;
				}
			} else {
				log.info(INVALID_SESSION_TOKEN_MESSAGE);
				responseStream.append(JsonHelper.createJSONErrorResponse(INVALID_SESSION_TOKEN_MESSAGE));
				statusCode = HttpServletResponse.SC_UNAUTHORIZED;
			}
			responseStream.flush();
		} catch (Throwable t) {
			log.warning(INTERNAL_ERROR_MESSAGE, t);
			if (responseStream != null)
				responseStream.print(JsonHelper.createJSONErrorResponse(INTERNAL_ERROR_MESSAGE, t));
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		httpResponse.setStatus(statusCode);
	}

	private int performDICOMHeaderRequest(PrintWriter responseStream, String queryString) throws IOException,
			FileNotFoundException
	{
		String studyIdKey = getStudyUIDFromRequest(queryString);
		String seriesIdKey = getSeriesUIDFromRequest(queryString);
		String imageIdKey = getInstanceUIDFromRequest(queryString);
		int statusCode;

		log.info("DICOMHeadersHandler query: " + queryString);

		if (studyIdKey != null && seriesIdKey != null && imageIdKey != null) {
			File tempDICOMFile = File.createTempFile(imageIdKey, ".tmp");
			int wadoStatusCode = EPADTools.downloadDICOMFileFromWADO(tempDICOMFile, studyIdKey, seriesIdKey, imageIdKey);
			if (wadoStatusCode == HttpServletResponse.SC_OK) {
				File tempTag = File.createTempFile(imageIdKey, "_tag.tmp");
				ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
				taskExecutor.execute(new DicomHeadersTask(tempDICOMFile, tempTag));
				taskExecutor.shutdown();
				try {
					taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
					BufferedReader tagReader = null;
					try {
						boolean isFirst = true;
						String dicomElement;
						tagReader = new BufferedReader(new FileReader(tempTag.getAbsolutePath()));

						responseStream.append("{ \"ResultSet\": [");

						while ((dicomElement = tagReader.readLine()) != null) {
							DICOMElementResult dicomElementResult = decodeDICOMElement(dicomElement);
							if (dicomElementResult != null) {
								if (!isFirst)
									responseStream.append(",\n");
								isFirst = false;
								responseStream.println(dicomElementResult2JSON(dicomElementResult));
							}
						}
						responseStream.append("] }");
					} finally {
						if (tagReader != null) {
							try {
								tagReader.close();
							} catch (IOException e) {
								log.warning("Error closing DICOM tag response stream", e);
							}
						}
					}
					statusCode = HttpServletResponse.SC_OK;
				} catch (InterruptedException e) {
					log.info("DICOM headers task interrupted");
					responseStream.print(JsonHelper.createJSONErrorResponse("DICOM headers task interrupted"));
					Thread.currentThread().interrupt();
					statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				}
			} else {
				log.info(WADO_INVOCATION_ERROR_MESSAGE + "; status code=" + wadoStatusCode);
				responseStream.print(JsonHelper.createJSONErrorResponse(WADO_INVOCATION_ERROR_MESSAGE + "; status code="
						+ wadoStatusCode));
				statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			}
		} else {
			log.info(BADLY_FORMED_QUERY_MESSAGE);
			responseStream.append(JsonHelper.createJSONErrorResponse(BADLY_FORMED_QUERY_MESSAGE));
			statusCode = HttpServletResponse.SC_BAD_REQUEST;
		}
		return statusCode;
	}

	private String dicomElementResult2JSON(DICOMElementResult dicomElementResult)
	{
		Gson gson = new Gson();

		return gson.toJson(dicomElementResult);
	}

	// TODO This code is very brittle. Rewrite to make more robust. Also ignores DICOM sequences.
	private DICOMElementResult decodeDICOMElement(String dicomElement)
	{
		String[] fields = dicomElement.split(" ");

		int valueFieldStartIndex = valueFieldStartIndex(fields);
		if (valueFieldStartIndex != -1) {
			int valueFieldEndIndex = valueFieldEndIndex(fields);

			if (valueFieldEndIndex != -1 && ((valueFieldEndIndex - valueFieldStartIndex) < 10)) {
				String tagCode = extractTagCodeFromField(fields[0]);
				String value = stripBraces(assembleValue(fields, valueFieldStartIndex, valueFieldEndIndex));
				String tagName = assembleValue(fields, valueFieldEndIndex + 1, fields.length - 1);

				return new DICOMElementResult(tagCode, tagName, value);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private String extractTagCodeFromField(String field)
	{
		String subFields[] = field.split(":>*");

		if (subFields.length == 2) {
			return subFields[1];
		} else
			return "";
	}

	private String assembleValue(String[] fields, int startIndex, int finishIndex)
	{
		String value = "";
		for (int i = startIndex; i <= finishIndex; i++) {
			if (i > startIndex)
				value += " ";
			value += fields[i];
		}
		return value;
	}

	private int valueFieldStartIndex(String[] fields)
	{
		for (int i = 0; i < fields.length; i++)
			if (fields[i].startsWith("["))
				return i;
		return -1;
	}

	private int valueFieldEndIndex(String[] fields)
	{
		for (int i = 0; i < fields.length; i++)
			if (fields[i].endsWith("]"))
				return i;
		return -1;
	}

	private String stripBraces(String valueField)
	{
		if (valueField.startsWith("[") && valueField.endsWith("]")) {
			return valueField.substring(1, valueField.length() - 1);
		} else {
			return "";
		}
	}

	// TODO Fix this mess
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
