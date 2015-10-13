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


