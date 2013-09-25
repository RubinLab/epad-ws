package edu.stanford.isis.epadws.handlers.dicom;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.dicom.DicomStudySearchType;
import edu.stanford.isis.epad.common.dicom.RSeriesData;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;

public class MySQLSearchHandlerJSON extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();
	private static final String MISSING_QUERY_MESSAGE = "No series or study query in request";
	private static final String MISSING_STUDY_SEARCH_TYPE_MESSAGE = "Missing DICOM study search type";
	private static final String QUERY_EXCEPTION_MESSAGE = "Error running query";

	public MySQLSearchHandlerJSON()
	{
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		PrintWriter out = httpResponse.getWriter();
		String result = "";
		httpResponse.setContentType("application/json");
		httpResponse.setStatus(HttpServletResponse.SC_OK);
		request.setHandled(true);

		String queryString = httpRequest.getQueryString();
		queryString = URLDecoder.decode(queryString, "UTF-8");

		log.info("Query from ePad : " + queryString);

		if (queryString != null) {
			queryString = queryString.trim();

			try {
				if (isDICOMSeriesRequest(queryString)) {
					result = performDICOMSeriesSearch(queryString);
				} else {
					DicomStudySearchType searchType = getSearchType(httpRequest);
					if (searchType != null) {
						result = performDICOMStudySearch(searchType, queryString);
					} else {
						log.info(MISSING_STUDY_SEARCH_TYPE_MESSAGE);
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						result = createJSONErrorResponse(MISSING_STUDY_SEARCH_TYPE_MESSAGE);
					}
				}
				httpResponse.setStatus(HttpServletResponse.SC_OK);
			} catch (Exception e) {
				log.warning(QUERY_EXCEPTION_MESSAGE, e);
				httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				result = createJSONErrorResponse(QUERY_EXCEPTION_MESSAGE, e);
			}
		} else {
			log.info(MISSING_QUERY_MESSAGE);
			httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			result = createJSONErrorResponse(MISSING_QUERY_MESSAGE);
		}
		out.append(result);
		out.flush();
	}

	// curl -v -X get "http://<ip>:<port>/search?patientName=*
	private String performDICOMStudySearch(DicomStudySearchType searchType, String queryString) throws Exception
	{
		final MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
		final String[] parts = queryString.split("=");
		final String searchString = parts[1].trim();
		final List<Map<String, String>> searchResult = dbQueries.doStudySearch(searchType.toString(), searchString);
		final StringBuilder result = new StringBuilder();
		boolean isFirst = true;

		log.info("MySqlSearchHandler(handleStudyRequest) = " + searchString);
		log.info("MySql found " + searchResult.size() + " results.");
		log.info("Search result: " + searchResult.toString());

		result.append("{ \"ResultSet\": [");

		for (Map<String, String> row : searchResult) {
			final String studyUID = row.get("study_iuid");
			final String patientName = row.get("pat_name");
			final String patientID = row.get("pat_id");
			final String examType = row.get("modality");
			final String dateAcquired = row.get("study_datetime");
			final String pngStatus = row.get("study_status");
			final String seriesCount = row.get("number_series");
			final String firstSeriesUID = row.get("series_iuid");
			final String firstSeriesDateAcquired = row.get("pps_start");
			final String studyAccessionNumber = row.get("accession_no");
			final String imagesCount = row.get("sum_images");
			final String stuidID = row.get("study_id");
			final String studyDescription = row.get("study_desc");
			final String physicianName = row.get("ref_physician");
			final String birthdate = row.get("pat_birthdate");
			final String sex = row.get("pat_sex");
			final StudySearchResult studySearchResult = new StudySearchResult(studyUID, patientName, patientID, examType,
					dateAcquired, pngStatus, seriesCount, firstSeriesUID, firstSeriesDateAcquired, studyAccessionNumber,
					imagesCount, stuidID, studyDescription, physicianName, birthdate, sex);
			if (!isFirst)
				result.append(",\n");
			isFirst = false;
			result.append(studySearchResult2JSON(studySearchResult));
		}
		result.append("] }");

		return result.toString();
	}

	private String studySearchResult2JSON(StudySearchResult studySearchResult)
	{
		Gson gson = new Gson();

		return gson.toJson(studySearchResult);
	}

	/**
	 * Get all the series for a study.
	 * 
	 * @param queryString String - query string which contains the study id. The query line looks like the following:
	 *          http://[ip:port]/search?searchType=series&studyUID=[studyID].
	 * 
	 *          The values are one line per series.
	 * 
	 *          Here we will look for *.series files within the study directory. If it is there then It will read that
	 *          file and add it to the result.
	 */
	private String performDICOMSeriesSearch(String queryString) throws Exception
	{
		final String studyIdKey = getStudyUIDFromRequest(queryString);
		final String studyUID = DicomFormatUtil.formatDirToUid(studyIdKey);
		final MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
		final List<Map<String, String>> series = dbQueries.doSeriesSearch(studyUID);
		final StringBuilder result = new StringBuilder();
		boolean isFirst = true;

		log.info("Series search column header: " + RSeriesData.getHeaderColumn());
		log.info("dbQueries.doSeriesSearch() had " + series.size() + " results, for studyUID=" + studyUID);

		result.append("{ \"ResultSet\": [");

		for (Map<String, String> row : series) {
			final String seriesID = row.get("series_iuid");
			final String patientID = row.get("pat_id");
			final String patientName = row.get("pat_name");
			final String seriesDate = reformatSeriesDate(row.get("study_datetime"));
			final String examType = row.get("modality");
			final String thumbnailURL = row.get("thumbnail_url");
			final String seriesDescription = row.get("series-desc");
			final int numberOfSeriesRelatedInstances = Integer.parseInt(row.get("num_instances"));
			final int imagesInSeries = Integer.parseInt(row.get("num_instances"));
			final String seriesStatus = row.get("series_status");
			final String bodyPart = row.get("body_part");
			final String institution = row.get("institution");
			final String stationName = row.get("station_name");
			final String department = row.get("department");
			final String accessionNumber = row.get("accession_no");
			final SeriesSearchResult seriesSearchResult = new SeriesSearchResult(seriesID, patientID, patientName,
					seriesDate, examType, thumbnailURL, seriesDescription, numberOfSeriesRelatedInstances, imagesInSeries,
					seriesStatus, bodyPart, institution, stationName, department, accessionNumber);
			if (!isFirst)
				result.append(",\n");
			isFirst = false;
			result.append(seriesSearchResult2JSON(seriesSearchResult));
		}
		result.append("] }");

		return result.toString();
	}

	private String seriesSearchResult2JSON(SeriesSearchResult seriesSearchResult)
	{
		Gson gson = new Gson();

		return gson.toJson(seriesSearchResult);
	}

	/**
	 * The DCM4CHEE MySql table returns the series date in the format: YYYY-MM-DD HH:MM:SS.sss
	 * 
	 * We want it in the format: YYYYMMDD
	 * 
	 * @param seriesDate YYYY-MM-DD HH:MM:SS.sss
	 * @return YYYYMMDD
	 */
	private static String reformatSeriesDate(String seriesDate)
	{
		try {
			if (seriesDate != null) {
				String[] parts = seriesDate.split(" ");
				return cleanString(parts[0], "-").replaceAll(" ", "");
			} else {
				return "00000000";
			}
		} catch (Exception e) {
			log.warning("cleanSeriesDate parse error for: " + seriesDate, e);
		}
		return seriesDate;
	}

	/**
	 * Remove a specific character from a from the string.
	 * 
	 * @param input String
	 * @param removeChar String character to remove
	 * @return String cleaned of character and trimmed.
	 */
	private static String cleanString(String input, String removeChar)
	{
		if (input == null)
			return null;
		if (removeChar == null)
			return input;

		return input.replaceAll(removeChar, " ").trim();
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
	private static boolean isDICOMSeriesRequest(String queryString)
	{
		String check = queryString.toLowerCase().trim();
		boolean isSeries = check.indexOf("earchtype=series") > 0;

		log.info(" isSeries=" + isSeries + " for: " + queryString);
		return isSeries;
	}

	private DicomStudySearchType getSearchType(HttpServletRequest httpRequest)
	{
		for (DicomStudySearchType curr : DicomStudySearchType.values()) {
			log.info("type :[" + curr.toString() + "]");
			log.info("M: " + httpRequest.getParameter(curr.toString()));
			if ((httpRequest.getParameter(curr.toString()) != null)) {
				return curr;
			}
		}
		log.info("ERROR: Request missing search parameter. req=" + httpRequest.toString());
		throw new IllegalArgumentException("Request missing search parameter. Req=" + httpRequest.toString());
	}

	private String createJSONErrorResponse(String errorMessage)
	{
		return "{ \"error\": \"" + errorMessage + "\"}";
	}

	private String createJSONErrorResponse(String errorMessage, Exception e)
	{
		return "{ \"error\": \"" + errorMessage + ": " + e.getMessage() + "\"}";
	}

}
