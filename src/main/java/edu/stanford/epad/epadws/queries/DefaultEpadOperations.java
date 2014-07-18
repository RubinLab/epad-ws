package edu.stanford.epad.epadws.queries;

import ij.ImagePlus;
import ij.io.Opener;
import ij.measure.Calibration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.pixelmed.dicom.SOPClass;

import edu.stanford.epad.common.dicom.DCM4CHEEImageDescription;
import edu.stanford.epad.common.dicom.DCM4CHEEUtil;
import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADDSOFrame;
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
import edu.stanford.epad.dtos.internal.DICOMElement;
import edu.stanford.epad.dtos.internal.DICOMElementList;
import edu.stanford.epad.dtos.internal.XNATExperiment;
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
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.processing.pipeline.task.ProjectDataDeleteTask;
import edu.stanford.epad.epadws.processing.pipeline.task.StudyDataDeleteTask;
import edu.stanford.epad.epadws.processing.pipeline.task.SubjectDataDeleteTask;
import edu.stanford.epad.epadws.processing.pipeline.watcher.Dcm4CheeDatabaseWatcher;
import edu.stanford.epad.epadws.xnat.XNATCreationOperations;
import edu.stanford.epad.epadws.xnat.XNATDeletionOperations;
import edu.stanford.epad.epadws.xnat.XNATUtil;

// TODO Too long - separate in to multiple classes

public class DefaultEpadOperations implements EpadOperations
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final DefaultEpadOperations ourInstance = new DefaultEpadOperations();

	private final EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
	private final Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
			.getDcm4CheeDatabaseOperations();

	private DefaultEpadOperations()
	{
	}

	public static DefaultEpadOperations getInstance()
	{
		return ourInstance;
	}

	/**
	 * Get operations
	 */

	@Override
	public EPADProjectList getProjectDescriptions(String username, String sessionID, EPADSearchFilter searchFilter)
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
	public EPADProject getProjectDescription(ProjectReference projectReference, String username, String sessionID)
	{
		return null; // TODO
	}

	@Override
	public EPADSubjectList getSubjectDescriptions(String projectID, String username, String sessionID,
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
	public EPADStudyList getStudyDescriptions(SubjectReference subjectReference, String username, String sessionID,
			EPADSearchFilter searchFilter)
	{
		EPADStudyList epadStudyList = new EPADStudyList();
		Set<String> studyUIDsInXNAT = XNATQueries.getStudyUIDsForSubject(sessionID, subjectReference.projectID,
				subjectReference.subjectID);
		DCM4CHEEStudyList dcm4CheeStudyList = Dcm4CheeQueries.getStudies(studyUIDsInXNAT);

		for (DCM4CHEEStudy dcm4CheeStudy : dcm4CheeStudyList.ResultSet.Result) {
			EPADStudy epadStudy = dcm4cheeStudy2EpadStudy(subjectReference.projectID, subjectReference.subjectID,
					dcm4CheeStudy, username);

			boolean filter = searchFilter.shouldFilterStudy(subjectReference.subjectID, epadStudy.studyAccessionNumber,
					epadStudy.examTypes, epadStudy.numberOfAnnotations);

			if (!filter)
				epadStudyList.addEPADStudy(epadStudy);
		}
		return epadStudyList;
	}

	@Override
	public EPADStudy getStudyDescription(StudyReference studyReference, String username, String sessionID)
	{
		XNATExperiment xnatExperiment = XNATQueries.getDICOMExperiment(sessionID, studyReference.projectID,
				studyReference.subjectID, studyReference.studyUID);

		if (xnatExperiment != null) {
			log.warning("Count not find XNAT study " + studyReference.studyUID + " for subject " + studyReference.subjectID
					+ " in project " + studyReference.projectID);
			return null;
		} else {
			DCM4CHEEStudy dcm4CheeStudy = Dcm4CheeQueries.getStudy(studyReference.studyUID);
			if (dcm4CheeStudy != null)
				return dcm4cheeStudy2EpadStudy(studyReference.projectID, studyReference.subjectID, dcm4CheeStudy, username);
			else {
				log.warning("Count not find dcm4chee study " + studyReference.studyUID + " for subject "
						+ studyReference.subjectID + " in project " + studyReference.projectID);
				return null;
			}
		}
	}

	@Override
	public EPADSeries getSeriesDescription(SeriesReference seriesReference, String username, String sessionID)
	{
		DCM4CHEESeries dcm4cheeSeries = Dcm4CheeQueries.getSeries(seriesReference.seriesUID);

		if (dcm4cheeSeries != null)
			return dcm4cheeSeries2EpadSeries(seriesReference.projectID, seriesReference.subjectID, dcm4cheeSeries, username);
		else {
			log.warning("Could not find series description for series " + seriesReference.seriesUID);
			return null;
		}
	}

	@Override
	public EPADSeriesList getSeriesDescriptions(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter)
	{
		EPADSeriesList epadSeriesList = new EPADSeriesList();

		DCM4CHEESeriesList dcm4CheeSeriesList = Dcm4CheeQueries.getSeriesInStudy(studyReference.studyUID);
		for (DCM4CHEESeries dcm4CheeSeries : dcm4CheeSeriesList.ResultSet.Result) {
			EPADSeries epadSeries = dcm4cheeSeries2EpadSeries(studyReference.projectID, studyReference.subjectID,
					dcm4CheeSeries, username);
			boolean filter = searchFilter.shouldFilterSeries(epadSeries.patientID, epadSeries.patientName,
					epadSeries.examType, epadSeries.accessionNumber, epadSeries.numberOfAnnotations);

			if (!filter)
				epadSeriesList.addEPADSeries(epadSeries);
		}
		return epadSeriesList;
	}

	@Override
	public EPADImageList getImageDescriptions(SeriesReference seriesReference, String sessionID,
			EPADSearchFilter searchFilter)
	{
		List<DCM4CHEEImageDescription> imageDescriptions = dcm4CheeDatabaseOperations.getImageDescriptions(
				seriesReference.studyUID, seriesReference.seriesUID);
		EPADImageList epadImageList = new EPADImageList();

		boolean isFirst = true;
		for (DCM4CHEEImageDescription dcm4cheeImageDescription : imageDescriptions) {
			if (isFirst) {
				DICOMElementList suppliedDICOMElements = getDICOMElements(dcm4cheeImageDescription.studyUID,
						dcm4cheeImageDescription.seriesUID, dcm4cheeImageDescription.imageUID);
				DICOMElementList defaultDICOMElements = getDefaultDICOMElements(dcm4cheeImageDescription.studyUID,
						dcm4cheeImageDescription.seriesUID, dcm4cheeImageDescription.imageUID, suppliedDICOMElements);

				EPADImage epadImage = createEPADImage(seriesReference, dcm4cheeImageDescription, suppliedDICOMElements,
						defaultDICOMElements);

				epadImageList.addImage(epadImage);
				isFirst = false;
			} else { // We do not add DICOM headers to remaining image descriptions because it would be too expensive
				EPADImage epadImage = createEPADImage(seriesReference, dcm4cheeImageDescription);
				epadImageList.addImage(epadImage);
			}
		}
		return epadImageList;
	}

	@Override
	public EPADImage getImageDescription(ImageReference imageReference, String sessionID)
	{
		DCM4CHEEImageDescription dcm4cheeImageDescription = dcm4CheeDatabaseOperations.getImageDescription(imageReference);
		DICOMElementList suppliedDICOMElements = getDICOMElements(imageReference);
		DICOMElementList defaultDICOMElements = getDefaultDICOMElements(imageReference, suppliedDICOMElements);

		return createEPADImage(imageReference, dcm4cheeImageDescription, suppliedDICOMElements, defaultDICOMElements);
	}

	@Override
	public EPADFrameList getFrameDescriptions(ImageReference imageReference)
	{
		DCM4CHEEImageDescription dcm4cheeImageDescription = dcm4CheeDatabaseOperations.getImageDescription(imageReference);
		List<EPADFrame> frames = new ArrayList<>();

		if (isDSO(dcm4cheeImageDescription)) {
			DICOMElementList suppliedDICOMElements = getDICOMElements(imageReference);
			List<DICOMElement> referencedSOPInstanceUIDDICOMElements = getDICOMElementsByCode(suppliedDICOMElements,
					PixelMedUtils.ReferencedSOPInstanceUIDCode);
			int numberOfFrames = referencedSOPInstanceUIDDICOMElements.size();

			if (numberOfFrames > 0) {
				DICOMElement firstDICOMElement = referencedSOPInstanceUIDDICOMElements.get(0);
				String studyUID = imageReference.studyUID; // DSO will be in same study as original images
				String referencedFirstImageUID = firstDICOMElement.value;
				String referencedSeriesUID = dcm4CheeDatabaseOperations.getSeriesUIDForImage(referencedFirstImageUID);
				DICOMElementList referencedDICOMElements = getDICOMElements(studyUID, referencedSeriesUID,
						referencedFirstImageUID);
				DICOMElementList defaultDICOMElements = getDefaultDICOMElements(imageReference, referencedDICOMElements);

				if (!referencedSeriesUID.equals("")) {
					boolean isFirst = true;
					for (DICOMElement dicomElement : referencedSOPInstanceUIDDICOMElements) {
						String referencedImageUID = dicomElement.value;
						DCM4CHEEImageDescription dcm4cheeReferencedImageDescription = dcm4CheeDatabaseOperations
								.getImageDescription(studyUID, referencedSeriesUID, referencedImageUID);
						String insertDate = dcm4cheeReferencedImageDescription.createdTime;
						String imageDate = dcm4cheeReferencedImageDescription.contentTime;
						String sliceLocation = dcm4cheeReferencedImageDescription.sliceLocation;
						int frameNumber = dcm4cheeReferencedImageDescription.instanceNumber - 1; // Frames 0-based, instances 1
						String losslessImage = getPNGMaskPath(studyUID, imageReference.seriesUID, imageReference.imageUID,
								frameNumber);
						String lossyImage = ""; // We do not have a lossy image for the DSO frame
						String sourceLosslessImage = getPNGPath(studyUID, referencedSeriesUID, referencedImageUID);
						String sourceLossyImage = getWADOPath(studyUID, referencedSeriesUID, referencedImageUID);

						if (isFirst) {
							EPADDSOFrame frame = new EPADDSOFrame(imageReference.projectID, imageReference.subjectID,
									imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, insertDate, imageDate,
									sliceLocation, frameNumber, losslessImage, lossyImage, suppliedDICOMElements, defaultDICOMElements,
									referencedSeriesUID, referencedImageUID, sourceLosslessImage, sourceLossyImage);
							frames.add(frame);
							isFirst = false;
						} else { // We do not add DICOM headers to remaining frame descriptions because it would be too expensive
							EPADDSOFrame frame = new EPADDSOFrame(imageReference.projectID, imageReference.subjectID,
									imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, insertDate, imageDate,
									sliceLocation, frameNumber, losslessImage, lossyImage, new DICOMElementList(),
									new DICOMElementList(), referencedSeriesUID, referencedImageUID, sourceLosslessImage,
									sourceLossyImage);
							frames.add(frame);
						}
					}
					return new EPADFrameList(frames);
				} else {
					log.warning("Could not find original series for DSO image " + imageReference.imageUID + " in series "
							+ imageReference.seriesUID);
				}
			} else {
				log.warning("Could not find frames for DSO image " + imageReference.imageUID + " in series "
						+ imageReference.seriesUID);
			}
		} else {
			log.warning("Attempt to get frames of non multi-frame image " + imageReference.imageUID + " in series "
					+ imageReference.seriesUID);
		}
		return new EPADFrameList();
	}

	private List<DICOMElement> getDICOMElementsByCode(DICOMElementList dicomElementList, String tagCode)
	{
		Set<DICOMElement> matchingDICOMElements = new LinkedHashSet<>(); // Maintain insertion order

		for (DICOMElement dicomElement : dicomElementList.ResultSet.Result) {
			// Do not allow duplicates.
			if (dicomElement.tagCode.equals(tagCode) && !matchingDICOMElements.contains(dicomElement))
				matchingDICOMElements.add(dicomElement);
		}

		return new ArrayList<>(matchingDICOMElements);
	}

	private boolean isDSO(DCM4CHEEImageDescription dcm4cheeImageDescription)
	{
		return dcm4cheeImageDescription.classUID.equals(SOPClass.SegmentationStorage);
	}

	@Override
	public EPADFrame getFrameDescription(FrameReference frameReference, String sessionID)
	{
		return null; // TODO
	}

	/**
	 * Creation operations
	 */

	@Override
	public void createSubjectAndStudy(String projectID, String subjectID, String subjectName, String studyUID,
			String sessionID)
	{
		String xnatSubjectLabel = XNATUtil.subjectID2XNATSubjectLabel(subjectID);
		int xnatStatusCode = XNATCreationOperations.createXNATSubject(projectID, xnatSubjectLabel, subjectName, sessionID);

		if (XNATUtil.unexpectedXNATCreationStatusCode(xnatStatusCode))
			log.warning("Error creating XNAT subject " + subjectName + " for study " + studyUID + "; status code="
					+ xnatStatusCode);

		xnatStatusCode = XNATCreationOperations.createXNATDICOMStudyExperiment(projectID, xnatSubjectLabel, studyUID,
				sessionID);

		if (XNATUtil.unexpectedXNATCreationStatusCode(xnatStatusCode))
			log.warning("Error creating XNAT experiment for study " + studyUID + "; status code=" + xnatStatusCode);
	}

	@Override
	public int createSeries(SeriesReference seriesReference, String sessionID)
	{
		return HttpServletResponse.SC_NOT_IMPLEMENTED; // TODO
	}

	@Override
	public int seriesDelete(SeriesReference seriesReference, String sessionID, String username)
	{
		return HttpServletResponse.SC_NOT_IMPLEMENTED; // TODO
	}

	@Override
	public Set<String> getExamTypesForSubject(String projectID, String patientID, String sessionID,
			EPADSearchFilter searchFilter)
	{
		Set<String> studyUIDs = XNATQueries.getStudyUIDsForSubject(sessionID, projectID, patientID);

		Set<String> examTypes = new HashSet<String>();

		for (String studyUID : studyUIDs)
			examTypes.addAll(getExamTypesForStudy(studyUID));

		return examTypes;
	}

	@Override
	public Set<String> getExamTypesForSubject(String patientID)
	{ // Probably could make this a single query to dcm4chee database.
		Set<String> studyUIDs = Dcm4CheeQueries.getStudyUIDsForPatient(patientID);

		Set<String> examTypes = new HashSet<String>();

		for (String studyUID : studyUIDs)
			examTypes.addAll(getExamTypesForStudy(studyUID));

		return examTypes;
	}

	@Override
	public Set<String> getExamTypesForStudy(String studyUID)
	{ // Probably could make this a single query to dcm4chee database.
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
		List<DCM4CHEESeries> newDcm4CheeSeries = new ArrayList<DCM4CHEESeries>();

		Set<String> allReadyDcm4CheeSeriesUIDs = dcm4CheeDatabaseOperations.getAllReadyDcm4CheeSeriesUIDs();
		Set<String> allEPADSeriesUIDs = epadDatabaseOperations.getAllSeriesUIDsFromEPadDatabase();
		allReadyDcm4CheeSeriesUIDs.removeAll(allEPADSeriesUIDs);

		List<String> newSeriesUIDs = new ArrayList<String>(allReadyDcm4CheeSeriesUIDs);

		for (String seriesUID : newSeriesUIDs) {
			DCM4CHEESeries dcm4CheeSeries = Dcm4CheeQueries.getSeries(seriesUID);
			if (dcm4CheeSeries != null) {
				newDcm4CheeSeries.add(dcm4CheeSeries);
			} else
				log.warning("Could not find new series " + seriesUID + " in dcm4chee");
		}
		return newDcm4CheeSeries;
	}

	@Override
	public Set<DICOMFileDescription> getUnprocessedDICOMFilesInSeries(String seriesUID)
	{
		Set<DICOMFileDescription> dicomFilesWithoutPNGs = new HashSet<DICOMFileDescription>();

		try {
			// Get list of DICOM file descriptions from DCM4CHEE.
			Set<DICOMFileDescription> dicomFileDescriptions = dcm4CheeDatabaseOperations.getDICOMFilesForSeries(seriesUID);

			// Get list of image UIDs in series for images recorded in ePAD database table epaddb.epad_files.
			Set<String> imageUIDs = epadDatabaseOperations.getImageUIDsInSeries(seriesUID);

			// Make a list of image UIDs that have no entry in ePAD files_table.
			for (DICOMFileDescription dicomFileDescription : dicomFileDescriptions) {
				if (!imageUIDs.contains(dicomFileDescription.imageUID))
					dicomFilesWithoutPNGs.add(dicomFileDescription);
			}
		} catch (Exception e) {
			log.warning("Error finding unprocessed file descriptions: " + e.getMessage(), e);
		}
		return dicomFilesWithoutPNGs;
	}

	@Override
	public int createProject(ProjectReference projectReference, String projectName, String projectDescription,
			String sessionID)
	{
		return XNATCreationOperations.createXNATProject(projectReference.projectID, projectName, projectDescription,
				sessionID);
	}

	@Override
	public int createSubject(SubjectReference subjectReference, String subjectName, String sessionID)
	{
		return XNATCreationOperations.createXNATSubject(subjectReference.projectID, subjectReference.subjectID,
				subjectName, sessionID);
	}

	@Override
	public int createStudy(StudyReference studyReference, String sessionID)
	{
		return XNATCreationOperations.createXNATDICOMStudyExperiment(studyReference.projectID, studyReference.subjectID,
				studyReference.studyUID, sessionID);
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
	public int subjectDelete(SubjectReference subjectReference, String sessionID, String username)
	{
		int xnatStatusCode;

		log.info("Scheduling deletion task for patient " + subjectReference.subjectID + " in project "
				+ subjectReference.projectID + " from user " + username);

		xnatStatusCode = XNATDeletionOperations.deleteXNATSubject(subjectReference.projectID, subjectReference.subjectID,
				sessionID);

		(new Thread(new SubjectDataDeleteTask(subjectReference.projectID, subjectReference.subjectID))).start();

		return xnatStatusCode;
	}

	@Override
	public int studyDelete(StudyReference studyReference, String sessionID, String username)
	{
		int xnatStatusCode;

		log.info("Scheduling deletion task for study " + studyReference.studyUID + " for patient "
				+ studyReference.subjectID + " in project " + studyReference.projectID + " from user " + username);

		xnatStatusCode = XNATDeletionOperations.deleteXNATDICOMStudy(studyReference.projectID, studyReference.studyUID,
				studyReference.studyUID, sessionID);

		(new Thread(new StudyDataDeleteTask(studyReference.projectID, studyReference.subjectID, studyReference.studyUID)))
				.start();

		return xnatStatusCode;
	}

	@Override
	public void deleteStudyFromEPadAndDcm4CheeDatabases(String studyUID)
	{
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
	public int createStudyAIM(StudyReference studyReference, String aimID, String sessionID)
	{
		try {
			epadDatabaseOperations.addAIM(studyReference, aimID);
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public int createSeriesAIM(SeriesReference seriesReference, String aimID, String sessionID)
	{
		try {
			epadDatabaseOperations.addAIM(seriesReference, aimID);
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public int createImageAIM(ImageReference imageReference, String aimID, String sessionID)
	{
		try {
			epadDatabaseOperations.addAIM(imageReference, aimID);
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public int createFrameAIM(FrameReference frameReference, String aimID, String sessionID)
	{
		try {
			epadDatabaseOperations.addAIM(frameReference, aimID);
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public int studyAIMDelete(StudyReference studyReference, String aimID, String sessionID, String username)
	{
		try {
			epadDatabaseOperations.addAIM(studyReference, aimID);
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public int seriesAIMDelete(SeriesReference seriesReference, String aimID, String sessionID, String username)
	{
		try {
			epadDatabaseOperations.addAIM(seriesReference, aimID);
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public int imageAIMDelete(ImageReference imageReference, String aimID, String sessionID, String username)
	{
		try {
			epadDatabaseOperations.addAIM(imageReference, aimID);
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public int frameAIMDelete(FrameReference frameReference, String aimID, String sessionID, String username)
	{
		try {
			epadDatabaseOperations.addAIM(frameReference, aimID);
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public EPADAIMList getProjectAIMDescriptions(ProjectReference projectReference, String username, String sessionID)
	{
		Set<EPADAIM> aims = epadDatabaseOperations.getAIMs(projectReference);

		return new EPADAIMList(new ArrayList<EPADAIM>(aims));
	}

	@Override
	public EPADAIMList getSubjectAIMDescriptions(SubjectReference subjectReference, String username, String sessionID)
	{
		Set<EPADAIM> aims = epadDatabaseOperations.getAIMs(subjectReference);

		return new EPADAIMList(new ArrayList<EPADAIM>(aims));
	}

	@Override
	public EPADAIM getProjectAIMDescription(ProjectReference projectReference, String aimID, String username,
			String sessionID)
	{
		return epadDatabaseOperations.getAIM(projectReference, aimID);
	}

	@Override
	public EPADAIM getSubjectAIMDescription(SubjectReference subjectReference, String aimID, String username,
			String sessionID)
	{
		return epadDatabaseOperations.getAIM(subjectReference, aimID);
	}

	@Override
	public EPADAIM getStudyAIMDescription(StudyReference studyReference, String aimID, String username, String sessionID)
	{
		return epadDatabaseOperations.getAIM(studyReference, aimID);
	}

	@Override
	public EPADAIM getSeriesAIMDescription(SeriesReference seriesReference, String aimID, String username,
			String sessionID)
	{
		return epadDatabaseOperations.getAIM(seriesReference, aimID);
	}

	@Override
	public EPADAIM getImageAIMDescription(ImageReference imageReference, String aimID, String username, String sessionID)
	{
		return epadDatabaseOperations.getAIM(imageReference, aimID);
	}

	@Override
	public EPADAIM getFrameAIMDescription(FrameReference frameReference, String aimID, String username, String sessionID)
	{
		return epadDatabaseOperations.getAIM(frameReference, aimID);
	}

	@Override
	public EPADAIMList getStudyAIMDescriptions(StudyReference studyReference, String username, String sessionID)
	{
		Set<EPADAIM> aims = epadDatabaseOperations.getAIMs(studyReference);

		return new EPADAIMList(new ArrayList<EPADAIM>(aims));
	}

	@Override
	public EPADAIMList getSeriesAIMDescriptions(SeriesReference seriesReference, String username, String sessionID)
	{
		Set<EPADAIM> aims = epadDatabaseOperations.getAIMs(seriesReference);

		return new EPADAIMList(new ArrayList<EPADAIM>(aims));
	}

	@Override
	public EPADAIMList getImageAIMDescriptions(ImageReference imageReference, String username, String sessionID)
	{
		Set<EPADAIM> aims = epadDatabaseOperations.getAIMs(imageReference);

		return new EPADAIMList(new ArrayList<EPADAIM>(aims));
	}

	@Override
	public EPADAIMList getFrameAIMDescriptions(FrameReference frameReference, String username, String sessionID)
	{
		Set<EPADAIM> aims = epadDatabaseOperations.getAIMs(frameReference);

		return new EPADAIMList(new ArrayList<EPADAIM>(aims));
	}

	private EPADStudy dcm4cheeStudy2EpadStudy(String suppliedProjectID, String suppliedSubjectID,
			DCM4CHEEStudy dcm4CheeStudy, String username)
	{
		String projectID = suppliedProjectID.equals("") ? EPADConfig.xnatUploadProjectID : suppliedProjectID;
		String patientName = dcm4CheeStudy.patientName;
		String xnatPatientID = XNATUtil.subjectID2XNATSubjectLabel(dcm4CheeStudy.patientID);
		String subjectID = suppliedSubjectID.equals("") ? xnatPatientID : suppliedSubjectID;
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

		return new EPADStudy(projectID, subjectID, patientName, studyUID, insertDate, firstSeriesUID,
				firstSeriesDateAcquired, physicianName, birthdate, sex, studyProcessingStatus, examTypes, studyDescription,
				studyAccessionNumber, numberOfSeries, numberOfImages, numberOfAnnotations);
	}

	private EPADSeries dcm4cheeSeries2EpadSeries(String suppliedProjectID, String suppliedSubjectID,
			DCM4CHEESeries dcm4CheeSeries, String username)
	{
		String projectID = suppliedProjectID.equals("") ? EPADConfig.xnatUploadProjectID : suppliedProjectID;
		String patientName = dcm4CheeSeries.patientName;
		String xnatPatientID = XNATUtil.subjectID2XNATSubjectLabel(dcm4CheeSeries.patientID);
		String subjectID = suppliedSubjectID.equals("") ? xnatPatientID : suppliedSubjectID;
		String studyUID = dcm4CheeSeries.studyUID;
		String seriesUID = dcm4CheeSeries.seriesUID;
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
		String firstImageUIDInSeries = (numberOfSeriesRelatedInstances != 1) ? "" : dcm4CheeDatabaseOperations
				.getFirstImageUIDInSeries(seriesUID);

		int numberOfAnnotations = AIMQueries.getNumberOfAIMAnnotationsForSeries(seriesUID, username);
		SeriesProcessingStatus seriesProcessingStatus = epadDatabaseOperations.getSeriesProcessingStatus(seriesUID);
		String createdTime = dcm4CheeSeries.createdTime != null ? dcm4CheeSeries.createdTime.toString() : "";

		return new EPADSeries(projectID, subjectID, patientName, studyUID, seriesUID, seriesDate, seriesDescription,
				examType, bodyPart, accessionNumber, numberOfImages, numberOfSeriesRelatedInstances, numberOfAnnotations,
				institution, stationName, department, seriesProcessingStatus, createdTime, firstImageUIDInSeries);
	}

	private StudyProcessingStatus getStudyProcessingStatus(String studyUID)
	{
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

	private String getPNGPath(String studyUID, String seriesUID, String imageUID)
	{ // TODO Look at this. Not very robust.
		String pngLocation = epadDatabaseOperations.getPNGLocation(studyUID, seriesUID, imageUID);
		String pngPath = pngLocation.substring(EPADConfig.getEPADWebServerPNGDir().length());

		return pngPath;
	}

	private String getPNGMaskPath(String studyUID, String seriesUID, String imageUID, int frameNumber)
	{
		return "studies/" + studyUID + "/series/" + seriesUID + "/images/" + imageUID + "/masks/" + frameNumber + ".png";
	}

	private String getWADOPath(String studyUID, String seriesUID, String imageUID)
	{
		return "?requestType=WADO&studyUID=" + studyUID + "&seriesUID=" + seriesUID + "&objectUID=" + imageUID;
	}

	private EPADImage createEPADImage(SeriesReference seriesReference, DCM4CHEEImageDescription dcm4cheeImageDescription,
			DICOMElementList dicomElements, DICOMElementList defaultDICOMElements)
	{
		return createEPADImage(seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID,
				seriesReference.seriesUID, dcm4cheeImageDescription.imageUID, dcm4cheeImageDescription, dicomElements,
				defaultDICOMElements);
	}

	private EPADImage createEPADImage(SeriesReference seriesReference, DCM4CHEEImageDescription dcm4cheeImageDescription)
	{
		return createEPADImage(seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID,
				seriesReference.seriesUID, dcm4cheeImageDescription.imageUID, dcm4cheeImageDescription, new DICOMElementList(),
				new DICOMElementList());
	}

	private EPADImage createEPADImage(ImageReference imageReference, DCM4CHEEImageDescription dcm4CheeImageDescription,
			DICOMElementList dicomElements, DICOMElementList defaultDICOMElements)
	{
		return createEPADImage(imageReference.projectID, imageReference.subjectID, imageReference.studyUID,
				imageReference.seriesUID, imageReference.imageUID, dcm4CheeImageDescription, dicomElements,
				defaultDICOMElements);
	}

	private EPADImage createEPADImage(String projectID, String subjectID, String studyUID, String seriesUID,
			String imageUID, DCM4CHEEImageDescription dcm4cheeImageDescription, DICOMElementList dicomElements,
			DICOMElementList defaultDICOMElements)
	{
		String classUID = dcm4cheeImageDescription.classUID;
		int instanceNumber = dcm4cheeImageDescription.instanceNumber;
		String sliceLocation = dcm4cheeImageDescription.sliceLocation;
		String imageDate = dcm4cheeImageDescription.contentTime;
		String insertDate = dcm4cheeImageDescription.createdTime;
		int numberOfFrames = 0; // TODO Look for MF from dcm4chee
		String losslessImage = getPNGPath(studyUID, seriesUID, imageUID);
		String lossyImage = getWADOPath(studyUID, seriesUID, imageUID);
		boolean isDSO = isDSO(dcm4cheeImageDescription);

		return new EPADImage(projectID, subjectID, studyUID, seriesUID, imageUID, classUID, insertDate, imageDate,
				sliceLocation, instanceNumber, losslessImage, lossyImage, dicomElements, defaultDICOMElements, numberOfFrames,
				isDSO);
	}

	private DICOMElementList getDICOMElements(ImageReference imageReference)
	{
		return getDICOMElements(imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID);
	}

	private DICOMElementList getDICOMElements(String studyUID, String seriesUID, String imageUID)
	{
		DICOMElementList dicomElementList = Dcm4CheeQueries.getDICOMElementsFromWADO(studyUID, seriesUID, imageUID);

		if (dicomElementList == null)
			log.warning("Could not get DICOM header for image " + imageUID + " in series " + seriesUID);

		return dicomElementList;
	}

	private DICOMElementList getDefaultDICOMElements(ImageReference imageReference, DICOMElementList suppliedDICOMElements)
	{
		return getDefaultDICOMElements(imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID,
				suppliedDICOMElements);
	}

	private DICOMElementList getDefaultDICOMElements(String studyUID, String seriesUID, String imageUID,
			DICOMElementList suppliedDicomElements)
	{
		List<DICOMElement> defaultDicomElements = new ArrayList<>();
		Map<String, List<DICOMElement>> suppliedDICOMElementMap = generateDICOMElementMap(suppliedDicomElements);

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.PatientNameCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.PatientNameCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.PatientNameCode, PixelMedUtils.PatientNameTagName, ""));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.ModalityCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.ModalityCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.ModalityCode, PixelMedUtils.ModalityTagName, ""));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.SeriesDescriptionCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.SeriesDescriptionCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.SeriesDescriptionCode,
					PixelMedUtils.SeriesDescriptionTagName, ""));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.PatientBirthDateCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.PatientBirthDateCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.PatientBirthDateCode,
					PixelMedUtils.PatientBirthDateTagName, "1900-01-01T00:00:00"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.SliceThicknessCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.SliceThicknessCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.SliceLocationCode, PixelMedUtils.SliceThicknessTagName,
					"0"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.SliceLocationCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.SliceLocationCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.SliceLocationCode, PixelMedUtils.SliceLocationTagName,
					"0"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.PatientSexCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.PatientSexCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.PatientSexCode, PixelMedUtils.PatientSexTagName, ""));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.ManufacturerCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.ManufacturerCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.ManufacturerCode, PixelMedUtils.ManufacturerTagName, ""));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.ModelNameCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.ModelNameCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.ModelNameCode, PixelMedUtils.ModelNameTagName, ""));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.SoftwareVersionCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.SoftwareVersionCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.SoftwareVersionCode,
					PixelMedUtils.SoftwareVersionTagName, ""));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.PixelSpacingCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.PixelSpacingCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.PixelSpacingCode, PixelMedUtils.PixelSpacingTagName,
					"1\\1"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.RescaleInterceptCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.RescaleInterceptCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.RescaleInterceptCode,
					PixelMedUtils.RescaleInterceptTagName, "0"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.RescaleSlopeCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.RescaleSlopeCode).get(0));
		else
			defaultDicomElements
					.add(new DICOMElement(PixelMedUtils.RescaleSlopeCode, PixelMedUtils.RescaleSlopeTagName, "1"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.StudyDateCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.StudyDateCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.StudyDateCode, PixelMedUtils.StudyDateTagName,
					"1900-01-01T00:00:00"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.StudyTimeCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.StudyTimeCode).get(0));
		else
			defaultDicomElements
					.add(new DICOMElement(PixelMedUtils.StudyTimeCode, PixelMedUtils.StudyTimeTagName, "00:00:00"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.RowsCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.RowsCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.RowsCode, PixelMedUtils.RowsTagName, "512"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.ColumnsCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.ColumnsCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.ColumnsCode, PixelMedUtils.ColumnsTagName, "512"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.BitsStoredCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.BitsStoredCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.BitsStoredCode, PixelMedUtils.BitsStoredTagName, "16"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.PixelRepresentationCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.PixelRepresentationCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.PixelRepresentationCode,
					PixelMedUtils.PixelRepresentationTagName, "0"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.WindowWidthCode)
				&& suppliedDICOMElementMap.containsKey(PixelMedUtils.WindowCenterCode)) {
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.WindowWidthCode).get(0));
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.WindowCenterCode).get(0));
		} else
			defaultDicomElements.addAll(getCalculatedWindowingDICOMElements(studyUID, seriesUID, imageUID));

		return new DICOMElementList(defaultDicomElements);
	}

	private Map<String, List<DICOMElement>> generateDICOMElementMap(DICOMElementList dicomElementList)
	{
		Map<String, List<DICOMElement>> result = new HashMap<>();

		for (DICOMElement dicomElement : dicomElementList.ResultSet.Result) {
			if (result.get(dicomElement.tagCode) != null)
				result.get(dicomElement.tagCode).add(dicomElement);
			else {
				List<DICOMElement> dicomElements = new ArrayList<>();
				dicomElements.add(dicomElement);
				result.put(dicomElement.tagCode, dicomElements);
			}
		}
		return result;
	}

	private List<DICOMElement> getCalculatedWindowingDICOMElements(String studyUID, String seriesUID, String imageUID)
	{
		List<DICOMElement> dicomElements = new ArrayList<>();
		long windowWidth = 1;
		long windowCenter = 0;

		try {
			File temporaryDicomFile = File.createTempFile(imageUID, ".dcm");
			DCM4CHEEUtil.downloadDICOMFileFromWADO(studyUID, seriesUID, imageUID, temporaryDicomFile);

			String dicomImageFilePath = temporaryDicomFile.getAbsolutePath();
			Opener opener = new Opener();
			ImagePlus image = opener.openImage(dicomImageFilePath);

			if (image != null) {
				double min = image.getDisplayRangeMin();
				double max = image.getDisplayRangeMax();
				Calibration cal = image.getCalibration();
				double minValue = cal.getCValue(min);
				double maxValue = cal.getCValue(max);
				windowWidth = Math.round(maxValue - minValue);
				windowCenter = Math.round(minValue + windowWidth / 2.0);

				log.info("Image " + imageUID + " in series " + seriesUID + " has a calculated window width of " + windowWidth
						+ " and window center of " + windowCenter);
			} else {
				log.warning("ImageJ failed to load DICOM file for image " + imageUID + " in series " + seriesUID
						+ " to calculate windowing");
			}
		} catch (IOException e) {
			log.warning("Error getting DICOM file from dcm4chee for image " + imageUID + " in series " + seriesUID, e);
		}

		dicomElements.add(new DICOMElement(PixelMedUtils.WindowWidthCode, PixelMedUtils.WindowWidthTagName, ""
				+ windowWidth));
		dicomElements.add(new DICOMElement(PixelMedUtils.WindowCenterCode, PixelMedUtils.WindowCenterTagName, ""
				+ windowCenter));

		return dicomElements;
	}

	// This is Pixelmed variant (though does not seem to be correct).
	// SourceImage srcDicomImage = new SourceImage(temporaryDicomFile.getAbsolutePath());
	// ImageEnhancer imageEnhancer = new ImageEnhancer(srcDicomImage);
	// imageEnhancer.findVisuParametersImage();
	// windowWidth = Math.round(imageEnhancer.getWindowWidth());
	// windowCenter = Math.round(imageEnhancer.getWindowCenter());

}
