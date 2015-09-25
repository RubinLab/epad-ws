<%@ page language="java"%>
<%@ page import="java.io.*"%>
<%@ page import="edu.stanford.epad.common.util.*"%>
<%@ page import="edu.stanford.epad.epadws.service.*"%><%@ page import="edu.stanford.epad.epadws.security.*"%>
<%@ page session="false" %><%
	String sessionID = SessionService.getJSessionIDFromRequest(request);
	String username = EPADSessionOperations.getSessionUser(sessionID);
	String wlusername = request.getParameter("wlusername");
	String workListID = request.getParameter("workListID");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<TITLE>Add Study</TITLE>
 <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
</HEAD>

<BODY bgcolor=white>
<h3>Add Study To Worklist<h3>
<b>WorkList:<%=workListID%>
<br><b>Projects:</b>
<div id=projdiv></div>
<br><b>Subjects:</b>
<div id=subjdiv></div>
<br><b>Studies:</b>
<div id=studydiv></div>
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
				var line = "<tr><td><span onclick=\"getSubjects('" + projects[i].id +"')\"><u>" + projects[i].name + "</u></span></td><td>" + projects[i].id + "</td></tr>\n";
				if (projects[i].name == 'All' || projects[i].name == 'Unassigned')
				{
					continue;
				}
				html = html + line;
			}
			html = html + "</table>\n";
			document.getElementById("projdiv").innerHTML = html;
		}
	});

   });

   function getSubjects(projectID)
   {
	   	var url = "<%=request.getContextPath()%>/v2/projects/" + projectID + "/subjects/" + "?username=<%=username%>";
		//alert(url);
		$.ajax({         
			url: url,         
			type: 'get',         
			async: false,         
			cache: false,         
			timeout: 30000,         
			error: function(){
				alert("Error getting subjects");
				return true;},
			success: function(response){
				var subjects = response.ResultSet.Result;
				var html = "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>Name</td><td>ID</td></tr>";
				for (i = 0; i < subjects.length; i++)
				{
					var line = "<tr><td><span onclick=\"getStudies('" + projectID + "','" + subjects[i].subjectID +"')\"><u>" + subjects[i].subjectName + "</u></span></td><td>" + subjects[i].subjectID + "</td></tr>\n";
					html = html + line;
				}
				html = html + "</table>\n";
				document.getElementById("subjdiv").innerHTML = html;
			}
		});

   }

   function getStudies(projectID, subjectID)
   {
	   	var url = "<%=request.getContextPath()%>/v2/projects/" + projectID + "/subjects/" + subjectID + "/studies/" + "?username=<%=username%>";
		//alert(url);
		$.ajax({         
			url: url,         
			type: 'get',         
			async: false,         
			cache: false,         
			timeout: 30000,         
			error: function(){
				alert("Error getting studies");
				return true;},
			success: function(response){
				var studies = response.ResultSet.Result;
				var html = "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>Name</td><td>ID</td></tr>";
				for (i = 0; i < studies.length; i++)
				{
					var line = "<tr><td><span onclick=\"addToWorkList('" + projectID + "','" + subjectID + "','" + studies[i].studyUID +"')\"><u>" + studies[i].studyDescription + "</u></span></td><td>" + studies[i].studyUID + "</td></tr>\n";
					html = html + line;
				}
				html = html + "</table>\n";
				document.getElementById("studydiv").innerHTML = html;
			}
		});

   }


   function addToWorkList(projectID, subjectID, studyUID)
   {
		var url = "<%=request.getContextPath()%>/v2/users/<%=wlusername%>/worklists/<%=workListID%>/projects/" + projectID + "/studies/"  + studyUID + "?username=<%=username%>";
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
				window.location='worklist.jsp?workListID=<%=workListID%>';
			}
		});
   }
</script>
</BODY>
</HTML>
