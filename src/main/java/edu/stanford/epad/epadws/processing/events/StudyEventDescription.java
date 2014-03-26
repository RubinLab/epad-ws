package edu.stanford.epad.epadws.processing.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StudyEventDescription
{
	public final String studyID;
	public final List<SeriesEventDescription> seriesEvents;
	private final ConcurrentMap<String, SeriesEventDescription> seriesEventMap;

	public StudyEventDescription(String studyID)
	{
		this.studyID = studyID;
		this.seriesEventMap = new ConcurrentHashMap<String, SeriesEventDescription>();
		this.seriesEvents = new ArrayList<SeriesEventDescription>();
	}

	public synchronized SeriesEventDescription recordSeriesEvent(String seriesID)
	{
		if (!seriesEventMap.containsKey(seriesID)) {
			SeriesEventDescription seriesUpdate = new SeriesEventDescription(studyID);
			this.seriesEvents.add(seriesUpdate);
			this.seriesEventMap.put(seriesID, seriesUpdate);
			return seriesUpdate;
		} else
			return seriesEventMap.get(seriesID);
	}

	public List<SeriesEventDescription> getSeriesEvents()
	{
		return new ArrayList<SeriesEventDescription>(seriesEventMap.values());
	}
}
