/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.xmldb;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import com.sleepycat.dbxml.XmlQueryContext;
import com.sleepycat.dbxml.XmlResults;

import edu.stanford.isis.dicomproxy.server.ProxyConfig;
import edu.stanford.isis.dicomproxy.server.ProxyLogger;

/**
 * This is based on the Aim Template Server XMLDatabaseResource class.
 *
 * @see edu.stanford.isis.ats.xmldb.XMLDatabaseResource
 */
public class XmlDatabaseResource extends ServerResource{

    protected static final ProxyLogger log = ProxyLogger.getInstance();
    ProxyConfig proxyConfig = ProxyConfig.getInstance();

    protected Environment env;

   /**
    * Constructs a new XMLDatabaseResource, including setting up the
    * environment.
    */
   public XmlDatabaseResource() {

      initXmlDbEnvironment();
   }

   protected void initXmlDbEnvironment()
   {

      File envHome = new File(proxyConfig.getParam("dbxml.path"));
      try {
         log.debug("New Environment envHome="+envHome);
         env = new Environment(envHome, null);
         printEnvironmentConfig();

      } catch(FileNotFoundException e) {
         log.warning("envHome not found. envHome=" + envHome, e);

         EnvironmentConfig envConfig = createEnvironmentConfig();
         try {
            log.debug("New Environment envConfig");
            env = new Environment(envHome, envConfig);
         } catch(Exception e_create) {
             log.warning("XDR70: " + e_create.getMessage(), e_create);
             log.info("Exiting.");
            System.exit(1);
         }

      } catch(DatabaseException e) {
         log.warning("XDR75: " + e.getMessage(), e);
         log.info("Exiting. (de)");
         System.exit(1);
      } catch(UnsatisfiedLinkError ule) {
          log.warning("XDR77: " + ule.getMessage(), ule);
          String javaLibPath = System.getProperty("java.library.path");
          log.debug("Check java.library.path. \n "+javaLibPath);
          log.info("Continue running ...");
      } catch(NoClassDefFoundError ncdfe) {
          log.warning("XDR85: " + ncdfe.getMessage(), ncdfe);
          log.info("Continue running ...");
      }
   }


    /**
     * Create the EnvironmentConfig for this application.
     * @return EnvironmentConfig
     */
    private EnvironmentConfig createEnvironmentConfig(){
        EnvironmentConfig envConf = new EnvironmentConfig();

        envConf.setAllowCreate(true);
        envConf.setInitializeCache(true);
        envConf.setInitializeLocking(true);
        envConf.setInitializeLogging(true);
        envConf.setTransactional(true);
        envConf.setCacheSize(200 * 1024 * 1024);
        envConf.setMaxLockers(4000);
        envConf.setMaxLocks(4000);
        envConf.setMaxLockObjects(4000);

        return envConf;
    }

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
    * This method must be called by all subclasses during the cleanup phase
    * after the XmlManager and XmlContainer have been closed in order to make
    * sure the environment can be used by other resources.
    * @throws DatabaseException on close failed.
    */
   protected void cleanup()
        throws DatabaseException
   {
       if(env!=null){
           log.info("Closing Environment");
           env.close();
           env=null;
       }else{
           log.info("cleanup() - com.sleepycat.db.Environment was already null.");
       }
   }

   /**
    * Puts an error string into an XML format.
    * @param message String
    * @return String
    */
   protected String genErrorXML(String message) {

       log.info("(XDR-145) Error: "+message);

      return "<?xml version=\"1.0\"?><error>\n <message>" +
      message.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;") +
      "</message>\n</error>";
   }

   /**
    * Processes GET requests for information about the XML database:
    * listCollections(): list collections in database
    * listDocuments(c): list documents in collection c
    * @return Representation
    */
   @Get
   public Representation getDatabaseInfo() {
      Form queryForm = this.getReference().getQueryAsForm();
      String getFunction = queryForm.getFirstValue("fun");

      log.info("function="+getFunction);

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

            File dbDir = new File(proxyConfig.getParam("dbxml.path"));

            String[] dbDirContents = dbDir.list();
            List<String> validContainers = new ArrayList<String>();
            for (String s : dbDirContents) {
               if (dbManager.existsContainer(s) != 0)
                  validContainers.add(s);
            }
            returnString = toXmlResultList(validContainers);
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
                     returnRep = new StringRepresentation(toXmlResultList(docNames));
                  }
               } catch(XmlException e) {
                  setStatus(Status.SERVER_ERROR_INTERNAL, "Error - internal database (XDR-224)");
                  returnRep = new StringRepresentation(genErrorXML("Error - internal database (XDR-224)."));
               } finally {
                  MetaDataUtil.deleteXmlResults(results);
                  MetaDataUtil.closeXmlContainer(container);
               }
            }
         }
         else {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "Unknown function requested");
            returnRep = new StringRepresentation(genErrorXML("Unknown function requested."));
         }
      } catch (DatabaseException e) {
         log.warning("XDR-243: DatabaseException", e);
         setStatus(Status.SERVER_ERROR_INTERNAL, "Error - internal database (XDR-244)");
         returnRep = new StringRepresentation(genErrorXML("Error - internal database (XDR-244): " + e.getMessage()));
      } finally {
          MetaDataUtil.closeXmlManager(dbManager);
      }
      return returnRep;
   }

   /**
    * Returns a String containing XML representing a return value or list of
    * return values.
    * @param results Collection of String
    * @return A String representation of the XML collection
    */
   protected String toXmlResultList(Collection<String> results) {
      String r = "";
      if(results.size() > 1)
         r += "<list>\n";
      for(String s : results)
         r += "<item>" + s + "</item>\n";
      if(results.size() > 1)
         r += "</list>";
      return r;
   }

//   protected String toXmlTemplateDownload(Collection<String> results){
//
//       for(String s: results){
//           return s;
//       }
//       return "";
//   }


   private String[] parseParams(String params) {
      if(params == null)
         return null;
      return params.split(",");
   }


    /**
     *
     * @param collection String
     * @param namespace String
     * @param queryString String
     * @return List of String
     */
    public List<String> processQuery(String collection, String namespace, String queryString){
      XmlManager dbManager = null;
      XmlContainer dbContainer = null;
      XmlResults queryResults = null;
      List<String> resultList = new ArrayList<String>();

      try {
         verifyEnv(env);
         dbManager = new XmlManager(env, XmlManagerConfig.DEFAULT);
         dbContainer = dbManager.openContainer(collection);

         // Set the context and collection for this query
         XmlQueryContext dbQueryContext = dbManager.createQueryContext(XmlQueryContext.LiveValues, XmlQueryContext.Eager);
         if (namespace != null && !namespace.isEmpty())
            dbQueryContext.setNamespace("", namespace);

         queryResults = dbManager.query(queryString, dbQueryContext);

         log.debug("Found "+queryResults.size()+" queryResults");

         while (queryResults.hasNext()) {
            resultList.add(queryResults.next().asString());
         }
      } catch (DatabaseException e) {
         log.warning("XDR-260: DatabaseException for XQuery:"+queryString, e);
         setStatus(Status.SERVER_ERROR_INTERNAL, "XQuery evaluation error: " + e.getMessage());
         resultList = null;
      } catch (Exception e) {
         log.warning("XDR-264: Exception", e);
         setStatus(Status.SERVER_ERROR_INTERNAL, "Internal error: " + e.getMessage());
         resultList = null;
      } finally {
         try {
             MetaDataUtil.deleteXmlResults(queryResults);
             MetaDataUtil.closeXmlContainer(dbContainer);
             MetaDataUtil.closeXmlManager(dbManager);
             cleanup();
         } catch (DatabaseException e) {
             log.warning("XDR-277: DatabaseException", e);
         }
      }

      return resultList;
    }


    /**
     * ToDo: return this name to "getCollection"
     * @param queryForm Form
     * @return String
     */
    public static String getCollection(Form queryForm){
        String collection = queryForm.getFirstValue("collection");
        if(collection==null || collection.isEmpty()){
            //NOTE: The default container value is "aim.dbxml"
            collection = ProxyConfig.getInstance().getParam("xmldb.collection");
        }

        if(collection==null){
            collection="aim.dbxml";
        }
        log.info("Setting collection to: '"+collection+"'");

        return collection;
    }


    /**
     *
     * @param queryForm Form
     * @return String
     */
    public static String getNamespace(Form queryForm){
        String namespace = queryForm.getFirstValue("namespace");
        if(namespace==null){
            namespace="gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIMTemplate";
            log.debug("adding namespace");
        }
        return namespace;
   }

    /**
     * This method is for debugging why the Environment isn't always set.
     * @param env Environment
     */
    private void verifyEnv(Environment env){
        try{
            if(env==null){
                log.info("env==null. calling initXmlDbEnv...()");
                initXmlDbEnvironment();
            }else{
                log.debug("env version: "+Environment.getVersionString());
                log.debug("   env home: "+env.getHome().getAbsolutePath());
                //ToDo: might want to print stats here.

                EnvironmentConfig ec = env.getConfig();
                log.debug("EnvConfig lock partitions: "+ec.getLockPartitions());
                //ToDo: might want to print stats here.
            }

        }catch(Exception e){
            log.warning("verifyEnv", e);
        }

    }

}
