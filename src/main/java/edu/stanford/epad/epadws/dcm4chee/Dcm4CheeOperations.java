package edu.stanford.epad.epadws.dcm4chee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;

/**
 * Operations on Dcm4Chee (as distinct from operations directly on DCM4CHEE's database).
 * 
 * 
 * @author martin
 * @see Dcm4CheeDatabaseOperations
 */
public class Dcm4CheeOperations
{
	private static EPADLogger log = EPADLogger.getInstance();

	public static void dcmsnd(File inputDirFile, boolean throwException) throws Exception
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileWriter tagFileWriter = null;

		try {
			String aeTitle = EPADConfig.aeTitle;
			String dicomServerIP = EPADConfig.dicomServerIP;
			String dicomServerPort = EPADConfig.dicomServerPort;
			String dicomServerTitleAndPort = aeTitle + "@" + dicomServerIP + ":" + dicomServerPort;

			dicomServerTitleAndPort = dicomServerTitleAndPort.trim();

			String dirPath = inputDirFile.getAbsolutePath();
			if (pathContainsSpaces(dirPath))
				dirPath = escapeSpacesInDirPath(dirPath);

			File dir = new File(dirPath);
			int nbFiles = -1;
			if (dir != null) {
				String[] filePaths = dir.list();
				if (filePaths != null)
					nbFiles = filePaths.length;
			}
			log.info("./dcmsnd: sending " + nbFiles + " file(s) - command: ./dcmsnd " + dicomServerTitleAndPort + " "
					+ dirPath);

			String[] command = { "./dcmsnd", dicomServerTitleAndPort, dirPath };

			ProcessBuilder processBuilder = new ProcessBuilder(command);
			String dicomScriptsDir = EPADConfig.getEPADWebServerDICOMBinDir();
			processBuilder.directory(new File(dicomScriptsDir));
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();

			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
				log.info("./dcmsend output: " + line);
			}

			try {
				int exitValue = process.waitFor();
				log.info("DICOM send exit value is: " + exitValue);
			} catch (InterruptedException e) {
				log.warning("Didn't send DICOM files in: " + inputDirFile.getAbsolutePath(), e);
			}
			String cmdLineOutput = sb.toString();
			writeUploadLog(cmdLineOutput);

			if (cmdLineOutput.toLowerCase().contains("error"))
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
		} catch (Exception e) {
			if (e instanceof IllegalStateException && throwException) {
				throw e;
			}
			log.warning("DicomSendTask failed to send DICOM files: " + e.getMessage());
			if (throwException) {
				throw new IllegalStateException("DicomSendTask failed to send DICOM files", e);
			}
		} catch (OutOfMemoryError oome) {
			log.warning("DicomSendTask out of memory: ", oome);
			if (throwException) {
				throw new IllegalStateException("DicomSendTask out of memory: ", oome);
			}
		} finally {
			IOUtils.closeQuietly(tagFileWriter);
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(is);
		}
	}

	public static void deleteStudy(String studyUID)
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			log.info("Deleting study " + studyUID + " files - command: ./dcmdeleteStudy " + studyUID);

			String[] command = { "./dcmdeleteStudy", studyUID };

			ProcessBuilder pb = new ProcessBuilder(command);
			String myScriptsBinDirectory = EPADConfig.getEPADWebServerMyScriptsDir();
			pb.directory(new File(myScriptsBinDirectory));

			Process process = pb.start();
			process.getOutputStream();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				log.info("./dcmdeleteStudy: " + line);
				sb.append(line).append("\n");
			}

			try {
				int exitValue = process.waitFor();
				log.info("DICOM delete study exit value is: " + exitValue);
			} catch (Exception e) {
				log.warning("Failed to delete DICOM study " + studyUID, e);
			}
			String cmdLineOutput = sb.toString();
			writeDeleteLog(cmdLineOutput);

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}
		} catch (Exception e) {
			log.warning("Failed to delete DICOM study " + studyUID, e);
		}
	}

	/**
	 * Delete from DCM4CHEE
	 * 
	 * @param seriesUID
	 * @throws Exception
	 */
	public static void deleteSeries(String seriesUID) throws Exception
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			log.info("Deleting series " + seriesUID + " files - command: ./dcmdeleteSeries " + seriesUID);

			String[] command = { "./dcmdeleteSeries", seriesUID };

			ProcessBuilder pb = new ProcessBuilder(command);
			String myScriptsDirectory = EPADConfig.getEPADWebServerMyScriptsDir();
			pb.directory(new File(myScriptsDirectory));

			Process process = pb.start();
			process.getOutputStream();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			try {
				int exitValue = process.waitFor();
				log.info("DICOM delete series exit value is: " + exitValue);
			} catch (Exception e) {
				log.warning("Failed to delete DICOM series " + seriesUID, e);
			}

			String cmdLineOutput = sb.toString();
			writeDeleteLog(cmdLineOutput);

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}
		} catch (Exception e) {
			log.warning("Failed to delete DICOM series " + seriesUID, e);
		}
	}

	private static String parseError(String output)
	{
		try {
			String[] lines = output.split("\n");
			for (String currLine : lines) {
				if (currLine.toLowerCase().contains("error")) {
					return currLine;
				}
			}
		} catch (Exception e) {
			log.warning("DicomSendTask.parseError had: " + e.getMessage() + " for: " + output, e);
		}
		return output;
	}

	/**
	 * Log the result of this upload to the log directory.
	 * 
	 * @param contents String
	 */
	private static void writeUploadLog(String contents)
	{
		String logDirectory = EPADConfig.getEPADWebServerLogDir();
		String fileName = logDirectory + "upload_" + System.currentTimeMillis() + ".log";
		EPADFileUtils.write(new File(fileName), contents);
	}

	/**
	 * Log the result of this delete to the log directory.
	 * 
	 * @param contents String
	 */
	private static void writeDeleteLog(String contents)
	{
		String logDirectory = EPADConfig.getEPADWebServerLogDir();
		String fileName = logDirectory + "delete_" + System.currentTimeMillis() + ".log";
		EPADFileUtils.write(new File(fileName), contents);
	}

	/**
	 * 
	 * @param dirPath String
	 * @return boolean true if this path contains spaces.
	 */
	private static boolean pathContainsSpaces(String dirPath)
	{
		return dirPath.indexOf(' ') > 0;
	}

	/**
	 * 
	 * @param path String
	 * @return String
	 */
	private static String escapeSpacesInDirPath(String path)
	{
		StringBuilder sb = new StringBuilder();
		String[] parts = path.split(" ");
		boolean isFirst = true;
		for (String part : parts) {
			if (!isFirst) {
				sb.append("\\ ");
			}
			sb.append(part);
			isFirst = false;
		}
		return sb.toString();
	}
}
