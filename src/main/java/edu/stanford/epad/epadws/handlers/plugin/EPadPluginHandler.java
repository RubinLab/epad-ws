/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.epad.epadws.handlers.plugin;

import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.plugins.PluginHandlerMap;
import edu.stanford.epad.common.plugins.PluginServletHandler;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

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
		int statusCode;

		httpResponse.setContentType("text/plain");
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		request.setHandled(true);

		String pluginName = request.getPathInfo();
		PluginServletHandler pluginServletHandler = pluginHandlerMap.getPluginServletHandler(pluginName);

		try {
			PrintWriter responseStream = httpResponse.getWriter();
			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				if (pluginServletHandler != null) {
					String method = httpRequest.getMethod();
					if ("GET".equalsIgnoreCase(method)) {
						statusCode = pluginServletHandler.doGet(httpRequest, httpResponse); // Status set by plugin
					} else if ("POST".equalsIgnoreCase(method)) {
						statusCode = pluginServletHandler.doPost(httpRequest, httpResponse); // Status set by plugin
					} else {
						statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_METHOD_NOT_ALLOWED, INVALID_METHOD_MESSAGE,
								log);
						httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET");
					}
				} else {
					statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_NOT_FOUND, PLUGIN_NOT_FOUND_MESSAGE + " "
							+ pluginName, log);
				}
			} else {
				statusCode = HandlerUtil.invalidTokenResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_ERROR_MESSAGE, log);
		}
		httpResponse.setStatus(statusCode);
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
