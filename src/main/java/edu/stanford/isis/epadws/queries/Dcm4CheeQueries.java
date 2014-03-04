package edu.stanford.isis.epadws.queries;

import java.util.List;
import java.util.Map;

import edu.stanford.isis.epad.common.dicom.DCM4CHEESeries;
import edu.stanford.isis.epad.common.dicom.DCM4CHEESeriesList;
import edu.stanford.isis.epad.common.dicom.DCM4CHEEStudy;
import edu.stanford.isis.epad.common.dicom.DCM4CHEEStudyList;
import edu.stanford.isis.epad.common.dicom.DCM4CHEEStudySearchType;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.epaddb.EpadDatabase;

/**
 * @author martin
 */
public class Dcm4CheeQueries
{
	private static final EPADLogger log = EPADLogger.getInstance();

	/**
	 * Query the DCM4CHEE database and return a list of study descriptions.
	 * 
	 */
	public static DCM4CHEEStudyList studySearch(DCM4CHEEStudySearchType searchType, String searchValue)
	{
		EpadQueries databaseOperations = EpadDatabase.getInstance().getDatabaseOperations();
		List<Map<String, String>> studySearchResult = databaseOperations.dicomStudySearch(searchType.toString(),
				searchValue);
		DCM4CHEEStudyList dicomStudiesDescription = new DCM4CHEEStudyList();

		for (Map<String, String> row : studySearchResult) {
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
			final DCM4CHEEStudy dicomStudyDescription = new DCM4CHEEStudy(studyUID, patientName, patientID, examType,
					dateAcquired, studyStatus, seriesCount, firstSeriesUID, firstSeriesDateAcquired, studyAccessionNumber,
					imagesCount, stuidID, studyDescription, physicianName, birthdate, sex);
			dicomStudiesDescription.addDICOMStudyDescription(dicomStudyDescription);
		}
		return dicomStudiesDescription;
	}

	/**
	 * Get all the series for a study from DCM4CHEE.
	 * 
	 * @param queryString String - query string which contains the study id. The query line looks like the following:
	 *          http://[ip:port]/search?searchType=series&studyUID=[studyID].
	 * 
	 */
	public static DCM4CHEESeriesList seriesSearch(String studyUID)
	{
		final EpadQueries databaseOperations = EpadDatabase.getInstance().getDatabaseOperations();
		final List<Map<String, String>> series = databaseOperations.findAllDicomSeriesInStudy(studyUID);
		DCM4CHEESeriesList dcm4cheeSeriesDescriptionList = new DCM4CHEESeriesList();

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
			final DCM4CHEESeries dcm4cheeSeriesDescription = new DCM4CHEESeries(seriesID, patientID, patientName, seriesDate,
					examType, thumbnailURL, seriesDescription, numberOfSeriesRelatedInstances, imagesInSeries, seriesStatus,
					bodyPart, institution, stationName, department, accessionNumber);
			dcm4cheeSeriesDescriptionList.addDCM4CHEESeriesDescription(dcm4cheeSeriesDescription);
		}
		return dcm4cheeSeriesDescriptionList;
	}

	private static String getStringValueFromRow(Map<String, String> row, String columnName)
	{
		String value = row.get(columnName);

		if (value == null)
			return "";
		else
			return value;
	}

	// TODO Perhaps throw exception rather than returning -1 for missing or erroneous values.
	private static int getIntegerFromRow(Map<String, String> row, String columnName)
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
}
