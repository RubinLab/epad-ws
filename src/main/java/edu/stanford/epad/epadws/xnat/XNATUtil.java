//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.xnat;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

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

	private static String xnatHost = EPADConfig.xnatServer;
	private static int xnatPort = EPADConfig.xnatPort;

	public static String projectID2XNATProjectLabel(String projectID)
	{ // Alphanumeric and dot and dash only
		String result = projectID.replaceAll("[^a-zA-Z0-9-\\\\.]", "_");
		return result;
	}

	// Only a-zA-Z0-9, dash, and underscore allowed. Replace everything else with underscore. We replace
	// spaces with underscores though XNAT would allow spaces in labels.
	public static String subjectID2XNATSubjectLabel(String dicomPatientID)
	{
		String result = dicomPatientID.replaceAll("[^a-zA-Z0-9-_]", "_").replaceAll("\\\\^", "_");

		return result;
	}

	public static String buildXNATProjectCreationURL(String xnatProjectID, String xnatProjectName, String description)
	{
		String xnatHost = EPADConfig.xnatServer;
		int xnatPort = EPADConfig.xnatPort;
		String queryString = "?ID=" + xnatProjectID + "&name=" + encode(xnatProjectName) + "&description="
				+ encode(description) + "&accessibility=private";
		String urlString = XNATUtil.buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE) + queryString;

		return urlString;
	}

	public static String buildXNATProjectDeletionURL(String xnatProjectLabelOrID)
	{
		String urlString = XNATUtil.buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE) + xnatProjectLabelOrID;

		return urlString;
	}

	public static String buildXNATSubjectCreationURL(String xnatProjectLabelOrID, String xnatSubjectLabel,
			String dicomPatientName)
	{
		String queryPart = "?label=" + xnatSubjectLabel + "&src=" + encode(dicomPatientName);
		String urlString = XNATUtil.buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE) + xnatProjectLabelOrID
				+ "/subjects" + queryPart;

		return urlString;
	}

	public static String buildXNATSubjectDeletionURL(String xnatProjectLabelOrID, String xnatSubjectLabelOrID)
	{
		String urlString = XNATUtil.buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE) + xnatProjectLabelOrID
				+ "/subjects/" + xnatSubjectLabelOrID;

		return urlString;
	}

	// Setting the label field explicitly in this URL causes a new experiment to be created for
	// the same study in different projects. Otherwise we have a shared experiment, which is not what we want.
	public static String buildXNATDICOMStudyCreationURL(String xnatProjectLabelOrID, String xnatSubjectLabelOrID,
			String dicomStudyUID)
	{
		String xnatExperimentID = XNATUtil.dicomStudyUID2XNATExperimentID(dicomStudyUID);
		String urlString = XNATUtil.buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE) + xnatProjectLabelOrID
				+ "/subjects/" + xnatSubjectLabelOrID + "/experiments/" + xnatExperimentID + "?name=" + dicomStudyUID
				+ "&xsiType=xnat:otherDicomSessionData";

		return urlString;
	}

	public static String buildXNATDICOMStudyDeletionURL(String xnatProjectLabelOrID, String xnatSubjectLabelOrID,
			String studyUID)
	{
		String urlString = XNATUtil.buildXNATBaseURL(xnatHost, xnatPort, XNAT_PROJECTS_BASE) + xnatProjectLabelOrID
				+ "/subjects/" + xnatSubjectLabelOrID + "/experiments/" + dicomStudyUID2XNATExperimentID(studyUID);

		return urlString;
	}

	public static String buildXNATBaseURL(String host, int port, String base)
	{
		return buildXNATBaseURL(host, port, base, "");
	}

	public static String buildXNATSessionURL()
	{
		return buildXNATBaseURL(xnatHost, xnatPort, XNATUtil.XNAT_SESSION_BASE);
	}

	public static String dicomStudyUID2XNATExperimentID(String dicomStudyUID)
	{
		return dicomStudyUID.replace('.', '_');
	}

	public static boolean unexpectedXNATCreationStatusCode(int statusCode)
	{
		return !(statusCode == HttpServletResponse.SC_OK || statusCode == HttpServletResponse.SC_CREATED || statusCode == HttpServletResponse.SC_CONFLICT);
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
