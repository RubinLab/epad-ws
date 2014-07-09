package edu.stanford.epad.epadws.handlers.core;

public class StudiesRouteTemplates
{
	public static final String STUDY_LIST = "/studies/";
	public static final String STUDY = STUDY_LIST + "{study}";
	public static final String STUDY_AIM_LIST = STUDY + "/aims/";
	public static final String STUDY_AIM = STUDY_AIM_LIST + "{aid}";
	public static final String SERIES_LIST = STUDY + "/series/";
	public static final String SERIES = SERIES_LIST + "{series}";
	public static final String SERIES_AIM_LIST = SERIES + "/aims/";
	public static final String SERIES_AIM = SERIES_AIM_LIST + "{aid}";
	public static final String IMAGE_LIST = SERIES + "/images/";
	public static final String IMAGE = IMAGE_LIST + "{image}";
	public static final String IMAGE_AIM_LIST = IMAGE + "/aims/";
	public static final String IMAGE_AIM = IMAGE_AIM_LIST + "{aid}";
	public static final String FRAME_LIST = IMAGE + "/frames/";
	public static final String FRAME = FRAME_LIST + "{frame}";
	public static final String FRAME_AIM_LIST = FRAME + "/aims/";
	public static final String FRAME_AIM = FRAME_AIM_LIST + "{aid}";
}
