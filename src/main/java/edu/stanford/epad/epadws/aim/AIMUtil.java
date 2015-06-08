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
package edu.stanford.epad.epadws.aim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.SequenceAttribute;
import com.pixelmed.dicom.SequenceItem;
import com.pixelmed.dicom.TagFromName;

import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.plugins.PluginAIMUtil;
import edu.stanford.epad.common.plugins.PluginConfig;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.MongoDBOperations;
import edu.stanford.epad.common.util.XmlNamespaceTranslator;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADAIMList.EPADAIMResultSet;
import edu.stanford.epad.dtos.internal.DICOMElement;
import edu.stanford.epad.dtos.internal.DICOMElementList;
import edu.stanford.epad.epadws.aim.aimapi.Aim;
import edu.stanford.epad.epadws.aim.aimapi.Aim4;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.event.EventHandler;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.processing.pipeline.task.PluginStartTask;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.SessionService;
import edu.stanford.epad.epadws.service.UserProjectService;
import edu.stanford.hakan.aim3api.base.AimException;
import edu.stanford.hakan.aim3api.base.DICOMImageReference;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;
import edu.stanford.hakan.aim3api.base.Person;
import edu.stanford.hakan.aim3api.base.Segmentation;
import edu.stanford.hakan.aim3api.base.SegmentationCollection;
import edu.stanford.hakan.aim3api.base.User;
import edu.stanford.hakan.aim3api.usage.AnnotationBuilder;
import edu.stanford.hakan.aim3api.usage.AnnotationGetter;
import edu.stanford.hakan.aim4api.base.ImageAnnotationCollection;

/**
 * 
 * 
 * 
 * @author martin
 */
public class AIMUtil
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String aim3Namespace = EPADConfig.aim3Namespace;
	private static final String eXistUsername = EPADConfig.eXistUsername;
	private static final String eXistPassword = EPADConfig.eXistPassword;
	private static final String eXistServerUrl = EPADConfig.eXistServerUrl;
	private static final String eXistCollection = EPADConfig.eXistCollection;
	private static final String baseAnnotationDir = EPADConfig.baseAnnotationDir;
	private static final String xsdFilePath = EPADConfig.xsdFilePath;
	private static final String useV4 = EPADConfig.useV4;
	private static final String aim4Namespace = EPADConfig.aim4Namespace;
	private static final String eXistCollectionV4 = EPADConfig.eXistCollectionV4;
	private static final String xsdFileV4 = EPADConfig.xsdFileV4;
	private static final String xsdFilePathV4 = EPADConfig.xsdFilePathV4;

	/**
	 * Save the annotation to the server in the AIM database. An invalid annotation will not be saved. Save a file backup
	 * just in case.
	 * 
	 * @param ImageAnnotation
	 * @return String
	 * @throws AimException
	 * @throws edu.stanford.hakan.aim4api.base.AimException
	 */		
	public static String saveImageAnnotationToServer(ImageAnnotation aim, String projectID, int frameNumber, String jsessionID) throws AimException,
	edu.stanford.hakan.aim4api.base.AimException
	{                        
		String result = "";

		if (aim.getCodeValue() != null) { 
			if (isPluginStillRunning(aim.getUniqueIdentifier()))
				throw new AimException("Previous version of this AIM " + aim.getUniqueIdentifier() + " is still being processed by the plugin");
			// For safety, write a backup file
			String tempXmlPath = baseAnnotationDir + "temp-" + aim.getUniqueIdentifier() + ".xml";
			String storeXmlPath = baseAnnotationDir + aim.getUniqueIdentifier() + ".xml";
			File tempFile = new File(tempXmlPath);
			File storeFile = new File(storeXmlPath);
			AnnotationBuilder.saveToFile(aim, tempXmlPath, xsdFilePath);
			log.info("Saving AIM file with ID " + aim.getUniqueIdentifier());
			result = AnnotationBuilder.getAimXMLsaveResult();

			log.info("Save AIM to file:" + result);
			if (storeFile.exists()) {
				storeFile.delete();
			}
			tempFile.renameTo(storeFile);

			if (useV4.equals("false")) {
				AnnotationBuilder.saveToServer(aim, eXistServerUrl, aim3Namespace, eXistCollection, xsdFilePath, eXistUsername,
						eXistPassword);
				result = AnnotationBuilder.getAimXMLsaveResult();
			} else {
			    String collectionName = eXistCollectionV4;
			    if (projectID != null && projectID.length() > 0)
			    	collectionName = collectionName + "/" + projectID;
			    ImageAnnotationCollection aim4 = aim.toAimV4();
				edu.stanford.hakan.aim4api.usage.AnnotationBuilder.saveToServer(aim4, eXistServerUrl, aim4Namespace,
						collectionName, xsdFilePathV4, eXistUsername, eXistPassword);
				result = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.getAimXMLsaveResult();
				try {
					if (projectID != null && projectID.length() > 0)
						saveAimToMongo(aim4, projectID);
				} catch (Exception e) {
					log.warning("Error saving aim to mongodb", e);
				}
			}

			log.info("Save AIM to Exist:" + result);
			log.info("CodingSchemeDesignator:" + aim.getCodingSchemeDesignator());
			
			if (aim.getCodingSchemeDesignator().equals("epad-plugin")) { // Which template has been used to fill the AIM file
				String templateName = aim.getCodeValue(); // ex: jjv-5
				log.info("Found an AIM plugin template with name " + templateName + " and AIM ID " + aim.getUniqueIdentifier());
				boolean templateHasBeenFound = false;
				String handlerName = null;
				String pluginName = null;

				List<String> list = PluginConfig.getInstance().getPluginTemplateList();
				for (int i = 0; i < list.size(); i++) {
					String templateNameFounded = list.get(i);
					if (templateNameFounded.equals(templateName)) {
						handlerName = PluginConfig.getInstance().getPluginHandlerList().get(i);
						pluginName = PluginConfig.getInstance().getPluginNameList().get(i);
						templateHasBeenFound = true;
					}
				}

				if (templateHasBeenFound) {
					log.info("Starting Plugin task for:" + pluginName);
					(new Thread(new PluginStartTask(jsessionID, pluginName, aim.getUniqueIdentifier(), frameNumber, projectID))).start();				
				}
			}
		}
		return result;
	}
	
	private static long getTime(String timestamp)
	{
		try
		{
			Date date = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").parse(timestamp);
			return date.getTime();
		}
		catch (Exception x)
		{
			return 0;
		}
	}

    /**
     * @param ImageAnnotationCollection aim - AIM 4 implementation
     * @param frameNumber
     * @param jsessionID
     * @return
     * @throws AimException
     * @throws edu.stanford.hakan.aim4api.base.AimException
     */
    public static String saveImageAnnotationToServer(ImageAnnotationCollection aim, String projectID, int frameNumber, String jsessionID) throws AimException,
    edu.stanford.hakan.aim4api.base.AimException {
    	return saveImageAnnotationToServer(aim, projectID, frameNumber, jsessionID, true);
    }
    
    public static String saveImageAnnotationToServer(ImageAnnotationCollection aim, String projectID, int frameNumber, String jsessionID, boolean invokePlugin) throws AimException,
	    edu.stanford.hakan.aim4api.base.AimException {
		String result = "";
		
		    log.info("=+=+=+=+=+=+=+=+=+=+=+=+= saveImageAnnotationToServer-1");
		if (aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCode() != null) { 
			
			if (isPluginStillRunning(aim.getUniqueIdentifier().getRoot()))
				throw new AimException("Previous version of this AIM " + aim.getUniqueIdentifier() + " is still being processed by the plugin");
			
			// For safety, write a backup file - what is this strange safety feature??
		    String tempXmlPath = baseAnnotationDir + "temp-" + aim.getUniqueIdentifier().getRoot() + ".xml";
		    String storeXmlPath = baseAnnotationDir + aim.getUniqueIdentifier().getRoot() + ".xml";
		    File tempFile = new File(tempXmlPath);
		    File storeFile = new File(storeXmlPath);
		    
		    edu.stanford.hakan.aim4api.usage.AnnotationBuilder.saveToFile(aim, tempXmlPath, xsdFilePathV4);
		    log.info("Saving AIM file with ID " + aim.getUniqueIdentifier());
		    result = AnnotationBuilder.getAimXMLsaveResult();
		
		    log.info(result);
		    if (storeFile.exists()) {
		        storeFile.delete();
		    }
		    tempFile.renameTo(storeFile);
		
		    String collectionName = eXistCollectionV4;
		    if (projectID != null && projectID.length() > 0)
		    	collectionName = collectionName + "/" + projectID;
		    edu.stanford.hakan.aim4api.usage.AnnotationBuilder.saveToServer(aim, eXistServerUrl, aim4Namespace,
		    		collectionName, xsdFilePathV4, eXistUsername, eXistPassword);
		    result = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.getAimXMLsaveResult();
		
		    log.info(result);
			try {
				if (projectID != null && projectID.length() > 0)
					saveAimToMongo(aim, projectID);
			} catch (Exception e) {
				log.warning("Error saving aim to mongodb", e);
			}
		
		    if (aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystemName().equals("epad-plugin")) { // Which template has been used to fill the AIM file
		        String templateName = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCode(); // ex: jjv-5
		        log.info("Found an AIM plugin template with name " + templateName + " and AIM ID " + aim.getUniqueIdentifier());
		        boolean templateHasBeenFound = false;
		        String handlerName = null;
		        String pluginName = null;
		
		        List<String> list = PluginConfig.getInstance().getPluginTemplateList();
		        for (int i = 0; i < list.size(); i++) {
		            String templateNameFounded = list.get(i);
		            if (templateNameFounded.equals(templateName)) {
		                handlerName = PluginConfig.getInstance().getPluginHandlerList().get(i);
		                pluginName = PluginConfig.getInstance().getPluginNameList().get(i);
		                templateHasBeenFound = true;
		            }
		        }
		
		        if (templateHasBeenFound && jsessionID != null) {
		        	// Start plugin task
					log.info("Starting Plugin task for:" + pluginName);
					(new Thread(new PluginStartTask(jsessionID, pluginName, aim.getUniqueIdentifier().getRoot(), frameNumber, projectID))).start();				
		        }
		    }
		}
		return result;
	}	
	
	public static boolean deleteAIM(String aimID, String projectID)
	{
		try {
			if (useV4.equals("false"))
				AnnotationGetter.removeImageAnnotationFromServer(eXistServerUrl, aim3Namespace, eXistCollection, eXistUsername,
						eXistPassword, aimID, false);
			else
			{
			    String collectionName = eXistCollectionV4;
			    if (projectID != null && projectID.length() > 0)
			    	collectionName = collectionName + "/" + projectID;
				edu.stanford.hakan.aim4api.database.exist.ExistManager.removeImageAnnotationCollectionFromServer(
						eXistServerUrl, aim4Namespace, collectionName, eXistUsername, eXistPassword, aimID);
				MongoDBOperations.deleteAnotationInMongo(aimID, projectID);
			}

			return true;
		} catch (Exception ex) {
			log.warning("Error deleting AIM annotation " + aimID, ex);
			return false;
		}
	}

	public static boolean isPluginStillRunning(String aimID)
	{
		try {
			EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			List<Map<String, String>> eventMaps = epadDatabaseOperations.getEpadEventsForAimID(aimID);
			if (eventMaps.size() == 0)
			{
				eventMaps = new ArrayList<Map<String, String>>();
				Map<String, String> eventMap = EventHandler.deletedEvents.get(aimID);
				if (eventMap != null)
					eventMaps.add(eventMap);
			}
			if (eventMaps.size() > 0)
			{
				log.info("last event:" + eventMaps.get(0));
				if ("Started".equals(eventMaps.get(0).get("event_status")) && getTime(eventMaps.get(0).get("created_time")) > (System.currentTimeMillis()-10*60*60*1000))
				{
					return true;
				}
			}
		}
		catch (Exception x) {
		}
		return false;
	}
	
	/**
	 * Generate an AIM file for a new DICOM Segmentation Object (DSO). This generation process is used when a new DSO is
	 * detected in dcm4chee. For the moment, we set the owner of the AIM annotation to admin.
	 * <p>
	 * This AIM file actually annotates the original image, NOT the DSO. The Referenced SOP Instance UID field in the
	 * DICOM DSO tag file identifies the image from which the segmentation object is derived from. It contains the imageID
	 * of the original image but does not contain the study or series identifiers for that image - so we need to discover
	 * them by querying ePAD.
	 */

	/**
	 * {@link PluginAIMUtil#generateAIMFileForDSO} is very similar.
	 * 
	 */
	public static ImageAnnotation generateAIMFileForDSO(File dsoFile) throws Exception
	{
		return generateAIMFileForDSO(dsoFile, "shared", null);
	}
	
	public static ImageAnnotation generateAIMFileForDSO(File dsoFile, String username, String projectID) throws Exception
	{
		log.info("Creating DSO AIM for user " + username + " in project " + projectID);
		AttributeList dsoDICOMAttributes = PixelMedUtils.readDICOMAttributeList(dsoFile);
		String patientID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.PatientID);
		String patientName = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.PatientName);
		String patientBirthDay = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.PatientBirthDate);
		if (patientBirthDay.trim().length() != 8) patientBirthDay = "19650212";
		String patientSex = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.PatientSex);
		if (patientSex.trim().length() != 1) patientSex = "F";
		String dsoDate = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SeriesDate);
		if (dsoDate.trim().length() != 8) dsoDate = "20001017";
		String sopClassUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SOPClassUID);
		String studyUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.StudyInstanceUID);
		log.info("DSO:" + dsoFile.getAbsolutePath() + " PatientID:" + patientID + " studyUID:" + studyUID + " projectID:" + projectID);
		String seriesUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SeriesInstanceUID);
		String imageUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SOPInstanceUID);
		String description = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SeriesDescription);
		// TODO: This call to get Referenced Image does not work ???
		String[] referencedImageUID = Attribute.getStringValues(dsoDICOMAttributes, TagFromName.ReferencedSOPInstanceUID);
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		SequenceAttribute referencedSeriesSequence =(SequenceAttribute)dsoDICOMAttributes.get(TagFromName.ReferencedSeriesSequence);
		String referencedSeriesUID = "";
		if (referencedSeriesSequence != null) {
		    Iterator sitems = referencedSeriesSequence.iterator();
		    if (sitems.hasNext()) {
		        SequenceItem sitem = (SequenceItem)sitems.next();
		        if (sitem != null) {
		            AttributeList list = sitem.getAttributeList();
		            SequenceAttribute referencedInstanceSeq = (SequenceAttribute) list.get(TagFromName.ReferencedInstanceSequence);
				    Iterator sitems2 = referencedInstanceSeq.iterator();
				    while (sitems2.hasNext())
				    {
					    sitem = (SequenceItem)sitems2.next();
			            list = sitem.getAttributeList();
			            if (list.get(TagFromName.ReferencedSOPInstanceUID) != null)
			            {		            	
			    			referencedImageUID = new String[1];
			    			referencedImageUID[0] = list.get(TagFromName.ReferencedSOPInstanceUID).getSingleStringValueOrEmptyString();
							referencedSeriesUID = dcm4CheeDatabaseOperations.getSeriesUIDForImage(referencedImageUID[0]);
							if (referencedSeriesUID != null && referencedSeriesUID.length() > 0)
							{
								log.info("ReferencedSOPInstanceUID:" + referencedImageUID[0]);
								break;
							}
							else
								log.info("DSO Referenced Image not found:" + referencedImageUID);
			            }
				    }
 		        }
		    }
		}
		if (referencedSeriesUID != null && referencedSeriesUID.length() != 0) { // Found corresponding series in dcm4chee
			String referencedStudyUID = studyUID; // Will be same study as DSO
			patientName = trimTrailing(patientName);
			log.info("Generating AIM file for DSO series " + seriesUID + " for patient " + patientName);
			log.info("SOP Class UID=" + sopClassUID);
			log.info("DSO Study UID=" + studyUID);
			log.info("DSO Series UID=" + seriesUID);
			log.info("DSO Image UID=" + imageUID);
			log.info("Referenced SOP Instance UID=" + referencedImageUID[0]);
			log.info("Referenced Series Instance UID=" + referencedSeriesUID);

			String name = description;
			if (name == null || name.trim().length() == 0) name = "segmentation";
			ImageAnnotation imageAnnotation = new ImageAnnotation(0, "", dsoDate.substring(0,4) + "-" + dsoDate.substring(4,6) + "-" + dsoDate.substring(6,8) + "T00:00:00", name, "SEG",
					"SEG Only", "", "", "");

			SegmentationCollection sc = new SegmentationCollection();
			sc.AddSegmentation(new Segmentation(0, imageUID, sopClassUID, referencedImageUID[0], 1));
			imageAnnotation.setSegmentationCollection(sc);

			DICOMImageReference originalDICOMImageReference = PluginAIMUtil.createDICOMImageReference(referencedStudyUID,
					referencedSeriesUID, referencedImageUID[0]);
			imageAnnotation.addImageReference(originalDICOMImageReference);
			DICOMImageReference dsoDICOMImageReference = PluginAIMUtil.createDICOMImageReference(studyUID, seriesUID,
					imageUID);
			imageAnnotation.addImageReference(dsoDICOMImageReference);

			Person person = new Person();
			person.setSex(patientSex.trim());
			if (patientBirthDay.trim().length() == 8)
				person.setBirthDate(patientBirthDay.substring(0,4) + "-" + patientBirthDay.substring(4,6) + "-" + patientBirthDay.substring(6,8) + "T00:00:00"); // TODO
			person.setId(patientID);
			person.setName(patientName);
			person.setCagridId(0);
			imageAnnotation.addPerson(person);
			// TODO Not general. See if we can generate AIM on GUI upload of DSO with correct user.
			setImageAnnotationUser(imageAnnotation, username);

			log.info("Saving AIM file for DSO " + imageUID + " in series " + seriesUID + " with ID "
					+ imageAnnotation.getUniqueIdentifier());
			try {
				boolean missingproject = false;
				if (projectID == null || projectID.trim().length() == 0)
				{
					missingproject = true;
					projectID = EPADConfig.xnatUploadProjectID;
				}
				String result = saveImageAnnotationToServer(imageAnnotation, projectID);
				if (result.toLowerCase().contains("success"))
				{
					ImageReference imageReference = new ImageReference(projectID, patientID, referencedStudyUID, referencedSeriesUID, referencedImageUID[0]);
					EpadDatabaseOperations dbOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
					EPADAIM aim = dbOperations.getAIM(imageAnnotation.getUniqueIdentifier());
					if (aim != null)
					{
						if (!username.equals("shared"))
							dbOperations.updateAIM(aim.aimID, projectID, username);
					}
					else
					{
						dbOperations.addDSOAIM(username, imageReference, seriesUID, imageAnnotation.getUniqueIdentifier(),
								edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(imageAnnotation.toAimV4()));
					}
					if (missingproject)
					{
						EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
						Project proj = projectOperations.getFirstProjectForStudy(studyUID);
						if (proj != null && !proj.getProjectId().equals(EPADConfig.xnatUploadProjectID))
						{
							generateAIMFileForDSO(dsoFile, "shared", proj.getProjectId());
						}
					}
				}
				return imageAnnotation;
			} catch (AimException e) {
				log.warning("Exception saving AIM file for DSO image " + imageUID + " in series " + seriesUID, e);
			}
		} else {
			log.warning("DSO " + imageUID + " in series " + seriesUID + " with no corresponding DICOM image");
		}
		return null;
		/*
		 * ServerEventUtil.postEvent(username, "DSOReady", imageAnnotation.getUniqueIdentifier(), aimName, patientID,
		 * patientName, "", "", "");
		 */
	}
	
	private static String trimTrailing(String xnatName)
	{
		while (xnatName.endsWith("^"))
			xnatName = xnatName.substring(0, xnatName.length()-1);
		String name = xnatName.trim();
		return name;
	}

	public static boolean uploadAIMAnnotations(HttpServletRequest httpRequest, PrintWriter responseStream,
			String annotationsUploadDirPath) throws FileUploadException, IOException, FileNotFoundException, AimException,
			edu.stanford.hakan.aim4api.base.AimException
	{ // See http://www.tutorialspoint.com/servlets/servlets-file-uploading.htm
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> items = upload.parseRequest(httpRequest);
        // Process the uploaded items
        Iterator<FileItem> fileItemIterator = items.iterator();
		String username = httpRequest.getParameter("username");
		int fileCount = 0;
		boolean saveError = false;
		String projectID = httpRequest.getParameter("projectID");
		String patientID = httpRequest.getParameter("patientID");
		String studyID = httpRequest.getParameter("studyUID");
		String seriesID = httpRequest.getParameter("seriesUID");
		String imageID = httpRequest.getParameter("imageUID");
		String frameNo = "0";
		while (fileItemIterator.hasNext()) {
			FileItem fileItem = fileItemIterator.next();
		    if (fileItem.isFormField()) {
		    	if (fileItem.getFieldName().equals("projectID"))
		    	{
		    		projectID = fileItem.getString();
		    	}
		    	else if (fileItem.getFieldName().equals("patientID"))
		    	{
		    		patientID = fileItem.getString();
		    	}
		    	else if (fileItem.getFieldName().equals("studyID"))
		    	{
		    		studyID = fileItem.getString();
		    	}
		    	else if (fileItem.getFieldName().equals("seriesID"))
		    	{
		    		seriesID = fileItem.getString();
		    	}
		    	else if (fileItem.getFieldName().equals("imageID"))
		    	{
		    		imageID = fileItem.getString();
		    	}
		    	else if (fileItem.getFieldName().equals("frameNo"))
		    	{
		    		frameNo = fileItem.getString();
		    	}
		    	else if (fileItem.getFieldName().equals("username"))
		    	{
		    		username = fileItem.getString();
		    	}
		    } else {
				fileCount++;		    	
				log.debug("Uploading annotation number " + fileCount);
				//FileItemStream fileItemStream = fileItemIterator.next();
				String name = fileItem.getFieldName();
				//InputStream inputStream = fileItemStream.openStream();
				String tempXMLFileName = "temp-" + System.currentTimeMillis() + ".xml";
				File aimFile = new File(annotationsUploadDirPath + tempXMLFileName);
                // write the file
		        try {
					fileItem.write(aimFile);
				} catch (Exception e) {
					e.printStackTrace();
					log.warning("Error receiving AIM file:" + e);
					responseStream.print("error reading (" + fileCount + "): " + name);
					continue;
				}
				responseStream.print("added (" + fileCount + "): " + name);
				if (useV4.equals("false"))
				{
					ImageAnnotation imageAnnotation = AIMUtil.getImageAnnotationFromFile(aimFile, xsdFilePath);
					if (imageAnnotation != null) {
						if (patientID == null) patientID = imageAnnotation.getListPerson().get(0).getId();
						if (studyID == null)
						{
							Aim aim = new Aim(imageAnnotation);
							imageID = aim.getFirstImageID();
							seriesID = aim.getSeriesID(imageID);
							studyID = aim.getStudyID(seriesID);
						}
						String jsessionID = SessionService.getJSessionIDFromRequest(httpRequest);
						log.info("Saving AIM file with ID " + imageAnnotation.getUniqueIdentifier() + " username:" + username);
						String result = AIMUtil.saveImageAnnotationToServer(imageAnnotation, projectID, 0, jsessionID);
						if (result.toLowerCase().contains("success") && projectID != null && username != null)
						{
							EpadOperations epadOperations = DefaultEpadOperations.getInstance();
							FrameReference frameReference = new FrameReference(projectID, patientID, studyID, seriesID, imageID, getInt(frameNo));
							epadOperations.createFrameAIM(username, frameReference, imageAnnotation.getUniqueIdentifier(), null, jsessionID);
						}
						responseStream.println("-- Add to AIM server: " + imageAnnotation.getUniqueIdentifier() + "<br>");
					} else {
						responseStream.println("-- Failed ! not added to AIM server<br>");
						saveError = true;
					}
				}
				else
				{
		            log.info("=+=+=+=+=+=+=+=+=+=+=+=+= uploadAIMAnnotations-2");
		            log.info("=+=+=+=+=+=+=+=+=+=+=+=+= xsdFilePathV4 : " + xsdFilePathV4);
		            ImageAnnotationCollection imageAnnotation = AIMUtil.getImageAnnotationFromFileV4(aimFile, xsdFilePathV4);
		            log.info("=+=+=+=+=+=+=+=+=+=+=+=+= uploadAIMAnnotations-3");
		            if (imageAnnotation != null) {
		                String jsessionID = SessionService.getJSessionIDFromRequest(httpRequest);

		            log.info("=+=+=+=+=+=+=+=+=+=+=+=+= uploadAIMAnnotations-4");
		                AIMUtil.saveImageAnnotationToServer(imageAnnotation, projectID, getInt(frameNo), jsessionID);
		                responseStream.println("-- Add to AIM server: " + imageAnnotation.getUniqueIdentifier().getRoot() + "<br>");
		            } else {
		                responseStream.println("-- Failed ! not added to AIM server<br>");
		                saveError = true;
		            }
				}
				if (aimFile.exists()) aimFile.delete();				
				projectID = null;
				patientID = null;
				studyID = null;
				seriesID = null;
				imageID = null;
				frameNo = "0";
		    }
		}
		return saveError;
	}

	/**
	 * @param aimFile
	 * @param sessionId
	 * @param username
	 * @return true if error in AIM save
	 * @throws AimException
	 * @throws edu.stanford.hakan.aim4api.base.AimException
	 */
	public static boolean saveAIMAnnotation(File aimFile, String projectID, String sessionId, String username) throws AimException,
	edu.stanford.hakan.aim4api.base.AimException
	{
		return saveAIMAnnotation(aimFile, projectID, 0, sessionId, username);
	}
	
	public static boolean saveAIMAnnotation(File aimFile, String projectID, int frameNumber, String sessionId, String username) throws AimException,
	edu.stanford.hakan.aim4api.base.AimException
	{
		if (aimFile == null)
			return true;
		if (useV4.equals("false")) {
			ImageAnnotation imageAnnotation = AIMUtil.getImageAnnotationFromFile(aimFile, xsdFilePath);
			if (imageAnnotation != null) {
				String patientID = imageAnnotation.getListPerson().get(0).getId();
				Aim aim = new Aim(imageAnnotation);
				String imageID = aim.getFirstImageID();
				String seriesID = aim.getSeriesID(imageID);
				String studyID = aim.getStudyID(seriesID);
				log.info("Saving AIM file with ID " + imageAnnotation.getUniqueIdentifier() + " username:" + username);
				String result = AIMUtil.saveImageAnnotationToServer(imageAnnotation, projectID, frameNumber, sessionId);
				log.info("Save annotation:" + result);
				if (result.toLowerCase().contains("success") && projectID != null && username != null)
				{
					EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
					FrameReference frameReference = new FrameReference(projectID, patientID, studyID, seriesID, imageID, new Integer(frameNumber));
					epadDatabaseOperations.addAIM(username, frameReference, imageAnnotation.getUniqueIdentifier());
				}
				return false;
			}
		} else {
			try {
				log.info("Converting AIM file to Object " + aimFile.getAbsolutePath());
				EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
	            ImageAnnotationCollection imageAnnotationColl = AIMUtil.getImageAnnotationFromFileV4(aimFile, xsdFilePathV4);
	            if (imageAnnotationColl == null) {
	        		List<Map<String, String>> coordinationTerms = epadDatabaseOperations.getCoordinationData("%");
                    ImageAnnotationCollection iac = edu.stanford.hakan.aim4api.usage.AnnotationGetter.getImageAnnotationCollectionFromFile(aimFile.getAbsolutePath());
                    edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation iaV3 = new  edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation(iac);
                    imageAnnotationColl = iaV3.toAimV4(coordinationTerms);
	            }
	            if (imageAnnotationColl != null) {
					EPADAIM ea = epadDatabaseOperations.getAIM(imageAnnotationColl.getUniqueIdentifier().getRoot());
					if (ea != null && !ea.projectID.equals(projectID))
						projectID = ea.projectID; 		// TODO: Do we change AIM project if it is in unassigned? 
					Aim4 aim = new Aim4(imageAnnotationColl);
					String patientID = aim.getPatientID();
					String imageID = aim.getFirstImageID();
					String seriesID = aim.getSeriesID(imageID);
					String studyID = aim.getStudyID(seriesID);
					log.info("Saving AIM file with ID " + imageAnnotationColl.getUniqueIdentifier() + " projectID:" + projectID + " username:" + username);
					String result = AIMUtil.saveImageAnnotationToServer(imageAnnotationColl, projectID, frameNumber, sessionId);
					String xml = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(imageAnnotationColl);
					if (xml == null || xml.trim().length() < 100)
						throw new Exception("Error converting ImageAnnotationCollection to String");
					log.info("Save annotation:" + result);
					if (result.toLowerCase().contains("success") && projectID != null && username != null)
					{
						FrameReference frameReference = new FrameReference(projectID, patientID, studyID, seriesID, imageID, new Integer(frameNumber));
						epadDatabaseOperations.addAIM(username, frameReference, imageAnnotationColl.getUniqueIdentifier().getRoot(), xml);
					}
					return false;
	            } 
            } catch (Exception e) {
				e.printStackTrace();
				log.warning("Error saving annotation", e);
            }
			
		}
		return true;
	}

	public static AIMSearchType getAIMSearchType(HttpServletRequest httpRequest)
	{
		for (AIMSearchType aimSearchType : AIMSearchType.values()) {
			if (httpRequest.getParameter(aimSearchType.getName()) != null)
				return aimSearchType;
		}
		log.warning("No valid AIM search type parameter found");
		return null;
	}

	public static void queryAIMImageAnnotations(PrintWriter responseStream, AIMSearchType aimSearchType,
			Map<String,String> searchValueByProject, String user, int index, int count) throws ParserConfigurationException, AimException, edu.stanford.hakan.aim4api.base.AimException
	{
		if (useV4.equals("false")) {
			queryAIMImageAnnotations(responseStream, aimSearchType, searchValueByProject.values().iterator().next(), user, index, count);
		} else {
			queryAIMImageAnnotations(responseStream, aimSearchType, searchValueByProject, user);
		}
	}

	public static void queryAIMImageAnnotations(PrintWriter responseStream, AIMSearchType aimSearchType,
			Map<String,String> searchValueByProject, String user) throws ParserConfigurationException, AimException, edu.stanford.hakan.aim4api.base.AimException
	{
		if (useV4.equals("false")) {
			queryAIMImageAnnotations(responseStream, aimSearchType, searchValueByProject.values().iterator().next(), user, 1, 5000);
		} else {
			long starttime = System.currentTimeMillis();
			List<ImageAnnotationCollection> aims = new ArrayList<ImageAnnotationCollection>();
			for(String projectID: searchValueByProject.keySet())
			{
				log.info("ProjectID:" + projectID + " type:" + aimSearchType + " value:" + searchValueByProject.get(projectID));
				List<ImageAnnotationCollection> paims = AIMQueries.getAIMImageAnnotationsV4(projectID, aimSearchType, searchValueByProject.get(projectID), user);
				log.info("" + paims.size() + " AIM4 file(s) found for project:" + projectID + " for user " + user);
				aims.addAll(paims);
			}	
			returnImageAnnotationsXMLV4(responseStream, aims);
		}
	}

	public static void queryAIMImageAnnotations(PrintWriter responseStream, String projectID, AIMSearchType aimSearchType,
			String searchValue, String user) throws ParserConfigurationException, AimException, edu.stanford.hakan.aim4api.base.AimException
	{
		if (useV4.equals("false")) {
			queryAIMImageAnnotations(responseStream, aimSearchType, searchValue, user, 1, 5000);
		} else {
			queryAIMImageAnnotationsV4(responseStream, projectID, aimSearchType, searchValue, user);
		}
	}

	public static void queryAIMImageAnnotations(PrintWriter responseStream, String projectID, AIMSearchType aimSearchType,
			String searchValue, String user, int startIndex, int count) throws ParserConfigurationException, AimException, edu.stanford.hakan.aim4api.base.AimException
	{
		if (useV4.equals("false")) {
			queryAIMImageAnnotations(responseStream, aimSearchType, searchValue, user, startIndex, count);
		} else {
			queryAIMImageAnnotationsV4(responseStream, projectID, aimSearchType, searchValue, user);
		}
	}

	public static void queryAIMImageAnnotations(PrintWriter responseStream, AIMSearchType aimSearchType,
			String searchValue, String user, int index, int count) throws ParserConfigurationException, AimException
		{
		
		List<ImageAnnotation> aims = AIMQueries.getAIMImageAnnotations(null, aimSearchType, searchValue, user, index, count);

		log.info("" + aims.size() + " AIM file(s) found for user " + user);

		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element root = doc.createElement("imageAnnotations");
		doc.appendChild(root);

		long starttime = System.currentTimeMillis();
		for (ImageAnnotation aim : aims) {
			//if (aim.getListUser().size() > 0)
			//	log.info("Aim name:" + aim.getName() + " Owner:" + aim.getListUser().get(0).getLoginName() + " Series:" + aim.getFirstSeriesID());
			//else
			//	log.info("Aim name:" + aim.getName() + " Owner:unknown, Series:" + aim.getFirstSeriesID());
			Node node = aim.getXMLNode(docBuilder.newDocument());
			Node copyNode = doc.importNode(node, true);
			Element res = (Element)copyNode; // Copy the node
			res.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			res.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			res.setAttribute("xsi:schemaLocation",
					"gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM AIM_v3_rv11_XML.xsd");
			Node n = renameNodeNS(res, "ImageAnnotation","gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM");
			root.appendChild(n); // Adding to the root
		}
		long xmlbldtime = System.currentTimeMillis();
		log.info("Time taken for convert annotations to xml:" + (xmlbldtime-starttime) + " msecs");
		String queryResults = XmlDocumentToString(doc, "gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM");
		long xmlstrtime = System.currentTimeMillis();
		log.info("Time taken for convert xml to string:" + (xmlstrtime-xmlbldtime) + " msecs");
		responseStream.print(queryResults);
		log.info("" + aims.size() + " annotations returned to client");
		long resptime = System.currentTimeMillis();
		log.info("Time taken for write http response:" + (resptime-xmlstrtime) + " msecs, length:" + queryResults.length());
	}

	public static EPADAIMList queryAIMImageAnnotationSummaries(EPADAIMList aims, String user, int index, int count, String sessionID) throws ParserConfigurationException, AimException
	{
		Map<String, String> projectAimIDs = getUIDCsvList(sessionID, aims, user);
		List<ImageAnnotation> annotations = new ArrayList<ImageAnnotation>();
		for (String projectId: projectAimIDs.keySet())
		{
			annotations.addAll(AIMQueries.getAIMImageAnnotations(projectId, AIMSearchType.ANNOTATION_UID, projectAimIDs.get(projectId), user, index, count));
		}
		log.info("" + annotations.size() + " AIM file(s) found for user " + user);

		Map<String, EPADAIM> aimMAP = new HashMap<String, EPADAIM>();
		EPADAIMResultSet rs = aims.ResultSet;
		for (EPADAIM aim: rs.Result)
		{
			aimMAP.put(aim.aimID, aim);
		}
		aims = new EPADAIMList();
		long starttime = System.currentTimeMillis();
		for (ImageAnnotation aim : annotations) {
			Aim a = new Aim(aim);
			EPADAIM ea = aimMAP.get(aim.getUniqueIdentifier());
			ea.name = aim.getName();
			ea.template = aim.getCodeMeaning();
			ea.templateType = aim.getCodeValue();
			ea.date = aim.getDateTime();
			ea.comment = a.getComment();
			if (a.getFirstStudyDate() != null)
				ea.studyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getFirstStudyDate());
			ea.patientName = a.getPatientName();
			ea.xml = null;
			aims.addAIM(ea);
		}
		long parsetime = System.currentTimeMillis();
		log.info("Time taken to parse annotations:" + (parsetime-starttime) + " msecs");
		log.info("" + annotations.size() + " annotations returned to client");
		return aims;
	}

	public static void queryAIMImageAnnotationsV4(PrintWriter responseStream, EPADAIMList aims, AIMSearchType aimSearchType,
			String searchValue, String user, String sessionID, boolean jsonFormat) throws Exception
	{
		Map<String, String> aimIdsMap = getUIDCsvList(sessionID, aims, user);
		Map<String, EPADAIM> aimsMap = new HashMap<String, EPADAIM>();
		for (EPADAIM aim: aims.ResultSet.Result)
		{
			aimsMap.put(aim.aimID, aim);
		}
		if (!aimSearchType.equals(AIMSearchType.JSON_QUERY)) {
			List<ImageAnnotationCollection> iacs = new ArrayList<ImageAnnotationCollection>();
			for (String projectID: aimIdsMap.keySet())
			{
				String aimIds = "," + aimIdsMap.get(projectID) + ",";
				List<ImageAnnotationCollection> paims = AIMQueries.getAIMImageAnnotationsV4(projectID, aimSearchType, searchValue, user);
				for (ImageAnnotationCollection aim: paims)
				{
					if (aimIds.contains("," + aim.getUniqueIdentifier().getRoot() + ","))
						iacs.add(aim);					
				}
			}
			log.info("" + iacs.size() + " AIM4 file(s) found for user " + user);
			if (iacs.size() == 0) return;
			if (!jsonFormat) {
				returnImageAnnotationsXMLV4(responseStream, iacs);
				return;
			} else {
				StringBuilder xml = new StringBuilder("<imageAnnotations>\n");
				for (ImageAnnotationCollection iac: iacs)	{
					EPADAIM aim = aimsMap.get(iac.getUniqueIdentifier().getRoot());
					if (aim != null && aim.xml != null)
						xml.append(aim.xml);
				}
				xml.append("</imageAnnotations>\n");
				responseStream.print(xml);
			}
		} else {
			StringBuilder jsonSB = new StringBuilder("{\"imageAnnotations\":");
			int count = 0;
			List<String> jsonIds = new ArrayList<String>();
			for (String projectID: aimIdsMap.keySet())
			{
				String aimIds = "," + aimIdsMap.get(projectID) + ",";
				List<String> jsons = AIMQueries.getJsonAnnotations(projectID, aimSearchType, searchValue, user);
				for (String json: jsons)
				{
					JsonObject root = (JsonObject)new JsonParser().parse(json);
					JsonObject imageCollection = root.getAsJsonObject("ImageAnnotationCollection");
					String aimID = imageCollection.getAsJsonObject("uniqueIdentifier").getAsJsonPrimitive("root").getAsString();
					if (aimIds.contains("," + aimID + ","))
					{
						if (count > 0)
							jsonSB.append(",");
						jsonSB.append(json);
						jsonIds.add(aimID);
						count++;						
					}
				}
			}
			jsonSB.append("}");
			log.info("" + count + " AIM4 file(s) found for user " + user);
			if (count == 0) return;
			if (jsonFormat) {
				responseStream.print(jsonSB);
				return;
			} else {
				StringBuilder xml = new StringBuilder("<imageAnnotations>\n");
				for (String id: jsonIds)	{
					EPADAIM aim = aimsMap.get(id);
					if (aim != null && aim.xml != null)
						xml.append(aim.xml);
				}
				xml.append("</imageAnnotations>\n");
				responseStream.print(xml);
			}
		}
	}

	public static void queryAIMImageAnnotationsV4(PrintWriter responseStream, String projectID, AIMSearchType aimSearchType,
			String searchValue, String user) throws ParserConfigurationException, edu.stanford.hakan.aim4api.base.AimException
	{
		List<ImageAnnotationCollection> aims = AIMQueries.getAIMImageAnnotationsV4(projectID, aimSearchType, searchValue, user);
		log.info("" + aims.size() + " AIM4 file(s) found for user " + user);
		if (aims.size() == 0) return;
		returnImageAnnotationsXMLV4(responseStream, aims);
	}

	public static void returnImageAnnotationsXMLV4(PrintWriter responseStream, List<ImageAnnotationCollection> aims) throws ParserConfigurationException, edu.stanford.hakan.aim4api.base.AimException
	{
		long starttime = System.currentTimeMillis();
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element root = doc.createElement("imageAnnotations");
		doc.appendChild(root);

		for (ImageAnnotationCollection aim : aims) {
			Node node = aim.getXMLNode(docBuilder.newDocument());
			Node copyNode = doc.importNode(node, true);
			Element res = (Element)copyNode; // Copy the node
			res.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			res.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			res.setAttribute("xsi:schemaLocation",
					"gme://caCORE.caCORE/4.4/edu.northwestern.radiology.AIM AIM_v4_rv44_XML.xsd");
			res.setAttribute("xmlns", "gme://caCORE.caCORE/4.4/edu.northwestern.radiology.AIM");
			Node n = renameNodeNS(res, "ImageAnnotationCollection","gme://caCORE.caCORE/4.4/edu.northwestern.radiology.AIM");
			root.appendChild(n); // Adding to the root
		}
		String queryResults = XmlDocumentToString(doc,"gme://caCORE.caCORE/4.4/edu.northwestern.radiology.AIM");		
		long xmltime = System.currentTimeMillis();
		log.info("Time taken create xml:" + (xmltime-starttime) + " msecs for " + aims.size() +" annotations");
		responseStream.print(queryResults);
		long resptime = System.currentTimeMillis();
		log.info("" + aims.size() + " annotations returned to client, time to write resp:" + (resptime-xmltime) + " msecs");
	}
	
	public static EPADAIMList queryAIMImageAnnotationSummariesV4(EPADAIMList aims, String user, String sessionID) throws ParserConfigurationException, AimException
	{
		long starttime = System.currentTimeMillis();
		EPADAIMList aimsFromExist = new EPADAIMList();
		EPADAIMList aimsFromDB = new EPADAIMList();
		for (EPADAIM ea: aims.ResultSet.Result)
		{
			if (ea.xml == null || ea.xml.equals(""))
			{
				aimsFromExist.addAIM(ea);
			}
			else
			{
				aimsFromDB.addAIM(ea);
			}
		}

		List<EPADAIM> aimsDB = new ArrayList<EPADAIM>();
		if (aimsFromDB.ResultSet.totalRecords > 0)
		{
			// Check permissions for aims from DB table
			Map<String, List<EPADAIM>> aimsMap = getPermittedAIMs(sessionID, aimsFromDB, user);
			long permtime = System.currentTimeMillis();
			for (List<EPADAIM> paims: aimsMap.values())
			{
				aimsDB.addAll(paims);
			}
			// Get other params
			for (int i = 0; i < aimsDB.size(); i++)
			{
				EPADAIM ea = aimsDB.get(i);
				try {
					List<ImageAnnotationCollection> iacs = edu.stanford.hakan.aim4api.usage.AnnotationGetter.getImageAnnotationCollectionsFromString(ea.xml, "");
					ImageAnnotationCollection aim = iacs.get(0);
					Aim4 a = new Aim4(aim);
					ea.name = aim.getImageAnnotations().get(0).getName().getValue();
					ea.template = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystem();// .getCode();
					ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
					ea.date = aim.getDateTime();
					ea.comment = a.getComment();
					if (a.getFirstStudyDate() != null)
						ea.studyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getFirstStudyDate());
					ea.patientName = a.getPatientName();
					ea.xml = null;
				} catch (edu.stanford.hakan.aim4api.base.AimException e) {
					log.warning("Invalid AIM xml in DB, aimID:" + ea.aimID, e);
					aimsDB.remove(i--);
					aimsFromExist.addAIM(ea);
				}
			}
		}
		long dbtime = System.currentTimeMillis();
		
		List<ImageAnnotationCollection> annotations = new ArrayList<ImageAnnotationCollection>();
		if (aimsFromExist.ResultSet.totalRecords > 0)
		{
			// Check permission for aims from Exist
			Map<String, String> projectAimIDs = getUIDCsvList(sessionID, aimsFromExist, user);
			for (String projectID: projectAimIDs.keySet())
			{
				String uids = projectAimIDs.get(projectID);
				if (uids.trim().length() > 0)
				{
					List<ImageAnnotationCollection> iacs = AIMQueries.getAIMImageAnnotationsV4(projectID, AIMSearchType.ANNOTATION_UID, uids, user);
					annotations.addAll(iacs);
					if (iacs.size() == 0)
						log.warning("Annotations not found in Exist for uids:" + uids);
					log.info("" + iacs.size() + " AIM4 file(s) found for project:" + projectID +" for user " + user);
				
				}
			}
		}
		long existtime = System.currentTimeMillis();

		Map<String, EPADAIM> aimMAP = new HashMap<String, EPADAIM>();
		EPADAIMResultSet rs = aims.ResultSet;
		for (EPADAIM aim: rs.Result)
		{
			aimMAP.put(aim.aimID, aim);
		}
		aims = new EPADAIMList();
		for (ImageAnnotationCollection aim : annotations) {
			try {
				Aim4 a = new Aim4(aim);
				EPADAIM ea = aimMAP.get(aim.getUniqueIdentifier());
				if (ea == null)  continue;
				if (aim.getImageAnnotations().get(0).getName() != null)
				{
					ea.name = aim.getImageAnnotations().get(0).getName().getValue();
				}
				if (aim.getImageAnnotations().get(0).getListTypeCode() != null && aim.getImageAnnotations().get(0).getListTypeCode().size() > 0)
				{
					ea.template = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
					ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystem();
				}
				ea.date = aim.getDateTime();
				ea.comment = a.getComment();
				if (a.getFirstStudyDate() != null)
					ea.studyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getFirstStudyDate());
				ea.patientName = a.getPatientName();
				aims.addAIM(ea);
			} catch (Exception x) {
				log.warning("Error parsing annotation:" + aim, x);
				x.printStackTrace();
			}
		}
		aims.ResultSet.Result.addAll(aimsDB);
		aims.ResultSet.totalRecords = aims.ResultSet.Result.size();
		long endtime = System.currentTimeMillis();
		log.info("" + aims.ResultSet.totalRecords + " annotation summaries returned to client, took:" + (endtime-starttime) 
				+ " msecs, db time:" + (dbtime-starttime) + " exist time:" + (existtime-dbtime)
				+ " iac to summaries:" + (existtime-endtime));
		return aims;
	}
	
	public static EPADAIMList queryAIMImageAnnotationSummariesV4(EPADAIMList aims, AIMSearchType searchType, String searchValue, String user, String sessionID) throws Exception
	{
		long starttime = System.currentTimeMillis();
		EPADAIMList returnAims = new EPADAIMList();
		Map<String, List<EPADAIM>> aimsMap = getPermittedAIMs(sessionID, aims, user);
		if (searchType.equals(AIMSearchType.AIM_QUERY))
		{
			for (String projectID: aimsMap.keySet())
			{
				List<EPADAIM> paims = aimsMap.get(projectID);
				Map<String, EPADAIM> paimsMap = new HashMap<String, EPADAIM>();
				for (EPADAIM paim: paims) {
					paimsMap.put(paim.aimID, paim);
				}
				List<ImageAnnotationCollection> iacs = AIMQueries.getAIMImageAnnotationsV4(projectID, searchType, searchValue, user);			
				// Get other params
				for (int i = 0; i < iacs.size(); i++)
				{
					ImageAnnotationCollection iac = iacs.get(i);
					try
					{
						EPADAIM ea = paimsMap.get(iac.getUniqueIdentifier().getRoot());
						Aim4 a = new Aim4(iac);
						ea.name = iac.getImageAnnotations().get(0).getName().getValue();
						ea.template = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystem();// .getCode();
						ea.templateType = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
						ea.date = iac.getDateTime();
						ea.comment = a.getComment();
						if (a.getFirstStudyDate() != null)
							ea.studyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getFirstStudyDate());
						ea.patientName = a.getPatientName();
						ea.xml = null;
						returnAims.addAIM(ea);
					} catch (Exception x) {
						log.warning("Error parsing ImageAnnotationCollection:" + iac, x);
					}
				}
			}
		}
		else if (searchType.equals(AIMSearchType.JSON_QUERY))
		{
			for (String projectID: aimsMap.keySet())
			{
				List<EPADAIM> paims = aimsMap.get(projectID);
				Map<String, EPADAIM> paimsMap = new HashMap<String, EPADAIM>();
				for (EPADAIM paim: paims) {
					paimsMap.put(paim.aimID, paim);
				}
				List<String> jsons = AIMQueries.getJsonAnnotations(projectID, searchType, searchValue, user);			
				log.debug("Json query:" + searchValue + " number of matchs:" + jsons.size());
				// Get other params
				for (int i = 0; i < jsons.size(); i++)
				{
					JsonObject root = null;
					try
					{
						root = (JsonObject)new JsonParser().parse(jsons.get(i));
						//log.debug("Json result:" + root);
						JsonObject imageCollection = root.getAsJsonObject("ImageAnnotationCollection");
						String aimID = imageCollection.getAsJsonObject("uniqueIdentifier").getAsJsonPrimitive("root").getAsString();
						EPADAIM ea = paimsMap.get(aimID);
						//log.debug("Json ImageAnnotation:" + imageCollection.getAsJsonObject("imageAnnotations").getAsJsonObject("ImageAnnotation"));
						ea.name = imageCollection.getAsJsonObject("imageAnnotations").getAsJsonObject("ImageAnnotation").getAsJsonObject("name").getAsJsonPrimitive("value").getAsString();
						ea.template = imageCollection.getAsJsonObject("imageAnnotations").getAsJsonObject("ImageAnnotation").getAsJsonObject("typeCode").getAsJsonPrimitive("codeSystem").getAsString();
						ea.date = imageCollection.getAsJsonObject("dateTime").getAsJsonPrimitive("value").getAsString();
						ea.comment = imageCollection.getAsJsonObject("imageAnnotations").getAsJsonObject("ImageAnnotation").getAsJsonObject("comment").getAsJsonPrimitive("value").getAsString();
						ea.studyDate = ((JsonObject)imageCollection.getAsJsonObject("imageAnnotations").getAsJsonObject("ImageAnnotation")
						.getAsJsonObject("imageReferenceEntityCollection")
						.getAsJsonArray("ImageReferenceEntity").get(0)).getAsJsonObject("imageStudy").getAsJsonObject("startDate").getAsJsonPrimitive("value").getAsString();
						ea.patientName = imageCollection.getAsJsonObject("person").getAsJsonObject("name").getAsJsonPrimitive("value").getAsString();
						ea.xml = null;
						returnAims.addAIM(ea);
					} catch (Exception x) {
						log.warning("Error parsing json annotation:" + root, x);
					}
				}
			}
		}
		
		long endtime = System.currentTimeMillis();
		log.info("" + returnAims.ResultSet.totalRecords + " annotation summaries returned to client, took:" + (endtime-starttime) + " msecs");
		return returnAims;
	}

	public static void queryAIMImageAnnotationsV4(PrintWriter responseStream, EPADAIMList aims, String user, String sessionID) throws ParserConfigurationException, edu.stanford.hakan.aim4api.base.AimException
	{
		long starttime = System.currentTimeMillis();
		String annotationsXML = queryAIMImageAnnotationsV4(aims, user, sessionID);
		responseStream.print(annotationsXML);
		long resptime = System.currentTimeMillis();
		log.info("Time to write resp:" + (resptime-starttime) + " msecs");
	}

	public static void queryAIMImageJsonAnnotations(PrintWriter responseStream, EPADAIMList aims, String user, String sessionID) throws Exception
	{
		long starttime = System.currentTimeMillis();
		String annotationsXML = queryAIMImageAnnotationsV4(aims, user, sessionID);
		String jsonString =  XML.toJSONObject(annotationsXML).toString(0);;
        if (jsonString == null)
        	throw new Exception("Error converting to json");
		responseStream.print(jsonString);
		long resptime = System.currentTimeMillis();
		log.info("Time to write resp:" + (resptime-starttime) + " msecs");
	}

	public static String queryAIMImageAnnotationsV4(EPADAIMList aims, String user, String sessionID) throws ParserConfigurationException, edu.stanford.hakan.aim4api.base.AimException
	{
		long starttime = System.currentTimeMillis();
		EPADAIMList aimsFromExist = new EPADAIMList();
		EPADAIMList aimsFromDB = new EPADAIMList();
		for (EPADAIM ea: aims.ResultSet.Result)
		{
			if (ea.xml == null || ea.xml.equals(""))
			{
				aimsFromExist.addAIM(ea);
			}
			else
			{
				aimsFromDB.addAIM(ea);
			}
		}

		StringBuilder aimsDBXml = new StringBuilder("<imageAnnotations>\n");
		List<EPADAIM> aimsDB = new ArrayList<EPADAIM>();
		if (aimsFromDB.ResultSet.totalRecords > 0)
		{
			// Check permissions for aims from DB table
			Map<String, List<EPADAIM>> aimsMap = getPermittedAIMs(sessionID, aimsFromDB, user);
			for (List<EPADAIM> paims: aimsMap.values())
			{
				for (int i = 0; i < paims.size(); i++)
				{
					EPADAIM ea = paims.get(i);
					aimsDBXml.append(ea.xml);
					aimsDB.add(ea);
				}
			}
		}
		
		List<ImageAnnotationCollection> annotations = new ArrayList<ImageAnnotationCollection>();
		if (aimsFromExist.ResultSet.totalRecords > 0)
		{
			// Check permission for aims from Exist
			Map<String, String> projectAimIDs = getUIDCsvList(sessionID, aimsFromExist, user);
			for (String projectID: projectAimIDs.keySet())
			{
				String uids = projectAimIDs.get(projectID);
				if (uids.trim().length() > 0)
				{
					List<ImageAnnotationCollection> iacs = AIMQueries.getAIMImageAnnotationsV4(projectID, AIMSearchType.ANNOTATION_UID, uids, user);
					annotations.addAll(iacs);
					if (iacs.size() == 0)
						log.warning("Annotations not found in Exist for uids:" + uids);
					log.info("" + iacs.size() + " AIM4 file(s) found for project:" + projectID +" for user " + user);
				
				}
			}
		}

		for (ImageAnnotationCollection aim : annotations) {
			aimsDBXml.append(edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(aim));
		}
		long xmltime = System.currentTimeMillis();
		aimsDBXml.append("</imageAnnotations>\n");
		log.info("Time taken create xml:" + (xmltime-starttime) + " msecs for " + (annotations.size()+ aimsDB.size()) +" annotations");
		return aimsDBXml.toString();
	}

	public static EPADAIMList getAllVersionSummaries(EPADAIM aim) throws Exception
	{
		List<ImageAnnotationCollection> iacs = AIMQueries.getAllVersionSummaries(aim);
		EPADAIMList aims = new EPADAIMList();
		for (int i = 0; i < iacs.size(); i++)
		{
			ImageAnnotationCollection iac = iacs.get(i);
			try
			{
				Aim4 a = new Aim4(iac);
				EPADAIM ea = new EPADAIM(iac.getUniqueIdentifier().getRoot(), a.getLoggedInUser().getLoginName(), 
						aim.projectID, aim.subjectID, aim.studyUID, aim.seriesUID, aim.imageUID, aim.instanceOrFrameNumber);
				ea.name = iac.getImageAnnotations().get(0).getName().getValue();
				ea.template = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystem();// .getCode();
				ea.templateType = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
				ea.date = iac.getDateTime();
				ea.comment = a.getComment();
				if (a.getFirstStudyDate() != null)
					ea.studyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getFirstStudyDate());
				ea.patientName = a.getPatientName();
				ea.xml = null;
				aims.addAIM(ea);
			} catch (Exception x) {
				log.warning("Error parsing ImageAnnotationCollection:" + iac, x);
			}
		}
		return aims;
	}

	public static EPADAIMList getPreviousVersionSummaries(EPADAIM aim) throws Exception
	{
		List<ImageAnnotationCollection> iacs = AIMQueries.getPreviousVersionSummaries(aim);
		EPADAIMList aims = new EPADAIMList();
		for (int i = 0; i < iacs.size(); i++)
		{
			ImageAnnotationCollection iac = iacs.get(i);
			try
			{
				Aim4 a = new Aim4(iac);
				EPADAIM ea = new EPADAIM(iac.getUniqueIdentifier().getRoot(), a.getLoggedInUser().getLoginName(), 
						aim.projectID, aim.subjectID, aim.studyUID, aim.seriesUID, aim.imageUID, aim.instanceOrFrameNumber);
				ea.name = iac.getImageAnnotations().get(0).getName().getValue();
				ea.template = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystem();// .getCode();
				ea.templateType = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
				ea.date = iac.getDateTime();
				ea.comment = a.getComment();
				if (a.getFirstStudyDate() != null)
					ea.studyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getFirstStudyDate());
				ea.patientName = a.getPatientName();
				ea.xml = null;
				aims.addAIM(ea);
			} catch (Exception x) {
				log.warning("Error parsing ImageAnnotationCollection:" + iac, x);
			}
		}
		return aims;
	}

	public static void returnAllVersions(PrintWriter responseStream, EPADAIM aim) throws Exception
	{
		List<ImageAnnotationCollection> iacs = AIMQueries.getAllVersionSummaries(aim);
		StringBuilder annotationsXML = new StringBuilder("<imageAnnotations>\n");

		for (ImageAnnotationCollection iac : iacs) {
			annotationsXML.append(edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(iac));
		}
		annotationsXML.append("</imageAnnotations>\n");
		responseStream.print(annotationsXML);
		return;
	}

	public static void returnPreviousVersions(PrintWriter responseStream, EPADAIM aim) throws Exception
	{
		List<ImageAnnotationCollection> iacs = AIMQueries.getPreviousVersionSummaries(aim);
		StringBuilder annotationsXML = new StringBuilder("<imageAnnotations>\n");

		for (ImageAnnotationCollection iac : iacs) {
			annotationsXML.append(edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(iac));
		}
		annotationsXML.append("</imageAnnotations>\n");
		responseStream.print(annotationsXML);
		return;
	}

	private static String XmlDocumentToString(Document document, String xmlns)
	{ // Create an XML document from a String
		new XmlNamespaceTranslator().addTranslation(null, xmlns)
				.addTranslation("", xmlns).translateNamespaces(document);

		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = null;

		try {
			trans = transfac.newTransformer();
		} catch (TransformerConfigurationException e) {
			log.warning("Error transforming XML document", e);
		}

		trans.setOutputProperty(OutputKeys.INDENT, "yes");

		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(document);

		try {
			trans.transform(source, result);
		} catch (TransformerException e) {
			log.warning("Error transforming XML document", e);
		}
		return sw.toString();
	}

	// Rename namespace of the nodes
	private static Node renameNodeNS(Node node, String newName, String xmlns)
	{
		Element newNode = node.getOwnerDocument().createElementNS(xmlns,
				newName);
		NamedNodeMap map = node.getAttributes();
		for (int i = 0; i < map.getLength(); i++) {
			newNode.setAttribute(map.item(i).getNodeName(), map.item(i).getNodeValue());
		}

		NodeList tempList = node.getChildNodes();
		for (int i = 0; i < tempList.getLength(); i++) {
			newNode.appendChild(tempList.item(i).cloneNode(true));
		}
		return newNode;
	}

	public static ImageAnnotation getImageAnnotationFromFile(File file, String xsdFilePath)	{
		try {
			return AnnotationGetter.getImageAnnotationFromFile(file.getAbsolutePath(), xsdFilePath);
		} catch (AimException e) {
			return null;
		}
	}

    public static ImageAnnotationCollection getImageAnnotationFromFileV4(File file, String xsdFilePath) {
        try {
			return edu.stanford.hakan.aim4api.usage.AnnotationGetter.getImageAnnotationCollectionFromFile(file.getAbsolutePath(), xsdFilePath);
		} catch (Exception e) {
			return null;
		}
    }
    
	public static String runPlugIn(String[] aimIDs, String templateName, String projectID, String jsessionID) throws Exception
	{                        
		String result = "";
		for (String aimID:  aimIDs)
		{
			if (isPluginStillRunning(aimID))
			{
				result = result + "\n" + "Previous version of this AIM " + aimID + " is still being processed by the plugin";
				continue;
			}
			boolean templateHasBeenFound = false;
			String handlerName = null;
			String pluginName = null;

			List<String> list = PluginConfig.getInstance().getPluginTemplateList();
			for (int i = 0; i < list.size(); i++) {
				String templateNameFounded = list.get(i);
				if (templateNameFounded.equals(templateName)) {
					handlerName = PluginConfig.getInstance().getPluginHandlerList().get(i);
					pluginName = PluginConfig.getInstance().getPluginNameList().get(i);
					templateHasBeenFound = true;
				}
			}

			if (templateHasBeenFound) {
				log.info("Starting Plugin task for:" + pluginName);
				(new Thread(new PluginStartTask(jsessionID, pluginName, aimID, 0, projectID))).start();				
			}
		}
		return result;
	}

	public static String readPlugInData(EPADAIM aim, String templateName, String jsessionID) throws Exception
	{
		String fileName = EPADConfig.getEPADWebServerResourcesDir() + "plugins/" + aim.aimID + "_" + templateName + ".json";
		File dataFile = new File(fileName);
		if (!dataFile.exists())
			throw new Exception("Plugin data file does not exist:" + fileName);
		String data = EPADFileUtils.readFileAsString(dataFile);
		return data;
	}
	
	private static void setImageAnnotationUser(ImageAnnotation imageAnnotation, String username)
	{
		List<User> userList = new ArrayList<User>();
		User user = new User();
		user.setLoginName(username);
		user.setName(username);
		user.setCagridId(0);
		userList.add(user);
		imageAnnotation.setListUser(userList);
	}
	
	private static String saveImageAnnotationToServer(ImageAnnotation aim, String projectID) throws AimException,
	edu.stanford.hakan.aim4api.base.AimException
	{
		String result = "";

		if (aim.getCodeValue() != null) { // First, write a backup file
			String tempXmlPath = baseAnnotationDir + "temp-" + aim.getUniqueIdentifier() + ".xml";
			String storeXmlPath = baseAnnotationDir + aim.getUniqueIdentifier() + ".xml";
			File tempFile = new File(tempXmlPath);
			File storeFile = new File(storeXmlPath);

			if (useV4.equals("false")) {
				AnnotationBuilder.saveToFile(aim, tempXmlPath, xsdFilePath);
				result = AnnotationBuilder.getAimXMLsaveResult();
			} else {
				edu.stanford.hakan.aim4api.usage.AnnotationBuilder.saveToFile(aim.toAimV4(), tempXmlPath, xsdFilePathV4);
				result = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.getAimXMLsaveResult();
			}

			log.info("Save AIM file:" + result);
			if (storeFile.exists()) {
				storeFile.delete();
			}
			tempFile.renameTo(storeFile);
			if (useV4.equals("false")) {
				AnnotationBuilder.saveToServer(aim, eXistServerUrl, aim3Namespace, eXistCollection, xsdFilePath, eXistUsername,
						eXistPassword);
				result = AnnotationBuilder.getAimXMLsaveResult();
			} else {
			    String collectionName = eXistCollectionV4;
			    if (projectID != null && projectID.length() > 0)
			    	collectionName = collectionName + "/" + projectID;
			    ImageAnnotationCollection aim4 = aim.toAimV4();
				edu.stanford.hakan.aim4api.usage.AnnotationBuilder.saveToServer(aim4, eXistServerUrl, aim4Namespace,
						collectionName, xsdFilePathV4, eXistUsername, eXistPassword);
				result = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.getAimXMLsaveResult();
				try {
					saveAimToMongo(aim4, projectID);
				} catch (Exception e) {
					log.warning("Error saving aim to mongodb", e);
				}
			}
			log.info("Save AIM to Exist:" + result);
		}
		return result;
	}
	
	public static Map<String, String> getUIDCsvList(String sessionID, EPADAIMList aimlist, String username)
	{
		long starttime = System.currentTimeMillis();
		Set<String> projectIDs = aimlist.getProjectIds();
		Map<String,String> projectAIMs = new HashMap<String, String>();
		String defProjectID = EPADConfig.xnatUploadProjectID;
		for (String projectID: projectIDs)
		{
			try
			{
				String csv = "";
				boolean isCollaborator = UserProjectService.isCollaborator(sessionID, username, projectID);
				defProjectID = projectID;
				log.info("User:" + username + " projectID:" + projectID + " isCollaborator:" + isCollaborator);
				Set<EPADAIM> aims = aimlist.getAIMsForProject(projectID);
				for (EPADAIM aim: aims)
				{
					if (!isCollaborator || aim.userName.equals(username) || aim.userName.equals("shared"))
						csv = csv + "," +  aim.aimID;
				}
				if (csv.length() != 0)
				{
					projectAIMs.put(projectID, csv.substring(1));
				}
			}
			catch (Exception x) {
				log.warning("Error checking AIM permission:", x);
			}
		}
		long endtime = System.currentTimeMillis();
		log.info("Time taken for checking AIM permissions:" + (endtime-starttime) + " msecs, in:" + aimlist.ResultSet.totalRecords + " ok:" + projectAIMs.values().size() + " username:" + username);
		
		if (projectAIMs.keySet().isEmpty())
			projectAIMs.put(defProjectID, "");
		return projectAIMs;
	}
	
	public static Map<String, List<EPADAIM>> getPermittedAIMs(String sessionID, EPADAIMList aimlist, String username)
	{
		long starttime = System.currentTimeMillis();
		Set<String> projectIDs = aimlist.getProjectIds();
		Map<String,List<EPADAIM>> projectAIMsMap = new HashMap<String, List<EPADAIM>>();
		for (String projectID: projectIDs)
		{
			try
			{
				boolean isCollaborator = UserProjectService.isCollaborator(sessionID, username, projectID);
				//log.info("User:" + username + " projectID:" + projectID + " isCollaborator:" + isCollaborator);
				Set<EPADAIM> aims = aimlist.getAIMsForProject(projectID);
				List<EPADAIM> projectAIMs = projectAIMsMap.get(projectID);
				if (projectAIMs == null)
				{
					projectAIMs = new ArrayList<EPADAIM>();
					projectAIMsMap.put(projectID, projectAIMs);
				}
				for (EPADAIM aim: aims)
				{
					if (!isCollaborator || aim.userName.equals(username) || aim.userName.equals("shared"))
						projectAIMs.add(aim);
				}
			}
			catch (Exception x) {
				log.warning("Error checking AIM permission:", x);
			}
		}
		long endtime = System.currentTimeMillis();
		log.info("Time taken for checking AIM permissions:" + (endtime-starttime) + " msecs, in:" + aimlist.ResultSet.totalRecords + " out:" + projectAIMsMap.values().size() + " username:" + username);
		
		return projectAIMsMap;
	}
	
	public static int convertAllAim3() throws Exception {
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		List<EPADAIM> epadaims = epadDatabaseOperations.getAIMs(new ProjectReference(null));
		return convertAim3(epadaims);
	}
	
	public static void convertAim3(String aimID) throws Exception {
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		EPADAIM epadaim = epadDatabaseOperations.getAIM(aimID);
		List<EPADAIM> epadaims = new ArrayList<EPADAIM>();
		epadaims.add(epadaim);
		convertAim3(epadaims);
	}
	
	public static int convertAim3(List<EPADAIM> epadaims) throws Exception {
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		List<Map<String, String>> coordinationTerms = epadDatabaseOperations.getCoordinationData("%");
		//log.info("coordinations:" + coordinationTerms);
		int count = 0;
		for (EPADAIM epadaim: epadaims)
		{
			log.info("Converting AIM3:" + epadaim.aimID + " in project " + epadaim.projectID);
			try {
				List<ImageAnnotation> aims = AIMQueries.getAIMImageAnnotations(epadaim.projectID, AIMSearchType.ANNOTATION_UID, epadaim.aimID, "admin", 1, 50000, true);
				if (aims.size() > 0)
				{
					ImageAnnotation aim = aims.get(0);
					String tempXmlPath = baseAnnotationDir + "temp-" + aim.getUniqueIdentifier() + ".xml";
					File tempFile = new File(tempXmlPath);
					AnnotationBuilder.saveToFile(aim, tempXmlPath, xsdFilePath);
                    ImageAnnotationCollection iac = edu.stanford.hakan.aim4api.usage.AnnotationGetter.getImageAnnotationCollectionFromFile(tempXmlPath);
                    edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation iaV3 = new  edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation(iac);
                    log.info("Saving AIM4:" + epadaim.aimID + " in project " + epadaim.projectID);
                    iac = iaV3.toAimV4(coordinationTerms);
					String result = AIMUtil.saveImageAnnotationToServer(iac, epadaim.projectID, 0, null, false);
					if (result.toLowerCase().contains("success") && epadaim.projectID != null)
					{
						epadDatabaseOperations.updateAIMXml(epadaim.aimID, edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(iac));
					}
					tempFile.delete();
					count++;
				}
				else
					log.warning("Error converting aim3:" + epadaim.aimID + ", not found");
				
			}
			catch (Exception x) {
				log.warning("Error converting aim3:" + epadaim.aimID, x);
			}
		}
		return count;
	}
	
	public static void updateTableXMLs(List<EPADAIM> aims)
	{
		long starttime = System.currentTimeMillis();
		HashMap<String, String> searchValueByProject = new HashMap<String, String>();
		Set<String> aimIDs = new HashSet<String>();
		for (EPADAIM aim: aims)
		{
			String projectID = aim.projectID;
			if (projectID == null || projectID.trim().length() == 0) continue;
				
			String searchValue = searchValueByProject.get(projectID);
			if (searchValue == null)
				searchValue = aim.aimID;
			else
				searchValue = searchValue + "," + aim.aimID;
			searchValueByProject.put(projectID, searchValue);
			aimIDs.add(aim.aimID);
		}
		
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		for(String projectID: searchValueByProject.keySet())
		{
			List<ImageAnnotationCollection> paims = AIMQueries.getAIMImageAnnotationsV4(projectID, AIMSearchType.ANNOTATION_UID, searchValueByProject.get(projectID), "admin");
			log.info("" + paims.size() + " AIM4 file(s) found for project:" + projectID);
			boolean mongoErr = false;
			for (ImageAnnotationCollection aim: paims)
			{
				aimIDs.remove(aim.getUniqueIdentifier().getRoot());
				try {
					EPADAIM epadAim = epadDatabaseOperations.updateAIMXml(aim.getUniqueIdentifier().getRoot(), edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(aim));
				} catch (Exception e) {
					log.warning("Error updating AIM Table XML", e);
				}
				try {
					if (!mongoErr)
						saveAimToMongo(aim, projectID);
				} catch (Exception e) {
					log.warning("Error saving to mongoDB", e);
					mongoErr = true;
				}
			}
		}
		// Delete aims that are not found in Exist and have null xml
		for (String aimID: aimIDs)
		{
			EPADAIM aim = epadDatabaseOperations.getAIM(aimID);
			if (aim.xml == null || aim.xml.length() == 0)
				epadDatabaseOperations.deleteAIM("admin", aimID);
		}
	}	

	public static void updateMongDB(List<EPADAIM> aims)
	{
		long starttime = System.currentTimeMillis();
		boolean mongoErr = false;
		for (EPADAIM aim: aims)
		{
			try {
				if (!mongoErr)
					MongoDBOperations.saveAnnotationToMongo(aim.aimID, aim.xml, aim.projectID);
			} catch (Exception e) {
				log.warning("Error saving to mongoDB", e);
				mongoErr = true;
			}
		}	
		
	}
	
	public static void saveAimToMongo(ImageAnnotationCollection aim, String collection) throws Exception
	{
		String aimXML = null;
		try {
			aimXML = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(aim);
		} catch (Exception e) {
			log.warning("Error converting AIM to XML", e);
			throw e;
		}
		MongoDBOperations.saveAnnotationToMongo(aim.getUniqueIdentifier().getRoot(), aimXML, collection);
	}
	
	public static String convertToJson(String xml)
	{
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(xml);
            String jsonString = xmlJSONObj.toString(0);
            //log.info("JSON:" + jsonString);
            return jsonString;
            // Some other ways to convert to json
//			HierarchicalStreamReader sourceReader = new XppReader(new StringReader(aimXml));
//			StringWriter buffer = new StringWriter();
//			//JettisonBadgerFishXmlDriver jettisonDriver = new JettisonBadgerFishXmlDriver();
//			JettisonMappedXmlDriver jettisonDriver = new JettisonMappedXmlDriver();
//			//jettisonDriver.createWriter(buffer);
//			HierarchicalStreamWriter destinationWriter = jettisonDriver.createWriter(buffer);
//			HierarchicalStreamCopier copier = new HierarchicalStreamCopier();
//			copier.copy(sourceReader, destinationWriter);
//			String jsonStr = buffer.toString();
//			log.info("JSON:" + jsonStr);
//			JSONObject jsonObj = new JSONObject(jsonStr);
//			String xml = XML.toString(jsonObj);
//			log.info("New XML:" + xml);

			//	            JSONObject xmlJSONObj = XML.toJSONObject(aimXml);
//            String newXml = XML.toString(xmlJSONObj);
        	// java net.sf.saxon.Transform source.xml xml-to-json.xsl use-badgerfish=true()
//        	String[] args = new String[3];
//        	args[0] = aimFile.getAbsolutePath();
//        	args[1] = "/home/epad/DicomProxy/etc/xml-to-json.xsl";
//        	//args[2] = aimFile.getAbsolutePath().replace(".xml", ".json");
//        	args[2] = "use-badgerfish=true";
//        	log.info("aim file:" + args[0]);
        	//(new Transform()).doTransform(args, "java net.sf.saxon.Transform");
            //log.info("Old xml: " + EPADFileUtils.readFileAsString(aimFile));
            //log.info("New Xml: " + EPADFileUtils.readFileAsString(new File(args[1])));
        } catch (Exception e) {
        	log.warning("Error converting aim to json", e);
			return null;
		}	
	}
	
	static final String[] SCHEMA_FILES = {
		"AIMTemplate_v2rv13.xsd",
		"AIM_v3.xsd",
		"AimXPath.xml",
		"AIMTemplate_v2rvStanford.xsd",
		"AIM_v4_rv44_XML.xsd",
		"ISO_datatypes_Narrative.xsd"		
	};
	
	public static void checkSchemaFiles()
	{
		for (String schemaFile: SCHEMA_FILES)
		{
			File file = new File(EPADConfig.getEPADWebServerSchemaDir() + schemaFile);
			if (!file.exists()) {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = new AIMUtil().getClass().getClassLoader().getResourceAsStream("schema/" + schemaFile);
		            out = new FileOutputStream(file);

		            // Transfer bytes from in to out
		            byte[] buf = new byte[1024];
		            int len;
		            while ((len = in.read(buf)) > 0)
		            {
		                    out.write(buf, 0, len);
		            }
				} catch (Exception x) {
					
				} finally {
		            IOUtils.closeQuietly(in);
		            IOUtils.closeQuietly(out);
				}
			}
		}
	}
	
	private static int getInt(String value)
	{
		try {
			return new Integer(value.trim()).intValue();
		} catch (Exception x) {
			return 0;
		}
	}

}
