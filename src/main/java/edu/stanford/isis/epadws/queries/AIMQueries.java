package edu.stanford.isis.epadws.queries;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.hakan.aim3api.base.AimException;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;
import edu.stanford.hakan.aim3api.usage.AnnotationGetter;
import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;

public class AIMQueries
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	private static String namespace = EPADConfig.getInstance().getStringPropertyValue("namespace");
	private static String serverUrl = EPADConfig.getInstance().getStringPropertyValue("serverUrl");
	private static String username = EPADConfig.getInstance().getStringPropertyValue("username");
	private static String password = EPADConfig.getInstance().getStringPropertyValue("password");
	private static String xsdFile = EPADConfig.getInstance().getStringPropertyValue("xsdFile");
	private static String xsdFilePath = EPADConfig.getInstance().getStringPropertyValue("baseSchemaDir") + xsdFile;
	private static String collection = EPADConfig.getInstance().getStringPropertyValue("collection");

	public static List<ImageAnnotation> getAIMImageAnnotationsForPerson(String personName, String user)
	{
		return getAIMImageAnnotations("personName", personName, user);
	}

	public static List<ImageAnnotation> getAIMImageAnnotationsForPatientId(String patientId, String user)
	{
		return getAIMImageAnnotations("patientID", patientId, user);
	}

	public static List<ImageAnnotation> getAIMImageAnnotationsForSeriesUID(String seriesUID, String user)
	{
		return getAIMImageAnnotations("seriesUID", seriesUID, user);
	}

	public static List<ImageAnnotation> getAIMImageAnnotationsForAnnotationUID(String annotationUID, String user)
	{
		return getAIMImageAnnotations("annotationUID", annotationUID, user);
	}

	/**
	 * Read the annotations from the AIM database by patient name, patient id, series id, annotation id, or just get all
	 * of them on a GET. Can also delete by annotation id.
	 * 
	 * @param valueType One of personName, patientId, seriesUID, annotationUID, deleteUID
	 * @param value
	 * @param user
	 * @return List<ImageAnnotation>
	 */
	public static List<ImageAnnotation> getAIMImageAnnotations(String valueType, String value, String user)
	{
		List<ImageAnnotation> retAims = new ArrayList<ImageAnnotation>();
		List<ImageAnnotation> aims = null;
		ImageAnnotation aim = null;

		if (valueType.equals("personName")) {
			String personName = value;
			try {
				aims = AnnotationGetter.getImageAnnotationsFromServerByPersonNameEqual(serverUrl, namespace, collection,
						username, password, personName, xsdFilePath);

			} catch (AimException e) {
				logger.warning("Exception on AnnotationGetter.getImageAnnotationsFromServerByPersonNameEqual " + personName, e);
			}
			if (aims != null) {
				retAims.addAll(aims);
			}
		} else if (valueType.equals("patientId")) {
			String patientId = value;
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
		} else if (valueType.equals("seriesUID")) {
			String seriesUID = value;
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
		} else if (valueType.equals("annotationUID")) {
			String annotationUID = value;
			if (value.equals("all")) {

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
		} else if (valueType.equals("deleteUID")) {
			String annotationUID = value;
			logger.info("calling performDelete with deleteUID on GET ");
			performDelete(annotationUID, collection, serverUrl);
			retAims = null;
		} else if (valueType.equals("key")) {
			logger.info("id1 is key id2 is " + value);
		}
		return retAims;
	}

	private static String performDelete(String uid, String collection, String serverURL)
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

}
