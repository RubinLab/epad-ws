package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.File;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;

public class DSOPNGGeneratorTask implements GeneratorTask
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final String seriesUID;
	private final File dsoFile;

	public DSOPNGGeneratorTask(String seriesUID, File dsoFile)
	{
		this.seriesUID = seriesUID;
		this.dsoFile = dsoFile;
	}

	@Override
	public void run()
	{
		log.info("Processing DSO for series  " + seriesUID + "; file=" + dsoFile.getAbsolutePath());

		try {
			DSOUtil.writeDSOPNGs(dsoFile);
		} catch (Exception e) {
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
