package edu.stanford.epad.epadws.processing.pipeline.watcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.common.dicom.DicomFormatUtil;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.EPADResources;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.processing.model.DicomSeriesProcessingState;
import edu.stanford.epad.epadws.processing.model.DicomSeriesProcessingStatus;
import edu.stanford.epad.epadws.processing.model.DicomSeriesProcessingStatusTracker;
import edu.stanford.epad.epadws.processing.model.SeriesProcessingDescription;
import edu.stanford.epad.epadws.processing.pipeline.task.GeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.task.PNGGridGeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.task.PngGeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;

/**
 * Process new DICOM series appearing in the series queue. Each series is described by a
 * {@link SeriesProcessingDescription}.
 * <p>
 * The {@link XNATSeriesWatcher} will have created XNAT entities for the subject and study in the appropriate project.
 * We now create ePAD representations for the studies that have arrived from dcm4chee.
 * <p>
 * These descriptions are placed in the queue by a {@link Dcm4CheeDatabaseWatcher}, which picks up new series by
 * monitoring a dcm4chee MySQL database.
 * <p>
 * This watcher submits these to the PNG generation task queue to be processed by the {@link PngGeneratorTask}.
 * 
 * @see XNATSeriesWatcher
 */
public class DICOMSeriesWatcher implements Runnable
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final BlockingQueue<SeriesProcessingDescription> dicomSeriesWatcherQueue;
	private final BlockingQueue<GeneratorTask> pngGeneratorTaskQueue;
	private final DicomSeriesProcessingStatusTracker dicomSeriesDescriptionTracker;
	private final String dcm4cheeRootDir; // Used by the PNG grid process only

	private final ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();

	private QueueAndWatcherManager queueAndWatcherManager;

	public DICOMSeriesWatcher(BlockingQueue<SeriesProcessingDescription> dicomSeriesWatcherQueue,
			BlockingQueue<GeneratorTask> pngGeneratorTaskQueue)
	{
		log.info("Starting the DICOM series watcher");

		this.dicomSeriesWatcherQueue = dicomSeriesWatcherQueue;
		this.pngGeneratorTaskQueue = pngGeneratorTaskQueue;
		this.dicomSeriesDescriptionTracker = DicomSeriesProcessingStatusTracker.getInstance();
		this.dcm4cheeRootDir = EPADConfig.getInstance().getStringPropertyValue("dcm4cheeDirRoot");
	}

	@Override
	public void run()
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();

		queueAndWatcherManager = QueueAndWatcherManager.getInstance();

		while (!shutdownSignal.hasShutdown()) {
			try {
				SeriesProcessingDescription seriesProcessingDescription = dicomSeriesWatcherQueue.poll(1000,
						TimeUnit.MILLISECONDS);

				if (seriesProcessingDescription != null) {
					String seriesUID = seriesProcessingDescription.getSeriesUID();
					String patientName = seriesProcessingDescription.getPatientName();
					int numberOfInstances = seriesProcessingDescription.getNumberOfInstances();
					log.info("Series watcher found new series " + seriesUID + " for patient " + patientName + " with "
							+ numberOfInstances + " instance(s).");
					dicomSeriesDescriptionTracker.addDicomSeriesProcessingStatus(new DicomSeriesProcessingStatus(
							seriesProcessingDescription));
				}
				// Loop through all new series and find images that have no corresponding PNG file recorded in ePAD database.
				// Update their status to reflect this so that we can monitor percent completion for each series.
				for (DicomSeriesProcessingStatus dicomSeriesProcessingStatus : dicomSeriesDescriptionTracker
						.getDicomSeriesProcessingStatusSet()) {
					SeriesProcessingDescription dicomSeriesProcessingDescription = dicomSeriesProcessingStatus
							.getDicomSeriesProcessingDescription();
					// Each entry in list is map with keys: study_iuid, sop_iuid, inst_no, series_iuid, filepath, file_size.
					String seriesUID = dicomSeriesProcessingDescription.getSeriesUID();
					String patientName = dicomSeriesProcessingDescription.getPatientName();
					int numberOfInstances = dicomSeriesProcessingDescription.getNumberOfInstances();
					List<DICOMFileDescription> unprocessedDICOMFileDescriptions = epadOperations
							.getUnprocessedDICOMFileDescriptionsForSeries(seriesUID);

					if (unprocessedDICOMFileDescriptions.size() > 0) {
						log.info("Found series " + dicomSeriesProcessingDescription.getSeriesUID() + " with "
								+ unprocessedDICOMFileDescriptions.size() + " unprocessed DICOM image(s).");
						dicomSeriesProcessingDescription.updateWithDICOMFileDescriptions(unprocessedDICOMFileDescriptions);
						dicomSeriesProcessingStatus.registerActivity();
						dicomSeriesProcessingStatus.setSeriesProcessingState(DicomSeriesProcessingState.IN_PIPELINE);
						queueAndWatcherManager.addToPNGGeneratorTaskPipeline(patientName, unprocessedDICOMFileDescriptions);
						log.info("Submitted series " + seriesUID + " for patient " + patientName + " with " + numberOfInstances
								+ " image(s) to PNG generator");
					} else { // All images have been submitted for PNG processing.
						/*
						 * Here is (not fully tested) code to generated 4x4 grids from the generated PNGs. This could be very
						 * expensive so we do not activate. List<Map<String, String>> processedPNGImages = mySqlQueries
						 * .getProcessedDICOMFileDescriptionsOrdered(currentSeriesDescription.getSeriesUID());
						 * 
						 * if (processedPNGImages.size() > 0) { // Convert processed PNG files to PNG grid files
						 * logger.info("Found " + processedPNGImages.size() + " PNG images. Converting to grid images.");
						 * currentSeriesStatus.setState(DicomImageProcessingState.IN_PNG_GRID_PIPELINE);
						 * addToPNGGridGeneratorTaskPipeline(unprocessedDICOMFileDescriptions); }
						 */
						// }
					}
				}
				// Loop through all current active series and remove them if they are done.
				for (DicomSeriesProcessingStatus dicomSeriesProcessingStatus : dicomSeriesDescriptionTracker
						.getDicomSeriesProcessingStatusSet()) {
					if (dicomSeriesProcessingStatus.isDone()) { // Remove finished series
						String seriesUID = dicomSeriesProcessingStatus.getDicomSeriesProcessingDescription().getSeriesUID();
						dicomSeriesDescriptionTracker.removeDicomSeriesProcessingStatus(dicomSeriesProcessingStatus);
						epadDatabaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.DONE);
					}
				}
			} catch (Exception e) {
				log.severe("Exception in DICOM series watcher thread", e);
			}
		}
	}

	@SuppressWarnings("unused")
	private void addToPNGGridGeneratorTaskPipeline(String seriesUID,
			List<Map<String, String>> unprocessedPNGImageDescriptions)
	{
		EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		int currentImageIndex = 0;
		for (Map<String, String> currentPNGImageDescription : unprocessedPNGImageDescriptions) {
			String inputPNGFilePath = getInputFilePath(currentPNGImageDescription); // Get the input file path.
			File inputPNGFile = new File(inputPNGFilePath);
			String outputPNGGridFilePath = createOutputFilePathForDicomPNGGridImage(currentPNGImageDescription);
			if (!databaseOperations.hasEpadFileRecord(outputPNGGridFilePath)) {
				log.info("SeriesWatcher has: " + currentPNGImageDescription.get("sop_iuid") + " PNG for grid processing.");
				// Need to get slice for PNG files.
				List<File> inputPNGGridFiles = getSliceOfPNGFiles(unprocessedPNGImageDescriptions, currentImageIndex, 16);
				createPngGridFileForPNGImages(seriesUID, inputPNGFile, inputPNGGridFiles, outputPNGGridFilePath);
			}
			currentImageIndex++;
		}
	}

	private List<File> getSliceOfPNGFiles(List<Map<String, String>> imageList, int currentImageIndex, int sliceSize)
	{
		List<File> sliceOfPNGFiles = new ArrayList<File>(sliceSize);

		for (int i = currentImageIndex; i < sliceSize; i++) {
			Map<String, String> currentPNGImage = imageList.get(i);
			String pngFilePath = getInputFilePath(currentPNGImage);
			File pngFile = new File(pngFilePath);
			sliceOfPNGFiles.add(pngFile);
		}
		return sliceOfPNGFiles;
	}

	private void createPngGridFileForPNGImages(String seriesUID, File inputPNGFile, List<File> inputPNGGridFiles,
			String outputPNGGridFilePath)
	{
		// logger.info("Offering to PNGGridTaskQueue: out=" + outputPNGGridFilePath + " in=" +
		// inputPNGFile.getAbsolutePath());

		File outputPNGFile = new File(outputPNGGridFilePath);
		EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		insertEpadFile(databaseOperations, outputPNGFile);

		PNGGridGeneratorTask pngGridGeneratorTask = new PNGGridGeneratorTask(seriesUID, inputPNGFile, inputPNGGridFiles,
				outputPNGFile);
		pngGeneratorTaskQueue.offer(pngGridGeneratorTask);
	}

	private void insertEpadFile(EpadDatabaseOperations epadDatabaseOperations, File outputPNGFile)
	{
		Map<String, String> epadFilesTable = Dcm4CheeDatabaseUtils.createEPadFilesTableData(outputPNGFile);
		epadFilesTable.put("file_status", "" + PNGFileProcessingStatus.IN_PIPELINE.getCode());
		epadDatabaseOperations.insertEpadFileRecord(epadFilesTable);
	}

	String getInputFilePath(Map<String, String> currImage)
	{
		return getDcm4cheeRootDir() + currImage.get("filepath");
	}

	/**
	 * 
	 * @param currImage Map of String to String
	 * @return String
	 */
	private String createOutputFilePathForDicomPNGGridImage(Map<String, String> currImage)
	{
		String seriesIUID = currImage.get("series_iuid");
		Dcm4CheeDatabaseOperations databaseOperations = Dcm4CheeDatabase.getInstance().getDcm4CheeDatabaseOperations();
		String studyUID = databaseOperations.getStudyUIDForSeries(seriesIUID);
		String imageUID = currImage.get("sop_iuid");
		StringBuilder outputPath = new StringBuilder();

		outputPath.append(EPADResources.getEPADWebServerPNGGridDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(seriesIUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(imageUID)).append(".png");

		return outputPath.toString();
	}

	public String getDcm4cheeRootDir()
	{
		if (dcm4cheeRootDir.endsWith("/")) {
			return dcm4cheeRootDir;
		}
		return dcm4cheeRootDir + "/";
	}
}
