package edu.stanford.epad.epadws.aim.dicomsr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.utils.MessageLogger;
import com.pixelmed.utils.PrintStreamMessageLogger;

import edu.stanford.epad.common.dicom.DCM4CHEEUtil;
import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.pixelmed.RealWorldValueMapFileWriter;
import edu.stanford.epad.common.plugins.PluginAIMUtil;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.internal.DICOMElement;
import edu.stanford.epad.dtos.internal.DICOMElementList;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.models.Template;
import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.hakan.aim4api.base.Algorithm;
import edu.stanford.hakan.aim4api.base.CD;
import edu.stanford.hakan.aim4api.base.CalculationData;
import edu.stanford.hakan.aim4api.base.CalculationEntity;
import edu.stanford.hakan.aim4api.base.DicomImageReferenceEntity;
import edu.stanford.hakan.aim4api.base.DicomSegmentationEntity;
import edu.stanford.hakan.aim4api.base.Dimension;
import edu.stanford.hakan.aim4api.base.II;
import edu.stanford.hakan.aim4api.base.Image;
import edu.stanford.hakan.aim4api.base.ImageAnnotation;
import edu.stanford.hakan.aim4api.base.ImageAnnotationCollection;
import edu.stanford.hakan.aim4api.base.ImageCollection;
import edu.stanford.hakan.aim4api.base.ImageSeries;
import edu.stanford.hakan.aim4api.base.ImageStudy;
import edu.stanford.hakan.aim4api.base.Person;
import edu.stanford.hakan.aim4api.base.ST;
import edu.stanford.hakan.aim4api.base.User;
import edu.stanford.hakan.aim4api.compability.aimv3.Modality;
import edu.stanford.hakan.aim4api.base.Enumerations.CalculationResultIdentifier;
import edu.stanford.hakan.aim4api.base.ExtendedCalculationResult;

/**
 * This class gets an aim and creates a dicom sr object using dcmqi
 * @author emelalkim
 *
 */
public class Aim2DicomSRConverter {
	private static final EPADLogger log = EPADLogger.getInstance();
	Dcm4CheeDatabaseOperations dcm4cheOp= Dcm4CheeDatabase.getInstance()
			.getDcm4CheeDatabaseOperations();
	public static String  runtid1500writer(DicomSRMetadata meta,String imageDir, String compositeDir, String outputFileName ) throws Exception{
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
		return runtid1500writer(metaFile.getAbsolutePath(), imageDir, compositeDir, outputFileName, true);
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
	public static String runtid1500writer(String metadataPath, String imageDir, String compositeDir,String outputFileName, boolean throwException) throws Exception
	{
		//          ./tid1500writer
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
				return null;
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
			return outputFileName;
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
			IOUtils.closeQuietly(is);
		}
		return null;
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
		Iterator<DICOMFileDescription> it=dicomFilesDescriptions.iterator();
		if (it.hasNext()) {
			DICOMFileDescription first = it.next();
			return first;
		}
		return null;

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

	/**
	 * fill the quantity and unit parts of the measurement item according to the unit in aim
	 * HU and SUV are the only supported units now.
	 * Update the method below unitRetrieveFromControlledTerm in case of new additions or modification
	 * @param item
	 * @param unit
	 * @return
	 */
	public MeasurementItem fillQuantityAndUnit(MeasurementItem item, String unit) {
		if (item==null) 
			item=new MeasurementItem();
		ControlledTerm quantity=null;
		ControlledTerm units=null;
		if (unit.equals("HU")) {
			quantity= new ControlledTerm("112031", "DCM", "Attenuation Coefficient");
			units= new ControlledTerm("[hnsf'U]", "UCUM", "Hounsfield unit");
		}else if (unit.equals("SUV")) {
			quantity= new ControlledTerm("126401", "DCM", "SUVbw");
			units= new ControlledTerm("{SUVbw}g/ml", "UCUM", "Standardized Uptake Value body weight");
		}else {
			//default?????
			units= new ControlledTerm("1", "UCUM", "no units");
		}

		item.setQuantity(quantity);
		item.setUnits(units);
		return item;
	}

	/**
	 * provides a going back mechanism for aim
	 * gets the aim unit from the controlled term in dicom sr.
	 * the controlled terms are defined by fillQuantityAndUnit
	 * Update it in case of new additions or modification
	 * @param units
	 * @return
	 */
	public String unitRetrieveFromControlledTerm(ControlledTerm units) {
		if (units==null) 
			return "";
		if (units.getCodeValue().equals("[hnsf'U]")) {
			return "HU";
		}else if (units.getCodeValue().equals("{SUVbw}g/ml")) {
			return "SUV";
		}else if (units.getCodeValue().equals("1")) {
			return "pixels";
		}
		return "";
	}
	public String Aim2DicomSR(String aimID,String projectID) {
		String imagePath=null,segPath=null,outputFileName=null;

		try {
			outputFileName = EPADConfig.getEPADWebServerDownloadWSDir()+"dicomsr"+System.currentTimeMillis()+".dcm";
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
				String sopClassUID = imageStudy.getImageSeries().getImageCollection().get(0).getSopClassUid().getRoot();


				ArrayList<String> imgFilenames=new ArrayList<>();
				imagePath=getPathandFilenames4Series(seriesUID,imgFilenames);
				log.info("image path is "+imagePath);
				meta.setImageLibrary(imgFilenames);

				//get the segmentation
				DicomSegmentationEntity segEntity= (DicomSegmentationEntity)iac.getImageAnnotations().get(0).getSegmentationEntityCollection().get(0);
				if (segEntity==null) {
					log.warning("no segmentation. DicomSR conversion doesn't work for annotations withpug segmentation. cannot continue" );
					return null;
				}
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
				//do not put all that extra information. leaving the code in case we decide otherwise later
				//NameManagerV4 commentManagerV4 = new NameManagerV4();
				//String fullName= commentManagerV4.toString(iac.getImageAnnotations().get(0));
				String name= iac.getImageAnnotations().get(0).getName().getValue();
				String desc=iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCode() + "|" + name;
				log.info("desc is:"+desc);
				if (desc.length()>64) {//dicom does not accept
					//get just the name (not required when no extra information)
					//desc=iac.getImageAnnotations().get(0).getListTypeCode().get(0).getCode() + "|" + name;

					//the actual shortening
					desc=desc.substring(0,64<desc.length()?64:desc.length());
					log.info("shortened desc is:"+desc);
				}

				meta.setSeriesDescription(desc);
				//clear the measurements. there are defaults for test
				meta.Measurements.clear();
				//create the measurement group. just put the series and segmentation image uid. leave the rest to the default
				MeasurementGroup mgrp=new MeasurementGroup();
				mgrp.setSourceSeriesForImageSegmentation(seriesUID);
				mgrp.setSegmentationSOPInstanceUID(dsoImageUID);
				//get the finding and finding site from dso header
				DICOMElementList dsoTags= Dcm4CheeQueries.getDICOMElementsFromWADO(studyUID, dsoSeriesUID, dsoImageUID);
				ControlledTerm category= new ControlledTerm();
				ControlledTerm type= new ControlledTerm();
				String injected=null, seriesDate=null, seriesTime=null, radioPhStartTime=null, radioPhHalfTime=null, weight=null, units=null;
				String min=null;
				String max=null;
				DICOMElementList imageTags= Dcm4CheeQueries.getDICOMElementsFromWADO(studyUID, seriesUID, seedImageUID);
				for (int i=0; i< dsoTags.ResultSet.totalRecords; i++) {
					DICOMElement tag=dsoTags.ResultSet.Result.get(i);
					if (tag.tagCode.equals(PixelMedUtils.CodeValueCode)) {
						if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Category Code Sequence")) {
							category.setCodeValue(tag.value);
						}else if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Type Code Sequence")) {
							type.setCodeValue(tag.value);
						}
					}
					if (tag.tagCode.equals(PixelMedUtils.CodeMeaningCode)) {
						if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Category Code Sequence")) {
							category.setCodeMeaning(tag.value);
						}else if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Type Code Sequence")) {
							type.setCodeMeaning(tag.value);
						}
					}
					if (tag.tagCode.equals(PixelMedUtils.CodingSchemeDesignatorCode)) {
						if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Category Code Sequence")) {
							category.setCodingSchemeDesignator(tag.value);
						}else if (tag.parentSequenceName.equalsIgnoreCase("Segmented Property Type Code Sequence")) {
							type.setCodingSchemeDesignator(tag.value);
						}
					}
				}
				for (int i=0; i< imageTags.ResultSet.totalRecords; i++) {
					DICOMElement tag=imageTags.ResultSet.Result.get(i);
					//get the other tags for suv

					if (tag.tagCode.equals(PixelMedUtils.TotalDoseCode)) {
						injected=tag.value;
					}
					if (tag.tagCode.equals(PixelMedUtils.SeriesDateCode)) {
						seriesDate=tag.value;
					}
					if (tag.tagCode.equals(PixelMedUtils.SeriesTimeCode)) {
						seriesTime=tag.value;
					}
					if (tag.tagCode.equals(PixelMedUtils.RadiopharmaceuticalStartTimeCode)) {
						radioPhStartTime=tag.value;
					}
					if (tag.tagCode.equals(PixelMedUtils.RadionuclideHalfLifeCode)) {
						radioPhHalfTime=tag.value;
					}
					if (tag.tagCode.equals(PixelMedUtils.PatientWeightCode)) {
						weight=tag.value;
					}
					if (tag.tagCode.equals(PixelMedUtils.UnitsCode)) {
						units=tag.value;
					}
					if (tag.tagCode.equals(PixelMedUtils.SmallestImagePixelValueCode)) {
						min=tag.value;
					}
					if (tag.tagCode.equals(PixelMedUtils.LargestImagePixelValueCode)) {
						max=tag.value;
					}


				}

				log.info("category is "+category.toJSON());
				log.info("type is "+type.toJSON());
				mgrp.Finding=category;
				mgrp.FindingSite=type;

				if (sopClassUID.startsWith("1.2.840.10008.5.1.4.1.1.128")){ //PET. create rwvm for suv
					log.info("PET image. creating rwvms");
					//calculate the scale factor
					log.info("injected "+injected + " date "+ seriesDate+" time "+ seriesTime +" start "+ radioPhStartTime + " half "+  radioPhHalfTime +" weight "+ weight +" units "+ units);
					double scaleFactor=calcSuvScaleFactorBW(injected, seriesDate, seriesTime, radioPhStartTime, radioPhHalfTime, weight, units);
					//write rwvms to a temp dir and put to composite context
					//actually copy the dso in the temp folder also. you can give one composite dir
					//InsertRealWorldValueMap 0 19 0 2.09516 explanation label "126401" "DCM" "" "SUVbw" "" patients/Patient-suv\ 279850299396958268090143072201919589014-Study-1.2.826.0.1.3680043.8.420.31045327731767436406834216341378921542/Series-1.2.826.0.1.3680043.8.420.26000445572709934008257479349240802467/ .

					//keep it under 16 digits. putting 12 in fraction. it can be 3 digits before fraction 
					String slope=String.format("%.12f", scaleFactor);
					log.info("slope is:"+slope);
					File rwvmDir = new File(outputFileName.substring(0, outputFileName.lastIndexOf("/")), "RWVM-" + outputFileName.substring(outputFileName.lastIndexOf("/")+1));
					rwvmDir.mkdirs();
					MessageLogger logger = new PrintStreamMessageLogger(System.err);
					String src = imagePath;
					String dstFolderName = rwvmDir.getAbsolutePath();
					log.info("call pixelmed with src:"+src+ " and dest:"+dstFolderName);
					
					Set<String> imageUIDs = Dcm4CheeDatabase.getInstance().getDcm4CheeDatabaseOperations().getImageUIDsForSeries(seriesUID); 
					
					File temporaryDICOMFile = File.createTempFile(seedImageUID, ".dcm");
					int wadoStatusCode = DCM4CHEEUtil.downloadDICOMFileFromWADO(studyUID, seriesUID, seedImageUID, temporaryDICOMFile);
					RealWorldValueMapFileWriter rwvmWriter=null;
					if (wadoStatusCode == HttpServletResponse.SC_OK) {
						AttributeList imageDICOMAttributes = PixelMedUtils.readDICOMAttributeList(temporaryDICOMFile);
					
						AttributeList[] original_attrs_list = new AttributeList[1];
						original_attrs_list[0] = imageDICOMAttributes;
						String[] imageUIDsArray= new String[imageUIDs.size()];
						rwvmWriter=new RealWorldValueMapFileWriter(imageUIDs.toArray(imageUIDsArray), original_attrs_list, min, max, "0", slope, "SUV calculated by ePAD");
						rwvmWriter.saveDicomFile(rwvmDir.getAbsolutePath()+File.separator+ rwvmWriter.getImageUID()+"rwvm.dcm");
						log.info("saving rwvm to "+rwvmDir.getAbsolutePath()+ File.separator+rwvmWriter.getImageUID()+"rwvm.dcm");
					}else {
						log.warning("Couldn't get dicom file");
					}
					
					//new InsertRealWorldValueMap(min,max,"0",slope,"SUV calculated by ePAD","", "126401","DCM","","SUVbw",null,src,dstFolderName,logger);

					//getting the first. we store just one seg
					File segFile=new File(segPath+File.separator+meta.getCompositeContext().get(0));

					
					//do not clear just add the first
//					meta.getCompositeContext().clear();
					for(File f:rwvmDir.listFiles()) {
						log.info("add file to composite "+ f.getName());
						meta.getCompositeContext().add(f.getName());
						//the name is already the uid
						//is it a problem it is the same uid with one of the images
						mgrp.setRwvmMapUsedForMeasurement(f.getName().replace("rwvm", "").replace(".dcm", ""));
						mgrp.setMeasurementMethod(new ControlledTerm("126401", "DCM", "SUVbw"));
						break;
					}

					File segFileCopy=new File(dstFolderName,segFile.getName());
					EPADFileUtils.copyFile(segFile, segFileCopy);

					log.info("update seg path to "+ dstFolderName);
					segPath=dstFolderName;

				}

				//add the measurements from aim
				mgrp.measurementItems.clear();
				for (CalculationEntity cal:iac.getImageAnnotations().get(0).getCalculationEntityCollection().getCalculationEntityList()) {
					log.info("calculation is "+cal.getDescription());
					ControlledTerm aimCalc= new ControlledTerm(cal.getListTypeCode().get(0));
					//ControlledTerm quantity= new ControlledTerm(cal.getListTypeCode().get(0));
					String value=cal.getCalculationResultCollection().getExtendedCalculationResultList().get(0).getCalculationDataCollection().get(0).getValue().getValue();
					String unit=cal.getCalculationResultCollection().getExtendedCalculationResultList().get(0).getUnitOfMeasure().getValue();

					MeasurementItem mit=new MeasurementItem();
					mit=fillQuantityAndUnit(mit, unit);
					//TODO handle calculations sent in the quantity (like volume)
					if (mit.quantity!= null){
						mit.setDerivationModifier(aimCalc);
					}
					else {
						mit.setQuantity(aimCalc);
						
					}

					//get the first 16 digits if longer
					mit.setValue(trimStr(value,16));
					mgrp.measurementItems.add(mit);
				}

				//add the measurements group to the object
				meta.Measurements.add(mgrp);



			}
			return runtid1500writer(meta, imagePath, segPath, outputFileName);




		} catch (IOException e) {
			log.warning("IO Error ",e);
		} catch (Exception e) {
			log.warning("run tid1500 exception ",e);
		}
		return null;

	}

	String trimStr(String str,int charNum) {
		return str.substring(0,charNum<str.length()?charNum:str.length());
	}
	String timeFormatter(String time) {
		if (time.length()>6 && time.contains("."))
			time=time.substring(0,time.indexOf("."));
		if (time.length()<6) time="0"+time;
		return time;
	}


	public double calcSuvScaleFactorBW(String injected, String acqDate,String acqTime,String radioPhStartTime, String radioPhHalfTime, String weight, String units) {
		return calcSuvScaleFactorBW("0", "0", injected, acqDate, acqTime, radioPhStartTime, radioPhHalfTime, weight, units);
	}

	//use series time not acquisition time!!!
	public double calcSuvScaleFactorBW(String rescaleSlope, String rescaleIntercept,String injected, String acqDate,String acqTime,String radioPhStartTime, String radioPhHalfTime, String weight, String units) {
		double rs= Double.parseDouble(rescaleSlope);
		double ri= Double.parseDouble(rescaleIntercept);
		double inj = Double.parseDouble(injected);
		acqTime=timeFormatter(acqTime);
		long st = Date.UTC(Integer.parseInt(acqDate.substring(0, 4)), Integer.parseInt(acqDate.substring(4, 6)), Integer.parseInt(acqDate.substring(6, 8)), Integer.parseInt(acqTime.substring(0, 2)), Integer.parseInt(acqTime.substring(2, 4)), Integer.parseInt(acqTime.substring(4, 6)));

		radioPhStartTime=timeFormatter(radioPhStartTime);
		long rst = Date.UTC(Integer.parseInt(acqDate.substring(0, 4)), Integer.parseInt(acqDate.substring(4, 6)), Integer.parseInt(acqDate.substring(6, 8)), Integer.parseInt(radioPhStartTime.substring(0, 2)), Integer.parseInt(radioPhStartTime.substring(2, 4)), Integer.parseInt(radioPhStartTime.substring(4, 6)));

		Double rht = Double.parseDouble(radioPhHalfTime);
		double w = Double.parseDouble(weight);

		double scaleFactorSUVbw=0;

		//if it is suv no need to compute
		if (units.startsWith("SUV")) 
			return 1;


		//TODO ml I omited extra checks like decaycorrection and start time zone. need to do that!! 
		if (injected != null && radioPhHalfTime != null && radioPhStartTime != null && acqDate != null && st != 0 && w != 0) {
			if (rst != 0) {
				double decayTime = (st - rst) / 1000.0;	// seconds
				double halfLife = rht;
				double injectedDose = inj;
				double decayedDose = injectedDose * Math.pow(2, -decayTime / halfLife);
				scaleFactorSUVbw = (w * 1000 / decayedDose);
				log.info("scale in func is :"+scaleFactorSUVbw);
				//ml I think it should be without rescale slope and intercept but I should verify!
				//					scaleFactorSUVbw = scaleFactorSUVbw * rs;

			}
		}


		//			double suvValue = (pix + ri) * scaleFactorSUVbw;		

		return scaleFactorSUVbw;

	}

	/**
	 * reads the dicom sr using tid1500reader from dcmqi and outputs a DicomSRMetadata object
	 * if the filename is given, it also saves the file with that name
	 * @param dicomsrPath
	 * @param outputFileName accepts null
	 * @param throwException
	 * @return
	 * @throws Exception
	 */
	public static DicomSRMetadata runtid1500reader(String dicomsrPath, String outputFileName, boolean throwException) throws Exception
	{
		//       ./tid1500reader --inputSRFileName dicomsr1479152980361.dcm --metaDataFileName meta1479.json
		if (outputFileName==null) {
			outputFileName="tmpmeta"+System.currentTimeMillis()+".json";

		}
		log.info("output json file:"+ outputFileName);
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		DicomSRMetadata meta = null;
		try {

			log.info("Sending file - command: ./tid1500reader "+ "--inputSRFileName " + dicomsrPath + " --metaDataFileName " +  outputFileName );
			String[] command = { "./tid1500reader","--inputSRFileName" ,dicomsrPath ,"--metaDataFileName", outputFileName };
			ProcessBuilder pb = new ProcessBuilder(command);

			String dicomBinDirectoryPath = EPADConfig.getEPADWebServerDICOMScriptsDir() + "bin/";
			File script = new File(dicomBinDirectoryPath, "tid1500reader");
			if (!script.exists())
				dicomBinDirectoryPath = EPADConfig.getEPADWebServerDICOMBinDir();
			log.info("tid1500reader binary directory: " + dicomBinDirectoryPath);
			script = new File(dicomBinDirectoryPath, "tid1500reader");
			if (!script.exists()) {
				log.warning("tid1500reader does not exist cannot continue!" );
				return null;
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
				log.info("./tid1500reader output: " + line);
			}

			try {
				int exitValue = process.waitFor();
				log.info("tid1500reader exit value is: " + exitValue);

				if (exitValue==0) { //success
					//read the json file
					File json = new File(dicomBinDirectoryPath, outputFileName);
					if (!json.exists()) {//no file
						return null;
					}
					String jsonStr = EPADFileUtils.readFileAsString(json);
					log.info("json:"+jsonStr);
					meta=DicomSRMetadata.fromJSON(jsonStr);
					log.info("meta:"+meta.toJSON());

				}


			} catch (InterruptedException e) {
				log.warning("Error executing tid1500reader " , e);
			}
			String cmdLineOutput = sb.toString();

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + cmdLineOutput);
			}
		} catch (Exception e) {
			if (e instanceof IllegalStateException && throwException) {
				throw e;
			}
			log.warning("tid1500reader failed: " + e.getMessage());
			if (throwException) {
				throw new IllegalStateException("tid1500reader failed", e);
			}
		} catch (OutOfMemoryError oome) {
			log.warning("tid1500reader ran out of memory", oome);
			if (throwException) {
				throw new IllegalStateException("tid1500reader ran out of memory", oome);
			}
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
		}

		return meta;
	}


	/**
	 * start the dicomsr to aim conversion using a file path
	 * @param filePath
	 * @param projectID
	 * @return the aim xml
	 */
	public String DicomSR2Aim(String filePath, String projectID) {
		try {
			return DicomSR2Aim(runtid1500reader(filePath, null, false), projectID);
		} catch (Exception e) {
			log.warning("tid1500reader throw exception", e);
		}
		return null;
	}

	/**
	 * puts 19000101000000 if null or empty
	 * @param d
	 * @return
	 */
	public String formatPatientBirthDate(String d) {
		if (d==null) d="";
		String date = ((d.length() >= 4) ? d.substring(0, 4) : "1900") 
				+ ((d.length() >= 6) ? d.substring(4, 6) : "01") 
				+ ((d.length() >= 8) ? d.substring(6, 8) : "01") + "000000";
		return date;

	}

	/**
	 * start the dicomsr to aim conversion using a metadata object
	 * @param filePath
	 * @param projectID
	 * @return the aim xml
	 */
	public String DicomSR2Aim(DicomSRMetadata meta, String projectID) {


		try {

			if (meta==null) {
				log.info("Metadata is empty. Exiting the conversion process");
				return null;
			}
			ImageAnnotationCollection iac = new ImageAnnotationCollection();
			User user=new User();
			user.setName(new ST(meta.observerContext.getPersonObserverName()));
			user.setLoginName(new ST(meta.observerContext.getPersonObserverName()));
			//               user.setRoleInTrial(""); ???
			iac.setUser(user);
			//set the date to current date
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Date now=new Date();

			iac.setDateTime(dateFormat.format(now));


			//patient info
			//               <person>
			//               <name value=""/>
			//               <id value="LIDC-IDRI-0314"/>
			//               <birthDate value="19000101000000"/>
			//               <sex value="F"/>
			//               <ethnicGroup/>
			//               </person>

			ImageAnnotation ia=new ImageAnnotation();

			ia.setDateTime(dateFormat.format(now));


			String seriesDesc=meta.getSeriesDescription();
			log.info("series desc "+seriesDesc);
			String[] desc=seriesDesc.split("\\|");
			log.info("desc length"+desc.length);
			EpadProjectOperations projOp = DefaultEpadProjectOperations.getInstance();
			Template t=null;
			if (desc.length==2) {

				t=projOp.getTemplate(desc[0].trim());
				if (t!=null) {
					ia.setName(new ST(desc[1]));
					//do not put all that extra information. leaving the code in case we decide otherwise later
					//					log.info("name :"+ia.getName().getValue());
					//					NameManagerV4 commentManagerV4 = new NameManagerV4(ia);
					//					log.info("name edited:"+ia.getName().getValue());
					ArrayList<CD> types=new ArrayList<>();
					types.add(new CD(t.getTemplateCode(),t.getTemplateName(),t.getCodingSchemeDesignator(),t.getCodingSchemeVersion()));
					ia.setTypeCode(types);
				}
				else {
					ia.setName(new ST(seriesDesc));
					//set default to seg only
					t=projOp.getTemplate("SEG");
					log.info("no template");
					if (t!=null) {
						log.info("using seg");
						ArrayList<CD> types=new ArrayList<>();
						types.add(new CD(t.getTemplateCode(),t.getTemplateName(),t.getCodingSchemeDesignator(),t.getCodingSchemeVersion()));
						ia.setTypeCode(types);
					}
				}
				//template info
				//               <typeCode code="ROI" codeSystemName="ROI Only">
				//               <iso:displayName xmlns:iso="uri:iso.org:21090" value="ROI Only"/>
				//               </typeCode>
			}else {
				ia.setName(new ST(seriesDesc));
				//set default to seg only
				t=projOp.getTemplate("SEG");
				log.info("no template");
				if (t!=null) {
					log.info("using seg");
					ArrayList<CD> types=new ArrayList<>();
					types.add(new CD(t.getTemplateCode(),t.getTemplateName(),t.getCodingSchemeDesignator(),t.getCodingSchemeVersion()));
					ia.setTypeCode(types);
				}
			}

			//comment???
			//       <comment value="CT /  / 10"/>
			for (MeasurementGroup meas:meta.getMeasurements()){

				String sourceSeriesUID=meas.SourceSeriesForImageSegmentation;
				log.info("source series is:"+sourceSeriesUID);
				String dsoInstanceUID=meas.segmentationSOPInstanceUID;
				DICOMFileDescription firstImage=getFirstInstanceUIDInSeries(sourceSeriesUID);
				String studyUID="na";
				String imageUID="na";
				if (firstImage==null) {
					log.info("couldn't retrieve dicom image. Cannot continue. Exiting.");
					return null;
				}
				else {
					studyUID=firstImage.studyUID;
					imageUID=firstImage.imageUID;
				}

				String sopClassUID="na",studyDate="na",studyTime="na", pName="na",pId="na",pBirthDate="na",pSex="na";

				//fill the missing information from dicom tags
				DICOMElementList tags= Dcm4CheeQueries.getDICOMElementsFromWADO(studyUID, sourceSeriesUID, imageUID);
				for (int i=0; i< tags.ResultSet.totalRecords; i++) {
					DICOMElement tag=tags.ResultSet.Result.get(i);
					if (tag.tagCode.equals(PixelMedUtils.SOPClassUIDCode)) 
						sopClassUID=tag.value;
					if (tag.tagCode.equals(PixelMedUtils.StudyDateCode)) 
						studyDate=tag.value;
					if (tag.tagCode.equals(PixelMedUtils.StudyTimeCode)) 
						studyTime=tag.value;
					if (tag.tagCode.equals(PixelMedUtils.PatientNameCode)) 
						pName=tag.value;
					if (tag.tagCode.equals(PixelMedUtils.PatientIDCode)) 
						pId=tag.value;
					if (tag.tagCode.equals(PixelMedUtils.PatientBirthDateCode)) 
						pBirthDate=tag.value;
					if (tag.tagCode.equals(PixelMedUtils.PatientSexCode)) 
						pSex=tag.value;

				}
				Person p=new Person();
				p.setBirthDate(formatPatientBirthDate(pBirthDate));
				p.setId(new ST(pId));
				p.setName(new ST(pName));
				p.setSex(new ST(pSex));
				iac.setPerson(p);


				//form the segmentation entity
				DicomSegmentationEntity segEntity=new DicomSegmentationEntity();
				segEntity.setReferencedSopInstanceUid(new II(imageUID));
				segEntity.setSegmentNumber(meas.getReferencedSegment());
				segEntity.setSopInstanceUid(new II(dsoInstanceUID));
				segEntity.setSopClassUid(new II("1.2.840.10008.5.1.4.1.1.66.4")); //segmentation
				ia.addSegmentationEntity(segEntity);     

				DicomImageReferenceEntity dicomImageReferenceEntity  = new DicomImageReferenceEntity();
				ImageStudy study = new ImageStudy();
				study.setInstanceUid(new II(studyUID));
				ImageSeries series=new ImageSeries();
				series.setInstanceUid(new II(sourceSeriesUID));
				Image image=new Image();
				image.setSopInstanceUid(new II(imageUID));
				image.setSopClassUid(new II(sopClassUID));
				ImageCollection imageCol=new ImageCollection();
				imageCol.addImage(image);
				series.setImageCollection(imageCol);
				Modality mod=Modality.getInstance();
				series.setModality(mod.get(sopClassUID));
				study.setImageSeries(series);
				study.setStartDate(studyDate);
				study.setStartTime(studyTime);
				dicomImageReferenceEntity.setImageStudy(study);
				ia.addImageReferenceEntity(dicomImageReferenceEntity);

				for (MeasurementItem item:meas.measurementItems) {
					CalculationEntity cal =new CalculationEntity();
					ControlledTerm quantity=item.getQuantity();
					ControlledTerm derivationMod=item.getDerivationModifier();
					if (derivationMod==null) //if it is null get the quantity
						derivationMod=quantity;
					ControlledTerm units=item.getUnits();
					cal.addTypeCode(new CD(derivationMod.CodeValue,derivationMod.CodeMeaning,derivationMod.CodingSchemeDesignator));
					cal.setDescription(new ST(derivationMod.CodeMeaning));
					ExtendedCalculationResult calculationResult=new ExtendedCalculationResult();

					calculationResult.setType(CalculationResultIdentifier.Scalar);
					//get the code meaning??? not a controlled term in aim get it back from look up table
					calculationResult.setUnitOfMeasure(new ST(unitRetrieveFromControlledTerm(units)));
					//ml assume all data types are double. dicom sr does not have data type
					calculationResult.setDataType(new CD("99EPADD1","Double","99EPAD"));

					// Create a CalculationData instance
					CalculationData calculationData = new CalculationData();
					calculationData.setValue(new ST(item.getValue()));
					calculationData.addCoordinate(0, 0); // TODO what goes here?

					// Create a Dimension instance
					Dimension dimension = new Dimension(0, 1, derivationMod.CodeMeaning);

					// Add calculationData to calculationResult
					calculationResult.addCalculationData(calculationData);

					// Add dimension to calculationResult
					calculationResult.addDimension(dimension);

					//                    // add the shape reference to the calculation
					//                    ReferencedGeometricShape reference = new ReferencedGeometricShape();
					//                    reference.setCagridId(0);
					//                    reference.setReferencedShapeIdentifier(shapeId);
					//                    calculation.addReferencedGeometricShape(reference);

					// Add calculationResult to calculation
					cal.addCalculationResult(calculationResult);

					//to calentity
					//                    <description value="Min"/>
					//                    <mathML/>
					//                    <algorithm>
					//                    <name value="Min"/>
					//                    <type code="RID12780" codeSystemName="RadLex" codeSystemVersion="3.2">
					//                    <iso:displayName xmlns:iso="uri:iso.org:21090" value="Calculation"/>
					//                    </type>
					//                    <version value="1.0"/>
					//                    </algorithm>
					Algorithm alg=new Algorithm();
					alg.setName(new ST(derivationMod.CodeMeaning));
					alg.setVersion(new ST("na"));
					ArrayList<CD> types=new ArrayList<>();
					types.add(new CD("RID12780","Calculation","RadLex","3.2"));
					alg.setType(types);
					cal.setAlgorithm(alg);

					ia.addCalculationEntity(cal);
				}

			}
			iac.addImageAnnotation(ia);

			log.info("annotation is: "+iac.toStringXML());
			return iac.toStringXML();

		} catch (Exception e) {
			log.warning("dicom sr to aim exception ",e);
		}

		return null;

	}




}


