package edu.stanford.isis.epadws.processing.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProjectEventDescription
{
	public final String projectID;
	public final Set<PatientEventDescription> patientEvents;
	private final Map<String, PatientEventDescription> patientEventMap;

	public ProjectEventDescription(String projectID)
	{
		this.projectID = projectID;
		this.patientEvents = new HashSet<PatientEventDescription>();
		this.patientEventMap = new HashMap<String, PatientEventDescription>();
	}

	public PatientEventDescription recordPatientEvent(String patientID)
	{
		if (!patientEventMap.containsKey(patientID)) {
			PatientEventDescription patientEvent = new PatientEventDescription(patientID);
			this.patientEventMap.put(patientID, patientEvent);
			this.patientEvents.add(patientEvent);
			return patientEvent;
		} else
			return patientEventMap.get(patientID);
	}
}
