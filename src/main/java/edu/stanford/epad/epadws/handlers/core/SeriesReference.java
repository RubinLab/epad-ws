package edu.stanford.epad.epadws.handlers.core;

import java.util.Map;

import edu.stanford.epad.epadws.handlers.HandlerUtil;

public class SeriesReference
{
	public final String projectID;
	public final String subjectID;
	public final String studyUID;
	public final String seriesUID;

	public SeriesReference(String projectID, String subjectID, String studyUID, String seriesUID)
	{
		this.projectID = projectID;
		this.subjectID = subjectID;
		this.studyUID = studyUID;
		this.seriesUID = seriesUID;
	}

	public static SeriesReference extract(String template, String pathInfo)
	{
		Map<String, String> templateMap = HandlerUtil.getTemplateMap(template, pathInfo);
		String projectID = HandlerUtil.getTemplateParameter(templateMap, "project", "");
		String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject", "");
		String studyUID = HandlerUtil.getTemplateParameter(templateMap, "study");
		String seriesUID = HandlerUtil.getTemplateParameter(templateMap, "series");

		ProjectReference.validateProjectID(projectID);
		SubjectReference.validateSubjectID(subjectID);
		StudyReference.validateStudyUID(studyUID);
		validateSeriesUID(seriesUID);

		return new SeriesReference(projectID, subjectID, studyUID, seriesUID);
	}

	protected static void validateSeriesUID(String seriesUID)
	{
		if (seriesUID == null)
			throw new RuntimeException("Invalid series UID found in request");
	}
}
