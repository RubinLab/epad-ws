package edu.stanford.epad.epadws.controllers;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.core.AIMReference;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.SessionService;

@RestController
@RequestMapping("/aims")
public class AimController {
	private static final EPADLogger log = EPADLogger.getInstance();
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void getAims( 
			@RequestParam(value="deletedAims", required = false) boolean deletedAims, 
			@RequestParam(value="start", defaultValue="0") int start,
			@RequestParam(value="count", defaultValue="5000") int count,
			@RequestParam(value="format", defaultValue="xml") String format,
			@RequestParam(value="projectID", required = false) String projectID, 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		long starttime = System.currentTimeMillis();
		AIMSearchType aimSearchType = AIMUtil.getAIMSearchType(request);
		String searchValue = aimSearchType != null ? request.getParameter(aimSearchType.getName()) : null;
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADAIMList aims = null;
		if (!deletedAims)
			aims = epadOperations.getAIMDescriptions(projectID, aimSearchType, searchValue, username, sessionID, start, count);
		
		long dbtime = System.currentTimeMillis();
		log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equals(format))
		{
			if (AIMSearchType.AIM_QUERY.equals(aimSearchType) || AIMSearchType.JSON_QUERY.equals(aimSearchType))
			{
				if (!deletedAims)
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, aimSearchType, searchValue, username, sessionID);
				else
					aims = AIMUtil.queryDeletedAIMImageAnnotationSummaries(aimSearchType, searchValue, username);
			}
			else
			{
				if (!deletedAims)
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
				else
					aims = AIMUtil.queryDeletedAIMImageAnnotationSummaries(aimSearchType, searchValue, username);
			}
			responseStream.append(aims.toJSON());
		}
		else if ("json".equals(format))
		{
			if (AIMSearchType.JSON_QUERY.equals(aimSearchType))
			{
				AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, aimSearchType, searchValue, username, sessionID, true);					
			}
			else
			{
				if (!deletedAims)
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				else
					AIMUtil.queryDeletedAIMImageJsonAnnotation(responseStream, aimSearchType, searchValue, username, sessionID);
			}
		}
		else
		{
			if (AIMSearchType.AIM_QUERY.equals(aimSearchType))
			{
				if (!deletedAims)
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, aimSearchType, searchValue, username, sessionID, false);					
				else
					AIMUtil.queryDeletedAIMImageAnnotations(responseStream, aimSearchType, searchValue, username, sessionID);
			}
			else
			{
				if (!deletedAims)
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
				else
					AIMUtil.queryDeletedAIMImageAnnotations(responseStream, aimSearchType, searchValue, username, sessionID);
			}
		}
	}
 
	@RequestMapping(value = "/{aimID}", method = RequestMethod.GET)
	public void getAims( 
						@RequestParam(value="version", required = false) String version, 
						@RequestParam(value="format", defaultValue="xml") String format,
						@RequestParam(value="projectID", required = false) String projectID, 
						@PathVariable String aimID,
						HttpServletRequest request, 
				        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADAIM aim = epadOperations.getAIMDescription(aimID, username, sessionID);
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equals(format))
		{	
			if ("all".equalsIgnoreCase(version))
			{
				EPADAIMList aims = AIMUtil.getAllVersionSummaries(aim);
				responseStream.append(aims.toJSON());
			}
			else if ("previous".equalsIgnoreCase(version))
			{
				EPADAIMList aims = AIMUtil.getPreviousVersionSummaries(aim);
				responseStream.append(aims.toJSON());
			}
			else if ("next".equalsIgnoreCase(version))
			{
				EPADAIMList aims = AIMUtil.getNextVersionSummaries(aim);
				responseStream.append(aims.toJSON());
			}
			else
				responseStream.append(aim.toJSON());
		}
		else if ("data".equals(format))
		{
			String templateName = request.getParameter("templateName");
			if (templateName == null || templateName.trim().length() == 0)
				throw new Exception("Invalid template name");
			String json = AIMUtil.readPlugInData(aim, templateName, sessionID);
			responseStream.append(json);
		}
		else
		{
			if ("all".equalsIgnoreCase(version))
			{
				AIMUtil.returnAllVersions(responseStream, aim);
			}
			else if ("previous".equalsIgnoreCase(version))
			{
				AIMUtil.returnPreviousVersions(responseStream, aim);
			}
			else
				AIMUtil.queryAIMImageAnnotations(responseStream, null, AIMSearchType.ANNOTATION_UID,
						aim.aimID, username);					
		}
	}

}
