//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.controllers;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADFile;
import edu.stanford.epad.dtos.EPADFileList;
import edu.stanford.epad.dtos.EPADFrame;
import edu.stanford.epad.dtos.EPADFrameList;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADMessage;
import edu.stanford.epad.dtos.EPADPlugin;
import edu.stanford.epad.dtos.EPADPluginList;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.dtos.EPADTemplateContainer;
import edu.stanford.epad.dtos.EPADTemplateContainerList;
import edu.stanford.epad.dtos.EPADUserList;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.controllers.exceptions.NotFoundException;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.core.AIMReference;
import edu.stanford.epad.epadws.handlers.core.AimsRouteTemplates;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilterBuilder;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.PluginReference;
import edu.stanford.epad.epadws.handlers.core.PluginRouteTemplates;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.ProjectsRouteTemplates;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.handlers.dicom.DownloadUtil;
import edu.stanford.epad.epadws.models.EpadFile;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.DefaultWorkListOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadWorkListOperations;
import edu.stanford.epad.epadws.service.PluginOperations;
import edu.stanford.epad.epadws.service.SessionService;
import edu.stanford.epad.epadws.service.UserProjectService;

@RestController
@RequestMapping("/plugins")
public class PluginController {
	private static final EPADLogger log = EPADLogger.getInstance();
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public EPADPluginList getEPADPlugins(
											@RequestParam(value="format", required = false, defaultValue = "json") String format,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {		
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		PluginOperations pluginOperations = PluginOperations.getInstance();
		log.info("Getting plugin descriptions");
		EPADPluginList plugins = null;
		if (format != null && format.trim().equalsIgnoreCase("summary"))
			plugins = pluginOperations.getPluginSummaries(username, sessionID);
		else
			plugins = pluginOperations.getPluginDescriptions(username, sessionID);
		
		log.info("Number of plugins:" + plugins.ResultSet.totalRecords);
		return plugins;
	}
	 
	@RequestMapping(value = "/{pluginID:.+}", method = RequestMethod.GET)
	public EPADPlugin getEPADPlugin( 
										@PathVariable String pluginID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		
		PluginOperations pluginOperations = PluginOperations.getInstance();
		PluginReference pluginReference = new PluginReference(pluginID);
		
		EPADPlugin plugin = pluginOperations.getPluginDescription(pluginReference.pluginID, username, sessionID);
		
		if (plugin == null) {
			throw new NotFoundException("Plugin " + pluginID + " not found");
		}
		return plugin;
			
	}
	
	@RequestMapping(value = "/{pluginID:.+}", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADPlugin( 
											@PathVariable String pluginID,
											@RequestParam(value="name", required=false) String name,
											@RequestParam(value="description", required=false) String description,
											@RequestParam(value="class", required=false) String javaclass,
											@RequestParam(value="enable", required=false) String enabled,
											@RequestParam(value="modality", required=false) String modality,
											@RequestParam(value="developer", required=false) String developer,
											@RequestParam(value="documentation", required=false) String documentation,
											@RequestParam(value="rate", required=false) String rate,
											@RequestParam(value="processMultipleAims", required=false) String processMultipleAims,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		PluginOperations pluginOperations = PluginOperations.getInstance();
		boolean isProcessMultipleAims = ("true".equalsIgnoreCase(processMultipleAims));
		PluginReference pluginReference = new PluginReference(pluginID);
		
		EPADPlugin plugin = pluginOperations.getPluginDescription(pluginReference.pluginID, username, sessionID);
		int statusCode = 0;
		if (plugin != null) {
			pluginOperations.updatePlugin(username, pluginReference.pluginID, name, description, javaclass, enabled, modality, developer,documentation,rate,sessionID,isProcessMultipleAims);
			statusCode = HttpServletResponse.SC_OK;
		} else {
			pluginOperations.createPlugin(username, pluginReference.pluginID, name, description, javaclass, enabled, modality, developer,documentation,rate, sessionID, isProcessMultipleAims);
			statusCode = HttpServletResponse.SC_OK;
		}
		
		
		log.warning("Create/Modify plugin, status:" + statusCode);
		if (statusCode != HttpServletResponse.SC_OK)
			throw new Exception("Error creating or modifying plugin");
	}
	 	
	
	@RequestMapping(value = "/{pluginID:.+}", method = RequestMethod.DELETE)
	public void deleteEPADPlugin( 
										@PathVariable String pluginID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		PluginOperations pluginOperations = PluginOperations.getInstance();
		PluginReference pluginReference = new PluginReference(pluginID);
		EPADPlugin plugin = pluginOperations.getPluginDescription(pluginReference.pluginID,username, sessionID);
		if (plugin == null) 
			throw new Exception("Plugin not found for id " + pluginReference.pluginID);
		pluginOperations.deletePlugin(username, pluginReference.pluginID);	
		
	}
	

}
