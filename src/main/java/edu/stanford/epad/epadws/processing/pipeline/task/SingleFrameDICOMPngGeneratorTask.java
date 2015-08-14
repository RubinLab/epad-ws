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
package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.common.dicom.DicomReader;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.service.UserProjectService;

public class SingleFrameDICOMPngGeneratorTask implements GeneratorTask
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final String patientName;
	private final String studyUID;
	private final String seriesUID;
	private final String imageUID;
	private final int instanceNumber;
	private final File dicomFile;
	private final File pngFile;
	
	static public Set imagesBeingProcessed = Collections.synchronizedSet(new HashSet());

	public SingleFrameDICOMPngGeneratorTask(String patientName, DICOMFileDescription dicomFileDescription,
			File dicomFile, File pngFile)
	{
		this.patientName = patientName;
		this.studyUID = dicomFileDescription.studyUID;
		this.seriesUID = dicomFileDescription.seriesUID;
		this.imageUID = dicomFileDescription.imageUID;
		this.instanceNumber = dicomFileDescription.instanceNumber;
		this.dicomFile = dicomFile;
		this.pngFile = pngFile;
	}

	@Override
	public String getSeriesUID()
	{
		return this.seriesUID;
	}

	@Override
	public void run()
	{
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // Let interactive thread run sooner
		if (imagesBeingProcessed.contains(imageUID))
		{
			log.info("Image " + imageUID + " already being processed");
			return;
		}
		generatePNGs();
	}

	private void generatePNGs()
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		File inputDICOMFile = dicomFile;
		File outputPNGFile = pngFile;
		Map<String, String> epadFilesRow = new HashMap<String, String>();
		OutputStream outputPNGStream = null;

		try {
			imagesBeingProcessed.add(imageUID);
			if (UserProjectService.pendingUploads.containsKey(studyUID))
			{
				String username = UserProjectService.pendingUploads.get(studyUID);
				if (username != null && username.indexOf(":") != -1)
					username = username.substring(0, username.indexOf(":"));
				if (username != null)
				{
					epadDatabaseOperations.insertEpadEvent(
							username, 
							"Study Upload Complete", 
							"", "", "", "", "", "", 
							"Study:" + studyUID);					
					UserProjectService.pendingUploads.remove(studyUID);
				}
			}
			DicomReader instance = new DicomReader(inputDICOMFile);
			String pngFilePath = outputPNGFile.getAbsolutePath();
			outputPNGFile = new File(pngFilePath);

			EPADFileUtils.createDirsAndFile(outputPNGFile);
			try {
				outputPNGStream = new FileOutputStream(outputPNGFile);
				ImageIO.write(instance.getPackedImage(), "png", outputPNGStream);
				outputPNGStream.close();
			} catch (Exception x) {
				// Try second method using pixelmed library
				log.warning("dcm4che failed to create PNG for instance " + instanceNumber + " in series " + seriesUID + " for patient "
						+ patientName + ", trying pixelmed", x);
				outputPNGFile.delete();
				instance.dcmconvpng3(0, outputPNGFile);
			}
			epadFilesRow = Dcm4CheeDatabaseUtils.createEPadFilesRowData(outputPNGFile.getAbsolutePath(),
					outputPNGFile.length(), imageUID);
			log.info("PNG of size " + getFileSize(epadFilesRow) + " generated for instance " + instanceNumber + " in series "
					+ seriesUID + ", study " + studyUID + " for patient " + patientName);

			epadDatabaseOperations.updateEpadFileRow(epadFilesRow.get("file_path"), PNGFileProcessingStatus.DONE,
					getFileSize(epadFilesRow), "");
		} catch (FileNotFoundException e) {
			log.warning("Failed to create PNG for instance " + instanceNumber + " in series " + seriesUID + " for patient "
					+ patientName, e);
			epadDatabaseOperations.updateEpadFileRow(epadFilesRow.get("file_path"), PNGFileProcessingStatus.ERROR, 0,
					"DICOM file not found.");
			epadDatabaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.ERROR);
		} catch (IOException e) {
			log.warning("Failed to create PNG for instance " + instanceNumber + " in series " + seriesUID + " for patient "
					+ patientName, e);
			epadDatabaseOperations.updateEpadFileRow(epadFilesRow.get("file_path"), PNGFileProcessingStatus.ERROR, 0,
					"IO Error: " + e.getMessage());
			epadDatabaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.ERROR);
		} catch (Throwable t) {
			log.warning("Failed to create PNG for instance " + instanceNumber + " in series " + seriesUID + " for patient "
					+ patientName, t);
			epadDatabaseOperations.updateEpadFileRow(epadFilesRow.get("file_path"), PNGFileProcessingStatus.ERROR, 0,
					"General Exception: " + t.getMessage());
			epadDatabaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.ERROR);
		} finally {
			imagesBeingProcessed.remove(imageUID);
			IOUtils.closeQuietly(outputPNGStream);
			if (inputDICOMFile.getName().endsWith(".tmp")) {
				inputDICOMFile.delete();
			}
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("PngGeneratorTask[").append(" in=").append(dicomFile);
		sb.append(" out=").append(pngFile).append("]");

		return sb.toString();
	}

	@Override
	public File getDICOMFile()
	{
		return dicomFile;
	}

	@Override
	public String getTagFilePath()
	{
		return pngFile.getAbsolutePath().replaceAll("\\.png", ".tag");
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
			log.warning("Warning: failed to get file", e);
			return 0;
		}
	}
}
