package edu.stanford.isis.epadws.processing.pipeline.watcher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.processing.model.DicomSeriesDescription;
import edu.stanford.isis.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.isis.epadws.xnat.XNATUtil;

public class XNATSeriesWatcher implements Runnable
{
	private final BlockingQueue<DicomSeriesDescription> xnatSeriesWatcherQueue;
	private final ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();

	private static final EPADLogger logger = EPADLogger.getInstance();
	private static final EPADConfig config = EPADConfig.getInstance();

	private final String uploadProjectID;
	private final String uploadProjectUser;
	private final String uploadProjectPassword;
	private String jsessionID;

	public XNATSeriesWatcher(BlockingQueue<DicomSeriesDescription> xnatSeriesWatcherQueue)
	{
		this.xnatSeriesWatcherQueue = xnatSeriesWatcherQueue;

		uploadProjectID = config.getStringConfigurationParameter("XNATUploadProjectID");
		uploadProjectUser = config.getStringConfigurationParameter("XNATUploadProjectUser");
		uploadProjectPassword = config.getStringConfigurationParameter("XNATUploadProjectPassword");

		jsessionID = null;
	}

	@Override
	public void run()
	{
		while (!shutdownSignal.hasShutdown()) {
			try {
				DicomSeriesDescription dicomSeriesDescription = xnatSeriesWatcherQueue.poll(5000, TimeUnit.MILLISECONDS);

				if (dicomSeriesDescription != null) {
					String studyIUID = dicomSeriesDescription.getStudyIUID();
					String patientID = dicomSeriesDescription.getPatientID();
					String patientName = dicomSeriesDescription.getPatientName();

					logger.info("XNAT series watcher found new study with ID" + studyIUID + " for patient " + patientName
							+ " with ID " + patientID);

					createXNATStudy(uploadProjectID, patientID, patientName, studyIUID);
				}
			} catch (Exception e) {
				logger.warning("Exception in XNATSeriesWatcher thread.", e);
			}
		}
	}

	private void createXNATStudy(String projectID, String subjectID, String subjectName, String studyIUID)
	{
		if (updateSessionID()) {
			XNATUtil.createXNATProject(uploadProjectID, uploadProjectID, jsessionID);

			if (XNATUtil.createXNATSubject(projectID, subjectName, subjectID, jsessionID))
				logger.info("Successfully created XNAT subject " + subjectName);

			if (XNATUtil.createXNATStudy(projectID, subjectID, studyIUID, jsessionID))
				logger.info("Successfully created XNAT study " + studyIUID);
		} else {
			logger.warning("Could not log into XNAT to upload study " + studyIUID);
		}
	}

	/**
	 * 
	 * @return True if successfully updated, false otherwise
	 */
	private boolean updateSessionID()
	{
		if (jsessionID != null && !XNATUtil.hasValidXNATSessionID(jsessionID)) { // Validating will extend validity
			XNATUtil.XNATSessionResponse xnatSessionResponse = XNATUtil.invokeXNATSessionIDService(uploadProjectUser,
					uploadProjectPassword);
			if (xnatSessionResponse.statusCode != HttpServletResponse.SC_OK) {
				logger.warning("Error invoking XNAT session service for study upload; statusCode = "
						+ xnatSessionResponse.statusCode);
				jsessionID = null;
				return false;
			} else {
				jsessionID = xnatSessionResponse.response;
				return true;
			}
		} else
			return true;
	}

}
