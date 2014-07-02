package edu.stanford.epad.epadws.processing.model;

import edu.stanford.epad.common.util.EPADLogger;

/**
 * Pipeline processing state of a DICOM series. A {@link DicomSeriesProcessingStatusTracker} holds these for all series
 * currently in ePAD's pipeline.
 * <p>
 * When a new series is detected, it either runs to completion or until it is idle for a set amount of time.
 */
public class DicomSeriesProcessingStatus
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	private static final long MAX_IDLE_TIME = 30000;

	private final SeriesProcessingDescription dicomSeriesProcessingDescription;
	private DicomSeriesProcessingState dicomSeriesProcessingState;
	private long lastActivityTimeStamp;

	public DicomSeriesProcessingStatus(SeriesProcessingDescription dicomSeriesProcessingDescription)
	{
		if (dicomSeriesProcessingDescription == null)
			throw new IllegalArgumentException("DICOM series processing description cannot be null");

		this.dicomSeriesProcessingDescription = dicomSeriesProcessingDescription;
		this.lastActivityTimeStamp = System.currentTimeMillis();
		this.dicomSeriesProcessingState = DicomSeriesProcessingState.NEW;
	}

	public float percentComplete()
	{
		return this.dicomSeriesProcessingDescription.percentComplete();
	}

	public SeriesProcessingDescription getDicomSeriesProcessingDescription()
	{
		return this.dicomSeriesProcessingDescription;
	}

	public void setSeriesProcessingState(DicomSeriesProcessingState pState)
	{
		this.dicomSeriesProcessingState = pState;
	}

	public DicomSeriesProcessingState getDicomSeriesProcessingState()
	{
		return this.dicomSeriesProcessingState;
	}

	/**
	 * We are done if the series has been idle for a while, or if it is complete.
	 * 
	 * @return boolean
	 */
	public boolean isDone()
	{
		long currTime = System.currentTimeMillis();

		if (this.dicomSeriesProcessingState == DicomSeriesProcessingState.COMPLETE
				|| dicomSeriesProcessingDescription.isComplete()) {
			String seriesUID = dicomSeriesProcessingDescription.getSeriesUID();
			logger.info("Series " + seriesUID + " is complete with "
					+ dicomSeriesProcessingDescription.getNumberOfInstances() + " instances");

			return true;
		}
		if (currTime > lastActivityTimeStamp + MAX_IDLE_TIME) {
			String seriesUID = dicomSeriesProcessingDescription.getSeriesUID();
			String patientName = dicomSeriesProcessingDescription.getPatientName();
			logger.info("Series " + seriesUID + " for patient " + patientName + " has completed processing.");
			return true;
		}
		return false;
	}

	public void registerActivity()
	{
		this.lastActivityTimeStamp = System.currentTimeMillis();
	}
}
