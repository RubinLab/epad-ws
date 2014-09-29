package edu.stanford.epad.epadws.handlers.core;

import java.util.Map;

import edu.stanford.epad.epadws.handlers.HandlerUtil;

public class SubjectReference
{
	public final String projectID;
	public final String subjectID;

	public SubjectReference(String projectID, String subjectID)
	{
		this.projectID = projectID;
		this.subjectID = subjectID;
	}

	public static SubjectReference extract(String template, String pathInfo)
	{
		Map<String, String> templateMap = HandlerUtil.getTemplateMap(template, pathInfo);
		String projectID = null;
		try
		{
			projectID = HandlerUtil.getTemplateParameter(templateMap, "project");
		}
		catch (Exception x) {}
		String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject");

		validateSubjectID(subjectID);

		return new SubjectReference(projectID, subjectID);
	}

	protected static void validateSubjectID(String subjectID)
	{
		if (subjectID == null)
			throw new RuntimeException("Invalid subject ID found in request");
	}
}
