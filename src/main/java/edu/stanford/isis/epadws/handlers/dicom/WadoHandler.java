package edu.stanford.isis.epadws.handlers.dicom;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.resources.server.WADOServerResource;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * WandoHandler
 * 
 * @author ckurtz
 * 
 * @see WADOServerResource
 */
public class WadoHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final EPADConfig config = EPADConfig.getInstance();

	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error";
	private static final String MISSING_QUERY_MESSAGE = "No query in request";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		ServletOutputStream responseStream = null;
		int statusCode;

		httpResponse.setContentType("image/jpeg");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getOutputStream();

			if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
				String queryString = httpRequest.getQueryString();
				queryString = URLDecoder.decode(queryString, "UTF-8");
				if (queryString != null) {
					log.info("WADOHandler, query=" + queryString);
					statusCode = performWADOQuery(queryString, responseStream);
				} else {
					log.info(MISSING_QUERY_MESSAGE);
					statusCode = HttpServletResponse.SC_BAD_REQUEST;
				}
				responseStream.flush();
			} else {
				log.info(INVALID_SESSION_TOKEN_MESSAGE);
				statusCode = HttpServletResponse.SC_UNAUTHORIZED;
			}
		} catch (Throwable t) {
			log.warning(INTERNAL_EXCEPTION_MESSAGE, t);
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		httpResponse.setStatus(statusCode);
	}

	private int performWADOQuery(String queryString, ServletOutputStream out) throws IOException, HttpException
	{
		String host = config.getParam("NameServer");
		int port = config.getIntParam("DicomServerWadoPort");
		String base = config.getParam("WadoUrlExtension");
		String wadoUrl = buildWADOURL(host, port, base, queryString);
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(wadoUrl);
		int statusCode = client.executeMethod(method);

		if (statusCode == HttpServletResponse.SC_OK) {
			InputStream res = null;
			try {
				res = method.getResponseBodyAsStream();
				int read = 0;
				byte[] bytes = new byte[4096];
				while ((read = res.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
			} finally {
				if (res != null) {
					try {
						res.close();
					} catch (IOException e) {
						log.warning("Error closing WADO response stream", e);
					}
				}
			}
		} else {
			log.info(INTERNAL_EXCEPTION_MESSAGE);
		}
		return statusCode;
	}

	private String buildWADOURL(String host, int port, String base, String queryString)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("http://").append(host);
		sb.append(":").append(port);
		sb.append(base);
		sb.append(queryString);

		log.info("Build wadoUrl = " + sb.toString());

		return sb.toString();
	}
}
