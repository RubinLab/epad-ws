package edu.stanford.epad.epadws.aim.dicomsr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;

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
	
	public void Aim2DicomSR(String aimUID) {
	
		DicomSRMetadata meta=new DicomSRMetadata();
		log.info(meta.toJSON());
		try {
			File metaFile=File.createTempFile("meta"+System.currentTimeMillis(),".json");
			log.info("Writing json to temp file "+ metaFile.getAbsolutePath());
			FileWriter fw = new FileWriter(metaFile);
			fw.write(meta.toJSON());
			fw.close();
			
			runtid1500writer(metaFile.getAbsolutePath(), "~/tid1500test", "~/tid1500test", "liver_sr_test.dcm", true);
			
			
		} catch (IOException e) {
			log.warning("IO Error ",e);
		} catch (Exception e) {
			log.warning("run tid1500 exception ",e);
		}
		
	}
	
	
}
