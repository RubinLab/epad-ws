package edu.stanford.epad.epadws.processing.pipeline.watcher;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.EPADResources;
import edu.stanford.epad.common.util.FileKey;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeOperations;
import edu.stanford.epad.epadws.processing.model.DicomUploadFile;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.epad.epadws.xnat.XNATCreationOperations;

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
	private static final long MAX_WAIT_TIME = 1200000; // 120 seconds //TODO temp x10
	private static final EPADLogger log = EPADLogger.getInstance();

	@Override
	public void run()
	{
		try {
			ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
			File rootUploadDirectory = new File(EPADResources.getEPADWebServerUploadDir());
			log.info("Starting the ePAD upload directory watcher; directory =" + EPADResources.getEPADWebServerUploadDir());
			while (true) {
				if (shutdownSignal.hasShutdown())
					return;

				try {
					List<File> newUploadDirectories = findNewUploadDirectory(rootUploadDirectory);
					if (newUploadDirectories != null) {
						for (File newUploadDirectory : newUploadDirectories) {
							processUploadDirectory(newUploadDirectory);
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
			log.severe("Warning: EPADUploadDirWatcher thread error", e);
		} finally {
			log.info("Warning: EPADUploadDirWatcher thread done.");
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

	private void processUploadDirectory(File directory) throws InterruptedException
	{
		try {
			boolean hasZipFile = waitOnEmptyUploadDirectory(directory);
			if (hasZipFile) {
				File zipFile = waitForZipUploadToComplete(directory);
				unzipFiles(zipFile);
			}
			// TODO Should not create XNAT entities until the DICOM send succeeds.
			XNATCreationOperations.createXNATEntitiesFromDICOMFilesInUploadDirectory(directory);
			cleanUploadDirectory(directory);
			sendFilesToDcm4Chee(directory);
		} catch (IOException ioe) {
			log.warning("IOException uploading " + directory.getAbsolutePath(), ioe);
			writeExceptionLog(directory, ioe);
		} catch (IllegalStateException e) {
			log.warning("IllegalStateException uploading " + directory.getAbsolutePath(), e);
			writeExceptionLog(directory, e);
		} catch (Exception e) {
			log.warning("Exception uploading " + directory.getAbsolutePath(), e);
			writeExceptionLog(directory, e);
		} finally {
			log.info("Upload of directory " + directory.getAbsolutePath() + " finished");
			deleteUploadDirectory(directory);
		}
	}

	private void cleanUploadDirectory(File dir)
	{ // TODO Should be deleteFilesInDirectoryWithoutExtension("dcm");
		EPADFileUtils.deleteFilesInDirectoryWithExtension(dir, "properties");
		EPADFileUtils.deleteFilesInDirectoryWithExtension(dir, "zip");
		EPADFileUtils.deleteFilesInDirectoryWithExtension(dir, "log");
	}

	private boolean waitOnEmptyUploadDirectory(File dir) throws InterruptedException
	{
		log.info("Found new upload - waiting for it to complete in directory " + dir.getAbsolutePath());
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
				throw new IllegalStateException("Exceeded maximum wait time to upload a ZIP file");
			Thread.sleep(2000);
		}
	}

	private File waitForZipUploadToComplete(File dir) throws InterruptedException
	{
		log.info("Waiting for completion of unzip in upload directory " + dir.getAbsolutePath());
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
				throw new IllegalStateException("No ZIP file in upload directory " + dir.getAbsolutePath());
			} else if (zipFiles.length > 1) {
				int numZipFiles = zipFiles.length;
				throw new IllegalStateException("Too many ZIP files (" + numZipFiles + ") in upload directory:"
						+ dir.getAbsolutePath());
			}
			FileKey zipFileKey = new FileKey(zipFiles[0]);
			DicomUploadFile zipFile = new DicomUploadFile(zipFileKey.getFile());

			long currZipFileSize = zipFile.getSize();
			long currZipFileLastUpdated = zipFile.getLastUpdated();

			if (prevZipFileSize == currZipFileSize && prevZipFileLastUpdated == currZipFileLastUpdated) {
				return zipFileKey.getFile(); // Uploading complete
			} else {
				prevZipFileSize = currZipFileSize;
				prevZipFileLastUpdated = currZipFileLastUpdated;
			}
			if ((System.currentTimeMillis() - zipFileStartWaitTime) > MAX_WAIT_TIME) {
				throw new IllegalStateException("ZIP file upload time exceeded");
			}
			Thread.sleep(1000);
		}
	}

	private void unzipFiles(File zipFile) throws IOException
	{
		log.info("Unzipping " + zipFile.getAbsolutePath());
		EPADFileUtils.extractFolder(zipFile.getAbsolutePath());
	}

	private void sendFilesToDcm4Chee(File directory) throws Exception
	{
		log.info("Sending DICOM files in upload directory " + directory.getAbsolutePath() + " to DCM4CHEE");
		Dcm4CheeOperations.dcmsnd(directory, true);
	}

	private void deleteUploadDirectory(File dir)
	{
		log.info("Deleting upload directory " + dir.getAbsolutePath());
		EPADFileUtils.deleteDirectoryAndContents(dir);
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
