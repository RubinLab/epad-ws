package edu.stanford.isis.epadws.processing.pipeline.watcher;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.FileKey;
import edu.stanford.isis.epad.common.util.ResourceUtils;
import edu.stanford.isis.epadws.processing.model.DicomUploadFile;
import edu.stanford.isis.epadws.processing.pipeline.task.DicomSendTask;
import edu.stanford.isis.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.isis.epadws.xnat.XNATUtil;

/**
 * Watches for a new directory containing ZIP or DICOM files in the ePAD upload directory. When a new directory is found
 * it puts a "dir.found" file into it. If the upload is a ZIP file it waits for the ZIP upload to complete and then
 * unzips it.
 * <p>
 * It then generates DICOM tag files for each DICOM file, creates XNAT entities for the DICOM files, and sends the DICOM
 * files to DCM4CHEE.
 * 
 * @author amsnyder
 */
public class EPADUploadDirWatcher implements Runnable
{
	private static final int CHECK_INTERVAL = 5000; // Check every 5 seconds
	private static final String FOUND_DIR_FILE = "dir.found";
	private static final long MAX_WAIT_TIME = 120000; // 120 seconds
	private static final EPADLogger log = EPADLogger.getInstance();

	@Override
	public void run()
	{
		try {
			ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
			File rootDir = new File(ResourceUtils.getEPADWebServerUploadDir());
			log.info("Startying the ePAD upload directory watcher; directory =" + ResourceUtils.getEPADWebServerUploadDir());
			while (true) {
				if (shutdownSignal.hasShutdown())
					return;

				try {
					List<File> newDirList = findNewUploadDirectory(rootDir);
					if (newDirList != null) {
						for (File currDir : newDirList) {
							processUploadDirectory(currDir);
						}
					}
				} catch (ConcurrentModificationException e) {
					log.warning("EPADUploadDirWatcher thread error ", e);
				}
				if (shutdownSignal.hasShutdown())
					return;
				TimeUnit.MILLISECONDS.sleep(CHECK_INTERVAL);
			}
		} catch (Exception e) {
			log.sever("EPADUploadDirWatcher thread error", e);
		} finally {
			log.info("EPADUploadDirWatcher thread done.");
		}
	}

	private List<File> findNewUploadDirectory(File dir)
	{ // Looks for new directories without the dir.found file.
		List<File> retVal = new ArrayList<File>();

		File[] allFiles = dir.listFiles();
		for (File currFile : allFiles) {
			if (currFile.isDirectory()) {
				if (!hasFoundDirFile(currFile)) {
					retVal.add(currFile);
				}
			}
		}
		return retVal;
	}

	private boolean hasFoundDirFile(File dir)
	{
		String[] allFilePaths = dir.list();
		for (String currPath : allFilePaths) {
			if (currPath.indexOf(FOUND_DIR_FILE) > 0) {
				return true;
			}
		}
		return false;
	}

	private void processUploadDirectory(File dir) throws InterruptedException
	{
		try {
			boolean hasZipFile = waitOnEmptyUploadDirectory(dir);
			if (hasZipFile) {
				File zipFile = waitForZipUploadToComplete(dir);
				unzipFiles(zipFile);
			}
			XNATUtil.createXNATEntitiesFromDICOMFilesInDirectory(dir);
			cleanUploadDirectory(dir);
			sendFilesToDcm4Chee(dir);
			deleteUploadDir(dir);
		} catch (IOException ioe) {
			log.warning("EPADUploadDirWatcher: error (IOException);dir=" + dir.getAbsolutePath(), ioe);
			writeExceptionLog(dir, ioe);
		} catch (IllegalStateException e) {
			log.warning("EPADUploadDirWatcher: error (IllegalStateException); dir=" + dir.getAbsolutePath(), e);
			writeExceptionLog(dir, e);
		} catch (Exception e) {
			log.warning("EPADUploadDirWatcher: error (Exception); dir=" + dir.getAbsolutePath(), e);
			writeExceptionLog(dir, e);
		} finally {
			log.info("EPADUploadDirWatcher: upload finished: " + dir.getAbsolutePath());
		}
	}

	private void cleanUploadDirectory(File dir)
	{
		EPADFileUtils.deleteFilesInDirWithExtension(dir, "properties");
		EPADFileUtils.deleteFilesInDirWithExtension(dir, "zip");
	}

	private boolean waitOnEmptyUploadDirectory(File dir) throws InterruptedException
	{
		log.info("EPADUploadDirWatcher: upload waiting for upload to start in directory: " + dir.getAbsolutePath());
		// If this file has only one ZIP file, wait for it to complete upload.
		long emptyDirStartWaitTime = System.currentTimeMillis();
		boolean hasZipFile = false;

		long oldSize = -1;
		int oldNumberOfFiles = -1;

		while (true) {
			String[] filePaths = dir.list();

			if (filePaths != null) {
				if (filePaths.length > 0) {
					long newSize = dir.getTotalSpace();
					int newNumberOfFiles = filePaths.length;

					if (oldNumberOfFiles != newNumberOfFiles || oldSize != newSize) {
						oldNumberOfFiles = newNumberOfFiles;
						oldSize = newSize;
					} else {
						for (String currPath : filePaths) {
							currPath = currPath.toLowerCase();
							if (currPath.endsWith(".zip")) {
								hasZipFile = true;
							}
						}
						return hasZipFile;
					}
				}
			}
			if ((System.currentTimeMillis() - emptyDirStartWaitTime) > MAX_WAIT_TIME)
				throw new IllegalStateException("Exceeded maximum wait time to upload a ZIP file.");
			Thread.sleep(2000);
		}
	}

	private File waitForZipUploadToComplete(File dir) throws InterruptedException
	{
		log.info("EPADUploadDirWatcher: waiting for completion of ZIP upload in directory: " + dir.getAbsolutePath());
		long zipFileStartWaitTime = System.currentTimeMillis();
		long prevZipFileSize = -1;
		long prevZipFileLastUpdated = 0;

		while (true) {
			File[] zipFiles = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name)
				{
					return name.toLowerCase().endsWith(".zip");
				}
			});

			if (zipFiles == null) {
				throw new IllegalStateException("No ZIP file in directory: " + dir.getAbsolutePath());
			} else if (zipFiles.length > 1) {
				int numZipFiles = zipFiles.length;
				throw new IllegalStateException("Too many ZIP files (" + numZipFiles + ") in directory:"
						+ dir.getAbsolutePath());
			}
			FileKey zipFileKey = new FileKey(zipFiles[0]);
			DicomUploadFile zipFile = new DicomUploadFile(zipFileKey.getFile());

			long currZipFileSize = zipFile.getSize();
			long currZipFileLastUpdated = zipFile.getLastUpdated();

			if (prevZipFileSize == currZipFileSize && prevZipFileLastUpdated == currZipFileLastUpdated) {
				return zipFileKey.getFile(); // Uploading complete.
			} else {
				prevZipFileSize = currZipFileSize;
				prevZipFileLastUpdated = currZipFileLastUpdated;
			}
			if ((System.currentTimeMillis() - zipFileStartWaitTime) > MAX_WAIT_TIME) {
				throw new IllegalStateException("Zip file upload time exceeded.");
			}
			Thread.sleep(1000);
		}
	}

	private void unzipFiles(File zipFile) throws IOException
	{
		log.info("EPADUploadDirWatcher: unzipping " + zipFile.getAbsolutePath());
		EPADFileUtils.extractFolder(zipFile.getAbsolutePath());
	}

	private void sendFilesToDcm4Chee(File dir) throws Exception
	{
		log.info("EPADUploadDirWatcher: sending directory " + dir.getAbsolutePath() + " to DCM4CHEE");
		DicomSendTask.dcmsnd(dir, true);
	}

	private void deleteUploadDir(File dir)
	{
		log.info("EPADUploadDirWatcher: deleting directory: " + dir.getAbsolutePath());
		EPADFileUtils.deleteDirAndContents(dir);
	}

	private void writeExceptionLog(File dir, Exception e)
	{
		String fileName = dir.getAbsolutePath() + "/exception_" + System.currentTimeMillis() + ".log";
		String content = makeLogExpContents(e);
		EPADFileUtils.write(new File(fileName), content);
	}

	private String makeLogExpContents(Exception e)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Failed to dcmsnd file for reason: ").append(e.getMessage()).append("\n");

		StackTraceElement[] ste = e.getStackTrace();
		for (StackTraceElement currSte : ste) {
			sb.append(currSte.getFileName()).append(".").append(currSte.getMethodName()).append(currSte.getLineNumber());
		}
		return sb.toString();
	}
}
