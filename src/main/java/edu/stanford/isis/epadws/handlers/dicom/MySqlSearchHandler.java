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
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;
import edu.stanford.isis.epadws.processing.mysql.MySqlStudyQueryBuilder;
import edu.stanford.isis.epadws.server.RSeriesData;

/**
 * Now handled by Restlet resource {@link SQLServerSearchResource}.
 * 
 * @author amsnyder
 * 
 * @deprecated
 * @see SQLServerSearchResource
 */
@Deprecated
public class MySqlSearchHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();

	private static final String BAD_REQUEST_MESSAGE = "Missing query!";
	private static final String INTERNAL_ERROR_MESSAGE = "Internal ePAD server error";

	public MySqlSearchHandler()
	{
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		PrintWriter out = httpResponse.getWriter();
		String queryString = httpRequest.getQueryString();

		httpResponse.setContentType("text/plain");
		request.setHandled(true);

		log.info("Query from ePad : " + queryString);

		if (queryString != null) {
			queryString = URLDecoder.decode(queryString, "UTF-8");
			queryString = queryString.trim();

			try {
				if (isSeriesRequest(queryString)) {
					handleSeriesRequest(out, queryString);
				} else {
					handleStudyRequest(out, queryString, httpRequest);
				}
				httpResponse.setStatus(HttpServletResponse.SC_OK);
			} catch (Exception e) {
				httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				log.warning(INTERNAL_ERROR_MESSAGE, e);
				out.print(createJSONErrorResponse(INTERNAL_ERROR_MESSAGE, e));
			}
			httpResponse.setStatus(HttpServletResponse.SC_OK);
		} else {
			log.info(BAD_REQUEST_MESSAGE);
			out.write(createJSONErrorResponse(BAD_REQUEST_MESSAGE));
			httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		out.flush();
	}

	private void handleStudyRequest(PrintWriter out, String queryString, HttpServletRequest httpRequest) throws Exception
	{
		final DicomStudySearchType searchType = getSearchType(httpRequest);
		final String[] parts = queryString.split("=");
		final String searchString = parts[1].trim();

		performStudySearch(out, searchType, searchString);
	}

	// curl -v -X get "http://<ip>:<port>/search/?patientName=*"
	private void performStudySearch(PrintWriter out, DicomStudySearchType searchType, String searchString)
			throws Exception
	{
		final MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
		final List<Map<String, String>> searchResult = dbQueries.doStudySearch(searchType.toString(), searchString);
		boolean isFirst = true;

		log.info("MySqlSearchHandler(handleStudyRequest) = " + searchString);
		log.info("MySql found " + searchResult.size() + " results.");
		log.info("Search result: " + searchResult.toString());

		out.append("{ \"ResultSet\": [");

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
				out.append(",\n");
			isFirst = false;
			out.append(studySearchResult2JSON(studySearchResult));
		}
		out.append("] }");
	}

	private String studySearchResult2JSON(StudySearchResult studySearchResult)
	{
		Gson gson = new Gson();

		return gson.toJson(studySearchResult);
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
	private void handleSeriesRequest(PrintWriter out, String queryString) throws Exception
	{
		final String studyIdKey = getStudyUIDFromRequest(queryString);
		final String studyUID = DicomFormatUtil.formatDirToUid(studyIdKey);
		final MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
		final List<Map<String, String>> series = dbQueries.doSeriesSearch(studyUID);
		boolean isFirst = true;

		out.print(RSeriesData.getHeaderColumn() + "\n");
		log.info("Series search column header: " + RSeriesData.getHeaderColumn());
		log.info("dbQueries.doSeriesSearch() had " + series.size() + " results, for studyUID=" + studyUID);

		out.append("{ \"ResultSet\": [");

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
				out.append(",\n");
			isFirst = false;
			out.append(seriesSearchResult2JSON(seriesSearchResult));
		}
		out.append("] }");
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

	/**
	 * @param searchType DicomSearchType The type of search patient name, modality, study date, etc...
	 * @param value String what to search for ...
	 */
	@SuppressWarnings("unused")
	private void debugPrintSQL(DicomStudySearchType searchType, String value)
	{
		MySqlStudyQueryBuilder queryBuilder = new MySqlStudyQueryBuilder(searchType.toString(), value);
		String query = queryBuilder.createStudySearchQuery();
		log.info("SQL: " + query);
	}

	private static String getStudyUIDFromRequest(String queryString)
	{
		queryString = queryString.toLowerCase();
		int index = queryString.indexOf("&studyuid=");
		String end = queryString.substring(index);
		end = end.replace('=', ' ');
		String[] parts = end.split(" ");
		String key = parts[1].replace('.', '_');
		log.info("key=" + key + ", queryString=" + queryString);
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
		boolean isSeries = check.indexOf("searchtype=series") >= 0;

		log.info("isSeries=" + isSeries + " for: " + queryString);
		return isSeries;
	}

	/**
	 * @param httpRequest HttpServletRequest
	 * @return DicomSearchType
	 */
	private DicomStudySearchType getSearchType(HttpServletRequest httpRequest)
	{
		for (DicomStudySearchType curr : DicomStudySearchType.values()) {
			String parameterName = curr.toString();
			if ((httpRequest.getParameter(parameterName) != null)) {
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
