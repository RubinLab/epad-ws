/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server.managers.pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.dicom.DicomTagFileUtils;
import edu.stanford.isis.epad.common.util.FileKey;
import edu.stanford.isis.epad.common.util.ResourceUtils;
import edu.stanford.isis.epadws.server.managers.support.DicomReader;

/**
 * Given a dicom file create the thumbnail file.
 * 
 * This process will use the given dicom file to generate the image.
 * 
 */
public class JPEGTask implements Callable<File>
{
	static final ProxyLogger log = ProxyLogger.getInstance();

	final File file;

	protected JPEGTask(File file)
	{
		this.file = file;
	}// JPEGTask

	@Override
	public File call() throws Exception
	{
		Process process = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			log.info("start JPEGTask");

			// File tagFile = getTagFileFromDcm(file);
			// Map<String,String> tags = DicomTagFileUtils.readTagFile(tagFile);

			String jpegPath = file.getAbsolutePath().replaceAll("\\.dcm", ".jpg");
			File jpegFile = new File(jpegPath);

			// format currently is dcm2jpg dcmFileIn jpgFileOut
			String[] command = new String[] { "./dcm2jpg", file.getAbsolutePath(), jpegFile.getAbsolutePath() };
			// ToDo: Determine the values that are truly leveled.

			ProcessBuilder pb = new ProcessBuilder(command);
			String dicomBinDirectory = ResourceUtils.getEPADWebServerDICOMBinDir();
			pb.directory(new File(dicomBinDirectory));

			process = pb.start();
			process.getOutputStream();// get the output stream.
			// Read out dir output
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

				log.info("JPEGTask call: " + sb.toString());
			} catch (InterruptedException e) {
				log.warning("Couldn't get tags for: " + file.getAbsolutePath(), e);
			}

			// we might want to try a 4096, 2048 with an offset of zero.

			// writeJpegsLeveled(4096,2048);
			writeJpegsLeveled(4096, 1024); // this is the value that I assume is "unleveled".

			// ToDo: This is temp code.
			writePackedPngs();

			// write thumbnail if needed, by shrinking an *.jpg file.
			writeThumbnailIfNeeded(file);

		} catch (Exception e) {
			log.warning("Failed to generate a thumbnail for " + file.getAbsolutePath(), e);
		} finally {
			closeReader(br);
			closeReader(isr);
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (Exception e) {
					log.warning("Failed to close InputStream.", e);
				}
			}
			if (process != null) {
				try {
					process.destroy();
				} catch (Exception e) {
					log.warning("Failed to destroy process.", e);
				}
			}
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

			// This might be the difference.

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
			String dicomBinDirectory = ResourceUtils.getEPADWebServerDICOMBinDir();
			pb.directory(new File(dicomBinDirectory));

			Process process = null;
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader br = null;

			try {
				process = pb.start();
				process.getOutputStream();// get the output stream.
				// Read out dir output
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
				closeReader(br);
				closeReader(isr);
				if (is != null) {
					try {
						is.close();
						is = null;
					} catch (Exception e) {
						log.warning("Failed to close InputStream.", e);
					}
				}
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
			log.sever("Failed to create JPEG", e);
		}
	}

	private void closeReader(Reader reader)
	{
		if (reader != null) {
			try {
				reader.close();
				reader = null;
			} catch (Exception e) {
				log.warning("Failed to close reader.", e);
			}
		}
	}

	/**
	 * Using the current dicom file read the tag to figure out if a thumbnail file should be created using it.
	 * 
	 * @param dicomFile File the Dicom file that will be made into a JPEG.
	 */
	public static void writeThumbnailIfNeeded(File dicomFile)
	{
		try {
			String tagFilePath = DicomTagFileUtils.createTagFilePath(dicomFile.getAbsolutePath());
			Map<String, String> tags = DicomTagFileUtils.readTagFile(new File(tagFilePath));
			ThumbnailManager thumbnailManager = ThumbnailManager.getInstance();
			thumbnailManager.writeThumbnailIfNeeded(tags, dicomFile);

		} catch (IOException ioe) {
			log.warning("Failed to write: " + dicomFile.getAbsolutePath(), ioe);
		}
	}

	public void writePackedPngs()
	{
		File input = file;
		File outputFile = null;
		OutputStream outputStream = null;
		try {

			DicomReader instance = new DicomReader(input);
			String pngFilePath = file.getAbsolutePath().replaceAll("\\.dcm", ".png");

			log.info("JPEGTask:Creating png file: " + pngFilePath);

			outputFile = new File(pngFilePath); // create the real file name here.
			outputStream = new FileOutputStream(outputFile);
			ImageIO.write(instance.getPackedImage(), "png", outputStream);

		} catch (FileNotFoundException e) {
			log.warning("failed to create packed png for: " + file.getAbsolutePath(), e);
		} catch (IOException e) {
			log.warning("failed to create packed PNG for: " + file.getAbsolutePath(), e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (Exception e) {
				}
			}
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
