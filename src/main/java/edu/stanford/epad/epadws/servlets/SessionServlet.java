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
		log.info("POST: EPADSessionHandler");
		new EPADSessionHandler().handle("", null, httpRequest, httpResponse);

	}

	@Override
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServletException, IOException {
		log.info("GET: EPADSessionHandler");
		new EPADSessionHandler().handle("", null, httpRequest, httpResponse);
	}

	@Override
	protected void doDelete(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServletException, IOException {
		log.info("DELETE: EPADSessionHandler");
		new EPADSessionHandler().handle("", null, httpRequest, httpResponse);
	}

	@Override
	protected void doOptions(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServletException, IOException {
		log.info("OPTIONS: EPADSessionHandler");
		new EPADSessionHandler().handle("", null, httpRequest, httpResponse);
	}

}
