package edu.stanford.isis.epadws.processing.pipeline.task;

import java.util.List;
import java.util.Map;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.persistence.Database;
import edu.stanford.isis.epadws.persistence.DatabaseOperations;
import edu.stanford.isis.epadws.persistence.Dcm4CheeOperations;
import edu.stanford.isis.epadws.persistence.FileOperations;

/**
 * Delete a patient and all studies for that patient in the ePAD and DCMCHEE databases.
 * 
 * @author martin
 * 
 */
public class PatientDeleteTask implements Runnable
{
	private static EPADLogger logger = EPADLogger.getInstance();

	// private final String projectID;
	private final String patientID;

	public PatientDeleteTask(String projectID, String patientID)
	{
		// this.projectID = projectID;
		this.patientID = patientID;
	}

	@Override
	public void run()
	{
		DatabaseOperations databaseOperations = Database.getInstance().getDatabaseOperations();

		try {
			List<String> studies = databaseOperations.getStudyIDsForPatient(patientID);

			for (String studyID : studies) {
				List<Map<String, String>> matchingSeries = databaseOperations.findAllSeriesInStudy(studyID);
				logger.info("Found " + matchingSeries.size() + " series in study " + patientID);

				Dcm4CheeOperations.deleteDicomStudy(patientID); // Must run after finding series in DCM4CHEE

				// Should not delete until after deleting study in DCM4CHEE or PNG pipeline will activate.
				for (Map<String, String> series : matchingSeries) {
					String seriesID = series.get("series_iuid");
					logger.info("SeriesID to delete in ePAD database: " + seriesID);
					databaseOperations.deleteSeries(seriesID);
				}
				databaseOperations.deleteDicomStudy(patientID);
				FileOperations.deletePNGsforDicomStudy(patientID);
			}
		} catch (Exception e) {
			logger.warning("Patient delete task has error: " + e.getMessage(), e);
		}
	}
}
