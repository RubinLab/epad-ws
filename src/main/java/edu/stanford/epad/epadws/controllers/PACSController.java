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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.DicomTagList;
import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.dtos.RemotePACEntity;
import edu.stanford.epad.dtos.RemotePACEntityList;
import edu.stanford.epad.dtos.RemotePACList;
import edu.stanford.epad.dtos.RemotePACQueryConfig;
import edu.stanford.epad.dtos.RemotePACQueryConfigList;
import edu.stanford.epad.epadws.controllers.exceptions.NotFoundException;
import edu.stanford.epad.epadws.models.RemotePACQuery;
import edu.stanford.epad.epadws.service.RemotePACService;
import edu.stanford.epad.epadws.service.SessionService;
import edu.stanford.epad.epadws.service.TCIAService;

@RestController
@RequestMapping("/pacs")
public class PACSController {
	private static final EPADLogger log = EPADLogger.getInstance();
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public RemotePACList getEPADRemotePACSList( 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		List<RemotePAC> pacs = RemotePACService.getInstance().getRemotePACs();
		RemotePACList pacList = new RemotePACList();
		for (RemotePAC pac: pacs)
			pacList.addRemotePAC(pac);
		return pacList;
	}
	
	@RequestMapping(value = "/{pacID:.+}", method = RequestMethod.GET)
	public RemotePAC getEPADRemotePACS( 
											@PathVariable String pacID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac == null)
			throw new NotFoundException("Remote PAC " + pacID + " not found");
		return pac;
	}
	 
	@RequestMapping(value = "/{pacID}/entities/", method = RequestMethod.GET)
	public RemotePACEntityList getEPADRemotePACEntities( 
									@RequestParam(value="patientNameFilter", required=false) String patientNameFilter,
									@RequestParam(value="patientIDFilter", required=false) String patientIDFilter,
									@RequestParam(value="studyIDFilter", required=false) String studyIDFilter,
									@RequestParam(value="studyDateFilter", required=false) String studyDateFilter,
									@RequestParam(value="modality", required=false) String modality,
									@RequestParam(value="tagGroup", required=false) String[] tagGroup,
									@RequestParam(value="tagElement", required=false) String[] tagElement,
									@RequestParam(value="tagValue", required=false) String[] tagValue,
									@RequestParam(value="tagType", required=false) String[] tagType,
									@PathVariable String pacID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		log.info("Get Remote Records, pacID:" + pacID);
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		boolean studiesOnly = !"true".equalsIgnoreCase(request.getParameter("series"));
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, patientNameFilter, patientIDFilter, 
					studyIDFilter, studyDateFilter, modality,
					tagGroup, tagElement, tagValue, tagType, false, studiesOnly);
			RemotePACEntityList entityList = new RemotePACEntityList();
			for (RemotePACEntity entity: entities)
				entityList.addRemotePACEntity(entity);
			return entityList;
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}
	 
	@RequestMapping(value = "/{pacID}/subjects/", method = RequestMethod.GET)
	public RemotePACEntityList getEPADRemotePACSubjects( 
									@RequestParam(value="patientNameFilter", defaultValue="") String patientNameFilter,
									@RequestParam(value="patientIDFilter", defaultValue="") String patientIDFilter,
									@PathVariable String pacID,
									@RequestParam(value="modality", required=false) String modality,
									@RequestParam(value="tagGroup", required=false) String[] tagGroup,
									@RequestParam(value="tagElement", required=false) String[] tagElement,
									@RequestParam(value="tagValue", required=false) String[] tagValue,
									@RequestParam(value="tagType", required=false) String[] tagType,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		boolean studiesOnly = !"true".equalsIgnoreCase(request.getParameter("series"));
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, patientNameFilter, patientIDFilter, "", "", modality, tagGroup, tagElement, tagValue, tagType, true, true);
			RemotePACEntityList entityList = new RemotePACEntityList();
			for (RemotePACEntity entity: entities)
				entityList.addRemotePACEntity(entity);
			return entityList;
		}
		else if (pacID.startsWith(TCIAService.TCIA_PREFIX))
		{
			List<RemotePACEntity> entities = TCIAService.getInstance().getPatientsForCollection(pacID.substring(5));
			RemotePACEntityList entityList = new RemotePACEntityList();
			for (RemotePACEntity entity: entities)
				entityList.addRemotePACEntity(entity);
			return entityList;
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}
	 
	@RequestMapping(value = "/{pacID}/subjects/{subjectID}/studies/", method = RequestMethod.GET)
	public RemotePACEntityList getEPADRemotePACStudies( 
									@RequestParam(value="studyDateFilter", defaultValue="") String studyDateFilter,
									@RequestParam(value="modality", required=false) String modality,
									@PathVariable String pacID,
									@PathVariable String subjectID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		boolean studiesOnly = !"true".equalsIgnoreCase(request.getParameter("series"));
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, "", subjectID, "", studyDateFilter, modality, false, true);
			RemotePACEntityList entityList = new RemotePACEntityList();
			for (RemotePACEntity entity: entities)
				entityList.addRemotePACEntity(entity);
			return entityList;
		}
		else if (pacID.startsWith(TCIAService.TCIA_PREFIX))
		{
			List<RemotePACEntity> entities = TCIAService.getInstance().getStudiesForPatient(pacID.substring(5), subjectID);
			RemotePACEntityList entityList = new RemotePACEntityList();
			for (RemotePACEntity entity: entities)
				entityList.addRemotePACEntity(entity);
			return entityList;
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}
	 
	@RequestMapping(value = "/{pacID}/subjects/{subjectID}/studies/{studyUID}/series/", method = RequestMethod.GET)
	public RemotePACEntityList getEPADRemotePACSeries( 
									@PathVariable String pacID,
									@PathVariable String subjectID,
									@PathVariable String studyUID,
									@RequestParam(value="modality", required=false) String modality,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		boolean studiesOnly = !"true".equalsIgnoreCase(request.getParameter("series"));
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, "", subjectID, studyUID, "", modality, false, false);
			RemotePACEntityList entityList = new RemotePACEntityList();
			for (RemotePACEntity entity: entities)
				entityList.addRemotePACEntity(entity);
			return entityList;
		}
		else if (pacID.startsWith(TCIAService.TCIA_PREFIX))
		{
			if (studyUID.indexOf(":") != -1)
				studyUID = studyUID.substring(studyUID.lastIndexOf(":")+1);
			List<RemotePACEntity> entities = TCIAService.getInstance().getSeriesForStudy(pacID.substring(5), subjectID, studyUID);
			RemotePACEntityList entityList = new RemotePACEntityList();
			for (RemotePACEntity entity: entities)
				entityList.addRemotePACEntity(entity);
			return entityList;
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}
	 
	@RequestMapping(value = "/{pacID}/entities/{entityID:.+}", method = RequestMethod.GET)
	public void transferEPADRemotePACEntity( 
									@RequestParam(value="projectID", required = true) String projectID,
									@PathVariable String pacID,
									@PathVariable String entityID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		log.info("Transfer Remote Data, pacID:" + pacID);
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		boolean studiesOnly = !"true".equalsIgnoreCase(request.getParameter("series"));
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			RemotePACService.getInstance().retrieveRemoteData(pac, entityID, projectID, username, sessionID);
		}
		else if (pacID.startsWith(TCIAService.TCIA_PREFIX))
		{
			if (entityID.indexOf("SUBJECT:") != -1 || entityID.indexOf("STUDY:") != -1)
				throw new Exception("Patient or Study can not be downloaded. Please select a Series");
			if (entityID.indexOf(":") != -1)
				entityID = entityID.substring(entityID.lastIndexOf(":")+1);
			TCIAService.getInstance().downloadSeriesFromTCIA(username, entityID, projectID);
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}
	 
	@RequestMapping(value = "/{pacID}/autoqueries/", method = RequestMethod.GET)
	public RemotePACQueryConfigList getEPADRemotePACQueries( 
									@PathVariable String pacID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		RemotePACService rps = RemotePACService.getInstance();
		List<RemotePACQuery> remoteQueries = rps.getRemotePACQueries(pacID);
		RemotePACQueryConfigList queryList = new RemotePACQueryConfigList();
		for (RemotePACQuery query: remoteQueries)
		{
			queryList.addRemotePACQueryConfig(rps.getConfig(query));
		}
		return queryList;
	}
	 
	@RequestMapping(value = "/{pacID}/autoqueries/{subjectID:.+}", method = RequestMethod.GET)
	public RemotePACQueryConfig getEPADRemotePACQuery( 
									@PathVariable String pacID,
									@PathVariable String subjectID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		RemotePACQuery remoteQuery = RemotePACService.getInstance().getRemotePACQuery(pacID, subjectID);
		if (remoteQuery != null)
		{
			return RemotePACService.getInstance().getConfig(remoteQuery);
		}
		else
			throw new NotFoundException("Query for " + subjectID + " not found");
	}
	 
	@RequestMapping(value = "/dicomtags/", method = RequestMethod.GET)
	public DicomTagList getDicomTags( 
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		return RemotePACService.getDicomTags();
	}
	 
	@RequestMapping(value = "/{pacID:.+}", method = RequestMethod.PUT)
	public void createRemotePAC( 
									@PathVariable String pacID,
									@RequestParam(value="aeTitle", required=true) String aeTitle,
									@RequestParam(value="hostname", required=true) String hostname,
									@RequestParam(value="port", required=true) int port,
									@RequestParam(value="deviceType", defaultValue="WSD") String primaryDeviceType,
									@RequestParam(value="queryModel") String queryModel,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac == null)
		{
			pac = new RemotePAC(pacID, aeTitle, hostname, port, queryModel, primaryDeviceType);
			RemotePACService.getInstance().addRemotePAC(username, pac);
		}
		else
		{
			pac = new RemotePAC(pacID, aeTitle, hostname, port, queryModel, primaryDeviceType);
			RemotePACService.getInstance().modifyRemotePAC(username, pac);
		}
	}	
	 
	@RequestMapping(value = "/{pacID}/autoqueries/{subjectID:.+}", method = RequestMethod.PUT)
	public void createRemotePACAutoQuery( 
									@PathVariable String pacID,
									@PathVariable String subjectID,
									@RequestParam(value="projectID", required=true) String projectID,
									@RequestParam(value="subjectName") String subjectName,
									@RequestParam(value="patientName") String patientName,
									@RequestParam(value="modality") String modality,
									@RequestParam(value="studyDate") String studyDate,
									@RequestParam(value="period") String period,
									@RequestParam(value="enable") String enable,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		if (subjectName == null)
			subjectName = patientName;
		if (enable != null)
		{
			RemotePACQuery query = RemotePACService.getInstance().getRemotePACQuery(pacID, subjectID);
			if ("true".equalsIgnoreCase(enable) && query != null)
			{
				RemotePACService.getInstance().enableRemotePACQuery(username, pacID, subjectID);
			}
			else if ("false".equalsIgnoreCase(enable))
			{	
				if (query == null)
					throw new Exception("Remote PAC and Patient not configured for periodic query");
				RemotePACService.getInstance().disableRemotePACQuery(username, pacID, subjectID);
			}
		}
		else
		{
			RemotePACService.getInstance().createRemotePACQuery(username, pacID, subjectID, subjectName, modality, studyDate, period, projectID);
		}
	}	
	 
	@RequestMapping(value = "/{pacID:.+}", method = RequestMethod.DELETE)
	public void deleteRemotePAC( 
									@PathVariable String pacID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			RemotePACService.getInstance().removeRemotePAC(username, pac);
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}
	 
	@RequestMapping(value = "/{pacID}/autoqueries/{subjectID:.+}", method = RequestMethod.DELETE)
	public void deleteRemotePACQuery( 
									@PathVariable String pacID,
									@PathVariable String subjectID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			RemotePACQuery remoteQuery = RemotePACService.getInstance().getRemotePACQuery(pacID, subjectID);
			if (remoteQuery != null)
			{
				RemotePACService.getInstance().removeRemotePACQuery(username, pacID, subjectID);
			}
			else
				throw new NotFoundException("Query for " + subjectID + " not found");
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}

}
