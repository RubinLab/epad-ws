/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.pipeline.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.EPADResources;
import edu.stanford.isis.epadws.processing.model.DicomUploadPipelineFiles;

public class ReadDicomTagsTask implements Callable<File>
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final DicomUploadPipelineFiles pipeline = DicomUploadPipelineFiles.getInstance();

	private final File dicomFile;

	public ReadDicomTagsTask(File dicomFile)
	{
		this.dicomFile = dicomFile;
	}

	@Override
	public File call() throws Exception
	{
		try {
			if (!EPADFileUtils.isFileType(dicomFile, ".dcm")) {
				log.info(dicomFile + " is NOT a DICOM file.");
				return null;
			}

			String tagPath = createTagFilePath(dicomFile);
			String[] command = { "./dcm2txt", "-w", "120", dicomFile.getAbsolutePath() };
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			String dicomBinDirectory = EPADResources.getEPADWebServerDICOMBinDir();

			processBuilder.directory(new File(dicomBinDirectory));

			Process process = processBuilder.start();
			process.getOutputStream();// get the output stream.
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			BufferedReader br = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}

			try { // Wait to get exit value
				process.waitFor(); // keep.
				// long totalTime = System.currentTimeMillis() - startTime;
				// log.info("Tags exit value is: " + exitValue+" and took: "+totalTime+" ms");
			} catch (InterruptedException e) {
				log.warning("Could not generate tag file for: " + dicomFile.getAbsolutePath(), e);
			}

			// Write the contents of this buffer to a file.
			File tagFile = new File(tagPath);
			FileWriter tagFileWriter = new FileWriter(tagFile);
			tagFileWriter.write(sb.toString());
			tagFileWriter.flush();
			tagFileWriter.close();
			return dicomFile;
		} catch (Exception e) {
			pipeline.addErrorFile(dicomFile, "ReadTagsTask error.", e);
			return null;
		}
	}

	/**
	 * replace *.dcm with *.tag in the file path. If no *.dcm file is there then just add it.
	 * 
	 * @param dcmFile File
	 * @return String
	 */
	private static String createTagFilePath(File dcmFile)
	{
		String dcmFilePath = dcmFile.getAbsolutePath();
		if (dcmFilePath.endsWith("dcm")) {
			return dcmFilePath.replaceAll("\\.dcm", "\\.tag");
		}
		if (dcmFilePath.endsWith("DCM")) {
			return dcmFilePath.replaceAll("\\.DCM", "\\.tag");
		}
		return dcmFilePath + ".tag";
	}
}
