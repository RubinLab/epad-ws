package edu.stanford.isis.epadws.epaddb;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.EPADResources;
import edu.stanford.isis.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.isis.epadws.processing.model.PNGGridGenerator;
import edu.stanford.isis.epadws.processing.model.PngProcessingStatus;
import edu.stanford.isis.epadws.queries.EpadQueries;

/**
 * Operations on files maintained by ePAD
 * 
 * 
 * @author martin
 */
public class FileOperations
{
	private static EPADLogger log = EPADLogger.getInstance();
	private static final int INSET_IMAGE_SIZE = 512;
	private static final int IMAGES_PER_AXIS = 4;

	/**
	 * Delete PNGs
	 * 
	 * @param uid
	 * @throws Exception
	 */
	public static void deletePNGsforDicomStudy(String studyUID) throws Exception
	{
		StringBuilder outputPath = new StringBuilder();
		outputPath.append(EPADResources.getEPADWebServerPNGDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("");

		File dirToDelete = new File(outputPath.toString());
		boolean success = delete(dirToDelete);

		log.info("Deleting the PNG for study at " + outputPath.toString() + " success = " + success);
	}

	public static void writePNGGridFile(File pngInputFile, List<File> inputPNGGridFiles, File outputPNGFile)
	{
		EpadQueries databaseOperations = EpadDatabase.getInstance().getDatabaseOperations();
		Map<String, String> epadFilesTable = new HashMap<String, String>();
		try {
			log.info("PNGGridGeneratorTask: creating PNG grid file: " + outputPNGFile.getAbsolutePath());
			epadFilesTable = Dcm4CheeDatabaseUtils.createEPadFilesTableData(outputPNGFile);

			EPADFileUtils.createDirsAndFile(outputPNGFile);

			boolean success = PNGGridGenerator.createPNGGridFile(inputPNGGridFiles, outputPNGFile, INSET_IMAGE_SIZE,
					IMAGES_PER_AXIS, 0, false);

			if (success) {
				log.info("Finished writing PNG grid file: " + outputPNGFile);
				int fileSize = getFileSize(epadFilesTable);
				databaseOperations.updateEpadFile(epadFilesTable.get("file_path"), PngProcessingStatus.DONE, fileSize, "");
			} else {
				log.info("Failed to create grid PNG file: " + outputPNGFile.getAbsolutePath());
				databaseOperations.updateEpadFile(epadFilesTable.get("file_path"), PngProcessingStatus.ERROR, 0,
						"Error generating grid");
			}
		} catch (Exception e) {
			log.warning("Failed to create grid PNG file: " + outputPNGFile.getAbsolutePath(), e);
			databaseOperations.updateEpadFile(epadFilesTable.get("file_path"), PngProcessingStatus.ERROR, 0,
					"General Exception: " + e.getMessage());
		}
	}

	/**
	 * Delete PNGs
	 * 
	 * @param uid
	 * @throws Exception
	 */
	public static void deletePNGforSeries(String seriesUID) throws Exception
	{
		EpadQueries databaseOperations = EpadDatabase.getInstance().getDatabaseOperations();
		String studyUID = databaseOperations.getDicomStudyUIDForSeries(seriesUID);
		StringBuilder outputPath = new StringBuilder();
		outputPath.append(EPADResources.getEPADWebServerPNGDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(seriesUID)).append("/");

		File dirToDelete = new File(outputPath.toString());
		boolean success = delete(dirToDelete);

		log.info("Deleting the PNG for series at " + outputPath.toString() + " success = " + success);
	}

	private static boolean delete(File file) throws IOException
	{
		boolean success = false;
		if (file.isDirectory()) {
			if (file.list().length == 0) {
				success = file.delete();
			} else {
				String files[] = file.list();
				for (String temp : files) {
					File fileDelete = new File(file, temp);
					delete(fileDelete);
				}
				if (file.list().length == 0) { // Check the directory again; if empty then delete it.
					success = file.delete();
				}
			}
		} else {
			success = file.delete();
		}
		return success;
	}

	private static int getFileSize(Map<String, String> epadFilesTable)
	{
		try {
			String fileSize = epadFilesTable.get("file_size");
			return Integer.parseInt(fileSize);
		} catch (Exception e) {
			log.warning("Failed to get file.", e);
			return 0;
		}
	}
}
