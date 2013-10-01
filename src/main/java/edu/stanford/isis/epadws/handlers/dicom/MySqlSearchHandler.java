package edu.stanford.isis.epadws.handlers.dicom;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.dicom.DicomStudySearchType;
import edu.stanford.isis.epad.common.dicom.RSeriesData;
import edu.stanford.isis.epad.common.util.SearchResultUtils;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;
import edu.stanford.isis.epadws.processing.mysql.MySqlStudyQueryBuilder;
import edu.stanford.isis.epadws.resources.server.DICOMSearchServerResource;

/**
 * Now handled by Restlet resource {@link DICOMSearchServerResource}.
 * 
 * @author amsnyder
 * 
 * @see DICOMSearchServerResource
 */
public class MySqlSearchHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();

	public MySqlSearchHandler()
	{
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		httpResponse.setContentType("text/plain");
		httpResponse.setStatus(HttpServletResponse.SC_OK);
		request.setHandled(true);

		PrintWriter out = httpResponse.getWriter();

		String queryString = httpRequest.getQueryString();
		queryString = URLDecoder.decode(queryString, "UTF-8");

		log.info("Query from ePad : " + queryString);

		if (queryString != null) {
			queryString = queryString.trim();

			if (isSeriesRequest(queryString)) {
				handleSeriesRequest(out, queryString);
			} else {
				handleStudyRequest(out, queryString, httpRequest);
			}
		} else {
			log.info("NO Query from request.");
		}
		out.flush();
	}

	private void handleStudyRequest(PrintWriter out, String queryString, HttpServletRequest httpRequest)
	{
		try {
			DicomStudySearchType searchType = getSearchType(httpRequest);

			log.info(queryString);
			String[] parts = queryString.split("=");
			String value = parts[1].trim();
			log.info("MySqlSearchHandler(handleStudyRequest) = " + value);

			ProxyConfig config = ProxyConfig.getInstance();
			String separator = config.getParam("fieldSeparator");

			debugPrintSQL(searchType, value);

			// headers for the request.
			out.print(new SearchResultUtils().get_STUDY_SEARCH_HEADER());

			final Map<String, String> translator = new HashMap<String, String>();
			translator.put("StudyUID", "study_iuid");
			translator.put("Patient Name", "pat_name");
			translator.put("Patient ID", "pat_id");
			translator.put("Exam Type", "modality");
			translator.put("Date Acquired", "study_datetime");
			translator.put("PNG Not Ready", "study_status");
			translator.put("Series Count", "number_series");
			translator.put("First SeriesUID", "series_iuid");
			translator.put("First Series Date Acquired", "pps_start");
			translator.put("Study Accession Number", "accession_no");
			translator.put("Images Count", "sum_images");

			translator.put("StudyID", "study_id");
			translator.put("Study Description", "study_desc");
			translator.put("Physician Name", "ref_physician");
			translator.put("Patient Birthdate", "pat_birthdate");
			translator.put("Patient Sex", "pat_sex");

			MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
			List<Map<String, String>> result = dbQueries.doStudySearch(searchType.toString(), value);

			log.info("MySql found " + result.size() + " results.");
			log.info("Search result: " + result.toString());

			for (Map<String, String> row : result) {
				StringBuilder sb = new StringBuilder();
				sb.append(row.get(translator.get("StudyUID"))).append(separator);
				sb.append(clean(row.get(translator.get("Patient Name")), "^")).append(separator);
				sb.append(row.get(translator.get("Patient ID"))).append(separator);
				sb.append(clean(row.get(translator.get("Exam Type")), "^")).append(separator);
				sb.append(row.get(translator.get("Date Acquired"))).append(separator);
				sb.append(row.get(translator.get("PNG Not Ready"))).append(separator);
				sb.append(row.get(translator.get("Series Count"))).append(separator);
				sb.append(row.get(translator.get("First SeriesUID"))).append(separator);
				sb.append(row.get(translator.get("First Series Date Acquired"))).append(separator);
				sb.append(row.get(translator.get("Study Accession Number"))).append(separator);
				sb.append(clean(row.get(translator.get("Images Count")), ", ")).append(separator);
				;

				sb.append(row.get(translator.get("StudyID"))).append(separator);
				sb.append(row.get(translator.get("Study Description"))).append(separator);
				sb.append(clean(row.get(translator.get("Physician Name")), "^")).append(separator);
				sb.append(row.get(translator.get("Patient Birthdate"))).append(separator);
				sb.append(row.get(translator.get("Patient Sex")));

				sb.append("\n");
				log.info("line = " + sb.toString());
				out.print(sb.toString());
			}

		} catch (Exception e) {
			log.warning("handleStudyRequest (mysql) had..", e);
		}
	}

	/**
	 * Remove a specific character from a from the string.
	 * 
	 * @param input String
	 * @param removeChar String character to remove
	 * @return String cleaned of character and trimmed.
	 */
	private static String clean(String input, String removeChar)
	{
		if (input == null)
			return null;
		if (removeChar == null)
			return input;

		return input.replaceAll(removeChar, " ").trim();
	}

	/**
	 * The DCM4CHEE MySql table returns the series date in the format. YYYY-MM-DD HH:MM:SS.sss
	 * 
	 * But we want it in the format:
	 * 
	 * YYYYMMDD
	 * 
	 * @param seriesDate YYYY-MM-DD HH:MM:SS.sss
	 * @return YYYYMMDD
	 */
	private static String cleanSeriesDate(String seriesDate)
	{
		try {
			if (seriesDate != null) {
				String[] parts = seriesDate.split(" ");
				return clean(parts[0], "-").replaceAll(" ", "");
			} else {
				return "00000000";
			}
		} catch (Exception e) {
			log.warning("cleanSeriesDate parse error for: " + seriesDate, e);
		}
		return seriesDate;
	}

	/**
	 * To Do: Get rid of this when debugging is finished.
	 * 
	 * @param searchType DicomSearchType The type of search patient name, modality, study date, etc...
	 * @param value String what to search for ...
	 */
	private void debugPrintSQL(DicomStudySearchType searchType, String value)
	{
		MySqlStudyQueryBuilder queryBuilder = new MySqlStudyQueryBuilder(searchType.toString(), value);
		String query = queryBuilder.createStudySearchQuery();
		log.info("SQL: " + query);
	}

	/**
	 * Get all the series for a study.
	 * 
	 * @param out PrintWriter for the output.
	 * @param queryString String - query string which contains the study id. The query line looks like the following:
	 *          http://[ip:port]/search?searchType=series&studyUID=[studyID].
	 * 
	 *          The return is text in a CSV format. The first line are the keys, and all the following lines are data:
	 * 
	 *          keys: "Series Id, Patient Id, Patient Name, Series Date, Exam Type, Thumbnail URL, Series Description,
	 *          NumberOfSeriesRelatedInstances, ImagesInSeries".
	 * 
	 *          The values are one line per series.
	 * 
	 *          Here we will look for *.series files within the study directory. If it is there then It will read that
	 *          file and add it to the result.
	 */
	private void handleSeriesRequest(PrintWriter out, String queryString)
	{
		MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
		String studyIdKey = getStudyUIDFromRequest(queryString);
		String studyUID = DicomFormatUtil.formatDirToUid(studyIdKey);
		List<Map<String, String>> series = dbQueries.doSeriesSearch(studyUID);

		out.print(RSeriesData.getHeaderColumn() + "\n");
		log.info("series search column header: " + RSeriesData.getHeaderColumn());

		Map<String, String> seriesTranslatorMap = createSeriesTranslatorMap();

		ProxyConfig config = ProxyConfig.getInstance();
		String separator = config.getParam("fieldSeparator");

		log.info("dbQueries.doSeriesSearch() had " + series.size() + " results, for studyUID=" + studyUID);
		for (Map<String, String> row : series) {
			StringBuilder sb = new StringBuilder();
			sb.append(row.get(seriesTranslatorMap.get("series-id"))).append(separator);
			sb.append(row.get(seriesTranslatorMap.get("patient-id"))).append(separator);
			sb.append(row.get(seriesTranslatorMap.get("patient-name"))).append(separator);
			sb.append(cleanSeriesDate(row.get(seriesTranslatorMap.get("series-date")))).append(separator);
			sb.append(row.get(seriesTranslatorMap.get("exam-type"))).append(separator);
			sb.append(row.get(seriesTranslatorMap.get("thumbnail-url"))).append(separator);
			sb.append(row.get(seriesTranslatorMap.get("series-desc"))).append(separator);
			sb.append(row.get(seriesTranslatorMap.get("num-series-in-rel-instances"))).append(separator);
			sb.append(row.get(seriesTranslatorMap.get("images-in-series"))).append(separator);
			sb.append(row.get(seriesTranslatorMap.get("png-not-ready"))).append(separator);

			sb.append(row.get(seriesTranslatorMap.get("body-part"))).append(separator);
			sb.append(row.get(seriesTranslatorMap.get("institution"))).append(separator);
			sb.append(row.get(seriesTranslatorMap.get("station-name"))).append(separator);
			sb.append(row.get(seriesTranslatorMap.get("department")));
			// sb.append(",")sb.append(row.get("accession_no")); //ToDo: uncomment this when ready to test client side.
			out.print(sb.toString() + "\n");
			log.info(sb.toString());
		}

	}

	/**
	 * Create a map where the keys are column names in the html page and values are column names in the database.
	 * 
	 * @return Map of String keys to String values.
	 */
	private static Map<String, String> createSeriesTranslatorMap()
	{
		Map<String, String> retVal = new HashMap<String, String>();
		retVal.put("series-id", "series_iuid");
		retVal.put("patient-id", "pat_id");
		retVal.put("patient-name", "pat_name");
		retVal.put("series-date", "study_datetime");
		retVal.put("exam-type", "modality");
		retVal.put("thumbnail-url", "thumbnail_url");
		retVal.put("series-desc", "series_desc");
		retVal.put("num-series-in-rel-instances", "num_instances");
		retVal.put("images-in-series", "num_instances");
		retVal.put("png-not-ready", "series_status");

		retVal.put("body-part", "body_part");
		retVal.put("institution", "institution");
		retVal.put("station-name", "station_name");
		retVal.put("department", "department");

		return retVal;
	}

	private static String getStudyUIDFromRequest(String queryString)
	{
		queryString = queryString.toLowerCase();
		int index = queryString.indexOf("&studyuid=");
		String end = queryString.substring(index);
		end = end.replace('=', ' ');
		String[] parts = end.split(" ");
		String key = parts[1].replace('.', '_');
		log.info("key=" + key + ",   queryString=" + queryString);
		return key;
	}

	/**
	 * Look for searchtype=series in the request to determine if it is a series request.
	 * 
	 * @param queryString String
	 * @return boolean
	 */
	private static boolean isSeriesRequest(String queryString)
	{
		String check = queryString.toLowerCase().trim();
		boolean isSeries = check.indexOf("earchtype=series") > 0;

		log.info(" isSeries=" + isSeries + " for: " + queryString);
		return isSeries;
	}

	/**
	 * pwd
	 * 
	 * @param httpRequest HttpServletRequest
	 * @return DicomSearchType
	 */
	private DicomStudySearchType getSearchType(HttpServletRequest httpRequest)
	{
		for (DicomStudySearchType curr : DicomStudySearchType.values()) {

			if ((httpRequest.getParameter(curr.toString()) != null)) {
				return curr;
			}
		}
		log.info("ERROR: Request missing search parameter. req=" + httpRequest.toString());
		throw new IllegalArgumentException("Request missing search parameter. Req=" + httpRequest.toString());
	}

	/**
	 * 
	 * @param searchType DicomSearchType
	 * @param httpRequest HttpServletRequest
	 * @return String
	 */
	@SuppressWarnings("unused")
	private String getSearchParam(DicomStudySearchType searchType, HttpServletRequest httpRequest)
	{
		return httpRequest.getParameter(searchType.toString());
	}

}
