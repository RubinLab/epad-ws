package edu.stanford.isis.epadws.processing.pipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import edu.stanford.isis.epad.common.ProxyConfig;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.ResourceUtils;
import edu.stanford.isis.epad.common.WadoUrlBuilder;
import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epadws.processing.model.PngStatus;
import edu.stanford.isis.epadws.processing.model.ProcessingState;
import edu.stanford.isis.epadws.processing.model.SeriesOrder;
import edu.stanford.isis.epadws.processing.model.SeriesOrderStatus;
import edu.stanford.isis.epadws.processing.mysql.DcmDbUtils;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;
import edu.stanford.isis.epadws.server.ShutdownSignal;

/**
 * Task to watch series.
 * 
 * @author amsnyder
 */
public class SeriesWatcher implements Runnable
{
	private final BlockingQueue<SeriesOrder> seriesQueue;
	private final BlockingQueue<GeneratorTask> generatorTaskQueue;
	private final SeriesOrderTracker seriesOrderTracker;
	private final String dcm4cheeRootDir;

	private final ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
	private final ProxyConfig config = ProxyConfig.getInstance();
	private final ProxyLogger logger = ProxyLogger.getInstance();

	public SeriesWatcher(BlockingQueue<SeriesOrder> seriesQueue, BlockingQueue<GeneratorTask> pngTaskQueue)
	{
		this.seriesQueue = seriesQueue;
		this.generatorTaskQueue = pngTaskQueue;
		this.seriesOrderTracker = SeriesOrderTracker.getInstance();
		dcm4cheeRootDir = ProxyConfig.getInstance().getParam("dcm4cheeDirRoot");
	}

	@Override
	public void run()
	{
		int loopCount = 0;
		MySqlQueries mySqlQueries = MySqlInstance.getInstance().getMysqlQueries();
		while (!shutdownSignal.hasShutdown()) {
			loopCount++;
			try {
				SeriesOrder seriesOrder = seriesQueue.poll(5000, TimeUnit.MILLISECONDS);

				if (seriesOrder != null) { // Add new SeriesOrder to the map.
					logger.info("Series watcher found new series with series UID" + seriesOrder.getSeriesUID());
					seriesOrderTracker.add(new SeriesOrderStatus(seriesOrder));
				}
				if (seriesOrderTracker.getStatusSet().size() > 0) { // Walk through the map
					logger.info("SeriesOrderTracker has: " + seriesOrderTracker.getStatusSet().size() + " series.");
				} else {
					// if (loopCount % 5 == 0) {
					// logger.info("SeriesOrderTracker running, but has no series.");
					// }
				}
				// Loop through all current active series and
				for (SeriesOrderStatus currentSeriesOrderStatus : seriesOrderTracker.getStatusSet()) {
					SeriesOrder currentSeriesOrder = currentSeriesOrderStatus.getSeriesOrder();
					List<Map<String, String>> newImageList = mySqlQueries.getUnprocessedPngFilesSeries(currentSeriesOrder
							.getSeriesUID());

					if (newImageList.size() > 0) { // Look for newly arrived DICOM images.
						logger.info("Found " + newImageList.size() + " DICOM images added.");
						currentSeriesOrder.updateImageList(newImageList); // SeriesOrder tracks instance order
						currentSeriesOrderStatus.registerActivity();
						currentSeriesOrderStatus.setState(ProcessingState.IN_PIPELINE);
						addToGeneratorTaskPipeline(newImageList);
					} else { // There are no unprocessed PNG files left.
						if (currentSeriesOrderStatus.getProcessingState() == ProcessingState.IN_PIPELINE) {
							logger.info("No unprocesses PNG files left for series with UID " + currentSeriesOrder.getSeriesUID());
							List<Map<String, String>> processedPNGImages = mySqlQueries
									.getProcessedPngFilesSeriesOrdered(currentSeriesOrder.getSeriesUID());
							if (processedPNGImages.size() > 0) { // Convert processed PNG files to PNG grid files
								logger.info("Found " + processedPNGImages.size() + " PNG images. Converting to grid images.");
								currentSeriesOrderStatus.setState(ProcessingState.IN_PNG_GRID_PIPELINE);
								addToPNGGridGeneratorTaskPipeline(newImageList);
							}
						}
					}
				}
				// Loop through all current active series and remove them if they are done.
				for (SeriesOrderStatus currentSeriesOrderStatus : seriesOrderTracker.getStatusSet()) {
					if (currentSeriesOrderStatus.isDone()) { // Remove finished series
						seriesOrderTracker.remove(currentSeriesOrderStatus);
					}
				}
			} catch (Exception e) {
				logger.warning("Exception SeriesWatcher thread.", e);
			}
		}
	}

	private void addToPNGGridGeneratorTaskPipeline(List<Map<String, String>> pngImageList)
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		int currentImageIndex = 0;
		for (Map<String, String> currentPNGImage : pngImageList) {
			String inputPNGFilePath = getInputFilePath(currentPNGImage); // Get the input file path.
			File inputPNGFile = new File(inputPNGFilePath);
			String outputPNGGridFilePath = createOutputFilePathForDicomPNGGridImage(currentPNGImage);
			logger.info("Checking epad_files table for: " + outputPNGGridFilePath);
			if (!queries.hasEpadFile(outputPNGGridFilePath)) {
				logger.info("SeriesWatcher has: " + currentPNGImage.get("sop_iuid") + " PNG for grid processing.");
				// Need to get slice for PNG files.
				List<File> inputPNGGridFiles = getSliceOfPNGFiles(pngImageList, currentImageIndex, 16);
				createPngGridFileForPNGImages(inputPNGFile, inputPNGGridFiles, outputPNGGridFilePath);
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

	private void createPngGridFileForPNGImages(File inputPNGFile, List<File> inputPNGGridFiles,
			String outputPNGGridFilePath)
	{
		logger.info("Offering to PNGGridTaskQueue: out=" + outputPNGGridFilePath + " in=" + inputPNGFile.getAbsolutePath());

		File outputPNGFile = new File(outputPNGGridFilePath);
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		insertEpadFile(queries, outputPNGFile);

		PNGGridGeneratorTask pngGridGeneratorTask = new PNGGridGeneratorTask(inputPNGFile, inputPNGGridFiles, outputPNGFile);
		generatorTaskQueue.offer(pngGridGeneratorTask);
	}

	private void addToGeneratorTaskPipeline(List<Map<String, String>> newImageList)
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		for (Map<String, String> currImage : newImageList) {
			String inputFilePath = getInputFilePath(currImage); // get the input file path.
			File inputFile = new File(inputFilePath);

			if (!inputFile.exists()) { // If the file does not exist (stored on another file system)
				try { // We create a temporary file
					File temp = File.createTempFile(currImage.get("sop_iuid"), ".tmp");
					feedFileWithDicomFromWado(temp, currImage);
					inputFile = temp;
				} catch (IOException e) {
					logger.warning("Exception when creating temp image : " + currImage.get("sop_iuid"), e);
				}
			}
			String outputFilePath = createOutputFilePathForDicomPNGImage(currImage); // Get the output file path.
			logger.info("Checking epad_files table for: " + outputFilePath);
			if (!queries.hasEpadFile(outputFilePath)) {
				if (PixelMedUtils.isDicomSegmentationObject(inputFilePath)) { // Generate slices of PNG mask
					logger.info("SeriesWatcher has: " + currImage.get("sop_iuid") + " DICOM segmentation object.");
					processDicomSegmentationObject(outputFilePath, inputFilePath);
				} else { // Generate PNG file.
					logger.info("SeriesWatcher has: " + currImage.get("sop_iuid") + " DICOM object.");
					createPngFileForDicomImage(outputFilePath, inputFile);
				}
			}
		}
	}

	private void feedFileWithDicomFromWado(File temp, Map<String, String> currImage)
	{
		// we use wado to get the dicom image
		String host = config.getParam("NameServer");
		int port = config.getIntParam("DicomServerWadoPort");
		String base = config.getParam("WadoUrlExtension");

		WadoUrlBuilder wadoUrlBuilder = new WadoUrlBuilder(host, port, base, WadoUrlBuilder.ContentType.FILE);

		// GET WADO call result.
		wadoUrlBuilder.setStudyUID(currImage.get("study_iuid"));
		wadoUrlBuilder.setSeriesUID(currImage.get("series_iuid"));
		wadoUrlBuilder.setObjectUID(currImage.get("sop_iuid"));

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
			logger.warning("Not able to build wado url for : " + temp.getName(), e);
		} catch (HttpException e) {
			logger.warning("Not able to get the wado image : " + temp.getName(), e);
		} catch (IOException e) {
			logger.warning("Not able to write the temp dicom image : " + temp.getName(), e);
		}
	}

	private void processDicomSegmentationObject(String outputFilePath, String inputFilePath)
	{
		logger.info("Segmentation Object found: " + inputFilePath);

		File inFile = new File(inputFilePath);
		File outFile = new File(outputFilePath);

		DicomSegmentObjectGeneratorTask dsoTask = new DicomSegmentObjectGeneratorTask(inFile, outFile);
		generatorTaskQueue.offer(dsoTask);
	}

	private void createPngFileForDicomImage(String outputFilePath, File inputFile)
	{
		logger.info("Offering to PngTaskQueue: out=" + outputFilePath + " in=" + inputFile.getAbsolutePath());

		File outputFile = new File(outputFilePath);
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		insertEpadFile(queries, outputFile);

		PngGeneratorTask pngTask = new PngGeneratorTask(inputFile, outputFile);
		generatorTaskQueue.offer(pngTask);
	}

	private void insertEpadFile(MySqlQueries queries, File outputFile)
	{
		Map<String, String> epadFilesTable = DcmDbUtils.createEPadFilesTableData(outputFile);
		epadFilesTable.put("file_status", "" + PngStatus.IN_PIPELINE.getCode());
		queries.insertEpadFile(epadFilesTable);
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
	 * 
	 * @param currImage Map of String to String
	 * @return String
	 */
	private String createOutputFilePathForDicomPNGImage(Map<String, String> currImage)
	{
		String seriesIUID = currImage.get("series_iuid");
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		String studyUID = queries.getStudyUIDForSeries(seriesIUID);
		String imageUID = currImage.get("sop_iuid");
		StringBuilder outputPath = new StringBuilder();

		outputPath.append(ResourceUtils.getEPADWebServerPNGDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(seriesIUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(imageUID)).append(".png");

		return outputPath.toString();
	}

	/**
	 * 
	 * @param currImage Map of String to String
	 * @return String
	 */
	private String createOutputFilePathForDicomPNGGridImage(Map<String, String> currImage)
	{
		String seriesIUID = currImage.get("series_iuid");
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		String studyUID = queries.getStudyUIDForSeries(seriesIUID);
		String imageUID = currImage.get("sop_iuid");
		StringBuilder outputPath = new StringBuilder();

		outputPath.append(ResourceUtils.getEPADWebServerPNGGridDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(seriesIUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(imageUID)).append(".png");

		return outputPath.toString();
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
}
