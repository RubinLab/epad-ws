package edu.stanford.isis.epadws.processing.pipeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import edu.stanford.isis.epad.common.ProxyFileUtils;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epadws.processing.model.PngStatus;
import edu.stanford.isis.epadws.processing.mysql.DcmDbUtils;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;
import edu.stanford.isis.epadws.server.managers.support.DicomReader;

/**
 * Generate a PNG file from a DICOM file.
 * 
 * @author amsnyder
 */
public class PngGeneratorTask implements GeneratorTask
{
	private static ProxyLogger logger = ProxyLogger.getInstance();
	private final File dicomInputFile;
	private final File pngOutputFile;

	public PngGeneratorTask(File dicomInputFile, File pngOutputFile)
	{
		this.dicomInputFile = dicomInputFile;
		this.pngOutputFile = pngOutputFile;
	}

	@Override
	public void run()
	{
		writePackedPNGs();
	}

	private void writePackedPNGs()
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		File inputDICOMFile = dicomInputFile;
		File outputPNGFile = pngOutputFile;
		OutputStream outputPNGStream = null;
		Map<String, String> epadFilesTable = new HashMap<String, String>();

		try {
			DicomReader instance = new DicomReader(inputDICOMFile);
			String pngFilePath = outputPNGFile.getAbsolutePath();
			epadFilesTable = DcmDbUtils.createEPadFilesTableData(outputPNGFile);
			outputPNGFile = new File(pngFilePath);

			logger.info("PngGeneratorTask: creating PNG file: " + outputPNGFile.getAbsolutePath());

			boolean created = ProxyFileUtils.createDirsAndFile(outputPNGFile); // Create the file
			if (created)
				logger.info("Using file: " + outputPNGFile.getAbsolutePath());

			outputPNGStream = new FileOutputStream(outputPNGFile);
			ImageIO.write(instance.getMyPackedImage(), "png", outputPNGStream);

			logger.info("Finished writing PNG file: " + outputPNGFile);
			epadFilesTable = DcmDbUtils.createEPadFilesTableData(outputPNGFile);
			queries.updateEpadFile(epadFilesTable.get("file_path"), PngStatus.DONE, getFileSize(epadFilesTable), "");
		} catch (FileNotFoundException e) {
			logger.warning("Failed to create packed PNG for: " + inputDICOMFile.getAbsolutePath(), e);
			queries.updateEpadFile(epadFilesTable.get("file_path"), PngStatus.ERROR, 0, "Dicom file not found.");
		} catch (IOException e) {
			logger.warning("Failed to create packed PNG for: " + inputDICOMFile.getAbsolutePath(), e);
			queries.updateEpadFile(epadFilesTable.get("file_path"), PngStatus.ERROR, 0, "IO Error.");
		} catch (Exception e) {
			logger.warning("Failed to create packed PNG for: " + inputDICOMFile.getAbsolutePath(), e);
			queries.updateEpadFile(epadFilesTable.get("file_path"), PngStatus.ERROR, 0,
					"General Exception: " + e.getMessage());
		} finally {
			if (inputDICOMFile.getName().endsWith(".tmp")) {
				boolean res = inputDICOMFile.delete();
				logger.info("Deleted temporary DICOM file : " + res);
			}
			if (outputPNGStream != null) {
				try {
					outputPNGStream.flush();
					outputPNGStream.close();
					outputPNGStream = null;
				} catch (Exception e) {
					logger.warning("Failed to close output stream", e);
				}
			}
		}
	}

	private int getFileSize(Map<String, String> epadFilesTable)
	{
		try {
			String fileSize = epadFilesTable.get("file_size");
			return Integer.parseInt(fileSize);
		} catch (Exception e) {
			logger.warning("Failed to get file.", e);
			return 0;
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("PngGeneratorTask[").append(" in=").append(dicomInputFile);
		sb.append(" out=").append(pngOutputFile).append("]");

		return sb.toString();
	}

	@Override
	public File getInputFile()
	{
		return dicomInputFile;
	}

	@Override
	public String getTagFilePath()
	{
		return pngOutputFile.getAbsolutePath().replaceAll("\\.png", ".tag");
	}

	@Override
	public String getTaskType()
	{
		return "Png";
	}
}
