package edu.stanford.epad.epadws.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.epad.epadws.handlers.admin.StatisticsHandler;

public class StatisticsServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServletException, IOException {
		new StatisticsHandler().handle("", null, httpRequest, httpResponse);
	}

}
