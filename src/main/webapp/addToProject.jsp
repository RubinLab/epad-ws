<%@ page language="java"%>
<%@ page import="java.io.*"%>
<%@ page import="edu.stanford.epad.common.util.*"%>
<%@ page import="edu.stanford.epad.epadws.service.*"%><%@ page import="edu.stanford.epad.epadws.security.*"%>
<%@ page session="false" %><%
	String sessionID = SessionService.getJSessionIDFromRequest(request);
	String username = EPADSessionOperations.getSessionUser(sessionID);
	String subjectID = request.getParameter("subjectID");
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
<h3>Add To Project<h3>
<b>Subject: </b><%=subjectID%>
<div id=projdiv></div>
<script>

$( document ).ready(function() {
	var url = "<%=request.getContextPath()%>/v2/projects/?username=<%=username%>";
	//alert(url);
	$.ajax({         
		url: url,         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting projects");
			return true;},
		success: function(response){
			var projects = response.ResultSet.Result;
			var html = "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>Name</td><td>ID</td></tr>";
			for (i = 0; i < projects.length; i++)
			{
				var line = "<tr><td><span onclick=\"addToProject('" + projects[i].id +"')\"><u>" + projects[i].name + "</u></span></td><td>" + projects[i].id + "</td></tr>\n";
				html = html + line;
			}
			html = html + "</table>\n";
			document.getElementById("projdiv").innerHTML = html;
		}
	});

   });

   function addToProject(projectID)
   {
		var url = "<%=request.getContextPath()%>/v2/projects/" + projectID + "/subjects/<%=subjectID%>?username=<%=username%>";
		$.ajax({         
			url: url,         
			type: 'put',         
			async: false,         
			cache: false,         
			timeout: 30000,         
			error: function(){
				alert("Error adding to  projects");
				return true;},
			success: function(response){
				return true;
			}
		});
   }
</script>
</BODY>
</HTML>
