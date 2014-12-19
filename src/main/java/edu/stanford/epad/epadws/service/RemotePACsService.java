package edu.stanford.epad.epadws.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pixelmed.dicom.AgeStringAttribute;
import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.CodeStringAttribute;
import com.pixelmed.dicom.DateAttribute;
import com.pixelmed.dicom.DecimalStringAttribute;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.IntegerStringAttribute;
import com.pixelmed.dicom.LongStringAttribute;
import com.pixelmed.dicom.PersonNameAttribute;
import com.pixelmed.dicom.ShortStringAttribute;
import com.pixelmed.dicom.SpecificCharacterSet;
import com.pixelmed.dicom.TagFromName;
import com.pixelmed.dicom.TimeAttribute;
import com.pixelmed.dicom.UniqueIdentifierAttribute;
import com.pixelmed.network.ApplicationEntity;
import com.pixelmed.network.ApplicationEntityMap;
import com.pixelmed.query.QueryTreeModel;
import com.pixelmed.query.QueryTreeRecord;

import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.dtos.RemotePACEntity;

public class RemotePACsService extends RemotePACSBase {

	static RemotePACsService rpsinstance;
	
	public static final int MAX_CACHE_ENTRIES = 25000;
	public static final int MAX_QUERY_SIZE = 7000;
	static Map<String, QueryTreeRecord> remoteQueryCache = new HashMap<String, QueryTreeRecord>();
	
	public static Map<String, String> pendingTransfers = new HashMap<String, String>();
	
	public static RemotePACsService getInstance() throws Exception {
		if (rpsinstance == null)
		{
			rpsinstance = new RemotePACsService();
		}
		return rpsinstance;
	}
	
	private RemotePACsService() throws DicomException, IOException {
		super();
	}

	public List<RemotePAC> getRemotePACs() {
		List<RemotePAC> rps = new ArrayList<RemotePAC>();
		ApplicationEntityMap aeMap = networkApplicationInformation.getApplicationEntityMap();
		for (Object aeName: aeMap.keySet()) {
			ApplicationEntity ae = (ApplicationEntity) aeMap.get(aeName);
			String localName = networkApplicationInformation.getLocalNameFromApplicationEntityTitle(aeName.toString());
			RemotePAC rp = new RemotePAC(localName, ae.getDicomAETitle(), ae.getPresentationAddress().getHostname(),
					ae.getPresentationAddress().getPort(), ae.getQueryModel(), ae.getPrimaryDeviceType());
			rps.add(rp);
		}
		return rps;
	}

	public RemotePAC getRemotePAC(String pacID) {
		ApplicationEntityMap aeMap = networkApplicationInformation.getApplicationEntityMap();
		for (Object aeName: aeMap.keySet()) {
			ApplicationEntity ae = (ApplicationEntity) aeMap.get(aeName);
			String localName = networkApplicationInformation.getLocalNameFromApplicationEntityTitle(aeName.toString());
			if (localName.equals(pacID))
			{
				return new RemotePAC(localName, ae.getDicomAETitle(), ae.getPresentationAddress().getHostname(),
					ae.getPresentationAddress().getPort(), ae.getQueryModel(), ae.getPrimaryDeviceType());
			}
		}
		return null;
	}
	
	public void addRemotePAC(RemotePAC pac) throws Exception {
		addRemotePAC(
				pac.pacID,
				pac.aeTitle,
				pac.hostname,
				pac.port,
				pac.queryModel,
				pac.primaryDeviceType);
	}
	
	public void modifyRemotePAC(RemotePAC pac) throws Exception {
		removeRemotePAC(pac.pacID);
		addRemotePAC(pac);
	}
	
	public void removeRemotePAC(RemotePAC pac) throws Exception {
		removeRemotePAC(pac.pacID);
		this.storeProperties(pac.pacID + " deleted by EPAD " + new Date());
	}
	
	public synchronized List<RemotePACEntity> queryRemoteData(RemotePAC pac, String patientNameFilter, String patientIDFilter, String studyDateFilter) throws Exception {
		
		try {
			this.setCurrentRemoteQueryInformationModel(pac.pacID, true);
			SpecificCharacterSet specificCharacterSet = new SpecificCharacterSet((String[])null);
			AttributeList filter = new AttributeList();
			{
				AttributeTag t = TagFromName.PatientName; Attribute a = new PersonNameAttribute(t,specificCharacterSet);
				if (patientNameFilter != null && patientNameFilter.length() > 0) {
					a.addValue(patientNameFilter);
				}
				filter.put(t,a);
			}
			{
				AttributeTag t = TagFromName.PatientID; Attribute a = new ShortStringAttribute(t,specificCharacterSet);
				if (patientIDFilter != null && patientIDFilter.length() > 0) {
					a.addValue(patientIDFilter);
				}
				filter.put(t,a);
			}
			{ AttributeTag t = TagFromName.PatientBirthDate; Attribute a = new DateAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.PatientSex; Attribute a = new CodeStringAttribute(t); filter.put(t,a); }

			{ AttributeTag t = TagFromName.StudyID; Attribute a = new ShortStringAttribute(t,specificCharacterSet); filter.put(t,a); }
			{ AttributeTag t = TagFromName.StudyDescription; Attribute a = new LongStringAttribute(t,specificCharacterSet); filter.put(t,a); }
			{ AttributeTag t = TagFromName.ModalitiesInStudy; Attribute a = new CodeStringAttribute(t); filter.put(t,a); }
			{
				AttributeTag t = TagFromName.StudyDate; Attribute a = new DateAttribute(t);
				if (studyDateFilter != null && studyDateFilter.length() > 0) {
					a.addValue(studyDateFilter);
				}
				filter.put(t,a);
			}
			{ AttributeTag t = TagFromName.StudyTime; Attribute a = new TimeAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.PatientAge; Attribute a = new AgeStringAttribute(t); filter.put(t,a); }

			{ AttributeTag t = TagFromName.SeriesDescription; Attribute a = new LongStringAttribute(t,specificCharacterSet); filter.put(t,a); }
			{ AttributeTag t = TagFromName.SeriesNumber; Attribute a = new IntegerStringAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.Manufacturer; Attribute a = new LongStringAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.Modality; Attribute a = new CodeStringAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.SeriesDate; Attribute a = new DateAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.SeriesTime; Attribute a = new TimeAttribute(t); filter.put(t,a); }

			{ AttributeTag t = TagFromName.InstanceNumber; Attribute a = new IntegerStringAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.ContentDate; Attribute a = new DateAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.ContentTime; Attribute a = new TimeAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.ImageType; Attribute a = new CodeStringAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.NumberOfFrames; Attribute a = new IntegerStringAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.WindowCenter; Attribute a = new DecimalStringAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.WindowWidth; Attribute a = new DecimalStringAttribute(t); filter.put(t,a); }

			{ AttributeTag t = TagFromName.StudyInstanceUID; Attribute a = new UniqueIdentifierAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.SeriesInstanceUID; Attribute a = new UniqueIdentifierAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.SOPInstanceUID; Attribute a = new UniqueIdentifierAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.SOPClassUID; Attribute a = new UniqueIdentifierAttribute(t); filter.put(t,a); }
			{ AttributeTag t = TagFromName.SpecificCharacterSet; Attribute a = new CodeStringAttribute(t); filter.put(t,a); a.addValue("ISO_IR 100"); }
			
			if (remoteQueryCache.keySet().size() > MAX_CACHE_ENTRIES)
				clearQueryCache();
			List<RemotePACEntity> remoteEntities = new ArrayList<RemotePACEntity>();
			String key = pac.pacID;
			QueryTreeModel treeModel = currentRemoteQueryInformationModel.performHierarchicalQuery(filter);
			QueryTreeRecord root = (QueryTreeRecord) treeModel.getRoot();
			remoteEntities = traverseTree(root, 0, remoteEntities, key);
			log.info("Number of entities returned:" + remoteEntities.size());
			return remoteEntities;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void clearQueryCache()
	{
		remoteQueryCache = new HashMap<String, QueryTreeRecord>();
	}
	
	DecimalFormat decformat = new DecimalFormat("00000");
	private List<RemotePACEntity> traverseTree(QueryTreeRecord node, int level, List<RemotePACEntity> entities, String key) {
		AttributeList al = node.getAllAttributesReturnedInIdentifier();
		Object tags = null;
		String ID = null;
		String IDtype = "Patient";
		if (al != null)
		{
			tags = al.keySet();
			ID = Attribute.getSingleStringValueOrNull(al,TagFromName.PatientID);
			if (ID == null)
			{
				ID = Attribute.getSingleStringValueOrNull(al,TagFromName.StudyID);
				IDtype = "Study";
			}
			if (ID == null)
			{
				ID = Attribute.getSingleStringValueOrNull(al,TagFromName.SeriesInstanceUID);
				IDtype = "Series";
			}
			if (ID == null)
			{
				ID = Attribute.getSingleStringValueOrNull(al,TagFromName.SOPInstanceUID);
				IDtype = "Instance";
			}
		}
		log.debug("Remote Query - Level:" + level + " Type:" + node.getInformationEntity() + " Value:" + node.getValue() + " entities:" + entities.size()
		+ " " + IDtype + ":" + ID);
		String type = "";
		if (node.getInformationEntity() != null) 
		{
			type = node.getInformationEntity().toString();
		}
		else if (entities.size() == 0)
		{
			type = "AE"; 
		}

		String ukey = key;
		if (entities.size() > 0)
		{
			ukey = entities.get(0).entityID + ":" + node.getUniqueKey().getDelimitedStringValuesOrEmptyString(); // Is this key better???
		}
		RemotePACEntity entity = new RemotePACEntity(type, node.getValue(), level, ukey);
		remoteQueryCache.put(ukey, node);
		entities.add(entity);
		
		if (entities.size() >= MAX_QUERY_SIZE)
			return entities;
		
		int n = ((QueryTreeRecord)node).getChildCount();
		
		for (int i = 0; i < n; i++) {
			traverseTree((QueryTreeRecord)((QueryTreeRecord)node).getChildAt(i), level+1, entities, key + ":" + decformat.format(i));
		}
		return entities;
	}
	
	public synchronized void retrieveRemoteData(RemotePAC pac, String entityID, String projectID, String userName, String sessionID) throws Exception {
		String uniqueKey = entityID;
		String root = uniqueKey;
		if (root.indexOf(":") != -1)
			root = root.substring(0, root.indexOf(":"));
		QueryTreeRecord node = remoteQueryCache.get(uniqueKey);
		// If no cached pointers, query entire PAC again (or should we give an error???)
		if (node == null)
		{
			queryRemoteData(pac, "", "", "");
			node = remoteQueryCache.get(uniqueKey);
			if (node == null)
				throw new Exception("Remote data not found");
		}
		String type = node.getInformationEntity().toString();
		String studyUID = null;
		String seriesUID = null;
		String patientID = null;
		String patientName = "";
		if (!type.equalsIgnoreCase("Study"))
		{
			if (type.equalsIgnoreCase("Series"))
				seriesUID = studyUID = node.getUniqueKey().getSingleStringValueOrNull();
			QueryTreeRecord parent = (QueryTreeRecord) node.getParent();
			if (parent != null)
			{
				type = parent.getInformationEntity().toString();
				if (!type.equalsIgnoreCase("Study"))
				{
					if (type.equalsIgnoreCase("Series"))
						seriesUID = studyUID = parent.getUniqueKey().getSingleStringValueOrNull();
					parent = (QueryTreeRecord) parent.getParent();
					type = parent.getInformationEntity().toString();
					if (type.equalsIgnoreCase("Study"))
					{
						studyUID = parent.getUniqueKey().getSingleStringValueOrNull();
						patientID = Attribute.getSingleStringValueOrNull(parent.getAllAttributesReturnedInIdentifier(),TagFromName.PatientID);
						patientName = Attribute.getSingleStringValueOrEmptyString(parent.getAllAttributesReturnedInIdentifier(),TagFromName.PatientName);
					}
				}
				else
				{
					studyUID = parent.getUniqueKey().getSingleStringValueOrNull();
					patientID = Attribute.getSingleStringValueOrNull(parent.getAllAttributesReturnedInIdentifier(),TagFromName.PatientID);
					patientName = Attribute.getSingleStringValueOrEmptyString(parent.getAllAttributesReturnedInIdentifier(),TagFromName.PatientName);
				}
			}
		}
		else
		{
			studyUID = uniqueKey;
			patientID = Attribute.getSingleStringValueOrNull(node.getAllAttributesReturnedInIdentifier(),TagFromName.PatientID);
			patientName = Attribute.getSingleStringValueOrEmptyString(node.getAllAttributesReturnedInIdentifier(),TagFromName.PatientName);
		}
		if (studyUID != null && patientID != null && projectID != null && projectID.trim().length() > 0)
		{
			UserProjectService.addSubjectAndStudyToProject(patientID, patientName, studyUID, projectID, sessionID, userName);
		}
		if (seriesUID != null)
			pendingTransfers.put(seriesUID, userName + ":" + projectID);
		this.setCurrentRemoteQueryInformationModel(pac.pacID, false);
		if (node != null) {
			setCurrentRemoteQuerySelection(node.getUniqueKeys(), node.getUniqueKey(), node.getAllAttributesReturnedInIdentifier());
			log.info("Request retrieval of "+currentRemoteQuerySelectionLevel+" "+currentRemoteQuerySelectionUniqueKey.getSingleStringValueOrEmptyString()+" from "+pac.pacID+" ("+currentRemoteQuerySelectionRetrieveAE+")");
			performRetrieve(currentRemoteQuerySelectionUniqueKeys,currentRemoteQuerySelectionLevel,currentRemoteQuerySelectionRetrieveAE);			
		}
			
	}
	
	public List<String> getPendingTransfers()
	{
		List<String> transfers = new ArrayList<String>();
		for (String id: pendingTransfers.keySet())
		{
			String transfer = id + ":" + pendingTransfers.get(id);
			transfers.add(transfer);
		}
		return transfers;
	}
	
}
