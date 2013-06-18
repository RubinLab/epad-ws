/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.db.aimxml;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class XmlDbApplication extends Application {

    @Override
    public synchronized Restlet createInboundRoot(){

      Router router = new Router(getContext());
      router.attach("", XQueryServerResource.class);
      router.attach("/aimupload", UploadResource.class);
      router.attach("/xquery", SimpleAIMQueryResource.class);
      return router;

    }


}
