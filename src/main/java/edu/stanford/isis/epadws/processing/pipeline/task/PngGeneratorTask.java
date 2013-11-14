package edu.stanford.isis.epadws.processing.pipeline.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import edu.stanford.isis.epad.common.dicom.DicomReader;
import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.processing.model.PngProcessingStatus;
import edu.stanford.isis.epadws.processing.persistence.Dcm4CheeDatabaseUtils;
import edu.stanford.isis.epadws.processing.persistence.MySqlInstance;
import edu.stanford.isis.epadws.processing.persistence.MySqlQueries;

/**
 * Generate a PNG file from a DICOM file.
 * 
 * @author amsnyder
 */
public class PngGeneratorTask implements GeneratorTask
{
	private static final EPADLogger logger = EPADLogger.getInstance();

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
		Map<String, String> epadFilesTable = new HashMap<String, String>();
		OutputStream outputPNGStream = null;

		try {
			DicomReader instance = new DicomReader(inputDICOMFile);
			String pngFilePath = outputPNGFile.getAbsolutePath();
			epadFilesTable = Dcm4CheeDatabaseUtils.createEPadFilesTableData(outputPNGFile);
			outputPNGFile = new File(pngFilePath);

			// logger.info("PngGeneratorTask: creating PNG file: " + outputPNGFile.getAbsolutePath());

			EPADFileUtils.createDirsAndFile(outputPNGFile);

			outputPNGStream = new FileOutputStream(outputPNGFile);
			ImageIO.write(instance.getPackedImage(), "png", outputPNGStream);

			epadFilesTable = Dcm4CheeDatabaseUtils.createEPadFilesTableData(outputPNGFile);
			queries
					.updateEpadFile(epadFilesTable.get("file_path"), PngProcessingStatus.DONE, getFileSize(epadFilesTable), "");
		} catch (FileNotFoundException e) {
			logger.warning("Failed to create packed PNG for: " + inputDICOMFile.getAbsolutePath(), e);
			queries.updateEpadFile(epadFilesTable.get("file_path"), PngProcessingStatus.ERROR, 0, "Dicom file not found.");
		} catch (IOException e) {
			logger.warning("Failed to create packed PNG for: " + inputDICOMFile.getAbsolutePath(), e);
			queries.updateEpadFile(epadFilesTable.get("file_path"), PngProcessingStatus.ERROR, 0, "IO Error.");
		} catch (Exception e) {
			logger.warning("Failed to create packed PNG for: " + inputDICOMFile.getAbsolutePath(), e);
			queries.updateEpadFile(epadFilesTable.get("file_path"), PngProcessingStatus.ERROR, 0,
					"General Exception: " + e.getMessage());
		} finally {
			if (inputDICOMFile.getName().endsWith(".tmp")) {
				inputDICOMFile.delete();
			}
			if (outputPNGStream != null) {
				try {
					outputPNGStream.flush();
					outputPNGStream.close();
					outputPNGStream = null;
				} catch (Exception e) {
					logger.warning("Failed to close PNG output stream", e);
				}
			}
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
}
