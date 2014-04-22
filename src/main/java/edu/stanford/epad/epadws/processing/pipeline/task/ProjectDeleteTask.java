package edu.stanford.epad.epadws.processing.pipeline.task;

import java.util.Set;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.queries.XNATQueries;
import edu.stanford.epad.epadws.xnat.XNATDeletionOperations;

/**
 * @author martin
 * 
 */
public class ProjectDeleteTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();

	private final String sessionID;
	private final String projectID;

	public ProjectDeleteTask(String sessionID, String projectID)
	{
		this.sessionID = sessionID;
		this.projectID = projectID;
	}

	@Override
	public void run()
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();

		try {
			Set<String> patientIDs = XNATQueries.subjectIDsForProject(sessionID, projectID);

			for (String patientID : patientIDs) {
				log.info("Deleting patient " + patientID + " in project " + projectID);

				Set<String> subjectStudyUIDs = XNATQueries.dicomStudyUIDsForSubject(sessionID, projectID, patientID);

				if (XNATDeletionOperations.deleteXNATSubject(projectID, patientID, sessionID))
					log.info("Deleted patient " + patientID + " in project " + projectID + " from XNAT");
				else
					log.warning("Error deleting patient " + patientID + " in project " + projectID + " from XNAT");

				Set<String> allStudyUIDs = XNATQueries.allDICOMStudyUIDs();
				subjectStudyUIDs.removeAll(allStudyUIDs); // Remove studies used elsewhere so that they are not deleted

				epadOperations.deleteStudiesFromEPadAndDcm4CheeDatabases(subjectStudyUIDs);
			}
			if (XNATDeletionOperations.deleteXNATProject(projectID, sessionID))
				log.info("Deleted project " + projectID + " from XNAT");
			else
				log.warning("Error deleting project " + projectID + " from XNAT");
		} catch (Exception e) {
			log.warning("Error deleting project " + projectID + " from XNAT", e);
		}
	}
}
