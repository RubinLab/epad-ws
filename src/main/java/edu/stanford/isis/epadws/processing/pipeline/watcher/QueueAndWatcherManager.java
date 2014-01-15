package edu.stanford.isis.epadws.processing.pipeline.watcher;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.EPADResources;
import edu.stanford.isis.epad.common.util.EPADTools;
import edu.stanford.isis.epadws.persistence.Dcm4CheeDatabaseUtils;
import edu.stanford.isis.epadws.persistence.DatabaseOperations;
import edu.stanford.isis.epadws.persistence.Database;
import edu.stanford.isis.epadws.processing.model.DicomSeriesDescription;
import edu.stanford.isis.epadws.processing.model.PngProcessingStatus;
import edu.stanford.isis.epadws.processing.pipeline.process.PngGeneratorProcess;
import edu.stanford.isis.epadws.processing.pipeline.task.DicomSegmentationObjectPNGMaskGeneratorTask;
import edu.stanford.isis.epadws.processing.pipeline.task.GeneratorTask;
import edu.stanford.isis.epadws.processing.pipeline.task.PngGeneratorTask;

public class QueueAndWatcherManager
{
	private static final EPADLogger logger = EPADLogger.getInstance();
	private static final BlockingQueue<DicomSeriesDescription> dicomSeriesWatcherQueue = new ArrayBlockingQueue<DicomSeriesDescription>(
			2000);
	private static final BlockingQueue<DicomSeriesDescription> xnatSeriesWatcherQueue = new ArrayBlockingQueue<DicomSeriesDescription>(
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
		logger.info("Starting QueueAndWatcherManager...");
		dcm4CheeDatabaseWatcher = new Dcm4CheeDatabaseWatcher(dicomSeriesWatcherQueue, xnatSeriesWatcherQueue);
		dicomSeriesWatcher = new DICOMSeriesWatcher(dicomSeriesWatcherQueue, pngGeneratorTaskQueue);
		xnatSeriesWatcher = new XNATSeriesWatcher(xnatSeriesWatcherQueue);
		pngGeneratorProcess = new PngGeneratorProcess(pngGeneratorTaskQueue);
		epadUploadDirWatcher = new EPADUploadDirWatcher();
		dcm4cheeRootDir = EPADConfig.getInstance().getParam("dcm4cheeDirRoot");
		logger.info("Started QueueAndWatcherManager...");
	}

	public void buildAndStart()
	{
		logger.info("Starting pipelines...");
		dcm4CheeDatabaseWatcherExec.execute(dcm4CheeDatabaseWatcher);
		dicomSeriesWatcherExec.execute(dicomSeriesWatcher);
		xnatSeriesWatcherExec.execute(xnatSeriesWatcher);
		pngGeneratorProcessExec.execute(pngGeneratorProcess);
		epadUploadDirWatcherExec.execute(epadUploadDirWatcher);
	}

	public void shutdown()
	{
		logger.info("Stopping pipelines...");
		dcm4CheeDatabaseWatcherExec.shutdown();
		dicomSeriesWatcherExec.shutdown();
		xnatSeriesWatcherExec.shutdown();
		pngGeneratorProcessExec.shutdown();
		epadUploadDirWatcherExec.shutdown();
	}

	// Each entry in list is map with keys: sop_iuid, inst_no, series_iuid, filepath, file_size.
	public void addToPNGGeneratorTaskPipeline(List<Map<String, String>> dicomImageFileDescriptions)
	{
		DatabaseOperations queries = Database.getInstance().getDatabaseOperations();

		for (Map<String, String> dicomImageDescription : dicomImageFileDescriptions) {
			String inputDICOMFilePath = getInputFilePath(dicomImageDescription); // Get the input file path.
			File inputDICOMFile = new File(inputDICOMFilePath);
			if (!inputDICOMFile.exists()) { // If the file does not exist (stored on another file system)
				try { // We create a temporary file
					logger.info("Creating temporary image ");
					File temp = File.createTempFile(dicomImageDescription.get("sop_iuid"), ".tmp");
					EPADTools.feedFileWithDICOMFromWADO(temp, dicomImageDescription);
					inputDICOMFile = temp;
				} catch (IOException e) {
					logger.warning("Exception when creating temporary image : " + dicomImageDescription.get("sop_iuid"), e);
				}
			}
			String outputPNGFilePath = createOutputPNGFilePathForDicomImage(dicomImageDescription);
			if (!queries.hasEpadFile(outputPNGFilePath)) {
				if (PixelMedUtils.isDicomSegmentationObject(inputDICOMFilePath)) { // Generate slices of PNG mask
					processDicomSegmentationObject(outputPNGFilePath, inputDICOMFilePath);
				} else { // Generate PNG file.
					createPNGFileForDICOMImage(outputPNGFilePath, inputDICOMFile);
				}
			}
		}
	}

	String getInputFilePath(Map<String, String> currImage)
	{
		// NOTE: Maybe we want to get this from the 'files' directory.

		// StringBuilder sb = new StringBuilder();
		// sb.append("[TEMP] PNG Creation. ");
		// sb.append("file_size=").append(currImage.get("file_size"));
		// sb.append(", inst_no=").append(currImage.get("inst_no"));
		// sb.append(", sop_iuid=").append(currImage.get("sop_iuid"));
		// sb.append(", series_iuid=").append(currImage.get("series_iuid"));
		// sb.append(", filepath=").append(currImage.get("filepath"));
		// logger.info(sb.toString());

		String retVal = getDcm4cheeRootDir() + currImage.get("filepath");

		return retVal;
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

	private void processDicomSegmentationObject(String outputFilePath, String inputFilePath)
	{
		File inFile = new File(inputFilePath);
		File outFile = new File(outputFilePath);

		DicomSegmentationObjectPNGMaskGeneratorTask dsoTask = new DicomSegmentationObjectPNGMaskGeneratorTask(inFile,
				outFile);
		pngGeneratorTaskQueue.offer(dsoTask);
		logger.info("Segmentation object found at " + inputFilePath);
	}

	private void createPNGFileForDICOMImage(String outputPNGFilePath, File inputDICOMFile)
	{
		File outputPNGFile = new File(outputPNGFilePath);
		DatabaseOperations queries = Database.getInstance().getDatabaseOperations();
		insertEpadFile(queries, outputPNGFile);
		PngGeneratorTask pngTask = new PngGeneratorTask(inputDICOMFile, outputPNGFile);
		pngGeneratorTaskQueue.offer(pngTask);
	}

	private void insertEpadFile(DatabaseOperations queries, File outputPNGFile)
	{
		Map<String, String> epadFilesTable = Dcm4CheeDatabaseUtils.createEPadFilesTableData(outputPNGFile);
		epadFilesTable.put("file_status", "" + PngProcessingStatus.IN_PIPELINE.getCode());
		queries.insertEpadFile(epadFilesTable);
	}

	/**
	 * 
	 * @param dicomImageDescription Map of String to String
	 * @return String
	 */
	private String createOutputPNGFilePathForDicomImage(Map<String, String> dicomImageDescription)
	{
		String seriesIUID = dicomImageDescription.get("series_iuid");
		DatabaseOperations queries = Database.getInstance().getDatabaseOperations();
		String studyUID = queries.getStudyUIDForSeries(seriesIUID);
		String imageUID = dicomImageDescription.get("sop_iuid");
		StringBuilder outputPath = new StringBuilder();

		outputPath.append(EPADResources.getEPADWebServerPNGDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(seriesIUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(imageUID)).append(".png");

		return outputPath.toString();
	}
}
