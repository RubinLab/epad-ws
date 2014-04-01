/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.epad.epadws.processing.leveling;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.EPADResources;
import edu.stanford.epad.common.util.FileKey;

/**
 * Creates one JPEG file leveled with the stated parameters.
 */
public class JPEGLevelingTask implements Callable<File>
{
	static final EPADLogger log = EPADLogger.getInstance();

	final File file;
	int ww;
	int wl;

	/**
	 * 
	 * @param file File
	 * @param windowWidth int
	 * @param windowLevel int
	 */
	protected JPEGLevelingTask(File file)
	{
		this.file = file;
		// this.ww = windowWidth;
		// this.wl = windowLevel;
	}// JPEGTask

	public void setLevel(int windowWidth, int windowLevel)
	{
		this.ww = windowWidth;
		this.wl = windowLevel;
	}

	@Override
	public File call() throws Exception
	{

		// all leveled jpegs go under a directory "ww[#]wl[#]"
		String jpegPath = makeLeveledJPegFilePath(file);
		File jpegFile = new File(jpegPath);

		if (jpegFile.exists()) {
			// don't create the file if it already exists.
			// log.info("Not creating: "+jpegFile.getName()+" since it already exists.");
			return file;
		}

		writeJpegsLeveled(ww, wl);
		return file;
	}

	/**
	 * Leveled JPEG files are all in the same directory under the series directory. This name is
	 * ww[windowWidth#]wl[windowLevel#]
	 * 
	 * While creating the path name is will see if the directory path exists. If it doesn't it will create the needed
	 * directories.
	 * 
	 * @param dicomFile File
	 * @return String
	 */
	public String makeLeveledJPegFilePath(File dicomFile)
	{
		String name = dicomFile.getName();
		File seriesDir = dicomFile.getParentFile();

		if (!name.endsWith(".dcm")) {
			throw new IllegalArgumentException("Expecting a DICOM file. Got: " + dicomFile.getAbsolutePath());
		}
		if (!seriesDir.isDirectory()) {
			throw new IllegalArgumentException("Not a directory: " + seriesDir.getAbsolutePath());
		}

		File leveledJPegDir = new File(seriesDir.getAbsolutePath() + "/ww" + ww + "wl" + wl);

		// make the directory if needed.
		EPADFileUtils.makeDirs(leveledJPegDir);

		String jpegFilePath = FileKey.getCanonicalPath(leveledJPegDir) + "/" + name.replaceAll("\\.dcm", ".jpg");
		return jpegFilePath;
	}

	/**
	 * Write a JPEG that is created using the following parameters. The resulting file for a file with the settings,
	 * window=4096 and center=2048 will have the following name.
	 * 
	 * [...instance-id...]_ww4096_wl2048.jpeg
	 * 
	 * @param width int range is 0-4096
	 * @param level int range is 0-4096
	 */
	public void writeJpegsLeveled(int width, int level)
	{

		String jpegPath = makeLeveledJPegFilePath(file);
		File jpegFile = new File(jpegPath);

		// format currently is dcm2jpg dcmFileIn jpgFileOut
		String[] command = new String[] { "./dcm2jpg", "-c=" + level, "-w=" + width, file.getAbsolutePath(),
				jpegFile.getAbsolutePath() };

		ProcessBuilder pb = new ProcessBuilder(command);
		String dicomBinDirectory = EPADResources.getEPADWebServerDICOMBinDir();
		pb.directory(new File(dicomBinDirectory));

		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			Process process = pb.start();
			process.getOutputStream();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				log.warning("Couldn't get tags for: " + file.getAbsolutePath(), e);
			}
		} catch (IOException ioe) {
			log.warning("Failed to make leveled image (" + width + "," + level + ")", ioe);
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(is);
		}
	}
}
