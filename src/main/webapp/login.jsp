<%@ page language="java"%>
<%@ page import="edu.stanford.epad.epadws.service.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Login </TITLE>
</HEAD>
<BODY>
<table align=center border=1 cellpadding=3 cellspacing=0>
<form name=loginform method=post action="<%=request.getContextPath()%>/session">
<tr><td width=50% align=right>Username:</td><td width=50% align=left><input name=username type=text></td></tr>
<tr><td align=right>Password:</td><td align left><input name=password type=password></td></tr>
<tr><td colspan=2 align=center><input type=button value=Submit onclick="document.loginform.submit()"></td></tr>
</form>
</table>
</BODY>
</HTML>
