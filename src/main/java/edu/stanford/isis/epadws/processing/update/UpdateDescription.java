package edu.stanford.isis.epadws.processing.update;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpdateDescription
{
	public final Set<ProjectUpdateDescription> projectUpdates;
	private final Map<String, ProjectUpdateDescription> projectUpdateMap; // projectID -> ProjectUpdateDescription

	public UpdateDescription()
	{
		this.projectUpdates = new HashSet<ProjectUpdateDescription>();
		this.projectUpdateMap = new HashMap<String, ProjectUpdateDescription>();
	}

	public ProjectUpdateDescription recordProjectUpdate(String projectID)
	{
		if (!projectUpdateMap.containsKey(projectID)) {
			ProjectUpdateDescription projectUpdateDescription = new ProjectUpdateDescription(projectID);
			this.projectUpdateMap.put(projectID, projectUpdateDescription);
			this.projectUpdates.add(projectUpdateDescription);
			return projectUpdateDescription;
		} else
			return projectUpdateMap.get(projectID);
	}

	public Set<ProjectUpdateDescription> getProjectUpdates()
	{
		return new HashSet<ProjectUpdateDescription>(projectUpdateMap.values());
	}
}
