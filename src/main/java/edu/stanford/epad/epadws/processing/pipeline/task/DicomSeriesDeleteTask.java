package edu.stanford.epad.epadws.processing.pipeline.task;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.FileOperations;

/**
 * Task to delete a DICOM series from ePAD's and dcm4chee's databases.
 * 
 */
public class DicomSeriesDeleteTask implements Runnable
{
	private static EPADLogger logger = EPADLogger.getInstance();
	private final String studyUID, seriesUID;

	public DicomSeriesDeleteTask(String studyUID, String seriesUID)
	{
		this.studyUID = studyUID;
		this.seriesUID = seriesUID;
	}

	@Override
	public void run()
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();

		try {
			logger.info("Deleting series " + seriesUID + " from dcm4chee's database");
			Dcm4CheeOperations.deleteSeries(seriesUID);

			// Should not delete until after deleting series in DCM4CHEE or PNG pipeline will activate.
			logger.info("Deleting series " + seriesUID + " from ePAD's database");
			epadDatabaseOperations.deleteSeries(seriesUID);
			FileOperations.deletePNGsForSeries(studyUID, seriesUID);
		} catch (Exception e) {
			logger.warning("run had: " + e.getMessage(), e);
		}
	}
}
