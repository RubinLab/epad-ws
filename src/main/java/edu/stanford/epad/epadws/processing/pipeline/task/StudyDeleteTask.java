package edu.stanford.epad.epadws.processing.pipeline.task;

import java.util.Set;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.queries.XNATQueries;
import edu.stanford.epad.epadws.xnat.XNATDeletionOperations;

/**
 * 
 * @author martin
 * 
 */
public class StudyDeleteTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();

	private final String sessionID;
	private final String projectID;
	private final String patientID;
	private final String studyUID;

	public StudyDeleteTask(String sessionID, String projectID, String patientID, String studyUID)
	{
		this.sessionID = sessionID;
		this.projectID = projectID;
		this.patientID = patientID;
		this.studyUID = studyUID;
	}

	@Override
	public void run()
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();

		try {
			log.info("Deleting study " + studyUID + " for patient " + patientID + " in project " + projectID);

			XNATDeletionOperations.deleteXNATDICOMStudy(projectID, patientID, studyUID, sessionID);

			Set<String> allStudyUIDs = XNATQueries.getAllDICOMStudyUIDs();
			if (!allStudyUIDs.contains(studyUID))
				epadOperations.deleteStudyFromEPadAndDcm4CheeDatabases(studyUID);
			else
				log.info("Study " + studyUID + " in use by other projects or subjects so will not be deleted from DCM4CHEE");
		} catch (Exception e) {
			log.warning("Error deleting study " + studyUID + " for patient " + patientID + " in project " + projectID, e);
		}
	}
}
