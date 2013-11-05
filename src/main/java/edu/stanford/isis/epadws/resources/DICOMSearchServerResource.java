package edu.stanford.isis.epadws.resources;

import java.util.List;
import java.util.Map;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.resource.Get;

import com.google.gson.Gson;

import edu.stanford.isis.epad.common.dicom.DICOMSeriesSearchResult;
import edu.stanford.isis.epad.common.dicom.DICOMStudySearchResult;
import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.dicom.DicomStudySearchType;
import edu.stanford.isis.epad.common.dicom.RSeriesData;
import edu.stanford.isis.epadws.processing.persistence.MySqlInstance;
import edu.stanford.isis.epadws.processing.persistence.MySqlQueries;

/**
 * Query the database using DICOM series or study search parameters.
 * 
 */
public class DICOMSearchServerResource extends BaseServerResource
{
	private static final String MISSING_QUERY_MESSAGE = "No series or study query in request";
	private static final String MISSING_STUDY_SEARCH_TYPE_MESSAGE = "Missing DICOM study search type";
	private static final String QUERY_EXCEPTION_MESSAGE = "Error running query";

	public DICOMSearchServerResource()
	{
		setNegotiated(false); // Disable content negotiation
	}

	@Override
	protected void doCatch(Throwable throwable)
	{
		log.warning("An exception was thrown in the DICOM search resource.", throwable);
	}

	@Get("text")
	public String query()
	{
		String queryString = getQuery().getQueryString(CharacterSet.UTF_8);
		log.info("Query from ePAD : " + queryString);

		if (queryString != null) {
			queryString = queryString.trim();

			try {
				String result = "";
				if (isDICOMSeriesRequest(queryString)) {
					result = performDICOMSeriesSearch(queryString);
				} else {
					DicomStudySearchType searchType = getDICOMStudySearchType();
					if (searchType != null) {
						result = performDICOMStudySearch(searchType, queryString);
					} else {
						log.info(MISSING_STUDY_SEARCH_TYPE_MESSAGE);
						setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
						return createJSONErrorResponse(MISSING_STUDY_SEARCH_TYPE_MESSAGE);
					}
				}
				setStatus(Status.SUCCESS_OK);
				return result;
			} catch (Exception e) {
				log.warning(QUERY_EXCEPTION_MESSAGE, e);
				setStatus(Status.SERVER_ERROR_INTERNAL);
				return createJSONErrorResponse(QUERY_EXCEPTION_MESSAGE, e);
			}
		} else {
			log.info(MISSING_QUERY_MESSAGE);
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return createJSONErrorResponse(MISSING_QUERY_MESSAGE);
		}
	}

	// curl -v -X get "http://<ip>:<port>/search?searchType=patientName&searchString=%2A

	private String performDICOMStudySearch(DicomStudySearchType searchType, String searchString) throws Exception
	{
		final MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
		final List<Map<String, String>> searchResult = dbQueries.doStudySearch(searchType.toString(), searchString);
		boolean isFirst = true;
		StringBuilder result = new StringBuilder();

		log.info("performDICOMStudySearch = " + searchString);
		log.info("MySql found " + searchResult.size() + " result(s).");

		result.append("{ \"ResultSet\": [");

		for (Map<String, String> row : searchResult) {
			final String studyUID = row.get("study_iuid");
			final String patientName = row.get("pat_name");
			final String patientID = row.get("pat_id");
			final String examType = row.get("modality");
			final String dateAcquired = row.get("study_datetime");
			final int studyStatus = getIntegerFromRow(row, "study_status");
			final int seriesCount = getIntegerFromRow(row, "number_series");
			final String firstSeriesUID = row.get("series_iuid");
			final String firstSeriesDateAcquired = row.get("pps_start");
			final String studyAccessionNumber = row.get("accession_no");
			final int imagesCount = getIntegerFromRow(row, "sum_images");
			final String stuidID = row.get("study_id");
			final String studyDescription = row.get("study_desc");
			final String physicianName = row.get("ref_physician");
			final String birthdate = row.get("pat_birthdate");
			final String sex = row.get("pat_sex");
			final DICOMStudySearchResult studySearchResult = new DICOMStudySearchResult(studyUID, patientName, patientID,
					examType, dateAcquired, studyStatus, seriesCount, firstSeriesUID, firstSeriesDateAcquired,
					studyAccessionNumber, imagesCount, stuidID, studyDescription, physicianName, birthdate, sex);
			if (!isFirst)
				result.append(",\n");
			isFirst = false;
			result.append(studySearchResult2JSON(studySearchResult));
		}
		result.append("] }");

		return result.toString();
	}

	private String studySearchResult2JSON(DICOMStudySearchResult studySearchResult)
	{
		Gson gson = new Gson();

		return gson.toJson(studySearchResult);
	}

	/**
	 * Get all the series for a study.
	 * 
	 * @param searchString String - query string which contains the study id. The query line looks like the following:
	 *          http://[ip:port]/search?searchType=series&studyUID=[studyID].
	 * 
	 *          The values are one line per series.
	 * 
	 *          Here we will look for *.series files within the study directory. If it is there then It will read that
	 *          file and add it to the result.
	 */
	private String performDICOMSeriesSearch(String searchString) throws Exception
	{
		final String studyIdKey = getStudyUIDFromRequest(searchString);
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
			final int numberOfSeriesRelatedInstances = getIntegerFromRow(row, "num_instances");
			final int imagesInSeries = Integer.parseInt(row.get("num_instances"));
			final int seriesStatus = getIntegerFromRow(row, "series_status");
			final String bodyPart = row.get("body_part");
			final String institution = row.get("institution");
			final String stationName = row.get("station_name");
			final String department = row.get("department");
			final String accessionNumber = row.get("accession_no");
			final DICOMSeriesSearchResult seriesSearchResult = new DICOMSeriesSearchResult(seriesID, patientID, patientName,
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

	// TODO Catch exception above
	private int getIntegerFromRow(Map<String, String> row, String columnName)
	{
		String value = row.get(columnName);

		if (value == null)
			return -1;
		else {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				log.info("expecting integer value in column " + columnName + " got " + value);
				return -1;
			}
		}
	}

	private DicomStudySearchType getDICOMStudySearchType()
	{
		for (DicomStudySearchType curr : DicomStudySearchType.values()) {
			if ((getQueryValue(curr.toString()) != null)) {
				return curr;
			}
		}
		return null;
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
