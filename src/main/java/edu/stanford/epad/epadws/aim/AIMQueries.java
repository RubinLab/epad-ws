//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.aim;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.MongoDBOperations;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.UserProjectService;
import edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation;
import edu.stanford.hakan.aim4api.audittrail.AuditTrailManager;
import edu.stanford.hakan.aim4api.base.ImageAnnotationCollection;

public class AIMQueries
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String aimNamespace = EPADConfig.aim3Namespace;
	private static final String eXistServerUrl = EPADConfig.eXistServerUrl;
	private static final String eXistUsername = EPADConfig.eXistUsername;
	private static final String eXistPassword = EPADConfig.eXistPassword;
	private static final String xsdFilePath = EPADConfig.xsdFilePath;
	private static final String eXistCollection = EPADConfig.eXistCollection;
	private static final String useV4 = EPADConfig.useV4;
	private static final String aim4Namespace = EPADConfig.aim4Namespace;
	private static final String eXistCollectionV4 = EPADConfig.eXistCollectionV4;
	private static final String xsdFilePath4 = EPADConfig.xsdFilePathV4;
	//ml
	private static final String aimeServerUrl = EPADConfig.aimeServerUrl;
	private static final String aimeApiKey = EPADConfig.aimeApiKey;

	public static int getNumberOfAIMAnnotationsForPatients(String sessionID, String username, Set<String> patientIDs)
	{ // Only count annotations for subjects in this project
		int totalAIMAnnotations = 0;

		for (String patientID : patientIDs)
			totalAIMAnnotations += getNumberOfAIMAnnotationsForPatient(patientID, username);

		return totalAIMAnnotations;
	}

	public static int getNumberOfAIMAnnotationsForPatient(String patientId, String username)
	{
		return getNumberOfAIMAnnotations(AIMSearchType.PATIENT_ID, patientId, username);
	}

	public static int getNumberOfAIMAnnotationsForSeries(String seriesUID, String username)
	{
		return getNumberOfAIMAnnotations(AIMSearchType.SERIES_UID, seriesUID, username);
	}

	public static int getNumberOfAIMAnnotationsForSeriesSet(Set<String> seriesUIDs, String username)
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
	/**************AIME Methods Start*********************/
	public static List<ImageAnnotationCollection> getAIMImageAnnotationsV4AIME(AIMSearchType aimSearchType, String value, String username)
	{
		List<ImageAnnotationCollection> resultAims = new ArrayList<ImageAnnotationCollection>();
		List<ImageAnnotationCollection> aims = null;
		ImageAnnotationCollection aim = null;
		long time1 = System.currentTimeMillis();
	    
		if (username != null && username.equals("")) { 
			try {
				log.info("Getting all AIM annotations");
					aims = edu.stanford.hakan.aim4api.usage.AIMEAnnotationGetter
							.getAllImageAnnotationCollections(aimeServerUrl, aimeApiKey);
					if (aims == null)
						aims = new ArrayList<ImageAnnotationCollection>();
			} catch (edu.stanford.hakan.aim4api.base.AimException e) {
				log.warning("Exception in AIMEAnnotationGetter.getAllImageAnnotationCollections", e);
			}
			if (aims != null)
				resultAims.addAll(aims);
		} else if (aimSearchType == AIMSearchType.PERSON_NAME) {
			String personName = value;
			try {
					aims = edu.stanford.hakan.aim4api.usage.AIMEAnnotationGetter
							.getImageAnnotationCollectionByPersonNameEqual(aimeServerUrl, aimeApiKey, personName);
					if (aims == null)
						aims = new ArrayList<ImageAnnotationCollection>();
			} catch (edu.stanford.hakan.aim4api.base.AimException e) {
				log.warning("Exception in AIMEAnnotationGetter.getImageAnnotationsFromServerByPersonNameEqual " + personName, e);
			}
			if (aims != null)
				resultAims.addAll(aims);
		} else if (aimSearchType == AIMSearchType.PATIENT_ID) {
			String patientId = value;
			try {
					aims = edu.stanford.hakan.aim4api.usage.AIMEAnnotationGetter
							.getImageAnnotationCollectionByUserNameAndPersonIdEqual(aimeServerUrl,aimeApiKey, username, patientId);
					if (aims == null)
						aims = new ArrayList<ImageAnnotationCollection>();
			} catch (edu.stanford.hakan.aim4api.base.AimException e) {
				log.warning("Exception in AIMEAnnotationGetter.getImageAnnotationsFromServerByPersonIdEqual " + patientId, e);
			}
			if (aims != null)
				resultAims.addAll(aims);
		} else if (aimSearchType == AIMSearchType.SERIES_UID) {
			
			log.warning("getImageAnnotationsFromServerByImageSeriesInstanceUIDEqual is not supported in AIME database");
					
		} else if (aimSearchType == AIMSearchType.ANNOTATION_UID) {
			String annotationUID = value;
			if (value.equals("all")) {

				try {
						aims = edu.stanford.hakan.aim4api.usage.AIMEAnnotationGetter
								.getImageAnnotationCollectionByUserLoginNameContains(aimeServerUrl,aimeApiKey, username);
						if (aims == null)
							aims = new ArrayList<ImageAnnotationCollection>();

				} catch (edu.stanford.hakan.aim4api.base.AimException e) {
					log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerWithAimQuery ", e);
				}
				if (aims != null)
					resultAims.addAll(aims);
			} else if (value.contains(",")){
				try {
						String aimQuery = "";
						aims = edu.stanford.hakan.aim4api.usage.AIMEAnnotationGetter
								.getImageAnnotationCollectionByUniqueIdentifierList(aimeServerUrl,aimeApiKey, value.split(","));
						log.info("Returning " + aims.size() + " v4 annotations");
						if (aims != null)
							resultAims.addAll(aims);

				} catch (edu.stanford.hakan.aim4api.base.AimException e) {
					log.warning("Exception in AIMEAnnotationGetter.getImageAnnotationCollectionByUniqueIdentifierList ", e);
				}
			} else {
				try {
						aim = edu.stanford.hakan.aim4api.usage.AIMEAnnotationGetter
								.getImageAnnotationCollectionByUniqueIdentifier(aimeServerUrl,aimeApiKey, annotationUID);

				} catch (edu.stanford.hakan.aim4api.base.AimException e) {
					log.warning("Exception in AIMEAnnotationGetter.getImageAnnotationFromServerByUniqueIdentifier " + annotationUID,
							e);
				}
				if (aim != null)
					resultAims.add(aim);
			}
		} else if (aimSearchType.equals(AIMSearchType.AIM_QUERY)) {
			log.warning("aimquery is not supported in AIME database");
			
		} else {
			log.warning("Unknown AIM search type " + aimSearchType.getName());
		}
		long time2 = System.currentTimeMillis();
		log.info("AIM query took " + (time2-time1) + " msecs for " + resultAims.size() + " aims ");
		return resultAims;
	}
	
	
	/**************AIME Methods End*********************/

	/**
	 * Read the annotations from the AIM database by patient name, patient id, series id, annotation id, or just get all
	 * of them on a GET. Can also delete by annotation id.
	 * 
	 * @param aimSearchType One of personName, patientId, seriesUID, annotationUID, deleteUID
	 * @param value
	 * @param user
	 * @return List<ImageAnnotation>
	 * @throws edu.stanford.hakan.aim4api.base.AimException
	 */
	public static List<ImageAnnotation> getAIMImageAnnotations(AIMSearchType aimSearchType, String value, String username)
	{
		return getAIMImageAnnotations(aimSearchType, value, username, 1, 5000);
	}
	
	public static List<ImageAnnotation> getAIMImageAnnotations(AIMSearchType aimSearchType, String value, String username, int startIndex, int count)
	{
		if (useV4.equals("false"))
		{
			return getAIMImageAnnotations(null, aimSearchType, value, username, startIndex, count);
		}
		else
		{
			List<ImageAnnotation> aims = new ArrayList<ImageAnnotation>();
    		Set<String> projectIDs;
			try {
				projectIDs = UserProjectService.getAllProjectIDs();
				for (String projectID: projectIDs)
				{
					aims.addAll(getAIMImageAnnotations(projectID, aimSearchType, value, username, startIndex, count));
				}
			} catch (Exception e) {
				log.warning("Error getting projects and annotations", e);;
			}
			return aims;
		}
	}
	public static List<ImageAnnotation> getAIMImageAnnotations(String projectID, AIMSearchType aimSearchType, String value, String username, int startIndex, int count)
	{
		return getAIMImageAnnotations(projectID, aimSearchType, value, username, startIndex, count, false);
	}
	public static List<ImageAnnotation> getAIMImageAnnotations(String projectID, AIMSearchType aimSearchType, String value, String username, int startIndex, int count, boolean use3Only)
	{
		log.info("Getting AIM annotations, aimSearchType:" + aimSearchType + " value:" + value + " start:" + startIndex + " count:" + count + " projectID:" + projectID);
		List<ImageAnnotation> resultAims = new ArrayList<ImageAnnotation>();
		List<ImageAnnotation> aims = null;
		ImageAnnotation aim = null;
		long time1 = System.currentTimeMillis();
	    String collection4Name = eXistCollectionV4;
	    if (projectID != null && projectID.length() > 0)
	    	collection4Name = collection4Name + "/" + projectID;
		if (username != null && username.equals("")) { // TODO Temporary hack to get all annotations!
			try {
				log.info("Getting all AIM annotations, start:" + startIndex + " count:" + count);
				List<edu.stanford.hakan.aim4api.base.ImageAnnotationCollection> iacs = edu.stanford.hakan.aim4api.usage.AnnotationGetter
						.getAllImageAnnotationCollections(eXistServerUrl, aim4Namespace, collection4Name, eXistUsername,
								eXistPassword, startIndex, count);
				if (aims == null)
					aims = new ArrayList<ImageAnnotation>();
				for (int i = 0; i < iacs.size(); i++)
					aims.add(new ImageAnnotation(iacs.get(i)));
			} catch (edu.stanford.hakan.aim4api.base.AimException e) {
				log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerByCagridIdEqual", e);
			}
			if (aims != null)
				resultAims.addAll(aims);
		} else if (aimSearchType == AIMSearchType.PERSON_NAME) {
			String personName = value;
			try {
				List<edu.stanford.hakan.aim4api.base.ImageAnnotationCollection> iacs = edu.stanford.hakan.aim4api.usage.AnnotationGetter
						.getImageAnnotationCollectionByPersonNameEqual(eXistServerUrl, aim4Namespace, collection4Name,
								eXistUsername, eXistPassword, personName);
				if (aims == null)
					aims = new ArrayList<ImageAnnotation>();
				for (int i = 0; i < iacs.size(); i++)
					aims.add(new ImageAnnotation(iacs.get(i)));
			} catch (edu.stanford.hakan.aim4api.base.AimException e) {
				log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerByPersonNameEqual " + personName, e);
			}
			if (aims != null)
				resultAims.addAll(aims);
		} else if (aimSearchType == AIMSearchType.PATIENT_ID) {
			String patientId = value;
			try {
				List<edu.stanford.hakan.aim4api.base.ImageAnnotationCollection> iacs = edu.stanford.hakan.aim4api.usage.AnnotationGetter
						.getImageAnnotationCollectionByUserNameAndPersonIdEqual(eXistServerUrl, aim4Namespace, collection4Name,
								eXistUsername, eXistPassword, username, patientId);
				if (aims == null)
					aims = new ArrayList<ImageAnnotation>();
				for (int i = 0; i < iacs.size(); i++)
					aims.add(new ImageAnnotation(iacs.get(i)));
			} catch (edu.stanford.hakan.aim4api.base.AimException e) {
				log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerByPersonIdEqual " + patientId, e);
			}
			if (aims != null)
				resultAims.addAll(aims);
		} else if (aimSearchType == AIMSearchType.SERIES_UID) {
			String seriesUID = value;
			try {
				List<edu.stanford.hakan.aim4api.base.ImageAnnotationCollection> iacs = edu.stanford.hakan.aim4api.usage.AnnotationGetter
						.getImageAnnotationCollectionByImageSeriesInstanceUIDEqual(eXistServerUrl, aim4Namespace,
								collection4Name, eXistUsername, eXistPassword, seriesUID);
				if (aims == null)
					aims = new ArrayList<ImageAnnotation>();
				for (int i = 0; i < iacs.size(); i++)
					aims.add(new ImageAnnotation(iacs.get(i)));
			} catch (edu.stanford.hakan.aim4api.base.AimException e) {
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
					List<edu.stanford.hakan.aim4api.base.ImageAnnotationCollection> iacs = edu.stanford.hakan.aim4api.usage.AnnotationGetter
							.getImageAnnotationCollectionByUserLoginNameEqual(eXistServerUrl, aim4Namespace, collection4Name,
									eXistUsername, eXistPassword, username);
					if (aims == null)
						aims = new ArrayList<ImageAnnotation>();
					for (int i = 0; i < iacs.size(); i++)
						aims.add(new ImageAnnotation(iacs.get(i)));

				} catch (edu.stanford.hakan.aim4api.base.AimException e) {
					log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerWithAimQuery ", e);
				}
				if (aims != null)
					resultAims.addAll(aims);
			} else if (value.contains(",")){
				try {
					String aimQuery = "";
					List<edu.stanford.hakan.aim4api.base.ImageAnnotationCollection> iacs = edu.stanford.hakan.aim4api.usage.AnnotationGetter
							.getImageAnnotationCollectionByUniqueIdentifierList(eXistServerUrl, aim4Namespace, collection4Name, 
									eXistUsername, eXistPassword, value.split(","));
					if (aims == null)
						aims = new ArrayList<ImageAnnotation>();
					for (int i = 0; i < iacs.size(); i++)
						aims.add(new ImageAnnotation(iacs.get(i)));

				} catch (edu.stanford.hakan.aim4api.base.AimException e) {
					log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerWithAimQuery ", e);
				}
				if (aims != null)
					resultAims.addAll(aims);
			} else {
				if (annotationUID.trim().length() == 0)
				{
					return resultAims;
				}
				try {
					edu.stanford.hakan.aim4api.base.ImageAnnotationCollection iac = edu.stanford.hakan.aim4api.usage.AnnotationGetter
							.getImageAnnotationCollectionByUniqueIdentifier(eXistServerUrl, aim4Namespace, collection4Name,
									eXistUsername, eXistPassword, annotationUID);
					if (iac != null)
						aim = new ImageAnnotation(iac);

				} catch (edu.stanford.hakan.aim4api.base.AimException e) {
					log.warning("Exception in AnnotationGetter.getImageAnnotationFromServerByUniqueIdentifier " + annotationUID,
							e);
				}
				if (aim != null)
					resultAims.add(aim);
			}
		} else if (aimSearchType.equals(AIMSearchType.AIM_QUERY)) {
			String aimQuery = value;
			try {
				log.info("Running AIM4 Query:" + aimQuery + " aimNamespace:" + aimNamespace + " xsdFilePath:" + xsdFilePath);
				List<edu.stanford.hakan.aim4api.base.ImageAnnotationCollection> iacs = edu.stanford.hakan.aim4api.usage.AnnotationGetter
						.getWithAimQuery(eXistServerUrl, aim4Namespace, eXistUsername, eXistPassword, aimQuery, xsdFilePath, startIndex, count);
				if (aims == null)
					aims = new ArrayList<ImageAnnotation>();
				for (int i = 0; i < iacs.size(); i++)
					aims.add(new ImageAnnotation(iacs.get(i)));
				if (aims != null)
					resultAims.addAll(aims);
			} catch (edu.stanford.hakan.aim4api.base.AimException e) {
				log.warning("Exception in AnnotationGetter.getWithAimQuery " + aimQuery,
						e);
			}
		} else {
			log.warning("Unknown AIM search type " + aimSearchType.getName());
		}
		long time2 = System.currentTimeMillis();
		log.info("AIM query took " + (time2-time1) + " msecs for " + resultAims.size() + " aims");
		return resultAims;
	}

	/**
	 * Read the annotations from the AIM database by patient name, patient id, series id, annotation id, or just get all
	 * of them on a GET. Can also delete by annotation id.
	 * 
	 * @param aimSearchType One of personName, patientId, seriesUID, annotationUID, deleteUID
	 * @param value
	 * @param user
	 * @return List<ImageAnnotationCollection>
	 * @throws edu.stanford.hakan.aim4api.base.AimException
	 */
	public static List<ImageAnnotationCollection> getAIMImageAnnotationsV4(String projectID, AIMSearchType aimSearchType, String value, String username)
	{
		List<ImageAnnotationCollection> resultAims = new ArrayList<ImageAnnotationCollection>();
		List<ImageAnnotationCollection> aims = null;
		ImageAnnotationCollection aim = null;
		long time1 = System.currentTimeMillis();
	    String collection4Name = eXistCollectionV4;
	    if (projectID != null && projectID.length() > 0)
	    	collection4Name = collection4Name + "/" + projectID;

		if (username != null && username.equals("")) { // TODO Temporary hack to get all annotations!
			try {
				log.info("Getting all AIM annotations");
					aims = edu.stanford.hakan.aim4api.usage.AnnotationGetter
							.getAllImageAnnotationCollections(eXistServerUrl, aim4Namespace, collection4Name, eXistUsername,
									eXistPassword);
					if (aims == null)
						aims = new ArrayList<ImageAnnotationCollection>();
			} catch (edu.stanford.hakan.aim4api.base.AimException e) {
				log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerByCagridIdEqual", e);
			}
			if (aims != null)
				resultAims.addAll(aims);
		} else if (aimSearchType == AIMSearchType.PERSON_NAME) {
			String personName = value;
			try {
					aims = edu.stanford.hakan.aim4api.usage.AnnotationGetter
							.getImageAnnotationCollectionByPersonNameEqual(eXistServerUrl, aim4Namespace, collection4Name,
									eXistUsername, eXistPassword, personName);
					if (aims == null)
						aims = new ArrayList<ImageAnnotationCollection>();
			} catch (edu.stanford.hakan.aim4api.base.AimException e) {
				log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerByPersonNameEqual " + personName, e);
			}
			if (aims != null)
				resultAims.addAll(aims);
		} else if (aimSearchType == AIMSearchType.PATIENT_ID) {
			String patientId = value;
			try {
					aims = edu.stanford.hakan.aim4api.usage.AnnotationGetter
							.getImageAnnotationCollectionByUserNameAndPersonIdEqual(eXistServerUrl, aim4Namespace, collection4Name,
									eXistUsername, eXistPassword, username, patientId);
					if (aims == null)
						aims = new ArrayList<ImageAnnotationCollection>();
			} catch (edu.stanford.hakan.aim4api.base.AimException e) {
				log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerByPersonIdEqual " + patientId, e);
			}
			if (aims != null)
				resultAims.addAll(aims);
		} else if (aimSearchType == AIMSearchType.SERIES_UID) {
			String seriesUID = value;
			try {
					aims = edu.stanford.hakan.aim4api.usage.AnnotationGetter
							.getImageAnnotationCollectionByImageSeriesInstanceUIDEqual(eXistServerUrl, aim4Namespace,
									collection4Name, eXistUsername, eXistPassword, seriesUID);
					if (aims == null)
						aims = new ArrayList<ImageAnnotationCollection>();
			} catch (edu.stanford.hakan.aim4api.base.AimException e) {
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
						aims = edu.stanford.hakan.aim4api.usage.AnnotationGetter
								.getImageAnnotationCollectionByUserLoginNameContains(eXistServerUrl, aim4Namespace, collection4Name,
										eXistUsername, eXistPassword, username);
						if (aims == null)
							aims = new ArrayList<ImageAnnotationCollection>();

				} catch (edu.stanford.hakan.aim4api.base.AimException e) {
					log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerWithAimQuery ", e);
				}
				if (aims != null)
					resultAims.addAll(aims);
			} else if (value.contains(",")){
				try {
						String aimQuery = "";
						aims = edu.stanford.hakan.aim4api.usage.AnnotationGetter
								.getImageAnnotationCollectionByUniqueIdentifierList(eXistServerUrl, aim4Namespace, collection4Name, 
										eXistUsername, eXistPassword, value.split(","));
						log.info("Returning " + aims.size() + " v4 annotations");
						if (aims != null)
							resultAims.addAll(aims);

				} catch (edu.stanford.hakan.aim4api.base.AimException e) {
					log.warning("Exception in AnnotationGetter.getImageAnnotationCollectionByUniqueIdentifierList ", e);
				}
			} else {
				try {
						aim = edu.stanford.hakan.aim4api.usage.AnnotationGetter
								.getImageAnnotationCollectionByUniqueIdentifier(eXistServerUrl, aim4Namespace, collection4Name,
										eXistUsername, eXistPassword, annotationUID);

				} catch (edu.stanford.hakan.aim4api.base.AimException e) {
					log.warning("Exception in AnnotationGetter.getImageAnnotationFromServerByUniqueIdentifier " + annotationUID,
							e);
				}
				if (aim != null)
					resultAims.add(aim);
			}
		} else if (aimSearchType.equals(AIMSearchType.AIM_QUERY)) {
			String aimQuery = value;
			try {
					log.info("Running AIM4 Query:" + aimQuery + " aimNamespace:" + aimNamespace + " xsdFilePath:" + xsdFilePath);
					aims = edu.stanford.hakan.aim4api.usage.AnnotationGetter
							.getWithAimQuery(eXistServerUrl, aim4Namespace, eXistUsername, eXistPassword, aimQuery, xsdFilePath);
					if (aims == null)
						aims = new ArrayList<ImageAnnotationCollection>();
					if (aims != null)
						resultAims.addAll(aims);
			} catch (edu.stanford.hakan.aim4api.base.AimException e) {
				log.warning("Exception in AnnotationGetter.getWithAimQuery " + aimQuery,
						e);
			}
		} else {
			log.warning("Unknown AIM search type " + aimSearchType.getName());
		}
		long time2 = System.currentTimeMillis();
		log.info("AIM query took " + (time2-time1) + " msecs for " + resultAims.size() + " aims in projectID:" + projectID);
		return resultAims;
	}

	/**
	 * Read the Deleted annotations from the AIM database by patient name, patient id, annotation id, or just get all
	 * of them on a GET.
	 * 
	 * @param aimSearchType One of personName, patientId, annotationUID
	 * @param value
	 * @param user
	 * @return List<ImageAnnotationCollection>
	 * @throws edu.stanford.hakan.aim4api.base.AimException
	 */
	public static List<ImageAnnotationCollection> getDeletedAIMImageAnnotations(AIMSearchType aimSearchType, String value, String username) throws Exception
	{
		List<ImageAnnotationCollection> resultAims = new ArrayList<ImageAnnotationCollection>();
		long time1 = System.currentTimeMillis();
	    String collection4Name = eXistCollectionV4;
	    if (aimSearchType == null) {
	    	aimSearchType = AIMSearchType.ANNOTATION_UID;
	    	value = "all";
	    }
		try {
			if (aimSearchType == AIMSearchType.PERSON_NAME) {
				String personName = value;
				resultAims = edu.stanford.hakan.aim4api.usage.AnnotationGetter
						.getDeletedImageAnnotationCollectionByPersonNameEqual(eXistServerUrl, aim4Namespace, collection4Name,
								eXistUsername, eXistPassword, personName);
			} else if (aimSearchType == AIMSearchType.PATIENT_ID) {
				String patientId = value;
				resultAims = edu.stanford.hakan.aim4api.usage.AnnotationGetter
						.getDeletedImageAnnotationCollectionByPersonIdEqual(eXistServerUrl, aim4Namespace, collection4Name,
								eXistUsername, eXistPassword, patientId);
			} else if (aimSearchType == AIMSearchType.ANNOTATION_UID) {
				String annotationUID = value;
				if (value.equals("all")) {
					if (DefaultEpadProjectOperations.getInstance().isAdmin(username))
					{	
						log.debug("Getting all deleted AIMS");
						resultAims = edu.stanford.hakan.aim4api.usage.AnnotationGetter
								.getDeletedImageAnnotationCollectionALL(eXistServerUrl, aim4Namespace, collection4Name,
										eXistUsername, eXistPassword);
					}
					else
					{
						log.debug("Getting deleted AIMS for " + username);
						resultAims = edu.stanford.hakan.aim4api.usage.AnnotationGetter
						.getDeletedImageAnnotationCollectionByUserNameEqual(eXistServerUrl, aim4Namespace, collection4Name,
								eXistUsername, eXistPassword, username);
					}
				} else {
					ImageAnnotationCollection aim = edu.stanford.hakan.aim4api.usage.AnnotationGetter
							.getDeletedImageAnnotationCollectionByUniqueIdentifier(eXistServerUrl, aim4Namespace, collection4Name,
									eXistUsername, eXistPassword, annotationUID);
					if (aim != null) resultAims.add(aim);
				}
			} else {
				log.warning("Unknown AIM search type " + aimSearchType.getName());
				throw new Exception("Unknown AIM search type " + aimSearchType.getName());
			}
		} catch (Exception e) {
			log.warning("Exception in AnnotationGetter ", e);
			throw e;
		}
		long time2 = System.currentTimeMillis();
		log.info("AIM query took " + (time2-time1) + " msecs for " + resultAims.size() + " deleted aims");
		return resultAims;
	}

	/**
	 * Read the annotations from the AIM database by patient name, patient id, series id, annotation id, or just get all
	 * of them on a GET. Can also delete by annotation id.
	 * 
	 * @param aimSearchType One of personName, patientId, seriesUID, annotationUID, deleteUID
	 * @param value
	 * @param user
	 * @return List<ImageAnnotationCollection>
	 * @throws edu.stanford.hakan.aim4api.base.AimException
	 */
	public static List<String> getJsonAnnotations(String projectID, AIMSearchType aimSearchType, String value, String username) throws Exception
	{
		//db.Test.find( { 'ImageAnnotationCollection.uniqueIdentifier.root': { $in: [ 'drr7d3pp5sr3up1hbt1v53cf4va5ga57fv8aeri6', 'iqn1vvxtdptm0wtzsc43kptl26oft5c08wxz0w1t' ] } } )
		List<String> results = new ArrayList<String>();
		long time1 = System.currentTimeMillis();
		DBObject query = null;
		if (aimSearchType == AIMSearchType.PERSON_NAME) {
			String personName = value;
			query = new BasicDBObject("ImageAnnotationCollection.person.name.value", personName);
		} else if (aimSearchType == AIMSearchType.PATIENT_ID) {
			String patientId = value;
			query = new BasicDBObject("ImageAnnotationCollection.person.id.value", patientId);
		} else if (aimSearchType == AIMSearchType.SERIES_UID) {
			String seriesUID = value;
			query = new BasicDBObject("ImageAnnotationCollection.imageAnnotations.ImageAnnotation.imageReferenceEntityCollection.ImageReferenceEntity.imageStudy.imageSeries.instanceUid.root", seriesUID);
		} else if (aimSearchType == AIMSearchType.ANNOTATION_UID) {
			String annotationUID = value;
			if (value.equals("all")) {
			} else if (value.contains(",")){
				String[] ids = value.split(",");
	            BasicDBList aimIds = new BasicDBList();
	            for (String id: ids)
	            {
	            	aimIds.add(id);
	            }
	            DBObject inClause = new BasicDBObject("$in", aimIds);
	            query = new BasicDBObject("ImageAnnotationCollection.uniqueIdentifier.root", inClause);				
			} else {
	            query = new BasicDBObject("ImageAnnotationCollection.uniqueIdentifier.root", value);				
			}
		} else if (aimSearchType.equals(AIMSearchType.JSON_QUERY)) {
			String jsonQuery = value;
			query = (DBObject) JSON.parse(jsonQuery);
		} else {
			log.warning("Unsupported AIM search type for mongoDB:" + aimSearchType.getName());
		}
		DB mongoDB = MongoDBOperations.getMongoDB();
		if (mongoDB == null)
			throw new Exception("Error connecting to MongoDB");
		DBCollection coll = mongoDB.getCollection(projectID);
		DBCursor cursor = null;
		try {
			cursor = coll.find(query);
		    while (cursor.hasNext()) {
	            DBObject obj = cursor.next();
	            results.add(obj.toString());
		    }
		} finally {
		    cursor.close();
		}		long time2 = System.currentTimeMillis();
		log.info("MongoDB query took " + (time2-time1) + " msecs for " + results.size() + " aims in projectID:" + projectID);
		return results;
	}

	private static int getNumberOfAIMAnnotations(AIMSearchType valueType, String value, String username)
	{
		return 1; // TODO This call is too slow if we have a lot of data on an ePAD instance so we disable for the moment.
		// return getCountAIMImageAnnotations(valueType, value, username);
	}

	@SuppressWarnings("unused")
//	private static int getCountAIMImageAnnotations(AIMSearchType aimSearchType, String value, String username)
//	{
//		int count = 0;
//
//		log.info("AIM count query with search type " + aimSearchType + ", value " + value + ", username " + username);
//
//		if (aimSearchType == AIMSearchType.PERSON_NAME) {
//			String personName = value;
//			try {
//				count = AnnotationGetter.getCountImageAnnotationByPersonNameEqual(eXistServerUrl, aimNamespace,
//						eXistCollection, eXistUsername, eXistPassword, personName);
//			} catch (Exception e) {
//				log.warning("Exception in AnnotationGetter.getCountImageAnnotationByPersonNameEqual " + personName, e);
//			}
//		} else if (aimSearchType == AIMSearchType.PATIENT_ID) {
//			String patientId = value;
//			try {
//				count = AnnotationGetter.getCountImageAnnotationByPersonIdAndUserNameEqual(eXistServerUrl, aimNamespace,
//						eXistCollection, eXistUsername, eXistPassword, patientId, username);
//			} catch (Exception e) {
//				log.warning("Exception in AnnotationGetter.getCountImageAnnotationByPersonIdEqual " + patientId, e);
//			}
//		} else if (aimSearchType == AIMSearchType.SERIES_UID) {
//			String seriesUID = value;
//			try {
//				count = AnnotationGetter.getCountImageAnnotationByImageSeriesInstanceUidEqual(eXistServerUrl, aimNamespace,
//						eXistCollection, eXistUsername, eXistPassword, seriesUID);
//			} catch (Exception e) {
//				log.warning("Exception in AnnotationGetter.getCountImageAnnotationByImageSeriesInstanceUIDEqual " + seriesUID,
//						e);
//			}
//		} else if (aimSearchType == AIMSearchType.ANNOTATION_UID) {
//			String annotationUID = value;
//			if (value.equals("all")) {
//				try {
//					count = AnnotationGetter.getCountImageAnnotationByUserLoginNameContains(eXistServerUrl, aimNamespace,
//							eXistCollection, eXistUsername, eXistPassword, username);
//				} catch (Exception e) {
//					log.warning("Exception in AnnotationGetter.getImageAnnotationsFromServerWithAimQuery ", e);
//				}
//			} else {
//				try {
//					count = AnnotationGetter.getCountImageAnnotationByUniqueIdentifierEqual(eXistServerUrl, aimNamespace,
//							eXistCollection, eXistUsername, eXistPassword, annotationUID);
//				} catch (Exception e) {
//					log.warning("Exception in AnnotationGetter.getCountImageAnnotationByUniqueIdentifier " + annotationUID, e);
//				}
//			}
//		} else {
//			log.warning("Unknown AIM search type " + aimSearchType.getName());
//			count = 0;
//		}
//		log.info("Number of annotations " + count);
//		return count;
//	}

	public static List<ImageAnnotationCollection> getAllVersions(EPADAIM aim) throws Exception
	{
		String collection4Name = eXistCollectionV4 + "/" + aim.projectID;
		ImageAnnotationCollection iac = edu.stanford.hakan.aim4api.usage.AnnotationGetter
				.getImageAnnotationCollectionByUniqueIdentifier(eXistServerUrl, aim4Namespace, collection4Name,
						eXistUsername, eXistPassword, aim.aimID);
		log.info("Getting all versions for aim id:" + aim.aimID);
		AuditTrailManager atm = new AuditTrailManager(eXistServerUrl, aim4Namespace, collection4Name, eXistUsername, eXistPassword, xsdFilePath4);
		List<ImageAnnotationCollection> iacs = atm.getListAllVersions(iac);
		log.info("Aim Api returned " + iacs.size() + " annotations");
		return iacs;
	}

	public static List<ImageAnnotationCollection> getPreviousVersions(EPADAIM aim) throws Exception
	{
		String collection4Name = eXistCollectionV4 + "/" + aim.projectID;
		ImageAnnotationCollection iac = edu.stanford.hakan.aim4api.usage.AnnotationGetter
				.getImageAnnotationCollectionByUniqueIdentifier(eXistServerUrl, aim4Namespace, collection4Name,
						eXistUsername, eXistPassword, aim.aimID);
		
		AuditTrailManager atm = new AuditTrailManager(eXistServerUrl, aim4Namespace, collection4Name, eXistUsername, eXistPassword, xsdFilePath4);
		return atm.getUndoList(iac);
	}
	
	public static List<ImageAnnotationCollection> getNextVersions(EPADAIM aim) throws Exception
	{
		String collection4Name = eXistCollectionV4 + "/" + aim.projectID;
		ImageAnnotationCollection iac = edu.stanford.hakan.aim4api.usage.AnnotationGetter
				.getImageAnnotationCollectionByUniqueIdentifier(eXistServerUrl, aim4Namespace, collection4Name,
						eXistUsername, eXistPassword, aim.aimID);
		
		AuditTrailManager atm = new AuditTrailManager(eXistServerUrl, aim4Namespace, collection4Name, eXistUsername, eXistPassword, xsdFilePath4);
		return atm.getRedoList(iac);
	}
	

	public static ImageAnnotationCollection makeCurrent(EPADAIM aim, ImageAnnotationCollection iac) throws Exception
	{
		String collection4Name = eXistCollectionV4 + "/" + aim.projectID;		
		AuditTrailManager atm = new AuditTrailManager(eXistServerUrl, aim4Namespace, collection4Name, eXistUsername, eXistPassword, xsdFilePath4);
		return atm.makeCurrent(iac);
	}
	
}
