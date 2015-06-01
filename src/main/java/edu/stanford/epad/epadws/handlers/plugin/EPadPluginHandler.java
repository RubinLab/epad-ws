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
import edu.stanford.epad.epadws.service.SessionService;

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
			if (SessionService.hasValidSessionID(httpRequest)) {
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
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_ERROR_MESSAGE, t, log);
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
