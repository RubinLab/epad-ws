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
package edu.stanford.epad.epadws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.DispatcherType;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.xml.sax.SAXException;

import edu.stanford.epad.common.plugins.PluginConfig;
import edu.stanford.epad.common.plugins.PluginController;
import edu.stanford.epad.common.plugins.PluginHandlerMap;
import edu.stanford.epad.common.plugins.PluginServletHandler;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.admin.ConvertAIM4Handler;
import edu.stanford.epad.epadws.handlers.admin.ImageCheckHandler;
import edu.stanford.epad.epadws.handlers.admin.ImageReprocessingHandler;
import edu.stanford.epad.epadws.handlers.admin.ResourceCheckHandler;
import edu.stanford.epad.epadws.handlers.admin.ResourceFailureLogHandler;
import edu.stanford.epad.epadws.handlers.admin.ServerStatusHandler;
import edu.stanford.epad.epadws.handlers.admin.StatisticsHandler;
import edu.stanford.epad.epadws.handlers.admin.XNATSyncHandler;
import edu.stanford.epad.epadws.handlers.aim.AimResourceHandler;
import edu.stanford.epad.epadws.handlers.coordination.CoordinationHandler;
import edu.stanford.epad.epadws.handlers.core.EPADHandler;
import edu.stanford.epad.epadws.handlers.dicom.WadoHandler;
import edu.stanford.epad.epadws.handlers.event.EventHandler;
import edu.stanford.epad.epadws.handlers.event.ProjectEventHandler;
import edu.stanford.epad.epadws.handlers.plugin.EPadPluginHandler;
import edu.stanford.epad.epadws.handlers.session.EPADSessionHandler;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownHookThread;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.epad.epadws.processing.pipeline.watcher.QueueAndWatcherManager;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.RemotePACService;

/**
 * Entry point for the ePAD Web Service.
 * <p>
 * Start an embedded Jetty server and all the threads required for this application. The application listens on the port
 * indicated by the property <i>ePadClientPort</i> in proxy-config.properties.
 * <p>
 * NOTE: The current directory must be set to the ePAD bin subdirectory (~epad/DicomProxy/bin) before calling the start
 * scripts associated with this application. Code in the WAR file needs to be updated to remove this restriction.
 */
public class Main
{
	private static final EPADLogger log = EPADLogger.getInstance();
	
	public static boolean separateWebServicesApp = false;

	
	public static void main(String[] args)
	{
		ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
		Server server = null;

		try {
			log.info("#####################################################");
			log.info("############# Starting ePAD Web Service #############");
			log.info("#####################################################");

			int epadPort = EPADConfig.epadPort;
			separateWebServicesApp = "true".equalsIgnoreCase(EPADConfig.getParamValue("SeparateWebServicesApp"));
			if (!separateWebServicesApp) {
				initializePlugins();
				startSupportThreads();
			}
			server = new Server(epadPort);
			configureJettyServer(server);
			addHandlers(server);
			Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
			log.info("Starting Jetty on port " + epadPort);
			server.start();
			server.join();
		} catch (BindException be) {
			log.severe("Bind exception", be);
			Throwable t = be.getCause();
			log.warning("Bind exception cause: " + be.getMessage(), t);
		} catch (SocketException se) {
			log.severe("Cannot bind to all sockets", se);
		} catch (Exception e) {
			log.severe("Fatal Exception. Shutting down ePAD Web Service", e);
		} catch (Error err) {
			log.severe("Fatal Error. Shutting down ePAD Web Service", err);
		} finally {
			log.info("#####################################################");
			log.info("############# Shutting down ePAD Web Service ########");
			log.info("#####################################################");

			shutdownSignal.shutdownNow();
			stopServer(server);
			if (!separateWebServicesApp) {
				EpadDatabase.getInstance().shutdown();
				QueueAndWatcherManager.getInstance().shutdown();
			}
			try { // Wait just long enough for some messages to be printed out.
				TimeUnit.MILLISECONDS.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		log.info("#####################################################");
		log.info("################## Exit ePAD Web Service ###########");
		log.info("#####################################################");
	}

	private static void configureJettyServer(Server server)
	{
		FileInputStream jettyConfigFileStream = null;
		try {
			String jettyConfigFilePath = EPADConfig.getEPADWebServerJettyConfigFilePath();
			jettyConfigFileStream = new FileInputStream(jettyConfigFilePath);
			XmlConfiguration configuration = new XmlConfiguration(jettyConfigFileStream);
			configuration.configure(server);
			log.info("Jetty server configured using configuration file " + jettyConfigFilePath);
		} catch (FileNotFoundException e) {
			log.warning("Could not find Jetty configuration file " + EPADConfig.getEPADWebServerJettyConfigFilePath());
		} catch (SAXException e) {
			log.warning("SAX error reading Jetty configuration file " + EPADConfig.getEPADWebServerJettyConfigFilePath(), e);
		} catch (IOException e) {
			log.warning("IO error reading Jetty configuration file " + EPADConfig.getEPADWebServerJettyConfigFilePath(), e);
		} catch (Exception e) {
			log.warning("Error processing Jetty configuration file " + EPADConfig.getEPADWebServerJettyConfigFilePath(), e);
		} finally {
			IOUtils.closeQuietly(jettyConfigFileStream);
		}
	}

	public static void initializePlugins()
	{
		PluginController.getInstance();
	}

	public static void startSupportThreads()
	{
		log.info("Starting support threads....");

		try {
			QueueAndWatcherManager.getInstance().buildAndStart();
			String version = new EPadWebServerVersion().getVersion();
			String db_version = version;
			if (db_version.indexOf(".") != db_version.lastIndexOf("."))
			{
				// remove second period;
				db_version = db_version.substring(0, db_version.lastIndexOf(".")) + db_version.substring(db_version.lastIndexOf(".")+1);
			}
			log.info("EpadWS version:" + version + " Database version:" + db_version);
			EpadDatabase.getInstance().startup(db_version);
			log.info("Startup of database was successful");
			EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			//log.info("Checking annotations table");
			databaseOperations.checkAndRefreshAnnotationsTable();
			log.info("Done with database/queues init");
			List<User> users = DefaultEpadProjectOperations.getInstance().getAllUsers();
			if (EPADConfig.UseEPADUsersProjects && users.size() <= 1) {
				// Sync XNAT to Epad if needed
				try {
					XNATSyncHandler.syncXNATtoEpad("admin", "");
				}
				catch (Exception x) {
					log.warning("Error syncing XNAT data", x);
				}
			}
			RemotePACService.checkPropertiesFile();
			AIMUtil.checkSchemaFiles();
		} catch (Exception e) {
			log.warning("Failed to start database", e);
			System.exit(1);
		}
	}

	private static void addHandlers(Server server)
	{
		List<Handler> handlerList = new ArrayList<Handler>();

		if (!separateWebServicesApp) {

			loadPluginClasses();
	
			addHandlerAtContextPath(new EPADSessionHandler(), "/epad/session", handlerList);
	
			addHandlerAtContextPath(new EPADHandler(), "/epad/v2", handlerList);
		}

//      My Attempt to replace above EPADHandler with Spring Controllers
//		- Does not work with embedded jetty, controller methods don't map correctly, controller mapping works fine under tomcat
//		- If someone can make it work, that would be great (comment out add EPADHandler above)
//		try {
//			handlerList.add(getServletContextHandler(getContext()));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			log.warning("Error setting up Spring Handle", e);
//			e.printStackTrace();
//		}

		String webAppPath = EPADConfig.getEPADWebServerWebappsDir() + "ePad.war";
		if (!new File(webAppPath).exists())
			webAppPath = EPADConfig.getEPADWebServerWebappsDir() + "epad-1.1.war";
		addWebAppAtContextPath(handlerList, webAppPath, "/epad");

		addHandlerAtContextPath(new ResourceCheckHandler(), "/epad/resources", handlerList);
		addFileServerAtContextPath(EPADConfig.getEPADWebServerResourcesDir(), handlerList, "/epad/resources");

		if (!separateWebServicesApp) {
			addHandlerAtContextPath(new WadoHandler(), "/epad/wado", handlerList);
	
			addHandlerAtContextPath(new AimResourceHandler(), "/epad/aimresource", handlerList);
	
			if (!"true".equalsIgnoreCase(EPADConfig.getParamValue("DISABLE_PLUGINS")))
			{	
				addHandlerAtContextPath(new EPadPluginHandler(), "/epad/plugin", handlerList);
			}
			addHandlerAtContextPath(new EventHandler(), "/epad/eventresource", handlerList);
			addHandlerAtContextPath(new ProjectEventHandler(), "/epad/events", handlerList);
	
			addHandlerAtContextPath(new ServerStatusHandler(), "/epad/status", handlerList);
			addHandlerAtContextPath(new ImageCheckHandler(), "/epad/imagecheck", handlerList);
			addHandlerAtContextPath(new ImageReprocessingHandler(), "/epad/imagereprocess", handlerList);
			addHandlerAtContextPath(new ConvertAIM4Handler(), "/epad/convertaim4", handlerList);
			addHandlerAtContextPath(new XNATSyncHandler(), "/epad/syncxnat", handlerList);
			addHandlerAtContextPath(new StatisticsHandler(), "/epad/statistics", handlerList);
			
			// TODO This call will disappear when we switch to AIM4
			addHandlerAtContextPath(new CoordinationHandler(), "/epad/coordination", handlerList);
		}

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(handlerList.toArray(new Handler[handlerList.size()]));
		server.setHandler(contexts);
		log.info("Done setting up restapi handlers");
	}

	private static void addHandlerAtContextPath(Handler handler, String contextPath, List<Handler> handlerList)
	{
		ContextHandler contextHandler = new ContextHandler(contextPath);

		contextHandler.setResourceBase(".");
		contextHandler.setClassLoader(Thread.currentThread().getContextClassLoader());
		contextHandler.setHandler(handler);
		handlerList.add(contextHandler);

		log.info("Added " + handler.getClass().getName() + " at context " + contextPath);
	}

	private static final String CONTEXT_PATH = "/epad/v2";
    private static final String CONFIG_LOCATION = "edu.stanford.epad.epadws.config.SpringConfig";
    private static final String MAPPING_URL = "/*";
    private static final String DEFAULT_PROFILE = "dev";    
    
    /**
     * Function for setting up Spring Context with embedded Jetty
     * 
     */
    private static ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath(CONTEXT_PATH);
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(context));
        //contextHandler.setResourceBase(EPADConfig.getEPADWebServerResourcesDir());
        return contextHandler;
    }

    private static WebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        context.getEnvironment().setDefaultProfiles(DEFAULT_PROFILE);
        return context;
    }
	/**
	 * Adds a WAR file from the webapps directory at a context path.
	 * 
	 * @param handlerList List of handlers
	 * @param webAppPath String war file name, with or without extension (e.g., ePad.war)
	 * @param contextPath The context to add the war file (e.g., /epad)
	 */
	private static void addWebAppAtContextPath(List<Handler> handlerList, String webAppPath, String contextPath)
	{
		if (!contextPath.startsWith("/")) {
			contextPath = "/" + contextPath;
		}
		WebAppContext webAppContext = new WebAppContext(webAppPath, contextPath);
		String home = System.getProperty("user.home");
		webAppContext.setTempDirectory(new File(home + "/DicomProxy/jetty")); // TODO Read from config file
		webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
		if (new File(EPADConfig.getEPADWebServerEtcDir()+"webdefault.xml").exists())
		{
			log.info("Adding webdefault.xml");
			webAppContext.setDefaultsDescriptor(EPADConfig.getEPADWebServerEtcDir()+"webdefault.xml");
		}
		log.info("WebAuthFilter:'" + EPADConfig.getParamValue("WebAuthFilter", null) + "'");
		//if (EPADConfig.webAuthPassword != null && EPADConfig.getParamValue("WebAuthFilter", null) != null)
		{
			try {
				Class filter = Class.forName(EPADConfig.getParamValue("WebAuthFilter","edu.stanford.epad.epadws.security.WebAuthFilter"));
				webAppContext.addFilter(filter, "/*", EnumSet.of(DispatcherType.REQUEST,DispatcherType.ASYNC,DispatcherType.FORWARD));
			} catch (ClassNotFoundException e) {
				log.warning("WebAuth Authentication Filter " + EPADConfig.getParamValue("WebAuthFilter") + " not found");
			}
		}
		handlerList.add(webAppContext);
		log.info("Added WAR " + webAppPath + " at context path " + contextPath);
	}

	private static void addFileServerAtContextPath(String baseDirectory, List<Handler> handlerList, String contextPath)
	{
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(false);
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });
		resourceHandler.setResourceBase(baseDirectory);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler, new ResourceFailureLogHandler(), new DefaultHandler() });

		addHandlerAtContextPath(handlers, contextPath, handlerList);

		log.info("Added file server for " + baseDirectory + " directory.");
	}

	/**
	 * Load all the plugins into a map.
	 */
	public static void loadPluginClasses()
	{
		try {
		PluginHandlerMap pluginHandlerMap = PluginHandlerMap.getInstance();
		PluginConfig pluginConfig = PluginConfig.getInstance();
		List<String> pluginHandlerList = pluginConfig.getPluginHandlerList();

		for (String pluginClassName : pluginHandlerList) {
			log.info("Loading plugin class: " + pluginClassName);
			PluginServletHandler psh = pluginHandlerMap.loadFromClassName(pluginClassName);
			if (psh != null) {
				String pluginName = psh.getName();
				pluginHandlerMap.setPluginServletHandler(pluginName, psh);
			} else {
				log.warning("Could not find plugin class: " + pluginClassName);
			}
		}
		}
		catch (Exception x) {
			log.warning("Error loading plugin", x);
		}
		log.info("Done loading plugins");
	}

	private static void stopServer(Server server)
	{
		try {
			if (server != null) {
				log.info("#####################################################");
				log.info("############### Stopping Jetty server ###############");
				log.info("#####################################################");
				server.stop();
			}
		} catch (Exception e) {
			log.warning("Failed to stop the Jetty server", e);
		}
	}
}
