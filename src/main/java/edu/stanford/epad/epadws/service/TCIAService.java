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
package edu.stanford.epad.epadws.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.Encryption;
import edu.stanford.epad.dtos.RemotePACEntity;
import edu.stanford.epad.dtos.TaskStatus;

/**
 * Class to query TCIA data. See https://wiki.cancerimagingarchive.net/display/Public/Wiki
 * 
 * @author Dev Gude
 *
 */
public class TCIAService  {
	
	static final EPADLogger log = EPADLogger.getInstance();

	static TCIAService tciainstance;
	
	public static final String TCIA_URL = "TCIA_URL";
	public static final String TCIA_APIKEY = "TCIA_APIKEY";
	public static final String TCIA_PREFIX = "tcia:";
	public static final String TCIA_SHAREDLISTS = "TCIA_SHAREDLISTS";
	
	public static String apiKey = null;	
	public static List<String> collections = null;
	public static Set<String> sharedLists = new LinkedHashSet<String>();
	
	public static TCIAService getInstance() throws Exception {
		if (tciainstance == null)
		{
			tciainstance = new TCIAService();
		}
		return tciainstance;
	}
	
	private TCIAService() throws Exception {
		String key = EPADConfig.getParamValue(TCIA_APIKEY);
		log.info("key:" + key + " len:" + key.length());
		if (key.length() == 64)
		{
			Encryption enc = new Encryption(EPADConfig.xnatServer);
			apiKey = enc.decrypt(key);
		}
		else
			apiKey = key;
		
		String sharedListsConfig = EPADConfig.getParamValue(TCIA_SHAREDLISTS);
		if (sharedListsConfig != null && sharedListsConfig.trim().length() > 0)
		{
			String[] lists = sharedListsConfig.split(",");
			for (String list: lists)
				sharedLists.add(list);
		}
	}

	public static long downtime = 0;
	public List<String> getCollections() throws Exception{
		if (collections == null) {
			if (downtime != 0 && ((System.currentTimeMillis() - downtime) < 4*3600*1000))
				return collections;
			JsonArray collArray = getResponseFromTCIA("getCollectionValues");
			downtime = 0;
			collections = new ArrayList<String>();
			for (int i = 0; i < collArray.size(); i++)
				collections.add(collArray.get(i).getAsJsonObject().get("Collection").getAsString());
			log.info("Collections:" + collections);
			if (collections.size() < 2)
				collections = null;
		}
		return collections;
	}

	public Set<String> getSharedLists()
	{
		return sharedLists;
	}
	
	public List<RemotePACEntity> getPatientsForCollection(String collection) throws Exception
	{
		List<RemotePACEntity> entities = new ArrayList<RemotePACEntity>();
		if (sharedLists.contains(collection))
		{
			String entityID = "TCIA" + ":" + collection + ":SUBJECT:UNKNOWN"; 
			RemotePACEntity entity = new RemotePACEntity("Patient", "UNKNOWN PATIENT", 1,entityID);
			entity.subjectID = "UNKNOWN PATIENT";
			entity.subjectName = "UNKNOWN PATIENT";
			entities.add(entity);
			return entities;
		}
		
		JsonArray patients = this.getResponseFromTCIA("getPatient?Collection=" + collection);
		for (int i = 0; i < patients.size(); i++)
		{
			JsonObject patient = patients.get(i).getAsJsonObject();
			String patientID = getJsonString(patient.get("PatientID"));
			String patientName = getJsonString(patient.get("PatientName"));
			if (patientName.length() == 0)
				patientName = patientID;
			//String patientGender = patient.get("PatientSex"));
			//String ethnicity = patient.get("EthnicGroup"));
			String entityID = "TCIA" + ":" + collection + ":SUBJECT:" + patientID; 
			RemotePACEntity entity = new RemotePACEntity("Patient", patientName, 1,entityID);
			entity.subjectID = patientID;
			entity.subjectName = patientName;
			entities.add(entity);
		}

		return entities;
	}

	public List<RemotePACEntity> getNewStudiesForPatient(String collection, String patientID, Date sinceDate) throws Exception
	{
		if (sharedLists.contains(collection))
			return new ArrayList<RemotePACEntity>();
		List<RemotePACEntity> entities = new ArrayList<RemotePACEntity>();
		JsonArray studies = this.getResponseFromTCIA("NewStudiesInPatientCollection?Collection=" + collection 
				+ "&PatientID=" + patientID + "&Date=" + new SimpleDateFormat("yyyy-MM-dd").format(sinceDate));
		for (int i = 0; i < studies.size(); i++)
		{
			JsonObject study = studies.get(i).getAsJsonObject();
			String patientName = getJsonString(study.get("PatientName"));
			String patientGender = getJsonString(study.get("PatientSex"));
			String ethnicity = getJsonString(study.get("EthnicGroup"));
			String studyUID = getJsonString(study.get("StudyInstanceUID"));
			String description = getJsonString(study.get("StudyDescription"));
			String studyDate = getJsonString(study.get("StudyDate"));
			int seriesCount = study.get("SeriesCount").getAsInt();
			if (description.length() == 0)
				description = studyUID + " " + studyDate;
			String patientAge = getJsonString(study.get("PatientAge"));
			String entityID = "TCIA" + ":" + collection + ":" + patientID + ":STUDY:" + studyUID; 
			RemotePACEntity entity = new RemotePACEntity("Study", description, 2, entityID);
			entity.subjectID = patientID;
			entity.subjectName = patientName;
			entities.add(entity);
		}
		return entities;
	}
	
	public List<RemotePACEntity> getStudiesForPatient(String collection, String patientID) throws Exception
	{
		List<RemotePACEntity> entities = new ArrayList<RemotePACEntity>();
		if (sharedLists.contains(collection))
		{
			String entityID = "TCIA" + ":" + collection + ":SUBJECT:UNKNOWN:STUDY:UNKNOWN"; 
			RemotePACEntity entity = new RemotePACEntity("Study", "UNKNOWN STUDY", 2,entityID);
			entity.subjectID = "UNKNOWN";
			entity.subjectName = "UNKNOWN";
			entity.studyDate = "";
			entity.studyDescription = "";
			entities.add(entity);
			return entities;
		}

		JsonArray studies = this.getResponseFromTCIA("getPatientStudy?Collection=" + collection + "&PatientID=" + patientID);
		for (int i = 0; i < studies.size(); i++)
		{
			JsonObject study = studies.get(i).getAsJsonObject();
			String patientName = getJsonString(study.get("PatientName"));
			String patientGender = getJsonString(study.get("PatientSex"));
			String ethnicity = getJsonString(study.get("EthnicGroup"));
			String studyUID = getJsonString(study.get("StudyInstanceUID"));
			String description = getJsonString(study.get("StudyDescription"));
			String studyDate = getJsonString(study.get("StudyDate"));
			int seriesCount = study.get("SeriesCount").getAsInt();
			if (description.length() == 0)
				description = studyUID + " " + studyDate;
			String patientAge = getJsonString(study.get("PatientAge"));
			String entityID = "TCIA" + ":" + collection + ":" + patientID + ":STUDY:" + studyUID; 
			RemotePACEntity entity = new RemotePACEntity("Study", description, 2, entityID);
			entity.subjectID = patientID;
			entity.subjectName = patientName;
			entity.studyDate = studyDate;
			entity.studyDescription = description;
			entities.add(entity);
		}
		return entities;
	}

	public List<RemotePACEntity> getSeriesForStudy(String collection, String patientID, String studyUID) throws Exception
	{
		if (sharedLists.contains(collection))
			return getSeriesForSharedLists(collection);
		List<RemotePACEntity> entities = new ArrayList<RemotePACEntity>();
		JsonArray seriess = this.getResponseFromTCIA("getSeries?Collection=" + collection 
									+ "&PatientID=" + patientID + "&StudyInstanceUID=" + studyUID);
		for (int i = 0; i < seriess.size(); i++)
		{
			JsonObject series = seriess.get(i).getAsJsonObject();
			String seriesUID = getJsonString(series.get("SeriesInstanceUID"));
			String modality = getJsonString(series.get("Modality"));
			String seriesDate = getJsonString(series.get("SeriesDate"));
			String description = getJsonString(series.get("SeriesDescription"));			
			String bodyPart = getJsonString(series.get("BodyPartExamined"));
			String seriesNumber = getJsonString(series.get("SeriesNumber"));
			String protocol = getJsonString(series.get("ProtocolName"));
			String manufacturer = getJsonString(series.get("Manufacturer"));
			String model = getJsonString(series.get("ManufacturerModelName"));
			String softwareVersion = getJsonString(series.get("SoftwareVersions"));
			int imageCount = series.get("ImageCount").getAsInt();
			if (description.length() == 0)
				description = bodyPart + " " + modality + " " + seriesDate;
			
			String entityID = "TCIA" + ":" + collection + ":" + patientID + ":" + studyUID + ":SERIES:" + seriesUID; 
			RemotePACEntity entity = new RemotePACEntity("Series", description, 3, entityID);
			entities.add(entity);
		}
		return entities;
	}
	
	public List<RemotePACEntity> getSeriesForSharedLists(String collection) throws Exception
	{
		String query = "ContentsByName?name=" + collection;
		List<RemotePACEntity> entities = new ArrayList<RemotePACEntity>();
		JsonArray seriess = this.getResponseFromTCIA(query, true);
		for (int i = 0; i < seriess.size(); i++)
		{
			JsonObject series = seriess.get(i).getAsJsonObject();
			String seriesUID = getJsonString(series.get("SERIES_INSTANCE_UID"));
			
			String entityID = "TCIA" + ":" + collection + ":::SERIES:" + seriesUID; 
			RemotePACEntity entity = new RemotePACEntity("Series", seriesUID, 3, entityID);
			entities.add(entity);
			entity.subjectID = seriesUID;
			entity.subjectName = seriesUID;
		}
		return entities;
	}
	
	private String getJsonString(JsonElement elem)
	{
		if (elem == null)
			return "";
		else
			return elem.getAsString();
	}
	
	public int downloadPatientFromTCIA(String username, String collection, String patientID, String projectID)
			throws Exception
	{
		if (sharedLists.contains(collection))
			return downloadSeriesFromTCIA(username, patientID, projectID); // Shared Lists only have SeriesUID
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_TCIA_DOWNLOAD, patientID, "Started download", new Date(), null);
		List<RemotePACEntity> entities = getStudiesForPatient(collection, patientID);
		for (RemotePACEntity entity: entities)
		{
			String studyUID = entity.entityID;
			if (studyUID.indexOf(":") != -1)
				studyUID = studyUID.substring(studyUID.lastIndexOf(":")+1);
			downloadStudyFromTCIA(username, collection, patientID, studyUID, projectID);
		}
		projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_TCIA_DOWNLOAD, patientID, "Completed download", null, new Date());
		return HttpServletResponse.SC_OK;
	}
	
	public int downloadStudyFromTCIA(String username, String collection, String patientID, String studyUID, String projectID)
			throws Exception
	{
		if (sharedLists.contains(collection))
			return downloadSeriesFromTCIA(username, patientID, projectID);
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_TCIA_DOWNLOAD, "Patient:" + patientID + ", Study:" + studyUID, "Started download", null, null);
		List<RemotePACEntity> entities = getSeriesForStudy(collection, patientID, studyUID);
		for (RemotePACEntity entity: entities)
		{
			String seriesUID = entity.entityID;
			if (seriesUID.indexOf(":") != -1)
				seriesUID = seriesUID.substring(seriesUID.lastIndexOf(":")+1);
			downloadSeriesFromTCIA(username, seriesUID, projectID);
		}
		projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_TCIA_DOWNLOAD, "Patient:" + patientID + ", Study:" + studyUID, "Completed download", null, new Date());
		return HttpServletResponse.SC_OK;
	}
	
	public static int downloadSeriesFromTCIA(String username, String seriesUID, String projectID)
			throws Exception
	{
		String tciaURL = EPADConfig.getParamValue(TCIA_URL, "https://services.cancerimagingarchive.net/services/v3/TCIA/query/getImage");
		tciaURL = tciaURL + "?SeriesInstanceUID=" + seriesUID;
		tciaURL = tciaURL + "&api_key=" + apiKey;
		log.debug("TCIA Download URL:" + tciaURL);
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(tciaURL);
		int statusCode = client.executeMethod(method);
		File uploadStoreDir = new File(EPADConfig.getEPADWebServerUploadDir()
													+ "temp" + System.currentTimeMillis());
		uploadStoreDir.mkdirs();
		File zipfile = new File(uploadStoreDir, "tcia.zip");
		long total = 0;
		if (statusCode == HttpServletResponse.SC_OK) {
			OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(zipfile);
				InputStream inputStream = method.getResponseBodyAsStream();
				int read = 0;
				byte[] bytes = new byte[4096];
				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
					total = total + read;
				}
			} finally {
				IOUtils.closeQuietly(outputStream);
				method.releaseConnection();
			}
			log.debug("TCIA download:" + total + " bytes");
			writePropertiesFile(uploadStoreDir, projectID, "", username);
		}
		else {
			log.warning("TCIA URL:" + tciaURL + " Status:" + statusCode);
		}
		return statusCode;
	}

	private JsonArray getResponseFromTCIA(String query) throws Exception
	{
		return getResponseFromTCIA(query, false);
	}

	private JsonArray getResponseFromTCIA(String query, boolean sharedList) throws Exception
	{
		String tciaURL = EPADConfig.getParamValue(TCIA_URL, "https://services.cancerimagingarchive.net/services/v3/TCIA/query/");
		if (sharedList)
			tciaURL = "https://services.cancerimagingarchive.net/services/v3/SharedList/query/";
		tciaURL = tciaURL + query;
		if (tciaURL.indexOf('?') == -1) 
			tciaURL = tciaURL  + "?";
		else
			tciaURL = tciaURL  + "&";
		tciaURL = tciaURL.replace(' ', '+') + "api_key=" + apiKey;
		log.debug("TCIA URL:" + tciaURL);
		HttpClient client = new HttpClient();
		try {
			GetMethod method = new GetMethod(tciaURL);
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpServletResponse.SC_OK) {
				InputStream is = method.getResponseBodyAsStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			    StringBuilder sb = new StringBuilder();
	
			    String line = null;
			    try {
			        while ((line = reader.readLine()) != null) {
			            sb.append(line + "\n");
			        }
			    } catch (IOException e) {
		            log.warning("Error reading response", e);;
			    } finally {
			        try {
			            is.close();
			        } catch (IOException e) {
			        }
			    }
			    String response = sb.toString();
				log.debug("TCIA response:" + response.length());
				//log.debug("TCIA Response:" + response);
			    JsonParser parser = new JsonParser();
			    return parser.parse(response).getAsJsonArray();
			} else {
				log.warning("TCIA URL:" + tciaURL + " Status:" + statusCode);
				downtime = System.currentTimeMillis();
				throw new Exception("Error calling TCIA, status = " + statusCode);
			}
		}
		catch (Exception x) {
			downtime = System.currentTimeMillis();
			log.warning("Error calling TCIA url:" + tciaURL, x);
			throw x;
		}
		
	}
	// add the properties file xnat_upload.properties.
	private static void writePropertiesFile(File storeDir, String project,
			String session, String user) {

		String projectName = "XNATProjectName=" + project;
		String sessionName = "XNATSessionID=" + session;
		String userName = "XNATUserName=" + user;

		try {
			File properties = new File(storeDir, "xnat_upload.properties");
			if (!properties.exists()) {
				properties.createNewFile();
			}
			FileOutputStream fop = new FileOutputStream(properties, false);

			fop.write(projectName.getBytes());
			fop.write("\n".getBytes());
			fop.write(sessionName.getBytes());
			fop.write("\n".getBytes());
			fop.write(userName.getBytes());
			fop.write("\n".getBytes());

			fop.flush();
			fop.close();

		} catch (IOException e) {
			log.info("Error writing properties file");
			e.printStackTrace();
		}
	}
	
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
	private Date getDate(String dateStr)
	{
		try
		{
			return dateformat.parse(dateStr);
		}
		catch (Exception x)
		{
			return null;
		}
	}
}
