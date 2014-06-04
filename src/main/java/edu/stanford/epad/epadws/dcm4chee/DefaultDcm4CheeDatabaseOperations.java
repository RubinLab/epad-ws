package edu.stanford.epad.epadws.dcm4chee;

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

import edu.stanford.epad.common.dicom.DicomParentCache;
import edu.stanford.epad.common.dicom.DicomParentType;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.internal.DCM4CHEEStudySearchType;
import edu.stanford.epad.epadws.epaddb.ConnectionPool;
import edu.stanford.epad.epadws.epaddb.DatabaseUtils;

public class DefaultDcm4CheeDatabaseOperations implements Dcm4CheeDatabaseOperations
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final ConnectionPool connectionPool;

	public DefaultDcm4CheeDatabaseOperations(ConnectionPool connectionPool)
	{
		this.connectionPool = connectionPool;
	}

	@Override
	public Map<String, String> getSeriesData(String seriesUID)
	{
		Map<String, String> retVal = new HashMap<String, String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_SERIES_BY_ID);
			ps.setString(1, seriesUID);

			rs = ps.executeQuery();
			if (rs.next()) {
				retVal = createResultMap(rs);
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public Map<String, String> studySearch(String studyUID)
	{
		Map<String, String> retVal = new HashMap<String, String>();
		Dcm4CheeStudyQueryBuilder queryBuilder = new Dcm4CheeStudyQueryBuilder(DCM4CHEEStudySearchType.STUDY_UID, studyUID);
		String searchSql = queryBuilder.createStudySearchQuery();

		Connection c = null;
		Statement s = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(searchSql);

			ResultSetMetaData metaData = rs.getMetaData();
			int colCount = metaData.getColumnCount();
			List<String> colNameKeys = new ArrayList<String>();
			for (int i = 1; i < colCount + 1; i++) {
				colNameKeys.add(metaData.getColumnName(i));
			}

			if (rs.next()) {
				for (String currKey : colNameKeys) {
					String value = rs.getString(currKey);
					if (isStudyDateColumn(currKey)) {
						value = DatabaseUtils.formatMySqlStudyDateToYYYYMMDDFormat(value);
					}
					retVal.put(currKey, value);
				}
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed for: SQL = " + searchSql + "; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, s, rs);
		}
		return retVal;
	}

	@Override
	public List<Map<String, String>> studySearch(DCM4CHEEStudySearchType type, String typeValue)
	{
		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();
		Dcm4CheeStudyQueryBuilder queryBuilder = new Dcm4CheeStudyQueryBuilder(type, typeValue);
		String searchSql = queryBuilder.createStudySearchQuery();

		Connection c = null;
		Statement s = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery(searchSql);

			ResultSetMetaData metaData = rs.getMetaData();
			int colCount = metaData.getColumnCount();
			List<String> colNameKeys = new ArrayList<String>();
			for (int i = 1; i < colCount + 1; i++) {
				colNameKeys.add(metaData.getColumnName(i));
			}

			while (rs.next()) {
				Map<String, String> line = new HashMap<String, String>();
				for (String currKey : colNameKeys) {
					String value = rs.getString(currKey);

					if (isStudyDateColumn(currKey)) {
						value = DatabaseUtils.formatMySqlStudyDateToYYYYMMDDFormat(value);
					}
					line.put(currKey, value);
				}
				retVal.add(line);
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; SQL = " + searchSql + "; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, s, rs);
		}
		return retVal;
	}

	@Override
	public List<Map<String, String>> findAllDicomSeriesInStudy(String studyUID)
	{
		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_SERIES_FOR_STUDY);
			ps.setString(1, studyUID);
			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();

			while (rs.next()) {
				Map<String, String> rowMap = new HashMap<String, String>();
				int nCols = metaData.getColumnCount();
				for (int i = 1; i < nCols + 1; i++) {
					String colName = metaData.getColumnName(i);
					String value = rs.getString(i);
					rowMap.put(colName, value);
				}
				retVal.add(rowMap);
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public Set<String> findAllSeriesUIDsInStudy(String studyUID)
	{
		List<Map<String, String>> seriesInStudy = findAllDicomSeriesInStudy(studyUID);
		Set<String> result = new HashSet<String>();

		for (Map<String, String> series : seriesInStudy) {
			String seriesID = series.get("series_iuid");
			result.add(seriesID);
		}
		return result;
	}

	@Override
	public Set<String> getAllReadyDcm4CheeSeriesUIDs()
	{
		Set<String> retVal = new HashSet<String>();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_SERIES_BY_STATUS);
			ps.setInt(1, 0); // A status of zero signals that DCM4CHEE processing has completed and the series is ready
			rs = ps.executeQuery();
			while (rs.next()) {
				retVal.add(rs.getString("series_iuid"));
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public Map<String, String> getPatientForStudy(String studyIUID)
	{
		Map<String, String> retVal = new HashMap<String, String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_PATIENT_FOR_STUDY);
			ps.setString(1, studyIUID);

			rs = ps.executeQuery();
			if (rs.next()) {
				retVal = createResultMap(rs);
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public Set<String> getStudyUIDsForPatient(String patientID)
	{
		Set<String> retVal = new HashSet<String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_STUDY_FOR_PATIENT);
			ps.setString(1, patientID);

			rs = ps.executeQuery();
			if (rs.next()) {
				retVal.add(rs.getString("study_iuid"));
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public Set<String> getImageUIDsForSeries(String seriesID)
	{
		Set<String> retVal = new HashSet<String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_INSTANCE_FOR_SERIES);
			ps.setString(1, seriesID);

			rs = ps.executeQuery();
			if (rs.next()) {
				retVal.add(rs.getString("sop_iuid"));
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public int getNumberOfStudiesForPatients(Set<String> patientIDs)
	{
		int numberOfStudies = 0;
		for (String patientID : patientIDs) {
			numberOfStudies += getNumberOfStudiesForPatient(patientID);
		}
		return numberOfStudies;
	}

	@Override
	public int getNumberOfStudiesForPatient(String patientID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_COUNT_STUDY_FOR_PATIENT);
			ps.setString(1, patientID);

			rs = ps.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
			else
				log.warning("Error getting study count from dcm4chee for patient ID " + patientID);
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return count;
	}

	@Override
	public Map<String, String> getParentStudyForSeries(String seriesUID)
	{
		Map<String, String> retVal = new HashMap<String, String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_PARENT_STUDY_FOR_SERIES);
			ps.setString(1, seriesUID);
			rs = ps.executeQuery();
			if (rs.next()) {
				retVal = createResultMap(rs);
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public String getSeriesUIDForImage(String imageUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_STUDY_AND_SERIES_FOR_INSTANCE);
			ps.setString(1, imageUID);

			rs = ps.executeQuery();
			if (rs.next())
				return rs.getString("series_iuid");
			else {
				log.warning("Could not find series for image " + imageUID);
				return "";
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
			return "";
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public String getStudyUIDForSeries(String seriesUID)
	{
		DicomParentCache cache = DicomParentCache.getInstance();

		if (cache.hasParent(seriesUID))
			return cache.getParent(seriesUID).getDicomUID();

		Map<String, String> dbResult = getParentStudyForSeries(seriesUID);
		String studyIUID = dbResult.get("study_iuid");
		cache.setParent(seriesUID, studyIUID, DicomParentType.STUDY);

		return studyIUID;
	}

	/**
	 * Looks in DCM4CHEE database to find a list of all DICOM image descriptions (table is pacsdb.files).
	 */
	@Override
	public List<Map<String, String>> getDicomImageFileDescriptionsForSeries(String seriesUID)
	{
		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_FILES_FOR_SERIES);
			ps.setString(1, seriesUID);
			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String, String> resultMap = createResultMap(rs);
				retVal.add(resultMap);
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public List<Map<String, String>> getImageDescriptions(String seriesUID)
	{
		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_INSTANCE_FOR_SERIES_ORDER_BY_INT);
			ps.setString(1, seriesUID);

			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String, String> resultMap = createResultMap(rs);
				retVal.add(resultMap);
			}
		} catch (SQLException e) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, e);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public Map<String, String> getImageDescription(String seriesUID, String imageID)
	{
		return null; // TODO
	}

	@Override
	public int getPrimaryKeyForInstanceUID(String imageUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.PK_FOR_INSTANCE);
			ps.setString(1, imageUID);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return -1;
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
			return -1;
		} finally {
			close(c, ps, rs);
		}
	}

	private static boolean isStudyDateColumn(String currKey)
	{
		return (currKey.toLowerCase().indexOf("date") > 0 || currKey.toLowerCase().indexOf("time") > 0);
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

	private Connection getConnection() throws SQLException
	{
		return connectionPool.getConnection();
	}

	private void close(Connection c, Statement s)
	{
		DatabaseUtils.close(s);
		connectionPool.freeConnection(c);
	}

	private void close(Connection c, PreparedStatement ps)
	{
		DatabaseUtils.close(ps);
		connectionPool.freeConnection(c);
	}

	private void close(Connection c, Statement s, ResultSet rs)
	{
		DatabaseUtils.close(rs);
		close(c, s);
	}

	private void close(Connection c, PreparedStatement ps, ResultSet rs)
	{
		close(c, ps);
		DatabaseUtils.close(rs);
	}
}
