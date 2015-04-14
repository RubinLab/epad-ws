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
<br>
<table align=center border=0 cellpadding=3 cellspacing=0 width=100%>
<tr><td width=33% align=center><a href='javascript:files()'>Files</a></td><td width=33% align=center><a href='javascript:aims()'>Annotations</a></td><td width=33% align=center><a href='javascript:pacs()' >PACs</a></td></tr>
</table>
<script>
var projectID;
$( document ).ready(function() {
	var url = "/epad/v2/projects/?username=<%=username%>";
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
			projectID = projects[0].id
		}
	});

   });
var currentleft = "projects";
function files()
{
	window.parent.rightpanel.location = "files.jsp?projectID=" + projectID;
	if (currentleft != "projects")
	{
		window.parent.leftpanel.location = "projects.jsp";
		currentleft = "projects";
	}
	window.parent.leftpanel.leftdata = "files";
	currentleft = "files";
}
function aims()
{
	window.parent.rightpanel.location = "images.jsp?projectID=" + projectID;
	if (currentleft != "projects")
	{
		window.parent.leftpanel.location = "projects.jsp";
		currentleft = "projects";
	}
	window.parent.leftpanel.leftdata = "images";
}
function pacs()
{
	window.parent.leftpanel.location = "pacs.jsp";
	currentleft = "pacs";
	//window.parent.rightpanel.location = "pacs.jsp?projectID=" + projectID;
	//window.parent.leftpanel.leftdata = "pacs";
}
</script>
</BODY>
</HTML>
