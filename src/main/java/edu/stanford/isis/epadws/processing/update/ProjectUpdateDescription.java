package edu.stanford.isis.epadws.processing.update;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProjectUpdateDescription
{
	public final String projectID;
	public final Set<PatientUpdateDescription> patientUpdates;
	private final Map<String, PatientUpdateDescription> patientUpdateMap;

	public ProjectUpdateDescription(String projectID)
	{
		this.projectID = projectID;
		this.patientUpdates = new HashSet<PatientUpdateDescription>();
		this.patientUpdateMap = new HashMap<String, PatientUpdateDescription>();
	}

	public PatientUpdateDescription recordPatientUpdate(String patientID)
	{
		if (!patientUpdateMap.containsKey(patientID)) {
			PatientUpdateDescription patientUpdate = new PatientUpdateDescription(patientID);
			this.patientUpdateMap.put(patientID, patientUpdate);
			this.patientUpdates.add(patientUpdate);
			return patientUpdate;
		} else
			return patientUpdateMap.get(patientID);
	}
}
