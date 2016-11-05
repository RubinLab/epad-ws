package edu.stanford.epad.epadws.aim.dicomsr;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.util.EPADConfig;

/**
 * This class gets an aim and creates a dicom sr object using dcmqi
 * @author emelalkim
 *
 */
public class Aim2DicomSRConverter {
	
	
	public static void runtid1500writer(String inputPathFile, boolean throwException) throws Exception
	{
//		/tid1500writer --metaDataFileName /Users/emelalkim/Desktop/test.json --compositeContextDataDir /Users/emelalkim/Documents/workspace/rsna/dcmqi/data/ct-3slice/ --imageLibraryDataDir /Users/emelalkim/Documents/workspace/rsna/dcmqi/data/ct-3slice/ --outputFileName liver_sr_test.dcm
//		InputStream is = null;
//		InputStreamReader isr = null;
//		BufferedReader br = null;
//
//		try {
//			String dcmServerTitlePort = aeTitle + "@"+EPADConfig.dicomServerIP +":" + dicomServerPort;
//			dcmServerTitlePort = dcmServerTitlePort.trim();
//			log.info("Sending file - command: ./dcmsnd " + dcmServerTitlePort + " " + inputPathFile);
//			String[] command = { "./tid1500writer", dcmServerTitlePort, inputPathFile };
//			ProcessBuilder pb = new ProcessBuilder(command);
//			
//			String dicomBinDirectoryPath = EPADConfig.getEPADWebServerDICOMScriptsDir() + "bin/";
//			File script = new File(dicomBinDirectoryPath, "dcmsnd");
//			if (!script.exists())
//				dicomBinDirectoryPath = EPADConfig.getEPADWebServerDICOMBinDir();
//			log.info("DICOM binary directory: " + dicomBinDirectoryPath);
//			pb.directory(new File(dicomBinDirectoryPath));
//			Process process = pb.start();
//			process.getOutputStream();
//			is = process.getInputStream();
//			isr = new InputStreamReader(is);
//			br = new BufferedReader(isr);
//
//			String line;
//			StringBuilder sb = new StringBuilder();
//			while ((line = br.readLine()) != null) {
//				sb.append(line).append("\n");
//				log.info("./dcmsend output: " + line);
//			}
//
//			try {
//				int exitValue = process.waitFor();
//				log.info("dcmsnd exit value is: " + exitValue);
//				if (sb.toString().contains("Sent 0 objects"))
//				{
//					log.warning("Zero objects sent to dcm4che, some error has occurred");
//					throw new Exception("Error sending files to dcm4che");
//				}
//
//			} catch (InterruptedException e) {
//				log.warning("Error sending DICOM files in: " + inputPathFile, e);
//			}
//			String cmdLineOutput = sb.toString();
//
//			if (cmdLineOutput.toLowerCase().contains("error")) {
//				throw new IllegalStateException("Failed for: " + cmdLineOutput);
//			}
//		} catch (Exception e) {
//			if (e instanceof IllegalStateException && throwException) {
//				throw e;
//			}
//			log.warning("dcmsnd failed: " + e.getMessage());
//			if (throwException) {
//				throw new IllegalStateException("dcmsnd failed", e);
//			}
//		} catch (OutOfMemoryError oome) {
//			log.warning("dcmsnd ran out of memory", oome);
//			if (throwException) {
//				throw new IllegalStateException("dcmsnd ran out of memory", oome);
//			}
//		} finally {
//			IOUtils.closeQuietly(br);
//			IOUtils.closeQuietly(isr);
//		}
	}
	
	public Aim2DicomSRConverter() {
		DicomSRMetadata meta=new DicomSRMetadata();
		System.out.println(meta.toJSON());
	}
	
	
}
