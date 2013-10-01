/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.handlers.admin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.util.JsonHelper;
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
public class StatusHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();

	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";
	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error getting server status";

	private final long startTime;

	public StatusHandler()
	{
		startTime = System.currentTimeMillis();
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		PrintWriter out = httpResponse.getWriter();

		httpResponse.setContentType("text/plain");
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		request.setHandled(true);

		if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
			try {
				ProxyConfig proxyConfig = ProxyConfig.getInstance();
				EPadProxyConfigImpl ePadProxyConfig = new EPadProxyConfigImpl();
				EPadPlugin ePadPlugin = new EPadPluginImpl();
				long upTime = System.currentTimeMillis() - startTime;
				long upTimeSec = upTime / 1000;

				out.println();
				out.println("--------------  DicomProxy status  --------------");
				out.println();
				out.println("ePAD server uptime: " + upTimeSec + " sec");
				out.println();
				out.println("Version: " + EPadWebServerVersion.getBuildDate());
				out.println();
				out.println("Listening on: " + proxyConfig.getParam("ListenIP") + ":" + proxyConfig.getParam("ListenPort"));
				out.println();
				out.println("Plugin Version - interface:      " + EPadPlugin.PLUGIN_INTERFACE_VERSION);
				out.println("Plugin Version - implementation: " + ePadPlugin.getPluginImplVersion());
				MySqlInstance instance = MySqlInstance.getInstance();
				out.println();
				out.println("DB Startup Time: " + instance.getStartupTime() + " ms");
				out.println();
				out.println("epad.war serverProxy=" + ePadProxyConfig.getProxyConfigParam("serverProxy"));
				out.println();
				out.println("pipelineActivity : " + getPipelineActivityLevel());

				httpResponse.setStatus(HttpServletResponse.SC_OK);
			} catch (Exception e) {
				log.warning(INTERNAL_EXCEPTION_MESSAGE, e);
				httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				out.append(JsonHelper.createJSONErrorResponse(INTERNAL_EXCEPTION_MESSAGE, e));
			} catch (Error e) {
				log.warning(INTERNAL_EXCEPTION_MESSAGE, e);
				httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				out.append(JsonHelper.createJSONErrorResponse(INTERNAL_EXCEPTION_MESSAGE, e));
			}
		} else {
			log.info(INVALID_SESSION_TOKEN_MESSAGE);
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			out.append(JsonHelper.createJSONErrorResponse(INVALID_SESSION_TOKEN_MESSAGE));
		}

		out.flush();
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
