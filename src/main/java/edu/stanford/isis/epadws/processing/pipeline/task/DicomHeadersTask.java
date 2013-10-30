package edu.stanford.isis.epadws.processing.pipeline.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.ResourceUtils;

/**
 * @author amsnyder
 */
public class DicomHeadersTask implements Runnable
{
	private static final EPADLogger logger = EPADLogger.getInstance();
	private final File dicomInputFile;
	private final File outputFile;

	public DicomHeadersTask(File dicomInputFile, File outputFile)
	{
		this.dicomInputFile = dicomInputFile;
		this.outputFile = outputFile;
	}

	@Override
	public void run()
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileWriter tagFileWriter = null;
		Process process = null;

		try {
			String[] command = { "./dcm2txt", "-w", "250", "-l", "250", dicomInputFile.getAbsolutePath() };

			ProcessBuilder pb = new ProcessBuilder(command);
			String dicomBinDir = ResourceUtils.getEPADWebServerDICOMBinDir();
			pb.directory(new File(dicomBinDir));

			process = pb.start();
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
				process.waitFor();
			} catch (InterruptedException e) {
				logger.warning("Couldn't get tags for: " + dicomInputFile.getAbsolutePath(), e);
			}

			boolean created = EPADFileUtils.createDirsAndFile(outputFile);
			if (created) {
				logger.info("DICOMHeadersTask, using temporary file: " + outputFile.getAbsolutePath());
			}

			File tagFile = outputFile;
			tagFileWriter = new FileWriter(tagFile);
			tagFileWriter.write(sb.toString());
			// tagFileWriter.flush();
			// tagFileWriter.close();

		} catch (Exception e) {
			logger.warning("DicomHeadersTask failed to create DICOM tags file:" + e.getMessage());
		} catch (OutOfMemoryError oome) {
			logger.warning("DicomHeadersTask OutOfMemoryError: ", oome);
		} finally {
			close(tagFileWriter);
			close(br);
			close(isr);
			close(is);
			if (process != null)
				process.destroy();
		}
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
}
