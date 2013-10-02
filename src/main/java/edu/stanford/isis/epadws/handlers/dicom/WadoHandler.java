package edu.stanford.isis.epadws.handlers.dicom;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epadws.resources.server.WADOServerResource;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * WandoHandler
 * <p>
 * Now handled by Restlet resource {@link WADOServerResource}.
 * 
 * @author ckurtz
 * 
 * @see WADOServerResource
 */
public class WadoHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();
	private static final ProxyConfig config = ProxyConfig.getInstance();

	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error";
	private static final String MISSING_QUERY_MESSAGE = "No query in request";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		ServletOutputStream out = httpResponse.getOutputStream();

		httpResponse.setContentType("image/jpeg");
		request.setHandled(true);

		if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
			String queryString = httpRequest.getQueryString();
			queryString = URLDecoder.decode(queryString, "UTF-8");
			log.info("WADO query from ePAD: " + queryString);
			if (queryString != null) {
				try {
					String host = config.getParam("NameServer");
					int port = config.getIntParam("DicomServerWadoPort");
					String base = config.getParam("WadoUrlExtension");
					String wadoUrl = buildWADOURL(host, port, base, queryString);
					HttpClient client = new HttpClient();
					GetMethod method = new GetMethod(wadoUrl);
					int statusCode = client.executeMethod(method);
					if (statusCode == HttpServletResponse.SC_OK) {
						InputStream res = method.getResponseBodyAsStream();
						int read = 0;
						byte[] bytes = new byte[4096];
						while ((read = res.read(bytes)) != -1) {
							out.write(bytes, 0, read);
						}
						res.close();
						log.info("Query succeeded");
						httpResponse.setStatus(HttpServletResponse.SC_OK);
					} else {
						log.info(INTERNAL_EXCEPTION_MESSAGE);
						httpResponse.setStatus(statusCode);
					}
				} catch (Exception e) {
					log.info(INTERNAL_EXCEPTION_MESSAGE);
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} catch (Error e) {
					log.info(INTERNAL_EXCEPTION_MESSAGE);
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			} else {
				log.info(MISSING_QUERY_MESSAGE);
				httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} else {
			log.info(INVALID_SESSION_TOKEN_MESSAGE);
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		out.flush();
		out.close();
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
