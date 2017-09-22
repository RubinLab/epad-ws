/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
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
import edu.stanford.epad.epadws.arrdb.ArrDatabase;
import edu.stanford.epad.epadws.arrdb.ArrDatabaseOperations;
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
				//check and clear old arrdb entities
				if (now.get(Calendar.HOUR_OF_DAY) == 3 && prevTime != null && prevTime.get(Calendar.HOUR_OF_DAY) != 3)
				{
					if (EPADConfig.logOlderThan_Days!=-1) { //if it is -1, do not delete log entries 
						log.info("3 am. Lets clear the audit log. Deleting entries older than "+ EPADConfig.logOlderThan_Days+" days");
						ArrDatabaseOperations arrDatabaseOperations = ArrDatabase.getInstance().getArrDatabaseOperations();
						arrDatabaseOperations.removeOldLogs(EPADConfig.logOlderThan_Days);
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
