package edu.stanford.isis.epadws.resources;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import edu.stanford.isis.epad.common.plugins.EPadPlugin;
import edu.stanford.isis.epad.common.plugins.impl.EPadPluginImpl;
import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epadws.EPadWebServerVersion;
import edu.stanford.isis.epadws.persistence.Database;
import edu.stanford.isis.epadws.processing.pipeline.PipelineFactory;

/**
 * Resource representing an ePAD Web Server.
 * <p>
 * To test:
 * 
 * <pre>
 * curl -v -X POST "http://host:port/server/shutdown"
 * curl -v -X GET "http://host:port/server/status"
 * </pre>
 * 
 * @author martin
 */
public class EPadWebServiceServerResource extends BaseServerResource implements EPadWebServiceResource
{
	private static final String SERVER_SHUTDOWN_MESSAGE = "Server shut down!";
	private static final String UNKNOWN_GET_OPERATION_MESSAGE = "Bad request - unknown GET server operation: ";
	private static final String UNKNOWN_POST_OPERATION_MESSAGE = "Bad request - unknown POST server operation: ";
	private static final String STATUS_PAGE_BUILD_ERROR_MESSAGE = "Error building status page: ";

	private static final long startTime = System.currentTimeMillis();

	public EPadWebServiceServerResource()
	{
		setNegotiated(false); // Disable content negotiation
	}

	@Override
	protected void doCatch(Throwable throwable)
	{
		log.debug("An exception was thrown in the ePAD web service resource.");
	}

	@Override
	@Get("text")
	public String status()
	{
		String operationName = getAttribute(TEMPLATE_OPERATION_NAME);
		if (ServerOperation.STATUS.hasOperationName(operationName)) {
			return generateStatusMessage();
		} else {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return UNKNOWN_GET_OPERATION_MESSAGE + operationName;
		}
	}

	@Override
	@Post("text")
	public String shutdown()
	{
		String operationName = getAttribute(TEMPLATE_OPERATION_NAME);
		if (ServerOperation.SHUTDOWN.hasOperationName(operationName)) {
			// TODO Shut it down!
			setStatus(Status.SUCCESS_OK);
			return SERVER_SHUTDOWN_MESSAGE;
		} else {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return UNKNOWN_POST_OPERATION_MESSAGE + operationName;
		}
	}

	private String generateStatusMessage()
	{
		StringBuilder out = new StringBuilder();
		log.info("Status request received");

		try {
			setResponseHeader("Access-Control-Allow-Origin", "*");

			EPADConfig proxyConfig = EPADConfig.getInstance();
			EPadPlugin ePadPlugin = new EPadPluginImpl();
			long upTime = System.currentTimeMillis() - startTime;
			long upTimeSec = upTime / 1000;

			out.append("--------------  ePAD Web Service status  --------------\n\n");
			out.append("ePAD Web Service uptime: " + upTimeSec + " sec\n\n");
			out.append("Version: " + EPadWebServerVersion.getBuildDate() + "\n\n");
			out.append("ePAD Web Service listening on: " + proxyConfig.getStringPropertyValue("ListenIP") + ":"
					+ proxyConfig.getIntegerPropertyValue("ListenPort") + "\n\n");
			out.append("Plugin Version - interface:      " + EPadPlugin.PLUGIN_INTERFACE_VERSION + "\n");
			out.append("Plugin Version - implementation: " + ePadPlugin.getPluginImplVersion() + "\n\n");
			Database instance = Database.getInstance();
			out.append("DB Startup Time: " + instance.getStartupTime() + " ms\n\n");
			out.append("pipelineActivity : " + getPipelineActivityLevel() + "\n");
			setStatus(Status.SUCCESS_OK);
		} catch (Exception e) {
			log.warning(STATUS_PAGE_BUILD_ERROR_MESSAGE, e);
			out.append(STATUS_PAGE_BUILD_ERROR_MESSAGE + e.getMessage() + "\n");
			setStatus(Status.SERVER_ERROR_INTERNAL);
		}
		return out.toString();
	}

	private String getPipelineActivityLevel()
	{
		PipelineFactory pipelineFactory = PipelineFactory.getInstance();
		StringBuilder sb = new StringBuilder();
		int activityLevel = pipelineFactory.getActivityLevel();
		int errCount = pipelineFactory.getErrorFileCount();
		if (activityLevel == 0) {
			sb.append("idle.");
		} else {
			sb.append("active-" + activityLevel);
			if (errCount > 0) {
				sb.append(" errors-" + errCount);
			}
		}
		return sb.toString();
	}
}