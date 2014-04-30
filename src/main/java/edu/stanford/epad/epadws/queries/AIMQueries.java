package edu.stanford.epad.epadws.queries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.hakan.aim3api.base.AimException;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;
import edu.stanford.hakan.aim3api.usage.AnnotationGetter;

public class AIMQueries
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static String aimNamespace = EPADConfig.getInstance().getStringPropertyValue("namespace");
	private static String eXistServerUrl = EPADConfig.getInstance().getStringPropertyValue("serverUrl");
	private static String eXistUsername = EPADConfig.getInstance().getStringPropertyValue("username");
	private static String eXistPassword = EPADConfig.getInstance().getStringPropertyValue("password");
	private static String aimXSDFile = EPADConfig.getInstance().getStringPropertyValue("xsdFile");
	private static String aimXSDFilePath = EPADConfig.getInstance().getStringPropertyValue("baseSchemaDir") + aimXSDFile;
	private static String eXistAIMCollection = EPADConfig.getInstance().getStringPropertyValue("collection");

	public static List<ImageAnnotation> getAIMAnnotationsForPerson(String personName, String username)
	{
		return getAIMImageAnnotations(AIMSearchType.PERSON_NAME, personName, username);
	}

	public static List<ImageAnnotation> getAIMAnnotationsForPatient(String patientId, String username)
	{
		return getAIMImageAnnotations(AIMSearchType.PATIENT_ID, patientId, username);
	}

	public static List<ImageAnnotation> getAIMAnnotationsForSeriesUID(String seriesUID, String username)
	{
		return getAIMImageAnnotations(AIMSearchType.SERIES_UID, seriesUID, username);
	}

	public static List<ImageAnnotation> getAIMAnnotationsForAnnotationUID(String annotationUID, String username)
	{
		return getAIMImageAnnotations(AIMSearchType.ANNOTATION_UID, annotationUID, username);
	}

	// Only count annotations for subjects in this project
	public static int getNumberOfAIMAnnotationsForPatients(String sessionID, String username, Set<String> patientIDs)
	{
		int totalAIMAnnotations = 0;

		for (String patientID : patientIDs) {
			totalAIMAnnotations += getNumberOfAIMAnnotationsForPatientID(patientID, username);
		}

		return totalAIMAnnotations;
	}

	public static int getNumberOfAIMAnnotationsForPatientID(String patientId, String username)
	{
		return getNumberOfAIMAnnotations(AIMSearchType.PATIENT_ID, patientId, username);
	}

	public static int getNumberOfAIMAnnotationsForSeriesUID(String seriesUID, String username)
	{
		return getNumberOfAIMAnnotations(AIMSearchType.SERIES_UID, seriesUID, username);
	}

	public static int getNumberOfAIMAnnotationsForSeriesUIDs(Set<String> seriesUIDs, String username)
	{
		int numberOfAIMAnnotations = 0;

		for (String seriesUID : seriesUIDs)
			numberOfAIMAnnotations += getNumberOfAIMAnnotations(AIMSearchType.SERIES_UID, seriesUID, username);

		return numberOfAIMAnnotations;
	}

	public static int getNumberOfAIMAnnotationsForAnnotationUID(String annotationUID, String username)
	{
		return getNumberOfAIMAnnotations(AIMSearchType.ANNOTATION_UID, annotationUID, username);
	}

	/**
	 * Read the annotations from the AIM database by patient name, patient id, series id, annotation id, or just get all
	 * of them on a GET. Can also delete by annotation id.
	 * 
	 * @param aimSearchType One of personName, patientId, seriesUID, annotationUID, deleteUID
	 * @param value
	 * @param user
	 * @return List<ImageAnnotation>
	 */
	public static List<ImageAnnotation> getAIMImageAnnotations(AIMSearchType aimSearchType, String value, String username)
	{
		List<ImageAnnotation> resultAims = new ArrayList<ImageAnnotation>();
		List<ImageAnnotation> aims = null;
		ImageAnnotation aim = null;

		if (aimSearchType == AIMSearchType.PERSON_NAME) {
			String personName = value;
			try {
				aims = AnnotationGetter.getImageAnnotationsFromServerByPersonNameEqual(eXistServerUrl, aimNamespace,
						eXistAIMCollection, eXistUsername, eXistPassword, personName, aimXSDFilePath);

			} catch (AimException e) {
				log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerByPersonNameEqual " + personName, e);
			}
			if (aims != null) {
				resultAims.addAll(aims);
			}
		} else if (aimSearchType == AIMSearchType.PATIENT_ID) {
			String patientId = value;
			try {
				aims = AnnotationGetter.getImageAnnotationsFromServerByPersonIDAndUserNameEqual(eXistServerUrl, aimNamespace,
						eXistAIMCollection, eXistUsername, eXistPassword, patientId, username, aimXSDFilePath);
			} catch (AimException e) {
				log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerByPersonIdEqual " + patientId, e);
			}
			if (aims != null) {
				resultAims.addAll(aims);
			}
		} else if (aimSearchType == AIMSearchType.SERIES_UID) {
			String seriesUID = value;
			try {
				aims = AnnotationGetter.getImageAnnotationsFromServerByImageSeriesInstanceUIDEqual(eXistServerUrl,
						aimNamespace, eXistAIMCollection, eXistUsername, eXistPassword, seriesUID, aimXSDFilePath);
			} catch (AimException e) {
				log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerByImageSeriesInstanceUIDEqual "
						+ seriesUID, e);
			}
			if (aims != null) {
				resultAims.addAll(aims);
			}
		} else if (aimSearchType == AIMSearchType.ANNOTATION_UID) {
			String annotationUID = value;
			if (value.equals("all")) {

				// String query = "SELECT FROM " + collection + " WHERE (ImageAnnotation.cagridId like '0')";
				try {
					aims = AnnotationGetter.getImageAnnotationsFromServerByUserLoginNameContains(eXistServerUrl, aimNamespace,
							eXistAIMCollection, eXistUsername, eXistPassword, username);
				} catch (AimException e) {
					log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerWithAimQuery ", e);
				}
				if (aims != null) {
					resultAims.addAll(aims);
				}
			} else {
				try {
					aim = AnnotationGetter.getImageAnnotationFromServerByUniqueIdentifier(eXistServerUrl, aimNamespace,
							eXistAIMCollection, eXistUsername, eXistPassword, annotationUID, aimXSDFilePath);
				} catch (AimException e) {
					log.warning("Exception in AnnotationGetter.getImageAnnotationFromServerByUniqueIdentifier " + annotationUID,
							e);
				}
				if (aim != null) {
					resultAims.add(aim);
				}
			}
		} else if (aimSearchType == AIMSearchType.DELETE_UID) { // TODO Fix this route
			String annotationUID = value;
			log.info("Calling performDelete with deleteUID on GET ");
			performDelete(annotationUID, eXistAIMCollection, eXistServerUrl);
			resultAims = null;
		} else if (aimSearchType == AIMSearchType.KEY) {
			log.warning("id1 is key id2 is " + value);
		} else {
			log.warning("Unknown AIM search type " + aimSearchType.getName());
		}
		return resultAims;
	}

	private static int getNumberOfAIMAnnotations(AIMSearchType valueType, String value, String username)
	{
		return 1; // TODO This call is too slow so we need an alternative mechanism.
		// return getAIMImageAnnotations(valueType, value, username).size();
		// TODO Use the following when Hakan fixes bug in new counting methods
		// return getCountAIMImageAnnotations(valueType, value, username);
	}

	private static int getCountAIMImageAnnotations(AIMSearchType aimSearchType, String value, String username)
	{
		int count = 0;

		log.info("AIM count query with search type " + aimSearchType + ", value " + value + ", username " + username);
		if (aimSearchType == AIMSearchType.PERSON_NAME) {
			String personName = value;
			try {
				count = AnnotationGetter.getCountImageAnnotationByPersonNameEqual(eXistServerUrl, aimNamespace,
						eXistAIMCollection, eXistUsername, eXistPassword, personName);
			} catch (Exception e) {
				log.warning("Exception in AnnotationGetter.getCountImageAnnotationByPersonNameEqual " + personName, e);
			}
		} else if (aimSearchType == AIMSearchType.PATIENT_ID) {
			String patientId = value;
			try {
				count = AnnotationGetter.getCountImageAnnotationByPersonIdAndUserNameEqual(eXistServerUrl, aimNamespace,
						eXistAIMCollection, eXistUsername, eXistPassword, patientId, username);
			} catch (Exception e) {
				log.warning("Exception in AnnotationGetter.getCountImageAnnotationByPersonIdEqual " + patientId, e);
			}
		} else if (aimSearchType == AIMSearchType.SERIES_UID) {
			String seriesUID = value;
			try {
				count = AnnotationGetter.getCountImageAnnotationByImageSeriesInstanceUidEqual(eXistServerUrl, aimNamespace,
						eXistAIMCollection, eXistUsername, eXistPassword, seriesUID);
			} catch (Exception e) {
				log.warning("Exception in AnnotationGetter.getCountImageAnnotationByImageSeriesInstanceUIDEqual " + seriesUID,
						e);
			}
		} else if (aimSearchType == AIMSearchType.ANNOTATION_UID) {
			String annotationUID = value;
			if (value.equals("all")) {
				try {
					count = AnnotationGetter.getCountImageAnnotationByUserLoginNameContains(eXistServerUrl, aimNamespace,
							eXistAIMCollection, eXistUsername, eXistPassword, username);
				} catch (Exception e) {
					log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerWithAimQuery ", e);
				}
			} else {
				try {
					count = AnnotationGetter.getCountImageAnnotationByUniqueIdentifierEqual(eXistServerUrl, aimNamespace,
							eXistAIMCollection, eXistUsername, eXistPassword, annotationUID);
				} catch (Exception e) {
					log.warning("Exception in AnnotationGetter.getCountImageAnnotationByUniqueIdentifier " + annotationUID, e);
				}
			}
		} else {
			log.warning("Unknown AIM search type " + aimSearchType.getName());
			count = 0;
		}
		log.info("Number of annotations " + count);
		return count;
	}

	private static String performDelete(String uid, String collection, String serverURL)
	{
		String result = "";

		log.info("performDelete on : " + uid);
		try {
			// AnnotationGetter.deleteImageAnnotationFromServer(serverUrl, namespace, collection, xsdFilePath,username,
			// password, uid);
			AnnotationGetter.removeImageAnnotationFromServer(eXistServerUrl, aimNamespace, collection, eXistUsername,
					eXistPassword, uid);

			log.info("after deletion on : " + uid);

		} catch (Exception ex) {
			result = "XML Deletion operation is Unsuccessful (Method Name; performDelete): " + ex.getLocalizedMessage();
			log.info("XML Deletion operation is Unsuccessful (Method Name; performDelete): " + ex.getLocalizedMessage());
		}
		log.info("AnnotationGetter.deleteImageAnnotationFromServer result: " + result);
		return result;
	}
}
