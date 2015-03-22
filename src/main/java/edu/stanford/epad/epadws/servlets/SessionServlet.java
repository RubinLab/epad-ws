package edu.stanford.epad.epadws.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.session.EPADSessionHandler;

public class SessionServlet extends HttpServlet {
	private static final EPADLogger log = EPADLogger.getInstance();

	@Override
	protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServletException, IOException {
		log.info("Login request");
		super.doPost(httpRequest, httpResponse);
		new EPADSessionHandler().handle("", null, httpRequest, httpResponse);
	}

}
