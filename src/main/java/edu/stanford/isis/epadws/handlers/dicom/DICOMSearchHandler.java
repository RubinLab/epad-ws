package edu.stanford.isis.epadws.handlers.dicom;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;

import edu.stanford.isis.epad.common.dicom.DICOMSeriesSearchResult;
import edu.stanford.isis.epad.common.dicom.DICOMStudySearchResult;
import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.dicom.DicomStudySearchType;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.handlers.HandlerUtil;
import edu.stanford.isis.epadws.persistence.Database;
import edu.stanford.isis.epadws.persistence.DatabaseOperations;
import edu.stanford.isis.epadws.xnat.XNATSessionOperations;

/**
 * <code>
 * curl -v -b JSESSIOND=<id> -X GET "http://<ip>:<port>/epad/searchj?patientName=*
 * curl -v -b JSESSIOND=<id> -X GET "http://<ip>:<port>/epad/searchj?searchType=series&studyUID=[studyID]"
 * </code>
 * 
 * @author martin
 */
public class DICOMSearchHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String MISSING_QUERY_MESSAGE = "No series or study query in DICOM search request";
	private static final String MISSING_STUDY_SEARCH_TYPE_MESSAGE = "Missing DICOM study search type";
	private static final String INTERNAL_EXCEPTION_MESSAGE = "Warning: internal error running query  on DICOM search route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on DICOM search route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("application/json");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String queryString = httpRequest.getQueryString();

				if (queryString != null) {
					queryString = URLDecoder.decode(queryString, "UTF-8");
					queryString = queryString.trim();
					log.info("DICOMSearchHandler query: " + queryString);
					if (isDICOMSeriesRequest(queryString)) {
						performDICOMSeriesSearch(responseStream, queryString);
					} else {
						DicomStudySearchType searchType = getSearchType(httpRequest);
						if (searchType != null) {
							performDICOMStudySearch(responseStream, searchType, queryString);
						} else {
							statusCode = HandlerUtil.infoJSONResponse(HttpServletResponse.SC_BAD_REQUEST,
									MISSING_STUDY_SEARCH_TYPE_MESSAGE, responseStream, log);
						}
					}
					responseStream.flush();
					statusCode = HttpServletResponse.SC_OK;
				} else {
					statusCode = HandlerUtil.infoJSONResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_QUERY_MESSAGE,
							responseStream, log);
				}
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_EXCEPTION_MESSAGE, t, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	/**
	 * <code>
	 * curl -v -X get "http://<ip>:<port>/search?patientName=*
	 * </code>
	 * 
	 * @param searchType
	 * @param queryString
	 */
	private void performDICOMStudySearch(PrintWriter outputStream, DicomStudySearchType searchType, String queryString)
	{
		final DatabaseOperations databaseOperations = Database.getInstance().getDatabaseOperations();
		final String[] parts = queryString.split("=");
		final String searchString = parts[1].trim();
		final List<Map<String, String>> searchResult = databaseOperations.dicomStudySearch(searchType.toString(),
				searchString);
		boolean isFirst = true;

		log.info("DICOMSearchHandler study search found " + searchResult.size() + " result(s).");

		outputStream.append("{ \"ResultSet\": [");

		for (Map<String, String> row : searchResult) {
			final String studyUID = getStringValueFromRow(row, "study_iuid");
			final String patientName = getStringValueFromRow(row, "pat_name");
			final String patientID = getStringValueFromRow(row, "pat_id");
			final String examType = getStringValueFromRow(row, "modality");
			final String dateAcquired = getStringValueFromRow(row, "study_datetime");
			final int studyStatus = getIntegerFromRow(row, "study_status");
			final int seriesCount = getIntegerFromRow(row, "number_series");
			final String firstSeriesUID = getStringValueFromRow(row, "series_iuid");
			final String firstSeriesDateAcquired = getStringValueFromRow(row, "pps_start");
			final String studyAccessionNumber = getStringValueFromRow(row, "accession_no");
			final int imagesCount = getIntegerFromRow(row, "sum_images");
			final String stuidID = getStringValueFromRow(row, "study_id");
			final String studyDescription = getStringValueFromRow(row, "study_desc");
			final String physicianName = getStringValueFromRow(row, "ref_physician");
			final String birthdate = getStringValueFromRow(row, "pat_birthdate");
			final String sex = getStringValueFromRow(row, "pat_sex");
			final DICOMStudySearchResult studySearchResult = new DICOMStudySearchResult(studyUID, patientName, patientID,
					examType, dateAcquired, studyStatus, seriesCount, firstSeriesUID, firstSeriesDateAcquired,
					studyAccessionNumber, imagesCount, stuidID, studyDescription, physicianName, birthdate, sex);
			if (!isFirst)
				outputStream.append(",\n");
			isFirst = false;
			outputStream.append(studySearchResult2JSON(studySearchResult));
		}
		outputStream.append("] }");
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
	private void performDICOMSeriesSearch(PrintWriter outputStream, String queryString)
	{
		final String studyIdKey = getStudyUIDFromRequest(queryString);
		final String studyUID = DicomFormatUtil.formatDirToUid(studyIdKey);
		final DatabaseOperations databaseOperations = Database.getInstance().getDatabaseOperations();
		final List<Map<String, String>> series = databaseOperations.findAllDicomSeriesInStudy(studyUID);
		boolean isFirst = true;

		log.info("DICOMSearchHandler series search found " + series.size() + " result(s) for study " + studyUID);

		outputStream.append("{ \"ResultSet\": [");

		for (Map<String, String> row : series) {
			final String seriesID = getStringValueFromRow(row, "series_iuid");
			final String patientID = getStringValueFromRow(row, "pat_id");
			final String patientName = getStringValueFromRow(row, "pat_name");
			final String seriesDate = reformatSeriesDate(getStringValueFromRow(row, "study_datetime"));
			final String examType = getStringValueFromRow(row, "modality");
			final String thumbnailURL = getStringValueFromRow(row, "thumbnail_url");
			final String seriesDescription = getStringValueFromRow(row, "series_desc");
			final int numberOfSeriesRelatedInstances = Integer.parseInt(getStringValueFromRow(row, "num_instances"));
			final int imagesInSeries = getIntegerFromRow(row, "num_instances");
			final int seriesStatus = getIntegerFromRow(row, "series_status");
			final String bodyPart = getStringValueFromRow(row, "body_part");
			final String institution = getStringValueFromRow(row, "institution");
			final String stationName = getStringValueFromRow(row, "station_name");
			final String department = getStringValueFromRow(row, "department");
			final String accessionNumber = getStringValueFromRow(row, "accession_no");
			final DICOMSeriesSearchResult seriesSearchResult = new DICOMSeriesSearchResult(seriesID, patientID, patientName,
					seriesDate, examType, thumbnailURL, seriesDescription, numberOfSeriesRelatedInstances, imagesInSeries,
					seriesStatus, bodyPart, institution, stationName, department, accessionNumber);
			if (!isFirst)
				outputStream.append(", ");
			isFirst = false;
			outputStream.append(seriesSearchResult2JSON(seriesSearchResult));
		}
		outputStream.append("] }");
	}

	private String seriesSearchResult2JSON(DICOMSeriesSearchResult seriesSearchResult)
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
		// log.info("key=" + key + ",   queryString=" + queryString);
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

		return isSeries;
	}

	private DicomStudySearchType getSearchType(HttpServletRequest httpRequest)
	{
		for (DicomStudySearchType curr : DicomStudySearchType.values()) {
			if ((httpRequest.getParameter(curr.toString()) != null)) {
				return curr;
			}
		}
		log.warning("ERROR: Request missing search parameter. req=" + httpRequest.toString());
		throw new IllegalArgumentException("Request missing search parameter. Req=" + httpRequest.toString());
	}

	private String getStringValueFromRow(Map<String, String> row, String columnName)
	{
		String value = row.get(columnName);

		if (value == null)
			return "";
		else
			return value;
	}

	// TODO Perhaps throw exception rather than returning -1 for missing or erroneous values.
	private int getIntegerFromRow(Map<String, String> row, String columnName)
	{
		String value = row.get(columnName);

		if (value == null)
			return -1;
		else {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				log.warning("expecting integer value in column " + columnName + " got " + value);
				return -1;
			}
		}
	}

	private String studySearchResult2JSON(DICOMStudySearchResult studySearchResult)
	{
		Gson gson = new Gson();

		return gson.toJson(studySearchResult);
	}
}
