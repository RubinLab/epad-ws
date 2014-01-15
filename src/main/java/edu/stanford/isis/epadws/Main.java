/*
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.xml.sax.SAXException;

import edu.stanford.isis.epad.common.plugins.PluginConfig;
import edu.stanford.isis.epad.common.plugins.PluginController;
import edu.stanford.isis.epad.common.plugins.PluginHandlerMap;
import edu.stanford.isis.epad.common.plugins.PluginServletHandler;
import edu.stanford.isis.epad.common.plugins.impl.EPadFilesImpl;
import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.EPADResources;
import edu.stanford.isis.epadws.handlers.admin.ImageCheckHandler;
import edu.stanford.isis.epadws.handlers.admin.ResourceCheckHandler;
import edu.stanford.isis.epadws.handlers.admin.ServerStatusHandler;
import edu.stanford.isis.epadws.handlers.aim.AimResourceHandler;
import edu.stanford.isis.epadws.handlers.coordination.CoordinationHandler;
import edu.stanford.isis.epadws.handlers.dicom.DICOMDeleteHandler;
import edu.stanford.isis.epadws.handlers.dicom.DICOMHeadersHandler;
import edu.stanford.isis.epadws.handlers.dicom.DICOMSearchHandler;
import edu.stanford.isis.epadws.handlers.dicom.DICOMSeriesOrderHandler;
import edu.stanford.isis.epadws.handlers.dicom.DICOMWindowingHandler;
import edu.stanford.isis.epadws.handlers.dicom.DicomSegmentationPathHandler;
import edu.stanford.isis.epadws.handlers.dicom.PatientDeleteHandler;
import edu.stanford.isis.epadws.handlers.dicom.WadoHandler;
import edu.stanford.isis.epadws.handlers.event.EventHandler;
import edu.stanford.isis.epadws.handlers.event.ProjectEventHandler;
import edu.stanford.isis.epadws.handlers.plugin.EPadPluginHandler;
import edu.stanford.isis.epadws.handlers.xnat.XNATProjectHandler;
import edu.stanford.isis.epadws.handlers.xnat.XNATSessionHandler;
import edu.stanford.isis.epadws.handlers.xnat.XNATSubjectHandler;
import edu.stanford.isis.epadws.persistence.Database;
import edu.stanford.isis.epadws.processing.leveling.WindowLevelFactory;
import edu.stanford.isis.epadws.processing.pipeline.threads.ShutdownHookThread;
import edu.stanford.isis.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.isis.epadws.processing.pipeline.watcher.QueueAndWatcherManager;

// TODO Add a generic authentication handler.
// See: https://github.com/eclipse/jetty.project/blob/master/examples/embedded/src/main/java/org/eclipse/jetty/embedded/SecuredHelloHandler.java

/**
 * Entry point for the EPad Web Service. In the architecture this is the between the web application and the DICOM
 * server.
 * <p>
 * Start an embedded Jetty server and all the threads required for this application.
 * 
 */
public class Main
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final EPADConfig epadConfig = EPADConfig.getInstance();

	/**
	 * Starts EPad web server
	 * <p>
	 * The application listens on the port indicated by the property <i>ePadClientPort</i> in proxy-config.properties.
	 * <p>
	 * The current directory must be set to the ePAD bin subdirectory before calling the start scripts associated with
	 * this application.
	 * 
	 * @param args String[]
	 * @see EPADConfig
	 */
	public static void main(String[] args)
	{
		ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
		Server server = null;

		try {
			int port = epadConfig.getIntegerPropertyValue("ePadClientPort");
			log.info("Starting the ePAD web service. Build date: " + EPadWebServerVersion.getBuildDate());
			initializePlugins();
			startSupportThreads();
			server = new Server(port);
			configureJettyServer(server);
			addHandlers(server);
			Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
			log.info("Starting Jetty on port " + port);
			server.start();
			server.join();
		} catch (BindException be) {
			log.severe("Bind exception", be);
			Throwable t = be.getCause();
			log.warning("Bind exception cause: " + be.getMessage(), t);
		} catch (SocketException se) {
			log.severe("Cannot bind to all sockets", se);
		} catch (Exception e) {
			log.severe("Fatal Exception. Shutting down EPad Web Service", e);
		} catch (Error err) {
			log.severe("Fatal Error. Shutting down EPad Web Service", err);
		} finally {
			log.info("#####################################################");
			log.info("############# Shutting down EPad Web Service ########");
			log.info("#####################################################");

			shutdownSignal.shutdownNow();
			stopServer(server);
			Database.getInstance().shutdown();
			QueueAndWatcherManager.getInstance().shutdown();
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

	private static void configureJettyServer(Server server)
	{
		try {
			String jettyConfigFilePath = EPADResources.getEPADWebServerJettyConfigFilePath();
			FileInputStream jettyConfigFileStream = new FileInputStream(jettyConfigFilePath);
			XmlConfiguration configuration = new XmlConfiguration(jettyConfigFileStream);
			configuration.configure(server);
			log.info("Jetty server configured using configuration file " + jettyConfigFilePath);
		} catch (FileNotFoundException e) {
			log.warning("Could not find Jetty configuration file " + EPADResources.getEPADWebServerJettyConfigFilePath());
		} catch (SAXException e) {
			log.warning("SAX error reading Jetty configuration file " + EPADResources.getEPADWebServerJettyConfigFilePath(),
					e);
		} catch (IOException e) {
			log.warning("IO error reading Jetty configuration file " + EPADResources.getEPADWebServerJettyConfigFilePath(), e);
		} catch (Exception e) {
			log.warning("Error processing Jetty configuration file " + EPADResources.getEPADWebServerJettyConfigFilePath(), e);
		}
	}

	private static void initializePlugins()
	{
		PluginController controller = PluginController.getInstance();
		controller.setImpl(new EPadFilesImpl());
	}

	private static void startSupportThreads()
	{
		log.info("Starting support threads....");

		try {
			QueueAndWatcherManager.getInstance().buildAndStart();
			Database.getInstance().startup();
			log.info("Startup of database was successful");
		} catch (Exception e) {
			log.warning("Failed to start database", e);
		}
		WindowLevelFactory.getInstance().buildAndStart();
	}

	private static void addHandlers(Server server)
	{
		List<Handler> handlerList = new ArrayList<Handler>();

		loadPluginClasses();

		addHandlerAtContextPath(new XNATSessionHandler(), "/epad/session", handlerList);
		addHandlerAtContextPath(new XNATProjectHandler(), "/epad/projects", handlerList);
		addHandlerAtContextPath(new XNATSubjectHandler(), "/epad/subjects", handlerList);
		addHandlerAtContextPath(new ServerStatusHandler(), "/epad/status", handlerList);
		addHandlerAtContextPath(new ImageCheckHandler(), "/epad/imagecheck", handlerList);
		addHandlerAtContextPath(new CoordinationHandler(), "/epad/coordination", handlerList);
		addHandlerAtContextPath(new DICOMSearchHandler(), "/epad/searchj", handlerList);
		addHandlerAtContextPath(new DICOMSeriesOrderHandler(), "/epad/seriesorderj", handlerList);
		addHandlerAtContextPath(new DICOMDeleteHandler(), "/epad/dicomdelete", handlerList);
		addHandlerAtContextPath(new DICOMHeadersHandler(), "/epad/dicomtagj", handlerList);
		addHandlerAtContextPath(new DicomSegmentationPathHandler(), "/epad/segmentationpath", handlerList);
		addHandlerAtContextPath(new DICOMWindowingHandler(), "/epad/dicomparam", handlerList);
		addHandlerAtContextPath(new PatientDeleteHandler(), "/epad/patientdelete", handlerList);
		addHandlerAtContextPath(new AimResourceHandler(), "/epad/aimresource", handlerList);
		addHandlerAtContextPath(new WadoHandler(), "/epad/wado", handlerList);
		addHandlerAtContextPath(new EventHandler(), "/epad/eventresource", handlerList);
		addHandlerAtContextPath(new ProjectEventHandler(), "/epad/events", handlerList);
		addHandlerAtContextPath(new EPadPluginHandler(), "/epad/plugin", handlerList);

		addWebAppAtContextPath(handlerList, "ePad.war", "/epad");

		addHandlerAtContextPath(new ResourceCheckHandler(), "/epad/resources", handlerList);
		addFileServerAtContextPath(EPADResources.getEPADWebServerResourcesDir(), handlerList, "/epad/resources");

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(handlerList.toArray(new Handler[handlerList.size()]));
		server.setHandler(contexts);
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
		ContextHandler contextHandler = new ContextHandler(contextPath);

		contextHandler.setResourceBase(".");
		contextHandler.setClassLoader(Thread.currentThread().getContextClassLoader());
		contextHandler.setHandler(handler);
		handlerList.add(contextHandler);

		log.info("Added " + handler.getClass().getName() + " at context: " + contextPath);
	}

	/**
	 * Adds a WAR file from the webapps directory at a context path.
	 * 
	 * @param handlerList List of handlers
	 * @param warFileName String war file name, with or without extension (e.g., ePad.war)
	 * @param contextPath The context to add the war file (e.g., /epad)
	 */
	private static void addWebAppAtContextPath(List<Handler> handlerList, String warFileName, String contextPath)
	{
		String webAppPath = EPADResources.getEPADWebServerWebappsDir() + warFileName;
		if (!contextPath.startsWith("/")) {
			contextPath = "/" + contextPath;
		}
		WebAppContext webAppContext = new WebAppContext(webAppPath, contextPath);
		webAppContext.setTempDirectory(new File("../jetty"));

		handlerList.add(webAppContext);
		log.info("Added " + warFileName + " at context path " + contextPath);
	}

	private static void addFileServerAtContextPath(String baseDir, List<Handler> handlerList, String contextPath)
	{
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
	 * Load all the plugins into a map.
	 */
	private static void loadPluginClasses()
	{
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
