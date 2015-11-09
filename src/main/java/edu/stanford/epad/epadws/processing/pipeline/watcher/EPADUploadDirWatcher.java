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
						"", "", "", "", "", "", "Upload Error:" + e.getMessage());
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
							"", "", "", "", "", "", "Error Processing Upload");					
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
					"", "", "", "", "", "", "Error Processing Upload");					
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
