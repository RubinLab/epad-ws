package edu.stanford.isis.epadws.handlers.dicom;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;
import edu.stanford.isis.epadws.resources.server.DICOMSeriesOrderServerResource;

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
 * The SQL needed for this is like:
 * 
 * select * from pacsdb.instance as i, pacsdb.series as s where i.series_fk=s.pk and s.series_iuid=?
 * 
 * Now handled by Restlet resource {@link DICOMSeriesOrderServerResource}.
 * 
 * @author amsnyder
 * 
 * @see DICOMSeriesOrderServerResource
 */
public class SeriesOrderHandler extends AbstractHandler
{
	static final ProxyLogger logger = ProxyLogger.getInstance();

	@Override
	public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
	{
		res.setContentType("text/plain");
		res.setStatus(HttpServletResponse.SC_OK);
		res.setHeader("Access-Control-Allow-Origin", "*");
		request.setHandled(true);

		String seriesIUID = req.getParameter("series_iuid");
		logger.info("SeriesTagHandler: series_iuid=" + seriesIUID);

		PrintWriter out = null;
		try {
			out = res.getWriter();

			out.println("filename,order,Slice Location,Content Time");

			MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();

			List<Map<String, String>> orderQueryEntires = queries.getOrderFile(seriesIUID);

			for (Map<String, String> entry : orderQueryEntires) {
				String sopInstanceUID = entry.get("sop_iuid");
				String fileNameField = createFileNameField(sopInstanceUID);
				String instNo = entry.get("inst_no");
				String sliceLoc = createSliceLocation(entry);// entry.get("inst_custom1");
				String contentTime = "null";

				out.println(fileNameField + "," + instNo + "," + sliceLoc + "," + contentTime);
			}

			out.flush();
		} catch (IOException ioe) {
			printException(out, "SeriesOrderHandler had IOException", ioe);
		} catch (Exception e) {
			printException(out, "SeriesOrderHandler had Exception", e);
		} catch (Error err) {
			printException(out, "SeriesOrderHandler had Error", err);
		} finally {
			if (out != null) {
				out.close();
				out = null;
			}
		}
	}

	private static String createSliceLocation(Map<String, String> entry)
	{
		String sliceLoc = entry.get("inst_custom1");
		if (sliceLoc == null) {
			return "0.0";
		}
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

	private static void printException(PrintWriter out, String message, Throwable t)
	{
		logger.warning(message, t);
		if (out != null) {
			out.print(message + " : " + t.getMessage());
			out.flush();
		}
	}
}
