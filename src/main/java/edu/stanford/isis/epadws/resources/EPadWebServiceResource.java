package edu.stanford.isis.epadws.resources;

import org.restlet.resource.Post;

/**
 * Resource representing an EPad Web Server.
 * <p>
 * Reached via <code>/server/{operation}</code> where <code>operation</code> is <code>shutdown</code> or
 * <code>status</code>.
 * <p>
 * 
 * @author martin
 */
public interface EPadWebServiceResource
{
	public final String TEMPLATE_OPERATION_NAME = "operation";

	@Post
	public String operation();

	enum ServerOperation {
		SHUTDOWN("shutdown"), STATUS("status");

		private final String operationName;

		ServerOperation(String operationName)
		{
			this.operationName = operationName;
		}

		public String getOperationName()
		{
			return operationName;
		}

		public boolean hasOperationName(String otherName)
		{
			return (otherName == null) ? false : operationName.equalsIgnoreCase(otherName);
		}
	}
}