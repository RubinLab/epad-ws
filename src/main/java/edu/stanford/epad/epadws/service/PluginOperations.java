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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADPlugin;
import edu.stanford.epad.dtos.EPADPluginList;
import edu.stanford.epad.dtos.EPADPluginParameter;
import edu.stanford.epad.dtos.EPADPluginParameterList;
import edu.stanford.epad.epadws.models.Plugin;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectToPlugin;
import edu.stanford.epad.epadws.models.ProjectToPluginParameter;
import edu.stanford.epad.epadws.models.User;

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
		
		return new EPADPluginParameter(String.valueOf(parameter.getId()),String.valueOf(parameter.getProjectId()),String.valueOf(parameter.getPluginId()),parameter.getName(),parameter.getDefaultValue());
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
	
	public EPADPluginList getPluginSummaries(String username, String sessionID) throws Exception {
		List<Plugin> plugins = getPlugins();
		List<Project> projects= projectOperations.getAllProjects();
		EPADPluginList epadPluginList = new EPADPluginList();
		for (Project project : projects) {

			for (Plugin plugin : plugins) {
				EPADPlugin epadPlugin = plugin2EPADPluginProject(plugin,project,true);
				
				if (epadPlugin != null)
				{
					epadPluginList.addEPADPlugin(epadPlugin);
				}
			}
		}


		return epadPluginList;
	}
	
	public EPADPluginList getPluginDescriptions(String username, String sessionID) throws Exception {
		List<Plugin> plugins = getPlugins();
		List<Project> projects= projectOperations.getAllProjects();
		EPADPluginList epadPluginList = new EPADPluginList();
		for (Project project : projects) {

			for (Plugin plugin : plugins) {
				EPADPlugin epadPlugin = plugin2EPADPluginProject(plugin,project,false);
				
				if (epadPlugin != null)
				{
					epadPluginList.addEPADPlugin(epadPlugin);
				}
			}
		}

		return epadPluginList;
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
		log.warning("PROJECT + PLUGIN "+ project.getProjectId() + " " + plugin.getPluginId() + " p2p " + projectPlugin);
		if (projectPlugin==null || !projectPlugin.getEnabled()) //project not enabled
			return null;
		EPADPluginParameterList parameters=getParametersByProjectIdAndPlugin(project.getId(), plugin.getId());
			
		if (returnSummary){

			return new EPADPlugin(plugin.getPluginId(),plugin.getName(),plugin.getDescription(),null,null,null,null,project.getProjectId(),project.getName(),parameters.getResult(),plugin.getDeveloper(),plugin.getDocumentation(),String.valueOf(plugin.getRate()));
		
		}
		return new EPADPlugin(plugin.getPluginId(),plugin.getName(),plugin.getDescription(),plugin.getJavaclass(),plugin.getEnabled(),plugin.getStatus(),plugin.getModality(),project.getProjectId(),project.getName(),parameters.getResult(),plugin.getDeveloper(),plugin.getDocumentation(),String.valueOf(plugin.getRate()));

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
			log.warning("save error");
		}
		return plugin;
			
	}

	
	public Plugin updatePlugin(String loggedInUser, String pluginId, String name, String description,
			String javaclass, String enabled, String modality, String developer, String documentation, String rate,String sessionID) throws Exception {
		
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
			log.warning("save error");
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
	
	

}


