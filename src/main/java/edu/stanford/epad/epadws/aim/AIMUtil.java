package edu.stanford.epad.epadws.aim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
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

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
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
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;
import edu.stanford.hakan.aim3api.base.AimException;
import edu.stanford.hakan.aim3api.base.DICOMImageReference;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;
import edu.stanford.hakan.aim3api.base.Person;
import edu.stanford.hakan.aim3api.base.Polyline;
import edu.stanford.hakan.aim3api.base.Segmentation;
import edu.stanford.hakan.aim3api.base.SegmentationCollection;
import edu.stanford.hakan.aim3api.base.TwoDimensionSpatialCoordinate;
import edu.stanford.hakan.aim3api.base.User;
import edu.stanford.hakan.aim3api.usage.AnnotationBuilder;
import edu.stanford.hakan.aim3api.usage.AnnotationGetter;

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

			log.info(result);
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

			log.info(result);

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
			log.warning("Error deleting AIM annotation " + aimID);
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
		String sopClassUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SOPClassUID);
		String studyUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.StudyInstanceUID);
		String seriesUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SeriesInstanceUID);
		String imageUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes, TagFromName.SOPInstanceUID);
		String referencedImageUID = Attribute.getSingleStringValueOrEmptyString(dsoDICOMAttributes,
				TagFromName.ReferencedSOPInstanceUID);
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		String referencedSeriesUID = dcm4CheeDatabaseOperations.getSeriesUIDForImage(referencedImageUID);

		if (referencedSeriesUID.length() != 0) { // Found corresponding series in dcm4chee
			String referencedStudyUID = studyUID; // Will be same study as DSO

			log.info("Generating AIM file for DSO series " + seriesUID + " for patient " + patientName);
			log.info("SOP Class UID=" + sopClassUID);
			log.info("DSO Study UID=" + studyUID);
			log.info("DSO Series UID=" + seriesUID);
			log.info("DSO Image UID=" + imageUID);
			log.info("Referenced SOP Instance UID=" + referencedImageUID);
			log.info("Referenced Series Instance UID=" + referencedSeriesUID);

			ImageAnnotation imageAnnotation = new ImageAnnotation(0, "", "2000-10-17T10:22:40", "segmentation", "SEG",
					"SEG Only", "", "", "");

			SegmentationCollection sc = new SegmentationCollection();
			sc.AddSegmentation(new Segmentation(0, imageUID, sopClassUID, referencedImageUID, 1));
			imageAnnotation.setSegmentationCollection(sc);

			DICOMImageReference originalDICOMImageReference = PluginAIMUtil.createDICOMImageReference(referencedStudyUID,
					referencedSeriesUID, referencedImageUID);
			imageAnnotation.addImageReference(originalDICOMImageReference);
			DICOMImageReference dsoDICOMImageReference = PluginAIMUtil.createDICOMImageReference(studyUID, seriesUID,
					imageUID);
			imageAnnotation.addImageReference(dsoDICOMImageReference);

			Polyline polyline = new Polyline();
			polyline.setCagridId(0);
			polyline.setIncludeFlag(false);
			polyline.setShapeIdentifier(0);
			polyline.addSpatialCoordinate(new TwoDimensionSpatialCoordinate(0, 0, "0", 0, 2693.0, 1821.0));
			polyline.addSpatialCoordinate(new TwoDimensionSpatialCoordinate(0, 1, "0", 0, 3236.0, 1821.0));
			polyline.addSpatialCoordinate(new TwoDimensionSpatialCoordinate(0, 2, "0", 0, 3236.0, 2412.0));
			polyline.addSpatialCoordinate(new TwoDimensionSpatialCoordinate(0, 3, "0", 0, 2693.0, 2412.0));
			polyline.setLineStyle("lineStyle");
			imageAnnotation.addGeometricShape(polyline);

			Person person = new Person();
			person.setSex("F"); // TODO
			person.setBirthDate("1965-02-12T00:00:00"); // TODO
			person.setId(patientID);
			person.setName(patientName);
			person.setCagridId(0);
			imageAnnotation.addPerson(person);

			// TODO Not general. See if we can generate AIM on GUI upload of DSO with correct user.
			setImageAnnotationUser(imageAnnotation, "admin");

			log.info("Saving AIM file for DSO " + imageUID + " in series " + seriesUID + " with ID "
					+ imageAnnotation.getUniqueIdentifier());
			try {
				saveImageAnnotationToServer(imageAnnotation);
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
		ServletFileUpload servletFileUpload = new ServletFileUpload();
		FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(httpRequest);
		int fileCount = 0;
		boolean saveError = false;

		while (fileItemIterator.hasNext()) {
			fileCount++;
			log.debug("Uploading annotation number " + fileCount);
			FileItemStream fileItemStream = fileItemIterator.next();
			String name = fileItemStream.getFieldName();
			InputStream inputStream = fileItemStream.openStream();
			// TODO Use File.createTempFile
			String tempXMLFileName = "temp-" + System.currentTimeMillis() + ".xml";
			File aimFile = new File(annotationsUploadDirPath + tempXMLFileName);
			FileOutputStream fos = null;
			try {
				int len;
				byte[] buffer = new byte[32768];
				fos = new FileOutputStream(aimFile);
				while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
					fos.write(buffer, 0, len);
				}
			} finally {
				IOUtils.closeQuietly(inputStream);
				IOUtils.closeQuietly(fos);
			}
			responseStream.print("added (" + fileCount + "): " + name);
			ImageAnnotation imageAnnotation = AIMUtil.getImageAnnotationFromFile(aimFile, xsdFilePath);
			if (imageAnnotation != null) {
				String jsessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
				AIMUtil.saveImageAnnotationToServer(imageAnnotation, jsessionID);
				responseStream.println("-- Add to AIM server: " + imageAnnotation.getUniqueIdentifier() + "<br>");
			} else {
				responseStream.println("-- Failed ! not added to AIM server<br>");
				saveError = true;
			}
		}
		return saveError;
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
		List<ImageAnnotation> aims = AIMQueries.getAIMImageAnnotations(aimSearchType, searchValue, user);
		log.info("" + aims.size() + " AIM file(s) found for user " + user);

		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element root = doc.createElement("imageAnnotations");
		doc.appendChild(root);

		for (ImageAnnotation aim : aims) {
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
		return AnnotationGetter.getImageAnnotationFromFile(file.getAbsolutePath(), xsdFilePath);
	}

	private static void setImageAnnotationUser(ImageAnnotation imageAnnotation, String username)
	{
		List<User> userList = new ArrayList<User>();
		User user = new User();
		user.setLoginName(username);
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

			log.info(result);
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
			log.info(result);
		}
		return result;
	}
}
