package edu.stanford.isis.epadws.handlers.dicom;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.processing.persistence.MySqlInstance;
import edu.stanford.isis.epadws.processing.persistence.MySqlQueries;

/**
 * @author kurtz
 */
public class DicomSegmentationPathHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final EPADConfig config = EPADConfig.getInstance();

	// TODO Convert to JSON and clean up entire class. Add authentication etc.

	@Override
	public void handle(String base, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException
	{
		httpResponse.setContentType("text/plain");
		httpResponse.setStatus(HttpServletResponse.SC_OK);
		request.setHandled(true);

		PrintWriter responseStream = httpResponse.getWriter();

		String queryString = httpRequest.getQueryString();

		if (queryString != null) {
			queryString = URLDecoder.decode(queryString, "UTF-8");
			queryString = queryString.trim();
			String imageIdKey = getInstanceUIDFromRequest(queryString);
			String[] res = null;
			log.info("SegmentationPath query from ePAD : " + queryString);
			if (imageIdKey != null) {
				log.info("DCMQR: " + imageIdKey);
				try {
					res = retrieveFromEpadDB(imageIdKey);
				} catch (Exception e) {
					log.warning("error reading database", e);
				}
			}
			String separator = config.getParam("fieldSeparator");
			responseStream.println("StudyUID" + separator + "SeriesUID" + separator + "ImageUID");
			if (res[0] != null && res[1] != null && res[2] != null)
				responseStream.println(res[0] + separator + res[1] + separator + res[2]);
			responseStream.flush();
		} else {
			log.warning("No segmentation query in request!");
		}
	}

	private static String getInstanceUIDFromRequest(String queryString)
	{
		String[] parts = queryString.split("&");
		String value = parts[0].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	public static String[] retrieveFromEpadDB(String imageIdKey) throws Exception
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		String study = null;
		String series = null;
		String[] res = new String[3];
		String imageIdKeyWithoutDot = imageIdKey.replaceAll("\\.", "_");
		String path = queries.selectEpadFilePathLike(imageIdKeyWithoutDot);

		log.info("Segmentation path found : " + path);

		if (path != null) {
			String[] tab = path.split("\\/");
			series = tab[tab.length - 2];
			study = tab[tab.length - 3];
		}
		res[0] = study;
		res[1] = series;
		res[2] = imageIdKeyWithoutDot;

		log.info("Segmentation DICOM files found : " + study + " " + series);

		return res;
	}
}
