<%@ page language="java"%>
<%@ page import="edu.stanford.epad.epadws.service.*"%>
<%@ page import="edu.stanford.epad.epadws.security.*"%>
<%@ page import="edu.stanford.epad.common.util.*"%>
<%@ page session="false" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> EPAD Plugins/Annotations </TITLE>
 <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
</HEAD>
<BODY bgcolor=white>
<%
			String sessionID = SessionService.getJSessionIDFromRequest(request);
			String username = EPADSessionOperations.getSessionUser(sessionID);
%>
<h2>Plugins (<a href=plugin.jsp?pluginID=new target=rightpanel>New</a>)</h2>
<div id=plugindiv></div>
<br><b>Annotations<span id=pluginId></span></b>
<div id=aimdiv></div>
<script>

function showImages(projectID)
{
	window.parent.rightpanel.location = "images.jsp?projectID=" + projectID;
	window.parent.buttons.projectID = projectID;
}

$( document ).ready(function() {
	var url = "<%=request.getContextPath()%>/v2/plugins/?username=<%=username%>";
	//alert(url);
	$.ajax({         
		url: url,         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting plugins");
			return true;},
		success: function(response){
			var plugins = response.ResultSet.Result;
			var html = "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>ID</td><td>Name</td><td>Description</td><td>Class</td><td>Status</td><td>Modality</td></tr>";
			for (i = 0; i < plugins.length; i++)
			{
				var line = "<tr><td><a href='index.jsp?pluginId=" + plugins[i].pluginId + "'><u>" + plugins[i].pluginId + "</u></a></span></td><td>" + plugins[i].name + "</td><td>" + plugins[i].description + "</td><td>" + plugins[i].javaclass + "</td><td>" + plugins[i].status + "</td><td>" + plugins[i].modality + "</td></tr>\n";
				html = html + line;
			}
			html = html + "</table>\n";
			document.getElementById("plugindiv").innerHTML = html;
		}
	});
	var selectedPlugin = getURLParameter("pluginId");
	if (selectedPlugin != null && selectedPlugin != '')
	{
		var url = "<%=request.getContextPath()%>/v2/aims/?format=summary&templateCode=" + selectedPlugin;
		//alert(url);
		$.ajax({         
			url: url,         
			type: 'get',         
			async: false,         
			cache: false,         
			timeout: 30000,         
			error: function(){
				alert("Error getting aims");
				return true;},
			success: function(response){
				var aims = response.ResultSet.Result;
				var html = "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>ID</td><td>Name</td><td>Owner</td><td>ProjectID</td><td>PatientID</td><td>SeriesID</td><td>DSOSeriesID</td></tr>";
				for (i = 0; i < aims.length; i++)
				{
					var line = "<tr><td><a href='annotation.jsp?aimID=" + aims[i].aimID + "&projectID=" + aims[i].projectID + "&subjectID=" + aims[i].subjectID + "'><u>" + aims[i].aimID + "</u></a></span></td><td>" + aims[i].name + "</td><td>" + aims[i].userName + "</td><td>" + aims[i].projectID + "</td><td>" + aims[i].subjectID + "</td><td>" + aims[i].seriesUID + "</td><td>" + aims[i].dsoSeriesUID + "</td></tr>\n";
					html = html + line;
				}
				html = html + "</table>\n";
				document.getElementById("aimdiv").innerHTML = html;
				document.getElementById("pluginId").innerHTML = " : " + selectedPlugin;
			}
		});
	}
   });

   function getURLParameter(sParam)
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
