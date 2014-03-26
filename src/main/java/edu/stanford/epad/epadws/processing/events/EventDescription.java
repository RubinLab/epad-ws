package edu.stanford.epad.epadws.processing.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EventDescription
{
	public final List<ProjectEventDescription> projectEvents;
	private final ConcurrentMap<String, ProjectEventDescription> projectEventMap; // projectID -> ProjectEventDescription

	public EventDescription()
	{
		this.projectEvents = new ArrayList<ProjectEventDescription>();
		this.projectEventMap = new ConcurrentHashMap<String, ProjectEventDescription>();
	}

	public synchronized ProjectEventDescription recordProjectEvent(String projectID)
	{
		if (!projectEventMap.containsKey(projectID)) {
			ProjectEventDescription projectEventDescription = new ProjectEventDescription(projectID);
			this.projectEventMap.put(projectID, projectEventDescription);
			this.projectEvents.add(projectEventDescription);
			return projectEventDescription;
		} else
			return projectEventMap.get(projectID);
	}

	public List<ProjectEventDescription> getProjectEvents()
	{
		return new ArrayList<ProjectEventDescription>(projectEventMap.values());
	}
}
