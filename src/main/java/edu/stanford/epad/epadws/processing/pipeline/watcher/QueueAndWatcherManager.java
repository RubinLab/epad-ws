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
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
import edu.stanford.epad.epadws.processing.pipeline.task.RTDICOMProcessingTask;
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
	private final ExecutorService epadSessionWatcherExec = Executors.newSingleThreadExecutor();

	private final Dcm4CheeDatabaseWatcher dcm4CheeDatabaseWatcher;
	private final DICOMSeriesWatcher dicomSeriesWatcher;
	private final XNATSeriesWatcher xnatSeriesWatcher;
	private final PngGeneratorProcess pngGeneratorProcess;
	private final EPADUploadDirWatcher epadUploadDirWatcher;
	private final EPADSessionWatcher epadSessionWatcher;

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
		epadSessionWatcher = new EPADSessionWatcher();
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
		epadSessionWatcherExec.execute(epadSessionWatcher);
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

	public void addDICOMFileToPNGGeneratorPipeline(String patientName, Set<DICOMFileDescription> dicomFileDescriptions)
	{
		boolean sameSeries = true; // This should always be the case, but who knows
		String prevSeriesUID = null;
		Set<DICOMFileDescription> dicomFilesCopy = new HashSet<DICOMFileDescription>();
		for (DICOMFileDescription dicomFileDescription : dicomFileDescriptions)
		{
			if (prevSeriesUID != null && !prevSeriesUID.equals(dicomFileDescription.seriesUID))
			{
				sameSeries = false;
				break;
			}
			dicomFilesCopy.add(dicomFileDescription);
		}
		for (DICOMFileDescription dicomFileDescription : dicomFileDescriptions) {
			String seriesUID = dicomFileDescription.seriesUID;
			String imageUID = dicomFileDescription.imageUID;
			String dicomFilePath = getDICOMFilePath(dicomFileDescription);
			String modality = dicomFileDescription.modality;
			File inputDICOMFile = new File(dicomFilePath);

			// If the file does not exist locally (because it is stored on another file system), download it.
			if (!inputDICOMFile.exists()) {
				inputDICOMFile = downloadRemoteDICOM(dicomFileDescription);
				dicomFilePath = inputDICOMFile.getAbsolutePath();
			}
			log.info("Dicom file, modality:" +  dicomFileDescription.modality);
			if ("RTSTRUCT".equals(modality))
			{
				extractRTDicomInfo(dicomFileDescription, inputDICOMFile);
			}
			if ("RTSTRUCT".equals(modality) || "RTPLAN".equals(modality) || "PR".equals(modality) || "SR".equals(modality)) return; // images to generate
			if (PixelMedUtils.isDicomSegmentationObject(dicomFilePath)) {
				if (sameSeries)
				{
					// Get the last image (alphabetically last - looks like this is true all the time???)
					String createdTime = dicomFileDescription.createdTime;
					for (DICOMFileDescription dsoFile : dicomFilesCopy)
					{
						// TODO - Should really convert to Date and then compare
						if (createdTime.compareTo(dsoFile.createdTime) < 0)
						{
							createdTime = dsoFile.createdTime;
							dicomFileDescription = dsoFile;
						}
					}
					log.info("DSO Shown filepath:" + dicomFileDescription.filePath + " createdTime:" + dicomFileDescription.createdTime);
					dicomFilePath = getDICOMFilePath(dicomFileDescription);
					inputDICOMFile = new File(dicomFilePath);
					if (!inputDICOMFile.exists()) {
						inputDICOMFile = downloadRemoteDICOM(dicomFileDescription);
						dicomFilePath = inputDICOMFile.getAbsolutePath();
					}
				}
				// Generate mask PNGs, also AIMFile if this is the first time (only one image)
				generateMaskPNGsForDicomSegmentationObject(dicomFileDescription, inputDICOMFile, dicomFilesCopy.size() == 1);
				if (sameSeries) break;
			} else if (PixelMedUtils.isMultiframedDicom(dicomFilePath)) {
				generatePNGsForMultiFrameDicom(dicomFileDescription, inputDICOMFile);
			} else { // Assume it is non multi-frame DICOM
				generatePNGFileForSingleFrameDICOMImage(patientName, dicomFileDescription, inputDICOMFile);
			}
		}
	}

	private File downloadRemoteDICOM(DICOMFileDescription dicomFileDescription)
	{
		String imageUID = dicomFileDescription.imageUID;
		String seriesUID = dicomFileDescription.seriesUID;
		try {
			log.info("Downloading remote DICOM file with image " + imageUID + " for series UID " + seriesUID);
			File downloadedDICOMFile = File.createTempFile(imageUID, ".tmp");
			DCM4CHEEUtil.downloadDICOMFileFromWADO(dicomFileDescription, downloadedDICOMFile);
			return downloadedDICOMFile;
		} catch (Exception e) {
			log.warning("Exception when downloading DICOM file with series UID " + seriesUID + " and image UID "
					+ dicomFileDescription.imageUID, e);
		}
		log.warning("Error downloading DICOM file with series UID " + seriesUID + " and image UID "
				+ dicomFileDescription.imageUID);
		return null;
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

	private void generateMaskPNGsForDicomSegmentationObject(DICOMFileDescription dicomFileDescription, File dsoFile , boolean generateAIM)
	{
		log.info("DICOM segmentation object found for series " + dicomFileDescription.seriesUID + " dso:" + dsoFile.getAbsolutePath());
		if (DSOMaskPNGGeneratorTask.seriesBeingProcessed.contains(dicomFileDescription.seriesUID))
		{
			log.info("QWMgr: DSO series " + dicomFileDescription.seriesUID + " already being processed");
			return;
		}
		String tagFilePath = createOutputPNGFilePathForSingleFrameDICOMImage(dicomFileDescription).replace(".png", ".tag");
		DSOMaskPNGGeneratorTask dsoMaskPNGGeneratorTask = new DSOMaskPNGGeneratorTask(dicomFileDescription.studyUID,
				dicomFileDescription.seriesUID,
				dsoFile, generateAIM, tagFilePath);

		pngGeneratorTaskQueue.offer(dsoMaskPNGGeneratorTask);
	}

	private void generatePNGsForMultiFrameDicom(DICOMFileDescription dicomFileDescription, File multiFrameDicomFile)
	{
		log.info("Multi-frame DICOM object found for series " + dicomFileDescription.seriesUID);

		String tagFilePath = createOutputPNGFilePathForSingleFrameDICOMImage(dicomFileDescription).replace(".png", ".tag");
		MultiFramePNGGeneratorTask dsoPNGGeneratorTask = new MultiFramePNGGeneratorTask(dicomFileDescription.studyUID, 
				dicomFileDescription.seriesUID,
				dicomFileDescription.imageUID,
				multiFrameDicomFile, tagFilePath);

		pngGeneratorTaskQueue.offer(dsoPNGGeneratorTask);
	}

	private void generatePNGFileForSingleFrameDICOMImage(String patientName, DICOMFileDescription dicomFileDescription,
			File dicomFile)
	{
		if (SingleFrameDICOMPngGeneratorTask.imagesBeingProcessed.contains(dicomFileDescription.imageUID))
		{
			log.info("QWMgr: Image " + dicomFileDescription.imageUID + " already being processed");
			return;
		}
		String outputPNGFilePath = createOutputPNGFilePathForSingleFrameDICOMImage(dicomFileDescription);
		File outputPNGFile = new File(outputPNGFilePath);
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		insertEpadFile(epadDatabaseOperations, outputPNGFilePath, outputPNGFile.length(), dicomFileDescription.imageUID);
		SingleFrameDICOMPngGeneratorTask pngGeneratorTask = new SingleFrameDICOMPngGeneratorTask(patientName,
				dicomFileDescription, dicomFile, outputPNGFile);
		pngGeneratorTaskQueue.offer(pngGeneratorTask);
	}

	private void extractRTDicomInfo(DICOMFileDescription dicomFileDescription, File dicomFile)
	{
		log.info("DICOM RT found for series " + dicomFileDescription.seriesUID + " dicomFile:" + dicomFile.getAbsolutePath());
		String rtFilePath = createOutputPNGFilePathForSingleFrameDICOMImage(dicomFileDescription).replace(".png", ".mat");
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		insertEpadFile(epadDatabaseOperations, rtFilePath, 0, dicomFileDescription.imageUID);
		RTDICOMProcessingTask rtTask = new RTDICOMProcessingTask(dicomFileDescription.studyUID, dicomFileDescription.seriesUID, dicomFileDescription.imageUID,
				dicomFile, rtFilePath);

		pngGeneratorTaskQueue.offer(rtTask);
	}

	private void insertEpadFile(EpadDatabaseOperations epadDatabaseOperations, String outputPNGFilePath, long fileSize,
			String imageUID)
	{
		Map<String, String> epadFilesRow = Dcm4CheeDatabaseUtils.createEPadFilesRowData(outputPNGFilePath, fileSize,
				imageUID);
		epadFilesRow.put("file_status", "" + SeriesProcessingStatus.IN_PIPELINE.getCode());
		epadDatabaseOperations.insertEpadFileRow(epadFilesRow);
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
