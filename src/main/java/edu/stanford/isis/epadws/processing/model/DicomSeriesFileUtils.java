/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.dicom.DicomTagFileUtils;
import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.EPADResources;

/**
 * The central location for reading and writing series files.
 */
public class DicomSeriesFileUtils
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private DicomSeriesFileUtils()
	{
	}

	public static final String SERIES_ID = "Series Id";
	public static final String PATIENT_ID = "Patient Id";
	public static final String PATIENT_NAME = "Patient Name";
	public static final String SERIES_DATE = "Series Date";
	public static final String EXAM_TYPE = "Exam Type";
	public static final String THUMBNAIL_URL = "Thumbnail URL";
	public static final String SERIES_DESC = "SeriesDescription";
	public static final String NUM_SERIES_REL_INSTANCES = "NumberOfSeriesRelatedInstances";
	public static final String IMAGES_IN_SERIES = "ImagesInSeries";

	/**
	 * Read a series file and return a Map of the values.
	 * 
	 * @param seriesFilePath String path to order file
	 * @return Map of String to String. The keys are SeriesFileUtils. values.
	 * @throws IOException if problem reading file.
	 */
	public static Map<String, String> readSeriesFile(String seriesFilePath) throws IOException
	{
		if (!seriesFilePath.endsWith(".series")) {
			throw new IllegalArgumentException("This is not an series file: " + seriesFilePath);
		}
		File seriesFile = new File(seriesFilePath);
		if (!seriesFile.exists()) {
			throw new IllegalStateException("This series file does not exist: " + seriesFile.getAbsolutePath());
		}
		log.info("Reading series file: " + seriesFile.getAbsolutePath());

		List<Map<String, String>> retVal = EPADFileUtils.readCsvFormattedFile(seriesFile);

		log.info("series file map #entires=" + retVal.get(0).size());

		return retVal.get(0);
	}

	/**
	 * Write a series file using a MAP with keys from the SeriesFileUtils.
	 * 
	 * @param seriesFilePath String file path
	 * @param seriesFileTags Map of String to String using Series File Tags.
	 */
	public static void writeSeriesFile(String seriesFilePath, Map<String, String> seriesFileTags)
	{
		// Verify the file map by looking for required tag not in the DICOM tags..
		if (seriesFileTags.get(DicomSeriesFileUtils.IMAGES_IN_SERIES) == null) {
			throw new IllegalArgumentException("Tag map is wrong version. It should use SeriesFileUtils keys.");
		}
		String content = createSeriesFileContent(seriesFileTags);
		if (content.indexOf("null") > 0) {
			log.info("BAD FILE: \n" + content);
			log.info(seriesFileTags.toString());
		}
		EPADFileUtils.overwrite(new File(seriesFilePath), content);
	}

	/**
	 * Uses "Series File Keys" to create content.
	 * 
	 * @param mapSeriesFileTags Map
	 * @return String
	 */
	public static String createSeriesFileContent(Map<String, String> mapSeriesFileTags)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(writeKeys());

		sb.append(getEitherTagType(mapSeriesFileTags, DicomTagFileUtils.SERIES_UID, SERIES_ID, "unknown")).append(",");
		sb.append(getEitherTagType(mapSeriesFileTags, DicomTagFileUtils.PATIENT_ID, PATIENT_ID, "unknown")).append(",");// PATIENT_ID
		sb.append(getEitherTagType(mapSeriesFileTags, DicomTagFileUtils.PATIENT_NAME, PATIENT_NAME, "unknown")).append(",");// PATIENT_NAME
		sb.append(getEitherTagType(mapSeriesFileTags, DicomTagFileUtils.STUDY_DATE, SERIES_DATE, "unknown")).append(",");// SERIES_DATE
		sb.append(getEitherTagType(mapSeriesFileTags, DicomTagFileUtils.MODALITY, EXAM_TYPE, "unknown")).append(",");// EXAM_TYPE
		sb.append(getEitherTagType(mapSeriesFileTags, THUMBNAIL_URL, THUMBNAIL_URL, "unknown")).append(",");// THUMBNAIL_URL
		sb.append(getEitherTagType(mapSeriesFileTags, DicomTagFileUtils.SERIES_DESCRIPTION, SERIES_DESC, "unknown"))
				.append(",");
		sb.append(getEitherTagType(mapSeriesFileTags, NUM_SERIES_REL_INSTANCES, NUM_SERIES_REL_INSTANCES, "unknown"))
				.append(",");
		sb.append(getEitherTagType(mapSeriesFileTags, IMAGES_IN_SERIES, IMAGES_IN_SERIES, "unknown"));

		return sb.toString();
	}

	private static String getEitherTagType(Map<String, String> tagMapUnknownType, String dicomTag, String seriesTag,
			String fallback)
	{
		String dicom = tagMapUnknownType.get(dicomTag);
		String series = tagMapUnknownType.get(seriesTag);
		if (series != null) {
			return series;
		} else if (dicom != null) {
			return dicom;
		} else {
			log.warning("Returning fallback value for tag: " + seriesTag, new Exception("unknown tag"));
			return fallback;
		}

	}

	/**
	 * Using the TAGS for a DICOM file create the series file.
	 * 
	 * @param mapDicomTags Map of String to String. The keys are DICOM file tags names, NOT the keys in a *.series file.
	 * @return String contents of the series file built from a Map based on DICOM tags.
	 */
	public static String createSeriesFileContentFromDicomTags(Map<String, String> mapDicomTags)
	{

		StringBuilder sb = new StringBuilder();
		sb.append(writeKeys());

		String seriesUID = mapDicomTags.get(DicomTagFileUtils.SERIES_UID);
		String studyUID = mapDicomTags.get(DicomTagFileUtils.STUDY_UID);

		sb.append(seriesUID).append(","); // SERIES_ID
		sb.append(mapDicomTags.get(DicomTagFileUtils.PATIENT_ID)).append(","); // PATIENT_ID
		sb.append(mapDicomTags.get(DicomTagFileUtils.PATIENT_NAME)).append(","); // PATIENT_NAME
		sb.append(mapDicomTags.get(DicomTagFileUtils.STUDY_DATE)).append(","); // SERIES_DATE
		sb.append(mapDicomTags.get(DicomTagFileUtils.MODALITY)).append(","); // EXAM_TYPE

		String thumbnailURL = ThumbnailFileUtil.createThumbnailPath(studyUID, seriesUID);
		sb.append(thumbnailURL).append(","); // THUMBNAIL_URL

		sb.append(mapDicomTags.get(DicomTagFileUtils.SERIES_DESCRIPTION)).append(","); // SERIES_DESC
		sb.append(useDefaultIfNullTag(mapDicomTags, DicomTagFileUtils.SERIES_NUMBER, "-1")).append(","); // NUM_SERIES_REL_INSTANCES

		// IMPORTANT NOTE: The "# of images in series" is not known by any TAG in the dicom file.
		// This is determined by a call about the series to a Dicom-Server which isn't an option when
		// uploading files. This number will be inferred by the number of DICOM or TAG files in the directory
		// at the same time as the "order" file is being created. The value here will be set low since the
		// series file is written which a file is first moved.

		sb.append("1"); // # images in series.

		logTagType(mapDicomTags);

		return sb.toString();
	}// createSeriesFileContentFromDicomTags

	private static void logTagType(Map<String, String> tagMap)
	{
		if (tagMap.containsKey(EXAM_TYPE)) {
			log.info("Tag type is seriesTag");
		} else if (tagMap.containsKey(DicomTagFileUtils.MODALITY)) {
			log.info("Tag type is dicomTag");
		} else {
			log.info("Tag type is unknown.");
		}
	}

	/**
	 * If this tag is not found use the default value.
	 * 
	 * @param map Map
	 * @param defaultValue String
	 * @param tagName String
	 * @return String
	 */
	private static String useDefaultIfNullTag(Map<String, String> map, String tagName, String defaultValue)
	{
		String tagValue = map.get(tagName);
		return !isEmptyString(tagValue) ? tagValue : defaultValue;
	}// useDefaultIfNullTag

	/**
	 * Check to see if this value is either null or an empty string.
	 * 
	 * @param str String
	 * @return boolean
	 */
	private static boolean isEmptyString(String str)
	{
		return str == null || "".equals(str);
	}

	public static String writeKeys()
	{

		EPADConfig config = EPADConfig.getInstance();
		String separator = config.getParam("fieldSeparator");

		StringBuilder sb = new StringBuilder();
		sb.append(SERIES_ID).append(separator);
		sb.append(PATIENT_ID).append(separator);
		sb.append(PATIENT_NAME).append(separator);
		sb.append(SERIES_DATE).append(separator);
		sb.append(EXAM_TYPE).append(separator);
		sb.append(THUMBNAIL_URL).append(separator);
		sb.append(SERIES_DESC).append(separator);
		sb.append(NUM_SERIES_REL_INSTANCES).append(separator);
		sb.append(IMAGES_IN_SERIES).append("\n");
		return sb.toString();
	}

	/**
	 * Parse all the *.series files in a directory. It reads the series files into a generic map, so they can be written
	 * out. A Map is used so additions or changes to the files with minimal changes in other places.
	 * 
	 * 
	 * @param seriesFiles List of series files from one directory.
	 * @return String The results of a series file.
	 */
	public static String parseSeriesFiles(List<File> seriesFiles)
	{

		StringBuilder sb = new StringBuilder();
		sb.append(writeKeys());

		EPADConfig config = EPADConfig.getInstance();
		String separator = config.getParam("fieldSeparator");

		try {
			for (File seriesFile : seriesFiles) {
				List<Map<String, String>> values = EPADFileUtils.readCsvFormattedFile(seriesFile);

				for (Map<String, String> valueMap : values) {
					sb.append(valueMap.get(SERIES_ID)).append(separator);
					sb.append(valueMap.get(PATIENT_ID)).append(separator);
					sb.append(valueMap.get(PATIENT_NAME)).append(separator);
					sb.append(valueMap.get(SERIES_DATE)).append(separator);
					sb.append(valueMap.get(EXAM_TYPE)).append(separator);
					sb.append(valueMap.get(THUMBNAIL_URL)).append(separator);
					sb.append(valueMap.get(SERIES_DESC)).append(separator);
					sb.append(valueMap.get(NUM_SERIES_REL_INSTANCES)).append(separator);
					sb.append(valueMap.get(IMAGES_IN_SERIES)).append("\n");

					// Debug - RSNA Search.
					if (valueMap.get(PATIENT_NAME) == null) {
						log.info("Does this file have a null value? :" + seriesFile.getAbsoluteFile());
						log.info("* valueMap: " + valueMap.toString());
					}// if

				}// for
			}// for
		} catch (Exception e) {
			log.warning("parseSeriesFile", e);
		}
		return sb.toString();
	}

	/**
	 * Look in the /resources/[studyId] directory for any file [seriesID].series
	 * 
	 * @param studyId String Can be in either 1.2.840... format or 1_2_840...
	 * @return List of File that are type *.series
	 */
	public static List<File> getSeriesFileFor(String studyId)
	{
		List<File> retVal = new ArrayList<File>();
		try {
			String studyDirName = DicomFormatUtil.formatUidToDir(studyId);
			File studyDir = new File(EPADResources.getEPADWebServerPNGDir() + studyDirName);
			if (!studyDir.exists()) {
				log.info("WARNING: Could not find directory for: " + studyId + ". Dir doesn't exist. dir="
						+ studyDir.getCanonicalPath());
				return retVal;
			}
			retVal = EPADFileUtils.getAllFilesWithExtension(studyDir, ".series");
		} catch (Exception e) {
			log.warning("getSeriesFileFor", e);
		}
		return retVal;
	}// getSeriesFileFor

}
