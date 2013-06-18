/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.db.aimxml;

import org.restlet.data.*;
import org.restlet.representation.*;
import org.restlet.resource.*;

import com.sleepycat.db.*;
import com.sleepycat.dbxml.*;


public class XQueryServerResource extends XMLDatabaseResource{

    /**
     * Processes a POST request for an XQuery evaluation.
     * @param entity
     * @return
     */
    @Post
    public Representation retrieveXML(Representation entity) {
       log.debug("retrieveXML " );
       
       MediaType returnType = MediaType.TEXT_PLAIN;
       Form queryForm = new Form(entity);
       String namespace = queryForm.getFirstValue("namespace");
       String queryString = queryForm.getFirstValue("xquery");
       if (queryString == null) {
          setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "No XQuery expression detected");
          return new StringRepresentation(genErrorXML("No XQuery expression detected."), returnType);
       }
       int startInd = queryString.indexOf("collection(\"") + 12;
       int endInd = queryString.substring(startInd).indexOf("\")")+startInd;
       if(startInd < 0 || endInd < 0) {
          setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "The XQuery must be made in the context of a collection, no collection specified");
          return new StringRepresentation(genErrorXML("The XQuery must be made in the context of a collection, no collection specified."));
       }
       String collection = queryString.substring(startInd, endInd);

       XmlManager dbManager = null;
       XmlContainer dbContainer = null;
       XmlResults queryResults = null;
       String returnString = "";

       try {
          dbManager = new XmlManager(env, XmlManagerConfig.DEFAULT);
          dbContainer = dbManager.openContainer(collection);

          // Set the context and collection for this query
          XmlQueryContext dbQueryContext = dbManager.createQueryContext(XmlQueryContext.LiveValues, XmlQueryContext.Eager);
          if (namespace != null && !namespace.isEmpty())
             dbQueryContext.setNamespace("", namespace);

          queryResults = dbManager.query(queryString, dbQueryContext);
          String res = "<resultList>\n";
          while (queryResults.hasNext()) {
                res += "<result>\n" + queryResults.next().asString() + "\n</result>\n";
          }
          returnString = res + "</resultList>";
       } catch (DatabaseException e) {
          System.err.println(e.getMessage());
          setStatus(Status.SERVER_ERROR_INTERNAL, "XQuery evaluation error");
          returnString = genErrorXML("XQuery evaluation error:\n" + e.getMessage());
       } catch (Exception e) {
          System.err.println(e.getMessage());
          setStatus(Status.SERVER_ERROR_INTERNAL, "Internal error");
          returnString = genErrorXML("Internal error:\n" + e.getMessage());
       } finally {
          try {
             if(queryResults != null)
                queryResults.delete();
             if (dbContainer != null)
                dbContainer.close();
             if (dbManager != null)
                dbManager.close();
             cleanup();
          } catch (DatabaseException e) {
             System.err.println(e.getMessage());
          }
       }

       return new StringRepresentation(returnString, returnType);
    }

}
