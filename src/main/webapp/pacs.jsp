<%@ page language="java"%>
<%@ page import="edu.stanford.epad.epadws.service.*"%>
<%@ page import="edu.stanford.epad.epadws.security.*"%>
<%@ page session="false" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> PACS </TITLE>
 <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
</HEAD>
<BODY bgcolor=white>
<%
			String sessionID = SessionService.getJSessionIDFromRequest(request);
			String username = EPADSessionOperations.getSessionUser(sessionID);
%>
<h2>PACs</h2>
<b>Patient Filter: </b><input type=text id=patientNameFilter name=patientNameFilter value='A*' size=10> 
<div id=pacdiv><div>
<script>
var leftdata = "images";

function showImages(pacID)
{
	window.parent.rightpanel.location = "pacsdata.jsp?pacID=" + pacID + "&patientNameFilter=" + document.getElementById("patientNameFilter").value;
	window.parent.buttons.pacID = pacID;
	window.parent.buttons.patientNameFilter = document.getElementById("patientNameFilter").value;
}

$( document ).ready(function() {
	var url = "<%=request.getContextPath()%>/v2/pacs/?username=<%=username%>";
	//alert(url);
	$.ajax({         
		url: url,         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting pacs");
			return true;},
		success: function(response){
			var pacs = response.ResultSet.Result;
			var html = "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>ID</td><td>AETitle</td><td>Host</td><td>Port</td></tr>";
			for (i = 0; i < pacs.length; i++)
			{
				var line = "<tr><td><span onclick=\"javascript:showImages('" + pacs[i].pacID + "')\"><u>" + pacs[i].pacID + "</u></span></td><td>" + pacs[i].aeTitle + "</td><td>"  +  pacs[i].hostname + "</td><td>" + pacs[i].port + "</td></tr>\n";
				html = html + line;
			}
			html = html + "</table>\n";
			document.getElementById("pacdiv").innerHTML = html;
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
