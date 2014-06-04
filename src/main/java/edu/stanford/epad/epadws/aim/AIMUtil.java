package edu.stanford.epad.epadws.aim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.TagFromName;

import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.plugins.PluginAIMUtil;
import edu.stanford.epad.common.plugins.PluginConfig;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
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

public class AIMUtil
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static String namespace = EPADConfig.getInstance().getStringPropertyValue("namespace");
	private static String serverUrl = EPADConfig.getInstance().getStringPropertyValue("serverUrl");
	private static String eXistUsername = EPADConfig.getInstance().getStringPropertyValue("username");
	private static String eXistPassword = EPADConfig.getInstance().getStringPropertyValue("password");
	private static final String eXistServerUrl = EPADConfig.getInstance().getStringPropertyValue("serverUrl");
	private static final String eXistCollection = EPADConfig.getInstance().getStringPropertyValue("collection");
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

			log.info("Saving AIM file with ID " + aim.getUniqueIdentifier());

			result = AnnotationBuilder.getAimXMLsaveResult();

			log.info("AnnotationBuilder.saveToFile result: " + result);
			if (storeFile.exists()) {
				storeFile.delete();
			}
			tempFile.renameTo(storeFile);

			AnnotationBuilder.saveToServer(aim, serverUrl, namespace, collection, xsdFilePath, eXistUsername, eXistPassword);
			result = AnnotationBuilder.getAimXMLsaveResult();
			log.info("AnnotationBuilder.saveToServer result: " + result);

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
	public static void generateAIMFileForDSO(File dsoFile) throws AimException
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

			log.info("Saving AIM file for DSO series " + seriesUID + " with ID " + imageAnnotation.getUniqueIdentifier());
			try {
				saveImageAnnotationToServer(imageAnnotation);
			} catch (AimException e) {
				log.warning("Exception saving AIM file for DSO series " + seriesUID, e);
			}
		} else {
			log.warning("Found DSO " + imageUID + " in series " + seriesUID + " with no corresponding DICOM image");
		}

		/*
		 * ServerEventUtil.postEvent(username, "DSOReady", imageAnnotation.getUniqueIdentifier(), aimName, patientID,
		 * patientName, "", "", "");
		 */
	}

	public static void setImageAnnotationUser(ImageAnnotation imageAnnotation, String username)
	{
		List<User> userList = new ArrayList<User>();
		User user = new User();
		user.setLoginName(username);
		imageAnnotation.setListUser(userList);
	}

	private static String saveImageAnnotationToServer(ImageAnnotation aim) throws AimException
	{
		String res = "";

		if (aim.getCodeValue() != null) { // First, write a backup file
			String tempXmlPath = baseAnnotationDir + "temp-" + aim.getUniqueIdentifier() + ".xml";
			String storeXmlPath = baseAnnotationDir + aim.getUniqueIdentifier() + ".xml";
			File tempFile = new File(tempXmlPath);
			File storeFile = new File(storeXmlPath);
			AnnotationBuilder.saveToFile(aim, tempXmlPath, xsdFilePath);
			res = AnnotationBuilder.getAimXMLsaveResult();
			log.info("AnnotationBuilder.saveToFile result: " + res);
			if (storeFile.exists()) {
				storeFile.delete();
			}
			tempFile.renameTo(storeFile);

			AnnotationBuilder.saveToServer(aim, eXistServerUrl, namespace, eXistCollection, xsdFilePath, eXistUsername,
					eXistPassword);
			res = AnnotationBuilder.getAimXMLsaveResult();
			log.info("AnnotationBuilder.saveToServer result: " + res);
		}
		return res;
	}
}
