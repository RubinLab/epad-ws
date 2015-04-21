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
	
	public int downloadStudyFromTCIA(String username, String collection, String patientID, String studyUID, String projectID)
			throws Exception
	{
		List<RemotePACEntity> entities = getSeriesForStudy(collection, patientID, studyUID);
		for (RemotePACEntity entity: entities)
		{
			String seriesUID = entity.entityID;
			if (seriesUID.indexOf(":") != -1)
				seriesUID = seriesUID.substring(seriesUID.lastIndexOf(":")+1);
			downloadSeriesFromTCIA(username, seriesUID, projectID);
		}
		return HttpServletResponse.SC_OK;
	}
	
	public static int downloadSeriesFromTCIA(String username, String seriesUID, String projectID)
			throws Exception
	{
		String tciaURL = EPADConfig.getParamValue(TCIA_URL, "https://services.cancerimagingarchive.net/services/v3/TCIA/query/getImage");
		tciaURL = tciaURL + "?SeriesInstanceUID=" + seriesUID;
		tciaURL = tciaURL + "&api_key=" + apiKey;
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(tciaURL);
		int statusCode = client.executeMethod(method);
		File uploadStoreDir = new File(EPADConfig.getEPADWebServerUploadDir()
													+ "temp" + System.currentTimeMillis());
		uploadStoreDir.mkdirs();
		File zipfile = new File(uploadStoreDir, "tcia.zip");

		if (statusCode == HttpServletResponse.SC_OK) {
			OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(zipfile);
				InputStream inputStream = method.getResponseBodyAsStream();
				int read = 0;
				byte[] bytes = new byte[4096];
				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
			} finally {
				IOUtils.closeQuietly(outputStream);
				method.releaseConnection();
			}
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
			log.info("Error writing prroperties file");
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
