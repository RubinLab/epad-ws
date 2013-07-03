package edu.stanford.isis.epadws.resources.server;

import org.restlet.data.Status;
import org.restlet.resource.Post;

import edu.stanford.isis.epad.plugin.server.EPadPlugin;
import edu.stanford.isis.epad.plugin.server.impl.EPadPluginImpl;
import edu.stanford.isis.epad.plugin.server.impl.EPadProxyConfigImpl;
import edu.stanford.isis.epadws.common.ProxyVersion;
import edu.stanford.isis.epadws.db.mysql.MySqlInstance;
import edu.stanford.isis.epadws.resources.EPadWebServiceResource;
import edu.stanford.isis.epadws.server.ProxyConfig;
import edu.stanford.isis.epadws.server.managers.pipeline.PipelineFactory;

/**
 * Resource representing an EPad Web Server.
 * <p>
 * To test:
 * 
 * <pre>
 * curl -v -X POST "http://host:port/server/shutdown"
 * curl -v -X POST "http://host:port/server/status"
 * </pre>
 * 
 * @author martin
 */
public class EPadWebServiceServerResource extends BaseServerResource implements EPadWebServiceResource
{
	private static final String SERVER_SHUTDOWN_MESSAGE = "Server shut down!";
	private static final String MALFORMED_REQUEST_MESSAGE = "Malformed request!";
	private static final String UNKNOWN_OPERATION_MESSAGE = "Bad request - unknown operation: ";
	private static final String STATUS_PAGE_BUILD_ERROR_MESSAGE = "Error building status page: ";

	private static final long startTime = System.currentTimeMillis();

	public EPadWebServiceServerResource()
	{
		setNegotiated(false); // Disable content negotiation
	}

	@Override
	protected void doCatch(Throwable throwable)
	{
		log.debug("An exception was thrown in the ePAD web serviceresource.\n");
	}

	@Override
	@Post("text")
	public String operation()
	{
		if (!getRequestAttributes().containsKey(TEMPLATE_OPERATION_NAME)) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return MALFORMED_REQUEST_MESSAGE;
		} else {
			String operationName = getAttribute(TEMPLATE_OPERATION_NAME);
			if (ServerOperation.STATUS.hasOperationName(operationName)) {
				return generateStatusMessage();
			} else if (ServerOperation.SHUTDOWN.hasOperationName(operationName)) {
				// TODO Shut it down!
				setStatus(Status.SUCCESS_OK);
				return SERVER_SHUTDOWN_MESSAGE;
			} else {
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return UNKNOWN_OPERATION_MESSAGE + operationName;
			}
		}
	}

	private String generateStatusMessage()
	{
		StringBuilder out = new StringBuilder();
		log.info("Status request received");

		try {
			setResponseHeader("Access-Control-Allow-Origin", "*");

			ProxyConfig proxyConfig = ProxyConfig.getInstance();
			EPadProxyConfigImpl ePadProxyConfig = new EPadProxyConfigImpl();
			EPadPlugin ePadPlugin = new EPadPluginImpl();
			long upTime = System.currentTimeMillis() - startTime;
			long upTimeSec = upTime / 1000;

			out.append("--------------  DicomProxy status  --------------\n\n");
			out.append("DICOM Proxy uptime: " + upTimeSec + " sec\n\n");
			out.append("Version: " + ProxyVersion.getBuildDate() + "\n\n");
			out.append("DicomProxy listening on: " + proxyConfig.getParam("ListenIP") + ":"
					+ proxyConfig.getParam("ListenPort") + "\n\n");
			out.append("Plugin Version - interface:      " + EPadPlugin.PLUGIN_INTERFACE_VERSION + "\n");
			out.append("Plugin Version - implementation: " + ePadPlugin.getPluginImplVersion() + "\n\n");
			MySqlInstance instance = MySqlInstance.getInstance();
			out.append("DB Startup Time: " + instance.getStartupTime() + " ms\n\n");
			out.append("epad.war serverProxy=" + ePadProxyConfig.getProxyConfigParam("serverProxy") + "\n\n");
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