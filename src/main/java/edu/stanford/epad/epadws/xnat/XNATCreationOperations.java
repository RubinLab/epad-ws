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

	public static void createXNATSubjectFromDICOMPatient(String xnatProjectID, String xnatSubjectLabel,
			String dicomPatientName, String jsessionID)
	{
		String xnatSubjectURL = XNATUtil.buildXNATSubjectCreationURL(xnatProjectID, xnatSubjectLabel, dicomPatientName);
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
				eventTracker.recordPatientEvent(jsessionID, xnatProjectID, xnatSubjectLabel);
		} catch (IOException e) {
			log.warning("Error calling XNAT", e);
		}
	}

	public static boolean createXNATExperimentFromDICOMStudy(String xnatProjectID, String xnatSubjectLabel,
			String dicomStudyUID, String jsessionID)
	{
		String xnatStudyURL = XNATUtil.buildXNATExperimentCreationURL(xnatProjectID, xnatSubjectLabel, dicomStudyUID);

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
				eventTracker.recordStudyEvent(jsessionID, xnatProjectID, xnatSubjectLabel, dicomStudyUID);
		} catch (IOException e) {
			log.warning("Error calling XNAT", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return (!unexpectedCreationStatusCode(xnatStatusCode));
	}

	@SuppressWarnings("unused")
	private static boolean createXNATProject(String xnatProjectID, String xnatProjectName, String jsessionID)
	{
		String xnatProjectURL = XNATUtil.buildXNATProjectCreationURL(xnatProjectID, xnatProjectName);
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
				eventTracker.recordProjectEvent(jsessionID, xnatProjectID);
		} catch (IOException e) {
			log.warning("Error calling XNAT", e);
			xnatStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		return (!unexpectedCreationStatusCode(xnatStatusCode));
	}

	/**
	 * Take a directory containing a list of DICOM files and create XNAT representations of the each DICOM image. This
	 * method expects a properties file called xnat_upload.properties in the directory. This file should contain an XNAT
	 * project name, which identified the project for the new patients and their studies, and an XNAT session ID.
	 * <p>
	 * In general, pushing DICOM files through a DCM4CHEE server monitored by ePAD is preferred to using this method.
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
				String xnatProjectID = XNATUtil.projectName2XNATProjectID(xnatUploadProperties.getProperty("XNATProjectName"));
				String xnatSessionID = xnatUploadProperties.getProperty("XNATSessionID");
				log.info("xnatProjectID " + xnatProjectID);

				int numberOfDICOMFiles = 0;
				if (xnatProjectID != null && xnatSessionID != null) {
					for (File dicomFile : DicomTagFileUtils.listDICOMFiles(uploadDirectory)) {
						// DCM4CHEE stores the patient name as upper case so we match. TODO get original from database?
						String dicomPatientName = DicomReader.getPatientName(dicomFile).toUpperCase();
						String dicomPatientID = DicomReader.getPatientID(dicomFile);
						String dicomStudyUID = DicomReader.getStudyIUID(dicomFile);

						if (dicomPatientName != null) {
							if (dicomPatientID == null) // TODO Check that this is a valid thing to do.
								dicomPatientID = dicomPatientName;
							String xnatSubjectLabel = XNATUtil.dicomPatientID2XNATSubjectLabel(dicomPatientID);
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
