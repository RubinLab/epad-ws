package edu.stanford.isis.epadws.processing.pipeline.watcher;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.processing.model.DicomSeriesOrder;
import edu.stanford.isis.epadws.processing.model.DicomSeriesOrderTracker;
import edu.stanford.isis.epadws.processing.persistence.MySqlInstance;
import edu.stanford.isis.epadws.processing.persistence.MySqlQueries;
import edu.stanford.isis.epadws.processing.pipeline.threads.ShutdownSignal;

/**
 * Watch for new studies that appear with a DCM4CHEE database with the 'study-status' field set to zero. Add them to the
 * series watcher queue to be processed by {@Dcm4CheeSeriesWatcher}.
 * 
 * @author amsnyder
 */
public class Dcm4CheeDatabaseWatcher implements Runnable
{
	private final EPADLogger logger = EPADLogger.getInstance();

	private final BlockingQueue<DicomSeriesOrder> dcm4CheeSeriesWatcherQueue;

	public Dcm4CheeDatabaseWatcher(BlockingQueue<DicomSeriesOrder> dcm4CheeSeriesWatcherQueue)
	{
		this.dcm4CheeSeriesWatcherQueue = dcm4CheeSeriesWatcherQueue;
	}

	@Override
	public void run()
	{
		ShutdownSignal signal = ShutdownSignal.getInstance();
		MySqlQueries mySqlQueries = MySqlInstance.getInstance().getMysqlQueries();

		while (!signal.hasShutdown()) {
			try {
				List<Map<String, String>> series = mySqlQueries.getSeriesForStatusEx(0);

				for (Map<String, String> currSeries : series) {
					String seriesIUid = currSeries.get("series_iuid");
					String seriesDesc = currSeries.get("series_desc");
					String numInstances = currSeries.get("num_instances");
					// Create a SeriesOrder to indicate new PNG files are being created.
					DicomSeriesOrder seriesOrder = new DicomSeriesOrder(Integer.parseInt(numInstances), seriesIUid);
					mySqlQueries.updateSeriesStatusCodeEx(325, seriesIUid);
					submitSeriesForPngGeneration(seriesOrder); // Submit this series to generate all the PNG files.
					float percentComplete = mySqlQueries.getPercentComplete(seriesIUid);
					DicomSeriesOrderTracker.getInstance().setPercentComplete(seriesIUid, percentComplete);

					logger.info("DCM4CHEE new series found - #images=" + numInstances + ", desc=" + seriesDesc + ", series iuid="
							+ seriesIUid);

					logger.info("[TEMP] Creating new OrderSeries numInstances=" + numInstances + " seriesIUid=" + seriesIUid);
				}
				Thread.sleep(500);
			} catch (Exception e) {
				logger.warning("DcmDbTableWatcher had: " + e.getMessage(), e);
			}
		}
	}

	/**
	 * 
	 * @param seriesOrder SeriesOrder
	 */
	private void submitSeriesForPngGeneration(DicomSeriesOrder seriesOrder)
	{
		logger.info("Submitting series for PNG generation: " + seriesOrder.getSeriesUID());
		dcm4CheeSeriesWatcherQueue.offer(seriesOrder);
	}
}
