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
import edu.stanford.isis.epad.common.util.EPADTools;
import edu.stanford.isis.epad.common.util.ResourceUtils;
import edu.stanford.isis.epadws.processing.model.DicomSeriesDescription;
import edu.stanford.isis.epadws.processing.model.PngProcessingStatus;
import edu.stanford.isis.epadws.processing.persistence.Dcm4CheeDatabaseUtils;
import edu.stanford.isis.epadws.processing.persistence.MySqlInstance;
import edu.stanford.isis.epadws.processing.persistence.MySqlQueries;
import edu.stanford.isis.epadws.processing.pipeline.process.PngGeneratorProcess;
import edu.stanford.isis.epadws.processing.pipeline.task.DicomSegmentObjectGeneratorTask;
import edu.stanford.isis.epadws.processing.pipeline.task.GeneratorTask;
import edu.stanford.isis.epadws.processing.pipeline.task.PngGeneratorTask;

/**
 * @author amsnyder
 */
public class QueueAndWatcherManager
{
	private static final EPADLogger logger = EPADLogger.getInstance();
	private static final BlockingQueue<DicomSeriesDescription> dcm4CheeSeriesWatcherQueue = new ArrayBlockingQueue<DicomSeriesDescription>(
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

	private static QueueAndWatcherManager ourInstance = new QueueAndWatcherManager();

	public static QueueAndWatcherManager getInstance()
	{
		return ourInstance;
	}

	private QueueAndWatcherManager()
	{
		dcm4CheeDatabaseWatcher = new Dcm4CheeDatabaseWatcher(dcm4CheeSeriesWatcherQueue, xnatSeriesWatcherQueue);
		dicomSeriesWatcher = new DICOMSeriesWatcher(dcm4CheeSeriesWatcherQueue, pngGeneratorTaskQueue);
		xnatSeriesWatcher = new XNATSeriesWatcher(xnatSeriesWatcherQueue);
		pngGeneratorProcess = new PngGeneratorProcess(pngGeneratorTaskQueue);
		epadUploadDirWatcher = new EPADUploadDirWatcher();
		dcm4cheeRootDir = EPADConfig.getInstance().getParam("dcm4cheeDirRoot");
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
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();

		for (Map<String, String> dicomImageDescription : dicomImageFileDescriptions) {
			String inputDICOMFilePath = getInputFilePath(dicomImageDescription); // Get the input file path.
			File inputDICOMFile = new File(inputDICOMFilePath);

			if (!inputDICOMFile.exists()) { // If the file does not exist (stored on another file system)
				try { // We create a temporary file
					File temp = File.createTempFile(dicomImageDescription.get("sop_iuid"), ".tmp");
					EPADTools.feedFileWithDICOMFromWADO(temp, dicomImageDescription);
					inputDICOMFile = temp;
				} catch (IOException e) {
					logger.warning("Exception when creating temp image : " + dicomImageDescription.get("sop_iuid"), e);
				}
			}
			String outputPNGFilePath = createOutputPNGFilePathForDicomImage(dicomImageDescription);
			logger.info("Checking epad_files table for: " + outputPNGFilePath);
			if (!queries.hasEpadFile(outputPNGFilePath)) {
				if (PixelMedUtils.isDicomSegmentationObject(inputDICOMFilePath)) { // Generate slices of PNG mask
					logger.info("SeriesWatcher has: " + dicomImageDescription.get("sop_iuid") + " DICOM segmentation object.");
					processDicomSegmentationObject(outputPNGFilePath, inputDICOMFilePath);
				} else { // Generate PNG file.
					logger.info("SeriesWatcher has: " + dicomImageDescription.get("sop_iuid") + " DICOM object.");
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

		DicomSegmentObjectGeneratorTask dsoTask = new DicomSegmentObjectGeneratorTask(inFile, outFile);
		pngGeneratorTaskQueue.offer(dsoTask);
		logger.info("Segmentation Object found: " + inputFilePath);
	}

	private void createPNGFileForDICOMImage(String outputPNGFilePath, File inputDICOMFile)
	{
		File outputPNGFile = new File(outputPNGFilePath);
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		insertEpadFile(queries, outputPNGFile);
		PngGeneratorTask pngTask = new PngGeneratorTask(inputDICOMFile, outputPNGFile);
		pngGeneratorTaskQueue.offer(pngTask);

		logger.info("Offering to PngTaskQueue: PNG file path=" + outputPNGFilePath + " DICOM file path="
				+ inputDICOMFile.getAbsolutePath());
	}

	private void insertEpadFile(MySqlQueries queries, File outputPNGFile)
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
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		String studyUID = queries.getStudyUIDForSeries(seriesIUID);
		String imageUID = dicomImageDescription.get("sop_iuid");
		StringBuilder outputPath = new StringBuilder();

		outputPath.append(ResourceUtils.getEPADWebServerPNGDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(seriesIUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(imageUID)).append(".png");

		return outputPath.toString();
	}
}
