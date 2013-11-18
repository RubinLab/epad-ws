package edu.stanford.isis.epadws.processing.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PatientEventDescription
{
	public final String patientID;
	public final List<StudyEventDescription> studyEvents;
	private final ConcurrentMap<String, StudyEventDescription> studyEventMap;

	public PatientEventDescription(String subjectID)
	{
		this.patientID = subjectID;
		this.studyEventMap = new ConcurrentHashMap<String, StudyEventDescription>();
		this.studyEvents = new ArrayList<StudyEventDescription>();
	}

	public synchronized StudyEventDescription recordStudyEvent(String studyID)
	{
		if (!studyEventMap.containsKey(studyID)) {
			StudyEventDescription studyEvent = new StudyEventDescription(studyID);
			this.studyEvents.add(studyEvent);
			this.studyEventMap.put(studyID, studyEvent);
			return studyEvent;
		} else
			return studyEventMap.get(studyID);
	}

	public List<StudyEventDescription> getStudyEvents()
	{
		return new ArrayList<StudyEventDescription>(studyEventMap.values());
	}
}
