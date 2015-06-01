//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

import edu.stanford.epad.common.dicom.DCM4CHEEImageDescription;
import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.common.dicom.DicomParentCache;
import edu.stanford.epad.common.dicom.DicomParentType;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.internal.DCM4CHEEStudySearchType;
import edu.stanford.epad.epadws.epaddb.ConnectionPool;
import edu.stanford.epad.epadws.epaddb.DatabaseUtils;
import edu.stanford.epad.epadws.handlers.core.ImageReference;

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
			if (log.isDebugEnabled())
				log.debug(ps.toString());

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
			if (log.isDebugEnabled())
				log.debug(searchSql);
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
			if (log.isDebugEnabled())
				log.debug(searchSql);
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
	public List<Map<String, String>> getAllSeriesInStudy(String studyUID)
	{
		List<Map<String, String>> retVal = new ArrayList<Map<String, String>>();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_SERIES_FOR_STUDY);
			ps.setString(1, studyUID);
			if (log.isDebugEnabled())
				log.debug(ps.toString());
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
	public Set<String> getAllSeriesUIDsInStudy(String studyUID)
	{
		List<Map<String, String>> seriesInStudy = getAllSeriesInStudy(studyUID);
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
			if (log.isDebugEnabled())
				log.debug(ps.toString());

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
	public String getFirstImageUIDInSeries(String seriesUID)
	{
		Set<String> imageUIDs = getImageUIDsForSeries(seriesUID);

		if (!imageUIDs.isEmpty())
			return imageUIDs.iterator().next();
		else
			return "";
	}

	@Override
	public Set<String> getImageUIDsForSeries(String seriesUID)
	{
		Set<String> retVal = new HashSet<String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_IMAGE_UID_FOR_SERIES);
			ps.setString(1, seriesUID);
			if (log.isDebugEnabled())
				log.debug(ps.toString());

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
			if (log.isDebugEnabled())
				log.debug(ps.toString());

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
			if (log.isDebugEnabled())
				log.debug(ps.toString());
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
			if (log.isDebugEnabled())
				log.debug(ps.toString());

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
		String studyUID = dbResult.get("study_iuid");
		cache.setParent(seriesUID, studyUID, DicomParentType.STUDY);

		return studyUID;
	}

	/**
	 * Looks in DCM4CHEE database to find a list of all DICOM file descriptions.
	 */
	@Override
	public Set<DICOMFileDescription> getDICOMFilesForSeries(String seriesUID)
	{
		Set<DICOMFileDescription> dicomFileDescriptions = new HashSet<DICOMFileDescription>();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_FILES_FOR_SERIES);
			ps.setString(1, seriesUID);
			if (log.isDebugEnabled())
				log.debug(ps.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String, String> resultMap = createResultMap(rs);
				String studyUID = resultMap.get("study_iuid");
				String createdTime = resultMap.get("created_time");
				String imageUID = resultMap.get("sop_iuid");
				int instanceNumber = extractInteger(seriesUID, "instance number", resultMap.get("inst_no"), 0);
				String filePath = resultMap.get("filepath");
				int fileSize = extractInteger(seriesUID, "file_size", resultMap.get("file_size"), 0);
				String modality = resultMap.get("modality");
				
				DICOMFileDescription dicomFileDescription = new DICOMFileDescription(studyUID, seriesUID, imageUID,
						instanceNumber, filePath, fileSize, createdTime, modality);
				dicomFileDescriptions.add(dicomFileDescription);
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		if (log.isDebugEnabled())
			log.debug("Found " + dicomFileDescriptions.size() + " rows");
		return dicomFileDescriptions;
	}

	@Override
	public List<DCM4CHEEImageDescription> getImageDescriptions(String studyUID, String seriesUID)
	{
		List<DCM4CHEEImageDescription> retVal = new ArrayList<>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_IMAGES_FOR_SERIES_ORDER_BY_INSTNO);
			ps.setString(1, seriesUID);
			if (log.isDebugEnabled())
				log.debug(ps.toString());

			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String, String> resultMap = createResultMap(rs);
				DCM4CHEEImageDescription imageDescription = extractDCM4CHEEImageDescription(studyUID, seriesUID, resultMap);
				retVal.add(imageDescription);
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
	public DCM4CHEEImageDescription getImageDescription(ImageReference imageReference)
	{
		return getImageDescription(imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID);
	}

	@Override
	public DCM4CHEEImageDescription getImageDescription(String studyUID, String seriesUID, String imageUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_IMAGE_FOR_SERIES);
			ps.setString(1, seriesUID);
			ps.setString(2, imageUID);
			if (log.isDebugEnabled())
				log.debug(ps.toString());
			rs = ps.executeQuery();
			if (rs.next()) {
				Map<String, String> resultMap = createResultMap(rs);
				return extractDCM4CHEEImageDescription(studyUID, seriesUID, resultMap);
			} else {
				log.warning("Could not find dcm4chee image data for image " + imageUID + " in series " + seriesUID);
				return null;
			}
		} catch (NumberFormatException e) {
			log.warning("Invalid instance number in dcm4chee image " + imageUID);
		} catch (SQLException e) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, e);
		} finally {
			close(c, ps, rs);
		}
		return null;
	}

	private DCM4CHEEImageDescription extractDCM4CHEEImageDescription(String studyUID, String seriesUID,
			Map<String, String> resultMap)
	{
		String imageUID = resultMap.get("sop_iuid");
		int instanceNumber = extractInteger(seriesUID, "instance number", resultMap.get("inst_no"), 0);
		String sliceLocation = resultMap.get("inst_custom1") != null ? resultMap.get("inst_custom1") : "0.0";
		String contentTime = resultMap.get("content_datetime");
		String updatedTime = resultMap.get("updated_time");
		String createdTime = resultMap.get("created_time");
		String classUID = resultMap.get("sop_cuid");

		return new DCM4CHEEImageDescription(studyUID, seriesUID, imageUID, instanceNumber, sliceLocation, contentTime,
				updatedTime, createdTime, classUID);
	}

	@Override
	public int getPrimaryKeyForImageUID(String imageUID)
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
				log.warning("Could not find primary key for image " + imageUID);
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

	private int extractInteger(String seriesUID, String fieldName, String rawValue, int defaultValue)
	{
		if (rawValue == null) {
			if (!"instance number".equals(fieldName))
				log.warning("Empty " + fieldName + " field  for series " + seriesUID + ", defaulting to " + defaultValue);
			return defaultValue;
		} else {
			try {
				return Integer.parseInt(rawValue);
			} catch (NumberFormatException e) {
				log.warning("Invalid format for integer in field " + fieldName + " for series " + seriesUID + "; rawValue = "
						+ rawValue);
				return defaultValue;
			}
		}
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
