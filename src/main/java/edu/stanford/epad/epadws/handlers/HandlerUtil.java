package edu.stanford.epad.epadws.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;

import com.sun.jersey.api.uri.UriTemplate;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.JsonHelper;
import edu.stanford.epad.dtos.EPADMessage;

/**
 * Utility methods for handlers
 * 
 * 
 * @author martin
 */
public class HandlerUtil
{
	public static int infoResponse(int responseCode, String message, PrintWriter responseStream, EPADLogger log)
	{
		log.info(message);
		if (responseStream != null)
			responseStream.append(message);
		return responseCode;
	}

	public static int infoResponse(int responseCode, String message, EPADLogger log)
	{
		return infoResponse(responseCode, message, null, log);
	}

	public static int infoJSONResponse(int responseCode, String message, PrintWriter responseStream, EPADLogger log)
	{
		log.info(message);
		if (responseStream != null)
			responseStream.append(JsonHelper.createJSONErrorResponse(message));
		return responseCode;
	}

	public static int infoJSONResponse(int responseCode, String message, EPADLogger log)
	{
		return infoJSONResponse(responseCode, message, null, log);
	}

	public static int warningResponse(int responseCode, String message, PrintWriter responseStream, EPADLogger log)
	{
		log.warning(message);
		if (responseStream != null)
			responseStream.append(message);
		return responseCode;
	}

	public static int warningResponse(int responseCode, String message, EPADLogger log)
	{
		log.warning(message);
		return responseCode;
	}

	public static int warningJSONResponse(int responseCode, String message, PrintWriter responseStream, EPADLogger log)
	{
		log.warning(message);
		if (responseStream != null)
			responseStream.append(JsonHelper.createJSONErrorResponse(message));
		return responseCode;
	}

	public static int warningResponse(int responseCode, String message, Throwable t, PrintWriter responseStream,
			EPADLogger log)
	{
		String finalMessage = message + (t == null ? "" : ((t.getMessage() == null) ? "" : ": " + t.getMessage()));
		log.warning(finalMessage, t);
		if (responseStream != null)
			responseStream.append(new EPADMessage(finalMessage, Level.WARN).toJSON());
		return responseCode;
	}

	public static int warningJSONResponse(int responseCode, String message, Throwable t, PrintWriter responseStream,
			EPADLogger log)
	{
		String finalMessage = message + (t == null ? "" : ((t.getMessage() == null) ? "" : ": " + t.getMessage()));
		log.warning(finalMessage, t);
		if (responseStream != null)
			responseStream.append(new EPADMessage(finalMessage).toJSON());
		return responseCode;
	}

	public static int warningJSONResponse(int responseCode, String message, Throwable t, EPADLogger log)
	{
		String finalMessage = message + (t == null ? "" : ((t.getMessage() == null) ? "" : ": " + t.getMessage()));
		log.warning(finalMessage, t);
		return responseCode;
	}

	public static int warningResponse(int responseCode, String message, Throwable t, EPADLogger log)
	{
		String finalMessage = message + (t == null ? "" : ((t.getMessage() == null) ? "" : ": " + t.getMessage()));
		log.warning(finalMessage, t);
		return responseCode;
	}

	public static int warningJSONResponse(int responseCode, String message, EPADLogger log)
	{
		log.warning(message);
		return responseCode;
	}

	public static int internalErrorResponse(String message, PrintWriter responseStream, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, responseStream, log);
	}

	public static int internalErrorResponse(String message, Throwable t, PrintWriter responseStream, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, t, responseStream, log);
	}

	public static int internalErrorResponse(String message, Throwable t, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, t, log);
	}

	public static int internalErrorResponse(String message, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, log);
	}

	public static int internalErrorJSONResponse(String message, Throwable t, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, t, log);
	}

	public static int internalErrorJSONResponse(String message, Throwable t, PrintWriter responseStream, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, t, responseStream, log);
	}

	public static int internalErrorJSONResponse(String message, PrintWriter responseStream, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, responseStream, log);
	}

	public static int invalidTokenJSONResponse(String message, PrintWriter responseStream, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_UNAUTHORIZED, message, responseStream, log);
	}

	public static int invalidTokenJSONResponse(String message, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_UNAUTHORIZED, message, log);
	}

	public static int invalidTokenResponse(String message, PrintWriter responseStream, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_UNAUTHORIZED, message, responseStream, log);
	}

	public static int badRequestResponse(String message, PrintWriter responseStream, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_BAD_REQUEST, message, responseStream, log);
	}

	public static int badRequestResponse(String message, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_BAD_REQUEST, message, log);
	}

	public static int invalidTokenResponse(String message, Throwable t, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_UNAUTHORIZED, message, t, log);
	}

	public static int invalidTokenResponse(String message, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_UNAUTHORIZED, message, log);
	}

	public static int badRequestJSONResponse(String message, PrintWriter responseStream, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, message, responseStream, log);
	}

	public static String getTemplateParameter(Map<String, String> templateMap, String parameterName)
	{
		if (templateMap.containsKey(parameterName)) {
			return templateMap.get(parameterName);
		} else
			throw new IllegalArgumentException("no " + parameterName + " parameter in request");
	}

	public static String getTemplateParameter(Map<String, String> templateMap, String parameterName, String defaultValue)
	{
		if (templateMap.containsKey(parameterName)) {
			return templateMap.get(parameterName);
		} else
			return defaultValue;
	}

	public static Map<String, String> getTemplateMap(String template, String path)
	{
		Map<String, String> map = new HashMap<String, String>();

		UriTemplate uriTemplate = new UriTemplate(template);
		if (uriTemplate.match(path, map)) {
			return map;
		} else {
			return Collections.<String, String> emptyMap();
		}
	}

	public static boolean matchesTemplate(String template, String path)
	{
		Map<String, String> map = new HashMap<String, String>();

		UriTemplate uriTemplate = new UriTemplate(template);
		return uriTemplate.match(path, map);
	}

	public static int streamGetResponse(String url, ServletOutputStream outputStream, EPADLogger log) throws IOException,
			HttpException
	{
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		int statusCode;

		try {
			statusCode = client.executeMethod(method);
			if (statusCode == HttpServletResponse.SC_OK) {
				InputStream is = method.getResponseBodyAsStream();
				int read = 0;
				byte[] bytes = new byte[4096];
				while ((read = is.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
			} else {
				log.warning("Unexpected response from " + url + ";statusCode=" + statusCode);
			}
		} finally {
			method.releaseConnection();
		}
		return statusCode;
	}

	public static List<File> extractFiles(FileItemIterator fileItemIterator, String prefix, String extension)
			throws FileUploadException, IOException, FileNotFoundException
	{
		List<File> files = new ArrayList<>();
		int sliceCount = 0;
		while (fileItemIterator.hasNext()) {
			FileItemStream fileItemStream = fileItemIterator.next();
			InputStream inputStream = fileItemStream.openStream();
			File temporaryFile = File.createTempFile(prefix + sliceCount + "_", extension);
			FileOutputStream fos = null;
			files.add(temporaryFile);
			try {
				int len;
				byte[] buffer = new byte[32768];
				fos = new FileOutputStream(temporaryFile);
				while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
					fos.write(buffer, 0, len);
				}
			} finally {
				IOUtils.closeQuietly(inputStream);
				IOUtils.closeQuietly(fos);
			}
		}
		return files;
	}

}
