package edu.stanford.epad.epadws.processing.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProjectEventDescription
{
	public final String projectID;
	public final List<PatientEventDescription> patientEvents;
	private final ConcurrentMap<String, PatientEventDescription> patientEventMap;

	public ProjectEventDescription(String projectID)
	{
		this.projectID = projectID;
		this.patientEvents = new ArrayList<PatientEventDescription>();
		this.patientEventMap = new ConcurrentHashMap<String, PatientEventDescription>();
	}

	public synchronized PatientEventDescription recordPatientEvent(String patientID)
	{
		if (!patientEventMap.containsKey(patientID)) {
			PatientEventDescription patientEvent = new PatientEventDescription(patientID);
			this.patientEventMap.put(patientID, patientEvent);
			this.patientEvents.add(patientEvent);
			return patientEvent;
		} else
			return patientEventMap.get(patientID);
	}

	public List<PatientEventDescription> getPatientEvents()
	{
		return new ArrayList<PatientEventDescription>(patientEventMap.values());
	}
}
