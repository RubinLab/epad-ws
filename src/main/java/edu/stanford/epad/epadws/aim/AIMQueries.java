/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
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
		} else if (aimSearchType == AIMSearchType.AIM_QUERY) {
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
