/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server.servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.isis.epadws.db.mysql.MySqlInstance;
/**
 * Servlet testing connection to H2 database.
 * @author Bradley Ross
 *
 */
@SuppressWarnings("serial")
public class ConnectionTest extends HttpServlet {
	/**
	 * H2 database instance.
	 */
	protected MySqlInstance db = null;
	/**
	 * slf4j/log4j logger for application.
	 */
	protected Logger logger = null;
	/**
	 * Constructor setting up logger.
	 */
	public ConnectionTest() {
		logger = LoggerFactory.getLogger(this.getClass());
	}
	/**
	 * Handler for HTTP POST requests.
	 * @param req request object
	 * @param resp response object
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		logger.info("ConnectionTest method is POST");
		PrintWriter out = resp.getWriter();
		message("Method is POST", out);
	}
	/**
	 * Handler for HTTP PUT requests.
	 * @param req request object
	 * @param resp response object
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		PrintWriter out = resp.getWriter();
		message("Method is PUT", out);
		logger.info("ConnectionTest method is PUT");
	}
	/**
	 * Handler for HTTP DELETE requests.
	 * @param req request object
	 * @param resp response object
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		logger.info("ConnectionTest method is DELETE");
		PrintWriter out = resp.getWriter();
		message("Method is DELETE", out);
	}
	/**
	 * Attempt to provide the equivalent of a servlet using Jetty.
	 * 
	 * @param  req request from the client
	 * @param resp the response that will be returned to the client
	 * @throws ServletException
	 * @throws IOException
	 * @see Connection
	 * @see DatabaseMetaData
	 * @see ResultSet
	 * @see ResultSetMetaData
	 * @see Statement
	 */
	public void doGet(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {
		MySqlInstance db = null;
		Connection conn = null;
		try {
			logger.info("Starting ConnectionTest HTTP transaction with method of GET");
			String query = " SELECT pat_name, study_uid, series_iuid, sop_iuid " +
					" from patient, study, series, instance WHERE " +
					"patient.pk=study.patient_fk and " +
					"study.pk=series.study_fk and " +
					"instance.series_fk=series.pk";
			db = MySqlInstance.getInstance();
			conn  = db.getConnection();
			PrintWriter writer = resp.getWriter();
			if (conn == null) {
				logger.error("ConnectionTest handler returned null value for Connection");
				resp.setContentType("text/html");
				resp.setStatus(HttpServletResponse.SC_OK);
				writer.println("<!DOCTYPE html>");
				writer.println("<html><head>");
				writer.println("<title>Connection Test</title>");
				writer.println("</head><body>");
				writer.println("<h1>Connection Test</h1>");
				writer.println("<p>Null value returned for connection</p>");
				writer.println("</body></html>");
				return;
			}
			resp.setContentType("text/html");
			resp.setStatus(HttpServletResponse.SC_OK);
			writer.println("<!DOCTYPE html>");
			writer.println("<html><head>");
			writer.println("<title>Connection Test</title>");
			writer.println("</head><body>");
			writer.println("<h1>Connection Test</h1>");
			/*
			 * First SQL Query
			 */
			DatabaseMetaData tables = conn.getMetaData();
			ResultSet rs = tables.getTables(null, null, null, null);
			writer.println("<p>Result set obtained</p>");
			writer.println("<table border=\"1\">");
			writer.println("<tr><td>Catalog</td><td>Schema</td><td>Type</td><td>Name</td></tr>");
			while (rs.next()) {
				writer.println("<tr><td>" +rs.getString("TABLE_CAT") + "</td><td>" +
						rs.getString("TABLE_SCHEM") + "</td><td>" +
						rs.getString("TABLE_TYPE") + "</td><td>" +
						rs.getString("TABLE_NAME") + "</td></tr>");
			}
			writer.println("</table>");
			rs.close();
			/*
			 * Second SQL Query
			 */
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			ResultSetMetaData meta = rs.getMetaData();
			int cols = meta.getColumnCount();
			writer.println();
			writer.print("<table border=\"1\"><tr>");
			for (int i = 1; i <= cols; i++) {
				writer.print("<td>" + meta.getColumnName(i) + "</td>");
			}
			writer.println("</tr>");
			while (rs.next()) {
				writer.print("<tr>");
				for (int i = 1; i <= cols; i++) {
					writer.print("<td>" + rs.getString(i) + "</td>");
				}
				writer.println("</tr>");
			}
			writer.println("</table>");
			writer.println("</body></html>");
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			logger.error("Error processing connectiontest" ,e);
			throw new ServletException("Exception encountered in ConnectionTest", e);
		} finally {
			try {
				if (!(conn == null)) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("Error processing connectiontest", e);
				throw new ServletException("Exception encountered in ConnectionTest", e);
			}
		}
		logger.info("ConnectionTest complete");
	}
	/**
	 * Escape control characters from a string that is part of an HTML
	 * document.
	 * @param value string to be processed
	 * @return processed string
	 */
	public String escapeHtml(String value) {
		return value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").
				replaceAll(">", "&gt;").replace("\n", "<br />");
	}
	/**
	 * Send a simple message in the HTTP request
	 * @param text message to be sent
	 * @param out PrintWriter object for servlet
	 */
	public void message(String text, PrintWriter out) {
		out.println("<!DOCTYPE html>");
		out.println("<html><head>");
		out.println("<title>ConnectionTest</title>");
		out.println("</head><body>");
		out.println("<h1>ConnectionTest</h1>");
		out.println("<p>" + text + "</p>");
		out.println("</body></html>");
	}
}
