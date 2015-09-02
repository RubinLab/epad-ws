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
		
		return getParametersByProjectIdAndPlugin(project.getId(), pluginId);
	}

	/**
	 * @param id project id
	 * @param pluginId plugin id
	 * @return plugin parameter list
	 * @throws Exception
	 */
	private EPADPluginParameterList getParametersByProjectIdAndPlugin(long id, String pluginId)
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
	
	public EPADPlugin getPluginDescription(PluginReference pluginRef,String username, String sessionID) throws Exception {
		Plugin plugin=getPlugin(pluginRef.pluginID);
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
			String javaclass, String enabled, String sessionID) {
		User user=null;
		try {
			user = projectOperations.getUser(loggedInUser);
		} catch (Exception e) {
			log.warning("get user error");
		}
		if (user != null && !user.isAdmin() )
			try {
				throw new Exception("No permission to create plugin");
			} catch (Exception e) {
				log.warning("No permission");
			}
		Plugin plugin = new Plugin();
		plugin.setPluginId(pluginId);
		plugin.setName(name);
		plugin.setDescription(description);
		plugin.setJavaclass(javaclass);
		if (enabled.equals("true"))
			plugin.setEnabled(true);
		else
			plugin.setEnabled(false);
		//plugin.setStatus(status);
		
		plugin.setCreator(loggedInUser);
		try {
			plugin.save();
		} catch (Exception e) {
			log.warning("save error");
		}
		return plugin;
			
	}
}
