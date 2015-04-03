package edu.stanford.epad.epadws.processing.pipeline.watcher;

import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.dtos.internal.DCM4CHEESeries;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.processing.model.SeriesProcessingDescription;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.RemotePACService;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * Watch for new studies that appear in ePAD's DCM4CHEE MySQL database with the 'study_status' field set to zero, which
 * indicates that they are a new series. Add them to ePAD's series watcher queues to be subsequently processed by
 * watchers (currently {@link DICOMSeriesWatcher} and {@link XNATSeriesWatcher}).
 */
public class Dcm4CheeDatabaseWatcher implements Runnable
{
	private final EPADLogger logger = EPADLogger.getInstance();

	private final int SleepTimeInMilliseconds = 5000;

	private final BlockingQueue<SeriesProcessingDescription> dcm4CheeSeriesWatcherQueue;
	private final BlockingQueue<SeriesProcessingDescription> xnatSeriesWatcherQueue;

	public Dcm4CheeDatabaseWatcher(BlockingQueue<SeriesProcessingDescription> dicomSeriesWatcherQueue,
			BlockingQueue<SeriesProcessingDescription> xnatSeriesWatcherQueue)
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
		EpadOperations epadQueries = DefaultEpadOperations.getInstance();
		int run = 0;
		while (!signal.hasShutdown()) {
			try {
				List<DCM4CHEESeries> dcm4CheeSeriesList = epadQueries.getNewDcm4CheeSeries();

				for (DCM4CHEESeries dcm4CheeSeries : dcm4CheeSeriesList) {
					String seriesUID = dcm4CheeSeries.seriesUID;
					String studyUID = dcm4CheeDatabaseOperations.getStudyUIDForSeries(seriesUID);
					String patientName = dcm4CheeSeries.patientName;
					String patientID = dcm4CheeSeries.patientID;
					String seriesDesc = dcm4CheeSeries.seriesDescription;
					int numInstances = dcm4CheeSeries.imagesInSeries;
					SeriesProcessingDescription dicomSeriesDescription = new SeriesProcessingDescription(numInstances,
							seriesUID, studyUID, patientName, patientID);
					epadDatabaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.IN_PIPELINE);
					submitSeriesForPngGeneration(dicomSeriesDescription); // Submit this series to generate all the PNG files.
					submitSeriesForXNATGeneration(dicomSeriesDescription); // Submit this series to generate XNAT information.

					logger.info("New DICOM series " + seriesUID + " (" + patientName + ", " + seriesDesc
							+ ") found in DCM4CHEE with " + numInstances + " image(s)");
				}
				if (run%1 == 0) // every 5 secs
					RemotePACService.checkTransfers();
				// Every tenth time check deleted dcm4che series
				if (run >= 10)
				{
					run = 0;
					Set<String> deletedSeriesUIDs = epadQueries.getDeletedDcm4CheeSeries();
					for (String seriesUID: deletedSeriesUIDs)
					{
						try
						{
							logger.info("Series + " + seriesUID + " no longer in DCM4CHE, deleting from epad database");
							epadDatabaseOperations.deleteSeries(seriesUID);
							// TODO: Delete generated PNGs also???
						} catch (Exception x) {
							logger.warning("Error deleting series from database");
						}
					}
				}
				else
				{
					run++;
				}
				
				Thread.sleep(SleepTimeInMilliseconds);
			} catch (Exception e) {
				logger.warning("Dcm4CheeDatabaseWatcher error", e);
			}
		}
		logger.info("Exiting ePAD's DCM4CHEE database watcher");
	}

	private void submitSeriesForPngGeneration(SeriesProcessingDescription dicomSeriesDescription)
	{
		dcm4CheeSeriesWatcherQueue.offer(dicomSeriesDescription);
	}

	private void submitSeriesForXNATGeneration(SeriesProcessingDescription dicomSeriesDescription)
	{
		xnatSeriesWatcherQueue.offer(dicomSeriesDescription);
	}
}
