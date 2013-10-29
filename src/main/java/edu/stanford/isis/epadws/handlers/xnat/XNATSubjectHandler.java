package edu.stanford.isis.epadws.handlers.xnat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.JsonHelper;
import edu.stanford.isis.epad.common.xnat.XNATSubjectDescription;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * XNAT-based subject retrieval handler. At present, a pretty thin wrapper around an XNAT subjects call. Primarily used
 * for search.
 * <p>
 * <code>
 * curl -b JSESSIONID=<session_id> -X GET "http://[host:port]/subjects?project=*&src=*nice*&columns=label,src" 
 * </code>
 * 
 * @author martin
 * @see XNATSubjectDescription
 */
public class XNATSubjectHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final EPADConfig config = EPADConfig.getInstance();

	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Invalid session token";
	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error getting subjects";
	private static final String XNAT_INVOCATION_ERROR_MESSAGE = "Error invoking XNAT subject call";

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
				statusCode = invokeXNATSubjectService(base, httpRequest, httpResponse, responseStream);
			} else {
				log.info(INVALID_SESSION_TOKEN_MESSAGE);
				responseStream.print(JsonHelper.createJSONErrorResponse(INVALID_SESSION_TOKEN_MESSAGE));
				statusCode = HttpServletResponse.SC_UNAUTHORIZED;
			}
		} catch (Throwable t) {
			log.warning(INTERNAL_EXCEPTION_MESSAGE, t);
			responseStream.print(JsonHelper.createJSONErrorResponse(INTERNAL_EXCEPTION_MESSAGE, t));
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			responseStream.flush();
		}
		httpResponse.setStatus(statusCode);
	}

	private int invokeXNATSubjectService(String base, HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			OutputStream outputStream) throws IOException
	{
		String xnatHost = config.getStringConfigurationParameter("XNATServer");
		int xnatPort = config.getIntegerConfigurationParameter("XNATPort");
		String xnatSubjectURL = XNATUtil.buildURLString(xnatHost, xnatPort, XNATUtil.XNAT_SUBJECT_BASE, base);
		HttpClient client = new HttpClient();
		String jsessionID = XNATUtil.getJSessionIDFromRequest(httpRequest);
		String queryString = httpRequest.getQueryString();

		if (queryString != null) {
			queryString = URLDecoder.decode(queryString, "UTF-8");
			queryString = queryString.trim();
			xnatSubjectURL = xnatSubjectURL.replaceFirst("/$", "") + "?" + queryString;
		}

		GetMethod getMethod = new GetMethod(xnatSubjectURL);
		getMethod.setRequestHeader("Cookie", "JSESSIONID=" + jsessionID);
		log.info("Invoking XNAT subject service at " + xnatSubjectURL);

		int statusCode = client.executeMethod(getMethod);
		if (statusCode == HttpServletResponse.SC_OK) {
			log.info("Successfully invoked XNAT subject service");
			InputStream xnatResponse = null;
			try {
				xnatResponse = getMethod.getResponseBodyAsStream();
				int read = 0;
				byte[] bytes = new byte[4096];
				while ((read = xnatResponse.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
			} finally {
				if (xnatResponse != null) {
					try {
						xnatResponse.close();
					} catch (IOException e) {
						log.warning("Error closing XNAT subject response stream", e);
					}
				}
			}
		} else {
			log.info(XNAT_INVOCATION_ERROR_MESSAGE + ";status code=" + statusCode);
			JsonHelper.createJSONErrorResponse(XNAT_INVOCATION_ERROR_MESSAGE + ";status code=" + statusCode);
		}
		return statusCode;
	}
}
