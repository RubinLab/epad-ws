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

	public static boolean deleteXNATProject(String xnatProjectLabelOrID, String jsessionID)
	{
		String xnatProjectDeleteURL = XNATUtil.buildXNATProjectDeletionURL(xnatProjectLabelOrID);
		HttpClient client = new HttpClient();
		DeleteMethod method = new DeleteMethod(xnatProjectDeleteURL);
		boolean successfulDelete = false;
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			log.info("Invoking XNAT with URL " + xnatProjectDeleteURL);
			xnatStatusCode = client.executeMethod(method);
			if (unexpectedDeletionStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT; status code = " + xnatStatusCode);
			else {
				eventTracker.recordProjectEvent(jsessionID, xnatProjectLabelOrID);
				successfulDelete = true;
			}
		} catch (IOException e) {
			log.warning("Error calling XNAT to delete from project " + xnatProjectLabelOrID, e);
		} finally {
			method.releaseConnection();
		}
		return successfulDelete;
	}

	public static boolean deleteXNATSubject(String xnatProjectLabelOrID, String xnatSubjectLabelOrID, String jsessionID)
	{
		String xnatSubjectDeleteURL = XNATUtil.buildXNATSubjectDeletionURL(xnatProjectLabelOrID, xnatSubjectLabelOrID);
		HttpClient client = new HttpClient();
		DeleteMethod method = new DeleteMethod(xnatSubjectDeleteURL);
		boolean successfulDelete = false;
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			log.info("Invoking XNAT with URL " + xnatSubjectDeleteURL);
			xnatStatusCode = client.executeMethod(method);
			if (unexpectedDeletionStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT; status code = " + xnatStatusCode);
			else {
				eventTracker.recordPatientEvent(jsessionID, xnatProjectLabelOrID, xnatSubjectLabelOrID);
				successfulDelete = true;
			}
		} catch (IOException e) {
			log.warning("Error calling XNAT to delete patient " + xnatSubjectLabelOrID + " from project "
					+ xnatProjectLabelOrID, e);
		} finally {
			method.releaseConnection();
		}
		return successfulDelete;
	}

	public static boolean deleteXNATDICOMStudy(String xnatProjectLabelOrID, String xnatSubjectLabelOrID, String studyUID,
			String sessionID)
	{
		String xnatStudyDeleteURL = XNATUtil.buildXNATDICOMStudyDeletionURL(xnatProjectLabelOrID, xnatSubjectLabelOrID,
				studyUID);
		HttpClient client = new HttpClient();
		DeleteMethod method = new DeleteMethod(xnatStudyDeleteURL);
		boolean successfulDelete = false;
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + sessionID);

		try {
			log.info("Invoking XNAT with URL " + xnatStudyDeleteURL);
			xnatStatusCode = client.executeMethod(method);
			if (unexpectedDeletionStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT; status code = " + xnatStatusCode);
			else {
				eventTracker.recordStudyEvent(sessionID, xnatProjectLabelOrID, xnatSubjectLabelOrID, studyUID);
				successfulDelete = true;
			}
		} catch (IOException e) {
			log.warning("Error calling XNAT to delete study + " + studyUID + " for patient " + xnatSubjectLabelOrID
					+ " from project " + xnatProjectLabelOrID, e);
		} finally {
			method.releaseConnection();
		}
		return successfulDelete;
	}

	private static boolean unexpectedDeletionStatusCode(int statusCode)
	{
		return !(statusCode == HttpServletResponse.SC_OK || statusCode == HttpServletResponse.SC_ACCEPTED);
	}
}
