<%@ page language="java"%>
<%@ page import="java.io.*"%>
<%@ page import="edu.stanford.epad.common.util.*"%>
<%@ page import="edu.stanford.epad.epadws.service.*"%><%@ page import="edu.stanford.epad.epadws.security.*"%>
<%@ page session="false" %><%
	String sessionID = SessionService.getJSessionIDFromRequest(request);
	String username = EPADSessionOperations.getSessionUser(sessionID);
	String projectID = request.getParameter("projectID");
	if (projectID == null)
	{
%><script> alert('ProjectID is null');</script><%
		return;
	}
	String subjectID = request.getParameter("subjectID");
	String studyUID = request.getParameter("studyUID");
	String seriesUID = request.getParameter("seriesUID");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<TITLE>Create DSO</TITLE>
 <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
</HEAD>

<BODY bgcolor=white>
<div id=imagelist></div>
 Project: <%=projectID%><br>
 Patient: <%=subjectID%><br>
 Study: <%=studyUID%><br>
 Series: <%=seriesUID%><br>
 DSO Name: <input type=text id=dsoName name=dsoName size=20><br>
 Mask Frame Numbers: <input type=text id="framenumbers" name="framenumbers" size="40"> (comma separated, 0 - max frames-1) 
 <form name="uploadform" id="uploadform"  action="<%=request.getContextPath()%>/v2/projects/<%=projectID%>/subjects/<%=subjectID%>/studies/<%=studyUID%>/series/<%=seriesUID%>/images/new/frames/?username=<%=username%>&type=new" method="post" enctype="multipart/form-data" accept-charset=utf-8>
  <!-- JSON File:  input type=file name=file0 --><br>
  Mask 1: <input type=file name=file1><br>
  Mask 2: <input type=file name=file2><br>
  Mask 3: <input type=file name=file3><br>
  Mask 4: <input type=file name=file4><br>
  Mask 5: <input type=file name=file5><br>
  Mask 6: <input type=file name=file6><br>
  Mask 7: <input type=file name=file7><br>
  Mask 8: <input type=file name=file8><br>
  Mask 9: <input type=file name=file9><br>
  Mask 10: <input type=file name=file10><br>
  Mask 11: <input type=file name=file11><br>
  Mask 12: <input type=file name=file12><br>
  Mask 13: <input type=file name=file13><br>
  Mask 14: <input type=file name=file14><br>
  Mask 15: <input type=file name=file15><br>
  Mask 16: <input type=file name=file16><br>
  Mask 17: <input type=file name=file17><br>
  Mask 18: <input type=file name=file18><br>
  Mask 19: <input type=file name=file19><br>
  Mask 20: <input type=file name=file20><br>
  <input type="button" value="Submit" onclick="uploadFiles()">
</form>
<script>
var filedata;
function uploadFiles()
{
	var form = document.getElementById("uploadform");
	var nameparm = "";
	if (document.getElementById("dsoName").value != "")
	{
		nameparm = "&name=" + document.getElementById("dsoName").value
	}
	form.action = form.action + "&editedFrameNumbers=" + document.getElementById("framenumbers").value + nameparm;
	form.submit();
}
$( document ).ready(function() {
	var url = "<%=request.getContextPath()%>/v2/projects/<%=projectID%>/subjects/<%=subjectID%>/studies/<%=studyUID%>/series/<%=seriesUID%>/aims/?format=summary";
	//alert(url);
	$.ajax({         
		url: url + "&username=<%=username%>",         
		type: 'get',         
		async: false,         
		cache: false,         
		timeout: 30000,         
		error: function(){
			alert("Error getting Annotations");
			return true;},
		success: function(response){
			var aims = response.ResultSet.Result;
			filedata = "<table border=1><tr bgcolor=lightgray><td></td><td>Name</td><td>Description</td><td>Type</td><td>Length</td><td>Created</td></tr>\n";
			for (i = 0; i < files.length; i++)
			{
				filedata = filedata + "<tr><td><img src=delete.jpg height=10px onclick='deleteFile(\"<%=projectID%>\",null,null,null,\"" + files[i].fileName +"\")'/></td><td><a href='download.jsp?path=" + files[i].path + "&name=" + files[i].name+ "'>" + files[i].name + "</a></td><td>" + files[i].template + "</td><td>" + files[i].patientName + "</td><td>" + files[i].studyDate + "</td><td>"  +  files[i].date + "</td></tr>\n";
			}
		}
	})
	filedata =  filedata + "</table>\n";
	document.getElementById("imagelist").innerHTML = filedata;
})
</script>
</BODY>
</HTML>
