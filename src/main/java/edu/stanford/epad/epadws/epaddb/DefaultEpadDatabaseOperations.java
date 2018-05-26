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
package edu.stanford.epad.epadws.epaddb;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.DB;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.MongoDBOperations;
import edu.stanford.epad.dtos.AnnotationStatus;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADData;
import edu.stanford.epad.dtos.EPADDataList;
import edu.stanford.epad.dtos.EPADUsage;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.aim.AIMDatabaseOperations;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.handlers.coordination.Term;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.models.EpadStatisticsTemplate;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.hakan.aim4api.base.AimException;
import edu.stanford.hakan.aim4api.base.Enumerations.AimVersion;

//TODO epad_images = SSIF, PNG, JPG
//TODO epad_aims = PSSSIF, AIM

public class DefaultEpadDatabaseOperations implements EpadDatabaseOperations
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final ConnectionPool connectionPool;

	public DefaultEpadDatabaseOperations(ConnectionPool connectionPool)
	{
		this.connectionPool = connectionPool;
	}

	@Override
	public int getFreeConnections() {
		return connectionPool.availableConnectionCount();
	}

	@Override
	public int getUsedConnections() {
		return connectionPool.usedConnectionCount();
	}

	@Override
	public String getPNGLocation(ImageReference imageReference)
	{
		return getPNGLocation(imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID);
	}

	@Override
	public String getPNGLocation(String studyUID, String seriesUID, String imageUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String pngFilePath = null;

		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_EPAD_FILE_PATH_FOR_IMAGE);
			ps.setString(1, imageUID);
			rs = ps.executeQuery();
			if (rs.next()) {
				pngFilePath = rs.getString(1);
			}
			if (pngFilePath == null)
				return pngFilePath;
			String imagePath = imageUID.replace('.', '_');  // Old style file name
			if (!pngFilePath.contains(imageUID) && !pngFilePath.contains(imagePath))
			{
				//delete the file reference that was wrong
				int badpk = rs.getInt(2);               
				log.info("Fixing pngFile:" + pngFilePath + " imageUID:" + imageUID + " imagePath:" + imagePath + " seriesUID:" + seriesUID + " pk:" + badpk);
				rs.close();
				ps.close();
				log.info("deleting from epaddb.epad_files where pk=" + badpk);
				ps = c.prepareStatement("delete from epaddb.epad_files where pk=" + badpk);
				ps.execute();
				ps.close();


				ps = c.prepareStatement(EpadDatabaseCommands.SELECT_EPAD_FILE_PATH_BY_IMAGE_UID);
				ps.setString(1, "%/" + imageUID + ".png");
				if (log.isDebugEnabled())
					log.debug(ps.toString());
				rs = ps.executeQuery();
				if (rs.next()) {
					pngFilePath = rs.getString(1);
				}
				else
				{
					rs.close();
					ps.close();
					ps = c.prepareStatement(EpadDatabaseCommands.SELECT_EPAD_FILE_PATH_BY_IMAGE_UID);
					ps.setString(1, "%/" + imagePath + ".png");
					if (log.isDebugEnabled())
						log.debug(ps.toString());
					rs = ps.executeQuery();
					if (rs.next()) {
						pngFilePath = rs.getString(1);
					}
					else
						pngFilePath = null;
				}
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
			return null;
		} finally {
			close(c, ps, rs);
		}
		if (pngFilePath == null)
			log.warning("Could not get PNG file path for image " + imageUID + " seriesUID:" + seriesUID);

		return pngFilePath;
	}

	
	@Override
	public List<EpadStatisticsTemplate> getTemplateStats() {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<EpadStatisticsTemplate> templateStats = new ArrayList<EpadStatisticsTemplate>();

		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_TEMPLATE_STATS);
			rs = ps.executeQuery();
			while (rs.next()) {
				EpadStatisticsTemplate st= new EpadStatisticsTemplate();
				st.setTemplateCode(rs.getString(1));
				st.setTemplateName(rs.getString(2));
				st.setAuthors(rs.getString(3));
				st.setVersion(rs.getString(4));
				st.setTemplateLevelType(rs.getString(5));
				st.setTemplateDescription(rs.getString(6));
				st.setFilePath(rs.getString(7));
				st.setFileId(rs.getString(8));
				st.setNumOfAims(rs.getInt(9));
				templateStats.add(st);
				
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return templateStats;
	}
	
	@Override
	public List<String> getAllPNGLocations(String imageUID) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> pngFilePaths = new ArrayList<String>();

		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_EPAD_FILE_PATH_FOR_IMAGE);
			ps.setString(1, imageUID);
			rs = ps.executeQuery();
			while (rs.next()) {
				String pngFilePath = rs.getString(1);
				pngFilePaths.add(pngFilePath);
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return pngFilePaths;
	}

	@Override
	public Map<String,String> getPixelValues(String imageUID) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String,String> pixelValues = new HashMap<String,String>();

		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_PIXEL_VALUES_FOR_IMAGE);
			ps.setString(1, imageUID);
			rs = ps.executeQuery();
			while (rs.next()) {
				String pngFilePath = rs.getString(1);
				String pixelValue = rs.getString(2);
				pixelValues.put(pngFilePath,pixelValue);
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return pixelValues;
	}
	
	@Override
	public String getPixelValuesForPng(String pngPath) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String,String> pixelValues = new HashMap<String,String>();

		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_PIXEL_VALUES_FOR_PATH);
			ps.setString(1, pngPath);
			rs = ps.executeQuery();
			while (rs.next()) {
				String pixelValue = rs.getString(1);
				return pixelValue;
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return null;
	}
	
	@Override
	public String getPNGLocation(FrameReference frameReference)
	{
		return ""; // TODO
	}

	@Override
	public String getJPGLocation(FrameReference frameReference)
	{
		return ""; // TODO
	}

	@Override
	public void checkAndRefreshAnnotationsTable() {
		Connection c = null;
		int count = 0;
		AIMDatabaseOperations adb = null;
		try {
			log.info("Checking annotations table");
			c = getConnection();
			adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.createAnnotationsTable();
			adb.alterAnnotationsTable();
			count = adb.getTotalAnnotationCount();
		} catch (SQLException sqle) {
			if (sqle.getMessage() != null && !sqle.getMessage().contains("doesn't exist"))
				log.warning("AIM Database operation failed:", sqle);
		} finally {
			if (adb == null)
			{
				close(c);
				return;
			}
			if (c == null) return;
		}
		if (count == 0)
		{
			log.info("No annotations found");
			try {
				log.info("Refreshing annotations table ...");

				if (EPADConfig.useV4.equals("false"))
				{
					adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
							EPADConfig.aim3Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
					adb.refreshTheAnnotationTable(AimVersion.AIMv3_0_1);
				}
				else
				{
					adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
							EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
					adb.refreshTheAnnotationTable(AimVersion.AIMv4_2);
				}
			} catch (SQLException | AimException | IOException sqle) {
				log.warning("AIM Database operation failed:", sqle);
			} finally {
				close(c);
			}
		}
		// Check empty xml columns
		try {
			log.info("Checking annotations table xml data ...");

			adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			List<EPADAIM> aims = adb.getAIMs("XML is null", 0, 0);
			AIMUtil.updateTableXMLs(aims);
		} catch (Exception sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		// Check empty name columns
		try {
			log.info("Checking annotations table name column ...");

			adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			List<EPADAIM> aims = adb.getAIMs("NAME is null", 0, 0);
			AIMUtil.updateTableNameColumn(aims);
		} catch (Exception sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		// Check empty template columns
		try {
			log.info("Checking annotations table template column ...");

			adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			List<EPADAIM> aims = adb.getAIMs("TEMPLATECODE is null", 0, 0);
			AIMUtil.updateTableTemplateColumn(aims);
		} catch (Exception sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}

		// Fix coordination AIMs
		//			try {
		//				c = getConnection();
		//				adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
		//						EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
		//				List<EPADAIM> aims = adb.getAIMs("XML like '%EPAD-prod%'", 0, 0);
		//				AIMUtil.convertAim3(aims);
		//				
		//			} catch (Exception x) {
		//				log.warning("Error fixing AIM for coordination tag:", x);
		//			} finally {
		//				close(c);
		//			}
		// Check mongoDB
		try {
			log.info("Checking mongoDB ...");
			DB mongoDB = MongoDBOperations.getMongoDB();
			if (mongoDB !=  null) {
				adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
						EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
				List<Project> projects = DefaultEpadProjectOperations.getInstance().getAllProjects();
				for (Project p: projects) {
					count = adb.getAIMCount(null, p.getProjectId(), null, null, null, null, 0);
					MongoDBOperations.createIndexes(p.getProjectId());
					int mongoCount = MongoDBOperations.getNumberOfDocuments("", p.getProjectId());
					log.info("Project:" + p.getProjectId() + " mysqlDB Count:" + count + " mongoDB Count:" + mongoCount);
					if (count > mongoCount) {
						List<EPADAIM> aims = adb.getAIMs(p.getProjectId(), AIMSearchType.ANNOTATION_UID, "all");
						log.info("Updating" + aims.size()+ " annotations");							
						AIMUtil.updateMongDB(aims);
					}
				}
			}
			else
				log.warning("No connection to mongoDB");
		} catch (Exception sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}

	}

	@Override
	public EPADAIM getAIM(String aimID) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIM(aimID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null; // TODO
	}

	@Override
	public EPADAIM getAIM(ProjectReference projectReference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			EPADAIM aim = adb.getAIM(aimID);
			if (aim.projectID.equals(projectReference.projectID))
				return aim;
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null; // TODO
	}

	@Override
	public EPADAIM getAIM(SubjectReference subjectReference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			EPADAIM aim = adb.getAIM(aimID);
			if (aim.subjectID.equals(subjectReference.subjectID))
				return aim;
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null; // TODO
	}

	@Override
	public EPADAIM getAIM(StudyReference studyReference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			EPADAIM aim = adb.getAIM(aimID);
			if (aim.studyUID.equals(studyReference.studyUID))
				return aim;
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null; // TODO
	}

	@Override
	public EPADAIM getAIM(SeriesReference seriesReference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			EPADAIM aim = adb.getAIM(aimID);
			if (aim.seriesUID.equals(seriesReference.seriesUID))
				return aim;
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null; // TODO
	}

	@Override
	public EPADAIM getAIM(ImageReference imageReference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			EPADAIM aim = adb.getAIM(aimID);
			if (aim.imageUID.equals(imageReference.imageUID))
				return aim;
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null; // TODO
	}

	@Override
	public EPADAIM getAIM(FrameReference frameReference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			EPADAIM aim = adb.getAIM(aimID);
			if (aim.imageUID.equals(frameReference.imageUID) && aim.instanceOrFrameNumber == frameReference.frameNumber)
				return aim;
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null; // TODO
	}

	@Override
	public List<EPADAIM> getAIMs(ProjectReference reference)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMs(reference.projectID, null, null, null, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public List<EPADAIM> getAIMs(SubjectReference reference)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMs(reference.projectID, reference.subjectID, null, null, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return new ArrayList<EPADAIM>();
	}

	@Override
	public List<EPADAIM> getAIMs(StudyReference reference)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMs(reference.projectID, null, reference.studyUID, null, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return new ArrayList<EPADAIM>();
	}

	@Override
	public List<EPADAIM> getAIMs(SeriesReference reference)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMs(reference.projectID, null, null, reference.seriesUID, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return new ArrayList<EPADAIM>();
	}

	@Override
	public List<EPADAIM> getAIMs(ImageReference reference)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMs(reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return new ArrayList<EPADAIM>();
	}

	@Override
	public List<EPADAIM> getAIMs(FrameReference reference)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMs(reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, reference.frameNumber);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return new ArrayList<EPADAIM>();
	}

	@Override
	public List<EPADAIM> getAIMs(String projectID, String subjectID,
			String studyUID, String seriesUID) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMs(projectID, subjectID, studyUID, seriesUID, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return new ArrayList<EPADAIM>();
	}

	@Override
	public List<EPADAIM> getAIMs(String projectID, AIMSearchType aimSearchType, String value, int start, int count) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			if (aimSearchType == null)
				return adb.getAIMs(projectID, null, null, null, null, 0, start, count);
			else
				return adb.getAIMs(projectID, aimSearchType, value, start, count);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return new ArrayList<EPADAIM>();
	}

	@Override
	public List<EPADAIM> getAIMsByDSOSeries(String dsoSeriesUID) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMs(null, null, null, null, null, 0, dsoSeriesUID, 1, 50);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return new ArrayList<EPADAIM>();
	}

	@Override
	public List<EPADAIM> getAIMsByDSOSeries(String projectID, String dsoSeriesUID) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMs(projectID, null, null, null, null, 0, dsoSeriesUID, 1, 50);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return new ArrayList<EPADAIM>();
	}

	@Override
	public List<EPADAIM> getAIMsByDSOSeries(String projectID, String patientID, String dsoSeriesUID) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMs(projectID, patientID, null, null, null, 0, dsoSeriesUID, 1, 50);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return new ArrayList<EPADAIM>();
	}

	@Override
	public List<EPADAIM> getAIMsByQuery(String sqlQuery) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMs(sqlQuery, 0, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return new ArrayList<EPADAIM>();
	}

	@Override
	public void addProjectToAIM(String projectID, String aimID) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.addProjectToAIM(projectID, aimID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public void removeProjectFromAIM(String projectID, String aimID) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.removeProjectFromAIM(projectID, aimID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public List<EPADAIM> getSharedAIMs(String projectID, String patientID, String seriesUID) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getSharedAIMs(projectID, patientID, seriesUID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return new ArrayList<EPADAIM>();
	}

	@Override
	public int getNumberOfAIMs(String userName, ProjectReference reference)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMCount(null, reference.projectID, null, null, null, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return 0;
	}

	@Override
	public int getNumberOfAIMs(String userName, SubjectReference reference)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMCount(userName, reference.projectID, reference.subjectID, null, null, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return 0;
	}

	@Override
	public int getNumberOfAIMs(String userName, StudyReference reference)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMCount(null, reference.projectID, reference.subjectID, reference.studyUID, null, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return 0;
	}

	@Override
	public int getNumberOfAIMs(String userName, SeriesReference reference)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMCount(null, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return 0;
	}

	@Override
	public int getNumberOfAIMs(String userName, ImageReference reference)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMCount(null, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return 0;

	}

	@Override
	public int getNumberOfAIMs(String userName, FrameReference reference)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMCount(null, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, reference.frameNumber);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return 0;

	}

	@Override
	public int getNumberOfAIMsForPatients(String projectID, Set<String> patientIDs, String userName) {
		int count = 0;
		for (String patientID: patientIDs)
		{ 
			count += this.getNumberOfAIMs(userName, new SubjectReference(projectID, patientID));
		}
		return count;
	}

	@Override
	public int getNumberOfAIMsForSeriesSet(String projectID, Set<String> seriesIDs, String username) {
		int count = 0;
		for (String seriesID: seriesIDs)
		{
			count += this.getNumberOfAIMsForSeries(projectID, seriesID, username);
		}
		return count;
	}

	@Override
	public int getNumberOfAIMsForSeries(String projectID,
			String seriesID, String username) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMCount(null, projectID, null, null, seriesID, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return 0;
	}

	
	
	@Override
	public int getNumberOfAIMs(String criteria) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMCount(criteria);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return 0;
	}

	@Override
	public EPADAIM addAIM(String userName, ProjectReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.insert(aimID, userName, reference.projectID, null, null, null, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM addAIM(String userName, SubjectReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.insert(aimID, userName, reference.projectID, reference.subjectID, null, null, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM addAIM(String userName, StudyReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, null, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM addAIM(String userName, SeriesReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM addAIM(String userName, ImageReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM addDSOAIM(String userName, ImageReference reference, String dsoSeriesUID, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, 0, dsoSeriesUID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM addAIM(String userName, FrameReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, reference.frameNumber);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM addAIM(String userName, FrameReference reference,
			String aimID, String aimXML, String aimName) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, 0, null, aimXML, aimName);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM addAIM(String userName, FrameReference reference,
			String aimID, String aimXML, String aimName, boolean isDicomSR) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, 0, null, aimXML, aimName, isDicomSR);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}
	
	@Override
	public EPADAIM addDSOAIM(String userName, ImageReference reference,
			String dsoSeriesUID, String aimID, String aimXML, String name) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, 0, dsoSeriesUID, aimXML, name);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM updateAIM(String aimID, String projectID, String username) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.updateAIM(aimID, projectID, username);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM updateAIMDSOSeries(String aimID, String dsoSeriesUID,
			String username) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.updateAIMDSOSeriesUID(aimID, dsoSeriesUID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM updateAIMXml(String aimID, String xml) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.updateAIMXml(aimID, xml);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM updateAIMColor(String aimID, String color) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.updateAIMColor(aimID, color);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM updateAIMTemplateCode(String aimID, String templateCode) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.updateAIMTemplateCode(aimID, templateCode);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM updateAIMName(String aimID, String name) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.updateAIMName(aimID, name);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM updateAIMDSOFrameNo(String aimID, int frameNo) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.updateAIMDSOFrameNo(aimID, frameNo);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public void deleteAIM(String userName, ProjectReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.delete(aimID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public void deleteAIM(String userName, SubjectReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.delete(aimID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public void deleteAIM(String userName, StudyReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.delete(aimID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public void deleteAIM(String userName, SeriesReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.delete(aimID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public void deleteAIM(String userName, ImageReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.delete(aimID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public void deleteAIM(String userName, FrameReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.delete(aimID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public void deleteAIM(String userName, String aimID) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.delete(aimID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public void insertEpadFileRow(Map<String, String> row)
	{
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.INSERT_INTO_EPAD_FILES);
			ps.setInt(1, Integer.parseInt(row.get("instance_fk")));
			ps.setInt(2, Integer.parseInt(row.get("file_type")));
			ps.setString(3, row.get("file_path"));
			ps.setInt(4, Integer.parseInt(row.get("file_size")));
			int seriesProcessingStatus = getSeriesProcessingStatusCode(row);
			ps.setInt(5, seriesProcessingStatus);
			String errMsg = getErrMsg(row);
			ps.setString(6, errMsg);
			ps.setString(7, row.get("file_md5"));
			ps.execute();
		} catch (SQLException sqle) {
			log.warning("Database operation failed", sqle);
		} catch (Exception e) {
			log.warning("Database operation (insert epad_file) failed; row=" + row, e);
		} finally {
			close(c, ps);
		}
	}

	@Override
	public void updateEpadFileRow(String filePath, PNGFileProcessingStatus pngFileProcessingStatus, long fileSize,
			String errorMsg)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.UPDATE_EPAD_FILES_FOR_EXACT_PATH);
			ps.setInt(1, pngFileProcessingStatus.getCode());
			ps.setLong(2, fileSize);
			ps.setString(3, getValueOrDefault(errorMsg, ""));
			ps.setString(4, filePath);
			log.info("running sql:"+ps.toString());
			ps.execute();
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}
	
	@Override
	public void insertPixelValues(String filePath, int frameNum, String pixelValues, String imageUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.INSERT_PIXEL_VALUES_FOR_EXACT_PATH);
			ps.setString(1, filePath);
			ps.setInt(2, frameNum);
			ps.setString(3, pixelValues);
			ps.setString(4, imageUID);
			ps.execute();
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public boolean hasEpadFileRow(String filePath)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_EPAD_FILES_FOR_EXACT_PATH);
			ps.setString(1, filePath);
			if (log.isDebugEnabled())
				log.debug(ps.toString());
			rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
			return false;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public List<String> getAllEPadInPipelineFilePaths()
	{
		return getAllEPadFilePathsWithStatus(PNGFileProcessingStatus.IN_PIPELINE);
	}

	@Override
	public List<String> getAllEPadFilePathsWithErrors()
	{
		return getAllEPadFilePathsWithStatus(PNGFileProcessingStatus.ERROR);
	}

	@Override
	public List<Map<String, String>> getEpadEventsForSessionID(String sessionID, boolean delete)
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

			if (!rows.isEmpty() && delete) { // Delete events up the most recent event for user
				log.info("Event search found " + rows.size() + " event(s) for session ID " + sessionID);
				String pk = rows.get(rows.size()-1).get("pk"); // We order by pk, an auto-increment field (which does not wrap)
				ps.close();
				ps = c.prepareStatement(EpadDatabaseCommands.DELETE_EVENTS_FOR_SESSIONID);
				ps.setString(1, sessionID);
				ps.setString(2, pk);
				int rowsAffected = ps.executeUpdate();
				log.info("" + rowsAffected + " old event(s) deleted for session ID " + sessionID);
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return rows;
	}

	@Override
	public List<Map<String, String>> getEpadEventsForAimID(String aimID)
	{
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_EVENTS_FOR_AIMID);
			ps.setString(1, aimID);
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
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return rows;
	}

	@Override
	public int deleteOldEvents() {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.DELETE_OLD_EVENTS);
			java.sql.Date oneHourAgo = new java.sql.Date(System.currentTimeMillis() - 60*60*1000);
			ps.setDate(1, oneHourAgo);
			int rows = ps.executeUpdate();
			return rows;
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return 0;
	}

	@Override
	public void forceDICOMReprocessing()
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.DELETE_ALL_FROM_EPAD_FILES);
			ps.executeUpdate();
			ps.close();
			ps = c.prepareStatement(EpadDatabaseCommands.DELETE_ALL_FROM_SERIES_STATUS);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
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
			log.warning("Database operation failed; debugInfo=" + debugInfo, e);
			throw e;
		} finally {
			close(c, ps, rs);
		}
	}
	
	@Override
	public int getAnnotationDoneUserCount(String projectUID, String subjectUID, String studyUID, String series_uid)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = -1;

		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_ANNOTATION_DONE_COUNT_FOR_SERIES_BY_IDs);
			ps.setString(1, projectUID);
			ps.setString(2, subjectUID);
			ps.setString(3, studyUID);
			ps.setString(4, series_uid);
			rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
				return count;
			} else
				return 0;
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
			return 0;
		} finally {
			close(c, ps, rs);
		}
	}
	
	@Override
	public AnnotationStatus getAnnotationStatusForUser(String projectUID, String subjectUID, String studyUID, String series_uid,
			String username)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int status = -1;

		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_ANNOTATION_STATUS_FOR_SERIES_BY_IDs);
			ps.setString(1, projectUID);
			ps.setString(2, subjectUID);
			ps.setString(3, studyUID);
			ps.setString(4, series_uid);
			ps.setString(5, username);
			rs = ps.executeQuery();
			if (rs.next()) {
				status = rs.getInt(1);
				return AnnotationStatus.getValue(status);
			} else
				return AnnotationStatus.NOT_STARTED;
		} catch (IllegalArgumentException e) {
			log.warning("Invalid enum value for " + AnnotationStatus.class.getName(), e);
			return null;
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
			return null;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public SeriesProcessingStatus getSeriesProcessingStatus(String seriesUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int status = -1;

		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_STATUS_FOR_SERIES_BY_ID);
			ps.setString(1, seriesUID);
			rs = ps.executeQuery();
			if (rs.next()) {
				status = rs.getInt(1);
				return SeriesProcessingStatus.getValue(status);
			} else
				return null;
		} catch (IllegalArgumentException e) {
			log.warning("Invalid enum value for " + SeriesProcessingStatus.class.getName(), e);
			return null;
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
			return null;
		} finally {
			close(c, ps, rs);
		}
	}
	
	@Override
	public EPADUsage getStats(String year)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int status = -1;
		
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.GET_YEARLY_CUMULATIVE_STAT);
			//we need to add % here
			ps.setString(1, '%'+year+'%');
			rs = ps.executeQuery();
			if (rs.next()) {
				return new EPADUsage(null, rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(9), rs.getInt(10), rs.getInt(8), rs.getInt(11), rs.getInt(13), rs.getInt(12), null);
			} else
				return null;
		} catch (IllegalArgumentException e) {
			log.warning("Invalid enum value for " + SeriesProcessingStatus.class.getName(), e);
			return null;
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
			return null;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public Timestamp getSeriesProcessingDate(String seriesUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int status = -1;

		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_STATUS_AND_CREATED_TIME_FOR_SERIES_BY_ID);
			ps.setString(1, seriesUID);
			rs = ps.executeQuery();
			if (rs.next()) {
				status = rs.getInt(1);
				if (status != SeriesProcessingStatus.IN_PIPELINE.getCode())
					return rs.getTimestamp(2);
				else {
					log.warning("Failed to get series processing date for series " + seriesUID);
					return null;
				}
			} else {
				log.warning("Failed to get series processing date for series " + seriesUID);
				return null;
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
			return null;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public void updateOrInsertSeries(String seriesUID, SeriesProcessingStatus seriesProcessingStatus)
	{
		if (!hasSeriesInEPadDatabase(seriesUID)) {
			recordNewSeries(seriesProcessingStatus, seriesUID);
		} else {
			updateSeriesProcessingStatus(seriesProcessingStatus, seriesUID);
		}
	}

	@Override
	public void updateSeriesDefaultTags(String seriesUID, String defaultTags) throws Exception {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.UPDATE_EPAD_SERIES_TAGS);
			ps.setString(1, defaultTags);
			ps.setString(2, seriesUID);
			ps.execute();
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
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
				log.warning("Database operation failed; debugInfo=" + debugInfo, e);
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
				log.warning("MySQL error getting auto increment key");
				throw new RuntimeException("MySQL error getting auto increment key");
			}
			for (int position = 0; position < termKeys.size(); position++) {
				int termKey = termKeys.get(position);
				ps = c.prepareStatement(EpadDatabaseCommands.INSERT_COORDINATION2TERM);
				ps.setInt(1, coordinationTermKey);
				ps.setInt(2, termKey);
				ps.setInt(3, position);
				ps.executeUpdate();
				ps.close();
			}

			String coordinationTermID = termIDPrefix + coordinationTermKey;
			ps = c.prepareStatement(EpadDatabaseCommands.UPDATE_COORDINATION);
			ps.setString(1, coordinationTermID);
			ps.setInt(2, coordinationTermKey);
			ps.executeUpdate();

			return new Term(coordinationTermID, schemaName, schemaVersion, description);
		} catch (SQLException e) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, e);
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
				log.warning("MySQL error getting auto increment key");
				throw new RuntimeException("MySQL error getting auto increment key");
			}
			return termID;
		} catch (SQLException e) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed;debugInfo=" + debugInfo, e);
			throw e;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public List<Map<String, String>> getCoordinationData(String coordinationID) throws Exception {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_COORDINATION_BY_ID);
			ps.setString(1, coordinationID);
			rs = ps.executeQuery();
			while (rs.next()) {
				coordinationID = rs.getString(1);
				String termID = rs.getString(2);
				String termSchema = rs.getString(3);
				String description = rs.getString(4);
				Map<String, String> coordination = new HashMap<String, String>();
				coordination.put("coordinationID", coordinationID);
				coordination.put("termID", termID);
				coordination.put("termSchema", termSchema);
				coordination.put("description", description);
				results.add(coordination);
			}
			return results;
		} catch (SQLException e) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed;debugInfo=" + debugInfo, e);
			throw e;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public Set<String> getAllSeriesUIDsFromEPadDatabase()
	{
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
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return retVal;
	}

	@Override
	public void deleteStudy(String studyUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.DELETE_FROM_EPAD_FILES);
			ps.setString(1, "%" + studyUID.replace('.', '_') + "%");
			log.info("delete sql:" + ps.toString());
			ps.executeUpdate();
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public void deleteSeries(String seriesUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();

			log.info("Deleting series " + seriesUID + " from ePAD files table");
			ps = c.prepareStatement(EpadDatabaseCommands.DELETE_FROM_EPAD_FILES);
			ps.setString(1, "%" + seriesUID.replace('.', '_') + "%");
			log.info("delete sql:" + ps.toString());
			int rows = ps.executeUpdate();
			ps.close();
			log.info("" + rows + " deleted from ePAD files table");
			ps = c.prepareStatement(EpadDatabaseCommands.DELETE_FROM_EPAD_FILES);
			ps.setString(1, "%" + seriesUID + "%");
			log.info("delete sql:" + ps.toString());
			rows = ps.executeUpdate();
			ps.close();
			log.info("" + rows + " deleted from ePAD files table");

			log.info("Deleting series " + seriesUID + " from ePAD status table");
			ps = c.prepareStatement(EpadDatabaseCommands.DELETE_SERIES_FROM_SERIES_STATUS);
			ps.setString(1, seriesUID);
			log.info("delete sql:" + ps.toString());
			rows = ps.executeUpdate();
			log.info("" + rows + " deleted from ePAD series status table");
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public void deleteSeriesOnly(String seriesUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();


			log.info("Deleting series " + seriesUID + " from ePAD status table");
			ps = c.prepareStatement(EpadDatabaseCommands.DELETE_SERIES_FROM_SERIES_STATUS);
			ps.setString(1, seriesUID);
			log.info("delete sql:" + ps.toString());
			int rows = ps.executeUpdate();
			log.info("" + rows + " deleted from ePAD series status table");
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public void insertEpadEvent(String sessionID, String event_status, String aim_uid, String aim_name,
			String patient_id, String patient_name, String template_id, String template_name, String plugin_name)
	{
		insertEpadEvent(sessionID, event_status,
				aim_uid, aim_name, patient_id,
				patient_name, template_id, template_name,
				plugin_name, "", "",
				"", "",false);
	}

	@Override
	public void insertEpadEvent(String sessionID, String event_status,
			String aim_uid, String aim_name, String patient_id,
			String patient_name, String template_id, String template_name,
			String plugin_name, String projectID, String projectName,
			String studyUID, String seriesUID, boolean error) {
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
			ps.setString(10, projectID);
			ps.setString(11, projectName);
			ps.setString(12, seriesUID);
			ps.setString(13, studyUID);
			ps.setString(14, String.valueOf(error));
			ps.execute();
		} catch (SQLException sqle) {
			log.warning("Database operation failed.", sqle);
		} catch (Exception e) {
			log.warning("Database operation (insert event) failed for AIM ID " + aim_uid, e);
		} finally {
			close(c, ps);
		}
	}

	// Enter short event
	@Override
	public void insertEpadEvent(String sessionID, String message, String name, String target)
	{
		Connection c = null;
		PreparedStatement ps = null;
		try {
			// logger.info("Inserting into event table: " + sessionID + " EVENT:" + aim_uid);

			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.INSERT_INTO_EVENT);
			ps.setString(1, sessionID);
			ps.setString(2, message);
			ps.setString(3, "");
			ps.setString(4, "");
			ps.setString(5, "");
			ps.setString(6, name);
			ps.setString(7, "");
			ps.setString(8, "");
			ps.setString(9, target);
			ps.execute();
		} catch (SQLException sqle) {
			log.warning("Database operation failed.", sqle);
		} catch (Exception e) {
			log.warning("Database operation (insert event) failed", e);
		} finally {
			close(c, ps);
		}
	}

	/**
	 * Cross database query that gets all image UIDs for a series if the corresponding image is recorded in the epad_files
	 * table.
	 * 
	 * @param seriesIUID String
	 * @return List of String (sopInstanceIds).
	 */
	@Override
	public Set<String> getImageUIDsInSeries(String seriesUID)
	{
		Set<String> retVal = new HashSet<String>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_EPAD_IMAGE_UIDS_FOR_SERIES);
			ps.setString(1, seriesUID);
			if (log.isDebugEnabled())
				log.debug(ps.toString());

			rs = ps.executeQuery();
			while (rs.next()) {
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
	public void deleteObsoleteEpadFileEntries() {
		Connection c = null;
		Statement s = null;
		try {
			c = getConnection();
			s = c.createStatement();
			log.info("delete sql:" + EpadDatabaseCommands.CLEANUP_OBSOLETE_EPAD_FILES);
			int rows = s.executeUpdate(EpadDatabaseCommands.CLEANUP_OBSOLETE_EPAD_FILES);
			log.info("Number of rows deleted:" + rows);
		} catch (SQLException sqle) {
			log.warning("Database operation failed", sqle);
		} finally {
			close(c, s);
		}
	}

	@Override
	public EPADDataList getEpadHostNames() {
		EPADDataList retVal = new EPADDataList();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_DISTINCT_EPADS);
			rs = ps.executeQuery();
			while (rs.next()) {
				String hostname = rs.getString(1);
				if (hostname.indexOf(":") != -1)
					hostname = hostname.substring(0, hostname.indexOf(":")).trim();
				retVal.addData(new EPADData(hostname, hostname, rs.getString(1), null));
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
	public int getNumberOfSeries() {
		int retVal = 0;
		Connection c = null;
		Statement s = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			s = c.createStatement();
			rs = s.executeQuery("Select count(*) from epaddb.series_status");
			if (rs.next()) {
				retVal = rs.getInt(1);
			}
			rs.close();
			rs = s.executeQuery("Select count(*) from epaddb.nondicom_series");
			if (rs.next()) {
				retVal = retVal + rs.getInt(1);
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, s, rs);
		}
		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations#insertDBObject(edu.stanford.epad.epadws.models.dao.AbstractDAO, java.lang.String, java.lang.String[][])
	 * Structure of dbColumns array in all methods below:
	 * java fieldName,java fieldType,db columnName,db columnType
	 * eg:
	 * 	{"id","long","Id",""}, // autoincrement
	 * 	{"name","String","name","varchar"},
	 *	{"numOfErrors","int","num_of_errors","integer"},
	 */
	@Override
	public Object insertDBObject(Object dbObject, String tableName, String[][] columns) throws Exception
	{
		Connection dbCon = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String insertSQL = getInsertSQL(tableName, columns);
		try
		{
			ps = dbCon.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
			setSQLValues(columns, ps, dbObject);
			if (log.isDebugEnabled())
				log.debug("insert sql:" + ps.toString());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if (rs.next())
			{
				String methodName = "set" + columns[0][0].substring(0,1).toUpperCase() + columns[0][0].substring(1);
				Integer value = rs.getInt(1);
				try
				{
					Method method = dbObject.getClass().getMethod(methodName, new Class[] {int.class});
					method.invoke(dbObject, new Object[] {value});
				}
				catch(NoSuchMethodException ne)
				{
					try
					{
						Method method = dbObject.getClass().getMethod(methodName, new Class[] {long.class});
						method.invoke(dbObject, new Object[] {value});
					}catch(Exception e)
					{
						e.printStackTrace();
						throw new IllegalArgumentException(e.getMessage());
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					throw new IllegalArgumentException(e.getMessage());
				}
			}
		}
		catch (SQLException x)
		{
			x.printStackTrace();
			throw x;
		}
		finally
		{
			close(dbCon, ps, rs);
		}
		return dbObject;
	}


	@Override
	public Object updateDBObject(Object dbObject, String dbTable, String[][] dbColumns) throws Exception
	{	   
		PreparedStatement ps = null;
		ResultSet rs = null;

		String updateSQL = getUpdateSQL(dbTable, dbColumns);
		Connection dbCon = null;
		try
		{
			dbCon = getConnection();
			//log.debug("Update:" + updateSQL);
			ps = dbCon.prepareStatement(updateSQL);
			setSQLValues(dbColumns, ps, dbObject);
			String methodName = "get" + dbColumns[0][0].substring(0,1).toUpperCase() + dbColumns[0][0].substring(1);
			try
			{
				Method method = dbObject.getClass().getMethod(methodName, (Class[])null);
				if (dbColumns[0][3].equalsIgnoreCase("Id") && dbColumns[0][1].equalsIgnoreCase("long"))
				{
					Long value = (Long) method.invoke(dbObject, (Object[])null);
					ps.setLong(dbColumns.length, value);					
				}
				else if (dbColumns[0][3].equalsIgnoreCase("Id"))
				{
					Integer value = (Integer) method.invoke(dbObject, (Object[])null);
					ps.setInt(dbColumns.length, value);					
				}
				else if (dbColumns[0][2].equalsIgnoreCase("String"))
				{
					String value = (String) method.invoke(dbObject, (Object[])null);
					ps.setString(dbColumns.length, value);					
				}
			}
			catch (Exception e)
			{
				log.warning("Error setting DB values", e);
				throw new IllegalArgumentException(e.getMessage());
			}
			if (log.isDebugEnabled())
				log.debug("update sql:" + ps.toString());
			ps.executeUpdate();
			ps.close();
		}
		finally
		{
			close(dbCon, ps, rs);
		}
		return dbObject;
	}

	@Override
	public int deleteDBObject(String dbTable, long id) throws Exception {
		return deleteDBObjects(dbTable, "id =" + id);
	}

	@Override
	public int deleteDBObjects(String dbTable, String criteria)
			throws Exception {
		criteria = criteria.trim();
		if (!criteria.toLowerCase().startsWith("where"))
			criteria = "where " + criteria;
		PreparedStatement ps = null;
		Connection dbCon = null;
		try
		{
			if (log.isDebugEnabled())
				log.debug("delete from " + dbTable + " " + criteria);
			dbCon = getConnection();
			ps = dbCon.prepareStatement("delete from " + dbTable + " " + criteria);
			int rows = ps.executeUpdate();
			if (log.isDebugEnabled())
				log.debug("" + rows + " rows deleted");
			return rows;
		}
		finally
		{
			close(dbCon, ps);
		}
	}

	@Override
	public List getDBObjects(Class dbClass, String dbTable, String[][] dbColumns, String criteria, int startRecord, int maxRecords, boolean distinct) throws Exception {
		criteria = criteria.trim();
		if (criteria.length() > 0 && !criteria.toLowerCase().startsWith("where"))
			criteria = "where " + criteria;
		Statement stmt = null;
		ResultSet rs = null;
		List datas = new ArrayList();
		Connection dbCon = null;
		try
		{
			dbCon = getConnection();
			if (startRecord == 0)
				stmt = dbCon.createStatement();
			else
				stmt = dbCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM "  + dbTable + " a " + criteria;
			if (distinct)
				sql = "SELECT DISTINCT * FROM "  + dbTable + " a " + criteria;
			if (maxRecords <= 0)
				maxRecords = 5000;
			if (startRecord > 0)
			{
				sql = sql + " LIMIT " + (startRecord-1) + "," + maxRecords;
			}
			else
				stmt.setMaxRows(maxRecords);
			if (log.isDebugEnabled())
				log.debug("Query:" + sql);
			rs = stmt.executeQuery(sql);
			Object data = null;
			while (rs.next()) 
			{
				data = dbClass.newInstance();
				getSQLValues(dbColumns, rs, data);
				datas.add(data);
			}
			if (log.isDebugEnabled())
				log.debug("Returned:" + datas.size() + " rows");
			return datas;
		}
		finally
		{
			close(dbCon, stmt, rs);
		}
	}

	@Override
	public Object retrieveObjectById(Object dbObject, long id, String dbTable,
			String[][] dbColumns) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		Connection dbCon = null;
		try
		{
			dbCon = getConnection();
			stmt = dbCon.createStatement();
			String sql = "SELECT * FROM "  + dbTable + " where id = " + id;
			if (log.isDebugEnabled())
				log.debug("Query:" + sql);
			rs = stmt.executeQuery(sql);
			Object data = null;
			if (rs.next()) 
			{
				getSQLValues(dbColumns, rs, dbObject);
				return dbObject;
			}
			return null;
		}
		finally
		{
			close(dbCon, stmt, rs);
		}
	}

	@Override
	public List<Long> getDBIds(String dbTable, String criteria,
			int startRecord, int maxRecords) throws Exception {
		criteria = criteria.trim();
		if (criteria.length() > 0 && !criteria.toLowerCase().startsWith("where"))
			criteria = "where " + criteria;
		Statement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List datas = new ArrayList();
		Connection dbCon = null;
		try
		{
			dbCon = getConnection();
			if (startRecord == 0)
				stmt = dbCon.createStatement();
			else
				stmt = dbCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT id FROM "  + dbTable + " " + criteria;
			if (maxRecords <= 0)
				maxRecords = 5000;
			if (startRecord > 0)
			{
				sql = sql + " LIMIT " + (startRecord+1) + "," + maxRecords;
			}
			else
				stmt.setMaxRows(maxRecords);
			log.debug("Query:" + sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				Long id = rs.getLong(1);
				datas.add(id);
			}
			log.debug("Returned:" + datas.size() + " rows");
			return datas;
		}
		finally
		{
			close(dbCon, ps, rs);
		}
	}

	@Override
	public int getDBCount(String dbTable, String criteria) throws Exception {
		criteria = criteria.trim();
		if (criteria.length() > 0 && !criteria.toLowerCase().startsWith("where"))
			criteria = "where " + criteria;
		Statement stmt = null;
		ResultSet rs = null;
		Connection dbCon = null;
		try
		{
			dbCon = getConnection();
			stmt = dbCon.createStatement();
			String sql = "SELECT count(*) FROM "  + dbTable + " " + criteria;
			if (log.isDebugEnabled())
				log.debug("Query:" + sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) 
			{
				int count = rs.getInt(1);
				if (log.isDebugEnabled())
					log.debug("Returned:" + count + " rows");
				return count;
			}
			else
				throw new SQLException ("Error retrieve count");
		}
		finally
		{
			close(dbCon, stmt, rs);
		}
	}

	private String getInsertSQL(String tableName, String[][]columns)
	{
		String insertSQL = "INSERT INTO " + tableName + " (";
		String values = ") VALUES (";
		String delim = "";
		for (int i = 0; i < columns.length; i++)
		{
			if (columns[i][3].equalsIgnoreCase("Id")) continue;
			insertSQL = insertSQL + delim + columns[i][2];
			values = values + delim + "?";
			delim = ",";
		}
		insertSQL = insertSQL + values +")";
		return insertSQL;
	}

	protected String getUpdateSQL(String tableName, String[][]columns)
	{
		String updateSQL = "UPDATE " + tableName + " set ";
		String idcol = "id";
		String comma = "";
		String whereClause = null;
		for (int i = 0; i < columns.length; i++)
		{
			if (columns[i][3].equalsIgnoreCase("Id") || columns[i][3].equalsIgnoreCase("RID") || columns[i][3].equalsIgnoreCase("CID"))
			{
				idcol = columns[i][2];				
				if (whereClause == null)
					whereClause = idcol + " = ?";
				else
					whereClause = " and " + idcol + " = ?";
				continue;
			}
			updateSQL = updateSQL + comma + columns[i][2] + " = ?";
			comma = ",";
		}
		updateSQL = updateSQL + " where " + whereClause;
		return updateSQL;		
	}

	/**
	 * Sets data values in insert/update preparedStatement using the object's column structure.
	 * @param columns - see above
	 * @param ps
	 * @param data
	 * @throws SQLException
	 */
	protected void setSQLValues(String[][]columns, PreparedStatement ps, Object data)
			throws SQLException
	{
		int i = 1;
		int j = 0;
		try
		{
			for (j = 0; j < columns.length; j++)	
			{
				if (columns[j][3].equalsIgnoreCase("Id"))
				{
					continue;				
				}
				String methodName = "get" + columns[j][0].substring(0,1).toUpperCase() + columns[j][0].substring(1);
				if (columns[j][1].equalsIgnoreCase("Boolean"))
					methodName = "is" + columns[j][0].substring(0,1).toUpperCase() + columns[j][0].substring(1);
				Method method = null;
				try {
					method = data.getClass().getMethod(methodName, (Class[])null);
				} catch (Exception x) {
					if (methodName.startsWith("is"))
						method = data.getClass().getMethod("get" + methodName.substring(2), (Class[])null);
				}
				if (columns[j][1].equalsIgnoreCase("String"))
				{
					String value = (String) method.invoke(data, (Object[])null);
					if (value != null)
						ps.setString(i, value);
					else
						ps.setNull(i, java.sql.Types.VARCHAR);		
				}
				else if (columns[j][1].equalsIgnoreCase("Integer") || columns[j][1].equalsIgnoreCase("int"))
				{
					Integer value = (Integer) method.invoke(data, (Object[])null);
					if (value != null)
						ps.setInt(i, value);
					else
						ps.setNull(i, java.sql.Types.INTEGER);		
				}
				else if (columns[j][1].equalsIgnoreCase("Long") || columns[j][1].equalsIgnoreCase("long"))
				{
					Long value = (Long) method.invoke(data, (Object[])null);
					if (value != null)
						ps.setLong(i, value);
					else
						ps.setNull(i, java.sql.Types.INTEGER);		
				}
				else if (columns[j][1].equalsIgnoreCase("Double"))
				{
					Double value = (Double) method.invoke(data, (Object[])null);
					if (value != null)
						ps.setDouble(i, value);
					else
						ps.setNull(i, java.sql.Types.DOUBLE);		
				}
				else if (columns[j][1].equalsIgnoreCase("Boolean"))
				{
					Boolean value = (Boolean) method.invoke(data, (Object[])null);
					if (value != null)
						ps.setBoolean(i, value);
					else
						ps.setInt(i, 0);		
				}
				else if (columns[j][1].equalsIgnoreCase("Timestamp"))
				{
					Timestamp value = (Timestamp) method.invoke(data, (Object[])null);
					if (value != null)
						ps.setTimestamp(i, new java.sql.Timestamp(value.getTime()));
					else
						ps.setNull(i, java.sql.Types.TIMESTAMP);		
				}
				else if (columns[j][1].equalsIgnoreCase("Date"))
				{
					Date value = (Date) method.invoke(data, (Object[])null);
					if (value != null)
						ps.setTimestamp(i, new java.sql.Timestamp(value.getTime()));
					else
						ps.setNull(i, java.sql.Types.DATE);		
				}
				i++;
			}
		}
		catch (Exception x)
		{
			x.printStackTrace();
			throw new SQLException("Error setting values for " + columns[j][0] + " : " + x.getMessage());
		}
	}

	/**
	 * Gets data values from result set and populates object using the object's column structure.
	 * @param columns - see above
	 * @param rs
	 * @param data
	 * @throws SQLException
	 */
	protected void getSQLValues(String[][]columns, ResultSet rs, Object data)
			throws SQLException
	{
		int i = 0;
		try
		{
			for (i = 0; i < columns.length; i++)	
			{
				String methodName = "set" + columns[i][0].substring(0,1).toUpperCase() + columns[i][0].substring(1);
				if (columns[i][1].equals("String"))
				{
					String value = rs.getString(columns[i][2]);
					Method method = data.getClass().getMethod(methodName, new Class[] {String.class});
					method.invoke(data, new Object[] {value});
				}
				else if (columns[i][1].equals("Integer"))
				{
					Integer value = rs.getInt(columns[i][2]);
					if (rs.getObject(columns[i][2]) == null) value = null;
					Method method = data.getClass().getMethod(methodName, new Class[] {Integer.class});
					method.invoke(data, new Object[] {value});
				}
				else if (columns[i][1].equals("int"))
				{
					Integer value = rs.getInt(columns[i][2]);
					Method method = data.getClass().getMethod(methodName, new Class[] {int.class});
					method.invoke(data, new Object[] {value});
				}
				else if (columns[i][1].equals("Long"))
				{
					Long value = rs.getLong(columns[i][2]);
					if (rs.getObject(columns[i][2]) == null) value = null;
					Method method = data.getClass().getMethod(methodName, new Class[] {Long.class});
					method.invoke(data, new Object[] {value});
				}
				else if (columns[i][1].equals("long"))
				{
					Long value = rs.getLong(columns[i][2]);
					Method method = data.getClass().getMethod(methodName, new Class[] {long.class});
					method.invoke(data, new Object[] {value});
				}
				else if (columns[i][1].equals("Boolean"))
				{
					Boolean value = rs.getBoolean(columns[i][2]);
					if (rs.getObject(columns[i][2]) == null) value = null;
					Method method = data.getClass().getMethod(methodName, new Class[] {Boolean.class});
					method.invoke(data, new Object[] {value});
				}
				else if (columns[i][1].equals("boolean"))
				{
					Boolean value = rs.getBoolean(columns[i][2]);
					Method method = data.getClass().getMethod(methodName, new Class[] {boolean.class});
					method.invoke(data, new Object[] {value});
				}
				else if (columns[i][1].equals("Timestamp"))
				{
					Timestamp value = rs.getTimestamp(columns[i][2]);
					Method method = data.getClass().getMethod(methodName, new Class[] {Timestamp.class});
					method.invoke(data, new Object[] {value});
				}
				else if (columns[i][1].equals("Date"))
				{
					Timestamp stamp = rs.getTimestamp((columns[i][2]));
					Date value = null;
					if (stamp != null)
						value = new Date(stamp.getTime());
					Method method = data.getClass().getMethod(methodName, new Class[] {Date.class});
					method.invoke(data, new Object[] {value});
				}
				else if (columns[i][1].equals("Double"))
				{
					Double value = rs.getDouble(columns[i][2]);
					if (rs.getObject(columns[i][2]) == null) value = null;
					Method method = data.getClass().getMethod(methodName, new Class[] {Double.class});
					method.invoke(data, new Object[] {value});
				}
				else if (columns[i][1].equals("double"))
				{
					Double value = rs.getDouble(columns[i][2]);
					Method method = data.getClass().getMethod(methodName, new Class[] {double.class});
					method.invoke(data, new Object[] {value});
				}
			}
		}
		catch (Exception x)
		{
			x.printStackTrace();
			throw new SQLException("Error setting values for " + columns[i][0] + ":" + x.getMessage());
		}
	}

	private List<String> getAllEPadFilePathsWithStatus(PNGFileProcessingStatus pngFileProcessingStatus)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<String>();

		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_ALL_EPAD_FILE_PATHS_WITH_STATUS);
			ps.setInt(1, pngFileProcessingStatus.getCode());
			rs = ps.executeQuery();
			while (rs.next()) {
				result.add(rs.getString(1));
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
			return null;
		} finally {
			close(c, ps, rs);
		}
		return result;
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
			log.warning("Database operation failed; debugInfo=" + debugInfo, e);
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

	private int getSeriesProcessingStatusCode(Map<String, String> filesTableRowData)
	{
		try {
			String fileStatus = filesTableRowData.get("file_status");
			if (fileStatus != null) {
				return Integer.parseInt(fileStatus);
			} else
				return SeriesProcessingStatus.ERROR.getCode();
		} catch (Exception e) {
			log.warning("failed to parse file_status.", e);
			return SeriesProcessingStatus.ERROR.getCode();
		}
	}

	@Override
	public boolean runSQLScript(String script) {
		Connection c = null;
		Statement s = null;
		String[] sqlstatements = script.split(";");
		try {
			c = getConnection();
			s = c.createStatement();
			for (String sql: sqlstatements)
			{
				if (sql.trim().length() != 0 && !sql.trim().startsWith("-- "))
					s.addBatch(sql.trim());
			}
			s.executeBatch();
			return true;
		} catch (Exception e) {
			log.warning("Database operation failed", e);
		} finally {
			close(c);
		}
		return false;
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
		} else if ("".equalsIgnoreCase(value)) {
			return def;
		}
		return value;
	}

	@Override
	public boolean hasSeriesInEPadDatabase(String seriesIUID)
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
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
			return false;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public String getSeriesDefaultTags(String seriesUID) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.GET_EPAD_SERIES_TAGS);
			ps.setString(1, seriesUID);
			rs = ps.executeQuery();
			if (rs.next())
			{
				String tags = rs.getString(1);
				return tags;
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		return null;
	}

	@Override
	public boolean hasStudyInDCM4CHE(String studyIUID) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.SELECT_DCM4CHE_STUDY_BY_ID);
			ps.setString(1, studyIUID);
			rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
			return false;
		} finally {
			close(c, ps, rs);
		}
	}

	@Override
	public String getDBVersion() {
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			c = getConnection();
			ps = c.prepareStatement("SELECT version FROM epaddb.dbversion");
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("version");
			}
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
			return null;
		} finally {
			close(c, ps, rs);
		}
		return null;
	}

	private void recordNewSeries(SeriesProcessingStatus seriesProcessingStatus, String seriesUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.INSERT_INTO_EPAD_SERIES_STATUS);
			ps.setString(1, seriesUID);
			ps.setInt(2, seriesProcessingStatus.getCode());
			ps.execute();
		} catch (SQLException sqle) {
			log.warning("Database operation failed", sqle);
		} catch (Exception e) {
			log.warning("Database operation failed; series=" + seriesUID, e);
		} finally {
			close(c, ps);
		}
	}

	private void updateSeriesProcessingStatus(SeriesProcessingStatus newSeriesProcessingStatus, String seriesUID)
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.UPDATE_EPAD_SERIES_STATUS);
			ps.setInt(1, newSeriesProcessingStatus.getCode());
			ps.setString(2, seriesUID);
			ps.execute();
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}

	private Connection getConnection() throws SQLException
	{
		return connectionPool.getConnection();
	}

	private void close(Connection c)
	{
		connectionPool.freeConnection(c);
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
		DatabaseUtils.close(rs);
		close(c, ps);
	}

	@Override
	public int getAIMCount(String projectID, String studyUID, String username) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.getAIMCount(projectID, studyUID, username);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return 0;
	}
	
	@Override
	public void calcMonthlyCumulatives()
	{
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement(EpadDatabaseCommands.CALCULATE_MONTHLY_CUMULATIVE_USAGE);
			ps.execute();
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
	}
}
