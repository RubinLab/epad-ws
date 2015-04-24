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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<TITLE>Upload Files</TITLE>
 <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
</HEAD>

<BODY>
<div id=imagelist></div>
<% if (subjectID == null) { %>
 <form name=uploadform  id=uploadform  action="v2/projects/<%=projectID%>/files/?username=<%=username%>" method="post" enctype="multipart/form-data" accept-charset=utf-8>
 Project:<%=projectID%><br>
<% } else if (studyUID == null) { %>
 <form action="v2/projects/<%=projectID%>/subjects/<%=subjectID%>/files/?username=<%=username%>" method="post" enctype="multipart/form-data" accept-charset=utf-8>
 Project:<%=projectID%> Patient:<%=subjectID%><br>
<% } else if (seriesUID == null) { %>
 <form action="v2/projects/<%=projectID%>/subjects/<%=subjectID%>/studies/<%=studyUID%>/files/?username=<%=username%>" method="post" enctype="multipart/form-data" accept-charset=utf-8>
 Project:<%=projectID%> Patient:<%=subjectID%> Study:<%=studyUID%><br>
<% } else { %>
 <form action="v2/projects/<%=projectID%>/subjects/<%=subjectID%>/studies/<%=studyUID%>/series/<%=seriesUID%>/files/?username=<%=username%>" method="post" enctype="multipart/form-data" accept-charset=utf-8>
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
  <input type="button" value="Submit" onclick="uploadFiles()">
</form>
<script>
var filedata;
function uploadFiles()
{
	document.getElementById("uploadform").submit();
}
$( document ).ready(function() {
<% if (subjectID == null) { %>
	var url = "/epad/v2/projects/<%=projectID%>/files/";
<% } else if (studyUID == null) { %>
	var url = "/epad/v2/projects/<%=projectID%>/subjects/<%=subjectID%>/files/";
<% } else if (seriesUID == null) { %>
	var url = "/epad/v2/projects/<%=projectID%>/subjects/<%=subjectID%>/studies/<%=studyUID%>/files/";
<% } else { %>
	var url = "/epad/v2/projects/<%=projectID%>/subjects/<%=subjectID%>/studies/<%=studyUID%>/series/<%=seriesUID%>/files/";
<% } %>
	//alert(url);
	$.ajax({         
		url: url + "?username=<%=username%>",         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting files");
			return true;},
		success: function(response){
			var files = response.ResultSet.Result;
			filedata = "<table border=1><tr bgcolor=lightgray><td></td><td>Name</td><td>Description</td><td>Type</td><td>Length</td><td>Created</td></tr>\n";
			for (i = 0; i < files.length; i++)
			{
				filedata = filedata + "<tr><td><img src=delete.jpg height=10px onclick='deleteFile(\"<%=projectID%>\",null,null,null,\"" + files[i].fileName +"\")'/></td><td><a href='download.jsp?path=" + files[i].path + "&name=" + files[i].fileName+ "'>" + files[i].fileName + "</a></td><td>" + files[i].description + "</td><td>" + files[i].fileType + "</td><td>" + files[i].fileLength + "</td><td>"  +  files[i].createdTime + "</td></tr>\n";
			}
		}
	})
	filedata =  filedata + "</table>\n";
	document.getElementById("imagelist").innerHTML = filedata;
})
</script>
</BODY>
</HTML>
