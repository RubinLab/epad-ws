package edu.stanford.epad.epadws.aim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.TagFromName;

import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.plugins.PluginAIMUtil;
import edu.stanford.epad.common.plugins.PluginConfig;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.XmlNamespaceTranslator;
import edu.stanford.epad.dtos.internal.DICOMElement;
import edu.stanford.epad.dtos.internal.DICOMElementList;
import edu.stanford.epad.epadws.aim.aimapi.Aim;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.queries.XNATQueries;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;
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
	
	public static String saveImageAnnotationToServer(ImageAnnotation aim, String jsessionID) throws AimException,
				edu.stanford.hakan.aim4api.base.AimException
	{
		String result = "";

		if (aim.getCodeValue() != null) { // For safety, write a backup file
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
				edu.stanford.hakan.aim4api.usage.AnnotationBuilder.saveToServer(aim.toAimV4(), eXistServerUrl, aim4Namespace,
						eXistCollectionV4, xsdFilePathV4, eXistUsername, eXistPassword);
				result = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.getAimXMLsaveResult();
			}

			log.info("Save AIM to Exist:" + result);
			
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
					HttpClient client = new HttpClient(); // TODO Get rid of localhost
					String url = "http://localhost:8080/epad/plugin/" + pluginName + "/?aimFile=" + aim.getUniqueIdentifier();
					log.info("Triggering ePAD plugin at " + url + ", handler name " + handlerName);
					GetMethod method = new GetMethod(url);
					method.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);
					try {
						int statusCode = client.executeMethod(method);
						log.info("Status code returned from plugin " + statusCode);
					} catch (HttpException e) {
						log.warning("HTTP error calling plugin ", e);
					} catch (IOException e) {
						log.warning("IO exception calling plugin ", e);
					} finally {
						method.releaseConnection();
					}
				}
			}
		}
		return result;
	}

	public static boolean deleteAIM(String aimID)
	{
		try {
			if (useV4.equals("false"))
				AnnotationGetter.removeImageAnnotationFromServer(eXistServerUrl, aim3Namespace, eXistCollection, eXistUsername,
						eXistPassword, aimID, false);
			else
				edu.stanford.hakan.aim4api.database.exist.ExistManager.removeImageAnnotationCollectionFromServer(
						eXistServerUrl, aim4Namespace, eXistCollectionV4, eXistUsername, eXistPassword, aimID);

			return true;
		} catch (Exception ex) {
			log.warning("Error deleting AIM annotation " + aimID, ex);
			return false;
		}
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
	public static void generateAIMFileForDSO(File dsoFile) throws Exception
	{
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
		String seriesUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SeriesInstanceUID);
		String imageUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SOPInstanceUID);
		String description = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SeriesDescription);
		log.info("DSO Series description:" + description);
		// TODO: This call to get Referenced Image does not work ???
		String[] referencedImageUID = Attribute.getStringValues(dsoDICOMAttributes, TagFromName.ReferencedSOPInstanceUID);
//		Need to try this instead		
//		SequenceAttribute referencedSeriesSequence =(SequenceAttribute)dsoDICOMAttributes.get(TagFromName.ReferencedSeriesSequence);
//		if (referencedSeriesSequence != null) {
//		    Iterator sitems = referencedSeriesSequence.iterator();
//		    if (sitems.hasNext()) {
//		        SequenceItem sitem = (SequenceItem)sitems.next();
//		        if (sitem != null) {
//		            AttributeList list = sitem.getAttributeList();
//		            Attribute a = list.get(TagFromName.ReferencedSOPClassUID);
//        			Attribute b = list.get(TagFromName.ReferencedSOPInstanceUID);
//        			Attribute c = list.get(TagFromName.ReferencedFrameNumber);
//		        }
//		    }
//		}
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		if (referencedImageUID == null || referencedImageUID.length == 0)
		{
			referencedImageUID = new String[1];
			DICOMElementList dicomElementList = Dcm4CheeQueries.getDICOMElementsFromWADO(studyUID, seriesUID, imageUID);
			for (DICOMElement dicomElement : dicomElementList.ResultSet.Result) {
				if (dicomElement.tagCode.equals(PixelMedUtils.ReferencedSOPInstanceUIDCode))
				{
					referencedImageUID[0] = dicomElement.value;
				}
			}
			if (referencedImageUID[0] == null)
				throw new Exception("DSO Referenced Image UID not found: " + referencedImageUID);
		}
		String referencedSeriesUID = dcm4CheeDatabaseOperations.getSeriesUIDForImage(referencedImageUID[0]);

		if (referencedSeriesUID.length() != 0) { // Found corresponding series in dcm4chee
			String referencedStudyUID = studyUID; // Will be same study as DSO

			log.info("Generating AIM file for DSO series " + seriesUID + " for patient " + patientName);
			log.info("SOP Class UID=" + sopClassUID);
			log.info("DSO Study UID=" + studyUID);
			log.info("DSO Series UID=" + seriesUID);
			log.info("DSO Image UID=" + imageUID);
			log.info("Referenced SOP Instance UID=" + referencedImageUID);
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
			setImageAnnotationUser(imageAnnotation, "shared");

			log.info("Saving AIM file for DSO " + imageUID + " in series " + seriesUID + " with ID "
					+ imageAnnotation.getUniqueIdentifier());
			try {
				String result = saveImageAnnotationToServer(imageAnnotation);
				if (result.toLowerCase().contains("success"))
				{
		    		String adminSessionID = XNATSessionOperations.getXNATAdminSessionID();
					String projectID = XNATQueries.getFirstProjectForStudy(adminSessionID, referencedStudyUID);
					EpadOperations epadOperations = DefaultEpadOperations.getInstance();
					ImageReference imageReference = new ImageReference(projectID, patientID, referencedStudyUID, referencedSeriesUID, referencedImageUID[0]);
					epadOperations.createImageAIM("shared", imageReference, imageAnnotation.getUniqueIdentifier(), null, "");
				}
			} catch (AimException e) {
				log.warning("Exception saving AIM file for DSO image " + imageUID + " in series " + seriesUID, e);
			}
		} else {
			log.warning("DSO " + imageUID + " in series " + seriesUID + " with no corresponding DICOM image");
		}

		/*
		 * ServerEventUtil.postEvent(username, "DSOReady", imageAnnotation.getUniqueIdentifier(), aimName, patientID,
		 * patientName, "", "", "");
		 */
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
					String jsessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
					log.info("Saving AIM file with ID " + imageAnnotation.getUniqueIdentifier() + " username:" + username);
					String result = AIMUtil.saveImageAnnotationToServer(imageAnnotation, jsessionID);
					if (result.toLowerCase().contains("success") && projectID != null && username != null)
					{
						EpadOperations epadOperations = DefaultEpadOperations.getInstance();
						FrameReference frameReference = new FrameReference(projectID, patientID, studyID, seriesID, imageID, new Integer(frameNo));
						epadOperations.createFrameAIM(username, frameReference, imageAnnotation.getUniqueIdentifier(), null, jsessionID);
					}
					responseStream.println("-- Add to AIM server: " + imageAnnotation.getUniqueIdentifier() + "<br>");
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
	 * @param sessionId
	 * @param username
	 * @return true if error in AIM save
	 * @throws AimException
	 * @throws edu.stanford.hakan.aim4api.base.AimException
	 */
	public static boolean saveAIMAnnotation(File aimFile, String projectID, String sessionId, String username) throws AimException,
			edu.stanford.hakan.aim4api.base.AimException
	{
		if (aimFile == null)
			return true;
		ImageAnnotation imageAnnotation = AIMUtil.getImageAnnotationFromFile(aimFile, xsdFilePath);
		if (imageAnnotation != null) {
			String patientID = imageAnnotation.getListPerson().get(0).getId();
			Aim aim = new Aim(imageAnnotation);
			String imageID = aim.getFirstImageID();
			String seriesID = aim.getSeriesID(imageID);
			String studyID = aim.getStudyID(seriesID);
			log.info("Saving AIM file with ID " + imageAnnotation.getUniqueIdentifier() + " username:" + username);
			String result = AIMUtil.saveImageAnnotationToServer(imageAnnotation, sessionId);
			log.info("Save annotation:" + result);
			if (result.toLowerCase().contains("success") && projectID != null && username != null)
			{
				EpadOperations epadOperations = DefaultEpadOperations.getInstance();
				FrameReference frameReference = new FrameReference(projectID, patientID, studyID, seriesID, imageID, new Integer(0));
				epadOperations.createFrameAIM(username, frameReference, imageAnnotation.getUniqueIdentifier(), null, sessionId);
			}
			return false;
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
			String searchValue, String user) throws ParserConfigurationException, AimException
	{
		queryAIMImageAnnotations(responseStream, null, aimSearchType, searchValue, user, 1, 5000);
	}

	public static void queryAIMImageAnnotations(PrintWriter responseStream, AIMSearchType aimSearchType,
			String searchValue, String user, int index, int count) throws ParserConfigurationException, AimException
	{
		queryAIMImageAnnotations(responseStream, null, aimSearchType, searchValue, user, index, count);
	}

	public static void queryAIMImageAnnotations(PrintWriter responseStream, String projectID, AIMSearchType aimSearchType,
			String searchValue, String user, int index, int count) throws ParserConfigurationException, AimException
		{
		
		List<ImageAnnotation> aims = AIMQueries.getAIMImageAnnotations(aimSearchType, searchValue, user, index, count);
//		Set<String> aimIds = null;
//		if (projectID != null && projectID.trim().length() > 0)
//		{
//			List<EPADAIM> aimRecords = EpadDatabase.getInstance().getEPADDatabaseOperations().getAIMs(projectID, aimSearchType, searchValue, index, count);
//			aimIds = new HashSet<String>();
//			for (EPADAIM aimRec: aimRecords)
//			{
//				aimIds.add(aimRec.aimID);
//			}
//		}
//		if (aimIds != null)
//		{
//			for (int i = 0; i < aims.size(); i++)
//			{
//				if (!aimIds.contains(aims.get(i).getUniqueIdentifier()))
//				{
//					aims.remove(i);
//					i--;
//				}
//			}
//		}
		log.info("" + aims.size() + " AIM file(s) found for user " + user);

		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element root = doc.createElement("imageAnnotations");
		doc.appendChild(root);

		for (ImageAnnotation aim : aims) {
			Aim a = new Aim(aim);
			log.info("Aim name:" + aim.getName() + " Owner:" + aim.getListUser().get(0).getLoginName() + " Series:" + a.getFirstSeriesID());
			Node node = aim.getXMLNode(docBuilder.newDocument());
			Node copyNode = doc.importNode(node, true);
			Element res = (Element)copyNode; // Copy the node
			res.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			res.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			res.setAttribute("xsi:schemaLocation",
					"gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM AIM_v3_rv11_XML.xsd");
			Node n = renameNodeNS(res, "ImageAnnotation");
			root.appendChild(n); // Adding to the root
		}
		String queryResults = XmlDocumentToString(doc);
		responseStream.print(queryResults);
	}

	public static void queryAIMImageAnnotationsV4(PrintWriter responseStream, AIMSearchType aimSearchType,
			String searchValue, String user) throws ParserConfigurationException, edu.stanford.hakan.aim4api.base.AimException
	{
		List<ImageAnnotationCollection> aims = AIMQueries.getAIMImageAnnotationsV4(aimSearchType, searchValue, user);
		log.info("" + aims.size() + " AIM file(s) found for user " + user);

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
					"gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM AIM_v3_rv11_XML.xsd");
			Node n = renameNodeNS(res, "ImageAnnotationCollection");
			root.appendChild(n); // Adding to the root
		}
		String queryResults = XmlDocumentToString(doc);
		responseStream.print(queryResults);
	}

	private static String XmlDocumentToString(Document document)
	{ // Create an XML document from a String
		new XmlNamespaceTranslator().addTranslation(null, "gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM")
				.addTranslation("", "gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM").translateNamespaces(document);

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
	private static Node renameNodeNS(Node node, String newName)
	{
		Element newNode = node.getOwnerDocument().createElementNS("gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM",
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

	public static ImageAnnotation getImageAnnotationFromFile(File file, String xsdFilePath) throws AimException,
			edu.stanford.hakan.aim4api.base.AimException
	{
		ImageAnnotation ia = AnnotationGetter.getImageAnnotationFromFile(file.getAbsolutePath(), xsdFilePath);
		log.info("Annotation:" + file.getAbsolutePath() + " PatientId:" + ia.getListPerson().get(0).getId());
		return ia;
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

	private static String saveImageAnnotationToServer(ImageAnnotation aim) throws AimException,
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
				edu.stanford.hakan.aim4api.usage.AnnotationBuilder.saveToServer(aim.toAimV4(), eXistServerUrl, aim4Namespace,
						eXistCollectionV4, xsdFilePathV4, eXistUsername, eXistPassword);
				result = edu.stanford.hakan.aim4api.usage.AnnotationBuilder.getAimXMLsaveResult();
			}
			log.info("Save AIM to Exist:" + result);
		}
		return result;
	}
}
