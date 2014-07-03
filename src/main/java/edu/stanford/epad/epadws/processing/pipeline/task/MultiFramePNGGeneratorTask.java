package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.File;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;

public class MultiFramePNGGeneratorTask implements GeneratorTask
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final String seriesUID;
	private final File multiFrameDICOMFile;

	public MultiFramePNGGeneratorTask(String seriesUID, File multiFrameDICOMFile)
	{
		this.seriesUID = seriesUID;
		this.multiFrameDICOMFile = multiFrameDICOMFile;
	}

	@Override
	public void run()
	{
		log.info("Processing multi-frame DICOM for series  " + seriesUID + "; file="
				+ multiFrameDICOMFile.getAbsolutePath());

		try {
			DSOUtil.writeMultiFramePNGs(multiFrameDICOMFile);
		} catch (Exception e) {
			log.warning("Error writing AIM file for multi-frame series " + seriesUID, e);
		}
	}

	@Override
	public File getDICOMFile()
	{
		return multiFrameDICOMFile;
	}

	@Override
	public String getTagFilePath()
	{
		return ""; // TODO
	}

	@Override
	public String getTaskType()
	{
		return "DSOPNGGeneration";
	}

	@Override
	public String getSeriesUID()
	{
		return this.seriesUID;
	}
}
