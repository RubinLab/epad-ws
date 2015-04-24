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
<BODY>
<%
			String sessionID = SessionService.getJSessionIDFromRequest(request);
			String loggedInUser = EPADSessionOperations.getSessionUser(sessionID);
			String username = request.getParameter("username");
%>
<h2>User: <%=username%></h2>
<div id=imagelist><div>
<table>
</table>
<script>
$( document ).ready(function() {
	var listdata;
	var url = "/epad/v2/users/<%=username%>";
	$.ajax({         
		url: url + "?username=<%=username%>",         
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
			html = html + "<tr><td>Logs</td><td>\n";
			var messages = user.messages.ResultSet.Result;
			for (i = 0; i < messages.length ; i++)
			{
				var html = html + "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>Date</td><td>Message</td><td>Level</td></tr>";
				for (i = 0; i < messages.length; i++)
				{
					var line = "<tr><td>" + messages[i].date + "</td><td>" + messages[i].message + "</td><td>" + messages[i].level + "</td></tr>\n";
					html = html + line;
				}
				html = html + "</table>\n";
			}
			html = html + "</td></tr>\n";
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
			document.getElementById("imagelist").innerHTML = html;
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
