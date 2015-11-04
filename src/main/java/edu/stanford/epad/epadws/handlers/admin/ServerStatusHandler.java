//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
					
				//responseStream.println("Plugin Version - interface:      " + EPadPlugin.PLUGIN_INTERFACE_VERSION + "<br>");
				//responseStream.println("Plugin Version - implementation: " + ePadPlugin.getPluginImplVersion() + "<br>");
				EpadDatabase epadDatabase = EpadDatabase.getInstance();
				responseStream.println("<br>");
				//responseStream.println("ePAD database startup time: " + epadDatabase.getStartupTime() + " ms, database version: " + epadDatabase.getEPADDatabaseOperations().getDBVersion() + "<br>");
				//Dcm4CheeDatabase dcm4CheeDatabase = Dcm4CheeDatabase.getInstance();
				//responseStream.println("<br>");
				//responseStream.println("DCM4CHEE database startup time: " + dcm4CheeDatabase.getStartupTime() + " ms" + "<br>");
				//responseStream.println("<br>");
				responseStream.println("<b>Config Server:</b> " + EPADConfig.xnatServer + "<br>");
				responseStream.println("<b>Config serverProxy:</b> " + EPADConfig.getParamValue("serverProxy") + "<br>");
				responseStream.println("<b>Config webserviceBase:</b> " + EPADConfig.getParamValue("webserviceBase") + "<br>");
				responseStream.println("<b>Hostname:</b> " + InetAddress.getLocalHost().getHostName() + "<br>");
				responseStream.println("<b>IP Address:</b> " + EpadStatisticsTask.getIPAddress() + "<br>");
				if (EPADSessionWatcher.diskspacealert)
				{
					responseStream.println("<br><font size=+1 color=red><b>Low System Disk Space</b></font><br>");					
				}
				responseStream.println("<style>tbody { display: block;max-height:350px;overflow-y:auto; } </style>");
				List<EventLog> recentLogs = new ArrayList<EventLog>();
				int free = EpadDatabase.getInstance().getEPADDatabaseOperations().getFreeConnections();
				int used = EpadDatabase.getInstance().getEPADDatabaseOperations().getUsedConnections();
				if ((user != null && user.isAdmin()) || debug) {
					responseStream.println("<br>");
					responseStream.println("<b>Available DB Connections:</b> " + free + "<br>");
					responseStream.println("<b>Used DB Connections:</b> " + used + "<br>");
					responseStream.println("<br>");
					long freeHeap = Runtime.getRuntime().freeMemory();
					long totalHeap = Runtime.getRuntime().totalMemory();
					DecimalFormat df = new DecimalFormat("###,###,###");
					responseStream.println("<b>Available Heap Space:</b> " + df.format(freeHeap) + "<br>");
					responseStream.println("<b>Total Heap Space:</b> " + df.format(totalHeap) + "<br>");
					responseStream.println("<br>");
					try {
						responseStream.println("<b>dcm4chee Free Space: </b>" + df.format(FileSystemUtils.freeSpaceKb(EPADConfig.dcm4cheeDirRoot)/1024) + " Mb<br>");
						responseStream.println("<b>ePad Free Space: </b>" + df.format(FileSystemUtils.freeSpaceKb(EPADConfig.getEPADWebServerBaseDir())/1024) + " Mb<br>");
						responseStream.println("<b>Tmp Free Space: </b>" + df.format(FileSystemUtils.freeSpaceKb(System.getProperty("java.io.tmpdir"))/1024) + " Mb (Max Upload)<br>");
						if (new File("/var/lib/mysql").exists())
							responseStream.println("<b>Mysql DB Free Space: </b>" + df.format(FileSystemUtils.freeSpaceKb("/var/lib/mysql")/1024) + " Mb<br>");
					} catch (Exception x) {}
					responseStream.println("<br><b>Current Sessions: </b>" + "<br>");
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
