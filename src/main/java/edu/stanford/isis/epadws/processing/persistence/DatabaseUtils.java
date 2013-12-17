/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import edu.stanford.isis.epad.common.util.EPADLogger;

public class DatabaseUtils
{
	private static EPADLogger logger = EPADLogger.getInstance();

	private DatabaseUtils()
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
}
