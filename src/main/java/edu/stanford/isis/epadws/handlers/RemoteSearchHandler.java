package edu.stanford.isis.epadws.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * Handles remote searches to the DicomInterface code. This makes calls to
 * 
 * @author amsnyder
 * 
 * @deprecated
 */
@Deprecated
public class RemoteSearchHandler extends AbstractHandler
{
	@Override
	public void handle(String s, Request request, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException, ServletException
	{
		// To change body of implemented methods use File | Settings | File Templates.
	}
}
