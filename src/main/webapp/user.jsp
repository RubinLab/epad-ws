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
<BODY bgcolor=white>
<%
			String sessionID = SessionService.getJSessionIDFromRequest(request);
			String loggedInUser = EPADSessionOperations.getSessionUser(sessionID);
			String username = request.getParameter("username");
%>
<h2>User: <%=username%></h2>
<div id=userdata></div>
<br><b>Worklists:</b>
<div id=wldata></div>
<br><b>Background Tasks:</b>
<div id=taskdata></div>
<br><b>Event Logs:</b>
<div id=logdata></div>
<table>
</table>
<style>tbody { display: block;max-height:350px;overflow-y:auto; } </style>
<script>
$( document ).ready(function() {
	var listdata;
	var url = "<%=request.getContextPath()%>/v2/users/<%=username%>";
	$.ajax({         
		url: url + "?username=<%=loggedInUser%>",         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting user");
			return true;},
		success: function(response){
			var user = response;
			var html = "<table border=1 cellpadding=2>\n";
			html = html + "<tr><td>First name</td><td><input type=text name=firstname size=30 value='" + user.firstname + "'></td></tr>\n";
			html = html + "<tr><td>Last name</td><td><input type=text name=lastname size=30 size=30 value='" + user.lastname + "'></td></tr>\n";
			html = html + "<tr><td>Email</td><td><input type=text name=email size=30 size=30 value='" + user.email + "'></td></tr>\n";
			html = html + "<tr><td>Permissions</td><td><input type=tex name=permissions size=30t size=30 value='" + user.permissions + "'></td></tr>\n";
			html = html + "<tr><td>Password</td><td><input type=password name=password size=30 size=30 value=''></td></tr>\n";
			html = html + "<tr><td>Repeat Password</td><td><input type=password name=password size=30 size=30 value=''></td></tr>\n";
			html = html + "<tr><td>Admin</td><td>" + user.admin + "</td></tr>\n";
			//html = html + "<tr><td>Logs</td><td>\n";
			//var messages = user.messages.ResultSet.Result;
			//for (i = 0; i < messages.length ; i++)
			//{
			//	var html = html + "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>Date</td><td>Message</td><td>Level</td></tr>";
			//	for (i = 0; i < messages.length; i++)
			//	{
			//		var line = "<tr><td>" + messages[i].date + "</td><td>" + messages[i].message + "</td><td>" + messages[i].level + "</td></tr>\n";
			//		html = html + line;
			//	}
			//	html = html + "</table>\n";
			//}
			//html = html + "</td></tr>\n";
			html = html + "<tr><td>Projects</td><td>\n";
			var projects = user.projects;
			var roles = user.projectToRole;
			for (i = 0; i < roles.length ; i++)
			{
				var html = html + "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>Project</td><td>Role</td><td>Remove</td></tr>";
				for (i = 0; i < roles.length; i++)
				{
					project = roles[i];
					role = roles[i];
					if (project.indexOf(":") != -1)
					{
						project = project.substring(0, project.indexOf(":"));
						role = role.substring(role.indexOf(":")+1);
					}
					var line = "<tr><td>" + project + "</td><td>" + role + "</td><td><img height=20px src=delete.jpg onclick=\"remove('" + project + "')\"></td></tr>\n";
					html = html + line;
				}
				html = html + "</table>\n";
			}
			html = html + "</td></tr>\n";
			html = html + "</table>\n";
			document.getElementById("userdata").innerHTML = html;
		}
	});
	url = "<%=request.getContextPath()%>/v2/users/<%=username%>/worklists/";
	$.ajax({         
		url: url + "?username=<%=loggedInUser%>",         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting worklists");
			return true;},
		success: function(response){
			var logs = response.ResultSet.Result;
			if (logs.length > 0) {
				var html = "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>ID</td><td>Description</td><td>Due Date</td><td>Start</td><td>Complete</td><td>Status</td></tr>";
				for (i = 0; i < logs.length; i++)
				{
					var line = "<tr><td><a href=worklist.jsp?workListID=" + logs[i].workListID + ">" + logs[i].workListID + "</a></td>";
					line = line + "<td>" + logs[i].Description + "</td>";
					line = line + "<td>" + logs[i].dueDate + "</td>";
					line = line + "<td>" + logs[i].startDate + "</td>";
					line = line + "<td>" + logs[i].completionDate + "</td>";
					line = line + "<td>" + logs[i].status + "</td>";
					line = line + "</tr>\n";
					html = html + line;
				}
				html = html + "</table>\n";
				document.getElementById("wldata").innerHTML = html;
			} else {
				document.getElementById("wldata").innerHTML = "No worklists defined";
			}
		}
	});

	url = "<%=request.getContextPath()%>/v2/users/<%=username%>/eventlogs/?start=0&count=100";
	$.ajax({         
		url: url + "&username=<%=loggedInUser%>",         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting logs");
			return true;},
		success: function(response){
			var logs = response.ResultSet.Result;
			if (logs.length > 0) {
			var html = "<table border=1 cellpadding=2><tbody><tr bgcolor=lightgray><td>Time</td><td>Action</td><td>ProjectID</td><td>ProjectName</td><td>SubjectID</td><td>SubjectName</td><td>StudyUID</td><td>SeriesUID</td><td>ImageUID</td><td>AimID</td><td>AimName</td><td>FileName</td><td>More Info</td></tr>";
			for (i = 0; i < logs.length; i++)
			{
				var line = "<tr><td>" + logs[i].createdTime + "</td>";
				line = line + "<td nowrap>" + logs[i].action + "</td>";
				line = line + "<td>" + logs[i].projectID + "</td>";
				line = line + "<td>" + logs[i].projectName + "</td>";
				line = line + "<td>" + logs[i].subjectUID + "</td>";
				line = line + "<td>" + logs[i].subjectName + "</td>";
				line = line + "<td>" + logs[i].studyUID + "</td>";
				line = line + "<td>" + logs[i].seriesUID + "</td>";
				line = line + "<td>" + logs[i].imageUID + "</td>";
				line = line + "<td>" + logs[i].aimID + "</td>";
				line = line + "<td>" + logs[i].aimName + "</td>";
				line = line + "<td>" + logs[i].filename + "</td>";
				line = line + "<td>" + logs[i].params + "</td>";
				line = line + "</tr>\n";
				html = html + line;
			}
			html = html + "</tbody></table>\n";
			document.getElementById("logdata").innerHTML = html;
			} else {
				document.getElementById("logdata").innerHTML = "No logs";
			}
		}
	});
	url = "<%=request.getContextPath()%>/v2/users/<%=username%>/taskstatus/";
	$.ajax({         
		url: url + "?username=<%=loggedInUser%>",         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting task status");
			return true;},
		success: function(response){
			var logs = response.ResultSet.Result;
			if (logs.length > 0) {
			var html = "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>Time</td><td>Type</td><td>Target</td><td>Status</td><td>Start</td><td>Complete</td></tr>";
			for (i = 0; i < logs.length; i++)
			{
				var line = "<tr><td nowrap>" + logs[i].statustime + "</td>";
				line = line + "<td nowrap>" + logs[i].type + "</td>";
				line = line + "<td nowrap>" + logs[i].target + "</td>";
				line = line + "<td nowrap>" + logs[i].status + "</td>";
				line = line + "<td nowrap>" + logs[i].starttime + "</td>";
				line = line + "<td nowrap>" + logs[i].completetime + "</td>";
				line = line + "</tr>\n";
				html = html + line;
			}
			html = html + "</table>\n";
			document.getElementById("taskdata").innerHTML = html;
			} else {
				document.getElementById("taskdata").innerHTML = "No task status records";
			}
		}
	});

   });
   function getURLParamater(sParam)
   {
		var sPageURL = window.location.search.substring(1);
		var sURLVariables = sPageURL.split('&');
		for (var i = 0; i < sURLVariables.length; i++) 
		{
			var sParameterName = sURLVariables[i].split('=');
			if (sParameterName[0] == sParam) 
			{
				return sParameterName[1];
			}
		}
   }
</script>
</BODY>
</HTML>
