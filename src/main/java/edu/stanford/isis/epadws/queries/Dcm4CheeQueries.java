package edu.stanford.isis.epadws.queries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import edu.stanford.epad.dtos.DCM4CHEESeries;
import edu.stanford.epad.dtos.DCM4CHEESeriesList;
import edu.stanford.epad.dtos.DCM4CHEEStudy;
import edu.stanford.epad.dtos.DCM4CHEEStudyList;
import edu.stanford.epad.dtos.DCM4CHEEStudySearchType;
import edu.stanford.epad.dtos.DICOMElement;
import edu.stanford.epad.dtos.DICOMElementList;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.EPADTools;
import edu.stanford.isis.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.isis.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.isis.epadws.processing.pipeline.task.DicomHeadersTask;

/**
 * @author martin
 */
public class Dcm4CheeQueries
{
	private static EPADLogger log = EPADLogger.getInstance();

	/**
	 * Query the DCM4CHEE database and return a list of study descriptions.
	 * <p>
	 * The {@link DCM4CHEEStudySearchType} specified the search type, e.g, patientName, patientID.
	 * 
	 */
	public static DCM4CHEEStudyList studySearch(DCM4CHEEStudySearchType searchType, String searchValue)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		List<Map<String, String>> studySearchResult = dcm4CheeDatabaseOperations.dicomStudySearch(searchType.toString(),
				searchValue);
		DCM4CHEEStudyList dcm4CheeStudyList = new DCM4CHEEStudyList();

		for (Map<String, String> dcm4CheeStudyData : studySearchResult) {
			String studyUID = getStringValueFromRow(dcm4CheeStudyData, "study_iuid");
			String patientName = getStringValueFromRow(dcm4CheeStudyData, "pat_name");
			String patientID = getStringValueFromRow(dcm4CheeStudyData, "pat_id");
			String examType = getStringValueFromRow(dcm4CheeStudyData, "modality");
			String dateAcquired = getStringValueFromRow(dcm4CheeStudyData, "study_datetime");
			int studyStatus = getIntegerFromRow(dcm4CheeStudyData, "study_status");
			int seriesCount = getIntegerFromRow(dcm4CheeStudyData, "number_series");
			String firstSeriesUID = getStringValueFromRow(dcm4CheeStudyData, "series_iuid");
			String firstSeriesDateAcquired = getStringValueFromRow(dcm4CheeStudyData, "pps_start");
			String studyAccessionNumber = getStringValueFromRow(dcm4CheeStudyData, "accession_no");
			int imagesCount = getIntegerFromRow(dcm4CheeStudyData, "sum_images");
			String stuidID = getStringValueFromRow(dcm4CheeStudyData, "study_id");
			String studyDescription = getStringValueFromRow(dcm4CheeStudyData, "study_desc");
			String physicianName = getStringValueFromRow(dcm4CheeStudyData, "ref_physician");
			String birthdate = getStringValueFromRow(dcm4CheeStudyData, "pat_birthdate");
			String sex = getStringValueFromRow(dcm4CheeStudyData, "pat_sex");
			DCM4CHEEStudy dcm4CheeStudy = new DCM4CHEEStudy(studyUID, patientName, patientID, examType, dateAcquired,
					studyStatus, seriesCount, firstSeriesUID, firstSeriesDateAcquired, studyAccessionNumber, imagesCount,
					stuidID, studyDescription, physicianName, birthdate, sex);
			dcm4CheeStudyList.addDCM4CHEEStudy(dcm4CheeStudy);
		}
		return dcm4CheeStudyList;
	}

	/**
	 * Get all the series for a study from DCM4CHEE.
	 * 
	 * @param queryString String - query string which contains the study id. The query line looks like the following:
	 *          http://[ip:port]/search?searchType=series&studyUID=[studyID].
	 * 
	 */
	public static DCM4CHEESeriesList getSeriesInStudy(String studyUID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		List<Map<String, String>> series = dcm4CheeDatabaseOperations.findAllDicomSeriesInStudy(studyUID);
		DCM4CHEESeriesList dcm4cheeSeriesList = new DCM4CHEESeriesList();

		for (Map<String, String> dcm4CheeSeriesData : series) {
			DCM4CHEESeries dcm4cheeSeries = extractDCM4CHEESeries(dcm4CheeSeriesData);
			dcm4cheeSeriesList.addDCM4CHEESeries(dcm4cheeSeries);
		}
		return dcm4cheeSeriesList;
	}

	public static DCM4CHEESeries getSeriesWithUID(String seriesUID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		Map<String, String> dcm4CheeSeriesData = dcm4CheeDatabaseOperations.getDcm4CheeSeriesDataWithUID(seriesUID);

		DCM4CHEESeries dcm4cheeSeries = extractDCM4CHEESeries(dcm4CheeSeriesData);
		return dcm4cheeSeries;
	}

	private static DCM4CHEESeries extractDCM4CHEESeries(Map<String, String> dcm4CheeSeriesData)
	{
		String studyUID = getStringValueFromRow(dcm4CheeSeriesData, "study_iuid");
		String seriesUID = getStringValueFromRow(dcm4CheeSeriesData, "series_iuid");
		String patientID = getStringValueFromRow(dcm4CheeSeriesData, "pat_id");
		String patientName = getStringValueFromRow(dcm4CheeSeriesData, "pat_name");
		String seriesDate = reformatSeriesDate(getStringValueFromRow(dcm4CheeSeriesData, "study_datetime"));
		String examType = getStringValueFromRow(dcm4CheeSeriesData, "modality");
		String thumbnailURL = getStringValueFromRow(dcm4CheeSeriesData, "thumbnail_url");
		String seriesDescription = getStringValueFromRow(dcm4CheeSeriesData, "series_desc");
		int numberOfSeriesRelatedInstances = Integer.parseInt(getStringValueFromRow(dcm4CheeSeriesData, "num_instances"));
		int imagesInSeries = getIntegerFromRow(dcm4CheeSeriesData, "num_instances");
		int seriesStatus = getIntegerFromRow(dcm4CheeSeriesData, "series_status");
		String bodyPart = getStringValueFromRow(dcm4CheeSeriesData, "body_part");
		String institution = getStringValueFromRow(dcm4CheeSeriesData, "institution");
		String stationName = getStringValueFromRow(dcm4CheeSeriesData, "station_name");
		String department = getStringValueFromRow(dcm4CheeSeriesData, "department");
		String accessionNumber = getStringValueFromRow(dcm4CheeSeriesData, "accession_no");
		DCM4CHEESeries dcm4cheeSeries = new DCM4CHEESeries(studyUID, seriesUID, patientID, patientName, seriesDate,
				examType, thumbnailURL, seriesDescription, numberOfSeriesRelatedInstances, imagesInSeries, seriesStatus,
				bodyPart, institution, stationName, department, accessionNumber);
		return dcm4cheeSeries;

	}

	public static DICOMElementList getDICOMElementsFromWADO(String studyUID, String seriesUID, String imageUID)
	{
		DICOMElementList dicomElementList = new DICOMElementList();

		try {
			File tempDICOMFile = File.createTempFile(imageUID, ".tmp");
			int wadoStatusCode = EPADTools.downloadDICOMFileFromWADO(tempDICOMFile, studyUID, seriesUID, imageUID);
			if (wadoStatusCode == HttpServletResponse.SC_OK) {
				File tempTag = File.createTempFile(imageUID, "_tag.tmp");
				ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
				taskExecutor.execute(new DicomHeadersTask(tempDICOMFile, tempTag));
				taskExecutor.shutdown();
				try {
					taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
					BufferedReader tagReader = null;
					try {
						String dicomElementString;
						tagReader = new BufferedReader(new FileReader(tempTag.getAbsolutePath()));

						while ((dicomElementString = tagReader.readLine()) != null) {
							DICOMElement dicomElement = decodeDICOMElementString(dicomElementString);
							if (dicomElement != null) {
								dicomElementList.addDICOMElement(dicomElement);
							} else {
								// log.warning("Warning: could not decode DICOM element " + dicomElementString + "; skipping");
							}
						}
					} finally {
						if (tagReader != null) {
							try {
								tagReader.close();
							} catch (IOException e) {
								log.warning("Error closing DICOM tag response stream", e);
							}
						}
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					log.warning("DICOM headers task interrupted!");
				}
			} else {
				log.warning("Error invoking WADO to get DICOM headers; status code=" + wadoStatusCode);
			}
		} catch (IOException e) {
			log.warning("IOException retrieving DICOM headers", e);
		}
		return dicomElementList;
	}

	// TODO This code is very brittle. Rewrite to make more robust. Also ignores DICOM sequences.
	private static DICOMElement decodeDICOMElementString(String dicomElement)
	{
		String[] fields = dicomElement.split(" ");

		int valueFieldStartIndex = valueFieldStartIndex(fields);
		if (valueFieldStartIndex != -1) {
			int valueFieldEndIndex = valueFieldEndIndex(fields);

			if (valueFieldEndIndex != -1 && ((valueFieldEndIndex - valueFieldStartIndex) < 10)) {
				String tagCode = extractTagCodeFromField(fields[0]);
				String value = stripBraces(assembleValue(fields, valueFieldStartIndex, valueFieldEndIndex));
				String tagName = assembleValue(fields, valueFieldEndIndex + 1, fields.length - 1);

				return new DICOMElement(tagCode, tagName, value);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private static String extractTagCodeFromField(String field)
	{
		String subFields[] = field.split(":>*");

		if (subFields.length == 2) {
			return subFields[1];
		} else
			return "";
	}

	private static String assembleValue(String[] fields, int startIndex, int finishIndex)
	{
		String value = "";
		for (int i = startIndex; i <= finishIndex; i++) {
			if (i > startIndex)
				value += " ";
			value += fields[i];
		}
		return value;
	}

	private static int valueFieldStartIndex(String[] fields)
	{
		for (int i = 0; i < fields.length; i++)
			if (fields[i].startsWith("["))
				return i;
		return -1;
	}

	private static int valueFieldEndIndex(String[] fields)
	{
		for (int i = 0; i < fields.length; i++)
			if (fields[i].endsWith("]"))
				return i;
		return -1;
	}

	private static String stripBraces(String valueField)
	{
		if (valueField.startsWith("[") && valueField.endsWith("]")) {
			return valueField.substring(1, valueField.length() - 1);
		} else {
			return "";
		}
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
