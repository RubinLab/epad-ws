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
import edu.stanford.isis.epad.common.util.ResourceUtils;
import edu.stanford.isis.epadws.processing.pipeline.DicomUploadPipelineFiles;

public class ReadTagsTask implements Callable<File>
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final DicomUploadPipelineFiles pipeline = DicomUploadPipelineFiles.getInstance();

	private final File file;

	public ReadTagsTask(File file)
	{
		this.file = file;
	}

	@Override
	public File call() throws Exception
	{

		// rename this file to something with a lower extension right now.
		// file.renameTo(new File(file.getName().toLowerCase()));

		try {
			// only dicom files should pass here.
			if (!EPADFileUtils.isFileType(file, ".dcm")) {
				log.info(file + " is NOT a DICOM file.");
				return null;
			}

			String tagPath = createTagPath(file);

			String[] command = { "./dcm2txt", "-w", "120", file.getAbsolutePath() };

			ProcessBuilder pb = new ProcessBuilder(command);
			String dicomBinDirectory = ResourceUtils.getEPADWebServerDICOMBinDir();
			pb.directory(new File(dicomBinDirectory));

			// long startTime = System.currentTimeMillis();
			Process process = pb.start();
			process.getOutputStream();// get the output stream.
			// Read out dir output
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			BufferedReader br = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}

			// Wait to get exit value
			try {
				process.waitFor(); // keep.
				// long totalTime = System.currentTimeMillis() - startTime;
				// log.info("Tags exit value is: " + exitValue+" and took: "+totalTime+" ms");
			} catch (InterruptedException e) {
				log.warning("Couldn't get tags for: " + file.getAbsolutePath(), e);
			}

			// write the contents of this buffer to a file.
			File tagFile = new File(tagPath);
			FileWriter tagFileWriter = new FileWriter(tagFile);
			tagFileWriter.write(sb.toString());
			tagFileWriter.flush();
			tagFileWriter.close();
			return file;
		} catch (Exception e) {
			pipeline.addErrorFile(file, "ReadTagsTask error.", e);
			return null;
		}
	}

	/**
	 * replace *.dcm with *.tag in the file path. If no *.dcm file is there then just add it.
	 * 
	 * @param dcmFile File
	 * @return String
	 */
	private static String createTagPath(File dcmFile)
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
