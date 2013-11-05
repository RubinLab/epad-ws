package edu.stanford.isis.epadws.handlers.xnat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.JsonHelper;
import edu.stanford.isis.epad.common.xnat.XNATProjectDescription;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * XNAT-based project retrieval handler. At present, a pretty thin wrapper around an XNAT projects call.
 * <p>
 * <code>
 * curl -b JSESSIONID=<session_id> -X GET "http://[host:port]/projects/"
 * </code>
 * 
 * @author martin
 * @see XNATProjectDescription
 */
public class XNATProjectHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final EPADConfig config = EPADConfig.getInstance();

	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Invalid session token";
	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error invoking XNAT";
	private static final String XNAT_INVOCATION_ERROR_MESSAGE = "Error invoking XNAT";
	private static final String INVALID_METHOD_MESSAGE = "Only GET, POST, PUT, DELETE methods valid for this route";

	@Override
	public void handle(String base, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException
	{
		ServletOutputStream responseStream = null; // TODO Look at use of ServletOutputStream
		int statusCode;

		httpResponse.setContentType("application/json");
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getOutputStream();

			if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
				statusCode = invokeXNATProjectService(base, httpRequest, httpResponse, responseStream);
			} else {
				log.info(INVALID_SESSION_TOKEN_MESSAGE);
				responseStream.print(JsonHelper.createJSONErrorResponse(INVALID_SESSION_TOKEN_MESSAGE));
				statusCode = HttpServletResponse.SC_UNAUTHORIZED;
			}
		} catch (Throwable t) {
			log.warning(INTERNAL_EXCEPTION_MESSAGE, t);
			responseStream.print(JsonHelper.createJSONErrorResponse(INTERNAL_EXCEPTION_MESSAGE, t));
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		httpResponse.setStatus(statusCode);
		responseStream.flush();
	}

	private int invokeXNATProjectService(String base, HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			OutputStream responseStream) throws IOException
	{
		String xnatHost = config.getStringConfigurationParameter("XNATServer");
		int xnatPort = config.getIntegerConfigurationParameter("XNATPort");
		String xnatURL = XNATUtil.buildURLString(xnatHost, xnatPort, XNATUtil.XNAT_PROJECT_BASE, base);
		HttpClient client = new HttpClient();
		String jsessionID = XNATUtil.getJSessionIDFromRequest(httpRequest);
		int statusCode;

		String queryString = httpRequest.getQueryString();

		if (queryString != null) {
			queryString = queryString.trim();
			xnatURL = xnatURL.replaceFirst("/$", "") + "?" + queryString;
		}

		HttpMethodBase xnatMethod = null;
		String method = httpRequest.getMethod();
		if ("GET".equalsIgnoreCase(method)) {
			xnatMethod = new GetMethod(xnatURL);
		} else if ("DELETE".equalsIgnoreCase(method)) {
			xnatMethod = new DeleteMethod(xnatURL);
		} else if ("POST".equalsIgnoreCase(method)) {
			xnatMethod = new PostMethod(xnatURL);
		} else if ("PUT".equalsIgnoreCase(method)) {
			xnatMethod = new PutMethod(xnatURL);
		}

		if (xnatMethod != null) {
			log.info("Invoking " + xnatMethod.getName() + " on XNAT at " + xnatURL);
			xnatMethod.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);
			statusCode = client.executeMethod(xnatMethod);
			if (statusCode == HttpServletResponse.SC_OK) {
				log.info("Successfully invoked XNAT");
				InputStream xnatResponse = null;
				try {
					xnatResponse = xnatMethod.getResponseBodyAsStream();
					int read = 0;
					byte[] bytes = new byte[4096];
					while ((read = xnatResponse.read(bytes)) != -1) {
						responseStream.write(bytes, 0, read);
					}
				} finally {
					if (xnatResponse != null) {
						try {
							xnatResponse.close();
						} catch (IOException e) {
							log.warning("Error closing XNAT response stream", e);
						}
					}
				}
			} else {
				log.info(XNAT_INVOCATION_ERROR_MESSAGE + ";status code=" + statusCode);
			}
		} else {
			log.info(INVALID_METHOD_MESSAGE + "; got " + method);
			httpResponse.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
			statusCode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
		}
		return statusCode;
	}
}
