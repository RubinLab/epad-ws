/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.db.aimxml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.stanford.isis.dicomproxy.server.ProxyConfig;
import edu.stanford.isis.dicomproxy.server.ProxyLogger;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlDocument;
import com.sleepycat.dbxml.XmlException;
import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlManagerConfig;
import com.sleepycat.dbxml.XmlResults;


public class XMLDatabaseResource extends ServerResource {

    protected static final ProxyLogger log = ProxyLogger.getInstance();
    ProxyConfig proxyConfig = ProxyConfig.getInstance();

    protected Map<String, String> dbVars;
    protected Environment env;

       /**
        * Constructs a new XMLDatabaseResource, including setting up the
        * environment.
        */
       public XMLDatabaseResource() {
          dbVars = proxyConfig.getAllParams();
          for (String key : dbVars.keySet()) {
             if (dbVars.get(key) == null) {
                log.info("Error: Could not read variable " + key + " in serverConfig.txt. Shutting down!!");
                System.exit(1);
             }
          }

          File envHome = new File(dbVars.get("dbpath"));
          try {

            //log.debug("New Environment envHome="+envHome);
            env = new Environment(envHome, null);
            //printEnvironmentConfig();

          } catch(FileNotFoundException e) {

             //log.info("Creating EnvironmentConfig from scratch.");
             EnvironmentConfig envConfig = new EnvironmentConfig();
             envConfig.setAllowCreate(true);
             envConfig.setInitializeCache(true);
             envConfig.setInitializeLocking(true);
             envConfig.setInitializeLogging(true);
             envConfig.setTransactional(true);
             envConfig.setCacheSize(200 * 1024 * 1024);
             envConfig.setMaxLockers(4000);
             envConfig.setMaxLocks(4000);
             envConfig.setMaxLockObjects(4000);
             try {
                env = new Environment(envHome, envConfig);
                //printEnvironmentConfig();

             } catch(Exception e_create) {
                log.warning("Failed to XML DB Environment. Shutting Down!!", e_create);
                System.exit(1);
             }
          } catch(DatabaseException e) {
             log.warning("Failed to XML DB Environment due to DatabaseException. Shutting Down!!", e);
             System.exit(1);
          } catch (UnsatisfiedLinkError ule) {
              log.warning("Failed to XML DB Environment due to link error with XML Database. Shutting Down!!", ule);
              System.exit(1);
          } catch (NoClassDefFoundError ncdfe) {
             log.warning("Failed to XML DB Environment due to Java classes definitions not found. Shutting Down!!", ncdfe);
             System.exit(1);
          } finally {
//        	  try {
//        		  if (env != null) {
//        			  System.err.println("Close the environment");
//        			  env.close();
//        		  }
//        	  }catch (DatabaseException de) {
//        		  System.err.println(de.getMessage());
//        	  }

          }
       }

        /**
         * This method must be called by all subclasses during the cleanup phase
         * after the XmlManager and XmlContainer have been closed in order to make
         * sure the environment can be used by other resources.
         */
        protected void cleanup() {
            //log.info("cleanup environment");
            try {
                if(env!=null){
                    env.close();
                    env=null;
                }
            } catch(Exception e) {
                log.warning("Exception thrown on closing the environment. ", e);
            }
        }

        @SuppressWarnings("unused")
		private void printEnvironmentConfig(){
        EnvironmentConfig ec;
        try {
            ec = env.getConfig();
            StringBuilder sb = new StringBuilder();
            sb.append("cache-size      : ").append(ec.getCacheSize()).append("\n");
            sb.append("cache-count     : ").append(ec.getCacheCount()).append("\n");
            sb.append("max-mutex       : ").append(ec.getMaxMutexes()).append("\n");
            sb.append("max-locks       : ").append(ec.getMaxLocks()).append("\n");
            sb.append("max-lockers     : ").append(ec.getMaxLockers()).append("\n");
            sb.append("max-lock-objects: ").append(ec.getMaxLockObjects()).append("\n");
            sb.append("lock-partitions : ").append(ec.getLockPartitions()).append("\n");
            sb.append("max-open-file   : ").append(ec.getMaxOpenFiles()).append("\n");

            log.info("EnvironmentConfig: \n"+sb.toString());
        } catch (DatabaseException ex) {
            log.warning("XDR-74: Out-Of-Memory DatabaseException: ", ex);
            System.exit(2);
        }
    }

       /**
        * Puts an error string into an XML format.
        * @param message String
        * @return String
        */
       protected String genErrorXML(String message) {
          return "<error>\n <message>" +
          message.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;") +
          "</message>\n</error>";
       }

       /**
        * Processes GET requests for information about the XML database:
        * listCollections(): list collections in database
        * listDocuments(c): list documents in collection c
        * @return
        */
       @Get
       public Representation getDatabaseInfo() {
          Form queryForm = this.getReference().getQueryAsForm();
          String getFunction = queryForm.getFirstValue("fun");
          String[] funParams = parseParams(queryForm.getFirstValue("params"));
          if (getFunction == null) {
             setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "No function requested");
             return new StringRepresentation(genErrorXML("No function requested."));
          }

          Representation returnRep;
          XmlManager dbManager = null;
          try {
             dbManager = new XmlManager(env, XmlManagerConfig.DEFAULT);

             //List collections
             if (getFunction.equals("listCollections")) {
                String returnString;
                File dbDir = new File(dbVars.get("dbpath"));
                String[] dbDirContents = dbDir.list();
                List<String> validContainers = new ArrayList<String>();
                for (String s : dbDirContents) {
                   if (dbManager.existsContainer(s) != 0)
                      validContainers.add(s);
                }
                returnString = toXml(validContainers);
                returnRep = new StringRepresentation(returnString);
             }
             else if(getFunction.equals("listDocuments")) {
                if(funParams == null || funParams.length < 1) {
                   setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "No parameters passed for function");
                   returnRep = new StringRepresentation(genErrorXML("No parameters passed for function."));
                }
                else {
                   XmlContainer container = null;
                   XmlResults results = null;
                   try {
                      if(dbManager.existsContainer(funParams[0]) == 0) {
                         setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "Collection " + funParams[0] + " does not exist");
                         returnRep = new StringRepresentation(genErrorXML("Collection " + funParams[0] + " does not exist."));
                      }
                      else {
                         container = dbManager.openContainer(funParams[0]);
                         results = container.getAllDocuments(null);
                         XmlDocument doc = dbManager.createDocument();
                         List<String> docNames = new ArrayList<String>();
                         while(results.next(doc)) {
                            docNames.add(doc.getName());
                         }
                         returnRep = new StringRepresentation(toXml(docNames));
                      }
                   } catch(XmlException e) {
                      setStatus(Status.SERVER_ERROR_INTERNAL, "Internal database error");
                      returnRep = new StringRepresentation(genErrorXML("Internal database error."));
                   } finally {
                       XmlDbUtil.deleteXmlResults(results);
                       XmlDbUtil.closeXmlContainer(container);
                   }
                }
             }
             else {
                setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "Unknown function requested");
                returnRep = new StringRepresentation(genErrorXML("Unknown function requested."));
             }
          } catch (DatabaseException e) {
             log.warning("DatabaseException",e);
             setStatus(Status.SERVER_ERROR_INTERNAL, "Internal database error");
             returnRep = new StringRepresentation(genErrorXML("Internal database error: " + e.getMessage()));
          } finally {
              XmlDbUtil.closeXmlManager(dbManager);
          }
          return returnRep;
       }

       /**
        * Returns a String containing XML representing a return value or list of
        * return values.
        * @param results
        * @return A String representation of the XML collection
        */
       protected String toXml(Collection<String> results) {
          String r = "";
          if(results.size() > 1) {
             r += "<list>\n";
          }
          for(String s : results) {
             r += "<value>" + s + "</value>\n";
          }
          if(results.size() > 1) {
             r += "</list>";
          }
          return r;
       }

       private String[] parseParams(String params) {
          if(params == null)
             return null;
          return params.split(",");
       }


}
