package edu.stanford.epad.epadws.handlers.core;

public class PACSRouteTemplates
{
	public static final String PACS_LIST = "/pacs/";
	public static final String PAC = PACS_LIST + "{pacid}";
	public static final String PAC_ENTITY_LIST = PAC + "/entities/";
	public static final String PAC_ENTITY = PAC_ENTITY_LIST + "{entityid}";
}
