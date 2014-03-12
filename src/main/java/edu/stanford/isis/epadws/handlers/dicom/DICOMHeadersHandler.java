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

import edu.stanford.epad.dtos.DICOMElement;
import edu.stanford.epad.dtos.DICOMElementList;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.EPADTools;
import edu.stanford.isis.epadws.handlers.HandlerUtil;
import edu.stanford.isis.epadws.processing.pipeline.task.DicomHeadersTask;
import edu.stanford.isis.epadws.xnat.XNATSessionOperations;

/**
 * Download headers for a series or study in one quick step.
 */
public class DICOMHeadersHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String INTERNAL_ERROR_MESSAGE = "Warning: internal error on DICOM headers route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on DICOM headers route";
	private static final String MISSING_QUERY_MESSAGE = "No query paramaters specified  on DICOM headers route";
	private static final String BADLY_FORMED_QUERY_MESSAGE = "Invalid query paramaters specified  on DICOM headers route";
	private static final String WADO_INVOCATION_ERROR_MESSAGE = "Warning: error retrieving header from WADO on DICOM headers route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("application/json");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String queryString = httpRequest.getQueryString();
				queryString = URLDecoder.decode(queryString, "UTF-8");
				if (queryString != null) {
					statusCode = performDICOMHeaderRequest(responseStream, queryString.trim());
				} else {
					statusCode = HandlerUtil.infoJSONResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_QUERY_MESSAGE,
							responseStream, log);
				}
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
			responseStream.flush();
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
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
						String dicomElementString;
						tagReader = new BufferedReader(new FileReader(tempTag.getAbsolutePath()));
						DICOMElementList dicomElementList = new DICOMElementList();

						while ((dicomElementString = tagReader.readLine()) != null) {
							DICOMElement dicomElement = decodeDICOMElementString(dicomElementString);
							if (dicomElement != null) {
								dicomElementList.addDICOMElement(dicomElement);
							} else {
								log.warning("Warning: could not decode DICOM element " + dicomElementString + "; skipping");
							}
						}
						responseStream.append(dicomElementList.toJSON());
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
					Thread.currentThread().interrupt();
					statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Warning: DICOM headers task interrupted!", responseStream, log);
				}
			} else {
				statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						WADO_INVOCATION_ERROR_MESSAGE + "; status code=" + wadoStatusCode, responseStream, log);
			}
		} else {
			statusCode = HandlerUtil.infoJSONResponse(HttpServletResponse.SC_BAD_REQUEST, BADLY_FORMED_QUERY_MESSAGE,
					responseStream, log);
		}
		return statusCode;
	}

	private String dicomElementResult2JSON(DICOMElement dicomElementResult)
	{
		Gson gson = new Gson();

		return gson.toJson(dicomElementResult);
	}

	// TODO This code is very brittle. Rewrite to make more robust. Also ignores DICOM sequences.
	private DICOMElement decodeDICOMElementString(String dicomElement)
	{
		String[] fields = dicomElement.split(" ");

		int valueFieldStartIndex = valueFieldStartIndex(fields);
		if (valueFieldStartIndex != -1) {
			int valueFieldEndIndex = valueFieldEndIndex(fields);

			if (valueFieldEndIndex != -1 && ((valueFieldEndIndex - valueFieldStartIndex) < 10)) {
				String tagCode = extractTagCodeFromField(fields[0]);
				String value = stripBraces(assembleValue(fields, valueFieldStartIndex, valueFieldEndIndex));
				String tagName = assembleValue(fields, valueFieldEndIndex + 1, fields.length - 1);

				return new DICOMElement(tagCode, tagName, value);
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
