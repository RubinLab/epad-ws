/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
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
					return;
				}
				TimeUnit.MILLISECONDS.sleep(CHECK_INTERVAL);
			}
		} catch (Exception e) {
			log.severe("UploadDirWatcher error.", e);
		} finally {
			log.info("Done. UploadDirWatcher thread.");
		}
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
