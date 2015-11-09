/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
package edu.stanford.epad.epadws.processing.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import edu.stanford.epad.common.util.EPADLogger;

public class PNGGridGenerator
{
	public static final int IMAGES_PER_AXIS = 4;
	public static final int NUMBER_OF_IMAGES = IMAGES_PER_AXIS * IMAGES_PER_AXIS;
	private static EPADLogger logger = EPADLogger.getInstance();

	private static final float COMPRESSION = 0.25f;

	/**
	 * Creates a big PNG file containing a grid on source PNG files.
	 * 
	 * @param insetImageSize Image size 512 or 1024 or 2048 etc
	 * @param imagesPerAxis Number of images per axis, ex. for a 512 imgSize with 4 images per axis it generates a 2048 x
	 *          2048 image.
	 * @param firstFileIndex Index of the first file
	 * @param inputPNGFiles Array of files to be packed
	 * @param join True if images are to be joined using the blue and alpha fields of the PNG image. Not working, use
	 *          always false
	 * @param generatedPNGGridFilename Filename of the generated file
	 * @throws IOException If any I/O errors happen
	 */
	public static boolean createPNGGridFile(List<File> inputPNGFiles, File generatedPNGGridFile, int insetImageSize,
			int imagesPerAxis, int firstFileIndex, boolean join)
	{
		int width = insetImageSize * imagesPerAxis;
		int height = insetImageSize * imagesPerAxis;
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D ig2 = bi.createGraphics();
		boolean success = false;
		ig2.fillRect(0, 0, width - 1, height - 1);

		ImageOutputStream ios = null;
		Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("PNG");
		ImageWriter imageWriter = imageWriters.next();
		try {
			int x = 0;
			int y = 0;
			for (int i = firstFileIndex; i < inputPNGFiles.size(); i++) {
				BufferedImage input = ImageIO.read(inputPNGFiles.get(i));

				if (join) {
					i++;
					BufferedImage input2 = ImageIO.read(inputPNGFiles.get(i));
					input = joinImages(insetImageSize, input, input2);
				}
				ig2.drawImage(input, x * insetImageSize, y * insetImageSize, null);
				logger.info("Img [" + x + ", " + y + "] " + inputPNGFiles.get(i).getAbsolutePath());

				x++;
				if (x >= imagesPerAxis) { // Make it jump to the next line if x > than the number of images in a line
					x = 0;
					y++;
					if (y >= imagesPerAxis) {
						break;
					}
				}
			}
			ios = ImageIO.createImageOutputStream(generatedPNGGridFile);
			if (imageWriter != null) {
				imageWriter.setOutput(ios);
				imageWriter.write(bi);
				success = true;
			} else {
				logger.info("Error generating PNG grid file: no image writer found for PNG format");
				success = false;
			}
		} catch (IOException e) {
			logger.warning("Error generating PNG grid file " + generatedPNGGridFile.getAbsolutePath(), e);
			success = false;
		} finally {
			if (ios != null) {
				try {
					ios.close();
				} catch (IOException e) {
					logger.warning("Failed to close image output stream for file " + generatedPNGGridFile.getAbsolutePath(), e);
				}
			}
			if (imageWriter != null)
				imageWriter.dispose();
		}
		return success;
	}

	/**
	 * Join 2 16 bits png images (r g) to form a 32 bits png image (r g b a) It does not work properly, either it isn't
	 * generated properly or the browser doesn't read it properly (when the alfa byte is used, the image is distorted). It
	 * isn't being used.
	 * 
	 * @param inputSize
	 * @param img1
	 * @param img2
	 * @return
	 */
	static BufferedImage joinImages(int inputSize, BufferedImage img1, BufferedImage img2)
	{
		BufferedImage auxImg = new BufferedImage(inputSize, inputSize, BufferedImage.TYPE_INT_ARGB);
		auxImg.createGraphics().fillRect(0, 0, inputSize - 1, inputSize - 1);

		// We need its raster to set the pixels' values.
		WritableRaster raster1 = img1.getRaster();
		WritableRaster raster2 = img2.getRaster();
		WritableRaster auxRaster = auxImg.getRaster();
		int[] pixel1 = new int[3];
		int[] pixel2 = new int[3];
		int[] auxPixel = new int[4];

		for (int y1 = raster1.getMinY(), y2 = raster2.getMinY(), yAux = auxRaster.getMinY(); y1 < raster1.getHeight(); y1++, y2++, yAux++) {
			for (int x1 = raster1.getMinX(), x2 = raster2.getMinX(), xAux = auxRaster.getMinX(); x1 < raster1.getWidth(); x1++, x2++, xAux++) {
				raster1.getPixel(x1, y1, pixel1);
				raster2.getPixel(x2, y2, pixel2);

				auxPixel[0] = pixel1[0]; // red
				auxPixel[1] = pixel1[1]; // green
				auxPixel[2] = pixel2[0]; // blue
				auxPixel[3] = pixel2[1]; // alpha DOESN'T work, png modifies everything when
				// we include it, the problem maybe here or in the browser
				auxRaster.setPixel(xAux, yAux, auxPixel);
			}
		}
		return auxImg;
	}

	/**
	 * Transform a PNG file in JPG taking in consideration the windowing values from the DICOM file.
	 * 
	 * Be careful: The windowing values are HARD CODED you need to read them from the DICOM file.
	 * 
	 * @param pngFile
	 * @throws IOException
	 */
	public static void png2JPG(File pngFile) throws IOException
	{
		BufferedImage input = ImageIO.read(pngFile);

		// int pixelRepresentation = 1;
		// int bitsStored = 16;
		// int rescaleIntercept = -1024;
		// int rescaleSlope = 1;
		// int shifting;
		// int ww = 350;
		// int wc = 40;

		int pixelRepresentation = 0;
		int bitsStored = 12;
		int rescaleIntercept = -1000;
		int rescaleSlope = 1;
		int shifting;
		int ww = 400;
		int wc = 40;

		if (pixelRepresentation > 0.5) {
			shifting = 1 << (bitsStored - 1);
		} else {
			shifting = 0;
		}

		// TODO: Taken from the javascript, an explanation here would be very
		// useful.
		double a01 = (255.0 * rescaleSlope) / ww;
		double a04 = (255.0 * (rescaleIntercept - rescaleSlope * shifting - wc + 0.5 * ww)) / ww;

		// We need its raster to set the pixels' values.
		WritableRaster raster = input.getRaster();
		for (int y = raster.getMinY(); y < raster.getHeight(); y++) {
			for (int x = raster.getMinX(); x < raster.getWidth(); x++) {
				int[] pixel = raster.getPixel(x, y, (int[])null);

				int gray = (int)(a01 * (pixel[0] * 256 + pixel[1]) + a04);
				// int gray = 255*(rescaleSlope * (pixel[0]*256 + pixel[1]) +
				// rescaleIntercept - rescaleSlope*shifting - wc +
				// ww/2)/ww;

				// int gray = 255*(rescaleSlope * (pixel[0]*256 + pixel[1]) +
				// rescaleIntercept - rescaleSlope*shifting - wc)/ww + 255/2;

				// (gray*ww/255 - (rescaleIntercept - rescaleSlope*shifting - wc
				// + 0.5*ww))/rescaleSlope

				if (gray < 0) {
					gray = 0;
				} else if (gray > 255) {
					gray = 255;
				}
				pixel[0] = gray;
				pixel[1] = gray;
				pixel[2] = gray;
				raster.setPixel(x, y, pixel);
			}
		}

		// You first need to enumerate the image writers that are available to
		// your configuration:
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");

		// Then, choose the first image writer available (unless you want to
		// choose a specific writer) and create an ImageWriter instance:
		ImageWriter writer = iter.next();
		// Instantiate an ImageWriteParam object with default compression options
		ImageWriteParam iwp = writer.getDefaultWriteParam();

		// Now, we can set the compression quality:
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwp.setCompressionQuality(COMPRESSION); // A float between 0 and 1
		// 1 specifies minimum compression and maximum quality

		// Output the file:
		File file = new File(pngFile + ".jpg");
		if (file.exists()) {
			file.delete();
		}
		FileImageOutputStream output = null;
		try {
			output = new FileImageOutputStream(file);
			writer.setOutput(output);
			IIOImage image = new IIOImage(input, null, null);
			writer.write(null, image, iwp);
		} finally {
			if (output != null)
				output.close();
			writer.dispose();
		}
	}
}
