package edu.stanford.epad.epadws.controllers;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADTemplateList;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.SessionService;

@RestController
@RequestMapping("/templates")
public class TemplateController {
	private static final EPADLogger log = EPADLogger.getInstance();
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public EPADTemplateList getTemplates(@RequestParam(value="username") String username, 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADTemplateList templates = epadOperations.getTemplateDescriptions(username, sessionID);
		return templates;
	}
 
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void createSystemTemplates(@RequestParam(value="username") String username, 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
	    String requestContentType = request.getContentType();
		Map<String, Object> paramData = null;
		int numberOfFiles = 0;
		File uploadedFile = null;
		if (requestContentType != null && requestContentType.startsWith("multipart/form-data"))
		{
			PrintWriter responseStream = response.getWriter();
			paramData = HandlerUtil.parsePostedData(request, responseStream);
			for (String param: paramData.keySet())
			{
				if (paramData.get(param) instanceof File)
				{
					if (uploadedFile == null)
						uploadedFile = (File) paramData.get(param);
					numberOfFiles++;
				}
			}
		}
		if (requestContentType == null || !requestContentType.startsWith("multipart/form-data"))
			throw new Exception("Invalid Content Type, should be multipart/form-data");
		if (numberOfFiles == 0)
			throw new Exception("No files found in post");
		
		int i = 0;
		for (String param: paramData.keySet())
		{
			if (paramData.get(param) instanceof File)
			{
				epadOperations.createSystemTemplate(username, (File)paramData.get(param), sessionID);
				i++;
			}
		}
		return;
	}
	 
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public void createSystemTemplate(@RequestParam(value="username") String username, 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		epadOperations.createSystemTemplate(username, uploadedFile, sessionID);
	}

}
