package edu.stanford.isis.epadws.handlers.event;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.SearchResultUtils;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;
import edu.stanford.isis.epadws.resources.server.EventServerResource;

/**
 * Get all the events for a user
 * <p>
 * Now handled by Restlet resource {@link EventServerResource}.
 * 
 * @author amsnyder
 * 
 * @deprecated
 * @see EventServerResource
 */
@Deprecated
public class EventSearchHandler extends AbstractHandler
{

	private static final ProxyLogger log = ProxyLogger.getInstance();
	ProxyConfig config = ProxyConfig.getInstance();

	public EventSearchHandler()
	{
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException, ServletException
	{

		String method = httpServletRequest.getMethod();

		httpServletResponse.setContentType("text/plain");
		httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		request.setHandled(true);
		PrintWriter out = httpServletResponse.getWriter();

		String queryString = httpServletRequest.getQueryString();
		queryString = URLDecoder.decode(queryString, "UTF-8");

		if ("GET".equalsIgnoreCase(method)) {
			// get the query

			if (queryString != null) {
				queryString = queryString.trim();
				// Get the parameters
				String userName = getUserNameFromRequest(queryString);

				// get all the events for a user
				if (userName != null) {

					MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
					List<Map<String, String>> result = dbQueries.getEventsForUser(userName);

					out.print(new SearchResultUtils().get_EVENT_SEARCH_HEADER());

					log.info("Get the events query from ePad : " + queryString + " MySql found " + result.size() + " results.");
					// log.info("Search result: "+result.toString());

					ProxyConfig config = ProxyConfig.getInstance();
					String separator = config.getParam("fieldSeparator");

					// Write the result
					for (Map<String, String> row : result) {
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
						// log.info("line = "+sb.toString());
						out.print(sb.toString());
					}

					out.flush();

				} else {
					log.info("Bad user name.");
				}

			} else {
				log.info("NO header Query from request.");
			}
		}

		if ("POST".equalsIgnoreCase(method)) {
			log.info("EventSearchHandler received POST method");

			queryString = queryString.trim();
			// Get the parameters
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

			// post an event for a user
			if (userName != null && event_status != null && aim_uid != null && aim_uid != null && aim_name != null
					&& patient_id != null && patient_name != null && template_id != null && template_name != null
					&& plugin_name != null) {
				MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
				dbQueries.insertEventInDb(userName, event_status, aim_uid, aim_name, patient_id, patient_name, template_id,
						template_name, plugin_name);
			} else {
				log.info("EventSearchHandler received POST method : bad parameters");
			}

		}

	}

	private static String getUserNameFromRequest(String queryString)
	{
		// log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[0].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getEventStatusFromRequest(String queryString)
	{
		// log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[1].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getAimUidFromRequest(String queryString)
	{
		// log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[2].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getAimNameFromRequest(String queryString)
	{
		// log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[3].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getPatientIdFromRequest(String queryString)
	{
		// log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[4].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getPatientNameFromRequest(String queryString)
	{
		// log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[5].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getTemplateIdFromRequest(String queryString)
	{
		// log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[6].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getTemplateNameFromRequest(String queryString)
	{
		// log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[7].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getPluginNameFromRequest(String queryString)
	{
		// log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[8].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

}
