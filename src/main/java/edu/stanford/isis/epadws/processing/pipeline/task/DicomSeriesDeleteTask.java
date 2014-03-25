package edu.stanford.isis.epadws.processing.pipeline.task;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.dcm4chee.Dcm4CheeOperations;
import edu.stanford.isis.epadws.epaddb.EpadDatabase;
import edu.stanford.isis.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.isis.epadws.epaddb.FileOperations;

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
			epadDatabaseOperations.deleteDicomSeries(seriesUID);
			FileOperations.deletePNGsforDicomSeries(studyUID, seriesUID);
		} catch (Exception e) {
			logger.warning("run had: " + e.getMessage(), e);
		}
	}
}
