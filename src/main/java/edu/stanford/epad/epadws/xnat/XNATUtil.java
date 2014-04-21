package edu.stanford.epad.epadws.xnat;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;

/**
 * @author martin
 */
public class XNATUtil
{
	public static final String XNAT_PROJECTS_BASE = "/xnat/data/projects/";
	private static final String XNAT_SESSION_BASE = "/xnat/data/JSESSION";

	private static final EPADLogger log = EPADLogger.getInstance();
	private static final EPADConfig config = EPADConfig.getInstance();

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

	// Setting the label field explicitly in this URL causes a new experiment to be created for
	// the same study in different projects. Otherwise we have a shared experiment, which is not what we want.
	public static String buildXNATDICOMExperimentCreationURL(String xnatProjectLabelOrID, String xnatSubjectLabelOrID,
			String dicomStudyUID)
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");
		String experimentID = XNATUtil.dicomStudyUID2XNATExperimentID(dicomStudyUID);
		String urlString = XNATUtil.buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE) + xnatProjectLabelOrID
				+ "/subjects/" + xnatSubjectLabelOrID + "/experiments/" + experimentID + "?name=" + dicomStudyUID
				+ "&xsiType=xnat:otherDicomSessionData";

		return urlString;
	}

	public static String buildXNATSubjectCreationURL(String xnatProjectLabelOrID, String xnatSubjectLabel,
			String dicomPatientName)
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");
		String queryPart = "?label=" + xnatSubjectLabel + "&src=" + encode(dicomPatientName);
		String urlString = XNATUtil.buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE) + xnatProjectLabelOrID
				+ "/subjects" + queryPart;

		return urlString;
	}

	public static String buildXNATProjectDeletionURL(String xnatProjectLabelOrID)
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");
		String urlString = XNATUtil.buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE) + xnatProjectLabelOrID;

		return urlString;
	}

	public static String buildXNATSubjectDeletionURL(String xnatProjectLabelOrID, String xnatSubjectLabelOrID)
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");
		String urlString = XNATUtil.buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE) + xnatProjectLabelOrID
				+ "/subjects/" + xnatSubjectLabelOrID;

		return urlString;
	}

	public static String buildXNATDICOMStudyDeletionURL(String xnatProjectLabelOrID, String xnatSubjectLabelOrID,
			String studyUID)
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");
		String urlString = XNATUtil.buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE) + xnatProjectLabelOrID
				+ "/subjects/" + xnatSubjectLabelOrID + "/experiments/" + dicomStudyUID2XNATExperimentID(studyUID);

		return urlString;
	}

	public static String buildXNATProjectCreationURL(String xnatProjectName)
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");
		String queryString = "?ID=" + XNATUtil.projectName2XNATProjectID(xnatProjectName) + "&name="
				+ encode(xnatProjectName);
		String urlString = XNATUtil.buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE) + queryString;

		return urlString;
	}

	public static String buildXNATBaseURL(String host, int port, String base)
	{
		return buildXNATBaseURL(host, port, base, "");
	}

	public static String buildXNATSessionURL()
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");

		return buildXNATBaseURL(xnatHost, xnatPort, XNATUtil.XNAT_SESSION_BASE);
	}

	public static String dicomStudyUID2XNATExperimentID(String dicomStudyUID)
	{
		return dicomStudyUID.replace('.', '_');
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

	private static String encode(String urlString)
	{
		try {
			return URLEncoder.encode(urlString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warning("Warning: error encoding URL " + urlString, e);
			return null;
		}
	}
}
