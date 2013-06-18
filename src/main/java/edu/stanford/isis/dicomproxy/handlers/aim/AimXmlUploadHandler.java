/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.handlers.aim;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlDocument;
import com.sleepycat.dbxml.XmlManager;

import edu.stanford.isis.dicomproxy.server.ProxyLogger;

/**
 * @deprecated
 * @author kurtz
 *
 */


public class AimXmlUploadHandler extends AbstractHandler {

	private static ProxyLogger logger = ProxyLogger.getInstance();
	

    @Override
    public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        try{

        	res.setContentType("text/plain");
            res.setStatus(HttpServletResponse.SC_OK);
            res.setHeader("Access-Control-Allow-Origin","*");

            request.setHandled(true);

            String field = req.getParameter("field");
            PrintWriter out = res.getWriter();

            out.println("<html>");
            out.println("<body>");
            out.println("You entered \"" + field + "\" into the text box.");
            out.println("</body>");
            out.println("</html>");

            out.flush();
        	
        	logger.info("calling readDb");
        	readDb();
        	

    		
        }catch (Exception e){
            logger.warning("Request failed.",e);
        }
    }
	
    // This function is used to ensure that databases are
    // properly closed, even on exceptions
    private static void cleanup(XmlManager mgr, XmlContainer cont) {
    logger.info("cleanup");
	try {
	    if (cont != null)
		cont.delete();
	    if (mgr != null)
		mgr.delete();
	} catch (Exception e) {
	    // ignore exceptions in cleanup
		logger.info("cleanup exception ");
	}
    }

    private static void readDb()  {
    	logger.info("readDb>>>");
	
	// An empty string means an in-memory container, which
	// will not be persisted
	String containerName = "";
	String content = "<hello>Hello World</hello>";
	String docName = "doc";
	
	logger.info("readDb inits");
	
	XmlManager mgr = null;
	XmlContainer cont = null;
	
	logger.info("readDb xml inits");
	
	try {
	    // All BDB XML programs require an XmlManager instance
		logger.info("readDb try creating an xml manager");
	    mgr = new XmlManager();
	    logger.info("readDb got one, try creating a cantainer");
	    cont = mgr.createContainer(containerName);
	    logger.info("readDb got a container, try putting a document");

	    cont.putDocument(docName, content);
	    logger.info("readDb put a document");

	    // Now, get the document
	    XmlDocument doc = cont.getDocument(docName);
	    String name = doc.getName();
	    String docContent = doc.getContentAsString();

	    // print it
	    logger.info("Document name: " + name + " Content: " +  docContent);

	} catch (Exception e) {
	    logger.info("Exception during readDb: ");
	}
	finally {
	    cleanup(mgr, cont);
	}
	
	logger.info("readDb<<<");
    }


}
