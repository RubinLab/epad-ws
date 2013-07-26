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
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.plugin.server.EPadFiles;
import edu.stanford.isis.epad.plugin.server.PluginServletHandler;
import edu.stanford.isis.epad.plugin.server.ePadPluginController;
import edu.stanford.isis.epad.plugin.server.impl.EPadFilesImpl;
import edu.stanford.isis.epad.plugin.server.impl.PluginConfig;
import edu.stanford.isis.epad.plugin.server.impl.PluginHandlerMap;
import edu.stanford.isis.epadws.common.ProxyVersion;
import edu.stanford.isis.epadws.db.mysql.MySqlInstance;
import edu.stanford.isis.epadws.db.mysql.pipeline.MySqlFactory;
import edu.stanford.isis.epadws.resources.server.EPadWebServiceServerResource;
import edu.stanford.isis.epadws.server.ProxyManager;
import edu.stanford.isis.epadws.server.ShutdownSignal;
import edu.stanford.isis.epadws.server.managers.leveling.WindowLevelFactory;
import edu.stanford.isis.epadws.server.threads.ShutdownHookThread;

/**
 * Entry point for the EPad Web Service. In the architecture this is the between the web application and the DICOM
 * server.
 * <p>
 * Start an embedded server, and all the threads required for this application, and attach all routes to REST resources.
 * 
 */
public class EPADWebService extends Application
{
	private static ProxyLogger log = ProxyLogger.getInstance();
	private final PluginHandlerMap pluginHandlerMap = PluginHandlerMap.getInstance();

	/**
	 * Starts EPad web server and sets several contexts to be used by restlets.
	 * <p>
	 * The application listens on the port indicated by the property <i>ePadClientPort</i> in proxy-config.properties.
	 * <p>
	 * The current directory must be set to the bin subdirectory before calling the start scripts associated with this
	 * application.
	 * 
	 * @param args String[]
	 * @see ProxyConfig
	 */
	public static void main(String[] args)
	{
		Component component = null;

		@SuppressWarnings("unused")
		ProxyManager proxyManager = ProxyManager.getInstance();

		try {
			ProxyConfig proxyConfig = ProxyConfig.getInstance(); // Reads configuration file
			int port = proxyConfig.getIntParam("ePadClientPort");

			log.info("Starting the ePAD Web Service. Build date: " + ProxyVersion.getBuildDate());
			initPlugins(); // Initialize plugin classes
			// startSupportThreads();
			component = createComponent(port);
			// testPluginImpl();
			Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());

			log.info("Starting server on port " + port);
			component.start();
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
		}
	}

	public EPADWebService()
	{
		log.info("******************Restlet Application Started **********************");
		setName("EPADWebServer");
		setDescription("EPAD Web Server");
		setOwner("RubinLab");
		setAuthor("RubinLab");
	}

	// TODO: Directory class for files. See http://restlet.org/learn/tutorial/2.2/
	// directory.setListingAllowed(true); directory.setDeeplyAccessible(true);
	/**
	 * Called when the Restlet framework initializes the {@link EPADWebService} application.
	 * <p>
	 * Here we link routes to resources.
	 */
	@Override
	public Restlet createInboundRoot()
	{
		log.info("****************** createInboundRoot **********************");

		// initPlugins();
		// initializePluginHandlers();

		Router router = new Router(getContext());

		log.info("Context: " + getContext());

		// TODO Put in configuration file
		Directory resourcesDirectory = new Directory(getContext(), "file:///Users/martin/tmp/resources");
		resourcesDirectory.setListingAllowed(true);
		router.attach("/resources", resourcesDirectory);

		// TODO Put in configuration file
		Directory warDirectory = new Directory(getContext(), "war:///Users/martin/tmp/webapps");
		warDirectory.setNegotiatingContent(false);
		warDirectory.setIndexName("Web_pad.html");
		router.attach("/epad", warDirectory);

		router.attach("/server/{operation}", EPadWebServiceServerResource.class);

		// router.attach("/plugins/{pluginName}", PluginServerResource.class);
		// router.attach("/level", WindowLevelServerResource.class);
		// router.attach("/dicomdelete", DICOMDeleteServerResource.class);
		// router.attach("/aimresource", AIMServerResource.class);
		// router.attach("/eWado", WADOServerResource.class);
		// router.attach("/seriestag", DICOMSeriesTagServerResource.class);
		// router.attach("/seriesorder", DICOMSeriesOrderServerResource.class);
		// router.attach("/search", SQLSearchServerResource.class);
		// router.attach("/dicomtag", DICOMHeadersServerResource.class);
		// router.attach("/dicomparam", DICOMVisuServerResource.class);
		// router.attach("/eventresource", EventServerResource.class);
		// router.attach("/segmentationpath", SegmentationPathServerResource.class);

		return router;
	}

	@Override
	public synchronized void stop() throws Exception
	{
		super.stop();

		ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();

		destroyPluginHandlers();
		log.info("#####################################################");
		log.info("############# Shutting down EPad Web Service ########");
		log.info("#####################################################");

		shutdownSignal.shutdownNow();

		MySqlInstance.getInstance().shutdown();
		MySqlFactory.getInstance().shutdown();
		WindowLevelFactory.getInstance().shutdown();

		try { // Wait just long enough for some messages to be printed out.
			TimeUnit.MILLISECONDS.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		log.info("#####################################################");
		log.info("################## Exit  EPad Web Service ###########");
		log.info("#####################################################");
	}

	/**
	 * Create the serverThe Restlets framework does not require that a particular server implementation is specified here.
	 * An implementation is picked up from loaded JARs. If the org.restlets.ext.jetty JAR is in the class path, for
	 * example, it will be picked up.
	 * 
	 * @return Component
	 */
	private static Component createComponent(int port)
	{
		Component component = new Component();

		component.getClients().add(Protocol.FILE);
		component.getClients().add(Protocol.WAR);
		component.getServers().add(Protocol.WAR, port);
		component.getServers().add(Protocol.HTTP, port);

		component.getClients().add(Protocol.HTTP); // Needed?

		component.getDefaultHost().attach(new EPADWebService());

		return component;
	}

	/**
	 * Make sure plugin has implementations.
	 */
	private static void initPlugins()
	{
		ePadPluginController controller = ePadPluginController.getInstance();
		controller.setImpl(new EPadFilesImpl());
	}

	public void initializePluginHandlers()
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
				log.sever("UnsatisfiedLinkError java.library.path = " + javaLibPath, ule);
			} catch (Exception e) {
				log.warning("Plugin: " + key + " failed init().", e);
			} catch (Error err) {
				log.sever("ERROR: Plugin: " + key + " failed init().", err);
			}
		}
	}

	public void destroyPluginHandlers()
	{ // Destroy all of the plugin handlers.
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

	/*
	 * private static void addHandlers(Server server) { List<Handler> handlerList = new ArrayList<Handler>();
	 * 
	 * loadPluginClasses();
	 * 
	 * addWebAppAtContextPath(handlerList, "ePad.war", "/epad"); addWebAppAtContextPath(handlerList, "AimQLWeb.war",
	 * "aqlweb"); addWebAppAtContextPath(handlerList, "originalEPad.war", "/apad"); addWebAppAtContextPath(handlerList,
	 * "epadGL.war", "/epadgl");
	 * 
	 * addFileServerAtContextPath("../resources", handlerList, "/resources");
	 * 
	 * addHandlerAtContextPath(new StatusHandler(), "/status", handlerList); addHandlerAtContextPath(new
	 * SignalShutdownHandler(), "/shutdown", handlerList); addHandlerAtContextPath(new WindowLevelHandler(), "/level",
	 * handlerList); addHandlerAtContextPath(new MySqlSearchHandler(), "/search", handlerList);
	 * addHandlerAtContextPath(new EPadPluginHandler(), "/plugin", handlerList); addHandlerAtContextPath(new
	 * SeriesTagHandler(), "/seriestag", handlerList); addHandlerAtContextPath(new SeriesOrderHandler(), "/seriesorder",
	 * handlerList); addHandlerAtContextPath(new DicomDeleteHandler(), "/dicomdelete", handlerList);
	 * addHandlerAtContextPath(new AimResourceHandler(), "/aimresource", handlerList); addHandlerAtContextPath(new
	 * DicomHeadersHandler(), "/dicomtag", handlerList); addHandlerAtContextPath(new DicomVisuHandler(), "/dicomparam",
	 * handlerList); addHandlerAtContextPath(new WadoHandler(), "/eWado", handlerList); addHandlerAtContextPath(new
	 * EventSearchHandler(), "/eventresource", handlerList); addHandlerAtContextPath(new SegmentationPathHandler(),
	 * "/segmentationpath", handlerList);
	 * 
	 * ContextHandlerCollection contexts = new ContextHandlerCollection(); contexts.setHandlers(handlerList.toArray(new
	 * Handler[handlerList.size()])); server.setHandler(contexts); }
	 */

	/**
	 * Load all the plugins into a map.
	 */
	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
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
}
