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
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.FileKey;
import edu.stanford.epad.epadws.processing.model.DicomUploadFile;
import edu.stanford.epad.epadws.processing.model.DicomUploadPipelineFiles;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;

/**
 * This is a process the runs every five seconds. If it sees a new ZIP, DCM or GZ file in two consecutive runs with the
 * same file size it will assume the it is "DONE" and put it into the QUEUE. This begins the process.
 * 
 * Note: This class does not appear to be in use - delete?
 */
public class EPADUploadZIPWatcher implements Runnable
{
	public static final String UPLOAD_ROOT_DIR = EPADConfig.getEPADWebServerUploadDir();
	public static final int CHECK_INTERVAL = 5000; // Check every 5 seconds.
	private final EPADLogger log = EPADLogger.getInstance();
	private final BlockingQueue<File> unzipQueue;

	/**
	 * Keeps track of files that are new, but not in the queue. If the file size and time-stamp does not change for 5
	 * seconds then assume it is ready for processing.
	 */
	private final Map<FileKey, DicomUploadFile> waitingForPipelineMap = new HashMap<FileKey, DicomUploadFile>();

	/**
	 * Tracks files that had a error in the queue.
	 */
	private final DicomUploadPipelineFiles uploadPipelineFiles = DicomUploadPipelineFiles.getInstance();

	/**
	 * Track /resource/upload/temp-* directories that are empty (no *.dcm files) and delete them if they remain empty for
	 * a certain period of time. (5 min). The Long value is the timestamp of when it is first seen as empty.
	 */
	private final Map<FileKey, Long> emptyDir = new HashMap<FileKey, Long>();
	private static final long EMPTY_DIR_INTERVAL = 60 * 15 * 1000; // in milliseconds

	public EPADUploadZIPWatcher(BlockingQueue<File> unzipQueue)
	{
		this.unzipQueue = unzipQueue;
	}

	@Override
	public void run()
	{
		try {
			ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
			while (true) {
				if (shutdownSignal.hasShutdown()) {
					log.info("Warning: EPADUploadZIPWatcher shutdown signal received.");
					return;
				}
				File rootDir = new File(UPLOAD_ROOT_DIR);
				try {
					searchDir(rootDir);
					searchForEmptyDirectories(rootDir);
					deleteEmptyDirectories();
				} catch (ConcurrentModificationException cme) {
					log.warning("Upload Watch Thread had: ", cme);
				}

				if (shutdownSignal.hasShutdown()) {
					log.info("Warning: EPADUploadZIPWatcher shutdown signal received.");
					return;
				}
				TimeUnit.MILLISECONDS.sleep(CHECK_INTERVAL);
			}
		} catch (Exception e) {
			log.severe("UploadDirWatcher error.", e);
		} finally {
			log.info("Done. UploadDirWatcher thread.");
		}
		log.info("Warning: EPADUploadZIPWatcher shutting down.");
	}

	/**
	 * Delete any directory that is more than EMPTY_DIR_INTERVAL milliseconds old.
	 */
	private void deleteEmptyDirectories()
	{
		Set<FileKey> keys = emptyDir.keySet();
		long deleteTime = System.currentTimeMillis() - EMPTY_DIR_INTERVAL;
		for (FileKey currKey : keys) {
			Long timestamp = emptyDir.get(currKey);
			if (deleteTime > timestamp) {
				File dirToDelete = currKey.getFile();
				if (EPADFileUtils.deleteDirectoryAndContents(dirToDelete)) {
					log.info("DELETED DIR: " + dirToDelete.getAbsolutePath());
					emptyDir.remove(currKey);
				} else {
					log.info("FAILED to delete dir: " + dirToDelete.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * Looks for empty directories under a root file add them.
	 * 
	 * @param rootDir File
	 */
	private void searchForEmptyDirectories(File rootDir)
	{
		try {
			List<File> dirs = EPADFileUtils.getDirectoriesIn(rootDir);
			for (File currDir : dirs) { // check to see if it is empty.
				int nFile = EPADFileUtils.countFilesWithEnding(currDir, ".dcm");
				FileKey keyFile = new FileKey(currDir);
				if (nFile == 0) {
					if (emptyDir.get(keyFile) == null) { // Add it with the current timestamp only if not already in the list.
						emptyDir.put(keyFile, System.currentTimeMillis());
						log.info("EMPTY_DIR list added: " + keyFile.toString());
					}
				} else {
					emptyDir.remove(keyFile); // Remove it from the list.
				}
			}
		} catch (Exception e) {
			log.warning(e.getMessage(), e);
		}
	}

	private void searchDir(File dir) throws InterruptedException
	{
		if (!dir.isDirectory()) // get all the files in this directory.
			return;

		File[] allFiles = dir.listFiles();

		// log.info("Found "+allFiles.length+" files/dir in: "+dir.getAbsolutePath());

		for (File currFile : allFiles) {
			if (currFile.isDirectory()) {
				searchDir(currFile);
			}
			if (currFile.canRead()) {
				FileKey currFileKey = new FileKey(currFile);
				boolean isError = uploadPipelineFiles.isKnownErrorFile(currFileKey);
				boolean isInPipeline = uploadPipelineFiles.isInPipeline(currFileKey);
				if (!isError && !isInPipeline) {
					// If this file is already there and it has not changed since last time, put it in pipeline.
					boolean shouldAddToPipeline = checkShouldAddToPipeline(currFileKey);
					if (shouldAddToPipeline) {
						log.info("Adding file: " + currFileKey + " to unzip queue.");
						boolean accepted = unzipQueue.offer(currFileKey.getFile(), 100, TimeUnit.MILLISECONDS);
						if (accepted) {
							uploadPipelineFiles.addToPipeline(currFileKey);
							waitingForPipelineMap.remove(currFileKey);
						} else {
							log.info("WARNING: File: " + currFileKey + " was not accepted to unzipQueue. Likely full.");
						}
					}
				}
			} else {
				log.info("Cannot read file: " + currFile.getAbsolutePath());
			}
		}
	}

	/**
	 * determines if this file should be added to the pipeline.
	 * 
	 * @param fileKey - FileKey
	 * @return boolean - true if it should be added.
	 */
	private boolean checkShouldAddToPipeline(FileKey fileKey)
	{
		if (fileKey.getFile().isDirectory())
			return false;

		// Only add *.dcm, *.zip, *.gz files.
		String fileName = fileKey.getFile().getName().toLowerCase();
		if (!isExtensionForPipeline(fileName)) {
			return false;
		}

		if (waitingForPipelineMap.containsKey(fileKey)) {
			// determine if the file has changed, since last checked.
			DicomUploadFile currFileState = new DicomUploadFile(fileKey.getFile());
			DicomUploadFile prevFileState = waitingForPipelineMap.get(fileKey);

			long currSize = currFileState.getSize();
			long prevSize = prevFileState.getSize();

			long currTimestamp = currFileState.getLastUpdated();
			long prevTimestamp = prevFileState.getLastUpdated();

			return (currSize == prevSize && currTimestamp == prevTimestamp);
		} else {
			waitingForPipelineMap.put(fileKey, new DicomUploadFile(fileKey.getFile()));
		}
		return false;
	}

	/**
	 * Return true if this is an extension that is be added to the pipeline. Those are zip, dcm, and gz.
	 * 
	 * @param filename String
	 * @return boolean
	 */
	private boolean isExtensionForPipeline(String filename)
	{
		if (filename.endsWith(".dcm"))
			return true;
		if (filename.endsWith(".zip"))
			return true;
		if (filename.endsWith(".gz"))
			return true;

		// also add files without an extension.
		if (filename.indexOf(".") < 0)
			return true;

		return false;
	}

	/**
	 * Return the number of files that had an exception in the pipeline.
	 * 
	 * @return int number of files in the error list.
	 */
	public int getErrorFileCount()
	{
		return uploadPipelineFiles.getErrorFiles().size();
	}
}
