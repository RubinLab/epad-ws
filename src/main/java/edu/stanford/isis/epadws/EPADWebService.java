/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws;

import java.net.BindException;
import java.net.SocketException;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.util.ResourceUtils;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.pipeline.QueueAndWatcherManager;
import edu.stanford.isis.epadws.resources.server.DICOMDeleteServerResource;
import edu.stanford.isis.epadws.resources.server.EPadWebServiceServerResource;
import edu.stanford.isis.epadws.resources.server.WindowLevelServerResource;
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
	private static ProxyConfig proxyConfig = ProxyConfig.getInstance();

	/**
	 * Starts EPad web server and sets several contexts to be used by Restlets.
	 * <p>
	 * The application listens on the port indicated by the property <i>ePadClientPort</i> in proxy-config.properties.
	 * 
	 * @param args String[]
	 * @see ProxyConfig
	 */
	public static void main(String[] args)
	{
		try {
			int port = proxyConfig.getIntParam("ePadClientPort");
			Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
			Component component = new Component();

			component.getServers().add(Protocol.HTTP, port);
			component.getClients().add(Protocol.FILE);
			component.getClients().add(Protocol.WAR);
			component.getClients().add(Protocol.HTTP);
			component.getClients().add(Protocol.CLAP);
			component.getDefaultHost().attach(new EPADWebService());
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

	@Override
	// Called when the Restlet framework initializes the {@link EPADWebService} application.
	public Restlet createInboundRoot()
	{
		Router router = new Router(getContext());

		log.info("Context: " + getContext());

		router.attach("/server/{operation}", EPadWebServiceServerResource.class);
		router.attach("/level", WindowLevelServerResource.class);
		router.attach("/dicomdelete", DICOMDeleteServerResource.class);
		// router.attach("/aimresource", AIMServerResource.class);
		// router.attach("/eWado", WADOServerResource.class);
		// router.attach("/seriestag", DICOMSeriesTagServerResource.class);
		// router.attach("/seriesorder", DICOMSeriesOrderServerResource.class);
		// router.attach("/search", SQLSearchServerResource.class);
		// router.attach("/dicomtag", DICOMHeadersServerResource.class);
		// router.attach("/dicomparam", DICOMVisuServerResource.class);
		// router.attach("/eventresource", EventServerResource.class);
		// router.attach("/segmentationpath", SegmentationPathServerResource.class);
		// router.attach("/plugins/{pluginName}", PluginServerResource.class);

		Directory resourcesDirectory = new Directory(getContext(), "file://" + ResourceUtils.getEPADWebServerResourcesDir());
		resourcesDirectory.setListingAllowed(true);
		router.attach("/resources", resourcesDirectory);

		Directory warDirectory = new Directory(getContext(), "file://" + ResourceUtils.getEPADWebServerWebappsDir());
		// warDirectory.setNegotiatingContent(false);
		// warDirectory.setIndexName("Web_pad.html");
		router.attach("/epad", warDirectory);

		getConnectorService().getClientProtocols().add(Protocol.FILE);

		return router;
	}

	/**
	 * Create the server. The Restlets framework does not require that a particular server implementation is specified
	 * here. An implementation is picked up from loaded JARs. If the org.restlets.ext.jetty JAR is in the class path, for
	 * example, it will be picked up.
	 * 
	 * @return Component
	 */
	public EPADWebService()
	{
		log.info("******************Restlet Application Started **********************");
		setName("EPADWebServer");
		setDescription("EPAD Web Server");
		setOwner("RubinLab");
		setAuthor("RubinLab");
	}

	@Override
	public synchronized void stop() throws Exception
	{
		super.stop();

		ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
		shutdownSignal.shutdownNow();

		MySqlInstance.getInstance().shutdown();
		QueueAndWatcherManager.getInstance().shutdown();
		WindowLevelFactory.getInstance().shutdown();
	}
}