//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.processing.model;

import edu.stanford.epad.common.util.EPADLogger;

/**
 * Pipeline processing state of a DICOM series. A {@link DicomSeriesProcessingStatusTracker} holds these for all series
 * currently in ePAD's pipeline.
 * <p>
 * When a new series is detected, it either runs to completion or until it is idle for a set amount of time.
 */
public class SeriesPipelineState
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	private static final long MAX_IDLE_TIME = 30000;

	private final SeriesProcessingDescription seriesProcessingDescription;
	private DicomSeriesProcessingState dicomSeriesProcessingState;
	private long lastActivityTimeStamp;

	public SeriesPipelineState(SeriesProcessingDescription seriesProcessingDescription)
	{
		if (seriesProcessingDescription == null)
			throw new IllegalArgumentException("DICOM series processing description cannot be null");

		this.seriesProcessingDescription = seriesProcessingDescription;
		this.lastActivityTimeStamp = System.currentTimeMillis();
		this.dicomSeriesProcessingState = DicomSeriesProcessingState.NEW;
	}

	public float percentComplete()
	{
		return this.seriesProcessingDescription.percentComplete();
	}

	public SeriesProcessingDescription getSeriesProcessingDescription()
	{
		return this.seriesProcessingDescription;
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
				|| this.dicomSeriesProcessingState == DicomSeriesProcessingState.ERROR
				|| seriesProcessingDescription.isComplete()) {
			String seriesUID = seriesProcessingDescription.getSeriesUID();
			if (this.dicomSeriesProcessingState != DicomSeriesProcessingState.ERROR)
				logger.info("Series " + seriesUID + " is complete with "
					+ seriesProcessingDescription.getNumberOfInstances() + " instances");

			return true;
		}
		if (currTime > lastActivityTimeStamp + MAX_IDLE_TIME) {
			String seriesUID = seriesProcessingDescription.getSeriesUID();
			String patientName = seriesProcessingDescription.getPatientName();
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
