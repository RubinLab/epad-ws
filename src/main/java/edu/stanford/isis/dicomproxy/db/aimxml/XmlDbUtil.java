/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.db.aimxml;

import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlException;
import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlResults;
import edu.stanford.isis.dicomproxy.server.ProxyLogger;

/**
 * Utility class for XmlDatabase.
 */
public class XmlDbUtil {

    private static final ProxyLogger log = ProxyLogger.getInstance();

    private XmlDbUtil(){}

       /**
    * If a collection does not exist for a contain then create it, otherwise open
    * the container.
    *
    * @param collection String
    * @param manager XmlManager
    * @return XmlContainer
    * @throws com.sleepycat.dbxml.XmlException on error
    */
   public static XmlContainer getXmlContainerForCollection(String collection, XmlManager manager)
        throws XmlException
    {
        XmlContainer container;
        if (manager.existsContainer(collection) == 0) {
            container = manager.createContainer(collection);
        } else {
            container = manager.openContainer(collection);
        }

        return container;
    }


   /**
    * Close an XmlContainer if it needs to be closed.
    * @param xmlContainer XmlContainer
    */
   public static void closeXmlContainer(XmlContainer xmlContainer){

       if( xmlContainer!=null ){
           try{
               xmlContainer.close();
           }catch(Exception e){
               log.warning("Failed to close XmlContainer", e);
           }
       }
   }

   /**
    * Close an XmlManager if it needs to be closed.
    *
    * @param xmlManager XmlManager
    */
   public static void closeXmlManager(XmlManager xmlManager){

        if( xmlManager!=null){
            try{
                xmlManager.close();
            }catch(Exception e){
                log.warning("Failed to close XmlManager", e);
            }
        }//if

   }//closeXmlManager


   /**
    * Close an XmlResult if it needs to be closed.
    * @param xmlResults XmlResults
    *
    */
   public static void deleteXmlResults(XmlResults xmlResults){

        if( xmlResults!=null ){
            try{
                xmlResults.delete();
            }catch(Exception e){
                log.warning("Failed to delete XmlResults", e);
            }
        }//if
   }

    public static void logEnvironmentConfig(EnvironmentConfig ec){
        try{
            if(ec==null){
                log.info("WARNING: logEnvironmentConfig had null EnvironmentConfig.");
                return;
            }

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
        } catch (Exception e) {
            log.warning("Failed logEnvironmentConfig. ",e);
        }
    }

}
