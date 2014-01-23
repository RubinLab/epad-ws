package edu.stanford.isis.epadws.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.EPADResources;

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

	public static void dcmsnd(File inputDirFile, boolean throwException) throws Exception
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileWriter tagFileWriter = null;

		try {
			EPADConfig epadConfig = EPADConfig.getInstance();
			String aeTitle = epadConfig.getStringPropertyValue("DicomServerAETitle");
			String dsIP = epadConfig.getStringPropertyValue("DicomServerIP");
			String dsPort = epadConfig.getStringPropertyValue("DicomServerPort");
			String dcmServerTitlePort = aeTitle + "@" + dsIP + ":" + dsPort;
			dcmServerTitlePort = dcmServerTitlePort.trim();

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
			log.info("./dcmsnd: sending " + nbFiles + " file(s) - command: ./dcmsnd " + dcmServerTitlePort + " " + dirPath);

			String[] command = { "./dcmsnd", dcmServerTitlePort, dirPath };

			ProcessBuilder processBuilder = new ProcessBuilder(command);
			String dicomScriptsDir = EPADResources.getEPADWebServerDICOMBinDir();
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

			try { // Wait to get exit value
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
			close(tagFileWriter);
			close(br);
			close(isr);
			close(is);
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
		String logDirectory = EPADResources.getEPADWebServerLogDir();
		String fileName = logDirectory + "upload_" + System.currentTimeMillis() + ".log";
		EPADFileUtils.write(new File(fileName), contents);
	}

	private static void close(Writer writer)
	{
		try {
			if (writer != null) {
				writer.flush();
				writer.close();
				writer = null;
			}
		} catch (Exception e) {
			log.warning("Failed to close writer", e);
		}
	}

	private static void close(Reader reader)
	{
		try {
			if (reader != null) {
				reader.close();
				reader = null;
			}
		} catch (Exception e) {
			log.warning("Failed to close reader", e);
		}
	}

	private static void close(InputStream stream)
	{
		try {
			if (stream != null) {
				stream.close();
				stream = null;
			}
		} catch (Exception e) {
			log.warning("Failed to close stream", e);
		}
	}

	public static void deleteDicomStudy(String uid) throws Exception
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			log.info("Deleting study " + uid + " files - command: ./dcmdeleteStudy " + uid);

			String[] command = { "./dcmdeleteStudy", uid };

			ProcessBuilder pb = new ProcessBuilder(command);
			String myScriptsBinDirectory = EPADResources.getEPADWebServerMyScriptsDir();
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
				int exitValue = process.waitFor(); // keep.
				log.info("DICOM delete study exit value is: " + exitValue);
			} catch (Exception e) {
				log.warning("Didn't delete DICOM files in: " + uid, e);
			}
			String cmdLineOutput = sb.toString();
			writeDeleteLog(cmdLineOutput);

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}
		} catch (Exception e) {
			log.warning("Didn't delete DICOM files in: " + uid, e);
		}
	}

	/**
	 * Delete from DCM4CHEE
	 * 
	 * @param uid
	 * @throws Exception
	 */
	public static void deleteSeries(String uid) throws Exception
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			log.info("Deleting series " + uid + " files - command: ./dcmdeleteSeries " + uid);

			String[] command = { "./dcmdeleteSeries", uid };

			ProcessBuilder pb = new ProcessBuilder(command);
			String myScriptsDirectory = EPADResources.getEPADWebServerMyScriptsDir();
			pb.directory(new File(myScriptsDirectory));

			Process process = pb.start();
			process.getOutputStream();// get the output stream.
			// Read out dir output
			is = process.getInputStream();
			isr = new InputStreamReader(is);

			br = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			try {
				// int exitValue = process.waitFor(); //keep.
				// long totalTime = System.currentTimeMillis() - startTime;
				// log.info("Tags exit value is: " + exitValue+" and took: "+totalTime+" ms");
			} catch (Exception e) {
				log.warning("Didn't delete DICOM files in: " + uid, e);
			}

			String cmdLineOutput = sb.toString();
			writeDeleteLog(cmdLineOutput);

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}
		} catch (Exception e) {
			log.warning("Didn't delete dicom files in: " + uid, e);
		}
	}

	/**
	 * Log the result of this delete to the log directory.
	 * 
	 * @param contents String
	 */
	private static void writeDeleteLog(String contents)
	{
		String logDirectory = EPADResources.getEPADWebServerLogDir();
		String fileName = logDirectory + "delete_" + System.currentTimeMillis() + ".log";
		EPADFileUtils.write(new File(fileName), contents);
	}
}
