package edu.stanford.epad.epadws.handlers.core;

import java.util.Map;

import edu.stanford.epad.epadws.handlers.HandlerUtil;

public class ProjectReference
{
	public final String projectID;

	public ProjectReference(String projectID)
	{
		this.projectID = projectID;
	}

	public static ProjectReference extract(String template, String pathInfo)
	{
		Map<String, String> templateMap = HandlerUtil.getTemplateMap(template, pathInfo);
		String projectID = HandlerUtil.getTemplateParameter(templateMap, "project");

		validateProjectID(projectID);

		return new ProjectReference(projectID);
	}

	public static void validateProjectID(String projectID)
	{
		if (projectID == null)
			throw new RuntimeException("Invalid project ID found in request");
	}
}
