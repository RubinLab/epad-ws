package edu.stanford.epad.epadws.processing.pipeline.task;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.TagFromName;
import com.pixelmed.display.SourceImage;

import edu.stanford.epad.common.dicom.DicomSegmentationObject;
import edu.stanford.epad.common.dicom.DicomTagFileUtils;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.plugins.PluginAIMUtil;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.EPADResources;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.hakan.aim3api.base.AimException;
import edu.stanford.hakan.aim3api.base.DICOMImageReference;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;
import edu.stanford.hakan.aim3api.base.Person;
import edu.stanford.hakan.aim3api.base.Polyline;
import edu.stanford.hakan.aim3api.base.Segmentation;
import edu.stanford.hakan.aim3api.base.SegmentationCollection;
import edu.stanford.hakan.aim3api.base.TwoDimensionSpatialCoordinate;
import edu.stanford.hakan.aim3api.usage.AnnotationBuilder;

/**
 * This task generates DICOM Segmentation Objects.
 * 
 * @author alansnyder
 */
public class DicomSegmentationObjectPNGMaskGeneratorTask implements GeneratorTask
{
	public String serverProxy = EPADConfig.getInstance().getStringPropertyValue("serverProxy");
	public String namespace = EPADConfig.getInstance().getStringPropertyValue("namespace");
	public String eXistServerUrl = EPADConfig.getInstance().getStringPropertyValue("serverUrl");
	public String eXistUsername = EPADConfig.getInstance().getStringPropertyValue("username");
	public String eXistPassword = EPADConfig.getInstance().getStringPropertyValue("password");
	public String baseAnnotationDir = EPADConfig.getInstance().getStringPropertyValue("baseAnnotationDir");
	public String xsdFile = EPADConfig.getInstance().getStringPropertyValue("xsdFile");
	public String xsdFilePath = EPADConfig.getInstance().getStringPropertyValue("baseSchemaDir") + xsdFile;
	public String eXistCollection = EPADConfig.getInstance().getStringPropertyValue("collection");
	public String templatePath = EPADConfig.getInstance().getStringPropertyValue("baseTemplatesDir");
	public String wadoProxy = EPADConfig.getInstance().getStringPropertyValue("wadoProxy");

	private static final EPADLogger log = EPADLogger.getInstance();
	private static final String baseDicomDirectory = EPADResources.getEPADWebServerPNGDir();

	private final String seriesUID;
	private final File dicomInputFile;
	private final File segObjectOutputFile;

	public DicomSegmentationObjectPNGMaskGeneratorTask(String seriesUID, File dicomInputFile, File segObjectOutputFile)
	{
		this.seriesUID = seriesUID;
		this.dicomInputFile = dicomInputFile;
		this.segObjectOutputFile = segObjectOutputFile;
	}

	@Override
	public void run()
	{
		Map<String, String> ids = readTagsFromDicomFile(dicomInputFile);
		String studyId = ids.get("study-id");
		String seriesId = ids.get("series-id");
		String imageId = ids.get("sop-inst-id");
		String objectId = ids.get("ref-sop-inst-id");

		studyId = studyId.replaceAll("\\.", "_");
		seriesId = seriesId.replaceAll("\\.", "_");
		imageId = imageId.replaceAll("\\.", "_");
		objectId = objectId.replaceAll("\\.", "_");

		log.info("Processing DSO for series  " + seriesUID + "; file=" + dicomInputFile.getAbsolutePath());

		try {
			DicomSegmentationObject dicomSegmentationObject = new DicomSegmentationObject();
			SourceImage sourceImage = dicomSegmentationObject.convert(dicomInputFile.getAbsolutePath());
			int count = sourceImage.getNumberOfBufferedImages();
			File repDest = new File(baseDicomDirectory + studyId + "/" + seriesId + "/");
			repDest.mkdirs();

			EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
			log.info("Writing DSO PNG masks for series " + seriesUID + "...");

			for (int i = 0; i < count; i++) { // Create the mask images
				BufferedImage source = sourceImage.getBufferedImage(count - i - 1);
				BufferedImage sourceWithTransparency = generateTransparentImage(source); // Generate a transparent image
				String pngMaskFilePath = baseDicomDirectory + studyId + "/" + seriesId + "/" + objectId + "-" + i + ".png";
				File pngMaskFile = new File(pngMaskFilePath);
				try {
					insertEpadFile(databaseOperations, pngMaskFile);
					log.info("Writing DSO PNG mask file " + i + " for series " + seriesUID);
					ImageIO.write(sourceWithTransparency, "png", pngMaskFile);
					databaseOperations.updateEpadFileRecord(pngMaskFilePath, PNGFileProcessingStatus.DONE, 77, "");
				} catch (IOException e) {
					log.warning("Failed to write DSO PNG mask for series " + seriesUID, e);
				}
				source = null;
				sourceWithTransparency = null;
			}
			log.info("... finished writing DSO PNG masks for series " + seriesUID);

			File dsoTagFile = new File(repDest.getAbsolutePath() + "/" + objectId + ".tag");
			if (!dsoTagFile.exists()) {
				dsoTagFile.createNewFile();
				ExecutorService taskExecutor = Executors.newFixedThreadPool(1);
				taskExecutor.execute(new DicomHeadersTask(seriesUID, dicomInputFile, dsoTagFile));
				taskExecutor.shutdown();
			}
			File tempDSOTagFile = File.createTempFile(dsoTagFile.getName(), ".temptag");
			tempDSOTagFile.createNewFile();
			DicomHeadersTask dicomHeadersTask = new DicomHeadersTask(seriesUID, dicomInputFile, tempDSOTagFile);
			dicomHeadersTask.run();
			generateAIMFileForDSO(tempDSOTagFile);
		} catch (Exception e) {
			log.warning("Error writing PNGs for DSO series " + seriesUID + ": " + e.getMessage());
		}
	}

	/**
	 * Generate an AIM file for a new DICOM Segmentation Object (DSO). This AIM file actually annotates the original
	 * image, NOT the DSO. The Referenced SOP Instance UID field in the DICOM DSO tag file identifies the image from which
	 * the segmentation object is derived from. It contains the imageID of the original image but does not contain the
	 * study or series identifiers for that image - so we need to discover them by querying ePAD.
	 */

	/**
	 * {@link PluginAIMUtil#generateAIMFileForDSO} is very similar - should merge
	 * 
	 */
	private void generateAIMFileForDSO(File dsoTagFile) throws Exception
	{
		Map<String, String> dsoDICOMTags = DicomTagFileUtils.readDICOMTagFile(dsoTagFile);

		String patientID = dsoDICOMTags.get("Patient ID"); // TODO Replace with constants from DicomTagFileUtils
		String patientName = dsoDICOMTags.get("Patient's Name");
		String sopClassUID = dsoDICOMTags.get("SOP Class UID");
		String dsoStudyInstanceUID = dsoDICOMTags.get("Study Instance UID"); // Study ID of the DSO (same as original image)
		String dsoSeriesInstanceUID = dsoDICOMTags.get("Series Instance UID"); // Series ID of DSO (DSO gets new series)
		String dsoSOPInstanceUID = dsoDICOMTags.get("SOP Instance UID"); // Image ID of DSO
		String referencedSOPInstanceUID = dsoDICOMTags.get("Referenced SOP Instance UID"); // DSO derived from this image
		String referencedSeriesInstanceUID = getDicomSeriesUIDFromImageUID(referencedSOPInstanceUID); // Series ID for same
		String referencedStudyInstanceUID = dsoStudyInstanceUID; // Will be same study as DSO

		log.info("Generating AIM file for DSO series " + dsoSeriesInstanceUID + " for patient " + patientName);
		log.info("SOP Class UID=" + sopClassUID);
		log.info("DSO Study Instance UID=" + dsoStudyInstanceUID);
		log.info("DSO Series Instance UID=" + dsoSeriesInstanceUID);
		log.info("DSO SOP Instance UID=" + dsoSOPInstanceUID);
		log.info("Referenced SOP Instance UID=" + referencedSOPInstanceUID);
		log.info("Referenced Series Instance UID=" + referencedSeriesInstanceUID);

		ImageAnnotation imageAnnotation = new ImageAnnotation(0, "", "2000-10-17T10:22:40", "segmentation", "SEG",
				"SEG Only", "", "", "");

		SegmentationCollection sc = new SegmentationCollection();
		sc.AddSegmentation(new Segmentation(0, dsoSOPInstanceUID, sopClassUID, referencedSOPInstanceUID, 1));
		imageAnnotation.setSegmentationCollection(sc);

		DICOMImageReference originalDICOMImageReference = PluginAIMUtil.createDICOMImageReference(
				referencedStudyInstanceUID, referencedSeriesInstanceUID, referencedSOPInstanceUID);
		imageAnnotation.addImageReference(originalDICOMImageReference);
		DICOMImageReference dsoDICOMImageReference = PluginAIMUtil.createDICOMImageReference(dsoStudyInstanceUID,
				dsoSeriesInstanceUID, dsoSOPInstanceUID);
		imageAnnotation.addImageReference(dsoDICOMImageReference);

		Polyline polyline = new Polyline();
		polyline.setCagridId(0);
		polyline.setIncludeFlag(false);
		polyline.setShapeIdentifier(0);
		polyline.addSpatialCoordinate(new TwoDimensionSpatialCoordinate(0, 0, "0", 0, 2693.0, 1821.0));
		polyline.addSpatialCoordinate(new TwoDimensionSpatialCoordinate(0, 1, "0", 0, 3236.0, 1821.0));
		polyline.addSpatialCoordinate(new TwoDimensionSpatialCoordinate(0, 2, "0", 0, 3236.0, 2412.0));
		polyline.addSpatialCoordinate(new TwoDimensionSpatialCoordinate(0, 3, "0", 0, 2693.0, 2412.0));
		polyline.setLineStyle("lineStyle");
		imageAnnotation.addGeometricShape(polyline);

		Person person = new Person();
		person.setSex("F"); // TODO
		person.setBirthDate("1965-02-12T00:00:00"); // TODO
		person.setId(patientID);
		person.setName(patientName);
		person.setCagridId(0);
		imageAnnotation.addPerson(person);

		log.info("Saving AIM file for DSO series " + dsoSeriesInstanceUID + " with ID "
				+ imageAnnotation.getUniqueIdentifier());
		try {
			saveImageAnnotationToServer(imageAnnotation);
		} catch (AimException e) {
			log.warning("Exception saving AIM file for DSO series " + dsoSeriesInstanceUID, e);
		}

		/*
		 * ServerEventUtil.postEvent(username, "DSOReady", imageAnnotation.getUniqueIdentifier(), aimName, patientID,
		 * patientName, "", "", "");
		 */
	}

	private BufferedImage generateTransparentImage(BufferedImage source)
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

	public static Image makeColorTransparent(BufferedImage im, final Color color)
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

	public static Image makeColorSemiTransparent(BufferedImage im, final Color color)
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

	private void insertEpadFile(EpadDatabaseOperations epadDatabaseOperations, File outputFile)
	{
		Map<String, String> epadFilesTable = Dcm4CheeDatabaseUtils.createEPadFilesTableData(outputFile);
		epadFilesTable.put("file_status", "" + SeriesProcessingStatus.IN_PIPELINE.getCode());
		epadDatabaseOperations.insertEpadFileRecord(epadFilesTable);
	}

	private static Map<String, String> readTagsFromDicomFile(File dicomInputFile)
	{
		Map<String, String> retVal = new HashMap<String, String>();
		retVal.put("study-id", "not-found");
		retVal.put("series-id", "not-found");
		retVal.put("sop-inst-id", "not-found");
		retVal.put("ref-sop-inst-id", "not-found"); // sopInstanceUID of the Segment in file:

		try {
			AttributeList attbList = PixelMedUtils.readAttributeListFromDicomFile(dicomInputFile.getAbsolutePath());

			String studyIUID = Attribute.getSingleStringValueOrEmptyString(attbList, TagFromName.StudyInstanceUID);
			String seriesIUID = Attribute.getSingleStringValueOrEmptyString(attbList, TagFromName.SeriesInstanceUID);
			String imageID = Attribute.getSingleStringValueOrEmptyString(attbList, TagFromName.SOPInstanceUID);
			// TODO Not there - String refID = Attribute.getSingleStringValueOrEmptyString(attbList,
			// TagFromName.ReferencedSOPInstanceUID);

			retVal.put("study-id", studyIUID);
			retVal.put("series-id", seriesIUID);
			retVal.put("sop-inst-id", imageID);
			// retVal.put("ref-sop-inst-id",refID);//ToDo: Is the tag: "Referenced SOP Instance UID" correct?
			retVal.put("ref-sop-inst-id", imageID);

			// NOTE: This is for debugging. Is this the frame # of the segmentation object? Need to compare to InstanceNumber.
			// String segmentNumber = Attribute.getSingleStringValueOrEmptyString(attbList, TagFromName.SegmentNumber);
			// String instanceNumber = Attribute.getSingleStringValueOrEmptyString(attbList, TagFromName.InstanceNumber);
			// logger.info("[TEMP] Segmentation Object. Is segmentNumber Tag there? Segment Number=[" + segmentNumber + "]."
			// + " Is it the same as the Instance Number Tag? Instance Number=[" + instanceNumber + "].");
		} catch (Exception e) {
			log.warning("Failed to read DICOM header tags of DSO " + dicomInputFile.getAbsolutePath(), e);
		}
		return retVal;
	}

	@Override
	public File getInputFile()
	{
		return dicomInputFile;
	}

	@Override
	public String getTagFilePath()
	{
		// ToDo: verify that path here. It might be in
		return segObjectOutputFile.getAbsolutePath().replaceAll("\\.png", ".tag");
	}

	@Override
	public String getTaskType()
	{
		return "DicomSegmentationObject";
	}

	private String saveImageAnnotationToServer(ImageAnnotation aim) throws AimException
	{
		String res = "";

		if (aim.getCodeValue() != null) { // First, write a backup file
			String tempXmlPath = this.baseAnnotationDir + "temp-" + aim.getUniqueIdentifier() + ".xml";
			String storeXmlPath = this.baseAnnotationDir + aim.getUniqueIdentifier() + ".xml";
			File tempFile = new File(tempXmlPath);
			File storeFile = new File(storeXmlPath);
			AnnotationBuilder.saveToFile(aim, tempXmlPath, xsdFilePath);
			res = AnnotationBuilder.getAimXMLsaveResult();
			log.info("AnnotationBuilder.saveToFile result: " + res);
			if (storeFile.exists()) {
				storeFile.delete();
			}
			tempFile.renameTo(storeFile);

			AnnotationBuilder.saveToServer(aim, eXistServerUrl, namespace, eXistCollection, xsdFilePath, eXistUsername,
					eXistPassword);
			res = AnnotationBuilder.getAimXMLsaveResult();
			log.info("AnnotationBuilder.saveToServer result: " + res);
		}
		return res;
	}

	private static String convertDicomFileNameToImageUID(String currFileName)
	{
		int lastDotIndex = currFileName.lastIndexOf('.');

		String uidPart = currFileName;
		if (lastDotIndex > 0) {
			uidPart = currFileName.substring(0, lastDotIndex);
		}
		uidPart = uidPart.replaceAll("_", ".");
		return uidPart;
	}

	// TODO Replace this with direct call to database
	private String getDicomSeriesUIDFromImageUID(String imageUID)
	{
		String url = "http://localhost:8080/epad/segmentationpath/" + "?image_iuid=" + imageUID; // TODO
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			int statusCode = client.executeMethod(method);

			if (statusCode != -1) {
				isr = new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8");
				br = new BufferedReader(isr);
				String line;
				while ((line = br.readLine()) != null) {
					String[] cols = line.split(",");
					if (cols != null && cols.length > 1) {
						String seriesUD = cols[1];
						if (!seriesUD.equals("SeriesUID")) {
							return convertDicomFileNameToImageUID(seriesUD);
						}
					}
				}
			}
			log.warning("Cound not find seriesUID for imageUID " + imageUID);
			return "";
		} catch (Exception e) {
			log.warning("Error getting seriesUID for imageUID " + imageUID, e);
			return "";
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			method.releaseConnection();
		}
	}

	@Override
	public String getSeriesUID()
	{
		return this.seriesUID;
	}
}
