package edu.stanford.epad.epadws.xnat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.dicom.DicomReader;
import edu.stanford.epad.common.dicom.DicomTagFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.processing.events.EventTracker;

/**
 * Methods for creating XNAT entities, such as projects, subjects, and experiments.
 * 
 * 
 * @author martin
 */
public class XNATCreationOperations
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String XNAT_UPLOAD_PROPERTIES_FILE_NAME = "xnat_upload.properties";

	private static final EventTracker eventTracker = EventTracker.getInstance();

	public static void createXNATSubjectFromDICOMPatient(String xnatProjectLabelOrID, String xnatSubjectLabel,
			String dicomPatientName, String jsessionID)
	{
		String xnatSubjectURL = XNATUtil.buildXNATSubjectCreationURL(xnatProjectLabelOrID, xnatSubjectLabel,
				dicomPatientName);
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(xnatSubjectURL);
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			// log.info("Creating patient " + dicomPatientName + " in project " + xnatProjectLabelOrID + " in XNAT");
			xnatStatusCode = client.executeMethod(method);
			if (unexpectedCreationStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT with URL " + xnatSubjectURL + "; status code = " + xnatStatusCode);
			else
				eventTracker.recordPatientEvent(jsessionID, xnatProjectLabelOrID, xnatSubjectLabel);
		} catch (IOException e) {
			log.warning("Error calling XNAT with URL " + xnatSubjectURL, e);
		} finally {
			method.releaseConnection();
		}
	}

	public static boolean createXNATExperimentFromDICOMStudy(String xnatProjectLabelOrID, String xnatSubjectLabelOrID,
			String studyUID, String jsessionID)
	{
		String xnatStudyURL = XNATUtil.buildXNATDICOMExperimentCreationURL(xnatProjectLabelOrID, xnatSubjectLabelOrID,
				studyUID);
		HttpClient client = new HttpClient();
		PutMethod putMethod = new PutMethod(xnatStudyURL);
		int xnatStatusCode;

		putMethod.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			// log.info("Creating study " + studyUID + " for patient " + xnatSubjectLabelOrID + " in project "
			// + xnatProjectLabelOrID + " in XNAT");
			xnatStatusCode = client.executeMethod(putMethod);
			if (unexpectedCreationStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT with URL " + xnatStudyURL + "; status code = " + xnatStatusCode);
			else
				eventTracker.recordStudyEvent(jsessionID, xnatProjectLabelOrID, xnatSubjectLabelOrID, studyUID);
		} catch (IOException e) {
			log.warning("Error calling XNAT with URL " + xnatStudyURL, e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			putMethod.releaseConnection();
		}
		return (!unexpectedCreationStatusCode(xnatStatusCode));
	}

	@SuppressWarnings("unused")
	private static boolean createXNATProject(String xnatProjectName, String jsessionID)
	{
		String xnatProjectURL = XNATUtil.buildXNATProjectCreationURL(xnatProjectName);
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(xnatProjectURL);
		int xnatStatusCode;

		method.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);

		try {
			log.info("Invoking XNAT with URL " + xnatProjectURL);
			xnatStatusCode = client.executeMethod(method);
			if (unexpectedCreationStatusCode(xnatStatusCode))
				log.warning("Failure calling XNAT; status code = " + xnatStatusCode);
			else
				eventTracker.recordProjectEvent(jsessionID, xnatProjectName);
		} catch (IOException e) {
			log.warning("Error calling XNAT", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			method.releaseConnection();
		}
		return (!unexpectedCreationStatusCode(xnatStatusCode));
	}

	/**
	 * Take a directory containing a list of DICOM files and create XNAT representations of the each DICOM image in the
	 * directory.
	 * <p>
	 * This method expects a properties file called xnat_upload.properties in the upload directory. This file should
	 * contain a property called XNATProjectName, which identified the project for the new patients and their studies, and
	 * XNATSessionID, which contains the session key of the user initiating the upload.
	 * 
	 * @param dicomUploadDirectory
	 */
	public static void createXNATEntitiesFromDICOMFilesInUploadDirectory(File dicomUploadDirectory)
	{
		String propertiesFilePath = dicomUploadDirectory.getAbsolutePath() + File.separator
				+ XNAT_UPLOAD_PROPERTIES_FILE_NAME;
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
				String xnatProjectID = XNATUtil.projectName2XNATProjectID(xnatUploadProperties.getProperty("XNATProjectName"));
				String xnatSessionID = xnatUploadProperties.getProperty("XNATSessionID");

				int numberOfDICOMFiles = 0;
				if (xnatProjectID != null && xnatSessionID != null) {
					for (File dicomFile : DicomTagFileUtils.listDICOMFiles(dicomUploadDirectory)) {
						String dicomPatientName = DicomReader.getPatientName(dicomFile);
						String dicomPatientID = DicomReader.getPatientID(dicomFile);
						String studyUID = DicomReader.getStudyIUID(dicomFile);

						if (dicomPatientID != null && dicomPatientName != null && studyUID != null) {
							dicomPatientName = dicomPatientName.toUpperCase(); // DCM4CHEE stores the patient name as upper case
							String xnatSubjectLabel = XNATUtil.dicomPatientID2XNATSubjectLabel(dicomPatientID);
							createXNATSubjectFromDICOMPatient(xnatProjectID, xnatSubjectLabel, dicomPatientName, xnatSessionID);
							createXNATExperimentFromDICOMStudy(xnatProjectID, xnatSubjectLabel, studyUID, xnatSessionID);
						} else
							log.warning("Missing patient name, ID or studyUID in DICOM file " + dicomFile.getAbsolutePath());
						numberOfDICOMFiles++;
					}
					if (numberOfDICOMFiles != 0)
						log.info("Found " + numberOfDICOMFiles + " DICOM file(s) in upload directory");
					else
						log.warning("No DICOM files found in upload directory!");
				} else {
					log.warning("Missing XNAT project name and/or session ID in properties file" + propertiesFilePath);
				}
			} catch (IOException e) {
				log.warning("Error processing upload in directory " + propertiesFilePath, e);
			} finally {
				IOUtils.closeQuietly(propertiesFileStream);
			}
		}
	}

	private static boolean unexpectedCreationStatusCode(int statusCode)
	{
		return !(statusCode == HttpServletResponse.SC_OK || statusCode == HttpServletResponse.SC_CREATED || statusCode == HttpServletResponse.SC_CONFLICT);
	}
}
