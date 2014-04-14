/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.dicom.DicomReader;
import edu.stanford.epad.common.dicom.DicomTagFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.EPADResources;
import edu.stanford.epad.common.util.FileKey;
import edu.stanford.epad.epadws.processing.pipeline.ThumbnailManager;

/**
 * Given a DICOM file create the thumbnail JPEG file.
 */
public class JPEGThumbnailGeneratorTask implements Callable<File>
{
	static private final EPADLogger log = EPADLogger.getInstance();

	private final File file;

	public JPEGThumbnailGeneratorTask(File file)
	{
		this.file = file;
	}

	@Override
	public File call() throws Exception
	{
		Process process = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			log.info("Starting JPEG thumbnail generator task...");
			String jpegPath = file.getAbsolutePath().replaceAll("\\.dcm", ".jpg");
			File jpegFile = new File(jpegPath);

			// format currently is dcm2jpg dcmFileIn jpgFileOut
			String[] command = new String[] { "./dcm2jpg", file.getAbsolutePath(), jpegFile.getAbsolutePath() };
			// ToDo: Determine the values that are truly leveled.

			ProcessBuilder pb = new ProcessBuilder(command);
			String dicomBinDirectory = EPADResources.getEPADWebServerDICOMBinDir();
			pb.directory(new File(dicomBinDirectory));

			process = pb.start();
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
				process.waitFor(); // keep.
				log.info("JPEGTask call: " + sb.toString());
			} catch (InterruptedException e) {
				log.warning("Couldn't get tags for: " + file.getAbsolutePath(), e);
			}
			writeJpegsLeveled(4096, 1024); // this is the value that I assume is "unleveled".
			writePackedPngs();
			writeThumbnailIfNeeded(file);
		} catch (Exception e) {
			log.warning("Failed to generate a thumbnail for " + file.getAbsolutePath(), e);
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(is);
			if (process != null)
				process.destroy();
		}
		return null;
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
		try {
			String leveledJPegDir = file.getParent();
			String name = file.getName();
			String jpegPath = FileKey.getCanonicalPath(new File(leveledJPegDir)) + "/" + name.replaceAll("\\.dcm", ".jpg");
			// String jpegPath = file.getAbsolutePath().replaceAll("\\.dcm","_ww"+width+"_wl"+level+".jpg");
			File jpegFile = new File(jpegPath);

			log.info("JPEGTask: start writing: " + jpegPath);

			// format currently is dcm2jpg dcmFileIn jpgFileOut
			String[] command = new String[] { "./dcm2jpg", "-c=" + level, "-w=" + width, file.getAbsolutePath(),
					jpegFile.getAbsolutePath() };

			ProcessBuilder pb = new ProcessBuilder(command);
			String dicomBinDirectory = EPADResources.getEPADWebServerDICOMBinDir();
			pb.directory(new File(dicomBinDirectory));

			Process process = null;
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader br = null;

			try {
				process = pb.start();
				process.getOutputStream();

				is = process.getInputStream();
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);

				String line;
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}

				// Wait to get exit value
				try {
					process.waitFor(); // keep.

					log.info("JPEGTask: " + sb.toString());
				} catch (InterruptedException e) {
					log.warning("Couldn't get tags for: " + file.getAbsolutePath(), e);
				}
			} catch (IOException ioe) {
				log.warning("Failed to make leveled image (" + width + "," + level + ")", ioe);
			} finally {
				IOUtils.closeQuietly(br);
				IOUtils.closeQuietly(isr);
				IOUtils.closeQuietly(is);
				if (process != null) {
					try {
						process.destroy();
					} catch (Exception e) {
						log.warning("Failed to destroy process.", e);
					}
				}
			}
			log.info("JPEGTask: finished writing: " + jpegPath);
		} catch (Exception e) {
			log.warning("Failed to create JPEG", e);
		}
	}

	/**
	 * Using the current DICOM file read the tag to figure out if a thumbnail file should be created using it.
	 * 
	 * @param dicomFile File the DICOM file that will be made into a JPEG.
	 */
	private void writeThumbnailIfNeeded(File dicomFile)
	{
		String tagFilePath = DicomTagFileUtils.createTagFilePath(dicomFile.getAbsolutePath());
		Map<String, String> tags = DicomTagFileUtils.readDICOMTagFile(new File(tagFilePath));
		ThumbnailManager thumbnailManager = ThumbnailManager.getInstance();
		thumbnailManager.writeThumbnailIfNeeded(tags, dicomFile);
	}

	private void writePackedPngs()
	{
		File input = file;
		OutputStream outputStream = null;
		try {
			DicomReader instance = new DicomReader(input);
			String pngFilePath = file.getAbsolutePath().replaceAll("\\.dcm", ".png");

			log.info("JPEGTask:Creating PNG file: " + pngFilePath);

			File outputFile = new File(pngFilePath); // create the real file name here.
			outputStream = new FileOutputStream(outputFile);
			ImageIO.write(instance.getPackedImage(), "png", outputStream);
		} catch (FileNotFoundException e) {
			log.warning("Dailed to create PNG for: " + file.getAbsolutePath(), e);
		} catch (IOException e) {
			log.warning("Failed to create PNG for: " + file.getAbsolutePath(), e);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}

	/**
	 * Given a DICOM file return the tag file.
	 * 
	 * @param dicomFile File
	 * @return File
	 */
	static File getTagFileFromDcm(File dicomFile)
	{
		String tagPath = dicomFile.getAbsolutePath().replaceAll("\\.dcm", "\\.tag");
		return new File(tagPath);
	}

}
