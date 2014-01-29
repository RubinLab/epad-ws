package edu.stanford.isis.epadws.xnat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.google.gson.Gson;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.xnat.XNATExperimentsResult;

/**
 * Methods for querying XNAT
 * 
 * 
 * @author martin
 */
public class XNATQueries
{
	private static final EPADLogger log = EPADLogger.getInstance();

	public static XNATExperimentsResult dicomExperiments(String sessionID)
	{
		String xnatExperimentsQueryURL = XNATUtil.buildDICOMExperimentsQueryURL();

		return invokeXNATDICOMExperimentsQuery(sessionID, xnatExperimentsQueryURL);
	}

	public static XNATExperimentsResult dicomExperimentsForProject(String sessionID, String projectID)
	{
		String xnatExperimentsQueryURL = XNATUtil.buildDICOMExperimentsForProjectQueryURL(projectID);

		return invokeXNATDICOMExperimentsQuery(sessionID, xnatExperimentsQueryURL);
	}

	public static XNATExperimentsResult dicomExperimentsForProjectAndPatient(String sessionID, String projectID,
			String patientID)
	{
		String xnatExperimentsQueryURL = XNATUtil.buildDICOMExperimentsForProjectAndPatientQueryURL(projectID, patientID);

		return invokeXNATDICOMExperimentsQuery(sessionID, xnatExperimentsQueryURL);
	}

	private static XNATExperimentsResult invokeXNATDICOMExperimentsQuery(String sessionID, String xnatDICOMExperimentsQueryURL)
	{
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(xnatDICOMExperimentsQueryURL);
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + sessionID);

		try {
			log.info("Invoking XNAT query at " + xnatDICOMExperimentsQueryURL);
			xnatStatusCode = client.executeMethod(method);
		} catch (IOException e) {
			log.warning("Warning: error performing XNAT query", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return processXNATExperimentsQueryResponse(method, xnatStatusCode);
	}

	private static XNATExperimentsResult processXNATExperimentsQueryResponse(GetMethod method, int xnatStatusCode)
	{
		if (xnatStatusCode == HttpServletResponse.SC_OK) {
			return extractXNATExperimentsFromResponse(method);
		} else if (xnatStatusCode == HttpServletResponse.SC_UNAUTHORIZED) {
			log.warning("Warning: invalid session token for XNAT experiments query");
			return XNATExperimentsResult.emptyExperiments();
		} else {
			log.warning("Warning: error performing XNAT experiments query; XNAT status code = " + xnatStatusCode);
			return XNATExperimentsResult.emptyExperiments();
		}
	}

	private static XNATExperimentsResult extractXNATExperimentsFromResponse(GetMethod method)
	{
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
			Gson gson = new Gson();
			XNATExperimentsResult xnatExperiments = gson.fromJson(reader, XNATExperimentsResult.class);
			return xnatExperiments;
		} catch (IOException e) {
			log.warning("Warning: error processing XNAT experiments query result", e);
			return XNATExperimentsResult.emptyExperiments();
		}
	}
}
