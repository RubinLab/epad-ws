package edu.stanford.epad.epadws.epaddb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.coordination.Term;
import edu.stanford.epad.epadws.processing.model.PngProcessingStatus;

public class DefaultEpadDatabaseOperations implements EpadDatabaseOperations
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final ConnectionPool connectionPool;

	public DefaultEpadDatabaseOperations(ConnectionPool connectionPool)
	{
		this.connectionPool = connectionPool;
	}

	@Override
	public void insertEpadFileRecord(Map<String, String> row)
	{
		Connection c = null;
		PreparedStatement ps = null;
		try {
			// logger.info("Inserting into epad_file table. data=" + row);
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.INSERT_INTO_EPAD_FILES);
			ps.setInt(1, Integer.parseInt(row.get("instance_fk")));// instance_fk
			ps.setInt(2, Integer.parseInt(row.get("file_type")));// file_type
			ps.setString(3, row.get("file_path"));// file_path
			ps.setInt(4, Integer.parseInt(row.get("file_size")));// file_size
			int fileStatus = getFileStatus(row);
			ps.setInt(5, fileStatus);// file_status
			String errMsg = getErrMsg(row);
			ps.setString(6, errMsg);// err_msg
			ps.setString(7, row.get("file_md5"));// file_md5
			ps.execute();
		} catch (SQLException sqle) {
			log.warning("Warning: database operation failed.", sqle);
		} catch (Exception e) {
			log.warning("database operation (insert epad_file) failed. data=" + row, e);
		} finally {
			close(c, ps);
		}
	}

	@Override
	public void updateEpadFileRecord(String filePath, PngProcessingStatus pngStatus, int fileSize, String errorMsg)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.UPDATE_EPAD_FILES_FOR_EXACT_PATH);

			ps.setInt(1, pngStatus.getCode());
			ps.setInt(2, fileSize);
			ps.setString(3, getValueOrDefault(errorMsg, ""));
			ps.setString(4, filePath);

			ps.execute();

		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public boolean hasEpadFileRecord(String filePath)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_EPAD_FILES_FOR_EXACT_PATH);
			ps.setString(1, filePath);

			rs = ps.executeQuery();
			return rs.next();

		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
			return false;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public List<String> selectEpadFilePath()
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<String>();

		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_ALL_EPAD_FILE_PATHS);
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(rs.getString(1));
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
			return null;
		} finally {
			close(c, ps, rs);
		}
		return result;
	}

	@Override
	public String selectEpadFilePathLike(String sopInstanceUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_PATH);
			ps.setString(1, "%" + sopInstanceUID + "%");
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			} else {
				return null;
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
			return null;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public List<Map<String, String>> getEpadEventsForSessionID(String sessionID)
	{
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_EVENTS_FOR_SESSIONID);
			ps.setString(1, sessionID);
			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();

			while (rs.next()) {
				Map<String, String> rowMap = new HashMap<String, String>();
				int nCols = metaData.getColumnCount();
				for (int i = 1; i < nCols + 1; i++) {
					String columnName = metaData.getColumnName(i);
					String value = rs.getString(i);
					rowMap.put(columnName, value);
				}
				rows.add(rowMap);
			}

			if (!rows.isEmpty()) { // Delete events up the most recent event for user
				log.info("Event search found " + rows.size() + " event(s) for session ID " + sessionID);

				String pk = rows.get(0).get("pk"); // We order by pk, an auto-increment field (which does not wrap)
				ps = c.prepareStatement(EpadDatabaseCommands.DELETE_EVENTS_FOR_SESSIONID);
				ps.setString(1, sessionID);
				ps.setString(2, pk);
				int rowsAffected = ps.executeUpdate();
				log.info("" + rowsAffected + " old event(s) deleted for session ID " + sessionID);
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return rows;
	}

	@Override
	public void forceDICOMReprocessing()
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.DELETE_ALL_FROM_SERIES_STATUS);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public int getKeyForTerm(Term term) throws SQLException
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_COORDINATION_TERM_KEY);
			ps.setString(1, term.getTermID());
			ps.setString(2, term.getSchemaName());
			ps.setString(3, term.getSchemaVersion());
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return -1;
			}
		} catch (SQLException e) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, e);
			throw e;
		} finally {
			close(c, ps, rs);
		}
	}

	/**
	 * Query <core>epaddb.coordination_description</code> table to find a description of a coordination and return this
	 * description as a {@link Term}.
	 * <p>
	 * The table has the following structure:
	 * 
	 * <pre>
	 * [coordination_key, coordination_id, schema, schema_version, description]
	 * </pre>
	 * 
	 * A <code>coordination_id</code> is a unique key. positions.
	 * 
	 * @param termKeys The term keys to coordinate; their position in the list signifies their order.
	 * @return A {@link Term} representing the coordination or null if its not recorded
	 */
	@Override
	public Term getCoordinationTerm(List<Integer> termKeys) throws SQLException
	{
		int coordinationKey = getCoordinationKey(termKeys);

		if (coordinationKey == -1)
			return null;
		else {
			Connection c = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				c = getConnection();
				ps = c.prepareStatement(EpadDatabaseCommands.SELECT_COORDINATION_USING_KEY);
				ps.setInt(1, coordinationKey);
				rs = ps.executeQuery();
				if (rs.next()) {
					String coordinationID = rs.getString(1);
					String schema = rs.getString(2);
					String schemaVersion = rs.getString(3);
					String description = rs.getString(4);
					return new Term(coordinationID, schema, schemaVersion, description);
				} else {
					return null; // Does not exist
				}
			} catch (SQLException e) {
				String debugInfo = DatabaseUtils.getDebugData(rs);
				log.warning("Warning: database operation failed. debugInfo=" + debugInfo, e);
				throw e;
			} finally {
				close(c, ps, rs);
			}
		}
	}

	@Override
	public Term insertCoordinationTerm(String termIDPrefix, String schemaName, String schemaVersion, String description,
			List<Integer> termKeys) throws SQLException
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();

			ps = c.prepareStatement(EpadDatabaseCommands.INSERT_COORDINATION, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, "");
			ps.setString(2, schemaName);
			ps.setString(3, schemaVersion);
			ps.setString(4, description);
			ps.executeUpdate();

			int coordinationTermKey;
			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				coordinationTermKey = rs.getInt(1);
			} else {
				throw new RuntimeException("MySQL error getting auto increment key");
			}
			for (int position = 0; position < termKeys.size(); position++) {
				int termKey = termKeys.get(position);
				ps = c.prepareStatement(EpadDatabaseCommands.INSERT_COORDINATION2TERM);
				ps.setInt(1, coordinationTermKey);
				ps.setInt(2, termKey);
				ps.setInt(3, position);
				ps.executeUpdate();
			}

			String coordinationTermID = termIDPrefix + coordinationTermKey;
			ps = c.prepareStatement(EpadDatabaseCommands.UPDATE_COORDINATION);
			ps.setString(1, coordinationTermID);
			ps.setInt(2, coordinationTermKey);
			ps.executeUpdate();

			return new Term(coordinationTermID, schemaName, schemaVersion, description);
		} catch (SQLException e) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, e);
			throw e;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public int insertTerm(Term term) throws SQLException
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.INSERT_COORDINATION_TERM, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, term.getTermID());
			ps.setString(2, term.getSchemaName());
			ps.setString(3, term.getSchemaVersion());
			ps.setString(4, term.getDescription());
			ps.executeUpdate();
			int termID;
			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				termID = rs.getInt(1);
			} else {
				throw new RuntimeException("MySQL error getting auto increment key");
			}
			return termID;
		} catch (SQLException e) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, e);
			throw e;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public Set<String> getAllSeriesUIDsFromEPadDatabase()
	{ // This is a "select series_iuid from epaddb.series_status";
		Set<String> retVal = new HashSet<String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement("select series_iuid from epaddb.series_status");

			rs = ps.executeQuery();
			while (rs.next()) {
				retVal.add(rs.getString("series_iuid"));
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public void deleteDicomStudy(String uid)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();

			ps = c.prepareStatement(EpadDatabaseCommands.DELETE_FROM_EPAD_FILES);
			ps.setString(1, "%" + uid.replace('.', '_') + "%");
			int rowsAffected = ps.executeUpdate();
			log.info("Rows affected " + rowsAffected);
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public void deleteDicomSeries(String uid)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();

			log.info("Deleting series " + uid + " from ePAD file table in database");
			ps = c.prepareStatement(EpadDatabaseCommands.DELETE_FROM_EPAD_FILES);
			ps.setString(1, "%" + uid.replace('.', '_') + "%");
			int rowsAffected = ps.executeUpdate();
			log.info("Rows affected " + rowsAffected);

			log.info("Deleting series " + uid + " from ePAD status table in database");
			ps = c.prepareStatement(EpadDatabaseCommands.DELETE_SERIES_FROM_SERIES_STATUS);
			ps.setString(1, uid);
			rowsAffected = ps.executeUpdate();
			log.info("Rows affected " + rowsAffected);
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public void insertEpadEvent(String sessionID, String event_status, String aim_uid, String aim_name,
			String patient_id, String patient_name, String template_id, String template_name, String plugin_name)
	{
		Connection c = null;
		PreparedStatement ps = null;
		try {
			// logger.info("Inserting into event table: " + sessionID + " EVENT:" + aim_uid);

			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.INSERT_INTO_EVENT);
			ps.setString(1, sessionID);
			ps.setString(2, event_status);
			ps.setString(3, aim_uid);
			ps.setString(4, aim_name);
			ps.setString(5, patient_id);
			ps.setString(6, patient_name);
			ps.setString(7, template_id);
			ps.setString(8, template_name);
			ps.setString(9, plugin_name);
			ps.execute();
		} catch (SQLException sqle) {
			log.warning("Warning: database operation failed.", sqle);
		} catch (Exception e) {
			log.warning("database operation (insert event) failed for AIM ID " + aim_uid, e);
		} finally {
			close(c, ps);
		}
	}

	@Override
	public void updateDicomSeriesStatusCode(int newStatusCode, String seriesIUID)
	{
		if (!hasSeriesInEPadDatabase(seriesIUID)) {
			insertSeriesInEPadDatabase(newStatusCode, seriesIUID);
		} else {
			updateSeriesInEPadDatabase(newStatusCode, seriesIUID);
		}
	}

	// TODO This is very low level and brittle. See if we can get information from DCM4CHEE database.
	@Override
	public String[] retrieveDicomStudySeriesAndImageIDs(String imageUID)
	{
		String study = null;
		String series = null;
		String[] studySeriesAndImageIDs = new String[3];
		String imageIdKeyWithoutDot = imageUID.replaceAll("\\.", "_");
		String path = selectEpadFilePathLike(imageIdKeyWithoutDot);

		if (path != null) {
			String[] tab = path.split("\\/");
			series = tab[tab.length - 2];
			study = tab[tab.length - 3];
		}
		studySeriesAndImageIDs[0] = study;
		studySeriesAndImageIDs[1] = series;
		studySeriesAndImageIDs[2] = imageIdKeyWithoutDot;

		return studySeriesAndImageIDs;
	}

	/**
	 * Cross database query that gets finished image instance IDs for a series.
	 * 
	 * @param seriesIUID String
	 * @return List of String (sopInstanceIds).
	 */
	@Override
	public List<String> getFinishedDICOMImageInstanceIDsForSeriesFromEPadDatabase(String seriesIUID)
	{
		List<String> retVal = new ArrayList<String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_EPAD_FILES_FOR_SERIES);
			ps.setString(1, seriesIUID);

			rs = ps.executeQuery();
			while (rs.next()) {
				retVal.add(rs.getString("sop_iuid"));
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	/**
	 * Query <core>epaddb.coordinations</code> table to find a coordination that contains all the supplied term keys (and
	 * no more) in the exact supplied position.
	 * <p>
	 * The table has the following structure:
	 * 
	 * <pre>
	 * [coordination_key, term_key, term_position]
	 * </pre>
	 * 
	 * A <code>coordination_key</code> is a non-unique key containing all the coordination's term keys and their
	 * positions.
	 * 
	 * @param termKeys The terms keys to coordinate; their position in the list signifies their order.
	 * @return The ID of the coordination or -1 if its not recorded
	 */
	private int getCoordinationKey(List<Integer> termKeys) throws SQLException
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(generateCoordinationKeyQuery(termKeys));
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return -1; // It does not exist in the
			}
		} catch (SQLException e) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, e);
			throw e;
		} finally {
			close(c, ps, rs);
		}
	}

	private String generateCoordinationKeyQuery(List<Integer> termKeys)
	{
		StringBuilder query = new StringBuilder();

		query.append("SELECT coordination_key FROM epaddb.coordination2term AS O WHERE ");

		int numberOfPositions = termKeys.size();
		for (int position = 0; position < termKeys.size(); position++) {
			int termKey = termKeys.get(position);

			if (position != 0)
				query.append(" AND ");
			query.append("EXISTS (SELECT * from epaddb.coordination2term AS I ");
			query.append("WHERE O.coordination_key = I.coordination_key AND I.term_key = " + termKey + " AND ");
			query.append("      I.position = " + position + ") ");
		}
		query.append("AND NOT EXISTS (SELECT * from epaddb.coordination2term AS I ");
		query.append("                WHERE O.coordination_key = I.coordination_key AND ");
		query.append("                      I.position > " + numberOfPositions + ")");

		return query.toString();
	}

	private Map<String, String> createResultMap(ResultSet resultSet) throws SQLException
	{
		Map<String, String> retVal = new HashMap<String, String>();
		ResultSetMetaData metaData = resultSet.getMetaData();

		int columnCount = metaData.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = metaData.getColumnName(i);
			String columnData = resultSet.getString(i);
			retVal.put(columnName, columnData);
		}

		return retVal;
	}

	private int getFileStatus(Map<String, String> data)
	{
		try {
			String fileStatus = data.get("file_status");
			if (fileStatus != null) {
				return Integer.parseInt(fileStatus);
			}
			return PngProcessingStatus.DONE.getCode();
		} catch (Exception e) {
			log.warning("failed to parse file_status.", e);
		}
		return PngProcessingStatus.DONE.getCode();
	}

	private String getErrMsg(Map<String, String> data)
	{
		String errMsg = data.get("err_msg");
		if (errMsg != null) {
			return errMsg;
		}
		return "";
	}

	private String getValueOrDefault(String value, String def)
	{
		if (value == null) {
			return def;
		} else if ("".equalsIgnoreCase("")) {
			return def;
		}
		return value;
	}

	private boolean hasSeriesInEPadDatabase(String seriesIUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_EPAD_SERIES_BY_ID);
			ps.setString(1, seriesIUID);
			rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
			return false;
		} finally {
			close(c, ps, rs);
		}
	}

	private void insertSeriesInEPadDatabase(int newStatusCode, String seriesIUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.INSERT_INTO_EPAD_SERIES_STATUS);
			ps.setString(1, seriesIUID);
			ps.setInt(2, newStatusCode);
			ps.execute();
		} catch (SQLException sqle) {
			log.warning("Warning: database operation failed.", sqle);
		} catch (Exception e) {
			log.warning("Warning: database operation failed. statusCode=" + newStatusCode + ", series=" + seriesIUID, e);
		} finally {
			close(c, ps);
		}
	}

	private void updateSeriesInEPadDatabase(int newStatusCode, String seriesIUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.UPDATE_EPAD_SERIES_STATUS);
			ps.setInt(1, newStatusCode);
			ps.setString(2, seriesIUID);
			ps.execute();
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	// /*** Database utilities ***/

	private Connection getConnection() throws SQLException
	{
		return connectionPool.getConnection();
	}

	private void close(Connection c, PreparedStatement ps)
	{
		DatabaseUtils.close(ps);
		connectionPool.freeConnection(c);
	}

	private void close(Connection c, PreparedStatement ps, ResultSet rs)
	{
		close(c, ps);
		DatabaseUtils.close(rs);
	}

}
