/*
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
import org.eclipse.jetty.webapp.WebAppContext;

import edu.stanford.isis.epad.common.plugins.EPadFiles;
import edu.stanford.isis.epad.common.plugins.PluginConfig;
import edu.stanford.isis.epad.common.plugins.PluginHandler;
import edu.stanford.isis.epad.common.plugins.PluginHandlerMap;
import edu.stanford.isis.epad.common.plugins.PluginServletHandler;
import edu.stanford.isis.epad.common.plugins.ePadPluginController;
import edu.stanford.isis.epad.common.plugins.impl.ClassFinderTestUtils;
import edu.stanford.isis.epad.common.plugins.impl.EPadFilesImpl;
import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.ResourceUtils;
import edu.stanford.isis.epadws.handlers.admin.ImageCheckHandler;
import edu.stanford.isis.epadws.handlers.admin.ResourceCheckHandler;
import edu.stanford.isis.epadws.handlers.admin.ServerStatusHandler;
import edu.stanford.isis.epadws.handlers.aim.AimResourceHandler;
import edu.stanford.isis.epadws.handlers.coordination.CoordinationHandler;
import edu.stanford.isis.epadws.handlers.dicom.DICOMDeleteHandler;
import edu.stanford.isis.epadws.handlers.dicom.DICOMHeadersHandler;
import edu.stanford.isis.epadws.handlers.dicom.DICOMSearchHandler;
import edu.stanford.isis.epadws.handlers.dicom.DICOMSeriesOrderHandler;
import edu.stanford.isis.epadws.handlers.dicom.DICOMSeriesTagHandler;
import edu.stanford.isis.epadws.handlers.dicom.DICOMVisuHandler;
import edu.stanford.isis.epadws.handlers.dicom.WadoHandler;
import edu.stanford.isis.epadws.handlers.event.EventSearchHandler;
import edu.stanford.isis.epadws.handlers.plugin.EPadPluginHandler;
import edu.stanford.isis.epadws.handlers.xnat.XNATProjectHandler;
import edu.stanford.isis.epadws.handlers.xnat.XNATSessionHandler;
import edu.stanford.isis.epadws.handlers.xnat.XNATSubjectHandler;
import edu.stanford.isis.epadws.processing.leveling.WindowLevelFactory;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
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
	private static final EPADConfig proxyConfig = EPADConfig.getInstance();

	/**
	 * Starts EPad web server and sets several contexts to be used by restlets.
	 * <p>
	 * The application listens on the port indicated by the property <i>ePadClientPort</i> in proxy-config.properties.
	 * <p>
	 * The current directory must be set to the bin subdirectory before calling the start scripts associated with this
	 * application.
	 * 
	 * @param args String[]
	 * @see EPADConfig
	 */
	public static void main(String[] args)
	{
		ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
		Server server = null;

		try {
			int port = proxyConfig.getIntParam("ePadClientPort");
			log.info("Starting the ePAD web service. Build date: " + EPadWebServerVersion.getBuildDate());
			initPlugins(); // Initialize plugin classes
			startSupportThreads();
			server = new Server(port);
			addHandlers(server);
			testPluginImpl();
			Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());

			// See: http://restlet-discuss.1400322.n2.nabble.com/Jetty-Webapp-td7313234.html

			log.info("Starting Jetty on port " + port);
			server.start();
			/*
			 * Component component = new Component(); component.getServers().add(Protocol.HTTP, 8081);
			 * component.getDefaultHost().attach(new EPADWebService());
			 * 
			 * log.info("Starting test Restlet component"); component.start();
			 */
			/*
			 * ServletContextHandler restletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
			 * restletContext.setContextPath("/restlet"); ServerServlet serverServlet = new ServerServlet(); ServletHolder
			 * servletHolder = new ServletHolder(serverServlet); servletHolder.setInitParameter("org.restlet.application",
			 * "edu.stanford.isis.epadws.EPADWebService"); restletContext.addServlet(servletHolder, "/*");
			 */
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
			QueueAndWatcherManager.getInstance().buildAndStart();
			MySqlInstance.getInstance().startup();
			log.info("Startup of MySql database was successful.");
		} catch (Exception e) {
			log.warning("Failed to start MySql database.", e);
		}
		WindowLevelFactory.getInstance().buildAndStart();
	}

	private static void addHandlers(Server server)
	{
		List<Handler> handlerList = new ArrayList<Handler>();

		loadPluginClasses();

		addHandlerAtContextPath(new XNATSessionHandler(), "/session", handlerList);
		addHandlerAtContextPath(new XNATProjectHandler(), "/projects", handlerList);
		addHandlerAtContextPath(new XNATSubjectHandler(), "/subjects", handlerList);
		addHandlerAtContextPath(new ServerStatusHandler(), "/status", handlerList);
		addHandlerAtContextPath(new ImageCheckHandler(), "/imagecheck", handlerList);
		addHandlerAtContextPath(new CoordinationHandler(), "/coordination", handlerList);
		addHandlerAtContextPath(new DICOMSearchHandler(), "/searchj", handlerList);
		addHandlerAtContextPath(new DICOMSeriesOrderHandler(), "/seriesorderj", handlerList);
		addHandlerAtContextPath(new DICOMDeleteHandler(), "/dicomdelete", handlerList); // TODO Deprecated?
		addHandlerAtContextPath(new DICOMHeadersHandler(), "/dicomtagj", handlerList);
		addHandlerAtContextPath(new DICOMSeriesTagHandler(), "/seriestag", handlerList);
		addHandlerAtContextPath(new DICOMVisuHandler(), "/dicomparam", handlerList);
		addHandlerAtContextPath(new AimResourceHandler(), "/aimresource", handlerList);
		addHandlerAtContextPath(new WadoHandler(), "/eWado", handlerList);
		addHandlerAtContextPath(new EventSearchHandler(), "/eventresource", handlerList);
		addHandlerAtContextPath(new EPadPluginHandler(), "/plugin", handlerList);

		addWebAppAtContextPath(handlerList, "ePad.war", "/epad");

		addHandlerAtContextPath(new ResourceCheckHandler(), "/resources", handlerList);
		addFileServerAtContextPath(ResourceUtils.getEPADWebServerResourcesDir(), handlerList, "/resources");

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
	 * Adds a WAR file from the web-apps directory at a context path.
	 * 
	 * @param handlerList List of handlers
	 * @param warFileName String war file name, with or without extension (e.g., ePad.war)
	 * @param contextPath The context to add the war file (e.g., /epad)
	 */
	private static void addWebAppAtContextPath(List<Handler> handlerList, String warFileName, String contextPath)
	{
		String webAppPath = ResourceUtils.getEPADWebServerWebappsDir() + warFileName;
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

	private static void testPluginImpl()
	{
		try {
			EPadFiles ePadFiles = new EPadFilesImpl();
			File baseDirFile = ePadFiles.getBaseDir();
			log.info("baseDirFile = " + baseDirFile.getAbsolutePath());
			String baseDirPath = ePadFiles.getBaseDirPath();
			log.info("baseDirPath = " + baseDirPath);

			// List<String> allStudies = ePadFiles.getStudies();
			// for (String currStudy : allStudies) {
			// log.info("Study: " + currStudy);
			// }
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
}
