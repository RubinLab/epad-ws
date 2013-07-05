package edu.stanford.isis.epadws.handlers.dicom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SourceImage;

import edu.stanford.isis.epadws.common.ImageEnhancer;
import edu.stanford.isis.epadws.common.WadoUrlBuilder;
import edu.stanford.isis.epadws.resources.server.DICOMVisuServerResource;
import edu.stanford.isis.epadws.server.ProxyConfig;
import edu.stanford.isis.epadws.server.ProxyLogger;

/**
 * Generate window width and center for a series or study in one quick step.
 * <p>
 * Now handles by Restlet resource {@link DICOMVisuServerResource}.
 * 
 * @author amsnyder
 * 
 * @deprecated
 * @see DICOMVisuServerResource
 */
@Deprecated
public class DicomVisuHandler extends AbstractHandler
{

	private static final ProxyLogger log = ProxyLogger.getInstance();
	ProxyConfig config = ProxyConfig.getInstance();

	public DicomVisuHandler()
	{
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException, ServletException
	{

		httpServletResponse.setContentType("text/plain");
		httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		request.setHandled(true);
		PrintWriter out = httpServletResponse.getWriter();

		String queryString = httpServletRequest.getQueryString();
		queryString = URLDecoder.decode(queryString, "UTF-8");
		log.info("Dicom visu query from ePad : " + queryString);

		if (queryString != null) {
			queryString = queryString.trim();
			// Get the parameters
			String studyIdKey = getStudyUIDFromRequest(queryString);
			String seriesIdKey = getSeriesUIDFromRequest(queryString);
			String imageIdKey = getInstanceUIDFromRequest(queryString);

			// get the wado and the tag file
			if (studyIdKey != null && seriesIdKey != null && imageIdKey != null) {
				File tempDicom = File.createTempFile(imageIdKey, ".tmp");
				feedFileWithDicomFromWado(tempDicom, studyIdKey, seriesIdKey, imageIdKey);

				// Generation of the window and center parameter
				SourceImage srcDicomImage = null;
				try {
					srcDicomImage = new SourceImage(tempDicom.getAbsolutePath());
				} catch (DicomException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				double windowWidth = 0.0;
				double windowCenter = 0.0;

				if (srcDicomImage != null) {
					ImageEnhancer ie = new ImageEnhancer(srcDicomImage);
					ie.findVisuParametersImage();

					windowWidth = ie.getWindowWidth();
					windowCenter = ie.getWindowCenter();
				}

				ProxyConfig config = ProxyConfig.getInstance();
				String separator = config.getParam("fieldSeparator");

				// Write the result
				out.println("windowWidth" + separator + "windowCenter");
				out.println(windowWidth + separator + windowCenter);

				out.flush();

			} else {
				log.info("Bad dicom reference.");
			}

		} else {
			log.info("NO header Query from request.");
		}
	}

	private static String getStudyUIDFromRequest(String queryString)
	{
		log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[0].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getSeriesUIDFromRequest(String queryString)
	{

		log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[1].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private static String getInstanceUIDFromRequest(String queryString)
	{

		log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[2].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	private void feedFileWithDicomFromWado(File temp, String studyIdKey, String seriesIdKey, String imageIdKey)
	{

		// we use wado to get the dicom image
		String host = config.getParam("NameServer");
		int port = config.getIntParam("DicomServerWadoPort");
		String base = config.getParam("WadoUrlExtension");

		WadoUrlBuilder wadoUrlBuilder = new WadoUrlBuilder(host, port, base, WadoUrlBuilder.Type.FILE);

		// GET WADO call result.
		wadoUrlBuilder.setStudyUID(studyIdKey);
		wadoUrlBuilder.setSeriesUID(seriesIdKey);
		wadoUrlBuilder.setObjectUID(imageIdKey);

		try {
			String wadoUrl = wadoUrlBuilder.build();
			log.info("Build wadoUrl = " + wadoUrl);

			// --Get the Dicom file from the server
			HttpClient client = new HttpClient();

			GetMethod method = new GetMethod(wadoUrl);
			// Execute the GET method
			int statusCode = client.executeMethod(method);

			if (statusCode != -1) {
				// Get the result as stream
				InputStream res = method.getResponseBodyAsStream();
				// write the inputStream to a FileOutputStream
				OutputStream out = new FileOutputStream(temp);

				int read = 0;
				byte[] bytes = new byte[4096];

				while ((read = res.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				res.close();
				out.flush();
				out.close();
			}

		} catch (UnsupportedEncodingException e) {
			log.warning("Not able to build wado url for : " + temp.getName(), e);
		} catch (HttpException e) {
			log.warning("Not able to get the wado image : " + temp.getName(), e);
		} catch (IOException e) {
			log.warning("Not able to write the temp dicom image : " + temp.getName(), e);
		}
	}

}
