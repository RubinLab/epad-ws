/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/*
 * The JPEGCodec and JPEGImageEncoder are classes that are not
 * to be used to the application software.  They are intended to
 * be accessed through the ImageIO package.
 import com.sun.image.codec.jpeg.JPEGCodec;
 import com.sun.image.codec.jpeg.JPEGImageEncoder;
 */


import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.util.EPADLogger;

/**
 * Produce thumbnail images of the DICOM images.
 */
public class ThumbnailFileUtil
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	private ThumbnailFileUtil()
	{
	}

	/**
	 * createThumbnailPath
	 * 
	 * @param studyId String
	 * @param seriesId String
	 * @return String
	 */
	public static String createThumbnailPath(String studyId, String seriesId)
	{
		return DicomFormatUtil.createDicomDirPath(studyId) + "/thumbnail_" + DicomFormatUtil.formatUidToDir(seriesId)
				+ ".jpg";
	}// createThumbnailPath

	/**
	 * createJPegPath which is in study, series
	 * 
	 * @param studyId String
	 * @param seriesId String
	 * @param sopInstanceId String
	 * @return String
	 */
	public static String createJPEGPath(String studyId, String seriesId, String sopInstanceId)
	{
		return DicomFormatUtil.createDicomDirPath(studyId, seriesId) + "/" + DicomFormatUtil.formatUidToDir(sopInstanceId)
				+ ".jpg";
	}// createJPEGPath

	/**
	 * Takes a large jpeg file and reduces the size.
	 * 
	 * @link http://www.philreeve.com/java_high_quality_thumbnails.php
	 * 
	 * @param inFilename String input file path
	 * @param outFilename String output file path
	 * @param largestDimension int largestDimension
	 * @return String Returns either an error message or a empty string if it works.
	 * @see ImageIO
	 */
	public static String shrinkJpegFile(String inFilename, String outFilename, int largestDimension)
	{

		logger.info("shrinkJpegFile in=" + inFilename + " out=" + outFilename);

		try {
			double scale;
			int sizeDifference, originalImageLargestDim;
			if (!inFilename.endsWith(".jpg") && !inFilename.endsWith(".jpeg") && !inFilename.endsWith(".gif")
					&& !inFilename.endsWith(".png")) {
				return "Error: Unsupported image type, please only either JPG, GIF or PNG";
			} else {
				Image inImage = Toolkit.getDefaultToolkit().getImage(inFilename);
				if (inImage.getWidth(null) == -1 || inImage.getHeight(null) == -1) {
					return "Error loading file: \"" + inFilename + "\"";
				} else {
					// find biggest dimension
					if (inImage.getWidth(null) > inImage.getHeight(null)) {
						scale = (double)largestDimension / (double)inImage.getWidth(null);
						sizeDifference = inImage.getWidth(null) - largestDimension;
						originalImageLargestDim = inImage.getWidth(null);
					} else {
						scale = (double)largestDimension / (double)inImage.getHeight(null);
						sizeDifference = inImage.getHeight(null) - largestDimension;
						originalImageLargestDim = inImage.getHeight(null);
					}
					// create an image buffer to draw to
					BufferedImage outImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB); // arbitrary init so code
																																														// compiles
					Graphics2D g2d;
					AffineTransform tx;
					if (scale < 1.0d) // only scale if desired size is smaller than original
					{
						int numSteps = sizeDifference / 100;
						int stepSize = sizeDifference / numSteps;
						int stepWeight = stepSize / 2;
						int heavierStepSize = stepSize + stepWeight;
						int lighterStepSize = stepSize - stepWeight;
						int currentStepSize, centerStep;
						double scaledW = inImage.getWidth(null);
						double scaledH = inImage.getHeight(null);
						if (numSteps % 2 == 1) // if there's an odd number of steps
							centerStep = (int)Math.ceil(numSteps / 2d); // find the center step
						else
							centerStep = -1; // set it to -1 so it's ignored later
						Integer intermediateSize, previousIntermediateSize = originalImageLargestDim;
						for (Integer i = 0; i < numSteps; i++) {
							if (i + 1 != centerStep) // if this isn't the center step
							{
								if (i == numSteps - 1) // if this is the last step
								{
									// fix the step size to account for decimal place errors previously
									currentStepSize = previousIntermediateSize - largestDimension;
								} else {
									if (numSteps - i > numSteps / 2) // if we're in the first half of the reductions
										currentStepSize = heavierStepSize;
									else
										currentStepSize = lighterStepSize;
								}
							} else // center step, use natural step size
							{
								currentStepSize = stepSize;
							}
							intermediateSize = previousIntermediateSize - currentStepSize;
							scale = (double)intermediateSize / (double)previousIntermediateSize;
							scaledW = (int)scaledW * scale;
							scaledH = (int)scaledH * scale;
							outImage = new BufferedImage((int)scaledW, (int)scaledH, BufferedImage.TYPE_INT_RGB);
							g2d = outImage.createGraphics();
							g2d.setBackground(Color.WHITE);
							g2d.clearRect(0, 0, outImage.getWidth(), outImage.getHeight());
							g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
							tx = new AffineTransform();
							tx.scale(scale, scale);
							g2d.drawImage(inImage, tx, null);
							g2d.dispose();
							inImage = new ImageIcon(outImage).getImage();
							previousIntermediateSize = intermediateSize;
						}
					} else {
						// just copy the original
						outImage = new BufferedImage(inImage.getWidth(null), inImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
						g2d = outImage.createGraphics();
						g2d.setBackground(Color.WHITE);
						g2d.clearRect(0, 0, outImage.getWidth(), outImage.getHeight());
						tx = new AffineTransform();
						tx.setToIdentity(); // use identity matrix so image is copied exactly
						g2d.drawImage(inImage, tx, null);
						g2d.dispose();
					}
					// JPEG-encode the image and write to file.
					OutputStream os = new FileOutputStream(outFilename);
					ImageIO.write(outImage, "jpeg", os);
					/*
					 * The JPEGImageEncoder and JPEGCodec classes used here are part of the internals of the Java run time and
					 * will not necessarily be the same for all implementations. ImageIO should be used (with a very few
					 * exceptions) to get the ImageWriter and ImageReader objects. JPEGImageEncoder encoder =
					 * JPEGCodec.createJPEGEncoder(os); encoder.encode(outImage);
					 */
					os.flush();
					os.close();
				}
			}
		} catch (Exception ex) {
			StringBuilder sb = new StringBuilder();
			sb.append("Failed to shrink JPEG file: ").append(inFilename);
			sb.append(" reason: ").append(ex.getMessage());

			logger.warning(sb.toString(), ex);

			return sb.toString();
		}
		return "";
	}
}
