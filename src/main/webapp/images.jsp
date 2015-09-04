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
			String projectID = request.getParameter("projectID");
			String unassignedOnly = "";
			if (request.getParameter("unassignedOnly") != null)
				unassignedOnly = "&unassignedOnly=" + request.getParameter("unassignedOnly");
%>
<h2>Project: <%=projectID%></h2>
<div id=imagelist><div>
<table>
</table>
<script>
$( document ).ready(function() {
	var listdata;
	var url = "<%=request.getContextPath()%>/v2/projects/<%=projectID%>/subjects/";
	$.ajax({         
		url: url + "?username=<%=username%><%=unassignedOnly%>",         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting subjects");
			return true;},
		success: function(response){
			var subjects = response.ResultSet.Result;
			listdata = "<table border=1><tr bgcolor=lightgray><td>Type</td><td>Name</td><td>ID</td><td>Studies/Series<br>Images/Annotations</td></tr>\n";
			for (i = 0; i < subjects.length; i++)
			{
				listdata =  listdata + "<tr id='Patient" + i + "' ><td>Patient(<a href='addToProject.jsp?subjectID=" +  subjects[i].subjectID + "' target='rightpanel'>Add To Project</a>)</td><td><a href='images.jsp?projectID=" + subjects[i].id + "' target='rightpanel'>" + subjects[i].subjectName + "</a>" + "&nbsp;&nbsp;<a href='javascript:deletePatient(\"" +  subjects[i].subjectID + "\",\"" + i + "\")'><img src=delete.jpg height=12px></a></td><td>" + subjects[i].subjectID + "</td><td>"  +  subjects[i].numberOfStudies + " / " + subjects[i].numberOfAnnotations + "</td></tr>\n";
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
							listdata =  listdata + "<tr><td>Study</td><td>&nbsp;&nbsp;&nbsp;" + studies[j].studyDescription + "&nbsp;&nbsp;<a href='javascript:downloadStudy(\"" +  subjects[i].subjectID + "\",\"" + studies[j].studyUID + "\")'><img src=download-icon.gif height=12px></a></td><td>" + studies[j].studyUID + "</td><td>"  +  studies[j].numberOfSeries + " / " + studies[j].numberOfAnnotations + "</td></tr>\n";
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
										if (series[k].seriesDescription == null || series[k].seriesDescription == '')
										{
											series[k].seriesDescription = 'n/a';
										}
										listdata =  listdata + "<tr><td nowrap>Series(<a href=createDSO.jsp?projectID=<%=projectID%>&subjectID=" + subjects[i].subjectID + "&studyUID=" + studies[j].studyUID + "&seriesUID=" + series[k].seriesUID + ">Create DSO</a>)(<a href=javascript:regen('" + series[k].seriesUID + "')>Regenerate PNGs</a>)</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=series.jsp?projectID=<%=projectID%>&subjectID=" + subjects[i].subjectID + "&studyUID=" + studies[j].studyUID + "&seriesUID=" + series[k].seriesUID + ">" + series[k].seriesDescription + "</a>&nbsp;&nbsp;<a href='javascript:downloadSeries(\"" +  subjects[i].subjectID + "\",\"" + studies[j].studyUID + "\",\"" + series[k].seriesUID + "\")'><img src=download-icon.gif height=12px></a></td><td>" + series[k].seriesUID + "</td><td>" + series[k].numberOfImages + " / "  +  series[k].numberOfAnnotations + "</td></tr>\n";
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
													if (aims[l].dsoSeriesUID != null && aims[l].dsoSeriesUID != "")
													{
														listdata =  listdata + "<tr><td>Aims</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='images.jsp?projectID=" + aims[l].aimID + "' target='rightpanel'>" + aims[l].name + " (" + aims[l].userName  + ")</a></td><td>" + aims[l].aimID + "/ " + aims[l].dsoSeriesUID + "(<a href=javascript:regen('" + aims[l].dsoSeriesUID + "')>Regenerate PNGs</a>)</td><td>"  +  aims[l].template + "/" + aims[l].templateType + "</td></tr>\n";
													}
													else
													{
														listdata =  listdata + "<tr><td>Aims</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='images.jsp?projectID=" + aims[l].aimID + "' target='rightpanel'>" + aims[l].name + " (" + aims[l].userName  + ")</a></td><td>" + aims[l].aimID + "</td><td>"  +  aims[l].template + "/" + aims[l].templateType + "</td></tr>\n";
													}
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
			listdata =  listdata + "</table>\n";
			document.getElementById("imagelist").innerHTML = listdata;
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

   function regen(seriesUID)
   {
		var url = "<%=request.getContextPath()%>/imagecheck/?seriesUID=" + seriesUID;
		window.open(url, "imagecheck", "width=200, height=100");
   }
   
   function downloadPatient(subjectID, studyUID)
   {
	   if (!confirm("Download Patient " + subjectID + "?"))
	   {
		   return;
	   }
		var url = "<%=request.getContextPath()%>/v2/projects/<%=projectID%>/subjects/" + subjectID + "?username=<%=username%>&format=stream";
		window.open(url);
   }
   
   function downloadStudy(subjectID, studyUID)
   {
	   if (!confirm("Download Study " + studyUID + "?"))
	   {
		   return;
	   }
		var url = "<%=request.getContextPath()%>/v2/projects/<%=projectID%>/subjects/" + subjectID + "/studies/" + studyUID + "?username=<%=username%>&format=stream";
		window.open(url);
   }
   
   function downloadSeries(subjectID, studyUID, seriesUID)
   {
	   if (!confirm("Download Series " + seriesUID + "?"))
	   {
		   return;
	   }
		var url = "<%=request.getContextPath()%>/v2/projects/<%=projectID%>/subjects/" + subjectID + "/studies/" + studyUID +  + "/series/" + seriesUID + "?username=<%=username%>&format=stream";
		window.open(url);
   }

   function deletePatient(subjectID , lineno)
   {
	   if (!confirm("Delete Patient " + subjectID + "?"))
	   {
		   return;
	   }
		var url = "<%=request.getContextPath()%>/v2/projects/<%=projectID%>/subjects/" + subjectID + "?username=<%=username%>";
		$.ajax({         
			url: url,         
			type: 'delete',         
			async: false,         
			cache: false,         
			timeout: 30000,         
			error: function(){
				alert("Error deleting patient");
				return true;},
			success: function(response){
					var row = document.getElementById("Patient" + line);
					row.style.textDecoration = "line-through";
			}
		})

   }

</script>
</BODY>
</HTML>
