/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.handlers.admin;

import edu.stanford.isis.epadws.server.ProxyLogger;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Listen for requests to shutdown the DicomProxy. It must pass a key value for the shutdown to work.
 *
 * The format is:
 *
 * /shutdown?user=amsnyder&auth=rev0604
 *
 */
public class SignalShutdownHandler extends AbstractHandler {

    private static ProxyLogger logger = ProxyLogger.getInstance();

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {

        PrintWriter out = httpServletResponse.getWriter();
        out.print("<html>");
        out.print("<body>");

        String remoteAddr = httpServletRequest.getRemoteAddr();
        String queryString = httpServletRequest.getQueryString();

        logger.info("Got shutdown request from: "+remoteAddr+" queryString="+queryString);
        out.print("Got shutdown request from: "+remoteAddr+" queryString="+queryString);


        //String user = httpServletRequest.getParameter("user");
        String auth = httpServletRequest.getParameter("auth");

        if(auth.equals("rev604")){

        }

        out.print("</body>");
        out.print("</html>");

    }
}
