package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.File;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;

public class DSOMaskPNGGeneratorTask implements GeneratorTask
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final String seriesUID;
	private final File dsoFile;
	private final boolean generateAIM;
	private final String tagFilePath;

	public DSOMaskPNGGeneratorTask(String seriesUID, File dsoFile, boolean generateAIM, String tagFilePath)
	{
		this.seriesUID = seriesUID;
		this.dsoFile = dsoFile;
		this.generateAIM = generateAIM;
		this.tagFilePath = tagFilePath;
	}

	@Override
	public void run()
	{
		log.info("Processing DSO for series  " + seriesUID + "; file=" + dsoFile.getAbsolutePath());

		try {
			DSOUtil.writeDSOMaskPNGs(dsoFile);
			if (generateAIM)
			{
				// Must be first upload, create AIM file
				AIMUtil.generateAIMFileForDSO(dsoFile);
			}
		} catch (Exception e) {
			log.warning("Error writing AIM file for DSO series " + seriesUID, e);
		}
	}

	@Override
	public File getDICOMFile()
	{
		return dsoFile;
	}

	@Override
	public String getTagFilePath()
	{
		return tagFilePath;
	}

	@Override
	public String getTaskType()
	{
		return "DSOPNGMaskGeneration";
	}

	@Override
	public String getSeriesUID()
	{
		return this.seriesUID;
	}
}
