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
<h2>Users</h2>
<div id=userdiv><div>
<script>

function showUser(username)
{
	window.parent.rightpanel.location = "user.jsp?username=" + username;
	window.parent.buttons.username = username;
}

$( document ).ready(function() {
	var url = "<%=request.getContextPath()%>/v2/users/?username=<%=username%>";
	//alert(url);
	$.ajax({         
		url: url,         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting users");
			return true;},
		success: function(response){
			var users = response.ResultSet.Result;
			var html = "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>Username</td><td>First</td><td>Last</td><td>Email</td></tr>";
			for (i = 0; i < users.length; i++)
			{
				var line = "<tr><td><span onclick=\"javascript:showUser('" + users[i].username + "')\"><u>" + users[i].username + "</u></span></td><td>" + users[i].firstname + "</td><td>"  +  users[i].lastname + "</td><td>" + users[i].email + "</td></tr>\n";
				html = html + line;
			}
			html = html + "</table>\n";
			document.getElementById("userdiv").innerHTML = html;
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
