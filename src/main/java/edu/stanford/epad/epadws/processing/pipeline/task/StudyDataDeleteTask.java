package edu.stanford.epad.epadws.processing.pipeline.task;

import java.util.Set;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.queries.XNATQueries;
import edu.stanford.epad.epadws.xnat.XNATDeletionOperations;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

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
	private final boolean deleteAims;
	
	public StudyDataDeleteTask(String projectID, String patientID, String studyUID, boolean deleteAims)
	{
		this.projectID = projectID;
		this.patientID = patientID;
		this.studyUID = studyUID;
		this.deleteAims = deleteAims;
	}

	@Override
	public void run()
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();

		try {
			String adminSessionID = XNATSessionOperations.getXNATAdminSessionID();
    		Set<String>projectIds = XNATQueries.allProjectIDs(adminSessionID);
    		boolean deleteCompletely = true;
    		boolean deleteInUnassigned = false;
    		for (String projectId: projectIds)
    		{
    			Set<String> allStudyUIDs = XNATQueries.getAllStudyUIDsForProject(projectId,adminSessionID);
    			if (allStudyUIDs.contains(studyUID.replace('.', '_')) || allStudyUIDs.contains(studyUID))
    			{
        			if (projectId.equals(EPADConfig.xnatUploadProjectID))
        			{
        				deleteInUnassigned =  true;
        				continue;
        			}
    				deleteCompletely = false;
    				break;
    			}
    		}
			if (deleteCompletely)
			{
				if (deleteInUnassigned)
				{
					XNATDeletionOperations.deleteXNATDICOMStudy(projectID, patientID,
						studyUID, adminSessionID);
				}
				epadOperations.deleteStudyFromEPadAndDcm4CheeDatabases(studyUID, deleteAims);
			}
			else
				 log.info("Study " + studyUID + " in use by other projects or subjects so will not be deleted from DCM4CHEE");
		} catch (Exception e) {
			log.warning("Error deleting study " + studyUID + " for patient " + patientID + " in project " + projectID, e);
		}
	}
}
