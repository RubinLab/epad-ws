package edu.stanford.epad.epadws.processing.pipeline.watcher;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.stanford.epad.common.dicom.DicomFormatUtil;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.EPADResources;
import edu.stanford.epad.common.util.EPADTools;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.processing.model.DicomSeriesProcessingDescription;
import edu.stanford.epad.epadws.processing.pipeline.process.PngGeneratorProcess;
import edu.stanford.epad.epadws.processing.pipeline.task.DicomSegmentationObjectPNGMaskGeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.task.GeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.task.PngGeneratorTask;

public class QueueAndWatcherManager
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final BlockingQueue<DicomSeriesProcessingDescription> dicomSeriesWatcherQueue = new ArrayBlockingQueue<DicomSeriesProcessingDescription>(
			2000);
	private static final BlockingQueue<DicomSeriesProcessingDescription> xnatSeriesWatcherQueue = new ArrayBlockingQueue<DicomSeriesProcessingDescription>(
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
		dcm4cheeRootDir = EPADConfig.getInstance().getStringPropertyValue("dcm4cheeDirRoot");
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

	// Each entry in list is map with keys sop_iuid, inst_no, series_iuid, filepath, and file_size.
	public void addToPNGGeneratorTaskPipeline(List<Map<String, String>> dicomImageFileDescriptions)
	{
		for (Map<String, String> dicomImageDescription : dicomImageFileDescriptions) {
			String seriesUID = dicomImageDescription.get("series_iuid");
			String instanceNumber = dicomImageDescription.get("inst_no");
			String inputDICOMFilePath = getInputFilePath(dicomImageDescription); // Get the input file path
			File inputDICOMFile = new File(inputDICOMFilePath);

			// If the file does not exist locally (because it is stored on another file system), download it.
			if (!inputDICOMFile.exists()) {
				try {
					String imageUID = dicomImageDescription.get("sop_iuid");
					log.info("Downloading remote DICOM file for image " + imageUID);
					File downloadedDICOMFile = File.createTempFile(imageUID, ".tmp");
					EPADTools.feedFileWithDICOMFromWADO(downloadedDICOMFile, dicomImageDescription);
					inputDICOMFile = downloadedDICOMFile;
				} catch (IOException e) {
					log.warning("Exception when downloading DICOM file with series ID " + seriesUID + " and image ID "
							+ dicomImageDescription.get("sop_iuid"), e);
				}
			}

			String outputPNGFilePath = createOutputPNGFilePathForDicomImage(dicomImageDescription);
			if (PixelMedUtils.isDicomSegmentationObject(inputDICOMFilePath)) { // Generate slices of PNG mask
				processDicomSegmentationObject(seriesUID, outputPNGFilePath, inputDICOMFilePath);
			} else { // Generate PNG file.
				createPNGFileForDICOMImage(seriesUID, instanceNumber, outputPNGFilePath, inputDICOMFile);
			}
		}
	}

	private String getInputFilePath(Map<String, String> dicomImageFileDescription)
	{
		return getDcm4cheeRootDir() + dicomImageFileDescription.get("filepath");
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

	private void processDicomSegmentationObject(String seriesUID, String outputFilePath, String inputFilePath)
	{
		File inFile = new File(inputFilePath);
		File outFile = new File(outputFilePath);

		log.info("DICOM segmentation object found for series " + seriesUID);

		DicomSegmentationObjectPNGMaskGeneratorTask dsoTask = new DicomSegmentationObjectPNGMaskGeneratorTask(seriesUID,
				inFile, outFile);
		pngGeneratorTaskQueue.offer(dsoTask);
	}

	private void createPNGFileForDICOMImage(String seriesUID, String instanceNumber, String outputPNGFilePath,
			File inputDICOMFile)
	{
		File outputPNGFile = new File(outputPNGFilePath);
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		insertEpadFile(epadDatabaseOperations, outputPNGFile);
		PngGeneratorTask pngGeneratorTask = new PngGeneratorTask(seriesUID, instanceNumber, inputDICOMFile, outputPNGFile);
		pngGeneratorTaskQueue.offer(pngGeneratorTask);
	}

	private void insertEpadFile(EpadDatabaseOperations epadDatabaseOperations, File outputPNGFile)
	{
		Map<String, String> epadFilesTable = Dcm4CheeDatabaseUtils.createEPadFilesTableData(outputPNGFile);
		epadFilesTable.put("file_status", "" + SeriesProcessingStatus.IN_PIPELINE.getCode());
		epadDatabaseOperations.insertEpadFileRecord(epadFilesTable);
	}

	/**
	 * 
	 * @param dicomImageDescription Map of String to String
	 * @return String
	 */
	private String createOutputPNGFilePathForDicomImage(Map<String, String> dicomImageDescription)
	{
		String seriesIUID = dicomImageDescription.get("series_iuid");
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		String studyUID = dcm4CheeDatabaseOperations.getStudyUIDForSeries(seriesIUID);
		String imageUID = dicomImageDescription.get("sop_iuid");
		StringBuilder outputPath = new StringBuilder();

		outputPath.append(EPADResources.getEPADWebServerPNGDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(seriesIUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(imageUID)).append(".png");

		return outputPath.toString();
	}
}
