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
			String username = EPADSessionOperations.getSessionUser(sessionID);
%>
<table>
</table>
<script>
$( document ).ready(function() {
	var url = "/epad/v2/projects/?username=<%=username%>";
	var patientIDFilter = getURLParamater("patientIDFilter");
	//alert(patientIDFilter)
	if (patientIDFilter != null && patientIDFilter != "")
	{
		url = url + "&patientIDFilter=" + patientIDFilter;
	}
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
			document.write("<table border=1><tr bgcolor=lightgray><td>Name</td><td>ID</td><td>Description</td><td>Subjects</td><td>Studies</td></tr>");
			for (i = 0; i < projects.length; i++)
			{
				document.write("<tr><td><a href='images.jsp?projectID=" + projects[i].id + "' target='rightpanel'>" + projects[i].name + "</a></td><td>" + projects[i].id + "</td><td>"  +  projects[i].description + "</td><td>" + projects[i].numberOfSubjects + "</td><td>" + projects[i].numberOfStudies + "</td></tr>\n");
			}
			document.write("</table>");
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
