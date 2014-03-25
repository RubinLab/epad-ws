package edu.stanford.isis.epadws.xnat;

import edu.stanford.isis.epad.common.util.EPADConfig;

/**
 * @author martin
 */
public class XNATQueryUtil
{
	private static final String XNAT_PROJECTS_BASE = "/xnat/data/projects/";
	private static final String XNAT_SUBJECTS_BASE = "/xnat/data/subjects/";
	private static final String XNAT_EXPERIMENTS_BASE = "/xnat/data/experiments/";

	private static final EPADConfig config = EPADConfig.getInstance();

	public static String buildProjectsURL(String base)
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");

		if (base.startsWith("/"))
			base = base.substring(1, base.length());

		return buildURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE, base);
	}

	public static String buildSubjectsURL(String base)
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");

		if (base.startsWith("/"))
			base = base.substring(1, base.length());

		return buildURL(xnatHost, xnatPort, XNAT_SUBJECTS_BASE, base);
	}

	public static String buildAllProjectsQueryURL()
	{
		return buildProjectBaseURL();
	}

	public static String buildAllUsersForProjectQueryURL(String projectID)
	{
		return buildProjectBaseURL() + projectID + "/users" + "?format=json";
	}

	public static String buildProjectQueryURL(String projectID)
	{
		return buildProjectBaseURL() + projectID + "?format=json";
	}

	public static String buildSubjectsQueryURL(String subjectID)
	{
		return buildSubjectsBaseURL() + "/" + subjectID + "?format=json";
	}

	/**
	 * Construct XNAT query to get all subjects for a project.
	 * 
	 * @param projectID
	 * @return
	 */
	public static String buildAllSubjectsForProjectQueryURL(String projectID)
	{
		return buildProjectBaseURL() + projectID + "/subjects/?columns=label,src,insert_user,insert_date,project";
	}

	/**
	 * Construct XNAT query to get subject for a project.
	 * 
	 * @param projectID
	 * @param subjectName
	 * @return
	 */
	public static String buildProjectsSubjectQueryURL(String projectID, String subjectName)
	{
		return buildSubjectsBaseURL() + "?project=" + projectID + "&src=" + subjectName + "&format=json";
	}

	// Query to find all DICOM studies
	public static String buildDICOMExperimentsQueryURL()
	{ // XNAT appears to require that the format=json parameter is at the end.
		return buildExperimentsBaseURL() + "?xsiType=xnat:otherDicomSessionData&format=json";
	}

	// Query to find a DICOM study with the specified studyUID for a particular project
	public static String buildDICOMExperimentsForProjectAndStudyQueryURL(String projectID, String studyUID)
	{
		return buildExperimentsBaseURL() + "?project=" + projectID + "&name=" + studyUID
				+ "&xsiType=xnat:otherDicomSessionData&format=json";
	}

	// Query to find all DICOM studies for a particular project
	public static String buildDICOMExperimentsForProjectQueryURL(String projectID)
	{
		return buildExperimentsBaseURL() + "?project=" + projectID + "&xsiType=xnat:otherDicomSessionData&format=json";
	}

	// Query to find all DICOM studies for a particular project and patient
	public static String buildDICOMExperimentsForProjectAndSubjectQueryURL(String projectID, String patientID)
	{
		return buildSubjectsBaseURL() + "?project=" + projectID + "&src=" + patientID
				+ "&xsiType=xnat:otherDicomSessionData&format=json";
	}

	// Setting the label field explicitly in this URL causes a new experiment to be created for
	// the same study in different projects. Otherwise we have a shared experiment, which is not what we want.
	public static String buildXNATExperimentCreationURL(String xnatProjectID, String xnatSubjectLabel,
			String dicomStudyUID)
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");
		String experimentID = XNATUtil.dicomStudyUID2XNATExperimentID(dicomStudyUID);
		String urlString = XNATUtil.buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE) + xnatProjectID + "/subjects/"
				+ xnatSubjectLabel + "/experiments/" + experimentID + "?name=" + dicomStudyUID
				+ "&xsiType=xnat:otherDicomSessionData";

		return urlString;
	}

	public static String buildXNATBaseURL(String host, int port, String base)
	{
		return buildXNATBaseURL(host, port, base, "");
	}

	public static String buildXNATProjectsURL()
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");

		return buildXNATBaseURL(xnatHost, xnatPort, XNATUtil.XNAT_PROJECTS_BASE);
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

	private static String buildProjectBaseURL()
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");

		return buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE);
	}

	private static String buildURL(String host, int port, String base, String ext)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("http://").append(host);
		sb.append(":").append(port);
		sb.append(base);
		sb.append(ext);

		return sb.toString();
	}

	private static String buildSubjectsBaseURL()
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");

		return buildXNATBaseURL(xnatHost, xnatPort, XNAT_SUBJECTS_BASE);
	}

	private static String buildExperimentsBaseURL()
	{
		String xnatHost = config.getStringPropertyValue("XNATServer");
		int xnatPort = config.getIntegerPropertyValue("XNATPort");

		return buildXNATBaseURL(xnatHost, xnatPort, XNAT_EXPERIMENTS_BASE);
	}
}
