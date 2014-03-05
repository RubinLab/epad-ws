package edu.stanford.isis.epadws.queries;

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

import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.dicom.DicomParentCache;
import edu.stanford.isis.epad.common.dicom.DicomParentType;
import edu.stanford.isis.epad.common.query.EPADDatabaseImage;
import edu.stanford.isis.epad.common.query.EPADDatabaseSeries;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.dcm4chee.Dcm4CheeDatabaseCommands;
import edu.stanford.isis.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.isis.epadws.dcm4chee.Dcm4CheeStudyQueryBuilder;
import edu.stanford.isis.epadws.epaddb.ConnectionPool;
import edu.stanford.isis.epadws.epaddb.DatabaseUtils;
import edu.stanford.isis.epadws.epaddb.EpadDatabase;
import edu.stanford.isis.epadws.epaddb.EpadDatabaseCommands;
import edu.stanford.isis.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.isis.epadws.handlers.coordination.Term;
import edu.stanford.isis.epadws.processing.model.PngProcessingStatus;
import edu.stanford.isis.epadws.processing.pipeline.watcher.Dcm4CheeDatabaseWatcher;

public class EpadDatabaseQueries implements EpadQueries, EpadDatabaseOperations, Dcm4CheeDatabaseOperations
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final ConnectionPool connectionPool;

	public EpadDatabaseQueries(ConnectionPool connectionPool)
	{
		this.connectionPool = connectionPool;
	}

	@Override
	public EPADDatabaseSeries peformEPADSeriesQuery(String seriesIUID)
	{
		EpadQueries databaseOperations = EpadDatabase.getInstance().getDatabaseOperations();
		List<Map<String, String>> orderQueryEntries = databaseOperations.getDicomSeriesOrder(seriesIUID);
		List<EPADDatabaseImage> epadImageList = new ArrayList<EPADDatabaseImage>();

		for (Map<String, String> entry : orderQueryEntries) {
			String imageUID = entry.get("sop_iuid");
			String fileName = createFileNameField(imageUID);
			String instanceNumberString = entry.get("inst_no");
			int instanceNumber = getInstanceNumber(instanceNumberString, seriesIUID, imageUID);
			String sliceLocation = createSliceLocation(entry); // entry.get("inst_custom1");
			String contentTime = "null"; // TODO Can we find this somewhere?

			EPADDatabaseImage epadImage = new EPADDatabaseImage(fileName, instanceNumber, sliceLocation, contentTime);
			epadImageList.add(epadImage);
		}
		EPADDatabaseSeries epadSeries = new EPADDatabaseSeries(epadImageList);
		return epadSeries;
	}

	@Override
	public List<Map<String, String>> getUnprocessedDicomImageFileDescriptionsForSeries(String seriesIUID)
	{
		List<Map<String, String>> dicomFilesWithoutPNGImagesFileDescriptions = new ArrayList<Map<String, String>>();
		try {
			// Get list of DICOM image descriptions from DCM4CHEE database table (pacsdb.files). Each image description is a
			// map with keys: i.sop_iuid, i.inst_no, s.series_iuid, f.filepath, f.file_size.
			List<Map<String, String>> dicomImageFileDescriptions = getDICOMImageFileDescriptionsForSeries(seriesIUID);

			// Get list of instance IDs for images in series from ePAD database table (epaddb.epad_files).
			List<String> finishedDICOMImageInstanceIDs = getFinishedDICOMImageInstanceIDsForSeriesFromEPadDatabase(seriesIUID);

			// logger.info("Found " + dicomImageFileDescriptions.size() + " unprocessed DICOM image(s) with files and "
			// + finishedDICOMImageInstanceIDs.size() + " processed image(s) for series " + shortenSting(seriesIUID));

			for (Map<String, String> dicomImageFileDescription : dicomImageFileDescriptions) {
				String sopIdWithFile = dicomImageFileDescription.get("sop_iuid");

				if (!finishedDICOMImageInstanceIDs.contains(sopIdWithFile)) {
					dicomFilesWithoutPNGImagesFileDescriptions.add(dicomImageFileDescription);
				}
			}
		} catch (Exception e) {
			log.warning("getUnprocessedDICOMImageFileDescriptions had " + e.getMessage(), e);
		}
		return dicomFilesWithoutPNGImagesFileDescriptions;
	}

	@Override
	public boolean hasEpadFile(String filePath)
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
	public Map<String, String> getDicomSeriesById(String seriesIUID)
	{
		Map<String, String> retVal = new HashMap<String, String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_SERIES_BY_ID);
			ps.setString(1, seriesIUID);

			rs = ps.executeQuery();
			if (rs.next()) {
				retVal = createResultMap(rs);
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

	/**
	 * Called by {@link Dcm4CheeDatabaseWatcher} to see if new series have been uploaded to DCM4CHEE that ePAD does not
	 * know about.
	 */
	@Override
	public List<Map<String, String>> getDicomSeriesForStatus(int statusCode)
	{
		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();

		try {
			Set<String> pacsSet = getNewSeriesFromPacsDb();
			Set<String> epadSet = getAllSeriesFromEPadDatabase();
			pacsSet.removeAll(epadSet);

			// logger.info("There " + pacsSet.size() + " studies in DCM4CHEE database and " + epadSet.size()
			// + " in the ePAD database");

			List<String> seriesList = new ArrayList<String>(pacsSet);

			for (String currSeries : seriesList) {
				Map<String, String> currSeriesData = getDicomSeriesById(currSeries);
				if (currSeriesData != null) {
					if (!currSeriesData.isEmpty()) {
						retVal.add(currSeriesData);
					}
				}
			}
		} catch (Exception e) {
			log.warning("Warning: database operation failed", e);
		}
		return retVal;
	}

	@Override
	public void insertEpadFile(Map<String, String> row)
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
	public void updateEpadFile(String filePath, PngProcessingStatus pngStatus, int fileSize, String errorMsg)
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

	@Override
	public List<Map<String, String>> dicomStudySearch(String type, String typeValue)
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
			log.warning("Warning: database operation failed for: _" + searchSql + "_ debugInfo=" + debugInfo, sqle);
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
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public List<String> findAllSeriesUIDsInStudy(String studyUID)
	{
		List<Map<String, String>> seriesInStudy = findAllDicomSeriesInStudy(studyUID);
		List<String> result = new ArrayList<String>();

		for (Map<String, String> series : seriesInStudy) {
			String seriesID = series.get("series_iuid");
			result.add(seriesID);
		}
		return result;
	}

	@Override
	public List<String> getNewDicomSeries()
	{
		List<String> retVal = new ArrayList<String>();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_SERIES_BY_STATUS);
			ps.setInt(1, 0);
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
	public Map<String, String> getPatientForDicomStudy(String studyIUID)
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
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	/**
	 * @param patientID
	 * @return A list of study IDs
	 */
	@Override
	public List<String> getDicomStudyUIDsForPatient(String patientID)
	{
		List<String> retVal = new ArrayList<String>();

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
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public Map<String, String> getParentStudyForDicomSeries(String seriesIUID)
	{
		Map<String, String> retVal = new HashMap<String, String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_PARENT_STUDY_FOR_SERIES);
			ps.setString(1, seriesIUID);
			rs = ps.executeQuery();
			if (rs.next()) {
				retVal = createResultMap(rs);
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
	public String getDicomStudyUIDForSeries(String seriesIUID)
	{
		DicomParentCache cache = DicomParentCache.getInstance();

		if (cache.hasParent(seriesIUID))
			return cache.getParent(seriesIUID).getDicomUID();

		Map<String, String> dbResult = getParentStudyForDicomSeries(seriesIUID);
		String studyIUID = dbResult.get("study_iuid");
		cache.setParent(seriesIUID, studyIUID, DicomParentType.STUDY);

		return studyIUID;
	}

	/**
	 * Looks in DCM4CHEE database to find a list of all DICOM image descriptions (table is pacsdb.files).
	 */
	@Override
	public List<Map<String, String>> getDICOMImageFileDescriptionsForSeries(String seriesIUID)
	{
		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_FILES_FOR_SERIES);
			ps.setString(1, seriesIUID);
			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String, String> resultMap = createResultMap(rs);
				retVal.add(resultMap);
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
	public List<Map<String, String>> getDicomSeriesOrder(String seriesUID)
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
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, e);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
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

	@Override
	public int getInstanceKeyForInstance(String sopInstanceUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.PK_FOR_INSTANCE);
			ps.setString(1, sopInstanceUID);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return -1;
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Warning: database operation failed. debugInfo=" + debugInfo, sqle);
			return -1;
		} finally {
			close(c, ps, rs);
		}
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

	// /*** Database utilities ***/

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

	private static boolean isStudyDateColumn(String currKey)
	{
		return (currKey.toLowerCase().indexOf("date") > 0 || currKey.toLowerCase().indexOf("time") > 0);
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

	private Set<String> getNewSeriesFromPacsDb()
	{
		List<String> pacsList = getNewDicomSeries();
		return new HashSet<String>(pacsList);
	}

	private Set<String> getAllSeriesFromEPadDatabase()
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

	/**
	 * Cross database query that gets finished image instance IDs for a series.
	 * 
	 * @param seriesIUID String
	 * @return List of String (sopInstanceIds).
	 */
	private List<String> getFinishedDICOMImageInstanceIDsForSeriesFromEPadDatabase(String seriesIUID)
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

	private String createFileNameField(String sopInstanceUID)
	{
		return DicomFormatUtil.formatUidToDir(sopInstanceUID) + ".dcm";
	}

	private int getInstanceNumber(String instanceNumberString, String seriesIUID, String imageUID)
	{
		if (instanceNumberString != null)
			try {
				return Integer.parseInt(instanceNumberString);
			} catch (NumberFormatException e) {
				log.warning("Invalid instance number " + instanceNumberString + " in image " + imageUID + " in series "
						+ seriesIUID);
				return 1; // Invalid instance number; default to 1
			}
		else
			return 1; // Missing instance number; default to 1.
	}

	private String createSliceLocation(Map<String, String> entry)
	{
		String sliceLoc = entry.get("inst_custom1");
		if (sliceLoc == null)
			return "0.0";
		else
			return sliceLoc;
	}

}
