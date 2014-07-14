package edu.stanford.epad.epadws.handlers.core;

import java.util.Map;

import edu.stanford.epad.epadws.handlers.HandlerUtil;

public class ImageReference
{
	public final String projectID;
	public final String subjectID;
	public final String studyUID;
	public final String seriesUID;
	public final String imageUID;

	public ImageReference(String projectID, String subjectID, String studyUID, String seriesUID, String imageUID)
	{
		this.projectID = projectID;
		this.subjectID = subjectID;
		this.studyUID = studyUID;
		this.seriesUID = seriesUID;
		this.imageUID = imageUID;
	}

	public static ImageReference extract(String template, String pathInfo)
	{
		Map<String, String> templateMap = HandlerUtil.getTemplateMap(template, pathInfo);
		String projectID = HandlerUtil.getTemplateParameter(templateMap, "project", "");
		String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject", "");
		String studyUID = HandlerUtil.getTemplateParameter(templateMap, "study");
		String seriesUID = HandlerUtil.getTemplateParameter(templateMap, "series");
		String imageUID = HandlerUtil.getTemplateParameter(templateMap, "image");

		ProjectReference.validateProjectID(projectID);
		SubjectReference.validateSubjectID(subjectID);
		StudyReference.validateStudyUID(studyUID);
		SeriesReference.validateSeriesUID(seriesUID);
		validateImageUID(imageUID);

		return new ImageReference(projectID, subjectID, studyUID, seriesUID, imageUID);
	}

	protected static void validateImageUID(String imageUID)
	{
		if (imageUID == null || imageUID.equals(""))
			throw new RuntimeException("Invalid image UID found in request");
	}
}
