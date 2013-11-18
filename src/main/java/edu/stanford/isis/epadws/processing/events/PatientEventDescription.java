package edu.stanford.isis.epadws.processing.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PatientEventDescription
{
	public final String subjectID;
	public final Set<StudyEventDescription> studyEvents;
	private final Map<String, StudyEventDescription> studyEventMap;

	public PatientEventDescription(String subjectID)
	{
		this.subjectID = subjectID;
		this.studyEventMap = new HashMap<String, StudyEventDescription>();
		this.studyEvents = new HashSet<StudyEventDescription>();
	}

	public StudyEventDescription recordStudyEvent(String studyID)
	{
		if (!studyEventMap.containsKey(studyID)) {
			StudyEventDescription studyEvent = new StudyEventDescription(studyID);
			this.studyEvents.add(studyEvent);
			this.studyEventMap.put(studyID, studyEvent);
			return studyEvent;
		} else
			return studyEventMap.get(studyID);
	}
}
