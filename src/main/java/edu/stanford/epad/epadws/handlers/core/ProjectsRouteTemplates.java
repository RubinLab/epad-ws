package edu.stanford.epad.epadws.handlers.core;

public class ProjectsRouteTemplates
{
	public static final String PROJECT_LIST = "/projects/";
	public static final String PROJECT = PROJECT_LIST + "{project}";
	public static final String PROJECT_AIM_LIST = PROJECT + "/aims/";
	public static final String PROJECT_AIM = PROJECT_AIM_LIST + "{aid}";
	public static final String PROJECT_FILE_LIST = PROJECT + "/files/";
	public static final String PROJECT_FILE = PROJECT_FILE_LIST + "{filename}";
	public static final String SUBJECT_LIST = PROJECT + "/subjects/";
	public static final String SUBJECT = SUBJECT_LIST + "{subject}";
	public static final String SUBJECT_STATUS = SUBJECT + "/status/{status}";
	public static final String SUBJECT_AIM_LIST = SUBJECT + "/aims/";
	public static final String SUBJECT_AIM = SUBJECT_AIM_LIST + "{aid}";
	public static final String SUBJECT_FILE_LIST = SUBJECT + "/files/";
	public static final String SUBJECT_FILE = SUBJECT_FILE_LIST + "{filename}";
	public static final String STUDY_LIST = SUBJECT + "/studies/";
	public static final String STUDY = STUDY_LIST + "{study}";
	public static final String STUDY_AIM_LIST = STUDY + "/aims/";
	public static final String STUDY_AIM = STUDY_AIM_LIST + "{aid}";
	public static final String STUDY_FILE_LIST = STUDY + "/files/";
	public static final String STUDY_FILE = STUDY_FILE_LIST + "{filename}";
	public static final String SERIES_LIST = STUDY + "/series/";
	public static final String SERIESFILE_LIST = STUDY + "/seriesfiles/";
	public static final String SERIES = SERIES_LIST + "{series}";
	public static final String SERIES_AIM_LIST = SERIES + "/aims/";
	public static final String SERIES_AIM = SERIES_AIM_LIST + "{aid}";
	public static final String SERIES_FILE_LIST = SERIES + "/files/";
	public static final String SERIES_FILE = SERIES_FILE_LIST + "{filename}";
	public static final String IMAGE_LIST = SERIES + "/images/";
	public static final String IMAGEFILE_LIST = SERIES + "/imagefiles/";
	public static final String IMAGE = IMAGE_LIST + "{image}";
	public static final String IMAGE_AIM_LIST = IMAGE + "/aims/";
	public static final String IMAGE_AIM = IMAGE_AIM_LIST + "{aid}";
	public static final String FRAME_LIST = IMAGE + "/frames/";
	public static final String FRAME = FRAME_LIST + "{frame}";
	public static final String FRAME_AIM_LIST = FRAME + "/aims/";
	public static final String FRAME_AIM = FRAME_AIM_LIST + "{aid}";
	public static final String USER_LIST = PROJECT + "/users/";
	public static final String USER = USER_LIST + "{username}";
}
