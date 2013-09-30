package edu.stanford.isis.epadws.handlers.admin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;

public class LoginHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();
	private static final ProxyConfig config = ProxyConfig.getInstance();

	private static final String MISSING_USERNAME_MESSAGE = "Missing user name";
	private static final String INVALID_METHOD_MESSAGE = "Only POST method is valid for this route";
	private static final String LOGIN_EXCEPTION_MESSAGE = "Internal login error";
	private static final String XNAT_INVOCATION_ERROR_MESSAGE = "XNAT call not successful";

	public LoginHandler()
	{
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		PrintWriter out = httpResponse.getWriter();
		String username = "";
		String password = "";
		String result = "";

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		log.info("Login request from ePad : ");

		String method = httpRequest.getMethod();
		if ("POST".equalsIgnoreCase(method)) {
			final String authorizationHeader = httpRequest.getHeader("Authorization");
			if (authorizationHeader != null && authorizationHeader.startsWith("Basic")) {
				String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
				String credentials = new String(Base64.decodeBase64(base64Credentials), Charset.forName("UTF-8"));
				String[] values = credentials.split(":", 2);
				username = values[0];
				password = values[1];
			}

			if (!username.equals("")) {
				String authString = buildAuthorizatonString(username, password);

				try {
					result = invokeXNATSessionIDService(httpResponse, authString);
				} catch (IOException e) {
					log.warning(LOGIN_EXCEPTION_MESSAGE, e);
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					result = LOGIN_EXCEPTION_MESSAGE + ": " + e.getMessage();
				} catch (Exception e) {
					log.warning(LOGIN_EXCEPTION_MESSAGE, e);
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					result = LOGIN_EXCEPTION_MESSAGE + ": " + e.getMessage();
				}
			} else {
				log.info(MISSING_USERNAME_MESSAGE);
				httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				result = MISSING_USERNAME_MESSAGE;
			}
		} else {
			log.info(INVALID_METHOD_MESSAGE);
			httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			httpResponse.setHeader("Access-Control-Allow-Methods", "POST");
			result = INVALID_METHOD_MESSAGE;
		}
		out.append(result);
		out.flush();
	}

	private String invokeXNATSessionIDService(HttpServletResponse httpResponse, String authString) throws IOException,
			HttpException
	{
		String xnatHost = config.getParam("XNATServer");
		int xnatPort = config.getIntParam("XNATPort");
		String xnatSessionBase = config.getParam("XNATSessionURLExtension");
		String xnatSessionURL = buildURLString(xnatHost, xnatPort, xnatSessionBase);
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(xnatSessionURL);
		String result;

		postMethod.setRequestHeader("Authorization", "Basic " + authString);

		log.info("Invoking XNAT session service at " + xnatSessionURL);

		int statusCode = client.executeMethod(postMethod);

		if (statusCode == HttpServletResponse.SC_OK) {
			log.info("Successfully invoked XNAT session service");
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(postMethod.getResponseBodyAsStream());
				int read = 0;
				char[] chars = new char[1024];
				StringBuilder sb = new StringBuilder();

				while ((read = isr.read(chars)) > 0) {
					sb.append(chars, 0, read);
				}
				String jsessionID = sb.toString();
				log.info("JSESSIONID returned from XNAT: " + jsessionID);
				result = jsessionID;
				httpResponse.setStatus(HttpServletResponse.SC_OK);
			} catch (IOException e) {
				log.warning(LOGIN_EXCEPTION_MESSAGE, e);
				httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				result = LOGIN_EXCEPTION_MESSAGE + ": " + e.getMessage();
			} finally {
				if (isr != null)
					isr.close();
			}
		} else {
			httpResponse.setStatus(statusCode);
			log.warning(XNAT_INVOCATION_ERROR_MESSAGE + "; status code =" + statusCode);
			result = XNAT_INVOCATION_ERROR_MESSAGE;
		}
		return result;
	}

	private String buildAuthorizatonString(String username, String password)
	{
		String authString = username + ":" + password;
		log.info("Authorization string: " + authString);
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		log.info("Base64 encoded authorization string: " + authStringEnc);

		return authStringEnc;
	}

	private String buildURLString(String host, int port, String base)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("http://").append(host);
		sb.append(":").append(port);
		sb.append(base);

		return sb.toString();
	}
}