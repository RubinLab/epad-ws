package edu.stanford.epad.epadws.processing.pipeline.task;

import edu.stanford.epad.common.util.EPADLogger;

/**
 * 
 * @author martin
 * 
 */
public class StudyDataDeleteTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();

	private final String projectID;
	private final String patientID;
	private final String studyUID;

	public StudyDataDeleteTask(String projectID, String patientID, String studyUID)
	{
		this.projectID = projectID;
		this.patientID = patientID;
		this.studyUID = studyUID;
	}

	@Override
	public void run()
	{
		// EpadOperations epadOperations = DefaultEpadOperations.getInstance();

		try {
			// TODO Fix. Seems to delete study even if used elsewhere.
			// Set<String> allStudyUIDs = XNATQueries.getAllDICOMStudyUIDs();
			// if (!allStudyUIDs.contains(studyUID))
			// epadOperations.deleteStudyFromEPadAndDcm4CheeDatabases(studyUID);
			// else
			// log.info("Study " + studyUID + " in use by other projects or subjects so will not be deleted from DCM4CHEE");
		} catch (Exception e) {
			log.warning("Error deleting study " + studyUID + " for patient " + patientID + " in project " + projectID, e);
		}
	}
}
