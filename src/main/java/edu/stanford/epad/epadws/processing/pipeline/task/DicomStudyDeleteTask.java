package edu.stanford.epad.epadws.processing.pipeline.task;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.internal.DCM4CHEESeries;
import edu.stanford.epad.dtos.internal.DCM4CHEESeriesList;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.PNGFilesOperations;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;

/**
 * Task to delete a DICOM study from ePAD's and dcm4chee's databases.
 * 
 */
public class DicomStudyDeleteTask implements Runnable
{
	private static EPADLogger logger = EPADLogger.getInstance();
	private final String studyUID;

	public DicomStudyDeleteTask(String studyUID)
	{
		this.studyUID = studyUID;
	}

	@Override
	public void run()
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();

		try {
			DCM4CHEESeriesList dcm4CheeSeriesList = Dcm4CheeQueries.getSeriesInStudy(studyUID);
			logger.info("Found " + dcm4CheeSeriesList.ResultSet.totalRecords + " series in study " + studyUID);

			logger.info("Deleting study " + studyUID + " from dcm4chee's database");
			Dcm4CheeOperations.deleteStudy(studyUID); // Must run after finding series in DCM4CHEE

			// Should not delete until after deleting study in DCM4CHEE or PNG pipeline will activate.
			for (DCM4CHEESeries series : dcm4CheeSeriesList.ResultSet.Result) {
				logger.info("Deleting series " + series.seriesUID + " from ePAD database");
				epadDatabaseOperations.deleteSeries(series.seriesUID);
			}
			logger.info("Deleting study " + studyUID + " from ePAD database");
			epadDatabaseOperations.deleteStudy(studyUID);
			PNGFilesOperations.deletePNGsForStudy(studyUID);
		} catch (Exception e) {
			logger.warning("run had: " + e.getMessage(), e);
		}
	}
}
