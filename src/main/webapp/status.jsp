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
	String user = request.getParameter("username");
	if (user == null) user = username;
	if (!user.equals(username) && !username.equals("admin"))
	{
%><script> alert('No permissions to access <%=user%>');</script><%
		return;
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<TITLE>Status</TITLE>
 <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
</HEAD>

<BODY bgcolor=white>
<form>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="button" value="Refresh Status" onclick="getStatus()">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="button" value="Refresh Logs" onclick="getLogs()">
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
	
	$( document ).ready(function() {
		getStatus();
		getLogs();
	})
		
	function status()
	{
		window.location = "<%=request.getContextPath()%>/status?Date=" + new Date();
	}

   function getStatus()
   {
	   	var url = "<%=request.getContextPath()%>/v2/users/<%=user%>/taskstatus/" + "?username=<%=username%>";
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
	   	var url = "<%=request.getContextPath()%>/v2/users/<%=user%>/eventlogs/" + "?username=<%=username%>";
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
