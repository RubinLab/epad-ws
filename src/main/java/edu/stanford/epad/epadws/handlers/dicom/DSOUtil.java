package edu.stanford.epad.epadws.handlers.dicom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.TagFromName;
import com.pixelmed.display.SourceImage;

import edu.stanford.epad.common.dicom.DicomSegmentationObject;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.EPADResources;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;

public class DSOUtil
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String baseDicomDirectory = EPADResources.getEPADWebServerPNGDir();

	public static boolean writeDSOMaskPNGs(String seriesUID, File dsoFile)
	{
		boolean success;

		try {
			EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			DicomSegmentationObject dso = new DicomSegmentationObject();
			SourceImage sourceDSOImage = dso.convert(dsoFile.getAbsolutePath());
			int numberOfFrames = sourceDSOImage.getNumberOfBufferedImages();
			AttributeList attbList = PixelMedUtils.readAttributeListFromDicomFile(dsoFile.getAbsolutePath());
			String studyUID = Attribute.getSingleStringValueOrEmptyString(attbList, TagFromName.StudyInstanceUID);
			String imageUID = Attribute.getSingleStringValueOrEmptyString(attbList, TagFromName.SOPInstanceUID);
			File pngMaskFilesDirectory = new File(baseDicomDirectory + studyUID.replaceAll("\\.", "_") + "/"
					+ seriesUID.replaceAll("\\.", "_") + "/");

			pngMaskFilesDirectory.mkdirs();

			log.info("Writing DSO PNG masks for series " + seriesUID + "...");

			for (int frameNumber = 0; frameNumber < numberOfFrames; frameNumber++) {
				BufferedImage bufferedImage = sourceDSOImage.getBufferedImage(numberOfFrames - frameNumber - 1);
				BufferedImage bufferedImageWithTransparency = generateTransparentImage(bufferedImage);
				String pngMaskFilePath = baseDicomDirectory + studyUID.replaceAll("\\.", "_") + "/"
						+ seriesUID.replaceAll("\\.", "_") + "/" + imageUID.replaceAll("\\.", "_") + "-" + frameNumber + ".png";
				File pngMaskFile = new File(pngMaskFilePath);
				try {
					insertEpadFile(databaseOperations, pngMaskFile);
					log.info("Writing DSO PNG mask file " + frameNumber + " for series " + seriesUID);
					ImageIO.write(bufferedImageWithTransparency, "png", pngMaskFile);
					databaseOperations.updateEpadFileRecord(pngMaskFilePath, PNGFileProcessingStatus.DONE, 77, "");
				} catch (IOException e) {
					log.warning("Failed to write DSO PNG mask file " + pngMaskFilePath + " for series " + seriesUID, e);
				}
			}
			log.info("... finished writing DSO PNG masks for series " + seriesUID);
			success = true;
		} catch (DicomException e) {
			log.warning("DICOM exception writing DSO PNG masks for series " + seriesUID, e);
			success = false;
		} catch (IOException e) {
			log.warning("IO exception writing DSO PNG masks for series " + seriesUID, e);
			success = false;
		}
		return success;
	}

	private static BufferedImage generateTransparentImage(BufferedImage source)
	{
		Image image = makeColorTransparent(source, Color.BLACK);
		BufferedImage transparent = imageToBufferedImage(image);
		Image image2 = makeColorSemiTransparent(transparent, Color.WHITE);
		BufferedImage transparent2 = imageToBufferedImage(image2);
		image = null;
		transparent = null;
		image2 = null;
		return transparent2;
	}

	private static BufferedImage imageToBufferedImage(Image image)
	{
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return bufferedImage;
	}

	private static Image makeColorTransparent(BufferedImage im, final Color color)
	{
		ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = color.getRGB() | 0xFF000000; // the color we are looking for... Alpha bits are set to
																													// opaque

			@Override
			public final int filterRGB(int x, int y, int rgb)
			{
				if ((rgb | 0xFF000000) == markerRGB) { // Mark the alpha bits as zero - transparent
					return 0x00FFFFFF & rgb;
				} else { // nothing to do
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	private static Image makeColorSemiTransparent(BufferedImage im, final Color color)
	{
		ImageFilter filter = new RGBImageFilter() {

			// the color we are looking for... Alpha bits are set to opaque
			public int markerRGB = color.getRGB() | 0xFF000000;

			@Override
			public final int filterRGB(int x, int y, int rgb)
			{
				if ((rgb | 0xFF000000) == markerRGB) { // Mark the alpha bits as zero - transparent
					int r = 255;
					int g = 0;
					int b = 0;
					int a = 80;
					int col = (a << 24) | (r << 16) | (g << 8) | b;
					return col;
				} else { // nothing to do
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	private static void insertEpadFile(EpadDatabaseOperations epadDatabaseOperations, File outputFile)
	{
		Map<String, String> epadFilesTable = Dcm4CheeDatabaseUtils.createEPadFilesTableData(outputFile);
		epadFilesTable.put("file_status", "" + SeriesProcessingStatus.IN_PIPELINE.getCode());
		epadDatabaseOperations.insertEpadFileRecord(epadFilesTable);
	}
}
