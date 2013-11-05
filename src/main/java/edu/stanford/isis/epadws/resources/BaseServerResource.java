package edu.stanford.isis.epadws.resources;

import org.restlet.engine.header.Header;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;

/**
 * Base resource for all ePAD web service REST resources.
 * 
 * @author martin
 */
public class BaseServerResource extends ServerResource
{
	protected static final EPADLogger log = EPADLogger.getInstance();
	protected static final EPADConfig config = EPADConfig.getInstance();

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
