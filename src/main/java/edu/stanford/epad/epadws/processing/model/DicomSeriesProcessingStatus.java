package edu.stanford.epad.epadws.processing.model;

import edu.stanford.epad.common.util.EPADLogger;

/**
 * Pipeline processing state of a DICOM series. A {@link DicomSeriesProcessingStatusTracker} holds these for all series
 * on the pipeline.
 * <p>
 * When a new series is detected, it either runs to completion or until it is idle for a set amount of time.
 */
public class DicomSeriesProcessingStatus
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	private static final long MAX_IDLE_TIME = 30000;

	private long lastActivityTimeStamp;

	private final DicomSeriesProcessingDescription dicomSeriesDescription;

	private DicomImageProcessingState dicomImageProcessingState;

	public DicomSeriesProcessingStatus(DicomSeriesProcessingDescription dicomSeriesDescription)
	{
		if (dicomSeriesDescription == null)
			throw new IllegalArgumentException("DICOM series description cannot be null");

		this.dicomSeriesDescription = dicomSeriesDescription;
		lastActivityTimeStamp = System.currentTimeMillis();
		dicomImageProcessingState = DicomImageProcessingState.NEW;
	}

	public float percentComplete()
	{
		return dicomSeriesDescription.percentComplete();
	}

	public DicomSeriesProcessingDescription getDicomSeriesProcessingDescription()
	{
		return dicomSeriesDescription;
	}

	public void setState(DicomImageProcessingState pState)
	{
		dicomImageProcessingState = pState;
	}

	public DicomImageProcessingState getProcessingState()
	{
		return dicomImageProcessingState;
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
			logger.info("Series " + dicomSeriesDescription.getSeriesUID() + " has completed processing.");
			return true;
		}
		if (dicomSeriesDescription.isComplete()) {
			logger.info("Series: " + dicomSeriesDescription.getSeriesUID() + " is complete with "
					+ dicomSeriesDescription.getNumberOfInstances() + " instances");
			return true;
		}
		return false;
	}

	public void registerActivity()
	{
		lastActivityTimeStamp = System.currentTimeMillis();
	}
}
