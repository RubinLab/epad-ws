package edu.stanford.isis.epadws.processing.mysql;

import java.io.InputStream;
import java.sql.Blob;
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

import javax.xml.bind.DatatypeConverter;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.dicom.DicomParentCache;
import edu.stanford.isis.epad.common.dicom.DicomParentType;
import edu.stanford.isis.epadws.handlers.coordination.Term;
import edu.stanford.isis.epadws.processing.model.PngStatus;

/**
 * @author amsnyder
 */
public class MySqlQueriesImpl implements MySqlQueries
{
	private static final ProxyLogger logger = ProxyLogger.getInstance();

	private final MySqlConnectionPool connectionPool;

	public MySqlQueriesImpl(MySqlConnectionPool connectionPool)
	{
		this.connectionPool = connectionPool;
	}

	@Override
	public List<Map<String, String>> doStudySearch(String type, String searchString)
	{

		// Handle the special case of a work-list search

		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();

		MySqlStudyQueryBuilder queryBuilder = new MySqlStudyQueryBuilder(type, searchString);
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
						value = DbUtils.formatMySqlStudyDateToYYYYMMDDFormat(value);
					}

					line.put(currKey, value);
				}
				retVal.add(line);
			}
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed for: _" + searchSql + "_ debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, s, rs);
		}
		return retVal;
	}

	private static boolean isStudyDateColumn(String currKey)
	{
		return (currKey.toLowerCase().indexOf("date") > 0 || currKey.toLowerCase().indexOf("time") > 0);
	}

	@Override
	public List<Map<String, String>> doSeriesSearch(String studyUID)
	{

		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();

			ps = c.prepareStatement(MySqlCalls.SELECT_SERIES_FOR_STUDY);
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
				}// for
				retVal.add(rowMap);
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}

		return retVal;
	}

	@Override
	public void doDeleteStudy(String uid)
	{

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();

			ps = c.prepareStatement(MySqlCalls.DELETE_FROM_EPAD_FILES);
			ps.setString(1, "%" + uid.replace('.', '_') + "%");
			ps.executeUpdate();

		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public void doDeleteSeries(String uid)
	{

		// TODO Auto-generated method stub
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.DELETE_FROM_EPAD_FILES);
			ps.setString(1, "%" + uid.replace('.', '_') + "%");
			ps.executeUpdate();

			ps = c.prepareStatement(MySqlCalls.DELETE_SERIES_FROM_SERIES_STATUS);
			ps.setString(1, uid);
			ps.executeUpdate();

		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}

	}

	/**
	 * Get all the studies with the study-stats of zero.
	 * 
	 * @return a list of studyUIDs.
	 */
	@Override
	public List<String> getNewStudies()
	{
		List<String> retVal = new ArrayList<String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_STUDY_BY_STATUS);
			ps.setInt(1, 0);

			rs = ps.executeQuery();
			while (rs.next()) {
				retVal.add(rs.getString("study_iuid"));
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}

		return retVal;
	}

	/**
	 * Get all the users
	 * 
	 * @return a list of users.
	 */
	@Override
	public List<String> getAllUsers()
	{
		List<String> retVal = new ArrayList<String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_ALL_USERS);

			rs = ps.executeQuery();
			while (rs.next()) {
				retVal.add(rs.getString("user_name"));
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}

		return retVal;
	}

	// @Deprecated
	@Override
	public List<String> getNewSeries()
	{
		List<String> retVal = new ArrayList<String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_SERIES_BY_STATUS);
			ps.setInt(1, 0);

			rs = ps.executeQuery();
			while (rs.next()) {
				retVal.add(rs.getString("series_iuid"));
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}

		return retVal;
	}

	@Override
	public List<String> getEPadDdSeriesForStatus(int statusCode)
	{
		List<String> retVal = new ArrayList<String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_EPAD_SERIES_BY_STATUS);
			ps.setInt(1, statusCode);

			rs = ps.executeQuery();
			while (rs.next()) {
				retVal.add(rs.getString("series_iuid"));
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}

		return retVal;
	}

	@Override
	public List<Map<String, String>> getStudiesForStatus(int statusCode)
	{
		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_STUDY_BY_STATUS);
			ps.setInt(1, statusCode);

			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String, String> resultMap = createResultMap(rs);
				retVal.add(resultMap);
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public List<Map<String, String>> getSeriesForStatus(int statusCode)
	{
		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_SERIES_BY_STATUS);
			ps.setInt(1, statusCode);

			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String, String> resultMap = createResultMap(rs);
				retVal.add(resultMap);
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public List<Map<String, String>> getSeriesForStatusEx(int statusCode)
	{
		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();

		try {
			Set<String> pacsSet = getNewSeriesFromPacsDb();
			Set<String> epadSet = getAllSeriesFromEPadDb();
			pacsSet.removeAll(epadSet);

			List<String> seriesList = new ArrayList<String>(pacsSet);

			// System.out.println(seriesList.toString());

			for (String currSeries : seriesList) {
				Map<String, String> currSeriesData = getSeriesById(currSeries);
				if (currSeriesData != null) {
					if (!currSeriesData.isEmpty()) {
						retVal.add(currSeriesData);
					}
				}
			}
		} catch (Exception e) {
			logger.warning("database operation failed.", e);
		}
		return retVal;
	}

	private Set<String> getNewSeriesFromPacsDb()
	{
		List<String> pacsList = getNewSeries();
		return new HashSet<String>(pacsList);
	}

	private Set<String> getAllSeriesFromEPadDb()
	{
		// This is a "select series_iuid from epaddb.series_status";

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
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;

	}

	@Override
	public Map<String, String> getSeriesById(String seriesIUID)
	{
		Map<String, String> retVal = new HashMap<String, String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_SERIES_BY_ID);
			ps.setString(1, seriesIUID);

			rs = ps.executeQuery();
			if (rs.next()) {
				retVal = createResultMap(rs);
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
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
			ps = c.prepareStatement(MySqlCalls.SELECT_PATIENT_FOR_STUDY);
			ps.setString(1, studyIUID);

			rs = ps.executeQuery();
			if (rs.next()) {
				retVal = createResultMap(rs);
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public Map<String, String> getParentStudyForSeries(String seriesIUID)
	{
		Map<String, String> retVal = new HashMap<String, String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_PARENT_STUDY_FOR_SERIES);
			ps.setString(1, seriesIUID);

			rs = ps.executeQuery();
			if (rs.next()) {
				retVal = createResultMap(rs);
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public Map<String, String> getParentSeriesForImage(String sopInstanceUID)
	{
		Map<String, String> retVal = new HashMap<String, String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_PARENT_SERIES_FOR_IMAGE);
			ps.setString(1, sopInstanceUID);

			rs = ps.executeQuery();
			if (rs.next()) {
				retVal = createResultMap(rs);
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public String getStudyUIDForSeries(String seriesIUID)
	{
		DicomParentCache cache = DicomParentCache.getInstance();

		if (cache.hasParent(seriesIUID)) {
			return cache.getParent(seriesIUID).getDicomUID();
		}

		// else call the database and cache the result.
		Map<String, String> dbResult = getParentStudyForSeries(seriesIUID);
		String studyIUID = dbResult.get("study_iuid");
		cache.setParent(seriesIUID, studyIUID, DicomParentType.STUDY);

		return studyIUID;
	}

	@Override
	public String getSeriesUIDForImage(String sopInstanceUID)
	{
		DicomParentCache cache = DicomParentCache.getInstance();

		if (cache.hasParent(sopInstanceUID)) {
			return cache.getParent(sopInstanceUID).getDicomUID();
		}

		// else call the database and cache the result.
		Map<String, String> dbResult = getParentSeriesForImage(sopInstanceUID);
		String seriesIUID = dbResult.get("series_iuid");
		cache.setParent(sopInstanceUID, seriesIUID, DicomParentType.SERIES);
		return seriesIUID;
	}

	@Override
	public List<String> getSopInstanceUidsForSeries(String seriesIUID)
	{
		List<String> retVal = new ArrayList<String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_INSTANCE_FOR_SERIES);
			ps.setString(1, seriesIUID);

			rs = ps.executeQuery();
			while (rs.next()) {

			}// while

		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public List<Map<String, String>> getImageFilesForSeriesOrdered(String seriesIUID)
	{
		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_FILES_FOR_SERIES_ORDERED);
			ps.setString(1, seriesIUID);

			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String, String> resultMap = createResultMap(rs);
				retVal.add(resultMap);
			}// while

		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public List<Map<String, String>> getImageFilesForSeries(String seriesIUID)
	{
		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_FILES_FOR_SERIES);
			ps.setString(1, seriesIUID);

			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String, String> resultMap = createResultMap(rs);
				retVal.add(resultMap);
			}// while

		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public Blob getImageBlobDataForSeries(String seriesIUID)
	{
		Blob retVal = null;

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_INSTANCE_FOR_SERIES);
			ps.setString(1, seriesIUID);

			rs = ps.executeQuery();

			if (rs.next()) {
				retVal = rs.getBlob("inst_attrs");
			}// if

		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public List<Map<String, String>> getUnprocessedPngFilesSeries(String seriesIUID)
	{

		List<Map<String, String>> imagesWithoutPngFiles = new ArrayList<Map<String, String>>();
		try {
			/* i.sop_iuid, i.inst_no, s.series_iuid, f.filepath, f.file_size */
			List<Map<String, String>> allImagesWithFiles = getImageFilesForSeries(seriesIUID);

			/* sop_iuid */
			List<String> finishedImage = getFinishedImagesForSeries(seriesIUID);

			logger.info("getImagesWithoutPngFile... has: " + allImagesWithFiles.size() + " images with files and "
					+ finishedImage.size() + " finished images for series=" + shortenSting(seriesIUID));

			for (Map<String, String> currImageWithFile : allImagesWithFiles) {
				String sopIdWithFile = currImageWithFile.get("sop_iuid");

				if (!finishedImage.contains(sopIdWithFile)) {
					imagesWithoutPngFiles.add(currImageWithFile);
				}
			}// for

		} catch (Exception e) {
			logger.warning("getImagesWithoutPngFileForSeries had " + e.getMessage(), e);
		}
		return imagesWithoutPngFiles;
	}

	@Override
	public List<Map<String, String>> getProcessedPngFilesSeriesOrdered(String seriesIUID)
	{
		List<Map<String, String>> imagesWithPngFiles = new ArrayList<Map<String, String>>();
		try {
			/* i.sop_iuid, i.inst_no, s.series_iuid, f.filepath, f.file_size */
			List<Map<String, String>> allImagesWithFiles = getImageFilesForSeriesOrdered(seriesIUID);

			/* sop_iuid */
			List<String> finishedImage = getFinishedImagesForSeries(seriesIUID);

			logger.info("getImagesWithoutPngFile... has: " + allImagesWithFiles.size() + " images with files and "
					+ finishedImage.size() + " finished images for series=" + shortenSting(seriesIUID));

			for (Map<String, String> currImageWithFile : allImagesWithFiles) {
				String sopIdWithFile = currImageWithFile.get("sop_iuid");

				if (finishedImage.contains(sopIdWithFile)) {
					imagesWithPngFiles.add(currImageWithFile);
				}
			}// for

		} catch (Exception e) {
			logger.warning("getImagesWithPngFileForSeries had " + e.getMessage(), e);
		}
		return imagesWithPngFiles;
	}

	private static String shortenSting(String longString)
	{
		if (longString.length() > 10) {
			StringBuilder sb = new StringBuilder();
			sb.append("...");
			int index = longString.length() - 5;
			sb.append(longString.substring(index));
			return sb.toString();
		}
		return longString;
	}

	/**
	 * Cross database query that gets finished image (png) files for a series.
	 * 
	 * @param seriesIUID String
	 * @return List of String (sopInstanceIds).
	 */
	private List<String> getFinishedImagesForSeries(String seriesIUID)
	{
		List<String> retVal = new ArrayList<String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_EPAD_FILES_FOR_SERIES);
			ps.setString(1, seriesIUID);

			rs = ps.executeQuery();
			while (rs.next()) {
				retVal.add(rs.getString("sop_iuid"));
			}// while

		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public void insertEpadFile(Map<String, String> data)
	{
		Connection c = null;
		PreparedStatement ps = null;
		try {
			logger.info("Inserting into epad_file table. data=" + data);

			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.INSERT_INTO_EPAD_FILES);

			ps.setInt(1, Integer.parseInt(data.get("instance_fk")));// instance_fk
			ps.setInt(2, Integer.parseInt(data.get("file_type")));// file_type
			ps.setString(3, data.get("file_path"));// file_path
			ps.setInt(4, Integer.parseInt(data.get("file_size")));// file_size

			int fileStatus = getFileStatus(data);
			ps.setInt(5, fileStatus);// file_status

			String errMsg = getErrMsg(data);
			ps.setString(6, errMsg);// err_msg
			ps.setString(7, data.get("file_md5"));// file_md5

			ps.execute();

		} catch (SQLException sqle) {
			logger.warning("database operation failed.", sqle);
		} catch (Exception e) {
			logger.warning("database operation (insert epad_file) failed. data=" + data, e);
		} finally {
			close(c, ps);
		}
	}

	@Override
	public void insertUserInDb(String username, String email, String password, String expirationdate, String userrole)
	{
		Connection c = null;
		PreparedStatement ps = null;
		try {
			logger.info("Inserting into login table: " + username);

			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.INSERT_INTO_USER);

			ps.setString(1, username);
			ps.setString(2, email);
			ps.setString(3, password);
			ps.setString(4, expirationdate);
			ps.setString(5, userrole);

			ps.execute();

		} catch (SQLException sqle) {
			logger.warning("database operation failed.", sqle);
		} catch (Exception e) {
			logger.warning("database operation (insert user) failed: " + username, e);
		} finally {
			close(c, ps);
		}
	}

	@Override
	public void insertEventInDb(String userName, String event_status, String aim_uid, String aim_name, String patient_id,
			String patient_name, String template_id, String template_name, String plugin_name)
	{
		Connection c = null;
		PreparedStatement ps = null;
		try {
			// logger.info("Inserting into event table: "+userName+" EVENT:"+aim_uid);

			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.INSERT_INTO_EVENT);

			String user_fk = getUserFK(userName);
			ps.setString(1, user_fk);
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
			logger.warning("database operation failed.", sqle);
		} catch (Exception e) {
			logger.warning("database operation (insert event) failed: " + aim_uid, e);
		} finally {
			close(c, ps);
		}
	}

	@Override
	public String getUserFK(String username)
	{
		Map<String, String> retVal = new HashMap<String, String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_USER_PK);
			ps.setString(1, username);

			rs = ps.executeQuery();
			if (rs.next()) {
				retVal = createResultMap(rs);
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}

		return retVal.get(retVal.keySet().toArray()[0]);

	}

	@Override
	public boolean hasEpadFile(String filePath)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_EPAD_FILES_FOR_EXACT_PATH);
			ps.setString(1, filePath);

			rs = ps.executeQuery();
			return rs.next();

		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
			return false;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public void updateEpadFile(String filePath, PngStatus pngStatus, int fileSize, String errorMsg)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.UPDATE_EPAD_FILES_FOR_EXACT_PATH);

			ps.setInt(1, pngStatus.getCode());
			ps.setInt(2, fileSize);
			ps.setString(3, getValueOrDefault(errorMsg, ""));
			ps.setString(4, filePath);

			ps.execute();

		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public int countEpadFilesLike(String likePath)
	{
		return 0; // To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public float getPercentComplete(String seriesUID)
	{
		// for a given series look at the number is instance. vs the number that has a unique file.

		int nPngFiles = getNumberOfPngFiles(seriesUID);
		int nInstances = getNumberOfInstances(seriesUID);

		if (nInstances <= 0) {
			logger.info("WARNING: series with zero instance might be Segmentation Object. seriesUID=" + seriesUID);
			return 0.01f;
		}

		float percent = nPngFiles / nInstances;
		return percent;
	}

	private int getNumberOfPngFiles(String seriesUID)
	{
		List<String> list = getSopInstanceUidsForSeries(seriesUID);
		return list.size();
	}

	private int getNumberOfInstances(String seriesUID)
	{
		List<String> list = getFinishedImagesForSeries(seriesUID);
		return list.size();
	}

	private int getFileStatus(Map<String, String> data)
	{
		try {
			String fileStatus = data.get("file_status");
			if (fileStatus != null) {
				return Integer.parseInt(fileStatus);
			}
			return PngStatus.DONE.getCode();
		} catch (Exception e) {
			logger.warning("failed to parse file_status.", e);
		}
		return PngStatus.DONE.getCode();
	}

	private String getErrMsg(Map<String, String> data)
	{
		String errMsg = data.get("err_msg");
		if (errMsg != null) {
			return errMsg;
		}
		return "";
	}

	static String getValueOrDefault(String value, String def)
	{
		if (value == null) {
			return def;
		} else if ("".equalsIgnoreCase("")) {
			return def;
		}
		return value;
	}

	@Override
	public List<Map<String, String>> getOrderFile(String seriesUID)
	{
		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_INSTANCE_FOR_SERIES_ORDER_BY_INT);
			ps.setString(1, seriesUID);

			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String, String> resultMap = createResultMap(rs);
				retVal.add(resultMap);
			}// while

		} catch (SQLException e) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, e);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@SuppressWarnings("unused")
	private boolean findPngFile(String studyUID, String seriesUID, String sopInstanceUID)
	{
		return DicomFormatUtil.hasFileWithExtension(studyUID, seriesUID, sopInstanceUID, ".png");
	}// findPngFile

	@Override
	public void updateStudiesStatusCode(int newStatusCode, String studyIUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.UPDATE_STUDY_STATUS_CODE);
			ps.setInt(1, newStatusCode);
			ps.setString(2, studyIUID);
			ps.execute();

		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public void updateSeriesStatusCode(int newStatusCode, String seriesIUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.UPDATE_SERIES_STATUS_CODE);
			ps.setInt(1, newStatusCode);
			ps.setString(2, seriesIUID);
			ps.execute();

		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public void updateSeriesStatusCodeEx(int newStatusCode, String seriesIUID)
	{
		if (!hasSeriesInEPadDb(seriesIUID)) {
			insertSeriesInEPadDb(newStatusCode, seriesIUID);
		} else {
			updateSeriesInEPadDb(newStatusCode, seriesIUID);
		}
	}

	public void insertSeriesInEPadDb(int newStatusCode, String seriesIUID)
	{
		// INSERT_INTO_EPAD_SERIES_STATUS

		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.INSERT_INTO_EPAD_SERIES_STATUS);
			ps.setString(1, seriesIUID);
			ps.setInt(2, newStatusCode);

			ps.execute();
		} catch (SQLException sqle) {
			logger.warning("database operation failed.", sqle);
		} catch (Exception e) {
			logger.warning("database operation failed. statusCode=" + newStatusCode + ", series=" + seriesIUID, e);
		} finally {
			close(c, ps);
		}

	}

	public boolean hasSeriesInEPadDb(String seriesIUID)
	{
		// SELECT_EPAD_SERIES_BY_ID

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_EPAD_SERIES_BY_ID);
			ps.setString(1, seriesIUID);

			rs = ps.executeQuery();
			return rs.next();

		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
			return false;
		} finally {
			close(c, ps, rs);
		}
	}

	public void updateSeriesInEPadDb(int newStatusCode, String seriesIUID)
	{

		// UPDATE_EPAD_SERIES_STATUS

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.UPDATE_EPAD_SERIES_STATUS);
			ps.setInt(1, newStatusCode);
			ps.setString(2, seriesIUID);
			ps.execute();

		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}

	}

	@Override
	public int getStudyKey(String studyUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.PK_FOR_STUDY);
			ps.setString(1, studyUID);

			sb.append("getStudyKey(").append(studyUID).append(").");

			rs = ps.executeQuery();
			if (rs.next()) {
				sb.append(" Found a study.");
				return rs.getInt(1);
			} else {
				sb.append(" Didn't find a study.");
				return -1;
			}
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
			return -1;
		} finally {
			logger.info(sb.toString());
			close(c, ps, rs);
		}
	}

	@Override
	public int getSeriesKey(String seriesUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.PK_FOR_SERIES);
			ps.setString(1, seriesUID);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return -1;
			}
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
			return -1;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public int getInstanceKey(String sopInstanceUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.PK_FOR_INSTANCE);
			ps.setString(1, sopInstanceUID);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return -1;
			}
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
			return -1;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public String getSeriesAttrs(String seriesUID)
	{

		byte[] blobBytes = getSeriesAttrsAsBytes(seriesUID);
		if (blobBytes != null) {
			return convertBytes2Base64(blobBytes);
		} else {
			return "";
		}
	}

	@Override
	public String getInstanceAttrs(String sopInstanceUID)
	{

		byte[] blobBytes = getInstanceAttrsAsBytes(sopInstanceUID);
		if (blobBytes != null) {
			return convertBytes2Base64(blobBytes);
		} else {
			return "";
		}
	}

	@Override
	public byte[] getSeriesAttrsAsBytes(String seriesUID)
	{
		try {
			Blob blob = getSeriesAttrsAsBlob(seriesUID);
			return blob.getBytes(1, (int)blob.length());
		} catch (SQLException sqle) {
			logger.warning("database operation failed.", sqle);
			return null;
		}
	}

	@Override
	public byte[] getInstanceAttrsAsBytes(String sopInstanceUID)
	{
		try {
			Blob blob = getInstanceAttrsAsBlob(sopInstanceUID);
			return blob.getBytes(1, (int)blob.length());
		} catch (SQLException sqle) {
			logger.warning("database operation failed.", sqle);
			return null;
		}
	}

	@Override
	public InputStream getPatientAttrsAsStream(String patId)
	{
		try {
			Blob blob = getPatientAttrsAsBlob(patId);
			return blob.getBinaryStream();
		} catch (SQLException sqle) {
			logger.warning("database operation failed.", sqle);
			return null;
		}
	}

	@Override
	public InputStream getStudyAttrsAsStream(String studyIUID)
	{
		try {
			Blob blob = getStudyAttrsAsBlob(studyIUID);
			return blob.getBinaryStream();
		} catch (SQLException sqle) {
			logger.warning("database operation failed.", sqle);
			return null;
		}
	}

	@Override
	public InputStream getSeriesAttrsAsStream(String seriesUID)
	{
		try {
			Blob blob = getSeriesAttrsAsBlob(seriesUID);
			return blob.getBinaryStream();
		} catch (SQLException sqle) {
			logger.warning("database operation failed.", sqle);
			return null;
		}
	}

	@Override
	public InputStream getInstanceAttrsAsStream(String sopInstanceUID)
	{
		try {
			Blob blob = getInstanceAttrsAsBlob(sopInstanceUID);
			return blob.getBinaryStream();
		} catch (SQLException sqle) {
			logger.warning("database operation failed.", sqle);
			return null;
		}
	}

	private Blob getPatientAttrsAsBlob(String patientID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_PATIENT_ATTRS);
			ps.setString(1, patientID);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getBlob(1);
			} else {
				return null;
			}
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
			return null;
		} finally {
			close(c, ps, rs);
		}
	}

	private Blob getStudyAttrsAsBlob(String studyIUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_STUDY_ATTRS);
			ps.setString(1, studyIUID);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getBlob(1);
			} else {
				return null;
			}
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
			return null;
		} finally {
			close(c, ps, rs);
		}
	}

	private Blob getSeriesAttrsAsBlob(String seriesUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_SERIES_ATTRS);
			ps.setString(1, seriesUID);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getBlob(1);
			} else {
				return null;
			}
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
			return null;
		} finally {
			close(c, ps, rs);
		}
	}

	private Blob getInstanceAttrsAsBlob(String sopInstanceUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_INSTANCE_ATTRS);
			ps.setString(1, sopInstanceUID);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getBlob(1);
			} else {
				return null;
			}
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
			return null;
		} finally {
			close(c, ps, rs);
		}
	}

	private static String convertBytes2Base64(byte[] bytes)
	{
		return DatatypeConverter.printBase64Binary(bytes);
	}

	public static Map<String, String> createResultMap(ResultSet resultSet) throws SQLException
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

	// /*** Database utilities ***/

	Connection getConnection() throws SQLException
	{
		return connectionPool.getConnection();
	}

	void close(Connection c, Statement s)
	{
		DbUtils.close(s);
		connectionPool.freeConnection(c);
	}

	void close(Connection c, PreparedStatement ps)
	{
		DbUtils.close(ps);
		connectionPool.freeConnection(c);
	}

	void close(Connection c, Statement s, ResultSet rs)
	{
		DbUtils.close(rs);
		close(c, s);
	}

	void close(Connection c, PreparedStatement ps, ResultSet rs)
	{
		close(c, ps);
		DbUtils.close(rs);
	}

	@Override
	public List<Map<String, String>> getEventsForUser(String username)
	{

		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();

			ps = c.prepareStatement(MySqlCalls.SELECT_EVENTS_FOR_USER);

			String user_fk = getUserFK(username);
			ps.setString(1, user_fk);

			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();

			while (rs.next()) {
				Map<String, String> rowMap = new HashMap<String, String>();
				int nCols = metaData.getColumnCount();
				for (int i = 1; i < nCols + 1; i++) {
					String colName = metaData.getColumnName(i);
					String value = rs.getString(i);
					rowMap.put(colName, value);
				}// for
				retVal.add(rowMap);
			}// while
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}

		return retVal;

	}

	@Override
	public String selectEpadFilePathLike(String sopInstanceUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(MySqlCalls.SELECT_PATH);
			ps.setString(1, "%" + sopInstanceUID + "%");
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			} else {
				return null;
			}
		} catch (SQLException sqle) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, sqle);
			return null;
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
			ps = c.prepareStatement(MySqlCalls.SELECT_TERM_KEY);
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
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, e);
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
				ps = c.prepareStatement(MySqlCalls.SELECT_COORDINATION_USING_KEY);
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
				String debugInfo = DbUtils.getDebugData(rs);
				logger.warning("database operation failed. debugInfo=" + debugInfo, e);
				throw e;
			} finally {
				close(c, ps, rs);
			}
		}
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
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, e);
			throw e;
		} finally {
			close(c, ps, rs);
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

			ps = c.prepareStatement(MySqlCalls.INSERT_COORDINATION, Statement.RETURN_GENERATED_KEYS);
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
				ps = c.prepareStatement(MySqlCalls.INSERT_COORDINATION2TERM);
				ps.setInt(1, coordinationTermKey);
				ps.setInt(2, termKey);
				ps.setInt(3, position);
				ps.executeUpdate();
			}

			String coordinationTermID = termIDPrefix + coordinationTermKey;
			ps = c.prepareStatement(MySqlCalls.UPDATE_COORDINATION);
			ps.setString(1, coordinationTermID);
			ps.setInt(2, coordinationTermKey);
			ps.executeUpdate();

			return new Term(coordinationTermID, schemaName, schemaVersion, description);
		} catch (SQLException e) {
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, e);
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
			ps = c.prepareStatement(MySqlCalls.INSERT_TERM, Statement.RETURN_GENERATED_KEYS);
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
			String debugInfo = DbUtils.getDebugData(rs);
			logger.warning("database operation failed. debugInfo=" + debugInfo, e);
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
}
