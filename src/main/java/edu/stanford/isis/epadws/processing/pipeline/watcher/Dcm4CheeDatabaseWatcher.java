package edu.stanford.isis.epadws.processing.pipeline.watcher;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.processing.model.DicomSeriesDescription;
import edu.stanford.isis.epadws.processing.model.DicomSeriesDescriptionTracker;
import edu.stanford.isis.epadws.processing.persistence.MySqlInstance;
import edu.stanford.isis.epadws.processing.persistence.MySqlQueries;
import edu.stanford.isis.epadws.processing.pipeline.threads.ShutdownSignal;

/**
 * Watch for new studies that appear with ePAD's DCM4CHEE MySQL database with the 'study-status' field set to zero,
 * which indicates that they are new series. Add them to the series watcher queues to be subsequently processed by
 * watchers (currently {@link DicomSeriesWatcher} and {@link XNATSeriesWatcher}).
 */
public class Dcm4CheeDatabaseWatcher implements Runnable
{
	private final EPADLogger logger = EPADLogger.getInstance();

	private final BlockingQueue<DicomSeriesDescription> dcm4CheeSeriesWatcherQueue;
	private final BlockingQueue<DicomSeriesDescription> xnatSeriesWatcherQueue;

	public Dcm4CheeDatabaseWatcher(BlockingQueue<DicomSeriesDescription> dicomSeriesWatcherQueue,
			BlockingQueue<DicomSeriesDescription> xnatSeriesWatcherQueue)
	{
		logger.info("Starting the DCM4CHEE database watcher");
		this.dcm4CheeSeriesWatcherQueue = dicomSeriesWatcherQueue;
		this.xnatSeriesWatcherQueue = xnatSeriesWatcherQueue;
	}

	@Override
	public void run()
	{
		ShutdownSignal signal = ShutdownSignal.getInstance();
		MySqlQueries mySqlQueries = MySqlInstance.getInstance().getMysqlQueries();

		while (!signal.hasShutdown()) {
			try {
				List<Map<String, String>> series = mySqlQueries.getSeriesForStatusInEPadDatabase(0);

				for (Map<String, String> currSeries : series) {
					String seriesIUid = currSeries.get("series_iuid");
					String studyIUID = mySqlQueries.getStudyUIDForSeries(seriesIUid);
					Map<String, String> patient = mySqlQueries.getPatientForStudyFromDcm4Chee(studyIUID);
					String patientName = patient.get("pat_name");
					String patientID = patient.get("pat_id");
					String seriesDesc = currSeries.get("series_desc");
					String numInstances = currSeries.get("num_instances");
					DicomSeriesDescription dicomSeriesDescription = new DicomSeriesDescription(Integer.parseInt(numInstances),
							seriesIUid, studyIUID, patientName, patientID);
					mySqlQueries.updateSeriesStatusCodeEx(325, seriesIUid);
					submitSeriesForPngGeneration(dicomSeriesDescription); // Submit this series to generate all the PNG files.
					submitSeriesForXNATGeneration(dicomSeriesDescription); // Submit this series to generate XNAT information.
					float percentComplete = mySqlQueries.getPercentComplete(seriesIUid);
					DicomSeriesDescriptionTracker.getInstance().setPercentComplete(seriesIUid, percentComplete);

					logger.info("New series (" + seriesDesc + ") found in DCM4CHEE with " + numInstances + " images and ID "
							+ seriesIUid);
				}
				Thread.sleep(500);
			} catch (Exception e) {
				logger.warning("Dcm4CheeDatabaseWatcher error", e);
			}
		}
	}

	private void submitSeriesForPngGeneration(DicomSeriesDescription dicomSeriesDescription)
	{
		dcm4CheeSeriesWatcherQueue.offer(dicomSeriesDescription);
	}

	private void submitSeriesForXNATGeneration(DicomSeriesDescription dicomSeriesDescription)
	{
		xnatSeriesWatcherQueue.offer(dicomSeriesDescription);
	}
}
