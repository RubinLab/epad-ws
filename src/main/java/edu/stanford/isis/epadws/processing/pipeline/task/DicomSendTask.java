package edu.stanford.isis.epadws.processing.pipeline.task;

import java.io.File;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.persistence.Dcm4CheeOperations;

public class DicomSendTask implements Runnable
{
	private static EPADLogger logger = EPADLogger.getInstance();

	private final File inputDir;

	public DicomSendTask(File inputDir)
	{
		this.inputDir = inputDir;
	}

	@Override
	public void run()
	{
		try {
			Dcm4CheeOperations.dcmsnd(inputDir, false);
		} catch (Exception e) {
			logger.warning("run had: " + e.getMessage(), e);
		}
	}
}
