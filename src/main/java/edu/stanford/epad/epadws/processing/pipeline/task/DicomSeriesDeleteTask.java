package edu.stanford.epad.epadws.processing.pipeline.task;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.FileOperations;

/**
 * Task to delete a DICOM Series
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
			Dcm4CheeOperations.deleteSeries(seriesUID);

			// Should not delete until after deleting series in DCM4CHEE or PNG pipeline will activate.
			epadDatabaseOperations.deleteSeries(seriesUID);
			FileOperations.deletePNGsForSeries(studyUID, seriesUID);
		} catch (Exception e) {
			logger.warning("run had: " + e.getMessage(), e);
		}
	}
}
