package edu.stanford.aim.proxy.tools;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
// import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
// import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.FileImageInputStream;

// import javax.imageio.ImageReader;
// import javax.imageio.ImageWriter;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che2.imageioimpl.plugins.dcm.DicomImageReader;
import org.dcm4che2.imageioimpl.plugins.dcm.DicomImageReaderSpi;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.StopTagInputHandler;

import edu.stanford.aim.proxy.helpers.RasterProcessor;

/**
 * Read a Dicom instance and generate a variety of other formats that can be used to create files.
 * 
 * @author Bradley Ross
 * @see edu.stanford.aim.proxy.servlet.files.GetJpeg
 * @see RasterProcessor
 * 
 *      Class extracted from https://bmir-gforge.stanford.edu/svn/dirac/trunk/ePAD/ePAD-2012/DicomInterface to remove
 *      dependency.
 */
public class DicomReader
{
	/**
	 * File containing Dicom data.
	 */
	protected File dicomFile = null;

	/**
	 * Constructor.
	 * 
	 * @param value file containing Dicom data
	 */
	public DicomReader(File value)
	{
		dicomFile = value;
	}

	/**
	 * Obtain a DicomObject object using the Dicom file.
	 * 
	 * <p>
	 * This object does not contain pixel data.
	 * <p>
	 * 
	 * @return DicomObject object.
	 * @throws IOException
	 */
	public DicomObject getDicomObject() throws Exception
	{
		DicomInputStream dis = null;
		StopTagInputHandler stop = new StopTagInputHandler(Tag.PixelData);
		dis = new DicomInputStream(dicomFile);
		dis.setHandler(stop);
		dis.close();
		return dis.readDicomObject();
	}

	/**
	 * Generate a buffered image using the parameters in the file.
	 * 
	 * @return image
	 * @throws IOException
	 */
	public BufferedImage getImage() throws IOException
	{
		return getImage(0);
	}

	/**
	 * Generate a buffered image using the parameters in the file.
	 * 
	 * @param frameValue frame number
	 * @return image
	 * @throws IOException
	 */
	public BufferedImage getImage(int frameValue) throws IOException
	{
		FileImageInputStream fis = null;
		fis = new FileImageInputStream(dicomFile);
		DicomImageReader codec = (DicomImageReader)new DicomImageReaderSpi().createReaderInstance();
		codec.setInput(fis);
		DicomImageReadParam param = (DicomImageReadParam)codec.getDefaultReadParam();
		BufferedImage image = codec.read(frameValue, param);
		fis.close();
		return image;
	}

	/**
	 * Generate a buffered image with the high order bits of PixelData in the red channel and low order bits in the green
	 * channel.
	 * 
	 * @return image
	 * @throws IOException
	 */
	public BufferedImage getPackedImage() throws IOException
	{
		return getPackedImage(0);
	}

	/**
	 * Generate a buffered image with the high order bits of PixelData in the red channel and low order bits in the green
	 * channel.
	 * 
	 * @param frameValue frame number
	 * @return image
	 * @throws IOException
	 */
	public BufferedImage getPackedImage(int frameValue) throws IOException
	{
		FileImageInputStream fis = null;
		DicomInputStream dis = null;
		StopTagInputHandler stop = new StopTagInputHandler(Tag.PixelData);
		dis = new DicomInputStream(dicomFile);
		dis.setHandler(stop);
		DicomObject object = dis.readDicomObject();
		RasterProcessor rasterProcessor = new RasterProcessor(object);
		dis.close();
		fis = new FileImageInputStream(dicomFile);
		DicomImageReader codec = (DicomImageReader)new DicomImageReaderSpi().createReaderInstance();
		codec.setInput(fis);
		DicomImageReadParam param = (DicomImageReadParam)codec.getDefaultReadParam();
		Raster raster = codec.readRaster(frameValue, param);
		BufferedImage packedImage = rasterProcessor.buildPng(raster);
		dis.close();
		return packedImage;
	}

	/**
	 * Test driver.
	 * 
	 * @param args first argument is name of Dicom file
	 */
	public static void main(String[] args)
	{
		try {
			File input = new File(args[0]);
			File outputFile = null;
			OutputStream outputStream = null;
			DicomReader instance = new DicomReader(input);
			outputFile = new File("packed.png");
			outputStream = new FileOutputStream(outputFile);
			ImageIO.write(instance.getPackedImage(), "png", outputStream);
			outputStream.close();
			System.out.println("Generating second image");
			outputFile = new File("image.jpeg");
			outputStream = new FileOutputStream(outputFile);
			ImageIO.write(instance.getImage(), "jpeg", outputStream);
			outputStream.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getClass().getName() + " " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getClass().getName() + " " + e.getMessage());
			e.printStackTrace();
		}
	}

}
