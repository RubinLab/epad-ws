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
			String projectID = request.getParameter("projectID");
%>
<h2>ProjectID: <%=projectID%></h2>
<table border=1 cellpadding=2>
<tr><td>Project ID:</td><td><input name=projectID id=projectID value="" size=30></td></tr>
<tr><td>Name:</td><td><input name=projectName id=projectName value="" size=30></td></tr>
<tr><td>Description:</td><td><input name=projectDescription id=projectDescription value="" size=30></td></tr>
<tr><td>Public:</td><td><input name=public id=public value="" size=30></td></tr>
<tr><td>Creator:</td><td><input readonly name=creator id=creator value="" size=30></td></tr>
<tr><td align=center colspan=2><input type=button value=Save onclick="saveProject()"></td></tr>
<div id=userdata></div>
<table>
<div id=logdata></div>
</table>
<script>
$( document ).ready(function() {
	var listdata;
	var url = "<%=request.getContextPath()%>/v2/projects/<%=projectID%>";
<% if (projectID != null && projectID.length() > 0 && !"new".equals(projectID)) { %>
	$.ajax({         
		url: url + "?username=<%=loggedInUser%>",         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting project:<%=projectID%>");
			return true;},
		success: function(response){
			var project = response;
			document.getElementById("projectID").value = project.id;
			document.getElementById("projectName").value = project.name;
			document.getElementById("projectDescription").value = project.description;
			document.getElementById("projectID").value = project.projectID;
			document.getElementById("creator").value = project.creator;
			document.getElementById("public").value = project.public;
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
<% } %>

	});

   function saveProject()
   {
		var projectID =	document.getElementById("projectID").value;
		var projectName =	document.getElementById("projectName").value;
		var projectDescription =	document.getElementById("projectDescription").value;
	   	var url = "<%=request.getContextPath()%>/v2/projects/" + escape(projectID) + "?projectName=" + escape(projectName) + "&projectDescription=" + escape(projectDescription);
		$.ajax({         
			url: url + "&username=<%=loggedInUser%>",         
			type: 'put',         
			async: false,         
			cache: false,         
			timeout: 30000,         
			error: function(){
				alert("Error saving project");
				return true;},
			success: function(response){
				alert("Project saved");
			}
   });
   
   }
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
