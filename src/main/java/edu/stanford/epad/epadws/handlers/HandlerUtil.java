package edu.stanford.epad.epadws.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import com.sun.jersey.api.uri.UriTemplate;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.JsonHelper;

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
		log.warning(message, t);
		if (responseStream != null)
			responseStream.append(message);
		return responseCode;
	}

	public static int warningJSONResponse(int responseCode, String message, Throwable t, PrintWriter responseStream,
			EPADLogger log)
	{
		log.warning(message, t);
		if (responseStream != null)
			responseStream.append(JsonHelper.createJSONErrorResponse(message));
		return responseCode;
	}

	public static int warningJSONResponse(int responseCode, String message, Throwable t, EPADLogger log)
	{
		log.warning(message, t);
		return responseCode;
	}

	public static int warningResponse(int responseCode, String message, Throwable t, EPADLogger log)
	{
		log.warning(message, t);
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

	public static int invalidTokenResponse(String message, Throwable t, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_UNAUTHORIZED, message, t, log);
	}

	public static int invalidTokenResponse(String message, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_UNAUTHORIZED, message, log);
	}

	public static String getParameter(Map<String, String> templateMap, String parameterName)
	{
		if (templateMap.containsKey(parameterName)) {
			return templateMap.get(parameterName);
		} else
			throw new IllegalArgumentException("no " + parameterName + " parameter in request");
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

	public static void streamGetResponse(String url, ServletOutputStream outputStream, EPADLogger log)
			throws IOException, HttpException
	{
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);

		try {
			int wadoStatusCode = client.executeMethod(method);

			if (wadoStatusCode == HttpServletResponse.SC_OK) {
				InputStream is = method.getResponseBodyAsStream();
				int read = 0;
				byte[] bytes = new byte[4096];
				while ((read = is.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
			} else {
				log.warning("Unexpected response from " + url + ";statusCode=" + wadoStatusCode);
			}
		} finally {
			method.releaseConnection();
		}
	}
}
