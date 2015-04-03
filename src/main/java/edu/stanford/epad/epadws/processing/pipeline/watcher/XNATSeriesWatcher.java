package edu.stanford.epad.epadws.processing.pipeline.watcher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.processing.model.SeriesProcessingDescription;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * Monitors the XNAT series queue. The queue contains descriptions of new DICOM series that have been uploaded to ePAD's
 * DCM4CHEE instance (and are modeled by {@link SeriesProcessingDescription} objects). Here, we create the XNAT subject
 * and study record for each series. The series data will then be subsequently process by the {@link DICOMSeriesWatcher}
 * .
 * <p>
 * This queue is populated by a {@link Dcm4CheeDatabaseWatcher}, which monitors a DCM4CHEE MySQL database for new DICOM
 * series.
 * 
 * @author martin
 * @see DICOMSeriesWatcher, SeriesProcessingDescription
 */
public class XNATSeriesWatcher implements Runnable
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final EpadOperations epadOperations = DefaultEpadOperations.getInstance();
	
	private static final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();	

	private final String xnatUploadProjectID;
	private final String xnatUploadProjectUser;
	private final BlockingQueue<SeriesProcessingDescription> xnatSeriesWatcherQueue;
	private final ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();

	private String jsessionID = null;

	public XNATSeriesWatcher(BlockingQueue<SeriesProcessingDescription> xnatSeriesWatcherQueue)
	{
		this.xnatSeriesWatcherQueue = xnatSeriesWatcherQueue;
		this.xnatUploadProjectID = EPADConfig.xnatUploadProjectID;
		this.xnatUploadProjectUser = EPADConfig.xnatUploadProjectUser;

		log.info("Starting the XNAT/EPAD series watcher");
	}

	@Override
	public void run()
	{
		while (!shutdownSignal.hasShutdown()) {
			try {
				SeriesProcessingDescription seriesProcessingDescription = xnatSeriesWatcherQueue.poll(5000,
						TimeUnit.MILLISECONDS);

				if (seriesProcessingDescription != null) {
					validateSeriesProcessingDescription(seriesProcessingDescription);

					String studyUID = seriesProcessingDescription.getStudyUID();
					String subjectID = seriesProcessingDescription.getSubjectID();
					String patientName = seriesProcessingDescription.getPatientName();

					log.info("XNAT/EPAD series watcher processing study " + studyUID + " for subject " + patientName + " with ID "
							+ subjectID);

					if (updateSessionIDIfNecessary()) {
						// We create the XNAT subject and study here. The series will subsequently arrive from dcm4chee where it
						// will be processed by the DICOMSeriesWatcher, which will process the series images.
						if (!EPADConfig.UseEPADUsersProjects) {
							epadOperations.createSubjectAndStudy(xnatUploadProjectUser, xnatUploadProjectID, subjectID, patientName, studyUID, jsessionID);
						} else {
							projectOperations.createSubject(xnatUploadProjectUser, subjectID, patientName, null, null);
							projectOperations.createStudy(xnatUploadProjectUser, studyUID, subjectID,"");
							projectOperations.addStudyToProject(xnatUploadProjectUser, studyUID, subjectID, xnatUploadProjectID);
						}
					} else
						log.warning("Unable to validate with XNAT to upload study " + studyUID + " for subject " + patientName
								+ " in project " + xnatUploadProjectID);
				}
			} catch (Exception e) {
				log.severe("Exception in XNAT/EPAD series watcher thread", e);
			}
		}
	}

	/**
	 * 
	 * @return True if successfully updated, false otherwise
	 */
	private boolean updateSessionIDIfNecessary()
	{
		if (!EPADConfig.UseEPADUsersProjects) {
			if (!XNATSessionOperations.hasValidXNATSessionID(this.jsessionID)) { // Validating will extend validity
				String sessionID = XNATSessionOperations.getXNATAdminSessionID();
				if (sessionID != null) {
					this.jsessionID = sessionID;
					return true;
				} else
					return false;
			} else
				return true;
		} else {
			this.jsessionID = ""; // Not needed by EPAD Project Service
			return true;
		}
	}

	private void validateSeriesProcessingDescription(SeriesProcessingDescription seriesProcessingDescription)
			throws IllegalArgumentException
	{
		if (seriesProcessingDescription == null)
			throw new IllegalArgumentException("Missing series description");
		if (seriesProcessingDescription.getSeriesUID().length() == 0)
			throw new IllegalArgumentException("Missing series UID in series description");
		if (seriesProcessingDescription.getSubjectID().length() == 0)
			throw new IllegalArgumentException("Missing patient ID in series description");
		if (seriesProcessingDescription.getPatientName().length() == 0)
			throw new IllegalArgumentException("Missing patient name in series description");
	}
}