package edu.stanford.isis.epadws.resources.server;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

/**
 * Server resource to dump invocation parameters. Can be used for testing.
 */
public class DebugServerResource extends BaseServerResource
{
	@Override
	public Representation handle()
	{
		String entity = "Method       : " + getRequest().getMethod() + "\nResource URI : " + getRequest().getResourceRef()
				+ "\nIP address   : " + getRequest().getClientInfo().getAddress() + "\nAgent name   : "
				+ getRequest().getClientInfo().getAgentName() + "\nAgent version: "
				+ getRequest().getClientInfo().getAgentVersion() + "\n";
		log.info("Request: " + getRequest());

		return new StringRepresentation(entity);
	}
}