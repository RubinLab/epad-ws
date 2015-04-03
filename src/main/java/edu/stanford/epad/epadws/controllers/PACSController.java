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

@RestController
@RequestMapping("/pacs")
public class PACSController {
	private static final EPADLogger log = EPADLogger.getInstance();
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public RemotePACList getEPADRemotePACS(@RequestParam(value="username") String username, 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		List<RemotePAC> pacs = RemotePACService.getInstance().getRemotePACs();
		RemotePACList pacList = new RemotePACList();
		for (RemotePAC pac: pacs)
			pacList.addRemotePAC(pac);
		return pacList;
	}
	
	@RequestMapping(value = "/{pacID}", method = RequestMethod.GET)
	public RemotePAC getEPADRemotePACS(@RequestParam(value="username") String username, 
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
	public RemotePACEntityList getEPADRemotePACEntities(@RequestParam(value="username") String username, 
									@RequestParam(value="patientNameFilter", defaultValue="") String patientNameFilter,
									@RequestParam(value="patientIDFilter", defaultValue="") String patientIDFilter,
									@RequestParam(value="studyIDFilter", defaultValue="") String studyIDFilter,
									@RequestParam(value="studyDateFilter", defaultValue="") String studyDateFilter,
									@RequestParam(value="tagGroup") String[] tagGroup,
									@RequestParam(value="tagElement") String[] tagElement,
									@RequestParam(value="tagValue") String[] tagValue,
									@PathVariable String pacID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		boolean studiesOnly = !"true".equalsIgnoreCase(request.getParameter("series"));
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, patientNameFilter, patientIDFilter, 
					studyIDFilter, studyDateFilter, 
					tagGroup, tagElement, tagValue, false, studiesOnly);
			RemotePACEntityList entityList = new RemotePACEntityList();
			for (RemotePACEntity entity: entities)
				entityList.addRemotePACEntity(entity);
			return entityList;
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}
	 
	@RequestMapping(value = "/{pacID}/subjects/", method = RequestMethod.GET)
	public RemotePACEntityList getEPADRemotePACSubjects(@RequestParam(value="username") String username, 
									@RequestParam(value="patientNameFilter", defaultValue="") String patientNameFilter,
									@RequestParam(value="patientIDFilter", defaultValue="") String patientIDFilter,
									@PathVariable String pacID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		boolean studiesOnly = !"true".equalsIgnoreCase(request.getParameter("series"));
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, patientNameFilter, patientIDFilter, "", "", true, true);
			RemotePACEntityList entityList = new RemotePACEntityList();
			for (RemotePACEntity entity: entities)
				entityList.addRemotePACEntity(entity);
			return entityList;
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}
	 
	@RequestMapping(value = "/{pacID}/subjects/{subjectID}/studies/", method = RequestMethod.GET)
	public RemotePACEntityList getEPADRemotePACStudies(@RequestParam(value="username") String username, 
									@RequestParam(value="studyDateFilter", defaultValue="") String studyDateFilter,
									@PathVariable String pacID,
									@PathVariable String subjectID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		boolean studiesOnly = !"true".equalsIgnoreCase(request.getParameter("series"));
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, "", subjectID, "", studyDateFilter, false, true);
			RemotePACEntityList entityList = new RemotePACEntityList();
			for (RemotePACEntity entity: entities)
				entityList.addRemotePACEntity(entity);
			return entityList;
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}
	 
	@RequestMapping(value = "/{pacID}/subjects/{subjectID}/studies/{studyUID}", method = RequestMethod.GET)
	public RemotePACEntityList getEPADRemotePACSeries(@RequestParam(value="username") String username, 
									@PathVariable String pacID,
									@PathVariable String subjectID,
									@PathVariable String studyUID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		boolean studiesOnly = !"true".equalsIgnoreCase(request.getParameter("series"));
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, "", subjectID, studyUID, "", false, false);
			RemotePACEntityList entityList = new RemotePACEntityList();
			for (RemotePACEntity entity: entities)
				entityList.addRemotePACEntity(entity);
			return entityList;
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}
	 
	@RequestMapping(value = "/{pacID}/entities/{entityID}", method = RequestMethod.GET)
	public void transferEPADRemotePACEntity(@RequestParam(value="username") String username, 
									@RequestParam(value="projectID", required = true) String projectID,
									@PathVariable String pacID,
									@PathVariable String entityID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		boolean studiesOnly = !"true".equalsIgnoreCase(request.getParameter("series"));
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			RemotePACService.getInstance().retrieveRemoteData(pac, entityID, projectID, username, sessionID);
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}
	 
	@RequestMapping(value = "/{pacID}/autoqueries/", method = RequestMethod.GET)
	public RemotePACQueryConfigList getEPADRemotePACQueries(@RequestParam(value="username") String username, 
									@PathVariable String pacID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			RemotePACService rps = RemotePACService.getInstance();
			List<RemotePACQuery> remoteQueries = rps.getRemotePACQueries(pacID);
			RemotePACQueryConfigList queryList = new RemotePACQueryConfigList();
			for (RemotePACQuery query: remoteQueries)
			{
				queryList.addRemotePACQueryConfig(rps.getConfig(query));
			}
			return queryList;
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}
	 
	@RequestMapping(value = "/{pacID}/autoqueries/{subjectID}", method = RequestMethod.GET)
	public RemotePACQueryConfig getEPADRemotePACQuery(@RequestParam(value="username") String username, 
									@PathVariable String pacID,
									@PathVariable String subjectID,
									HttpServletRequest request, 
							        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
		if (pac != null)
		{
			RemotePACQuery remoteQuery = RemotePACService.getInstance().getRemotePACQuery(pacID, subjectID);
			if (remoteQuery != null)
			{
				return RemotePACService.getInstance().getConfig(remoteQuery);
			}
			else
				throw new NotFoundException("Query for " + subjectID + " not found");
		}
		else
			throw new NotFoundException("Remote PAC " + pacID + " not found");
	}

}
