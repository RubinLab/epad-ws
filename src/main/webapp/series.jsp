<%@ page language="java"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.stanford.epad.common.util.*"%>
<%@ page import="edu.stanford.epad.epadws.service.*"%>
<%@ page import="edu.stanford.epad.epadws.security.*"%>
<%@ page import="edu.stanford.epad.epadws.queries.*"%>
<%@ page import="edu.stanford.epad.dtos.*"%>
<%@ page import="edu.stanford.epad.dtos.internal.*"%>
<%@ page import="edu.stanford.epad.epadws.handlers.core.*"%>
<%@ page import="edu.stanford.epad.common.pixelmed.*"%>
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
	EpadOperations operations = DefaultEpadOperations.getInstance();
	SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
	EPADSeries series = operations.getSeriesDescription(seriesReference, username, sessionID);
	EPADImageList images = operations.getImageDescriptions(seriesReference, sessionID,new EPADSearchFilter());

	List<String> imagePaths = new ArrayList<String>();
	String width = "";
	String height = "";
	for (EPADImage image: images.ResultSet.Result)
	{
		if (width.length() == 0)
		{
			DICOMElementList dicomElementList = image.dicomElements;
			for (DICOMElement dicomElement : dicomElementList.ResultSet.Result) {
				if (dicomElement.tagCode.equals(PixelMedUtils.RowsCode))
					width = dicomElement.value;
				else if (dicomElement.tagCode.equals(PixelMedUtils.ColumnsCode))
					height = dicomElement.value;
			}
		}
		imagePaths.add(image.losslessImage);
	}
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
 Project: <%=projectID%><br>
 Patient: <%=subjectID%><br>
 Study: <%=studyUID%><br>
 Series: <%=seriesUID%><br>
 Image: <span id=imageno>1</span>
<script>
var images = new Array();
<%
	for (String imagePath: imagePaths)
	{
%>		images.push("http://<%=request.getServerName()%>:8080/epad/resources/dicom/<%=imagePath%>"); <%
	}
%>
var lastScrollTop = 0;
var index = 0;
$(document).ready(function() {
	$(window).scroll(function(event){
	   var st = $(this).scrollTop();
	   if (st > lastScrollTop){
		   next();
	   } else {
		   prev();
	   }
	   lastScrollTop = st;
	});
	$('imagediv').bind('rightclick', function(){ 
		alert('right mouse button is pressed');
	});
});

function next()
{
   index++;
   if (index > images.length)
   {
	   index--;
	   return;
   }
   document.getElementById("imagediv").src = images[index];
   document.getElementById("imageno").innerHTML = "" + (index+1);
}
function prev()
{
   index--;
   if (index < 0)
   {
	   index = 0;
	   return;
   }
   document.getElementById("imagediv").src = images[index];
}
</script>
<div id=imagediv><img src="http://<%=request.getServerName()%>:8080/epad/resources/dicom/<%=imagePaths.get(0)%>" width="<%=width%>" height="<%=height%>" onclick="next()"/></div>
</BODY>
</HTML>
