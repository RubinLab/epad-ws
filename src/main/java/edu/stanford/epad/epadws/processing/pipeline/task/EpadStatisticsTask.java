package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PutMethod;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.models.EpadStatistics;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.RemotePACQuery;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.service.RemotePACService;

/**
 * 
 * Gather statistics and transmit. (Should be run once a day)
 *  
 * @author dev
 * 
 */
public class EpadStatisticsTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();
	private final EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
	private static Calendar prevTime = null;

	@Override
	public void run()
	{
		try {
			log.info("Getting epad statistics");
			EpadStatistics es = new EpadStatistics();
			int users = new User().getCount("");
			int projects = new Project().getCount("");
			int patients = new Subject().getCount("");
			int studies = new Study().getCount("");
			int series = new User().getCount("");
			int npacs = RemotePACService.getInstance().getRemotePACs().size();
			int aims = epadDatabaseOperations.getNumberOfAIMs("1 = 1");
			int dsos = epadDatabaseOperations.getNumberOfAIMs("DSOSeriesUID is not null or DSOSeriesUID != ''");
			int pacQueries = new RemotePACQuery().getCount("");
			int wls = 0;
			try {
				wls = new WorkList().getCount("");
			} catch (Exception x) {}
			String host = EPADConfig.xnatServer;
			if (host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase("127.0.0.1") || host.equalsIgnoreCase("epad-vm"))
				host = InetAddress.getLocalHost().getHostName();
			es.setHost(host);
			es.setNumOfUsers(users);
			es.setNumOfProjects(projects);
			es.setNumOfPatients(patients);
			es.setNumOfStudies(studies);
			es.setNumOfSeries(series);
			es.setNumOfAims(aims);
			es.setNumOfDSOs(dsos);
			es.setNumOfWorkLists(wls);
			es.setNumOfPacs(npacs);
			es.setNumOfAutoQueries(pacQueries);
			es.setCreator("admin");
			es.save();
			Calendar now = Calendar.getInstance();
			boolean daily = true;
			if ("Weekly".equalsIgnoreCase(EPADConfig.getParamValue("StatisticsPeriod", "Daily")))
				daily = false;
			if (!"true".equalsIgnoreCase(EPADConfig.getParamValue("DISABLE_STATISTICS_TRANSMIT")))
			{
				if (daily || now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
				{
					String epadUrl = EPADConfig.getParamValue("EpadStatisticsURL", "https://epad-public.stanford.edu/epad/statistics/");
					epadUrl = epadUrl + "?numOfUsers=" + users;
					epadUrl = epadUrl + "&numOfProjects=" + users;
					epadUrl = epadUrl + "&numOfPatients=" + patients;
					epadUrl = epadUrl + "&numOfStudies=" + studies;
					epadUrl = epadUrl + "&numOfSeries=" + series;
					epadUrl = epadUrl + "&numOfAims=" + aims;
					epadUrl = epadUrl + "&numOfDSOs=" + dsos;
					epadUrl = epadUrl + "&numOfWorkLists=" + wls;
					epadUrl = epadUrl + "&host=" + host;
					HttpClient client = new HttpClient();
					PutMethod putMethod = new PutMethod(epadUrl);

					try {
						log.info("Sending statistics to Central Epad, url:" + epadUrl);
						int status = client.executeMethod(putMethod);
						log.info("Done Sending, status:" + putMethod.getStatusLine());
					} catch (IOException e) {
						log.warning("Error calling Central Epad with URL " + epadUrl, e);
					} finally {
						putMethod.releaseConnection();
					}
				}
			}
			prevTime = now;
		} catch (Exception e) {
			log.warning("Error is saving/sending statistics", e);
		}
	}
}
