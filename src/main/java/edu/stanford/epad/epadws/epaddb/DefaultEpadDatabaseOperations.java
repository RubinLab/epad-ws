package edu.stanford.epad.epadws.epaddb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.aim.AIMDatabaseOperations;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.handlers.coordination.Term;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
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
			String imagePath = imageUID.replace('.', '_');
			if (!pngFilePath.contains(imagePath))
			{
				rs.close();
				ps.close();
				ps = c.prepareStatement(EpadDatabaseCommands.SELECT_EPAD_FILE_PATH_BY_IMAGE_UID);
				ps.setString(1, "%/" + imagePath + "%");
				rs = ps.executeQuery();
				if (rs.next()) {
					pngFilePath = rs.getString(1);
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
	public List<EPADAIM> getAIMs(String dsoSeriesUID) {
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
	public void addAIM(String userName, ProjectReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.insert(aimID, userName, reference.projectID, null, null, null, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public void addAIM(String userName, StudyReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, null, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public void addAIM(String userName, SeriesReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, null, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public void addAIM(String userName, ImageReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, 0);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public void addDSOAIM(String userName, ImageReference reference, String dsoSeriesUID, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, 0, dsoSeriesUID);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
	}

	@Override
	public void addAIM(String userName, FrameReference reference, String aimID)
	{
		Connection c = null;
		try {
			c = getConnection();
			AIMDatabaseOperations adb = new AIMDatabaseOperations(c, EPADConfig.eXistServerUrl,
					EPADConfig.aim4Namespace, EPADConfig.eXistCollection, EPADConfig.eXistUsername, EPADConfig.eXistPassword);
			adb.insert(aimID, userName, reference.projectID, reference.subjectID, reference.studyUID, reference.seriesUID, reference.imageUID, reference.frameNumber);
		} catch (SQLException sqle) {
			log.warning("AIM Database operation failed:", sqle);
		} finally {
			close(c);
		}
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
				String pk = rows.get(0).get("pk"); // We order by pk, an auto-increment field (which does not wrap)
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
