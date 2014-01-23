package edu.stanford.isis.epadws.handlers.coordination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.handlers.HandlerUtil;
import edu.stanford.isis.epadws.persistence.Database;
import edu.stanford.isis.epadws.persistence.DatabaseOperations;
import edu.stanford.isis.epadws.xnat.XNATSessionOperations;

/**
 * The coordination handler is responsible for taking a set of ordered terms and returning a unique ID for those terms.
 * If the coordination is already recorded, the existing ID is returned; if it is a new coordination, a new ID returned
 * and the coordination is recorded.
 * <p>
 * The set of terms (each of which consists of a term ID, a schema name, a schema version and a description) is passed
 * to this call as a JSON array.
 * <p>
 * The following is an example of a coordination containing two terms:
 * 
 * <pre>
 * { "terms": [ 
 *             { "termID"        : "RID3434",
 *               "schema"        : "RADLEX",
 *               "schemaVersion" : "1.0",
 *               "description"   : "leg"
 *             },
 *             { "termID"        : "RID834",
 *               "schema"        : "RADLEX",
 *               "schemaVersion" : "1.0",
 *               "description"   : "foot"
 *             }
 *            ]
 * }
 * </pre>
 * 
 * The response will be a single term describing the coordination.
 * <p>
 * For example:
 * 
 * <pre>
 * { "termID"         : "EPAD34",
 *    "schema"        : "EPAD",
 *    "schemaVersion" : "1.0",
 *    "description"   : "leg foot"
 * }
 * </pre>
 * 
 * Basic test invocation:
 * 
 * <pre>
 * curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d ' {"terms" : [] }' http://<server>:<port>/epad/coordination/
 * curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST --data @<filename>  http://<server>:<port>/epad/coordination/
 * </pre>
 * 
 * @author martin
 */
public class CoordinationHandler extends AbstractHandler
{
	private static final String EPAD_TERM_PREFIX = "EPAD"; // TODO Should eventually be recorded in a configuration file.
	private static final String EPAD_SCHEMA_NAME = "EPAD"; // TODO Should eventually be recorded in a configuration file.
	private static final String EPAD_SCHEMA_VERSION = "1.0";
	private static final String SERVER_PREFIX_PARAM_NAME = "coordinationTermPrefix";
	private static final String SERVER_DEFAULT_PREFIX = "0";

	private static final String FORBIDDEN_MESSAGE = "Forbidden method - only GET supported on coordination route!";
	private static final String BAD_TERMS_MESSAGE = "Two or more terms should be provided on coordination route!";
	private static final String INTERNAL_ERROR_MESSAGE = "Internal server error  on coordination route";
	private static final String INTERNAL_IO_ERROR_MESSAGE = "Internal server IO error  on coordination route";
	private static final String INTERNAL_SQL_ERROR_MESSAGE = "Internal server SQL error on coordination route";
	private static final String BAD_JSON_MESSAGE = "Bad JSON - does not represent a valid coordination";
	private static final String UNPARSABLE_JSON = "Unparsable JSON in coordination route";
	private static final String INVALID_TOKEN_MESSAGE = "Session token is invalid on coordination route";

	private final static int MIN_COORDINATION_TERMS = 2;

	private static final EPADLogger log = EPADLogger.getInstance();
	private final EPADConfig config = EPADConfig.getInstance();

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("application/json;charset=UTF-8");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();
			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String method = httpRequest.getMethod();
				if ("POST".equalsIgnoreCase(method)) {
					Coordination coordination = readCoordination(request);
					log.info("Received AIM Template coordination: " + coordination);
					if (coordination != null && coordination.isValid()) {
						if (coordination.getNumberOfTerms() >= MIN_COORDINATION_TERMS) {
							Term term = getCoordinationTerm(coordination);
							responseStream.append(term2JSON(term));
							log.info("Returned AIM Template coordination term: " + term);
							// TODO Should also return SC_CREATED with location header if new.
							statusCode = HttpServletResponse.SC_OK;
						} else {
							statusCode = HandlerUtil.infoJSONResponse(HttpServletResponse.SC_BAD_REQUEST, BAD_TERMS_MESSAGE, log);
						}
					} else {
						statusCode = HandlerUtil.infoJSONResponse(HttpServletResponse.SC_BAD_REQUEST, BAD_JSON_MESSAGE, log);
					}
				} else {
					statusCode = HandlerUtil.infoJSONResponse(HttpServletResponse.SC_FORBIDDEN, FORBIDDEN_MESSAGE, log);
				}
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_TOKEN_MESSAGE, log);
			}
		} catch (JsonParseException e) {
			statusCode = HandlerUtil.warningJSONResponse(HttpServletResponse.SC_BAD_REQUEST, UNPARSABLE_JSON, e,
					responseStream, log);
		} catch (IOException e) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_IO_ERROR_MESSAGE, e, responseStream, log);
		} catch (SQLException e) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_SQL_ERROR_MESSAGE, e, responseStream, log);
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	private Coordination readCoordination(Request request) throws IOException
	{
		String json = readJSON(request);
		return json2Coordination(json);
	}

	private String readJSON(Request request) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		String line = null;
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null)
			sb.append(line);
		return sb.toString();
	}

	private String term2JSON(Term term)
	{
		Gson gson = new Gson();

		return gson.toJson(term);
	}

	private Coordination json2Coordination(String json)
	{
		Gson gson = new Gson();
		Coordination coordination = gson.fromJson(json, Coordination.class);

		return coordination;
	}

	/**
	 * Get a {@link Term} representing a coordination from the database. If the coordination is not recorded, record it
	 * and return it.
	 * 
	 * @param dbQueries
	 * @param coordination
	 * @return A term representing the coordination; should not be null
	 */
	private Term getCoordinationTerm(Coordination coordination) throws SQLException
	{
		List<Integer> termKeys = new ArrayList<Integer>();
		DatabaseOperations databaseOperations = Database.getInstance().getDatabaseOperations();

		for (Term term : coordination.getTerms()) {
			int termKey = getTermKey(databaseOperations, term);
			termKeys.add(termKey);
		}

		Term term = databaseOperations.getCoordinationTerm(termKeys);
		if (term == null) { // No coordination existed
			String description = coordination.generateDescription();
			String termIDPrefix = getTermIDPrefix();
			term = databaseOperations.insertCoordinationTerm(termIDPrefix, EPAD_SCHEMA_NAME, EPAD_SCHEMA_VERSION,
					description, termKeys);
		}
		return term;
	}

	private String getTermIDPrefix()
	{
		String serverPrefix = config.getStringPropertyValue(SERVER_PREFIX_PARAM_NAME);

		if (serverPrefix == null)
			serverPrefix = SERVER_DEFAULT_PREFIX;

		return EPAD_TERM_PREFIX + "-" + serverPrefix + "-";
	}

	/**
	 * Get the ID of a term from the database. If the term is not recorded, record it and get its new ID.
	 * 
	 * @param databaseOperations
	 * @param term
	 * @return The key of the term; should not be null
	 */
	private int getTermKey(DatabaseOperations databaseOperations, Term term) throws SQLException
	{
		int termKey = databaseOperations.getKeyForTerm(term); // TODO Cache rather than hit database each time.
		if (termKey == -1) {
			termKey = databaseOperations.insertTerm(term);
		}
		return termKey;
	}
}