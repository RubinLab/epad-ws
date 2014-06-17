package edu.stanford.epad.epadws.processing.pipeline.task;

import edu.stanford.epad.common.util.EPADLogger;

/**
 * @author martin
 * 
 */
public class ProjectDataDeleteTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();

	private final String projectID;

	public ProjectDataDeleteTask(String projectID)
	{
		this.projectID = projectID;
	}

	@Override
	public void run()
	{
		try {
			// Set<String> patientIDs = XNATQueries.getSubjectIDsForProject(sessionID, projectID);
			// Set<String> subjectStudyUIDs = XNATQueries.getDICOMStudyUIDsForSubject(sessionID, projectID, patientID);
			// EpadOperations epadOperations = DefaultEpadOperations.getInstance();
			// TODO Fix. Seems to delete study even if used elsewhere.
			// Set<String> allStudyUIDs = XNATQueries.getAllDICOMStudyUIDs();
			// subjectStudyUIDs.removeAll(allStudyUIDs); // Remove studies used elsewhere so that they are not deleted
			// epadOperations.deleteStudiesFromEPadAndDcm4CheeDatabases(subjectStudyUIDs);
		} catch (Exception e) {
			log.warning("Error deleting dcm4chee studies for project " + projectID + " from XNAT", e);
		}
	}
}
