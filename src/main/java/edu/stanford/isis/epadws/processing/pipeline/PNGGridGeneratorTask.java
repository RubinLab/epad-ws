package edu.stanford.isis.epadws.processing.pipeline;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.util.ProxyFileUtils;
import edu.stanford.isis.epadws.processing.model.PngStatus;
import edu.stanford.isis.epadws.processing.mysql.DcmDbUtils;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;
import edu.stanford.isis.epadws.server.managers.support.PNGGridGenerator;

/**
 * Given a set of PNG files, generate a single PNG containing a grid of these files.
 * 
 * @author martin
 */
public class PNGGridGeneratorTask implements GeneratorTask
{
	private final File pngInputFile;
	private final List<File> inputPNGGridFiles;
	private final File outputPNGFile;

	private static final int INSET_IMAGE_SIZE = 512;
	private static final int IMAGES_PER_AXIS = 4;
	private static ProxyLogger logger = ProxyLogger.getInstance();

	public PNGGridGeneratorTask(File pngInputFile, List<File> inputPNGGridFiles, File outputPNGFile)
	{
		this.pngInputFile = pngInputFile;
		this.inputPNGGridFiles = new ArrayList<File>(inputPNGGridFiles);
		this.outputPNGFile = outputPNGFile;
	}

	@Override
	public void run()
	{
		writePNGGridFile();
	}

	private void writePNGGridFile()
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		Map<String, String> epadFilesTable = new HashMap<String, String>();
		try {
			logger.info("PNGGridGeneratorTask: creating PNG grid file: " + outputPNGFile.getAbsolutePath());
			epadFilesTable = DcmDbUtils.createEPadFilesTableData(outputPNGFile);

			boolean created = ProxyFileUtils.createDirsAndFile(outputPNGFile); // Create the file
			if (created)
				logger.info("Using file: " + outputPNGFile.getAbsolutePath());

			boolean success = PNGGridGenerator.createPNGGridFile(inputPNGGridFiles, outputPNGFile, INSET_IMAGE_SIZE,
					IMAGES_PER_AXIS, 0, false);

			if (success) {
				logger.info("Finished writing PNG grid file: " + outputPNGFile);
				int fileSize = getFileSize(epadFilesTable);
				queries.updateEpadFile(epadFilesTable.get("file_path"), PngStatus.DONE, fileSize, "");
			} else {
				logger.info("Failed to create grid PNG file: " + outputPNGFile.getAbsolutePath());
				queries.updateEpadFile(epadFilesTable.get("file_path"), PngStatus.ERROR, 0, "Error generating grid");
			}
		} catch (Exception e) {
			logger.warning("Failed to create grid PNG file: " + outputPNGFile.getAbsolutePath(), e);
			queries.updateEpadFile(epadFilesTable.get("file_path"), PngStatus.ERROR, 0,
					"General Exception: " + e.getMessage());
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
		sb.append("PNGGridGeneratorTask[").append(" in=").append(pngInputFile).append(" grid files=");
		for (File file : inputPNGGridFiles)
			sb.append(" " + file.getName());
		sb.append(" out=").append(outputPNGFile).append("]");

		return sb.toString();
	}

	@Override
	public File getInputFile()
	{
		return pngInputFile;
	}

	@Override
	public String getTagFilePath()
	{
		return outputPNGFile.getAbsolutePath().replaceAll("\\.png", ".tag");
	}

	@Override
	public String getTaskType()
	{
		return "PNGGrid";
	}
}
