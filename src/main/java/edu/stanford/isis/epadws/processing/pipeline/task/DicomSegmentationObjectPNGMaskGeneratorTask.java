package edu.stanford.isis.epadws.processing.pipeline.task;

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

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.TagFromName;
import com.pixelmed.display.SourceImage;

import edu.stanford.hakan.aim3api.base.AimException;
import edu.stanford.hakan.aim3api.base.DICOMImageReference;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;
import edu.stanford.hakan.aim3api.base.ImageSeries;
import edu.stanford.hakan.aim3api.base.ImageStudy;
import edu.stanford.hakan.aim3api.base.Person;
import edu.stanford.hakan.aim3api.base.Polyline;
import edu.stanford.hakan.aim3api.base.Segmentation;
import edu.stanford.hakan.aim3api.base.SegmentationCollection;
import edu.stanford.hakan.aim3api.base.TwoDimensionSpatialCoordinate;
import edu.stanford.hakan.aim3api.usage.AnnotationBuilder;
import edu.stanford.isis.epad.common.dicom.DicomSegmentationObject;
import edu.stanford.isis.epad.common.dicom.DicomTagFileUtils;
import edu.stanford.isis.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.EPADResources;
import edu.stanford.isis.epadws.persistence.Database;
import edu.stanford.isis.epadws.persistence.DatabaseOperations;
import edu.stanford.isis.epadws.persistence.Dcm4CheeDatabaseUtils;
import edu.stanford.isis.epadws.processing.model.PngProcessingStatus;

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
	public String dbpath = EPADConfig.getInstance().getStringPropertyValue("dbpath");
	public String templatePath = EPADConfig.getInstance().getStringPropertyValue("baseTemplatesDir");
	public String wadoProxy = EPADConfig.getInstance().getStringPropertyValue("wadoProxy");

	private static final EPADLogger logger = EPADLogger.getInstance();
	private static final String baseDicomDirectory = EPADResources.getEPADWebServerPNGDir();

	private final File dicomInputFile;
	private final File segObjectOutputFile;

	public DicomSegmentationObjectPNGMaskGeneratorTask(File dicomInputFile, File segObjectOutputFile)
	{
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

		logger.info("Processing DSO for file " + dicomInputFile.getAbsolutePath());
		logger.info("getSegmentation study " + studyId + " series " + seriesId + " image " + imageId + " object "
				+ objectId);

		try {
			DicomSegmentationObject dicomSegmentationObject = new DicomSegmentationObject();
			SourceImage sourceImage = dicomSegmentationObject.convert(dicomInputFile.getAbsolutePath());
			int count = sourceImage.getNumberOfBufferedImages();
			File repDest = new File(baseDicomDirectory + studyId + "/" + seriesId + "/");
			repDest.mkdirs();

			DatabaseOperations queries = Database.getInstance().getDatabaseOperations();
			for (int i = 0; i < count; i++) { // Create the mask images
				BufferedImage source = sourceImage.getBufferedImage(count - i - 1);
				BufferedImage sourceWithTransparency = generateTransparentImage(source); // Generate a transparent image
				String pngUrl = baseDicomDirectory + studyId + "/" + seriesId + "/" + objectId + "-" + i + ".png";
				File sourceFile = new File(pngUrl);
				try {
					insertEpadFile(queries, sourceFile);
					ImageIO.write(sourceWithTransparency, "png", sourceFile);
					queries.updateEpadFile(pngUrl, PngProcessingStatus.DONE, 77, "");
					logger.info("Writing DSO PNG mask " + pngUrl);
				} catch (IOException e) {
					logger.warning("Failed to write DSO PNG mask ", e);
				}
				source = null;
				sourceWithTransparency = null;
			}
			File tagFile = new File(repDest.getAbsolutePath() + "/" + objectId + ".tag");
			if (!tagFile.exists()) {
				tagFile.createNewFile();
				ExecutorService taskExecutor = Executors.newFixedThreadPool(1);
				taskExecutor.execute(new DicomHeadersTask(dicomInputFile, tagFile));
				taskExecutor.shutdown();
			}
			File tagFileTemp = File.createTempFile(tagFile.getName(), ".temptag");
			tagFileTemp.createNewFile();
			DicomHeadersTask dicomHeadersTask = new DicomHeadersTask(dicomInputFile, tagFileTemp);
			dicomHeadersTask.run();
			generateAIMFile(tagFileTemp);
			System.gc();
		} catch (Exception e) {
			logger.warning("Error: when trying to write PNGs for segmentation object" + e.getMessage());
		}
	}

	/**
	 * Generate an AIM file linking to a DICOM Segmentation Object (DSO). The References SOP Instance UID in the DICOM DSO
	 * tag file identifies the image from which the segmentation object is derived from. The DICOM file does not contain
	 * the study or series identifiers for that image so we discover it by querying ePAD.
	 * <p>
	 * This AIM file actually annotates the original image NOT the DSO. We link to the DSO by storing the DOS's image ID
	 * in the sopInstanceUID field in a Segmentation element. When processing this AIM file, the user can find the DSO's
	 * series by searching using this imageID.
	 * 
	 * @param dicomTagFile
	 * @throws Exception
	 */
	private void generateAIMFile(File dicomTagFile) throws Exception
	{
		Map<String, String> dicomTags = DicomTagFileUtils.readTagFile(dicomTagFile);

		String patientID = dicomTags.get("Patient ID");
		String patientName = dicomTags.get("Patient's Name");
		String sopClassUID = dicomTags.get("SOP Class UID");
		String dsoStudyInstanceUID = dicomTags.get("Study Instance UID"); // Study ID of the DSO (same as original image)
		String dsoSeriesInstanceUID = dicomTags.get("Series Instance UID"); // Series ID of DSO (DSO gets new series)
		String dsoSopInstanceUID = dicomTags.get("SOP Instance UID"); // Image ID of DSO
		String referencedSOPInstanceUID = dicomTags.get("Referenced SOP Instance UID"); // Image from which DSO is derived
		String referencedSeriesInstanceUID = getDicomSeriesUIDFromImageUID(referencedSOPInstanceUID); // Series ID for same
		String referencedStudyInstanceUID = dsoStudyInstanceUID; // Will be same study as DSO

		logger.info("Patient ID=" + patientID);
		logger.info("Patient's Name=" + patientName);
		logger.info("SOP Class UID=" + sopClassUID);
		logger.info("DSO Study Instance UID=" + dsoStudyInstanceUID);
		logger.info("DSO Series Instance UID=" + dsoSeriesInstanceUID);
		logger.info("DSO SOP Instance UID=" + dsoSopInstanceUID);
		logger.info("Referenced SOP Instance UID=" + referencedSOPInstanceUID);
		logger.info("Referenced Series Instance UID=" + referencedSeriesInstanceUID);

		ImageAnnotation imageAnnotation = new ImageAnnotation(0, "", "2012-10-17T10:22:40", "segmentation", "SEG",
				"SEG Only", "", "", "");

		SegmentationCollection sc = new SegmentationCollection();
		sc.AddSegmentation(new Segmentation(0, dsoSopInstanceUID, sopClassUID, referencedSOPInstanceUID, 1));
		imageAnnotation.setSegmentationCollection(sc);

		DICOMImageReference dicomImageReference = new DICOMImageReference();
		dicomImageReference.setCagridId(0);

		ImageStudy imageStudy = new ImageStudy();
		imageStudy.setCagridId(0);
		imageStudy.setInstanceUID(referencedStudyInstanceUID);
		imageStudy.setStartDate("2012-01-16T00:00:00");
		imageStudy.setStartTime("12:45:34");

		ImageSeries imageSeries = new ImageSeries();
		imageSeries.setCagridId(0);
		imageSeries.setInstanceUID(referencedSeriesInstanceUID);

		edu.stanford.hakan.aim3api.base.Image image = new edu.stanford.hakan.aim3api.base.Image();
		image.setCagridId(0);
		image.setSopClassUID("112233"); // TODO
		image.setSopInstanceUID(referencedSOPInstanceUID);

		imageSeries.addImage(image); // Add Image to ImageSeries
		imageStudy.setImageSeries(imageSeries); // Add ImageSeries to ImageStudy
		dicomImageReference.setImageStudy(imageStudy); // Add ImageStudy to ImageReference
		imageAnnotation.addImageReference(dicomImageReference); // Add DicomImageReference to ImageAnnotation

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
		person.setSex("F");
		person.setBirthDate("1965-02-12T00:00:00");
		person.setId(patientID);
		person.setName(patientName);
		person.setCagridId(0);
		imageAnnotation.addPerson(person);

		logger.info("Saving AIM file to server " + imageAnnotation.getUniqueIdentifier());
		try {
			saveImageAnnotationToServer(imageAnnotation);
		} catch (AimException e) {
			logger.warning("Exception saving AIM file to server", e);
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

	private void insertEpadFile(DatabaseOperations queries, File outputFile)
	{
		Map<String, String> epadFilesTable = Dcm4CheeDatabaseUtils.createEPadFilesTableData(outputFile);
		epadFilesTable.put("file_status", "" + PngProcessingStatus.IN_PIPELINE.getCode());
		queries.insertEpadFile(epadFilesTable);
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
			// String refID = Attribute.getSingleStringValueOrEmptyString(attbList, TagFromName.ReferencedSOPInstanceUID);

			retVal.put("study-id", studyIUID);
			retVal.put("series-id", seriesIUID);
			retVal.put("sop-inst-id", imageID);
			// retVal.put("ref-sop-inst-id",refID);//ToDo: Is the tag: "Referenced SOP Instance UID" correct?
			retVal.put("ref-sop-inst-id", imageID);

			// NOTE: This is for debugging. Is this the frame # of the segmentation object? Need to compare to InstanceNumber.
			String segmentNumber = Attribute.getSingleStringValueOrEmptyString(attbList, TagFromName.SegmentNumber);
			String instanceNumber = Attribute.getSingleStringValueOrEmptyString(attbList, TagFromName.InstanceNumber);

			logger.info("[TEMP] Segmentation Object. Is segmentNumber Tag there? Segment Number=[" + segmentNumber + "]."
					+ " Is it the same as the Instance Number Tag? Instance Number=[" + instanceNumber + "].");
		} catch (Exception e) {
			logger.warning("Failed to read Dicom header tags of segmentation object: " + dicomInputFile.getAbsolutePath(), e);
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
			logger.info("AnnotationBuilder.saveToFile result: " + res);
			if (storeFile.exists()) {
				storeFile.delete();
			}
			tempFile.renameTo(storeFile);

			AnnotationBuilder.saveToServer(aim, eXistServerUrl, namespace, eXistCollection, xsdFilePath, eXistUsername,
					eXistPassword);
			res = AnnotationBuilder.getAimXMLsaveResult();
			logger.info("AnnotationBuilder.saveToServer result: " + res);
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
	private String getDicomSeriesUIDFromImageUID(String imageUID) throws Exception
	{
		String url = "http://localhost:8080/segmentationpath/" + "?image_iuid=" + imageUID;
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		int statusCode = client.executeMethod(method);

		if (statusCode != -1) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] cols = line.split(",");
				if (cols != null && cols.length > 1) {
					String seriesUD = cols[1];
					if (!seriesUD.equals("SeriesUID")) {
						return convertDicomFileNameToImageUID(seriesUD);
					}
				}
			}
		}
		return null;
	}
}

// <?xml version="1.0"?>
// <ImageAnnotation xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" dateTime="2012-10-24T11:03:31"
// uniqueIdentifier="1.2.276.0.7230010.3.1.3.2225507198.6232.1351101811.798"
// xmlns="gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM" codingSchemeDesignator="na" codeMeaning="na"
// aimVersion="3.0" xsi:schemaLocation="gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM AIM_v3_rv11_XML.xsd"
// codeValue="na" name="NA__2012-10-24T11:03:31" cagridId="0">
// <user>
// <User numberWithinRoleOfClinicalTrial="1" loginName="" roleInTrial="Performing" name="" cagridId="0"/>
// </user>
// <equipment>
// <Equipment softwareVersion="Slicer 4.1.0-2012-10-22 r21223 Reporting 7cbc2f3"
// manufacturerName="Brigham and Women's Hospital, Surgical Planning Lab" cagridId="0"
// manufacturerModelName="3D_Slicer_4_Reporting"/>
// </equipment>
// <anatomicEntityCollection>
// <AnatomicEntity annotatorConfidence="0.0" codingSchemeDesignator="3DSlicer" codeMeaning="tissue" codeValue="1"
// label="tissue" cagridId="0"/>
// </anatomicEntityCollection>
// <segmentationCollection>
// <Segmentation sopClassUID="1.2.840.10008.5.1.4.1.1.66.4" referencedSopInstanceUID="" segmentNumber="1"
// sopInstanceUID="1.2.276.0.7230010.3.1.4.2225507198.6232.1351101812.800" cagridId="0"/>
// </segmentationCollection>
// <imageReferenceCollection/>
// <geometricShapeCollection/>
// <person>
// <Person birthDate="1928-04-17T00:00:00" id="NA" name="NA" sex="M" cagridId="0"/>
// </person>
// </ImageAnnotation>

// @Get
// public ArrayList<String> getSegmentation() {
// SourceImage sourceImage = null;
// DicomSegmentationObject dso = null;
// AttributeList list = null;
// GeometryOfVolume geometry = null;
//
// ArrayList<String> retPngs = new ArrayList<String>();
// List<String> pngs = null;
// String encoded = null;
// byte[] bytes;
//
// String studyId = (String) getRequestAttributes().get("id1");
// String seriesId = (String) getRequestAttributes().get("id2");
// String imageId = (String) getRequestAttributes().get("id3");
// String objectId = (String) getRequestAttributes().get("id4");
//
// studyId = studyId.replaceAll("\\.", "_");
// seriesId = seriesId.replaceAll("\\.", "_");
// imageId = imageId.replaceAll("\\.", "_");
// objectId = objectId.replaceAll("\\.", "_");
//
// logger.info("getSegmentation study " + studyId + " series " + seriesId + " image " + imageId+ " object " + objectId
// );
//
// // test out some pixelmed calls
// dso = new DicomSegmentationObject();
// // should be able to get one image url to do the attribute thing here
//
// String imageUrl = baseDicomDirectory + studyId + "/" + seriesId + "/" + imageId + ".dcm";
// String objectUrl = baseDicomDirectory + studyId + "/" + seriesId + "/segmentation/" + objectId + ".dcm";
//
// File imageFile = new File(imageUrl);
// File objectFile = new File(objectUrl);
//
// try {
// sourceImage = dso.convert(objectUrl);
// int count = sourceImage.getNumberOfBufferedImages();
//
// for (int i=0;i<count;i++) {
// BufferedImage source = sourceImage.getBufferedImage(i);
// String pngUrl = baseDicomDirectory + studyId + "/" + seriesId + "/segmentation/" + objectId + "-" + i + ".png";
// File sourceFile = new File(pngUrl);
// try {
// ImageIO.write(source, "png", sourceFile);
// bytes = DicomSegmentationObject.getFileBytes(sourceFile);
// encoded = DicomSegmentationObject.base64EncodeBytes(bytes);
// retPngs.add(encoded);
// } catch (IOException e) {
// logger.warning("failed to write segmentation png", e);
// }
// }
// } catch (Exception e) {
// logger.info("Error: when trying to write pngs for segmentation object" + e.getMessage());
// }
//
// // test out some pixelmed calls
// list = dso.getAttributes(imageUrl);
// if (list != null) logger.info(list.toString());
// //geometry = dso.getGeometry(list);
// //if (geometry != null) logger.info(geometry.toString());
//
//
// return retPngs;
// }
//

