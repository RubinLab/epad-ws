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
import edu.stanford.isis.epad.common.util.SearchResultUtils;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * @author amsnyder
 */
public class EventSearchHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();
	private static final ProxyConfig config = ProxyConfig.getInstance();

	private static final String INVALID_METHOD_MESSAGE = "Only POST and GET methods valid for this route";
	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error";
	private static final String MISSING_USER_NAME_MESSAGE = "No user name in query";
	private static final String BAD_PARAMETERS_MESSAGE = "Missing parameters in query";
	private static final String MISSING_QUERY_MESSAGE = "No query in request";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		PrintWriter out = httpResponse.getWriter();

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
			String method = httpRequest.getMethod();
			String queryString = httpRequest.getQueryString();
			queryString = URLDecoder.decode(queryString, "UTF-8");

			if ("GET".equalsIgnoreCase(method)) {
				if (queryString != null) {
					queryString = queryString.trim();
					log.info("Events query from ePAD: " + queryString);
					String userName = getUserNameFromRequest(queryString);
					if (userName != null) {
						try {
							findEventsForUser(out, userName);
							httpResponse.setStatus(HttpServletResponse.SC_OK);
						} catch (Exception e) {
							log.info(INTERNAL_EXCEPTION_MESSAGE);
							out.append(INTERNAL_EXCEPTION_MESSAGE);
							httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						} catch (Error e) {
							log.info(INTERNAL_EXCEPTION_MESSAGE);
							out.append(INTERNAL_EXCEPTION_MESSAGE);
							httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						}
					} else {
						log.info(MISSING_USER_NAME_MESSAGE);
						out.append(MISSING_USER_NAME_MESSAGE);
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					}
				} else {
					log.info(MISSING_QUERY_MESSAGE);
					out.append(MISSING_QUERY_MESSAGE);
					httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			} else if ("POST".equalsIgnoreCase(method)) {
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
						httpResponse.setStatus(HttpServletResponse.SC_OK);
					} else {
						log.info(BAD_PARAMETERS_MESSAGE);
						out.append(BAD_PARAMETERS_MESSAGE);
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					}
				} else {
					log.info(MISSING_QUERY_MESSAGE);
					out.append(MISSING_QUERY_MESSAGE);
					httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			} else {
				log.info(INVALID_METHOD_MESSAGE);
				out.append(INVALID_METHOD_MESSAGE);
				httpResponse.setHeader("Access-Control-Allow-Methods", "POST GET");
				httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			}
		} else {
			log.info(INVALID_SESSION_TOKEN_MESSAGE);
			out.append(INVALID_SESSION_TOKEN_MESSAGE);
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		out.flush();
	}

	private void findEventsForUser(PrintWriter out, String userName)
	{
		MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
		List<Map<String, String>> result = dbQueries.getEventsForUser(userName);

		out.print(new SearchResultUtils().get_EVENT_SEARCH_HEADER());
		log.info("Event search found " + result.size() + " result(s).");

		String separator = config.getParam("fieldSeparator");
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
			out.print(sb.toString());
		}
	}

	private static String getUserNameFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[0].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getEventStatusFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[1].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getAimUidFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[2].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getAimNameFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[3].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getPatientIdFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[4].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getPatientNameFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[5].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getTemplateIdFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[6].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getTemplateNameFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[7].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getPluginNameFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[8].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}
}
