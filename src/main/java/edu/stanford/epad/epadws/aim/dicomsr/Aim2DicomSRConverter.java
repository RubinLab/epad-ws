package edu.stanford.epad.epadws.aim.dicomsr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.junit.internal.runners.model.EachTestNotifier;

import com.pixelmed.dicom.TagFromName;

import edu.stanford.epad.common.dicom.DCM4CHEEUtil;
import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.plugins.PluginAIMUtil;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.internal.DICOMElement;
import edu.stanford.epad.dtos.internal.DICOMElementList;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;
import edu.stanford.hakan.aim4api.base.CD;
import edu.stanford.hakan.aim4api.base.CalculationEntity;
import edu.stanford.hakan.aim4api.base.DicomImageReferenceEntity;
import edu.stanford.hakan.aim4api.base.DicomSegmentationEntity;
import edu.stanford.hakan.aim4api.base.II;
import edu.stanford.hakan.aim4api.base.Image;
import edu.stanford.hakan.aim4api.base.ImageAnnotation;
import edu.stanford.hakan.aim4api.base.ImageAnnotationCollection;
import edu.stanford.hakan.aim4api.base.ImageCollection;
import edu.stanford.hakan.aim4api.base.ImageSeries;
import edu.stanford.hakan.aim4api.base.ImageStudy;

/**
 * This class gets an aim and creates a dicom sr object using dcmqi
 * @author emelalkim
 *
 */
public class Aim2DicomSRConverter {
	private static final EPADLogger log = EPADLogger.getInstance();
	Dcm4CheeDatabaseOperations dcm4cheOp= Dcm4CheeDatabase.getInstance()
			.getDcm4CheeDatabaseOperations();
	public static void runtid1500writer(DicomSRMetadata meta,String imageDir, String compositeDir, String outputFileName ) throws Exception{
		log.info(meta.toJSON());
		
		File metaFile=File.createTempFile("meta"+System.currentTimeMillis(),".json");
		log.info("Writing json to temp file "+ metaFile.getAbsolutePath());
		FileWriter fw = new FileWriter(metaFile);
		fw.write(meta.toJSON());
		fw.close();
		if (imageDir==null)
			imageDir="/home/epad/tid1500test";
		if (compositeDir==null)
			compositeDir="/home/epad/tid1500test"; //for testing
		if (outputFileName==null) 
			outputFileName="liver_sr_test.dcm";
		runtid1500writer(metaFile.getAbsolutePath(), imageDir, compositeDir, outputFileName, true);
	}
	
	
	/**
	 * run tid1500 writer from DicomProxy/etc/scripts/bin
	 * @param metadataPath
	 * @param imageDir
	 * @param compositeDir
	 * @param outputFileName
	 * @param throwException
	 * @throws Exception
	 */
	public static void runtid1500writer(String metadataPath, String imageDir, String compositeDir,String outputFileName, boolean throwException) throws Exception
	{
//		./tid1500writer 
		//--metaDataFileName /Users/emelalkim/Desktop/test.json 
		//--compositeContextDataDir /Users/emelalkim/Documents/workspace/rsna/dcmqi/data/ct-3slice/ -->path for seg and rwvm dcm files
		//--imageLibraryDataDir /Users/emelalkim/Documents/workspace/rsna/dcmqi/data/ct-3slice/  -->path for actual images
		//--outputFileName liver_sr_test.dcm 
		
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			
			log.info("Sending file - command: ./tid1500writer "+ "--metaDataFileName " + metadataPath + " --compositeContextDataDir " +  compositeDir + " --imageLibraryDataDir " + imageDir + " --outputFileName " + outputFileName);
			String[] command = { "./tid1500writer","--metaDataFileName" ,metadataPath ,"--compositeContextDataDir", compositeDir , "--imageLibraryDataDir", imageDir, "--outputFileName", outputFileName };
			ProcessBuilder pb = new ProcessBuilder(command);
			
			String dicomBinDirectoryPath = EPADConfig.getEPADWebServerDICOMScriptsDir() + "bin/";
			File script = new File(dicomBinDirectoryPath, "tid1500writer");
			if (!script.exists())
				dicomBinDirectoryPath = EPADConfig.getEPADWebServerDICOMBinDir();
			log.info("tid1500writer binary directory: " + dicomBinDirectoryPath);
			script = new File(dicomBinDirectoryPath, "tid1500writer");
			if (!script.exists()) {
				log.warning("tid1500writer does not exist cannot continue!" );
				return;
			}
				
			
			pb.directory(new File(dicomBinDirectoryPath));
			Process process = pb.start();
			process.getOutputStream();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
				log.info("./tid1500writer output: " + line);
			}

			try {
				int exitValue = process.waitFor();
				log.info("tid1500writer exit value is: " + exitValue);
				

			} catch (InterruptedException e) {
				log.warning("Error executing tid1500writer " , e);
			}
			String cmdLineOutput = sb.toString();

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + cmdLineOutput);
			}
		} catch (Exception e) {
			if (e instanceof IllegalStateException && throwException) {
				throw e;
			}
			log.warning("tid1500writer failed: " + e.getMessage());
			if (throwException) {
				throw new IllegalStateException("tid1500writer failed", e);
			}
		} catch (OutOfMemoryError oome) {
			log.warning("tid1500writer ran out of memory", oome);
			if (throwException) {
				throw new IllegalStateException("tid1500writer ran out of memory", oome);
			}
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
		}
	}
	
	public Aim2DicomSRConverter() {
	}
	
	private String getDICOMFileName(DICOMFileDescription dicomFileDescription)
	{
		return dicomFileDescription.filePath.substring(dicomFileDescription.filePath.lastIndexOf("/")+1);
	}
	private String getDICOMFilePath(DICOMFileDescription dicomFileDescription)
	{
		return EPADConfig.dcm4cheeDirRoot + dicomFileDescription.filePath;
	}
	
	private File downloadRemoteDICOM(DICOMFileDescription dicomFileDescription)
	{
		String imageUID = dicomFileDescription.imageUID;
		String seriesUID = dicomFileDescription.seriesUID;
		try {
			log.info("Downloading remote DICOM file with image " + imageUID + " for series UID " + seriesUID);
			File downloadedDICOMFile = File.createTempFile(imageUID, ".tmp");
			DCM4CHEEUtil.downloadDICOMFileFromWADO(dicomFileDescription, downloadedDICOMFile);
			return downloadedDICOMFile;
		} catch (Exception e) {
			log.warning("Exception when downloading DICOM file with series UID " + seriesUID + " and image UID "
					+ dicomFileDescription.imageUID, e);
		}
		log.warning("Error downloading DICOM file with series UID " + seriesUID + " and image UID "
				+ dicomFileDescription.imageUID);
		return null;
	}
	
	
	public String getPath4Series(String seriesUID) {
		Dcm4CheeDatabaseOperations dcm4cheOp= Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		Set<DICOMFileDescription> dicomFilesDescriptions = dcm4cheOp.getDICOMFilesForSeries(seriesUID);
		DICOMFileDescription dicomFileDescription =dicomFilesDescriptions.iterator().next();
		String imagePath = getDICOMFilePath(dicomFileDescription);
		File inputDICOMFile = new File(imagePath);

		// If the file does not exist locally (because it is stored on another file system), download it.
		if (!inputDICOMFile.exists()) {
			inputDICOMFile = downloadRemoteDICOM(dicomFileDescription);
			imagePath = inputDICOMFile.getAbsolutePath();
		}
		
		imagePath = imagePath.substring(0, imagePath.lastIndexOf('/'));
		return imagePath;
	}
	
	
	public DICOMFileDescription getFirstInstanceUIDInSeries(String seriesUID) {
		Set<DICOMFileDescription> dicomFilesDescriptions = dcm4cheOp.getDICOMFilesForSeries(seriesUID);
		DICOMFileDescription first = dicomFilesDescriptions.iterator().next();
		return first;
		
	}
	public String getPathandFilename4DSOSeries(String seriesUID, ArrayList<String> filenames) {
		Set<DICOMFileDescription> dicomFilesDescriptions = dcm4cheOp.getDICOMFilesForSeries(seriesUID);
		DICOMFileDescription lastDSO = dicomFilesDescriptions.iterator().next();
		String createdTime = lastDSO.createdTime;
		for (DICOMFileDescription dicomFileDescription : dicomFilesDescriptions) {
			if (createdTime.compareTo(dicomFileDescription.createdTime) < 0)
			{
				createdTime = dicomFileDescription.createdTime;
				lastDSO = dicomFileDescription;
			}
		}
		log.info("filename is:"+getDICOMFileName(lastDSO));
		filenames.add(getDICOMFileName(lastDSO));
		
		
		//get the path of the last
		if (lastDSO!=null) {
			String imagePath = getDICOMFilePath(lastDSO);
			File inputDICOMFile = new File(imagePath);
	
			// If the file does not exist locally (because it is stored on another file system), download it.
			if (!inputDICOMFile.exists()) {
				inputDICOMFile = downloadRemoteDICOM(lastDSO);
				imagePath = inputDICOMFile.getAbsolutePath();
			}
			
			imagePath = imagePath.substring(0, imagePath.lastIndexOf('/'));
			return imagePath;
		}
		return null;
	}
	
	public String getPathandFilenames4Series(String seriesUID, ArrayList<String> filenames) {
		
		Set<DICOMFileDescription> dicomFilesDescriptions = dcm4cheOp.getDICOMFilesForSeries(seriesUID);
		DICOMFileDescription dicomFileDescription=null;
		Iterator<DICOMFileDescription> it=dicomFilesDescriptions.iterator();
		while (it.hasNext()) {
			dicomFileDescription =it.next();
			log.info("filename is:"+getDICOMFileName(dicomFileDescription));
			filenames.add(getDICOMFileName(dicomFileDescription));
		}
		//get the path of the last
		if (dicomFileDescription!=null) {
			String imagePath = getDICOMFilePath(dicomFileDescription);
			File inputDICOMFile = new File(imagePath);
	
			// If the file does not exist locally (because it is stored on another file system), download it.
			if (!inputDICOMFile.exists()) {
				inputDICOMFile = downloadRemoteDICOM(dicomFileDescription);
				imagePath = inputDICOMFile.getAbsolutePath();
			}
			
			imagePath = imagePath.substring(0, imagePath.lastIndexOf('/'));
			return imagePath;
		}
		return null;
	}
	
	public void Aim2DicomSR(String aimID,String projectID) {
		String imagePath=null,segPath=null,outputFileName=null; 
		
		try {
			ImageAnnotationCollection iac = PluginAIMUtil.getImageAnnotationCollectionFromServer(aimID, projectID);
			DicomSRMetadata meta=new DicomSRMetadata();
			if (iac != null) {
				// Get DICOM path					
				DicomImageReferenceEntity dicomImageReferenceEntity = (DicomImageReferenceEntity) iac
																		.getImageAnnotations().get(0).getImageReferenceEntityCollection().get(0);
				ImageStudy imageStudy = dicomImageReferenceEntity.getImageStudy();
				String studyUID = imageStudy.getInstanceUid().getRoot();
				String seriesUID = imageStudy.getImageSeries().getInstanceUid().getRoot();
				String seedImageUID = imageStudy.getImageSeries().getImageCollection().get(0).getSopInstanceUid().getRoot();
				log.info("the image " +seedImageUID+ " series "+ seriesUID);
				
				ArrayList<String> imgFilenames=new ArrayList<>();
				imagePath=getPathandFilenames4Series(seriesUID,imgFilenames);
				log.info("image path is "+imagePath);
				meta.setImageLibrary(imgFilenames);
				
				//get the segmentation
				DicomSegmentationEntity segEntity= (DicomSegmentationEntity)iac.getImageAnnotations().get(0).getSegmentationEntityCollection().get(0);
				String dsoImageUID= segEntity.getSopInstanceUid().getRoot();
				log.info("segmentation uid:"+dsoImageUID);
				
				String dsoSeriesUID = PluginAIMUtil.getDSOSeriesUID(aimID);
				log.info("dsoseriesuid:"+dsoSeriesUID);
				ArrayList<String> dsoFilenames=new ArrayList<>();
				segPath=getPathandFilename4DSOSeries(dsoSeriesUID, dsoFilenames);
				log.info("dso path is "+segPath);
				
				meta.setCompositeContext(dsoFilenames);
				
				meta.observerContext.setPersonObserverName(iac.getUser().getName().getValue());
				//set the series desc as annotation name
				meta.setSeriesDescription(iac.getImageAnnotations().get(0).getName().getValue());
				//clear the measurements. there are defaults for test
				meta.Measurements.clear();
				//create the measurement group. just put the series and segmentation image uid. leave the rest to the default
				MeasurementGroup mgrp=new MeasurementGroup();
				mgrp.setSourceSeriesForImageSegmentation(seriesUID);
				mgrp.setSegmentationSOPInstanceUID(dsoImageUID);
				//get the finding and finding site from dso header
				DICOMElementList tags= Dcm4CheeQueries.getDICOMElementsFromWADO(studyUID, dsoSeriesUID, dsoImageUID);
				ControlledTerm category= new ControlledTerm();
				ControlledTerm type= new ControlledTerm();
				for (int i=0; i< tags.ResultSet.totalRecords; i++) {
					DICOMElement tag=tags.ResultSet.Result.get(i);
					log.info ("tag code="+tag.tagCode+" value="+tag.value+" parent="+tag.parentSequenceName);
					if (tag.tagCode.equals(PixelMedUtils.CodeValueCode)) {
						log.info ("value parent for"+tag.tagName + " = "+ tag.parentSequenceName);
						if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Category Code Sequence")) {
							category.setCodeValue(tag.value);
						}else if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Type Code Sequence")) {
							type.setCodeValue(tag.value);
						}
					}
					if (tag.tagCode.equals(PixelMedUtils.CodeMeaningCode)) {
						log.info ("meaning parent for"+tag.tagName + " = "+ tag.parentSequenceName);
						if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Category Code Sequence")) {
							category.setCodeMeaning(tag.value);
						}else if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Type Code Sequence")) {
							type.setCodeMeaning(tag.value);
						}
					}
					if (tag.tagCode.equals(PixelMedUtils.CodingSchemeDesignatorCode)) {
						log.info ("designator parent for"+tag.tagName + " = "+ tag.parentSequenceName);
						if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Category Code Sequence")) {
							category.setCodingSchemeDesignator(tag.value);
						}else if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Type Code Sequence")) {
							type.setCodingSchemeDesignator(tag.value);
						}
					}
				}
				log.info("category is "+category.toJSON());
				log.info("type is "+type.toJSON());
				mgrp.Finding=category;
				mgrp.FindingSite=type;
				
				//add the measurements from aim
				mgrp.measurementItems.clear();
				for (CalculationEntity cal:iac.getImageAnnotations().get(0).getCalculationEntityCollection().getCalculationEntityList()) {
					log.info("calculation is "+cal.getDescription());
					ControlledTerm derivationMod= new ControlledTerm(cal.getListTypeCode().get(0));//???
					ControlledTerm quantity= new ControlledTerm(cal.getListTypeCode().get(0));
					String value=cal.getCalculationResultCollection().getExtendedCalculationResultList().get(0).getCalculationDataCollection().get(0).getValue().getValue();
					//use this for now. PROBLEM. aim does not have controlled term for this
					ControlledTerm units= new ControlledTerm("[hnsf'U]", "UCUM", "Hounsfield unit");
					MeasurementItem mit=new MeasurementItem();
					//fails if I send null but he has samples with no derivation.
					//nothing wrong with the produced json. something wrong with tid1500writer. 
					//it also does not write mean and standard dev
					//TODO find a way to not send it or just remove from class
					mit.setDerivationModifier(derivationMod);
					mit.setQuantity(quantity);
					mit.setUnits(units);
					mit.setValue(value);
					mgrp.measurementItems.add(mit);
				}
				
				//add the measurements group to the object
				meta.Measurements.add(mgrp);
				
				outputFileName = "dicomsr"+System.currentTimeMillis()+".dcm";
				
			}
			runtid1500writer(meta, imagePath, segPath, outputFileName);
			
		} catch (IOException e) {
			log.warning("IO Error ",e);
		} catch (Exception e) {
			log.warning("run tid1500 exception ",e);
		}
		
	}
	
	
	public void DicomSR2Aim(DicomSRMetadata meta) {
		
		
		String imagePath=null,segPath=null,outputFileName=null; 
		
		try {
			
			if (meta==null) {
				log.info("Metadata is empty. Exiting the conversion process");
				return;
			}
			ImageAnnotationCollection iac = new ImageAnnotationCollection();
			ImageAnnotation ia=new ImageAnnotation();

			ArrayList<String> imageNames= meta.getImageLibrary();
			
			//get the first measurement group. TODO what if there are more than one
			MeasurementGroup meas=meta.getMeasurements().get(0);
			String sourceSeriesUID=meas.SourceSeriesForImageSegmentation;
			String dsoInstanceUID=meas.segmentationSOPInstanceUID;
			DICOMFileDescription firstImage=getFirstInstanceUIDInSeries(sourceSeriesUID);
			
			//form the segmentation entity
			DicomSegmentationEntity segEntity=new DicomSegmentationEntity();
			segEntity.setReferencedSopInstanceUid(new II(firstImage.imageUID));
			segEntity.setSegmentNumber(meas.getReferencedSegment());
			segEntity.setSopInstanceUid(new II(dsoInstanceUID));
			segEntity.setSopClassUid(new II("1.2.840.10008.5.1.4.1.1.66.4")); //segmentation
			
					
			DicomImageReferenceEntity dicomImageReferenceEntity  = new DicomImageReferenceEntity();
			ImageStudy study = new ImageStudy();
			study.setInstanceUid(new II(firstImage.studyUID));
			ImageSeries series=new ImageSeries();
			series.setInstanceUid(new II(sourceSeriesUID));
			Image image=new Image();
			image.setSopInstanceUid(new II(firstImage.imageUID));
			image.setSopClassUid(new II("tbd"));// how to get it
			ImageCollection imageCol=new ImageCollection();
			imageCol.addImage(image);
			series.setImageCollection(imageCol);
			series.setModality(new CD(firstImage.modality,firstImage.modality,"tbd"));
			
			
			study.setImageSeries(series);
			dicomImageReferenceEntity.setImageStudy(study);
			
			if (iac != null) {
				// Get DICOM path					
				DicomImageReferenceEntity dicomImageReferenceEntity = (DicomImageReferenceEntity) iac
																		.getImageAnnotations().get(0).getImageReferenceEntityCollection().get(0);
				ImageStudy imageStudy = dicomImageReferenceEntity.getImageStudy();
				String studyUID = imageStudy.getInstanceUid().getRoot();
				String seriesUID = imageStudy.getImageSeries().getInstanceUid().getRoot();
				String seedImageUID = imageStudy.getImageSeries().getImageCollection().get(0).getSopInstanceUid().getRoot();
				log.info("the image " +seedImageUID+ " series "+ seriesUID);
				
				ArrayList<String> imgFilenames=new ArrayList<>();
				imagePath=getPathandFilenames4Series(seriesUID,imgFilenames);
				log.info("image path is "+imagePath);
				meta.setImageLibrary(imgFilenames);
				
				//get the segmentation
				DicomSegmentationEntity segEntity= (DicomSegmentationEntity)iac.getImageAnnotations().get(0).getSegmentationEntityCollection().get(0);
				String dsoImageUID= segEntity.getSopInstanceUid().getRoot();
				log.info("segmentation uid:"+dsoImageUID);
				
				String dsoSeriesUID = PluginAIMUtil.getDSOSeriesUID(aimID);
				log.info("dsoseriesuid:"+dsoSeriesUID);
				ArrayList<String> dsoFilenames=new ArrayList<>();
				segPath=getPathandFilename4DSOSeries(dsoSeriesUID, dsoFilenames);
				log.info("dso path is "+segPath);
				
				meta.setCompositeContext(dsoFilenames);
				
				meta.observerContext.setPersonObserverName(iac.getUser().getName().getValue());
				//set the series desc as annotation name
				meta.setSeriesDescription(iac.getImageAnnotations().get(0).getName().getValue());
				//clear the measurements. there are defaults for test
				meta.Measurements.clear();
				//create the measurement group. just put the series and segmentation image uid. leave the rest to the default
				MeasurementGroup mgrp=new MeasurementGroup();
				mgrp.setSourceSeriesForImageSegmentation(seriesUID);
				mgrp.setSegmentationSOPInstanceUID(dsoImageUID);
				//get the finding and finding site from dso header
				DICOMElementList tags= Dcm4CheeQueries.getDICOMElementsFromWADO(studyUID, dsoSeriesUID, dsoImageUID);
				ControlledTerm category= new ControlledTerm();
				ControlledTerm type= new ControlledTerm();
				for (int i=0; i< tags.ResultSet.totalRecords; i++) {
					DICOMElement tag=tags.ResultSet.Result.get(i);
					log.info ("tag code="+tag.tagCode+" value="+tag.value+" parent="+tag.parentSequenceName);
					if (tag.tagCode.equals(PixelMedUtils.CodeValueCode)) {
						log.info ("value parent for"+tag.tagName + " = "+ tag.parentSequenceName);
						if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Category Code Sequence")) {
							category.setCodeValue(tag.value);
						}else if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Type Code Sequence")) {
							type.setCodeValue(tag.value);
						}
					}
					if (tag.tagCode.equals(PixelMedUtils.CodeMeaningCode)) {
						log.info ("meaning parent for"+tag.tagName + " = "+ tag.parentSequenceName);
						if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Category Code Sequence")) {
							category.setCodeMeaning(tag.value);
						}else if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Type Code Sequence")) {
							type.setCodeMeaning(tag.value);
						}
					}
					if (tag.tagCode.equals(PixelMedUtils.CodingSchemeDesignatorCode)) {
						log.info ("designator parent for"+tag.tagName + " = "+ tag.parentSequenceName);
						if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Category Code Sequence")) {
							category.setCodingSchemeDesignator(tag.value);
						}else if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Type Code Sequence")) {
							type.setCodingSchemeDesignator(tag.value);
						}
					}
				}
				log.info("category is "+category.toJSON());
				log.info("type is "+type.toJSON());
				mgrp.Finding=category;
				mgrp.FindingSite=type;
				
				//add the measurements from aim
				mgrp.measurementItems.clear();
				for (CalculationEntity cal:iac.getImageAnnotations().get(0).getCalculationEntityCollection().getCalculationEntityList()) {
					log.info("calculation is "+cal.getDescription());
					ControlledTerm derivationMod= new ControlledTerm(cal.getListTypeCode().get(0));//???
					ControlledTerm quantity= new ControlledTerm(cal.getListTypeCode().get(0));
					String value=cal.getCalculationResultCollection().getExtendedCalculationResultList().get(0).getCalculationDataCollection().get(0).getValue().getValue();
					//use this for now. PROBLEM. aim does not have controlled term for this
					ControlledTerm units= new ControlledTerm("[hnsf'U]", "UCUM", "Hounsfield unit");
					MeasurementItem mit=new MeasurementItem();
					//fails if I send null but he has samples with no derivation.
					//nothing wrong with the produced json. something wrong with tid1500writer. 
					//it also does not write mean and standard dev
					//TODO find a way to not send it or just remove from class
					mit.setDerivationModifier(derivationMod);
					mit.setQuantity(quantity);
					mit.setUnits(units);
					mit.setValue(value);
					mgrp.measurementItems.add(mit);
				}
				
				//add the measurements group to the object
				meta.Measurements.add(mgrp);
				
				outputFileName = "dicomsr"+System.currentTimeMillis()+".dcm";
				
			}
			runtid1500writer(meta, imagePath, segPath, outputFileName);
			
		} catch (IOException e) {
			log.warning("IO Error ",e);
		} catch (Exception e) {
			log.warning("run tid1500 exception ",e);
		}
		
	}
	
	
	
	
}
