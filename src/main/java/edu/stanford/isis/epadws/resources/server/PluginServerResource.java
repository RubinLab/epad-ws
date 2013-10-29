package edu.stanford.isis.epadws.resources.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restlet.data.Status;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.resource.Get;

import edu.stanford.isis.epad.common.plugins.PluginHandlerMap;
import edu.stanford.isis.epad.common.plugins.PluginServletHandler;

public class PluginServerResource extends BaseServerResource
{
	private static final String PLUGIN_NOT_FOUND_MESSAGE = "Could not find plugin: ";
	private static final String MISSING_PLUGIN_NAME_MESSAGE = "Missing plugin name";

	private static PluginHandlerMap pluginHandlerMap = PluginHandlerMap.getInstance();

	public PluginServerResource()
	{
		setNegotiated(false); // Disable content negotiation
	}

	@Override
	protected void doCatch(Throwable throwable)
	{
		log.warning("An exception was thrown in the plugin resource.", throwable);
	}

	@Get("text")
	public String pluginGet()
	{
		setResponseHeader("Access-Control-Allow-Origin", "*");

		String pluginName = getAttribute("pluginName");

		if (pluginName != null) {
			PluginServletHandler pluginHandler = pluginHandlerMap.getPluginServletHandler(pluginName);
			HttpServletRequest req = ServletUtils.getRequest(getRequest());
			HttpServletResponse res = ServletUtils.getResponse(getResponse());

			if (pluginHandler != null) {
				pluginHandler.doGet(req, res);
				return "TODO";
			} else {
				log.info(PLUGIN_NOT_FOUND_MESSAGE + pluginName);
				setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				return PLUGIN_NOT_FOUND_MESSAGE + pluginName;
			}
		} else {
			log.info(MISSING_PLUGIN_NAME_MESSAGE);
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return MISSING_PLUGIN_NAME_MESSAGE;
		}
	}

	@Get("post")
	public String pluginPost()
	{
		setResponseHeader("Access-Control-Allow-Origin", "*");

		String pluginName = getAttribute("pluginName");

		if (pluginName != null) {
			PluginServletHandler pluginHandler = pluginHandlerMap.getPluginServletHandler(pluginName);
			HttpServletRequest req = ServletUtils.getRequest(getRequest());
			HttpServletResponse res = ServletUtils.getResponse(getResponse());

			if (pluginHandler != null) {
				pluginHandler.doPost(req, res);
				return "TODO";
			} else {
				log.info(PLUGIN_NOT_FOUND_MESSAGE + pluginName);
				setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				return PLUGIN_NOT_FOUND_MESSAGE + pluginName;
			}
		} else {
			log.info(MISSING_PLUGIN_NAME_MESSAGE);
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return MISSING_PLUGIN_NAME_MESSAGE;
		}
	}
}
