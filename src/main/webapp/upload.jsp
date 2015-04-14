<%@ page language="java"%>
<%@ page import="java.io.*"%>
<%@ page import="edu.stanford.epad.common.util.*"%>
<%@ page import="edu.stanford.epad.epadws.service.*"%><%@ page import="edu.stanford.epad.epadws.security.*"%>
<%@ page session="false" %><%
	String sessionID = SessionService.getJSessionIDFromRequest(request);
	String username = EPADSessionOperations.getSessionUser(sessionID);
	String projectID = request.getParameter("projectID");
	if (projectID == null)
	{
%><script> alert('ProjectID is null');</script><%
		return;
	}
	String subjectID = request.getParameter("subjectID");
	String studyUID = request.getParameter("studyUID");
	String seriesUID = request.getParameter("seriesUID");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Query RPAC</TITLE>
 <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
</HEAD>

<BODY>
<% if (subjectID == null) { %>
 <form action="v2/projects/<%=projectID%>/files/?username=<%=username%>" method="post" enctype="multipart/form-data">
 Project:<%=projectID%><br>
<% } else if (studyUID == null) { %>
 <form action="v2/projects/<%=projectID%>/subjects/<%=subjectID%>/studies/<%=studyUID%>/series/<%=seriesUID%>/files/?username=<%=username%>" method="post" enctype="multipart/form-data">
 Project:<%=projectID%> Patient:<%=subjectID%><br>
<% } else if (seriesUID == null) { %>
 <form action="v2/projects/<%=projectID%>/subjects/<%=subjectID%>/studies/<%=studyUID%>/series/<%=seriesUID%>/files/?username=<%=username%>" method="post" enctype="multipart/form-data">
 Project:<%=projectID%> Patient:<%=subjectID%> Study:<%=studyUID%><br>
<% } else { %>
 <form action="v2/projects/<%=projectID%>/subjects/<%=subjectID%>/studies/<%=studyUID%>/series/<%=seriesUID%>/files/?username=<%=username%>" method="post" enctype="multipart/form-data">
  Project:<%=projectID%> Patient:<%=subjectID%> Study:<%=studyUID%> Series:<%=seriesUID%><br>
<% } %>
  File 1: <input type=file name=file1><br>
  File 1 Description: <input type=text name=description size=30><br>
  File 2: <input type=file name=file2><br>
  File 2 Description: <input type=text name=description size=30><br>
  File 3: <input type=file name=file3><br>
  File 3 Description: <input type=text name=description size=30><br>
  File 4: <input type=file name=file4><br>
  File 4 Description: <input type=text name=description size=30><br>
  <input type="submit" value="Submit">
</form>
</BODY>
</HTML>
