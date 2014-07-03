package edu.stanford.epad.epadws.processing.pipeline.watcher;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.stanford.epad.common.dicom.DCM4CHEEUtil;
import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.processing.model.SeriesProcessingDescription;
import edu.stanford.epad.epadws.processing.pipeline.process.PngGeneratorProcess;
import edu.stanford.epad.epadws.processing.pipeline.task.DSOMaskPNGGeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.task.GeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.task.MultiFramePNGGeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.task.SingleFrameDICOMPngGeneratorTask;

public class QueueAndWatcherManager
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final BlockingQueue<SeriesProcessingDescription> dicomSeriesWatcherQueue = new ArrayBlockingQueue<SeriesProcessingDescription>(
			2000);
	private static final BlockingQueue<SeriesProcessingDescription> xnatSeriesWatcherQueue = new ArrayBlockingQueue<SeriesProcessingDescription>(
			2000);
	private static final BlockingQueue<GeneratorTask> pngGeneratorTaskQueue = new ArrayBlockingQueue<GeneratorTask>(2000);
	// private static final BlockingQueue<DicomHeadersTask> dicomHeadersTaskQueue = new
	// ArrayBlockingQueue<DicomHeadersTask>(2000);

	private final ExecutorService dcm4CheeDatabaseWatcherExec = Executors.newSingleThreadExecutor();
	private final ExecutorService dicomSeriesWatcherExec = Executors.newSingleThreadExecutor();
	private final ExecutorService xnatSeriesWatcherExec = Executors.newSingleThreadExecutor();
	private final ExecutorService pngGeneratorProcessExec = Executors.newSingleThreadExecutor();
	private final ExecutorService epadUploadDirWatcherExec = Executors.newSingleThreadExecutor();

	private final Dcm4CheeDatabaseWatcher dcm4CheeDatabaseWatcher;
	private final DICOMSeriesWatcher dicomSeriesWatcher;
	private final XNATSeriesWatcher xnatSeriesWatcher;
	private final PngGeneratorProcess pngGeneratorProcess;
	private final EPADUploadDirWatcher epadUploadDirWatcher;

	private final String dcm4cheeRootDir;

	private final static QueueAndWatcherManager ourInstance = new QueueAndWatcherManager();

	public static QueueAndWatcherManager getInstance()
	{
		return ourInstance;
	}

	private QueueAndWatcherManager()
	{
		log.info("Starting QueueAndWatcherManager...");
		dcm4CheeDatabaseWatcher = new Dcm4CheeDatabaseWatcher(dicomSeriesWatcherQueue, xnatSeriesWatcherQueue);
		dicomSeriesWatcher = new DICOMSeriesWatcher(dicomSeriesWatcherQueue, pngGeneratorTaskQueue);
		xnatSeriesWatcher = new XNATSeriesWatcher(xnatSeriesWatcherQueue);
		pngGeneratorProcess = new PngGeneratorProcess(pngGeneratorTaskQueue);
		epadUploadDirWatcher = new EPADUploadDirWatcher();
		dcm4cheeRootDir = EPADConfig.dcm4cheeDirRoot;
		log.info("Started QueueAndWatcherManager...");
	}

	public void buildAndStart()
	{
		log.info("Starting pipelines...");
		dcm4CheeDatabaseWatcherExec.execute(dcm4CheeDatabaseWatcher);
		dicomSeriesWatcherExec.execute(dicomSeriesWatcher);
		xnatSeriesWatcherExec.execute(xnatSeriesWatcher);
		pngGeneratorProcessExec.execute(pngGeneratorProcess);
		epadUploadDirWatcherExec.execute(epadUploadDirWatcher);
	}

	public void shutdown()
	{
		log.info("Stopping pipelines...");
		dcm4CheeDatabaseWatcherExec.shutdown();
		dicomSeriesWatcherExec.shutdown();
		xnatSeriesWatcherExec.shutdown();
		pngGeneratorProcessExec.shutdown();
		epadUploadDirWatcherExec.shutdown();
	}

	public void addDICOMFileToPNGGeneratorPipeline(String patientName, List<DICOMFileDescription> dicomFileDescriptions)
	{
		for (DICOMFileDescription dicomFileDescription : dicomFileDescriptions) {
			String seriesUID = dicomFileDescription.seriesUID;
			String imageUID = dicomFileDescription.imageUID;
			String dicomFilePath = getDICOMFilePath(dicomFileDescription);
			File inputDICOMFile = new File(dicomFilePath);

			// If the file does not exist locally (because it is stored on another file system), download it.
			if (!inputDICOMFile.exists()) {
				try {
					log.info("Downloading remote DICOM file with image " + imageUID + " for patient " + patientName);
					File downloadedDICOMFile = File.createTempFile(imageUID, ".tmp");
					DCM4CHEEUtil.downloadDICOMFileFromWADO(dicomFileDescription, downloadedDICOMFile);
					inputDICOMFile = downloadedDICOMFile;
				} catch (IOException e) {
					log.warning("Exception when downloading DICOM file with series UID " + seriesUID + " and image UID "
							+ dicomFileDescription.imageUID, e);
				}
			}

			if (PixelMedUtils.isDicomSegmentationObject(dicomFilePath)) {
				generateMaskPNGsForDicomSegmentationObject(dicomFileDescription, inputDICOMFile);
			} else if (PixelMedUtils.isMultiframedDicom(dicomFilePath)) {
				generatePNGsForMultiFrameDicom(dicomFileDescription, inputDICOMFile);
			} else { // Assume it is non multi-frame DICOM
				generatePNGFileForSingleFrameDICOMImage(patientName, dicomFileDescription, inputDICOMFile);
			}
		}
	}

	private String getDICOMFilePath(DICOMFileDescription dicomFileDescription)
	{
		return getDcm4cheeRootDir() + dicomFileDescription.filePath;
	}

	/**
	 * Add a forward slash if it is missing.
	 * 
	 * @return String
	 */
	public String getDcm4cheeRootDir()
	{
		if (dcm4cheeRootDir.endsWith("/"))
			return dcm4cheeRootDir;
		else
			return dcm4cheeRootDir + "/";
	}

	private void generateMaskPNGsForDicomSegmentationObject(DICOMFileDescription dicomFileDescription, File dsoFile)
	{
		log.info("DICOM segmentation object found for series " + dicomFileDescription.seriesUID);

		DSOMaskPNGGeneratorTask dsoMaskPNGGeneratorTask = new DSOMaskPNGGeneratorTask(dicomFileDescription.seriesUID,
				dsoFile);

		pngGeneratorTaskQueue.offer(dsoMaskPNGGeneratorTask);
	}

	private void generatePNGsForMultiFrameDicom(DICOMFileDescription dicomFileDescription, File multiFrameDicomFile)
	{
		log.info("Multi-frame DICOM object found for series " + dicomFileDescription.seriesUID);

		MultiFramePNGGeneratorTask dsoPNGGeneratorTask = new MultiFramePNGGeneratorTask(dicomFileDescription.seriesUID,
				multiFrameDicomFile);

		pngGeneratorTaskQueue.offer(dsoPNGGeneratorTask);
	}

	private void generatePNGFileForSingleFrameDICOMImage(String patientName, DICOMFileDescription dicomFileDescription,
			File dicomFile)
	{
		String outputPNGFilePath = createOutputPNGFilePathForSingleFrameDICOMImage(dicomFileDescription);
		File outputPNGFile = new File(outputPNGFilePath);
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		insertEpadFile(epadDatabaseOperations, outputPNGFile);
		SingleFrameDICOMPngGeneratorTask pngGeneratorTask = new SingleFrameDICOMPngGeneratorTask(patientName,
				dicomFileDescription, dicomFile, outputPNGFile);
		pngGeneratorTaskQueue.offer(pngGeneratorTask);
	}

	private void insertEpadFile(EpadDatabaseOperations epadDatabaseOperations, File outputPNGFile)
	{
		Map<String, String> epadFilesTable = Dcm4CheeDatabaseUtils.createEPadFilesTableData(outputPNGFile);
		epadFilesTable.put("file_status", "" + SeriesProcessingStatus.IN_PIPELINE.getCode());
		epadDatabaseOperations.insertEpadFileRecord(epadFilesTable);
	}

	private String createOutputPNGFilePathForSingleFrameDICOMImage(DICOMFileDescription dicomFileDescription)
	{
		String studyUID = dicomFileDescription.studyUID;
		String seriesUID = dicomFileDescription.seriesUID;
		String imageUID = dicomFileDescription.imageUID;
		StringBuilder outputPNGFilePath = new StringBuilder();

		outputPNGFilePath.append(EPADConfig.getEPADWebServerPNGDir());
		outputPNGFilePath.append("/studies/" + studyUID);
		outputPNGFilePath.append("/series/" + seriesUID);
		outputPNGFilePath.append("/images/" + imageUID + ".png");

		return outputPNGFilePath.toString();
	}
}
