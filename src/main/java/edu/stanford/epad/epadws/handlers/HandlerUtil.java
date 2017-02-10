/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
package edu.stanford.epad.epadws.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import com.sun.jersey.api.uri.UriTemplate;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADMessage;
import xmlwise.Plist;

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
		{
			message = new EPADMessage(message, Level.INFO).toJSON();
			log.info("Message to client:" + message);
			responseStream.append(message);
		}
		return responseCode;
	}

	public static int infoJSONResponse(int responseCode, String message, EPADLogger log)
	{
		return infoJSONResponse(responseCode, message, null, log);
	}

	public static int warningResponse(int responseCode, String message, PrintWriter responseStream, EPADLogger log)
	{
		if (responseStream != null)
		{
			if (!message.startsWith("{"))
				message = new EPADMessage(message).toJSON();
			log.warning("Message to client:" + message);
			responseStream.append(message);
		}
		else
			log.warning(message);
		return responseCode;
	}

	public static int warningResponse(int responseCode, String message, EPADLogger log)
	{
		log.warning(message);
		return responseCode;
	}

	public static int warningJSONResponse(int responseCode, String message, PrintWriter responseStream, EPADLogger log)
	{
		if (responseStream != null)
		{
			message = new EPADMessage(message).toJSON();
			log.warning("Message to client:" + message);
			responseStream.append(message);
		}
		else
		log.warning(message);
		return responseCode;
	}

	public static int warningResponse(int responseCode, String message, Throwable t, PrintWriter responseStream,
			EPADLogger log)
	{
		String finalMessage = message + (t == null ? "" : ((t.getMessage() == null) ? "" : ": " + t.getMessage()));
		if (responseStream != null)
		{
			finalMessage = new EPADMessage(finalMessage).toJSON();
			responseStream.append(finalMessage);
		}
		else
		log.warning(finalMessage);
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

    public static JSONObject getPostedJson(HttpServletRequest httpRequest) throws Exception
    {
    	StringBuffer jb = new StringBuffer();
		String line = null;
		try {
		    BufferedReader reader = new BufferedReader(new InputStreamReader( httpRequest.getInputStream()));
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		} catch (Exception e) {
			log.warning("Error receiving data:" + e);
			throw e;
		}
		log.debug("Posted Json:" + jb);
		try {
    	    return new JSONObject(jb.toString());
		} catch (Exception e) {
			log.warning("Error parsing JSON request string:" + jb);
		}    	
		return null;
    }
    
    public static JSONObject getPostedPListXML(HttpServletRequest httpRequest) throws Exception
    {
    	StringBuffer jb = new StringBuffer();
    	StringBuffer jb2 = new StringBuffer();
		String line = null;
		try {
		    BufferedReader reader = new BufferedReader(new InputStreamReader( httpRequest.getInputStream()));
		    while ((line = reader.readLine()) != null){
		      jb.append(line);
		    }
		    return parsePListFile(jb.toString());
		   
		} catch (Exception e) {
			log.warning("Error receiving data:" + e);
			throw e;
		}
  	
    }
    
    public static JSONObject parsePListFile(String fileContent){
    	try {
    		String json="";
    		Map<String, Object> properties = Plist.fromXml(fileContent); // loads the (nested) properties.
    		log.info("map contents");
    		for (Map.Entry<String, Object> entry : properties.entrySet())
    		{
    			log.info(entry.getKey() + "/" + entry.getValue());

    			json=entry.getValue().toString().replaceAll("=", ":").replaceAll("[\\._a-zA-Z0-9-\\(\\) ]+", "\\\"$0\\\"").replaceAll("\\\"\\(", "\\[\\\"").replaceAll("\\\" \\(", "\\[\\\"").replaceAll("\\)\\\"", "\\\"\\]").replaceAll("\\\" \\\"", "").replaceAll("\\\" ", "\\\"");
    			json="{\"Images\":" +json + "}" ; 
    			log.info("json is:"+json);

    		}
    		if (json!=null)
    			return new JSONObject(json);
    	} catch (Exception e) {
    		log.warning("Error parsing plist data", e);
    	}

    	return null;
    }

    public static String getPostedString(HttpServletRequest httpRequest) throws Exception
    {
    	StringBuffer jb = new StringBuffer();
		String line = null;
		try {
		    BufferedReader reader = new BufferedReader(new InputStreamReader( httpRequest.getInputStream()));
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		} catch (Exception e) {
			log.warning("Error receiving data:" + e);
			throw e;
		}
		log.debug("Posted string:" + jb);
		try {
    	    return jb.toString();
		} catch (Exception e) {
			log.warning("Error parsing request string:" + jb);
		}    	
		return null;
    }
    
	public static Map<String, Object> parsePostedData(String uploadDirPath, HttpServletRequest httpRequest, PrintWriter responseStream) throws Exception
	{
		File uploadDir = new File(uploadDirPath);
		if (!uploadDir.exists())
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
