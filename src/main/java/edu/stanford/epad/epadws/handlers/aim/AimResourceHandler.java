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
import edu.stanford.epad.common.util.XmlNamespaceTranslator;
import edu.stanford.epad.epadws.aim.AIMQueries;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;
import edu.stanford.hakan.aim3api.base.AimException;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;

public class AimResourceHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static String xsdFile = EPADConfig.getInstance().getStringPropertyValue("xsdFile");
	private static String xsdFilePath = EPADConfig.getInstance().getStringPropertyValue("baseSchemaDir") + xsdFile;

	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error in AIM handler";
	private static final String INVALID_METHOD_MESSAGE = "Only POST and GET methods valid for the AIM route";
	private static final String FILE_UPLOAD_ERROR_MESSAGE = "AIM file upload failures; see response for details";
	private static final String MISSING_QUERY_MESSAGE = "No query in AIM request";
	private static final String BAD_QUERY_MESSAGE = "Bad query in AIM request; should have search type and user";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid for AIM request";

	/**
	 * To test the post try:
	 * 
	 * <pre>
	 * curl --form upload=@/tmp/AIM_83ga0zjofj3y8ncm8wb1k3mlitis1glyugamx0zl.xml http://[host]:[port]/epad/aimresource/
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
					if (queryString != null) { // TODO httpRequest.getParameter with "patientID", "user"
						AIMSearchType aimSearchType = getAIMSearchType(httpRequest);
						String searchValue = aimSearchType != null ? httpRequest.getParameter(aimSearchType.getName()) : null;
						String user = httpRequest.getParameter("user");
						log.info("GET request for AIM resource from user " + user + "; query type is " + aimSearchType + ", value "
								+ searchValue);

						if (validParameters(aimSearchType, searchValue, user)) {
							queryAIMImageAnnotations(responseStream, aimSearchType, searchValue, user);
							statusCode = HttpServletResponse.SC_OK;
						} else
							statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_BAD_REQUEST, BAD_QUERY_MESSAGE, log);
					} else
						statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_QUERY_MESSAGE, log);
				} else if ("POST".equalsIgnoreCase(method)) {
					String annotationsUploadDirPath = EPADConfig.getEPADWebServerAnnotationsUploadDir();
					log.info("Uploading AIM annotation(s) to directory " + annotationsUploadDirPath);
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

	private AIMSearchType getAIMSearchType(HttpServletRequest httpRequest)
	{
		for (AIMSearchType aimSearchType : AIMSearchType.values()) {
			if (httpRequest.getParameter(aimSearchType.getName()) != null)
				return aimSearchType;
		}
		log.warning("No valid AIM search type parameter found");
		return null;
	}

	private void queryAIMImageAnnotations(PrintWriter responseStream, AIMSearchType aimSearchType, String searchValue,
			String user) throws ParserConfigurationException, AimException
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

	private boolean uploadAIMAnnotations(HttpServletRequest httpRequest, PrintWriter responseStream,
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

	private boolean validParameters(AIMSearchType aimSearchType, String searchValue, String user)
	{
		return (aimSearchType != null && searchValue != null && user != null);
	}
}
