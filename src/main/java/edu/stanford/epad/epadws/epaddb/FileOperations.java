package edu.stanford.epad.epadws.epaddb;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.epad.common.dicom.DicomFormatUtil;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.EPADResources;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.epad.epadws.processing.model.PNGGridGenerator;
import edu.stanford.epad.epadws.processing.model.PngProcessingStatus;

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

	public static void deletePNGsForStudy(String studyUID)
	{
		StringBuilder outputPath = new StringBuilder();
		outputPath.append(EPADResources.getEPADWebServerPNGDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("");

		try {
			File dirToDelete = new File(outputPath.toString());
			boolean success = delete(dirToDelete);
			log.info("Deleted the PNGs for study " + studyUID + " at " + outputPath.toString() + "; success = " + success);
		} catch (IOException e) {
			log.warning("Error deleting the PNGs for study " + studyUID + " at " + outputPath.toString(), e);
		}
	}

	public static void deletePNGsForSeries(String studyUID, String seriesUID)
	{
		StringBuilder outputPath = new StringBuilder();
		outputPath.append(EPADResources.getEPADWebServerPNGDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("/" + DicomFormatUtil.formatUidToDir(seriesUID));

		try {
			File dirToDelete = new File(outputPath.toString());
			boolean success = delete(dirToDelete);
			log.info("Deleteed the PNGs for series " + seriesUID + " at " + outputPath.toString() + "; success = " + success);
		} catch (IOException e) {
			log.warning("Error deleting the PNGs for series " + seriesUID + " at " + outputPath.toString(), e);
		}
	}

	public static void writePNGGridFile(String seriesUID, File pngInputFile, List<File> inputPNGGridFiles,
			File outputPNGFile)
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		Map<String, String> epadFilesTable = new HashMap<String, String>();
		try {
			log.info("Creating PNG grid file " + outputPNGFile.getAbsolutePath() + " for series " + seriesUID);
			epadFilesTable = Dcm4CheeDatabaseUtils.createEPadFilesTableData(outputPNGFile);

			EPADFileUtils.createDirsAndFile(outputPNGFile);

			boolean success = PNGGridGenerator.createPNGGridFile(inputPNGGridFiles, outputPNGFile, INSET_IMAGE_SIZE,
					IMAGES_PER_AXIS, 0, false);

			if (success) {
				log.info("Finished writing PNG grid file " + outputPNGFile + " for series " + seriesUID);
				int fileSize = getFileSize(epadFilesTable);
				epadDatabaseOperations.updateEpadFileRecord(epadFilesTable.get("file_path"), PngProcessingStatus.DONE,
						fileSize, "");
			} else {
				log.info("Failed to create grid PNG file " + outputPNGFile.getAbsolutePath() + " for series " + seriesUID);
				epadDatabaseOperations.updateEpadFileRecord(epadFilesTable.get("file_path"), PngProcessingStatus.ERROR, 0,
						"Error generating grid");
			}
		} catch (Exception e) {
			log.warning("Failed to create grid PNG file " + outputPNGFile.getAbsolutePath() + " for series " + seriesUID, e);
			epadDatabaseOperations.updateEpadFileRecord(epadFilesTable.get("file_path"), PngProcessingStatus.ERROR, 0,
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
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		String studyUID = dcm4CheeDatabaseOperations.getStudyUIDForSeries(seriesUID);
		StringBuilder outputPath = new StringBuilder();
		outputPath.append(EPADResources.getEPADWebServerPNGDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(seriesUID)).append("/");

		File dirToDelete = new File(outputPath.toString());
		boolean success = delete(dirToDelete);

		log.info("Deleting PNGs for series " + seriesUID + " at " + outputPath.toString() + "; success = " + success);
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
			log.warning("Failed to get file", e);
			return 0;
		}
	}
}
