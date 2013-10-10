package edu.stanford.isis.epadws.handlers.dicom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.dicom.DICOMElementResult;
import edu.stanford.isis.epad.common.util.JsonHelper;
import edu.stanford.isis.epad.common.util.WadoUrlBuilder;
import edu.stanford.isis.epadws.processing.pipeline.DicomHeadersTask;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * Download headers for a series or study in one quick step.
 */
public class DICOMHeadersHandlerJSON extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();
	private static final ProxyConfig config = ProxyConfig.getInstance();

	private static final String INTERNAL_ERROR_MESSAGE = "Internal error on delete";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";
	private static final String MISSING_QUERY_MESSAGE = "No query paramaters specified";
	private static final String BADLY_FORMED_QUERY_MESSAGE = "Invalid query paramaters specified";
	private static final String WADO_INVOCATION_ERROR_MESSAGE = "Error retrieving header from WADO";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter outputStream = null;
		int statusCode;

		httpResponse.setContentType("application/json");
		request.setHandled(true);

		try {
			outputStream = httpResponse.getWriter();

			if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
				String queryString = httpRequest.getQueryString();
				queryString = URLDecoder.decode(queryString, "UTF-8");

				if (queryString != null) {
					statusCode = performDICOMHeaderRequest(outputStream, queryString.trim());
				} else {
					log.info(MISSING_QUERY_MESSAGE);
					outputStream.append(JsonHelper.createJSONErrorResponse(MISSING_QUERY_MESSAGE));
					statusCode = HttpServletResponse.SC_BAD_REQUEST;
				}
			} else {
				log.info(INVALID_SESSION_TOKEN_MESSAGE);
				outputStream.append(JsonHelper.createJSONErrorResponse(INVALID_SESSION_TOKEN_MESSAGE));
				statusCode = HttpServletResponse.SC_UNAUTHORIZED;
			}
		} catch (Throwable t) {
			log.warning(INTERNAL_ERROR_MESSAGE, t);
			outputStream.print(JsonHelper.createJSONErrorResponse(INTERNAL_ERROR_MESSAGE, t));
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}
		httpResponse.setStatus(statusCode);
	}

	private int performDICOMHeaderRequest(PrintWriter outputStream, String queryString) throws IOException,
			FileNotFoundException
	{
		String studyIdKey = getStudyUIDFromRequest(queryString);
		String seriesIdKey = getSeriesUIDFromRequest(queryString);
		String imageIdKey = getInstanceUIDFromRequest(queryString);
		int statusCode;

		log.info("DICOMHeadersHandler query: " + queryString);

		if (studyIdKey != null && seriesIdKey != null && imageIdKey != null) {
			File tempDICOMFile = File.createTempFile(imageIdKey, ".tmp");
			int wadoStatusCode = feedFileWithDICOMFromWADO(tempDICOMFile, studyIdKey, seriesIdKey, imageIdKey);
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

						outputStream.append("{ \"ResultSet\": [");

						while ((dicomElement = tagReader.readLine()) != null) {
							DICOMElementResult dicomElementResult = decodeDICOMElement(dicomElement);
							if (dicomElementResult != null) {
								if (!isFirst)
									outputStream.append(",\n");
								isFirst = false;
								outputStream.println(dicomElementResult2JSON(dicomElementResult));
							}
						}
						outputStream.append("] }");
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
					outputStream.print(JsonHelper.createJSONErrorResponse("DICOM headers task interrupted"));
					Thread.currentThread().interrupt();
					statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				}
			} else {
				log.info(WADO_INVOCATION_ERROR_MESSAGE + "; status code=" + wadoStatusCode);
				outputStream.print(JsonHelper.createJSONErrorResponse(WADO_INVOCATION_ERROR_MESSAGE + "; status code="
						+ wadoStatusCode));
				statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			}
		} else {
			log.info(BADLY_FORMED_QUERY_MESSAGE);
			outputStream.append(JsonHelper.createJSONErrorResponse(BADLY_FORMED_QUERY_MESSAGE));
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

	private int feedFileWithDICOMFromWADO(File temp, String studyIdKey, String seriesIdKey, String imageIdKey)
			throws IOException
	{
		String host = config.getParam("NameServer");
		int port = config.getIntParam("DicomServerWadoPort");
		String base = config.getParam("WadoUrlExtension");

		WadoUrlBuilder wadoUrlBuilder = new WadoUrlBuilder(host, port, base, WadoUrlBuilder.ContentType.FILE);

		wadoUrlBuilder.setStudyUID(studyIdKey);
		wadoUrlBuilder.setSeriesUID(seriesIdKey);
		wadoUrlBuilder.setObjectUID(imageIdKey);

		String wadoUrl = wadoUrlBuilder.build();

		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(wadoUrl);
		int statusCode = client.executeMethod(method);

		if (statusCode == HttpServletResponse.SC_OK) {
			InputStream wadoResponseStream = null;
			OutputStream outputStream = null;
			try {
				wadoResponseStream = method.getResponseBodyAsStream();
				outputStream = new FileOutputStream(temp);
				int read = 0;
				byte[] bytes = new byte[4096];
				while ((read = wadoResponseStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
			} finally {
				if (wadoResponseStream != null)
					wadoResponseStream.close();
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			}
		}
		return statusCode;
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
