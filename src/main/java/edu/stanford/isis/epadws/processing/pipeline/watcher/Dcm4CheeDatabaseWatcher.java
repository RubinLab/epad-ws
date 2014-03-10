package edu.stanford.isis.epadws.processing.pipeline.watcher;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.isis.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.isis.epadws.epaddb.EpadDatabase;
import edu.stanford.isis.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.isis.epadws.processing.model.DicomSeriesProcessingDescription;
import edu.stanford.isis.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.isis.epadws.queries.DefaultEpadQueries;
import edu.stanford.isis.epadws.queries.EpadQueries;

/**
 * Watch for new studies that appear with ePAD's DCM4CHEE MySQL database with the 'study-status' field set to zero,
 * which indicates that they are a new series. Add them to ePAD's series watcher queues to be subsequently processed by
 * watchers (currently {@link DICOMSeriesWatcher} and {@link XNATSeriesWatcher}).
 */
public class Dcm4CheeDatabaseWatcher implements Runnable
{
	private final EPADLogger logger = EPADLogger.getInstance();

	private final BlockingQueue<DicomSeriesProcessingDescription> dcm4CheeSeriesWatcherQueue;
	private final BlockingQueue<DicomSeriesProcessingDescription> xnatSeriesWatcherQueue;

	public Dcm4CheeDatabaseWatcher(BlockingQueue<DicomSeriesProcessingDescription> dicomSeriesWatcherQueue,
			BlockingQueue<DicomSeriesProcessingDescription> xnatSeriesWatcherQueue)
	{
		logger.info("Starting ePAD's DCM4CHEE database watcher");
		this.dcm4CheeSeriesWatcherQueue = dicomSeriesWatcherQueue;
		this.xnatSeriesWatcherQueue = xnatSeriesWatcherQueue;
	}

	@Override
	public void run()
	{
		ShutdownSignal signal = ShutdownSignal.getInstance();
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		EpadQueries epadQueries = DefaultEpadQueries.getInstance();

		while (!signal.hasShutdown()) {
			try {
				List<Map<String, String>> series = epadQueries.getDicomSeriesForStatus(0);

				for (Map<String, String> currSeries : series) {
					String seriesIUid = currSeries.get("series_iuid");
					String studyIUID = dcm4CheeDatabaseOperations.getDicomStudyUIDForSeries(seriesIUid);
					Map<String, String> patient = dcm4CheeDatabaseOperations.getPatientForDicomStudy(studyIUID);
					String patientName = patient.get("pat_name");
					String patientID = patient.get("pat_id");
					String seriesDesc = currSeries.get("series_desc");
					String numInstances = currSeries.get("num_instances");
					DicomSeriesProcessingDescription dicomSeriesDescription = new DicomSeriesProcessingDescription(
							Integer.parseInt(numInstances), seriesIUid, studyIUID, patientName, patientID);
					epadDatabaseOperations.updateDicomSeriesStatusCode(325, seriesIUid);
					submitSeriesForPngGeneration(dicomSeriesDescription); // Submit this series to generate all the PNG files.
					submitSeriesForXNATGeneration(dicomSeriesDescription); // Submit this series to generate XNAT information.

					logger.info("New DICOM series (" + patientName + ", " + seriesDesc + ") found in DCM4CHEE with "
							+ numInstances + " image(s) and series ID " + seriesIUid);
				}
				Thread.sleep(500);
			} catch (Exception e) {
				logger.warning("Dcm4CheeDatabaseWatcher error", e);
			}
		}
	}

	private void submitSeriesForPngGeneration(DicomSeriesProcessingDescription dicomSeriesDescription)
	{
		dcm4CheeSeriesWatcherQueue.offer(dicomSeriesDescription);
	}

	private void submitSeriesForXNATGeneration(DicomSeriesProcessingDescription dicomSeriesDescription)
	{
		xnatSeriesWatcherQueue.offer(dicomSeriesDescription);
	}
}
