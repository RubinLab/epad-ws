package edu.stanford.isis.epadws.handlers;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.JsonHelper;

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
		responseStream.append(message);
		return responseCode;
	}

	public static int warningJSONResponse(int responseCode, String message, Throwable t, PrintWriter responseStream,
			EPADLogger log)
	{
		log.warning(message, t);
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

	public static int invalidTokenResponse(String message, Throwable t, PrintWriter responseStream, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_UNAUTHORIZED, message, t, responseStream, log);
	}

	public static int invalidTokenJSONResponse(String message, Throwable t, PrintWriter responseStream, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_UNAUTHORIZED, message, t, responseStream, log);
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

	public static Map<String, String> extractParameters(String queryString)
	{
		Map<String, String> result = new HashMap<String, String>();
		String[] avPairs = queryString.trim().split("&");

		for (int i = 0; i < avPairs.length; i++) {
			String[] avPair = avPairs[i].trim().split("=");
			String attribute = avPair[0];
			String value = avPair[1];
			result.put(attribute, value);
		}
		return result;
	}

}
