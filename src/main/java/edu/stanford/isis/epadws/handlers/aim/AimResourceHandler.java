package edu.stanford.isis.epadws.handlers.aim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.restlet.resource.Get;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.stanford.hakan.aim3api.base.AimException;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;
import edu.stanford.hakan.aim3api.usage.AnnotationBuilder;
import edu.stanford.hakan.aim3api.usage.AnnotationGetter;
import edu.stanford.isis.epad.common.plugins.PluginConfig;
import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.EPADResources;
import edu.stanford.isis.epad.common.util.XmlNamespaceTranslator;
import edu.stanford.isis.epadws.xnat.XNATUtil;

public class AimResourceHandler extends AbstractHandler
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	public String serverProxy = EPADConfig.getInstance().getParam("serverProxy");
	public String namespace = EPADConfig.getInstance().getParam("namespace");
	public String serverUrl = EPADConfig.getInstance().getParam("serverUrl");
	public String username = EPADConfig.getInstance().getParam("username");
	public String password = EPADConfig.getInstance().getParam("password");
	public String baseAnnotationDir = EPADConfig.getInstance().getParam("baseAnnotationDir");
	public String xsdFile = EPADConfig.getInstance().getParam("xsdFile");
	public String xsdFilePath = EPADConfig.getInstance().getParam("baseSchemaDir") + xsdFile;
	public String collection = EPADConfig.getInstance().getParam("collection");
	public String dbpath = EPADConfig.getInstance().getParam("dbpath");
	public String templatePath = EPADConfig.getInstance().getParam("baseTemplatesDir");
	public String wadoProxy = EPADConfig.getInstance().getParam("wadoProxy");

	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error";
	private static final String INVALID_METHOD_MESSAGE = "Only POST and GET methods valid for this route";
	private static final String FILE_UPLOAD_ERROR_MESSAGE = "File upload failures; see response for details";
	private static final String MISSING_QUERY_MESSAGE = "No series or study query in AIM request";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";

	/**
	 * To test the post try:
	 * 
	 * <pre>
	 * curl --form upload=@/home/kurtz/Bureau/AIM_83ga0zjofj3y8ncm8wb1k3mlitis1glyugamx0zl.xml
	 * http://epad-prod1.stanford.edu:8080/aimresource/
	 * </pre>
	 */
	@Override
	public void handle(String base, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/xml");
		httpResponse.setHeader("Cache-Control", "no-cache");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
				String method = httpRequest.getMethod();
				if ("GET".equalsIgnoreCase(method)) {
					String queryString = httpRequest.getQueryString();
					queryString = URLDecoder.decode(queryString, "UTF-8");
					logger.info("AimResourceHandler received query: " + queryString);
					if (queryString != null) {
						queryAIMImageAnnotations(responseStream, queryString);
						statusCode = HttpServletResponse.SC_OK;
					} else {
						logger.info(MISSING_QUERY_MESSAGE);
						responseStream.append(MISSING_QUERY_MESSAGE);
						statusCode = HttpServletResponse.SC_BAD_REQUEST;
					}
				} else if ("POST".equalsIgnoreCase(method)) { // http://www.tutorialspoint.com/servlets/servlets-file-uploading.htm
					String annotationsUploadDirPath = EPADResources.getEPADWebServerAnnotationsUploadDir();
					logger.info("Uploading annotations to directory " + annotationsUploadDirPath);
					try {
						boolean saveError = uploadAIMAnnotations(httpRequest, responseStream, annotationsUploadDirPath);
						if (saveError) {
							logger.warning(FILE_UPLOAD_ERROR_MESSAGE);
							responseStream.append(FILE_UPLOAD_ERROR_MESSAGE + "<br>");
							statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
						} else {
							statusCode = HttpServletResponse.SC_OK;
						}
					} catch (Throwable t) {
						logger.warning("Failed to upload AIM files to directory" + annotationsUploadDirPath, t);
						responseStream.append("Failed to upload AIM files to directory " + annotationsUploadDirPath + "; error="
								+ t.getMessage());
						statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
					}
				} else {
					logger.warning(INVALID_METHOD_MESSAGE);
					responseStream.append(INVALID_METHOD_MESSAGE);
					httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET");
					statusCode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
				}
			} else {
				logger.warning(INVALID_SESSION_TOKEN_MESSAGE);
				responseStream.append(INVALID_SESSION_TOKEN_MESSAGE);
				statusCode = HttpServletResponse.SC_UNAUTHORIZED;
			}
		} catch (Throwable t) {
			logger.warning(INTERNAL_EXCEPTION_MESSAGE, t);
			if (responseStream != null)
				responseStream.append(INTERNAL_EXCEPTION_MESSAGE + t.getMessage() + "<br>");
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		httpResponse.setStatus(statusCode);
	}

	private void queryAIMImageAnnotations(PrintWriter out, String queryString) throws ParserConfigurationException,
			AimException
	{
		queryString = queryString.trim();
		String[] queryStrings = queryString.split("&");
		String id1 = null;
		String id2 = null;
		String user = null;
		if (queryStrings.length == 2) {
			String[] patientIDString = queryStrings[0].split("=");
			String[] userString = queryStrings[1].split("=");
			id1 = patientIDString[0];
			id2 = patientIDString[1];
			user = userString[1];
		}
		ArrayList<ImageAnnotation> aims = getAIMImageAnnotations(id1, id2, user);
		logger.info("AimResourceHandler, number of AIM files found: " + aims.size());

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
		out.print(queryResults);
	}

	private boolean uploadAIMAnnotations(HttpServletRequest httpRequest, PrintWriter responseStream,
			String annotationsUploadDirPath) throws FileUploadException, IOException, FileNotFoundException, AimException
	{
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(httpRequest);
		int fileCount = 0;
		boolean saveError = false;

		while (iter.hasNext()) {
			fileCount++;
			logger.debug("Uploading annotation number " + fileCount);
			FileItemStream item = iter.next();
			String name = item.getFieldName();
			// logger.debug("FieldName = " + name);
			InputStream stream = item.openStream();
			String tempName = "temp-" + System.currentTimeMillis() + ".xml";
			File f = new File(annotationsUploadDirPath + tempName);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(f);
				int len;
				byte[] buffer = new byte[32768];
				while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
					fos.write(buffer, 0, len);
				}
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						logger.warning("Error closing AIM upload stream", e);
					}
				}
			}
			responseStream.print("added (" + fileCount + "): " + name);
			ImageAnnotation ia = AnnotationGetter.getImageAnnotationFromFile(f.getAbsolutePath(), xsdFilePath);
			if (ia != null) {
				String jsessionID = XNATUtil.getJSessionIDFromRequest(httpRequest);
				saveImageAnnotationToServer(ia, jsessionID);
				responseStream.println("-- Add to AIM server: " + ia.getUniqueIdentifier() + "<br>");
			} else {
				responseStream.println("-- Failed ! not added to AIM server<br>");
				saveError = true;
			}
		}
		return saveError;
	}

	/**
	 * Read the annotations from the aim database by patient name, patient id, series id, annotation id, or just get all
	 * of them on a GET. Can also delete by annotation id.
	 * 
	 * @return ArrayList<ImageAnnotation>
	 */
	@Get
	public ArrayList<ImageAnnotation> getAIMImageAnnotations(String id1, String id2, String user)
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
				logger.warning("Exception on AnnotationGetter.getImageAnnotationsFromServerByPersonNameEqual " + personName, e);
			}
			if (aims != null) {
				retAims.addAll(aims);
			}
		} else if (id1.equals("patientId")) {
			String patientId = id2;
			try {
				/*
				 * aims = AnnotationGetter.getImageAnnotationsFromServerByPersonIdEqual(serverUrl, namespace, collection,
				 * username, password, patientId, xsdFilePath);
				 */
				aims = AnnotationGetter.getImageAnnotationsFromServerByPersonIDAndUserNameEqual(serverUrl, namespace,
						collection, username, password, patientId, user, xsdFilePath);
			} catch (AimException e) {
				logger.warning("Exception on AnnotationGetter.getImageAnnotationsFromServerByPersonIdEqual " + patientId, e);
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
				logger.warning("Exception on AnnotationGetter.getImageAnnotationsFromServerByImageSeriesInstanceUIDEqual "
						+ seriesUID, e);
			}
			if (aims != null) {
				retAims.addAll(aims);
			}
		} else if (id1.equals("annotationUID")) {
			String annotationUID = id2;
			if (id2.equals("all")) {

				// String query = "SELECT FROM " + collection + " WHERE (ImageAnnotation.cagridId like '0')";
				try {
					aims = AnnotationGetter.getImageAnnotationsFromServerByUserLoginNameContains(serverUrl, namespace,
							collection, username, password, user);
					/*
					 * aims = AnnotationGetter.getImageAnnotationsFromServerWithAimQuery(serverUrl, namespace, username, password,
					 * query, xsdFilePath);
					 */
				} catch (AimException e) {
					logger.warning("Exception on AnnotationGetter.getImageAnnotationsFromServerWithAimQuery ", e);
				}
				if (aims != null) {
					retAims.addAll(aims);
				}
			} else {
				try {
					aim = AnnotationGetter.getImageAnnotationFromServerByUniqueIdentifier(serverUrl, namespace, collection,
							username, password, annotationUID, xsdFilePath);
				} catch (AimException e) {
					logger.warning("Exception on AnnotationGetter.getImageAnnotationFromServerByUniqueIdentifier "
							+ annotationUID, e);
				}
				if (aim != null) {
					retAims.add(aim);
				}
			}
		} else if (id1.equals("deleteUID")) {
			String annotationUID = id2;
			logger.info("calling performDelete with deleteUID on GET ");
			performDelete(annotationUID, collection, serverUrl);
			retAims = null;
		} else if (id1.equals("key")) {
			logger.info("id1 is key id2 is " + id2);
		}
		return retAims;
	}

	/**
	 * Save the annotation to the server in the AIM database. An invalid annotation will not be saved. Save a file backup
	 * just in case.
	 * 
	 * @param ImageAnnotation
	 * @return String
	 * @throws AimException
	 */
	public String saveImageAnnotationToServer(ImageAnnotation aim, String jsessionID) throws AimException
	{
		String result = "";

		if (aim.getCodeValue() != null) { // For safety, write a backup file
			String tempXmlPath = this.baseAnnotationDir + "temp-" + aim.getUniqueIdentifier() + ".xml";
			String storeXmlPath = this.baseAnnotationDir + aim.getUniqueIdentifier() + ".xml";
			File tempFile = new File(tempXmlPath);
			File storeFile = new File(storeXmlPath);
			AnnotationBuilder.saveToFile(aim, tempXmlPath, xsdFilePath);

			logger.info("Saving AIM file with ID " + aim.getUniqueIdentifier());

			result = AnnotationBuilder.getAimXMLsaveResult();

			logger.info("AnnotationBuilder.saveToFile result: " + result);
			if (storeFile.exists()) {
				storeFile.delete();
			}
			tempFile.renameTo(storeFile);

			AnnotationBuilder.saveToServer(aim, serverUrl, namespace, collection, xsdFilePath, username, password);
			result = AnnotationBuilder.getAimXMLsaveResult();
			logger.info("AnnotationBuilder.saveToServer result: " + result);

			if (aim.getCodingSchemeDesignator().equals("epad-plugin")) { // Which template has been used to fill the AIM file
				String templateName = aim.getCodeValue(); // ex: jjv-5
				logger.info("Found an AIM plugin template with name " + templateName + " and AIM ID "
						+ aim.getUniqueIdentifier());
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
					HttpClient client = new HttpClient();
					String url = "http://localhost:8080/plugin/" + pluginName + "/?aimFile=" + aim.getUniqueIdentifier();
					logger.info("Triggering ePAD plugin at " + url + ", handler name " + handlerName);
					GetMethod method = new GetMethod(url);
					method.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);
					try {
						int statusCode = client.executeMethod(method);
						logger.info("Status code returned from plugin " + statusCode);
					} catch (HttpException e) {
						logger.warning("HTTP error calling plugin ", e);
					} catch (IOException e) {
						logger.warning("IO exception calling plugin ", e);
					}
				}
			}
		}
		return result;
	}

	// Delete the document from the aim database.
	private String performDelete(String uid, String collection, String serverURL)
	{
		String result = "";

		logger.info("performDelete on : " + uid);
		try {
			// AnnotationGetter.deleteImageAnnotationFromServer(serverUrl, namespace, collection, xsdFilePath,username,
			// password, uid);
			AnnotationGetter.removeImageAnnotationFromServer(serverUrl, namespace, collection, username, password, uid);

			logger.info("after deletion on : " + uid);

		} catch (Exception ex) {
			result = "XML Deletion operation is Unsuccessful (Method Name; performDelete): " + ex.getLocalizedMessage();
			logger.info("XML Deletion operation is Unsuccessful (Method Name; performDelete): " + ex.getLocalizedMessage());
		}
		logger.info("AnnotationGetter.deleteImageAnnotationFromServer result: " + result);
		return result;
	}

	// Create an xml document from a String
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
