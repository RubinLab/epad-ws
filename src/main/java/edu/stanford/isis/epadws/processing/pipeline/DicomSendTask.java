package edu.stanford.isis.epadws.processing.pipeline;

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
import edu.stanford.isis.epad.common.util.ResourceUtils;

/**
 * 
 * 
 * @author amsnyder
 */
public class DicomSendTask implements Runnable
{
	private static EPADLogger logger = EPADLogger.getInstance();

	private final File inputDir;

	public DicomSendTask(File inputDir)
	{
		this.inputDir = inputDir;
	}

	@Override
	public void run()
	{
		try {
			dcmsnd(inputDir, false);
		} catch (Exception e) {
			logger.warning("run had: " + e.getMessage(), e);
		}
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

	public static void dcmsnd(File inputDirFile, boolean throwException) throws Exception
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileWriter tagFileWriter = null;

		try {
			EPADConfig pc = EPADConfig.getInstance();
			String aeTitle = pc.getParam("DicomServerAETitle");
			String dsIP = pc.getParam("DicomServerIP");
			String dsPort = pc.getParam("DicomServerPort");

			String dcmServerTitlePort = aeTitle + "@" + dsIP + ":" + dsPort;
			dcmServerTitlePort = dcmServerTitlePort.trim();

			String dirPath = inputDirFile.getAbsolutePath();
			if (pathContainsSpaces(dirPath)) {
				dirPath = escapeSpacesInDirPath(dirPath);
			}

			File dir = new File(dirPath);
			int nbFiles = -1;
			if (dir != null) {
				String[] filePaths = dir.list();
				if (filePaths != null)
					nbFiles = filePaths.length;
			}
			logger.info("Sending " + nbFiles + " files - command: ./dcmsnd " + dcmServerTitlePort + " " + dirPath);

			String[] command = { "./dcmsnd", dcmServerTitlePort, dirPath };

			ProcessBuilder pb = new ProcessBuilder(command);
			String dicomScriptsDir = ResourceUtils.getEPADWebServerDICOMBinDir();
			logger.info("dicomScriptsDir: " + dicomScriptsDir);
			pb.directory(new File(dicomScriptsDir));

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

			try { // Wait to get exit value
				process.waitFor(); // keep.
				// long totalTime = System.currentTimeMillis() - startTime;
				// log.info("Tags exit value is: " + exitValue+" and took: "+totalTime+" ms");
			} catch (InterruptedException e) {
				logger.warning("Didn't send DICOM files in: " + inputDirFile.getAbsolutePath(), e);
			}
			String cmdLineOutput = sb.toString();
			writeUploadLog(cmdLineOutput);

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}
		} catch (Exception e) {
			if (e instanceof IllegalStateException && throwException) {
				throw e;
			}
			logger.warning("DicomHeadersTask failed to create DICOM tags file: " + e.getMessage());
			if (throwException) {
				throw new IllegalStateException("DicomHeadersTask failed to create DICom tags file.", e);
			}
		} catch (OutOfMemoryError oome) {
			logger.warning("DicomHeadersTask OutOfMemoryError: ", oome);
			if (throwException) {
				throw new IllegalStateException("DicomHeadersTask OutOfMemoryError: ", oome);
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
			logger.warning("DicomSendTask.parseError had: " + e.getMessage() + " for: " + output, e);
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
		String logDirectory = ResourceUtils.getEPADWebServerLogDir();
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
			logger.warning("Failed to close writer", e);
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
			logger.warning("Failed to close reader", e);
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
			logger.warning("Failed to close stream", e);
		}
	}

	/**
	 * For testing.
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		System.out.println("testing spaces in directory. ");
		String dirWithoutSpace = "/home/epad/cureData/9/9_40_456/";
		String dirWithSpace = "/home/epad/cureData/9/9 40 456/";

		if (pathContainsSpaces(dirWithoutSpace)) {
			logger.warning("TEST FAILED. pathContainsSpaces error");
		}

		if (pathContainsSpaces(dirWithSpace)) {
			String newDirPath = escapeSpacesInDirPath(dirWithSpace);
			logger.warning("Expected: /home/epad/cureData/9\\ 40\\ 456/  Actual: " + newDirPath);
		}
		logger.info("DONE.");
	}
}
