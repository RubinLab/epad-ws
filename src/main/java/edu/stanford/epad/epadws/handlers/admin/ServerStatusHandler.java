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
package edu.stanford.epad.epadws.handlers.admin;

import java.io.File;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileSystemUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.TaskStatus;
import edu.stanford.epad.epadws.EPadWebServerVersion;
import edu.stanford.epad.epadws.Main;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.models.EventLog;
import edu.stanford.epad.epadws.models.Plugin;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.processing.pipeline.PipelineFactory;
import edu.stanford.epad.epadws.processing.pipeline.task.EpadStatisticsTask;
import edu.stanford.epad.epadws.processing.pipeline.watcher.EPADSessionWatcher;
import edu.stanford.epad.epadws.processing.pipeline.watcher.QueueAndWatcherManager;
import edu.stanford.epad.epadws.security.EPADSession;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.PluginOperations;
import edu.stanford.epad.epadws.service.SessionService;

/**
 * <code>
 * curl -v -b JSESSIOND=<id> -X GET "http://<ip>:<port>/epad/status/"
 * </code>
 * 
 * @author martin
 */
public class ServerStatusHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on status route";
	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error getting server status";

	private static Long startTime = null;

	public ServerStatusHandler()
	{
		if (startTime == null) startTime = System.currentTimeMillis();
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/html");
		if (request != null)					// In case handler is not called thru jetty
			request.setHandled(true);

		try {
			boolean debug = "true".equalsIgnoreCase(httpRequest.getParameter("system_debug"));
			responseStream = httpResponse.getWriter();
			boolean validSession = SessionService.hasValidSessionID(httpRequest);
			User user = null;
			String sessionID = null;
			String username = null;
			if (validSession) {				
				sessionID = SessionService.getJSessionIDFromRequest(httpRequest);
				username = EPADSessionOperations.getSessionUser(sessionID);
				user = DefaultEpadProjectOperations.getInstance().getUser(username);
				long upTime = System.currentTimeMillis() - startTime;
				long upTimeHr = upTime / (1000*60*60);
				long remain = upTime % (1000*60*60);
				long upTimeMin = remain / (1000*60);
				long upTimeSec = remain % (1000*60);
				upTimeSec = upTimeSec / 1000;
				responseStream.println("<body  topmargin='10px' leftmargin='10px' width='900px'>");
				String path = httpRequest.getContextPath().replace("status/", "").replace("status", "");
				if (!path.endsWith("/")) path = path + "/";
				responseStream.println("<a href=\"javascript:window.parent.location='" + path + "Web_pad.html'\"><b>Back to ePAD</b></a>");
				if (Main.testPages && user != null && user.isAdmin())
					responseStream.println("<div style='float:right'><a href=\"javascript:window.parent.location='" + path + "test/index.jsp'\"><b>Test Pages</b></a></div>");
				responseStream.println("<hr>");
				responseStream.println("<h3><center>ePAD Server Status</center></h3>");
				responseStream.println("<hr>");
				responseStream.println("<b>ePAD server started:</b> " + new Date(startTime) + "<br>");
				responseStream.println("<b>ePAD server uptime:</b> " + upTimeHr + " hrs " + upTimeMin + " mins " + upTimeSec + " secs<br>");
				responseStream.println("<br>");
			}
			if (!validSession)
				responseStream.println("Version: " + new EPadWebServerVersion().getVersion() + " Build Date: " + new EPadWebServerVersion().getBuildDate() + " Build Host: " + new EPadWebServerVersion().getBuildHost() + "<br>");
			if (validSession || debug) {
				responseStream.println("<b>Version:</b> " + new EPadWebServerVersion().getVersion() + " <b>Build Date:</b> " + new EPadWebServerVersion().getBuildDate() + " <b>Build Host:</b> " + new EPadWebServerVersion().getBuildHost() + "<br>");
					
				//responseStream.println("Plugin Version - interface:      " + EPadPlugin.PLUGIN_INTERFACE_VERSION + "<br>");
				//responseStream.println("Plugin Version - implementation: " + ePadPlugin.getPluginImplVersion() + "<br>");
				EpadDatabase epadDatabase = EpadDatabase.getInstance();
				responseStream.println("<table width=50%>");
				//responseStream.println("ePAD database startup time: " + epadDatabase.getStartupTime() + " ms, database version: " + epadDatabase.getEPADDatabaseOperations().getDBVersion() + "<br>");
				//Dcm4CheeDatabase dcm4CheeDatabase = Dcm4CheeDatabase.getInstance();
				//responseStream.println("<br>");
				//responseStream.println("DCM4CHEE database startup time: " + dcm4CheeDatabase.getStartupTime() + " ms" + "<br>");
				//responseStream.println("<br>");
				responseStream.println("<tr><td colspan=2><hr></td></tr>");
				responseStream.println("<tr><td><b>Config Server:</b></td><td nowrap>" + EPADConfig.xnatServer + "</td></tr>");
				responseStream.println("<tr><td><b>Config serverProxy:</b></td><td nowrap>" + EPADConfig.getParamValue("serverProxy") + "</td></tr>");
				responseStream.println("<tr><td><b>Config webserviceBase:</b></td><td nowrap>" + EPADConfig.getParamValue("webserviceBase") + "</td></tr>");
				responseStream.println("<tr><td><b>Hostname:</b></td><td>" + InetAddress.getLocalHost().getHostName() + "</td></tr>");
				responseStream.println("<tr><td><b>IP Address:</b></td><td>" + EpadStatisticsTask.getIPAddress() + "</td></tr>");
				if (EPADSessionWatcher.diskspacealert)
				{
					responseStream.println("<tr><td colspan=2><font size=+1 color=red><b>Low System Disk Space</b></font></td></tr>");					
				}
				responseStream.println("<style>tbody { display: block;max-height:440px;overflow-y:auto; } </style>");
				List<EventLog> recentLogs = new ArrayList<EventLog>();
				int free = EpadDatabase.getInstance().getEPADDatabaseOperations().getFreeConnections();
				int used = EpadDatabase.getInstance().getEPADDatabaseOperations().getUsedConnections();
				if ((user != null && user.isAdmin()) || debug) {
					responseStream.println("<tr><td colspan=2><hr></td></tr>");
					responseStream.println("<tr><td nowrap><b>Available DB Connections:</b></td><td>" + free + "</td></tr>");
					responseStream.println("<tr><td><b>Used DB Connections:</b></td><td>" + used + "</td></tr>");
					responseStream.println("<tr><td colspan=2><hr></td></tr>");
					long freeHeap = Runtime.getRuntime().freeMemory();
					long totalHeap = Runtime.getRuntime().totalMemory();
					DecimalFormat df = new DecimalFormat("###,###,###");
					responseStream.println("<tr><td><b>Available Heap Space:</b></td><td>" + df.format(freeHeap) + "</td></tr>");
					responseStream.println("<tr><td><b>Total Heap Space:</b></td><td>" + df.format(totalHeap) + "</td></tr>");
					responseStream.println("<tr><td colspan=2><hr></td></tr>");
					try {
						responseStream.println("<tr><td><b>dcm4chee Free Space: </b></td><td>" + df.format(FileSystemUtils.freeSpaceKb(EPADConfig.dcm4cheeDirRoot)/1024) + " Mb</td></tr>");
						responseStream.println("<tr><td><b>ePad Free Space: </b></td><td>" + df.format(FileSystemUtils.freeSpaceKb(EPADConfig.getEPADWebServerBaseDir())/1024) + " Mb</td></tr>");
						responseStream.println("<tr><td><b>Tmp Free Space: </b></td><td>" + df.format(FileSystemUtils.freeSpaceKb(System.getProperty("java.io.tmpdir"))/1024) + " Mb (Max Upload)</td></tr>");
						if (new File("/var/lib/mysql").exists())
							responseStream.println("<tr><td><b>Mysql DB Free Space: </b></td><td>" + df.format(FileSystemUtils.freeSpaceKb("/var/lib/mysql")/1024) + " Mb</td></tr>");
					} catch (Exception x) {}
					responseStream.println("<tr><td colspan=2><hr></td></tr>");
					responseStream.println("<tr><td><b>Series Queue:</b></td><td>" + QueueAndWatcherManager.dicomSeriesWatcherQueue.size() + "</td></tr>");
					responseStream.println("<tr><td><b>PNG Queue:</b></td><td>" + QueueAndWatcherManager.pngGeneratorTaskQueue.size() + "</td></tr>");
					responseStream.println("<tr><td><b>AddToProject Queue:</b></td><td>" + QueueAndWatcherManager.xnatSeriesWatcherQueue.size() + "</td></tr>");
					responseStream.println("<tr><td colspan=2><hr></td></tr>");
					responseStream.println("</table>");
					List<Plugin> plugins = PluginOperations.getInstance().getPlugins();
					responseStream.println("<br>");
					responseStream.println("<b>Loaded Plugins:</b>");
					responseStream.println("<br><table border=1 cellpadding=2><tr style='font-weight: bold;'>" + 
							"<td align=center>ID</td><td align=center>Name</td><td align=center>Description</td>" +
							"<td align=center>Javaclass</td><td align=center>Enabled</td><td align=center>Status</td><td align=center>Modality</td></tr>");
					for (Plugin plugin: plugins)
					{
						responseStream.println("<tr><td>" + plugin.getPluginId() + "</td><td>" + plugin.getName() + "</td><td>" + checkNull(plugin.getDescription(), "&nbsp;") + "</td>");
						responseStream.println("<td>" + plugin.getJavaclass() + "</td><td>" + plugin.getEnabled() + "</td><td>" + checkNull(plugin.getStatus(), "&nbsp;") + "</td><td>" + checkNull(plugin.getModality(), "&nbsp;") + "</td></tr>");
					}
					responseStream.println("</table>");
					responseStream.println("<br><b>Current Sessions: </b>");
					Map<String, EPADSession> sessions = EPADSessionOperations.getCurrentSessions();
					responseStream.println("<br><table border=1 cellpadding=2 ><tbody><tr style='font-weight: bold;'><td align=center>User</td><td align=center>Started</td><td align=center>Client</td><td align=center>Last Request</td><td align=center>Request Time</td></tr>");
					for (String id: sessions.keySet()) {
						String you = "";
						if (id.equals(sessionID))
							you = "*";
						EPADSession session = sessions.get(id);
						responseStream.println("<tr><td>" + session.getUsername() + you + "</td><td>" + shortformat.format(session.getCreatedTime()) + "</td><td>" + session.getRemoteHost() + "/" + session.getRemoteAddr() + "</td><td>" + session.getLastRequest() + "</td><td>" + shortformat.format(session.getLastActivity()) + "</td></tr>");
					}
					responseStream.println("</table>");
					Collection<User> users = DefaultEpadProjectOperations.getUserCache();
					responseStream.println("<br><b>Background Tasks: </b>");
					responseStream.println("<br><table border=1 cellpadding=2 ><tbody><tr style='font-weight: bold;'><td align=center>User</td><td align=center>Task</td><td align=center>Start</td><td align=center>Complete</td><td align=center>Elapsed</td><td align=center>Target</td><td align=center>Status</td></tr>");
					boolean empty = true;
					for (User u: users)
					{
						Collection<TaskStatus> tssCol = u.getCurrentTasks().values();
						List<TaskStatus> tss = new ArrayList<TaskStatus>();
						tss.addAll(tssCol);
						Collections.sort(tss, new TSComparator());
						for (TaskStatus ts: tss)
						{
							responseStream.println("<tr><td>" + u.getUsername() + "</td><td>" + ts.type + "</td><td>" + ts.starttime + "</td><td>" + checkNull(ts.completetime, "In Process") + "</td><td>" + getDiff(getDate(ts.starttime), getDate(ts.completetime)) + "</td><td>" + ts.target + "</td><td style='max-width:500px; max-height:40px; overflow-y:auto'>" + ts.status + "</td></tr>");
							empty = false;
						}
					}
					if (empty)
						responseStream.println("<tr><td colspan=100% align=center>No background processes running</td></tr>");
					responseStream.println("</tbody></table>");
					if (free > 0 && !debug)
						recentLogs = DefaultEpadProjectOperations.getInstance().getUseEventLogs("%", 0, 100);
				}  else {
					responseStream.println("<br><b>Background Tasks: </b>");
					responseStream.println("<br><table border=1 cellpadding=2 ><tbody><tr style='font-weight: bold;'><td align=center>User</td><td align=center>Task</td><td align=center>Start</td><td align=center>Complete</td><td align=center>Elapsed</td><td align=center>Target</td><td align=center>Status</td></tr>");
					Collection<TaskStatus> tssCol = user.getCurrentTasks().values();
					List<TaskStatus> tss = new ArrayList<TaskStatus>();
					tss.addAll(tssCol);
					Collections.sort(tss, new TSComparator());
					for (TaskStatus ts: tss)
					{
						responseStream.println("<tr><td>" + user.getUsername() + "</td><td>" + ts.type + "</td><td>" + ts.starttime + "</td><td>" + checkNull(ts.completetime, "In Process") + "</td><td>" + getDiff(getDate(ts.starttime), getDate(ts.completetime)) + "</td><td>" + ts.target + "</td><td>" + ts.status + "</td></tr>");
					}
					if (tss.size() == 0)
						responseStream.println("<tr><td colspan=100% align=center>No background processes running</td></tr>");
					responseStream.println("</tbody></table>");
					if (free > 0)
						recentLogs = DefaultEpadProjectOperations.getInstance().getUseEventLogs(username, 0, 100);
				}
				responseStream.println("<br><b>Event Logs: </b>");
				responseStream.println("<br><table border=1 cellpadding=2><tbody><tr style='font-weight: bold;'><td align=center>Time</td><td align=center>User</td><td align=center>Action</td><td align=center>Project</td><td align=center >Target</td></tr>");
				for (EventLog elog: recentLogs)
				{
					responseStream.println("<tr style='max-height:40px;'><td nowrap>" + dateformat.format(elog.getCreatedTime()) 
								+ "</td><td>" + elog.getUsername() + "</td><td nowrap>" + elog.getFunction() 
								+ "</td><td nowrap>" + checkNull(elog.getProjectID(), "N/A") 
								+ "</td><td><div style='max-width:500px; max-height:40px; overflow-y:auto'>" + checkNull(elog.getTarget(), "N/A") + "</div></td></tr>");
				}
				responseStream.println("</tbody></table>");
				
				responseStream.println("</body>");
				} 
				statusCode = HttpServletResponse.SC_OK;
			} catch (Throwable t) {
				log.warning(INTERNAL_EXCEPTION_MESSAGE, t);
				statusCode = HandlerUtil.internalErrorResponse(INTERNAL_EXCEPTION_MESSAGE, responseStream, log);
			}
			httpResponse.setStatus(statusCode);
	}
	
	public static String checkNull(String value, String defaultValue)
	{
		if (value == null)
			return defaultValue;
		else
			return value;
	}
	
	static SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	public static Date getDate(String dateStr)
	{
		if (dateStr == null || dateStr.length() == 0)
			return new Date();
		try
		{
			return dateformat.parse(dateStr);
		}
		catch (Exception x) {}
		return null;
	}
	
	static SimpleDateFormat shortformat = new SimpleDateFormat("MM/dd HH:mm:ss");
	
	public static String getDiff(Date start, Date end)
	{
		if (start == null)
			return null;
		if (end == null) end = new Date();
		long ms = end.getTime() - start.getTime();
		String diff = ms/(1000*60) + " mins " + ms%(1000*60)/1000  + " secs";
		return diff;
	}
	
	public class TSComparator implements Comparator<TaskStatus> {
		public int compare(TaskStatus o1, TaskStatus o2) {
			try
			{
//				if (o2.completetime != null)
//					return getDate(o2.completetime).compareTo(getDate(o1.completetime));
//				else
					return getDate(o2.starttime).compareTo(getDate(o1.starttime));
			} catch (Exception x) {}
			return 0;
		}
	}

	private String getPipelineActivityLevel()
	{
		PipelineFactory pipelineFactory = PipelineFactory.getInstance();
		StringBuilder sb = new StringBuilder();
		int activityLevel = pipelineFactory.getActivityLevel();
		int errCount = pipelineFactory.getErrorFileCount();
		if (activityLevel == 0) {
			sb.append("idle.");
		} else {
			sb.append("active-" + activityLevel);
			if (errCount > 0) {
				sb.append(" errors-" + errCount);
			}
		}
		return sb.toString();
	}
}
