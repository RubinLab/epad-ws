/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.handlers.admin;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.hakan.aim3api.base.AimException;
import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.plugin.server.EPadPlugin;
import edu.stanford.isis.epad.plugin.server.impl.EPadPluginImpl;
import edu.stanford.isis.epad.plugin.server.impl.EPadProxyConfigImpl;
import edu.stanford.isis.epadws.EPadWebServerVersion;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.resources.server.EPadWebServiceServerResource;
import edu.stanford.isis.epadws.server.managers.pipeline.PipelineFactory;

/**
 * Returns the public status of the Proxy.
 * <p>
 * Now handled by Restlet resource {@link EPadWebServiceServerResource}.
 * 
 * @author amsnyder
 * 
 * @deprecated
 */
@Deprecated
public class StatusHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();
	public String serviceUrl = ProxyConfig.getInstance().getParam("serviceUrl");

	private final long startTime;

	public StatusHandler()
	{
		startTime = System.currentTimeMillis();
	}

	/**
	 * Save the annotation to the server in the aim database. An invalid annotation will not be saved. Save a file backup
	 * just in case.
	 * 
	 * @param aim
	 * 
	 * @return
	 * 
	 * @throws AimException
	 * 
	 * @throws IOException
	 */

	@SuppressWarnings("unused")
	private static void saveToServer() throws IOException
	{
		log.info("saveToServer");

		File fileToUpload = new File("/home/epad/AIM_029dwxvdypcwvlimke1kb4f59nlt3nw1ymmlf2bo.xml");
		String urlToConnect = "http://epad-dev2.stanford.edu:8080/aimresource/";
		String paramToSend = "upload";
		String boundary = Long.toHexString(System.currentTimeMillis());

		log.info("saveToServer has an aim file");

		URLConnection connection = new URL(urlToConnect).openConnection();
		connection.setDoOutput(true); // This sets request method to POST.
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

		connection.connect();

		log.info("Connection open ");

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));

			writer.println("--" + boundary);
			writer.println("Content-Disposition: form-data; name=\"paramToSend\"");
			writer.println("Content-Type: text/plain; charset=UTF-8");
			writer.println();
			writer.println(paramToSend);

			writer.println("--" + boundary);
			writer.println("Content-Disposition: form-data; name=\"fileToUpload\"; filename=\"file.txt\"");
			writer.println("Content-Type: text/plain; charset=UTF-8");
			writer.println();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToUpload), "UTF-8"));
				for (String line; (line = reader.readLine()) != null;) {
					writer.println(line);
				}
			} catch (Exception e) {
				log.warning("Failed to send aim with buffer.", e);
			} finally {
				if (reader != null)
					try {
						reader.close();
					} catch (IOException logOrIgnore) {
					}
			}

			writer.println("--" + boundary + "--");
		} catch (Exception e) {
			log.warning("Failed to send aim.", e);
		} finally {
			if (writer != null)
				writer.close();
		}

		log.info("saveToServer done");

	}

	public static void saveToServer2() throws IOException
	{

		log.info("saveToServer");

		File fileToUpload = new File("/home/epad/AIM_029dwxvdypcwvlimke1kb4f59nlt3nw1ymmlf2bo.xml");
		String urlToConnect = "http://epad-dev2.stanford.edu:8080/aimresource/";

		log.info("saveToServer has an aim file");

		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(urlToConnect);

			FileBody bin = new FileBody(fileToUpload);

			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("bin", bin);

			httppost.setEntity(reqEntity);

			System.out.println("executing request " + httppost.getRequestLine());
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			if (resEntity != null) {
				System.out.println("Response content length: " + resEntity.getContentLength());
			}
			// TODO EntityUtils.consume(resEntity);
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {
			}
		}

		log.info("saveToServer done");

	}

	public static byte[] serialize(Object obj) throws IOException
	{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);

		return b.toByteArray();

	}

	/*
	 * public static void tryPOST() throws IOException { System.out.println("=== PUT: create a new database ===");
	 * 
	 * // The java URL connection to the resource URL url = new URL("http://epad-prod1.stanford.edu:8080/aimresource");
	 * System.out.println("\n* URL: " + url);
	 * 
	 * // Establish the connection to the URL HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // // Set
	 * an output connection // conn.setDoOutput(true); // Set as PUT request conn.setRequestMethod("PUT");
	 * 
	 * // Get and cache output stream OutputStream out = new BufferedOutputStream(conn.getOutputStream());
	 * System.out.println("\n* get the stream"); // Create and cache file input stream InputStream in = new
	 * BufferedInputStream( new FileInputStream(ResourceUtils.getEPADWebServerAnnotationsDir() +
	 * "AIM_4145453244701952000.xml"));
	 * 
	 * // Send document to server System.out.println("\n* Send document..."); for(int i; (i = in.read()) != -1;)
	 * out.write(i); in.close(); out.close();
	 * 
	 * // Print the HTTP response code System.out.println("\n* HTTP response: " + conn.getResponseCode() + " (" +
	 * conn.getResponseMessage() + ')');
	 * 
	 * // Close connection conn.disconnect(); }
	 */

	// public static void tryPUT2() throws IOException {
	// HttpClient client = new HttpClient();
	// /* Implement an HTTP PUT method on the resource */
	// PutMethod put = new PutMethod("http://epad-prod1.stanford.edu:8080/aimresource/");
	// File input = new File(ResourceUtils.getEPADWebServerAnnotationsDir() + "AIM_4145453244701952000.xml");
	// RequestEntity entity = new FileRequestEntity(input, "Content-Length: 1431");
	// put.setRequestEntity(entity);
	// /* Create an access token and set it in the request header*/
	// // String token = GetAuth.getAuthQuest();
	// // put.setRequestHeader("Authorization", token);
	// /* Execute the HTTP PUT request */
	// client.executeMethod(put);
	//
	//
	// /* Display the response */
	// System.out.println("Response status code: " + put.getStatusCode());
	// System.out.println("Response header: ");
	// // Header[] respHeaders=put.getResponseHeaders();
	// //
	// // for (int i = 0; i < respHeaders.length; i++) {
	// // System.out.println(respHeaders[i]);
	// // }
	// }

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{

		log.info("status request");
		PrintWriter out = httpResponse.getWriter();

		try {
			httpResponse.setContentType("text/plain");
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			httpResponse.setHeader("Access-Control-Allow-Origin", "*");

			request.setHandled(true);

			long upTime = System.currentTimeMillis() - startTime;
			long upTimeSec = upTime / 1000;

			ProxyConfig proxyConfig = ProxyConfig.getInstance();
			EPadProxyConfigImpl ePadProxyConfig = new EPadProxyConfigImpl();
			EPadPlugin ePadPlugin = new EPadPluginImpl();

			out.println();
			out.println("--------------  DicomProxy status  --------------");
			out.println();
			out.println("DICOM Proxy uptime: " + upTimeSec + " sec");
			out.println();
			out.println("Version: " + EPadWebServerVersion.getBuildDate());
			out.println();
			out.println("DicomProxy listening on: " + proxyConfig.getParam("ListenIP") + ":"
					+ proxyConfig.getParam("ListenPort"));
			out.println();
			out.println("Plugin Version - interface:      " + EPadPlugin.PLUGIN_INTERFACE_VERSION);
			out.println("Plugin Version - implementation: " + ePadPlugin.getPluginImplVersion());
			MySqlInstance instance = MySqlInstance.getInstance();
			out.println();
			out.println("DB Startup Time: " + instance.getStartupTime() + " ms");
			out.println();
			out.println("epad.war serverProxy=" + ePadProxyConfig.getProxyConfigParam("serverProxy"));
			out.println();
			out.println("pipelineActivity : " + getPipelineActivityLevel());

			saveToServer2();

		} catch (Exception e) {
			log.warning("Failed to build status page.", e);
		} finally {
			out.flush();
		}
	}

	private String getPipelineActivityLevel()
	{
		PipelineFactory pipelineFactory = PipelineFactory.getInstance();
		StringBuilder sb = new StringBuilder();
		int activityLevel = pipelineFactory.getActivityLevel();
		int errCount = pipelineFactory.getErrorFileCount();
		if (activityLevel == 0) {
			sb.append("idle.");
		} else {
			sb.append("active-" + activityLevel);
			if (errCount > 0) {
				sb.append(" errors-" + errCount);
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @return String
	 */
	@SuppressWarnings("unused")
	private String getStatusAsText()
	{
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}

}
