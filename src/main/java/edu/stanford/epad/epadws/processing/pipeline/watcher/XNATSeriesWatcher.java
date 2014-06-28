package edu.stanford.epad.epadws.processing.pipeline.watcher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.processing.model.DicomSeriesProcessingDescription;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

/**
 * Monitors the XNAT series queue. The queue contains descriptions of new DICOM series that have been uploaded to ePAD's
 * DCM4CHEE instance (and are modeled by {@link DicomSeriesProcessingDescription} objects).
 * <p>
 * This queue is populated by a {@link Dcm4CheeDatabaseWatcher}, which monitors a DCM4CHEE MySQL database for new DICOM
 * series.
 * 
 * @author martin
 */
public class XNATSeriesWatcher implements Runnable
{
	private final BlockingQueue<DicomSeriesProcessingDescription> xnatSeriesWatcherQueue;
	private final ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();

	private static final EPADLogger log = EPADLogger.getInstance();
	private static final EPADConfig config = EPADConfig.getInstance();
	private static final EpadOperations epadOperations = DefaultEpadOperations.getInstance();

	private final String xnatUploadProjectID;

	private String jsessionID = null;

	public XNATSeriesWatcher(BlockingQueue<DicomSeriesProcessingDescription> xnatSeriesWatcherQueue)
	{
		this.xnatSeriesWatcherQueue = xnatSeriesWatcherQueue;

		xnatUploadProjectID = config.getStringPropertyValue("XNATUploadProjectID");

		log.info("Starting the XNAT series watcher");
	}

	@Override
	public void run()
	{
		while (!shutdownSignal.hasShutdown()) {
			try {
				DicomSeriesProcessingDescription dicomSeriesDescription = xnatSeriesWatcherQueue.poll(5000,
						TimeUnit.MILLISECONDS);

				if (dicomSeriesDescription != null) {

					validateDICOMSeriesProcessingDescription(dicomSeriesDescription);

					String studyUID = dicomSeriesDescription.getStudyUID();
					String subjectID = dicomSeriesDescription.getSubjectID();
					String patientName = dicomSeriesDescription.getPatientName();

					log.info("XNAT series watcher processing study " + studyUID + " for subject " + patientName + " with ID "
							+ subjectID);

					// We create the subject and study here. The study will subsequently arrive from dcm4chee where it
					// will be processed by the DICOMSeriesWatcher.

					if (updateSessionIDIfNecessary())
						epadOperations.createSubjectAndStudy(xnatUploadProjectID, subjectID, patientName, studyUID, jsessionID);
					else
						log.warning("Unable to validate to upload study " + studyUID + " for subject " + patientName
								+ " in project " + xnatUploadProjectID);
				}
			} catch (Exception e) {
				log.warning("Exception in XNAT series watcher thread", e);
			}
		}
	}

	/**
	 * 
	 * @return True if successfully updated, false otherwise
	 */
	private boolean updateSessionIDIfNecessary()
	{
		if (!XNATSessionOperations.hasValidXNATSessionID(jsessionID)) { // Validating will extend validity
			String sessionID = XNATSessionOperations.getXNATAdminSessionID();
			if (sessionID != null) {
				jsessionID = sessionID;
				return true;
			} else
				return false;
		} else
			return true;
	}

	private void validateDICOMSeriesProcessingDescription(DicomSeriesProcessingDescription dicomSeriesDescription)
			throws IllegalArgumentException
	{
		if (dicomSeriesDescription == null)
			throw new IllegalArgumentException("Missing series description");
		if (dicomSeriesDescription.getSeriesUID().length() == 0)
			throw new IllegalArgumentException("Missing series UID in series description");
		if (dicomSeriesDescription.getSubjectID().length() == 0)
			throw new IllegalArgumentException("Missing patient ID in series description");
		if (dicomSeriesDescription.getPatientName().length() == 0)
			throw new IllegalArgumentException("Missing patient name in series description");
	}
}