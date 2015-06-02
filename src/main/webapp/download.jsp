<%@ page language="java"%><%@ page import="java.io.*"%><%@ page import="edu.stanford.epad.common.util.*"%><%@ page import="edu.stanford.epad.epadws.service.*"%><%@ page import="edu.stanford.epad.epadws.security.*"%><%@ page session="false" %><%
	String sessionID = SessionService.getJSessionIDFromRequest(request);
	String username = EPADSessionOperations.getSessionUser(sessionID);
	String path = request.getParameter("path");
	if (path == null || path.trim().length() == 0) {
%><script> alert('File path is null');</script><%
		return;
	}
	String name = request.getParameter("name");
	String type = request.getParameter("type");
	File file = new File(EPADConfig.getEPADWebServerResourcesDir() +  path);
	if (!file.exists()) {
%><script> alert('File <%=file.getAbsolutePath()%> does not exist');</script><%
		return;
	}
	if (name == null) name = file.getName();
    EPADFileUtils.downloadFile(request, response, file, name);
%>