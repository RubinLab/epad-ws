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
			String pacID = request.getParameter("pacID");
			String patientNameFilter =  request.getParameter("patientNameFilter");
			if (patientNameFilter == null)
				patientNameFilter = "A*";
%>
<h2>PAC: <%=pacID%></h2>
<% if (!pacID.startsWith("tcia")) { %>
<b>Patient Filter: </b> <%=patientNameFilter%>
<% } %>
<br><b>Download To Project: <b> <select name=projectID id=projsel></select>
<br>
<div id=imagelist><div>
<table>
</table>
<script>
$( document ).ready(function() {
	var listdata;
	var url = "<%=request.getContextPath()%>/v2/pacs/<%=pacID%>/subjects/";
	$.ajax({         
		url: url + "?username=<%=username%>&patientNameFilter=<%=patientNameFilter%>",         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting subjects");
			return true;},
		success: function(response){
			var subjects = response.ResultSet.Result;
			listdata = "<table border=1><tr bgcolor=lightgray><td>Type</td><td>Name</td><td>ID</td><td>In Epad</td></tr>\n";
			for (i = 0; i < subjects.length; i++)
			{
				listdata =  listdata + "<tr><td>Patient</td><td><a href='javascript:getStudies(" + i +",\"" + url + "\",\"" + subjects[i].subjectID + "\")'>" + subjects[i].subjectName + "</a></td><td>" + subjects[i].subjectID + "</td><td>" + subjects[i].inEpad + "</td></tr>\n";
				listdata =  listdata + "<tr><td colspan=4 id='study" + i + "'></td></tr>\n";
			}

			listdata =  listdata + "</table>\n";
			document.getElementById("imagelist").innerHTML = listdata;
		}
	});
	url = "<%=request.getContextPath()%>/v2/projects/?username=<%=username%>";
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
			var projsel = document.getElementById("projsel");
			for (i = 0; i < projects.length ; i++)
			{
				var opt = document.createElement('option');
				opt.text = projects[i].name;
				opt.value = projects[i].id;
				projsel.appendChild(opt);
			}
		}
	});

   });

   function getStudies(row, url, subjectID)
   {
		var listdata = "<table border=1 width=100%>\n";
				var url2 = url + subjectID + "/studies/";
				$.ajax({         
					url: url2 + "?username=<%=username%>",         
					type: 'get',         
					async: false,         
					cache: false,         
					timeout: 30000,         
					error: function(){
						var err = response.responseText;
						if (err != null)
						{
							//alert(JSON.stringify(err));
							err = JSON.parse(err).message;
						}
						alert("Error getting studies " + err);
						//return true;
						},
					success: function(response){
						var studies = response.ResultSet.Result;
						for (j = 0; j < studies.length; j++)
						{
							listdata =  listdata + "<tr><td><img src=download-icon.gif onclick=\"download('" + studies[j].entityID + "')\" height=20px></td><td>" + studies[j].entityType + "</td><td>&nbsp;&nbsp;&nbsp;<a href='javascript:getSeries(" + j +",\"" + url2 + "\",\"" + studies[j].entityID + "\")'>" + studies[j].entityValue + "</a></td><td>" + studies[j].entityID + "</td><td>"  + studies[j].inEpad + "</td></tr>\n";
							listdata =  listdata + "<tr><td colspan=4 id='series" + j + "'></td></tr>\n";
						}
					}
				})
			listdata =  listdata + "</table>\n";
			document.getElementById("study" + row).innerHTML = listdata;
   }

	function getSeries(row, url, studyUID)
	{
		var listdata = "<table border=1 width=100%>\n";
		var url3 = url + studyUID + "/series/";
		//alert(url3);
		$.ajax({         
			url: url3 + "?username=<%=username%>",         
			type: 'get',         
			async: false,         
			cache: false,         
			timeout: 30000,         
			error: function(){
				var err = response.responseText;
				if (err != null)
				{
					//alert(JSON.stringify(err));
					err = JSON.parse(err).message;
				}
				alert("Error getting series " + err);
				//return true;
			},
			success: function(response){
				var series = response.ResultSet.Result;
				for (k = 0; k < series.length; k++)
				{
					listdata =  listdata + "<tr><td><img src=download-icon.gif onclick=\"download('" + series[j].entityID + "')\"height=20px/></td><td>" + series[k].entityType + "</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + series[k].entityValue + "</td><td>" + series[k].entityID + "</td><td>"  +  series[k].inEpad + "</td></tr>\n";
				}
			}
		})
		listdata =  listdata + "</table>\n";
		document.getElementById("series" + row).innerHTML = listdata;
	
	}
	function download(entityID)
	{
		var projsel = document.getElementById("projsel");
		var url = "<%=request.getContextPath()%>/v2/pacs/<%=pacID%>/entities/" + entityID;
		$.ajax({         
			url: url + "?username=<%=username%>&projectID=" + projsel.options[projsel.selectedIndex].value,        
			type: 'get',         
			async: false,         
			cache: false,         
			timeout: 30000,         
			error: function(response){
				var err = response.responseText;
				if (err != null)
				{
					//alert(JSON.stringify(err));
					err = JSON.parse(err).message;
				}
				alert("Error downloading:" + err);
				return true;},
			success: function(response){
				alert("Data transfer has been initiated");
			}
		});

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
