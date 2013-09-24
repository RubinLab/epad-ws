package edu.stanford.isis.epadws.resources.server;

import java.util.List;
import java.util.Map;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import edu.stanford.isis.epad.common.util.SearchResultUtils;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;

public class EventServerResource extends BaseServerResource
{
	private static final String INSERT_SUCCESS_MESSAGE = "Event created successfully";
	private static final String MISSING_QUERY_MESSAGE = "No query paramaters in request";
	private static final String MISSING_USER_NAME_MESSAGE = "No user name in request";
	private static final String MISSING_PARAMETERS_MESSAGE = "No paramaters in request";

	public EventServerResource()
	{
		setNegotiated(false); // Disable content negotiation
	}

	@Override
	protected void doCatch(Throwable throwable)
	{
		log.warning("An exception was thrown in the event server resource.", throwable);
	}

	@Get("text")
	public String queryEvent()
	{
		String queryString = getQuery().getQueryString(CharacterSet.UTF_8);

		if (queryString != null) {
			String userName = getUserNameFromRequest(queryString.trim());
			if (userName != null) { // Get all the events for a user
				log.info("Got events query from ePad : " + queryString);
				String out = executeEventQuery(userName);
				log.info(INSERT_SUCCESS_MESSAGE);
				setStatus(Status.SUCCESS_OK);
				return out.toString();
			} else {
				log.info(MISSING_USER_NAME_MESSAGE);
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return MISSING_USER_NAME_MESSAGE;
			}
		} else {
			log.info(MISSING_QUERY_MESSAGE);
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return MISSING_QUERY_MESSAGE;
		}
	}

	@Post("text")
	public String insertEvent()
	{
		log.info("EventSearchHandler received POST method");

		String queryString = getQuery().getQueryString(CharacterSet.UTF_8);

		if (queryString != null) {
			queryString = queryString.trim();
			String userName = getUserNameFromRequest(queryString);
			String event_status = getEventStatusFromRequest(queryString);
			String aim_uid = getAimUidFromRequest(queryString);
			String aim_name = getAimNameFromRequest(queryString);
			String patient_id = getPatientIdFromRequest(queryString);
			String patient_name = getPatientNameFromRequest(queryString);
			String template_id = getTemplateIdFromRequest(queryString);
			String template_name = getTemplateNameFromRequest(queryString);
			String plugin_name = getPluginNameFromRequest(queryString);

			log.info("Save event : " + aim_uid + " for user " + userName);

			if (userName != null && event_status != null && aim_uid != null && aim_uid != null && aim_name != null
					&& patient_id != null && patient_name != null && template_id != null && template_name != null
					&& plugin_name != null) {
				MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
				dbQueries.insertEventInDb(userName, event_status, aim_uid, aim_name, patient_id, patient_name, template_id,
						template_name, plugin_name);

				log.info(INSERT_SUCCESS_MESSAGE);
				setStatus(Status.SUCCESS_CREATED);
				return INSERT_SUCCESS_MESSAGE;
			} else {
				log.info(MISSING_PARAMETERS_MESSAGE);
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return MISSING_PARAMETERS_MESSAGE;
			}
		} else {
			log.info(MISSING_QUERY_MESSAGE);
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return MISSING_QUERY_MESSAGE;
		}
	}

	private String executeEventQuery(String userName)
	{
		StringBuilder out = new StringBuilder();
		MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
		List<Map<String, String>> result = dbQueries.getEventsForUser(userName);

		out.append(new SearchResultUtils().get_EVENT_SEARCH_HEADER());
		log.info("Found " + result.size() + " results.");
		String separator = config.getParam("fieldSeparator");

		for (Map<String, String> row : result) { // Write the result
			StringBuilder sb = new StringBuilder();
			sb.append(row.get("pk")).append(separator);
			sb.append(row.get("event_status")).append(separator);
			sb.append(row.get("created_time")).append(separator);
			sb.append(row.get("aim_uid")).append(separator);
			sb.append(row.get("aim_name")).append(separator);
			sb.append(row.get("patient_id")).append(separator);
			sb.append(row.get("patient_name")).append(separator);
			sb.append(row.get("template_id")).append(separator);
			sb.append(row.get("template_name")).append(separator);
			sb.append(row.get("plugin_name"));
			sb.append("\n");
			out.append(sb.toString());
		}
		return out.toString();
	}

	// TODO Look at these methods. Not sure why they are manually processing query.
	private String getUserNameFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[0].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private String getEventStatusFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[1].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private String getAimUidFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[2].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private String getAimNameFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[3].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private String getPatientIdFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[4].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private String getPatientNameFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[5].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private String getTemplateIdFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[6].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private String getTemplateNameFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[7].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private String getPluginNameFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[8].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}
}
