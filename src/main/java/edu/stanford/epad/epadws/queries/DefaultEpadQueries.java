package edu.stanford.epad.epadws.queries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.common.dicom.DicomFormatUtil;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.DCM4CHEESeries;
import edu.stanford.epad.dtos.DCM4CHEESeriesList;
import edu.stanford.epad.dtos.DCM4CHEEStudy;
import edu.stanford.epad.dtos.DCM4CHEEStudyList;
import edu.stanford.epad.dtos.EPADDatabaseImage;
import edu.stanford.epad.dtos.EPADDatabaseSeries;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.dtos.XNATProject;
import edu.stanford.epad.dtos.XNATProjectList;
import edu.stanford.epad.dtos.XNATSubject;
import edu.stanford.epad.dtos.XNATSubjectList;
import edu.stanford.epad.dtos.XNATUserList;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.search.EPADSearchFilter;
import edu.stanford.epad.epadws.processing.pipeline.watcher.Dcm4CheeDatabaseWatcher;

public class DefaultEpadQueries implements EpadQueries
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final DefaultEpadQueries ourInstance = new DefaultEpadQueries();

	private DefaultEpadQueries()
	{
	}

	public static DefaultEpadQueries getInstance()
	{
		return ourInstance;
	}

	@Override
	public EPADProjectList getAllProjectsForUser(String sessionID, String username, EPADSearchFilter searchFilter)
	{
		EPADProjectList epadProjectList = new EPADProjectList();
		XNATProjectList xnatProjectList = XNATQueries.allProjects(sessionID);

		for (XNATProject xnatProject : xnatProjectList.ResultSet.Result) {
			EPADProject epadProject = xnatProject2EPADProject(sessionID, username, xnatProject);
			epadProjectList.addEPADProject(epadProject);
		}
		return epadProjectList;
	}

	@Override
	public EPADSubjectList getAllSubjectsForProject(String sessionID, String projectID, EPADSearchFilter searchFilter)
	{
		EPADSubjectList epadSubjectList = new EPADSubjectList();
		XNATSubjectList xnatSubjectList = XNATQueries.subjectsForProject(sessionID, projectID);
		XNATUserList xnatUsers = XNATQueries.usersForProject(sessionID, projectID);

		for (XNATSubject xnatSubject : xnatSubjectList.ResultSet.Result) {
			if (!XNATQueries.filterSubject(xnatSubject, searchFilter)) {
				EPADSubject epadSubject = xnatSubject2EPADSubject(sessionID, xnatUsers.getLoginNames(), xnatSubject,
						searchFilter);
				epadSubjectList.addEPADSubject(epadSubject);
			}
		}
		return epadSubjectList;
	}

	@Override
	public EPADStudyList getAllStudiesForPatient(String sessionID, String projectID, String patientID,
			EPADSearchFilter searchFilter)
	{
		EPADStudyList epadStudyList = new EPADStudyList();
		XNATUserList xnatUsers = XNATQueries.usersForProject(sessionID, projectID);
		// TODO Need to restrict to only studies that are in XNAT - not just return all dcm4chee studies for this patient.
		// Set<String> studyUIDsInXNAT = XNATQueries.studyUIDsForSubject(sessionID, projectID, patientID);
		DCM4CHEEStudyList dcm4CheeStudyList = Dcm4CheeQueries.studiesForPatient(patientID);
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();

		for (DCM4CHEEStudy dcm4CheeStudy : dcm4CheeStudyList.ResultSet.Result) {
			String studyUID = dcm4CheeStudy.studyUID;
			String insertDate = dcm4CheeStudy.dateAcquired;
			String firstSeriesUID = dcm4CheeStudy.firstSeriesUID;
			String firstSeriesDateAcquired = dcm4CheeStudy.firstSeriesDateAcquired;
			String physicianName = dcm4CheeStudy.physicianName;
			String birthdate = dcm4CheeStudy.birthdate;
			String sex = dcm4CheeStudy.sex;
			int studyStatus = dcm4CheeStudy.studyStatus;
			String studyDescription = dcm4CheeStudy.studyDescription;
			String studyAccessionNumber = dcm4CheeStudy.studyAccessionNumber;
			Set<String> examTypes = getExamTypesForStudy(sessionID, projectID, patientID, studyUID, searchFilter);
			int numberOfSeries = dcm4CheeStudy.seriesCount;
			int numberOfImages = dcm4CheeStudy.imagesCount;
			Set<String> seriesUIDs = dcm4CheeDatabaseOperations.findAllSeriesUIDsInStudy(studyUID);
			int numberOfAnnotations = (seriesUIDs.size() <= 0) ? 0 : AIMQueries.getNumberOfAIMAnnotationsForSeriesUIDs(
					seriesUIDs, xnatUsers.getLoginNames());

			EPADStudy epadStudy = new EPADStudy(projectID, studyUID, insertDate, firstSeriesUID, firstSeriesDateAcquired,
					physicianName, birthdate, sex, studyStatus, examTypes, studyDescription, studyAccessionNumber,
					numberOfSeries, numberOfImages, numberOfAnnotations);
			epadStudyList.addEPADStudy(epadStudy);
		}
		return epadStudyList;
	}

	@Override
	public EPADSeriesList getAllSeriesForStudy(String sessionID, String projectID, String subjectID, String studyUID,
			EPADSearchFilter searchFilter)
	{
		EPADSeriesList epadSeriesList = new EPADSeriesList();
		XNATUserList xnatUsers = XNATQueries.usersForProject(sessionID, projectID);
		Set<String> usernames = xnatUsers.getLoginNames();

		DCM4CHEESeriesList dcm4CheeSeriesList = Dcm4CheeQueries.getSeriesInStudy(studyUID);
		for (DCM4CHEESeries dcm4CheeSeries : dcm4CheeSeriesList.ResultSet.Result) {
			String seriesUID = dcm4CheeSeries.seriesUID;
			String patientID = dcm4CheeSeries.patientID;
			String patientName = dcm4CheeSeries.patientName;
			String seriesDate = dcm4CheeSeries.seriesDate;
			String seriesDescription = dcm4CheeSeries.seriesDate;
			String examType = dcm4CheeSeries.examType;
			String bodyPart = dcm4CheeSeries.bodyPart;
			int numberOfImages = dcm4CheeSeries.imagesInSeries;
			int numberOfAnnotations = AIMQueries.getNumberOfAIMAnnotationsForSeriesUID(seriesUID, usernames);

			EPADSeries epadSeries = new EPADSeries(studyUID, seriesUID, patientID, patientName, seriesDate,
					seriesDescription, examType, bodyPart, numberOfImages, numberOfAnnotations);
			epadSeriesList.addEPADSeries(epadSeries);
		}

		return epadSeriesList;
	}

	@Override
	public EPADImageList getAllImagesForSeries(String sessionID, String projectID, String subjectUID, String studyUID,
			String seriesUID, EPADSearchFilter searchFilter)
	{
		EPADImageList epadImageList = new EPADImageList();
		// XNATUserList xnatUsers = XNATQueries.usersForProject(sessionID, projectID);
		// Set<String> usernames = xnatUsers.getLoginNames();

		// TODO Need to add some call to Dcm4CheeDatabaseOperations to get image information
		// DCM4CHEEImageList dcm4CheeImageList = Dcm4CheeQueries.getImages(seriesUID);
		// Loop through images
		// String imageUID = dcm4CheeSeries.
		// String insertDate =
		// String imageDate =
		// int sliceLocation =
		// int instanceNumber =
		//
		// EPADImage epadImage = new EPADImage(imageUID, seriesUID, studyUID, subjectUID, insertDate, imageDate,
		// sliceLocation, instanceNumber);
		//
		// epadImageList.addEPADImage(epadImage);

		return epadImageList;
	}

	@Override
	public Set<String> getExamTypesForPatient(String sessionID, String projectID, String patientID,
			EPADSearchFilter searchFilter)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		// Set<String> studyUIDs = XNATQueries.dicomStudyUIDsForSubject(sessionID, projectID, subjectID);
		Set<String> studyUIDs = dcm4CheeDatabaseOperations.getStudyUIDsForPatient(patientID);

		Set<String> examTypes = new HashSet<String>();

		for (String studyUID : studyUIDs)
			examTypes.addAll(getExamTypesForStudy(sessionID, projectID, patientID, studyUID, searchFilter));

		return examTypes;
	}

	@Override
	public Set<String> getExamTypesForStudy(String sessionID, String projectID, String subjectID, String studyUID,
			EPADSearchFilter searchFilter)
	{
		DCM4CHEESeriesList dcm4CheeSeriesList = Dcm4CheeQueries.getSeriesInStudy(studyUID);
		Set<String> examTypes = new HashSet<String>();

		for (DCM4CHEESeries dcm4CheeSeries : dcm4CheeSeriesList.ResultSet.Result) {
			examTypes.add(dcm4CheeSeries.examType);
		}
		return examTypes;
	}

	@Override
	public Set<String> getSeriesUIDsForPatient(String sessionID, String projectID, String patientID,
			EPADSearchFilter searchFilter)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		// Set<String> studyUIDs = XNATQueries.dicomStudyUIDsForSubject(sessionID, projectID, patientID);
		Set<String> studyUIDs = dcm4CheeDatabaseOperations.getStudyUIDsForPatient(patientID);
		Set<String> seriesIDs = new HashSet<String>();

		for (String studyUID : studyUIDs) {
			Set<String> seriesIDsForStudy = dcm4CheeDatabaseOperations.findAllSeriesUIDsInStudy(studyUID);
			seriesIDs.addAll(seriesIDsForStudy);
		}
		return seriesIDs;
	}

	/**
	 * Called by {@link Dcm4CheeDatabaseWatcher} to see if new series have been uploaded to DCM4CHEE that ePAD does not
	 * know about.
	 */
	@Override
	public List<DCM4CHEESeries> getNewDcm4CheeSeries()
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		List<DCM4CHEESeries> dcm4CheeSeriesList = new ArrayList<DCM4CHEESeries>();

		Set<String> dcm4CheeSeriesUIDs = dcm4CheeDatabaseOperations.getNewDcm4CheeSeriesUIDs();
		Set<String> epadSeriesUIDs = epadDatabaseOperations.getAllSeriesUIDsFromEPadDatabase();
		dcm4CheeSeriesUIDs.removeAll(epadSeriesUIDs);

		// logger.info("There " + pacsSet.size() + " studies in DCM4CHEE database and " + epadSet.size()
		// + " in the ePAD database");

		List<String> seriesUIDList = new ArrayList<String>(dcm4CheeSeriesUIDs);

		for (String seriesUID : seriesUIDList) {
			DCM4CHEESeries dcm4CheeSeries = Dcm4CheeQueries.getSeriesWithUID(seriesUID);
			if (dcm4CheeSeries != null) {
				dcm4CheeSeriesList.add(dcm4CheeSeries);
			}
		}
		return dcm4CheeSeriesList;
	}

	@Override
	public EPADDatabaseSeries getSeries(String seriesIUID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		List<Map<String, String>> orderQueryEntries = dcm4CheeDatabaseOperations.getSeriesOrder(seriesIUID);
		List<EPADDatabaseImage> epadImageList = new ArrayList<EPADDatabaseImage>();

		for (Map<String, String> entry : orderQueryEntries) {
			String imageUID = entry.get("sop_iuid");
			String fileName = createFileNameField(imageUID);
			String instanceNumberString = entry.get("inst_no");
			int instanceNumber = getInstanceNumber(instanceNumberString, seriesIUID, imageUID);
			String sliceLocation = createSliceLocation(entry); // entry.get("inst_custom1");
			String contentTime = "null"; // TODO Can we find this somewhere?

			EPADDatabaseImage epadImage = new EPADDatabaseImage(fileName, instanceNumber, sliceLocation, contentTime);
			epadImageList.add(epadImage);
		}
		EPADDatabaseSeries epadSeries = new EPADDatabaseSeries(epadImageList);
		return epadSeries;
	}

	@Override
	public List<Map<String, String>> getUnprocessedDicomImageFileDescriptionsForSeries(String seriesIUID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();

		List<Map<String, String>> dicomFilesWithoutPNGImagesFileDescriptions = new ArrayList<Map<String, String>>();

		try {
			// Get list of DICOM image descriptions from DCM4CHEE database table (pacsdb.files). Each image description is a
			// map with keys: i.sop_iuid, i.inst_no, s.series_iuid, f.filepath, f.file_size.
			List<Map<String, String>> dicomImageFileDescriptions = dcm4CheeDatabaseOperations
					.getDicomImageFileDescriptionsForSeries(seriesIUID);

			// Get list of instance IDs for images in series from ePAD database table (epaddb.epad_files).
			List<String> finishedDICOMImageInstanceIDs = epadDatabaseOperations
					.getFinishedDICOMImageInstanceUIDsForSeriesFromEPadDatabase(seriesIUID);

			// logger.info("Found " + dicomImageFileDescriptions.size() + " unprocessed DICOM image(s) with files and "
			// + finishedDICOMImageInstanceIDs.size() + " processed image(s) for series " + shortenSting(seriesIUID));

			for (Map<String, String> dicomImageFileDescription : dicomImageFileDescriptions) {
				String sopIdWithFile = dicomImageFileDescription.get("sop_iuid");

				if (!finishedDICOMImageInstanceIDs.contains(sopIdWithFile)) {
					dicomFilesWithoutPNGImagesFileDescriptions.add(dicomImageFileDescription);
				}
			}
		} catch (Exception e) {
			log.warning("getUnprocessedDICOMImageFileDescriptions had " + e.getMessage(), e);
		}
		return dicomFilesWithoutPNGImagesFileDescriptions;
	}

	private String createFileNameField(String sopInstanceUID)
	{
		return DicomFormatUtil.formatUidToDir(sopInstanceUID) + ".dcm";
	}

	private int getInstanceNumber(String instanceNumberString, String seriesIUID, String imageUID)
	{
		if (instanceNumberString != null)
			try {
				return Integer.parseInt(instanceNumberString);
			} catch (NumberFormatException e) {
				log.warning("Invalid instance number " + instanceNumberString + " in image " + imageUID + " in series "
						+ seriesIUID);
				return 1; // Invalid instance number; default to 1
			}
		else
			return 1; // Missing instance number; default to 1.
	}

	private String createSliceLocation(Map<String, String> entry)
	{
		String sliceLoc = entry.get("inst_custom1");
		if (sliceLoc == null)
			return "0.0";
		else
			return sliceLoc;
	}

	private EPADProject xnatProject2EPADProject(String sessionID, String username, XNATProject xnatProject)
	{
		String secondaryID = xnatProject.secondary_ID;
		String piLastName = xnatProject.pi_lastname;
		String description = xnatProject.description;
		String name = xnatProject.name;
		String id = xnatProject.ID;
		String piFirstName = xnatProject.pi_firstname;
		String uri = xnatProject.URI;
		Set<String> patientIDs = XNATQueries.patientIDsForProject(sessionID, xnatProject.ID);
		int numberOfPatients = patientIDs.size();
		log.info("numberOfPatients " + numberOfPatients);
		int numberOfStudies = Dcm4CheeQueries.getNumberOfStudiesForPatients(patientIDs);
		log.info("numberOfStudies " + numberOfStudies);
		XNATUserList xnatUsers = XNATQueries.usersForProject(sessionID, xnatProject.ID);
		Set<String> usernames = xnatUsers.getLoginNames();
		log.info("usernames " + usernames);
		int numberOfAnnotations = AIMQueries.getNumberOfAIMAnnotationsForPatients(sessionID, usernames, patientIDs);
		log.info("numberOfAnnotations " + numberOfAnnotations);

		EPADProject epadProject = new EPADProject(secondaryID, piLastName, description, name, id, piFirstName, uri,
				numberOfPatients, numberOfStudies, numberOfAnnotations, patientIDs, xnatUsers.getLoginNames());

		return epadProject;
	}

	private EPADSubject xnatSubject2EPADSubject(String sessionID, Set<String> usernames, XNATSubject xnatSubject,
			EPADSearchFilter searchFilter)
	{
		EpadQueries epadQueries = DefaultEpadQueries.getInstance();

		String patientName = xnatSubject.src;
		String project = xnatSubject.project;
		String xnatID = xnatSubject.ID;
		String uri = xnatSubject.URI;
		String insertUser = xnatSubject.insert_user;
		String insertDate = xnatSubject.insert_date;
		String patientID = xnatSubject.label;
		int numberOfStudies = Dcm4CheeQueries.getNumberOfStudiesForPatient(patientID);
		int numberOfAnnotations = AIMQueries.getNumberOfAIMAnnotationsForPatient(usernames, patientID);
		Set<String> examTypes = epadQueries.getExamTypesForPatient(sessionID, xnatSubject.project, xnatSubject.ID,
				searchFilter);
		EPADSubject epadSubject = new EPADSubject(project, patientName, insertUser, xnatID, insertDate, patientID, uri,
				numberOfStudies, numberOfAnnotations, examTypes);

		return epadSubject;
	}
}
