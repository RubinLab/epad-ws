package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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

public class SingleFrameDICOMPngGeneratorTask implements GeneratorTask
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final String patientName;
	private final String studyUID;
	private final String seriesUID;
	private final int instanceNumber;
	private final File dicomFile;
	private final File pngFile;

	public SingleFrameDICOMPngGeneratorTask(String patientName, DICOMFileDescription dicomFileDescription,
			File dicomFile, File pngFile)
	{
		this.patientName = patientName;
		this.studyUID = dicomFileDescription.studyUID;
		this.seriesUID = dicomFileDescription.seriesUID;
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
		generatePNGs();
	}

	private void generatePNGs()
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		File inputDICOMFile = dicomFile;
		File outputPNGFile = pngFile;
		Map<String, String> epadFilesTableData = new HashMap<String, String>();
		OutputStream outputPNGStream = null;

		try {
			DicomReader instance = new DicomReader(inputDICOMFile);
			String pngFilePath = outputPNGFile.getAbsolutePath();
			epadFilesTableData = Dcm4CheeDatabaseUtils.createEPadFilesTableData(outputPNGFile);
			outputPNGFile = new File(pngFilePath);

			EPADFileUtils.createDirsAndFile(outputPNGFile);
			outputPNGStream = new FileOutputStream(outputPNGFile);
			ImageIO.write(instance.getPackedImage(), "png", outputPNGStream);
			outputPNGStream.close();
			epadFilesTableData = Dcm4CheeDatabaseUtils.createEPadFilesTableData(outputPNGFile);
			log.info("PNG of size " + getFileSize(epadFilesTableData) + " generated for instance " + instanceNumber
					+ " in series " + seriesUID + ", study " + studyUID + " for patient " + patientName);

			epadDatabaseOperations.updateEpadFileRecord(epadFilesTableData.get("file_path"), PNGFileProcessingStatus.DONE,
					getFileSize(epadFilesTableData), "");
		} catch (FileNotFoundException e) {
			log.warning("Failed to create PNG for instance " + instanceNumber + " in series " + seriesUID + " for patient "
					+ patientName, e);
			epadDatabaseOperations.updateEpadFileRecord(epadFilesTableData.get("file_path"), PNGFileProcessingStatus.ERROR,
					0, "DICOM file not found.");
			epadDatabaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.ERROR);
		} catch (IOException e) {
			log.warning("Failed to create PNG for instance " + instanceNumber + " in series " + seriesUID + " for patient "
					+ patientName, e);
			epadDatabaseOperations.updateEpadFileRecord(epadFilesTableData.get("file_path"), PNGFileProcessingStatus.ERROR,
					0, "IO Error: " + e.getMessage());
			epadDatabaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.ERROR);
		} catch (Throwable t) {
			log.warning("Failed to create PNG for instance " + instanceNumber + " in series " + seriesUID + " for patient "
					+ patientName, t);
			epadDatabaseOperations.updateEpadFileRecord(epadFilesTableData.get("file_path"), PNGFileProcessingStatus.ERROR,
					0, "General Exception: " + t.getMessage());
			epadDatabaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.ERROR);
		} finally {
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
	public File getDSOFile()
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
