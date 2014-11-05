package edu.stanford.epad.epadws.processing.pipeline.task;

import java.util.Set;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.service.UserProjectService;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * Delete a patient and all studies for that patient in the ePAD and DCMCHEE databases.
 * 
 * @author martin
 * 
 */
public class SubjectDataDeleteTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();

	private final String projectID;
	private final String patientID;
	private final String username;

	public SubjectDataDeleteTask(String projectID, String patientID, String username)
	{
		this.projectID = projectID;
		this.patientID = patientID;
		this.username = username;
	}

	@Override
	public void run()
	{
		try {
    		Set<String>projectIds = UserProjectService.getAllProjectIDs();
    		boolean deleteCompletely = true;
    		for (String projectId: projectIds)
    		{
    			if (projectId.equals(projectID)) continue;
    			Set<String> allSubjectIDs = UserProjectService.getSubjectIDsForProject(projectId);
    			if (allSubjectIDs.contains(patientID.replace('.', '_')) || allSubjectIDs.contains(patientID))
    			{
    				log.info("Subject:" + patientID + " still exists in " + projectId);
    				deleteCompletely = false;
    				break;
    			}
    		}
    		if (deleteCompletely)
    		{
				Set<String> subjectStudyUIDs = UserProjectService.getStudyUIDsForSubject(projectID, patientID);
				EpadOperations epadOperations = DefaultEpadOperations.getInstance();
				for (String studyUID: subjectStudyUIDs)
				{
					StudyReference studyReference = new StudyReference(projectID, patientID, studyUID);
					String adminSessionID = "";
					if (!EPADConfig.UseEPADUsersProjects) {
						adminSessionID = XNATSessionOperations.getXNATAdminSessionID();
					} else {
						adminSessionID = EPADSessionOperations.getAdminSessionID();
					}
					epadOperations.studyDelete(studyReference, adminSessionID, false, username);
				}
    		}
		} catch (Exception e) {
			log.warning("Error deleting patient " + patientID + " in project " + projectID, e);
		}
	}
}
