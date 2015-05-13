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
package edu.stanford.epad.epadws.processing.pipeline.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.dtos.RemotePACEntity;
import edu.stanford.epad.dtos.internal.DCM4CHEEStudy;
import edu.stanford.epad.dtos.internal.DCM4CHEEStudyList;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.RemotePACQuery;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.RemotePACService;
import edu.stanford.epad.epadws.service.TCIAService;

/**
 * Query Remote PAC for new Studies and schedule transfer to local DCM4CHE PAc.
 * 
 * @author dev
 * 
 */
public class RemotePACQueryTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();

	private final RemotePACQuery pacQuery;

	public RemotePACQueryTask(RemotePACQuery pacQuery)
	{
		this.pacQuery = pacQuery;
	}

	@Override
	public void run()
	{
		List<RemotePACQuery> queries = new ArrayList<RemotePACQuery>();
		RemotePACService rps;
		try {
			rps = RemotePACService.getInstance();
		} catch (Exception e) {
			log.warning("Error initializing Remote PAC Service", e);
			return;
		}
		if (pacQuery != null)
		{
			queries.add(pacQuery);
		}
		else
		{
			 // Get/Process All enabled PAC queries
			Calendar cal = Calendar.getInstance();
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			int dayOfWeekInMonth = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
			List objects;
			try {
				objects = new RemotePACQuery().getObjects("enabled = 1");
			} catch (Exception e) {
				log.warning("Error getting query list from database", e);
				return;
			}
			for (Object object: objects)
			{
				RemotePACQuery query = (RemotePACQuery) object;
				if ("weekly".equalsIgnoreCase(query.getPeriod()) && dayOfWeek != Calendar.SUNDAY) continue;
				if ("monthly".equalsIgnoreCase(query.getPeriod()) && dayOfWeek != Calendar.SUNDAY && dayOfWeekInMonth != 1) continue;
				queries.add(query);
			}
		}
		for (RemotePACQuery query: queries)
		{
			try {
				RemotePAC pac = rps.getRemotePAC(query.getPacId());
				Subject subject = (Subject) new Subject(query.getSubjectId()).retrieve();
				log.info("Processing Remote PAC Query, PAC:" + query.getPacId() + " Subject:" + subject.getSubjectUID());
				String studyDate = query.getLastStudyDate();
				if (studyDate == null)
				{
					try {
						List<Study> studies = DefaultEpadProjectOperations.getInstance().getStudiesForSubject(subject.getSubjectUID());
						Set<String> studyUIDs = new HashSet<String>();
						for (Study study: studies)
						{
							studyUIDs.add(study.getStudyUID());
						}
						DCM4CHEEStudyList dcm4CheeStudyList = Dcm4CheeQueries.getStudies(studyUIDs);
						Date date = null;
						for (DCM4CHEEStudy dcs: dcm4CheeStudyList.ResultSet.Result)
						{
							if (date == null || getDate(dcs.dateAcquired).after(date))
								date = getDate(dcs.dateAcquired);
						}
						if (date != null)
						{
							Calendar cal = Calendar.getInstance();
							cal.setTime(date);
							cal.add(Calendar.DATE, 1);
							studyDate = dateformat.format(cal.getTime());
							log.info("Subject:" + subject.getSubjectUID() + " Last Study Date from DCM4CHE:" + studyDate);	
						}
					} catch (Exception x) {};
				}
				List<RemotePACEntity> entities = new ArrayList<RemotePACEntity>();
				String collection = "";
				if (pac != null)
				{
					if (studyDate != null && !studyDate.endsWith("-"))
						studyDate = studyDate + "-";
					String modality = query.getModality();
					entities = rps.queryRemoteData(pac, null, subject.getSubjectUID(), "", studyDate, modality, false, true);
				}
				else if (query.getPacId().startsWith(TCIAService.TCIA_PREFIX))
				{
					collection = query.getPacId().substring(5);
					entities = TCIAService.getInstance().getNewStudiesForPatient(collection, subject.getSubjectUID(), query.getLastQueryTime());
				}
				query.setLastQueryStatus("Query Completed, number of new objects:" + entities.size());
				String status = "";
				Date newStudyDate = null;
				for (RemotePACEntity entity: entities)
				{
					boolean lastStudyDateUpdated = true;
					if (entity.entityType.equalsIgnoreCase("Study"))
					{
						lastStudyDateUpdated = false;
						String projectID = null;
						if (query.getProjectId() != null)
						{
							Project project = (Project) new Project(query.getId()).retrieve();
							projectID = project.getProjectId();
						}
						if (pac != null)
						{
							String studyUID = rps.retrieveRemoteData(pac, entity.entityID, projectID, query.getRequestor(), "");
							status = status + "\nStudy:" + studyUID + " transfer initiated from PAC " + pac.pacID;
							if (studyUID.indexOf(":") != -1)
							{
								studyDate = studyUID.substring(studyUID.lastIndexOf(":")+1);
								Date sdate = getDate(studyDate);
								if (sdate != null && (newStudyDate == null || sdate.before(newStudyDate)))
								{
									newStudyDate = sdate;
									Calendar cal = Calendar.getInstance();
									cal.setTime(sdate);
									cal.add(Calendar.DATE, 1);
									query.setLastStudyDate(dateformat.format(cal.getTime()));
									log.info("Setting new study date:" + studyDate);
									lastStudyDateUpdated = true;
								}
							}
						}
						else
						{
							String studyUID = entity.entityID;
							if (studyUID.indexOf(":") != -1)
								studyUID = studyUID.substring(studyUID.lastIndexOf(":")+1);
							TCIAService.getInstance().downloadStudyFromTCIA(query.getRequestor(), collection, subject.getSubjectUID(), studyUID, projectID);
						}
					}
					if (!lastStudyDateUpdated)
					{
						query.setLastStudyDate(dateformat.format(new Date()));
					}
					query.setLastQueryStatus(status);
					log.info(status);
				}
			} catch (Exception e) {
				log.warning("Error in Remote PAC Query, PAC ID:" + query.getPacId(), e);
				query.setLastQueryStatus(e.getMessage());
			}
			query.setLastQueryTime(new Date());
			try {
				query.save();
			} catch (Exception e) {
				log.warning("Error saving Remote PAC Query status", e);
			}
		}
	}
	
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
	private Date getDate(String dateStr)
	{
		try
		{
			return dateformat.parse(dateStr);
		}
		catch (Exception x)
		{
			return null;
		}
	}

}
