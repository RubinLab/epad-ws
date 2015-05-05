package edu.stanford.epad.epadws.handlers.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.DicomTagList;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADFile;
import edu.stanford.epad.dtos.EPADFileList;
import edu.stanford.epad.dtos.EPADFrame;
import edu.stanford.epad.dtos.EPADFrameList;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADMessage;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.dtos.EPADTemplateList;
import edu.stanford.epad.dtos.EPADUsage;
import edu.stanford.epad.dtos.EPADUsageList;
import edu.stanford.epad.dtos.EPADUser;
import edu.stanford.epad.dtos.EPADUserList;
import edu.stanford.epad.dtos.EPADWorklist;
import edu.stanford.epad.dtos.EPADWorklistList;
import edu.stanford.epad.dtos.EPADWorklistStudyList;
import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.dtos.RemotePACEntity;
import edu.stanford.epad.dtos.RemotePACEntityList;
import edu.stanford.epad.dtos.RemotePACList;
import edu.stanford.epad.dtos.RemotePACQueryConfigList;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.models.EpadFile;
import edu.stanford.epad.epadws.models.EpadStatistics;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.models.RemotePACQuery;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.models.WorkListToStudy;
import edu.stanford.epad.epadws.models.WorkListToSubject;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.security.EPADSession;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.DefaultWorkListOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadWorkListOperations;
import edu.stanford.epad.epadws.service.RemotePACService;
import edu.stanford.epad.epadws.service.SessionService;
import edu.stanford.epad.epadws.service.TCIAService;
import edu.stanford.epad.epadws.service.UserProjectService;

/**
 * @author martin
 */
public class EPADHandler extends AbstractHandler
{
	private static final String INTERNAL_ERROR_MESSAGE = "Internal error";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";
	private static final String FORBIDDEN_MESSAGE = "Forbidden method - only GET, DELETE, PUT, and POST allowed!";
	private static final String NO_USERNAME_MESSAGE = "Must have username parameter for requests!";

	private static final EPADLogger log = EPADLogger.getInstance();

	/*
	 * Main class for handling rest calls using the epad v2 api.
	 * 
	 * Note: These long if/then/else statements looks terrible, they need to be replaced by something like jersey with annotations
	 * But there seems to be some problem using jersey with embedded jetty and multiple handlers - still need to solve that
	 * 
	 */
	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("application/json");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();
			String method = httpRequest.getMethod();

			String sessionID = SessionService.getJSessionIDFromRequest(httpRequest);
			boolean releaseSession = false;
			if (sessionID == null)
			{
				sessionID = SessionService.getJSessionIDFromRequest(httpRequest, true);
				releaseSession = true;
			}

			String username = httpRequest.getParameter("username");
			if (username == null && sessionID != null)
				username = EPADSessionOperations.getSessionUser(sessionID);
			log.info("User:" + username  + " host:" + EPADSessionOperations.getSessionHost(sessionID) + " method:" + httpRequest.getMethod() 
					+ ", url: " + httpRequest.getPathInfo() + ", parameters: "
					+ httpRequest.getQueryString() + " sessionId:" + sessionID);
			if (SessionService.hasValidSessionID(sessionID)) {
				if (EPADConfig.UseEPADUsersProjects) {
					String sessionUser = EPADSessionOperations.getSessionUser(sessionID);
					if (username != null && !username.equals(sessionUser))
					{
						throw new Exception("Incorrect username in request:" + username + " session belongs to " + sessionUser);
					}
					else
						username = sessionUser;
				}
				if (username != null) {
					if ("GET".equalsIgnoreCase(method)) {
						statusCode = EPADGetHandler.handleGet(httpRequest, httpResponse, responseStream, username, sessionID);
					} else if ("DELETE".equalsIgnoreCase(method)) {
						statusCode = EPADDeleteHandler.handleDelete(httpRequest, responseStream, username, sessionID);
					} else if ("PUT".equalsIgnoreCase(method)) {
						statusCode = EPADPutHandler.handlePut(httpRequest, httpResponse, responseStream, username, sessionID);
					} else if ("POST".equalsIgnoreCase(method)) {
						statusCode = EPADPostHandler.handlePost(httpRequest, responseStream, username, sessionID);
					} else {
						statusCode = HandlerUtil.badRequestJSONResponse(FORBIDDEN_MESSAGE, responseStream, log);
					}
				} else
					statusCode = HandlerUtil.badRequestJSONResponse(NO_USERNAME_MESSAGE, responseStream, log);
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
			if (releaseSession) {
				EPADSessionOperations.invalidateSessionID(sessionID);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warning("Error in handle request:", e);
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, e, responseStream, log);
		}
		log.info("Status returned to client:" + statusCode);
		httpResponse.setStatus(statusCode);
	}
}
