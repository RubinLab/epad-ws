package edu.stanford.isis.epadws.processing.pipeline;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.util.FileKey;
import edu.stanford.isis.epad.common.util.ProxyFileUtils;
import edu.stanford.isis.epad.common.util.ResourceUtils;
import edu.stanford.isis.epadws.server.ShutdownSignal;
import edu.stanford.isis.epadws.server.managers.pipeline.UploadFile;

/**
 * Watches for a new directory and a zip file. When a new directory is found it puts a "dir.found" file into it and
 * passes it to the DicomSendTask.
 * 
 * @author amsnyder
 */
public class MySqlUploadDirWatcher implements Runnable
{
	public static final int CHECK_INTERVAL = 5000; // check every 5 seconds.
	public static final String FOUND_DIR_FILE = "dir.found";
	private static final long MAX_WAIT_TIME = 120000; // in milliseconds
	private final ProxyLogger log = ProxyLogger.getInstance();

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
				} catch (ConcurrentModificationException cme) {
					log.warning("Upload Watch Thread had: ", cme);
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
		// wait for the zip file to complete.
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

			// should have only one zip file in directory.
			if (zipFiles == null) {
				throw new IllegalStateException("No zip file in directory. dir=" + dir.getAbsolutePath());
			} else if (zipFiles.length > 1) {
				int numZipFiles = zipFiles.length;
				throw new IllegalStateException("Too many zip files (" + numZipFiles + ") in directory. dir="
						+ dir.getAbsolutePath());
			}

			FileKey zipFileKey = new FileKey(zipFiles[0]);
			UploadFile zipFile = new UploadFile(zipFileKey.getFile());

			long currZipFileSize = zipFile.getSize();
			long currZipFileLastUpdated = zipFile.getLastUpdated();

			if (prevZipFileSize == currZipFileSize && prevZipFileLastUpdated == currZipFileLastUpdated) {
				// uploading complete.
				return zipFileKey.getFile();
			} else {
				prevZipFileSize = currZipFileSize;
				prevZipFileLastUpdated = currZipFileLastUpdated;
			}

			if ((System.currentTimeMillis() - zipFileStartWaitTime) > MAX_WAIT_TIME) {
				throw new IllegalStateException("Zip file upload time exceeded.");
			}
			// sleep one second.
			Thread.sleep(1000);
		}// while
	}

	private void unzipFiles(File zipFile) throws IOException
	{
		log.info("Unzipping: " + zipFile.getAbsolutePath());
		ProxyFileUtils.extractFolder(zipFile.getAbsolutePath());
	}

	private void sendDicom(File dir) throws Exception
	{
		log.info("DCMSND: " + dir.getAbsolutePath());
		DicomSendTask.dcmsnd(dir, true);
	}

	private void deleteDir(File dir)
	{
		log.info("MySQL deleting directory: " + dir.getAbsolutePath());
		ProxyFileUtils.deleteDirAndContents(dir);
	}

	private void writeExceptionLog(File dir, Exception e)
	{
		String fileName = dir.getAbsolutePath() + "/exception_" + System.currentTimeMillis() + ".log";
		String content = makeLogExpContents(e);
		ProxyFileUtils.write(new File(fileName), content);
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
