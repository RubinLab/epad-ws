<%@ page language="java"%>
<%@ page import="edu.stanford.epad.epadws.service.*"%>
<%@ page import="edu.stanford.epad.epadws.security.*"%>
<%@ page session="false" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Login </TITLE>
 <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
</HEAD>
<%
			String sessionID = SessionService.getJSessionIDFromRequest(request);
			String username = EPADSessionOperations.getSessionUser(sessionID);
%>
<frameset cols="20%, 80%">

<frameset rows="90%, 10%">
<frame name=leftpanel src="projects.jsp">
<frame name=buttons src="buttons.jsp" noresize scrolling>
</frameset>

<frameset rows="*">
<frame name=rightpanel src="restapi.html">
</frameset>

</frameset>
</HTML>
