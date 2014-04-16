package edu.stanford.epad.epadws.handlers.dicom;

import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.DCM4CHEESeriesList;
import edu.stanford.epad.dtos.DCM4CHEEStudyList;
import edu.stanford.epad.dtos.DCM4CHEEStudySearchType;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * <code>
 * curl -v -b JSESSIOND=<id> -X GET "http://<ip>:<port>/epad/searchj?patientName=*
 * curl -v -b JSESSIOND=<id> -X GET "http://<ip>:<port>/epad/searchj?searchType=series&studyUID=[studyID]"
 * </code>
 * 
 * @author martin
 */
public class DCM4CHEESearchHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String MISSING_QUERY_MESSAGE = "No series or study query in DCM4CHEE search request";
	private static final String MISSING_STUDY_UID_MESSAGE = "Missing study UID in query";
	private static final String MISSING_STUDY_SEARCH_TYPE_MESSAGE = "Missing the study search type in query";
	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error running query on DCM4CHEE search route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid on DCM4CHEE search route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("application/json");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String queryString = httpRequest.getQueryString();

				if (queryString != null) {
					queryString = URLDecoder.decode(queryString, "UTF-8").trim();
					String searchType = httpRequest.getParameter("searchtype");
					if (searchType != null && searchType.equals("series")) {
						String studyUID = httpRequest.getParameter("studyUID");
						if (studyUID != null) {
							log.info("Searching for series in study " + studyUID);
							DCM4CHEESeriesList dcm4CheeSeriesList = Dcm4CheeQueries.getSeriesInStudy(studyUID);
							log.info("Found " + dcm4CheeSeriesList.getNumberOfSeries() + " series in study " + studyUID);
							responseStream.append(dcm4CheeSeriesList.toJSON());
						} else {
							statusCode = HandlerUtil.infoJSONResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_STUDY_UID_MESSAGE,
									responseStream, log);
						}
					} else {
						DCM4CHEEStudySearchType studySearchType = getStudySearchType(httpRequest);
						if (studySearchType != null) {
							String searchValue = httpRequest.getParameter(studySearchType.getName());
							DCM4CHEEStudyList dcm4CheeStudyList = Dcm4CheeQueries.studySearch(studySearchType, searchValue);
							log.info("DCM4CHEESearchHandler: " + dcm4CheeStudyList.getNumberOfStudies() + " study result(s)");
							responseStream.append(dcm4CheeStudyList.toJSON());
						} else {
							statusCode = HandlerUtil.infoJSONResponse(HttpServletResponse.SC_BAD_REQUEST,
									MISSING_STUDY_SEARCH_TYPE_MESSAGE, responseStream, log);
						}
					}
					responseStream.flush();
					statusCode = HttpServletResponse.SC_OK;
				} else {
					statusCode = HandlerUtil.infoJSONResponse(HttpServletResponse.SC_BAD_REQUEST, MISSING_QUERY_MESSAGE,
							responseStream, log);
				}
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_EXCEPTION_MESSAGE, t, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	private DCM4CHEEStudySearchType getStudySearchType(HttpServletRequest httpRequest)
	{
		for (DCM4CHEEStudySearchType curr : DCM4CHEEStudySearchType.values()) {
			if ((httpRequest.getParameter(curr.getName()) != null)) {
				return curr;
			}
		}
		log.warning("Request missing study search type parameter; request=" + httpRequest.toString());
		throw new IllegalArgumentException("Missing study search type search parameter; request=" + httpRequest.toString());
	}
}
