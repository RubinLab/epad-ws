package edu.stanford.isis.epadws.xnat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

import edu.stanford.isis.epad.common.dicom.DicomReader;
import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.processing.model.DicomTagFileUtils;
import edu.stanford.isis.epadws.processing.update.UpdateTracker;

/**
 * @author martin
 */
public class XNATUtil
{
	private static final String XNAT_PROJECT_BASE = "/xnat/data/projects/";
	private static final String XNAT_SUBJECT_BASE = "/xnat/data/subjects/";

	private static final String XNAT_UPLOAD_PROPERTIES_FILE_NAME = "xnat_upload.properties";

	private static final EPADLogger log = EPADLogger.getInstance();
	private static final EPADConfig config = EPADConfig.getInstance();

	private static final String XNAT_SESSION_BASE = "/xnat/data/JSESSION";
	private static final String LOGIN_EXCEPTION_MESSAGE = "Internal login error";
	private static final String XNAT_UNAUTHORIZED_MESSAGE = "XNAT login not successful";
	private static final String XNAT_LOGIN_ERROR_MESSAGE = "Unexpected XNAT login response";

	private static final UpdateTracker updateTracker = UpdateTracker.getInstance();

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

	/**
	 * Take a directory containing a list of DICOM files and create XNAT representations of the each DICOM image. This
	 * method expects a properties file called xnat_upload.properties in the directory. This file should contain an XNAT
	 * project name and an XNAT session ID.
	 * 
	 * @param uploadDirectory
	 */
	public static void createXNATEntitiesFromDICOMFilesInDirectory(File uploadDirectory)
	{
		String propertiesFilePath = uploadDirectory.getAbsolutePath() + File.separator + XNAT_UPLOAD_PROPERTIES_FILE_NAME;
		File xnatUploadPropertiesFile = new File(propertiesFilePath);

		if (!xnatUploadPropertiesFile.exists())
			log.warning("Could not find XNAT upload properties file " + propertiesFilePath);
		else {
			Properties xnatUploadProperties = new Properties();
			FileInputStream propertiesFileStream = null;
			try {
				log.info("Found XNAT upload properties file " + propertiesFilePath);
				propertiesFileStream = new FileInputStream(xnatUploadPropertiesFile);
				xnatUploadProperties.load(propertiesFileStream);
				String xnatProjectID = xnatUploadProperties.getProperty("XNATProjectName");
				String xnatSessionID = xnatUploadProperties.getProperty("XNATSessionID");
				log.info("xnatProjectID " + xnatProjectID);

				int numberOfDICOMFiles = 0;
				if (xnatProjectID != null && xnatSessionID != null) {
					for (File dicomFile : DicomTagFileUtils.listDICOMFiles(uploadDirectory)) {
						DicomReader dicomReader = new DicomReader(dicomFile);
						// DCM4CHEE stores the patient name as upper case so we match. TODO get original from database?
						String dicomPatientName = dicomReader.getPatientName().toUpperCase();
						String dicomPatientID = dicomReader.getPatientID();
						String dicomStudyUID = dicomReader.getStudyIUID();

						if (dicomPatientName != null) {
							if (dicomPatientID == null) // TODO Check that this is a valid thing to do.
								dicomPatientID = dicomPatientName;
							String xnatSubjectLabel = dicomPatientID2XNATSubjectLabel(dicomPatientID);
							createXNATSubjectFromDICOMPatient(xnatProjectID, xnatSubjectLabel, dicomPatientName, xnatSessionID);
							if (dicomStudyUID != null)
								createXNATExperimentFromDICOMStudy(xnatProjectID, xnatSubjectLabel, dicomStudyUID, xnatSessionID);
							else
								log.warning("Missing study UID in DICOM file " + dicomFile.getAbsolutePath());
						} else
							log.warning("Missing patient name in DICOM file " + dicomFile.getAbsolutePath());
						numberOfDICOMFiles++;
					}
					if (numberOfDICOMFiles != 0)
						log.info("Found " + numberOfDICOMFiles + " DICOM files in upload directory");
					else
						log.warning("No DICOM files found in upload directory!");
				} else {
					log.warning("Missing XNAT project name and/or session ID in properties file" + propertiesFilePath);
				}
			} catch (IOException e) {
				log.warning("Error processing upload in directory " + propertiesFilePath, e);
			} finally {
				if (propertiesFileStream != null) {
					try {
						propertiesFileStream.close();
					} catch (IOException e) {
						log.warning("Error closing XNAT upload properties file " + propertiesFilePath, e);
					}
				}
			}
		}
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

	public static boolean createXNATProject(String xnatProjectID, String xnatProjectName, String jsessionID)
	{
		String xnatHost = config.getStringConfigurationParameter("XNATServer");
		int xnatPort = config.getIntegerConfigurationParameter("XNATPort");
		String xnatProjectURL = buildXNATProjectCreationURL(xnatHost, xnatPort, XNAT_PROJECT_BASE, xnatProjectID,
				xnatProjectName);
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(xnatProjectURL);
		int xnatStatusCode;

		postMethod.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			log.info("Invoking XNAT with URL " + xnatProjectURL);
			xnatStatusCode = client.executeMethod(postMethod);
			if (unexpectedCreationStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT; status code = " + xnatStatusCode);
			else
				updateTracker.recordUpdate(jsessionID, xnatProjectID);
		} catch (IOException e) {
			log.warning("Error calling XNAT", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return (!unexpectedCreationStatusCode(xnatStatusCode));
	}

	private static String buildXNATProjectCreationURL(String host, int port, String base, String xnatProjectID,
			String xnatProjectName)
	{
		String queryString = "?ID=" + projectName2XNATProjectID(xnatProjectName) + "&name=" + encode(xnatProjectName);
		String urlString = buildURLString(host, port, base) + queryString;

		return urlString;
	}

	public static void createXNATSubjectFromDICOMPatient(String xnatProjectID, String xnatSubjectLabel,
			String dicomPatientName, String jsessionID)
	{
		String xnatHost = config.getStringConfigurationParameter("XNATServer");
		int xnatPort = config.getIntegerConfigurationParameter("XNATPort");
		String xnatSubjectURL = buildXNATSubjectCreationURL(xnatHost, xnatPort, XNAT_PROJECT_BASE, xnatProjectID,
				xnatSubjectLabel, dicomPatientName);
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(xnatSubjectURL);
		int xnatStatusCode;

		postMethod.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			log.info("Invoking XNAT with URL " + xnatSubjectURL);
			xnatStatusCode = client.executeMethod(postMethod);
			if (unexpectedCreationStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT; status code = " + xnatStatusCode);
			else
				updateTracker.recordUpdate(jsessionID, xnatProjectID, xnatSubjectLabel);
		} catch (IOException e) {
			log.warning("Error calling XNAT", e);
		}
	}

	private static String buildXNATSubjectCreationURL(String host, int port, String base, String xnatProjectID,
			String xnatSubjectLabel, String dicomPatientName)
	{
		String queryPart = "?label=" + xnatSubjectLabel + "&src=" + encode(dicomPatientName);
		String urlString = buildURLString(host, port, base) + xnatProjectID + "/subjects" + queryPart;

		return urlString;
	}

	private static boolean unexpectedCreationStatusCode(int statusCode)
	{
		return !(statusCode == HttpServletResponse.SC_OK || statusCode == HttpServletResponse.SC_CREATED || statusCode == HttpServletResponse.SC_CONFLICT);
	}

	public static boolean createXNATExperimentFromDICOMStudy(String xnatProjectID, String xnatSubjectLabel,
			String dicomStudyUID, String jsessionID)
	{
		String xnatHost = config.getStringConfigurationParameter("XNATServer");
		int xnatPort = config.getIntegerConfigurationParameter("XNATPort");
		String xnatStudyURL = buildXNATExperimentCreationURL(xnatHost, xnatPort, XNAT_PROJECT_BASE, xnatProjectID,
				xnatSubjectLabel, dicomStudyUID);

		HttpClient client = new HttpClient();
		PutMethod putMethod = new PutMethod(xnatStudyURL);
		int xnatStatusCode;

		putMethod.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			log.info("Invoking XNAT with URL " + xnatStudyURL);
			xnatStatusCode = client.executeMethod(putMethod);
			if (unexpectedCreationStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT; status code = " + xnatStatusCode);
			else
				updateTracker.recordUpdate(jsessionID, xnatProjectID, xnatSubjectLabel, dicomStudyUID);
		} catch (IOException e) {
			log.warning("Error calling XNAT", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return (!unexpectedCreationStatusCode(xnatStatusCode));
	}

	// Setting the label field explicitly in this URL causes duplicate experiments to be created for
	// the same study.
	private static String buildXNATExperimentCreationURL(String host, int port, String base, String xnatProjectID,
			String xnatSubjectLabel, String dicomStudyUID)
	{
		String experimentID = dicomStudyUID2XNATExperimentID(dicomStudyUID);
		String urlString = buildURLString(host, port, base) + xnatProjectID + "/subjects/" + xnatSubjectLabel
				+ "/experiments/" + experimentID + "?name=" + dicomStudyUID + "&xsiType=xnat:otherDicomSessionData";

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

	private static String projectName2XNATProjectID(String xnatProjectName)
	{
		String result = xnatProjectName.replaceAll("[^a-zA-Z0-9\\\\.\\\\-_]", "_");

		// log.info("projectName2XNATProjectID: in=" + projectName + ", out=" + result);

		return result;
	}

	public static String dicomPatientID2XNATSubjectLabel(String dicomPatientID)
	{
		String result = dicomPatientID.replaceAll("[^a-zA-Z0-9\\\\.\\\\-_]", "_").replaceAll("\\\\^", "_");

		// log.info("dicomPatientID2XNATSubjectLabel: in=" + dicomPatientID + ", out=" + result);

		return result;
	}

	private static String dicomStudyUID2XNATExperimentID(String dicomStudyUID)
	{
		return dicomStudyUID.replace('.', '_');
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

	public static String buildProjectURLString(String base)
	{
		String xnatHost = config.getStringConfigurationParameter("XNATServer");
		int xnatPort = config.getIntegerConfigurationParameter("XNATPort");

		if (base.startsWith("/"))
			base = base.substring(1, base.length());

		return buildURLString(xnatHost, xnatPort, XNAT_PROJECT_BASE, base);
	}

	public static String buildSubjectURLString(String base)
	{
		String xnatHost = config.getStringConfigurationParameter("XNATServer");
		int xnatPort = config.getIntegerConfigurationParameter("XNATPort");

		if (base.startsWith("/"))
			base = base.substring(1, base.length());

		return buildURLString(xnatHost, xnatPort, XNAT_SUBJECT_BASE, base);
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
