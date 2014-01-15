/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.handlers.plugin;

import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.plugins.PluginHandlerMap;
import edu.stanford.isis.epad.common.plugins.PluginServletHandler;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * 
 * 
 * 
 * @author martin
 */
public class EPadPluginHandler extends AbstractHandler
{
	private final EPADLogger log = EPADLogger.getInstance();
	private final PluginHandlerMap pluginHandlerMap = PluginHandlerMap.getInstance();

	private static final String INVALID_METHOD_MESSAGE = "Only POST and GET methods valid for the plugin route";
	private static final String PLUGIN_NOT_FOUND_MESSAGE = "Could not find plugin";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on plugin route";
	private static final String INTERNAL_ERROR_MESSAGE = "Internal error on plugin route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;

		httpResponse.setContentType("text/plain");
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		request.setHandled(true);

		String pluginName = request.getPathInfo();
		PluginServletHandler pluginServletHandler = pluginHandlerMap.getPluginServletHandler(pluginName);

		try {
			responseStream = httpResponse.getWriter();
			if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
				if (pluginServletHandler != null) {
					String method = httpRequest.getMethod();
					if ("GET".equalsIgnoreCase(method)) {
						pluginServletHandler.doGet(httpRequest, httpResponse); // Status set by plugin
					} else if ("POST".equalsIgnoreCase(method)) {
						pluginServletHandler.doPost(httpRequest, httpResponse); // Status set by plugin
					} else {
						log.info(INVALID_METHOD_MESSAGE);
						responseStream.append(INVALID_METHOD_MESSAGE);
						writeDebugInfo(request, responseStream);
						httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET");
						httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					}
				} else {
					log.warning(PLUGIN_NOT_FOUND_MESSAGE + " " + pluginName);
					responseStream.append(PLUGIN_NOT_FOUND_MESSAGE + " " + pluginName);
					writeDebugInfo(request, responseStream);
					httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			} else {
				log.info(INVALID_SESSION_TOKEN_MESSAGE);
				responseStream.append(INVALID_SESSION_TOKEN_MESSAGE);
				httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} catch (Throwable t) {
			log.severe(INTERNAL_ERROR_MESSAGE, t);
			responseStream.append(INTERNAL_ERROR_MESSAGE);
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void writeDebugInfo(Request request, PrintWriter out)
	{
		String contextPath = request.getContextPath();
		String contentType = request.getContentType();
		String method = request.getMethod();
		String protocol = request.getProtocol();
		String queryString = request.getQueryString();
		String queryEncoding = request.getQueryEncoding();
		String remoteAddr = request.getRemoteAddr();
		String requestURI = request.getRequestURI();
		String pluginName = request.getPathInfo();

		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("Plugin Name:    ").append(pluginName).append("\n");
		sb.append("Method:         ").append(method).append("\n");
		sb.append("----------------").append("\n");
		sb.append("Context Path:   ").append(contextPath).append("\n");
		sb.append("Content Type:   ").append(contentType).append("\n");
		sb.append("Protocol:       ").append(protocol).append("\n");
		sb.append("Query String:   ").append(queryString).append("\n");
		sb.append("Query Encoding: ").append(queryEncoding).append("\n");
		sb.append("Remote Addr:    ").append(remoteAddr).append("\n");
		sb.append("Request URI:    ").append(requestURI).append("\n");

		log.info(sb.toString());
		out.append(sb.toString());
	}

	@Override
	public void doStart()
	{ // Initialize all of the plugin handlers.
		Set<String> keys = pluginHandlerMap.getAllPluginNames();
		for (String key : keys) {
			PluginServletHandler handler = pluginHandlerMap.getPluginServletHandler(key);
			try {
				log.info("init plugin: - " + handler.getName() + " - " + handler.getVersion());
				handler.init();
			} catch (ExceptionInInitializerError eiie) {
				Throwable cause = eiie.getCause();
				log.warning("Plugin: " + key + " init exception: " + key, eiie);
				log.warning("Plugin ExceptionInInitializerError caused by: ", cause);

			} catch (UnsatisfiedLinkError ule) {
				String javaLibPath = System.getProperty("java.library.path");
				log.severe("UnsatisfiedLinkError java.library.path=" + javaLibPath, ule);
			} catch (Exception e) {
				log.warning("Plugin: " + key + " failed init().", e);
			} catch (Error err) {
				log.severe("ERROR: Plugin: " + key + " failed init().", err);
			}
		}
	}

	@Override
	public void destroy()
	{
		Set<String> keys = pluginHandlerMap.getAllPluginNames();
		for (String key : keys) {
			PluginServletHandler handler = pluginHandlerMap.getPluginServletHandler(key);
			try {
				handler.destroy();
			} catch (Exception e) {
				log.warning("Plugin: " + key + " failed destroy().", e);
			}
		}
	}
}
