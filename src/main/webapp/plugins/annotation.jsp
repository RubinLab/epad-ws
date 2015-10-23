<%@ page session="false" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Annotation </TITLE>
 <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
</HEAD>
<BODY bgcolor=white>
<h1>TEST PLUGIN</h1>
<b>Project: </b><span id=project></span><br>
<b>Patient: </b><span id=patient></span><br>
<b>AimId: </b><span id=aim></span><br>
<b>Name: </b><span id=aimname></span><br>
<b>Template: </b><span id=template></span><br>
<b>Modality: </b><span id=modality></span><br>
<b>Aim XML:</b><textarea align=bottom cols=80 rows=20 id=aimxml></textarea>
<script>
var projectID = getURLParameter("projectID");
document.getElementById("project").innerHTML = getURLParameter("projectID");
document.getElementById("patient").innerHTML = getURLParameter("subjectID");
var aimID = getURLParameter("aimID");
document.getElementById("aim").innerHTML =  aimID;
if (aimID != null && aimID.length > 10)
{
	var url = "/epad/v2/projects/" + projectID + "/aims/?annotationUID=" + aimID;
	$.ajax({         
		url: url,         
		type: 'get',         
		async: false,         
		cache: false,         
		dataType: 'text',
		error: function(){
			alert("Error getting aim");
			return true;
		},
		success: function(response){
			//alert(response);
			document.getElementById("aimxml").innerHTML = response;
			return true;
		}
	});
	var url = "/epad/v2/projects/" + projectID + "/aims/?annotationUID=" + aimID + "&format=json";
	$.ajax({         
		url: url,         
		type: 'get',         
		async: false,         
		cache: false,         
		error: function(){
			alert("Error getting aim");
			return true;
		},
		success: function(response){
			document.getElementById("template").innerHTML = response.imageAnnotations.ImageAnnotationCollection.imageAnnotations.ImageAnnotation.typeCode.codeSystem;
			document.getElementById("aimname").innerHTML = response.imageAnnotations.ImageAnnotationCollection.imageAnnotations.ImageAnnotation.name.value;
			document.getElementById("modality").innerHTML = response.imageAnnotations.ImageAnnotationCollection.imageAnnotations.ImageAnnotation.comment.value;
			return true;
		}
	});
}

   function getURLParameter(sParam)
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
