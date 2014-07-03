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
