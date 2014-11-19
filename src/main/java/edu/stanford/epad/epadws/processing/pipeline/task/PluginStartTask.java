package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import edu.stanford.epad.common.util.EPADLogger;

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
        HttpClient client = new HttpClient(); // TODO Get rid of localhost
        String url = "http://localhost:8080/epad/plugin/" + pluginName + "/?aimFile=" + annotationID 
        		+ "&frameNumber=" + frameNumber + "&projectID=" + projectID;
        log.info("Triggering ePAD plugin at " + url);
        GetMethod method = new GetMethod(url);
        method.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);
        try {
            int statusCode = client.executeMethod(method);
            log.info("Status code returned from plugin " + statusCode);
        } catch (HttpException e) {
            log.warning("HTTP error calling plugin ", e);
        } catch (IOException e) {
            log.warning("IO exception calling plugin ", e);
        } finally {
            method.releaseConnection();
        }

	}
}
