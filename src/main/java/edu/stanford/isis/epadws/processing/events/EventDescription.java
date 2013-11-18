package edu.stanford.isis.epadws.processing.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventDescription
{
	public final Set<ProjectEventDescription> projectEvents;
	private final Map<String, ProjectEventDescription> projectEventMap; // projectID -> ProjectEventDescription

	public EventDescription()
	{
		this.projectEvents = new HashSet<ProjectEventDescription>();
		this.projectEventMap = new HashMap<String, ProjectEventDescription>();
	}

	public ProjectEventDescription recordProjectEvent(String projectID)
	{
		if (!projectEventMap.containsKey(projectID)) {
			ProjectEventDescription projectEventDescription = new ProjectEventDescription(projectID);
			this.projectEventMap.put(projectID, projectEventDescription);
			this.projectEvents.add(projectEventDescription);
			return projectEventDescription;
		} else
			return projectEventMap.get(projectID);
	}

	public Set<ProjectEventDescription> getProjectEvents()
	{
		return new HashSet<ProjectEventDescription>(projectEventMap.values());
	}
}
