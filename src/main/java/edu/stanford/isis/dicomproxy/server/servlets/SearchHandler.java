package edu.stanford.isis.dicomproxy.server.servlets;
import java.io.IOException;
import edu.stanford.isis.dicomproxy.db.mysql.MySqlInstance;

import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Set;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
// import java.sql.ResultSetMetaData;
// import java.sql.DatabaseMetaData;
// import java.sql.Statement;
import java.sql.PreparedStatement;

import edu.stanford.isis.dicomproxy.handlers.dicom.RsnaSearchHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import edu.stanford.aim.proxy.helpers.JsonHelper;

/**
 * Modified servlet for handling searches and tests.
 * <p>The original version of the search had the browser calling the 
 *    /search.  The browser now calls the url /epad/rest/search instead
 *    of the url /search, and a proxy reroutes the request 
 *    back to /search.</p>
 * <p>I am running into problems with the system stating that the object
 *    is already closed.  This is presumably the connection object.  It
 *    may be that since this was a pool of connections, closed
 *    connections were not replaced.</p>
 * @author Bradley Ross
 * @see RsnaSearchHandler
 */
@SuppressWarnings("serial")
public class SearchHandler extends HttpServlet
{
	protected Logger debugLogger = LoggerFactory.getLogger(this.getClass());
	/**
	 * This was the header for the list of series as a CSV file.
	 */
	protected String  headerSeries = "Series Id,Patient Id,Patient Name,Series Date,Exam Type," + 
			"Thumbnail URL,SeriesDescription,NumberOfSeriesRelatedInstances,ImagesInSeries";
	/**
	 * This was the header for the list of studies as a CSV file.
	 */
	protected String  headerStudy = "StudyUID, Patient Name, Patient ID, Exam Type, Date Acquired";
	/**
	 * SQL statement getting list of studies.
	 * @see studyQueryAllStmt
	 */
	protected String studyQueryAll = "SELECT PATIENT.PAT_ID, PATIENT.PAT_ID_ISSUER, PATIENT.PAT_NAME, " +
			"PATIENT.PAT_BIRTHDATE, PATIENT.PAT_SEX, STUDY.STUDY_UID, STUDY.STUDY_ID, STUDY.STUDY_DESC, " +
			"STUDY.STUDY_DATETIME, STUDY.ACCESSION_NO, STUDY.REF_PHYSICIAN " +
			"FROM STUDY, PATIENT WHERE PATIENT.PK = STUDY.PATIENT_FK";
	/**
	 * Prepared statement generated from {@link #studyQueryAll}.
	 */
	protected PreparedStatement studyQueryAllStmt = null;
	/**
	 * SQL statement getting list of series.
	 * @see seriesQueryStudyStmt
	 */
	protected String seriesQueryStudy = "SELECT PATIENT.PAT_ID, PATIENT.PAT_ID_ISSUER, PATIENT.PAT_NAME, " +
			"PATIENT.PAT_BIRTHDATE, PATIENT.PAT_SEX, STUDY.STUDY_UID, STUDY.STUDY_ID, STUDY.STUDY_DESC, " +
			"STUDY.STUDY_DATETIME, STUDY.ACCESSION_NO, STUDY.REF_PHYSICIAN, " +
			"SERIES.SERIES_IUID, SERIES.SERIES_NO, SERIES.MODALITY, SERIES.BODY_PART, " +
			"SERIES.LATERALITY, SERIES.SERIES_DESC, SERIES.INSTITUTION,  " +
			"SERIES.STATION_NAME, SERIES.DEPARTMENT, SERIES.PERF_PHYSICISAN " +
			"FROM STUDY, PATIENT, SERIES WHERE PATIENT.PK = STUDY.PATIENT_FK AND " +
			"SERIES.STUDY_FK = STUDY.PK AND STUDY.STUDY_UID = ? ";
	/**
	 * Prepared statement generated from {@link seriesQueryStudy}.
	 */
	protected PreparedStatement seriesQueryStudyStmt = null;
	/**
	 * SQL statement counting rows in ISSUER table.
	 * @see issuerCountStmt
	 */
	protected String issuerCount = "SELECT COUNT(*) AS COUNTER FROM ISSUER";
	/**
	 * Prepared statement generated from {@link #issuerCount}.
	 */
	protected PreparedStatement issuerCountStmt = null;
	/**
	 * SQL statement counting rows in PATIENT table.
	 * @see patientCountStmt
	 */
	protected String patientCount = "SELECT COUNT(*) AS COUNTER FROM PATIENT";
	/**
	 * Prepared statement generated from {@link #patientCount}.
	 */
	protected PreparedStatement patientCountStmt = null;
	/**
	 * SQL statement counting rows in STUDY table.
	 * @see studyCountStmt
	 */
	protected String studyCount = "SELECT COUNT(*) AS COUNTER FROM STUDY";
	/**
	 * Prepared statement generated from {@link #studyCount}.
	 */
	protected PreparedStatement studyCountStmt = null;
	/**
	 * SQL statement counting rows in SERIES table.
	 * @see seriesCountStmt
	 */
	protected String seriesCount = "SELECT COUNT(*) AS COUNTER FROM SERIES";
	/**
	 * Prepared statement generated from {@link #seriesCount}.
	 */
	protected PreparedStatement seriesCountStmt = null;
	/**
	 * SQL statement counting rows in INSTANCE table.
	 * @see instanceCountStmt
	 */
	protected String instanceCount = "SELECT COUNT(*) AS COUNTER FROM INSTANCE";
	/**
	 * Prepared statement generated from {@link #instanceCount}.
	 */
	protected PreparedStatement instanceCountStmt = null;
	/*
	 * Database instance.
	 */
	MySqlInstance db = null;
	/*
	 * Database connection.
	 */
	Connection conn = null;
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
		db = MySqlInstance.getInstance();
		conn = db.getConnection();
		} catch (SQLException e) {
			throw new ServletException ("Unable to get database connection", e );
		}
	}
	@Override
	public void destroy() {
		super.destroy();
		try {
		conn.close(); 
		} catch (SQLException e) {
			debugLogger.error("Unable close database connection", e );
		}
	}
	@Override
	public void service (HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
			{
		/*
		 * Type of search to be carried out.
		 */
		String searchType = null;
		/*
		 * String to be searched for in requests for lists of studies.
		 */
		String searchString = null;
		/*
		 * Study instance UID for searches for lists of series.
		 */
		String studyUid = null; 

		debugLogger.info("Processing SQL with " + request.getQueryString());
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		searchType = request.getParameter("searchtype");
		searchString = request.getParameter("searchstring");
		studyUid = request.getParameter("studyUID");
		if (searchType == null) {
			searchType = "study";
		} else if (searchType.trim().length() == 0) {
			searchType = "study";
		}
		List<Map<String,String>> itemList = null;
		ResultSet rs = null;
		try {
			if (conn == null) {
				debugLogger.error("Value of jdbc connection was null");
			} else if (conn.isClosed()) {
				debugLogger.error("JDBC connection was closed");
			}
			if (searchType.trim().equalsIgnoreCase("study")) {
				debugLogger.info("Executing search for studies");
				if (studyQueryAllStmt == null) {
					studyQueryAllStmt = conn.prepareStatement(studyQueryAll);
				} else if (studyQueryAllStmt.isClosed()) {
					studyQueryAllStmt = conn.prepareStatement(studyQueryAll);
				}
				itemList = new ArrayList<Map<String,String>>();
				/*
				 * This is where I am getting a message that the object is already closed.
				 */
				rs = studyQueryAllStmt.executeQuery();
				while (rs.next()) {
					Map<String,String> item = new HashMap<String,String>();
					item.put("StudyInstanceUID", rs.getString("STUDY_UID"));
					item.put("PatientName", rs.getString("PAT_NAME"));
					item.put("PatientID", rs.getString("PAT_ID"));
					itemList.add(item);
				}
				StringBuffer message = new StringBuffer();
				if (searchType != null) {
					message.append(" searchType=" + searchType.trim() + " ");
				}
				if (searchString != null) {
					message.append(" searchString=" + searchString.trim() + " ");
				}
				printList(itemList, out, message.toString());
			} else if (searchType.trim().equalsIgnoreCase("series")) {
				debugLogger.info("Executing search for series");
				if (seriesQueryStudyStmt == null) {
					seriesQueryStudyStmt = conn.prepareStatement(seriesQueryStudy);
				} else if (seriesQueryStudyStmt.isClosed()) {
					seriesQueryStudyStmt = conn.prepareStatement(seriesQueryStudy);
				}
				if (studyUid == null) {
					out.println("{\"result\":\"failure\", \"message\":\"illegal option\" }");
					out.close();
					return;
				} else if (studyUid.trim().length() == 0) {
					out.println("{\"result\":\"failure\", \"message\":\"illegal option\" }");
					out.close();
					return;
				} else {
					itemList = new ArrayList<Map<String,String>>();
					seriesQueryStudyStmt.setString(1, studyUid);
					rs = seriesQueryStudyStmt.executeQuery();
					while (rs.next()) {
						Map<String,String> item = new HashMap<String,String>();
						item.put("StudyInstanceUID", rs.getString("STUDY_UID"));
						item.put("SeriesInstanceUID", rs.getString("SERIES_IUID"));
						item.put("PatientName", rs.getString("PAT_NAME"));
						item.put("SeriesDescription", rs.getString("SERIES_DESC"));
						itemList.add(item);
					}
				}
				StringBuffer message = new StringBuffer();
				message.append("Study UID is " + studyUid);
				printList(itemList, out, message.toString());
			} else if (searchType.trim().equalsIgnoreCase("tables")) {
				itemList = new ArrayList<Map<String,String>>();
				Map<String, String> item = new HashMap<String,String>();
				/*
				 * Count rows in ISSUER table.
				 */
				if (issuerCountStmt == null) {
					issuerCountStmt = conn.prepareStatement(issuerCount); 
				} else if (issuerCountStmt.isClosed()) {
					issuerCountStmt = conn.prepareStatement(issuerCount);
				}
				rs = issuerCountStmt.executeQuery();
				rs.next();
				item.put("ISSUER", Integer.toString(rs.getInt("COUNTER")));
				/*
				 * Count rows in PATIENT table.
				 */
				if (patientCountStmt == null) {
					patientCountStmt = conn.prepareStatement(patientCount);
				} else if (patientCountStmt.isClosed()) {
					patientCountStmt = conn.prepareStatement(patientCount);
				}
				rs = patientCountStmt.executeQuery();
				rs.next();
				item.put("PATIENT", Integer.toString(rs.getInt("COUNTER")));
				/*
				 * Count rows in STUDY table.
				 */
				if (studyCountStmt == null) {
					studyCountStmt = conn.prepareStatement(studyCount);
				} else if (studyCountStmt.isClosed()) {
					studyCountStmt = conn.prepareStatement(studyCount);
				}
				rs = studyCountStmt.executeQuery();
				rs.next();
				item.put("STUDY", Integer.toString(rs.getInt("COUNTER")));
				/*
				 * Count rows in SERIES table.
				 */
				if (seriesCountStmt == null) {
					seriesCountStmt = conn.prepareStatement(seriesCount);
				} else if (seriesCountStmt.isClosed()) {
					seriesCountStmt = conn.prepareStatement(seriesCount);
				}
				rs = seriesCountStmt.executeQuery();
				rs.next();
				item.put("SERIES", Integer.toString(rs.getInt("COUNTER")));
				/*
				 * Count rows in INSTANCE table.
				 */
				if (instanceCountStmt == null) {
					instanceCountStmt = conn.prepareStatement(instanceCount);
				} else if (instanceCountStmt.isClosed()) {
					instanceCountStmt = conn.prepareStatement(instanceCount);
				}
				rs = instanceCountStmt.executeQuery();
				rs.next();
				item.put("INSTANCE", Integer.toString(rs.getInt("COUNTER")));
				itemList.add(item);
				String message = "List of row counts for tables";
				printList(itemList, out, message.toString());
			}else {
				out.println("{\"result\":\"failure\", \"message\":\"illegal option\" }");
			}
		} catch (SQLException e) {
			debugLogger.error("SQL error", e);
			throw new ServletException("SQL problem", e);
		} 
		out.close();
			}
	/**
	 * Send the contents of a map as an HTTP response.
	 * @param list Map to be displayed
	 * @param out PrintWriter used for output
	 * @param message Message to go with listing
	 */
	protected void printList(List<Map<String,String>> list, PrintWriter out, String message) {
		if (list == null) {
			debugLogger.error("SearchHandler.printList called with null value for list");
			return;
		}
		ListIterator<Map<String,String>> listIter = list.listIterator();
		out.println("{\"result\":\"success\",\"message\":\"" + JsonHelper.escape(message) + "\",\"list\":[");
		while (listIter.hasNext()) {
			Map<String,String> map = listIter.next();
			Set<Map.Entry<String,String>> set = map.entrySet();
			Iterator<Map.Entry<String,String>> iter = set.iterator();
			boolean first = true;
			while (iter.hasNext()) {
				if (first) {
					first = false;
				} else {
					out.print(",");
				}
				Map.Entry<String,String> item = iter.next();
				/* Map.Entry.getKey()  Map.Entry.getValue() */
				out.print( "\"" + item.getKey() + "\":\"" + item.getValue() + "\"");
			}
			out.println("}");
		}
		out.println(" ] }");
	}
	/**
	 * Modify a string by replacing the HTML escape characters with literal representations.
	 * @param value string to be processed
	 * @return processed string
	 */
	protected String escapeHtml(String value) {
		if (value == null) { return new String(); }
		return value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br />");
	}
	/*
	 * %2a is hexadecimal for asterisk
	 * http://epad-dev-acrin.phila.acr.org:8080/search/?searchType=series&studyUID=123
	 * Series Id,Patient Id,Patient Name,Series Date,Exam Type,Thumbnail URL,SeriesDescription,NumberOfSeriesRelatedInstances,ImagesInSeries
	 * http://epad-dev-acrin.phila.acr.org:8080/search?debug
	 * {searchtype=examtype&searchstring=%2a=StudyUID, Patient Name, Patient ID, Exam Type, Date Acquired
	 * http://epad-dev-acrin.phila.acr.org:8080/search/?searchtype=examtype&searchstring=%2a
	 * http://epad-dev-acrin.phila.acr.org:8080/search/?searchtype=patientname&searchstring=%2a
	 * http://epad-dev-acrin.phila.acr.org:8080/search/?searchtype=series&studyUID=1.3.6.1.4.1.19291.2.1.1.2941823662908100019
	 * Series Id,Patient Id,Patient Name,Series Date,Exam Type,Thumbnail URL,SeriesDescription,NumberOfSeriesRelatedInstances,ImagesInSeries
	 * 
	 */
}
