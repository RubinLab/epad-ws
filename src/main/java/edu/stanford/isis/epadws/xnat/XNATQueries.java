package edu.stanford.isis.epadws.xnat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.google.gson.Gson;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.xnat.XNATExperiments;

public class XNATQueries
{
	private static final EPADLogger log = EPADLogger.getInstance();

	public static XNATExperiments experiments(String sessionID)
	{
		String xnatExperimentsQueryURL = XNATUtil.buildXNATExperimentsURL();

		return invokeXNATExperimentsQuery(xnatExperimentsQueryURL);
	}

	public static XNATExperiments experimentsForProject(String projectID)
	{
		// TODO
		return new XNATExperiments();
	}

	public static XNATExperiments experimentsForProjectAndSubject(String projectID, String subjectID)
	{
		// TODO
		return new XNATExperiments();
	}

	private static XNATExperiments invokeXNATExperimentsQuery(String xnatExperimentsQueryURL)
	{
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(xnatExperimentsQueryURL);
		int xnatStatusCode;

		try {
			log.info("Invoking XNAT query at " + xnatExperimentsQueryURL);
			xnatStatusCode = client.executeMethod(method);
		} catch (IOException e) {
			log.warning("Warning: error performing XNAT query", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return processXNATExperimentsQueryResponse(method, xnatStatusCode);
	}

	private static XNATExperiments processXNATExperimentsQueryResponse(GetMethod method, int xnatStatusCode)
	{
		if (xnatStatusCode == HttpServletResponse.SC_OK) {
			return extractXNATExperimentsFromResponse(method);
		} else if (xnatStatusCode == HttpServletResponse.SC_UNAUTHORIZED) {
			log.warning("Warning: invalid session token for XNAT experiments query");
			return XNATExperiments.emptyExperiments();
		} else {
			log.warning("Warning: error performing XNAT experiments query; XNAT status code = " + xnatStatusCode);
			return XNATExperiments.emptyExperiments();
		}
	}

	private static XNATExperiments extractXNATExperimentsFromResponse(GetMethod method)
	{
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
			Gson gson = new Gson();
			XNATExperiments xnatExperiments = gson.fromJson(reader, XNATExperiments.class);
			return xnatExperiments;
		} catch (IOException e) {
			log.warning("Warning: error processing XNAT experiments query result", e);
			return XNATExperiments.emptyExperiments();
		}
	}
}
