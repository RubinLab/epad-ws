/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.EPadWebServerVersion;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.models.EpadFile;
import edu.stanford.epad.epadws.models.EpadStatistics;
import edu.stanford.epad.epadws.models.EpadStatisticsTemplate;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.models.Plugin;
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
			int files = new EpadFile().getCount("");
			int templates = new EpadFile().getCount("filetype = '" + FileType.TEMPLATE.getName() + "'");
			int plugins = new Plugin().getCount("");
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
			es.setNumOfFiles(files);
			es.setNumOfPlugins(plugins);
			es.setNumOfTemplates(templates);
			es.setCreator("admin");
			es.save();
			
			//get the template statistics
			List<EpadStatisticsTemplate> templateStats= epadDatabaseOperations.getTemplateStats();
			
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
					//send the number statistics
					String epadUrl = EPADConfig.getParamValue("EpadStatisticsURL", "https://epad-public.stanford.edu/epad/statistics/");
					epadUrl = epadUrl + "?numOfUsers=" + users;
					epadUrl = epadUrl + "&numOfProjects=" + projects;
					epadUrl = epadUrl + "&numOfPatients=" + patients;
					epadUrl = epadUrl + "&numOfStudies=" + studies;
					epadUrl = epadUrl + "&numOfSeries=" + series;
					epadUrl = epadUrl + "&numOfAims=" + aims;
					epadUrl = epadUrl + "&numOfDSOs=" + dsos;
					epadUrl = epadUrl + "&numOfWorkLists=" + wls;
					epadUrl = epadUrl + "&numOfFiles=" + files;
					epadUrl = epadUrl + "&numOfPlugins=" + plugins;
					epadUrl = epadUrl + "&numOfTemplates=" + templates;
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
					
					
					//send statistics for templates
					for (EpadStatisticsTemplate st:templateStats) {
						//get the xml first
						String filePath=st.getFilePath()+st.getFileId()+".xml";
						log.info("path "+filePath);
						File f=null;
						if (filePath!= null && (f=new File(filePath)).exists()) {
							st.setTemplateText(EPADFileUtils.readFileAsString(f));
						}
						st.setCreator("admin");
						//persist to db
						st.save();
						
						epadUrl =  EPADConfig.getParamValue("EpadTemplateStatisticsURL", "http://epad-dev4.stanford.edu:8080/epad/statistics/templates/");
						epadUrl = epadUrl + "?templateCode=" + encode(st.getTemplateCode());
						epadUrl = epadUrl + "&templateName=" + encode(st.getTemplateName());
						epadUrl = epadUrl + "&authors=" + encode(st.getAuthors());
						epadUrl = epadUrl + "&version=" + encode(st.getVersion());
						epadUrl = epadUrl + "&templateLevelType=" + encode(st.getTemplateLevelType());
						epadUrl = epadUrl + "&templateDescription=" + encode(st.getTemplateDescription());
						epadUrl = epadUrl + "&numOfAims=" +  st.getNumOfAims();
						epadUrl = epadUrl + "&host=" + host;
						putMethod = new PutMethod(epadUrl);
						putMethod.setRequestEntity(new StringRequestEntity(st.getTemplateText(), "text/xml", "UTF-8"));						
						
						try {
							log.info("Sending template statistics to Central Epad, url:" + epadUrl);
							int status = client.executeMethod(putMethod);
							log.info("Done Sending, status:" + putMethod.getStatusLine());
						} catch (IOException e) {
							log.warning("Error calling Central Epad with URL " + epadUrl, e);
						} finally {
							putMethod.releaseConnection();
						}
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
										msg,  // Message
										"", "", // aimUID, aimName
										"", // patient ID
										"", // patient Name
										"", // templateID
										"", // templateName
										"Please update ePAD");	// PluginName
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
	
	private static String encode(String urlString)
	{
		try {
			return URLEncoder.encode(urlString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warning("Warning: error encoding URL " + urlString, e);
			return null;
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
