/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.handlers.plugin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.plugin.server.PluginServletHandler;
import edu.stanford.isis.epad.plugin.server.impl.PluginHandlerMap;
import edu.stanford.isis.epadws.resources.server.PluginServerResource;

/**
 * The handler for plugins.
 * <p>
 * Now handled by Restlet resource {@link PluginServerResource}.
 * 
 * @see PluginServerResource
 */
public class EPadPluginHandler extends AbstractHandler
{
	private final ProxyLogger logger = ProxyLogger.getInstance();
	private final PluginHandlerMap pluginHandlerMap = PluginHandlerMap.getInstance();

	@Override
	public void doStart()
	{
		// init all of the plugin handlers.
		Set<String> keys = pluginHandlerMap.getAllPluginNames();
		for (String key : keys) {
			PluginServletHandler handler = pluginHandlerMap.getPluginServletHandler(key);
			try {
				logger.info("init plugin: - " + handler.getName() + " - " + handler.getVersion());
				handler.init();
			} catch (ExceptionInInitializerError eiie) {
				Throwable cause = eiie.getCause();
				logger.warning("Plugin: " + key + " init exception: " + key, eiie);
				logger.warning("Plugin ExceptionInInitializerError caused by: ", cause);

			} catch (UnsatisfiedLinkError ule) {
				String javaLibPath = System.getProperty("java.library.path");
				logger.sever("UnsatisfiedLinkError java.library.path=" + javaLibPath, ule);
			} catch (Exception e) {
				logger.warning("Plugin: " + key + " failed init().", e);
			} catch (Error err) {
				logger.sever("ERROR: Plugin: " + key + " failed init().", err);
			}
		}
	}

	@Override
	public void destroy()
	{
		// init all of the plugin handlers.
		Set<String> keys = pluginHandlerMap.getAllPluginNames();
		for (String key : keys) {
			PluginServletHandler handler = pluginHandlerMap.getPluginServletHandler(key);
			try {
				handler.destroy();
			} catch (Exception e) {
				logger.warning("Plugin: " + key + " failed destroy().", e);
			}
		}
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res) throws IOException,
			ServletException
	{
		res.setContentType("text/plain");
		res.setStatus(HttpServletResponse.SC_OK);
		res.setHeader("Access-Control-Allow-Origin", "*");
		request.setHandled(true);

		// Find this plugin.
		String pluginName = request.getPathInfo();
		PluginServletHandler pluginHandler = pluginHandlerMap.getPluginServletHandler(pluginName);

		if (pluginHandler != null) {
			String method = req.getMethod();
			if ("get".equalsIgnoreCase(method)) {
				pluginHandler.doGet(req, res);
			} else if ("post".equalsIgnoreCase(method)) {
				pluginHandler.doPost(req, res);
			} else {
				send404ErrorMessage(request, res, "Plugin " + pluginName + " doesn't implement method: " + method);
			}
		} else {
			// plugin was not found.
			send404ErrorMessage(request, res, "Plugin " + pluginName + " not found.");
		}
	}

	private void send404ErrorMessage(Request request, HttpServletResponse res, String errorMessage)
	{
		try {
			PrintWriter out = res.getWriter();
			res.setStatus(404);
			out.print("404 Error: " + errorMessage);
			out.print(printDebugInfo(request));
			out.flush();
			out.close();
		} catch (IOException ioe) {
			logger.sever("Error", ioe);
		}
	}

	private String printDebugInfo(Request request)
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

		logger.info(sb.toString());
		return sb.toString();
	}

}
