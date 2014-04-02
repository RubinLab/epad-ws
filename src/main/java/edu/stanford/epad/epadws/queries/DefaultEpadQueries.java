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
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.dtos.XNATExperiment;
import edu.stanford.epad.dtos.XNATExperimentList;
import edu.stanford.epad.dtos.XNATProject;
import edu.stanford.epad.dtos.XNATProjectList;
import edu.stanford.epad.dtos.XNATSubject;
import edu.stanford.epad.dtos.XNATSubjectList;
import edu.stanford.epad.dtos.XNATUserList;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
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
	public EPADProjectList getAllProjectsForUser(String sessionID, String username)
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
	public EPADSubjectList getAllSubjectsForProject(String sessionID, String projectID)
	{
		EPADSubjectList epadSubjectList = new EPADSubjectList();
		XNATSubjectList xnatSubjectList = XNATQueries.subjectsForProject(sessionID, projectID);
		XNATUserList xnatUsers = XNATQueries.usersForProject(sessionID, projectID);

		for (XNATSubject xnatSubject : xnatSubjectList.ResultSet.Result) {
			EPADSubject epadSubject = xnatSubject2EPADSubject(sessionID, xnatUsers.getLoginNames(), xnatSubject);
			epadSubjectList.addEPADSubject(epadSubject);
		}
		return epadSubjectList;
	}

	@Override
	public EPADStudyList getAllStudiesForSubject(String sessionID, String projectID, String subjectID)
	{
		EPADStudyList epadStudyList = new EPADStudyList();
		XNATUserList xnatUsers = XNATQueries.usersForProject(sessionID, projectID);
		XNATExperimentList xnatExperimentList = XNATQueries.getDICOMExperimentsForProjectAndSubject(sessionID, projectID,
				subjectID);
		DCM4CHEEStudyList dcm4CheeStudyList = Dcm4CheeQueries.studiesForPatient(subjectID);
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		Map<String, DCM4CHEEStudy> dcm4CheeStudyUIDMap = dcm4CheeStudyList.generateStudyUIDMap();

		for (XNATExperiment xnatExperiment : xnatExperimentList.ResultSet.Result) {
			String studyUID = xnatExperiment.label;
			if (dcm4CheeStudyUIDMap.containsKey(studyUID)) {
				DCM4CHEEStudy dcm4CheeStudy = dcm4CheeStudyUIDMap.get(studyUID);
				String insertDate = xnatExperiment.insert_date;
				String date = xnatExperiment.date;
				String uri = xnatExperiment.URI;
				String studyDescription = dcm4CheeStudy.studyDescription;
				String studyAccessionNumber = dcm4CheeStudy.studyAccessionNumber;
				Set<String> examTypes = getExamTypesForStudy(sessionID, projectID, subjectID, studyUID);
				Set<String> seriesUIDs = dcm4CheeDatabaseOperations.findAllSeriesUIDsInStudy(studyUID);
				int numberOfSeries = seriesUIDs.size();
				int numberOfAnnotations = AIMQueries.getNumberOfAIMAnnotationsForSeriesUIDs(seriesUIDs,
						xnatUsers.getLoginNames());

				EPADStudy epadStudy = new EPADStudy(projectID, studyUID, insertDate, date, uri, examTypes, studyDescription,
						studyAccessionNumber, numberOfSeries, numberOfAnnotations);
				epadStudyList.addEPADStudy(epadStudy);
			} else
				log.warning("Found study " + studyUID + " in XNAT with no corresponding DCM4CHEE study; project =" + projectID
						+ ", subjectID =" + subjectID);
		}
		return epadStudyList;

	}

	@Override
	public EPADSeriesList getAllSeriesForStudy(String sessionID, String projectID, String subjectID, String studyUID)
	{
		EPADSeriesList epadSeriesList = new EPADSeriesList();
		XNATUserList xnatUsers = XNATQueries.usersForProject(sessionID, projectID);
		Set<String> usernames = xnatUsers.getLoginNames();
		XNATExperiment xnatExperiment = XNATQueries.getDICOMExperimentForProjectAndSubjectAndStudyUID(sessionID, projectID,
				subjectID, studyUID);

		if (xnatExperiment == null) {
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
		} else
			log.warning("Could not find  study " + studyUID + " in XNAT; project =" + projectID + ", subjectID =" + subjectID);

		return epadSeriesList;
	}

	@Override
	public Set<String> getExamTypesForSubject(String sessionID, String projectID, String subjectID)
	{
		Set<String> studyUIDs = XNATQueries.dicomStudyUIDsForSubject(sessionID, projectID, subjectID);
		Set<String> examTypes = new HashSet<String>();

		for (String studyUID : studyUIDs)
			examTypes.addAll(getExamTypesForStudy(sessionID, projectID, subjectID, studyUID));

		return examTypes;
	}

	@Override
	public Set<String> getExamTypesForStudy(String sessionID, String projectID, String subjectID, String studyUID)
	{
		DCM4CHEESeriesList dcm4CheeSeriesList = Dcm4CheeQueries.getSeriesInStudy(studyUID);
		Set<String> examTypes = new HashSet<String>();

		for (DCM4CHEESeries dcm4CheeSeries : dcm4CheeSeriesList.ResultSet.Result) {
			examTypes.add(dcm4CheeSeries.examType);
		}
		return examTypes;
	}

	@Override
	public Set<String> getSeriesUIDsForSubject(String sessionID, String projectID, String subjectID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		Set<String> studyIDs = XNATQueries.dicomStudyUIDsForSubject(sessionID, projectID, subjectID);
		Set<String> seriesIDs = new HashSet<String>();

		for (String studyID : studyIDs) {
			Set<String> seriesIDsForStudy = dcm4CheeDatabaseOperations.findAllSeriesUIDsInStudy(studyID);
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
		int numberOfSubjects = XNATQueries.numberOfSubjectsForProject(sessionID, xnatProject.ID);
		int numberOfStudies = XNATQueries.numberOfStudiesForProject(sessionID, xnatProject.ID);
		XNATUserList xnatUsers = XNATQueries.usersForProject(sessionID, xnatProject.ID);
		Set<String> usernames = xnatUsers.getLoginNames();
		int numberOfAnnotations = AIMQueries.getNumberOfAIMAnnotationsForProject(sessionID, usernames, xnatProject.ID);

		EPADProject epadProject = new EPADProject(secondaryID, piLastName, description, name, id, piFirstName, uri,
				numberOfSubjects, numberOfStudies, numberOfAnnotations, xnatUsers.getLoginNames());

		return epadProject;
	}

	private EPADSubject xnatSubject2EPADSubject(String sessionID, Set<String> usernames, XNATSubject xnatSubject)
	{
		EpadQueries epadQueries = DefaultEpadQueries.getInstance();

		String project = xnatSubject.project;
		String subjectName = xnatSubject.src;
		String xnatID = xnatSubject.ID;
		String uri = xnatSubject.URI;
		String insertUser = xnatSubject.insert_user;
		String insertDate = xnatSubject.insert_date;
		String subjectID = xnatSubject.label;
		int numberOfStudies = XNATQueries.numberOfStudiesForSubject(sessionID, xnatSubject.project, xnatSubject.ID);
		int numberOfAnnotations = AIMQueries.getNumberOfAIMAnnotationsForPatientID(usernames, xnatSubject.ID);
		Set<String> examTypes = epadQueries.getExamTypesForSubject(sessionID, xnatSubject.project, xnatSubject.ID);
		EPADSubject epadSubject = new EPADSubject(project, subjectName, insertUser, xnatID, insertDate, subjectID, uri,
				numberOfStudies, numberOfAnnotations, examTypes);

		return epadSubject;
	}
}
