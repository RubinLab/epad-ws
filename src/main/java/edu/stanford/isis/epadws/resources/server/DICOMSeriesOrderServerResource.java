package edu.stanford.isis.epadws.resources.server;

import java.util.List;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.resource.Get;

import edu.stanford.isis.epadws.common.DicomFormatUtil;
import edu.stanford.isis.epadws.db.mysql.MySqlInstance;
import edu.stanford.isis.epadws.db.mysql.MySqlQueries;

/**
 * Create and "order-file" by calling the MySql database. It will look like the following (to be compatible with the
 * older "ord" file format):
 * 
 * <pre>
 * filename,order,Slice Location,Content Time
 * 1_2_840_113619_2_55_3_2819264857_499_1244214379_94_1.dcm,1,-6.500,104551.000000
 * 1_2_840_113619_2_55_3_2819264857_499_1244214379_94_2.dcm,2,-7.125,104551.000000
 * </pre>
 * 
 * filename is - sopInstanceUID with .dcm at end. sop_iuid in instance table. order - inst_no in instance table. Slice
 * Location - inst_custom1 field in instance table Content Time - Doesn't seem to be used for now, so will set to
 * 'null'.
 * <p>
 * The SQL needed for this is like:
 * 
 * <pre>
 * select * from pacsdb.instance as i, pacsdb.series as s where i.series_fk=s.pk and s.series_iuid=?
 * </pre>
 */
public class DICOMSeriesOrderServerResource extends BaseServerResource
{
	private static final String SUCCESS_MESSAGE = "Series order request succeeded";
	private static final String EXCEPTION_MESSAGE = "Series order request has exception: ";
	private static final String ERROR_MESSAGE = "Series order request has exception: ";

	public DICOMSeriesOrderServerResource()
	{
		setNegotiated(false); // Disable content negotiation
	}

	@Override
	protected void doCatch(Throwable throwable)
	{
		log.warning("An exception was thrown in the DICOM series order resource.", throwable);
	}

	@Get("text")
	public String seriesOrder()
	{
		setResponseHeader("Access-Control-Allow-Origin", "*");

		String seriesIUID = getQueryValue("series_iuid");
		log.info("SeriesTag: series_iuid=" + seriesIUID);

		try {
			String response = query(seriesIUID);
			log.info(SUCCESS_MESSAGE);
			setStatus(Status.SUCCESS_OK);
			return response;
		} catch (Exception e) {
			log.warning(EXCEPTION_MESSAGE, e);
			setStatus(Status.SERVER_ERROR_INTERNAL);
			return EXCEPTION_MESSAGE + e.getMessage();
		} catch (Error e) {
			log.warning(ERROR_MESSAGE, e);
			setStatus(Status.SERVER_ERROR_INTERNAL);
			return ERROR_MESSAGE + e.getMessage();
		}
	}

	private String query(String seriesIUID)
	{
		StringBuilder out = new StringBuilder();

		out.append("filename,order,Slice Location,Content Time\n");

		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();

		List<Map<String, String>> orderQueryEntires = queries.getOrderFile(seriesIUID);

		for (Map<String, String> entry : orderQueryEntires) {
			String sopInstanceUID = entry.get("sop_iuid");
			String fileNameField = createFileNameField(sopInstanceUID);
			String instNo = entry.get("inst_no");
			String sliceLoc = createSliceLocation(entry);// entry.get("inst_custom1");
			String contentTime = "null";

			out.append(fileNameField + "," + instNo + "," + sliceLoc + "," + contentTime + "\n");
		}
		return out.toString();
	}

	private String createSliceLocation(Map<String, String> entry)
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
	private String createFileNameField(String sopInstanceUID)
	{
		return DicomFormatUtil.formatUidToDir(sopInstanceUID) + ".dcm";
	}
}