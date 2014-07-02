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
import java.util.ArrayList;
import java.util.List;
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

	public static List<File> getDSOFrameFiles(String studyUID, String seriesUID, String imageUID)
	{
		return new ArrayList<>(); // TODO
	}

	public static File createDSO(String studyUID, String seriesUID, String imageUID, List<File> frames)
	{
		// File temporaryDSOFile = File.createTempFile(sourceImageUID, ".tmpDSO");
		// EPADTools.downloadDICOMFileFromWADO(studyUID, seriesUID, imageUID, temporaryDSOFile);

		// TIFFMasksToDSOConverter converter = new TIFFMasksToDSOConverter();
		// converter.convert(maskFilePaths, dicomImageFilePaths, dsoFile.getAbsolutePath());
		return null;
	}

	public static void writeDSOPNGs(File dsoFile) throws Exception
	{
		try {
			EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			DicomSegmentationObject dso = new DicomSegmentationObject();
			SourceImage sourceDSOImage = dso.convert(dsoFile.getAbsolutePath());
			int numberOfFrames = sourceDSOImage.getNumberOfBufferedImages();
			AttributeList dicomAttributes = PixelMedUtils.readAttributeListFromDicomFile(dsoFile.getAbsolutePath());
			String studyUID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.StudyInstanceUID);
			String seriesUID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.SeriesInstanceUID);
			String imageUID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.SOPInstanceUID);

			String pngDirectoryPath = baseDicomDirectory + "/studies/" + studyUID + "/series/" + seriesUID + "/images/"
					+ imageUID + "/frame/";
			File pngFilesDirectory = new File(pngDirectoryPath);

			log.info("Writing PNGs for DSO " + imageUID + " in series " + seriesUID);

			pngFilesDirectory.mkdirs();

			for (int frameNumber = 0; frameNumber < numberOfFrames; frameNumber++) {
				BufferedImage bufferedImage = sourceDSOImage.getBufferedImage(numberOfFrames - frameNumber - 1);
				String pngFilePath = pngDirectoryPath + frameNumber + ".png";
				File pngFile = new File(pngFilePath);
				try {
					insertEpadFile(databaseOperations, pngFile);
					log.info("Writing PNG for frame " + frameNumber + " for DSO image " + imageUID + " in series " + seriesUID);
					log.info("File width " + bufferedImage.getWidth() + ", height " + bufferedImage.getHeight());
					ImageIO.write(bufferedImage, "png", pngFile);
					databaseOperations.updateEpadFileRecord(pngFilePath, PNGFileProcessingStatus.DONE, 77, "");
				} catch (IOException e) {
					log.warning("Failure writing PNG mask file " + pngFilePath + " for image " + imageUID + " in series "
							+ seriesUID, e);
				}
			}
			log.info("Finished writing PNGs for DSO " + imageUID + " in series " + seriesUID);
		} catch (DicomException e) {
			log.warning("DICOM exception writing DSO PNGs", e);
			throw new Exception("DICOM exception writing DSO PNGs", e);
		} catch (IOException e) {
			log.warning("IO exception writing DSO PNGs", e);
			throw new Exception("IO exception writing DSO PNGs", e);
		}
	}

	public static void writeDSOMaskPNGs(File dsoFile) throws Exception
	{
		try {
			EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			DicomSegmentationObject dso = new DicomSegmentationObject();
			SourceImage sourceDSOImage = dso.convert(dsoFile.getAbsolutePath());
			int numberOfFrames = sourceDSOImage.getNumberOfBufferedImages();
			AttributeList dicomAttributes = PixelMedUtils.readAttributeListFromDicomFile(dsoFile.getAbsolutePath());
			String studyUID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.StudyInstanceUID);
			String seriesUID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.SeriesInstanceUID);
			String imageUID = Attribute.getSingleStringValueOrEmptyString(dicomAttributes, TagFromName.SOPInstanceUID);

			String pngMaskDirectoryPath = baseDicomDirectory + "/studies/" + studyUID + "/series/" + seriesUID + "/images/"
					+ imageUID + "/masks/";
			File pngMaskFilesDirectory = new File(pngMaskDirectoryPath);

			log.info("Writing PNG masks for DSO " + imageUID + " in series " + seriesUID + "...");

			pngMaskFilesDirectory.mkdirs();

			for (int frameNumber = 0; frameNumber < numberOfFrames; frameNumber++) {
				BufferedImage bufferedImage = sourceDSOImage.getBufferedImage(numberOfFrames - frameNumber - 1);
				BufferedImage bufferedImageWithTransparency = generateTransparentImage(bufferedImage);

				String pngMaskFilePath = pngMaskDirectoryPath + frameNumber + ".png";

				File pngMaskFile = new File(pngMaskFilePath);
				try {
					insertEpadFile(databaseOperations, pngMaskFile);
					log.info("Writing PNG mask file frame " + frameNumber + " for DSO image " + imageUID + " in series "
							+ seriesUID);
					log.info("Mask file width " + bufferedImage.getWidth() + ", height " + bufferedImage.getHeight());
					ImageIO.write(bufferedImageWithTransparency, "png", pngMaskFile);
					databaseOperations.updateEpadFileRecord(pngMaskFilePath, PNGFileProcessingStatus.DONE, 77, "");
				} catch (IOException e) {
					log.warning("Failure writing PNG mask file " + pngMaskFilePath + " for frame " + frameNumber + " of DSO "
							+ imageUID + " in series " + seriesUID, e);
				}
			}
			log.info("... finished writing PNG masks for DSO image " + imageUID + " in series " + seriesUID);
		} catch (DicomException e) {
			log.warning("DICOM exception writing DSO PNG masks", e);
			throw new Exception("DICOM exception writing DSO PNG masks", e);
		} catch (IOException e) {
			log.warning("IO exception writing DSO PNG masks", e);
			throw new Exception("IO exception writing DSO PNG masks", e);
		}
	}

	private static BufferedImage generateTransparentImage(BufferedImage source)
	{
		Image image = makeColorOpaque(source, Color.BLACK);
		BufferedImage transparent = imageToBufferedImage(image);
		Image image2 = makeColorBlackAndTransparent(transparent, Color.WHITE);
		BufferedImage transparent2 = imageToBufferedImage(image2);
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

	private static Image makeColorOpaque(BufferedImage im, final Color color)
	{
		ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = color.getRGB() | 0xFF000000;

			@Override
			public final int filterRGB(int x, int y, int rgb)
			{
				if ((rgb | 0xFF000000) == markerRGB) {
					return 0xFF000000 | rgb;
				} else {
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	@SuppressWarnings("unused")
	private static Image makeColorTransparent(BufferedImage im, final Color color)
	{
		ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = color.getRGB() | 0xFF000000;

			@Override
			public final int filterRGB(int x, int y, int rgb)
			{
				if ((rgb | 0xFF000000) == markerRGB) {
					return 0x00FFFFFF & rgb;
				} else {
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	private static Image makeColorBlackAndTransparent(BufferedImage im, final Color color)
	{
		ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = color.getRGB() | 0xFF000000;

			@Override
			public final int filterRGB(int x, int y, int rgb)
			{
				if ((rgb | 0xFF000000) == markerRGB) {
					int r = 255;
					int g = 255;
					int b = 255;
					int a = 0;
					int col = (a << 24) | (r << 16) | (g << 8) | b;
					return col;
				} else {
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
