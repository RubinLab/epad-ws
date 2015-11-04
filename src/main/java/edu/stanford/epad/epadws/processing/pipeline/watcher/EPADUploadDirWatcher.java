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
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.FileKey;
import edu.stanford.epad.dtos.TaskStatus;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.processing.model.DicomUploadFile;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.UserProjectService;

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
	private static final long MAX_WAIT_TIME = 3600000; // 1 hour (was 20 minutes before)
	private static final long MIN_WAIT_TIME = 1200000; // 20 minutes before
	private static final EPADLogger log = EPADLogger.getInstance();
	private final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();

	@Override
	public void run()
	{
		try {
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // Let interactive thread run sooner
			ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
			File rootUploadDirectory = new File(EPADConfig.getEPADWebServerUploadDir());
			log.info("Starting the ePAD upload directory watcher; directory =" + EPADConfig.getEPADWebServerUploadDir());
			long count = 0;
			while (true) {
				if (shutdownSignal.hasShutdown())
				{
					log.info("Warning: EPADUploadDirWatcher shutdown signal received.");
					return;
				}

				try {
					if (count%720 == 0)
						log.info("EPADUploadDirWatcher: Checking new uploads, count:" + count);
					count++;
					List<File> newUploadDirectories = findNewUploadDirectory(rootUploadDirectory);
					if (newUploadDirectories != null) {
						if (newUploadDirectories.size() > 0)
							log.info("Found " + newUploadDirectories.size() + " upload directories");
						for (File newUploadDirectory : newUploadDirectories) {
							processUploadDirectory(newUploadDirectory);
						}
						if (newUploadDirectories.size() > 0)
							log.info("EPADUploadDirWatcher: Done processing directories");
					}
				} catch (Exception e) {
					log.warning("EPADUploadDirWatcher thread error ", e);
				}
				if (shutdownSignal.hasShutdown())
				{
					log.info("Warning: EPADUploadDirWatcher shutdown signal received.");
					return;
				}
				TimeUnit.MILLISECONDS.sleep(CHECK_INTERVAL);
			}
		} catch (Error e) {
			log.severe("Warning: EPADUploadDirWatcher thread error", e);
		} catch (Throwable e) {
			log.severe("Warning: EPADUploadDirWatcher thread error", e);
		} finally {
			log.info("Warning: EPADUploadDirWatcher thread done.");
		}
		log.info("Warning: EPADUploadDirWatcher shutting down.");
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

	private String getUserNameFromProperties(File xnatprops)
	{
		String username = null;
		if (xnatprops.exists())
		{
			FileInputStream propertiesFileStream = null;
			try {
				Properties xnatUploadProperties = new Properties();
				propertiesFileStream = new FileInputStream(xnatprops);
				xnatUploadProperties.load(propertiesFileStream);
				String xnatProjectLabel = xnatUploadProperties.getProperty("XNATProjectName");
				String xnatSessionID = xnatUploadProperties.getProperty("XNATSessionID");
				username = xnatUploadProperties.getProperty("XNATUserName");
			} catch (Exception x) {
				
			} finally {
				if (propertiesFileStream != null)
					try {
						propertiesFileStream.close();
					} catch (IOException e) {
					}
			}
		}
		return username;
	}
	private void processUploadDirectory(File directory) throws InterruptedException
	{
		File zipFile = null;
		File zipDirectory = null;
		String username = null;
		boolean processed = false;
		try {
			File xnatprops = new File(directory, UserProjectService.XNAT_UPLOAD_PROPERTIES_FILE_NAME);
			username = getUserNameFromProperties(xnatprops);
			if (username != null)
				projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_UPLOAD, directory.getName(), "Started upload", new Date(), null);
			boolean hasZipFile = waitOnEmptyUploadDirectory(directory);
			if (username == null)
				username = getUserNameFromProperties(xnatprops);
			if (hasZipFile) {
				for (;;)
				{
					zipFile = waitForZipUploadToComplete(directory, username);
					if (zipFile == null) break;
					if (zipFile.getName().contains(" "))
						zipFile = EPADFileUtils.renameFile(zipFile, zipFile.getName().replace(' ', '_'));
					projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_UNZIP, zipFile.getName(), "Started unzip", new Date(), null);
					unzipFiles(zipFile);
					projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_UNZIP, zipFile.getName(), "Completed unzip", null, new Date());
					int removeLen = 4;
					if (zipFile.getName().toLowerCase().endsWith(".tar.gz"))
						removeLen = 7;
					else if (zipFile.getName().toLowerCase().endsWith(".gz"))
						removeLen = 0;
					zipDirectory = new File(directory, zipFile.getName().substring(0, zipFile.getName().length()-removeLen));
					if (xnatprops.exists())
						EPADFileUtils.copyFile(xnatprops, new File(zipDirectory, UserProjectService.XNAT_UPLOAD_PROPERTIES_FILE_NAME));
					projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_ADD_TO_PROJECT, zipDirectory.getName(), "Started processing", new Date(), null);
					String userName = UserProjectService.createProjectEntitiesFromDICOMFilesInUploadDirectory(zipDirectory, true);
					String fileCount = "";
					if (userName != null && userName.contains(":"))
						fileCount = userName.substring(userName.indexOf(":") +1);
					if (fileCount.equals("0") || fileCount.equals(""))
						fileCount = "Zero DICOM files Uploaded. Please check error log.";
					else if (fileCount.length() > 0)
						fileCount = fileCount + " files found.";
					projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_ADD_TO_PROJECT, zipDirectory.getName(), "Completed processing " + fileCount, null, new Date());
					cleanUploadDirectory(zipDirectory);
					if (userName != null)
					{
						sendFilesToDcm4Chee(userName, zipDirectory);
					}
					deleteUploadDirectory(zipDirectory);
					zipFile.delete();
				}
				processed = true;
			}
			String[] files = directory.list();
			if (files.length > 1 || (files.length == 1 && !files[0].contains("properties")))
			{
				projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_ADD_TO_PROJECT, directory.getName(), "Started processing", new Date(), null);
				String userName = UserProjectService.createProjectEntitiesFromDICOMFilesInUploadDirectory(directory, false);
				String fileCount = "";
				if (userName != null && userName.contains(":"))
					fileCount = userName.substring(userName.indexOf(":") +1);
				if (fileCount.equals("0") || fileCount.equals(""))
					fileCount = "Zero DICOM files Uploaded. Please check error log.";
				else if (fileCount.length() > 0)
					fileCount = fileCount + " files found.";
				projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_ADD_TO_PROJECT, directory.getName(), "Completed processing " + fileCount, null, new Date());
				log.info("Cleaning upload directory");
				cleanUploadDirectory(directory);
				files = directory.list();
				if (userName != null && files.length > 0)
				{
					sendFilesToDcm4Chee(userName, directory);
				}
				processed = true;
			}
			if (processed)
				projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_UPLOAD, directory.getName(), "Completed upload", null, new Date());
			else
				projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_UPLOAD, directory.getName(), "Completed upload - No files found", null, new Date());
		} catch (Exception e) {
			log.warning("Exception uploading " + directory.getAbsolutePath(), e);
			if (zipFile != null)
				projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_UNZIP, zipFile.getName(), null, null, new Date());
			if (zipDirectory != null)
				projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_ADD_TO_PROJECT, zipDirectory.getName(), null, null, new Date());
			if (username == null)
				username = UserProjectService.getUserNameFromPropertiesFile(directory);
			if (username != null) {
				if (username.indexOf(":") != -1)
					username = username.substring(0, username.indexOf(":"));
				String zipName = "DicomFile";
				if (zipFile != null) zipName = zipFile.getName();
				EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
				projectOperations.createEventLog(username, null, null, null, null, null, null, zipName, "Error processing uploaded file",  e.getMessage(), true);
				epadDatabaseOperations.insertEpadEvent(
						username, 
						"Error processing uploaded file:" + zipName, 
						zipName, "", zipName, zipName, zipName, zipName, "Upload Error:" + e.getMessage());
			}
			writeExceptionLog(directory, e);
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_UPLOAD, directory.getName(), "Failed upload:" + e.getMessage(), null, new Date());
		} finally {
			log.info("Upload of directory " + directory.getAbsolutePath() + " finished");
			try {
				deleteUploadDirectory(directory);
			} catch (Exception x) {}
		}
	}

	private void cleanUploadDirectory(File dir)
	{ // TODO Should be deleteFilesInDirectoryWithoutExtension("dcm");
		if (dir.exists())
		{
			EPADFileUtils.deleteFilesInDirectoryWithExtension(dir, "properties");
			EPADFileUtils.deleteFilesInDirectoryWithExtension(dir, "zip");
			EPADFileUtils.deleteFilesInDirectoryWithExtension(dir, "log");
			EPADFileUtils.deleteFilesInDirectoryWithExtension(dir, "json");
			EPADFileUtils.deleteFilesInDirectoryWithExtension(dir, "jpeg");
			EPADFileUtils.deleteFilesInDirectoryWithExtension(dir, "jpg");
			EPADFileUtils.deleteFilesInDirectoryWithExtension(dir, "png");
		}
	}

	private boolean waitOnEmptyUploadDirectory(File dir) throws InterruptedException
	{
		log.info("Found new upload - waiting for it to complete in directory " + dir.getAbsolutePath());
		// If this file has only one ZIP file, wait for it to complete upload.
		long emptyDirStartWaitTime = System.currentTimeMillis();
		boolean hasZipFile = false;

		long oldSize = -1;
		int oldNumberOfFiles = -1;
		int count = 0;
		while (true) {
			String[] filePaths = dir.list();
			count++;
			if (filePaths != null) {
				if (filePaths.length > 0) {
					long newSize = dir.getTotalSpace();
					int newNumberOfFiles = filePaths.length;

					if (oldNumberOfFiles != newNumberOfFiles || oldSize != newSize 
							|| (newNumberOfFiles == 1  && (System.currentTimeMillis() - emptyDirStartWaitTime) < MIN_WAIT_TIME)) {
						if (count%200 == 0)
							log.info("Waiting on directory " + dir.getName() + ", number of files:" + newNumberOfFiles + ", directory size:" + newSize);
						oldNumberOfFiles = newNumberOfFiles;
						oldSize = newSize;
					} else {
						log.info("Files uploaded(should be at least two files): " + Arrays.toString(filePaths));
						for (String currPath : filePaths) {
							currPath = currPath.toLowerCase();
							if (currPath.endsWith(".zip") || currPath.endsWith(".gz") || currPath.endsWith(".tar") || currPath.endsWith(".tgz")) {
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

	private File waitForZipUploadToComplete(File dir, String username) throws InterruptedException
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
					return name.toLowerCase().endsWith(".zip") || name.toLowerCase().endsWith(".gz") || name.toLowerCase().endsWith(".tar") || name.toLowerCase().endsWith(".tgz");
				}
			});

			if (zipFiles == null || zipFiles.length == 0) {
				return null;
//				throw new IllegalStateException("No ZIP file in upload directory " + dir.getAbsolutePath());
//			} else if (zipFiles.length > 1) {
//				int numZipFiles = zipFiles.length;
//				throw new IllegalStateException("Too many ZIP files (" + numZipFiles + ") in upload directory:"
//						+ dir.getAbsolutePath());
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
			Thread.sleep(2000);
		}
	}

	private void unzipFiles(File zipFile) throws IOException
	{
		log.info("Unzipping " + zipFile.getAbsolutePath());
		if (zipFile.getName().toLowerCase().endsWith(".zip"))
		{
			EPADFileUtils.extractFolder(zipFile.getAbsolutePath());
		}
		else if (zipFile.getName().toLowerCase().endsWith(".gz") || zipFile.getName().toLowerCase().endsWith(".tgz"))
		{
			try
			{
				EPADFileUtils.unGzip(zipFile, zipFile.getParentFile());
				String ungzName = zipFile.getName().substring(0, zipFile.getName().length()-3);
				zipFile.delete();
				File ungz = new File(zipFile.getParentFile(), ungzName);
				if (ungz.exists() && !ungz.getName().contains("."))
				{
					ungz.renameTo(new File(zipFile.getParentFile(), ungzName + ".tar"));
					ungz = new File(zipFile.getParentFile(), ungzName + ".tar");
				}
				else if (ungz.exists() && ungz.getName().endsWith("."))
				{
					ungz.renameTo(new File(zipFile.getParentFile(), ungzName + "tar"));
					ungz = new File(zipFile.getParentFile(), ungzName + "tar");
				}
				log.debug("Lookin for tar:" + ungz.getAbsolutePath());
				if (ungz.exists() && ungz.getName().toLowerCase().endsWith(".tar"))
				{
					File directory = new File(zipFile.getParentFile(), ungz.getName().substring(0, ungz.getName().lastIndexOf(".")));
					directory.mkdirs();
					log.debug("Untar directory:" +  directory.getAbsolutePath());
					EPADFileUtils.unTar(ungz, directory);
					String[] files = directory.list();
					log.debug("Untarred " + files.length + " files");
					ungz.delete();
				}				
			}
			catch (Exception x)
			{
				log.warning("Error ungzipping/untaring " + zipFile.getAbsolutePath());
			}
		}
		else if (zipFile.getName().toLowerCase().endsWith(".tar"))
		{
			try
			{
				File directory = new File(zipFile.getParentFile(), zipFile.getName().substring(0, zipFile.getName().lastIndexOf(".")));
				directory.mkdirs();
				EPADFileUtils.unTar(zipFile, directory);
				String[] files = directory.list();
				log.debug("Untarred " + files.length + " files");
				zipFile.delete();				
			}
			catch (Exception x)
			{
				log.warning("Error untaring " + zipFile.getAbsolutePath(), x);
			}
		}
	}

	private void sendFilesToDcm4Chee(String username, File directory) throws Exception
	{
		try {
			int count = 0;
			log.debug("Username:" + username);
			if (username.indexOf(":") != -1)
			{
				count = getInt(username.substring(username.lastIndexOf(":")+1));
				username = username.substring(0, username.lastIndexOf(":"));
			}
			if (count < 5000) {
				log.info("Sending DICOM files in upload directory " + directory.getAbsolutePath() + " to DCM4CHEE, number of files:" + count);
				projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_DCM4CHE_SEND, directory.getName(), "Started push", new Date(), null);
				Dcm4CheeOperations.dcmsnd(directory, true);
				projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_DCM4CHE_SEND, directory.getName(), "Completed push", null, new Date());
			} else {
				log.info("More than 5000 files to upload, trying to split dcmsend");
				int errcnt = 0;
				File[] dirs = new File[1];
				dirs[0] = directory;
				File root = dirs[0];
				while (dirs.length == 1)
				{
					root = dirs[0];
					dirs = dirs[0].listFiles();
				}
				String extraDir = "extra" + System.currentTimeMillis();
				File extra = new File(root, extraDir);
				extra.mkdirs();
				boolean movedToExtra = false;
				for (File dir: dirs)
				{
					if (dir.isDirectory()) {
						log.info("Sending DICOM files in upload directory " + dir.getAbsolutePath() + " to DCM4CHEE");
						projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_DCM4CHE_SEND, dir.getName(), "Started push", new Date(), null);
						boolean ok = Dcm4CheeOperations.dcmsnd(dir, false);
						if (!ok)
						{
							errcnt = errcnt + dir.list().length;
							projectOperations.createEventLog(username, null, null, null, null, null, null, dir.getName(), "Error sending to dcm4che",  null, true);
							log.warning("Error in upload: sending " + dir.getAbsolutePath() + " to dcm4che");
							projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_DCM4CHE_SEND, dir.getName(), "Failed push", null, new Date());
						}
						else
							projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_DCM4CHE_SEND, dir.getName(), "Completed push", null, new Date());
							
					} else {
						dir.renameTo(new File(extra, dir.getName()));
						movedToExtra = true;
					}
				}
				if (movedToExtra) {
					log.info("Sending DICOM files in upload directory " + extra.getAbsolutePath() + " to DCM4CHEE");
					boolean ok = Dcm4CheeOperations.dcmsnd(extra, false);
					if (!ok)
					{
						errcnt = errcnt + extra.list().length;
						log.warning("Error in upload: sending " + extra.getAbsolutePath() + " to dcm4che");
						projectOperations.createEventLog(username, null, null, null, null, null, null, extra.getName(), "Error sending to dcm4che",  null, true);
					}
				}
				if (errcnt > 0) {
					log.warning("Errors in " + errcnt + " dicoms while sending " + directory.getAbsolutePath() + " to dcm4che");
					EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
					epadDatabaseOperations.insertEpadEvent(
							username, 
							"Errors in sending " + errcnt + " DICOM files to DCM4CHEE", 
							"Dicoms", "Dicoms", "Dicoms", "Dicoms", "Dicoms", "Dicoms", "Error Processing Upload");					
					projectOperations.createEventLog(username, null, null, null, null, null, null, directory.getName(), "DCM4CHEE SEND", "Error sending " + errcnt + " DICOM files to DCM4CHEE", true);
				}
			}
		} catch (Exception x) {
			projectOperations.updateUserTaskStatus(username, TaskStatus.TASK_DCM4CHE_SEND, directory.getName(), "Error sending:" + x.getMessage(), null, new Date());
			log.warning("Error in upload: sending " + directory.getAbsolutePath() + " to dcm4che");
			EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			epadDatabaseOperations.insertEpadEvent(
					username, 
					"Error sending DICOM files to DCM4CHEE", 
					"Dicoms", "Dicoms", "Dicoms", "Dicoms", "Dicoms", "Dicoms", "Error Processing Upload");					
			projectOperations.createEventLog(username, null, null, null, null, null, null, directory.getName(), "DCM4CHEE SEND", "Error sending DICOM files to DCM4CHEE", true);
		}
		try {
			EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();	
			Set<String> seriesUIDs = UserProjectService.pendingPNGs.keySet();
			for (String seriesUID: seriesUIDs) {
				databaseOperations.deleteSeriesOnly(seriesUID); // Delete uploaded series status
			}
			
		} catch (Exception x) {
			
		}
	}
	
	private static int getInt(String value)
	{
		try {
			return new Integer(value.trim()).intValue();
		} catch (Exception x) {
			return 0;
		}
	}

	private void deleteUploadDirectory(File dir)
	{
		if (dir.exists())
		{
			log.info("Deleting upload directory " + dir.getAbsolutePath());
			EPADFileUtils.deleteDirectoryAndContents(dir);
		}
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
