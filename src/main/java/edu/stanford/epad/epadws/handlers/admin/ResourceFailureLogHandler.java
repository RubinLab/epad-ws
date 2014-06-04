package edu.stanford.epad.epadws.handlers.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADLogger;

/**
 * Log any failures on resource route.
 * 
 * 
 * @author martin
 */
public class ResourceFailureLogHandler extends AbstractHandler
{
	private final EPADLogger log = EPADLogger.getInstance();

	@Override
	public void handle(String base, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		request.setHandled(false);
		log.warning("Failed resource request " + httpRequest.getRequestURI());
	}
}
