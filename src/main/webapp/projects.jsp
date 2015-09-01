<%@ page language="java"%>
<%@ page import="edu.stanford.epad.epadws.service.*"%>
<%@ page import="edu.stanford.epad.epadws.security.*"%>
<%@ page import="edu.stanford.epad.common.util.*"%>
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
			String username = EPADSessionOperations.getSessionUser(sessionID);
%>
<h2>Projects</h2>
<div id=projdiv><div>
<script>
var leftdata = "images";

function showImages(projectID)
{
	window.parent.rightpanel.location = "images.jsp?projectID=" + projectID;
	window.parent.buttons.projectID = projectID;
}

$( document ).ready(function() {
	var url = "<%=request.getContextPath()%>/v2/projects/?username=<%=username%>";
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
			var html = "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>Name</td><td>ID</td><td>Description</td><td>Subjects</td></tr>";
			for (i = 0; i < projects.length; i++)
			{
				var line = "<tr><td><span onclick=\"window.parent.rightpanel.location = leftdata +'.jsp?projectID=" + projects[i].id +"';window.parent.buttons.projectID ='" + projects[i].id + "';\"><u>" + projects[i].name + "</u></span></td><td>" + projects[i].id + "</td><td>"  +  projects[i].description + "</td><td>" + projects[i].numberOfSubjects + "</td></tr>\n";
				html = html + line;
				if (projects[i].id == '<%=EPADConfig.xnatUploadProjectID%>')
				{
					var line = "<tr><td><span onclick=\"window.parent.rightpanel.location = leftdata +'.jsp?projectID=" + projects[i].id +"&unassignedOnly=true';window.parent.buttons.projectID ='" + projects[i].id + "';\"><u>Really Unassigned</u></span></td><td>" + projects[i].id + "</td><td>"  +  projects[i].description + "</td><td>...</td></tr>\n";
					html = html + line;
				}
			}
			html = html + "</table>\n";
			document.getElementById("projdiv").innerHTML = html;
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
