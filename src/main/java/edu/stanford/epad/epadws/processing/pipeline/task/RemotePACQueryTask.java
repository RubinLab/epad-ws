package edu.stanford.epad.epadws.processing.pipeline.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.dtos.RemotePACEntity;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.RemotePACQuery;
import edu.stanford.epad.epadws.models.Subject;
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
				if (!studyDate.endsWith("-"))
					studyDate = studyDate + "-";
				List<RemotePACEntity> entities = rps.queryRemoteData(pac, null, subject.getSubjectUID(), studyDate);
				query.setLastQueryStatus("Query Completed, number of new objects:" + entities.size());
				String status = "";
				boolean lastStudyDateUpdated = false;
				for (RemotePACEntity entity: entities)
				{
					if (entity.entityType.equalsIgnoreCase("Study"))
					{
						String projectID = null;
						if (query.getProjectId() != null)
						{
							Project project = (Project) new Project(query.getId()).retrieve();
							projectID = project.getProjectId();
						}
						String studyUID = rps.retrieveRemoteData(pac, entity.entityID, projectID, query.getRequestor(), "");
						status = status + "\nStudy:" + studyUID + " transfer initiated from PAC " + pac.pacID;
						Date newStudyDate = null;
						if (studyUID.indexOf(":") != -1)
						{
							studyDate = studyUID.substring(studyUID.indexOf(":")+1);
							Date sdate = getDate(studyDate);
							if (sdate != null && (newStudyDate == null || sdate.before(newStudyDate)))
							{
								newStudyDate = sdate;
								query.setLastStudyDate(studyDate); // TODO: Do we need to add one to date?
								lastStudyDateUpdated = true;
							}
						}
					}
					if (!lastStudyDateUpdated)
					{
						query.setLastStudyDate(dateformat.format(new Date()));
					}
					query.setLastQueryStatus(status);
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
