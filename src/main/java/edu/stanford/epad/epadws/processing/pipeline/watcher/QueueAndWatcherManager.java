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

	public static final BlockingQueue<SeriesProcessingDescription> dicomSeriesWatcherQueue = new ArrayBlockingQueue<SeriesProcessingDescription>(
			2000);
	public static final BlockingQueue<SeriesProcessingDescription> xnatSeriesWatcherQueue = new ArrayBlockingQueue<SeriesProcessingDescription>(
			2000);
	public static final BlockingQueue<GeneratorTask> pngGeneratorTaskQueue = new ArrayBlockingQueue<GeneratorTask>(2000);
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

	private static String dcm4cheeRootDir;

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
	
	private static String getDICOMFilePath(DICOMFileDescription dicomFileDescription)
	{
		return getDcm4cheeRootDir() + dicomFileDescription.filePath;
	}

	/**
	 * Add a forward slash if it is missing.
	 * 
	 * @return String
	 */
	public static String getDcm4cheeRootDir()
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

	public static void extractRTDicomInfo(DICOMFileDescription dicomFileDescription, File dicomFile)
	{
		log.info("DICOM RT found for series " + dicomFileDescription.seriesUID + " dicomFile:" + dicomFile.getAbsolutePath());
		RTDICOMProcessingTask rtTask = new RTDICOMProcessingTask(dicomFileDescription.studyUID, dicomFileDescription.seriesUID, dicomFileDescription.imageUID,
				dicomFile, null);

		pngGeneratorTaskQueue.offer(rtTask);
	}
	public static void extractRTDicomInfo(DICOMFileDescription dicomFileDescription, String referencedSeriedUID)
	{
		String dicomFilePath = getDICOMFilePath(dicomFileDescription);
		File dicomFile = new File(dicomFilePath);	
		log.info("DICOM RT found for series " + dicomFileDescription.seriesUID + " dicomFile:" + dicomFile.getAbsolutePath());
		RTDICOMProcessingTask rtTask = new RTDICOMProcessingTask(dicomFileDescription.studyUID, dicomFileDescription.seriesUID, dicomFileDescription.imageUID,
				dicomFile, referencedSeriedUID);

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
		if (!EPADConfig.getEPADWebServerPNGDir().endsWith("/"))
			outputPNGFilePath.append("/");
		outputPNGFilePath.append("studies/" + studyUID);
		outputPNGFilePath.append("/series/" + seriesUID);
		outputPNGFilePath.append("/images/" + imageUID + ".png");

		return outputPNGFilePath.toString();
	}
}
