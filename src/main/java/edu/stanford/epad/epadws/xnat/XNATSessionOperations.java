package edu.stanford.epad.epadws.xnat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;

/**
 * XNAT session management methods
 * 
 * 
 * @author martin
 */
public class XNATSessionOperations
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final EPADConfig config = EPADConfig.getInstance();

	private static final String XNAT_SESSION_BASE = "/xnat/data/JSESSION";

	private static final String LOGIN_EXCEPTION_MESSAGE = "Internal login error";
	private static final String XNAT_UNAUTHORIZED_MESSAGE = "XNAT login not successful";
	private static final String XNAT_LOGIN_ERROR_MESSAGE = "Unexpected XNAT login response";

	public static final class XNATSessionResponse
	{
		public final int statusCode;
		public final String response;

		public XNATSessionResponse(int responseCode, String response)
		{
			this.statusCode = responseCode;
			this.response = response;
		}
	}

	/**
	 * @param HttpServlerRequest
	 * @return XNATSessionResponse
	 * @throws IllegalArgumentException
	 */
	public static XNATSessionResponse invokeXNATSessionIDService(HttpServletRequest httpRequest)
	{
		String username = extractUserNameFromAuthorizationHeader(httpRequest);
		String password = extractPasswordFromAuthorizationHeader(httpRequest);

		return getXNATSessionID(username, password);
	}

	public static XNATSessionResponse getXNATSessionID(String username, String password)
	{
		String xnatSessionURL = buildXNATSessionURL();
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(xnatSessionURL);
		String authString = buildAuthorizationString(username, password);
		XNATSessionResponse xnatSessionResponse;
		int xnatStatusCode;

		try {
			log.info("Invoking XNAT session service for user " + username + " at " + xnatSessionURL);
			method.setRequestHeader("Authorization", "Basic " + authString);
			xnatStatusCode = client.executeMethod(method);
		} catch (IOException e) {
			log.warning("Error calling XNAT session service for user " + username, e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}

		try {
			if (xnatStatusCode == HttpServletResponse.SC_OK) {
				log.info("Successfully invoked XNAT session service for user " + username);
				try {
					StringBuilder sb = new StringBuilder();
					InputStreamReader isr = null;
					try {
						isr = new InputStreamReader(method.getResponseBodyAsStream());
						int read = 0;
						char[] chars = new char[128];
						while ((read = isr.read(chars)) > 0) {
							sb.append(chars, 0, read);
						}
					} finally {
						IOUtils.closeQuietly(isr);
					}
					String jsessionID = sb.toString();
					xnatSessionResponse = new XNATSessionResponse(HttpServletResponse.SC_OK, jsessionID);
				} catch (IOException e) {
					log.warning(LOGIN_EXCEPTION_MESSAGE, e);
					xnatSessionResponse = new XNATSessionResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							LOGIN_EXCEPTION_MESSAGE + ": " + e.getMessage());
				}
			} else if (xnatStatusCode == HttpServletResponse.SC_UNAUTHORIZED) {
				log.warning(XNAT_UNAUTHORIZED_MESSAGE);
				xnatSessionResponse = new XNATSessionResponse(xnatStatusCode, XNAT_UNAUTHORIZED_MESSAGE);
			} else {
				log.warning(XNAT_LOGIN_ERROR_MESSAGE + "; XNAT status code = " + xnatStatusCode);
				xnatSessionResponse = new XNATSessionResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						XNAT_LOGIN_ERROR_MESSAGE + "; XNAT status code = " + xnatStatusCode);
			}
		} finally {
			method.releaseConnection();
		}
		return xnatSessionResponse;
	}

	public static int invalidateXNATSessionID(HttpServletRequest httpRequest)
	{
		String xnatSessionURL = buildXNATSessionURL();
		HttpClient client = new HttpClient();
		DeleteMethod method = new DeleteMethod(xnatSessionURL);
		String jsessionID = getJSessionIDFromRequest(httpRequest);
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			xnatStatusCode = client.executeMethod(method);
		} catch (IOException e) {
			log.warning("Error calling XNAT session service to invalidate session ID", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			method.releaseConnection();
		}

		if (xnatStatusCode != HttpServletResponse.SC_OK)
			log.warning("XNAT delete session call returned status code " + xnatStatusCode);

		return xnatStatusCode;
	}

	public static boolean hasValidXNATSessionID(HttpServletRequest httpRequest)
	{
		String jsessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);

		if (jsessionID == null) // The getJSessionIDFromRequest method logs warning in this case.
			return false;
		else
			return hasValidXNATSessionID(jsessionID);
	}

	public static boolean hasValidXNATSessionID(String jsessionID)
	{
		String xnatSessionURL = XNATUtil.buildXNATSessionURL();
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(xnatSessionURL);
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			xnatStatusCode = client.executeMethod(method);
		} catch (IOException e) {
			log.warning("Error calling XNAT", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			method.releaseConnection();
		}
		return (xnatStatusCode == HttpServletResponse.SC_OK);
	}

	public static String getJSessionIDFromRequest(HttpServletRequest servletRequest)
	{
		String jSessionID = null;

		Cookie[] cookies = servletRequest.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("JSESSIONID".equalsIgnoreCase(cookie.getName())) {
					jSessionID = cookie.getValue();
					break;
				}
			}
		}
		if (jSessionID == null)
			log.warning("No JSESESSIONID cookie present in request " + servletRequest.getRequestURL());

		return jSessionID;
	}

	public static String extractUserNameFromAuthorizationHeader(HttpServletRequest httpRequest)
	{
		String credentials = extractCredentialsFromAuthorizationHeader(httpRequest);
		String[] values = credentials.split(":", 2);

		if (values.length != 0 && values[0] != null)
			return values[0];
		else
			return "";
	}

	public static String getXNATAdminSessionID()
	{
		String xnatUploadProjectUser = config.getStringPropertyValue("XNATUploadProjectUser");
		String xnatUploadProjectPassword = config.getStringPropertyValue("XNATUploadProjectPassword");

		XNATSessionResponse xnatSessionResponse = XNATSessionOperations.getXNATSessionID(xnatUploadProjectUser,
				xnatUploadProjectPassword);
		if (xnatSessionResponse.statusCode != HttpServletResponse.SC_OK) {
			log.warning("Error invoking XNAT session service for study upload; statusCode = "
					+ xnatSessionResponse.statusCode);
			return null;
		} else {
			return xnatSessionResponse.response;
		}
	}

	private static String extractPasswordFromAuthorizationHeader(HttpServletRequest request)
	{
		String credentials = extractCredentialsFromAuthorizationHeader(request);
		String[] values = credentials.split(":", 2);
		if (values.length > 1 && values[1] != null)
			return values[1];
		else
			return "";
	}

	private static String extractCredentialsFromAuthorizationHeader(HttpServletRequest request)
	{
		String authorizationHeader = request.getHeader("Authorization");
		String credentials = "";

		if (authorizationHeader != null && authorizationHeader.startsWith("Basic")) {
			String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
			credentials = new String(Base64.decodeBase64(base64Credentials), Charset.forName("UTF-8"));
		}
		return credentials;
	}

	private static String buildXNATSessionURL()
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");

		return buildXNATBaseURL(xnatHost, xnatPort, XNAT_SESSION_BASE);
	}

	public static String buildXNATBaseURL(String host, int port, String base)
	{
		return buildXNATBaseURL(host, port, base, "");
	}

	private static String buildXNATBaseURL(String host, int port, String base, String ext)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("http://").append(host);
		sb.append(":").append(port);
		sb.append(base);
		sb.append(ext);

		return sb.toString();
	}

	private static String buildAuthorizationString(String username, String password)
	{
		String authString = username + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);

		return authStringEnc;
	}
}
