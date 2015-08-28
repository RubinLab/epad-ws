package edu.stanford.epad.epadws.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADPluginParameter;
import edu.stanford.epad.dtos.EPADPluginParameterList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectToPluginParameter;
import edu.stanford.epad.epadws.models.ProjectToSubject;
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

	public EPADPluginParameterList getParameterForTemplateForProject(String projectId, String templateCode)
			throws Exception {
		Project project = projectOperations.getProject(projectId);
		if (project == null) return new EPADPluginParameterList();
		
		return getParametersByProjectIdAndTemplate(project.getId(), templateCode);
	}

	/**
	 * @param id project id
	 * @param templateCode template code
	 * @return plugin parameter list
	 * @throws Exception
	 */
	private EPADPluginParameterList getParametersByProjectIdAndTemplate(long id, String templateCode)
			throws Exception {

			
		List<ProjectToPluginParameter> objects = new ProjectToPluginParameter().getObjects(" project_id =" + id + " and templateCode ='" +templateCode + "' ");
		
		EPADPluginParameterList parameters = new EPADPluginParameterList();
		for (ProjectToPluginParameter object : objects) {
			parameters.addEPADPluginParameter(parameter2EPADPluginParameter(object));
		}
		
		return parameters;
	}

	
	private EPADPluginParameter parameter2EPADPluginParameter(ProjectToPluginParameter parameter) throws Exception
	{
		
		return new EPADPluginParameter(String.valueOf(parameter.getId()),String.valueOf(parameter.getProjectId()),parameter.getTemplateCode(),parameter.getName(),parameter.getDefaultValue());
	}
}
