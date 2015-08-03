package edu.stanford.epad.epadws.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.epad.epadws.handlers.dicom.ResourcesFileHandler;

public class ResourcesFileServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServletException, IOException {
		new ResourcesFileHandler().handle("", null, httpRequest, httpResponse);
	}

}
