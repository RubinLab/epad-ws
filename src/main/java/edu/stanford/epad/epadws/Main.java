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
package edu.stanford.epad.epadws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.BindException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.DispatcherType;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.xml.sax.SAXException;

import edu.stanford.epad.common.plugins.PluginController;
import edu.stanford.epad.common.plugins.PluginServletHandler;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADFileList;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.admin.ConvertAIM4Handler;
import edu.stanford.epad.epadws.handlers.admin.CopyAimsToExistHandler;
import edu.stanford.epad.epadws.handlers.admin.ImageCheckHandler;
import edu.stanford.epad.epadws.handlers.admin.ImageReprocessingHandler;
import edu.stanford.epad.epadws.handlers.admin.ResourceCheckHandler;
import edu.stanford.epad.epadws.handlers.admin.ResourceFailureLogHandler;
import edu.stanford.epad.epadws.handlers.admin.ServerStatusHandler;
import edu.stanford.epad.epadws.handlers.admin.StatisticsHandler;
import edu.stanford.epad.epadws.handlers.admin.XNATSyncHandler;
import edu.stanford.epad.epadws.handlers.aim.AimResourceHandler;
import edu.stanford.epad.epadws.handlers.coordination.CoordinationHandler;
import edu.stanford.epad.epadws.handlers.core.EPADHandler;
import edu.stanford.epad.epadws.handlers.dicom.DownloadHandler;
import edu.stanford.epad.epadws.handlers.dicom.ResourcesFileHandler;
import edu.stanford.epad.epadws.handlers.dicom.WadoHandler;
import edu.stanford.epad.epadws.handlers.event.EventHandler;
import edu.stanford.epad.epadws.handlers.event.ProjectEventHandler;
import edu.stanford.epad.epadws.handlers.plugin.EPadPluginHandler;
import edu.stanford.epad.epadws.handlers.plugin.StatusListenerHandler;
import edu.stanford.epad.epadws.handlers.session.EPADSessionHandler;
import edu.stanford.epad.epadws.models.DisabledTemplate;
import edu.stanford.epad.epadws.models.EpadFile;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.models.Plugin;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectToTemplate;
import edu.stanford.epad.epadws.models.ProjectType;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.plugins.PluginConfig;
import edu.stanford.epad.epadws.plugins.PluginHandlerMap;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownHookThread;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.epad.epadws.processing.pipeline.watcher.QueueAndWatcherManager;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.PluginOperations;
import edu.stanford.epad.epadws.service.RemotePACService;

/**
 * Note: When the ePAD webservices and ePAD client is split into two separate webapps, this class becomes obsolete and is not used
 * 
 * Entry point for the ePAD Web Service.
 * <p>
 * Start an embedded Jetty server and all the threads required for this application. The application listens on the port
 * indicated by the property <i>ePadClientPort</i> in proxy-config.properties.
 * <p>
 * NOTE: The current directory must be set to the ePAD bin subdirectory (~epad/DicomProxy/bin) before calling the start
 * scripts associated with this application. Code in the WAR file needs to be updated to remove this restriction.
 */
public class Main
{
	private static final EPADLogger log = EPADLogger.getInstance();
	
	public static boolean separateWebServicesApp = true;

	
	public static void main(String[] args)
	{
		ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
		Server server = null;

		try {
			checkPropertiesFile();
			checkResourcesFolders();
			checkPluginsFile();
			RemotePACService.checkPropertiesFile();
			int	epadPort = EPADConfig.epadPort;
			Dcm4CheeOperations.checkScriptFiles();
			separateWebServicesApp = "true".equalsIgnoreCase(EPADConfig.getParamValue("SeparateWebServicesApp"));
			if (!separateWebServicesApp) {
				log.info("#####################################################");
				log.info("############# Starting ePAD Web Service #############");
				log.info("#####################################################");
				initializePlugins();
				startSupportThreads();
			} else {
				log.info("#####################################################");
				log.info("############# Starting ePAD GWT FrontEnd ############");
				log.info("#####################################################");
			}
			server = new Server(epadPort);
			configureJettyServer(server);
			addHandlers(server);
			Runtime.getRuntime().addShutdownHook(new ShutdownHookThread());
			log.info("Starting Jetty on port " + epadPort);
			server.start();
			setupTestFiles();
			server.join();
		} catch (BindException be) {
			log.severe("Bind exception", be);
			Throwable t = be.getCause();
			log.warning("Bind exception cause: " + be.getMessage(), t);
		} catch (SocketException se) {
			log.severe("Cannot bind to all sockets", se);
		} catch (Exception e) {
			log.severe("Fatal Exception. Shutting down ePAD Web Service", e);
		} catch (Error err) {
			log.severe("Fatal Error. Shutting down ePAD Web Service", err);
		} finally {
			log.info("#####################################################");
			log.info("############# Shutting down ePAD  ###################");
			log.info("#####################################################");

			shutdownSignal.shutdownNow();
			stopServer(server);
			if (!separateWebServicesApp) {
				EpadDatabase.getInstance().shutdown();
				QueueAndWatcherManager.getInstance().shutdown();
			}
			try { // Wait just long enough for some messages to be printed out.
				TimeUnit.MILLISECONDS.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		log.info("#####################################################");
		log.info("################## Exit ePAD Web Service ###########");
		log.info("#####################################################");
	}

	public static void checkPropertiesFile() {
		String macdockerhost = System.getenv("MACDOCKER_HOST");
		String dockerhost = macdockerhost;
		if (dockerhost == null)
			dockerhost = System.getenv("DOCKER_HOST");
		if (dockerhost != null && dockerhost.startsWith("tcp://"))
			dockerhost = dockerhost.substring(6);
		if (dockerhost != null && dockerhost.contains(":"))
			dockerhost = dockerhost.substring(0, dockerhost.indexOf(":"));
		String hostname = dockerhost;
		if (hostname == null || hostname.length() == 0)
			hostname = System.getenv("HOSTNAME");
		if (hostname == null || hostname.length() == 0)
			hostname = "localhost";
		File propertiesFile = new File(System.getProperty("user.home") + "/DicomProxy/etc/", EPADConfig.configFileName);
		File clientProperties = null;
		if (!propertiesFile.exists()) {
			File macproperties = new File(System.getProperty("user.home") + "/mac/etc/", EPADConfig.configFileName);
			if (macproperties.exists() || (macdockerhost != null && macdockerhost.length() > 0))
			{
				clientProperties = propertiesFile;
				propertiesFile = macproperties;
			}
		}
		if (!propertiesFile.exists()) {
			propertiesFile.getParentFile().mkdirs();
			BufferedReader reader = null;
			InputStream is = null;
			StringBuilder sb = new StringBuilder();
			try {
				is = EPADFileUtils.class.getClassLoader().getResourceAsStream(propertiesFile.getName());
				reader = new BufferedReader(new InputStreamReader(is, "UTF8"));
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
			} catch (Exception x) {
				log.warning("Error creating epad properties file", x);
				return;
			} finally {
				if (reader != null)
					IOUtils.closeQuietly(reader);
				else if (is != null)
					IOUtils.closeQuietly(is);
			}
			String propsStr = sb.toString().replace("_HOSTNAME_", hostname);
			EPADFileUtils.write(propertiesFile, propsStr);
		}
		if (clientProperties != null) {
			EPADFileUtils.copyFile(propertiesFile, clientProperties);
		}
	}
	
	
	public static void checkPluginsFile() {
		File pluginsFile = new File(EPADConfig.getEPADWebServerPluginConfigFilePath());
		if (!pluginsFile.exists()) {
			pluginsFile.getParentFile().mkdirs();
			BufferedReader reader = null;
			InputStream is = null;
			StringBuilder sb = new StringBuilder();
			try {
				is = EPADFileUtils.class.getClassLoader().getResourceAsStream(pluginsFile.getName());
				reader = new BufferedReader(new InputStreamReader(is, "UTF8"));
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
			} catch (Exception x) {
				log.warning("Error creating plugin config file", x);
				return;
			} finally {
				if (reader != null)
					IOUtils.closeQuietly(reader);
				else if (is != null)
					IOUtils.closeQuietly(is);
			}
			EPADFileUtils.write(pluginsFile, sb.toString());
		}
	}
	
	static final String[] epadDockerScripts = {
		"epad_docker.sh",
		"stop_dockerepad.sh",
		"start_dockerepad.sh",
		"uninstall_dockerepad.sh",
	};
	static final String[] epadOtherScripts = {		
		"plugin-manager.sh", //plugin manager script file
	};
	

	public static void checkResourcesFolders() {
		File folder = new File(EPADConfig.getEPADWebServerBaseDir() + "bin/");
		if (!folder.exists()) folder.mkdirs();
		folder = new File(EPADConfig.getEPADWebServerBaseDir() + "tmp/");
		if (!folder.exists()) folder.mkdirs();
		folder = new File(EPADConfig.getEPADWebServerResourcesDir());
		if (!folder.exists()) folder.mkdirs();
		folder = new File(EPADConfig.getEPADWebServerEtcDir());
		if (!folder.exists()) folder.mkdirs();
		folder = new File(EPADConfig.getEPADWebServerDICOMScriptsDir());
		if (!folder.exists()) folder.mkdirs();
		folder = new File(EPADConfig.getEPADWebServerUploadDir());
		//ml if (!folder.exists()) folder.mkdirs(); //remove temp dirs
		if (folder.exists()) {
			log.info("Deleting existing temp upload directory "+ folder.getName());
			EPADFileUtils.deleteDirectoryContents(folder);
		}
		else
			folder.mkdirs();
		folder = new File(EPADConfig.getEPADWebServerDownloadDir());
		if (folder.exists()) {
			log.info("Deleting existing temp download directory "+ folder.getName());
			EPADFileUtils.deleteDirectoryContents(folder);
		}
		else
			folder.mkdirs();
		folder = new File(EPADConfig.getEPADWebServerDownloadWSDir());
		if (folder.exists()) {
			log.info("Deleting existing temp downloadWS directory "+ folder.getName());
			EPADFileUtils.deleteDirectoryContents(folder);
		}
		else
			folder.mkdirs();
		folder = new File(EPADConfig.getEPADWebServerFileUploadDir());
		if (!folder.exists()) folder.mkdirs();
		folder = new File(EPADConfig.getEPADWebServerAnnotationsDir());
		if (!folder.exists()) folder.mkdirs();
		folder = new File(EPADConfig.getEPADWebServerAnnotationsUploadDir());
		if (!folder.exists()) folder.mkdirs();
		folder = new File(EPADConfig.getEPADWebServerSchemaDir());
		if (!folder.exists()) folder.mkdirs();
		log.info("checking and copying startup scripts");
		if (System.getenv("DCM4CHEE_NAME")!=null) { //this is docker instance. copy docker versions of scripts
			log.info("getting docker versions");
			copyFiles(System.getProperty("user.home") + "/mac/bin/", EPADConfig.getEPADWebServerBaseDir() + "bin/", "scripts/docker/", epadDockerScripts);
		}
		//copy other scripts
		copyFiles(System.getProperty("user.home") + "/mac/bin/", EPADConfig.getEPADWebServerBaseDir() + "bin/", "scripts/", epadOtherScripts);

	}

	private static void copyFiles(String macDir,String dicomProxyDir,String resourceDir, String[] scripts){
		try
		{
			// Copy start/stop scripts over (mainly for docker)
			File binDir = new File(macDir);
			if (!binDir.exists())
				binDir = new File(dicomProxyDir);
			for (String scriptFile: scripts)
			{
				File file = new File(binDir, scriptFile);
				if (!file.exists()) {
					InputStream in = null;
					OutputStream out = null;
					try {
						in = new Dcm4CheeOperations().getClass().getClassLoader().getResourceAsStream(resourceDir + scriptFile);
			            out = new FileOutputStream(file);
	
			            // Transfer bytes from in to out
			            byte[] buf = new byte[1024];
			            int len;
			            while ((len = in.read(buf)) > 0)
			            {
			                    out.write(buf, 0, len);
			            }
					} catch (Exception x) {
						
					} finally {
			            IOUtils.closeQuietly(in);
			            IOUtils.closeQuietly(out);
					}
				}
				if (file.exists())
					file.setExecutable(true);
			}
		} catch (Exception x) {log.warning("Exception in docker script copy",x);}

	}
	
	private static void configureJettyServer(Server server)
	{
		FileInputStream jettyConfigFileStream = null;
		try {
			String jettyConfigFilePath = EPADConfig.getEPADWebServerJettyConfigFilePath();
			jettyConfigFileStream = new FileInputStream(jettyConfigFilePath);
			XmlConfiguration configuration = new XmlConfiguration(jettyConfigFileStream);
			configuration.configure(server);
			log.info("Jetty server configured using configuration file " + jettyConfigFilePath);
		} catch (FileNotFoundException e) {
			log.warning("Could not find Jetty configuration file " + EPADConfig.getEPADWebServerJettyConfigFilePath());
		} catch (SAXException e) {
			log.warning("SAX error reading Jetty configuration file " + EPADConfig.getEPADWebServerJettyConfigFilePath(), e);
		} catch (IOException e) {
			log.warning("IO error reading Jetty configuration file " + EPADConfig.getEPADWebServerJettyConfigFilePath(), e);
		} catch (Exception e) {
			log.warning("Error processing Jetty configuration file " + EPADConfig.getEPADWebServerJettyConfigFilePath(), e);
		} finally {
			IOUtils.closeQuietly(jettyConfigFileStream);
		}
	}

	public static void initializePlugins()
	{
		PluginController.getInstance();
	}

	public static void startSupportThreads()
	{
		log.info("Starting support threads....");

		try {
			QueueAndWatcherManager.getInstance().buildAndStart();
			String version = new EPadWebServerVersion().getVersion();
			String db_version = version;
			if (db_version.indexOf(".") != db_version.lastIndexOf("."))
			{
				// remove second period;
				db_version = db_version.substring(0, db_version.lastIndexOf(".")) + db_version.substring(db_version.lastIndexOf(".")+1);
			}
			log.info("EpadWS version:" + version + " Database version:" + db_version);
			EpadDatabase.getInstance().startup(db_version);
			log.info("Startup of database was successful");
			EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			//log.info("Checking annotations table");
			databaseOperations.checkAndRefreshAnnotationsTable();
			log.info("Done with database/queues init");
			
			//check nonassigned
			Project nonassgnedproj=DefaultEpadProjectOperations.getInstance().getProject("nonassigned");
			if (nonassgnedproj==null){ //no nonassigned create it
				DefaultEpadProjectOperations.getInstance().createProject("admin", "nonassigned", "nonassigned", "Nonassigned subjects", null, ProjectType.PUBLIC);
			
			
			
			
			}
			//disabling xnat sync September 12, 2017
//			List<User> users = DefaultEpadProjectOperations.getInstance().getAllUsers();
//			if (EPADConfig.UseEPADUsersProjects && users.size() <= 1) {
//				// Sync XNAT to Epad if needed
//				try {
//					XNATSyncHandler.syncXNATtoEpad("admin", "");
//				}
//				catch (Exception x) {
//					log.warning("Error syncing XNAT data", x);
//				}
//			}
			AIMUtil.checkSchemaFiles();
			AIMUtil.checkTemplateFiles();
			checkTemplateProjectRel();
			
		} catch (Exception e) {
			log.warning("Failed to start database", e);
			System.exit(1);
		}
	}

	private static void checkTemplateProjectRel(){
		try {
			EpadOperations epadOperations = DefaultEpadOperations.getInstance();
			if (new ProjectToTemplate().getCount("")<=0) {
				epadOperations.migrateTemplates();
			}
			//check if the templates are new
			epadOperations.checkOldTemplates();

		} catch (Exception e) {
			log.warning("Couldn't get project template relation,",e);
		}
		
	}
	private static void addHandlers(Server server)
	{
		List<Handler> handlerList = new ArrayList<Handler>();

		if (!separateWebServicesApp) {

			loadPluginClasses();
	
			addHandlerAtContextPath(new EPADSessionHandler(), "/epad/session", handlerList);
	
			addHandlerAtContextPath(new EPADHandler(), "/epad/v2", handlerList);
			
			
		}

//      My Attempt to replace above EPADHandler with Spring Controllers
//		- Does not work with embedded jetty, controller methods don't map correctly, controller mapping works fine under tomcat
//		- If someone can make it work, that would be great (comment out add EPADHandler above)
//		try {
//			handlerList.add(getServletContextHandler(getContext()));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			log.warning("Error setting up Spring Handle", e);
//			e.printStackTrace();
//		}
		String webAppPath = System.getProperty("user.home") + "/mac/webapps/" + "ePad.war";  // This is specially for docker on macs
		if (!new File(webAppPath).exists())
		{
			webAppPath = EPADConfig.getEPADWebServerWebappsDir() + "ePad.war"; // This is the usual, normal path
		}
		if (!new File(webAppPath).exists())
		{
			webAppPath = EPADConfig.getEPADWebServerWebappsDir() + "epad-1.1.war"; // This is for development/testing
		}
		if (!new File(webAppPath).exists())
		{
			webAppPath = System.getProperty("user.home") + "/epad/webapps/" + "ePad.war"; // Fallback for docker
			if (!new File(webAppPath).exists())
				throw new RuntimeException("ePad.war not found");
		}
		addWebAppAtContextPath(handlerList, webAppPath, "/epad");

		addHandlerAtContextPath(new ResourceCheckHandler(), "/epad/resources", handlerList);
		addFileServerAtContextPath(EPADConfig.getEPADWebServerResourcesDir(), handlerList, "/epad/resources");

		if (!separateWebServicesApp) {
			addHandlerAtContextPath(new WadoHandler(), "/epad/wado", handlerList);
	
			addHandlerAtContextPath(new AimResourceHandler(), "/epad/aimresource", handlerList);
	
			if (!"true".equalsIgnoreCase(EPADConfig.getParamValue("DISABLE_PLUGINS")))
			{	
				addHandlerAtContextPath(new EPadPluginHandler(), "/epad/plugin", handlerList);
			}
			addHandlerAtContextPath(new EventHandler(), "/epad/eventresource", handlerList);
			addHandlerAtContextPath(new ProjectEventHandler(), "/epad/events", handlerList);
	
			addHandlerAtContextPath(new ServerStatusHandler(), "/epad/status", handlerList);
			addHandlerAtContextPath(new ImageCheckHandler(), "/epad/imagecheck", handlerList);
			addHandlerAtContextPath(new ImageReprocessingHandler(), "/epad/imagereprocess", handlerList);
			addHandlerAtContextPath(new ConvertAIM4Handler(), "/epad/convertaim4", handlerList);
			addHandlerAtContextPath(new CopyAimsToExistHandler(), "/epad/copyToExist", handlerList);
			addHandlerAtContextPath(new XNATSyncHandler(), "/epad/syncxnat", handlerList);
			addHandlerAtContextPath(new StatisticsHandler(), "/epad/statistics", handlerList);
			addHandlerAtContextPath(new ResourcesFileHandler(), "/epad/resourcesFile", handlerList);
			addHandlerAtContextPath(new DownloadHandler(), "/epad/download", handlerList);
			
			addHandlerAtContextPath(new StatusListenerHandler(), "/epad/statuslistener", handlerList);
			
			// TODO This call will disappear when we switch to AIM4
			addHandlerAtContextPath(new CoordinationHandler(), "/epad/coordination", handlerList);
		}

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(handlerList.toArray(new Handler[handlerList.size()]));
		server.setHandler(contexts);
		log.info("Done setting up restapi handlers");
	}

	private static void addHandlerAtContextPath(Handler handler, String contextPath, List<Handler> handlerList)
	{
		ContextHandler contextHandler = new ContextHandler(contextPath);
		contextHandler.setAliases(true);
		contextHandler.setResourceBase(".");
		contextHandler.setClassLoader(Thread.currentThread().getContextClassLoader());
		contextHandler.setHandler(handler);
		handlerList.add(contextHandler);

		log.info("Added " + handler.getClass().getName() + " at context " + contextPath);
	}

	private static final String CONTEXT_PATH = "/epad/v2";
    private static final String CONFIG_LOCATION = "edu.stanford.epad.epadws.config.SpringConfig";
    private static final String MAPPING_URL = "/*";
    private static final String DEFAULT_PROFILE = "dev";    
    
    /**
     * Function for setting up Spring Context with embedded Jetty
     * 
     */
    private static ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath(CONTEXT_PATH);
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(context));
        //contextHandler.setResourceBase(EPADConfig.getEPADWebServerResourcesDir());
        return contextHandler;
    }

    private static WebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        context.getEnvironment().setDefaultProfiles(DEFAULT_PROFILE);
        return context;
    }
	/**
	 * Adds a WAR file from the webapps directory at a context path.
	 * 
	 * @param handlerList List of handlers
	 * @param webAppPath String war file name, with or without extension (e.g., ePad.war)
	 * @param contextPath The context to add the war file (e.g., /epad)
	 */
	private static void addWebAppAtContextPath(List<Handler> handlerList, String webAppPath, String contextPath)
	{
		if (!contextPath.startsWith("/")) {
			contextPath = "/" + contextPath;
		}
		WebAppContext webAppContext = new WebAppContext(webAppPath, contextPath);
		String home = System.getProperty("user.home");
		webAppContext.setTempDirectory(new File(home + "/DicomProxy/jetty")); // TODO Read from config file
		webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
		if (new File(EPADConfig.getEPADWebServerEtcDir()+"webdefault.xml").exists())
		{
			log.info("Adding webdefault.xml");
			webAppContext.setDefaultsDescriptor(EPADConfig.getEPADWebServerEtcDir()+"webdefault.xml");
		}
		log.info("WebAuthFilter:'" + EPADConfig.getParamValue("WebAuthFilter", null) + "'");
		if (EPADConfig.webAuthPassword != null && EPADConfig.getParamValue("WebAuthFilter", null) != null)
		{
			try {
				Class filter = Class.forName(EPADConfig.getParamValue("WebAuthFilter","edu.stanford.epad.epadws.security.WebAuthFilter"));
				webAppContext.addFilter(filter, "/*", EnumSet.of(DispatcherType.REQUEST,DispatcherType.ASYNC,DispatcherType.FORWARD));
			} catch (ClassNotFoundException e) {
				log.warning("WebAuth Authentication Filter " + EPADConfig.getParamValue("WebAuthFilter") + " not found");
			}
		}
		handlerList.add(webAppContext);
		log.info("Added WAR " + webAppPath + " at context path " + contextPath);
	}

	private static void addFileServerAtContextPath(String baseDirectory, List<Handler> handlerList, String contextPath)
	{
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(false);
		resourceHandler.setAliases(true);
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });
		resourceHandler.setResourceBase(baseDirectory);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler, new ResourceFailureLogHandler(), new DefaultHandler() });

		addHandlerAtContextPath(handlers, contextPath, handlerList);

		log.info("Added file server for " + baseDirectory + " directory.");
	}

	public static boolean testPages = false;
	private static void setupTestFiles()
	{
		String deployPath = EPADConfig.getEPADWebServerBaseDir() + "jetty/webapp/test/";
		File testFilesDir = new File(EPADConfig.getEPADWebServerBaseDir() + "webapps/test/");
		try
		{
			if (testFilesDir.exists() && testFilesDir.isDirectory())
			{
				File deployDir = new File(deployPath);
				if (!deployDir.exists())
					deployDir.mkdirs();
				File[] testFiles = testFilesDir.listFiles();
				for (File f: testFiles)
				{
					if (f.isDirectory()) continue;
					File outFile = new File(deployDir, f.getName());
					EPADFileUtils.copyFile(f, outFile);
				}
				testPages = true;
			}
		} 
		catch (Exception x) {
			
		}
		File pluginsDir = new File(EPADConfig.getEPADWebServerBaseDir() + "webapps/plugins/");
		deployPath = EPADConfig.getEPADWebServerBaseDir() + "jetty/webapp/plugins/";
		try
		{
			if (pluginsDir.exists() && pluginsDir.isDirectory())
			{
				File deployDir = new File(deployPath);
				if (!deployDir.exists())
					deployDir.mkdirs();
				File[] pluginFiles = pluginsDir.listFiles();
				for (File f: pluginFiles)
				{
					if (f.isDirectory()) continue;
					File outFile = new File(deployDir, f.getName());
					EPADFileUtils.copyFile(f, outFile);
				}
			}
		} 
		catch (Exception x) {
			
		}
	}

	/**
	 * Load all the plugins into a map.
	 */
	public static void loadPluginClasses()
	{
		try {
			PluginHandlerMap pluginHandlerMap = PluginHandlerMap.getInstance();
			PluginConfig pluginConfig = PluginConfig.getInstance();
			List<String> pluginHandlerList = pluginConfig.getPluginHandlerList();
			PluginOperations pluginOps = PluginOperations.getInstance();
			List<Plugin> plugins = new ArrayList<Plugin>();
			try {
				plugins = pluginOps.getPlugins();
			} catch (Exception x) {};
			
			for (String pluginClassName : pluginHandlerList) {
				log.info("Loading plugin class: " + pluginClassName);
				try
				{
					PluginServletHandler psh = pluginHandlerMap.loadFromClassName(pluginClassName);
					if (psh != null) {
						String pluginName = psh.getName();
						pluginHandlerMap.setPluginServletHandler(pluginName, psh);
					} else {
						log.warning("Could not find plugin class: " + pluginClassName);
					}
				} catch (Exception x) {
					for (Plugin plugin: plugins) {
						if (plugin.getJavaclass().equals(pluginClassName)) {
							plugin.setStatus("Error loading class:" + x.getMessage());
							try {
								plugin.save();
							} catch (Exception x2) {}
						}
					}
				}
			}
		}
		catch (Exception x) {
			log.warning("Error loading plugin", x);
		}
		log.info("Done loading plugins");
	}

	private static void stopServer(Server server)
	{
		try {
			if (server != null) {
				log.info("#####################################################");
				log.info("############### Stopping Jetty server ###############");
				log.info("#####################################################");
				server.stop();
			}
		} catch (Exception e) {
			log.warning("Failed to stop the Jetty server", e);
		}
	}
}
