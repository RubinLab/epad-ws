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
<h2>Project: <%=projectID%> (<a href=upload.jsp?projectID=<%=projectID%>>Upload</a>)</h2>
<div id=imagelist><div>
<script>
var filedata;
$( document ).ready(function() {
	var url = "/epad/v2/projects/<%=projectID%>/files/";
	$.ajax({         
		url: url + "?username=<%=username%>",         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting files");
			return true;},
		success: function(response){
			var files = response.ResultSet.Result;
			filedata = "<table border=1><tr bgcolor=lightgray><td></td><td>Name</td><td>Description</td><td>Type</td><td>Length</td><td>Created</td></tr>\n";
			for (i = 0; i < files.length; i++)
			{
				filedata = filedata + "<tr><td><img src=delete.jpg height=10px onclick='deleteFile(\"<%=projectID%>\",null,null,null,\"" + files[i].fileName +"\")'/></td><td><a href='download.jsp?path=" + files[i].path + "&name=" + files[i].fileName+ "'>" + files[i].fileName + "</a></td><td>" + files[i].description + "</td><td>" + files[i].fileType + "</td><td>" + files[i].fileLength + "</td><td>"  +  files[i].createdTime + "</td></tr>\n";
			}
		}
	})
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
			filedata = filedata + "<table border=1><tr bgcolor=lightgray><td>Type</td><td>Name</td><td>ID</td><td>Studies/Series/Annotations</td><td>Size</td><td>Date</td></tr>";
			for (i = 0; i < subjects.length; i++)
			{
				filedata = filedata + "<tr><td>Patient (<a href=upload.jsp?projectID=<%=projectID%>&subjectID=" + subjects[i].subjectID + ">Upload</a>)</td><td><a href='images.jsp?projectID=" + subjects[i].id + "' target='rightpanel'>" + subjects[i].subjectName + "</a></td><td>" + subjects[i].subjectID + "</td><td>"  +  subjects[i].numberOfStudies + " / " + subjects[i].numberOfAnnotations + "</td></tr>\n";
				var urlf = url + subjects[i].subjectID + "/files/";
				$.ajax({         
					url: urlf + "?username=<%=username%>",         
					type: 'get',         
					async: false,         
					cache: false,         
					timeout: 30000,         
					error: function(){
						alert("Error getting files");
						return true;},
					success: function(response){
						var files = response.ResultSet.Result;
						//filedata = "<table border=1><tr bgcolor=lightgray><td>Name</td><td>Type</td><td>Length</td><td>Created</td></tr>\n";
						for (f = 0; f < files.length; f++)
						{
							filedata = filedata + "<tr><td>File</td><td><a href='" + files.path + "'>" + files[f].fileName + "</a></td><td>" + files[f].description + "</td><td>" + files[f].fileType + "</td><td>" + files[f].fileLength + "</td><td>"  +  files[f].createdTime + "</td></tr>\n";
						}
					}
				})
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
							filedata = filedata + "<tr><td>Study(<a href=upload.jsp?projectID=<%=projectID%>&subjectID=" + subjects[i].subjectID + "&studyUID=" + studies[j].studyUID + ">Upload</a>)</td><td>&nbsp;&nbsp;&nbsp;<a href='images.jsp?projectID=" + studies[j].studyUID + "' target='rightpanel'>" + studies[j].studyDescription + "</a></td><td>" + studies[j].studyUID + "</td><td>"  +  studies[j].numberOfSeries + " / " + studies[j].numberOfAnnotations + "</td></tr>\n";
							var urlf = url2 + studies[j].studyUID + "/files/";
							$.ajax({         
								url: urlf + "?username=<%=username%>",         
								type: 'get',         
								async: false,         
								cache: false,         
								timeout: 30000,         
								error: function(){
									alert("Error getting files");
									return true;},
								success: function(response){
									var files = response.ResultSet.Result;
									//filedata = "<table border=1><tr bgcolor=lightgray><td>Name</td><td>Type</td><td>Length</td><td>Created</td></tr>\n";
									for (f = 0; f < files.length; f++)
									{
										filedata = filedata + "<tr><td>File</td><td><a href='" + files.path + "'>" + files[f].fileName + "</a></td><td>" + files[f].description + "</td><td>" + files[f].fileType + "</td><td>" + files[f].fileLength + "</td><td>"  +  files[f].createdTime + "</td></tr>\n";
									}
								}
							})
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
										filedata = filedata + "<tr><td>Series(<a href=upload.jsp?projectID=<%=projectID%>&subjectID=" + subjects[i].subjectID + "&studyUID=" + studies[j].studyUID + "&seriesUID=" + series[k].seriesUID + ">Upload</a>)</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='images.jsp?projectID=" + series[k].seriesUID + "' target='rightpanel'>" + series[k].seriesDescription + "</a></td><td>" + series[k].seriesUID + "</td><td>na / "  +  series[k].numberOfAnnotations + "</td></tr>\n";
										var urlf = url3 + series[k].seriesUID + "/files/";
										$.ajax({         
											url: urlf + "?username=<%=username%>",         
											type: 'get',         
											async: false,         
											cache: false,         
											timeout: 30000,         
											error: function(){
												alert("Error getting files");
												return true;},
											success: function(response){
												var files = response.ResultSet.Result;
												//filedata = "<table border=1><tr bgcolor=lightgray><td>Name</td><td>Type</td><td>Length</td><td>Created</td></tr>\n";
												for (f = 0; f < files.length; f++)
												{
													filedata = filedata + "<tr><td>File</td><td><a href='" + files.path + "'>" + files[f].fileName + "</a></td><td>" + files[f].description + "</td><td>" + files[f].fileType + "</td><td>" + files[f].fileLength + "</td><td>"  +  files[f].createdTime + "</td></tr>\n";
												}
											}
										})
//										var url4 = url3 + series[k].seriesUID + "/aims/?format=summary";
//										$.ajax({         
//											url: url4 + "&username=<%=username%>",         
//											type: 'get',         
//											async: false,         
//											cache: false,         
//											timeout: 30000,         
//											error: function(){
//												alert("Error getting aims");
//												return true;},
//											success: function(response){
//												var aims = response.ResultSet.Result;
//												for (l = 0; l < aims.length; l++)
//												{
//													filedata = filedata + "<tr><td>Aims</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='images.jsp?projectID=" + aims[l].aimID + "' target='rightpanel'>" + aims[l].name + " (" + aims[l].userName  + ")</a></td><td>" + aims[l].aimID + "</td><td>"  +  aims[l].template + "</td></tr>\n";
//												}
//											}
//										})
									}
								}
							})
						}
					}
				})
			}
			filedata =  filedata + "</table>\n";
			document.getElementById("imagelist").innerHTML = filedata;
		}
	});

   });
   
   function deleteFile(projectID, subjectID, studyUID, seriesUID, filename)
   {
	   url = "/epad/v2/projects/" + projectID + "/";
	   if (subjectID != null)
	   {
		   url = url + subjectID + "/";
	   }
	   if (studyUID != null)
	   {
		   url = url + studyUID + "/";
	   }
	   if (seriesUID != null)
	   {
		   url = url + seriesUID + "/";
	   }
	   url = url + "files/" + filename
		   alert(url);
		$.ajax({         
			url: url + "?username=<%=username%>",         
			type: 'delete',         
			async: false,         
			cache: false,         
			timeout: 30000,         
			error: function(){
				alert("Error deleting file");
				return true;},
			success: function(response){
			}
		})
	}

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
