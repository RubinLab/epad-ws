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
	private static final EPADLogger log = EPADLogger.getInstance();

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
			final DCM4CHEEStudy dcm4CheeStudy = new DCM4CHEEStudy(studyUID, patientName, patientID, examType, dateAcquired,
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
	public static DCM4CHEESeriesList seriesSearch(String studyUID)
	{
		final Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		final List<Map<String, String>> series = dcm4CheeDatabaseOperations.findAllDicomSeriesInStudy(studyUID);
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
			dcm4cheeSeriesDescriptionList.addDCM4CHEESeries(dcm4cheeSeriesDescription);
		}
		return dcm4cheeSeriesDescriptionList;
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
