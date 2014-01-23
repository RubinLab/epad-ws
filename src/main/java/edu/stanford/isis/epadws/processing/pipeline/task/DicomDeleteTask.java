package edu.stanford.isis.epadws.processing.pipeline.task;

import java.util.List;
import java.util.Map;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.persistence.Database;
import edu.stanford.isis.epadws.persistence.DatabaseOperations;
import edu.stanford.isis.epadws.persistence.Dcm4CheeOperations;
import edu.stanford.isis.epadws.persistence.FileOperations;

/**
 * Task to delete a DICOM study
 * 
 * @author amsnyder
 */
public class DicomDeleteTask implements Runnable
{
	private static EPADLogger logger = EPADLogger.getInstance();
	private final String uidToDelete;
	private final boolean deleteStudy;

	public DicomDeleteTask(String uid, boolean deleteStudy)
	{
		this.uidToDelete = uid;
		this.deleteStudy = deleteStudy;
	}

	@Override
	public void run()
	{
		DatabaseOperations databaseOperations = Database.getInstance().getDatabaseOperations();

		try {
			if (deleteStudy) {
				List<Map<String, String>> study2series = databaseOperations.findAllDicomSeriesInStudy(uidToDelete);
				logger.info("Found " + study2series.size() + " series in study " + uidToDelete);

				Dcm4CheeOperations.deleteDicomStudy(uidToDelete); // Must run after finding series in DCM4CHEE

				// Should not delete until after deleting study in DCM4CHEE or PNG pipeline will activate.
				for (Map<String, String> series : study2series) {
					String seriesID = series.get("series_iuid");
					logger.info("SeriesID to delete in ePAD database: " + seriesID);
					databaseOperations.deleteDicomSeries(seriesID);
				}
				databaseOperations.deleteDicomStudy(uidToDelete);
				FileOperations.deletePNGsforDicomStudy(uidToDelete);
			} else {
				logger.warning("Attempt at (currently unsupported) delete of individual series " + uidToDelete);
				// Not supported in the current version of dcm4chee
				/*
				 * //To avoid to fire the png generation pipeline databaseOperations.updateSeriesStatusCodeEx(77,uidToDelete);
				 * //Delete from dcm4chee dcmDeleteSeries(uidToDelete); //Delete the entries in the table
				 * databaseOperations.deleteSeries(uidToDelete); //Delete the files deletePNGforSeries(uidToDelete);
				 */
			}
		} catch (Exception e) {
			logger.warning("run had: " + e.getMessage(), e);
		}
	}
}
