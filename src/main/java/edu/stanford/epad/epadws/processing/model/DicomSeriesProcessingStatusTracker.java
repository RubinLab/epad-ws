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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class to keep track of all series in the pipeline. Each series has a {@link SeriesPipelineState}
 * object describing its processing status.
 */
public class DicomSeriesProcessingStatusTracker
{
	private static final DicomSeriesProcessingStatusTracker ourInstance = new DicomSeriesProcessingStatusTracker();
	private final Map<String, SeriesPipelineState> statusMap = new ConcurrentHashMap<String, SeriesPipelineState>();

	public static DicomSeriesProcessingStatusTracker getInstance()
	{
		return ourInstance;
	}

	private DicomSeriesProcessingStatusTracker()
	{
	}

	public void addSeriesPipelineState(SeriesPipelineState dicomSeriesProcessingStatus)
	{
		if (dicomSeriesProcessingStatus == null) {
			throw new IllegalArgumentException("dicomSeriesProcessingStatus cannot be null");
		}
		SeriesProcessingDescription dicomSeriesDescription = dicomSeriesProcessingStatus.getSeriesProcessingDescription();
		if (dicomSeriesDescription == null) {
			throw new IllegalArgumentException("dicomSeriesDescription cannot be null");
		}
		String seriesUID = dicomSeriesDescription.getSeriesUID();
		statusMap.put(seriesUID, dicomSeriesProcessingStatus);
	}

	public void removeSeriesPipelineState(SeriesPipelineState dicomSeriesProcessingStatus)
	{
		String seriesUID = dicomSeriesProcessingStatus.getSeriesProcessingDescription().getSeriesUID();
		statusMap.remove(seriesUID);
	}

	public Set<SeriesPipelineState> getSeriesPipelineStates()
	{
		return new HashSet<SeriesPipelineState>(statusMap.values());
	}

	public SeriesPipelineState getDicomSeriesProcessingStatus(String seriesUID)
	{
		return statusMap.get(seriesUID);
	}
}
