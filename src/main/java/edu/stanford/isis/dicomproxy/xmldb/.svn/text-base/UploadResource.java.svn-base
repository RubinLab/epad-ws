/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.xmldb;

import org.restlet.data.*;
import org.restlet.representation.*;
import org.restlet.resource.*;
import org.restlet.util.Series;
import org.restlet.engine.http.header.DispositionReader;
import org.restlet.engine.http.header.HeaderConstants;
import org.restlet.ext.fileupload.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;

import com.sleepycat.db.*;
import com.sleepycat.dbxml.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 *
 */
public class UploadResource extends XmlDatabaseResource
{

    /**
        * Processes POST requests of file upload.
        * @param entity Representation
        * @return Representation
        */
       @Post
       public Representation receiveRepresentation(Representation entity) {
          if (entity != null) {
             if(entity.getMediaType().equals(MediaType.MULTIPART_FORM_DATA, true))
                return receiveMultipartFormData();
             else if(entity.getMediaType().equals(MediaType.APPLICATION_XML, true))
                return receiveXmlFile(entity);
          }
          return new StringRepresentation("Error: Form type must be multipart/form-data or file");
       }

       private Representation receiveXmlFile(Representation entity) {
          Representation returnRep;
          File storeDir = new File(proxyConfig.getParam("dbxml.path"));
          try {
             @SuppressWarnings("unchecked")
             Series<Parameter> params = (Series<Parameter>) this.getRequest().getAttributes().get("org.restlet.http.headers");
             String header = params.getFirstValue(HeaderConstants.HEADER_CONTENT_DISPOSITION);
             if(header == null)
                throw new Exception("No Content-Disposition found in header.");
             Disposition disp = new DispositionReader(header).readValue();
             String containerName = disp.getParameters().getFirstValue("collection");

             File tempFile = new File(storeDir, disp.getFilename());

             log.info("Uploading temp AIM.XML file : "+tempFile.getAbsolutePath());

             FileOutputStream fos = new FileOutputStream(tempFile);
             entity.write(fos);
             fos.close();
             List<File> uploaded = new ArrayList<File>();
             uploaded.add(tempFile);
             returnRep = addFiles(uploaded, containerName);
          } catch(Exception e) {
             returnRep = new StringRepresentation("File upload failed: " + e.getMessage());
          }

          return returnRep;
       }

       private Representation receiveMultipartFormData() {
          File storeDir = new File(proxyConfig.getParam("dbxml.path"));
          DiskFileItemFactory itemFactory = new DiskFileItemFactory();
          itemFactory.setSizeThreshold(1024000);
          RestletFileUpload fileUpload = new RestletFileUpload(itemFactory);

          List<FileItem> files;
          try {
             files = fileUpload.parseRequest(getRequest());
          } catch (FileUploadException e) {
              log.warning("UR-81", e);
             files = new ArrayList<FileItem>();
          }

          log.debug("Multipart- found: "+files.size()+" files to upload.");

          String containerName = null;
          List<File> uploaded = new ArrayList<File>();
          for (FileItem fi : files) {
             // Check to see if the FileItem is in fact the form field containing
             // the container we should use
             if (fi.isFormField() && fi.getFieldName().equals("collection")) {
                containerName = fi.getString();
             } else if (fi.getName() != null && fi.getName().length() > 0) {

                String onlyFileName = getFileNameAtEndOfPath(fi);
                File file = new File(storeDir, onlyFileName);

                boolean failed = false;
                try {
                   fi.write(file);
                } catch (Exception e) {
                   log.warning("UR-97", e);
                   failed = true;
                }
                if (!failed)
                   uploaded.add(file);
                   log.debug("Mulitpart uploaded: "+file.getAbsolutePath());
             }
          }

          return addFiles(uploaded, containerName);
       }

       private Representation addFiles(List<File> uploaded, String containerName) {
          Representation returnRep;

          XmlManager manager = null;
          XmlContainer container = null;
          log.debug("Uploading: "+ uploaded.size()+" files to "+containerName);
          String returnString = "Files added:\n";
          try {
             manager = new XmlManager(env, XmlManagerConfig.DEFAULT);
             if (manager.existsContainer(containerName) == 0) {
                returnString = "Collection created.\n" + returnString;
                container = manager.createContainer(containerName);
             } else {
                container = manager.openContainer(containerName);
                returnString = "Added to collection " + containerName + ".\n" + returnString;
             }

             XmlDocumentConfig docConfig = XmlDocumentConfig.DEFAULT;
             docConfig.setGenerateName(false);
             for (File f : uploaded) {
                 XmlInputStream xis = manager.createLocalFileInputStream(f.getPath());
                container.putDocument(f.getName(), xis, docConfig);
                returnString += f.getName() + "\n";

                XmlDocument doc = container.getDocument(f.getName());
                addMetaData(doc);
                container.updateDocument(doc);
                log.debug("Updated _"+f.getName()+"_ with meta-data.");
             }
             returnRep = new StringRepresentation(returnString);
          } catch (DatabaseException e) {
              log.warning("(UR-152) Failed to upload file.", e);
             returnRep = new StringRepresentation("Error - internal database (UR-167): " + e.getMessage());
          } catch (Exception e) {
              log.warning("(UR-155) Error while uploading file.", e);
              returnRep = new StringRepresentation("Error (UR-170): " + e.getMessage());
          } finally {
             try {
                //Delete local copies of files
                for(File f : uploaded) {
                    try{
                        boolean success = f.delete();
                        if(!success){
                            log.info("Failed to delete file: "+f.getAbsolutePath());
                        }
                    }catch(Exception e){ log.warning("Failed to close uploaded file.", e); }
                }
                MetaDataUtil.closeXmlContainer(container);
                MetaDataUtil.closeXmlManager(manager);
                cleanup();
                log.debug("Upload complete.");
             } catch (DatabaseException e) {
                log.warning("UR-168", e);
             }
          }

          return returnRep;
       }

        private void addMetaData(XmlDocument doc){
            try{
                //add owner to file.
                doc.setMetaData(
                        MetaDataUtil.METADATA_URI,
                        MetaDataUtil.OWNER,
                        new XmlValue(MetaDataUtil.getOwnerFromRequest(this.getRequest())));

                doc.setMetaData(
                        MetaDataUtil.METADATA_URI,
                        MetaDataUtil.DATE_PUBLISHED,
                        new XmlValue(""+System.currentTimeMillis()));

                doc.setMetaData(
                        MetaDataUtil.METADATA_URI,
                        MetaDataUtil.PERMISSION,
                        new XmlValue(MetaDataUtil.PERM_PUBLIC));

                log.debug("Added meta-data: owner="+MetaDataUtil.getOwnerFromRequest(this.getRequest()));

            }catch(Exception e){
                log.warning("Failed to add meta-data", e);
            }
        }

       /**
        * Pull out just the file-name from the path.
        * @param fi FileItem
        * @return String just file-name.
        */
       private String getFileNameAtEndOfPath(FileItem fi){

           String name = fi.getName();
           log.debug("FileItem name: "+name);

           String retVal = name;

           int lastSlash = name.lastIndexOf("\\");
           if(lastSlash>0){
               retVal = name.substring(lastSlash);
               log.debug("getFileNameAtEndOfPath="+retVal);
           }
           return retVal;
       }

}
