package edu.stanford.isis.epadws.processing.model;

import edu.stanford.isis.epad.common.util.EPADLogger;

/**
 * Status of the SeriesOrder class.
 * <p>
 * When a new series is detected, it either runs to completion or until it is idle for a set amount of time.
 * 
 * @author amsnyder
 */
public class DicomSeriesStatus
{
	EPADLogger logger = EPADLogger.getInstance();

	private static final long MAX_IDLE_TIME = 30000;

	private long lastActivityTimeStamp;

	private final DicomSeriesDescription seriesOrder;

	private DicomImageProcessingState state;

	public DicomSeriesStatus(DicomSeriesDescription seriesOrder)
	{
		if (seriesOrder == null)
			throw new IllegalArgumentException("seriesOrder cannot be null.");

		this.seriesOrder = seriesOrder;
		lastActivityTimeStamp = System.currentTimeMillis();
		state = DicomImageProcessingState.NEW;
	}

	public DicomSeriesDescription getSeriesDescription()
	{
		return seriesOrder;
	}

	public void setState(DicomImageProcessingState pState)
	{
		state = pState;
	}

	public DicomImageProcessingState getProcessingState()
	{
		return state;
	}

	/**
	 * We are done if the series has been idle for a while, or if it is complete.
	 * 
	 * @return boolean
	 */
	public boolean isDone()
	{
		// Has the series been idle for too long?
		long currTime = System.currentTimeMillis();
		if (currTime > lastActivityTimeStamp + MAX_IDLE_TIME) {
			// log this series as being done.
			logger.info("Series: " + seriesOrder.getSeriesUID() + " is idle. Downloaded " + seriesOrder.getFinishedCount()
					+ " of " + seriesOrder.size() + " images.");
			return true;
		}
		if (seriesOrder.isComplete()) {
			logger.info("Series: " + seriesOrder.getSeriesUID() + " is complete. #images=" + seriesOrder.size());
			return true;
		}
		return false;
	}

	public void registerActivity()
	{
		lastActivityTimeStamp = System.currentTimeMillis();
	}
}
