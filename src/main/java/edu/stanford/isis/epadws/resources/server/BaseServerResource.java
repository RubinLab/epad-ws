package edu.stanford.isis.epadws.resources.server;

import org.restlet.engine.header.Header;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import edu.stanford.isis.epadws.server.ProxyConfig;
import edu.stanford.isis.epadws.server.ProxyLogger;

public class BaseServerResource extends ServerResource
{
	protected static final ProxyLogger log = ProxyLogger.getInstance();
	protected static final ProxyConfig config = ProxyConfig.getInstance();

	protected void setResponseHeader(String header, String value)
	{
		@SuppressWarnings("unchecked")
		Series<Header> responseHeaders = (Series<Header>)getResponse().getAttributes().get("org.restlet.http.headers");
		if (responseHeaders == null) {
			responseHeaders = new Series<Header>(Header.class);
			getResponse().getAttributes().put("org.restlet.http.headers", responseHeaders);
		}
		responseHeaders.add(new Header(header, value));
	}
}
