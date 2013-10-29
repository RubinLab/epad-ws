/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.resources.server.DICOMSearchServerResource;
import edu.stanford.isis.epadws.resources.server.EPadWebServiceServerResource;
import edu.stanford.isis.epadws.resources.server.WindowLevelServerResource;

/**
 * Entry point for the EPad Web Service. In the architecture this is the between the web application and the DICOM
 * server.
 * <p>
 * Start an embedded server, and all the threads required for this application, and attach all routes to REST resources.
 * 
 */
public class EPADWebService extends Application
{
	private static final EPADLogger log = EPADLogger.getInstance();

	@Override
	public void start() throws Exception
	{
		super.start();
		log.info("++++++++++++++++++++++++++++++++Restlet started");
	}

	// Called when the Restlet framework initializes the {@link EPADWebService} application.
	@Override
	public Restlet createInboundRoot()
	{
		Router router = new Router(getContext());

		log.info("************************************Context: " + getContext());

		router.attach("/search", DICOMSearchServerResource.class);

		log.info("************************************Added /search");

		router.attach("/server/{operation}", EPadWebServiceServerResource.class);
		router.attach("/level", WindowLevelServerResource.class);
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
		// router.attach("/plugins/{pluginName}", PluginServerResource.class);

		/*
		 * Directory resourcesDirectory = new Directory(getContext(), "file://" +
		 * ResourceUtils.getEPADWebServerResourcesDir()); resourcesDirectory.setListingAllowed(true);
		 * router.attach("/resources", resourcesDirectory);
		 * 
		 * Directory warDirectory = new Directory(getContext(), "file://" + ResourceUtils.getEPADWebServerWebappsDir()); //
		 * warDirectory.setNegotiatingContent(false); // warDirectory.setIndexName("Web_pad.html"); router.attach("/epad",
		 * warDirectory);
		 */
		// getConnectorService().getClientProtocols().add(Protocol.FILE);

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
}