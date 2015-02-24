package edu.stanford.epad.epadws.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.CodeStringAttribute;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.TagFromName;
import com.pixelmed.network.DicomNetworkException;
import com.pixelmed.network.NetworkApplicationInformation;
import com.pixelmed.network.NetworkApplicationInformationFederated;
import com.pixelmed.network.NetworkApplicationProperties;
import com.pixelmed.network.PresentationAddress;
import com.pixelmed.query.QueryInformationModel;
import com.pixelmed.query.QueryTreeBrowser;
import com.pixelmed.query.QueryTreeRecord;
import com.pixelmed.query.StudyRootQueryInformationModel;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;

/**
 * <p>This class is based on pixelmed class  retrieving DICOM studies of patients and downloading or transmitting them.</p>
 * 
 * The code has been somewhat hacked to remove the interactive part (See original edu.stanford.epad.common.pixelmed.DicomQR.java)
 * 
 * @author	camille kurtz, dclunie
 */
public class RemotePACSBase {
	
	protected static final EPADLogger log = EPADLogger.getInstance();

	protected NetworkApplicationProperties networkApplicationProperties;
	protected NetworkApplicationInformation networkApplicationInformation;

	protected QueryInformationModel currentRemoteQueryInformationModel;
	protected QueryTreeBrowser currentRemoteQueryTreeBrowser;

	protected QueryTreeRecord currentRemoteQuerySelectionQueryTreeRecord;
	protected AttributeList currentRemoteQuerySelectionUniqueKeys;
	protected Attribute currentRemoteQuerySelectionUniqueKey;
	protected String currentRemoteQuerySelectionRetrieveAE;
	protected String currentRemoteQuerySelectionLevel;

	protected String ourCalledAETitle;		// set when reading network properties; used not just in StorageSCP, but also when creating exported meta information headers
	
	private Properties applicationProperties;
	
	/**
	 * <p>Store the properties from the current properties file.</p>
	 */
	protected void loadProperties() throws IOException {
		applicationProperties = new Properties(/*defaultProperties*/);
		String whereFrom = EPADConfig.getEPADRemotePACsConfigFilePath();
		try {
			// load properties from last invocation
			FileInputStream in = new FileInputStream(whereFrom);
			applicationProperties.load(in);
			in.close();
		}
		catch (IOException e) {
			log.warning("Error loading RemotePACs properties", e);
			throw e;
		}
	}
	
	/**
	 * <p>Store the current properties in the current properties file.</p>
	 *
	 * @param	comment		the description to store as the header of the properties file
	 * @exception	IOException
	 */
	protected void storeProperties(String comment) throws IOException {
		String whereTo = EPADConfig.getEPADRemotePACsConfigFilePath();
		FileOutputStream out = new FileOutputStream(whereTo);
		applicationProperties.store(out,comment);
		out.close();
	}
		
	protected void addRemotePAC(String localname, String aeTitle, String hostname, int port, String queryModel, String primaryDeviceType) throws IOException, DicomNetworkException {
		networkApplicationInformation = networkApplicationProperties.getNetworkApplicationInformation();
		networkApplicationInformation.add(localname,
			aeTitle,
			hostname,
			port,
			queryModel,
			primaryDeviceType);
		log.debug("2 localNameToApplicationEntityTitleMap:" + networkApplicationInformation.getListOfLocalNamesOfApplicationEntities());
		applicationProperties = networkApplicationProperties.getProperties(getProperties());
		this.storeProperties(localname + " added by EPAD " + new Date());
	}
	
	protected void removeRemotePAC(String localname) throws IOException, DicomNetworkException {
		networkApplicationInformation = networkApplicationProperties.getNetworkApplicationInformation();
		networkApplicationInformation.remove(localname);
		applicationProperties = networkApplicationProperties.getProperties(getProperties());
		log.debug("4 localNameToApplicationEntityTitleMap:" + networkApplicationInformation.getListOfLocalNamesOfApplicationEntities());
		log.debug("4 applicationEntityTitleToLocalNameMap:" + networkApplicationInformation.getListOfApplicationEntityTitlesOfApplicationEntities());
		this.storeProperties(localname + " deleted by EPAD " + new Date());
	}
	
	/**
	 * <p>Get the properties for the application that have already been loaded (see {@link #loadProperties() loadProperties()}).</p>
	 *
	 * @return	the properties
	 */
	protected Properties getProperties() { return applicationProperties; }
	/**
	 * <p>Searches for the property with the specified key in the specified property list, insisting on a value.</p>
	 *
	 * @param	properties	the property list to search
	 * @param	key		the property name
	 * @throws	Exception	if there is no such property or it has no value
	 */
	static public String getPropertyInsistently(Properties properties,String key) throws Exception {
		String value = properties.getProperty(key);
		if (value == null || value.length() == 0) {
			throw new Exception("Properties do not contain value for "+key);
		}
		return value;
	} 

	/**
	 * <p>Searches for the property with the specified key in this application's property list, insisting on a value.</p>
	 *
	 * @param	key		the property name
	 * @throws	Exception	if there is no such property or it has no value
	 */
	public String getPropertyInsistently(String key) throws Exception {
		return getPropertyInsistently(applicationProperties,key);
	} 

	protected void setCurrentRemoteQueryInformationModel(String remoteAEForQuery, boolean infoOnly) throws Exception {
		currentRemoteQueryInformationModel=null;
		String stringForTitle="";
		if (remoteAEForQuery != null && remoteAEForQuery.length() > 0 && networkApplicationProperties != null && networkApplicationInformation != null) {
			try {
				String              queryCallingAETitle = networkApplicationProperties.getCallingAETitle();
				// If requesting data (ie dicom files) and not just info, send directly to dcm4che, not to epad server
				
				String               queryCalledAETitle = networkApplicationInformation.getApplicationEntityTitleFromLocalName(remoteAEForQuery);
				PresentationAddress presentationAddress = networkApplicationInformation.getApplicationEntityMap().getPresentationAddress(queryCalledAETitle);

				if (presentationAddress == null) {
					throw new Exception("For remote query AE <"+remoteAEForQuery+">, presentationAddress cannot be determined");
				}

				String                        queryHost = presentationAddress.getHostname();
				int			      queryPort = presentationAddress.getPort();
				if (!infoOnly) {
						// Send data directly to dcm4che
						// May need to change host ip if DCM4CHE is running on a separate machine
						queryPort = new Integer(EPADConfig.dicomServerPort);
				}
				String                       queryModel = networkApplicationInformation.getApplicationEntityMap().getQueryModel(queryCalledAETitle);
				int                     queryDebugLevel = networkApplicationProperties.getQueryDebugLevel();

				if (NetworkApplicationProperties.isStudyRootQueryModel(queryModel) || queryModel == null) {
					currentRemoteQueryInformationModel=new StudyRootQueryInformationModel(queryHost,queryPort,queryCalledAETitle,queryCallingAETitle,queryDebugLevel);
					stringForTitle=":"+remoteAEForQuery;
				}
				else {
					throw new Exception("For remote query AE <"+remoteAEForQuery+">, query model "+queryModel+" not supported");
				}
				//log.info("Remote AE set to:" + remoteAEForQuery);
			}
			catch (Exception e) {		// if an AE's property has no value, or model not supported
				e.printStackTrace(System.err);
				throw e;
			}
		}
	}

	// should be extracted to a utility class ... also used in DoseUtility and DoseReporterWithLegacyOCRAndAutoSendToRegistry :(
	public static String getQueryRetrieveLevel(AttributeList identifier,Attribute uniqueKey) {
		String level = null;
		if (identifier != null) {
			Attribute a = identifier.get(TagFromName.QueryRetrieveLevel);
			if (a != null) {
				level = a.getSingleStringValueOrNull();
			}
		}
		if (level == null) {
			// QueryRetrieveLevel must have been (erroneously) missing in query response ... see with Dave Harvey's code on public server
			// so try to guess it from unique key in tree record
			// Fixes [bugs.mrmf] (000224) Missing query/retrieve level in C-FIND response causes tree select and retrieve to fail
			if (uniqueKey != null) {
				AttributeTag tag = uniqueKey.getTag();
				if (tag != null) {
					if (tag.equals(TagFromName.PatientID)) {
						level="PATIENT";
					}
					else if (tag.equals(TagFromName.StudyInstanceUID)) {
						level="STUDY";
					}
					else if (tag.equals(TagFromName.SeriesInstanceUID)) {
						level="SERIES";
					}
					else if (tag.equals(TagFromName.SOPInstanceUID)) {
						level="IMAGE";
					}
				}
			}
			log.warning("DownloadOrTransmit.getQueryRetrieveLevel(): Guessed missing Query Retrieve Level to be "+level);
		}
		return level;
	}

	protected void setCurrentRemoteQuerySelection(AttributeList uniqueKeys,Attribute uniqueKey,AttributeList identifier) {
		currentRemoteQuerySelectionUniqueKeys=uniqueKeys;
		currentRemoteQuerySelectionUniqueKey=uniqueKey;
		currentRemoteQuerySelectionRetrieveAE=null;
		if (identifier != null) {
			Attribute aRetrieveAETitle=identifier.get(TagFromName.RetrieveAETitle);
			if (aRetrieveAETitle != null) currentRemoteQuerySelectionRetrieveAE=aRetrieveAETitle.getSingleStringValueOrNull();
		}
		if (currentRemoteQuerySelectionRetrieveAE == null) {
			// it is legal for RetrieveAETitle to be zero length at all but the lowest levels of
			// the query model :( (See PS 3.4 C.4.1.1.3.2)
			// (so far the Leonardo is the only one that doesn't send it at all levels)
			// we could recurse down to the lower levels and get the union of the value there
			// but lets just keep it simple and ...
			// default to whoever it was we queried in the first place ...
			if (currentRemoteQueryInformationModel != null) {
				currentRemoteQuerySelectionRetrieveAE=currentRemoteQueryInformationModel.getCalledAETitle();
			}
		}
		currentRemoteQuerySelectionLevel = null;
		if (identifier != null) {
			Attribute a = identifier.get(TagFromName.QueryRetrieveLevel);
			if (a != null) {
				currentRemoteQuerySelectionLevel = a.getSingleStringValueOrNull();
			}
		}
		if (currentRemoteQuerySelectionLevel == null) {
			// QueryRetrieveLevel must have been (erroneously) missing in query response ... see with Dave Harvey's code on public server
			// so try to guess it from unique key in tree record
			// Fixes [bugs.mrmf] (000224) Missing query/retrieve level in C-FIND response causes tree select and retrieve to fail
			if (uniqueKey != null) {
				AttributeTag tag = uniqueKey.getTag();
				if (tag != null) {
					if (tag.equals(TagFromName.PatientID)) {
						currentRemoteQuerySelectionLevel="PATIENT";
					}
					else if (tag.equals(TagFromName.StudyInstanceUID)) {
						currentRemoteQuerySelectionLevel="STUDY";
					}
					else if (tag.equals(TagFromName.SeriesInstanceUID)) {
						currentRemoteQuerySelectionLevel="SERIES";
					}
					else if (tag.equals(TagFromName.SOPInstanceUID)) {
						currentRemoteQuerySelectionLevel="IMAGE";
					}
				}
			}
			log.warning("RemotePACs.setCurrentRemoteQuerySelection(): Guessed missing currentRemoteQuerySelectionLevel to be "+currentRemoteQuerySelectionLevel);
		}
	}

	protected void performRetrieve(AttributeList uniqueKeys,String selectionLevel,String retrieveAE) {
		try {
			AttributeList identifier = new AttributeList();
			if (uniqueKeys != null) {
				identifier.putAll(uniqueKeys);
				{ AttributeTag t = TagFromName.QueryRetrieveLevel; Attribute a = new CodeStringAttribute(t); a.addValue(selectionLevel); identifier.put(t,a); }
				currentRemoteQueryInformationModel.performHierarchicalMoveFrom(identifier,retrieveAE);
			} else {			
			// else do nothing, since no unique key to specify what to retrieve
				log.warning("UniqueKeys for retrieval are null");
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public RemotePACSBase() throws DicomException, IOException {
		loadProperties();
		
		try {
			networkApplicationProperties = new NetworkApplicationProperties(getProperties(),true/*addPublicStorageSCPsIfNoRemoteAEsConfigured*/);
		}
		catch (Exception e) {
			networkApplicationProperties = null;
		}
		{
			NetworkApplicationInformationFederated federatedNetworkApplicationInformation = new NetworkApplicationInformationFederated();
			federatedNetworkApplicationInformation.startupAllKnownSourcesAndRegister(networkApplicationProperties);
			networkApplicationInformation = federatedNetworkApplicationInformation;
			log.info("networkApplicationInformation ...\n"+networkApplicationInformation);
		}		
	}
}
