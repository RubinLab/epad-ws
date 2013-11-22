package edu.stanford.isis.epadws.handlers.dicom;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;

import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.dicom.DicomImageDescriptionSearchResult;
import edu.stanford.isis.epad.common.dicom.DicomSeriesDescriptionSearchResult;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.JsonHelper;
import edu.stanford.isis.epadws.processing.persistence.MySqlInstance;
import edu.stanford.isis.epadws.processing.persistence.MySqlQueries;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * Create description of the images in a series and their order.
 * 
 * <p>
 * <code>
 * { "ResultSet": 
 *    [ { "contentTime": "", "fileName": "1_2_840_113619_2_131_2819278861_1343943578_325998.dcm", "instanceNumber": 1, "sliceLocation": "-0.0800" },
 *    ...
 *    ]
 * }
 * </code>
 * <p>
 * The SQL to find this information in the DCM4CHEE database is like:
 * <p>
 * <code>
 * select * from pacsdb.instance as i, pacsdb.series as s where i.series_fk=s.pk and s.series_iuid=?
 * </code>
 * <p>
 * To test:
 * <p>
 * <code>
 * curl -b JSESSIONID=<id> -X GET "http://[host]:[port]/seriesorderj/?series_iuid=1.2.840.113619.2.55.3.25168424.5576.1168603848.697"
 * </code>
 */
public class DicomSeriesOrderHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String MISSING_SERIES_IUID_MESSAGE = "No Series IUID parameter in request";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";
	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error in series order handler";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("application/json");
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();
			if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
				String seriesIUID = httpRequest.getParameter("series_iuid");
				if (seriesIUID != null) {
					peformDICOMSeriesDescriptionQuery(responseStream, seriesIUID);
					statusCode = HttpServletResponse.SC_OK;
				} else {
					log.info(MISSING_SERIES_IUID_MESSAGE);
					responseStream.append(JsonHelper.createJSONErrorResponse(MISSING_SERIES_IUID_MESSAGE));
					statusCode = HttpServletResponse.SC_BAD_REQUEST;
				}
			} else {
				log.info(INVALID_SESSION_TOKEN_MESSAGE);
				responseStream.append(JsonHelper.createJSONErrorResponse(INVALID_SESSION_TOKEN_MESSAGE));
				statusCode = HttpServletResponse.SC_UNAUTHORIZED;
			}
		} catch (Throwable t) {
			log.severe(INTERNAL_EXCEPTION_MESSAGE, t);
			if (responseStream != null)
				responseStream.append(JsonHelper.createJSONErrorResponse(INTERNAL_EXCEPTION_MESSAGE, t));
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		httpResponse.setStatus(statusCode);
	}

	private void peformDICOMSeriesDescriptionQuery(PrintWriter outputStream, String seriesIUID)
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		List<Map<String, String>> orderQueryEntries = queries.getOrderFile(seriesIUID);
		List<DicomImageDescriptionSearchResult> imageDescriptions = new ArrayList<DicomImageDescriptionSearchResult>();

		log.info("DICOMSeriesOrderHandler for series " + seriesIUID);

		for (Map<String, String> entry : orderQueryEntries) {
			String sopInstanceUID = entry.get("sop_iuid");
			String fileName = createFileNameField(sopInstanceUID);
			int instanceNumber = Integer.parseInt(entry.get("inst_no"));
			String sliceLocation = createSliceLocation(entry);// entry.get("inst_custom1");
			String contentTime = "null"; // TODO Can we find this somewhere?

			DicomImageDescriptionSearchResult dicomImageDescription = new DicomImageDescriptionSearchResult(fileName,
					instanceNumber, sliceLocation, contentTime);
			imageDescriptions.add(dicomImageDescription);
		}
		DicomSeriesDescriptionSearchResult dicomSeriesDescriptionSearchResult = new DicomSeriesDescriptionSearchResult(
				imageDescriptions);
		outputStream.print(dicomSeriesDescriptionSearchResult2JSON(dicomSeriesDescriptionSearchResult));
	}

	private String createSliceLocation(Map<String, String> entry)
	{
		String sliceLoc = entry.get("inst_custom1");
		if (sliceLoc == null)
			return "0.0";
		else
			return sliceLoc;
	}

	/**
	 * 
	 * @param sopInstanceUID String
	 * @return String
	 */
	private String createFileNameField(String sopInstanceUID)
	{
		return DicomFormatUtil.formatUidToDir(sopInstanceUID) + ".dcm";
	}

	private String dicomSeriesDescriptionSearchResult2JSON(
			DicomSeriesDescriptionSearchResult dicomSeriesDescriptionSearchResult)
	{
		Gson gson = new Gson();

		return gson.toJson(dicomSeriesDescriptionSearchResult);
	}
}
