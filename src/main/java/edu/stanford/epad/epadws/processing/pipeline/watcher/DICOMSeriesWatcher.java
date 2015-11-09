//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.processing.pipeline.watcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.EventMessageCodes;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.dtos.TaskStatus;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.processing.model.DicomSeriesProcessingState;
import edu.stanford.epad.epadws.processing.model.DicomSeriesProcessingStatusTracker;
import edu.stanford.epad.epadws.processing.model.SeriesPipelineState;
import edu.stanford.epad.epadws.processing.model.SeriesProcessingDescription;
import edu.stanford.epad.epadws.processing.pipeline.task.GeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.task.ImageCheckTask;
import edu.stanford.epad.epadws.processing.pipeline.task.PNGGridGeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.task.RemotePACQueryTask;
import edu.stanford.epad.epadws.processing.pipeline.task.SingleFrameDICOMPngGeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.UserProjectService;

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
 * This watcher submits these to the PNG generation task queue to be processed by the
 * {@link SingleFrameDICOMPngGeneratorTask}.
 * 
 * @see XNATSeriesWatcher
 */
public class DICOMSeriesWatcher implements Runnable
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final BlockingQueue<SeriesProcessingDescription> dicomSeriesWatcherQueue;
	private final BlockingQueue<GeneratorTask> pngGeneratorTaskQueue;
	private final DicomSeriesProcessingStatusTracker dicomSeriesTracker;
	private final String dcm4cheeRootDir; // Used by the PNG grid process only

	private final ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();

	private QueueAndWatcherManager queueAndWatcherManager;

	public DICOMSeriesWatcher(BlockingQueue<SeriesProcessingDescription> dicomSeriesWatcherQueue,
			BlockingQueue<GeneratorTask> pngGeneratorTaskQueue)
	{
		log.info("Starting the DICOM series watcher");

		this.dicomSeriesWatcherQueue = dicomSeriesWatcherQueue;
		this.pngGeneratorTaskQueue = pngGeneratorTaskQueue;
		this.dicomSeriesTracker = DicomSeriesProcessingStatusTracker.getInstance();
		this.dcm4cheeRootDir = EPADConfig.dcm4cheeDirRoot;
	}

	static long count = 0;
	@Override
	public void run()
	{
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // Let interactive thread run sooner
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();

		queueAndWatcherManager = QueueAndWatcherManager.getInstance();
		Calendar prevTime = null;

		while (!shutdownSignal.hasShutdown()) {
			count++;
			if (count%1000 == 0)
				log.debug("DICOMSeriesWatcher:" + count);
			try {
				SeriesProcessingDescription seriesProcessingDescription = dicomSeriesWatcherQueue.poll(2000,
						TimeUnit.MILLISECONDS);

				if (seriesProcessingDescription != null) {
					String seriesUID = seriesProcessingDescription.getSeriesUID();
					String patientName = seriesProcessingDescription.getPatientName();
					int numberOfInstances = seriesProcessingDescription.getNumberOfInstances();
					log.info("Series watcher found new series " + seriesUID + " for patient " + patientName + " with "
							+ numberOfInstances + " instance(s).");
					if (!dicomSeriesTracker.getSeriesPipelineStates().contains(seriesUID))
					{
						dicomSeriesTracker.addSeriesPipelineState(new SeriesPipelineState(seriesProcessingDescription));
					}
					else
						log.info("Series " + seriesUID + " is already on queue");

				}
				// Loop through all series being processed and find images that have no corresponding PNG file recorded in ePAD
				// database. Update their status to reflect this so that we can monitor percent completion for each series.
				for (SeriesPipelineState activeSeriesPipelineState : dicomSeriesTracker.getSeriesPipelineStates()) {
					SeriesProcessingDescription activeSeriesProcessingDescription = activeSeriesPipelineState
							.getSeriesProcessingDescription();
					String seriesUID = activeSeriesProcessingDescription.getSeriesUID();
					String studyUID = activeSeriesProcessingDescription.getStudyUID();
					String patientName = activeSeriesProcessingDescription.getPatientName();
					Set<DICOMFileDescription> unprocessedDICOMFiles = epadOperations.getUnprocessedDICOMFilesInSeries(seriesUID);

					if (unprocessedDICOMFiles.size() > 0) {
						log.info("Series " + activeSeriesProcessingDescription.getSeriesUID() + " has "
								+ unprocessedDICOMFiles.size() + " unprocessed DICOM image(s) remaining.");
						activeSeriesProcessingDescription.updateWithDICOMFileDescriptions(unprocessedDICOMFiles);
						activeSeriesPipelineState.registerActivity();
						if (!activeSeriesPipelineState.equals(DicomSeriesProcessingState.IN_PIPELINE)) // 
						{
							log.info("Run:" + count + " Submitted " + unprocessedDICOMFiles.size() + " image(s) for series " + seriesUID
									+ " to PNG generator");
							String username = UserProjectService.pendingUploads.get(studyUID);
							if (username != null && username.indexOf(":") != -1)
								username = username.substring(0, username.indexOf(":"));
							//if (username != null)
							//	projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_DICOM_PNG_GEN, seriesUID, "Generating PNGs", new Date(), null);
							queueAndWatcherManager.addDICOMFileToPNGGeneratorPipeline(patientName, unprocessedDICOMFiles);
						}
						activeSeriesPipelineState.setSeriesProcessingState(DicomSeriesProcessingState.IN_PIPELINE);
					} else { // All images have been submitted for PNG processing.
						/**
						 * Here is (not fully tested) code to generated 4x4 grids from the generated PNGs. This could be very
						 * expensive both in terms of conversion time and space so we do not activate.
						 * <p>
						 * List<DICOMFileDescription processedPNGImages = mySqlQueries
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
				for (SeriesPipelineState seriesPipelineState : dicomSeriesTracker.getSeriesPipelineStates()) {
					if (seriesPipelineState.isDone()) { // Remove finished series
						String seriesUID = seriesPipelineState.getSeriesProcessingDescription().getSeriesUID();
						String studyUID = seriesPipelineState.getSeriesProcessingDescription().getStudyUID();
						String patientID = seriesPipelineState.getSeriesProcessingDescription().getSubjectID();
						String patientName = seriesPipelineState.getSeriesProcessingDescription().getPatientName();
						dicomSeriesTracker.removeSeriesPipelineState(seriesPipelineState);
						epadDatabaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.DONE);
						log.info("Series " + seriesUID + " processing completed");
						if (UserProjectService.pendingUploads.containsKey(studyUID))
						{
							String username = UserProjectService.pendingUploads.get(studyUID);
							String projectID = EPADConfig.xnatUploadProjectID;
							if (username != null && username.indexOf(":") != -1)
							{
								projectID = username.substring(username.indexOf(":")+1);
								username = username.substring(0, username.indexOf(":"));
							}
							if (username != null)
							{
								epadDatabaseOperations.insertEpadEvent(
										username, 
										EventMessageCodes.STUDY_PROCESSED, 
										"", "", patientID, patientName, "", "", 
										"Study:" + studyUID,
										projectID,studyUID,"","",false);					
								UserProjectService.pendingUploads.remove(studyUID);
							}
						}
						if (UserProjectService.pendingPNGs.containsKey(seriesUID))
						{
							String username = UserProjectService.pendingPNGs.get(seriesUID);
							String projectID = EPADConfig.xnatUploadProjectID;
							if (username != null && username.indexOf(":") != -1)
							{
								projectID = username.substring(username.indexOf(":")+1);
								username = username.substring(0, username.indexOf(":"));
							}
							if (username != null)
							{
								projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_DICOM_PNG_GEN, seriesUID, "Generating PNGs Completed", null, new Date());
								epadDatabaseOperations.insertEpadEvent(
										username, 
										EventMessageCodes.IMAGE_PROCESSED, 
										"", "", patientID, patientName, "", "", 
										"Series:" + seriesUID,
										projectID,"","",seriesUID, false);					
								UserProjectService.pendingPNGs.remove(seriesUID);
							}
						}
					}
				}
				Calendar now = Calendar.getInstance();
				if (now.get(Calendar.HOUR_OF_DAY) == 1 && prevTime != null && prevTime.get(Calendar.HOUR_OF_DAY) != 1)
				{
					// Run at 1 am.
					try {
						if (!"true".equalsIgnoreCase(EPADConfig.getParamValue("DISABLE_IMAGECHECK")))
						{	
							ImageCheckTask ict = new ImageCheckTask();
							new Thread(ict).start();
						}
					} catch (Exception x) {
						log.warning("Exception running ImageCheck", x);
					}
					try {
						if (!"true".equalsIgnoreCase(EPADConfig.getParamValue("DISABLE_REMOTEPAC_QUERY")))
						{	
							RemotePACQueryTask rpqt = new RemotePACQueryTask(null);
							new Thread(rpqt).start();
						}
					} catch (Exception x) {
						log.warning("Exception running Remote Queries", x);
					}
				}
				prevTime = now;
			} catch (Exception e) {
				log.severe("Exception in DICOM series watcher thread", e);
			}
		}
		log.info("Warning: DICOMSeriesWatcher shutting down.");
	}

	@SuppressWarnings("unused")
	private void addToPNGGridGeneratorTaskPipeline(String seriesUID, String imageUID,
			List<Map<String, String>> unprocessedPNGImageDescriptions)
	{
		EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		int currentImageIndex = 0;
		for (Map<String, String> currentPNGImageDescription : unprocessedPNGImageDescriptions) {
			String inputPNGFilePath = getInputFilePath(currentPNGImageDescription); // Get the input file path.
			File inputPNGFile = new File(inputPNGFilePath);
			String outputPNGGridFilePath = createOutputFilePathForDicomPNGGridImage(currentPNGImageDescription);
			if (!databaseOperations.hasEpadFileRow(outputPNGGridFilePath)) {
				log.info("SeriesWatcher has: " + currentPNGImageDescription.get("sop_iuid") + " PNG for grid processing.");
				// Need to get slice for PNG files.
				List<File> inputPNGGridFiles = getSliceOfPNGFiles(unprocessedPNGImageDescriptions, currentImageIndex, 16);
				createPngGridFileForPNGImages(seriesUID, imageUID, inputPNGFile, inputPNGGridFiles, outputPNGGridFilePath);
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

	private void createPngGridFileForPNGImages(String seriesUID, String imageUID, File inputPNGFile,
			List<File> inputPNGGridFiles, String outputPNGGridFilePath)
	{
		// logger.info("Offering to PNGGridTaskQueue: out=" + outputPNGGridFilePath + " in=" +
		// inputPNGFile.getAbsolutePath());

		File outputPNGFile = new File(outputPNGGridFilePath);
		EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		insertEpadFile(databaseOperations, outputPNGGridFilePath, outputPNGFile.length(), imageUID);

		PNGGridGeneratorTask pngGridGeneratorTask = new PNGGridGeneratorTask(seriesUID, imageUID, inputPNGFile,
				inputPNGGridFiles, outputPNGFile);
		pngGeneratorTaskQueue.offer(pngGridGeneratorTask);
	}

	private void insertEpadFile(EpadDatabaseOperations epadDatabaseOperations, String outputPNGFilePath, long fileSize,
			String imageUID)
	{
		Map<String, String> epadFilesTable = Dcm4CheeDatabaseUtils.createEPadFilesRowData(outputPNGFilePath, fileSize,
				imageUID);
		epadFilesTable.put("file_status", "" + PNGFileProcessingStatus.IN_PIPELINE.getCode());
		epadDatabaseOperations.insertEpadFileRow(epadFilesTable);
	}

	String getInputFilePath(Map<String, String> currImage)
	{
		return getDcm4cheeRootDir() + currImage.get("filepath");
	}

	private String createOutputFilePathForDicomPNGGridImage(Map<String, String> currImage)
	{
		String seriesIUID = currImage.get("series_iuid");
		Dcm4CheeDatabaseOperations databaseOperations = Dcm4CheeDatabase.getInstance().getDcm4CheeDatabaseOperations();
		String studyUID = databaseOperations.getStudyUIDForSeries(seriesIUID);
		String imageUID = currImage.get("sop_iuid");
		StringBuilder outputPath = new StringBuilder();

		outputPath.append(EPADConfig.getEPADWebServerPNGGridDir());
		outputPath.append("/studies/" + studyUID);
		outputPath.append("/series/" + seriesIUID);
		outputPath.append("/images/" + imageUID);

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
