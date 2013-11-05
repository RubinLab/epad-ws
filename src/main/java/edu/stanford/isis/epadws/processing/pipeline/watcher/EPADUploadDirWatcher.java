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

/**
 * Watches for a new directory and a ZIP file in the ePAD upload directory. When a new directory is found it puts a
 * "dir.found" file into it and passes it to the {@DicomSendTask}.
 * 
 * @author amsnyder
 */
public class EPADUploadDirWatcher implements Runnable
{
	public static final int CHECK_INTERVAL = 5000; // Check every 5 seconds
	public static final String FOUND_DIR_FILE = "dir.found";
	private static final long MAX_WAIT_TIME = 120000; // 120 seconds
	private final EPADLogger log = EPADLogger.getInstance();

	@Override
	public void run()
	{
		try {
			ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
			File rootDir = new File(ResourceUtils.getEPADWebServerUploadDir());
			log.info("MySQL upload directory:" + ResourceUtils.getEPADWebServerUploadDir());
			while (true) {
				if (shutdownSignal.hasShutdown()) {
					return;
				}
				try {
					List<File> newDirList = findNewDir(rootDir);
					if (newDirList != null) {
						for (File currDir : newDirList) {
							processDir(currDir);
						}
					}
				} catch (ConcurrentModificationException e) {
					log.warning("Upload Watch Thread had error: ", e);
				}
				if (shutdownSignal.hasShutdown()) {
					return;
				}
				TimeUnit.MILLISECONDS.sleep(CHECK_INTERVAL);
			}
		} catch (Exception e) {
			log.sever("UploadDirWatcher error.", e);
		} finally {
			log.info("Done. UploadDirWatcher thread.");
		}
	}

	// looks for new files without the dir.found file in it.
	private List<File> findNewDir(File dir)
	{
		List<File> retVal = new ArrayList<File>();

		File[] allFiles = dir.listFiles();
		for (File currFile : allFiles) {
			if (currFile.isDirectory()) {
				if (!hasSignalDir(currFile)) {
					retVal.add(currFile);
				}
			}
		}
		return retVal;
	}

	private boolean hasSignalDir(File dir)
	{
		String[] allFilePaths = dir.list();
		for (String currPath : allFilePaths) {
			if (currPath.indexOf(FOUND_DIR_FILE) > 0) {
				return true;
			}
		}
		return false;
	}

	private void processDir(File dir) throws InterruptedException
	{
		try {
			boolean hasZipFile = waitOnEmptyDir(dir);
			if (hasZipFile) {
				File zipFile = waitForZipUploadComplete(dir);
				unzipFiles(zipFile);
			}
			sendDicom(dir);
			deleteDir(dir);
		} catch (IOException ioe) {
			log.warning("DicomSend failed (IOException) for dir" + dir.getAbsolutePath(), ioe);
			writeExceptionLog(dir, ioe);
		} catch (IllegalStateException e) {
			log.warning("DicomSend failed (ISE) for dir=" + dir.getAbsolutePath(), e);
			writeExceptionLog(dir, e);
		} catch (Exception e) {
			log.warning("DicomSend failed (Exception) for dir=" + dir.getAbsolutePath(), e);
			writeExceptionLog(dir, e);
		} finally {
			log.info("Upload finished: " + dir.getAbsolutePath());
		}
	}

	private boolean waitOnEmptyDir(File dir) throws InterruptedException
	{
		log.info("Upload waiting on empty dir: " + dir.getAbsolutePath());
		// if this file has only one zip file, wait for it to complete upload.
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
				throw new IllegalStateException("Exceeded max wait time to upload a file.");
			Thread.sleep(2000);
		}
	}

	private File waitForZipUploadComplete(File dir) throws InterruptedException
	{
		log.info("MySQL waiting for completion of: " + dir.getAbsolutePath());
		long zipFileStartWaitTime = System.currentTimeMillis(); // Wait for the zip file to complete.
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

			if (zipFiles == null) { // Should have only one zip file in directory.
				throw new IllegalStateException("No zip file in directory. dir=" + dir.getAbsolutePath());
			} else if (zipFiles.length > 1) {
				int numZipFiles = zipFiles.length;
				throw new IllegalStateException("Too many zip files (" + numZipFiles + ") in directory. dir="
						+ dir.getAbsolutePath());
			}

			FileKey zipFileKey = new FileKey(zipFiles[0]);
			DicomUploadFile zipFile = new DicomUploadFile(zipFileKey.getFile());

			long currZipFileSize = zipFile.getSize();
			long currZipFileLastUpdated = zipFile.getLastUpdated();

			if (prevZipFileSize == currZipFileSize && prevZipFileLastUpdated == currZipFileLastUpdated) {
				return zipFileKey.getFile(); // uploading complete.
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
		log.info("Unzipping: " + zipFile.getAbsolutePath());
		EPADFileUtils.extractFolder(zipFile.getAbsolutePath());
	}

	private void sendDicom(File dir) throws Exception
	{
		log.info("DCMSND: " + dir.getAbsolutePath());
		DicomSendTask.dcmsnd(dir, true);
	}

	private void deleteDir(File dir)
	{
		log.info("MySQL deleting directory: " + dir.getAbsolutePath());
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
