package edu.stanford.epad.epadws.processing.pipeline.task;

import edu.stanford.epad.common.util.EPADLogger;

/**
 * Delete a patient and all studies for that patient in the ePAD and DCMCHEE databases.
 * 
 * @author martin
 * 
 */
public class PatientDataDeleteTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();

	private final String projectID;
	private final String patientID;

	public PatientDataDeleteTask(String projectID, String patientID)
	{
		this.projectID = projectID;
		this.patientID = patientID;
	}

	@Override
	public void run()
	{
		try {
			// TODO Fix. Seems to delete study even if used elsewhere.
			// Set<String> subjectStudyUIDs = XNATQueries.getDICOMStudyUIDsForSubject(sessionID, projectID, patientID);
			// EpadOperations epadOperations = DefaultEpadOperations.getInstance();
			// Set<String> allStudyUIDs = XNATQueries.getAllDICOMStudyUIDs();
			// subjectStudyUIDs.removeAll(allStudyUIDs); // Remove studies used elsewhere so that they are not deleted
			// epadOperations.deleteStudiesFromEPadAndDcm4CheeDatabases(subjectStudyUIDs);
		} catch (Exception e) {
			log.warning("Error deleting patient " + patientID + " in project " + projectID, e);
		}
	}
}
