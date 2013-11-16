package edu.stanford.isis.epadws.processing.update;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PatientUpdateDescription
{
	public final String subjectID;
	public final Set<StudyUpdateDescription> studyUpdates;
	private final Map<String, StudyUpdateDescription> studyUpdateMap;

	public PatientUpdateDescription(String subjectID)
	{
		this.subjectID = subjectID;
		this.studyUpdateMap = new HashMap<String, StudyUpdateDescription>();
		this.studyUpdates = new HashSet<StudyUpdateDescription>();
	}

	public StudyUpdateDescription recordStudyUpdate(String studyID)
	{
		if (!studyUpdateMap.containsKey(studyID)) {
			StudyUpdateDescription studyUpdate = new StudyUpdateDescription(studyID);
			this.studyUpdates.add(studyUpdate);
			this.studyUpdateMap.put(studyID, studyUpdate);
			return studyUpdate;
		} else
			return studyUpdateMap.get(studyID);
	}
}
