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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADTemplateContainerList;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.SessionService;

@RestController
@RequestMapping("/templates")
public class TemplateController {
	private static final EPADLogger log = EPADLogger.getInstance();
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public EPADTemplateContainerList getTemplates( 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADTemplateContainerList templates = epadOperations.getTemplateDescriptions(username, sessionID);
		return templates;
	}
 
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void createSystemTemplates( 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
	    String requestContentType = request.getContentType();
		Map<String, Object> paramData = null;
		int numberOfFiles = 0;
		File uploadedFile = null;
		try {
			if (requestContentType != null && requestContentType.startsWith("multipart/form-data"))
			{
				PrintWriter responseStream = response.getWriter();
				String uploadDir = EPADConfig.getEPADWebServerFileUploadDir() + "temp" + Long.toString(System.currentTimeMillis());
				paramData = HandlerUtil.parsePostedData(uploadDir, request, responseStream);
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
		}
		finally {
			if (uploadedFile != null)
			{
				if (uploadedFile.getParentFile().exists())
				{
					log.info("Deleting upload directory " + uploadedFile.getParentFile().getAbsolutePath());
					EPADFileUtils.deleteDirectoryAndContents(uploadedFile.getParentFile());
				}
			}
		}
		return;
	}
	 
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public void createSystemTemplate( 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		epadOperations.createSystemTemplate(username, uploadedFile, sessionID);
	}

}
