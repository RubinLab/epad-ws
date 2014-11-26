package edu.stanford.epad.epadws.handlers.event;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * 
 * 
 * 
 * @author martin
 */
public class EventHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String INVALID_METHOD_MESSAGE = "Only POST and GET methods valid for the events route";
	private static final String INTERNAL_ERROR_MESSAGE = "Internal error on event search";
	private static final String MISSING_JSESSIONID_MESSAGE = "No session identifier in event query";
	private static final String BAD_PARAMETERS_MESSAGE = "Missing parameters in event query";
	private static final String MISSING_QUERY_MESSAGE = "No query in event request";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on event route";

	@Override
	public void handle(String base, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
			try {
				String method = httpRequest.getMethod();
				responseStream = httpResponse.getWriter();

				if ("GET".equalsIgnoreCase(method)) {
					String jsessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
					if (jsessionID != null) {
						findEventsForSessionID(responseStream, jsessionID);
						statusCode = HttpServletResponse.SC_OK;
					} else {
						statusCode = HandlerUtil.badRequestResponse(MISSING_JSESSIONID_MESSAGE, log);
					}
				} else if ("POST".equalsIgnoreCase(method)) {
					String queryString = httpRequest.getQueryString();
					if (queryString != null) {
						String jsessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
						String event_status = httpRequest.getParameter("event_status");
						String aim_uid = httpRequest.getParameter("aim_uid");
						String aim_name = httpRequest.getParameter("aim_name");
						String patient_id = httpRequest.getParameter("patient_id");
						String patient_name = httpRequest.getParameter("patient_name");
						String template_id = httpRequest.getParameter("template_id");
						String template_name = httpRequest.getParameter("template_name");
						String plugin_name = httpRequest.getParameter("plugin_name");

						log.info("Got event for AIM ID " + aim_uid + " with JSESSIONID " + jsessionID);

						if (jsessionID != null && event_status != null && aim_uid != null && aim_uid != null && aim_name != null
								&& patient_id != null && patient_name != null && template_id != null && template_name != null
								&& plugin_name != null) {
							EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
							epadDatabaseOperations.insertEpadEvent(jsessionID, event_status, aim_uid, aim_name, patient_id,
									patient_name, template_id, template_name, plugin_name);
							responseStream.flush();
							statusCode = HttpServletResponse.SC_OK;
						} else {
							statusCode = HandlerUtil.badRequestResponse(BAD_PARAMETERS_MESSAGE, log);
						}
					} else {
						statusCode = HandlerUtil.badRequestResponse(MISSING_QUERY_MESSAGE, log);
					}
				} else {
					statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_METHOD_NOT_ALLOWED, INVALID_METHOD_MESSAGE,
							log);
					httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET");
				}
			} catch (Throwable t) {
				statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, INTERNAL_ERROR_MESSAGE,
						log);
			}
		} else {
			statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_UNAUTHORIZED, INVALID_SESSION_TOKEN_MESSAGE, log);
		}
		httpResponse.setStatus(statusCode);
	}

	public static Map<String, Map<String, String>> deletedEvents = new HashMap<String, Map<String, String>>();
	
	private void findEventsForSessionID(PrintWriter responseStrean, String sessionID)
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		// TODO This map should be replaced with a class describing an event.
		List<Map<String, String>> eventMap = epadDatabaseOperations.getEpadEventsForSessionID(sessionID);
		String separator = ", ";

		responseStrean.println("event_number, event_status, Date, aim_uid, aim_name, patient_id, patient_name, "
				+ "template_id, template_name, plugin_name");

		for (Map<String, String> row : eventMap) {
			deletedEvents.put(row.get("aim_uid"), row);
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
			responseStrean.print(sb.toString());
			log.info(sb.toString());
		}
		if (deletedEvents.size() > 100) purgeDeletedEvents();
	}
	
	private void purgeDeletedEvents()
	{
		Collection<Map<String, String>> eventMaps = deletedEvents.values();
		for (Map<String, String> eventMap: eventMaps)
		{
			if (getTime(eventMap.get("created_time")) < (System.currentTimeMillis()-10*60*60*1000))
			{
				deletedEvents.remove(eventMap.get("aim_uid"));
			}
			
		}
	}
	
	private static long getTime(String timestamp)
	{
		try
		{
			Date date = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").parse(timestamp);
			return date.getTime();
		}
		catch (Exception x)
		{
			return 0;
		}
	}
}
