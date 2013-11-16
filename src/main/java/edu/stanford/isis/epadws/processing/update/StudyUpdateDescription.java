package edu.stanford.isis.epadws.processing.update;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StudyUpdateDescription
{
	public final String studyID;
	public final Set<SeriesUpdateDescription> seriesUpdates;
	private final Map<String, SeriesUpdateDescription> seriesUpdateMap;

	public StudyUpdateDescription(String studyID)
	{
		this.studyID = studyID;
		this.seriesUpdateMap = new HashMap<String, SeriesUpdateDescription>();
		this.seriesUpdates = new HashSet<SeriesUpdateDescription>();
	}

	public SeriesUpdateDescription recordSeriesUpdate(String seriesID)
	{
		if (!seriesUpdateMap.containsKey(seriesID)) {
			SeriesUpdateDescription seriesUpdate = new SeriesUpdateDescription(studyID);
			this.seriesUpdates.add(seriesUpdate);
			this.seriesUpdateMap.put(seriesID, seriesUpdate);
			return seriesUpdate;
		} else
			return seriesUpdateMap.get(seriesID);
	}
}
