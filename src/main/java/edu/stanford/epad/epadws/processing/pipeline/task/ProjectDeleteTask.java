package edu.stanford.epad.epadws.processing.pipeline.task;

import java.util.Set;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.FileOperations;
import edu.stanford.epad.epadws.queries.XNATQueries;
import edu.stanford.epad.epadws.xnat.XNATDeletionOperations;

/**
 * @author martin
 * 
 */
public class ProjectDeleteTask implements Runnable
{
	private static EPADLogger logger = EPADLogger.getInstance();

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
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();

		try {
			Set<String> patientIDs = XNATQueries.patientIDsForProject(sessionID, projectID);

			for (String patientID : patientIDs) {
				logger.info("Deleting patient " + patientID + " in project " + projectID);

				Set<String> studyUIDs = XNATQueries.dicomStudyUIDsForSubject(sessionID, projectID, patientID);

				if (XNATDeletionOperations.deleteXNATSubject(projectID, patientID, sessionID))
					logger.info("Deleted patient " + patientID + " in project " + projectID + " from XNAT");
				else
					logger.warning("Error in deleting patient " + patientID + " in project " + projectID + " from XNAT");

				// Now delete studies from dcm4chee and ePAD's database; includes deleting PNGs for studies.
				for (String studyUID : studyUIDs) {
					Set<String> seriesUIDs = dcm4CheeDatabaseOperations.findAllSeriesUIDsInStudy(studyUID);
					logger.info("Found " + seriesUIDs.size() + " series in study " + patientID);

					logger.info("Deleting study " + studyUID + " from dcm4chee's database");
					Dcm4CheeOperations.deleteStudy(studyUID); // Must run after finding series in DCM4CHEE

					// Should not delete until after deleting study in DCM4CHEE or PNG pipeline will activate.
					for (String seriesUID : seriesUIDs) {
						logger.info("Deleting series " + seriesUID + " from ePAD database");
						epadDatabaseOperations.deleteSeries(seriesUID);
					}
					logger.info("Deleting study " + studyUID + " from ePAD database");
					epadDatabaseOperations.deleteStudy(studyUID);
					FileOperations.deletePNGsForStudy(studyUID);
				}
			}
			if (XNATDeletionOperations.deleteXNATProject(projectID, sessionID))
				logger.info("Deleted project " + projectID + " from XNAT");
			else
				logger.warning("Error in deleting project " + projectID + " from XNAT");

		} catch (Exception e) {
			logger.warning("Error deleting project " + projectID + " from XNAT", e);
		}
	}
}
