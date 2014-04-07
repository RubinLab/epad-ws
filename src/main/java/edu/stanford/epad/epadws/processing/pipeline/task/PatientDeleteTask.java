package edu.stanford.epad.epadws.processing.pipeline.task;

import java.util.Set;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.FileOperations;

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
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();

		// We assume patient and associated studies already deleted from XNAT.
		// TODO Need to do this deletion here instead of at the client side!!!

		// Now ask XNAT if we have some of these studies remaining. Need to use admin password to find all!
		// If we have some, don't delete ePAD PNGs or Dcm4Chee DICOMS.
		// public static XNATSessionResponse getXNATSessionID(admin, admin)
		// XNATQueries.dicomExperiments(sessionID);
		// public static XNATSessionResponse deleteXNATSessionID(admin, admin)

		try {
			Set<String> studyUIDs = dcm4CheeDatabaseOperations.getStudyUIDsForPatient(patientID);

			for (String studyID : studyUIDs) {
				Set<String> seriesUIDs = dcm4CheeDatabaseOperations.findAllSeriesUIDsInStudy(studyID);
				logger.info("Found " + seriesUIDs.size() + " series in study " + patientID);

				Dcm4CheeOperations.deleteStudy(studyID); // Must run after finding series in DCM4CHEE

				// Should not delete until after deleting study in DCM4CHEE or PNG pipeline will activate.
				for (String seriesUID : seriesUIDs) {
					logger.info("SeriesUID to delete in ePAD database: " + seriesUID);
					epadDatabaseOperations.deleteSeries(seriesUID);
				}
				epadDatabaseOperations.deleteStudy(studyID);
				FileOperations.deletePNGsForStudy(studyID);
			}
		} catch (Exception e) {
			logger.warning("Patient delete task has error: " + e.getMessage(), e);
		}
	}
}
