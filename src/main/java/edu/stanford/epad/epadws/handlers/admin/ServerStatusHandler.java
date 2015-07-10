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

import java.io.PrintWriter;
import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.plugins.EPadPlugin;
import edu.stanford.epad.common.plugins.impl.EPadPluginImpl;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.EPadWebServerVersion;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.processing.pipeline.PipelineFactory;
import edu.stanford.epad.epadws.processing.pipeline.task.EpadStatisticsTask;
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

		httpResponse.setContentType("text/plain");
		if (request != null)					// In case handler is not called thru jetty
			request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (SessionService.hasValidSessionID(httpRequest)) {
				
				EPadPlugin ePadPlugin = new EPadPluginImpl();
				long upTime = System.currentTimeMillis() - startTime;
				long upTimeSec = upTime / 1000;

				responseStream.println();
				responseStream.println("--------------  ePAD Server Status  --------------");
				responseStream.println();
				responseStream.println("ePAD server uptime: " + upTimeSec + " second(s)");
				responseStream.println();
				responseStream.println("Version: " + new EPadWebServerVersion().getVersion() + " Build Date: " + new EPadWebServerVersion().getBuildDate() + " Build Host: " + new EPadWebServerVersion().getBuildHost());
				responseStream.println();
				responseStream.println("Plugin Version - interface:      " + EPadPlugin.PLUGIN_INTERFACE_VERSION);
				responseStream.println("Plugin Version - implementation: " + ePadPlugin.getPluginImplVersion());
				EpadDatabase epadDatabase = EpadDatabase.getInstance();
				responseStream.println();
				responseStream.println("ePAD database startup time: " + epadDatabase.getStartupTime() + " ms, database version: " + epadDatabase.getEPADDatabaseOperations().getDBVersion());
				Dcm4CheeDatabase dcm4CheeDatabase = Dcm4CheeDatabase.getInstance();
				responseStream.println();
				responseStream.println("DCM4CHEE database startup time: " + dcm4CheeDatabase.getStartupTime() + " ms");
				responseStream.println();
				responseStream.println("Config Server: " + EPADConfig.xnatServer);
				responseStream.println("Config serverProxy: " + EPADConfig.getParamValue("serverProxy"));
				responseStream.println("Config webserviceBase: " + EPADConfig.getParamValue("webserviceBase"));
				responseStream.println("Hostname: " + InetAddress.getLocalHost().getHostName());
				responseStream.println("IP Address: " + EpadStatisticsTask.getIPAddress());
				responseStream.println();

				statusCode = HttpServletResponse.SC_OK;
			} else {
				log.warning(INVALID_SESSION_TOKEN_MESSAGE);
				statusCode = HandlerUtil.invalidTokenResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
		} catch (Throwable t) {
			log.warning(INTERNAL_EXCEPTION_MESSAGE, t);
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_EXCEPTION_MESSAGE, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
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
