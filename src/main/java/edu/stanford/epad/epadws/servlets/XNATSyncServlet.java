package edu.stanford.epad.epadws.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.epad.epadws.handlers.admin.XNATSyncHandler;

public class XNATSyncServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServletException, IOException {
		super.doGet(httpRequest, httpResponse);
		new XNATSyncHandler().handle("", null, httpRequest, httpResponse);
	}

}
