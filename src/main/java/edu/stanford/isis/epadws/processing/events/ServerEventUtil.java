package edu.stanford.isis.epadws.processing.events;

import edu.stanford.isis.epadws.processing.persistence.MySqlInstance;
import edu.stanford.isis.epadws.processing.persistence.MySqlQueries;

public class ServerEventUtil
{
	public static void postEvent(String username, String event_status, String aimUid, String aimName, String patientID,
			String patientName, String templateID, String templateName, String pluginName)
	{
		MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
		dbQueries.insertEvent(username, event_status, aimUid, aimName, patientID, patientName, templateID, templateName,
				pluginName);
	}
}