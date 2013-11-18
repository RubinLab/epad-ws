package edu.stanford.isis.epadws.processing.events;

import java.util.HashMap;
import java.util.Map;

public class EventTracker
{
	private final Map<String, EventDescription> eventMap = new HashMap<String, EventDescription>();

	private static final EventTracker ourInstance = new EventTracker();

	public static EventTracker getInstance()
	{
		return ourInstance;
	}

	private EventTracker()
	{
	}

	public void addSession(String sessionID)
	{
		if (!eventMap.containsKey(sessionID)) {
			eventMap.put(sessionID, new EventDescription());
		}

		// TODO Remove old invalid sessions
	}

	public ProjectEventDescription recordEvent(String sessionID, String projectID)
	{
		EventDescription eventDescription = getEventDescription(sessionID);
		return eventDescription.recordProjectEvent(projectID);
	}

	public PatientEventDescription recordEvent(String sessionID, String projectID, String patientID)
	{
		ProjectEventDescription projectEvent = recordEvent(sessionID, projectID);
		return projectEvent.recordPatientEvent(patientID);
	}

	public StudyEventDescription recordEvent(String sessionID, String projectID, String patientID, String studyID)
	{
		PatientEventDescription patientEvent = recordEvent(sessionID, projectID, patientID);
		return patientEvent.recordStudyEvent(studyID);
	}

	public SeriesEventDescription recordEvent(String sessionID, String projectID, String patientID, String studyID,
			String seriesID)
	{
		StudyEventDescription studyEvent = recordEvent(sessionID, projectID, patientID, studyID);
		return studyEvent.recordSeriesEvent(seriesID);
	}

	public void recordEvent(String sessionID, String projectID, String patientID, String studyID, String seriesID,
			float percentComplete)
	{
		SeriesEventDescription seriesEvent = recordEvent(sessionID, projectID, patientID, studyID, seriesID);
		seriesEvent.percentPNGGenerationComplete = percentComplete;
	}

	public EventDescription getEventDescription(String sessionID)
	{
		if (!eventMap.containsKey(sessionID)) {
			EventDescription eventDescription = new EventDescription();
			eventMap.put(sessionID, eventDescription);
			return eventDescription;
		} else
			return eventMap.get(sessionID);
	}
}
