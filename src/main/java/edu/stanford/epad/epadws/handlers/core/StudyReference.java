package edu.stanford.epad.epadws.handlers.core;

import java.util.Map;

import edu.stanford.epad.epadws.handlers.HandlerUtil;

public class StudyReference
{
	public final String projectID;
	public final String subjectID;
	public final String studyUID;

	public StudyReference(String projectID, String subjectID, String studyUID)
	{
		this.projectID = projectID;
		this.subjectID = subjectID;
		this.studyUID = studyUID;
	}

	public static StudyReference extract(String template, String pathInfo)
	{
		Map<String, String> templateMap = HandlerUtil.getTemplateMap(template, pathInfo);
		String projectID = HandlerUtil.getTemplateParameter(templateMap, "project", "");
		String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject", "");
		String studyUID = HandlerUtil.getTemplateParameter(templateMap, "study");

		ProjectReference.validateProjectID(projectID);
		SubjectReference.validateSubjectID(subjectID);
		validateStudyUID(studyUID);

		return new StudyReference(projectID, subjectID, studyUID);
	}

	protected static void validateStudyUID(String studyUID)
	{
		if (studyUID == null)
			throw new RuntimeException("Invalid study UID found in request");
	}
}
