//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.processing.events;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EventTracker
{
	private final ConcurrentMap<String, List<ProjectEventDescription>> projectEventMap = new ConcurrentHashMap<String, List<ProjectEventDescription>>();
	// private static final EPADLogger log = EPADLogger.getInstance();

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

		// log.info("recordProjectEvent: " + sessionID + ", projectID " + projectID);

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
		// log.info("recordPatientEvent: " + sessionID + ", projectID " + projectID + ", patientID " + patientID);

		ProjectEventDescription projectEvent = recordProjectEvent(sessionID, projectID);
		return projectEvent.recordPatientEvent(patientID);
	}

	public StudyEventDescription recordStudyEvent(String sessionID, String projectID, String patientID, String studyID)
	{
		// log.info("recordPatientEvent: " + sessionID + ", projectID " + projectID + ", patientID " + patientID
		// + ", studyID " + studyID);

		PatientEventDescription patientEvent = recordPatientEvent(sessionID, projectID, patientID);
		return patientEvent.recordStudyEvent(studyID);
	}

	public SeriesEventDescription recordSeriesEvent(String sessionID, String projectID, String patientID, String studyID,
			String seriesID)
	{
		// log.info("recordPatientEvent: " + sessionID + ", projectID " + projectID + ", patientID " + patientID
		// + ", studyID " + studyID + ", seriesID " + seriesID);

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
