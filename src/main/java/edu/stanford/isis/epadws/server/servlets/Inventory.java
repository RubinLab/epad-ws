package edu.stanford.isis.epadws.server.servlets;
import java.io.IOException;

import java.io.PrintWriter;
import java.util.List;
// import java.util.ArrayList;
import java.util.Map;
// import java.util.HashMap;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Set;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
// import java.sql.DatabaseMetaData;
// import java.sql.Statement;
import java.sql.PreparedStatement;

import edu.stanford.isis.epadws.db.mysql.MySqlInstance;
import edu.stanford.isis.epadws.handlers.dicom.RsnaSearchHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import edu.stanford.aim.proxy.helpers.JsonHelper;

/**
 * Provide an inventory of the patients, studies, series, and instances
 * contained in the database.
 * @author Bradley Ross
 * @see RsnaSearchHandler
 */
@SuppressWarnings("serial")
public class Inventory extends HttpServlet
{
	/**
	 * SQL statement to list patients, studies, series, and
	 * instances in the database.
	 */
	protected String inventory = "SELECT PATIENT.PAT_NAME, " +
			" PATIENT.PAT_ID, PATIENT.PAT_ID_ISSUER, " +
			" STUDY.STUDY_ID, STUDY.STUDY_DESC, " +
			" SERIES.SERIES_NO, SERIES.SERIES_DESC, INSTANCE.SOP_CUID " +
			" COUNT(*) AS COUNTER " +
			" FROM PATIENT, STUDY, SERIES, INSTANCE " +
			" WHERE PATIENT.PK = STUDY.PATIENT_FK AND " +
			" SERIES.STUDY_FK = STUDY.PK AND INSTANCE.SERIES_FK = SERIES.PK " +
			" GROUP BY PATIENT.PK, STUDY.PK, SERIES.PK, " +
			" PAT_ID, PAT_ID_ISSUER, STUDY_DESC, SERIES_DESC, " +
			" SOP_CUID " +
			" ORDER BY PAT_NAME, STUDY_ID, SERIES_NO ";
	/**
	 * Prepared statement generated from {@link #inventory}.
	 */
	protected PreparedStatement inventoryStmt = null;
	/**
	 * slf4j logger.
	 */
	protected Logger debugLogger = LoggerFactory.getLogger(this.getClass());
	/*
	 * Database instance.
	 */
	MySqlInstance db = null;
	/*
	 * Database connection.
	 */
	Connection conn = null;
	/**
	 * Method run when servlet object is created.
	 */
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
	/**
	 * Method run when servlet object is destroyed.
	 */
	@Override
	public void destroy() {
		super.destroy();
		try {
			conn.close(); 
		} catch (SQLException e) {
			debugLogger.error("Unable close database connection", e );
		}
	}
	/**
	 * Method run when servlet is called.
	 */
	@Override
	public void service (HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException  {
		debugLogger.info("Processing Inventory Servlet");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		ResultSet rs = null;
		ResultSetMetaData meta = null;
		try {
			if (conn == null) {
				debugLogger.error("Value of jdbc connection was null");
			} else if (conn.isClosed()) {
				debugLogger.error("JDBC connection was closed");
				return;
			}
			if (inventoryStmt == null) {
				inventoryStmt = conn.prepareStatement(inventory);
			} else if (inventoryStmt.isClosed()) {
				inventoryStmt = conn.prepareStatement(inventory);
			}
			out.println("<html><head>");
			out.println("<title>Inventory List</title>");
			out.println("</head><body>");
			out.println("<h1>Inventory List</h1>");
			rs = inventoryStmt.executeQuery();
			meta = rs.getMetaData();
			int columnCount = meta.getColumnCount();
			out.println("<table border=\"1\">");
			out.println("<tr>");
			for (int i = 1; i <= columnCount; i++) {
				out.println("<td>" + escapeHtml(meta.getColumnLabel(i)) + "</td>");
			}
			out.println("</tr>");
			while (rs.next()) {
				out.println("<tr>");
				for (int i = 1; i <= columnCount; i++) {
					out.println("<td>" + escapeHtml(rs.getString(i)) + "</td>");
				}
				out.println("</tr>");
			}
			out.println("</table>");
			out.println("</body></html>");

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
			out.println("{\"result\":\"failure\",\"message\":\"" + JsonHelper.escape(message) +
					"\",\"list\":[]}");
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
	 */
}
