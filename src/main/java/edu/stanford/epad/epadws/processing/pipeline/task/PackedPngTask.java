/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import edu.stanford.epad.common.dicom.DicomReader;
import edu.stanford.epad.common.util.EPADLogger;

/**
 * Creates a packed PNG file based. DicomReader code is from Bradley Ross.
 * 
 * @see edu.stanford.epad.common.dicom.DicomReader#main
 */
public class PackedPngTask implements Callable<File>
{
	static private final EPADLogger logger = EPADLogger.getInstance();

	private final File file;

	protected PackedPngTask(File f)
	{
		file = f;
	}

	@Override
	public File call() throws Exception
	{
		File inputFile = file;
		File outputFile = null;
		OutputStream outputStream = null;
		try {
			DicomReader instance = new DicomReader(inputFile);
			String pngFilePath = file.getAbsolutePath().replaceAll("\\.dcm", ".png");

			logger.info("Creating PNG file: " + pngFilePath);
			outputFile = new File(pngFilePath);
			outputStream = new FileOutputStream(outputFile);
			ImageIO.write(instance.getPackedImage(), "png", outputStream);
		} catch (FileNotFoundException e) {
			logger.warning("failed to create packed PNG for: " + file.getAbsolutePath(), e);
		} catch (IOException e) {
			logger.warning("failed to create packed PNG for: " + file.getAbsolutePath(), e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (Exception e) {
					logger.warning("Error closing packed PNG output stream", e);
				}
			}
		}
		return outputFile;
	}
}
