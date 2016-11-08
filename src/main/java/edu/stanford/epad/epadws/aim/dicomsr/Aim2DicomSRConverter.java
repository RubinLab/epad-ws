package edu.stanford.epad.epadws.aim.dicomsr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.dicom.DCM4CHEEUtil;
import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.common.plugins.PluginAIMUtil;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.hakan.aim4api.base.DicomImageReferenceEntity;
import edu.stanford.hakan.aim4api.base.ImageAnnotationCollection;
import edu.stanford.hakan.aim4api.base.ImageStudy;

/**
 * This class gets an aim and creates a dicom sr object using dcmqi
 * @author emelalkim
 *
 */
public class Aim2DicomSRConverter {
	private static final EPADLogger log = EPADLogger.getInstance();
	
	
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
	public void Aim2DicomSR(String aimID,String projectID) {
		String imagePath="",segPath=""; 
		try {
			ImageAnnotationCollection iac = PluginAIMUtil.getImageAnnotationCollectionFromServer(aimID, projectID);
			
			if (iac != null) {
					
				// Get DICOM path					
				DicomImageReferenceEntity dicomImageReferenceEntity = (DicomImageReferenceEntity) iac
																		.getImageAnnotations().get(0).getImageReferenceEntityCollection().get(0);
				ImageStudy imageStudy = dicomImageReferenceEntity.getImageStudy();
				String studyUID = imageStudy.getInstanceUid().getRoot();
				String seriesUID = imageStudy.getImageSeries().getInstanceUid().getRoot();
				String seedImageUID = imageStudy.getImageSeries().getImageCollection().get(0).getSopInstanceUid().getRoot();
				log.info("the image " +seedImageUID+ " series "+ seriesUID);
				
				imagePath=getPath4Series(seriesUID);
				log.info("image path is "+imagePath);
				
//				DicomSegmentationEntity segEntity= (DicomSegmentationEntity)iac.getImageAnnotations().get(0).getSegmentationEntityCollection().get(0);
//				log.info("segmentation uid:"+segEntity.getSopInstanceUid());
				
				String dsoSeriesUID = PluginAIMUtil.getDSOSeriesUID(aimID);
				log.info("dsoseriesuid:"+dsoSeriesUID);
				String dsoPath=getPath4Series(dsoSeriesUID);
				log.info("dso path is "+dsoPath);
				
			}
	
			DicomSRMetadata meta=new DicomSRMetadata();
			log.info(meta.toJSON());
		
			File metaFile=File.createTempFile("meta"+System.currentTimeMillis(),".json");
			log.info("Writing json to temp file "+ metaFile.getAbsolutePath());
			FileWriter fw = new FileWriter(metaFile);
			fw.write(meta.toJSON());
			fw.close();
			
			imagePath="/home/epad/tid1500test";
			segPath="/home/epad/tid1500test"; //for testing
			runtid1500writer(metaFile.getAbsolutePath(), imagePath, segPath, "liver_sr_test.dcm", true);
			
			
		} catch (IOException e) {
			log.warning("IO Error ",e);
		} catch (Exception e) {
			log.warning("run tid1500 exception ",e);
		}
		
	}
	
	
}
