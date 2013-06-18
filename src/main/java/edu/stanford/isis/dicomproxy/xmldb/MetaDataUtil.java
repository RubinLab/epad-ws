/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.xmldb;

import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlDocument;
import com.sleepycat.dbxml.XmlException;
import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlMetaData;
import com.sleepycat.dbxml.XmlMetaDataIterator;
import com.sleepycat.dbxml.XmlQueryContext;
import com.sleepycat.dbxml.XmlResults;
import com.sleepycat.dbxml.XmlValue;
import java.util.Set;
import org.restlet.Request;
import org.restlet.data.Form;

import edu.stanford.isis.dicomproxy.server.ProxyLogger;

/**
 * Utility function for dealing with XML objects.
 *
 * @see edu.stanford.isis.ats.xmldb.MetaDataUtil
 */
public class MetaDataUtil
{


    public static final String METADATA_URI = "http://metaData/URI";
    public static final String OWNER = "owner";

    public static final String DATE_PUBLISHED = "date-published";
    public static final String ALLOWED_USER = "allowed-user";
    public static final String PERMISSION = "permission";

    public static final String PERM_PUBLIC = "public";
    public static final String PERM_PRIVATE = "private";
    public static final String PERM_GROUP = "group";

    private static final ProxyLogger log = ProxyLogger.getInstance();

    public static String getOwnerFromRequest(Request req){
        if(req==null){
            throw new IllegalArgumentException("Request was null.");
        }

        //ToDo: need to get the owner from the request cookie!?!

        return "annon";
    }

//    public static XmlResults queryForAdmin(XmlManager manager, String collection, String namespace)
//            throws XmlException
//    {
//        XmlQueryContext context = manager.createQueryContext();
//        context.setNamespace("tc", namespace);
//        String query = "collection('"+collection+"')/tc:TemplateContainer";
//        return manager.query(query, context);
//    }

    public static String getUidAttribFromDoc(XmlDocument xmlDoc){
        String content = "no content";
        try {

            content = xmlDoc.getContentAsString();
            int part1 = content.indexOf("uid=");
            int part2 = content.indexOf("=", part1);

            String uidSnip = content.substring(part2, part2+100);

            String cleanSnip = uidSnip.replaceAll("\"", " ");
            cleanSnip = cleanSnip.replaceAll("'", " ");

            int start = cleanSnip.indexOf(" ");
            int end = cleanSnip.indexOf(" ", start+1);

            String retVal = cleanSnip.substring(start, end+1).trim();

            log.debug("UID: _"+retVal+"_ start="+start+" end="+end);
            return retVal;

        } catch (XmlException ex) {
            log.warning("Couldn't get UID attribute (MDU-72)", ex);
            return "";
        } catch (Exception e) {
            log.warning("Failed get UID attribute (MDU-75)", e);
            log.debug("content="+content);
            return "";
        }
    }

    /**
     *
     * @param metaDataName String
     * @param xmlDocument XmlDocument
     * @return String
     */
    public static String getMetaData(String metaDataName, XmlDocument xmlDocument){
        XmlValue xmlValue = new XmlValue();
        try{
            boolean found = xmlDocument.getMetaData(MetaDataUtil.METADATA_URI, metaDataName, xmlValue);
            if(found && xmlValue!=null){
                return xmlValue.asString();
            }else{
                return "";
            }
        }catch(Exception ex){
            log.warning("Couldn't data for: "+metaDataName,ex);
            return "";
        }
    }


    public static String getDocName(XmlDocument xmlDocument){
        try{
            return xmlDocument.getName();
        }catch(Exception e){
            log.warning("Failed to get name.", e);
            return "";
        }
    }

   /**
     *
     * @param xmlDoc xmlDocument
     */
    public static void debugMetaData(XmlDocument xmlDoc){
        try {
            log.debug("xmlDoc name: "+xmlDoc.getName());
            XmlMetaDataIterator iterator = xmlDoc.getMetaDataIterator();
            XmlMetaData data = iterator.next();
            while(data!=null){
                String name = data.get_name();
                XmlValue xmlValue = data.get_value();
                if(xmlValue!=null){
                  log.debug("META-DATA name: "+data.get_name()+" value: "+data.get_value().asString()+" uri: "+data.get_uri());
                }

                data = iterator.next();
            }

            XmlValue owner = new XmlValue();
            if( xmlDoc.getMetaData(MetaDataUtil.METADATA_URI, MetaDataUtil.OWNER, owner)){
                //log.debug(" owner="+owner);
            }

            String content = xmlDoc.getContentAsString();
            if(content!=null){
            }else{
                log.debug("Content is null.");
            }

        } catch (XmlException ex) {
            log.warning("logMetaData had:", ex);
        } catch (Exception e) {
            log.warning("Continue after logMetaData had exception", e);
        }
    }

   public static String changeMetaDataIfFound(XmlDocument doc, String paramName, Form queryForm){
       try{
           String value = queryForm.getFirstValue(paramName);
           if(value!=null){
               doc.setMetaData(MetaDataUtil.METADATA_URI, paramName, new XmlValue(value));
               return "Changed "+paramName+" to "+value;
           }else{

               log.debug("Meta-data: "+paramName+" not found on queryForm. Data not changed. ");
               Set<String> names = queryForm.getNames();
               StringBuilder sb = new StringBuilder("List of names: ");
               for(String curr : names){ sb.append(curr).append(",");}
               log.debug(sb.toString());
           }
           return null;
       }catch(Exception e){
           log.warning("failed to change metadata parm "+paramName, e);
           return paramName+" had: "+e.getMessage();
       }
   }

    public static XmlDocument getXmlDocumentForUid(String uid, XmlResults xmlResults){

        try{
            int debugIndex=1;
            while(xmlResults.hasNext()){
                XmlDocument xmlDocument = xmlResults.next().asDocument();
                if(xmlDocument!=null){
                    //check if row is owner.
                    String docUid = MetaDataUtil.getUidAttribFromDoc(xmlDocument);
                    if(uid.equalsIgnoreCase(docUid)){
                        return xmlDocument;
                    }
                }
            }//while

            log.info("WARNING: No document was found for uid="+uid);
            return null;
        } catch(XmlException xe) {
            log.warning("XmlException ", xe);
            return null;
        }

    }

   /**
    * If a collection does not exist for a contain then create it, otherwise open
    * the container.
    *
    * @param collection String
    * @param manager XmlManager
    * @return XmlContainer
    * @throws XmlException on error
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
    * ToDo:  Move this and others into a different utility class.
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
    * ToDo: Move this and other into a different utility class.
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

}
