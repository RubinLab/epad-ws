//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.epaddb;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.epad.epadws.processing.model.PNGGridGenerator;

/**
 * Operations on PNG files maintained by ePAD
 * 
 * 
 * @author martin
 */
public class PNGFilesOperations
{
	private static EPADLogger log = EPADLogger.getInstance();

	private static final int INSET_IMAGE_SIZE = 512;
	private static final int IMAGES_PER_AXIS = 4;

	public static void deletePNGsForStudy(String studyUID)
	{
		StringBuilder outputPath = new StringBuilder();
		outputPath.append(EPADConfig.getEPADWebServerPNGDir());
		outputPath.append("/studies/" + studyUID);

		try {
			File dirToDelete = new File(outputPath.toString());
			boolean success = deleteFile(dirToDelete);
			log.info("Deleted the PNGs for study " + studyUID + " at " + outputPath.toString() + "; success = " + success);
		} catch (IOException e) {
			log.warning("Error deleting the PNGs for study " + studyUID + " at " + outputPath.toString(), e);
		}
	}

	public static void writePNGGridFile(String seriesUID, String imageUID, File pngInputFile,
			List<File> inputPNGGridFiles, File outputPNGFile)
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		Map<String, String> epadFilesTable = new HashMap<String, String>();
		try {
			log.info("Creating PNG grid file " + outputPNGFile.getAbsolutePath() + " for series " + seriesUID);
			epadFilesTable = Dcm4CheeDatabaseUtils.createEPadFilesRowData(outputPNGFile.getAbsolutePath(),
					outputPNGFile.length(), imageUID);

			EPADFileUtils.createDirsAndFile(outputPNGFile);

			boolean success = PNGGridGenerator.createPNGGridFile(inputPNGGridFiles, outputPNGFile, INSET_IMAGE_SIZE,
					IMAGES_PER_AXIS, 0, false);

			if (success) {
				log.info("Finished writing PNG grid file " + outputPNGFile + " for series " + seriesUID);
				int fileSize = getFileSize(epadFilesTable);
				epadDatabaseOperations.updateEpadFileRow(epadFilesTable.get("file_path"), PNGFileProcessingStatus.DONE,
						fileSize, "");
			} else {
				log.info("Failed to create grid PNG file " + outputPNGFile.getAbsolutePath() + " for series " + seriesUID);
				epadDatabaseOperations.updateEpadFileRow(epadFilesTable.get("file_path"), PNGFileProcessingStatus.ERROR, 0,
						"Error generating grid");
			}
		} catch (Exception e) {
			log.warning("Failed to create grid PNG file " + outputPNGFile.getAbsolutePath() + " for series " + seriesUID, e);
			epadDatabaseOperations.updateEpadFileRow(epadFilesTable.get("file_path"), PNGFileProcessingStatus.ERROR, 0,
					"General Exception: " + e.getMessage());
		}
	}

	private static boolean deleteFile(File file) throws IOException
	{
		boolean success = false;
		if (file.isDirectory()) {
			if (file.list().length == 0) {
				success = file.delete();
			} else {
				String files[] = file.list();
				for (String temp : files) {
					File fileDelete = new File(file, temp);
					deleteFile(fileDelete);
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
