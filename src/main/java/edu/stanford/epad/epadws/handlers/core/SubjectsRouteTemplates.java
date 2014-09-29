package edu.stanford.epad.epadws.handlers.core;

public class SubjectsRouteTemplates
{
	public static final String SUBJECT_LIST = "/subjects/";
	public static final String SUBJECT = SUBJECT_LIST + "{subject}";
	public static final String SUBJECT_AIM_LIST = SUBJECT + "/aims/";
	public static final String SUBJECT_AIM = SUBJECT_AIM_LIST + "{aid}";
}
