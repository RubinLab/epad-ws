/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.epad.epadws.epaddb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import edu.stanford.epad.common.util.EPADLogger;

public class DatabaseUtils
{
	private static EPADLogger logger = EPADLogger.getInstance();

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
			}
			return sb.toString();
		} catch (Exception e) {
			return "No debug data because of exception: " + e.getMessage();
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
	
    public static String toSQL(Object value)
    {
        if (value == null) return null;

        if (value instanceof String)
        {
            return "'" + escapeQuote((String)value) + "'";
        }
        else if (value instanceof Collection)
        {
            StringBuffer sqlBuf = new StringBuffer();
            Collection values = (Collection) value;
            String delim = "(";
            for (Object val : values)
            {
                sqlBuf.append(delim);
                sqlBuf.append(toSQL(val));
                delim = ",";
            }
            sqlBuf.append(")");
            return sqlBuf.toString();
        }
        else if (value instanceof Number)
        {
            return value.toString();
        }
        else
            return "'" + value.toString() + "'";
    }

    /**
     * Escapes single quotes
     */
    public static String escapeQuote( String value )
    {
        int ind = value.indexOf("'");
        if (ind == -1)
            return value;
        else if (ind == 0)
            return "''" + escapeQuote(value.substring(ind+1));
        else if (ind == (value.length() - 1))
            return value.substring(0,ind) + "''";
        else
            return value.substring(0,ind) + "''" + escapeQuote(value.substring(ind+1));
    }
	
}
