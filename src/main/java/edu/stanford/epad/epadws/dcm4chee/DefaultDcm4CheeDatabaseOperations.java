/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
package edu.stanford.epad.epadws.dcm4chee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
					if (currKey.toLowerCase().contains("study_datetime") || currKey.toLowerCase().contains("pps_start")) {
						Timestamp ts = rs.getTimestamp(currKey);
						if (ts != null)
							value = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date(ts.getTime()));
						//value = DatabaseUtils.formatMySqlStudyDateToYYYYMMDDFormat(value);
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
					if (colName.toLowerCase().contains("study_datetime") || colName.toLowerCase().contains("pps_start")) {
						Timestamp ts = rs.getTimestamp(i);
						if (ts != null)
							value = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date(ts.getTime()));
					}
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
	public Set<String> getAllDcm4CheeSeriesUIDs() {
		Set<String> retVal = new HashSet<String>();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = getConnection();
			ps = c.prepareStatement(Dcm4CheeDatabaseCommands.SELECT_SERIES);
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
		log.info("sop_cuid :" + classUID);
		String inst_attrs = resultMap.get("inst_attrs_ch").replaceAll("\"", "").replaceAll("\n", "").replaceAll("\r", "").replace(System.getProperty("line.separator"), "").replaceAll("R.DS", "RDS").replaceAll("S.DS", "SDS");
		log.info("Instance attrs:" + inst_attrs);
		log.info("Instance attrs RDS loc " + inst_attrs.indexOf("RDS"));
		log.info("Instance attrs SDS loc " + inst_attrs.indexOf("SDS"));
		String rescaleIntercept = inst_attrs.substring(inst_attrs.indexOf("RDS")+3,inst_attrs.indexOf("(",inst_attrs.indexOf("RDS"))).trim();
		String rescaleSlope = inst_attrs.substring(inst_attrs.indexOf("SDS")+3).trim();
		//ml rescale slope and intercept added
		return new DCM4CHEEImageDescription(studyUID, seriesUID, imageUID, instanceNumber, sliceLocation, contentTime,
				updatedTime, createdTime, classUID,rescaleIntercept, rescaleSlope);
//		return new DCM4CHEEImageDescription(studyUID, seriesUID, imageUID, instanceNumber, sliceLocation, contentTime,
//				updatedTime, createdTime, classUID);
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
			if (columnName.toLowerCase().contains("study_datetime") || columnName.toLowerCase().contains("pps_start")) {
				Timestamp ts = resultSet.getTimestamp(i);
				if (ts != null)
					columnData = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date(ts.getTime()));
			}
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
