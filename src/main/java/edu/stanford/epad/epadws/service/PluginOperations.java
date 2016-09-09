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
package edu.stanford.epad.epadws.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import edu.stanford.epad.common.plugins.PluginServletHandler;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADFile;
import edu.stanford.epad.dtos.EPADFileList;
import edu.stanford.epad.dtos.EPADPlugin;
import edu.stanford.epad.dtos.EPADPluginList;
import edu.stanford.epad.dtos.EPADPluginParameter;
import edu.stanford.epad.dtos.EPADPluginParameterList;
import edu.stanford.epad.dtos.EPADTemplateContainer;
import edu.stanford.epad.dtos.EPADTemplateContainerList;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.models.Plugin;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectToPlugin;
import edu.stanford.epad.epadws.models.ProjectToPluginParameter;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.plugins.PluginHandlerMap;
import edu.stanford.epad.epadws.plugins.PluginParameter;
import edu.stanford.epad.epadws.plugins.PluginParameterParser;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;

/**
 * All PlugIn related operations
 * 
 * @author Emel Alkim
 *
 */
public class PluginOperations {

	private static final EPADLogger log = EPADLogger.getInstance();
	private final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
	
	
	private static final PluginOperations ourInstance = new PluginOperations();
	
	
	private PluginOperations()
	{
	}

	public static PluginOperations getInstance()
	{
		return ourInstance;
	}

	public EPADPluginParameterList getParameterForPluginOfProject(String projectId, String pluginId)
			throws Exception {
		Project project = projectOperations.getProject(projectId);
		if (project == null) return new EPADPluginParameterList();
		Plugin plugin=getPlugin(pluginId);
		return getParametersByProjectIdAndPlugin(project.getId(), plugin.getId());
	}

	/**
	 * @param id project id
	 * @param pluginId plugin id
	 * @return plugin parameter list
	 * @throws Exception
	 */
	private EPADPluginParameterList getParametersByProjectIdAndPlugin(long id, long pluginId)
			throws Exception {

			
		List<ProjectToPluginParameter> objects = new ProjectToPluginParameter().getObjects(" project_id =" + id + " and plugin_id =" + pluginId + " ");
		
		EPADPluginParameterList parameters = new EPADPluginParameterList();
		if (objects.isEmpty()) { // no default parameters for project, collect global defaults
			Project unassigned=projectOperations.getProject(EPADConfig.xnatUploadProjectID);
			objects = new ProjectToPluginParameter().getObjects(" project_id =" + unassigned.getId() + " and plugin_id =" +pluginId + " ");
		}
		for (ProjectToPluginParameter object : objects) {
			parameters.addEPADPluginParameter(parameter2EPADPluginParameter(object));
		}
		
		return parameters;
	}

	
	private EPADPluginParameter parameter2EPADPluginParameter(ProjectToPluginParameter parameter) throws Exception
	{
		
		return new EPADPluginParameter(String.valueOf(parameter.getId()),String.valueOf(parameter.getProjectId()),String.valueOf(parameter.getPluginId()),parameter.getName(),parameter.getDefaultValue(),parameter.getType(),parameter.getDescription());
	}
	
	public void setProjectPluginEnable(String loggedInUser, String projectId, String pluginId, String enabled, String sessionID) throws Exception {
		User user=null;
		try {
			user = projectOperations.getUser(loggedInUser);
		} catch (Exception e) {
			log.warning("get user error");
		}
		if (pluginId=="")
			throw new Exception("Plugin id cannot be empty");
		if (projectId=="")
			throw new Exception("Project id cannot be empty");
		
		ProjectToPlugin projectPlugin = getProjectToPlugin(projectId,pluginId);
		if (projectPlugin==null) {
			projectPlugin=new ProjectToPlugin();
			Plugin plugin=getPlugin(pluginId);
			projectPlugin.setPluginId(plugin.getId());
			Project project=projectOperations.getProject(projectId);
			projectPlugin.setProjectId(project.getId());
			
		}
		if (enabled!=null && enabled.equals("true"))
			projectPlugin.setEnabled(true);
		else
			projectPlugin.setEnabled(false);
		projectPlugin.save();
		
	}
	
	private ProjectToPlugin getProjectToPlugin(String projectId,String pluginId) throws Exception {
		Project project=projectOperations.getProject(projectId);
		Plugin plugin=getPlugin(pluginId);
		ProjectToPlugin projectPlugin=new ProjectToPlugin();
		if (project!=null && plugin!=null)
			projectPlugin= (ProjectToPlugin) projectPlugin.getObject("project_id=" + project.getId() + " and plugin_id = " + plugin.getId() );
		return projectPlugin;
	}
	
	public EPADPluginList getPluginSummariesForProject(String projectId, String username, String sessionID) throws Exception {
		List<Plugin> plugins = getPluginsForProject(projectId);
		Project project=projectOperations.getProject(projectId);
		EPADPluginList epadPluginList = new EPADPluginList();
		for (Plugin plugin : plugins) {
			EPADPlugin epadPlugin = plugin2EPADPluginProject(plugin,project,true);
			
			if (epadPlugin != null)
			{
				epadPluginList.addEPADPlugin(epadPlugin);
			}
		}
		List<Plugin> pluginsAll = getPluginsForProject(EPADConfig.xnatUploadProjectID);
		for (Plugin plugin : pluginsAll) {
			boolean isFound=false;
			for(int i=0; i< epadPluginList.ResultSet.totalRecords; i++) {
				log.info("plugin we are looking:"+plugin.getPluginId() + " plugin in list:"+epadPluginList.ResultSet.Result.get(i).pluginId);
				if (plugin.getPluginId().equals(epadPluginList.ResultSet.Result.get(i).pluginId)){
					isFound=true;
					break;
				}
					
			}
			if (!isFound){
				log.info("not found. adding "+plugin.getPluginId());
				EPADPlugin epadPlugin = plugin2EPADPluginProject2(plugin,project,true);
				if (epadPlugin != null)
				{
					epadPluginList.addEPADPlugin(epadPlugin);
				}
			}
				
		}
		return epadPluginList;
	}
	public EPADPluginList getPluginDescriptionsForProject(String projectId, String username, String sessionID) throws Exception {
		List<Plugin> plugins = getPluginsForProject(projectId);
		Project project=projectOperations.getProject(projectId);
		EPADPluginList epadPluginList = new EPADPluginList();
		for (Plugin plugin : plugins) {
			EPADPlugin epadPlugin = plugin2EPADPluginProject(plugin,project,false);
			
			if (epadPlugin != null)
			{
				epadPluginList.addEPADPlugin(epadPlugin);
			}
		}
		List<Plugin> pluginsAll = getPluginsForProject(EPADConfig.xnatUploadProjectID);
		for (Plugin plugin : pluginsAll) {
			boolean isFound=false;
			for(int i=0; i< epadPluginList.ResultSet.totalRecords; i++) {
				if (plugin.getPluginId().equals(epadPluginList.ResultSet.Result.get(i).pluginId)){
					isFound=true;
					break;
				}
			}
			if (!isFound){
				EPADPlugin epadPlugin = plugin2EPADPluginProject2(plugin,project,true);
				if (epadPlugin != null)
				{
					epadPluginList.addEPADPlugin(epadPlugin);
				}
			}
				
		}
		return epadPluginList;
	}
	
	public List<Plugin> getPluginsForProject(String projectId) throws Exception {
		Project project=projectOperations.getProject(projectId);
		if (project==null)
			throw new Exception("Project with id="+projectId+ " not found!");
		List objects = new ProjectToPlugin().getObjects(" project_id = " + project.getId() + " and enabled=1 ");
		List<ProjectToPlugin> projectPlugins = new ArrayList<ProjectToPlugin>();
		
		projectPlugins.addAll(objects);
		List<Plugin> plugins = new ArrayList<Plugin>();
		for (ProjectToPlugin projectPlugin:projectPlugins) {
			Plugin plugin= new Plugin();
			plugin = (Plugin) plugin.getObject("id = " + projectPlugin.getPluginId() );
			plugins.add(plugin);
		}
		return plugins;
	}
	
	public boolean getPlugins(List<String> pluginHandlerList,List<String> pluginTemplateList,List<String> pluginNameList) throws Exception
	{
		List<Plugin> plugins = getPlugins();
		for (Plugin plugin : plugins) {
			pluginHandlerList.add(plugin.getJavaclass());
			pluginTemplateList.add(plugin.getPluginId());
			pluginNameList.add(plugin.getName());
		}
		if (pluginHandlerList.isEmpty())
			return false;
		return true;
	}
	
	
	public EPADPluginList getPluginSummaries(String username, String sessionID, String processMultipleAims) throws Exception {
		List<Plugin> plugins = getPlugins();
		List<Project> projects= projectOperations.getAllProjects();
		boolean isProcessMultipleAims = ("true".equalsIgnoreCase(processMultipleAims));
		
		EPADPluginList epadPluginList = new EPADPluginList();
		for (Project project : projects) {

			for (Plugin plugin : plugins) {
				//if null get all, if not get the ones that are same
				if (processMultipleAims==null || plugin.getProcessMultipleAims()==isProcessMultipleAims) {
					EPADPlugin epadPlugin = plugin2EPADPluginProject(plugin,project,true);
					
					if (epadPlugin != null)
					{
						epadPluginList.addEPADPlugin(epadPlugin);
					}
				}
			}
		}


		return epadPluginList;
	}
	
	public EPADPluginList getPluginSummaries(String username, String sessionID) throws Exception {
		return getPluginSummaries(username, sessionID, null);
	}
	
	public EPADPluginList getPluginDescriptions(String username, String sessionID) throws Exception {
		return getPluginDescriptions(username, sessionID,null);
	}
	public EPADPluginList getPluginDescriptions(String username, String sessionID, String processMultipleAims) throws Exception {
		List<Plugin> plugins = getPlugins();
		List<Project> projects= projectOperations.getAllProjects();
		boolean isProcessMultipleAims = ("true".equalsIgnoreCase(processMultipleAims));
		EPADPluginList epadPluginList = new EPADPluginList();
		for (Project project : projects) {

			for (Plugin plugin : plugins) {
				if (processMultipleAims==null || plugin.getProcessMultipleAims()==isProcessMultipleAims) {
					EPADPlugin epadPlugin = plugin2EPADPluginProject(plugin,project,false);
					
					if (epadPlugin != null)
					{
						epadPluginList.addEPADPlugin(epadPlugin);
					}
				}
			}
		}

		return epadPluginList;
	}
	
	public boolean doesPluginExist(String pluginId, String username, String sessionID) throws Exception {
		Plugin plugin=getPlugin(pluginId);

		if (plugin==null) return false;
		return true;
	}
	
	public EPADPlugin getPluginDescription(String pluginId, String username, String sessionID) throws Exception {
		Plugin plugin=getPlugin(pluginId);

		if (plugin==null) return null;
		Project globalProject=projectOperations.getProject(EPADConfig.xnatUploadProjectID);
		EPADPlugin epadPlugin = plugin2EPADPluginProject(plugin,globalProject,false);
		return epadPlugin;
	}
	
	private EPADPlugin plugin2EPADPluginProject(Plugin plugin,Project project,Boolean returnSummary) throws Exception
	{
		if (project==null){
			project=projectOperations.getProject(EPADConfig.xnatUploadProjectID);
			if (project==null)
				throw new Exception("Global project cannot be retrieved");
		}
		
		ProjectToPlugin projectPlugin = getProjectToPlugin(project.getProjectId(),plugin.getPluginId());
//		log.warning("PROJECT + PLUGIN "+ project.getProjectId() + " " + plugin.getPluginId() + " p2p " + projectPlugin);
		if (projectPlugin==null || !projectPlugin.getEnabled()) //project not enabled
			return null;
		EPADPluginParameterList parameters=getParametersByProjectIdAndPlugin(project.getId(), plugin.getId());
			
		if (returnSummary){

			return new EPADPlugin(plugin.getPluginId(),plugin.getName(),plugin.getDescription(),null,null,null,null,project.getProjectId(),project.getName(),parameters.getResult(),plugin.getDeveloper(),plugin.getDocumentation(),String.valueOf(plugin.getRate()),plugin.getProcessMultipleAims());
		
		}
		return new EPADPlugin(plugin.getPluginId(),plugin.getName(),plugin.getDescription(),plugin.getJavaclass(),plugin.getEnabled(),plugin.getStatus(),plugin.getModality(),project.getProjectId(),project.getName(),parameters.getResult(),plugin.getDeveloper(),plugin.getDocumentation(),String.valueOf(plugin.getRate()),plugin.getProcessMultipleAims());

	}
	
	private EPADPlugin plugin2EPADPluginProject2(Plugin plugin,Project project,Boolean returnSummary) throws Exception
	{
		if (project==null){
			project=projectOperations.getProject(EPADConfig.xnatUploadProjectID);
			if (project==null)
				throw new Exception("Global project cannot be retrieved");
		}
		//disabled check!!!!!!!
//		ProjectToPlugin projectPlugin = getProjectToPlugin(project.getProjectId(),plugin.getPluginId());
////		log.warning("PROJECT + PLUGIN "+ project.getProjectId() + " " + plugin.getPluginId() + " p2p " + projectPlugin);
//		if (projectPlugin==null || !projectPlugin.getEnabled()) //project not enabled
//			return null;
		EPADPluginParameterList parameters=getParametersByProjectIdAndPlugin(project.getId(), plugin.getId());
			
		if (returnSummary){

			return new EPADPlugin(plugin.getPluginId(),plugin.getName(),plugin.getDescription(),null,null,null,null,project.getProjectId(),project.getName(),parameters.getResult(),plugin.getDeveloper(),plugin.getDocumentation(),String.valueOf(plugin.getRate()),plugin.getProcessMultipleAims());
		
		}
		return new EPADPlugin(plugin.getPluginId(),plugin.getName(),plugin.getDescription(),plugin.getJavaclass(),plugin.getEnabled(),plugin.getStatus(),plugin.getModality(),project.getProjectId(),project.getName(),parameters.getResult(),plugin.getDeveloper(),plugin.getDocumentation(),String.valueOf(plugin.getRate()),plugin.getProcessMultipleAims());

	}
	
	public List<Plugin> getPlugins() throws Exception {
		List objects = new Plugin().getObjects("");
		List<Plugin> plugins = new ArrayList<Plugin>();
		plugins.addAll(objects);
		return plugins;
	}
	
	public List<ProjectToPluginParameter> getPluginParameters(String pluginId) throws Exception {
		if (pluginId.trim().equals("")) return null;
		Plugin plugin = new Plugin();
		plugin = (Plugin) plugin.getObject("plugin_id = '" + pluginId+ "'");
		if (plugin==null) return null;
		List objects = new ProjectToPluginParameter().getObjects("plugin_id = " + plugin.getId() );
		List<ProjectToPluginParameter> pluginparams = new ArrayList<ProjectToPluginParameter>();
		pluginparams.addAll(objects);
		return pluginparams;
	}
	
	public Plugin getPlugin(String pluginId) throws Exception {
		Plugin plugin = new Plugin();
		plugin = (Plugin) plugin.getObject("plugin_id = '" + pluginId+ "'");
		return plugin;
	}

	public Plugin getPluginByName(String pluginName) throws Exception {
		Plugin plugin = new Plugin();
		plugin = (Plugin) plugin.getObject("name = '" + pluginName+ "'");
		return plugin;
	}
	
	public Plugin createPlugin(String loggedInUser, String pluginId, String name, String description,
			String javaclass, String enabled, String modality,  String developer, String documentation, String rate, String sessionID) throws Exception {
		return createPlugin(loggedInUser, pluginId, name, description, javaclass, enabled, modality, developer, documentation, rate, sessionID, false);
	}
	public Plugin createPlugin(String loggedInUser, String pluginId, String name, String description,
			String javaclass, String enabled, String modality,  String developer, String documentation, String rate, String sessionID, Boolean processMultipleAims) throws Exception {
		User user=null;
		try {
			user = projectOperations.getUser(loggedInUser);
		} catch (Exception e) {
			log.warning("get user error");
		}
		if (user != null && !user.isAdmin() )
			throw new Exception("No permission to create plugin");
			
		Plugin plugin = new Plugin();
		if (pluginId=="")
			throw new Exception("Plugin id cannot be empty");
		plugin.setPluginId(pluginId);
		if (name=="")
			throw new Exception("Plugin name cannot be empty");
		plugin.setName(name);
		plugin.setDescription(description);
		if (javaclass=="")
			throw new Exception("Plugin handler class cannot be empty");
		plugin.setJavaclass(javaclass);
		if (enabled!=null){
			if (enabled.equals("true"))
				plugin.setEnabled(true);
			else
				plugin.setEnabled(false);
		}
		plugin.setProcessMultipleAims(processMultipleAims);
		
		//plugin.setStatus(status);
		
		if (modality!=null && modality!="") 
			plugin.setModality(modality);

		if (developer!=null && developer!="") 
			plugin.setDeveloper(developer);
		if (documentation!=null && documentation!="") 
			plugin.setDocumentation(documentation);
		if (rate!=null && rate!="") {
			plugin.rate(Integer.parseInt(rate));
		}
		plugin.setCreator(loggedInUser);
		try {
			plugin.save();
		} catch (Exception e) {
			log.warning("save error "+e.getMessage());
		}
		return plugin;
			
	}
	public Plugin updatePlugin(String loggedInUser, String pluginId, String name, String description,
			String javaclass, String enabled, String modality, String developer, String documentation, String rate,String sessionID) throws Exception {
		return updatePlugin(loggedInUser, pluginId, name, description, javaclass, enabled, modality, developer, documentation, rate, sessionID, false);
	}
	
	public Plugin updatePlugin(String loggedInUser, String pluginId, String name, String description,
			String javaclass, String enabled, String modality, String developer, String documentation, String rate,String sessionID, Boolean processMultipleAims) throws Exception {
		
		User user=null;
		try {
			user = projectOperations.getUser(loggedInUser);
		} catch (Exception e) {
			log.warning("get user error");
		}
		if (user != null && !user.isAdmin() )
			throw new Exception("No permission to update plugin");
			
		Plugin plugin = getPlugin(pluginId);
		if (pluginId!=null && pluginId.trim()!="")  //can plugin id be changed?
			plugin.setPluginId(pluginId);
		if (name!=null &&  name.trim()!="")
			plugin.setName(name);
		if (description!=null &&  description.trim()!="")
			plugin.setDescription(description);
		if (javaclass!=null && javaclass.trim()!="")
			plugin.setJavaclass(javaclass);
		if (enabled!=null){
			if (enabled.equals("true"))
				plugin.setEnabled(true);
			else
				plugin.setEnabled(false);
		}
		plugin.setProcessMultipleAims(processMultipleAims);
		//plugin.setStatus(status);
		log.info("updating plugin "+ plugin.getId() +"processmultiple"+ processMultipleAims);
		if (modality!=null && modality!="") 
			plugin.setModality(modality);
		if (developer!=null && developer!="") 
			plugin.setDeveloper(developer);
		if (documentation!=null && documentation!="") 
			plugin.setDocumentation(documentation);
		if (rate!=null && rate!="") {
			plugin.rate(Integer.parseInt(rate));
		}
		plugin.setCreator(loggedInUser);
		try {
			plugin.save();
		} catch (Exception e) {
			log.warning("save error "+e.getMessage());
		}
		return plugin;
			
	}
	
	
	public void deletePlugin(String username, String pluginId)
			throws Exception {
		User loggedIn = projectOperations.getUser(username);
		if (loggedIn != null && !loggedIn.isAdmin())
			throw new Exception("No permission to delete plugin");
		Plugin plugin = getPlugin(pluginId);
		if (plugin == null)
			throw new Exception("Plugin not found for id " + pluginId);
		new ProjectToPluginParameter().deleteObjects("plugin_id=" + plugin.getId());
		new ProjectToPlugin().deleteObjects("plugin_id=" + plugin.getId());
		
		plugin.delete();
		
	}
	
	public void deleteParameters(String username, String pluginId)
			throws Exception {
		User loggedIn = projectOperations.getUser(username);
		if (loggedIn != null && !loggedIn.isAdmin())
			throw new Exception("No permission to delete plugin");
		Plugin plugin = getPlugin(pluginId);
		if (plugin == null)
			throw new Exception("Plugin not found for id " + pluginId);
		new ProjectToPluginParameter().deleteObjects("plugin_id=" + plugin.getId());
		
	}

	public boolean addParameters(String loggedInUser,String projectId, String pluginId, String[] paramNames, String[] paramValues) throws Exception {
		//if the project is not unassigned, get the global parameter list
		//change default values for the defined parameters
		//keep global defaults for undefined ones
		if (!projectId.equals(EPADConfig.xnatUploadProjectID)) {
			EPADPluginParameterList globalParams=getParameterForPluginOfProject(EPADConfig.xnatUploadProjectID, pluginId);
			Map<String,String> params=new HashMap<String,String>();
			for (int i=0; i<paramNames.length;i++){
				params.put(paramNames[i], paramValues[i]);
			}
			
			for (EPADPluginParameter param : globalParams.getResult()) {  
				if (params.containsKey(param.getName())) {
					setParameter(loggedInUser, projectId, pluginId, param.getName(), params.get(param.getName()));
					params.put(param.getName(),"");
					log.info("existing param name:" + param.getName() + " value " + params.get(param.getName()));
				} else {
					setParameter(loggedInUser, projectId, pluginId, param.getName(), param.getDefaultValue());
					log.info("not existing param name:" + param.getName() + " value " + param.getDefaultValue());
					
				}
					
			}
			//params'ta olup global'de olmayan icin hata
			for (String paramName:params.keySet()) {
				if (params.get(paramName)!="")  {
					setParameter(loggedInUser, projectId, pluginId, paramName, params.get(paramName));
					params.put(paramName,"");
					log.info("existing param name:" + paramName + " value " + params.get(paramName));
				}
					
			}
			
			
		}
		else {
			for (int i=0; i<paramNames.length;i++){
				setParameter(loggedInUser, projectId, pluginId, paramNames[i], paramValues[i]);
			}
		}
		return true;
	}
	public void setParameter(String loggedInUser,String projectId, String pluginId, String paramName, String defaultValue) throws Exception {
		setParameter(loggedInUser, projectId, pluginId, paramName, defaultValue,null, null);
		
	}
	
	public void setParameter(String loggedInUser,String projectId, String pluginId, String paramName, String defaultValue, String type, String description) throws Exception {
		User user=null;
		try {
			user = projectOperations.getUser(loggedInUser);
		} catch (Exception e) {
			log.warning("get user error");
		}
		if (user != null && !user.isAdmin() )
			throw new Exception("No permission to add plugin parameter");
		
		Project project = projectOperations.getProject(projectId);
		if (project == null )
			throw new Exception("No project with id " + projectId);
		ProjectToPluginParameter param=getParameter(loggedInUser, projectId, pluginId, paramName); //get the parameter to update
		if (param==null) {
			param= new ProjectToPluginParameter(); //create new if not db tuple with the param name

			param.setProjectId(project.getId());
			Plugin plugin = getPlugin(pluginId);
			param.setPluginId(plugin.getId());
			param.setName(paramName);


		}
		param.setDefaultValue(defaultValue);
		param.setCreator(loggedInUser);
		if (type!=null)
			param.setType(type);
		if (description!=null)
			param.setDescription(description);
		param.save();
		
	}
	
	public ProjectToPluginParameter getParameter(String loggedInUser,String projectId, String pluginId, String paramName) throws Exception {
		Project project = projectOperations.getProject(projectId);
		if (project == null) return null;
		Plugin plugin=getPlugin(pluginId);
		if (plugin == null) return null;
		ProjectToPluginParameter param = new ProjectToPluginParameter();
		param = (ProjectToPluginParameter) param.getObject("project_id=" + project.getId() + " and plugin_id = " + plugin.getId()+ "  and name = '" + paramName + "'");
		return param;
	}
	
	
	
	
	 /** 
	 * @param templateFile
	 * @return a String array containing code value, code meaning pair
	 */
	public String[] getTemplateCodeAndMeaning(String templateFile){
		String templateName = "";
		String templateType = "";
		String templateLevelType = "";
		String templateCode = "";
//		File tfile = new File(EPADConfig.getEPADWebServerResourcesDir() + getEpadFilePath(efile));
		File tfile = new File(templateFile);
		
		String xml="";
		try {
			xml = EPADFileUtils.readFileAsString(tfile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject root = XML.toJSONObject(xml);
		JSONObject container = root.getJSONObject("TemplateContainer");
		JSONArray templateObjs = new JSONArray();
		try {
			JSONObject templateObj = container.getJSONObject("Template");
			templateObjs.put(templateObj);
		}
		catch (Exception x) {
			templateObjs = container.getJSONArray("Template");
		}
		for (int i = 0; i < templateObjs.length(); i++)
		{
			JSONObject templateObj = templateObjs.getJSONObject(i);
			String templateUID = templateObj.getString("uid");
			templateName = templateObj.getString("name");
			templateType = templateObj.getString("codeMeaning");
			templateCode = templateObj.getString("codeValue");
		}
		return new String[]{templateCode,templateType};
	}
	
	public List<String> getClassNames(String jarFile){
		List<String> classNames = new ArrayList<String>();
		ZipInputStream zip;
		try {
			zip = new ZipInputStream(new FileInputStream(jarFile));
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
				//$ for inner classes
			    if (!entry.isDirectory() && entry.getName().endsWith(".class") && !entry.getName().contains("$")) {
			        // This ZipEntry represents a class. Now, what class does it represent?
			        String className = entry.getName().replace('/', '.'); // including ".class"
			        className=className.substring(0, className.length() - ".class".length());
			        Class pluginClass = Class.forName(className);
					if ((pluginClass.newInstance() instanceof PluginServletHandler)) {
				        classNames.add(className);
				        log.info("class found:"+className);
					}else {
						log.info("not plugin class found:"+className);
					}
			    }
			}
		} catch (FileNotFoundException e) {
			log.warning("Jar file not found");
		} catch (IOException e) {
			log.warning("IO exception ",e);
		} catch (ClassNotFoundException e) {
			log.warning("Class not found exception ",e);
		} catch (InstantiationException e) {
			log.warning("Class InstantiationException ",e);
		} catch (IllegalAccessException e) {
			log.warning("IllegalAccessException ",e);
		}
		
		return classNames;
	}
	
	public EPADTemplateContainer getTemplateWithCode(String code) {
		EpadOperations epadOp = DefaultEpadOperations.getInstance();
		EPADTemplateContainer dbTemplate=null;
		EPADTemplateContainerList containers;
		try {
			containers = epadOp.getTemplateDescriptions("admin","");
		
			for (int i=0; i<containers.ResultSet.totalRecords; i++) {
				if (containers.ResultSet.Result.get(i).templateCode.equals(code)) {
					dbTemplate= containers.ResultSet.Result.get(i);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception getting the template with code "+ code +" "+e.getMessage());
		}
		return dbTemplate;
	}
	
	public String createLocalPlugin(String jarFile,String descFile,String templateFile,String className, boolean processMultipleAims, String name, boolean overwrite) {
		String sessionID="";
		String pluginName="";
		String templateId=null;
		String templateType=null;
		String user="admin";
		EpadOperations epadOp = DefaultEpadOperations.getInstance();
		String error="SUCCESS! Please restart ePad";
		try {
			
			/************************** required check ***********************/
			
			if (jarFile==null && name==null)
				return "Jar file path or name is required";
			if (templateFile==null && name==null)
				return "Template file path or name is required";
			
			/************************** retrievals ***********************/
			
			
			//get the id and type from input template (codevalue and meaning)
			
			if (templateFile!=null) {
				String[] templateInfo=null;
				templateInfo=getTemplateCodeAndMeaning(templateFile);
				templateId=templateInfo[0];
				templateType=templateInfo[1];
			}
			
			//get the class names from the jar file
			if (jarFile!=null) {
				List<String> classes=getClassNames(jarFile);
				if (classes.size()>1 && className==null) {
					return "Multiple plugin handler classes in jar file. Class name required";
				}
				if (classes.size()==1) {
					System.out.println("Found class "+ classes.get(0));
					if (className!=null && !classes.get(0).equals(className)) {
						System.out.println("Input class name does not exist in the jar file. Using "+ classes.get(0));
					}
					className=classes.get(0);
				}
				if (classes.size()==0)
					return "No plugin handler class in the jar";
			}
			/************************** controls ***********************/
			//check the jar.. we do this twice.. second time do not check
			if (jarFile!=null) {
				File jar=new File(jarFile);
				File dest=new File(EPADConfig.getEPADWebServerBaseDir()+"lib/plugins/"+jar.getName());
				if (dest.exists() && !overwrite)
					return "The jar file already exists.Try running with -o option if you wantto overwrite the file";
				
			}
			
			if (className!=null) {
				PluginHandlerMap pluginHandlerMap = PluginHandlerMap.getInstance();
				
				//check if plugin class exists in jar
				PluginServletHandler psh = pluginHandlerMap.loadFromClassName(className);
				if (psh != null) {
					pluginName = psh.getName();
					
				} else {
					return "Could not find plugin class: " + className+ ". Check your class name";
				}
				//if name is null just use the pluginname from class
				if (name==null) {
					name=pluginName;
				}
				else if (!pluginName.equals(name) && !overwrite) { //check the class's getName. is it same with input name
					return "The class specifies the plugin name as "+pluginName+" in the getName() method. You have entered "+ name + " as the name. It should be the same";
				}
			}
			
			//check if template has epad-plugin in code-meaning
			if (templateType!=null && !templateType.equalsIgnoreCase("epad-plugin"))
				return "The template code meaning should be epad-plugin for plugin to be triggered automatically. You have " +templateType+". Change the code meaning in template";
			
			//if new plugin check if it matches with something already in the system
			if (!overwrite) {
				//check if the plugin with name or id already exists
				if (getPlugin(templateId)!= null)
					return "The template code value is used as plugin id. This code value " +templateId+" is already used. Change the code value in template or try with option -o if you want to update";
				
				if (getPluginByName(name)!=null)
					return "There is already a plugin with the name "+ name+ " try with option -o if you want to update";
			
				//check if template already exists
				if (getTemplateWithCode(templateId)!=null)
					return "The template with code value " +templateId+" is already used. Change the code value in template try with option -o if you want to update";
			
			} 
				
			
			
			
			
			String description="";
			String modality="";//doesnot exist in slicer's file
			String developer="";
			String documentation="";
			String rate="";//doesnot exist
			
			//TODO xsd validation??
			//extract information of plugin from parameters descFile
			if (descFile!=null){
				PluginParameterParser parser=PluginParameterParser.getInstance();
				parser.parse(descFile);
				documentation=parser.getDocumentation();
				description=parser.getDescription();
				developer=parser.getContributor();
			}
			
			
			/************************** create or update the plugin ***********************/
			//copy the jar. second time do not check
			if (jarFile!=null) {
				File jar=new File(jarFile);
				File dest=new File(EPADConfig.getEPADWebServerBaseDir()+"lib/plugins/"+jar.getName());
				FileUtils.copyFile(jar,dest);
			}
			
			//create plugin if not overwrite
			if (!overwrite){
				createPlugin(user, templateId, name, description, className, "true", modality, developer, documentation, rate, sessionID,processMultipleAims);
				setProjectPluginEnable(user, EPADConfig.xnatUploadProjectID, templateId, "true", sessionID);
				System.out.println("Plugin "+name + " is created");
			}else { //if overwrite make the checks and update
				//we are trying to update
				//check if the plugin exists, it should!
				Plugin p=getPluginByName(name);
				if (p==null)
					return "There is no plugin with the name "+ name;
				//if pluginName is not empty(no jar file) and different from the given name change the name
				if (!pluginName.equals("") && !pluginName.equals(name)) {
					if (getPluginByName(pluginName)!=null)
						return "A plugin with name " + pluginName + " already exists";
					System.out.println("Updating the plugin name from "+name+ " to "+ pluginName);
					p.setName(pluginName);
					p.save();
				}
				//if template not empty and different, change the template and plugin id. 
				if (templateFile!=null) {
					//find the template in the system
					EPADTemplateContainer dbTemplate= getTemplateWithCode(p.getPluginId());
					
					//remove old template
					if (dbTemplate!=null)
						epadOp.deleteFile(user, new ProjectReference(EPADConfig.xnatUploadProjectID), dbTemplate.fileName);
					
					//if template has a new code update plugin id
					//if the plugin id is the same with the new templates template code, they are just trying to update the template delete is enough
					if (!templateId.equals(p.getPluginId())){
						if (getTemplateWithCode(templateId)!=null)
							return "The template with code value " +templateId+" is already used. Change the code value in template";
						System.out.println("Updating the plugin id from "+p.getPluginId()+ " to "+ templateId);
						p.setPluginId(templateId);
						p.save();
						
					}
					
				}else { //if no template file get the plugin id (needed for parameters)
					templateId=p.getPluginId();
				}
				if (descFile!=null) {
					try{
						//delete all the parameter files that is for this plugin, there should be only one!
						ProjectReference pr =new ProjectReference(EPADConfig.xnatUploadProjectID);
						EPADFileList files=epadOp.getFileDescriptions(pr, user, sessionID,new EPADSearchFilter(),false);
						for (EPADFile f:files.ResultSet.Result) {
							if (f.description.equals(templateId) && (f.fileType.equals("") || f.fileType.equals(FileType.PARAMETERS.getName()))){
								epadOp.deleteFile(user, pr, f.fileName);
							}
						}
						//check if the properties retrieved from desc file changed (description, etc)
						p.setDescription(description);
						p.setDocumentation(documentation);
						p.setDeveloper(developer);
						p.save();
						
					}catch (Exception e){
						log.info("File "+descFile+" doesn't exist");
					}
				}
				
				
				
			}
			//create plugin parameters if there is a desc file
			if (descFile!=null) {
				//deletes if there are any and adds 
				updateparams(descFile,name,overwrite);
//				if (templateId!=null &&!setParameters(descFile, templateId))
//					return "Couldn't create parameters";
			}
			
			//save template
			//TODO local??
			if (templateFile!=null){
				epadOp.createFile(user, EPADConfig.xnatUploadProjectID, null, null, null,
						new File(templateFile), "local", FileType.TEMPLATE.getName(), sessionID);
			}
			
			
			//save parameters descFile
			//put pluginid in description
			if (descFile!=null){
				epadOp.createFile(user, EPADConfig.xnatUploadProjectID, null, null, null,
					new File(descFile), templateId, FileType.PARAMETERS.getName(), sessionID);
			}
			
			
		
		} catch (Exception e) {
			log.warning(e.getMessage(),e);
			error="Exception occured. "+e.getMessage();
			
		}
		
		return error;
	}
	
	public boolean setParameters(String descFile, String pluginId) {
		String user="admin";
		if (descFile!=null){
			PluginParameterParser parser=PluginParameterParser.getInstance();
			List<PluginParameter> parameters= parser.parse(descFile);
			for (PluginParameter p:parameters) {
				try {
					setParameter(user, EPADConfig.xnatUploadProjectID, pluginId, p.getName(), p.getDefaultVal(), p.getType(), p.getDescription());
				} catch (Exception e) {
					System.out.println("Problem setting the parameters:");
				}
			}
		}
		return true;
	}
	
	public String updateparams(String descFile, String pluginName, boolean overwrite){
		Project proj;
		try {
			proj = projectOperations.getProject(EPADConfig.xnatUploadProjectID);
		
			Plugin plugin= getPluginByName(pluginName);
			EPADPluginParameterList pList=getParametersByProjectIdAndPlugin(proj.getId(), plugin.getId());
			if (pList.ResultSet.totalRecords>0 && !overwrite)
				return "Plugin already has parameters. Try running with -o option if you wantto delete all and add again. This will remove all default settings for all the projects";
			deleteParameters("admin", plugin.getPluginId());
			if (!setParameters(descFile, plugin.getPluginId()))
				return "Couldn't set parameters";
		} catch (Exception e) {
			return "Exception occured."+e.getMessage();
		}
		return "SUCCESS! Parameters are updated";
		
	}
	
	public static String getValue(String[] argv, int i) {
		if (i>=argv.length) {
			return null;
		}
		if (argv[i].startsWith("-")){
			System.out.println(argv[i] +" is not a value");
			return null;
		}
		return argv[i];
	}
	
	public static void displayHelp() {
		System.out.println("This script analyses the input files and creates a plugin, uploads the template and the parameter files if exists and copies the jar to plugins directory");
		System.out.println("Parameters");
		System.out.println("\t-j \tJar file full path");
		System.out.println("\t-t \tTemplate file full path");
		System.out.println("\t-p \tParameter file full path");
		System.out.println("\t-c \tClass name (including the package name, case sensitive). If you do not specify we try extracting from the jar file");
		System.out.println("\t-m \tDoes the plugin process multiple aims at once? Default is false.");
		System.out.println("\t-n \tName of the plugin. Should be same with the name in the handler class. If you do not give a name the name in the class is used");
		System.out.println("\t-o \tOverwrite");
		System.out.println("\t-h \tDisplay help");
		
		System.out.println("Sample usages");
		System.out.println("Creating plugin");
		System.out.println("./plugin-manager.sh -j ~/myplugin-1.1.jar -t /root/myplugin/myplugin-template.xml \n\t: extracts all information from the files");
		System.out.println("./plugin-manager.sh -j ~/myplugin-1.1.jar -t /root/myplugin/myplugin-template.xml -c edu.stanford.epad.plugins.myplugin.MyPluginHandler -m false -n myplugin  \n\t: more detailed parameters, manager checks if they match");
		System.out.println("./plugin-manager.sh -j ~/myplugin-1.1.jar -t /root/myplugin/myplugin-template.xml -o \n\t: overwrite the plugin with new information extracted from the files");
		System.out.println("Adding parameters");
		System.out.println("./plugin-manager.sh -p /root/myplugin/myplugin-parameters.xml -n myplugin -o \n\t: adds the parameters in the file to the plugin, deletes all existing and adds from scratch");

	}
	
	public static void main(String[] argv) {
		if (argv.length==0){
			System.out.println("No parameters specified");
			displayHelp();
			return;
		}
		
		
		String jarFile=null;
		String descFile=null;
		String templateFile=null;
		String className=null;
		boolean processMultipleAims=false;
		String name=null;
		String description=null;
		boolean overwrite=false;
		boolean notKnown=false;
		for (int i=0; i < argv.length; i++) {
			notKnown=false; 
			switch (argv[i]) {
			case "-j":
				jarFile=getValue(argv,i+1);
				break;
			case "-p":
				descFile=getValue(argv,i+1);
				break;
			case "-t":
				templateFile=getValue(argv,i+1);
				break;
			case "-c":
				className=getValue(argv,i+1);
				break;
			case "-m":
				processMultipleAims="true".equals(getValue(argv,i+1));
				break;
			case "-n":
				name=getValue(argv,i+1);
				break;
			case "-h":
				displayHelp();
				break;
			case "-o":
				overwrite=true;
				break;
			default:
				notKnown=true;
			}
			if (notKnown && argv[i].startsWith("-")){
				System.out.println("Unknown parameter:"+argv[i]);
				return;
			}
		}

		PluginOperations po=new PluginOperations();
		String resultMsg=po.createLocalPlugin(jarFile, descFile, templateFile, className, processMultipleAims, name,overwrite);
		System.out.println(resultMsg);
	}


}


