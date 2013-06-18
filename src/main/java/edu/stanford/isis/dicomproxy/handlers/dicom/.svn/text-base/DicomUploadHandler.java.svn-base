/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.handlers.dicom;

import edu.stanford.isis.dicomproxy.server.ProxyLogger;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
//TODO AAA Should this be deprecated
/**
 * Attempt at downloading zip files - not used (TBR).
 * @author amsnyder
 */
public class DicomUploadHandler extends AbstractHandler
{
    private static final ProxyLogger log = ProxyLogger.getInstance();
    /**
     * slf4j logger.
     */
    protected Logger debugLogger = null;
    public DicomUploadHandler() {
    	debugLogger = LoggerFactory.getLogger(this.getClass());
    }
    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
        debugLogger.warn("Calling DicomUploadHandler");
    	httpResponse.setContentType("text/plain");
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.setHeader("Access-Control-Allow-Origin","*");
        request.setHandled(true);

        PrintWriter out = httpResponse.getWriter();

        String method = httpServletRequest.getMethod();
        if("GET".equalsIgnoreCase(method)){
            String msg = "DicomUploadHandler received GET method.";
            log.debug(msg);
            out.print(msg);
            //return;
        }

        String tempDir = "temp-"+System.currentTimeMillis()+"/";
        log.info("Uploading files to dir: "+tempDir);
        log.debug("method: "+method+",  why not POST?!");
        //create the directory for uploading file.
        try{
            File tempUploadDir = new File("../resources/upload/"+tempDir);
            tempUploadDir.mkdirs();
            ServletFileUpload upload = new ServletFileUpload();
            
            FileItemIterator iter = upload.getItemIterator(httpServletRequest);
            int fileCount = 0;
            while(iter.hasNext()){
                fileCount++;
                log.debug("starting file #"+fileCount);
                FileItemStream item = iter.next();

                String name = item.getFieldName();
                log.debug("FieldName = "+name);
                InputStream stream = item.openStream();

                //ByteArrayOutputStream out = new ByteArrayOutputStream();
                FileOutputStream fos = new FileOutputStream( new File(tempUploadDir+name));
                int len;
                byte[] buffer = new byte[32768];
                while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
                     fos.write(buffer,0,len);
                }//while
                out.print("added ("+fileCount+"): "+name);
            }//while

            out.flush();

        }catch(Exception e){
            log.warning("Failed to upload DICOM files to _"+tempDir+"_",e);
        }catch(Error temp){
            log.warning("Error. Could jar file be missing from start script?",temp);
        }finally{
            log.info("leaving DicomUploadHandler handle.");
            if(out!=null){
                out.close();
            }
        }
    }
}
