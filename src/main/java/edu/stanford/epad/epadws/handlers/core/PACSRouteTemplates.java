package edu.stanford.epad.epadws.handlers.core;

public class PACSRouteTemplates
{
	public static final String PACS_LIST = "/pacs/";
	public static final String PAC = PACS_LIST + "{pacid}";
	public static final String PAC_ENTITY_LIST = PAC + "/entities/";
	public static final String PAC_SUBJECT_LIST = PAC + "/subjects/";
	public static final String PAC_STUDY_LIST = PAC_SUBJECT_LIST + "{subjectid}/studies/";
	public static final String PAC_SERIES_LIST = PAC_STUDY_LIST + "{studyid}/series/";
	public static final String PAC_ENTITY = PAC_ENTITY_LIST + "{entityid}";
	public static final String PAC_QUERY_LIST = PAC + "/autoqueries/";
	public static final String PAC_QUERY = PAC + "/autoqueries/{subjectid}";
}
