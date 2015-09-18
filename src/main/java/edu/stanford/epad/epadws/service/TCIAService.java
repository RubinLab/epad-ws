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
import java.util.List;

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
	
	public static String apiKey = null;	
	public static List<String> collections = null;
	
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
	}

	public List<String> getCollections() throws Exception{
		if (collections == null) {
			JsonArray collArray = getResponseFromTCIA("getCollectionValues");
			collections = new ArrayList<String>();
			for (int i = 0; i < collArray.size(); i++)
				collections.add(collArray.get(i).getAsJsonObject().get("Collection").getAsString());
			log.info("Collections:" + collections);
			if (collections.size() < 2)
				collections = null;
		}
		return collections;
	}

	public List<RemotePACEntity> getPatientsForCollection(String collection) throws Exception
	{
		List<RemotePACEntity> entities = new ArrayList<RemotePACEntity>();
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
		String tciaURL = EPADConfig.getParamValue(TCIA_URL, "https://services.cancerimagingarchive.net/services/v3/TCIA/query/");
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
				throw new Exception("Error calling TCIA, status = " + statusCode);
			}
		}
		catch (Exception x) {
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
