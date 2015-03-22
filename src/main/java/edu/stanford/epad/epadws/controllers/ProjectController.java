package edu.stanford.epad.epadws.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.epadws.controllers.exceptions.NotFoundException;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilterBuilder;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.SessionService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
	private static final EPADLogger log = EPADLogger.getInstance();
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public EPADProjectList getEPADProjects(@RequestParam(value="username") String username, 
											@RequestParam(value="annotationCount", required = false) boolean annotationCount,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		log.info("Getting project descriptions");
		EPADProjectList projectList = epadOperations.getProjectDescriptions(username, sessionID, searchFilter, annotationCount); 
		//PrintWriter responseStream = response.getWriter();
		//responseStream.append(projectList.toJSON());
		//response.setStatus(HttpServletResponse.SC_OK);
		log.info("Number of projects:" + projectList.ResultSet.totalRecords);
		return projectList;
	}
	 
	@RequestMapping(value = "/{projectID}", method = RequestMethod.GET)
	public EPADProject getEPADProject(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADProject project = epadOperations.getProjectDescription(projectReference, username, sessionID);
		if (project == null)
			throw new NotFoundException("Project " + projectID + " not found");
		return project;
	}

}
