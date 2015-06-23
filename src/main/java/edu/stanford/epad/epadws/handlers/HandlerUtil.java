//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;

import com.sun.jersey.api.uri.UriTemplate;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADMessage;

/**
 * Utility methods for handlers
 * 
 * 
 * @author martin
 */
public class HandlerUtil
{
	private static final EPADLogger log = EPADLogger.getInstance();

	public static int infoResponse(int responseCode, String message, PrintWriter responseStream, EPADLogger log)
	{
		log.info(message);
		if (responseStream != null)
			responseStream.append(message);
		return responseCode;
	}

	public static int infoResponse(int responseCode, String message, EPADLogger log)
	{
		return infoResponse(responseCode, message, null, log);
	}

	public static int infoJSONResponse(int responseCode, String message, PrintWriter responseStream, EPADLogger log)
	{
		log.info(message);
		if (responseStream != null)
			responseStream.append(new EPADMessage(message, Level.INFO).toJSON());
		return responseCode;
	}

	public static int infoJSONResponse(int responseCode, String message, EPADLogger log)
	{
		return infoJSONResponse(responseCode, message, null, log);
	}

	public static int warningResponse(int responseCode, String message, PrintWriter responseStream, EPADLogger log)
	{
		log.warning(message);
		if (responseStream != null)
		{
			if (!message.startsWith("{"))
				message = new EPADMessage(message).toJSON();
			responseStream.append(message);
		}
		return responseCode;
	}

	public static int warningResponse(int responseCode, String message, EPADLogger log)
	{
		log.warning(message);
		return responseCode;
	}

	public static int warningJSONResponse(int responseCode, String message, PrintWriter responseStream, EPADLogger log)
	{
		log.warning(message);
		if (responseStream != null)
			responseStream.append(new EPADMessage(message).toJSON());
		return responseCode;
	}

	public static int warningResponse(int responseCode, String message, Throwable t, PrintWriter responseStream,
			EPADLogger log)
	{
		String finalMessage = message + (t == null ? "" : ((t.getMessage() == null) ? "" : ": " + t.getMessage()));
		log.warning(finalMessage);
		if (responseStream != null)
			responseStream.append(new EPADMessage(finalMessage).toJSON());
		return responseCode;
	}

	public static int warningJSONResponse(int responseCode, String message, Throwable t, PrintWriter responseStream,
			EPADLogger log)
	{
		String finalMessage = message + (t == null ? "" : ((t.getMessage() == null) ? "" : ": " + t.getMessage()));
		log.warning(finalMessage, t);
		if (responseStream != null)
			responseStream.append(new EPADMessage(finalMessage).toJSON());
		return responseCode;
	}

	public static int warningJSONResponse(int responseCode, String message, Throwable t, EPADLogger log)
	{
		String finalMessage = message + (t == null ? "" : ((t.getMessage() == null) ? "" : ": " + t.getMessage()));
		log.warning(finalMessage, t);
		return responseCode;
	}

	public static int warningResponse(int responseCode, String message, Throwable t, EPADLogger log)
	{
		String finalMessage = message + (t == null ? "" : ((t.getMessage() == null) ? "" : ": " + t.getMessage()));
		log.warning(finalMessage, t);
		return responseCode;
	}

	public static int warningJSONResponse(int responseCode, String message, EPADLogger log)
	{
		log.warning(message);
		return responseCode;
	}

	public static int internalErrorResponse(String message, PrintWriter responseStream, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, responseStream, log);
	}

	public static int internalErrorResponse(String message, Throwable t, PrintWriter responseStream, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, t, responseStream, log);
	}

	public static int internalErrorResponse(String message, Throwable t, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, t, log);
	}

	public static int internalErrorResponse(String message, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, log);
	}

	public static int internalErrorJSONResponse(String message, Throwable t, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, t, log);
	}

	public static int internalErrorJSONResponse(String message, Throwable t, PrintWriter responseStream, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, t, responseStream, log);
	}

	public static int internalErrorJSONResponse(String message, PrintWriter responseStream, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, responseStream, log);
	}

	public static int invalidTokenJSONResponse(String message, PrintWriter responseStream, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_UNAUTHORIZED, message, responseStream, log);
	}

	public static int invalidTokenJSONResponse(String message, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_UNAUTHORIZED, message, log);
	}

	public static int invalidTokenResponse(String message, PrintWriter responseStream, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_UNAUTHORIZED, message, responseStream, log);
	}

	public static int badRequestResponse(String message, PrintWriter responseStream, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_BAD_REQUEST, message, responseStream, log);
	}

	public static int badRequestResponse(String message, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_BAD_REQUEST, message, log);
	}

	public static int invalidTokenResponse(String message, Throwable t, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_UNAUTHORIZED, message, t, log);
	}

	public static int invalidTokenResponse(String message, EPADLogger log)
	{
		return warningResponse(HttpServletResponse.SC_UNAUTHORIZED, message, log);
	}

	public static int badRequestJSONResponse(String message, PrintWriter responseStream, EPADLogger log)
	{
		return warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, message, responseStream, log);
	}

	public static String getTemplateParameter(Map<String, String> templateMap, String parameterName)
	{
		if (templateMap.containsKey(parameterName)) {
			return templateMap.get(parameterName);
		} else
			throw new IllegalArgumentException("no " + parameterName + " parameter in request");
	}

	public static String getTemplateParameter(Map<String, String> templateMap, String parameterName, String defaultValue)
	{
		if (templateMap.containsKey(parameterName)) {
			return templateMap.get(parameterName);
		} else
			return defaultValue;
	}

	public static Map<String, String> getTemplateMap(String template, String path)
	{
		Map<String, String> map = new HashMap<String, String>();

		UriTemplate uriTemplate = new UriTemplate(template);
		if (uriTemplate.match(path, map)) {
			return map;
		} else {
			return Collections.<String, String> emptyMap();
		}
	}

	public static boolean matchesTemplate(String template, String path)
	{
		Map<String, String> map = new HashMap<String, String>();

		UriTemplate uriTemplate = new UriTemplate(template);
		return uriTemplate.match(path, map);
	}

	public static int streamGetResponse(String url, OutputStream outputStream, EPADLogger log) throws IOException,
			HttpException
	{
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		int statusCode;

		try {
			statusCode = client.executeMethod(method);
			if (statusCode == HttpServletResponse.SC_OK) {
				InputStream is = method.getResponseBodyAsStream();
				int read = 0;
				byte[] bytes = new byte[4096];
				while ((read = is.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
			} else {
				log.warning("Unexpected response from " + url + ";statusCode=" + statusCode);
			}
		} finally {
			method.releaseConnection();
		}
		return statusCode;
	}

	public static List<File> extractFiles(FileItemIterator fileItemIterator, String prefix, String extension)
			throws FileUploadException, IOException, FileNotFoundException
	{
		List<File> files = new ArrayList<>();
		int sliceCount = 0;
		while (fileItemIterator.hasNext()) {
			FileItemStream fileItemStream = fileItemIterator.next();
			InputStream inputStream = fileItemStream.openStream();
			File temporaryFile = File.createTempFile(prefix + sliceCount + "_", extension);
			FileOutputStream fos = null;
			files.add(temporaryFile);
			try {
				int len;
				byte[] buffer = new byte[32768];
				fos = new FileOutputStream(temporaryFile);
				while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
					fos.write(buffer, 0, len);
				}
			} finally {
				IOUtils.closeQuietly(inputStream);
				IOUtils.closeQuietly(fos);
			}
		}
		return files;
	}
	
	public static File getUploadedFile(HttpServletRequest httpRequest)
	{
		String uploadDirPath = EPADConfig.getEPADWebServerFileUploadDir() + "temp" + Long.toString(System.currentTimeMillis());
		File uploadDir = new File(uploadDirPath);
		uploadDir.mkdirs();
		String fileName = httpRequest.getParameter("fileName");
		String tempXMLFileName = "temp" + System.currentTimeMillis() + "-annotation.xml";
		if (fileName != null)
			tempXMLFileName = "temp" + System.currentTimeMillis() + "-" + fileName;
		File uploadedFile = new File(uploadDir, tempXMLFileName);
		try
		{
			// opens input stream of the request for reading data
			InputStream inputStream = httpRequest.getInputStream();
			
			// opens an output stream for writing file
			FileOutputStream outputStream = new FileOutputStream(uploadedFile);
			
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			log.info("Receiving data...");
			int len = 0;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				len = len + bytesRead;
				outputStream.write(buffer, 0, bytesRead);
			}
			
			log.debug("Data received, len:" + len);
			outputStream.close();
			inputStream.close();
			if (len == 0)
			{
				try {
					uploadedFile.delete();
					uploadDir.delete();
				} catch (Exception x) {}
				uploadedFile = null;
			}
			else
				log.debug("Created File:" + uploadedFile.getAbsolutePath());
			if (len > 0 && (tempXMLFileName.endsWith(".xml") || tempXMLFileName.endsWith(".txt")))
			{
				log.debug("PUT Data:" + readFile(uploadedFile));
			}
//			if (fileType != null)
//			{
//				File changeFileExt = new File(uploadedFile.getParentFile(), tempXMLFileName.substring(0, tempXMLFileName.length()-3) + fileType);
//				uploadedFile.renameTo(changeFileExt);
//				uploadedFile = changeFileExt;
//			}
			return uploadedFile;
		}
		catch (Exception x)
		{
			log.warning("Error receiving Annotations file", x);
		}
		return null;
	}
	
    private static String readFile(File aimFile) throws Exception
    {
        BufferedReader in = new BufferedReader(new FileReader(aimFile));
        StringBuilder sb = new StringBuilder();
        String line;
        try
        {
            while ((line = in.readLine()) != null)
            {
            	sb.append(line + "\n");
            }
        }
        finally
        {
            in.close();
        }
        return sb.toString();
    }

	public static Map<String, Object> parsePostedData(HttpServletRequest httpRequest, PrintWriter responseStream) throws Exception
	{
		String uploadDirPath = EPADConfig.getEPADWebServerFileUploadDir() + "temp" + Long.toString(System.currentTimeMillis());
		File uploadDir = new File(uploadDirPath);
		uploadDir.mkdirs();
		
		Map<String, Object> params = new HashMap<String, Object>();
	    // Create a factory for disk-based file items
	    DiskFileItemFactory factory = new DiskFileItemFactory();
	    // Create a new file upload handler
	    ServletFileUpload upload = new ServletFileUpload(factory);
	    List<FileItem> items = upload.parseRequest(httpRequest);
		Iterator<FileItem> fileItemIterator = items.iterator();
		int fileCount = 0;
		while (fileItemIterator.hasNext()) {
			FileItem fileItem = fileItemIterator.next();
		    if (fileItem.isFormField()) {
		    	if (params.get(fileItem.getFieldName()) == null)
		    		params.put(fileItem.getFieldName(), fileItem.getString());
			    List values = (List) params.get(fileItem.getFieldName() + "_List");
			    if (values == null) {
			    	values = new ArrayList();
			    	params.put(fileItem.getFieldName() + "_List", values);
			    }
			    values.add(fileItem.getString());
		    } else {
				fileCount++;		    	
				String fieldName = fileItem.getFieldName();
				if (fieldName.trim().length() == 0)
					fieldName = "File" + fileCount;
				String fileName = fileItem.getName();
				log.debug("Uploading file number " + fileCount);
				log.debug("FieldName: " + fieldName);
				log.debug("File Name: " + fileName);
				log.debug("ContentType: " + fileItem.getContentType());
				log.debug("Size (Bytes): " + fileItem.getSize());
				if (fileItem.getSize() != 0)
				{
			        try {
						String tempFileName = "temp" + System.currentTimeMillis() + "-" + fileName;
						File file = new File(uploadDirPath + "/" + tempFileName);
						log.debug("FileName: " + file.getAbsolutePath());
		                // write the file
						fileItem.write(file);
				    	if (params.get(fileItem.getFieldName()) == null)
				    		params.put(fileItem.getFieldName(), file);
				    	else
				    		params.put(fileItem.getFieldName()+fileCount, file);
					    List values = (List) params.get(fileItem.getFieldName() + "_List");
					    if (values == null) {
					    	values = new ArrayList();
					    	params.put(fileItem.getFieldName() + "_List", values);
					    }
					    values.add(file);
					} catch (Exception e) {
						e.printStackTrace();
						log.warning("Error receiving file:" + e);
						responseStream.print("error reading (" + fileCount + "): " + fileItem.getName());
						continue;
					}
				}
		    }
		}
		return params;
	}

}
