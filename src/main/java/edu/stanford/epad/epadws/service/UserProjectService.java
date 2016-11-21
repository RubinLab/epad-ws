/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
package edu.stanford.epad.epadws.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;

import edu.stanford.epad.common.dicom.DicomReader;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.MailUtil;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.TaskStatus;
import edu.stanford.epad.epadws.aim.AIMQueries;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.aim.dicomsr.Aim2DicomSRConverter;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.XNATQueries;
import edu.stanford.epad.epadws.security.IdGenerator;
import edu.stanford.epad.epadws.xnat.XNATCreationOperations;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;
import edu.stanford.epad.epadws.xnat.XNATUtil;
import edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation;

/**
 * Originally a Wrapper class to call either XNAT or EPAD project/user api (not only calls EPAD api)
 * 
 * @author Dev Gude
 *
 */
public class UserProjectService {
	private static final EPADLogger log = EPADLogger.getInstance();

	public static Map<String, String> pendingPNGs = new HashMap<String, String>();
	public static Map<String, String> pendingUploads = new HashMap<String, String>();

	private static final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();	
	private static final EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();	

	public static final String XNAT_UPLOAD_PROPERTIES_FILE_NAME = "xnat_upload.properties";
	public static HashSet<String> duplicatePatientIds= new HashSet<>();

	/**
	 * Check if user is collaborator
	 * @param sessionID
	 * @param username
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	public static boolean isCollaborator(String sessionID, String username, String projectID) throws Exception {
		return projectOperations.isCollaborator(username, projectID);
	}

	/**
	 * Check if user is owner
	 * @param sessionID
	 * @param username
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	public static boolean isOwner(String sessionID, String username, String projectID) throws Exception {
		return projectOperations.isOwner(username, projectID);
	}

	/**
	 * Check if user is member
	 * @param sessionID
	 * @param username
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	public static boolean isMember(String sessionID, String username, String projectID) throws Exception {
		return projectOperations.isMember(username, projectID);
	}

	/**
	 * Get all project ids
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getAllProjectIDs() throws Exception {
		List<Project> projects = projectOperations.getAllProjects();
		Set<String> projectIDs = new HashSet<String>();
		for (Project project: projects)
			projectIDs.add(project.getProjectId());
		return projectIDs;
	}

	/**
	 * Get all study uids for project
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getAllStudyUIDsForProject(String projectID) throws Exception {
		List<Study> studies = projectOperations.getAllStudiesForProject(projectID);
		Set<String> studyIDs = new HashSet<String>();
		for (Study study: studies)
			studyIDs.add(study.getStudyUID());
		return studyIDs;
	}

	/**
	 * Get all study uids for subject
	 * @param projectID
	 * @param patientID
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getStudyUIDsForSubject(String projectID, String patientID) throws Exception {
		List<Study> studies = projectOperations.getStudiesForProjectAndSubject(projectID, patientID);
		Set<String> studyIDs = new HashSet<String>();
		for (Study study: studies)
			studyIDs.add(study.getStudyUID());
		return studyIDs;
	}

	/**
	 * Get first project fro study
	 * @param studyUID
	 * @return
	 * @throws Exception
	 */
	public static String getFirstProjectForStudy(String studyUID) throws Exception {
		Project project = projectOperations.getFirstProjectForStudy(studyUID);
		if (project != null)
			return project.getProjectId();
		else
			return EPADConfig.xnatUploadProjectID;
	}

	/**
	 * Get subjectids for project
	 * @param projectID
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getSubjectIDsForProject(String projectID) throws Exception {
		List<Subject> subjects = projectOperations.getSubjectsForProject(projectID);
		Set<String> studyIDs = new HashSet<String>();
		for (Subject subject: subjects)
			studyIDs.add(subject.getSubjectUID());
		return studyIDs;
	}

	/**
	 * Take a directory containing a list of DICOM files and create XNAT representations of the each DICOM image in the
	 * directory.
	 * <p>
	 * This method expects a properties file called xnat_upload.properties in the upload directory. This file should
	 * contain a property called XNATProjectName, which identifies the project ID for the new patients and their studies,
	 * and XNATSessionID, which contains the session key of the user initiating the upload.
	 * 
	 * <p>
	 * If the header of a DICOM image is missing the patient name, patient ID, or study instance UID field then it is
	 * skipped.
	 * 
	 * @param dicomUploadDirectory
	 */
	/**
	 * @param dicomUploadDirectory
	 * @return
	 */
	public static String createProjectEntitiesFromDICOMFilesInUploadDirectory(File dicomUploadDirectory, boolean zip)
	{
		int numberOfDICOMFiles = 0;
		String propertiesFilePath = dicomUploadDirectory.getAbsolutePath() + File.separator
				+ XNAT_UPLOAD_PROPERTIES_FILE_NAME;
		File xnatUploadPropertiesFile = new File(propertiesFilePath);
		try {
			Thread.sleep(5000); // Give it a couple of seconds for the property file to appear
		} catch (InterruptedException e1) {}
		String xnatUserName = null;
		if (!xnatUploadPropertiesFile.exists())
			log.warning("Could not find XNAT upload properties file " + propertiesFilePath);
		else {
			Properties xnatUploadProperties = new Properties();
			FileInputStream propertiesFileStream = null;
			try {
				propertiesFileStream = new FileInputStream(xnatUploadPropertiesFile);
				xnatUploadProperties.load(propertiesFileStream);
				String xnatProjectLabel = xnatUploadProperties.getProperty("XNATProjectName");
				if (xnatProjectLabel != null && xnatProjectLabel.equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned")))
					throw new Exception("Files can not be uploaded to this project:" + xnatProjectLabel);
				String xnatSessionID = xnatUploadProperties.getProperty("XNATSessionID");
				xnatUserName = xnatUploadProperties.getProperty("XNATUserName");
				String patientID = xnatUploadProperties.getProperty("SubjectName");
				if (patientID == null) patientID = xnatUploadProperties.getProperty("SubjectID");
				String studyUID = xnatUploadProperties.getProperty("StudyName");
				if (studyUID == null) studyUID = xnatUploadProperties.getProperty("StudyUID");
				String seriesUID = xnatUploadProperties.getProperty("SeriesName");
				if (seriesUID == null) seriesUID = xnatUploadProperties.getProperty("SeriesUID");
				log.info("Found XNAT upload properties file " + propertiesFilePath + " project:" + xnatProjectLabel + " user:" + xnatUserName);
				log.info("Properties:" + xnatUploadProperties);
				log.info("XNAT Properties, projectID:"  + xnatProjectLabel + " username:" + xnatUserName + " patient:" + patientID + " study:" + studyUID + " series:" + seriesUID);
				if (xnatProjectLabel != null) {
					xnatUploadPropertiesFile.delete();
					//ml prevent null username 
					if (xnatUserName == null)
						xnatUserName = EPADConfig.xnatUploadProjectUser;
					//ml sessionid param set to null for not triggering the plugin (it was xnatSessionID) 
					numberOfDICOMFiles = createProjectEntitiesFromDICOMFilesInUploadDirectory(dicomUploadDirectory, xnatProjectLabel, null, xnatUserName, patientID, studyUID, seriesUID, !zip);
					if (numberOfDICOMFiles != 0)
					{
						projectOperations.createEventLog(xnatUserName, xnatProjectLabel, null, null, null, null, null, dicomUploadDirectory.getName(), "UPLOAD DICOMS", "Number of Dicoms: " +numberOfDICOMFiles, false);
						log.info("Found " + numberOfDICOMFiles + " DICOM file(s) in directory uploaded by " + xnatUserName + " for project " + xnatProjectLabel);
					}
					else
					{
						log.warning("No DICOM files found in upload directory!");
						return null;
					}
				} else {
					log.warning("Missing XNAT project name and/or session ID in properties file" + propertiesFilePath);
				}
				return xnatUserName + ":" + numberOfDICOMFiles;
			} catch (Exception e) {
				log.warning("Error processing upload in directory " + dicomUploadDirectory.getAbsolutePath(), e);
			} finally {
				IOUtils.closeQuietly(propertiesFileStream);
			}
		}
		return xnatUserName + ":" + numberOfDICOMFiles;
	}

	public static String getUserNameFromPropertiesFile(File dicomUploadDirectory) {
		String propertiesFilePath = dicomUploadDirectory.getAbsolutePath() + File.separator
				+ XNAT_UPLOAD_PROPERTIES_FILE_NAME;
		File xnatUploadPropertiesFile = new File(propertiesFilePath);
		try {
			Thread.sleep(2000); // Give it a couple of seconds for the property file to appear
		} catch (InterruptedException e1) {}
		if (!xnatUploadPropertiesFile.exists()) {
			log.warning("Could not find XNAT upload properties file " + propertiesFilePath);
		}
		else {
			Properties xnatUploadProperties = new Properties();
			FileInputStream propertiesFileStream = null;
			try {
				propertiesFileStream = new FileInputStream(xnatUploadPropertiesFile);
				xnatUploadProperties.load(propertiesFileStream);
				String xnatProjectLabel = xnatUploadProperties.getProperty("XNATProjectName");
				String xnatSessionID = xnatUploadProperties.getProperty("XNATSessionID");
				String xnatUserName = xnatUploadProperties.getProperty("XNATUserName");
				log.info("Properties:" + xnatUploadProperties);
				return xnatUserName;
			} catch (Exception e) {
				log.warning("Error processing upload in directory " + dicomUploadDirectory.getAbsolutePath(), e);
			} finally {
				IOUtils.closeQuietly(propertiesFileStream);
			}
		}
		return null;
	}

	/**
	 * Create subject/study records from uploaded dicoms and add to project 
	 * @param dicomUploadDirectory
	 * @param projectID
	 * @param sessionID
	 * @param username
	 * @return
	 * @throws Exception
	 */
	private static int createProjectEntitiesFromDICOMFilesInUploadDirectory(File dicomUploadDirectory, String projectID, String sessionID, String username, String subjectID, String studyUID, String seriesUID, boolean allFiles) throws Exception
	{
		int numberOfDICOMFiles = 0;
		Collection<File> files = listDICOMFiles(dicomUploadDirectory);
		log.info("Number of files found:" + files.size());
		int nondicoms = 0;
		long i = 0;
		for (File dicomFile : files) {
			try {
				log.info("File " + i++ + " : " +dicomFile.getName());
				if (!isDicomFile(dicomFile)) {
					if (PixelMedUtils.isDicomSR(dicomFile.getAbsolutePath())) {
						try {
							log.info("DicomSR found in createProjectEntitiesFromDICOMFilesInUploadDirectory. processing");
							Aim2DicomSRConverter converter=new Aim2DicomSRConverter();
							String xml=converter.DicomSR2Aim(dicomFile.getAbsolutePath(), projectID);
							String tmpAimName="/tmp/tmpAim"+System.currentTimeMillis()+".xml";
							File tmpAim=new File(tmpAimName);
							EPADFileUtils.write(tmpAim, xml);
							log.info("tmp aim path:"+ tmpAim.getAbsolutePath());
							if (AIMUtil.saveAIMAnnotation(tmpAim, projectID, 0, sessionID, username, false))
								log.warning("Error processing aim file:" + dicomFile.getName());
						} catch (Exception x) {
							log.warning("Error uploading aim file:" + dicomFile.getName() + ":" + x.getMessage());
						}
						dicomFile.delete();
						nondicoms++;
						continue;
					}
					if (dicomFile.getName().endsWith(".xml"))
					{
						try {
							if (AIMUtil.saveAIMAnnotation(dicomFile, projectID, 0, sessionID, username, true))
								log.warning("Error processing aim file:" + dicomFile.getName());
						} catch (Exception x) {
							log.warning("Error uploading aim file:" + dicomFile.getName() + ":" + x.getMessage());
						}
						dicomFile.delete();
						nondicoms++;
						continue;
					}
					else if ((allFiles || dicomFile.getName().endsWith(".nii")))
					{
						try {
							DefaultEpadOperations.getInstance().createFile(username, projectID, subjectID, studyUID, seriesUID, dicomFile, null, null, sessionID);
						} catch (Exception x) {
							log.warning("Error uploading file:" + dicomFile.getName() + ":" + x.getMessage(), x);
						}
						dicomFile.delete();
						nondicoms++;
						continue;
					}
					else
					{
						try {
							log.warning("Deleting non-dicom file:" + dicomFile.getName());
							dicomFile.delete();
						} catch (Exception x) {log.warning("Error deleting", x);}						
						continue;
					}
				}
				projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_ADD_TO_PROJECT, projectID, dicomUploadDirectory.getName(), "Files processed: " + i, null, null);
				log.debug("Adding to project:" + dicomFile.getName());
				if (createProjectEntitiesFromDICOMFile(dicomFile, projectID, sessionID, username))
					numberOfDICOMFiles++;
			} catch (Throwable x) {
				log.warning("Error processing dicom:" + dicomFile.getName(), x);
				databaseOperations.insertEpadEvent(
						username, 
						"Error processing dicom:" + dicomFile.getName(), 
						dicomFile.getName(), "", dicomFile.getName(), dicomFile.getName(), dicomFile.getName(), projectID, "Error:" + x.getMessage());				}
		}
		projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_ADD_TO_PROJECT, dicomUploadDirectory.getName(), "Files processed: " + numberOfDICOMFiles, null, new Date());
		if (nondicoms != 0)
			projectOperations.createEventLog(username, projectID, null, null, null, null, null, "UPLOAD FILES", "Number of files: " +nondicoms);
		log.info("Number of dicom files in upload:" + numberOfDICOMFiles);
		log.info("Number of non-dicom files in upload:" + nondicoms);
		return numberOfDICOMFiles;
	}
	
	

	/**
	 * Create subject/study records from uploaded dicom file and add to project 
	 * @param dicomFile
	 * @param projectID
	 * @param sessionID
	 * @param username
	 * @throws Exception
	 */
	public static boolean createProjectEntitiesFromDICOMFile(File dicomFile, String projectID, String sessionID, String username) throws Exception
	{
		DicomObject dicomObject=null;
		try{
			dicomObject = DicomReader.getDicomObject(dicomFile);
		}catch(Exception e){
			log.warning("Dicom object couldn't be retrieved!");
			return false;
		}
		//corrupt and/or wrong dicom file control ml
		if (dicomObject==null) {
			log.warning("Dicom object couldn't be retrieved!");
			return false;
		}
		String dicomPatientName = dicomObject.getString(Tag.PatientName);
		String dicomPatientID = dicomObject.getString(Tag.PatientID);
		String studyUID = dicomObject.getString(Tag.StudyInstanceUID);
		String studyDate = dicomObject.getString(Tag.StudyDate);
		String seriesUID = dicomObject.getString(Tag.SeriesInstanceUID);
		String modality = dicomObject.getString(Tag.Modality);
		log.info("Uploading dicom, username:" + username + " projectID:" + projectID + " patientName:" + dicomPatientName + " patientID:" + dicomPatientID + " studyUID:" + studyUID + " studyDate:" + studyDate + " seriesUID:" + seriesUID + " modality:" + modality);
		if (dicomPatientID == null || dicomPatientID.trim().length() == 0 
				|| dicomPatientID.equalsIgnoreCase("ANON") 
				|| dicomPatientID.equalsIgnoreCase("Unknown") 
				|| dicomPatientID.contains("%") 
				|| dicomPatientID.equalsIgnoreCase("Anonymous"))
		{
			String message = "Invalid patientID:'" + dicomPatientID + "' file:" + dicomFile.getName() + ", Rejecting file";
			log.warning(message);
			if (dicomPatientID != null)
				message = "Invalid non-unique patient ID " + dicomPatientID + " in DICOM file";
			if (dicomPatientID != null && dicomPatientID.contains("%"))
			{
				message = "An invalid character in patient ID " + dicomPatientID;
			}
			databaseOperations.insertEpadEvent(
					username, 
					message, 
					seriesUID, "", "Invalid PatientID:" + dicomPatientID, dicomPatientName, studyUID, projectID, "Error in Upload");					
			dicomFile.delete();
			projectOperations.createEventLog(username, projectID, dicomPatientID, studyUID, seriesUID, null, null, dicomFile.getName(), "UPLOAD SERIES", message, true);
			return false;
		}
		if (pendingUploads.size() < 300)
			pendingUploads.put(studyUID, username + ":" + projectID);
		if (pendingPNGs.size() < 300)
			pendingPNGs.put(seriesUID, username + ":" + projectID);
		
		//check if the patient id already exist in the system. If so put a log or something, specifying the patient name that is used and the project
		Subject subject = projectOperations.getSubject(dicomPatientID);
		//TODO for some reason this(dicomPatientName.trim().toLowerCase().equalsIgnoreCase(subject.getName().trim().toLowerCase()) does not work for Anonymous
		if (subject != null && dicomPatientName!=null && subject.getName()!=null && !dicomPatientName.trim().toLowerCase().equalsIgnoreCase(subject.getName().trim().toLowerCase()) ) {
			if (!duplicatePatientIds.contains(dicomPatientID)) {
				duplicatePatientIds.add(dicomPatientID);
				List<Project> projects=projectOperations.getProjectsForSubject(subject.getSubjectUID());
				StringBuilder projectsStr=new StringBuilder();
				for (Project p: projects) {
					projectsStr.append(p.getName());
					projectsStr.append(",");
				}
				String message="The patient "+dicomPatientName+" is already uploaded as "+subject.getName()+" in project(s): "+ projectsStr.toString().substring(0, projectsStr.length()-1);
				projectOperations.createEventLog(username, projectID, dicomPatientID, studyUID, seriesUID, null, null, dicomFile.getName(), "DUPLICATE DEIDENTIFICATION", message, true);
			}
			//for keeping the same name in cache
			dicomPatientName=subject.getName();
		}
		
		if (dicomPatientID != null && studyUID != null) {
			//databaseOperations.deleteSeriesOnly(seriesUID); // This will recreate all images
			if (dicomPatientName == null) dicomPatientName = "";
			dicomPatientName = dicomPatientName.toUpperCase(); // DCM4CHEE stores the patient name as upper case

			addSubjectAndStudyToProject(dicomPatientID, dicomPatientName, studyUID, studyDate, projectID, sessionID, username);

			if ("SEG".equals(modality))
			{
				try {
					//					List<EPADAIM> aims = databaseOperations.getAIMsByDSOSeries(projectID, dicomPatientID, seriesUID);
					//					List<ImageAnnotation> ias = AIMQueries.getAIMImageAnnotations(AIMSearchType.SERIES_UID, seriesUID, username, 1, 50);
					//					if (ias.size() == 0 || aims.size() == 0) 
					//						AIMUtil.generateAIMFileForDSO(dicomFile, username, projectID);
					List<EPADAIM> aims = databaseOperations.getAIMsByDSOSeries(null, dicomPatientID, seriesUID);
					List<ImageAnnotation> ias = AIMQueries.getAIMImageAnnotations(AIMSearchType.SERIES_UID, seriesUID, username, 1, 50);
					boolean generateAim = false;
					if (aims.size() == 1 && aims.get(0).projectID.equals(EPADConfig.xnatUploadProjectID) && !projectID.equals(EPADConfig.xnatUploadProjectID))
						generateAim = true;
					if (generateAim || ias.size() == 0 || aims.size() == 0) 
					{
						AIMUtil.generateAIMFileForDSO(dicomFile, username, projectID);
					}
					else
					{
						boolean projectAIMExists = false;
						for (EPADAIM aim: aims)
						{
							if (aim.projectID.equals(projectID))
							{
								projectAIMExists = true;
								break;
							}
						}
						if (!projectAIMExists)
							databaseOperations.addProjectToAIM(projectID, aims.get(0).aimID);
					}
					Set<String> imageUIDs = Dcm4CheeDatabase.getInstance().getDcm4CheeDatabaseOperations().getImageUIDsForSeries(seriesUID);
					if (false && !imageUIDs.isEmpty()) {
						String message = "DSO for  patientID:" + dicomPatientID + " Series:" + seriesUID + " file:" + dicomFile.getName() + " already exists. Please delete DSO before reuploading";
						log.warning(message);
						databaseOperations.insertEpadEvent(
								username, 
								message, 
								seriesUID, "", dicomPatientID, dicomPatientName, studyUID, projectID, "Error in Upload");					
						dicomFile.delete();
						projectOperations.createEventLog(username, projectID, dicomPatientID, studyUID, seriesUID, null, null, dicomFile.getName(), "UPLOAD DSO", message, true);
						return false;
					}
					String imageUID = dicomObject.getString(Tag.SOPInstanceUID);
					String pngMaskDirectoryPath = EPADConfig.getEPADWebServerPNGDir() + "/studies/" + studyUID + "/series/" + seriesUID + "/images/"
							+ imageUID + "/masks/";
					File pngDirectory = new File(pngMaskDirectoryPath);
					if (pngDirectory.exists())
					{
						//						File[] files = pngDirectory.listFiles();
						//						for (File file: files)
						//						{
						//							//file.delete();
						//						}
					}
				} catch (Exception x) {
					log.warning("Error generating DSO Annotation:", x);
					databaseOperations.insertEpadEvent(
							username, 
							"Error generating DSO Annotation", 
							seriesUID, "", dicomPatientID, dicomPatientName, studyUID, projectID, "Upload " + dicomFile.getName());					
					projectOperations.createEventLog(username, projectID, dicomPatientID, studyUID, seriesUID, null, null, dicomFile.getName(), "UPLOAD DSO", "Error generating DSO Annotation", true);
				}
			}
		} else {
			log.warning("Missing patient ID or studyUID in DICOM file " + dicomFile.getAbsolutePath());
			databaseOperations.insertEpadEvent(
					username, 
					"Missing patient ID or studyUID in DICOM file", 
					seriesUID, "", dicomPatientID, dicomPatientName, studyUID, projectID, "Process Upload");					
			projectOperations.createEventLog(username, projectID, dicomPatientID, studyUID, seriesUID, null, null, null, "UPLOAD DSO", "Missing patient ID or studyUID in DICOM file " + dicomFile.getName(), true);
		}
		return true;
	}

	/**
	 * Add subject/study records to project 
	 * @param subjectID
	 * @param subjectName
	 * @param studyUID
	 * @param projectID
	 * @param sessionID
	 * @param username
	 */
	public static void addSubjectAndStudyToProject(String subjectID, String subjectName, String studyUID, String studyDate, String projectID, String sessionID, String username) {
		try {
			log.info("Create Subject:" + subjectID);
			projectOperations.createSubject(username, subjectID, subjectName, null, null);
			log.info("Create Study:" +  studyUID);
			projectOperations.createStudy(username, studyUID, subjectID, "", getDate(studyDate));
			log.info("Upload/Transfer: Adding Study:" +  studyUID + " Subject:" + subjectID + " to Project:" + projectID);
			projectOperations.addStudyToProject(username, studyUID, subjectID, projectID);
		} catch (Exception e) {
			log.warning("Error creating subject/study in EPAD:", e);
		}
	}

	private static Collection<File> listDICOMFiles(File dir)
	{
		log.info("Checking upload directory:" + dir.getAbsolutePath());
		Set<File> files = new HashSet<File>();
		if (!dir.isDirectory())
		{
			log.info("Not a directory:" + dir.getAbsolutePath());
			return files;
		}
		if (dir.listFiles() != null) {
			for (File entry : dir.listFiles()) {
				//skip if this is a jpg file
				if (entry.getName().endsWith(".jpg") || entry.getName().endsWith(".jpeg"))
					continue;
				if (isDicomFile(entry))
				{
					files.add(entry);
				}
				else if (!entry.isDirectory() && entry.getName().indexOf(".") == -1)
				{
					try {
						File newFile = new File(entry.getParentFile(), entry.getName()+".dcm");
						entry.renameTo(newFile);
						files.add(newFile);
					} catch (Exception x) {log.warning("Error renaming", x);}
				}
				else if (!entry.isDirectory() && entry.getName().startsWith("1.") && (entry.getName().lastIndexOf(".") != entry.getName().length()-3))
				{
					try {
						File newFile = new File(entry.getParentFile(), entry.getName()+".dcm");
						entry.renameTo(newFile);
						files.add(newFile);
					} catch (Exception x) {log.warning("Error renaming", x);}
				}
				else if (entry.isDirectory()) 
				{
					files.addAll(listDICOMFiles(entry));
				}
				else
				{
					files.add(entry);
					//					try {
					//						log.warning("Deleting non-dicom file:" + entry.getName());
					//						entry.delete();
					//					} catch (Exception x) {log.warning("Error deleting", x);}
				}
			}
		}
		else if (!dir.getName().endsWith(".zip")){
			try {
				log.warning("Deleting non-dicom file:" + dir.getName());
				dir.delete();
			} catch (Exception x) {log.warning("Error deleting", x);}
		}

		return files;
	}

	public static void sendNewPassword(String loggedInUsername, String username) throws Exception
	{
		log.info("New password requested for " + username);
		User loggedInUser =  projectOperations.getUser(loggedInUsername);
		if (loggedInUser == null)
			throw new Exception("User not found " + loggedInUsername);
		User user = projectOperations.getUser(username);
		if (user == null)
			throw new Exception("User not found " + username);
		if (!loggedInUsername.equals(username) && !loggedInUser.isAdmin() && !loggedInUsername.equals(user.getCreator()))
			throw new Exception("No permissions to reset " + username + "'s password");
		String newPwd = username;
		if (newPwd.length() > 4) newPwd = newPwd.substring(0, 4);
		newPwd = newPwd + new IdGenerator().generateId(6);
		user.setPassword(newPwd);
		boolean tls = "true".equalsIgnoreCase(EPADConfig.getParamValue("SMTPtls", "true"));
		MailUtil mu = new MailUtil(	EPADConfig.getParamValue("SMTPHost", "smtp.gmail.com"), 
				EPADConfig.getParamValue("SMTPPort", "587"), 
				EPADConfig.getParamValue("MailUser", "epadstanford@gmail.com"), 
				EPADConfig.getParamValue("MailPassword"), 
				true);
		// No password, try sendMail
		if (EPADConfig.getParamValue("MailPassword") == null || EPADConfig.getParamValue("MailPassword").trim().length() == 0) {
			mu = new MailUtil();
		}
		mu.send(user.getEmail(), 
				EPADConfig.xnatServer + "_noreply@stanford.edu", 
				"New password for ePAD@" + EPADConfig.xnatServer, 
				"Hello " + user.getFirstName() + " " + user.getLastName() + ",\n\nYour new ePAD password is " + newPwd + "\n\nPlease login and reset your password.\n\nRegards\n\nePAD Team");
		projectOperations.updateUser("admin", username,
				null, null, null, newPwd, null, null, 
				new ArrayList<String>(), new ArrayList<String>());
	}

	/**
	 * @param file
	 * @return
	 */
	public static boolean isDicomFile(File file)
	{
		//original
		return (file.isFile()
				&& (file.getName().toLowerCase().endsWith(".dcm") || file.getName().toLowerCase().endsWith(".dso") || file.getName().toLowerCase().endsWith(".pres"))
				&& !file.getName().startsWith("."))&& !PixelMedUtils.isDicomSR(file.getAbsolutePath());
		//ml the previous method failed on jpgs 
//		if (file.isFile()
//				&& (file.getName().toLowerCase().endsWith(".dcm") || file.getName().toLowerCase().endsWith(".dso") || file.getName().toLowerCase().endsWith(".pres"))
//				&& !file.getName().startsWith(".")) {
//			DicomObject dicomObject=null;
//			try{
//				dicomObject = DicomReader.getDicomObject(file);
//			}catch(Exception e){
//				log.warning("Dicom object couldn't be retrieved!");
//				return false;
//			}
//			return true;
//			
//		}
//		return false;
		// return file.isFile() && DicomFileUtil.hasMagicWordInHeader(file);
	}

	static SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
	private static Date getDate(String dateStr)
	{
		try
		{
			return dateformat.parse(dateStr);
		}
		catch (Exception x)
		{
			return null;
		}
	}

}
