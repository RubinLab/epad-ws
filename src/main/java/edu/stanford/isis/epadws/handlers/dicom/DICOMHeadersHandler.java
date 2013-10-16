package edu.stanford.isis.epadws.handlers.dicom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.util.WadoUrlBuilder;
import edu.stanford.isis.epadws.processing.pipeline.DicomHeadersTask;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * Download headers for a series or study in one quick step.
 * 
 * @author amsnyder
 * 
 */
public class DICOMHeadersHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();
	private static final ProxyConfig config = ProxyConfig.getInstance();

	private static final String INTERNAL_ERROR_MESSAGE = "Internal error on delete";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";
	private static final String MISSING_QUERY_MESSAGE = "No query paramaters specified";
	private static final String BADLY_FORMED_QUERY_MESSAGE = "Invalid query paramaters specified";
	private static final String WADO_INVOCATION_ERROR_MESSAGE = "Error retrieving header from WADO";

	public DICOMHeadersHandler()
	{
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServletException
	{
		PrintWriter responseStream = null;
		int statusCode;

		try {
			responseStream = httpResponse.getWriter();

			httpResponse.setContentType("text/plain");
			request.setHandled(true);

			if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
				String queryString = httpRequest.getQueryString();
				queryString = URLDecoder.decode(queryString, "UTF-8");

				if (queryString != null) {
					queryString = queryString.trim();
					log.info("DICOMHeadersHandler query: " + queryString);
					String studyIdKey = getStudyUIDFromRequest(queryString);
					String seriesIdKey = getSeriesUIDFromRequest(queryString);
					String imageIdKey = getInstanceUIDFromRequest(queryString);

					if (studyIdKey != null && seriesIdKey != null && imageIdKey != null) {
						File tempDICOMFile = File.createTempFile(imageIdKey, ".tmp");
						int wadoStatusCode = feedFileWithDICOMFromWADO(tempDICOMFile, studyIdKey, seriesIdKey, imageIdKey);
						if (wadoStatusCode == HttpServletResponse.SC_OK) {
							File tempTag = File.createTempFile(imageIdKey, "_tag.tmp");
							ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
							taskExecutor.execute(new DicomHeadersTask(tempDICOMFile, tempTag));
							taskExecutor.shutdown();
							try {
								taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
								BufferedReader tagReader = new BufferedReader(new FileReader(tempTag.getAbsolutePath()));
								try {
									String line;
									while ((line = tagReader.readLine()) != null) {
										log.info("DICOMHEADERLINE: " + line);
										responseStream.println(line);
									}
								} finally {
									if (tagReader != null) {
										try {
											tagReader.close();
										} catch (IOException e) {
											log.warning("Error closing DICOM tag response stream", e);
										}
									}
								}
								statusCode = HttpServletResponse.SC_OK;
							} catch (InterruptedException e) {
								log.info("DICOM headers task interrupted");
								responseStream.print("DICOM headers task interrupted");
								Thread.currentThread().interrupt();
								statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
							}
						} else {
							log.info(WADO_INVOCATION_ERROR_MESSAGE + "; status code=" + wadoStatusCode);
							responseStream.print(WADO_INVOCATION_ERROR_MESSAGE + "; status code=" + wadoStatusCode);
							statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
						}
					} else {
						log.info(BADLY_FORMED_QUERY_MESSAGE);
						responseStream.append(BADLY_FORMED_QUERY_MESSAGE);
						statusCode = HttpServletResponse.SC_BAD_REQUEST;
					}
				} else {
					log.info(MISSING_QUERY_MESSAGE);
					responseStream.append(MISSING_QUERY_MESSAGE);
					statusCode = HttpServletResponse.SC_BAD_REQUEST;
				}
			} else {
				log.info(INVALID_SESSION_TOKEN_MESSAGE);
				responseStream.append(INVALID_SESSION_TOKEN_MESSAGE);
				statusCode = HttpServletResponse.SC_UNAUTHORIZED;
			}
		} catch (Throwable t) {
			log.warning(INTERNAL_ERROR_MESSAGE, t);
			responseStream.print(INTERNAL_ERROR_MESSAGE + ": " + t.getMessage());
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		} finally {
			if (responseStream != null) {
				responseStream.flush();
				responseStream.close();
			}
		}
		httpResponse.setStatus(statusCode);
	}

	private int feedFileWithDICOMFromWADO(File temp, String studyIdKey, String seriesIdKey, String imageIdKey)
			throws IOException
	{
		String host = config.getParam("NameServer");
		int port = config.getIntParam("DicomServerWadoPort");
		String base = config.getParam("WadoUrlExtension");

		WadoUrlBuilder wadoUrlBuilder = new WadoUrlBuilder(host, port, base, WadoUrlBuilder.ContentType.FILE);

		wadoUrlBuilder.setStudyUID(studyIdKey);
		wadoUrlBuilder.setSeriesUID(seriesIdKey);
		wadoUrlBuilder.setObjectUID(imageIdKey);

		String wadoUrl = wadoUrlBuilder.build();

		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(wadoUrl);
		int statusCode = client.executeMethod(method);

		if (statusCode == HttpServletResponse.SC_OK) {
			InputStream wadoResponseStream = null;
			OutputStream outputStream = null;
			try {
				wadoResponseStream = method.getResponseBodyAsStream();
				outputStream = new FileOutputStream(temp);
				int read = 0;
				byte[] bytes = new byte[4096];
				while ((read = wadoResponseStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
			} finally {
				if (wadoResponseStream != null)
					wadoResponseStream.close();
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			}
		}
		return statusCode;
	}

	// TODO Fix this mess
	private static String getStudyUIDFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[0].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getSeriesUIDFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[1].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getInstanceUIDFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[2].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}
}
