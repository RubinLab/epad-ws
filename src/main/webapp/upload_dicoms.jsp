<%@ page language="java"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.stanford.epad.common.util.*"%>
<%@ page import="edu.stanford.epad.epadws.handlers.*"%>
<%@ page import="edu.stanford.epad.epadws.service.*"%>
<%@ page import="edu.stanford.epad.epadws.security.*"%>
<%@ page session="false" %><%
	EPADLogger log = EPADLogger.getInstance();
	String sessionID = SessionService.getJSessionIDFromRequest(request);
	String username = EPADSessionOperations.getSessionUser(sessionID);
	String projectID = request.getParameter("projectID");
	if (projectID == null)
	{
%><script> alert('ProjectID is null');</script><%
		return;
	}
	String subjectID = null;
	String studyUID = null;
	String seriesUID = null;
	String requestContentType = request.getContentType();
	if (requestContentType != null && requestContentType.startsWith("multipart/form-data"))
	{
		PrintWriter responseStream = response.getWriter();
		String uploadDirPath = EPADConfig.getEPADWebServerUploadDir() + "temp" + Long.toString(System.currentTimeMillis());
		String projectName = "XNATProjectName=" + projectID;
		String sessionName = "XNATSessionID=" + sessionID;
		String userName = "XNATUserName=" + username;

		FileWriter fw = null;
		try {
			File uploadDir = new File(uploadDirPath);
			uploadDir.mkdirs();
			File xnatProps = new File(uploadDirPath, "xnat_upload.properties");
			fw = new FileWriter(xnatProps);
			fw.write(projectName + "\n");
			fw.write(sessionName + "\n");
			fw.write(userName + "\n");
		}
		catch (Exception x)
		{
			System.out.println("Error writing xnat file");
			x.printStackTrace();
		}
		finally 
		{
			if (fw != null) fw.close();
		}
		try
		{
			Map<String, Object> paramData = HandlerUtil.parsePostedData(uploadDirPath, request, responseStream);
			subjectID = (String) paramData.get("subjectID");
			studyUID = (String) paramData.get("studyUID");
			seriesUID = (String) paramData.get("seriesUID");
		} 
		catch (Exception x)
		{
%>
			<script>alert("<%=x.getMessage()%>");</script>
<%
			log.warning("Error saving uploaded files", x);
			x.printStackTrace();
		}
	}
	else
	{
		subjectID = request.getParameter("subjectID");
		studyUID = request.getParameter("studyUID");
		seriesUID = request.getParameter("seriesUID");
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<TITLE>Upload Files</TITLE>
 <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
</HEAD>

<BODY bgcolor=white>
<div id=imagelist></div>
<br>
<% if (subjectID == null) { %>
 <form name="uploadform" id="uploadform" method="post" enctype="multipart/form-data" accept-charset=utf-8>
 Project: <input type=text readonly name="projectID" value="<%=projectID%>" size=10><br>
<% } else if (studyUID == null) { %>
 <form name="uploadform" id="uploadform"  method="post" enctype="multipart/form-data" accept-charset=utf-8>
 Project: <input type=text readonly name="projectID" value="<%=projectID%>"> Patient: <input type=text readonly name="subjectID" value="<%=subjectID%>"><br>
<% } else if (seriesUID == null) { %>
 <form name="uploadform" id="uploadform" method="post" enctype="multipart/form-data" accept-charset=utf-8>
 Project: <input type=text readonly name="projectID" value="<%=projectID%>"> Patient: <input type=text readonly name="subjectID" value="<%=subjectID%>"> Study: <input type=text readonly name="studyUID" value="<%=studyUID%>"><br>
<% } else { %>
 <form name="uploadform" id="uploadform" method="post" enctype="multipart/form-data" accept-charset=utf-8>
  Project: <input type=text readonly name="projectID" value="<%=projectID%>"> Patient: <input type=text readonly name="subjectID" value="<%=subjectID%>"> Study: <input type=text readonly name="studyUID" value="<%=studyUID%>"> Series: <input type=text readonly name="seriesUID" value="<%=seriesUID%>"><br>
<% } %>
 <br>
 <b>Zip Files:</b> <input id=uploadlist type=file name=file multiple="multiple" accept=".zip,.dcm,.dso"><br>
  <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="Submit" onclick="uploadFiles()">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="button" value="Status" onclick="getStatus()">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="button" value="Logs" onclick="getLogs()">
</form>
<span id=flist></span>
<br>
<b>Status:</b>
<div id=taskstatusdiv></div>
<br>
<b>Logs:</b>
<div id=logsdiv></div>
<style>tbody { display: block;max-height:350px;overflow-y:auto; } </style>
<script>
var filedata;
var selDiv = "";
		
document.addEventListener("DOMContentLoaded", init, false);
	
function init() {
	document.querySelector('#uploadlist').addEventListener('change', handleFileSelect, false);
	selDiv = document.querySelector("#flist");
	getStatus();
	getLogs();
}
		
function handleFileSelect(e) {
	
	if(!e.target.files) return;
	
	selDiv.innerHTML = "";
	
	var files = e.target.files;
	for(var i=0; i<files.length; i++) {
		var f = files[i];
		
		selDiv.innerHTML += f.name + "<br/>";
	}
}
		
	function uploadFiles()
	{
		document.getElementById("uploadform").submit();
	}
	function status()
	{
		window.location = "<%=request.getContextPath()%>/status?Date=" + new Date();
	}

   function getStatus()
   {
	   	var url = "<%=request.getContextPath()%>/v2/users/<%=username%>/taskstatus/" + "?username=<%=username%>";
		//alert(url);
		$.ajax({         
			url: url,         
			type: 'get',         
			async: false,         
			cache: false,         
			timeout: 30000,         
			error: function(){
				alert("Error getting status");
				return true;},
			success: function(response){
				var tstats = response.ResultSet.Result;
				var html = "<table border=1 cellpadding=2><tbody><tr style='font-weight: bold;'><td align=center>User</td><td align=center>Task</td><td align=center>Start</td><td align=center>Complete</td><td align=center>Target</td><td align=center>Status</td></tr>";
				for (i = 0; i < tstats.length; i++)
				{
					var compl = tstats[i].completetime;
					if (compl == null || compl == "")
					{
						compl = "In Process";
					}
					var line = "<tr><td>" + tstats[i].username + "</td><td>" + tstats[i].type + "</td><td>" + tstats[i].starttime + "</td><td>" + compl + "</td><td>" + tstats[i].target + "</td><td>" + tstats[i].status + "</td></tr>\n";
					html = html + line;
				}
				if (tstats.length == 0)
				{
					var line = "<tr><td colspan=100%>No Background Tasks Running</td></tr>\n";
					html = html + line;
				}
				html = html + "</tbody></table>\n";
				document.getElementById("taskstatusdiv").innerHTML = html;
			}
		});
   }

   function getLogs()
   {
	   	var url = "<%=request.getContextPath()%>/v2/users/<%=username%>/eventlogs/" + "?username=<%=username%>";
		//alert(url);
		$.ajax({         
			url: url,         
			type: 'get',         
			async: false,         
			cache: false,         
			timeout: 30000,         
			error: function(){
				alert("Error getting logs");
				return true;},
			success: function(response){
				var logs = response.ResultSet.Result;
				var html = "<table border=1 cellpadding=2><tbody><tr style='font-weight: bold;'><td align=center>Time</td><td align=center>User</td><td align=center>Action</td><td align=center>Project</td><td align=center >Info</td></tr>";
				for (i = 0; i < logs.length; i++)
				{
					var info = "";
					if (logs[i].subjectUID != undefined)
					{
						info = logs[i].subjectUID + ":";
					}
					if (logs[i].studyUID != undefined)
					{
						info = info + logs[i].studyUID + ":";
					}
					if (logs[i].seriesUID != undefined)
					{
						info = info + logs[i].seriesUID + ":";
					}
					if (logs[i].filename != undefined)
					{
						info = info + logs[i].filename + ":";
					}
					if (logs[i].params != undefined)
					{
						info = info + logs[i].params;
					}
					var line = "<tr><td nowrap>" + logs[i].createdTime + "</td><td>" + logs[i].username + "</td><td nowrap>" + logs[i].action + "</td><td>" + logs[i].projectID + "</td><td>" + info + "</td></tr>\n";
					html = html + line;
				}
				html = html + "</tbody></table>\n";
				document.getElementById("logsdiv").innerHTML = html;
			}
		});

   }

</script>
</BODY>
</HTML>
