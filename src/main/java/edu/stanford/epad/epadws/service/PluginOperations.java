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

			
		List<ProjectToPluginParameter> objects = new ProjectToPluginParameter().getObjects(" project_id =" + id + " and plugin_id =" +pluginId + " ");
		
		EPADPluginParameterList parameters = new EPADPluginParameterList();
		for (ProjectToPluginParameter object : objects) {
			parameters.addEPADPluginParameter(parameter2EPADPluginParameter(object));
		}
		
		return parameters;
	}

	
	private EPADPluginParameter parameter2EPADPluginParameter(ProjectToPluginParameter parameter) throws Exception
	{
		
		return new EPADPluginParameter(String.valueOf(parameter.getId()),String.valueOf(parameter.getProjectId()),String.valueOf(parameter.getPluginId()),parameter.getName(),parameter.getDefaultValue());
	}
	
	
	public EPADPluginList getPluginDescriptions(String username, String sessionID) throws Exception {
		List<Plugin> plugins = getPlugins();
		EPADPluginList epadPluginList = new EPADPluginList();
		for (Plugin plugin : plugins) {
			EPADPlugin epadPlugin = plugin2EPADPlugin(plugin);
			
			if (epadPlugin != null)
			{
				epadPluginList.addEPADPlugin(epadPlugin);
			}
		}
		return epadPluginList;
	}
	
	public EPADPlugin getPluginDescription(String pluginId,String username, String sessionID) throws Exception {
		Plugin plugin=getPlugin(pluginId);

		if (plugin==null) return null;
		EPADPlugin epadPlugin = plugin2EPADPlugin(plugin);
		return epadPlugin;
	}
	
	private EPADPlugin plugin2EPADPlugin(Plugin plugin) throws Exception
	{
		
		return new EPADPlugin(plugin.getPluginId(),plugin.getName(),plugin.getDescription(),plugin.getJavaclass(),plugin.getEnabled(),plugin.getStatus());
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
			String javaclass, String enabled, String sessionID) throws Exception {
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
		
		plugin.setCreator(loggedInUser);
		try {
			plugin.save();
		} catch (Exception e) {
			log.warning("save error");
		}
		return plugin;
			
	}

	public Plugin updatePlugin(String loggedInUser, String pluginId, String name, String description,
			String javaclass, String enabled, String sessionID) throws Exception {
		
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

	public void addParameter(String loggedInUser,String projectId, String pluginId, String paramName, String defaultValue) throws Exception {
		User user=null;
		try {
			user = projectOperations.getUser(loggedInUser);
		} catch (Exception e) {
			log.warning("get user error");
		}
		if (user != null && !user.isAdmin() )
			throw new Exception("No permission to add plugin parameter");
		
		ProjectToPluginParameter param=new ProjectToPluginParameter();
		Project project = projectOperations.getProject(projectId);
		param.setProjectId(project.getId());
		Plugin plugin = getPlugin(pluginId);
		param.setPluginId(plugin.getId());
		param.setName(paramName);
		param.setDefaultValue(defaultValue);
		param.setCreator(loggedInUser);
		param.save();
		
	}

}

