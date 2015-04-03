package edu.stanford.epad.epadws.security;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.service.SessionService;

public class WebAuthFilter implements Filter {

	private static final EPADLogger log = EPADLogger.getInstance();
	private static final String WEBAUTH_HEADER = "X-WEBAUTH-USER";
	private static final String JSESSIONID_COOKIE = "JSESSIONID";
	private static final String LOGGEDINUSER_COOKIE = "ePADLoggedinUser";
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String webAuthUser = httpRequest.getHeader(WEBAUTH_HEADER);
	    if (webAuthUser != null && webAuthUser.length() > 0)
		{
			String sessionID = SessionService.getJSessionIDFromRequest(httpRequest);
			if (SessionService.hasValidSessionID(sessionID)) {
				String sessionUser = EPADSessionOperations.getSessionUser(sessionID);
				if (!sessionUser.equals(webAuthUser))
					sessionID = null;
			} else {
				sessionID = null;
			}
			if (sessionID == null) {
				try {
					// Note: On the client, this call should be SessionResource.createSession(List<String>() with username, password)
					log.info("Session ID for user:"+ webAuthUser + " is null, creating new session");
					sessionID = EPADSessionOperations.authenticateWebAuthUser(webAuthUser, EPADConfig.webAuthPassword);
		            Cookie userName = new Cookie(LOGGEDINUSER_COOKIE, webAuthUser);
		            userName.setMaxAge(-1);
		            userName.setPath("/epad/");
		            httpResponse.addCookie(userName);
		            Cookie sessionCookie = new Cookie(JSESSIONID_COOKIE, sessionID);
		            sessionCookie.setMaxAge(-1);
		            sessionCookie.setPath("/epad/");
		            httpResponse.addCookie(sessionCookie);
		            if (httpRequest.getRequestURL().toString().indexOf("Web_pad") == -1)
		            	httpResponse.sendRedirect("Web_pad.html");
		            return;
				} catch (Exception e) {
					log.warning("Error logging in WebAuth User", e);
				}
			}
		} else {
			String sessionID = SessionService.getJSessionIDFromRequest(httpRequest);
			if (!SessionService.hasValidSessionID(sessionID) && httpRequest.getRequestURL().toString().indexOf("login.jsp") == -1 && !httpRequest.getRequestURL().toString().endsWith("/session")) {
            	httpResponse.sendRedirect("/epad/login.jsp");
            	return;
			}
			if (httpRequest.getRequestURL().indexOf("/v2/") != -1)				
				log.info("Request from client:" + httpRequest.getRequestURL());
		}
	     
        filterChain.doFilter(request, response);
		return;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
