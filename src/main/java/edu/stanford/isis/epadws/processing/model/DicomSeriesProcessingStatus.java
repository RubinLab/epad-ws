package edu.stanford.isis.epadws.processing.model;

import edu.stanford.isis.epad.common.util.EPADLogger;

/**
 * Pipeline processing state of a DICOM series.
 * <p>
 * When a new series is detected, it either runs to completion or until it is idle for a set amount of time.
 * 
 * @author amsnyder
 */
public class DicomSeriesProcessingStatus
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	private static final long MAX_IDLE_TIME = 30000;

	private long lastActivityTimeStamp;

	private final DicomSeriesDescription dicomSeriesDescription;

	private DicomImageProcessingState imageProcessingState;

	public DicomSeriesProcessingStatus(DicomSeriesDescription dicomSeriesDescription)
	{
		if (dicomSeriesDescription == null)
			throw new IllegalArgumentException("DICOM series description cannot be null.");

		this.dicomSeriesDescription = dicomSeriesDescription;
		lastActivityTimeStamp = System.currentTimeMillis();
		imageProcessingState = DicomImageProcessingState.NEW;
	}

	public DicomSeriesDescription getSeriesDescription()
	{
		return dicomSeriesDescription;
	}

	public void setState(DicomImageProcessingState pState)
	{
		imageProcessingState = pState;
	}

	public DicomImageProcessingState getProcessingState()
	{
		return imageProcessingState;
	}

	/**
	 * We are done if the series has been idle for a while, or if it is complete.
	 * 
	 * @return boolean
	 */
	public boolean isDone()
	{
		long currTime = System.currentTimeMillis();
		if (currTime > lastActivityTimeStamp + MAX_IDLE_TIME) {
			logger.info("Series: " + dicomSeriesDescription.getSeriesUID() + " is idle.");
			return true;
		}
		if (dicomSeriesDescription.isComplete()) {
			logger.info("Series: " + dicomSeriesDescription.getSeriesUID() + " is complete. #images="
					+ dicomSeriesDescription.size());
			return true;
		}
		return false;
	}

	public void registerActivity()
	{
		lastActivityTimeStamp = System.currentTimeMillis();
	}
}
