package edu.stanford.isis.epadws.handlers.coordination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.util.JsonHelper;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;
import edu.stanford.isis.epadws.xnat.XNATUtil;

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
 * curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d ' {"terms" : [] }' http://<server>:<port>/coordination/
 * curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST --data @<filename>  http://<server>:<port>/coordination/
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

	private static final String FORBIDDEN_MESSAGE = "Forbidden method - only GET supported!";
	private static final String BAD_TERMS_MESSAGE = "Two or more terms should be provided!";
	private static final String INTERNAL_ERROR_MESSAGE = "Internal server error";
	private static final String INTERNAL_IO_ERROR_MESSAGE = "Internal server IO error";
	private static final String INTERNAL_SQL_ERROR_MESSAGE = "Internal server SQL error";
	private static final String BAD_JSON_ERROR_MESSAGE = "Bad JSON - does not represent a valid coordination";
	private static final String UNPARSABLE_JSON_ERROR_MESSAGE = "Unparsable JSON";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";

	private final static int MIN_COORDINATION_TERMS = 2;

	private static final ProxyLogger log = ProxyLogger.getInstance();
	private final ProxyConfig config = ProxyConfig.getInstance();

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		PrintWriter out = httpResponse.getWriter();

		httpResponse.setContentType("application/json;charset=UTF-8");
		request.setHandled(true);

		if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
			String method = httpRequest.getMethod();
			if ("POST".equalsIgnoreCase(method)) {
				try {
					Coordination coordination = readCoordination(request);
					log.info("Received AIM Template coordination: " + coordination);
					if (coordination != null && coordination.isValid()) {
						if (coordination.getNumberOfTerms() >= MIN_COORDINATION_TERMS) {
							Term term = getCoordinationTerm(coordination);
							out.append(term2JSON(term));
							log.info("Returned AIM Template coordination term: " + term);
							// TODO Should also return SC_CREATED with location header if new.
							httpResponse.setStatus(HttpServletResponse.SC_OK);
						} else {
							httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							log.info(BAD_TERMS_MESSAGE);
							out.print(JsonHelper.createJSONErrorResponse(BAD_TERMS_MESSAGE));
						}
					} else {
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						log.info(BAD_JSON_ERROR_MESSAGE);
						out.print(JsonHelper.createJSONErrorResponse(BAD_JSON_ERROR_MESSAGE));
					}
				} catch (JsonParseException e) {
					httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					log.warning(UNPARSABLE_JSON_ERROR_MESSAGE, e);
					out.print(JsonHelper.createJSONErrorResponse(UNPARSABLE_JSON_ERROR_MESSAGE, e));
				} catch (IOException e) {
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					log.warning(INTERNAL_IO_ERROR_MESSAGE, e);
					out.print(JsonHelper.createJSONErrorResponse(INTERNAL_IO_ERROR_MESSAGE, e));
				} catch (SQLException e) {
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					log.warning(INTERNAL_SQL_ERROR_MESSAGE, e);
					out.print(JsonHelper.createJSONErrorResponse(INTERNAL_SQL_ERROR_MESSAGE, e));
				} catch (Exception e) {
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					log.warning(INTERNAL_ERROR_MESSAGE, e);
					out.print(JsonHelper.createJSONErrorResponse(INTERNAL_ERROR_MESSAGE, e));
				} catch (Error e) {
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					log.warning(INTERNAL_ERROR_MESSAGE, e);
					out.print(JsonHelper.createJSONErrorResponse(INTERNAL_ERROR_MESSAGE, e));
				}
			} else {
				httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
				log.info(FORBIDDEN_MESSAGE);
				out.print(JsonHelper.createJSONErrorResponse(FORBIDDEN_MESSAGE));
			}
		} else {
			log.info(INVALID_SESSION_TOKEN_MESSAGE);
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			out.append(INVALID_SESSION_TOKEN_MESSAGE);
		}
		out.flush();
		out.close();
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
		MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();

		for (Term term : coordination.getTerms()) {
			int termKey = getTermKey(dbQueries, term);
			termKeys.add(termKey);
		}

		Term term = dbQueries.getCoordinationTerm(termKeys);
		if (term == null) { // No coordination existed
			String description = coordination.generateDescription();
			String termIDPrefix = getTermIDPrefix();
			term = dbQueries.insertCoordinationTerm(termIDPrefix, EPAD_SCHEMA_NAME, EPAD_SCHEMA_VERSION, description,
					termKeys);
		}
		return term;
	}

	private String getTermIDPrefix()
	{
		String serverPrefix = config.getParam(SERVER_PREFIX_PARAM_NAME);

		if (serverPrefix == null)
			serverPrefix = SERVER_DEFAULT_PREFIX;

		return EPAD_TERM_PREFIX + "-" + serverPrefix + "-";
	}

	/**
	 * Get the ID of a term from the database. If the term is not recorded, record it and get its new ID.
	 * 
	 * @param dbQueries
	 * @param term
	 * @return The key of the term; should not be null
	 */
	private int getTermKey(MySqlQueries dbQueries, Term term) throws SQLException
	{
		int termKey = dbQueries.getKeyForTerm(term); // TODO Cache rather than hit database each time.
		if (termKey == -1) {
			termKey = dbQueries.insertTerm(term);
		}
		return termKey;
	}
}