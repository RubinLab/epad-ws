package edu.stanford.isis.epadws.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUploadException;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.w3c.dom.Document;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADResources;
import edu.stanford.isis.epad.common.util.XmlNamespaceTranslator;

/**
 * AIM resource. AIM XML files in an XML database can be queried or uploaded.
 * 
 * @author martin
 */
public class AIMServerResource extends BaseServerResource
{
	private final String namespace = EPADConfig.getInstance().getParam("namespace"); // TODO Constants for these names
	private final String serverUrl = EPADConfig.getInstance().getParam("serverUrl");
	private final String username = EPADConfig.getInstance().getParam("username");
	private final String password = EPADConfig.getInstance().getParam("password");
	private final String baseAnnotationDir = EPADConfig.getInstance().getParam("baseAnnotationDir");
	private final String xsdFile = EPADConfig.getInstance().getParam("xsdFile");
	private final String xsdFilePath = EPADConfig.getInstance().getParam("baseSchemaDir") + xsdFile;
	private final String collection = EPADConfig.getInstance().getParam("collection");

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

	/**
	 * To test the post try:
	 * 
	 * <pre>
	 * curl --form upload=@/tmp/AIMFile.xml http://localhost:8080/aimresource/
	 */
	@Post("xml")
	public String uploadAIM(Representation representation)
	{
		log.info("AIMServerResource received POST method");
		String filePath = EPADResources.getEPADWebServerAnnotationsUploadDir();

		try { // Create the directory for AIM file upload
			StringBuilder out = new StringBuilder();
			RestletFileUpload fileUpload = new RestletFileUpload();
			FileItemIterator fileIterator = fileUpload.getItemIterator(representation);
			uploadXMLFiles(filePath, fileIterator, out);
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

	private void uploadXMLFiles(String filePath, FileItemIterator fileItemIterator, StringBuilder out)
			throws FileUploadException, IOException, FileNotFoundException
	{
	}

	public static String xmlDocumentToString(Document document)
	{
		// TODO These namespaces should be constants, ideally in the aim3api project.
		// add the good namespace
		new XmlNamespaceTranslator().addTranslation(null, "gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM")
				.addTranslation("", "gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM").translateNamespaces(document);

		// set up a transformer
		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = null;
		;
		try {
			trans = transfac.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException("Error transforming XML document: " + e.getMessage(), e);
		}

		trans.setOutputProperty(OutputKeys.INDENT, "yes");

		// create string from XML tree
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(document);

		try {
			trans.transform(source, result);
		} catch (TransformerException e) {
			throw new RuntimeException("Error converting XML document: " + e.getMessage(), e);
		}
		return sw.toString();
	}
}
