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
			String projectID = request.getParameter("projectID");
%>
<table>
</table>
<script>
$( document ).ready(function() {
	var url = "/epad/v2/projects/<%=projectID%>/subjects/";
	$.ajax({         
		url: url + "?username=<%=username%>",         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting subjects");
			return true;},
		success: function(response){
			var subjects = response.ResultSet.Result;
			document.write("<table border=1><tr bgcolor=lightgray><td>Name</td><td>ID</td><td>Studies/Series/Annotations</td></tr>");
			for (i = 0; i < subjects.length; i++)
			{
				document.write("<tr><td><a href='images.jsp?projectID=" + subjects[i].id + "' target='rightpanel'>" + subjects[i].subjectName + "</a></td><td>" + subjects[i].subjectID + "</td><td>"  +  subjects[i].numberOfStudies + " / " + subjects[i].numberOfAnnotations + "</td></tr>\n");
				var url2 = url + subjects[i].subjectID + "/studies/";
				$.ajax({         
					url: url2 + "?username=<%=username%>",         
					type: 'get',         
					async: false,         
					cache: false,         
					timeout: 30000,         
					error: function(){
						alert("Error getting studies");
						return true;},
					success: function(response){
						var studies = response.ResultSet.Result;
						for (j = 0; j < studies.length; j++)
						{
							document.write("<tr><td>&nbsp;&nbsp;&nbsp;<a href='images.jsp?projectID=" + studies[j].studyUID + "' target='rightpanel'>" + studies[j].studyDescription + "</a></td><td>" + studies[j].studyUID + "</td><td>"  +  studies[j].numberOfSeries + " / " + studies[j].numberOfAnnotations + "</td></tr>\n");
							var url3 = url2 + studies[j].studyUID + "/series/";
							$.ajax({         
								url: url3 + "?username=<%=username%>",         
								type: 'get',         
								async: false,         
								cache: false,         
								timeout: 30000,         
								error: function(){
									alert("Error getting series");
									return true;},
								success: function(response){
									var series = response.ResultSet.Result;
									for (k = 0; k < series.length; k++)
									{
										document.write("<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='images.jsp?projectID=" + series[k].seriesUID + "' target='rightpanel'>" + series[k].seriesDescription + "</a></td><td>" + series[k].seriesUID + "</td><td>na / "  +  series[k].numberOfAnnotations + "</td></tr>\n");
										var url4 = url3 + series[k].seriesUID + "/aims/?format=summary";
										$.ajax({         
											url: url4 + "&username=<%=username%>",         
											type: 'get',         
											async: false,         
											cache: false,         
											timeout: 30000,         
											error: function(){
												alert("Error getting aims");
												return true;},
											success: function(response){
												var aims = response.ResultSet.Result;
												for (l = 0; l < aims.length; l++)
												{
													document.write("<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='images.jsp?projectID=" + aims[l].aimID + "' target='rightpanel'>" + aims[l].name + " (" + aims[l].userName  + ")</a></td><td>" + aims[l].aimID + "</td><td>"  +  aims[l].template + "</td></tr>\n");
												}
											}
										})
									}
								}
							})
						}
					}
				})
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
