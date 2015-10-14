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

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.EPadWebServerVersion;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.models.EpadStatistics;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.RemotePACQuery;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
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
	
	public static String newEPADVersion = "";	
	public static boolean newEPADVersionAvailable = false;
	
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
			int series = epadDatabaseOperations.getNumberOfSeries();
			int npacs = RemotePACService.getInstance().getRemotePACs().size();
			int aims = epadDatabaseOperations.getNumberOfAIMs("1 = 1");
			int dsos = epadDatabaseOperations.getNumberOfAIMs("DSOSeriesUID is not null or DSOSeriesUID != ''");
			int pacQueries = new RemotePACQuery().getCount("");
			int wls = 0;
			try {
				wls = new WorkList().getCount("");
			} catch (Exception x) {}
			String host = EPADConfig.xnatServer;
			if (host == null || host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase("127.0.0.1") || host.equalsIgnoreCase("epad-vm"))
				host = System.getenv("DOCKER_HOST");;
				if (host == null || host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase("127.0.0.1") || host.equalsIgnoreCase("epad-vm"))
					host = System.getenv("HOSTNAME");;
			if (host == null || host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase("127.0.0.1") || host.equalsIgnoreCase("epad-vm"))
				host = InetAddress.getLocalHost().getHostName();
			if (host == null || host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase("127.0.0.1") || host.equalsIgnoreCase("epad-vm"))
				host = getIPAddress();
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
					long delay = new Random().nextInt(1800 + 1);
					if (EPADConfig.xnatServer.indexOf("stanford") == -1)
						Thread.sleep(1000*delay); // So that all don't do this at the same time
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
			
		} catch (Exception e) {
			log.warning("Error is saving/sending statistics", e);
		}
		GetMethod getMethod = null;
		try {
			String epadUrl = EPADConfig.getParamValue("EpadStatusURL", "https://epad-public.stanford.edu/epad/status/");
			HttpClient client = new HttpClient();
			getMethod = new GetMethod(epadUrl);
			int status = client.executeMethod(getMethod);
			if (status == HttpServletResponse.SC_OK) {
				String response = getMethod.getResponseBodyAsString();
				int versInd = response.indexOf("Version:");
				if (versInd != -1) {
					String version = response.substring(versInd + "Version:".length()+1);
					if (version.indexOf("\n") != -1)
						version  = version.substring(0, version.indexOf("\n"));
					if (version.indexOf(" ") != -1)
						version  = version.substring(0, version.indexOf(" "));
					log.info("Current ePAD version:" + version + " Our Version:" + new EPadWebServerVersion().getVersion());
					if (!version.equals(new EPadWebServerVersion().getVersion()))
					{
						newEPADVersion = version;
						newEPADVersionAvailable = true;
						String msg = "A new version of ePAD: " + version + " is available, please go to ftp://epad-distribution.stanford.edu/ to download";
						log.info(msg);
						List<User> admins = new User().getObjects("admin = 1 and enabled = 1");
						for (User admin: admins)
						{
							List<Map<String, String>> userEvents = epadDatabaseOperations.getEpadEventsForSessionID(admin.getUsername(), false);
							boolean skip = false;
							for (Map<String, String> event: userEvents)
							{
								if (event.get("aim_name").equals("Upgrade"))
								{
									skip = true;
									break;
								}
							}
							if (skip) continue;
							if (EPADConfig.xnatServer.indexOf("stanford") == -1)
								epadDatabaseOperations.insertEpadEvent(
										admin.getUsername(), 
										msg, 
										"System", "Upgrade",
										"System", 
										"Upgrade", 
										"Upgrade", 
										"Upgrade",
										"Please update ePAD");												
						}
					}
				}
			}
			else
				log.warning("Error is getting epad version");
			
		} catch (Exception x) {
			log.warning("Error is getting epad version", x);
		} finally {
			getMethod.releaseConnection();
		}
	}
	
	public static String getIPAddress()
	{
		String ip = "";
		String ipi = "";
		Enumeration e;
		try {
			e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements())
			{
			    NetworkInterface n = (NetworkInterface) e.nextElement();
			    Enumeration ee = n.getInetAddresses();
			    while (ee.hasMoreElements())
			    {
			        InetAddress i = (InetAddress) ee.nextElement();
			        ipi = i.getHostAddress();
			        if (!ipi.startsWith("127") && !ipi.startsWith("192") && !ipi.startsWith("172") 
			        		&& !ipi.startsWith("10.") && !ipi.startsWith("0:"))
			        	ip = ipi;
			    }
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		if (ip.length() == 0)
			return ipi;
		else
			return ip;
	}
}
