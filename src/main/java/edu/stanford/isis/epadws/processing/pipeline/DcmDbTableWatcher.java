package edu.stanford.isis.epadws.processing.pipeline;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epadws.processing.model.SeriesOrder;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;
import edu.stanford.isis.epadws.server.ShutdownSignal;

/**
 * Watch for new studies that appear with a database with the 'study-status' field set to zero
 * 
 * 
 * 
 * @author amsnyder
 */
public class DcmDbTableWatcher implements Runnable
{
	ProxyLogger logger = ProxyLogger.getInstance();

	final BlockingQueue<SeriesOrder> seriesQueue;

	public DcmDbTableWatcher(BlockingQueue<SeriesOrder> seriesQueue)
	{
		this.seriesQueue = seriesQueue;
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
					SeriesOrder seriesOrder = new SeriesOrder(Integer.parseInt(numInstances), seriesIUid);
					mySqlQueries.updateSeriesStatusCodeEx(325, seriesIUid);
					submitSeriesForPngGeneration(seriesOrder); // Submit this series to generate all the PNG files.
					float percentComplete = mySqlQueries.getPercentComplete(seriesIUid);
					SeriesOrderTracker.getInstance().setPercentComplete(seriesIUid, percentComplete);

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
	private void submitSeriesForPngGeneration(SeriesOrder seriesOrder)
	{
		logger.info("Submitting series for PNG generation: " + seriesOrder.getSeriesUID());
		seriesQueue.offer(seriesOrder);
	}
}