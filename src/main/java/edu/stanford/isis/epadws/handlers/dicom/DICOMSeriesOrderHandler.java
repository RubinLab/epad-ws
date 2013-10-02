package edu.stanford.isis.epadws.handlers.dicom;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.dicom.DICOMSeriesOrderSearchResult;
import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.util.JsonHelper;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * Create and "order-file" by calling the MySql database. It will look like the following (to be compatible with the
 * older "ord" file format.
 * 
 * filename,order,Slice Location,Content Time
 * 1_2_840_113619_2_55_3_2819264857_499_1244214379_94_1.dcm,1,-6.500,104551.000000
 * 1_2_840_113619_2_55_3_2819264857_499_1244214379_94_2.dcm,2,-7.125,104551.000000
 * 
 * filename is - sopInstanceUID with .dcm at end. sop_iuid in instance table. order - inst_no in instance table. Slice
 * Location - inst_custom1 field in instance table Content Time - Doesn't seem to be used for now, so will set to
 * 'null'.
 * 
 * The SQL needed for this is like: <code>
 * select * from pacsdb.instance as i, pacsdb.series as s where i.series_fk=s.pk and s.series_iuid=?
 * </code>
 * 
 * To test: <code>
 * curl -b JSESSIONID=<id> -X GET "http://epad-dev2.stanford.edu:8080/seriesorderj/?series_iuid=1.2.840.113619.2.55.3.25168424.5576.1168603848.697"
 * </code>
 */
public class DICOMSeriesOrderHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();

	private static final String MISSING_SERIES_IUID_MESSAGE = "No Series IUID parameter in request";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";
	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		PrintWriter out = httpResponse.getWriter();

		httpResponse.setContentType("application/json");
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		request.setHandled(true);

		if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
			String seriesIUID = httpRequest.getParameter("series_iuid");
			if (seriesIUID != null) {
				try {
					peformDICOMSeriesOrderQuery(out, seriesIUID);
					httpResponse.setStatus(HttpServletResponse.SC_OK);
				} catch (Exception e) {
					log.warning(INTERNAL_EXCEPTION_MESSAGE, e);
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					out.append(JsonHelper.createJSONErrorResponse(INTERNAL_EXCEPTION_MESSAGE, e));
				} catch (Error e) {
					log.warning(INTERNAL_EXCEPTION_MESSAGE, e);
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					out.append(JsonHelper.createJSONErrorResponse(INTERNAL_EXCEPTION_MESSAGE, e));
				}
			} else {
				log.info(MISSING_SERIES_IUID_MESSAGE);
				httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.append(JsonHelper.createJSONErrorResponse(MISSING_SERIES_IUID_MESSAGE));
			}
		} else {
			log.info(INVALID_SESSION_TOKEN_MESSAGE);
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			out.append(JsonHelper.createJSONErrorResponse(INVALID_SESSION_TOKEN_MESSAGE));
		}
		out.flush();
		out.close();
	}

	private static void peformDICOMSeriesOrderQuery(PrintWriter out, String seriesIUID) throws NumberFormatException
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		List<Map<String, String>> orderQueryEntries = queries.getOrderFile(seriesIUID);

		log.info("DICOMSeriesOrderHandler: series_iuid=" + seriesIUID);

		out.println("{ \"ResultSet\": [");

		for (Map<String, String> entry : orderQueryEntries) {
			String sopInstanceUID = entry.get("sop_iuid");
			String fileName = createFileNameField(sopInstanceUID);
			int instanceNumber = Integer.parseInt(entry.get("inst_no"));
			String sliceLocation = createSliceLocation(entry);// entry.get("inst_custom1");
			String contentTime = "null";

			DICOMSeriesOrderSearchResult searchResult = new DICOMSeriesOrderSearchResult(fileName, instanceNumber,
					sliceLocation, contentTime);
			out.println(seriesOrderSearchResult2JSON(searchResult));
		}
		out.print("] }");
	}

	private static String createSliceLocation(Map<String, String> entry)
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
	private static String createFileNameField(String sopInstanceUID)
	{
		return DicomFormatUtil.formatUidToDir(sopInstanceUID) + ".dcm";
	}

	private static String seriesOrderSearchResult2JSON(DICOMSeriesOrderSearchResult seriesOrderSearchResult)
	{
		Gson gson = new Gson();

		return gson.toJson(seriesOrderSearchResult);
	}

}
