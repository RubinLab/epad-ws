package edu.stanford.isis.epadws.processing.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class to keep track of all series in the pipeline. Each series has a {@link DicomSeriesProcessingStatus}
 * object describing its processing status.
 */
public class DicomSeriesProcessingStatusTracker
{
	private static final DicomSeriesProcessingStatusTracker ourInstance = new DicomSeriesProcessingStatusTracker();
	private final Map<String, DicomSeriesProcessingStatus> statusMap = new ConcurrentHashMap<String, DicomSeriesProcessingStatus>();

	public static DicomSeriesProcessingStatusTracker getInstance()
	{
		return ourInstance;
	}

	private DicomSeriesProcessingStatusTracker()
	{
	}

	public void addDicomSeriesProcessingStatus(DicomSeriesProcessingStatus dicomSeriesProcessingStatus)
	{
		if (dicomSeriesProcessingStatus == null) {
			throw new IllegalArgumentException("dicomSeriesProcessingStatus cannot be null");
		}
		DicomSeriesDescription dicomSeriesDescription = dicomSeriesProcessingStatus.getDicomSeriesDescription();
		if (dicomSeriesDescription == null) {
			throw new IllegalArgumentException("dicomSeriesDescription cannot be null");
		}
		String seriesUID = dicomSeriesDescription.getSeriesUID();
		statusMap.put(seriesUID, dicomSeriesProcessingStatus);
	}

	public void removeDicomSeriesProcessingStatus(DicomSeriesProcessingStatus dicomSeriesProcessingStatus)
	{
		String seriesUID = dicomSeriesProcessingStatus.getDicomSeriesDescription().getSeriesUID();
		statusMap.remove(seriesUID);
	}

	public Set<DicomSeriesProcessingStatus> getDicomSeriesProcessingStatusSet()
	{
		return new HashSet<DicomSeriesProcessingStatus>(statusMap.values());
	}

	public DicomSeriesProcessingStatus getDicomSeriesProcessingStatus(String seriesUID)
	{
		return statusMap.get(seriesUID);
	}
}
