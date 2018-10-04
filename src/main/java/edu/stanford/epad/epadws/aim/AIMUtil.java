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
import com.pixelmed.dicom.SOPClass;
import com.pixelmed.dicom.SequenceAttribute;
import com.pixelmed.dicom.SequenceItem;
import com.pixelmed.dicom.TagFromName;

import edu.stanford.epad.common.dicom.DCM4CHEEImageDescription;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.plugins.PluginAIMUtil;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.EventMessageCodes;
import edu.stanford.epad.common.util.ExportAimOperations;
import edu.stanford.epad.common.util.MongoDBOperations;
import edu.stanford.epad.common.util.XmlNamespaceTranslator;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADAIMList.EPADAIMResultSet;
import edu.stanford.epad.dtos.internal.DCM4CHEESeries;
import edu.stanford.epad.dtos.internal.DICOMElementList;
import edu.stanford.hakan.aim4api.project.epad.Aim4;
import edu.stanford.hakan.aim4api.project.epad.Aim4;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.handlers.event.EventHandler;
import edu.stanford.epad.epadws.models.NonDicomSeries;
import edu.stanford.epad.epadws.models.Plugin;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.Template;
import edu.stanford.epad.epadws.plugins.PluginConfig;
import edu.stanford.epad.epadws.processing.pipeline.task.DSOStatisticsTask;
import edu.stanford.epad.epadws.processing.pipeline.task.ImageCheckTask;
import edu.stanford.epad.epadws.processing.pipeline.task.PluginStartTask;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.PluginOperations;
import edu.stanford.epad.epadws.service.SessionService;
import edu.stanford.epad.epadws.service.UserProjectService;
import edu.stanford.hakan.aim4api.base.AimException;
import edu.stanford.hakan.aim4api.base.CD;
import edu.stanford.hakan.aim4api.base.CalculationEntity;
import edu.stanford.hakan.aim4api.base.DicomImageReferenceEntity;
import edu.stanford.hakan.aim4api.base.DicomSegmentationEntity;
import edu.stanford.hakan.aim4api.base.Enumerations;
import edu.stanford.hakan.aim4api.base.ExtendedCalculationResult;
import edu.stanford.hakan.aim4api.base.II;
import edu.stanford.hakan.aim4api.base.ImageAnnotationCollection;
import edu.stanford.hakan.aim4api.base.ImageReferenceEntity;
import edu.stanford.hakan.aim4api.base.ImageStudy;
import edu.stanford.hakan.aim4api.base.ST;
import edu.stanford.hakan.aim4api.base.SegmentationEntityCollection;
import edu.stanford.hakan.aim4api.compability.aimv3.DICOMImageReference;
import edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation;
import edu.stanford.hakan.aim4api.compability.aimv3.Modality;
import edu.stanford.hakan.aim4api.compability.aimv3.Person;
import edu.stanford.hakan.aim4api.compability.aimv3.Segmentation;
import edu.stanford.hakan.aim4api.compability.aimv3.SegmentationCollection;
import edu.stanford.hakan.aim4api.compability.aimv3.User;
import edu.stanford.hakan.aim4api.project.epad.Patient;
import edu.stanford.hakan.aim4api.usage.AnnotationBuilder;
import edu.stanford.hakan.aim4api.usage.AnnotationGetter;
import edu.stanford.hakan.aim4api.usage.AnnotationValidator;

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
	//a list of strings, holds information about the uploaded dso, to be used during DSOMaskPNGGeneration to update the aim enity in db
	//holds	seriesID,dsoInstanceUid,aimID,projectID	in this order	
	public static List<String[]> dsoAims=new ArrayList<>();
	
	public static void updateDSOStartIndex(EPADAIM aim,int dsoStartIndex) {
		ImageAnnotationCollection iac=null;
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		try {
			iac = AnnotationGetter.getImageAnnotationCollectionFromString(aim.xml, xsdFilePathV4);
			iac.getImageAnnotation().setDsoStartIndex(dsoStartIndex);
			//update the one in db
			epadDatabaseOperations.updateAIMXml(aim.aimID, iac.getXMLString());
			//update the one in exist
			
			saveImageAnnotationToServer(iac, aim.projectID, dsoStartIndex, null, false);
		} catch (AimException e) {
			log.info("Aim4 exception getting the aim from string " + e.getMessage());
		}
	}
	
	public static void updateDSOStartIndexAndName(EPADAIM aim,int dsoStartIndex, String name) {
		ImageAnnotationCollection iac=null;
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		try {
			iac = AnnotationGetter.getImageAnnotationCollectionFromString(aim.xml, xsdFilePathV4);
			iac.getImageAnnotation().setDsoStartIndex(dsoStartIndex);
			iac.getImageAnnotation().setName(new ST(name));
			//update the one in db
			epadDatabaseOperations.updateAIMXml(aim.aimID, iac.getXMLString());
			//update the one in exist
			
			saveImageAnnotationToServer(iac, aim.projectID, dsoStartIndex, null, false);
		} catch (AimException e) {
			log.info("Aim4 exception getting the aim from string " + e.getMessage());
		}
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
    public static ImageAnnotationCollection saveImageAnnotationToServer(ImageAnnotationCollection aim, String projectID, int frameNumber, String jsessionID) throws AimException,
    edu.stanford.hakan.aim4api.base.AimException {
    	return saveImageAnnotationToServer(aim, projectID, frameNumber, jsessionID, true);
    }
    
    public static ImageAnnotationCollection saveImageAnnotationToServer(ImageAnnotationCollection aim, String projectID, int frameNumber, String jsessionID, boolean invokePlugin) throws AimException,
	    edu.stanford.hakan.aim4api.base.AimException {
		//String result = "";
		
		if (aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCode() != null) { 
			
			if (isPluginStillRunning(aim.getUniqueIdentifier().getRoot()))
				throw new edu.stanford.hakan.aim4api.base.AimException("Previous version of this AIM " + aim.getUniqueIdentifier().getRoot() + " is still being processed by the plugin");
			
			
			// For safety, write a backup file - what is this strange safety feature??
		    String tempXmlPath = baseAnnotationDir + "temp-" + aim.getUniqueIdentifier().getRoot() + ".xml";
		    String storeXmlPath = baseAnnotationDir + aim.getUniqueIdentifier().getRoot() + ".xml";
		    File tempFile = new File(tempXmlPath);
		    File storeFile = new File(storeXmlPath);
		    String xml = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(aim);
		    log.info("Saving AIM xml="+ xml);
		    log.info("Saving AIM file with ID " + aim.getUniqueIdentifier().getRoot() + " to temp folder "+ tempXmlPath);
			edu.stanford.hakan.aim4api.usage.AnnotationBuilder.saveToFile(aim, tempXmlPath, xsdFilePathV4);
		    
		    log.info(AnnotationBuilder.getAimXMLsaveResult());
		    if (storeFile.exists()) {
		        storeFile.delete();
		    }
		    tempFile.renameTo(storeFile);
		
		    String collectionName = eXistCollectionV4;
		    if (projectID != null && projectID.length() > 0)
		    	collectionName = collectionName + "/" + projectID;
		    aim = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.saveToServer(aim, eXistServerUrl, aim4Namespace,
		    		collectionName, xsdFilePathV4, eXistUsername, eXistPassword);
		  
		
		    log.info(edu.stanford.hakan.aim4api.usage.AnnotationBuilder.getAimXMLsaveResult());
			try {
				if (projectID != null && projectID.length() > 0)
					saveAimToMongo(aim, projectID);
			} catch (Exception e) {
				log.warning("Error saving aim to mongodb", e);
			}
			//do not depend on epad-plugin. see if the template has a plugin with codevalue=plugin id
			PluginOperations pluginOp=PluginOperations.getInstance();
			Plugin plugin=null;
		    try {
				if (aim.getImageAnnotation().getListTypeCode().get(0).getCode()!=null && (plugin=pluginOp.getPlugin(aim.getImageAnnotation().getListTypeCode().get(0).getCode()))!=null){
					String templateName = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName().getValue(); // ex: jjvector
				    log.info("Found an AIM plugin template with name " + templateName + " and AIM ID " + aim.getUniqueIdentifier().getRoot());

				    if (jsessionID != null && invokePlugin) {
				    	// Start plugin task
						log.info("Starting Plugin task for:" + plugin.getName());
						(new Thread(new PluginStartTask(jsessionID, plugin.getName(), aim.getUniqueIdentifier().getRoot(), frameNumber, projectID))).start();				
				    }
				}
			} catch (Exception e) {
				log.warning("Couldn't get plugin", e);
			}
		}
		return aim;
	}	
	
	public static boolean deleteAIM(String aimID, String projectID)
	{
		try {
		    String collectionName = eXistCollectionV4;
		    if (projectID != null && projectID.length() > 0)
		    	collectionName = collectionName + "/" + projectID;
		    List<ImageAnnotationCollection> aims = AIMQueries.getAIMImageAnnotationsV4(projectID, AIMSearchType.ANNOTATION_UID, aimID, "admin");
			log.info("Number of aims found in exist with aimID = " + aimID + " : " + aims.size());
			edu.stanford.hakan.aim4api.database.exist.ExistManager.removeImageAnnotationCollectionFromServer(
					eXistServerUrl, aim4Namespace, collectionName, eXistUsername, eXistPassword, aimID);
			if (EPADConfig.exportURL!=null){
				if (aims.size()==0)
					log.warning("No aim file");
				else  //if there is more than one it will get the first
					ExportAimOperations.sendDeleteJsonToApi(aimID,aims.get(0));
			}
			MongoDBOperations.deleteAnotationInMongo(aimID, projectID);

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
	public static Aim4 generateAIMFileForDSO(File dsoFile) throws Exception
	{
		return generateAIMFileForDSO(dsoFile, "shared", null);
	}
	
	public static Aim4 generateAIMFileForDSO(File dsoFile, String username, String projectID) throws Exception
	{
		return generateAIMFileForDSO(dsoFile, username, projectID, null);
	}
	
	
	public static Aim4 generateAIMFileForDSO(File dsoFile, String username, String projectID, String aimName) throws Exception
	{
		return generateAIMFileForDSO(dsoFile, username, projectID, aimName, false);
	}
	public static Aim4 generateAIMFileForDSO(File dsoFile, String username, String projectID, String aimName, boolean generateCalcs) throws Exception
	{
		log.info("Creating DSO AIM for user " + username + " in project " + projectID + " file:" + dsoFile.getAbsolutePath());
		AttributeList dsoDICOMAttributes = PixelMedUtils.readDICOMAttributeList(dsoFile);
		String patientID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.PatientID);
		String patientName = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.PatientName);
		String patientBirthDay = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.PatientBirthDate);

		//just date or date with time. and put 19000101000000 so we understand it is default
		if (patientBirthDay.trim().length() != 8 && patientBirthDay.trim().length() != 14) patientBirthDay = "19000101000000";
		String patientSex = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.PatientSex);
		if (patientSex.trim().length() != 1) patientSex = "F";
		String dsoDate = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SeriesDate);
		if (dsoDate.trim().length() != 8 && dsoDate.trim().length() != 14) dsoDate = "19000101000000";
		String dsoTime = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SeriesTime);
		//append time to date
		if (dsoTime.replace(":", "").trim().length()>=6)
			dsoTime=dsoTime.replace(":", "").trim().substring(0, 6);
		else dsoTime = "000000";
		dsoDate+=dsoTime;
		String studyDate = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.StudyDate);
		if (studyDate.trim().length() != 8) studyDate = "19000101";
		String studyTime = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.StudyTime);
		if (studyTime.trim().length() > 6) studyTime=studyTime.substring(0,6);
		else if (studyTime.trim().length() != 6) studyTime = "000000";

		String sopClassUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SOPClassUID);
		String studyUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.StudyInstanceUID);
		log.info("DSO:" + dsoFile.getAbsolutePath() + " PatientID:" + patientID + " studyUID:" + studyUID + " projectID:" + projectID);
		String seriesUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SeriesInstanceUID);
		String imageUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SOPInstanceUID);
		String description = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SeriesDescription);
		// TODO: This call to get Referenced Image does not work ???
		String[] referencedImageUID = Attribute.getStringValues(dsoDICOMAttributes, TagFromName.ReferencedSOPInstanceUID);
		String accessionNumber = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.AccessionNumber);
		
		String[] segNums = SequenceAttribute.getArrayOfSingleStringValueOrEmptyStringOfNamedAttributeWithinSequenceItems(dsoDICOMAttributes, TagFromName.SegmentSequence, TagFromName.SegmentNumber);
		if (segNums == null) segNums = new String[1];
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
		            referencedImageUID = new String[referencedInstanceSeq.getNumberOfItems()];
		            log.info("Sequence num of items is "+ referencedInstanceSeq.getNumberOfItems());
				    Iterator sitems2 = referencedInstanceSeq.iterator();
				    int index=0;
				    while (sitems2.hasNext())
				    {
					    sitem = (SequenceItem)sitems2.next();
			            list = sitem.getAttributeList();
			            if (list.get(TagFromName.ReferencedSOPInstanceUID) != null)
			            {		            	
			    			referencedImageUID[index++] = list.get(TagFromName.ReferencedSOPInstanceUID).getSingleStringValueOrEmptyString();
							log.info("referenced "+ (index-1) + " is " +referencedImageUID[index-1]);
			    			referencedSeriesUID = dcm4CheeDatabaseOperations.getSeriesUIDForImage(referencedImageUID[0]);
							if (referencedSeriesUID != null && referencedSeriesUID.length() > 0)
							{
								log.info("ReferencedSOPInstanceUID:" + referencedImageUID[0]);
//								break;
							}
							else
								log.info("DSO Referenced Image not found:" + referencedImageUID[0]);
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
			
	
			
			DCM4CHEEImageDescription id=dcm4CheeDatabaseOperations.getImageDescription(referencedStudyUID, referencedSeriesUID, referencedImageUID[0]);
			log.info("class uid from image description" + id.classUID);
			
			String name = aimName;
			if (name == null || name.trim().length() == 0) name = description;
			if (name == null || name.trim().length() == 0) name = "segmentation";
			
			EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
			edu.stanford.epad.epadws.models.User user=projectOperations.getUser(username);
			
			//get the seg template
			Template t=projectOperations.getTemplate("SEG");
			CD template=null;
			if (t!=null){
				//TODO it puts different values in the aim file than standard use. What does the gui put????
				template=new CD(t.getTemplateCode(),t.getTemplateName(),t.getCodingSchemeDesignator(),t.getCodingSchemeVersion());
			}
			Aim4 aim=new Aim4(username, user.getFullName(), patientName, patientID, patientBirthDay, patientSex, template, name, "", referencedImageUID[0], id.classUID, studyDate, studyTime, referencedStudyUID, referencedSeriesUID, accessionNumber, null, dsoDate);
			EpadOperations epadOperations = DefaultEpadOperations.getInstance();
			String uniquePatientID=epadOperations.getUniquePatientID(patientID, patientName);
			if (!uniquePatientID.equalsIgnoreCase(patientID)){
				aim.getPerson().setSourcePatientGroupId(new ST(patientID));
				patientID=uniquePatientID;
				aim.getPerson().setId(new ST(patientID));
			}
			DicomSegmentationEntity dc = new DicomSegmentationEntity();
			dc.setUniqueIdentifier();
			dc.setReferencedSopInstanceUid(new II(referencedImageUID[0]));
			dc.setSegmentNumber(1);
			dc.setSopClassUid(new II(sopClassUID));
			dc.setSopInstanceUid(new II(imageUID));
			dc.setSeriesInstanceUid(new II(seriesUID));
			dc.setStudyInstanceUid(new II(studyUID));
			aim.setSegmentationEntity(dc);
			
			//let aimapi to generate a uid
			aim.setTrackingUniqueIdentifier(null);
			aim.setAimStudyInstanceUid(new II(studyUID));
			//TODO aim series
			aim.setAimSeriesInstanceUid(null);
			aim.setAimAccessionNumber(new ST(accessionNumber));

			//genCalcs moved
			
			log.info("Saving AIM file for DSO " + imageUID + " in series " + seriesUID + " with ID "
					+ aim.getUniqueIdentifier().getRoot() + " date "+ dsoDate);
			try {
				boolean missingproject = false;
				if (projectID == null || projectID.trim().length() == 0)
				{
					missingproject = true;
					projectID = EPADConfig.xnatUploadProjectID;
				}
				//don't know dso start number!!!
				ImageAnnotationCollection aim4 = saveImageAnnotationToServer(aim, projectID, 0, null, false);
				if (aim4 != null)
				{
					ImageReference imageReference = new ImageReference(projectID, patientID, referencedStudyUID, referencedSeriesUID, referencedImageUID[0]);
					EpadDatabaseOperations dbOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
					EPADAIM eaim = dbOperations.getAIM(aim.getUniqueIdentifier().getRoot());
					if (eaim != null)
					{
						if (!username.equals("shared"))
							dbOperations.updateAIM(eaim.aimID, projectID, username);
					}
					else
					{
						eaim = dbOperations.addDSOAIM(username, imageReference, seriesUID, aim.getUniqueIdentifier().getRoot(),
								edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(aim4), name);
						if ((eaim.color == null || eaim.color.trim().length() == 0) && segNums.length > 1)
						{
							dbOperations.updateAIMColor(eaim.aimID, ","); // Indicate multiple colors needed
						}
					}
					if (missingproject)
					{
						Project proj = projectOperations.getFirstProjectForStudy(studyUID);
						if (proj != null && !proj.getProjectId().equals(EPADConfig.xnatUploadProjectID))
						{
							generateAIMFileForDSO(dsoFile, "shared", proj.getProjectId());
						}
					}
					if (generateCalcs){
						try {
							log.info("Starting DSO statistics task");
							DSOStatisticsTask stats = new DSOStatisticsTask(username, projectID, referencedSeriesUID, referencedImageUID, eaim.aimID, dsoFile.getAbsolutePath());
							new Thread(stats).start();
							
						} catch (Exception x) {
							log.warning("Exception running DSOStatistics", x);
						}
//						//open the referenced images and calculate the aggregations
//						Double[] calcs=DSOUtil.generateCalcs(referencedSeriesUID,referencedImageUID,dsoFile);
//						//add calculations to aim
//						if (calcs!=null){
//							log.info("Retrieved calculations are: min="+calcs[0]+ " max="+calcs[1]+ " mean="+calcs[2]+" stddev="+calcs[3]);
//							String units="pixels";
//							if (aim.getModality().equals("PT")) {
//								units="SUV";
//							}
//							if (aim.getModality().equals("CT")) {
//								units="HU";
//								
//							}
//							//start fresh, delete old ones
//							aim.clearCalculationEntityCollection();
//							aim.addCalculationEntityWithRef(Aim4.createMinCalculation(calcs[0], null, units),dc);
//							aim.addCalculationEntityWithRef(Aim4.createMaxCalculation(calcs[1], null, units),dc);
//							aim.addCalculationEntityWithRef(Aim4.createMeanCalculation(calcs[2], null, units),dc);
//							aim.addCalculationEntityWithRef(Aim4.createStdDevCalculation(calcs[3], null, units),dc);
//							if (calcs.length>4) aim.addCalculationEntityWithRef(Aim4.createVolumeCalculation(calcs[4], null, units),dc);
//							
//							
//						}
						
					}
				}
				return aim;
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

	
	public static ImageAnnotation generateAIMForNiftiDSO(String username, String projectID, String subjectID, String studyUID, String seriesUID, String imageUID, File niftiFile) throws Exception
	{
		log.info("Creating DSO AIM for user " + username + " in project " + projectID);
		
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance().getDcm4CheeDatabaseOperations();
		NonDicomSeries ndSeries = projectOperations.getNonDicomSeries(seriesUID);
		String referencedSeriesUID = ndSeries.getReferencedSeries();
		Date seriesDate = ndSeries.getSeriesDate();
		if (seriesDate == null) seriesDate = new Date();
		String dsoDate = new SimpleDateFormat("yyyyMMdd").format(seriesDate);
		Subject subject = projectOperations.getSubject(subjectID);
		String referencedStudyUID = studyUID; // Will be same study as DSO
		String patientName = subject.getName();
		log.info("Generating AIM file for Nifti DSO: " + imageUID + " for patient " + patientName);
		log.info("DSO Study UID=" + studyUID);
		log.info("DSO Series UID=" + seriesUID);
		log.info("DSO Image UID=" + imageUID);
		log.info("Referenced Series Instance UID=" + referencedSeriesUID);
		List<DCM4CHEEImageDescription> imageDescriptions = dcm4CheeDatabaseOperations.getImageDescriptions(studyUID, referencedSeriesUID);
		String name = imageUID;
		if (name == null || name.trim().length() == 0) name = "segmentation";
		
		ImageAnnotation imageAnnotation = new ImageAnnotation(0, "", dsoDate.substring(0,4) + "-" + dsoDate.substring(4,6) + "-" + dsoDate.substring(6,8) + "T00:00:00", name, "SEG",
				"SEG Only", "", "", "");

		SegmentationCollection sc = new SegmentationCollection();
		sc.AddSegmentation(new Segmentation(0, imageUID, SOPClass.SegmentationStorage, imageDescriptions.get(0).imageUID, 1));
		imageAnnotation.setSegmentationCollection(sc);

		DICOMImageReference originalDICOMImageReference = PluginAIMUtil.createDICOMImageReferenceV3Compability(referencedStudyUID,
				referencedSeriesUID, imageDescriptions.get(0).imageUID);
		imageAnnotation.addImageReference(originalDICOMImageReference);
		DICOMImageReference dsoDICOMImageReference = PluginAIMUtil.createDICOMImageReferenceV3Compability(studyUID, seriesUID, imageUID);
		imageAnnotation.addImageReference(dsoDICOMImageReference);

		Person person = new Person();
		if (subject.getGender() != null)
			person.setSex(subject.getGender().trim());
		String patientBirthDay = "";
		if (patientBirthDay.trim().length() == 8)
			person.setBirthDate(patientBirthDay.substring(0,4) + "-" + patientBirthDay.substring(4,6) + "-" + patientBirthDay.substring(6,8) + "T00:00:00"); // TODO
		person.setId(subjectID);
		person.setName(patientName);
		person.setCagridId(0);
		imageAnnotation.addPerson(person);
		// TODO Not general. See if we can generate AIM on GUI upload of DSO with correct user.
		setImageAnnotationUser(imageAnnotation, username);

		log.info("Saving AIM file for DSO " + imageUID + " in series " + seriesUID + " with ID "
				+ imageAnnotation.getUniqueIdentifier());
		try {
			ImageAnnotationCollection iac  = saveImageAnnotationToServer(imageAnnotation, projectID);
			if (iac != null)
			{
				ImageReference imageReference = new ImageReference(projectID, subjectID, referencedStudyUID, referencedSeriesUID, imageDescriptions.get(0).imageUID);
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
							edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(iac), name);
				}
			}
			return imageAnnotation;
		} catch (AimException e) {
			log.warning("Exception saving AIM file for DSO image " + imageUID + " in series " + seriesUID, e);
		}
		return null;
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
	            ImageAnnotationCollection imageAnnotation = AIMUtil.getImageAnnotationFromFileV4(aimFile, xsdFilePathV4);
	        
	            if (imageAnnotation != null) {
	                String jsessionID = SessionService.getJSessionIDFromRequest(httpRequest);

	                AIMUtil.saveImageAnnotationToServer(imageAnnotation, projectID, getInt(frameNo), jsessionID);
	                responseStream.println("-- Add to AIM server: " + imageAnnotation.getUniqueIdentifier().getRoot() + "<br>");
	            } else {
	                responseStream.println("-- Failed ! not added to AIM server<br>");
	                saveError = true;
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
	 * @param projectID
	 * @param sessionId
	 * @param username
	 * @param uploaded
	 * @return true if error in AIM save
	 * @throws AimException
	 * @throws edu.stanford.hakan.aim4api.base.AimException
	 */
	public static boolean saveAIMAnnotation(File aimFile, String projectID, String sessionId, String username) throws AimException
	{
		return saveAIMAnnotation(aimFile, projectID, 0, sessionId, username, false);
	}
	
	public static boolean saveAIMAnnotation(File aimFile, String projectID, int frameNumber, String sessionId, String username, boolean uploaded) throws AimException
	{
		return saveAIMAnnotation(aimFile, projectID, frameNumber, sessionId, username, uploaded, false);
	}

	public static boolean saveAIMAnnotation(File aimFile, String projectID, int frameNumber, String sessionId, String username, boolean uploaded, boolean isDicomSR) throws AimException
	{
		final Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		if (aimFile == null)
			return true;
		try {
			log.info("Converting AIM file to ImageAnnotationCollection Object " + aimFile.getAbsolutePath());
			EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
            ImageAnnotationCollection imageAnnotationColl = AIMUtil.getImageAnnotationFromFileV4(aimFile, xsdFilePathV4);
            if (imageAnnotationColl == null) {
            	List<Map<String, String>> coordinationTerms = new ArrayList<Map<String, String>>();
        		try
        		{
	            	coordinationTerms = epadDatabaseOperations.getCoordinationData("%");
        		} catch (Exception x) {};
                ImageAnnotationCollection iac = edu.stanford.hakan.aim4api.usage.AnnotationGetter.getImageAnnotationCollectionFromFile(aimFile.getAbsolutePath());
                edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation iaV3 = new  edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation(iac);
                imageAnnotationColl = iaV3.toAimV4(coordinationTerms);
                if (imageAnnotationColl == null)
                	log.warning("Unable to save aim:" + EPADFileUtils.readFileAsString(aimFile));
            }
            
            if (imageAnnotationColl != null) {
            	Aim4 aim = new Aim4(imageAnnotationColl);
            	//let's check if the subject is in epad and give notification. do not block
            	String patientID = aim.getPatientID();
            	final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
            	Subject subject = projectOperations.getSubject(patientID);
				if (subject == null) {
					log.warning("The patient does not exist in epad. ");
					//permanent log
					projectOperations.createEventLog(username, projectID, patientID, null, null, null, null, aimFile.getName(), EventMessageCodes.NO_PATIENT, "Patient "+ patientID+ " don't exist", true);
				}
            	
            	SegmentationEntityCollection sec = imageAnnotationColl.getImageAnnotations().get(0).getSegmentationEntityCollection();
				if (sec != null)
					log.debug("Aim4: " + imageAnnotationColl.getUniqueIdentifier().getRoot() + " SEC size:" + sec.getSegmentationEntityList().size());
				if (sec != null  && imageAnnotationColl.getImageAnnotations().get(0).getListTypeCode().get(0).getCode().equals("SEG"))
				{
					if (uploaded)
						throw new Exception("SEGOnly annotations should not be uploaded");
					if (sec.getSegmentationEntityList().size() == 0)			
					{
						epadDatabaseOperations.deleteAIM(username, imageAnnotationColl.getUniqueIdentifier().getRoot());
						throw new Exception("Invalid AIM, contains empty segmentation data");
					}
					//ml this is a segmentation aim sent from ui. 
					//delete and add the calculations everytime, ui calculates wrong anyway
					boolean generateCalcs=true;					
					if (generateCalcs){
						//open the referenced images and calculate the aggregations
						DicomSegmentationEntity dseg=(DicomSegmentationEntity) sec.getSegmentationEntityList().get(0);
						DicomImageReferenceEntity dimg= (DicomImageReferenceEntity) imageAnnotationColl.getImageAnnotation().getImageReferenceEntityCollection().get(0);
						String seriesUID=dimg.getImageStudy().getImageSeries().getInstanceUid().getRoot();
						String[] referencedImageUID=new String[2];
						
						Double[] calcs=DSOUtil.generateCalcs(dseg.getSopInstanceUid().getRoot());
						//add calculations to aim
						if (calcs!=null){
							log.info("Retrieved calculations are: min="+calcs[0]+ " max="+calcs[1]+ " mean="+calcs[2]+" stddev="+calcs[3]);
							String units="pixels";
							if (aim.getModality().equals("PT")) {
								units="SUV";
							}
							if (aim.getModality().equals("CT")) {
								units="HU";
								
							}
							//start fresh, delete old ones
							aim.clearCalculationEntityCollection();
							aim.addCalculationEntityWithRef(Aim4.createMinCalculation(calcs[0], null, units),dseg);
							aim.addCalculationEntityWithRef(Aim4.createMaxCalculation(calcs[1], null, units),dseg);
							aim.addCalculationEntityWithRef(Aim4.createMeanCalculation(calcs[2], null, units),dseg);
							aim.addCalculationEntityWithRef(Aim4.createStdDevCalculation(calcs[3], null, units),dseg);
							if (calcs.length>4) aim.addCalculationEntityWithRef(Aim4.createVolumeCalculation(calcs[4], null, units),dseg);
							
						}
						
					}
					
				}
				EPADAIM ea = epadDatabaseOperations.getAIM(imageAnnotationColl.getUniqueIdentifier().getRoot());
				String originalPatientID = aim.getOriginalPatientID();
				log.info("pat "+ imageAnnotationColl.getPerson().getId());
				log.info("pat id "+ imageAnnotationColl.getPerson().getId().getValue()+ " aimpatid "+ patientID + "  aimorig "+ originalPatientID);
				String patientName = aim.getPatientName();
				if (!patientID.equalsIgnoreCase(originalPatientID)) {
					//find the unique patient id for this epad instance and use that while saving
					EpadOperations epadOperations = DefaultEpadOperations.getInstance();
					String uniquePatientID=epadOperations.getUniquePatientID(originalPatientID, patientName);
					log.info("Unique patient id as "+uniquePatientID);
					if (!patientID.equalsIgnoreCase(uniquePatientID)){
						patientID=uniquePatientID;
//						Patient p=aim.getPatient();
//						p.setId(uniquePatientID);
//						log.info("new patient id is "+p.getId());
//						aim.setPatient(p);
//						log.info("Using patient id as "+aim.getPatientID());
						edu.stanford.hakan.aim4api.base.Person annotationPatient=imageAnnotationColl.getPerson();
						annotationPatient.setId(new ST(uniquePatientID));
						log.info("set annotation patient id as "+imageAnnotationColl.getPerson().getId().getValue());
					}
				}
				
				String imageID = aim.getFirstImageID().trim();
				String seriesID = aim.getFirstSeriesID();
				if (imageID != null && imageID.length() > 0)
					seriesID = aim.getSeriesID(imageID);
				String studyID = aim.getStudyID(seriesID);
				
				if (imageID==null || imageID.equals("") || imageID.equalsIgnoreCase("na")) {
					//aim has missing information. possibly from pf migration
					log.info("Aim4 has missing information. possibly from pf migration. Series is"+ seriesID + " comment is "+ aim.getComment());
					//TODO fill in image, study and patient info
					//check if series is in epad
					DCM4CHEESeries series=Dcm4CheeQueries.getSeries(seriesID);
					if (series==null) {
						log.warning("Series is not in the epad repository. Cannot fill in data. Aim4 cannot be listed without an imageid");
					}else {
						String[] comment=aim.getComment().split("/");
						Double sliceLoc=-99999.0;
						Double sliceThickness=-99999.0; //TODO get slice thickness
						try{
							if (comment.length==3) {
								sliceLoc=Double.valueOf(comment[1]);
								sliceThickness=Double.valueOf(comment[2]);
							} else if (comment.length==2) { //this shouldn't happen
								sliceLoc=Double.valueOf(comment[0]);
								sliceThickness=Double.valueOf(comment[1]);
							} 
						}catch(NumberFormatException ne) {
							log.warning("Couldn't get slice location " + aim.getComment());
							
						}
						if (sliceLoc!=-99999.0) {
							EpadOperations epadOperations = DefaultEpadOperations.getInstance();
							List<DCM4CHEEImageDescription> imageDescriptions = dcm4CheeDatabaseOperations.getImageDescriptions(
									series.studyUID, seriesID);
							for(DCM4CHEEImageDescription image:imageDescriptions){
								DICOMElementList imageDICOMElements = epadOperations.getDICOMElements(image.studyUID,
										image.seriesUID, image.imageUID);
								String imagePosition=epadOperations.getDICOMElement(imageDICOMElements,PixelMedUtils.ImagePositionPatientCode);
								Double imageLoc=-1.0;
								try{
									if (imagePosition!=null) {
										imageLoc=Double.parseDouble(imagePosition.split("\\\\")[2]);
									}
								}catch(Exception e){
									
									log.warning("Couldn't get image position "+e.getMessage()) ;
								}
								if ( imageLoc==-1.0) {
									imageLoc=((Double)Double.parseDouble(image.sliceLocation));
									log.info("Couldn't get image position using slice loc" +imageLoc) ;
								}
								if (((Double)(imageLoc-(0.5*sliceThickness))).intValue()==sliceLoc.intValue()||imageLoc.intValue()==sliceLoc.intValue()){
									log.info("found image "+ image.instanceNumber +  " uid "+image.imageUID);
									imageID=image.imageUID;
									//fill in person info
									{
						 				Patient p=new Patient();
						 				p.setBirthDate(epadOperations.getDICOMElement(imageDICOMElements,PixelMedUtils.PatientBirthDateCode));
						 				p.setSex(epadOperations.getDICOMElement(imageDICOMElements,PixelMedUtils.PatientSexCode));
										p.setName(series.patientName);
										p.setId(series.patientID);
										aim.setPatient(p);
										if (originalPatientID.equalsIgnoreCase(patientID))
											patientID=p.getId();
										
										edu.stanford.hakan.aim4api.base.Person annotationPatient = imageAnnotationColl.getPerson();
										annotationPatient.setBirthDate(p.getBirthDate());
										annotationPatient.setSex(new ST(p.getSex()));
										annotationPatient.setName(new ST(p.getName()));
										annotationPatient.setId(new ST(p.getId()));
									}
									//fill in the study and image info
									{
										List<ImageReferenceEntity> refs= imageAnnotationColl.getImageAnnotation().getImageReferenceEntityCollection().getImageReferenceEntityList();
										if (refs.size()==1) { //this should be the case for pf
											aim.setStudyID(series.seriesUID, series.studyUID, series.seriesDate, series.createdTime);
											
											ImageStudy study=((DicomImageReferenceEntity)refs.get(0)).getImageStudy();
											studyID=series.studyUID;
											study.setInstanceUid(new II(series.studyUID));
											study.setStartDate(series.seriesDate);
											study.setStartTime(series.createdTime);
											
											Modality mod=Modality.getInstance();
											//if the modality cannot be retrieved put default
											if ((mod.get(image.classUID))!=null)
												study.getImageSeries().setModality(mod.get(image.classUID));
											else
												study.getImageSeries().setModality(mod.getDefaultModality());
											study.getImageSeries().getImageCollection().get(0).setSopInstanceUid(new II(imageID));
											study.getImageSeries().getImageCollection().get(0).setSopClassUid(new II(image.classUID));
										}
									}
						 			
						 			
									break;
								}
							}
						}
					}
				}
				//try fix for aim edit tedseg
				if (studyID==null || studyID.equals("")) {
					studyID=dcm4CheeDatabaseOperations.getStudyUIDForSeries(seriesID);
					
				}
				List<String> seriesIds = aim.getSeriesIDs();
				if (ea != null && !ea.projectID.equals(projectID))
				{
					if (ea.projectID.equals(EPADConfig.xnatUploadProjectID))
					{
						epadDatabaseOperations.updateAIM(ea.aimID, projectID, username);
					}
					else
						projectID = ea.projectID;
				}
				String dsoSeriesUID = "";
				if (seriesIds.size() > 1)
				{
					dsoSeriesUID = seriesIds.get(1);
					
					log.info("Source SeriesUID:" + seriesID + " dsoSeriesUID:" +  dsoSeriesUID);
				}
				if (ea != null)
				{
					log.info("frame num:"+ea.dsoFrameNo);
					if (ea.dsoFrameNo!=0) {//there is a frame number in db use that!
						log.info("setting the xml with existing frameno "+ea.dsoFrameNo + " aim: "+ imageAnnotationColl.getXMLString());
						imageAnnotationColl.getImageAnnotation().setDsoStartIndex(ea.dsoFrameNo);
						
					}
					if (ea.seriesUID==null) {
						log.warning("no series in aim. skipping check");
					}else {
						log.debug("ea.seriesUID:" + ea.seriesUID + " seriesID:" +  seriesID);
						if (ea.seriesUID!=null && !ea.seriesUID.equals(seriesID) && (seriesIds.size() == 1 || !ea.seriesUID.equals(seriesIds.get(1))))
						{
							String message = "Invalid SeriesUID in AIM xml, AimID:" + ea.aimID + " Incorrect seriesUID in AIM:" + seriesID + " Should be:" + ea.seriesUID;
							log.warning(message);
							String xml = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(imageAnnotationColl);
							log.info("DSO aim:" + xml);
							epadDatabaseOperations.deleteAIM("admin", ea.aimID);
	//							throw new Exception(message);
							imageID = ea.imageUID;
							seriesID = ea.seriesUID;
							return false;
						}
						if (seriesIds.size() > 1 && ea.seriesUID!=null && ea.seriesUID.equals(seriesIds.get(1)))
						{
							seriesID = seriesIds.get(1); // How weird, the actual series is second
							dsoSeriesUID = seriesIds.get(0);
							log.info("Source SeriesUID:" + seriesID + " dsoSeriesUID:" +  dsoSeriesUID);
						}
					}
				}
				log.info("Saving AIM file with ID " + imageAnnotationColl.getUniqueIdentifier() + " projectID:" + projectID + " seriesUID:" + seriesID + " username:" + username);
				imageAnnotationColl = AIMUtil.saveImageAnnotationToServer(imageAnnotationColl, projectID, frameNumber, sessionId);
				String xml = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(imageAnnotationColl);
				if (xml == null || xml.trim().length() < 100)
					throw new Exception("Error converting ImageAnnotationCollection to String");
				log.info("Save annotation:" + imageAnnotationColl.getUniqueIdentifier().getRoot());
				if (imageAnnotationColl != null && projectID != null && username != null)
				{
					FrameReference frameReference = new FrameReference(projectID, patientID, studyID, seriesID, imageID, new Integer(frameNumber));
					EPADAIM eaim = epadDatabaseOperations.addAIM(username, frameReference, imageAnnotationColl.getUniqueIdentifier().getRoot(), xml, imageAnnotationColl.getImageAnnotation().getName().getValue(),isDicomSR);
					try {
						if (sec != null && sec.getSegmentationEntityList().size() > 0)
						{
							if (sec.getSegmentationEntityList().get(0).getXsiType().equals("DicomSegmentationEntity"))
							{
								DicomSegmentationEntity dse = (DicomSegmentationEntity) sec.getSegmentationEntityList().get(0);
								log.info("DSO RSUID:" + dse.getReferencedSopInstanceUid().getRoot() + " SUID:" + dse.getSopInstanceUid().getRoot());
								SeriesReference seriesReference = new SeriesReference(projectID, null, null, seriesID);
								List<EPADAIM> aims = epadDatabaseOperations.getAIMs(seriesReference);
								//ml aim for no imageref for dso
								if (dse!=null && seriesIds.size()==1) {
									
									dsoSeriesUID=dcm4CheeDatabaseOperations.getSeriesUIDForImage(dse.getSopInstanceUid().getRoot());
									if (dsoSeriesUID.equals("")) { //DSO is not in db yet
										//required info
//										seriesID,dsoInstanceUid,aimID,projectID		
										dsoAims.add(new String[]{seriesID,dse.getSopInstanceUid().getRoot(),eaim.aimID,projectID, String.valueOf(isDicomSR)});
									}
								}
//								updateDSOStartIndex(eaim, e.ds);
								//if (eaim != null && eaim.dsoSeriesUID == null && aims.size() > 1 && seriesIds.size() > 1) {
								//ml 
								if (eaim != null && eaim.dsoSeriesUID == null && aims.size() > 1 && !dsoSeriesUID.equals("")) {
									for (EPADAIM e: aims)
									{
										log.info("Checking, aimID:" + e.aimID + " dsoSeries:" + e.dsoSeriesUID + " this:" + dsoSeriesUID);
										if (!e.aimID.equals(eaim.aimID) && e.dsoSeriesUID != null && e.dsoSeriesUID.equals(dsoSeriesUID))
										{
											ImageReference imageReference = new ImageReference(projectID, e.subjectID, e.studyUID, e.seriesUID, e.imageUID);										
											log.info("Templates :" + eaim.template + " and :" + e.template);	
											log.info("Names :" + eaim.name + " and :" + e.name);	
											log.info("isDicomSR :" + eaim.isDicomSR + " and :" + e.isDicomSR);		
											if ((eaim.name!=null && e.name!=null) && (eaim.template==null && e.template==null)  && (eaim.name.equals(e.name) ||  eaim.name.equals(e.name.replace("ePAD DSO-", ""))) &&(e.isDicomSR==false && eaim.isDicomSR==false)){// if none of them are dicomsr
											
												epadDatabaseOperations.deleteAIM("admin", e.aimID);
												log.info("Deleting aim with name:" + e.name + " and aimID:" + e.aimID);
											}
											log.info("Updating dsoSeriesUID in aim database:" + e.dsoSeriesUID + " aimID:" + eaim.aimID);
											epadDatabaseOperations.addDSOAIM(username, imageReference, e.dsoSeriesUID, eaim.aimID);												
											if (eaim.dsoFrameNo == 0)
												epadDatabaseOperations.updateAIMDSOFrameNo(eaim.aimID, e.dsoFrameNo);												
//											break;
										}
									}
								}								
							}
						}
						
					} catch (Exception x) {
						log.warning("Error checking segmentation", x);
					}
				}
				return false;
            } 
        } catch (Exception e) {
        	try {
        		log.warning("Error saving annotation:" + EPADFileUtils.readFileAsString(aimFile), e);
        	} catch (Exception x) {
        		log.warning("Error saving annotation", e);
        	}
        	if (e.getMessage().toLowerCase().contains("validation")) {
        		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
        		projectOperations.createEventLog(username, projectID, null, null, null, null, null, "", "Check Aim4 Schema", EventMessageCodes.AIM_VALIDATION_ERROR, true);
        		EpadDatabaseOperations epadDatabaseOperations=EpadDatabase.getInstance().getEPADDatabaseOperations();
        		epadDatabaseOperations.insertEpadEvent(sessionId,
        				EventMessageCodes.AIM_VALIDATION_ERROR, 
        				"", "", "", "", "", "", 
        				"",
        				projectID,"","","", true);
        	}else
        		throw new edu.stanford.hakan.aim4api.base.AimException(e.getMessage());
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
			Map<String,String> searchValueByProject, String user) throws ParserConfigurationException, AimException, edu.stanford.hakan.aim4api.base.AimException
	{
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

	public static void queryAIMImageAnnotations(PrintWriter responseStream, String projectID, AIMSearchType aimSearchType,
			String searchValue, String user) throws ParserConfigurationException, AimException, edu.stanford.hakan.aim4api.base.AimException
	{
		queryAIMImageAnnotationsV4(responseStream, projectID, aimSearchType, searchValue, user);
	}

	public static void queryAIMImageAnnotations(PrintWriter responseStream, String projectID, AIMSearchType aimSearchType,
			String searchValue, String user, int startIndex, int count) throws ParserConfigurationException, AimException, edu.stanford.hakan.aim4api.base.AimException
	{
		queryAIMImageAnnotationsV4(responseStream, projectID, aimSearchType, searchValue, user);
	}

	//ml aimv3 is not used anymore
	public static EPADAIMList queryAIMImageAnnotationSummaries(EPADAIMList aims, String user, int index, int count, String sessionID) throws ParserConfigurationException, AimException
	{
//		Map<String, String> projectAimIDs = getUIDCsvList(sessionID, aims, user);
//		List<ImageAnnotation> annotations = new ArrayList<ImageAnnotation>();
//		for (String projectId: projectAimIDs.keySet())
//		{
//			annotations.addAll(AIMQueries.getAIMImageAnnotations(projectId, AIMSearchType.ANNOTATION_UID, projectAimIDs.get(projectId), user, index, count));
//		}
//		log.info("" + annotations.size() + " AIM file(s) found for user " + user);
//
//		Map<String, EPADAIM> aimMAP = new HashMap<String, EPADAIM>();
//		EPADAIMResultSet rs = aims.ResultSet;
//		for (EPADAIM aim: rs.Result)
//		{
//			aimMAP.put(aim.aimID, aim);
//		}
//		aims = new EPADAIMList();
//		long starttime = System.currentTimeMillis();
//		for (ImageAnnotation aim : annotations) {
//			Aim4 a = new Aim4(aim);
//			EPADAIM ea = aimMAP.get(aim.getUniqueIdentifier());
//			ea.name = aim.getName();
//			ea.template = aim.getCodeMeaning();
//			ea.templateType = aim.getCodeValue();
//			ea.date = aim.getDateTime();
//			ea.comment = a.getComment();
//			if (a.getFirstStudyDate() != null)
//				ea.studyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getFirstStudyDate());
//			ea.patientName = a.getPatientName();
//			ea.xml = null;
//			aims.addAIM(ea);
//		}
//		long parsetime = System.currentTimeMillis();
//		log.info("Time taken to parse annotations:" + (parsetime-starttime) + " msecs");
//		log.info("" + annotations.size() + " annotations returned to client");
//		return aims;
		log.warning("not supported use queryAIMImageAnnotationSummariesV4");
		return null;
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
					EPADConfig.aim4Namespace+ " " + EPADConfig.xsdFileV4);
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
	
	public static EPADAIMList filterPermittedImageAnnotations(EPADAIMList aims, String user, String sessionID) throws ParserConfigurationException, AimException
	{
		return filterPermittedImageAnnotations(aims, user, sessionID,null);
	}
	public static EPADAIMList filterPermittedImageAnnotations(EPADAIMList aims, String user, String sessionID, String projectUID) throws ParserConfigurationException, AimException
	{
		long starttime = System.currentTimeMillis();
		EPADAIMList aimsFromExist = new EPADAIMList();
		EPADAIMList aimsFromDB = new EPADAIMList();
		for (EPADAIM ea: aims.ResultSet.Result)
		{
			if (projectUID==null || projectUID.equals("") || ea.projectID.equals(projectUID)) {
				if (ea.xml == null || ea.xml.equals(""))
				{
					aimsFromExist.addAIM(ea);
				}
				else
				{
					aimsFromDB.addAIM(ea);
				}
			}
		}
		log.info("aimd from db:"+aimsFromDB.ResultSet.totalRecords);
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
					List<ImageAnnotationCollection> iacs = edu.stanford.hakan.aim4api.usage.AnnotationGetter.getImageAnnotationCollectionsFromString(ea.xml, null);
					ImageAnnotationCollection aim = iacs.get(0);
					Aim4 a = new Aim4(aim);
					ea.name = aim.getImageAnnotations().get(0).getName().getValue();
					//ea.template = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystem();// .getCode();
					//ml
					ea.template = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
					if (aim.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName()!=null)
						ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName().getValue();
					else
						ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystemName();
//					ea.date = aim.getDateTime();
					if (a.getDateTimeAsDate()!=null)
						ea.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getDateTimeAsDate());
					
					ea.comment = a.getComment();
					if (a.getFirstStudyDate() != null)
						ea.studyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getFirstStudyDate());
					ea.patientName = a.getPatientName();
					ea.xml = aim.getXMLString(); //the only different thing from summaries!!! very bad solution!!
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
				EPADAIM ea = aimMAP.get(aim.getUniqueIdentifier().getRoot());
				if (ea == null)  continue;
				if (aim.getImageAnnotations().get(0).getName() != null)
				{
					ea.name = aim.getImageAnnotations().get(0).getName().getValue();
				}
				if (aim.getImageAnnotations().get(0).getListTypeCode() != null && aim.getImageAnnotations().get(0).getListTypeCode().size() > 0)
				{
					//ml what is this?
					//it is like this just above 
//					ea.template = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName().getValue();
//					ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
					//ml
					ea.template = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
					if (aim.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName()!=null)
						ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName().getValue();
					else
						ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystemName();
				}
//				ea.date = aim.getDateTime();
				if (a.getDateTimeAsDate()!=null)
					ea.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getDateTimeAsDate());
				
				ea.comment = a.getComment();
				if (a.getFirstStudyDate() != null)
					ea.studyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getFirstStudyDate());
				ea.patientName = a.getPatientName();
				ea.xml = aim.getXMLString(); //the only different thing from summaries!!! very bad solution!!
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
					List<ImageAnnotationCollection> iacs = edu.stanford.hakan.aim4api.usage.AnnotationGetter.getImageAnnotationCollectionsFromString(ea.xml, null);
					ImageAnnotationCollection aim = iacs.get(0);
					Aim4 a = new Aim4(aim);
					ea.name = aim.getImageAnnotations().get(0).getName().getValue();
					//ea.template = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystem();// .getCode();
					//ml
					ea.template = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
					if (aim.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName()!=null)
						ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName().getValue();
					else
						ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystemName();
//					ea.date = aim.getDateTime();
					if (a.getDateTimeAsDate()!=null)
						ea.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getDateTimeAsDate());
					
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
				EPADAIM ea = aimMAP.get(aim.getUniqueIdentifier().getRoot());
				if (ea == null)  continue;
				if (aim.getImageAnnotations().get(0).getName() != null)
				{
					ea.name = aim.getImageAnnotations().get(0).getName().getValue();
				}
				if (aim.getImageAnnotations().get(0).getListTypeCode() != null && aim.getImageAnnotations().get(0).getListTypeCode().size() > 0)
				{
					//ml what is this?
					//it is like this just above 
//					ea.template = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName().getValue();
//					ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
					//ml
					ea.template = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
					if (aim.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName()!=null)
						ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName().getValue();
					else
						ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystemName();
				}
//				ea.date = aim.getDateTime();
				if (a.getDateTimeAsDate()!=null)
					ea.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getDateTimeAsDate());
				
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
						//ml
						ea.template = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
						if (iac.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName()!=null)
							ea.templateType = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName().getValue();
						else
							ea.templateType = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystemName();
//						ea.date = iac.getDateTime();
						if (a.getDateTimeAsDate()!=null)
							ea.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getDateTimeAsDate());
						
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
						//ml
						ea.template = imageCollection.getAsJsonObject("imageAnnotations").getAsJsonObject("ImageAnnotation").getAsJsonObject("typeCode").getAsJsonPrimitive("code").getAsString();
						ea.templateType = imageCollection.getAsJsonObject("imageAnnotations").getAsJsonObject("ImageAnnotation").getAsJsonObject("typeCode").getAsJsonPrimitive("codeSystemName").getAsString();
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
		List<ImageAnnotationCollection> iacs = AIMQueries.getAllVersions(aim);
		EPADAIMList aims = new EPADAIMList();
		for (int i = 0; i < iacs.size(); i++)
		{
			ImageAnnotationCollection iac = iacs.get(i);
			try
			{
				Aim4 a = new Aim4(iac);
				EPADAIM ea = new EPADAIM(iac.getUniqueIdentifier().getRoot(), aim.userName, 
						aim.projectID, aim.subjectID, aim.studyUID, aim.seriesUID, aim.imageUID, aim.instanceOrFrameNumber);
				ea.name = iac.getImageAnnotations().get(0).getName().getValue();
				//ml
				ea.template = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
				if (iac.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName()!=null)
					ea.templateType = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName().getValue();
				else
					ea.templateType = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystemName();
				
//				ea.date = iac.getDateTime();
				if (a.getDateTimeAsDate()!=null)
					ea.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getDateTimeAsDate());
				
				ea.comment = a.getComment();
				if (a.getFirstStudyDate() != null)
					ea.studyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getFirstStudyDate());
				ea.patientName = a.getPatientName();
				ea.xml = null;
				ea.user = a.getLoggedInUser().getLoginName().getValue();
				aims.addAIM(ea);
			} catch (Exception x) {
				log.warning("Error parsing ImageAnnotationCollection:" + iac, x);
			}
		}
		return aims;
	}

	public static EPADAIMList getPreviousVersionSummaries(EPADAIM aim) throws Exception
	{
		List<ImageAnnotationCollection> iacs = AIMQueries.getPreviousVersions(aim);
		EPADAIMList aims = new EPADAIMList();
		for (int i = 0; i < iacs.size(); i++)
		{
			ImageAnnotationCollection iac = iacs.get(i);
			try
			{
				Aim4 a = new Aim4(iac);
				EPADAIM ea = new EPADAIM(iac.getUniqueIdentifier().getRoot(), aim.userName, 
						aim.projectID, aim.subjectID, aim.studyUID, aim.seriesUID, aim.imageUID, aim.instanceOrFrameNumber);
				ea.name = iac.getImageAnnotations().get(0).getName().getValue();
				//ml
				ea.template = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
				if (iac.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName()!=null)
					ea.templateType = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName().getValue();
				else
					ea.templateType = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystemName();
//				ea.date = iac.getDateTime();
				if (a.getDateTimeAsDate()!=null)
					ea.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getDateTimeAsDate());
				
				ea.comment = a.getComment();
				if (a.getFirstStudyDate() != null)
					ea.studyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getFirstStudyDate());
				ea.patientName = a.getPatientName();
				ea.xml = null;
				ea.user = a.getLoggedInUser().getLoginName().getValue();
				aims.addAIM(ea);
			} catch (Exception x) {
				log.warning("Error parsing ImageAnnotationCollection:" + iac, x);
			}
		}
		return aims;
	}

	public static EPADAIMList getNextVersionSummaries(EPADAIM aim) throws Exception
	{
		List<ImageAnnotationCollection> iacs = AIMQueries.getNextVersions(aim);
		EPADAIMList aims = new EPADAIMList();
		for (int i = 0; i < iacs.size(); i++)
		{
			ImageAnnotationCollection iac = iacs.get(i);
			try
			{
				Aim4 a = new Aim4(iac);
				EPADAIM ea = new EPADAIM(iac.getUniqueIdentifier().getRoot(), aim.userName, 
						aim.projectID, aim.subjectID, aim.studyUID, aim.seriesUID, aim.imageUID, aim.instanceOrFrameNumber);
				ea.name = iac.getImageAnnotations().get(0).getName().getValue();
				//ml
				ea.template = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
				if (iac.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName()!=null)
					ea.templateType = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName().getValue();
				else
					ea.templateType = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystemName();
//				ea.date = iac.getDateTime();
				if (a.getDateTimeAsDate()!=null)
					ea.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getDateTimeAsDate());
				
				ea.comment = a.getComment();
				if (a.getFirstStudyDate() != null)
					ea.studyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getFirstStudyDate());
				ea.patientName = a.getPatientName();
				ea.xml = null;
				ea.user = a.getLoggedInUser().getLoginName().getValue();
				aims.addAIM(ea);
			} catch (Exception x) {
				log.warning("Error parsing ImageAnnotationCollection:" + iac, x);
			}
		}
		return aims;
	}

	public static void returnAllVersions(PrintWriter responseStream, EPADAIM aim) throws Exception
	{
		List<ImageAnnotationCollection> iacs = AIMQueries.getAllVersions(aim);
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
		List<ImageAnnotationCollection> iacs = AIMQueries.getPreviousVersions(aim);
		StringBuilder annotationsXML = new StringBuilder("<imageAnnotations>\n");

		for (ImageAnnotationCollection iac : iacs) {
			annotationsXML.append(edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(iac));
		}
		annotationsXML.append("</imageAnnotations>\n");
		responseStream.print(annotationsXML);
		return;
	}
	
	public static void undoLastAIM(EPADAIM aim) throws Exception {
		List<ImageAnnotationCollection> iacs = AIMQueries.getPreviousVersions(aim);
		if (iacs.size() == 0)
			throw new Exception("Undo List is empty");
		ImageAnnotationCollection iac = AIMQueries.makeCurrent(aim, iacs.get(0));
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		epadDatabaseOperations.updateAIMXml(aim.aimID, edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(iac));
	}
	
	public static void redoLastAIM(EPADAIM aim) throws Exception {
		List<ImageAnnotationCollection> iacs = AIMQueries.getNextVersions(aim);
		if (iacs.size() == 0)
			throw new Exception("Redo List is empty");
		ImageAnnotationCollection iac = AIMQueries.makeCurrent(aim, iacs.get(0));
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		epadDatabaseOperations.updateAIMXml(aim.aimID, edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(iac));
	}
	
	public static EPADAIMList queryDeletedAIMImageAnnotationSummaries(AIMSearchType searchType, String searchValue, String user) throws Exception
	{
		long starttime = System.currentTimeMillis();
		EPADAIMList returnAims = new EPADAIMList();
		List<ImageAnnotationCollection> iacs = AIMQueries.getDeletedAIMImageAnnotations(searchType, searchValue, user);			
		// Get other params
		for (int i = 0; i < iacs.size(); i++)
		{
			ImageAnnotationCollection iac = iacs.get(i);
			try
			{
				Aim4 a = new Aim4(iac);
				EPADAIM ea = new EPADAIM(iac.getUniqueIdentifier().getRoot(), a.getLoggedInUser().getLoginName().getValue(), "", a.getPatientID(), a.getFirstStudyID(), a.getFirstSeriesID(), a.getFirstImageID(), 0);
				ea.name = iac.getImageAnnotations().get(0).getName().getValue();
				//ml
				ea.template = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
				if (iac.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName()!=null)
					ea.templateType = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName().getValue();
				else
					ea.templateType = iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystemName();
//				ea.date = iac.getDateTime();
				if (a.getDateTimeAsDate()!=null)
					ea.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getDateTimeAsDate());
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
		
		long endtime = System.currentTimeMillis();
		log.info("" + returnAims.ResultSet.totalRecords + " annotation summaries returned to client, took:" + (endtime-starttime) + " msecs");
		return returnAims;
	}

	public static void queryDeletedAIMImageAnnotations(PrintWriter responseStream, AIMSearchType searchType, String searchValue, String user, String sessionID) throws Exception
	{
		long starttime = System.currentTimeMillis();
		String annotationsXML = queryDeletedAIMImageAnnotations(searchType, searchValue, user);
		responseStream.print(annotationsXML);
		long resptime = System.currentTimeMillis();
		log.info("Time to write resp:" + (resptime-starttime) + " msecs");
	}

	public static void queryDeletedAIMImageJsonAnnotation(PrintWriter responseStream, AIMSearchType searchType, String searchValue, String user, String sessionID) throws Exception
	{
		long starttime = System.currentTimeMillis();
		String annotationsXML = queryDeletedAIMImageAnnotations(searchType, searchValue, user);
		String jsonString =  XML.toJSONObject(annotationsXML).toString(0);;
        if (jsonString == null)
        	throw new Exception("Error converting to json");
		responseStream.print(jsonString);
		long resptime = System.currentTimeMillis();
		log.info("Time to write resp:" + (resptime-starttime) + " msecs");
	}
	
	public static String queryDeletedAIMImageAnnotations(AIMSearchType searchType, String searchValue, String user) throws Exception
	{
		long starttime = System.currentTimeMillis();
		StringBuilder aimsDBXml = new StringBuilder("<imageAnnotations>\n");
		List<ImageAnnotationCollection> iacs = AIMQueries.getDeletedAIMImageAnnotations(searchType, searchValue, user);			

		for (ImageAnnotationCollection aim : iacs) {
			aimsDBXml.append(edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(aim));
		}
		long xmltime = System.currentTimeMillis();
		aimsDBXml.append("</imageAnnotations>\n");
		log.info("Time taken create xml:" + (xmltime-starttime) + " msecs for " + iacs.size() +" annotations");
		return aimsDBXml.toString();
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

//	public static ImageAnnotation getImageAnnotationFromFile(File file, String xsdFilePath)	{
//		try {
//			return AnnotationGetter.getImageAnnotationFromFile(file.getAbsolutePath(), xsdFilePath);
//		} catch (AimException e) {
//			return null;
//		}
//	}

    public static ImageAnnotationCollection getImageAnnotationFromFileV4(File file, String xsdFilePath) {
        try {
	        String version = AnnotationValidator.getAimVersion(file.getAbsolutePath());
	        if ("".equals(version)) {
	            throw new AimException("This is not a AIM Annotation File.");
	        }
	        if (version.contains("4"))
				return edu.stanford.hakan.aim4api.usage.AnnotationGetter.getImageAnnotationCollectionFromFile(file.getAbsolutePath(), xsdFilePath);
	        else
	        	return edu.stanford.hakan.aim4api.usage.AnnotationGetter.getImageAnnotationCollectionFromFile(file.getAbsolutePath());
		} catch (Exception e) {
			log.warning("couldn't get image annotation from file ", e);
			return null;
		}
    }
    
    public static String runPlugIn(String[] aimIDs, String templateName, String projectID, String jsessionID) throws Exception
	{                        
		return runPlugIn(aimIDs, templateName, projectID, jsessionID,true);
	}
    
    //this actually gets template_id (codevalue) not templatename
	public static String runPlugIn(String[] aimIDs, String templateName, String projectID, String jsessionID, boolean inParallel) throws Exception
	{                        
		String result = "";
		
		//ml moved out of for. look once
		boolean templateHasBeenFound = false;
		String handlerName = null;
		String pluginName = null;

		List<String> list = PluginConfig.getInstance().getPluginTemplateList();
		log.info("plugin count: "+ list.size());
		for (int i = 0; i < list.size(); i++) {
			String templateNameFounded = list.get(i);
			log.info("found plugin: " + templateNameFounded + " name:" + PluginConfig.getInstance().getPluginNameList().get(i));
			if (templateNameFounded.equals(templateName)) {
				handlerName = PluginConfig.getInstance().getPluginHandlerList().get(i);
				pluginName = PluginConfig.getInstance().getPluginNameList().get(i);
				templateHasBeenFound = true;
			}
		}
		
		if (inParallel) { 
			for (String aimID:  aimIDs)
			{
				if (isPluginStillRunning(aimID))
				{
					result = result + "\n" + "Previous version of this AIM " + aimID + " is still being processed by the plugin";
					continue;
				}
				
	
				if (templateHasBeenFound && jsessionID != null) {
					log.info("Starting Plugin task for:" + pluginName);
					(new Thread(new PluginStartTask(jsessionID, pluginName, aimID, 0, projectID))).start();				
				}
			}
		} else { //send all aims at once
			(new Thread(new PluginStartTask(jsessionID, pluginName, aimIDs, 0, projectID))).start();	
		}
		return result;
	}

	public static String readPlugInData(EPADAIM aim, String templateName, String jsessionID) throws Exception
	{
		String fileName = EPADConfig.getEPADWebServerAnnotationsDir() + "plugins/" + aim.aimID + "_" + templateName + ".json";
		File dataFile = new File(fileName);
		if (!dataFile.exists())
		{
			fileName = EPADConfig.getEPADWebServerResourcesDir() + "plugins/" + aim.aimID + "_" + templateName + ".json";
			dataFile = new File(fileName);
			if (!dataFile.exists())
				throw new Exception("Plugin data file does not exist:" + fileName);
		}
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
	
	private static ImageAnnotationCollection saveImageAnnotationToServer(ImageAnnotation aim, String projectID) throws AimException,
	edu.stanford.hakan.aim4api.base.AimException
	{
	    ImageAnnotationCollection aim4 = null;
		String result = "";

		if (aim.getCodeValue() != null) { // First, write a backup file
			String tempXmlPath = baseAnnotationDir + "temp-" + aim.getUniqueIdentifier() + ".xml";
			String storeXmlPath = baseAnnotationDir + aim.getUniqueIdentifier() + ".xml";
			File tempFile = new File(tempXmlPath);
			File storeFile = new File(storeXmlPath);

			edu.stanford.hakan.aim4api.usage.AnnotationBuilder.saveToFile(aim.toAimV4(), tempXmlPath, xsdFilePathV4);
			result = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.getAimXMLsaveResult();

			log.info("Save AIM file:" + result);
			if (storeFile.exists()) {
				storeFile.delete();
			}
			tempFile.renameTo(storeFile);
		    String collectionName = eXistCollectionV4;
		    if (projectID != null && projectID.length() > 0)
		    	collectionName = collectionName + "/" + projectID;
		    aim4 = aim.toAimV4();
		    aim4 = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.saveToServer(aim4, eXistServerUrl, aim4Namespace,
					collectionName, xsdFilePathV4, eXistUsername, eXistPassword);
			try {
				saveAimToMongo(aim4, projectID);
			} catch (Exception e) {
				log.warning("Error saving aim to mongodb", e);
			}
			log.info("Save AIM to Exist:" + result);
		}
		return aim4;
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
					if (!isCollaborator || aim.userName.equalsIgnoreCase(username) || aim.userName.equalsIgnoreCase("shared"))
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
					if (!isCollaborator || aim.userName.equalsIgnoreCase(username) || aim.userName.equalsIgnoreCase("shared"))
						projectAIMs.add(aim);
				}
			}
			catch (Exception x) {
				log.warning("Error getting aim permissions", x);
			}
		}
		long endtime = System.currentTimeMillis();
		log.info("Time taken for checking AIM permissions:" + (endtime-starttime) + " msecs, in:" + aimlist.ResultSet.totalRecords + " out:" + projectAIMsMap.values().size() + " username:" + username);
		
		return projectAIMsMap;
	}
	
	public static String getReferencedImage(EPADAIM aim) throws Exception
	{
		String xml = aim.xml;
		if (xml == null)
			throw new Exception("XML data is null, aim:" + aim.aimID);
		int uidInd = xml.indexOf("sopInstanceUID");
		if (uidInd == -1)
			uidInd = xml.indexOf("imageReferenceUID");
		if (uidInd == -1)
			return null;
		int quoteIndex = xml.indexOf('"', uidInd);
		if (quoteIndex == -1)
			return null;
		int secondquote = xml.indexOf('"', quoteIndex);
		return xml.substring(quoteIndex, secondquote);
	}
	
//	public static int convertAllAim3() throws Exception {
//		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
//		List<EPADAIM> epadaims = epadDatabaseOperations.getAIMs(new ProjectReference(null));
//		return convertAim3(epadaims);
//	}
//	
//	public static void convertAim3(String aimID) throws Exception {
//		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
//		EPADAIM epadaim = epadDatabaseOperations.getAIM(aimID);
//		List<EPADAIM> epadaims = new ArrayList<EPADAIM>();
//		epadaims.add(epadaim);
//		convertAim3(epadaims);
//	}
//	
//	public static int convertAim3(List<EPADAIM> epadaims) throws Exception {
//		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
//		List<Map<String, String>> coordinationTerms = epadDatabaseOperations.getCoordinationData("%");
//		//log.info("coordinations:" + coordinationTerms);
//		int count = 0;
//		for (EPADAIM epadaim: epadaims)
//		{
//			log.info("Converting AIM3:" + epadaim.aimID + " in project " + epadaim.projectID);
//			try {
//				List<ImageAnnotation> aims = AIMQueries.getAIMImageAnnotations(epadaim.projectID, AIMSearchType.ANNOTATION_UID, epadaim.aimID, "admin", 1, 50000, true);
//				if (aims.size() > 0)
//				{
//					ImageAnnotation aim = aims.get(0);
//					String tempXmlPath = baseAnnotationDir + "temp-" + aim.getUniqueIdentifier() + ".xml";
//					File tempFile = new File(tempXmlPath);
//					AnnotationBuilder.saveToFile(aim, tempXmlPath, xsdFilePath);
//                    ImageAnnotationCollection iac = edu.stanford.hakan.aim4api.usage.AnnotationGetter.getImageAnnotationCollectionFromFile(tempXmlPath);
//                    edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation iaV3 = new  edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation(iac);
//                    log.info("Saving AIM4:" + epadaim.aimID + " in project " + epadaim.projectID);
//                    iac = iaV3.toAimV4(coordinationTerms);
//					iac = AIMUtil.saveImageAnnotationToServer(iac, epadaim.projectID, 0, null, false);
//					if (iac != null && epadaim.projectID != null)
//					{
//						epadDatabaseOperations.updateAIMXml(epadaim.aimID, edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(iac));
//					}
//					tempFile.delete();
//					count++;
//				}
//				else
//					log.warning("Error converting aim3:" + epadaim.aimID + ", not found");
//				
//			}
//			catch (Exception x) {
//				log.warning("Error converting aim3:" + epadaim.aimID, x);
//			}
//		}
//		return count;
//	}
	
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
				log.warning("Annotation " + aimID + " not found in ExistDB");
			//	epadDatabaseOperations.deleteAIM("admin", aimID);
		}
	}	
	
	public static void updateTableNameColumn(List<EPADAIM> aims)
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		for (EPADAIM aim: aims)
		{
			if (aim.xml == null || aim.xml.trim().length() == 0) continue;
			try {
				List<ImageAnnotationCollection> iacs = edu.stanford.hakan.aim4api.usage.AnnotationGetter.getImageAnnotationCollectionsFromString(aim.xml, null);
				if (iacs.size() == 0) continue;
				ImageAnnotationCollection iac = iacs.get(0);
				EPADAIM epadAim = epadDatabaseOperations.updateAIMName(iac.getUniqueIdentifier().getRoot(), iac.getImageAnnotation().getName().getValue());
			} catch (Exception e) {
				log.warning("Error updating AIM Table Name", e);
			}
		}
	}
	
	public static void updateTableTemplateColumn(List<EPADAIM> aims)
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		for (EPADAIM aim: aims)
		{
			if (aim.xml == null || aim.xml.trim().length() == 0) continue;
			try {
				List<ImageAnnotationCollection> iacs = edu.stanford.hakan.aim4api.usage.AnnotationGetter.getImageAnnotationCollectionsFromString(aim.xml, null);
				if (iacs.size() == 0) continue;
				ImageAnnotationCollection iac = iacs.get(0);
				log.info("aim:"+iac.getUniqueIdentifier().getRoot() +" templatecode:" +  iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCode());
				EPADAIM epadAim = epadDatabaseOperations.updateAIMTemplateCode(iac.getUniqueIdentifier().getRoot(), iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCode());
			} catch (Exception e) {
				log.warning("Error updating AIM Table TemplateCode", e);
			}
		}
	}
	
	public static int updateExistDBFromMysql(String aimID)
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		String criteria = "1 = 1";
		if (aimID != null && aimID.trim().length() > 0)
			criteria = "annotationUID like '" + aimID + "'";
		List<EPADAIM> aims = epadDatabaseOperations.getAIMsByQuery(criteria);
		int count = 0;
		for (EPADAIM aim: aims)
		{
			if (aim.xml == null || aim.xml.trim().length() == 0) continue;
			try {
				List<ImageAnnotationCollection> iacs = edu.stanford.hakan.aim4api.usage.AnnotationGetter.getImageAnnotationCollectionsFromString(aim.xml, null);
				if (iacs.size() == 0) continue;
				ImageAnnotationCollection iac = iacs.get(0);
			    String collectionName = eXistCollectionV4;
			    if (aim.projectID != null && aim.projectID.length() > 0)
			    	collectionName = collectionName + "/" + aim.projectID;
			    edu.stanford.hakan.aim4api.usage.AnnotationBuilder.saveToServer(iac, eXistServerUrl, aim4Namespace,
			    		collectionName, xsdFilePathV4, eXistUsername, eXistPassword);
			    count++;
			} catch (Exception e) {
				log.warning("Error updating Exist Database", e);
			}
		}
		return count;
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
		if (EPADConfig.exportURL!=null)
			ExportAimOperations.sendJsonToApi(aim.getUniqueIdentifier().getRoot(), aim, aimXML);
		
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
		"ISO_datatypes_Narrative.xsd",
		"AIM_v4.1_rv2_XML.xsd",
		"AIM_v4.2_rv2_XML.xsd"
	};
	
	static final String[] OVERWRITE_SCHEMA_FILES = {
			"AIM_v4_XMLStanford.xsd"
		};
	
	private static void copyFile(String schemaFile, boolean overwrite){
		File file = new File(EPADConfig.getEPADWebServerSchemaDir() + schemaFile);
		if (!file.exists() || overwrite) {
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
	public static void checkSchemaFiles()
	{
		for (String schemaFile: SCHEMA_FILES)
		{
			copyFile(schemaFile,false);
		}
		
		for (String schemaFile: OVERWRITE_SCHEMA_FILES)
		{
			copyFile(schemaFile,true);
		}
	}
	
	static final String[] TEMPLATE_FILES = {
		"ATS_Template.xml",
		"Closed_Shape_Only.xml",
		"Line_Only.xml",
		"LungMin.xml",
		"LungNoduleTemplate.xml",
		"MB_template.xml",
		"plugin-lesionseg.xml",
		"plugin-riesz.xml",
		"plugin-sampleplugin.xml",
		"plugin-tedseg.xml",
		"plugin_jjvec.xml",
		"RECIST.xml",
		"ROI-Only_Template.xml",
		"SEG-Only_Template.xml",
		"Shape_Only.xml",		
	};
	
	public static void checkTemplateFiles()
	{
		File dir = new File(EPADConfig.getEPADWebServerTemplatesDir());
		if (!dir.exists())
			dir.mkdirs();
		for (String templateFile: TEMPLATE_FILES)
		{
			File file = new File(EPADConfig.getEPADWebServerTemplatesDir() + templateFile);
			if (!file.exists()) {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = new AIMUtil().getClass().getClassLoader().getResourceAsStream("templates/" + templateFile);
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
	
	
	/****************************AIME Methods**********************************/
	public static void queryAIMImageAnnotationsV4AIME(PrintWriter responseStream, AIMSearchType aimSearchType,
			String searchValue, String user, String sessionID, boolean jsonFormat) throws Exception
	{
		List<ImageAnnotationCollection> iacs = null;
		if (aimSearchType!=null){
			iacs=AIMQueries.getAIMImageAnnotationsV4AIME(aimSearchType, searchValue, user);
			log.warning("get with searchtype "+ aimSearchType.getName() + " " +searchValue);
		}
		else{
			iacs=AIMQueries.getAIMImageAnnotationsV4AIME(AIMSearchType.ANNOTATION_UID, "all", user);
			log.warning("get all annotations");
		}
		if (aimSearchType!=AIMSearchType.JSON_QUERY) {
			if (iacs.size() == 0) return;
				StringBuilder xml = new StringBuilder("<imageAnnotations>\n");
				
				for (ImageAnnotationCollection iac: iacs)	{
					xml.append(iac.getXMLString());
				}
				xml.append("</imageAnnotations>\n");
			if (!jsonFormat) {
				responseStream.print(xml.toString());
				
			} else {
				responseStream.print(convertToJson(xml.toString()));
			}
		} else {
			log.info("JSON_QUERY not supported by AIME");
		}
	}

	public static void queryAIMImageAnnotationsV4AIME(PrintWriter responseStream, AIMSearchType aimSearchType,
			String searchValue, String user) throws ParserConfigurationException, edu.stanford.hakan.aim4api.base.AimException
	{
		List<ImageAnnotationCollection> aims = AIMQueries.getAIMImageAnnotationsV4AIME( aimSearchType, searchValue, user);
		log.info("" + aims.size() + " AIM4 file(s) found for user " + user);
		if (aims.size() == 0) return;
		returnImageAnnotationsXMLV4(responseStream, aims);
	}

	
	public static EPADAIMList queryAIMImageAnnotationSummariesV4AIME(String user, String sessionID) throws Exception
	{

		return queryAIMImageAnnotationSummariesV4AIME(AIMSearchType.ANNOTATION_UID, "all",user,sessionID);
	}
	
	public static EPADAIMList queryAIMImageAnnotationSummariesV4AIME(AIMSearchType searchType, String searchValue, String user, String sessionID) throws Exception
	{
		if (searchType!=null){
			return queryAIMImageAnnotationSummariesV4AIME(user,sessionID);
		}
		long starttime = System.currentTimeMillis();
		List<ImageAnnotationCollection> iacs = AIMQueries.getAIMImageAnnotationsV4AIME(searchType, searchValue,user);
		
		EPADAIMList aims = new EPADAIMList();
		for (ImageAnnotationCollection aim : iacs) {
			try {
				Aim4 a = new Aim4(aim);
				EPADAIM ea =null;
				try
				{
					ea = new EPADAIM(aim.getUniqueIdentifier().getRoot(), a.getLoggedInUser().getLoginName().getValue(), "", a.getPatientID(), a.getFirstStudyID(), a.getFirstSeriesID(), a.getFirstImageID(), 0);
					
				} catch (Exception x) {
					log.warning("Error parsing ImageAnnotationCollection:" + aim, x);
				}
				if (ea == null)  continue;
				if (aim.getImageAnnotations().get(0).getName() != null)
				{
					ea.name = aim.getImageAnnotations().get(0).getName().getValue();
				}
				if (aim.getImageAnnotations().get(0).getListTypeCode() != null && aim.getImageAnnotations().get(0).getListTypeCode().size() > 0)
				{
					//ml
					ea.template = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCode();
					if (aim.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName()!=null)
						ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getDisplayName().getValue();
					else
						ea.templateType = aim.getImageAnnotations().get(0).getListTypeCode().get(0).getCodeSystemName();
				}
//				ea.date = aim.getDateTime();
				if (a.getDateTimeAsDate()!=null)
					ea.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getDateTimeAsDate());
				
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
		aims.ResultSet.totalRecords = aims.ResultSet.Result.size();
		long endtime = System.currentTimeMillis();
		log.info("" + aims.ResultSet.totalRecords + " annotation summaries returned to client, took:" + (endtime-starttime));
		return aims;
	}

	public static void queryAIMImageAnnotationsV4AIME(PrintWriter responseStream, String user, String sessionID) throws ParserConfigurationException, edu.stanford.hakan.aim4api.base.AimException
	{
		long starttime = System.currentTimeMillis();
		String annotationsXML = queryAIMImageAnnotationsV4AIME(user, sessionID);
		responseStream.print(annotationsXML);
		long resptime = System.currentTimeMillis();
		log.info("Time to write resp:" + (resptime-starttime) + " msecs");
	}

	public static void queryAIMImageJsonAnnotationsAIME(PrintWriter responseStream, String user, String sessionID) throws Exception
	{
		long starttime = System.currentTimeMillis();
		String annotationsXML = queryAIMImageAnnotationsV4AIME(user, sessionID);
		String jsonString =  XML.toJSONObject(annotationsXML).toString(0);;
        if (jsonString == null)
        	throw new Exception("Error converting to json");
		responseStream.print(jsonString);
		long resptime = System.currentTimeMillis();
		log.info("Time to write resp:" + (resptime-starttime) + " msecs");
	}

	public static String queryAIMImageAnnotationsV4AIME(String user, String sessionID) throws ParserConfigurationException, edu.stanford.hakan.aim4api.base.AimException
	{
		long starttime = System.currentTimeMillis();
		List<ImageAnnotationCollection> iacs = AIMQueries.getAIMImageAnnotationsV4AIME(AIMSearchType.ANNOTATION_UID, "all",user);
		StringBuilder aimsDBXml = new StringBuilder("<imageAnnotations>\n");
		
		for (ImageAnnotationCollection aim : iacs) {
			aimsDBXml.append(edu.stanford.hakan.aim4api.usage.AnnotationBuilder.convertToString(aim));
		}
		long xmltime = System.currentTimeMillis();
		aimsDBXml.append("</imageAnnotations>\n");
		log.info("Time taken create xml:" + (xmltime-starttime) + " msecs for " + (iacs.size()) +" annotations");
		return aimsDBXml.toString();
	
	}

	/****************************AIME Methods***********************************/
	
	
	private static final String VERSION = "1.0";
	private static final String PRIVATE_DESIGNATOR = "private";
	
	/**
	 * epad-ws's addcalculation. it can be used for accessing the dynamic lexicon
	 * @param value
	 * @param shapeId
	 * @param units
	 * @param name
	 * @param code
	 * @return
	 */
	public static CalculationEntity addCalculation(String value, Integer shapeId, String units, String name, String code) {

		CalculationEntity cal =new CalculationEntity();
		cal.setUniqueIdentifier();
		CD calcCD= edu.stanford.hakan.aim4api.compability.aimv3.Lexicon.getInstance().get(code);
		String desc="";
		if (calcCD!=null || ((calcCD= edu.stanford.epad.common.util.Lexicon.getInstance().getLex(name))!=null)) {
			cal.addTypeCode(new CD(calcCD.getCode(),calcCD.getDisplayName().getValue(),calcCD.getCodeSystemName()));
			cal.setDescription(new ST(calcCD.getDisplayName().getValue()));
			desc=calcCD.getDisplayName().getValue();
		}else {

			cal.addTypeCode(new CD(name,name,PRIVATE_DESIGNATOR));
			cal.setDescription(new ST(name));
			desc=name;

		}
		//TODO Compact
		ExtendedCalculationResult calculationResult=new ExtendedCalculationResult();

		calculationResult.setType(Enumerations.CalculationResultIdentifier.Scalar);
		calculationResult.setUnitOfMeasure(new ST(units));
		if (units.equals(""))
			calculationResult.setDataType(new CD("99EPADD2","String","99EPAD"));
		else
			calculationResult.setDataType(new CD("C48870","Double","NCI"));

		// Create a CalculationData instance
		edu.stanford.hakan.aim4api.base.CalculationData calculationData = new edu.stanford.hakan.aim4api.base.CalculationData();
		calculationData.setValue(new ST(value));
		calculationData.addCoordinate(0, 0);

		// Create a Dimension instance
		edu.stanford.hakan.aim4api.base.Dimension dimension = new edu.stanford.hakan.aim4api.base.Dimension(0, 1, desc);

		// Add calculationData to calculationResult
		calculationResult.addCalculationData(calculationData);

		// Add dimension to calculationResult
		calculationResult.addDimension(dimension);

		//this should be rdf removing for now. do not have shape id. and do not see it in the recist aim.
		//                    // add the shape reference to the calculation
		//                    ReferencedGeometricShape reference = new ReferencedGeometricShape();
		//                    reference.setCagridId(0);
		//                    reference.setReferencedShapeIdentifier(shapeId);
		//                    calculation.addReferencedGeometricShape(reference);

		// Add calculationResult to calculation
		cal.addCalculationResult(calculationResult);

		//removing algorithm from standard calculations
//		Algorithm alg=new Algorithm();
//		alg.setName(new ST(desc));
//		alg.setVersion(new ST(VERSION));
//		ArrayList<CD> types=new ArrayList<>();
//		types.add(new CD("RID12780","Calculation","RadLex","3.2"));
//		alg.setType(types);
//		cal.setAlgorithm(alg);

		return cal;

	}
}
