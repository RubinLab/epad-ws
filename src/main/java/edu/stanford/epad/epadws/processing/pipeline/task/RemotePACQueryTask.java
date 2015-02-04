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
			List objects;
			try {
				objects = new RemotePACQuery().getObjects("enabled = 1");
			} catch (Exception e) {
				log.warning("Error getting query list from database", e);
				return;
			}
			queries.addAll(objects);
		}
		for (RemotePACQuery query: queries)
		{
			try {
				RemotePAC pac = rps.getRemotePAC(query.getPacId());
				Subject subject = (Subject) new Subject(query.getSubjectId()).retrieve();
				log.info("Processing Remote PAC Query, PAC:" + pac.pacID + " Subject:" + subject.getSubjectUID());
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
				if (studyDate != null && !studyDate.endsWith("-"))
					studyDate = studyDate + "-";
				List<RemotePACEntity> entities = rps.queryRemoteData(pac, null, subject.getSubjectUID(), studyDate, false);
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
