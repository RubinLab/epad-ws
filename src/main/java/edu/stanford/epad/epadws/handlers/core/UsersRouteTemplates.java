package edu.stanford.epad.epadws.handlers.core;

public class UsersRouteTemplates
{
	public static final String USER_LIST = "/users/";
	public static final String USER = USER_LIST + "{username}";
	public static final String USER_SESSIONS = USER + "/sessions/";
	public static final String USER_WORKLISTS = USER + "/worklists/";
	public static final String USER_WORKLIST = USER_WORKLISTS + "{worklistID}";
	public static final String USER_SUBJECT = USER + "/subjects/" + "{subjectID}";;
	public static final String USER_STUDY = USER + "/studies/" + "{studyUID}";;
	public static final String USER_REVIEWERS = USER + "/reviewers/";
	public static final String USER_REVIEWER = USER + "/reviewers/{username}";
	public static final String USER_REVIEWEES = USER + "/reviewees/";
	public static final String USER_REVIEWEE = USER + "/reviewees/{username}";
}
