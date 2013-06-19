/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.db.aimxml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.restlet.data.Disposition;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.engine.http.header.DispositionReader;
import org.restlet.engine.http.header.HeaderConstants;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.util.Series;

import com.sleepycat.db.DatabaseException;
import com.sleepycat.dbxml.XmlContainer;
import com.sleepycat.dbxml.XmlDocument;
import com.sleepycat.dbxml.XmlDocumentConfig;
import com.sleepycat.dbxml.XmlManager;
import com.sleepycat.dbxml.XmlManagerConfig;


public class UploadResource extends XMLDatabaseResource {

    /**
     * Processes POST requests of file upload.
     * @param entity Representation
     * @return Representation
     */
    @Post
    public Representation receiveRepresentation(Representation entity) {
        try{
        if (entity != null) {
            if(entity.getMediaType().equals(MediaType.MULTIPART_FORM_DATA, true)){
        	  log.info("MULTIPART_FORM_DATA MEDIA-TYPE "+entity.getMediaType());
             return receiveMultipartFormData();
            }else if(entity.getMediaType().equals(MediaType.APPLICATION_XML, true)){
        	  log.info("APPLICATION_XML MEDIA-TYPE "+entity.getMediaType());
             return receiveXmlFile(entity);
            }else if(entity.getMediaType().equals(MediaType.TEXT_XML, true)){
            	log.info("TEXT_XML MEDIA-TYPE "+entity.getMediaType());
            	return receiveXmlFile(entity);
            }else if(entity.getMediaType().equals(MediaType.APPLICATION_WWW_FORM, true)){
            	log.info("APPLICATION_WWW_FORM MEDIA-TYPE "+entity.getMediaType());
            	return receiveWWWFile(entity);
            }else{
              log.info("Unknown MEDIA-TYPE "+entity.getMediaType());
              logRepresentationDebugInfo(entity);
            }
        }
        }catch(Exception e){
            log.warning("receiveRepresentation had: ",e);
        }
        return new StringRepresentation("Error: Form type must be multipart/form-data or file");
    }

    /**
     * Just used for debugging.
     * @param entity Representation
     */
    private void logRepresentationDebugInfo(Representation entity){

        StringBuilder sb = new StringBuilder();
        try{
            sb.append("media-type=").append(entity.getMediaType()).append("\n");
            sb.append("size=").append(entity.getSize()).append("\n");
            sb.append("text= _ ").append(URLDecoder.decode(entity.getText(),"UTF-8")).append(" _ \n");
        }catch (IOException ioe){
            sb.append(ioe.getMessage()).append("\n");
        }finally {
            log.info( sb.toString() );
        }
    }

 
	@SuppressWarnings("unused")
	private Representation receiveWWWFile(Representation entity) {
		log.info("receiveWWWFile " );
		Representation returnRep = null;
		//File tempFile;
		if (entity instanceof InputRepresentation) {
			log.info("receiveWWWFile entity is instanceof InputRepresentation ");
			
			@SuppressWarnings("unchecked")
			Series<Parameter> params = (Series<Parameter>) this.getRequest().getAttributes().get("org.restlet.http.headers");
			log.info("receiveWWWFile params " + params);
			String header = params.getFirstValue(HeaderConstants.HEADER_CONTENT_DISPOSITION);
		    log.info("receiveWWWFile header " + header);
		    
		    Disposition disposition = null;
			if (disposition != null) {
				if (params != null) {
					log.info("receiveWWWFile header "
							+ params.getFirstValue(HeaderConstants.HEADER_CONTENT_DISPOSITION));
					log.info("receiveWWWFile namespace "
							+ params.getFirstValue("namespace"));
					log.info("receiveWWWFile xquery "
							+ params.getFirstValue("xquery"));
					log.info("receiveWWWFile filename "
							+ params.getFirstValue("filename"));
					log.info("receiveWWWFile params " + params.toString());
					log.info("receiveWWWFile disp.getFilename "
							+ entity.getDisposition().getFilename());
				}
			}
		} else {
			log.info("receiveWWWFile entity is not InputRepresentation");
		}
         return returnRep;
    }
    
    private Representation receiveXmlFile(Representation entity) {
       Representation returnRep;
       File tempFile;
       boolean doOverwrite = true;
       
       @SuppressWarnings("unchecked")
       Series<Parameter> params = (Series<Parameter>) this.getRequest().getAttributes().get("org.restlet.http.headers");
       String header = params.getFirstValue(HeaderConstants.HEADER_CONTENT_DISPOSITION);
       
       try {
          if(header == null) {
             throw new Exception("No Content-Disposition found in header.");
          } else {
 
        	Disposition disp = new DispositionReader(header).readValue();
        	disp.getFilename();
          	String containerName = disp.getParameters().getFirstValue("collection");
     	    log.info("receiveXMLFile header is not null - got a file " + disp.getFilename() + " and a container " + containerName);
     	    String overwrite = disp.getParameters().getFirstValue("doOverwrite");
     	    log.debug("receiveXMLFile overwrite param value " + overwrite);
     	    if (overwrite != null) {
     	    	if (overwrite.equals("true")) {
     	    		log.debug("receiveXMLFile overwrite equals true, set doOverwrite true ");
     	    		doOverwrite = true;
     	    	}
     	    }

          	if (entity instanceof InputRepresentation) { 
         	   log.info("write this file to " + dbVars.get("dbpath") + " " + disp.getFilename());
         	   tempFile = new File(dbVars.get("dbpath"), disp.getFilename());
         	   FileOutputStream fos = new FileOutputStream(tempFile);
         	   entity.write(fos);
         	   fos.close();
            } else {
            	// so just use the fact that we wrote the file to this location before the post
            	log.info("use the file at " + "../resources/annotations" + " " + disp.getFilename());  
              	tempFile = new File("../resources/annotations", disp.getFilename());	
            }
          	
          	List<File> uploaded = new ArrayList<File>();
          	uploaded.add(tempFile);

       
          	returnRep = addFiles(uploaded, containerName, doOverwrite);
          }
       } catch(Exception e) {
    	   log.info("receiveXMLFile catch error " + e);
           returnRep = new StringRepresentation("File upload failed: " + e.getMessage());
       } 
       return returnRep;
    }

    private Representation receiveMultipartFormData() {
       File storeDir = new File(dbVars.get("dbpath"));
       DiskFileItemFactory itemFactory = new DiskFileItemFactory();
       itemFactory.setSizeThreshold(1024000);
       RestletFileUpload fileUpload = new RestletFileUpload(itemFactory);

       List<FileItem> files;
       try {
          files = fileUpload.parseRequest(getRequest());
       } catch (FileUploadException e) {
          log.warning("Xml file upload failed.",e);
          files = new ArrayList<FileItem>();
       }

       String containerName = null;
       List<File> uploaded = new ArrayList<File>();
       for (FileItem fi : files) {
          // Check to see if the FileItem is in fact the form field containing
          // the container we should use
          if (fi.isFormField() && fi.getFieldName().equals("collection")) {
             containerName = fi.getString();
          } else if (fi.getName() != null && fi.getName().length() > 0) {
             File file = new File(storeDir, fi.getName());
             boolean failed = false;
             try {
                fi.write(file);
             } catch (Exception e) {
                log.warning("Xml file upload write failed.",e);
                failed = true;
             }
             if (!failed)
                uploaded.add(file);
          }
       }
       return addFiles(uploaded, containerName, false);
    }


    private Representation addFiles(List<File> uploaded, String containerName, boolean doOverwrite) {
       Representation returnRep;

       log.info("addFiles containerName="+containerName+" has "+uploaded.size()+" files." + " and we are overwriting " + doOverwrite);
       
       XmlManager manager = null;
       XmlContainer container = null;
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
        	 
        	  // use the modified file name as the document name because we know this string
        	  // contains the unique id for the annotation
             String documentName = f.getName().replace("temp-", "").replace(".xml", "");
             
             if (doOverwrite) {	 
            	 try {
                     XmlDocument currentDocument = container.getDocument(documentName);
                     container.deleteDocument(currentDocument);
                  } catch (DatabaseException e) {
                      log.debug("XML Database exception during file upload. " + e.getMessage());
                  } 
             } 
             
             container.putDocument(documentName, manager.createLocalFileInputStream(f.getPath()), docConfig);
             returnString += documentName + "\n";
          }
          returnRep = new StringRepresentation(returnString);
       } catch (DatabaseException e) {
          log.warning("XML Database exception during file upload.",e);
          returnRep = new StringRepresentation("Internal database error: " + e.getMessage());
       } catch (Exception e) {
           log.warning("Exception during XML File upload.",e);
           returnRep = new StringRepresentation("Exception during upload: " + e.getMessage());
       } finally {
            //Delete local copies of files
            for(File f : uploaded) {
                try{
                    boolean success = f.delete();
                    if(!success){log.info("Failed to delete file: "+f.getAbsolutePath());}
                }catch (Exception e){ log.warning("Failed to close uploaded file.", e); }
            }
            XmlDbUtil.closeXmlContainer(container);
            XmlDbUtil.closeXmlManager(manager);
            cleanup();
       }

       logReturnRep(returnRep); 
       return returnRep;
    }

    /**
     * For debugging.
     * @param returnRep Representation
     */
    private void logReturnRep(Representation returnRep){
        try{
            log.info(returnRep.getText());
        }catch (Exception e){
            log.warning("logReturnRep had",e);
        }
    }

}
