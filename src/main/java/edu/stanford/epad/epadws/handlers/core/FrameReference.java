package edu.stanford.epad.epadws.handlers.core;

import java.util.Map;

import edu.stanford.epad.epadws.handlers.HandlerUtil;

public class FrameReference
{
	public final String projectID;
	public final String subjectID;
	public final String studyUID;
	public final String seriesUID;
	public final String imageUID;
	public final int frameNumber;

	public FrameReference(String projectID, String subjectID, String studyUID, String seriesUID, String imageUID,
			int frameNumber)
	{
		this.projectID = projectID;
		this.subjectID = subjectID;
		this.studyUID = studyUID;
		this.seriesUID = seriesUID;
		this.imageUID = imageUID;
		this.frameNumber = frameNumber;
	}

	public static FrameReference extract(String template, String pathInfo)
	{
		Map<String, String> templateMap = HandlerUtil.getTemplateMap(template, pathInfo);
		String projectID = HandlerUtil.getTemplateParameter(templateMap, "project", "");
		String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subject", "");
		String studyUID = HandlerUtil.getTemplateParameter(templateMap, "study");
		String seriesUID = HandlerUtil.getTemplateParameter(templateMap, "series");
		String imageUID = HandlerUtil.getTemplateParameter(templateMap, "image");
		String frameNumberString = HandlerUtil.getTemplateParameter(templateMap, "frame");

		ProjectReference.validateProjectID(projectID);
		SubjectReference.validateSubjectID(subjectID);
		StudyReference.validateStudyUID(studyUID);
		SeriesReference.validateSeriesUID(seriesUID);
		ImageReference.validateImageUID(imageUID);
		int frameNumber = validateFrameNumber(frameNumberString);

		return new FrameReference(projectID, subjectID, studyUID, seriesUID, imageUID, frameNumber);
	}

	protected static int validateFrameNumber(String frameNumberString)
	{
		if (frameNumberString == null)
			throw new RuntimeException("Missing frame number in request");
		else {
			try {
				return Integer.parseInt(frameNumberString);
			} catch (NumberFormatException e) {
				throw new RuntimeException("Invalid frame number " + frameNumberString);
			}
		}
	}
}
