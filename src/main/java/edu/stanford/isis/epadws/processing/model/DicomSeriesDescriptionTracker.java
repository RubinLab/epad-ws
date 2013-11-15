package edu.stanford.isis.epadws.processing.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import edu.stanford.isis.epadws.processing.persistence.MySqlInstance;
import edu.stanford.isis.epadws.processing.persistence.MySqlQueries;

/**
 * Singleton class to keep track of all images and which are in the pipeline.
 * 
 * @author alansnyder
 */
public class DicomSeriesDescriptionTracker
{
	private static final DicomSeriesDescriptionTracker ourInstance = new DicomSeriesDescriptionTracker();
	private final Map<String, DicomSeriesProcessingStatus> statusMap = new ConcurrentHashMap<String, DicomSeriesProcessingStatus>();
	private final Map<String, Float> completionMap = new ConcurrentHashMap<String, Float>();

	public static DicomSeriesDescriptionTracker getInstance()
	{
		return ourInstance;
	}

	private DicomSeriesDescriptionTracker()
	{
	}

	public void add(DicomSeriesProcessingStatus seriesOrderStatus)
	{
		if (seriesOrderStatus == null) {
			throw new IllegalArgumentException("seriesOrderStatus cannot be null.");
		}
		DicomSeriesDescription so = seriesOrderStatus.getSeriesDescription();
		if (so == null) {
			throw new IllegalArgumentException("SeriesOrder cannot be null.");
		}
		String seriesUID = so.getSeriesUID();
		statusMap.put(seriesUID, seriesOrderStatus);
	}

	public void remove(DicomSeriesProcessingStatus seriesOrderStatus)
	{
		String seriesUID = seriesOrderStatus.getSeriesDescription().getSeriesUID();
		statusMap.remove(seriesUID);
	}

	public Set<DicomSeriesProcessingStatus> getStatusSet()
	{
		return new HashSet<DicomSeriesProcessingStatus>(statusMap.values());
	}

	public DicomSeriesProcessingStatus get(String seriesUID)
	{
		return statusMap.get(seriesUID);
	}

	/**
	 * Get a cached result for the percent complete, since in most cases it will be 100%. If the series doesn't have the
	 * value calculated. (Like on the first call, then calculate and cache the result.)
	 * 
	 * @param seriesUID String
	 * @return float value between 0.0 and 100.0 percent.
	 */
	public float getPercentComplete(String seriesUID)
	{
		float percentComplete = -1.0f;
		if (!completionMap.containsKey(seriesUID)) {
			// call the database to calculate it now.
			MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
			percentComplete = queries.getPercentComplete(seriesUID);
			completionMap.put(seriesUID, percentComplete);
		} else {
			percentComplete = completionMap.get(seriesUID);
		}
		return percentComplete;
	}

	/**
	 * 
	 * @param seriesUID String
	 * @param percentComplete float
	 */
	public void setPercentComplete(String seriesUID, float percentComplete)
	{
		completionMap.put(seriesUID, percentComplete);
	}
}
