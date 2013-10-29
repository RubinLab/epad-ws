package edu.stanford.isis.epadws.server.managers.support;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.stream.FileImageInputStream;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che2.imageioimpl.plugins.dcm.DicomImageReader;
import org.dcm4che2.imageioimpl.plugins.dcm.DicomImageReaderSpi;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.StopTagInputHandler;

import edu.stanford.isis.epad.common.dicom.RasterProcessor;

/**
 * 
 * Tools for processing DICOM images.
 * 
 * TODO Merge this with DicomReader in epad-common.
 * 
 */
public class DicomReader extends edu.stanford.isis.epad.common.dicom.DicomReader
{
	/**
	 * Constructor.
	 * 
	 * @param value file containing Dicom data
	 */
	public DicomReader(File value)
	{
		super(value);
	}

	/**
	 * Generate a buffered image with the high order bits of PixelData in the red channel and low order bits in the green
	 * channel.
	 * 
	 * @return image
	 * @throws IOException
	 */
	public BufferedImage getMyPackedImage() throws IOException
	{
		return getMyPackedImage(0);
	}

	/**
	 * Generate a buffered image with the high order bits of PixelData in the red channel and low order bits in the green
	 * channel.
	 * 
	 * @param frameValue frame number
	 * @return image
	 * @throws IOException
	 */
	public BufferedImage getMyPackedImage(int frameValue) throws IOException
	{
		FileImageInputStream fis = null;
		DicomInputStream dis = null;
		StopTagInputHandler stop = new StopTagInputHandler(Tag.PixelData);
		dis = new DicomInputStream(dicomFile);
		dis.setHandler(stop);
		DicomObject object = dis.readDicomObject();
		RasterProcessor rasterProcessor = new RasterProcessor(object);
		// RasterProcessorDP rasterProcessor = new RasterProcessorDP(object);
		dis.close();
		fis = new FileImageInputStream(dicomFile);
		DicomImageReader codec = (DicomImageReader)new DicomImageReaderSpi().createReaderInstance();
		codec.setInput(fis);
		DicomImageReadParam param = (DicomImageReadParam)codec.getDefaultReadParam();
		Raster raster = codec.readRaster(frameValue, param);
		BufferedImage packedImage = rasterProcessor.buildPng(raster);
		dis.close();
		fis.close();
		return packedImage;
	}
}
