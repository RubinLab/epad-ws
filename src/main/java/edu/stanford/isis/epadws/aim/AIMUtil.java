package edu.stanford.isis.epadws.aim;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import edu.stanford.hakan.aim3api.base.AimException;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;
import edu.stanford.hakan.aim3api.usage.AnnotationBuilder;
import edu.stanford.isis.epad.common.plugins.PluginConfig;
import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;

public class AIMUtil
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	private static String namespace = EPADConfig.getInstance().getStringPropertyValue("namespace");
	private static String serverUrl = EPADConfig.getInstance().getStringPropertyValue("serverUrl");
	private static String username = EPADConfig.getInstance().getStringPropertyValue("username");
	private static String password = EPADConfig.getInstance().getStringPropertyValue("password");
	private static String baseAnnotationDir = EPADConfig.getInstance().getStringPropertyValue("baseAnnotationDir");
	private static String xsdFile = EPADConfig.getInstance().getStringPropertyValue("xsdFile");
	private static String xsdFilePath = EPADConfig.getInstance().getStringPropertyValue("baseSchemaDir") + xsdFile;
	private static String collection = EPADConfig.getInstance().getStringPropertyValue("collection");

	/**
	 * Save the annotation to the server in the AIM database. An invalid annotation will not be saved. Save a file backup
	 * just in case.
	 * 
	 * @param ImageAnnotation
	 * @return String
	 * @throws AimException
	 */
	public static String saveImageAnnotationToServer(ImageAnnotation aim, String jsessionID) throws AimException
	{
		String result = "";

		if (aim.getCodeValue() != null) { // For safety, write a backup file
			String tempXmlPath = baseAnnotationDir + "temp-" + aim.getUniqueIdentifier() + ".xml";
			String storeXmlPath = baseAnnotationDir + aim.getUniqueIdentifier() + ".xml";
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
					HttpClient client = new HttpClient(); // TODO Get rid of localhost
					String url = "http://localhost:8080/epad/plugin/" + pluginName + "/?aimFile=" + aim.getUniqueIdentifier();
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

}
