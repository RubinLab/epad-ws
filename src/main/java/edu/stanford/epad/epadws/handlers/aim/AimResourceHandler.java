package edu.stanford.epad.epadws.handlers.aim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
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
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.EPADResources;
import edu.stanford.epad.common.util.XmlNamespaceTranslator;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.queries.AIMQueries;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;
import edu.stanford.hakan.aim3api.base.AimException;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;
import edu.stanford.hakan.aim3api.usage.AnnotationGetter;

public class AimResourceHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static String xsdFile = EPADConfig.getInstance().getStringPropertyValue("xsdFile");
	private static String xsdFilePath = EPADConfig.getInstance().getStringPropertyValue("baseSchemaDir") + xsdFile;

	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error in AIM handler";
	private static final String INVALID_METHOD_MESSAGE = "Only POST and GET methods valid for the AIM route";
	private static final String FILE_UPLOAD_ERROR_MESSAGE = "AIM file upload failures; see response for details";
	private static final String MISSING_QUERY_MESSAGE = "No series or study query in AIM request";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid for AIM request";

	/**
	 * To test the post try:
	 * 
	 * <pre>
	 * curl --form upload=@/home/kurtz/Bureau/AIM_83ga0zjofj3y8ncm8wb1k3mlitis1glyugamx0zl.xml
	 * http://epad-prod1.stanford.edu:8080/epad/aimresource/
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

			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String method = httpRequest.getMethod();
				if ("GET".equalsIgnoreCase(method)) {
					String queryString = httpRequest.getQueryString();
					queryString = URLDecoder.decode(queryString, "UTF-8");
					log.info("AimResourceHandler received query: " + queryString);
					if (queryString != null) { // TODO httpRequest.getParameter with "patientID", "user"
						queryAIMImageAnnotations(responseStream, queryString);
						statusCode = HttpServletResponse.SC_OK;
					} else {
						statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_QUERY_MESSAGE, log);
					}
				} else if ("POST".equalsIgnoreCase(method)) {
					String annotationsUploadDirPath = EPADResources.getEPADWebServerAnnotationsUploadDir();
					log.info("Uploading annotations to directory " + annotationsUploadDirPath);
					try {
						boolean saveError = uploadAIMAnnotations(httpRequest, responseStream, annotationsUploadDirPath);
						if (saveError) {
							statusCode = HandlerUtil.internalErrorResponse(FILE_UPLOAD_ERROR_MESSAGE + "<br>", log);
						} else {
							statusCode = HttpServletResponse.SC_OK;
						}
					} catch (Throwable t) {
						String errorMessage = "Failed to upload AIM files to directory " + annotationsUploadDirPath;
						statusCode = HandlerUtil.internalErrorResponse(errorMessage, t, log);
					}
				} else {
					httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET");
					statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_METHOD_NOT_ALLOWED, INVALID_METHOD_MESSAGE,
							log);
				}
			} else {
				statusCode = HandlerUtil.invalidTokenResponse(INVALID_SESSION_TOKEN_MESSAGE, log);
			}
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_EXCEPTION_MESSAGE + t.getMessage() + "<br>", t,
					responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	private void queryAIMImageAnnotations(PrintWriter responseStream, String queryString)
			throws ParserConfigurationException, AimException
	{
		// TODO Replace this mess with getParameter calls on request.
		queryString = queryString.trim();
		String[] queryStrings = queryString.split("&");
		String valueType = null;
		String value = null;
		String user = null;
		if (queryStrings.length == 2) {
			String[] patientIDString = queryStrings[0].split("=");
			String[] userString = queryStrings[1].split("=");
			valueType = patientIDString[0];
			value = patientIDString[1];
			user = userString[1];
		}
		List<ImageAnnotation> aims = AIMQueries.getAIMImageAnnotations(valueType, value, user);
		log.info("AimResourceHandler, number of AIM files found: " + aims.size());

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

	private boolean uploadAIMAnnotations(HttpServletRequest httpRequest, PrintWriter responseStream,
			String annotationsUploadDirPath) throws FileUploadException, IOException, FileNotFoundException, AimException
	{ // http://www.tutorialspoint.com/servlets/servlets-file-uploading.htm
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(httpRequest);
		int fileCount = 0;
		boolean saveError = false;

		while (iter.hasNext()) {
			fileCount++;
			log.debug("Uploading annotation number " + fileCount);
			FileItemStream item = iter.next();
			String name = item.getFieldName();
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
				IOUtils.closeQuietly(fos);
			}
			responseStream.print("added (" + fileCount + "): " + name);
			ImageAnnotation ia = AnnotationGetter.getImageAnnotationFromFile(f.getAbsolutePath(), xsdFilePath);
			if (ia != null) {
				String jsessionID = XNATSessionOperations.getJSessionIDFromRequest(httpRequest);
				AIMUtil.saveImageAnnotationToServer(ia, jsessionID);
				responseStream.println("-- Add to AIM server: " + ia.getUniqueIdentifier() + "<br>");
			} else {
				responseStream.println("-- Failed ! not added to AIM server<br>");
				saveError = true;
			}
		}
		return saveError;
	}

	// Create an XML document from a String
	private static String XmlDocumentToString(Document document)
	{
		new XmlNamespaceTranslator().addTranslation(null, "gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM")
				.addTranslation("", "gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM").translateNamespaces(document);

		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = null;

		try {
			trans = transfac.newTransformer();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		trans.setOutputProperty(OutputKeys.INDENT, "yes");

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
}
