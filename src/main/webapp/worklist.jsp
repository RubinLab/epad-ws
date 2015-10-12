<%@ page language="java"%>
<%@ page import="edu.stanford.epad.epadws.service.*"%>
<%@ page import="edu.stanford.epad.epadws.security.*"%>
<%@ page import="edu.stanford.epad.epadws.models.*"%>
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
			String workListID = request.getParameter("workListID");
			WorkList wl = DefaultWorkListOperations.getInstance().getWorkList(workListID);
			User user = (User) DefaultEpadProjectOperations.getInstance().getDBObject(User.class, wl.getUserId());
			String username = user.getUsername();
%>
<h2>User: <%=username%></h2>
<h3>WorkList: <%=workListID%></h3>
<b>Description:</b><%=wl.getDescription()%>
<br><b>Due Date:</b><%=wl.getDueDate()%>
<br><b>Start:</b><%=wl.getStartDate()%>
<br><b>Complete:</b><%=wl.getCompleteDate()%>
<br><b>Status:</b><%=wl.getStatus()%>
<br><br>
<b>Subjects:</b>(<a href="addSubjectToWorkList.jsp?wlusername=<%=username%>&workListID=<%=workListID%>">Add New</a>)

<div id=subjectdata></div>
<br><b>Studies:</b>(<a href="addStudyToWorkList.jsp?wlusername<%=username%>&workListID=<%=workListID%>">Add New</a>)
<div id=studydata></div>
<table>
</table>
<script>
$( document ).ready(function() {
	var listdata;
	var url = "<%=request.getContextPath()%>/v2/users/<%=username%>/worklists/<%=workListID%>/subjects/";
	$.ajax({         
		url: url + "?username=<%=loggedInUser%>",         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting worklist subjects");
			return true;},
		success: function(response){
			var logs = response.ResultSet.Result;
			if (logs.length > 0) {
				var html = "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>SubjectID</td><td>Subject Name</td><td>ProjectID</td><td>Start</td><td>Complete</td><td>Status</td></tr>";
				for (i = 0; i < logs.length; i++)
				{
					var line = "<tr><td><a href=subject.jsp?subjectID=" + logs[i].subjectID + "&projectID=" + logs[i].projectID+">" + logs[i].subjectID + "</a></td>";
					line = line + "<td>" + logs[i].subjectName + "</td>";
					line = line + "<td>" + logs[i].projectID + "</td>";
					line = line + "<td>" + logs[i].startDate + "</td>";
					line = line + "<td>" + logs[i].completionDate + "</td>";
					line = line + "<td>" + logs[i].status + "</td>";
					line = line + "</tr>\n";
					html = html + line;
				}
				html = html + "</table>\n";
				document.getElementById("subjectdata").innerHTML = html;
			} else {
				document.getElementById("subjectdata").innerHTML = "No subjects in worklist";
			}
		}
	});
	url = "<%=request.getContextPath()%>/v2/users/<%=username%>/worklists/<%=workListID%>/studies/";
	$.ajax({         
		url: url + "?username=<%=loggedInUser%>",         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting worklist studies");
			return true;},
		success: function(response){
			var logs = response.ResultSet.Result;
			if (logs.length > 0) {
				var html = "<table border=1 cellpadding=2><tr bgcolor=lightgray><td>StudyUID</td><td>SubjectID</td><td>ProjectID</td><td>Start</td><td>Complete</td><td>Status</td></tr>";
				for (i = 0; i < logs.length; i++)
				{
					var line = "<tr><td><a href=study.jsp?studyUID=" + logs[i].studyUID + "&subjectID=" + logs[i].subjectID + "&projectID=" + logs[i].projectID+">" + logs[i].studyUID + "</a></td>";
					line = line + "<td>" + logs[i].subjectID + "</td>";
					line = line + "<td>" + logs[i].projectID + "</td>";
					line = line + "<td>" + logs[i].startDate + "</td>";
					line = line + "<td>" + logs[i].completionDate + "</td>";
					line = line + "<td>" + logs[i].status + "</td>";
					line = line + "</tr>\n";
					html = html + line;
				}
				html = html + "</table>\n";
				document.getElementById("studydata").innerHTML = html;
			} else {
				document.getElementById("studydata").innerHTML = "No studies in worklist";
			}
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
   function addSubject(subjectID, projectID)
   {
	url = "<%=request.getContextPath()%>/v2/users/<%=username%>/worklists/<%=workListID%>/projects/" + projectID + "/subjects/" + subjectID;
	$.ajax({         
		url: url + "?username=<%=loggedInUser%>",         
		type: 'put',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error adding subject to  worklist");
			return true;},
		success: function(response){
		}
	});
   }
  function addStudy(studyUID, projectID)
   {
	url = "<%=request.getContextPath()%>/v2/users/<%=username%>/worklists/<%=workListID%>/projects/" + projectID + "/studies/" + studyUID;
	$.ajax({         
		url: url + "?username=<%=loggedInUser%>",         
		type: 'put',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error adding study to  worklist");
			return true;},
		success: function(response){
		}
	});
   }

   function getProjects()
   {
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
			return response.ResultSet.Result;
		}
	});
   }

   function getSubjects(projectID)
   {
	var url = "<%=request.getContextPath()%>/v2/projects/" + projectID + "/subjects/?username=<%=username%>";
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
			return response.ResultSet.Result;
		}
	});
   }

</script>
</BODY>
</HTML>
