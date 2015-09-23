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
package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.TaskStatus;
import edu.stanford.epad.epadws.models.Plugin;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.PluginOperations;

/**
 * Start Plugin and pass it aim id / frame num.
 * 
 * @author dev
 * 
 */
public class PluginStartTask implements Runnable
{
	private static EPADLogger log = EPADLogger.getInstance();

	private final String jsessionID;
	private final String pluginName;
	private final String annotationID;
	private final int frameNumber;
	private final String projectID;

	public PluginStartTask(String jsessionID, String pluginName, String annotationID, int frameNumber, String projectID)
	{
		this.jsessionID = jsessionID;
		this.pluginName = pluginName;
		this.annotationID = annotationID;
		this.frameNumber = frameNumber;
		this.projectID = projectID;
	}

	@Override
	public void run()
	{
		String username = EPADSessionOperations.getSessionUser(jsessionID);
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		projectOperations.createEventLog(username, projectID, null, null, null, null, annotationID, "Start PlugIn", pluginName);
        HttpClient client = new HttpClient(); // TODO Get rid of localhost
        String url = EPADConfig.getParamValue("serverProxy", "http://localhost:8080") 
        		+ EPADConfig.getParamValue("webserviceBase", "/epad") + "/plugin/" + pluginName + "/?aimFile=" + annotationID 
        		+ "&frameNumber=" + frameNumber + "&projectID=" + projectID;
        log.info("Triggering ePAD plugin at " + url);
		projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_PLUGIN, pluginName + ":" + annotationID, "Started Plugin", new Date(), null);
        GetMethod method = new GetMethod(url);
        method.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);
        try {
            int statusCode = client.executeMethod(method);
            log.info("Status code returned from plugin " + statusCode);
        } catch (Exception e) {
            log.warning("Error calling plugin " + pluginName, e);
    		try {
        		PluginOperations pluginOperations = PluginOperations.getInstance();
				Plugin plugin = pluginOperations.getPluginByName(pluginName);
				plugin.setStatus("Error calling plugin :" + e.getMessage());
				plugin.save();
			} catch (Exception e1) { }
    		
      } finally {
            method.releaseConnection();
        }

	}
}
