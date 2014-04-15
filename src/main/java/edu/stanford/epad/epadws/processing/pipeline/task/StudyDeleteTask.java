package edu.stanford.epad.epadws.processing.pipeline.task;

import java.util.Set;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.FileOperations;
import edu.stanford.epad.epadws.xnat.XNATDeletionOperations;

/**
 * 
 * @author martin
 * 
 */
public class StudyDeleteTask implements Runnable
{
	private static EPADLogger logger = EPADLogger.getInstance();

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
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();

		try {
			logger.info("Deleting study " + studyUID + " for patient " + patientID + " in project " + projectID);

			XNATDeletionOperations.deleteXNATDICOMStudy(projectID, patientID, studyUID, sessionID);

			Set<String> seriesUIDs = dcm4CheeDatabaseOperations.findAllSeriesUIDsInStudy(studyUID);
			logger.info("Found " + seriesUIDs.size() + " series in study " + studyUID);

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
		} catch (Exception e) {
			logger.warning("Error deleting study " + studyUID + " for patient " + patientID + " in project " + projectID, e);
		}
	}
}
