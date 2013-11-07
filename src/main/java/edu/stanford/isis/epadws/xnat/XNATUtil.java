package edu.stanford.isis.epadws.xnat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;

/**
 * @author martin
 */
public class XNATUtil
{
	public static final String XNAT_PROJECT_BASE = "/xnat/data/projects/";
	public static final String XNAT_SUBJECT_BASE = "/xnat/data/subjects/";

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

		return invokeXNATSessionIDService(username, password);
	}

	public static XNATSessionResponse invokeXNATSessionIDService(String username, String password)
	{
		String xnatHost = config.getStringConfigurationParameter("XNATServer");
		int xnatPort = config.getIntegerConfigurationParameter("XNATPort");
		String xnatSessionURL = buildURLString(xnatHost, xnatPort, XNAT_SESSION_BASE);
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(xnatSessionURL);
		String authString = buildAuthorizatonString(username, password);
		XNATSessionResponse xnatSessionResponse;
		int xnatStatusCode;

		try {
			log.info("Invoking XNAT session service at " + xnatSessionURL);
			postMethod.setRequestHeader("Authorization", "Basic " + authString);
			xnatStatusCode = client.executeMethod(postMethod);
		} catch (IOException e) {
			log.warning("Error calling XNAT session service", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}

		if (xnatStatusCode == HttpServletResponse.SC_OK) {
			log.info("Successfully invoked XNAT session service");
			InputStreamReader isr = null;
			try {
				StringBuilder sb = new StringBuilder();
				isr = null;
				try {
					isr = new InputStreamReader(postMethod.getResponseBodyAsStream());
					int read = 0;
					char[] chars = new char[128];
					while ((read = isr.read(chars)) > 0) {
						sb.append(chars, 0, read);
					}
				} finally {
					if (isr != null) {
						try {
							isr.close();
						} catch (IOException e) {
							log.warning("Error closing XNAT session response stream", e);
						}
					}
				}
				String jsessionID = sb.toString();
				log.info("JSESSIONID returned from XNAT");
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
		return xnatSessionResponse;
	}

	public static int invalidateXNATSessionID(HttpServletRequest httpRequest)
	{
		String xnatHost = config.getStringConfigurationParameter("XNATServer");
		int xnatPort = config.getIntegerConfigurationParameter("XNATPort");
		String xnatSessionURL = buildURLString(xnatHost, xnatPort, XNAT_SESSION_BASE);
		HttpClient client = new HttpClient();
		DeleteMethod deleteMethod = new DeleteMethod(xnatSessionURL);
		String jsessionID = getJSessionIDFromRequest(httpRequest);
		int xnatStatusCode;

		deleteMethod.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			xnatStatusCode = client.executeMethod(deleteMethod);
		} catch (IOException e) {
			log.warning("Error calling XNAT session service to invalidate session ID", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}

		if (xnatStatusCode != HttpServletResponse.SC_OK)
			log.warning("XNAT delete session call returned status code " + xnatStatusCode);

		return xnatStatusCode;
	}

	public static boolean hasValidXNATSessionID(HttpServletRequest httpRequest)
	{
		String jsessionID = getJSessionIDFromRequest(httpRequest);

		return hasValidXNATSessionID(jsessionID);
	}

	public static boolean createXNATProject(String projectID, String projectName, String jsessionID)
	{
		String xnatHost = config.getStringConfigurationParameter("XNATServer");
		int xnatPort = config.getIntegerConfigurationParameter("XNATPort");
		String xnatProjectURL = buildXNATProjectCreationURL(xnatHost, xnatPort, XNAT_PROJECT_BASE, projectID, projectName);
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(xnatProjectURL);
		int xnatStatusCode;

		postMethod.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			log.info("Invoking XNAT with URL " + xnatProjectURL);
			xnatStatusCode = client.executeMethod(postMethod);
			if (unexpectedCreationStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT; status code = " + xnatStatusCode);
		} catch (IOException e) {
			log.warning("Error calling XNAT", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return (!unexpectedCreationStatusCode(xnatStatusCode));
	}

	private static String buildXNATProjectCreationURL(String host, int port, String base, String projectID,
			String projectName)
	{
		String queryString = "?ID=" + projectName2XNATProjectID(projectName) + "&name=" + encode(projectName);
		String urlString = buildURLString(host, port, base) + queryString;

		return urlString;
	}

	public static boolean createXNATSubject(String projectID, String subjectName, String subjectID, String jsessionID)
	{
		String xnatHost = config.getStringConfigurationParameter("XNATServer");
		int xnatPort = config.getIntegerConfigurationParameter("XNATPort");
		String xnatSubjectURL = buildXNATSubjectCreationURL(xnatHost, xnatPort, XNAT_PROJECT_BASE, projectID, subjectName,
				subjectID);
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(xnatSubjectURL);
		int xnatStatusCode;

		postMethod.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			log.info("Invoking XNAT with URL " + xnatSubjectURL);
			xnatStatusCode = client.executeMethod(postMethod);
			if (unexpectedCreationStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT; status code = " + xnatStatusCode);
		} catch (IOException e) {
			log.warning("Error calling XNAT", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return (!unexpectedCreationStatusCode(xnatStatusCode));
	}

	private static String buildXNATSubjectCreationURL(String host, int port, String base, String projectID,
			String subjectID, String subjectName)
	{
		String queryPart = "?label=" + patientName2XNATSubjectLabel(subjectName) + "&src=" + encode(subjectName);
		String urlString = buildURLString(host, port, base) + projectID + "/subjects" + queryPart;

		return urlString;
	}

	private static boolean unexpectedCreationStatusCode(int statusCode)
	{
		return !(statusCode == HttpServletResponse.SC_OK || statusCode == HttpServletResponse.SC_CREATED || statusCode == HttpServletResponse.SC_CONFLICT);
	}

	public static boolean createXNATStudy(String projectID, String subjectID, String studyID, String jsessionID)
	{
		String xnatHost = config.getStringConfigurationParameter("XNATServer");
		int xnatPort = config.getIntegerConfigurationParameter("XNATPort");
		String xnatStudyURL = buildXNATExperimentCreationURL(xnatHost, xnatPort, XNAT_PROJECT_BASE, projectID, subjectID,
				studyID);

		HttpClient client = new HttpClient();
		PutMethod putMethod = new PutMethod(xnatStudyURL);
		int xnatStatusCode;

		putMethod.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			log.info("Invoking XNAT with URL " + xnatStudyURL);
			xnatStatusCode = client.executeMethod(putMethod);
			if (unexpectedCreationStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT; status code = " + xnatStatusCode);
		} catch (IOException e) {
			log.warning("Error calling XNAT", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return (!unexpectedCreationStatusCode(xnatStatusCode));
	}

	private static String buildXNATExperimentCreationURL(String host, int port, String base, String projectID,
			String subjectID, String studyUID)
	{
		String experimentID = studyUID2XNATExperimentID(studyUID);
		String urlString = buildURLString(host, port, base) + projectID + "/subjects/" + subjectID + "/experiments/"
				+ experimentID + "?name=" + studyUID + "&xsiType=xnat:otherDicomSessionData";

		return urlString;
	}

	public static boolean hasValidXNATSessionID(String jsessionID)
	{
		String xnatHost = config.getStringConfigurationParameter("XNATServer");
		int xnatPort = config.getIntegerConfigurationParameter("XNATPort");
		String xnatSessionURL = buildURLString(xnatHost, xnatPort, XNAT_SESSION_BASE);
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(xnatSessionURL);
		int xnatStatusCode;

		getMethod.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			xnatStatusCode = client.executeMethod(getMethod);
		} catch (IOException e) {
			log.warning("Error calling XNAT", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return (xnatStatusCode == HttpServletResponse.SC_OK);
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

	private static String projectName2XNATProjectID(String projectName)
	{
		String result = projectName.replaceAll("[^a-zA-Z0-9\\\\.\\\\-_]", "_");

		// log.info("projectName2XNATProjectID: in=" + projectName + ", out=" + result);

		return result;
	}

	private static String patientName2XNATSubjectLabel(String patientName)
	{
		String result = patientName.replaceAll("[^a-zA-Z0-9\\\\.\\\\-_]", "_").replaceAll("\\\\^", "_");

		// log.info("patientName2XNATSubjectLabel: in=" + patientName + ", out=" + result);

		return result;
	}

	private static String studyUID2XNATExperimentID(String studyUID)
	{
		return studyUID.replace('.', '_');
	}

	private static String encode(String urlString)
	{
		try {
			return URLEncoder.encode(urlString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warning("Error encoding URL " + urlString, e);
			return null;
		}
	}

	public static String buildURLString(String host, int port, String base)
	{
		return buildURLString(host, port, base, "");
	}

	public static String buildURLString(String host, int port, String base, String ext)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("http://").append(host);
		sb.append(":").append(port);
		sb.append(base);
		sb.append(ext);

		return sb.toString();
	}

	public static String getJSessionIDFromRequest(HttpServletRequest servletRequest)
	{
		String jSessionID = "";

		Cookie[] cookies = servletRequest.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("JSESSIONID".equalsIgnoreCase(cookie.getName())) {
					jSessionID = cookie.getValue();
					break;
				}
			}
		}
		if (jSessionID.length() == 0)
			log.warning("No JSESESSIONID cookie present in request");
		return jSessionID;
	}

	private static String buildAuthorizatonString(String username, String password)
	{
		String authString = username + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);

		return authStringEnc;
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
}
