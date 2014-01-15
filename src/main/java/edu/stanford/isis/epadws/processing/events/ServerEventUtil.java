package edu.stanford.isis.epadws.processing.events;

import edu.stanford.isis.epadws.persistence.DatabaseOperations;
import edu.stanford.isis.epadws.persistence.Database;

public class ServerEventUtil
{
	public static void postEvent(String username, String event_status, String aimUid, String aimName, String patientID,
			String patientName, String templateID, String templateName, String pluginName)
	{
		DatabaseOperations dbQueries = Database.getInstance().getDatabaseOperations();
		dbQueries.insertEpadEvent(username, event_status, aimUid, aimName, patientID, patientName, templateID,
				templateName, pluginName);
	}
}