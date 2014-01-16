package edu.stanford.isis.epadws.xnat;

import java.nio.charset.Charset;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;

/**
 * @author martin
 */
public class XNATUtil
{
	public static final String XNAT_SESSION_BASE = "/xnat/data/JSESSION";
	public static final String XNAT_PROJECT_BASE = "/xnat/data/projects/";
	public static final String XNAT_SUBJECT_BASE = "/xnat/data/subjects/";

	private static final EPADLogger log = EPADLogger.getInstance();
	private static final EPADConfig config = EPADConfig.getInstance();

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

	public static String extractUserNameFromAuthorizationHeader(HttpServletRequest httpRequest)
	{
		String credentials = extractCredentialsFromAuthorizationHeader(httpRequest);
		String[] values = credentials.split(":", 2);

		if (values.length != 0 && values[0] != null)
			return values[0];
		else
			return "";
	}

	public static String projectName2XNATProjectID(String xnatProjectName)
	{ // Alphanumeric and dot and dash only
		String result = xnatProjectName.replaceAll("[^a-zA-Z0-9\\\\-_]", "_");

		// log.info("projectName2XNATProjectID: in=" + projectName + ", out=" + result);

		return result;
	}

	// Only a-zA-Z0-9, dash, underscore, and space allowed. Replace with underscore. Here we also replace spaces with
	// underscores though XNAT would allow spaces in labels.
	public static String dicomPatientID2XNATSubjectLabel(String dicomPatientID)
	{
		String result = dicomPatientID.replaceAll("[^a-zA-Z0-9\\\\-_]", "_").replaceAll("\\\\^", "_");

		// log.info("dicomPatientID2XNATSubjectLabel: in=" + dicomPatientID + ", out=" + result);

		return result;
	}

	public static String buildProjectURLString(String base)
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");

		if (base.startsWith("/"))
			base = base.substring(1, base.length());

		return buildURLString(xnatHost, xnatPort, XNAT_PROJECT_BASE, base);
	}

	public static String buildSubjectURLString(String base)
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");

		if (base.startsWith("/"))
			base = base.substring(1, base.length());

		return buildURLString(xnatHost, xnatPort, XNAT_SUBJECT_BASE, base);
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

	public static String extractPasswordFromAuthorizationHeader(HttpServletRequest request)
	{
		String credentials = extractCredentialsFromAuthorizationHeader(request);
		String[] values = credentials.split(":", 2);
		if (values.length > 1 && values[1] != null)
			return values[1];
		else
			return "";
	}

	public static String buildURLString(String host, int port, String base)
	{
		return buildURLString(host, port, base, "");
	}

	private static String buildURLString(String host, int port, String base, String ext)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("http://").append(host);
		sb.append(":").append(port);
		sb.append(base);
		sb.append(ext);

		return sb.toString();
	}

	public static String dicomStudyUID2XNATExperimentID(String dicomStudyUID)
	{
		return dicomStudyUID.replace('.', '_');
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
