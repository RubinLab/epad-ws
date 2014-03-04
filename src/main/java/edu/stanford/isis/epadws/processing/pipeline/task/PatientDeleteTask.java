package edu.stanford.isis.epadws.processing.pipeline.task;

import java.util.List;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.dcm4chee.Dcm4CheeOperations;
import edu.stanford.isis.epadws.epaddb.EpadDatabase;
import edu.stanford.isis.epadws.epaddb.FileOperations;
import edu.stanford.isis.epadws.queries.EpadQueries;

/**
 * Delete a patient and all studies for that patient in the ePAD and DCMCHEE databases.
 * 
 * @author martin
 * 
 */
public class PatientDeleteTask implements Runnable
{
	private static EPADLogger logger = EPADLogger.getInstance();

	// private final String projectID;
	private final String patientID;

	public PatientDeleteTask(String projectID, String patientID)
	{
		// this.projectID = projectID;
		this.patientID = patientID;
	}

	@Override
	public void run()
	{
		EpadQueries databaseOperations = EpadDatabase.getInstance().getDatabaseOperations();

		// We assume patient and associated studies already deleted from XNAT.
		// TODO Need to do this deletion here instead of at the client side!!!

		// Now ask XNAT if we have some of these studies remaining. Need to use admin password to find all!
		// If we have some, don't delete ePAD PNGs or Dcm4Chee DICOMS.
		// public static XNATSessionResponse getXNATSessionID(admin, admin)
		// XNATQueries.dicomExperiments(sessionID);
		// public static XNATSessionResponse deleteXNATSessionID(admin, admin)

		try {
			List<String> studyUIDs = databaseOperations.getDicomStudyUIDsForPatient(patientID);

			for (String studyID : studyUIDs) {
				List<String> seriesUIDs = databaseOperations.findAllSeriesUIDsInStudy(studyID);
				logger.info("Found " + seriesUIDs.size() + " series in study " + patientID);

				Dcm4CheeOperations.deleteDicomStudy(studyID); // Must run after finding series in DCM4CHEE

				// Should not delete until after deleting study in DCM4CHEE or PNG pipeline will activate.
				for (String seriesUID : seriesUIDs) {
					logger.info("SeriesUID to delete in ePAD database: " + seriesUID);
					databaseOperations.deleteDicomSeries(seriesUID);
				}
				databaseOperations.deleteDicomStudy(studyID);
				FileOperations.deletePNGsforDicomStudy(studyID);
			}
		} catch (Exception e) {
			logger.warning("Patient delete task has error: " + e.getMessage(), e);
		}
	}
}
