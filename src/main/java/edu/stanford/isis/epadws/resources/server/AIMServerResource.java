package edu.stanford.isis.epadws.resources.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.stanford.hakan.aim3api.base.AimException;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;
import edu.stanford.hakan.aim3api.usage.AnnotationBuilder;
import edu.stanford.hakan.aim3api.usage.AnnotationGetter;
import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.plugin.server.impl.PluginConfig;

/**
 * AIM resource. AIM files can be queries or uploaded.
 * 
 * @author martin
 */
public class AIMServerResource extends BaseServerResource
{
	private final String namespace = ProxyConfig.getInstance().getParam("namespace"); // TODO Constants for these names
	private final String serverUrl = ProxyConfig.getInstance().getParam("serverUrl");
	private final String username = ProxyConfig.getInstance().getParam("username");
	private final String password = ProxyConfig.getInstance().getParam("password");
	private final String baseAnnotationDir = ProxyConfig.getInstance().getParam("baseAnnotationDir");
	private final String xsdFile = ProxyConfig.getInstance().getParam("xsdFile");
	private final String xsdFilePath = ProxyConfig.getInstance().getParam("baseSchemaDir") + xsdFile;
	private final String collection = ProxyConfig.getInstance().getParam("collection");

	private static final String PARSER_ERROR_MESSAGE = "XML parser error: ";
	private static final String DOM_ERROR_MESSAGE = "DOM error: ";
	private static final String AIM_ERROR_MESSAGE = "AIM error: ";
	private static final String BAD_REQUEST_ERROR_MESSAGE = "Missing query";
	private static final String FAILED_TO_UPLOAD_MESSAGE = "Failed to upload AIM files to directory ";
	private static final String UPLOAD_ERROR_MESSAGE = "Upload error. Could JAR file be missing from start script? ";

	public AIMServerResource()
	{
		setNegotiated(false); // Disable content negotiation
	}

	@Override
	protected void doCatch(Throwable throwable)
	{
		log.warning("An exception was thrown in the AIM resource.", throwable);
	}

	@Get("xml")
	public String queryAIM()
	{
		String queryString = getQuery().getQueryString(CharacterSet.UTF_8);
		log.info("AimResourceHandler received GET method : " + queryString);

		setResponseHeader("Cache-Control", "no-cache");

		if (queryString != null) {
			ArrayList<ImageAnnotation> imageAnnotations = getAIMImageAnnotations(queryString);
			try {
				String queryResults = buildXMLDocument(imageAnnotations);
				setStatus(Status.SUCCESS_OK);
				return queryResults;
			} catch (ParserConfigurationException e) {
				setStatus(Status.SERVER_ERROR_INTERNAL);
				log.warning(PARSER_ERROR_MESSAGE, e);
				return PARSER_ERROR_MESSAGE + e.getMessage();
			} catch (DOMException e) {
				setStatus(Status.SERVER_ERROR_INTERNAL);
				log.warning(DOM_ERROR_MESSAGE, e);
				return DOM_ERROR_MESSAGE + e.getMessage();
			} catch (AimException e) {
				setStatus(Status.SERVER_ERROR_INTERNAL);
				log.warning(AIM_ERROR_MESSAGE, e);
				return AIM_ERROR_MESSAGE + e.getMessage();
			}
		} else {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			log.info(BAD_REQUEST_ERROR_MESSAGE);
			return BAD_REQUEST_ERROR_MESSAGE;
		}
	}

	@Post("xml")
	public String uploadAIM(Representation representation)
	{
		log.info("AIM resource received POST method");
		String filePath = "/home/epad/DicomProxy/resources/annotations/upload/"; // TODO This should come from config file

		try { // Create the directory for AIM file upload
			StringBuilder out = new StringBuilder();
			RestletFileUpload fileUpload = new RestletFileUpload();
			FileItemIterator fileIterator = fileUpload.getItemIterator(representation);
			uploadFiles(filePath, fileIterator, out);
			setStatus(Status.SUCCESS_OK);
			return out.toString();
		} catch (Exception e) {
			String errorMessage = FAILED_TO_UPLOAD_MESSAGE + filePath + ": " + e.getMessage();
			log.info(errorMessage);
			setStatus(Status.SERVER_ERROR_INTERNAL);
			return errorMessage;
		} catch (Error e) {
			String errorMessage = UPLOAD_ERROR_MESSAGE + e.getMessage();
			log.info(errorMessage);
			setStatus(Status.SERVER_ERROR_INTERNAL);
			return errorMessage;
		}
	}

	private void uploadFiles(String filePath, FileItemIterator fileIterator, StringBuilder out)
			throws FileUploadException, IOException, FileNotFoundException, AimException
	{
		log.info("Uploading files to directory: " + filePath);

		int fileCount = 0;
		while (fileIterator.hasNext()) {
			FileItemStream item = fileIterator.next();
			String name = item.getFieldName();
			InputStream stream = item.openStream();
			fileCount++;

			log.debug("Starting file #" + fileCount);
			log.debug("FieldName = " + name);

			String tempName = "temp-" + System.currentTimeMillis() + ".xml";
			File f = new File(filePath + tempName);
			FileOutputStream fos = new FileOutputStream(f);
			try {
				int len;
				byte[] buffer = new byte[32768];
				while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
					fos.write(buffer, 0, len);
				}
			} finally {
				fos.close();
			}
			out.append("Added (" + fileCount + "): " + name + "\n");

			// Transform it an AIM File
			ImageAnnotation ia = AnnotationGetter.getImageAnnotationFromFile(f.getAbsolutePath(), xsdFilePath);
			if (ia != null) {
				saveToServer(ia);
				out.append("-- Add to AIM server: " + ia.getUniqueIdentifier() + "<br>\n");
			} else {
				out.append("-- Failed ! not added to AIM server<br>\n");
			}
		}
	}

	private String buildXMLDocument(ArrayList<ImageAnnotation> aims) throws ParserConfigurationException, AimException
	{
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element root = doc.createElement("imageAnnotations");
		doc.appendChild(root);

		for (ImageAnnotation aim : aims) {
			Node node = aim.getXMLNode(docBuilder.newDocument());
			Node copyNode = doc.importNode(node, true);

			// Copy the node
			Element res = (Element)copyNode;
			res.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			res.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			res.setAttribute("xsi:schemaLocation",
					"gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM AIM_v3_rv11_XML.xsd");

			Node n = renameNodeNS(res, "ImageAnnotation");
			root.appendChild(n); // Add to the root
		}
		String queryResults = XmlDocumentToString(doc);
		return queryResults;
	}

	private ArrayList<ImageAnnotation> getAIMImageAnnotations(String queryString)
	{
		String[] queryStrings = queryString.trim().split("=");
		String id1 = null;
		String id2 = null;
		if (queryStrings.length == 2) {
			id1 = queryStrings[0];
			id2 = queryStrings[1];
		} else {
			if (queryStrings.length == 1) {
				id1 = queryStrings[0];
			}
		}
		ArrayList<ImageAnnotation> aims = getAIM(id1, id2); // Get the AIM files
		return aims;
	}

	/**
	 * Save the annotation to the server in the AIM database. An invalid annotation will not be saved. Save a file backup
	 * just in case.
	 * 
	 * @param ImageAnnotation
	 * @return String
	 * @throws AimException
	 */
	public String saveToServer(ImageAnnotation aim) throws AimException
	{
		String res = "";

		if (aim.getCodeValue() != null) { // For safety, write a backup file
			String tempXmlPath = this.baseAnnotationDir + "temp-" + aim.getUniqueIdentifier() + ".xml";
			String storeXmlPath = this.baseAnnotationDir + aim.getUniqueIdentifier() + ".xml";
			File tempFile = new File(tempXmlPath);
			File storeFile = new File(storeXmlPath);
			AnnotationBuilder.saveToFile(aim, tempXmlPath, xsdFilePath);
			res = AnnotationBuilder.getAimXMLsaveResult();
			log.info("AnnotationBuilder.saveToFile result: " + res);
			if (storeFile.exists()) {
				storeFile.delete();
			}
			tempFile.renameTo(storeFile);

			AnnotationBuilder.saveToServer(aim, serverUrl, namespace, collection, xsdFilePath, username, password);
			res = AnnotationBuilder.getAimXMLsaveResult();
			log.info("AnnotationBuilder.saveToServer result: " + res);

			// Check for plugin!!
			if (aim.getCodingSchemeDesignator().equals("epad-plugin")) {
				// find which template has been used to fill the aim file
				String templateName = aim.getCodeValue(); // ex: jjv-5

				log.info("Plugin detection : " + templateName);

				// Check if this template corresponds to a plugin
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
					log.info("Template Has Been Found, handler : " + handlerName);
					// Trigger the plugin
					// String nameAIM=aim.getUniqueIdentifier() + ".xml";

					String url = "http://localhost:8080/plugin/" + pluginName + "/?aimFile=" + aim.getUniqueIdentifier();
					// --Post to the plugin
					HttpClient client = new HttpClient();

					log.info("Triggering plugin at the following adress : " + url);
					GetMethod method = new GetMethod(url);
					// Execute the GET method
					try {
						client.executeMethod(method);
					} catch (HttpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return res;
	}

	/**
	 * Read the annotations from the aim database by patient name, patient id, series id, annotation id, or just get all
	 * of them on a GET. Can also delete by annotation id.
	 * 
	 * @return ArrayList<ImageAnnotation>
	 */
	public ArrayList<ImageAnnotation> getAIM(String id1, String id2)
	{
		ArrayList<ImageAnnotation> retAims = new ArrayList<ImageAnnotation>();
		List<ImageAnnotation> aims = null;
		ImageAnnotation aim = null;

		if (id1.equals("personName")) {
			String personName = id2;
			try {
				aims = AnnotationGetter.getImageAnnotationsFromServerByPersonNameEqual(serverUrl, namespace, collection,
						username, password, personName, xsdFilePath);
			} catch (AimException e) {
				log.warning("Exception on AnnotationGetter.getImageAnnotationsFromServerByPersonNameEqual " + personName, e);
			}
			if (aims != null) {
				retAims.addAll(aims);
			}
		} else if (id1.equals("patientId")) {
			String patientId = id2;
			try {
				aims = AnnotationGetter.getImageAnnotationsFromServerByPersonIdEqual(serverUrl, namespace, collection,
						username, password, patientId, xsdFilePath);
			} catch (AimException e) {
				log.warning("Exception on AnnotationGetter.getImageAnnotationsFromServerByPersonIdEqual " + patientId, e);
			}
			if (aims != null) {
				retAims.addAll(aims);
			}
		} else if (id1.equals("seriesUID")) {
			String seriesUID = id2;
			try {
				aims = AnnotationGetter.getImageAnnotationsFromServerByImageSeriesInstanceUIDEqual(serverUrl, namespace,
						collection, username, password, seriesUID, xsdFilePath);
			} catch (AimException e) {
				log.warning("Exception on AnnotationGetter.getImageAnnotationsFromServerByImageSeriesInstanceUIDEqual "
						+ seriesUID, e);
			}
			if (aims != null) {
				retAims.addAll(aims);
			}
		} else if (id1.equals("annotationUID")) {
			String annotationUID = id2;
			if (id2.equals("all")) {
				String query = "SELECT FROM " + collection + " WHERE (ImageAnnotation.cagridId like '0')";
				try {
					aims = AnnotationGetter.getImageAnnotationsFromServerWithAimQuery(serverUrl, namespace, username, password,
							query, xsdFilePath);
				} catch (AimException e) {
					log.warning("Exception on AnnotationGetter.getImageAnnotationsFromServerWithAimQuery ", e);
				}
				if (aims != null) {
					retAims.addAll(aims);
				}
			} else {
				try {
					aim = AnnotationGetter.getImageAnnotationFromServerByUniqueIdentifier(serverUrl, namespace, collection,
							username, password, annotationUID, xsdFilePath);
				} catch (AimException e) {
					log.warning("Exception on AnnotationGetter.getImageAnnotationFromServerByUniqueIdentifier " + annotationUID,
							e);
				}
				if (aim != null) {
					retAims.add(aim);
				}
			}
		} else if (id1.equals("deleteUID")) {
			String annotationUID = id2;
			log.info("calling performDelete with deleteUID on GET ");
			performDelete(annotationUID, collection, serverUrl);
			retAims = null;
		} else if (id1.equals("key")) {
			log.info("id1 is key id2 is " + id2);
		}
		log.info("Number of AIM files founded : " + retAims.size());

		return retAims;
	}

	// Create an XML document from a String
	public static String XmlDocumentToString(Document document)
	{

		// add the good namespace
		new XmlNamespaceTranslator().addTranslation(null, "gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM")
				.addTranslation("", "gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM").translateNamespaces(document);

		// set up a transformer
		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = null;
		;
		try {
			trans = transfac.newTransformer();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		trans.setOutputProperty(OutputKeys.INDENT, "yes");

		// create string from xml tree
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(document);

		try {
			trans.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sw.toString();
	}

	// Delete the document from the aim database.
	private String performDelete(String uid, String collection, String serverURL)
	{
		String result = "";

		log.info("performDelete on : " + uid);
		try {
			// AnnotationGetter.deleteImageAnnotationFromServer(serverUrl, namespace, collection, xsdFilePath,username,
			// password, uid);
			AnnotationGetter.removeImageAnnotationFromServer(serverUrl, namespace, collection, username, password, uid);

			log.info("after deletion on : " + uid);

		} catch (Exception ex) {
			result = "XML Deletion operation is Unsuccessful (Method Name; performDelete): " + ex.getLocalizedMessage();
			log.info("XML Deletion operation is Unsuccessful (Method Name; performDelete): " + ex.getLocalizedMessage());
		}
		log.info("AnnotationGetter.deleteImageAnnotationFromServer result: " + result);
		return result;
	}

	// rename namespace of the nodes
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
}
