package edu.stanford.epad.epadws.queries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.common.dicom.DicomFormatUtil;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADDatabaseImage;
import edu.stanford.epad.dtos.EPADDatabaseSeries;
import edu.stanford.epad.dtos.EPADFrame;
import edu.stanford.epad.dtos.EPADFrameList;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.dtos.StudyProcessingStatus;
import edu.stanford.epad.dtos.internal.DCM4CHEESeries;
import edu.stanford.epad.dtos.internal.DCM4CHEESeriesList;
import edu.stanford.epad.dtos.internal.DCM4CHEEStudy;
import edu.stanford.epad.dtos.internal.DCM4CHEEStudyList;
import edu.stanford.epad.dtos.internal.XNATProject;
import edu.stanford.epad.dtos.internal.XNATProjectList;
import edu.stanford.epad.dtos.internal.XNATSubject;
import edu.stanford.epad.dtos.internal.XNATSubjectList;
import edu.stanford.epad.dtos.internal.XNATUserList;
import edu.stanford.epad.epadws.aim.AIMQueries;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.PNGFilesOperations;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.processing.pipeline.task.PatientDataDeleteTask;
import edu.stanford.epad.epadws.processing.pipeline.task.ProjectDataDeleteTask;
import edu.stanford.epad.epadws.processing.pipeline.task.StudyDataDeleteTask;
import edu.stanford.epad.epadws.processing.pipeline.watcher.Dcm4CheeDatabaseWatcher;
import edu.stanford.epad.epadws.xnat.XNATDeletionOperations;

public class DefaultEpadOperations implements EpadOperations
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final DefaultEpadOperations ourInstance = new DefaultEpadOperations();

	private DefaultEpadOperations()
	{
	}

	public static DefaultEpadOperations getInstance()
	{
		return ourInstance;
	}

	@Override
	public EPADProject getProjectDescription(String projectID, String username, String sessionID)
	{
		return null; // TODO
	}

	@Override
	public EPADProjectList getAllProjectDescriptionsForUser(String username, String sessionID,
			EPADSearchFilter searchFilter)
	{
		EPADProjectList epadProjectList = new EPADProjectList();
		XNATProjectList xnatProjectList = XNATQueries.allProjects(sessionID);

		for (XNATProject xnatProject : xnatProjectList.ResultSet.Result) {
			EPADProject epadProject = xnatProject2EPADProject(sessionID, username, xnatProject, searchFilter);

			if (epadProject != null)
				epadProjectList.addEPADProject(epadProject);
		}
		return epadProjectList;
	}

	@Override
	public EPADSubjectList getAllSubjectDescriptionsForProject(String projectID, String username, String sessionID,
			EPADSearchFilter searchFilter)
	{
		EPADSubjectList epadSubjectList = new EPADSubjectList();
		XNATSubjectList xnatSubjectList = XNATQueries.getSubjectsForProject(sessionID, projectID);

		for (XNATSubject xnatSubject : xnatSubjectList.ResultSet.Result) {
			EPADSubject epadSubject = xnatSubject2EPADSubject(sessionID, username, xnatSubject, searchFilter);
			if (epadSubject != null)
				epadSubjectList.addEPADSubject(epadSubject);
		}
		return epadSubjectList;
	}

	@Override
	public EPADSubject getSubjectDescription(SubjectReference subjectReference, String username, String sessionID)
	{
		return null; // TODO
	}

	@Override
	public EPADStudyList getAllStudyDescriptionsForSubject(SubjectReference subjectReference, String username,
			String sessionID, EPADSearchFilter searchFilter)
	{
		EPADStudyList epadStudyList = new EPADStudyList();
		Set<String> studyUIDsInXNAT = XNATQueries.getDICOMStudyUIDsForSubject(sessionID, subjectReference.projectID,
				subjectReference.subjectID);
		DCM4CHEEStudyList dcm4CheeStudyList = Dcm4CheeQueries.getStudies(studyUIDsInXNAT);
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();

		for (DCM4CHEEStudy dcm4CheeStudy : dcm4CheeStudyList.ResultSet.Result) {
			String patientName = dcm4CheeStudy.patientName;
			String studyUID = dcm4CheeStudy.studyUID;
			String insertDate = dcm4CheeStudy.dateAcquired;
			String firstSeriesUID = dcm4CheeStudy.firstSeriesUID;
			String firstSeriesDateAcquired = dcm4CheeStudy.firstSeriesDateAcquired;
			String physicianName = dcm4CheeStudy.physicianName;
			String birthdate = dcm4CheeStudy.birthdate;
			String sex = dcm4CheeStudy.sex;
			String studyDescription = dcm4CheeStudy.studyDescription;
			String studyAccessionNumber = dcm4CheeStudy.studyAccessionNumber;
			Set<String> examTypes = getExamTypesForStudy(studyUID);
			int numberOfSeries = dcm4CheeStudy.seriesCount;
			int numberOfImages = dcm4CheeStudy.imagesCount;
			Set<String> seriesUIDs = dcm4CheeDatabaseOperations.getAllSeriesUIDsInStudy(studyUID);
			StudyProcessingStatus studyProcessingStatus = getStudyProcessingStatus(studyUID);
			int numberOfAnnotations = (seriesUIDs.size() <= 0) ? 0 : AIMQueries.getNumberOfAIMAnnotationsForSeriesSet(
					seriesUIDs, username);

			boolean filter = searchFilter.shouldFilterStudy(subjectReference.subjectID, studyAccessionNumber, examTypes,
					numberOfAnnotations);

			if (!filter) {
				EPADStudy epadStudy = new EPADStudy(subjectReference.projectID, subjectReference.subjectID, patientName,
						studyUID, insertDate, firstSeriesUID, firstSeriesDateAcquired, physicianName, birthdate, sex,
						studyProcessingStatus, examTypes, studyDescription, studyAccessionNumber, numberOfSeries, numberOfImages,
						numberOfAnnotations);
				epadStudyList.addEPADStudy(epadStudy);
			}
		}
		return epadStudyList;
	}

	@Override
	public EPADStudy getStudyDescription(StudyReference studyReference, String username, String sessionID)
	{
		return null; // TODO
	}

	private StudyProcessingStatus getStudyProcessingStatus(String studyUID)
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		boolean seriesNotStarted = false;
		boolean seriesWithNoDICOM = false;
		boolean seriesInPipeline = false;
		boolean seriesWithError = false;

		Set<String> seriesUIDs = dcm4CheeDatabaseOperations.getAllSeriesUIDsInStudy(studyUID);

		for (String seriesUID : seriesUIDs) {
			SeriesProcessingStatus seriesProcessingStatus = epadDatabaseOperations.getSeriesProcessingStatus(seriesUID);
			if (seriesProcessingStatus == null)
				seriesNotStarted = true;
			if (seriesProcessingStatus == SeriesProcessingStatus.NO_DICOM)
				seriesWithNoDICOM = true;
			if (seriesProcessingStatus == SeriesProcessingStatus.ERROR)
				seriesWithError = true;
			if (seriesProcessingStatus == SeriesProcessingStatus.IN_PIPELINE)
				seriesInPipeline = true;
		}

		if (seriesNotStarted)
			return StudyProcessingStatus.STUDY_STATUS_NOT_STARTED;
		if (seriesWithError)
			return StudyProcessingStatus.STUDY_STATUS_ERROR_MISSING_PNG;
		else if (seriesWithNoDICOM)
			return StudyProcessingStatus.STUDY_STATUS_ERROR_MISSING_DICOM;
		else if (seriesInPipeline)
			return StudyProcessingStatus.STUDY_STATUS_PROCESSING;
		else
			return StudyProcessingStatus.STUDY_STATUS_COMPLETED;
	}

	@Override
	public EPADSeries getSeriesDescription(SeriesReference seriesReference, String username, String sessionID)
	{
		return null; // TODO
	}

	@Override
	public EPADSeriesList getAllSeriesDescriptionsForStudy(StudyReference studyReference, String username,
			String sessionID, EPADSearchFilter searchFilter)
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		EPADSeriesList epadSeriesList = new EPADSeriesList();

		DCM4CHEESeriesList dcm4CheeSeriesList = Dcm4CheeQueries.getSeriesInStudy(studyReference.studyUID);
		for (DCM4CHEESeries dcm4CheeSeries : dcm4CheeSeriesList.ResultSet.Result) {
			String seriesUID = dcm4CheeSeries.seriesUID;
			String patientID = dcm4CheeSeries.patientID;
			String patientName = dcm4CheeSeries.patientName;
			String seriesDate = dcm4CheeSeries.seriesDate;
			String seriesDescription = dcm4CheeSeries.seriesDescription;
			String examType = dcm4CheeSeries.examType;
			String bodyPart = dcm4CheeSeries.bodyPart;
			String accessionNumber = dcm4CheeSeries.accessionNumber;
			String institution = dcm4CheeSeries.institution;
			String stationName = dcm4CheeSeries.stationName;
			String department = dcm4CheeSeries.department;
			int numberOfImages = dcm4CheeSeries.imagesInSeries;
			int numberOfSeriesRelatedInstances = dcm4CheeSeries.numberOfSeriesRelatedInstances;
			int numberOfAnnotations = AIMQueries.getNumberOfAIMAnnotationsForSeries(seriesUID, username);
			SeriesProcessingStatus seriesProcessingStatus = epadDatabaseOperations.getSeriesProcessingStatus(seriesUID);
			String createdTime = dcm4CheeSeries.createdTime != null ? dcm4CheeSeries.createdTime.toString() : "";
			boolean filter = searchFilter.shouldFilterSeries(patientID, patientName, examType, accessionNumber,
					numberOfAnnotations);

			if (!filter) {
				EPADSeries epadSeries = new EPADSeries(studyReference.projectID, patientID, patientName,
						studyReference.studyUID, seriesUID, seriesDate, seriesDescription, examType, bodyPart, accessionNumber,
						numberOfImages, numberOfSeriesRelatedInstances, numberOfAnnotations, institution, stationName, department,
						seriesProcessingStatus, createdTime);
				epadSeriesList.addEPADSeries(epadSeries);
			}
		}
		return epadSeriesList;
	}

	@Override
	public EPADImageList getAllImageDescriptionsForSeries(SeriesReference seriesReference, String sessionID,
			EPADSearchFilter searchFilter)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		List<Map<String, String>> imageDescriptions = dcm4CheeDatabaseOperations
				.getImageDescriptions(seriesReference.seriesUID);
		EPADImageList epadImageList = new EPADImageList();

		for (Map<String, String> imageDescription : imageDescriptions) {
			String imageUID = imageDescription.get("sop_iuid");
			String instanceNumberString = imageDescription.get("inst_no");
			int instanceNumber = getInstanceNumber(instanceNumberString, seriesReference.seriesUID, imageUID);
			String sliceLocation = getSliceLocation(imageDescription);
			String imageDate = imageDescription.get("content_datetime");
			String insertDate = imageDescription.get("created_time");

			EPADImage epadImage = new EPADImage(seriesReference.projectID, seriesReference.subjectID,
					seriesReference.studyUID, seriesReference.seriesUID, imageUID, insertDate, imageDate, sliceLocation,
					instanceNumber);
			epadImageList.addImage(epadImage);
		}
		return epadImageList;
	}

	@Override
	public EPADImage getImageDescription(ImageReference imageReference, String sessionID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		Map<String, String> imageDescription = dcm4CheeDatabaseOperations.getImageDescription(imageReference.seriesUID,
				imageReference.imageUID);

		String imageUID = imageDescription.get("sop_iuid");
		String instanceNumberString = imageDescription.get("inst_no");
		int instanceNumber = getInstanceNumber(instanceNumberString, imageReference.seriesUID, imageUID);
		String sliceLocation = getSliceLocation(imageDescription); // entry.get("inst_custom1");
		String imageDate = imageDescription.get("content_datetime");
		String insertDate = imageDescription.get("created_time");

		EPADImage epadImage = new EPADImage(imageReference.projectID, imageReference.subjectID, imageReference.studyUID,
				imageReference.seriesUID, imageUID, insertDate, imageDate, sliceLocation, instanceNumber);

		return epadImage;
	}

	@Override
	public EPADFrameList getAllFrameDescriptionsForImage(ImageReference imageReference, String sessionID,
			EPADSearchFilter searchFilter)
	{
		return new EPADFrameList(); // TODO
	}

	@Override
	public EPADFrame getFrameDescription(FrameReference frameReference, String sessionID)
	{
		return null; // TODO
	}

	@Override
	public Set<String> getExamTypesForSubject(String projectID, String patientID, String sessionID,
			EPADSearchFilter searchFilter)
	{
		Set<String> studyUIDs = XNATQueries.getDICOMStudyUIDsForSubject(sessionID, projectID, patientID);

		Set<String> examTypes = new HashSet<String>();

		for (String studyUID : studyUIDs)
			examTypes.addAll(getExamTypesForStudy(studyUID));

		return examTypes;
	}

	@Override
	public Set<String> getExamTypesForSubject(String patientID)
	{ // TODO Probably could make this a single query to dcm4chee database.
		Set<String> studyUIDs = Dcm4CheeQueries.getStudyUIDsForPatient(patientID);

		Set<String> examTypes = new HashSet<String>();

		for (String studyUID : studyUIDs)
			examTypes.addAll(getExamTypesForStudy(studyUID));

		return examTypes;
	}

	@Override
	public Set<String> getExamTypesForStudy(String studyUID)
	{ // TODO Probably could make this a single query to dcm4chee database.
		DCM4CHEESeriesList dcm4CheeSeriesList = Dcm4CheeQueries.getSeriesInStudy(studyUID);
		Set<String> examTypes = new HashSet<String>();

		for (DCM4CHEESeries dcm4CheeSeries : dcm4CheeSeriesList.ResultSet.Result) {
			examTypes.add(dcm4CheeSeries.examType);
		}
		return examTypes;
	}

	@Override
	public Set<String> getSeriesUIDsForSubject(String projectID, String patientID, String sessionID,
			EPADSearchFilter searchFilter)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		// Set<String> studyUIDs = XNATQueries.dicomStudyUIDsForSubject(sessionID, projectID, patientID);
		Set<String> studyUIDs = dcm4CheeDatabaseOperations.getStudyUIDsForPatient(patientID);
		Set<String> seriesIDs = new HashSet<String>();

		for (String studyUID : studyUIDs) {
			Set<String> seriesIDsForStudy = dcm4CheeDatabaseOperations.getAllSeriesUIDsInStudy(studyUID);
			seriesIDs.addAll(seriesIDsForStudy);
		}
		return seriesIDs;
	}

	/**
	 * Called by {@link Dcm4CheeDatabaseWatcher} to see if new series have been uploaded to DCM4CHEE that ePAD does not
	 * know about.
	 * <p>
	 * We might want to consider getting series from dcm4chee where their upload time (DCM4CHEESeries.createdTime) is
	 * after ePAD's processing time (EPADSeries.createdTime), indicating a repeat upload.
	 */
	@Override
	public List<DCM4CHEESeries> getNewDcm4CheeSeries()
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		List<DCM4CHEESeries> newDcm4CheeSeries = new ArrayList<DCM4CHEESeries>();

		Set<String> allReadyDcm4CheeSeriesUIDs = dcm4CheeDatabaseOperations.getAllReadyDcm4CheeSeriesUIDs();
		Set<String> allEPADSeriesUIDs = epadDatabaseOperations.getAllSeriesUIDsFromEPadDatabase();
		allReadyDcm4CheeSeriesUIDs.removeAll(allEPADSeriesUIDs);

		List<String> newSeriesUIDs = new ArrayList<String>(allReadyDcm4CheeSeriesUIDs);

		for (String seriesUID : newSeriesUIDs) {
			DCM4CHEESeries dcm4CheeSeries = Dcm4CheeQueries.getSeries(seriesUID);
			if (dcm4CheeSeries != null) {
				newDcm4CheeSeries.add(dcm4CheeSeries);
			}
		}
		return newDcm4CheeSeries;
	}

	@Override
	public EPADDatabaseSeries getSeries(String seriesUID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		List<Map<String, String>> imageDescriptions = dcm4CheeDatabaseOperations.getImageDescriptions(seriesUID);
		List<EPADDatabaseImage> epadImageList = new ArrayList<EPADDatabaseImage>();

		for (Map<String, String> imageDescription : imageDescriptions) {
			String imageUID = imageDescription.get("sop_iuid");
			String fileName = createFileNameField(imageUID);
			String instanceNumberString = imageDescription.get("inst_no");
			int instanceNumber = getInstanceNumber(instanceNumberString, seriesUID, imageUID);
			String sliceLocation = getSliceLocation(imageDescription);
			String contentTime = "null"; // TODO Can we find this somewhere?

			EPADDatabaseImage epadImage = new EPADDatabaseImage(fileName, instanceNumber, sliceLocation, contentTime);
			epadImageList.add(epadImage);
		}
		EPADDatabaseSeries epadSeries = new EPADDatabaseSeries(epadImageList);
		return epadSeries;
	}

	@Override
	public List<Map<String, String>> getUnprocessedDicomImageFileDescriptionsForSeries(String seriesUID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();

		List<Map<String, String>> dicomImagesWithoutPNGImageFileDescriptions = new ArrayList<Map<String, String>>();

		try {
			// Get list of DICOM image descriptions from DCM4CHEE database table (pacsdb.files). Each image description is a
			// map with keys: i.sop_iuid, i.inst_no, s.series_iuid, f.filepath, f.file_size.
			List<Map<String, String>> dicomImageFileDescriptions = dcm4CheeDatabaseOperations
					.getImageFileDescriptionsForSeries(seriesUID);

			// Get list of image UIDs in series for images recorded in ePAD database table epaddb.epad_files.
			List<String> seriesImageUIDs = epadDatabaseOperations.getAllImageUIDsInSeries(seriesUID);

			// Make a list of image UIDs that have no entry in ePAD files_table.
			for (Map<String, String> dicomImageFileDescription : dicomImageFileDescriptions) {
				String imageUID = dicomImageFileDescription.get("sop_iuid");

				if (!seriesImageUIDs.contains(imageUID)) {
					dicomImagesWithoutPNGImageFileDescriptions.add(dicomImageFileDescription);
				}
			}
		} catch (Exception e) {
			log.warning("getUnprocessedDICOMImageFileDescriptions had " + e.getMessage(), e);
		}
		return dicomImagesWithoutPNGImageFileDescriptions;
	}

	@Override
	public int projectDelete(String projectID, String sessionID, String username)
	{
		int xnatStatusCode;

		Set<String> subjectIDs = XNATQueries.getSubjectIDsForProject(sessionID, projectID);

		for (String patientID : subjectIDs) {
			log.info("Deleting patient " + patientID + " in project " + projectID);
			xnatStatusCode = XNATDeletionOperations.deleteXNATSubject(projectID, patientID, sessionID);
		}
		xnatStatusCode = XNATDeletionOperations.deleteXNATProject(projectID, sessionID);

		log.info("Scheduling dcm4chee deletion task for project" + projectID + " from user " + username);

		(new Thread(new ProjectDataDeleteTask(projectID))).start();

		return xnatStatusCode;
	}

	@Override
	public int patientDelete(String projectID, String patientID, String sessionID, String username)
	{
		int xnatStatusCode;

		log.info("Scheduling deletion task for patient " + patientID + " in project " + projectID + " from user "
				+ username);

		xnatStatusCode = XNATDeletionOperations.deleteXNATSubject(projectID, patientID, sessionID);

		(new Thread(new PatientDataDeleteTask(projectID, patientID))).start();

		return xnatStatusCode;
	}

	@Override
	public int studyDelete(String projectID, String patientID, String studyUID, String sessionID, String username)
	{
		int xnatStatusCode;

		log.info("Scheduling deletion task for study " + studyUID + " for patient " + patientID + " in project "
				+ projectID + " from user " + username);

		xnatStatusCode = XNATDeletionOperations.deleteXNATDICOMStudy(projectID, patientID, studyUID, sessionID);

		(new Thread(new StudyDataDeleteTask(projectID, patientID, studyUID))).start();

		return xnatStatusCode;
	}

	@Override
	public void deleteStudyFromEPadAndDcm4CheeDatabases(String studyUID)
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();

		// Now delete studies from dcm4chee and ePAD's database; includes deleting PNGs for studies.
		Set<String> seriesUIDs = dcm4CheeDatabaseOperations.getAllSeriesUIDsInStudy(studyUID);
		log.info("Found " + seriesUIDs.size() + " series in study " + studyUID);

		log.info("Deleting study " + studyUID + " from dcm4chee's database");
		Dcm4CheeOperations.deleteStudy(studyUID); // Must run after finding series in DCM4CHEE

		// First delete all series in study from ePAD's database; then delete the study itself.
		// Should not delete until after deleting study in DCM4CHEE or PNG pipeline will activate.
		for (String seriesUID : seriesUIDs) {
			log.info("Deleting series " + seriesUID + " from ePAD database");
			epadDatabaseOperations.deleteSeries(seriesUID);
		}
		log.info("Deleting study " + studyUID + " from ePAD database");
		epadDatabaseOperations.deleteStudy(studyUID);

		// Delete the underlying PNGs for the study
		PNGFilesOperations.deletePNGsForStudy(studyUID);
	}

	@Override
	public void deleteStudiesFromEPadAndDcm4CheeDatabases(Set<String> studyUIDs)
	{
		for (String studyUID : studyUIDs)
			deleteStudyFromEPadAndDcm4CheeDatabases(studyUID);
	}

	@Override
	public EPADAIMList getProjectAIMDescriptions(String projectID, String username, String sessionID)
	{
		return new EPADAIMList(); // TODO
	}

	@Override
	public EPADAIM getProjectAIMDescription(String projectID, String aimID, String username, String sessionID)
	{
		return null; // TODO
	}

	@Override
	public EPADAIMList getSubjectAIMDescriptions(SubjectReference subjectReference, String username, String sessionID)
	{
		return new EPADAIMList(); // TODO
	}

	@Override
	public EPADAIM getSubjectAIMDescription(SubjectReference subjectReference, String aimID, String username,
			String sessionID)
	{
		return null; // TODO
	}

	@Override
	public EPADAIMList getStudyAIMDescriptions(StudyReference studyReference, String username, String sessionID)
	{
		return new EPADAIMList(); // TODO
	}

	@Override
	public EPADAIM getStudyAIMDescription(StudyReference studyReference, String aimID, String username, String sessionID)
	{
		return null; // TODO
	}

	@Override
	public EPADAIMList getSeriesAIMDescriptions(SeriesReference seriesReference, String username, String sessionID)
	{
		return new EPADAIMList(); // TODO
	}

	@Override
	public EPADAIM getSeriesAIMDescription(SeriesReference seriesReference, String aimID, String username,
			String sessionID)
	{
		return null; // TODO
	}

	@Override
	public EPADAIMList getImageAIMDescriptions(ImageReference imageReference, String username, String sessionID)
	{
		return new EPADAIMList(); // TODO
	}

	@Override
	public EPADAIM getImageAIMDescription(ImageReference imageReference, String aimID, String username, String sessionID)
	{
		return null; // TODO
	}

	// TODO
	@Override
	public EPADAIMList getFrameAIMDescriptions(FrameReference frameReference, String username, String sessionID)
	{
		return new EPADAIMList(); // TODO
	}

	// TODO
	@Override
	public EPADAIM getFrameAIMDescription(FrameReference frameReference, String aimID, String username, String sessionID)
	{
		return null; // TODO
	}

	private String createFileNameField(String sopInstanceUID)
	{
		return DicomFormatUtil.formatUidToDir(sopInstanceUID) + ".dcm";
	}

	private int getInstanceNumber(String instanceNumberString, String seriesUID, String imageUID)
	{
		if (instanceNumberString != null)
			try {
				return Integer.parseInt(instanceNumberString);
			} catch (NumberFormatException e) {
				log.warning("Invalid instance number " + instanceNumberString + " in image " + imageUID + " in series "
						+ seriesUID);
				return 1; // Invalid instance number; default to 1
			}
		else
			return 1; // Missing instance number; default to 1.
	}

	private String getSliceLocation(Map<String, String> entry)
	{
		String sliceLoc = entry.get("inst_custom1");
		if (sliceLoc == null)
			return "0.0";
		else
			return sliceLoc;
	}

	private EPADProject xnatProject2EPADProject(String sessionID, String username, XNATProject xnatProject,
			EPADSearchFilter searchFilter)
	{
		String projectName = xnatProject.name;
		if (!searchFilter.shouldFilterProject(projectName)) {
			String projectID = xnatProject.ID;

			String secondaryID = xnatProject.secondary_ID;
			String piLastName = xnatProject.pi_lastname;
			String description = xnatProject.description;
			String piFirstName = xnatProject.pi_firstname;
			String uri = xnatProject.URI;
			Set<String> patientIDs = XNATQueries.getSubjectIDsForProject(sessionID, projectID);
			int numberOfPatients = patientIDs.size();
			int numberOfAnnotations = AIMQueries.getNumberOfAIMAnnotationsForPatients(sessionID, username, patientIDs);

			if (!searchFilter.shouldFilterProject(projectName, numberOfAnnotations)) {
				int numberOfStudies = Dcm4CheeQueries.getNumberOfStudiesForPatients(patientIDs);
				XNATUserList xnatUsers = XNATQueries.getUsersForProject(projectID);
				Set<String> usernames = xnatUsers.getLoginNames();
				// Set<String> usernames = new HashSet<String>();

				return new EPADProject(secondaryID, piLastName, description, projectName, projectID, piFirstName, uri,
						numberOfPatients, numberOfStudies, numberOfAnnotations, patientIDs, usernames);
			} else
				return null;
		} else
			return null;
	}

	private EPADSubject xnatSubject2EPADSubject(String sessionID, String username, XNATSubject xnatSubject,
			EPADSearchFilter searchFilter)
	{
		EpadOperations epadQueries = DefaultEpadOperations.getInstance();

		String patientID = xnatSubject.label;
		String patientName = xnatSubject.src;

		if (!searchFilter.shouldFilterSubject(patientID, patientName)) {
			String projectID = xnatSubject.project;
			String xnatSubjectID = xnatSubject.ID;
			String uri = xnatSubject.URI;
			String insertUser = xnatSubject.insert_user;
			String insertDate = xnatSubject.insert_date;
			int numberOfAnnotations = AIMQueries.getNumberOfAIMAnnotationsForPatient(patientID, username);
			if (!searchFilter.shouldFilterSubject(patientID, patientName, numberOfAnnotations)) {
				// Set<String> examTypes = epadQueries.getExamTypesForPatient(projectID, patientID, sessionID, searchFilter);
				Set<String> examTypes = epadQueries.getExamTypesForSubject(patientID);

				if (!searchFilter.shouldFilterSubject(patientID, patientName, examTypes, numberOfAnnotations)) {
					int numberOfStudies = Dcm4CheeQueries.getNumberOfStudiesForPatient(patientID);

					return new EPADSubject(projectID, patientID, patientName, insertUser, xnatSubjectID, insertDate, uri,
							numberOfStudies, numberOfAnnotations, examTypes);
				} else
					return null;
			} else
				return null;
		} else
			return null;
	}
}
