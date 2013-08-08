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
 * Given a specific input and output file generate the png file.
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
		writePackedPngs();
	}

	private void writePackedPngs()
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();

		File inputFile = dicomInputFile;
		File outputFile = pngOutputFile;
		OutputStream outputStream = null;
		Map<String, String> epadFilesTable = new HashMap<String, String>();
		try {
			DicomReader instance = new DicomReader(inputFile);
			String pngFilePath = outputFile.getAbsolutePath();
			epadFilesTable = DcmDbUtils.createEPadFilesTableData(outputFile);
			outputFile = new File(pngFilePath); // Create the real file name here.
			logger.info("PngGeneratorTask: creating png file: " + outputFile.getAbsolutePath());

			boolean created = ProxyFileUtils.createDirsAndFile(outputFile); // Create the file
			if (created)
				logger.info("Using file: " + outputFile.getAbsolutePath());

			outputStream = new FileOutputStream(outputFile);
			ImageIO.write(instance.getMyPackedImage(), "png", outputStream);

			logger.info("Finished writing PNG file: " + outputFile);
			epadFilesTable = DcmDbUtils.createEPadFilesTableData(outputFile);
			int fileSize = getFileSize(epadFilesTable);
			queries.updateEpadFile(epadFilesTable.get("file_path"), PngStatus.DONE, fileSize, "");
		} catch (FileNotFoundException e) {
			logger.warning("failed to create packed png for: " + inputFile.getAbsolutePath(), e);
			queries.updateEpadFile(epadFilesTable.get("file_path"), PngStatus.ERROR, 0, "Dicom file not found.");
		} catch (IOException e) {
			logger.warning("failed to create packed PNG for: " + inputFile.getAbsolutePath(), e);
			queries.updateEpadFile(epadFilesTable.get("file_path"), PngStatus.ERROR, 0, "IO Error.");
		} catch (Exception e) {
			logger.warning("FAILED to create packed PNG for: " + inputFile.getAbsolutePath(), e);
			queries.updateEpadFile(epadFilesTable.get("file_path"), PngStatus.ERROR, 0,
					"General Exception: " + e.getMessage());
		} finally {
			if (inputFile.getName().endsWith(".tmp")) {
				boolean res = inputFile.delete();
				logger.info("deletion of input temp dicom File : " + res);
			}
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
					outputStream = null;
				} catch (Exception e) {
					logger.warning("Failed to close outputStream.", e);
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
