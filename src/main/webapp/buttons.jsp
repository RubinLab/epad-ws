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
			String username = EPADSessionOperations.getSessionUser(sessionID);
%>
<br>
<table align=center border=0 cellpadding=3 cellspacing=0 width=100%>
<tr><td width=25% align=center><a href='javascript:files()'>Files</a></td><td width=25% align=center><a href='javascript:aims()'>Annotations</a></td><td width=25% align=center><a href='javascript:pacs()' >PACs</a></td><td width=25% align=center><a href='javascript:users()' >Users</a></td></tr>
<tr><td width=100% align=center colspan=4><a href='javascript:logout()'>Logout</a></td></tr>
</table>
<script>
var projectID;
var patientNameFilter = "A*";
var pacID;
var username;

$( document ).ready(function() {
	var url = "<%=request.getContextPath()%>/v2/projects/?username=<%=username%>";
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
	var url = "<%=request.getContextPath()%>/v2/pacs/?username=<%=username%>";
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
			pacID = pacs[0].pacID
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
	if (pacID != null)
		window.parent.rightpanel.location = "pacsdata.jsp?pacID=" + pacID + "&patientNameFilter=" + patientNameFilter;
	if (currentleft != "pacs")
	{
		window.parent.leftpanel.location = "pacs.jsp";
		currentleft = "pacs";
	}
	window.parent.leftpanel.leftdata = "pacsdata";
}
function users()
{
	if (username != null)
		window.parent.rightpanel.location = "user.jsp?username=" + username;
	if (currentleft != "users")
	{
		window.parent.leftpanel.location = "users.jsp";
		currentleft = "users";
	}
	window.parent.leftpanel.leftdata = "usersdata";
}
function logout()
{
	var url = "<%=request.getContextPath()%>/session";
	$.ajax({         
		url: url,         
		type: 'delete',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error logging out");
			window.top.location = "login.jsp";
			return true;},
		success: function(response){
			window.top.location = "login.jsp";
		}
	});
}
</script>
</BODY>
</HTML>
