package edu.stanford.isis.epadws.processing.events;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.stanford.isis.epad.common.util.EPADLogger;

public class EventTracker
{
	private final ConcurrentMap<String, List<ProjectEventDescription>> projectEventMap = new ConcurrentHashMap<String, List<ProjectEventDescription>>();
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final EventTracker ourInstance = new EventTracker();

	public static EventTracker getInstance()
	{
		return ourInstance;
	}

	private EventTracker()
	{
	}

	public synchronized ProjectEventDescription recordProjectEvent(String sessionID, String projectID)
	{
		ProjectEventDescription projectEvent;

		log.info("recordProjectEvent: " + sessionID + ", projectID" + projectID);

		if (projectEventMap.containsKey(sessionID)) {
			projectEvent = null;
			for (ProjectEventDescription currentProjectEvent : projectEventMap.get(sessionID)) {
				if (currentProjectEvent.projectID.equals(projectID)) {
					projectEvent = currentProjectEvent;
					break;
				}
			}
			if (projectEvent == null) { // We have no events for this project
				projectEvent = new ProjectEventDescription(projectID);
				projectEventMap.get(sessionID).add(projectEvent);
			}
		} else {
			List<ProjectEventDescription> projectEvents = new ArrayList<ProjectEventDescription>();
			projectEvent = new ProjectEventDescription(projectID);
			projectEvents.add(projectEvent);
			projectEventMap.put(sessionID, projectEvents);
		}
		return projectEvent;
	}

	// TODO Too coarse locking
	public synchronized String dumpProjectEvents(String sessionID)
	{
		StringBuilder result = new StringBuilder();
		final GsonBuilder gsonBuilder = new GsonBuilder();

		gsonBuilder.excludeFieldsWithModifiers(Modifier.PRIVATE);

		final Gson gson = gsonBuilder.create();

		result.append("{ \"projectEvents\": ");

		log.info("dumpProjectEvents.keys: " + projectEventMap.keySet());

		if (projectEventMap.containsKey(sessionID)) {
			result.append(gson.toJson(projectEventMap.get(sessionID)));

			projectEventMap.get(sessionID).clear();
		} else {
			result.append("[]");
		}

		result.append(" }");

		return result.toString();
	}

	public PatientEventDescription recordPatientEvent(String sessionID, String projectID, String patientID)
	{
		log.info("recordPatientEvent: " + sessionID + ", projectID" + projectID + ", patientID" + patientID);

		ProjectEventDescription projectEvent = recordProjectEvent(sessionID, projectID);
		return projectEvent.recordPatientEvent(patientID);
	}

	public StudyEventDescription recordStudyEvent(String sessionID, String projectID, String patientID, String studyID)
	{
		log.info("recordPatientEvent: " + sessionID + ", projectID" + projectID + ", patientID" + patientID + ", studyID "
				+ studyID);

		PatientEventDescription patientEvent = recordPatientEvent(sessionID, projectID, patientID);
		return patientEvent.recordStudyEvent(studyID);
	}

	public SeriesEventDescription recordSeriesEvent(String sessionID, String projectID, String patientID, String studyID,
			String seriesID)
	{
		log.info("recordPatientEvent: " + sessionID + ", projectID" + projectID + ", patientID" + patientID + ", studyID "
				+ studyID + ", seriesID " + seriesID);

		StudyEventDescription studyEvent = recordStudyEvent(sessionID, projectID, patientID, studyID);
		return studyEvent.recordSeriesEvent(seriesID);
	}

	public void recordSeriesEvent(String sessionID, String projectID, String patientID, String studyID, String seriesID,
			float percentPNGGenerationComplete)
	{
		SeriesEventDescription seriesEvent = recordSeriesEvent(sessionID, projectID, patientID, studyID, seriesID);
		seriesEvent.percentPNGGenerationComplete = percentPNGGenerationComplete;
	}
}
