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
import edu.stanford.epad.dtos.EPADAIM;
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
				log.info("Fixing pngFile:" + pngFilePath + " imageUID:" + imageUID + " imagePath:" + imagePath);
				rs.close();
				ps.close();
				ps = c.prepareStatement(EpadDatabaseCommands.SELECT_EPAD_FILE_PATH_BY_IMAGE_UID);
				ps.setString(1, "%/" + imageUID + ".png");
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
					adb.refreshTheAnnotationTable(AimVersion.AIMv4_0);
				}
			} catch (SQLException | AimException | IOException sqle) {
				log.warning("AIM Database operation failed:", sqle);
			} finally {
				close(c);
			}
		}
		if (!EPADConfig.useV4.equals("false"))
		{
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
			return adb.getAIMCount(null, reference.projectID, reference.subjectID, null, null, null, 0);
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
			String aimID, String aimXML) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, 0, null, aimXML);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
		return null;
	}

	@Override
	public EPADAIM addDSOAIM(String userName, ImageReference reference,
			String dsoSeriesUID, String aimID, String aimXML) {
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			return adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, 0, dsoSeriesUID, aimXML);
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
			log.warning("Database operation failed.", sqle);
		} catch (Exception e) {
			log.warning("Database operation (insert event) failed for AIM ID " + aim_uid, e);
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
			s.executeUpdate(EpadDatabaseCommands.CLEANUP_OBSOLETE_EPAD_FILES);
		} catch (SQLException sqle) {
			log.warning("Database operation failed", sqle);
		} finally {
			close(c, s);
		}
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
			log.debug("delete from " + dbTable + " " + criteria);
			dbCon = getConnection();
			ps = dbCon.prepareStatement("delete from " + dbTable + " " + criteria);
			int rows = ps.executeUpdate();
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
            String sql = "SELECT * FROM "  + dbTable + " " + criteria;
            if (distinct)
            	sql = "SELECT DISTINCT * FROM "  + dbTable + " " + criteria;
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
            Object data = null;
		    while (rs.next()) 
		    {
		    	data = dbClass.newInstance();
		    	getSQLValues(dbColumns, rs, data);
		    	datas.add(data);
		    }
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
            log.info("Query:" + sql);
            rs = stmt.executeQuery(sql);
		    if (rs.next()) 
		    {
		    	int count = rs.getInt(1);
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
		} else if ("".equalsIgnoreCase("")) {
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
		close(c, s);
		DatabaseUtils.close(rs);
	}
	
	private void close(Connection c, PreparedStatement ps, ResultSet rs)
	{
		close(c, ps);
		DatabaseUtils.close(rs);
	}
}
