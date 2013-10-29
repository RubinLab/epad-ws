/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.mysql;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.stanford.isis.epad.common.dicom.DicomSeriesUID;
import edu.stanford.isis.epad.common.dicom.DicomStudyUID;
import edu.stanford.isis.epad.common.dicom.DicomTagFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.FileKey;
import edu.stanford.isis.epad.common.util.ResourceUtils;

/**
 * Utilities for a database.
 */
public class DbUtils
{

	private static EPADLogger logger = EPADLogger.getInstance();

	private DbUtils()
	{
	}

	public static void close(Connection conn)
	{
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				logger.warning("Failed to close connection", e);
			}
		}
	}

	public static void close(Statement s)
	{
		if (s != null) {
			try {
				s.close();
			} catch (SQLException e) {
				logger.warning("Failed to close statement", e);
			}
		}
	}

	public static void close(PreparedStatement ps)
	{
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				logger.warning("Failed to close prepared statement", e);
			}
		}
	}

	public static void close(ResultSet rs)
	{
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.warning("Failed to close result set", e);
			}
		}
	}

	/**
	 * Get some debug information to debug why an exception occurred.
	 * 
	 * @param rs ResultSet
	 * @return String
	 */
	public static String getDebugData(ResultSet rs)
	{

		try {
			StringBuilder sb = new StringBuilder();

			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			sb.append("#columns = " + colCount);

			for (int i = 0; i < colCount; i++) {
				sb.append(" | ");
				sb.append(rsmd.getColumnName(i + 1));
			}// for
			return sb.toString();

		} catch (Exception e) {
			return "No debug data do to: " + e.getMessage();
		}
	}

	/**
	 * Convert a String value into a sql timestamp.
	 * 
	 * @param currTimeInMs String
	 * @return java.sql.Timestamp
	 */
	public static Timestamp toTimestamp(String currTimeInMs)
	{
		try {
			if ("".equals(currTimeInMs)) {
				return new Timestamp(0);
			}
			return toTimestamp(Long.parseLong(currTimeInMs));
		} catch (Exception e) {
			throw new IllegalArgumentException("Expected a valid time stamp (long), but was _" + currTimeInMs + "_ instead.");
		}
	}

	public static Timestamp toTimestamp(long timeInMs)
	{
		return new Timestamp(timeInMs);
	}

	/**
	 * If the value is null return an empty string.
	 * 
	 * @param optionalValue
	 * @return empty string if null.
	 */
	public static String toOptional(String optionalValue)
	{
		if (optionalValue == null)
			return "";
		return optionalValue;
	}

	/**
	 * Returns a default value if an optional number is null. If the optional value isn't a valid integer log a warning
	 * and return the default value.
	 * 
	 * @param optionalValue String
	 * @param defaultValue int
	 * @return int either the optionalValue parsed as an integer, or the default value.
	 */
	public static int toOptionalInt(String optionalValue, int defaultValue)
	{
		if (optionalValue == null)
			return defaultValue;
		try {
			return Integer.parseInt(optionalValue);
		} catch (NumberFormatException nfe) {
			logger.warning("Invalid number format: value=" + optionalValue, nfe);
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param filepath String
	 * @return String
	 */
	public static String makeCanonicalFilepath(String filepath)
	{
		return FileKey.getCanonicalPath(new File(filepath));
	}

	/**
	 * The date-time format arrives as YYYYMMDD-HHMMSS.mmmmm and needs to be turned into YYYYMMDD to match RSNA format.
	 * 
	 * @param dateTime String in YYYYMMDD-HHMMSS format.
	 * @return String in YYYYMMDD format.
	 */
	public static String formatStudyDateToYYYYMMDDFormat(String dateTime)
	{
		String[] justDate = dateTime.split("-");
		if (justDate[0] != null) {
			// logger.info("formatStudyDateToYYYYMMDDFormat dateTime="+dateTime+" justDate[0]="+justDate[0]);
			return justDate[0];
		}
		return dateTime;
	}

	/**
	 * The date-time format arrives as YYYY-MM-DD HH:MM:SS.mmmmm and needs to be turned into YYYYMMDD to match RSNA
	 * format.
	 * 
	 * @param dateTime String in YYYY-MM-DD HH:MM:SS format.
	 * @return String in YYYYMMDD format.
	 */
	public static String formatMySqlStudyDateToYYYYMMDDFormat(String dateTime)
	{
		try {
			if (dateTime != null) {
				// logger.info("input date to modified = "+dateTime);
				dateTime = dateTime.trim();
				String[] justDate = dateTime.split(" ");
				if (justDate[0] != null) {
					String[] parts = justDate[0].split("-");
					if (parts.length == 3) {
						return parts[0] + parts[1] + parts[2];
					}
					return justDate[0];
				}
			} else {
				dateTime = "00000000";
			}
		} catch (Exception e) {
			logger.warning("MySql didn't parse date: " + dateTime, e);
		}

		return dateTime;
	}

	/**
	 * 
	 * @param tags Map of String to String
	 * @return String
	 */
	public static String makeThumbnailFilePath(Map<String, String> tags)
	{

		DicomSeriesUID seriesUID = new DicomSeriesUID(tags.get(DicomTagFileUtils.SERIES_UID));
		DicomStudyUID studyUID = new DicomStudyUID(tags.get(DicomTagFileUtils.STUDY_UID));

		return ResourceUtils.makeThumbnailFilePath(studyUID, seriesUID);
	}

	/**
	 * 
	 * @param dbValues Map
	 */
	public static void debugInstanceKeys(Map<DbQueries.InstanceKey, String> dbValues)
	{
		DbQueries.InstanceKey[] allKeys = DbQueries.InstanceKey.values();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (DbQueries.InstanceKey key : allKeys) {
			sb.append("{");
			String value = dbValues.get(key);
			sb.append(key).append("=").append(value);
			sb.append("},");
		}
		sb.append("]");

		logger.info("InstanceKey map: " + sb.toString());
	}

	/**
	 * Used for debugging the database.
	 * 
	 * @param name String
	 * @param key int
	 * @param searchValue String
	 */
	public static void logKey(String name, int key, String searchValue)
	{
		logger.info(name + ": " + searchValue + ", pk=" + key);
	}

	/**
	 * Takes multiple database create commands from a single string and turns them into multiple commands. It expects to
	 * see the SQL commands in the following format.
	 * 
	 * 
	 * 
	 * @param content String
	 * @return String[]
	 */
	public static List<String> parseCreateTable(String content)
	{
		List<String> retVal = new ArrayList<String>();
		String[] parts = content.split(";");

		for (String currPart : parts) {
			if (currPart.length() > 0) {
				// retVal.add(currPart.toUpperCase()+";");
				retVal.add(currPart + ";");
				// System.out.println(currPart);
			}
		}// for

		return retVal;
	}

}
