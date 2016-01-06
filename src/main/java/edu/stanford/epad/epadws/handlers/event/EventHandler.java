/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
package edu.stanford.epad.epadws.handlers.event;

import java.io.IOException;
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
import edu.stanford.epad.dtos.EPADEventMessage;
import edu.stanford.epad.dtos.EPADObjectList;
import edu.stanford.epad.dtos.TaskStatus;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.SessionService;

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

	public static final String EVENT_COMPLETE = "complete";
	public static final String EVENT_FAILED = "failed";
	
	private static int count = 999;
	
	@Override
	public void handle(String base, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/plain");
		if (request != null)					// In case handler is not called thru jetty
			request.setHandled(true);

		if (SessionService.hasValidSessionID(httpRequest)) {
			try {
				String method = httpRequest.getMethod();
				responseStream = httpResponse.getWriter();

				if ("GET".equalsIgnoreCase(method)) {
					String jsessionID = SessionService.getJSessionIDFromRequest(httpRequest);
					if (count++ >= 1000)
					{
						log.info("Get Event request with JSESSIONID " + jsessionID + " format:" + httpRequest.getParameter("format"));
						count = 0;
					}
					if (jsessionID != null) {
						String username = httpRequest.getParameter("username");
						if (username == null)
							username = SessionService.getUsernameForSession(jsessionID);
						boolean json = "json".equalsIgnoreCase(httpRequest.getParameter("format"));
						if (json)
							httpResponse.setContentType("application/json");
						findEventsForSessionID(username, responseStream, jsessionID, json);
						statusCode = HttpServletResponse.SC_OK;
					} else {
						statusCode = HandlerUtil.badRequestResponse(MISSING_JSESSIONID_MESSAGE, log);
					}
				} else if ("POST".equalsIgnoreCase(method)) {
					String queryString = httpRequest.getQueryString();
					if (queryString != null) {
						String jsessionID = SessionService.getJSessionIDFromRequest(httpRequest);
						String event_status = httpRequest.getParameter("event_status");
						String aim_uid = httpRequest.getParameter("aim_uid");
						String aim_name = httpRequest.getParameter("aim_name");
						String patient_id = httpRequest.getParameter("patient_id");
						String patient_name = httpRequest.getParameter("patient_name");
						String template_id = httpRequest.getParameter("template_id");
						String template_name = httpRequest.getParameter("template_name");
						String plugin_name = httpRequest.getParameter("plugin_name");
						String project_id = httpRequest.getParameter("project_id");
						String project_name = httpRequest.getParameter("project_name");
						String series_uid = httpRequest.getParameter("series_uid");
						String study_uid = httpRequest.getParameter("study_uid");

						log.info("Got event for AIM ID " + aim_uid + " with JSESSIONID " + jsessionID);
						if (jsessionID.indexOf(",") != -1)
							jsessionID = jsessionID.substring(0, jsessionID.indexOf(","));

						if (jsessionID != null && event_status != null && aim_uid != null && aim_uid != null && aim_name != null
								&& patient_id != null && patient_name != null && template_id != null && template_name != null
								&& plugin_name != null) {
							boolean error = false;
							if (EVENT_FAILED.equalsIgnoreCase(event_status)) error = true;
							EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
							epadDatabaseOperations.insertEpadEvent(jsessionID, event_status, aim_uid, aim_name, patient_id,
									patient_name, template_id, template_name, plugin_name, project_id, project_name, study_uid, series_uid, error);
							responseStream.flush();
							statusCode = HttpServletResponse.SC_OK;
							EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
							String username = SessionService.getUsernameForSession(jsessionID);
							Date endDate = null;
							if (EVENT_COMPLETE.equalsIgnoreCase(event_status) || EVENT_FAILED.equalsIgnoreCase(event_status))
							{
								endDate = new Date();
								projectOperations.createEventLog(username, project_id, patient_id, study_uid, series_uid, null, aim_uid, null, "Plugin " + plugin_name + ": " + event_status, "", error);
							}
							projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_PLUGIN, plugin_name.toLowerCase() + ":" + aim_uid, event_status, null, endDate);
						
						} else {
							log.warning("Required parameter missing, event_status:" + event_status +
									" aim_uid:" + aim_uid + " aim_name" + aim_name + 
									" patient_id: " + patient_id + " patient_name:" + patient_name +
									" template_id:" + template_id + " template_name:" + template_name +
									" plugin_name:"+ plugin_name +
									" project_id:"+ project_id);
							statusCode = HandlerUtil.badRequestResponse(BAD_PARAMETERS_MESSAGE, log);
						}
					} else {
						log.warning("Event parameters are all missing");
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
			try {
				responseStream = httpResponse.getWriter();
				responseStream.println("event_number, event_status, Date, aim_uid, aim_name, patient_id, patient_name, "
						+ "template_id, template_name, plugin_name, project_id");
				
				// message[PATIENT_NAME] + message[PLUGIN_NAME] + " " + message[EVENT_STATUS]
				StringBuilder sb = new StringBuilder();
				sb.append("1, ");
				sb.append("You are not logged in! ");
				sb.append("2015-XX-XX XX:XX:XX.0, ");
				sb.append(" , ");
				sb.append(" , ");
				sb.append("Invalid Session, ");
				sb.append(" , ");
				sb.append(" , ");
				sb.append(" , ");
				sb.append("Please Stop Requests., ");
				sb.append("\n");
				responseStream.print(sb.toString());
				responseStream.print(sb.toString());
				responseStream.print(sb.toString());
				if (false && System.currentTimeMillis()%3 == 0)
					statusCode = HttpServletResponse.SC_OK;
				else
					statusCode = HttpServletResponse.SC_UNAUTHORIZED;
			} catch (IOException e) {
				statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_UNAUTHORIZED, INVALID_SESSION_TOKEN_MESSAGE, log);
			}
		}
		httpResponse.setStatus(statusCode);
	}

	public static Map<String, Map<String, String>> deletedEvents = new HashMap<String, Map<String, String>>();
	
	private void findEventsForSessionID(String username, PrintWriter responseStream, String sessionID, boolean json)
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		// TODO This map should be replaced with a class describing an event.
		if (sessionID.indexOf(",") != -1)
			sessionID = sessionID.substring(0, sessionID.indexOf(","));
		List<Map<String, String>> eventMap = epadDatabaseOperations.getEpadEventsForSessionID(sessionID, true);
		List<Map<String, String>> userEvents = epadDatabaseOperations.getEpadEventsForSessionID(username, true);
		eventMap.addAll(userEvents);
		String separator = ", ";

		if (eventMap.size() == 0)
		{
			//responseStrean.println("No new events posted");
			//log.info("No new events posted");
			return;
		}
		EPADObjectList jsonList = new EPADObjectList();
		if (!json)
		{	
			responseStream.println("event_number, event_status, Date, aim_uid, aim_name, patient_id, patient_name, "
				+ "template_id, template_name, plugin_name, project_id");
		}
		
		for (Map<String, String> row : eventMap) {
			deletedEvents.put(row.get("aim_uid"), row);
			if (getTime(row.get("created_time")) < (new Date().getTime() - 5*60*1000) && !"System".equals(row.get("aim_uid")))
			{
				continue;
			}
			if (!json)
			{
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
				sb.append(row.get("plugin_name")).append(separator);
				sb.append(row.get("project_id"));
				sb.append("\n");
				responseStream.print(sb.toString());
				log.info(sb.toString());
			}
			else
			{
				EPADEventMessage event = new EPADEventMessage(new Integer(row.get("pk")), row.get("event_status"), "",
								row.get("patient_id"), row.get("patient_name"), row.get("project_id"),
								row.get("project_name"), row.get("study_uid"), row.get("series_uid"),
								row.get("aim_uid"), row.get("aim_name"),
								row.get("template_id"), row.get("template_name"), row.get("plugin_id"),
								row.get("plugin_name"), row.get("created_time"), new Boolean(row.get("error")));
				jsonList.addObject(event);

			}

		}
		if (json)
		{
			responseStream.append(jsonList.toJSON());
		}
		if (deletedEvents.size() > 100) purgeDeletedEvents();
	}
	
	private void purgeDeletedEvents()
	{
		Collection<Map<String, String>> eventMaps = deletedEvents.values();
		for (Map<String, String> eventMap: eventMaps)
		{
			if (getTime(eventMap.get("created_time")) < (new Date().getTime()- 1*60*60*1000))
			{
				deletedEvents.remove(eventMap.get("aim_uid"));
			}
			
		}
	}
	
	private static long getTime(String timestamp)
	{
		try
		{
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
			return date.getTime();
		}
		catch (Exception x)
		{
			return 0;
		}
	}
}
