/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws;

import java.io.File;
import java.net.BindException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.restlet.ext.servlet.ServerServlet;

import edu.stanford.isis.epad.plugin.server.EPadFiles;
import edu.stanford.isis.epad.plugin.server.PluginHandler;
import edu.stanford.isis.epad.plugin.server.PluginServletHandler;
import edu.stanford.isis.epad.plugin.server.ePadPluginController;
import edu.stanford.isis.epad.plugin.server.impl.ClassFinderTestUtils;
import edu.stanford.isis.epad.plugin.server.impl.EPadFilesImpl;
import edu.stanford.isis.epad.plugin.server.impl.PluginConfig;
import edu.stanford.isis.epad.plugin.server.impl.PluginHandlerMap;
import edu.stanford.isis.epadws.common.ProxyVersion;
import edu.stanford.isis.epadws.db.mysql.MySqlInstance;
import edu.stanford.isis.epadws.db.mysql.pipeline.MySqlFactory;
import edu.stanford.isis.epadws.handlers.admin.SignalShutdownHandler;
import edu.stanford.isis.epadws.handlers.admin.StatusHandler;
import edu.stanford.isis.epadws.handlers.aim.AimResourceHandler;
import edu.stanford.isis.epadws.handlers.dicom.DicomDeleteHandler;
import edu.stanford.isis.epadws.handlers.dicom.DicomHeadersHandler;
import edu.stanford.isis.epadws.handlers.dicom.DicomVisuHandler;
import edu.stanford.isis.epadws.handlers.dicom.MySqlSearchHandler;
import edu.stanford.isis.epadws.handlers.dicom.SegmentationPathHandler;
import edu.stanford.isis.epadws.handlers.dicom.SeriesOrderHandler;
import edu.stanford.isis.epadws.handlers.dicom.SeriesTagHandler;
import edu.stanford.isis.epadws.handlers.dicom.WadoHandler;
import edu.stanford.isis.epadws.handlers.dicom.WindowLevelHandler;
import edu.stanford.isis.epadws.handlers.event.EventSearchHandler;
import edu.stanford.isis.epadws.handlers.plugin.EPadPluginHandler;
import edu.stanford.isis.epadws.server.ProxyConfig;
import edu.stanford.isis.epadws.server.ProxyLogger;
import edu.stanford.isis.epadws.server.ProxyManager;
import edu.stanford.isis.epadws.server.ShutdownSignal;
import edu.stanford.isis.epadws.server.managers.leveling.WindowLevelFactory;
import edu.stanford.isis.epadws.server.threads.ShutdownHookThread;

/**
 * Entry point for the EPad Web Service. In the architecture this is the between the web application and the DICOM
 * server.
 * 
 * Start an embedded Jetty server, and all the threads required for this application.
 * 
 */
public class Main
{
	private static ProxyLogger log = ProxyLogger.getInstance();

	/**
	 * Starts EPad web server and sets several contexts to be used by restlets.
	 * <p>
	 * The application listens on the port indicated by the property <i>ePadClientPort</i> in proxy-config.properties.
	 * <p>
	 * The current directory must be set to the bin subdirectory before calling the start scripts associated with this
	 * application.
	 * <p>
	 * This method sets up the following contexts on the Jetty server.
	 * <ul>
	 * <li>
	 * /resources - this is a file server for the resources subdirectory {@link #addFileServer(String, List)} using
	 * {@link ResourceHandler}.</li>
	 * <li>
	 * /status - page showing status of server {@link StatusHandler}</li>
	 * <li>
	 * /epad - adds the web application <code>../webaps/ePad.war</code>.</li>
	 * <li>
	 * /apad - adds the web application <code>../webapps/originalEPad.war</code> .</li>
	 * <li>
	 * /queryretrieve - adds the web application <code>../webapps/DicomInterface.war</code>.</li>
	 * <li>
	 * /search - {@link edu.stanford.isis.epadws.handlers.dicom.RsnaSearchHandler} or
	 * {@link edu.stanford.isis.epadws.handlers.dicom.SearchHandler}.</li>
	 * <li>
	 * /status - {@link edu.stanford.isis.epadws.handlers.admin.StatusHandler}.</li>
	 * </ul>
	 * 
	 * @param args String[]
	 * @see ProxyConfig
	 */
	public static void main(String[] args)
	{
		ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
		Server server = null;

		@SuppressWarnings("unused")
		ProxyManager proxyManager = ProxyManager.getInstance();

		try {
			ProxyConfig proxyConfig = ProxyConfig.getInstance(); // Reads configuration file
			int port = proxyConfig.getIntParam("ePadClientPort");

			log.info("Starting the Dicom Proxy. Build date: " + ProxyVersion.getBuildDate());
			initPlugins(); // Initialize plugin classes
			startSupportThreads();
			server = createServer(port);
			addHandlers(server);
			testPluginImpl();
			Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());

			log.info("Starting Jetty " + server.getClass().getPackage().getImplementationVersion() + " on port " + port);
			server.start();
			server.join();
		} catch (BindException be) {
			log.sever("Bind exception", be);
			Throwable t = be.getCause();
			log.warning("Bind exception cause: " + be.getMessage(), t);
		} catch (SocketException se) {
			log.sever("Cannot bind to all sockets.", se);
		} catch (Exception e) {
			log.sever("Fatal Exception. Shutting down EPad Web Service.", e);
		} catch (Error err) {
			log.sever("Fatal Error. Shutting down EPad Web Service.", err);
		} finally {
			log.info("#####################################################");
			log.info("############# Shutting down EPad Web Service ########");
			log.info("#####################################################");

			shutdownSignal.shutdownNow();
			stopServer(server);

			MySqlInstance.getInstance().shutdown();
			MySqlFactory.getInstance().shutdown();
			WindowLevelFactory.getInstance().shutdown();

			try { // Wait just long enough for some messages to be printed out.
				TimeUnit.MILLISECONDS.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		log.info("#####################################################");
		log.info("################## Exit  EPad Web Service ###########");
		log.info("#####################################################");
	}

	/**
	 * Make sure plugin has implementations.
	 */
	private static void initPlugins()
	{
		ePadPluginController controller = ePadPluginController.getInstance();
		controller.setImpl(new EPadFilesImpl());
	}

	private static void startSupportThreads()
	{
		log.info("Starting support threads.");

		try { // Start MySql database.
			MySqlFactory.getInstance().buildAndStart();
			MySqlInstance.getInstance().startup();
			log.info("Startup of MySql database was successful.");
		} catch (Exception e) {
			log.warning("Failed to start MySql database.", e);
		}
		WindowLevelFactory.getInstance().buildAndStart();
	}

	/**
	 * Create the server and add handlers.
	 * 
	 * @return Server
	 */
	private static Server createServer(int port)
	{
		Server server = new Server(port);

		return server;
	}

	private static void addHandlers(Server server)
	{
		List<Handler> handlerList = new ArrayList<Handler>();

		loadPluginClasses();

		addWebAppAtContextPath(handlerList, "ePad.war", "/epad");
		addWebAppAtContextPath(handlerList, "AimQLWeb.war", "aqlweb");
		addWebAppAtContextPath(handlerList, "originalEPad.war", "/apad");
		addWebAppAtContextPath(handlerList, "epadGL.war", "/epadgl");

		addFileServerAtContextPath("../resources", handlerList, "/resources");

		addHandlerAtContextPath(new StatusHandler(), "/status", handlerList);
		addHandlerAtContextPath(new SignalShutdownHandler(), "/shutdown", handlerList);
		addHandlerAtContextPath(new WindowLevelHandler(), "/level", handlerList);
		addHandlerAtContextPath(new DicomDeleteHandler(), "/dicomdelete", handlerList);
		addHandlerAtContextPath(new AimResourceHandler(), "/aimresource", handlerList);
		addHandlerAtContextPath(new WadoHandler(), "/eWado", handlerList);
		addHandlerAtContextPath(new SeriesTagHandler(), "/seriestag", handlerList);
		addHandlerAtContextPath(new SeriesOrderHandler(), "/seriesorder", handlerList);
		addHandlerAtContextPath(new MySqlSearchHandler(), "/search", handlerList);
		addHandlerAtContextPath(new DicomHeadersHandler(), "/dicomtag", handlerList);
		addHandlerAtContextPath(new DicomVisuHandler(), "/dicomparam", handlerList);
		addHandlerAtContextPath(new EventSearchHandler(), "/eventresource", handlerList);
		addHandlerAtContextPath(new SegmentationPathHandler(), "/segmentationpath", handlerList);
		addHandlerAtContextPath(new EPadPluginHandler(), "/plugin", handlerList);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(handlerList.toArray(new Handler[handlerList.size()]));
		server.setHandler(contexts);
	}

	// Old deprecated code from above. Delete soon.
	// configureAimXmlDatabase(proxyConfig, handlerList);
	// Configure if the search is going to be RSNA mode, H2 DB mode or original Dicom Server mode.
	// configureSearchHandler(proxyConfig, handlerList);
	// addHandlerAtContext(new DicomUploadHandler(), "/dicomupload", handlerList);
	// addFileServerAtContext(SeriesWatcher, handlerList, "/dcm4chee");//only needed if we need the raw DICOM files.
	// This is the context for war files is the webapps directory.
	// createWebAppContext(server); //ToDo: re-enable once new VM is running.

	/**
	 * Adds a war file from the web-apps directory at a context path.
	 * 
	 * @param handlerList List of handlers
	 * @param warFileName String war file name, with or without extension (e.g., ePad.war)
	 * @param contextPath The context to add the war file (e.g., /epad)
	 */
	private static void addWebAppAtContextPath(List<Handler> handlerList, String warFileName, String contextPath)
	{
		String webAppPath = "../webapps/" + warFileName;
		if (!contextPath.startsWith("/")) {
			contextPath = "/" + contextPath;
		}
		WebAppContext webAppContext = new WebAppContext(webAppPath, contextPath);

		handlerList.add(webAppContext);
		log.info("Added " + warFileName + " at context path " + contextPath);
	}

	private static void addFileServerAtContextPath(String baseDir, List<Handler> handlerList, String contextPath)
	{
		// Start file server from resources directory.
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });

		resourceHandler.setResourceBase(baseDir);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler, new DefaultHandler() });

		addHandlerAtContextPath(handlers, contextPath, handlerList);

		log.info("Added file server from " + baseDir + " directory.");
	}

	/**
	 * @param handler Handler
	 * @param contextPath String
	 * @param handlerList List of Handler
	 * @see Handler
	 * @see ContextHandler
	 */
	private static void addHandlerAtContextPath(Handler handler, String contextPath, List<Handler> handlerList)
	{
		ContextHandler contextHandler = new ContextHandler();

		if (!contextPath.startsWith("/"))
			contextPath = "/" + contextPath;

		contextHandler.setContextPath(contextPath);
		contextHandler.setResourceBase(".");
		contextHandler.setClassLoader(Thread.currentThread().getContextClassLoader());

		contextHandler.setHandler(handler); // Add status handler
		handlerList.add(contextHandler);

		log.info("Added " + handler.getClass().getName() + " at context: " + contextPath);
	}

	/**
	 * Load all the plugins into a map.
	 */
	private static void loadPluginClasses()
	{
		PluginHandlerMap pluginHandlerMap = PluginHandlerMap.getInstance();
		PluginConfig pluginConfig = PluginConfig.getInstance();
		List<String> pluginHandlerList = pluginConfig.getPluginHandlerList();

		for (String currClassName : pluginHandlerList) {
			log.info("Loading plugin class: " + currClassName);
			PluginServletHandler psh = pluginHandlerMap.loadFromClassName(currClassName);
			if (psh != null) {
				String pluginName = psh.getName();
				pluginHandlerMap.setPluginServletHandler(pluginName, psh);
			} else {
				log.info("WARNING: Didn't find plugin class: " + currClassName);
			}
		}
	}

	/**
	 * Stop the server cleanly by checking it isn't null and catching exceptions.
	 * 
	 * @param server The server
	 */
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
			log.warning("Failed to stop the jetty server.", e);
		}
	}

	// Old deprecated code from above. Delete soon.
	// configureAimXmlDatabase(proxyConfig, handlerList);
	// Configure if the search is going to be RSNA mode, H2 DB mode or original Dicom Server mode.
	// configureSearchHandler(proxyConfig, handlerList);
	// addHandlerAtContext(new DicomUploadHandler(), "/dicomupload", handlerList);
	// addFileServerAtContext(SeriesWatcher, handlerList, "/dcm4chee");//only needed if we need the raw DICOM files.
	// This is the context for war files is the webapps directory.
	// createWebAppContext(server); //ToDo: re-enable once new VM is running.

	private static void testPluginImpl()
	{
		try {
			EPadFiles ePadFiles = new EPadFilesImpl();
			File baseDirFile = ePadFiles.getBaseDir();
			log.info("baseDirFile = " + baseDirFile.getAbsolutePath());
			String baseDirPath = ePadFiles.getBaseDirPath();
			log.info("baseDirPath = " + baseDirPath);

			List<String> allStudies = ePadFiles.getStudies();
			for (String currStudy : allStudies) {
				log.info("Study: " + currStudy);
			}
		} catch (Exception e) {
			log.warning("Failed ePadPlugin test.", e);
		} catch (Error err) {
			log.warning("Failed ePadPlugin with an error.", err);
		}
	}

	/**
	 * Temporary test to find best way to get plugin classes using reflection.
	 */
	@SuppressWarnings("unused")
	private static void pluginReflectionsTests()
	{
		try {
			// Look for a class that has annotation EPadPluginHandler
			String packageName = "edu.stanford.isis.plugins.first";
			List<Class<?>> pluginClasses = ClassFinderTestUtils.getClasses(packageName);

			log.info("Found :" + pluginClasses.size() + " classes in package: " + packageName);
			for (Class<?> pluginClass : pluginClasses) {
				if (ClassFinderTestUtils.hasAnnotation(pluginClass, PluginHandler.class)) {
					log.info("Found PluginHandler class: " + pluginClass.getName());
				}
			}
			PluginHandlerMap pluginHandlerMap = PluginHandlerMap.getInstance();
			pluginHandlerMap.classLoaderForName();
			// ClassFinderTestUtils.classForName();
			// ClassFinderTestUtils.dynamicClassLoader();
			// ClassFinderTestUtils.classFinderMethod();

			// try to read the class-path for some
			log.info("Classpath: \n" + ClassFinderTestUtils.printClasspath());

			// Read the Manifest of classes in some jar files.
			ClassFinderTestUtils.readJarManifestForClass(pluginHandlerMap.getClass());
		} catch (Exception e) {
			log.warning("Failed plugin reflections test.", e);
		}
	}

	/**
	 * Check the proxy-config.properties file for the useDbXml setting. Add this handler if true.
	 * 
	 * @param proxyConfig ProxyConfig
	 * @param handlerList List of Handlers
	 */
	@SuppressWarnings("unused")
	private static void configureAimXmlDatabase(ProxyConfig proxyConfig, List<Handler> handlerList)
	{
		if ("true".equalsIgnoreCase(proxyConfig.getParam("useDbXml"))) {
			try {
				ServletContextHandler restletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
				restletContextHandler.setContextPath("/restlet");
				ServerServlet serverServlet = new ServerServlet();
				ServletHolder servletHolder = new ServletHolder(serverServlet);
				servletHolder
						.setInitParameter("org.restlet.application", "edu.stanford.isis.epadws.db.aimxml.XmlDbApplication");
				restletContextHandler.addServlet(servletHolder, "/*");
				handlerList.add(restletContextHandler);
			} catch (NoClassDefFoundError error) {
				log.sever("## ERROR ##: AIM XML Database: No Restlet class found. Continue without AIM XML Database!", error);
			} catch (Exception e) {
				log.warning("## ERROR ##: Failed to register Context. Continue without AIM XML Database.", e);
			}
		} else {
			log.info("WARNING: Not using AIM XML Database!");
		}
	}

	/**
	 * Configure the search handler based on the etc/proxy-config.properties settings.
	 * 
	 * @param proxyConfig ProxyConfig
	 * @param handlerList List of Handler
	 */
	@SuppressWarnings("unused")
	private static void configureSearchHandler(ProxyConfig proxyConfig, List<Handler> handlerList)
	{
		/*
		 * NOTE This section is deprecated. But will be similar to the final result
		 */
		// if(!"true".equalsIgnoreCase(proxyConfig.getParam("rsnaSearchDemo"))){
		//
		// //This is the DicomServer was are talking with.
		// String dicomServerAETitle =
		// proxyConfig.getParam("DicomServerAETitle");
		// String dicomServerIP = proxyConfig.getParam("DicomServerIP");
		// String dicomServerPort = proxyConfig.getParam("DicomServerPort");
		// log.info("Search uses DicomServer ("+dicomServerAETitle+") @"+dicomServerIP+":"+dicomServerPort);
		//
		// //This should be the IP address that the DicomServer sees.
		// String myIP = proxyConfig.getParam("ListenIP");
		// String myPort = proxyConfig.getParam("ListenPort");
		// log.info("Expecting DicomProxy to listen @"+myIP+":"+myPort);
		//
		// ProxyManager proxyManager = ProxyManager.getInstance();
		// addHandlerAtContext(new
		// SearchHandler(proxyManager),"/search",handlerList);
		// }else{

		log.info("Using MySQL Database Search Mode.");
		addHandlerAtContextPath(new MySqlSearchHandler(), "/search", handlerList);
	}

	/**
	 * Creates a context for war files in the webapps directory.
	 * 
	 * @param server Server
	 */
	@SuppressWarnings("unused")
	private static void createWebAppContext(Server server)
	{
		try {
			// final int jettyPort = 8327; // ToDo: Move this to
			// "proxy-config.properties" with
			// default setting 8088.
			final String contextPath = "/a";

			// log.info("Starting webapp context on port="+jettyPort+" and contextPath="+contextPath);

			WebAppContext webapp = new WebAppContext();
			webapp.setParentLoaderPriority(true);
			webapp.setContextPath(contextPath);
			webapp.setWar("../webapps");
			webapp.setDefaultsDescriptor("../etc/webdefault.xml");
			server.setHandler(webapp);
		} catch (Exception e) {
			log.warning("failed to start webapp context.", e);
		}
	}
}
