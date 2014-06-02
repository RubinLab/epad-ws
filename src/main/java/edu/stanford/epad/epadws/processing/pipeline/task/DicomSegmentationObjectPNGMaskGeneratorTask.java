package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.File;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.hakan.aim3api.base.AimException;

/**
 * This task generates a DICOM Segmentation Object
 */
public class DicomSegmentationObjectPNGMaskGeneratorTask implements GeneratorTask
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final String seriesUID;
	private final File dsoFile;
	private final File segObjectOutputFile;

	public DicomSegmentationObjectPNGMaskGeneratorTask(String seriesUID, File dsoFile, File segObjectOutputFile)
	{
		this.seriesUID = seriesUID;
		this.dsoFile = dsoFile;
		this.segObjectOutputFile = segObjectOutputFile;
	}

	@Override
	public void run()
	{
		log.info("Processing DSO for series  " + seriesUID + "; file=" + dsoFile.getAbsolutePath());

		try {
			DSOUtil.writeDSOMaskPNGs(seriesUID, dsoFile);

			AIMUtil.generateAIMFileForDSO(dsoFile);
		} catch (AimException e) {
			log.warning("Error writing AIM file for DSO series " + seriesUID, e);
		}
	}

	@Override
	public File getDSOFile()
	{
		return dsoFile;
	}

	@Override
	public String getTagFilePath()
	{
		// ToDo: verify that path here. It might be in
		return segObjectOutputFile.getAbsolutePath().replaceAll("\\.png", ".tag");
	}

	@Override
	public String getTaskType()
	{
		return "DicomSegmentationObject";
	}

	@Override
	public String getSeriesUID()
	{
		return this.seriesUID;
	}
}
