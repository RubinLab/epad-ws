package edu.stanford.isis.epadws.processing.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StudyEventDescription
{
	public final String studyID;
	public final Set<SeriesEventDescription> seriesEvents;
	private final Map<String, SeriesEventDescription> seriesEventMap;

	public StudyEventDescription(String studyID)
	{
		this.studyID = studyID;
		this.seriesEventMap = new HashMap<String, SeriesEventDescription>();
		this.seriesEvents = new HashSet<SeriesEventDescription>();
	}

	public SeriesEventDescription recordSeriesEvent(String seriesID)
	{
		if (!seriesEventMap.containsKey(seriesID)) {
			SeriesEventDescription seriesUpdate = new SeriesEventDescription(studyID);
			this.seriesEvents.add(seriesUpdate);
			this.seriesEventMap.put(seriesID, seriesUpdate);
			return seriesUpdate;
		} else
			return seriesEventMap.get(seriesID);
	}
}
