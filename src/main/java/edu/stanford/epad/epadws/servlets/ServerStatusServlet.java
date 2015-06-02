package edu.stanford.epad.epadws.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.admin.ServerStatusHandler;

public class ServerStatusServlet extends HttpServlet {
	private static final EPADLogger log = EPADLogger.getInstance();

	@Override
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServletException, IOException {
		new ServerStatusHandler().handle("", null, httpRequest, httpResponse);
	}

}
