package edu.stanford.epad.epadws.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADPlugin;
import edu.stanford.epad.dtos.EPADPluginList;
import edu.stanford.epad.dtos.EPADPluginParameter;
import edu.stanford.epad.dtos.EPADPluginParameterList;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;
import edu.stanford.epad.epadws.handlers.core.PluginReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.models.Plugin;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectToPlugin;
import edu.stanford.epad.epadws.models.ProjectToPluginParameter;
import edu.stanford.epad.epadws.models.ProjectToSubject;
import edu.stanford.epad.epadws.models.ProjectType;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;
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
		EPADPluginList epadPluginList = new EPADPluginList();
		for (Plugin plugin : plugins) {
			EPADPlugin epadPlugin = plugin2EPADPlugin(plugin,true);
			
			if (epadPlugin != null)
			{
				epadPluginList.addEPADPlugin(epadPlugin);
			}
		}
		return epadPluginList;
	}
	public EPADPluginList getPluginDescriptionsForProject(String projectId, String username, String sessionID) throws Exception {
		List<Plugin> plugins = getPluginsForProject(projectId);
		EPADPluginList epadPluginList = new EPADPluginList();
		for (Plugin plugin : plugins) {
			EPADPlugin epadPlugin = plugin2EPADPlugin(plugin,false);
			
			if (epadPlugin != null)
			{
				epadPluginList.addEPADPlugin(epadPlugin);
			}
		}
		return epadPluginList;
	}
	
	public List<Plugin> getPluginsForProject(String projectId) throws Exception {
		Project project=projectOperations.getProject(projectId);
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
	
	public EPADPluginList getPluginSummaries(String username, String sessionID) throws Exception {
		List<Plugin> plugins = getPlugins();
		EPADPluginList epadPluginList = new EPADPluginList();
		for (Plugin plugin : plugins) {
			EPADPlugin epadPlugin = plugin2EPADPlugin(plugin,true);
			
			if (epadPlugin != null)
			{
				epadPluginList.addEPADPlugin(epadPlugin);
			}
		}
		return epadPluginList;
	}
	
	public EPADPluginList getPluginDescriptions(String username, String sessionID) throws Exception {
		List<Plugin> plugins = getPlugins();
		EPADPluginList epadPluginList = new EPADPluginList();
		for (Plugin plugin : plugins) {
			EPADPlugin epadPlugin = plugin2EPADPlugin(plugin,false);
			
			if (epadPlugin != null)
			{
				epadPluginList.addEPADPlugin(epadPlugin);
			}
		}
		return epadPluginList;
	}
	
	public EPADPlugin getPluginDescription(String pluginId, String username, String sessionID) throws Exception {
		Plugin plugin=getPlugin(pluginId);

		if (plugin==null) return null;
		EPADPlugin epadPlugin = plugin2EPADPlugin(plugin,false);
		return epadPlugin;
	}
	
	private EPADPlugin plugin2EPADPlugin(Plugin plugin,Boolean returnSummary) throws Exception
	{
		if (returnSummary)
			return new EPADPlugin(plugin.getPluginId(),plugin.getName(),plugin.getDescription(),null,null,null,null);

		return new EPADPlugin(plugin.getPluginId(),plugin.getName(),plugin.getDescription(),plugin.getJavaclass(),plugin.getEnabled(),plugin.getStatus(),plugin.getModality());
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

	public Plugin createPlugin(String loggedInUser, String pluginId, String name, String description,
			String javaclass, String enabled, String modality, String sessionID) throws Exception {
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

		plugin.setCreator(loggedInUser);
		try {
			plugin.save();
		} catch (Exception e) {
			log.warning("save error");
		}
		return plugin;
			
	}

	public Plugin updatePlugin(String loggedInUser, String pluginId, String name, String description,
			String javaclass, String enabled, String modality, String sessionID) throws Exception {
		
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

	public void addParameters(String loggedInUser,String projectId, String pluginId, String[] paramNames, String[] paramValues) throws Exception {
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
					log.info("existing param name:" + param.getName() + " value " + params.get(param.getName()));
				} else {
					setParameter(loggedInUser, projectId, pluginId, param.getName(), param.getDefaultValue());
					log.info("not existing param name:" + param.getName() + " value " + param.getDefaultValue());
					
				}
					
			}
			
		}
		else {
			for (int i=0; i<paramNames.length;i++){
				setParameter(loggedInUser, projectId, pluginId, paramNames[i], paramValues[i]);
			}
		}
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


