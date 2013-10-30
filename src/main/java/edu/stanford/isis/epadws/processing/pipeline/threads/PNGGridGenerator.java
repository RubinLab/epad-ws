package edu.stanford.isis.epadws.processing.pipeline.threads;

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

import edu.stanford.isis.epad.common.util.EPADLogger;

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
	@SuppressWarnings("unused")
	static private void png2JPG(File pngFile) throws IOException
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
		// instantiate an ImageWriteParam object with default compression
		// options
		ImageWriteParam iwp = writer.getDefaultWriteParam();

		// Now, we can set the compression quality:
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwp.setCompressionQuality(COMPRESSION); // an float between 0 and 1
		// 1 specifies minimum compression and maximum quality

		// Output the file:
		File file = new File(pngFile + ".jpg");
		if (file.exists()) {
			file.delete();
		}
		FileImageOutputStream output = new FileImageOutputStream(file);
		writer.setOutput(output);
		IIOImage image = new IIOImage(input, null, null);
		writer.write(null, image, iwp);
		writer.dispose();
	}

	// static void readJpg(String fileName, String imgId) throws IOException {
	//
	// URL url = new URL("http://epad-prod1.stanford.edu:9080/wado/wado?requestType=WADO&studyUID="
	// + STUDY_ID.replace('_', '.') + "&seriesUID=" + SERIES_ID.replace('_', '.') + "&objectUID="
	// + imgId.replace('_', '.'));
	// InputStream is = url.openConnection().getInputStream();
	//
	// BufferedInputStream input = new BufferedInputStream(is);
	//
	// File file = new File(fileName + ".jpg");
	// BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
	//
	// byte[] buf = new byte[1024];
	//
	// int i;
	// while ((i = input.read(buf)) != -1) {
	// output.write(buf, 0, i);
	// }
	// input.close();
	// output.close();
	// }
}
