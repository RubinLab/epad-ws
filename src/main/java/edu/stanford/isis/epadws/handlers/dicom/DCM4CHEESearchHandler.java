package edu.stanford.isis.epadws.handlers.dicom;

import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.dtos.DCM4CHEESeriesList;
import edu.stanford.epad.dtos.DCM4CHEEStudyList;
import edu.stanford.epad.dtos.DCM4CHEEStudySearchType;
import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.handlers.HandlerUtil;
import edu.stanford.isis.epadws.queries.Dcm4CheeQueries;
import edu.stanford.isis.epadws.xnat.XNATSessionOperations;

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
	private static final String MISSING_STUDY_SEARCH_TYPE_MESSAGE = "Missing DCM4CHEE study search type";
	private static final String INTERNAL_EXCEPTION_MESSAGE = "Warning: internal error running query on DCM4CHEE search route";
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
					queryString = URLDecoder.decode(queryString, "UTF-8");
					queryString = queryString.trim();
					log.info("DCM4CHEESearchHandler query: " + queryString);
					if (isDICOMSeriesRequest(queryString)) { // TODO httpRequest.getParameter: searchtype=series&studyUID=
						String studyIdKey = getStudyUIDFromRequest(queryString);
						String studyUID = DicomFormatUtil.formatDirToUid(studyIdKey);
						DCM4CHEESeriesList dcm4CheeSeriesList = Dcm4CheeQueries.getSeriesInStudy(studyUID);
						responseStream.append(dcm4CheeSeriesList.toJSON());
					} else {
						DCM4CHEEStudySearchType dcm4CheeSearchType = getSearchType(httpRequest);
						if (dcm4CheeSearchType != null) {
							String[] parts = queryString.split("=");
							String searchValue = parts[1].trim();
							DCM4CHEEStudyList dcm4CheeStudyList = Dcm4CheeQueries.studySearch(dcm4CheeSearchType, searchValue);
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

	// TODO Fix this nastiness.
	private static String getStudyUIDFromRequest(String queryString)
	{
		queryString = queryString.toLowerCase();
		int index = queryString.indexOf("&studyuid=");
		String end = queryString.substring(index);
		end = end.replace('=', ' ');
		String[] parts = end.split(" ");
		String key = parts[1].replace('.', '_');
		// log.info("key=" + key + ",   queryString=" + queryString);
		return key;
	}

	/**
	 * Look for searchtype=series in the request to determine if it is a series request.
	 * 
	 * @param queryString String
	 * @return boolean
	 */
	private static boolean isDICOMSeriesRequest(String queryString)
	{
		String check = queryString.toLowerCase().trim();
		boolean isSeries = check.indexOf("earchtype=series") > 0;

		return isSeries;
	}

	private DCM4CHEEStudySearchType getSearchType(HttpServletRequest httpRequest)
	{
		for (DCM4CHEEStudySearchType curr : DCM4CHEEStudySearchType.values()) {
			if ((httpRequest.getParameter(curr.toString()) != null)) {
				return curr;
			}
		}
		log.warning("ERROR: Request missing search parameter. req=" + httpRequest.toString());
		throw new IllegalArgumentException("Request missing search parameter. Req=" + httpRequest.toString());
	}
}
