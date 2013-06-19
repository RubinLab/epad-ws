/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy;

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

import edu.stanford.isis.dicomproxy.common.ProxyVersion;
import edu.stanford.isis.dicomproxy.db.mysql.MySqlInstance;
import edu.stanford.isis.dicomproxy.db.mysql.pipeline.MySqlFactory;
import edu.stanford.isis.dicomproxy.handlers.admin.SignalShutdownHandler;
import edu.stanford.isis.dicomproxy.handlers.admin.StatusHandler;
import edu.stanford.isis.dicomproxy.handlers.aim.AimResourceHandler;
import edu.stanford.isis.dicomproxy.handlers.dicom.DicomDeleteHandler;
import edu.stanford.isis.dicomproxy.handlers.dicom.DicomHeadersHandler;
import edu.stanford.isis.dicomproxy.handlers.dicom.DicomVisuHandler;
import edu.stanford.isis.dicomproxy.handlers.dicom.MySqlSearchHandler;
import edu.stanford.isis.dicomproxy.handlers.dicom.SegmentationPathHandler;
import edu.stanford.isis.dicomproxy.handlers.dicom.SeriesOrderHandler;
import edu.stanford.isis.dicomproxy.handlers.dicom.SeriesTagHandler;
import edu.stanford.isis.dicomproxy.handlers.dicom.WadoHandler;
import edu.stanford.isis.dicomproxy.handlers.dicom.WindowLevelHandler;
import edu.stanford.isis.dicomproxy.handlers.event.EventSearchHandler;
import edu.stanford.isis.dicomproxy.handlers.plugin.EPadPluginHandler;
import edu.stanford.isis.dicomproxy.server.ProxyConfig;
import edu.stanford.isis.dicomproxy.server.ProxyLogger;
import edu.stanford.isis.dicomproxy.server.ProxyManager;
import edu.stanford.isis.dicomproxy.server.ShutdownSignal;
import edu.stanford.isis.dicomproxy.server.managers.leveling.WindowLevelFactory;
import edu.stanford.isis.dicomproxy.server.threads.ShutdownHookThread;
import edu.stanford.isis.epad.plugin.server.EPadFiles;
import edu.stanford.isis.epad.plugin.server.PluginHandler;
import edu.stanford.isis.epad.plugin.server.PluginServletHandler;
import edu.stanford.isis.epad.plugin.server.ePadPluginController;
import edu.stanford.isis.epad.plugin.server.impl.ClassFinderTestUtils;
import edu.stanford.isis.epad.plugin.server.impl.EPadFilesImpl;
import edu.stanford.isis.epad.plugin.server.impl.PluginConfig;
import edu.stanford.isis.epad.plugin.server.impl.PluginHandlerMap;

/**
 * Entry point for the DICOM Proxy. In the architecture this is the between the
 * web-application and the DICOM server.
 * 
 * Start an embedded Jetty server, and all the threads required for this
 * application.
 * 
 */
public class Main {

	private static ProxyLogger log = ProxyLogger.getInstance();

	/**
	 * Starts DicomProxy server and sets several contexts to be used by
	 * restlets.
	 * 
	 * <p>
	 * The application listens on the port indicated by the property
	 * <i>ePadClientPort</i> in proxy-config.properties.
	 * </p>
	 * 
	 * <p>
	 * The current directory must be set to the bin subdirectory before calling
	 * the start scripts associated with this application.
	 * </p>
	 * <p>
	 * This method sets up the following contexts on the Jetty server.
	 * </p>
	 * <ul>
	 * <li>
	 * /resources - this is a file server for the resources subdirectory
	 * {@link #addFileServer(String, List)} using {@link ResourceHandler}.</li>
	 * <li>
	 * /status - page showing status of server {@link StatusHandler}</li>
	 * <li>
	 * /epad - adds the web application <code>../webaps/ePad.war</code>.</li>
	 * <li>
	 * /apad - adds the web application <code>../webapps/originalEPad.war</code>
	 * .</li>
	 * <li>
	 * /queryretrieve - adds the web application
	 * <code>../webapps/DicomInterface.war</code>.</li>
	 * <li>
	 * /search -
	 * {@link edu.stanford.isis.dicomproxy.handlers.dicom.RsnaSearchHandler} or
	 * {@link edu.stanford.isis.dicomproxy.handlers.dicom.SearchHandler}.</li>
	 * <li>
	 * /status -
	 * {@link edu.stanford.isis.dicomproxy.handlers.admin.StatusHandler}.</li>
	 * </ul>
	 * 
	 * @param args
	 *            String[]
	 * 
	 * @see ProxyConfig
	 */
	public static void main(String[] args) {

		ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();

		@SuppressWarnings("unused") // Initialize singleton
		ProxyManager proxyManager = ProxyManager.getInstance();
		Server server = null;
		try {
			log.info("---- Starting the Dicom Proxy. Build date: "
					+ ProxyVersion.getBuildDate());

			// read config file.
			@SuppressWarnings("unused") // Initialize singleton
			ProxyConfig proxyConfig = ProxyConfig.getInstance();

			// init plugin classes.
			initPlugins();

			startThreads();

			server = createServer();

			testPluginImpl();

			// look for the EPadPluginHandler annotation in

			// create the shutdown hook.
			Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());

			log.info("Starting jetty "
					+ server.getClass().getPackage().getImplementationVersion()
					+ " ...");

			server.start();
			server.join();

		} catch (BindException be) {
			log.sever("Bind exception", be);
			Throwable t = be.getCause();
			log.warning("Bind exception cause: " + be.getMessage(), t);
		} catch (SocketException se) {
			log.sever("Cannot bind to all sockets.", se);
		} catch (Exception e) {
			log.sever("Fatal Exception. Shutting down DicomProxy", e);
		} catch (Error err) {
			log.sever("Fatal Error. Shutting down DicomProxy.", err);
		} finally {

			log.info("#####################################################");
			log.info("############# Shutting down DicomProxy ##############");
			log.info("#####################################################");

			shutdownSignal.shutdownNow();
			stopJettyServer(server);

			MySqlInstance.getInstance().shutdown();
			MySqlFactory.getInstance().shutdown();

			WindowLevelFactory.getInstance().shutdown();

			System.out.println("Shutting down DicomProxy");
			// wait just long enough for some messages to be printed out.
			try {
				TimeUnit.MILLISECONDS.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		log.info("#####################################################");
		log.info("###############    Exit DicomProxy    ###############");
		log.info("#####################################################");

		System.out
				.println("#####################################################");
		System.out
				.println("###############    Exit DicomProxy    ###############");
		System.out
				.println("#####################################################");

	}// main

	/**
	 * Stop the jetty server cleanly by checking it isn't null and catching
	 * exceptions.
	 * 
	 * @param server
	 *            Server
	 */
	private static void stopJettyServer(Server server) {
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

	/**
	 * Creates a context for war files in the webapps directory.
	 * 
	 * @param server
	 *            Server
	 */
	@SuppressWarnings("unused")
	private static void createWebAppContext(Server server) {
		try {
			//final int jettyPort = 8327; // ToDo: Move this to
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

	/**
	 * Make sure plugin has implementations.
	 */
	private static void initPlugins() {

		ePadPluginController controller = ePadPluginController.getInstance();
		controller.setImpl(new EPadFilesImpl());

	}

	private static void testPluginImpl() {

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
	 * Adds a war file from the web-apps directory at a context.
	 * 
	 * @param handlerList
	 *            list of handlers
	 * @param warFileName
	 *            String war file name. With or without extension. (ex.
	 *            ePad.war)
	 * @param context
	 *            The context to add the war file. (ex. /epad)
	 */
	private static void addWebApp(List<Handler> handlerList,
			String warFileName, String context) {

		String webAppPath = "../webapps/" + warFileName;
		if (!context.startsWith("/")) {
			context = "/" + context;
		}
		String contextPath = context;
		WebAppContext webAppContext = new WebAppContext(webAppPath, contextPath);

		handlerList.add(webAppContext);
		log.info("Added " + warFileName + " at context: " + context);
	}

	/**
	 * Add a file server for the resources directory.
	 * 
	 * @param baseDir
	 *            String
	 * @param handlerList
	 *            List<Handler>
	 * @see Handler
	 * @see ResourceHandler
	 * @see DefaultHandler
	 */
	private static void addFileServer(String baseDir, List<Handler> handlerList) {
		addFileServerAtContext(baseDir, handlerList, "/resources");

		// //Start file server from resources directory.
		// ResourceHandler resourceHandler = new ResourceHandler();
		// resourceHandler.setDirectoriesListed(true);
		// resourceHandler.setWelcomeFiles(new String[]{"index.html"});
		//
		// resourceHandler.setResourceBase(baseDir);
		//
		// HandlerList handlers = new HandlerList();
		// handlers.setHandlers(new Handler[] { resourceHandler, new
		// DefaultHandler() });
		//
		// addHandlerAtContext(handlers,"/resources",handlerList);
		//
		// log.info("Added file server from "+baseDir+" directory.");
	}

	private static void addFileServerAtContext(String baseDir,
			List<Handler> handlerList, String context) {
		// Start file server from resources directory.
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });

		resourceHandler.setResourceBase(baseDir);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler,
				new DefaultHandler() });

		addHandlerAtContext(handlers, context, handlerList);

		log.info("Added file server from " + baseDir + " directory.");
	}

	/**
	 * 
	 * @param handler
	 *            Handler
	 * @param contextPath
	 *            String
	 * @param handlerList
	 *            List of Handler
	 * @see Handler
	 * @see ContextHandler
	 */
	private static void addHandlerAtContext(Handler handler,
			String contextPath, List<Handler> handlerList) {
		ContextHandler context = new ContextHandler();

		if (!contextPath.startsWith("/")) {
			contextPath = "/" + contextPath;
		}

		context.setContextPath(contextPath);
		context.setResourceBase(".");
		context.setClassLoader(Thread.currentThread().getContextClassLoader());

		// Add status handler
		context.setHandler(handler);

		handlerList.add(context);

		log.info("Added " + handler.getClass().getName() + " at context: "
				+ contextPath);
	}

	/**
     *
     */
	public static void startThreads() {
		log.info("Starting support threads.");
		// start the pipeline factory.

		// start MySql database.
		try {
			MySqlFactory.getInstance().buildAndStart();
			MySqlInstance.getInstance().startup();
			log.info("Startup of MySql database was successful.");
		} catch (Exception e) {
			log.warning("Failed to start MySql database.", e);
		}

		WindowLevelFactory.getInstance().buildAndStart();
	}

	// public class WebComponent extends Component {
	//
	// }

	/**
	 * 
	 * @return Server
	 */
	public static Server createServer() {

		Server server = null;

		ProxyConfig proxyConfig = ProxyConfig.getInstance();
		int port = proxyConfig.getIntParam("ePadClientPort");
		server = new Server(port);

		log.info("... Jetty J2EE server listening on port: " + port);

		loadPluginClasses();

		// Add status
		List<Handler> handlerList = new ArrayList<Handler>();
		addHandlerAtContext(new StatusHandler(), "/status", handlerList);
		addHandlerAtContext(new SignalShutdownHandler(), "/shutdown",
				handlerList);// listen for a connection to request a shutdown.
		addHandlerAtContext(new WindowLevelHandler(), "/level", handlerList);// creates
																				// new
																				// leveled
																				// jpegs
																				// on
																				// the
																				// fly.

		addFileServer("../resources", handlerList);
		// addFileServerAtContext(SeriesWatcher,handlerList,"/dcm4chee");//only
		// needed if we need the raw DICOM files.

		addWebApp(handlerList, "ePad.war", "/epad");
		addWebApp(handlerList, "AimQLWeb.war", "aqlweb");
		addWebApp(handlerList, "originalEPad.war", "/apad");
		addWebApp(handlerList, "epadGL.war", "/epadgl");

		// Configure if the search is going to be RSNA mode, H2 DB mode or
		// original Dicom Server mode?
		configureSearchHandler(proxyConfig, handlerList);

		addHandlerAtContext(new EPadPluginHandler(), "/plugin", handlerList);

		addHandlerAtContext(new SeriesTagHandler(), "/seriestag", handlerList);
		addHandlerAtContext(new SeriesOrderHandler(), "/seriesorder",
				handlerList);
		addHandlerAtContext(new DicomDeleteHandler(), "/dicomdelete",
				handlerList);
		addHandlerAtContext(new AimResourceHandler(), "/aimresource",
				handlerList);
		addHandlerAtContext(new DicomHeadersHandler(), "/dicomtag", handlerList);
		addHandlerAtContext(new DicomVisuHandler(), "/dicomparam", handlerList);
		addHandlerAtContext(new WadoHandler(), "/eWado", handlerList);

		addHandlerAtContext(new EventSearchHandler(), "/eventresource",
				handlerList);

		addHandlerAtContext(new SegmentationPathHandler(), "/segmentationpath",
				handlerList);

		// addHandlerAtContext(new
		// DicomUploadHandler(),"/dicomupload",handlerList);

		// This is the context for war files in the webapps directory.
		// createWebAppContext(server); //ToDo: re-enable once new VM is
		// running.

		configureAimXmlDatabase(proxyConfig, handlerList);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(handlerList.toArray(new Handler[handlerList.size()]));
		server.setHandler(contexts);

		return server;
	}

	/**
	 * Check the proxy-config.properties file for the useDbXml setting. Add this
	 * if true.
	 * 
	 * @param proxyConfig
	 *            ProxyConfig
	 * @param handlerList
	 *            List of Handlers
	 */
	private static void configureAimXmlDatabase(ProxyConfig proxyConfig,
			List<Handler> handlerList) {
		if ("true".equalsIgnoreCase(proxyConfig.getParam("useDbXml"))) {

			try {

				ServletContextHandler restletContextHandler = new ServletContextHandler(
						ServletContextHandler.NO_SESSIONS);
				restletContextHandler.setContextPath("/restlet");
				ServerServlet serverServlet = new ServerServlet();
				ServletHolder servletHolder = new ServletHolder(serverServlet);
				servletHolder
						.setInitParameter("org.restlet.application",
								"edu.stanford.isis.dicomproxy.db.aimxml.XmlDbApplication");
				restletContextHandler.addServlet(servletHolder, "/*");
				handlerList.add(restletContextHandler);

			} catch (NoClassDefFoundError error) {
				log.sever(
						"## ERROR ##: AIM XML Database: No Restlet class found. Continue without AIM XML Database!",
						error);
			} catch (Exception e) {
				log.warning(
						"## ERROR ##: Failed to register Context. Continue without AIM XML Database.",
						e);
			}

		} else {
			log.info("WARNING: Not using AIM XML Database!");
		}
	}

	/**
	 * Configure the search handler based on the etc/proxy-config.properties
	 * settings.
	 * 
	 * @param proxyConfig
	 *            ProxyConfig
	 * @param handlerList
	 *            List of Handler
	 */
	private static void configureSearchHandler(ProxyConfig proxyConfig,
			List<Handler> handlerList) {

		/*
		 * NOTE This section is deprecated. But will be similar to the final
		 * result
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
		addHandlerAtContext(new MySqlSearchHandler(), "/search", handlerList);
	}

	/**
	 * Load all the plugins into a map.
	 */
	private static void loadPluginClasses() {
		PluginHandlerMap pluginHandlerMap = PluginHandlerMap.getInstance();
		PluginConfig pluginConfig = PluginConfig.getInstance();
		List<String> pluginHandlerList = pluginConfig.getPluginHandlerList();
		for (String currClassName : pluginHandlerList) {
			log.info("Loading plugin class: " + currClassName);
			PluginServletHandler psh = pluginHandlerMap
					.loadFromClassName(currClassName);
			if (psh != null) {
				String pluginName = psh.getName();
				pluginHandlerMap.setPluginServletHandler(pluginName, psh);
			} else {
				log.info("WARNING: Didn't find plugin class: " + currClassName);
			}
		}
	}

	/**
	 * Temporary test to find best way to get plugin classes using reflection.
	 */
	@SuppressWarnings("unused")
	private static void pluginReflectionsTests() {
		try {

			// Look for a class that has annotation EPadPluginHandler
			String packageName = "edu.stanford.isis.plugins.first";
			List<Class<?>> pluginClasses = ClassFinderTestUtils
					.getClasses(packageName);

			log.info("Found :" + pluginClasses.size() + " classes in package: "
					+ packageName);
			for (Class<?> pluginClass : pluginClasses) {
				if (ClassFinderTestUtils.hasAnnotation(pluginClass,
						PluginHandler.class)) {
					log.info("Found PluginHandler class: "
							+ pluginClass.getName());
				}
			}
			PluginHandlerMap pluginHandlerMap = PluginHandlerMap.getInstance();
			pluginHandlerMap.classLoaderForName();
			// ClassFinderTestUtils.classForName();
			// ClassFinderTestUtils.dynamicClassLoader();
			// ClassFinderTestUtils.classFinderMethod();

			// try to read the class-path for some
			// Print out the class path
			log.info("Classpath: \n" + ClassFinderTestUtils.printClasspath());

			// Read the Manifest of classes in some jar files.

			//
			ClassFinderTestUtils.readJarManifestForClass(pluginHandlerMap
					.getClass());

		} catch (Exception e) {
			log.warning("Failed plugin reflections test.", e);
		}
	}

}
