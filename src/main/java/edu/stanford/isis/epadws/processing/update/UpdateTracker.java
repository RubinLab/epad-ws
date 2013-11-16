package edu.stanford.isis.epadws.processing.update;

import java.util.HashMap;
import java.util.Map;

public class UpdateTracker
{
	private final Map<String, UpdateDescription> changeMap = new HashMap<String, UpdateDescription>();

	private static final UpdateTracker ourInstance = new UpdateTracker();

	public static UpdateTracker getInstance()
	{
		return ourInstance;
	}

	private UpdateTracker()
	{
	}

	public void addSession(String sessionID)
	{
		if (!changeMap.containsKey(sessionID)) {
			changeMap.put(sessionID, new UpdateDescription());
		}

		// TODO Remove old invalid sessions
	}

	public ProjectUpdateDescription recordUpdate(String sessionID, String projectID)
	{
		UpdateDescription updateDescription = getUpdateDescription(sessionID);
		return updateDescription.recordProjectUpdate(projectID);
	}

	public PatientUpdateDescription recordUpdate(String sessionID, String projectID, String patientID)
	{
		ProjectUpdateDescription projectUpdate = recordUpdate(sessionID, projectID);
		return projectUpdate.recordPatientUpdate(patientID);
	}

	public StudyUpdateDescription recordUpdate(String sessionID, String projectID, String patientID, String studyID)
	{
		PatientUpdateDescription patientUpdate = recordUpdate(sessionID, projectID, patientID);
		return patientUpdate.recordStudyUpdate(studyID);
	}

	public SeriesUpdateDescription recordUpdate(String sessionID, String projectID, String patientID, String studyID,
			String seriesID)
	{
		StudyUpdateDescription studyUpdate = recordUpdate(sessionID, projectID, patientID, studyID);
		return studyUpdate.recordSeriesUpdate(seriesID);
	}

	public void recordUpdate(String sessionID, String projectID, String patientID, String studyID, String seriesID,
			float percentComplete)
	{
		SeriesUpdateDescription seriesUpdate = recordUpdate(sessionID, projectID, patientID, studyID, seriesID);
		seriesUpdate.percentComplete = percentComplete;
	}

	public UpdateDescription getUpdateDescription(String sessionID)
	{
		if (!changeMap.containsKey(sessionID)) {
			UpdateDescription updateDescription = new UpdateDescription();
			changeMap.put(sessionID, updateDescription);
			return updateDescription;
		} else
			return changeMap.get(sessionID);
	}
}
