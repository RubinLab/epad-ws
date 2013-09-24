package edu.stanford.isis.epadws.processing.pipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.util.ResourceUtils;
import edu.stanford.isis.epad.common.util.WadoUrlBuilder;
import edu.stanford.isis.epadws.processing.login.MySqlLoginDirWatcher;
import edu.stanford.isis.epadws.processing.model.PngStatus;
import edu.stanford.isis.epadws.processing.model.SeriesOrder;
import edu.stanford.isis.epadws.processing.mysql.DcmDbUtils;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;

/**
 * @author amsnyder
 */
public class QueueAndWatcherManager
{
	private static final ProxyLogger logger = ProxyLogger.getInstance();
	private static final BlockingQueue<SeriesOrder> seriesWatchQueue = new ArrayBlockingQueue<SeriesOrder>(2000);
	// private static final BlockingQueue<DicomHeadersTask> dicomHeadersTaskQueue = new
	// ArrayBlockingQueue<DicomHeadersTask>(2000);
	private static final BlockingQueue<GeneratorTask> pngGeneratorTaskQueue = new ArrayBlockingQueue<GeneratorTask>(2000);

	final ExecutorService dbTableWatcherExec = Executors.newSingleThreadExecutor();
	final ExecutorService seriesWatcherExec = Executors.newSingleThreadExecutor();
	final ExecutorService pngProcessExec = Executors.newSingleThreadExecutor();

	// watches upload directory.
	final ExecutorService uploadDirWatcherExec = Executors.newSingleThreadExecutor();

	// login watcher.
	final ExecutorService loginDirWatcherExec = Executors.newSingleThreadExecutor();

	private static QueueAndWatcherManager ourInstance = new QueueAndWatcherManager();

	final DcmDbTableWatcher dbTableWatcher;
	final SeriesWatcher seriesWatcher;
	final PngGeneratorProcess pngGeneratorProcess;

	final MySqlUploadDirWatcher uploadDirWatcher;
	final MySqlLoginDirWatcher loginDirWatcher;

	private final String dcm4cheeRootDir;
	private final ProxyConfig config = ProxyConfig.getInstance();

	public static QueueAndWatcherManager getInstance()
	{
		return ourInstance;
	}

	private QueueAndWatcherManager()
	{
		dbTableWatcher = new DcmDbTableWatcher(seriesWatchQueue);
		seriesWatcher = new SeriesWatcher(seriesWatchQueue, pngGeneratorTaskQueue);
		pngGeneratorProcess = new PngGeneratorProcess(pngGeneratorTaskQueue);
		uploadDirWatcher = new MySqlUploadDirWatcher();
		loginDirWatcher = new MySqlLoginDirWatcher();
		dcm4cheeRootDir = ProxyConfig.getInstance().getParam("dcm4cheeDirRoot");
	}

	public void buildAndStart()
	{
		logger.info("Starting PNG generator pipeline.");
		dbTableWatcherExec.execute(dbTableWatcher);
		seriesWatcherExec.execute(seriesWatcher);
		pngProcessExec.execute(pngGeneratorProcess);
		uploadDirWatcherExec.execute(uploadDirWatcher);

		logger.info("Starting login checker.");
		loginDirWatcherExec.execute(loginDirWatcher);
	}

	public void shutdown()
	{
		logger.info("Stopping PNG generator pipeline.");
		dbTableWatcherExec.shutdown();
		seriesWatcherExec.shutdown();
		pngProcessExec.shutdown();
		uploadDirWatcherExec.shutdown();
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
					feedFileWithDicomFromWado(temp, dicomImageDescription);
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
		if (dcm4cheeRootDir.endsWith("/")) {
			return dcm4cheeRootDir;
		}
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
		Map<String, String> epadFilesTable = DcmDbUtils.createEPadFilesTableData(outputPNGFile);
		epadFilesTable.put("file_status", "" + PngStatus.IN_PIPELINE.getCode());
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

	private void feedFileWithDicomFromWado(File temp, Map<String, String> dicomImageFileDescription)
	{
		// we use wado to get the dicom image
		String host = config.getParam("NameServer");
		int port = config.getIntParam("DicomServerWadoPort");
		String base = config.getParam("WadoUrlExtension");

		WadoUrlBuilder wadoUrlBuilder = new WadoUrlBuilder(host, port, base, WadoUrlBuilder.ContentType.FILE);

		// GET WADO call result.
		wadoUrlBuilder.setStudyUID(dicomImageFileDescription.get("study_iuid"));
		wadoUrlBuilder.setSeriesUID(dicomImageFileDescription.get("series_iuid"));
		wadoUrlBuilder.setObjectUID(dicomImageFileDescription.get("sop_iuid"));

		try {
			String wadoUrl = wadoUrlBuilder.build();
			logger.info("Build wadoUrl = " + wadoUrl);

			// --Get the Dicom file from the server
			HttpClient client = new HttpClient();

			GetMethod method = new GetMethod(wadoUrl);
			// Execute the GET method
			int statusCode = client.executeMethod(method);

			if (statusCode != -1) {
				// Get the result as stream
				InputStream res = method.getResponseBodyAsStream();
				// write the inputStream to a FileOutputStream
				OutputStream out = new FileOutputStream(temp);

				int read = 0;
				byte[] bytes = new byte[4096];

				while ((read = res.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				res.close();
				out.flush();
				out.close();
			}
		} catch (UnsupportedEncodingException e) {
			logger.warning("Not able to build WADO URL for : " + temp.getName(), e);
		} catch (HttpException e) {
			logger.warning("Not able to get the WADO image : " + temp.getName(), e);
		} catch (IOException e) {
			logger.warning("Not able to write the temp DICOM image : " + temp.getName(), e);
		}
	}

}
