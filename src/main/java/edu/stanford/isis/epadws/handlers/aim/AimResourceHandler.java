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

import javax.servlet.ServletException;
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
import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.util.JsonHelper;
import edu.stanford.isis.epad.common.util.ResourceUtils;
import edu.stanford.isis.epad.common.util.XmlNamespaceTranslator;
import edu.stanford.isis.epad.plugin.server.impl.PluginConfig;
import edu.stanford.isis.epadws.xnat.XNATUtil;

public class AimResourceHandler extends AbstractHandler
{
	private static final ProxyLogger logger = ProxyLogger.getInstance();

	public String serverProxy = ProxyConfig.getInstance().getParam("serverProxy");
	public String namespace = ProxyConfig.getInstance().getParam("namespace");
	public String serverUrl = ProxyConfig.getInstance().getParam("serverUrl");
	public String username = ProxyConfig.getInstance().getParam("username");
	public String password = ProxyConfig.getInstance().getParam("password");
	public String baseAnnotationDir = ProxyConfig.getInstance().getParam("baseAnnotationDir");
	public String xsdFile = ProxyConfig.getInstance().getParam("xsdFile");
	public String xsdFilePath = ProxyConfig.getInstance().getParam("baseSchemaDir") + xsdFile;
	public String collection = ProxyConfig.getInstance().getParam("collection");
	public String dbpath = ProxyConfig.getInstance().getParam("dbpath");
	public String templatePath = ProxyConfig.getInstance().getParam("baseTemplatesDir");
	public String wadoProxy = ProxyConfig.getInstance().getParam("wadoProxy");

	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error";
	private static final String INVALID_METHOD_MESSAGE = "Only POST and GET methods valid for this route";
	private static final String FILE_UPLOAD_ERROR_MESSAGE = "File upload failures; see response for details";
	private static final String MISSING_QUERY_MESSAGE = "No series or study query in request";
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
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		PrintWriter out = httpResponse.getWriter();

		httpResponse.setContentType("text/xml");
		httpResponse.setHeader("Cache-Control", "no-cache");

		if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
			String method = httpRequest.getMethod();
			if ("GET".equalsIgnoreCase(method)) {
				String queryString = httpRequest.getQueryString();
				queryString = URLDecoder.decode(queryString, "UTF-8");
				logger.info("AimResourceHandler received GET method : " + queryString);
				if (queryString != null) {
					try { // Build an XML document
						queryAIMImageAnnotations(out, queryString);
						httpResponse.setStatus(HttpServletResponse.SC_OK);
					} catch (Exception e) {
						logger.warning(INTERNAL_EXCEPTION_MESSAGE);
						out.append(INTERNAL_EXCEPTION_MESSAGE + "<br>");
						httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					} catch (Error e) {
						logger.warning(INTERNAL_EXCEPTION_MESSAGE);
						out.append(INTERNAL_EXCEPTION_MESSAGE + "<br>");
						httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
				} else {
					logger.info(MISSING_QUERY_MESSAGE);
					out.append(JsonHelper.createJSONErrorResponse(MISSING_QUERY_MESSAGE));
					httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			} else if ("POST".equalsIgnoreCase(method)) { // http://www.tutorialspoint.com/servlets/servlets-file-uploading.htm
				String annotationsUploadDirPath = ResourceUtils.getEPADWebServerAnnotationsUploadDir();
				logger.info("Uploading files to dir: " + annotationsUploadDirPath);
				try {
					boolean saveError = uploadAIMAnnotations(httpRequest, out, annotationsUploadDirPath);
					if (saveError) {
						logger.warning(FILE_UPLOAD_ERROR_MESSAGE);
						out.append(FILE_UPLOAD_ERROR_MESSAGE + "<br>");
						httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					} else {
						httpResponse.setStatus(HttpServletResponse.SC_OK);
					}
				} catch (Exception e) {
					logger.warning("Failed to upload AIM files to _" + annotationsUploadDirPath + "_", e);
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} catch (Error e) {
					logger.warning("Error. Could jar file be missing from start script?", e);
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			} else {
				logger.info(INVALID_METHOD_MESSAGE);
				out.append(INVALID_METHOD_MESSAGE);
				httpResponse.setHeader("Access-Control-Allow-Methods", "POST GET");
				httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			}
		} else {
			logger.info(INVALID_SESSION_TOKEN_MESSAGE);
			out.append(INVALID_SESSION_TOKEN_MESSAGE);
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		out.flush();
		out.close();
	}

	private void queryAIMImageAnnotations(PrintWriter out, String queryString) throws ParserConfigurationException,
			AimException
	{
		queryString = queryString.trim();
		String[] queryStrings = queryString.split("=");
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
		ArrayList<ImageAnnotation> aims = getAIMImageAnnotations(id1, id2);

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

	private boolean uploadAIMAnnotations(HttpServletRequest httpRequest, PrintWriter out, String annotationsUploadDirPath)
			throws FileUploadException, IOException, FileNotFoundException, AimException
	{
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(httpRequest);
		int fileCount = 0;
		boolean saveError = false;

		while (iter.hasNext()) {
			fileCount++;
			logger.debug("starting file #" + fileCount);
			FileItemStream item = iter.next();
			String name = item.getFieldName();
			logger.debug("FieldName = " + name);
			InputStream stream = item.openStream();
			String tempName = "temp-" + System.currentTimeMillis() + ".xml";
			File f = new File(annotationsUploadDirPath + tempName);
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
			out.print("added (" + fileCount + "): " + name);
			ImageAnnotation ia = AnnotationGetter.getImageAnnotationFromFile(f.getAbsolutePath(), xsdFilePath);
			if (ia != null) {
				saveToServer(ia);
				out.println("-- Add to AIM server: " + ia.getUniqueIdentifier() + "<br>");
			} else {
				out.println("-- Failed ! not added to AIM server<br>");
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
	public ArrayList<ImageAnnotation> getAIMImageAnnotations(String id1, String id2)
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
				aims = AnnotationGetter.getImageAnnotationsFromServerByPersonIdEqual(serverUrl, namespace, collection,
						username, password, patientId, xsdFilePath);
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
				String query = "SELECT FROM " + collection + " WHERE (ImageAnnotation.cagridId like '0')";
				try {
					aims = AnnotationGetter.getImageAnnotationsFromServerWithAimQuery(serverUrl, namespace, username, password,
							query, xsdFilePath);
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
		logger.info("Number of AIM files founded : " + retAims.size());

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
			logger.info("AnnotationBuilder.saveToFile result: " + res);
			if (storeFile.exists()) {
				storeFile.delete();
			}
			tempFile.renameTo(storeFile);

			AnnotationBuilder.saveToServer(aim, serverUrl, namespace, collection, xsdFilePath, username, password);
			res = AnnotationBuilder.getAimXMLsaveResult();
			logger.info("AnnotationBuilder.saveToServer result: " + res);

			// Check for plugin!!
			if (aim.getCodingSchemeDesignator().equals("epad-plugin")) {
				// find which template has been used to fill the aim file
				String templateName = aim.getCodeValue(); // ex: jjv-5

				logger.info("Plugin detection : " + templateName);

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
					logger.info("Template Has Been Found, handler : " + handlerName);
					// Trigger the plugin
					// String nameAIM=aim.getUniqueIdentifier() + ".xml";

					String url = "http://localhost:8080/plugin/" + pluginName + "/?aimFile=" + aim.getUniqueIdentifier();
					// --Post to the plugin
					HttpClient client = new HttpClient();

					logger.info("Triggering plugin at the following adress : " + url);
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
