<%@ page language="java"%>
<%@ page import="java.net.*"%>
<%@ page import="edu.stanford.epad.epadws.service.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Login </TITLE>
</HEAD>
<BODY>
<%
		Cookie sessionCookie = new Cookie("JSESSIONID", "");
		sessionCookie.setMaxAge(10*10); //ml test
		sessionCookie.setPath(request.getContextPath() + "/");
		response.addCookie(sessionCookie);
		sessionCookie = new Cookie("JSESSIONID", "");
		sessionCookie.setMaxAge(10*10);
		sessionCookie.setPath(request.getContextPath());
		response.addCookie(sessionCookie);
		String redirect = "/index.jsp";
		if (request.getRequestURI().contains("test/")) redirect = "/test/index.jsp";
%>
<h2><center>ePad Web Services</center></h2>
<p>
<table align=center border=1 cellpadding=3 cellspacing=0>
<form name=loginform method=post action="<%=request.getContextPath()%>/session/?redirectUrl=<%=URLEncoder.encode(request.getContextPath() + redirect)%>">
<tr><td width=50% align=right>Username:</td><td width=50% align=left><input name=username type=text></td></tr>
<tr><td align=right>Password:</td><td align left><input name=password type=password></td></tr>
<tr><td colspan=2 align=center><input type=button value=Submit onclick="document.loginform.submit()"></td></tr>
</form>
</table>
</BODY>
</HTML>
