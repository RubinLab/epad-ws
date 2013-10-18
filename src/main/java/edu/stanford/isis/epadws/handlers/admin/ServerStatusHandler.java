/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.handlers.admin;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.plugin.server.EPadPlugin;
import edu.stanford.isis.epad.plugin.server.impl.EPadPluginImpl;
import edu.stanford.isis.epad.plugin.server.impl.EPadProxyConfigImpl;
import edu.stanford.isis.epadws.EPadWebServerVersion;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.server.managers.pipeline.PipelineFactory;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * <code>
 * curl -v -b JSESSIOND=<id> -X GET "http://<ip>:<port>/status/"
 * </code>
 * 
 * @author martin
 */
public class ServerStatusHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();

	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";
	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error getting server status";

	private final long startTime;

	public ServerStatusHandler()
	{
		startTime = System.currentTimeMillis();
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/plain");
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
				ProxyConfig proxyConfig = ProxyConfig.getInstance();
				EPadProxyConfigImpl ePadProxyConfig = new EPadProxyConfigImpl();
				EPadPlugin ePadPlugin = new EPadPluginImpl();
				long upTime = System.currentTimeMillis() - startTime;
				long upTimeSec = upTime / 1000;

				responseStream.println();
				responseStream.println("--------------  ePAD Server Status  --------------");
				responseStream.println();
				responseStream.println("ePAD server uptime: " + upTimeSec + " second(s)");
				responseStream.println();
				responseStream.println("Version: " + EPadWebServerVersion.getBuildDate());
				responseStream.println();
				responseStream.println("Listening on: " + proxyConfig.getParam("ListenIP") + ":"
						+ proxyConfig.getParam("ListenPort"));
				responseStream.println();
				responseStream.println("Plugin Version - interface:      " + EPadPlugin.PLUGIN_INTERFACE_VERSION);
				responseStream.println("Plugin Version - implementation: " + ePadPlugin.getPluginImplVersion());
				MySqlInstance instance = MySqlInstance.getInstance();
				responseStream.println();
				responseStream.println("Database startup time: " + instance.getStartupTime() + " ms");
				responseStream.println();
				responseStream.println("epad.war serverProxy=" + ePadProxyConfig.getProxyConfigParam("serverProxy"));
				responseStream.println();
				responseStream.println("Pipeline activity level: " + getPipelineActivityLevel());

				statusCode = HttpServletResponse.SC_OK;
			} else {
				log.info(INVALID_SESSION_TOKEN_MESSAGE);
				responseStream.append(INVALID_SESSION_TOKEN_MESSAGE);
				statusCode = HttpServletResponse.SC_UNAUTHORIZED;
			}
		} catch (Throwable t) {
			log.warning(INTERNAL_EXCEPTION_MESSAGE, t);
			responseStream.append(INTERNAL_EXCEPTION_MESSAGE + ": " + t.getMessage());
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			if (responseStream != null) {
				responseStream.flush();
			}
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
