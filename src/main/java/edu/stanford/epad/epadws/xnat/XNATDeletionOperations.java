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

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.processing.events.EventTracker;

/**
 * Methods for deleting XNAT entities, such as projects, subjects, and experiments.
 * 
 * 
 * @author martin
 */
public class XNATDeletionOperations
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final EventTracker eventTracker = EventTracker.getInstance();

	public static int deleteXNATProject(String xnatProjectLabelOrID, String jsessionID)
	{
		String xnatProjectDeleteURL = XNATUtil.buildXNATProjectDeletionURL(xnatProjectLabelOrID);
		HttpClient client = new HttpClient();
		DeleteMethod method = new DeleteMethod(xnatProjectDeleteURL);
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			log.info("Invoking XNAT with URL " + xnatProjectDeleteURL);
			xnatStatusCode = client.executeMethod(method);
			if (unexpectedDeletionStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT to delete project; status code = " + xnatStatusCode);
			else {
				eventTracker.recordProjectEvent(jsessionID, xnatProjectLabelOrID);
			}
		} catch (IOException e) {
			log.warning("Error calling XNAT to delete for project " + xnatProjectLabelOrID, e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			method.releaseConnection();
		}
		return xnatStatusCode;
	}

	public static int deleteXNATSubject(String xnatProjectLabelOrID, String xnatSubjectLabelOrID, String jsessionID)
	{
		String xnatSubjectDeleteURL = XNATUtil.buildXNATSubjectDeletionURL(xnatProjectLabelOrID, xnatSubjectLabelOrID);
		HttpClient client = new HttpClient();
		DeleteMethod method = new DeleteMethod(xnatSubjectDeleteURL);
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			log.info("Invoking XNAT with URL " + xnatSubjectDeleteURL);
			xnatStatusCode = client.executeMethod(method);
			if (unexpectedDeletionStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT to delete subject; status code = " + xnatStatusCode);
			else {
				eventTracker.recordPatientEvent(jsessionID, xnatProjectLabelOrID, xnatSubjectLabelOrID);
			}
		} catch (IOException e) {
			log.warning("Error calling XNAT to delete patient " + xnatSubjectLabelOrID + " from project "
					+ xnatProjectLabelOrID, e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			method.releaseConnection();
		}
		return xnatStatusCode;
	}

	public static int deleteXNATDICOMStudy(String xnatProjectLabelOrID, String xnatSubjectLabelOrID, String studyUID,
			String sessionID)
	{
		String xnatStudyDeleteURL = XNATUtil.buildXNATDICOMStudyDeletionURL(xnatProjectLabelOrID, xnatSubjectLabelOrID,
				studyUID);
		HttpClient client = new HttpClient();
		DeleteMethod method = new DeleteMethod(xnatStudyDeleteURL);
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + sessionID);

		try {
			log.info("Invoking XNAT with URL " + xnatStudyDeleteURL);
			xnatStatusCode = client.executeMethod(method);
			if (unexpectedDeletionStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT to delete Study; status code = " + xnatStatusCode);
			else {
				eventTracker.recordStudyEvent(sessionID, xnatProjectLabelOrID, xnatSubjectLabelOrID, studyUID);
			}
		} catch (IOException e) {
			log.warning("Error calling XNAT to delete study + " + studyUID + " for patient " + xnatSubjectLabelOrID
					+ " from project " + xnatProjectLabelOrID, e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			method.releaseConnection();
		}
		return xnatStatusCode;
	}

	private static boolean unexpectedDeletionStatusCode(int statusCode)
	{
		return !(statusCode == HttpServletResponse.SC_OK || statusCode == HttpServletResponse.SC_ACCEPTED);
	}

	public static boolean successStatusCode(int statusCode)
	{
		return (statusCode == HttpServletResponse.SC_OK || statusCode == HttpServletResponse.SC_ACCEPTED);
	}
}
