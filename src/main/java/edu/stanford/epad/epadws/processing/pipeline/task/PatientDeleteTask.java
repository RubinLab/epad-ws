package edu.stanford.epad.epadws.processing.pipeline.task;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.xnat.XNATDeletionOperations;

/**
 * Delete a patient and all studies for that patient in the ePAD and DCMCHEE databases.
 * 
 * @author martin
 * 
 */
public class PatientDeleteTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();

	private final String sessionID;
	private final String projectID;
	private final String patientID;

	public PatientDeleteTask(String sessionID, String projectID, String patientID)
	{
		this.sessionID = sessionID;
		this.projectID = projectID;
		this.patientID = patientID;
	}

	@Override
	public void run()
	{
		try {
			// Set<String> subjectStudyUIDs = XNATQueries.getDICOMStudyUIDsForSubject(sessionID, projectID, patientID);

			log.info("Deleting patient " + patientID + " in project " + projectID);

			if (XNATDeletionOperations.deleteXNATSubject(projectID, patientID, sessionID)) {
				log.info("Deleted patient " + patientID + " in project " + projectID + " from XNAT");
				// TODO Fix. Seems to delete study even if used elsewhere.
				// EpadOperations epadOperations = DefaultEpadOperations.getInstance();
				// Set<String> allStudyUIDs = XNATQueries.getAllDICOMStudyUIDs();
				// subjectStudyUIDs.removeAll(allStudyUIDs); // Remove studies used elsewhere so that they are not deleted
				// epadOperations.deleteStudiesFromEPadAndDcm4CheeDatabases(subjectStudyUIDs);

			} else
				log.warning("Error deleting patient " + patientID + " in project " + projectID + " from XNAT");

		} catch (Exception e) {
			log.warning("Error deleting patient " + patientID + " in project " + projectID, e);
		}
	}
}
