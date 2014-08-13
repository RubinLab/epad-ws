package edu.stanford.epad.epadws.dcm4chee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.util.EPADConfig;
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

	/**
	 * TODO This does not work. The ./dcmdeleteSeries script invoked the dcm4chee twiddle command but it appears that the
	 * moveSeriesToTrash operation it calls has no effect in this version of dcm4chee. See:
	 * http://www.dcm4che.org/jira/browse/WEB-955
	 */
	public static boolean deleteSeries(String seriesUID)
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		boolean success = false;

		try {
			log.info("Deleting series " + seriesUID);

			String[] command = { "./dcmdeleteSeries", seriesUID };
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			String myScriptsDirectory = EPADConfig.getEPADWebServerMyScriptsDir();
			processBuilder.directory(new File(myScriptsDirectory));
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			process.getOutputStream();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
				log.info("./dcmdeleteSeries: " + line);
			}
			try {
				int exitValue = process.waitFor();
				if (exitValue == 0) {
					log.info("Deleted DICOM series " + seriesUID);
					success = true;
				} else
					log.warning("Failed to delete DICOM series " + seriesUID + "; exitValue=" + exitValue);
			} catch (Exception e) {
				log.warning("Failed to delete DICOM series " + seriesUID, e);
			}

			String cmdLineOutput = sb.toString();

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}
		} catch (IOException e) {
			log.warning("Failed to delete DICOM series " + seriesUID, e);
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(is);
		}
		return success;
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

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}
		} catch (Exception e) {
			log.warning("Failed to delete DICOM study " + studyUID, e);
		}
	}

	private static String parseError(String output)
	{
		String result = "";
		try {
			String[] lines = output.split("\n");
			for (String currLine : lines) {
				if (currLine.toLowerCase().contains("error")) {
					result = result + currLine;
				}
			}
		} catch (Exception e) {
			log.warning("DicomSendTask.parseError had: " + e.getMessage() + " for: " + output, e);
		}
		if (result.length() > 0)
			return result;
		else
			return output;
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
