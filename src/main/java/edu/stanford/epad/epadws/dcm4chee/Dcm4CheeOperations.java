//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.dcm4chee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;

/**
 * Operations on Dcm4Chee (as distinct from operations directly on DCM4CHEE's database).
 * 
 * 
 * @author martin
 * @see Dcm4CheeDatabaseOperations
 */
public class Dcm4CheeOperations
{
	private static EPADLogger log = EPADLogger.getInstance();
	
	static final String[] scriptFiles = {
		"dcmsnd",
		"dcmdeleteSeries",
		"dcmdeleteStudy",
		"twiddle.sh",
		"dcm2txt",
	};
	
	static final String[] libFiles = {
		"dcm4che-tool-dcmsnd-2.0.24.jar",
		"dcm4che-tool-dcm2txt-2.0.24.jar",
		"dcm4che-core-2.0.24.jar",
		"dcm4che-net-2.0.24.jar",
		"slf4j-log4j12-1.6.1.jar",
		"slf4j-api-1.6.1.jar",
		"log4j-1.2.16.jar",
		"commons-cli-1.2.jar",
		"twiddle.jar",
		"jbossall-client.jar",
		"getopt.jar",
		"jboss-jmx.jar",
	};
	
	public static void checkScriptFiles()
	{
		try
		{
			File scriptDir = new File(EPADConfig.getEPADWebServerDICOMScriptsDir() + "bin/");
			if (!scriptDir.exists())
				scriptDir.mkdirs();
			for (String scriptFile: scriptFiles)
			{
				File file = new File(scriptDir, scriptFile);
				if (!file.exists()) {
					InputStream in = null;
					OutputStream out = null;
					try {
						in = new Dcm4CheeOperations().getClass().getClassLoader().getResourceAsStream("scripts/" + scriptFile);
			            out = new FileOutputStream(file);
	
			            // Transfer bytes from in to out
			            byte[] buf = new byte[1024];
			            int len;
			            while ((len = in.read(buf)) > 0)
			            {
			                    out.write(buf, 0, len);
			            }
					} catch (Exception x) {
						
					} finally {
			            IOUtils.closeQuietly(in);
			            IOUtils.closeQuietly(out);
			            file.setExecutable(true);
					}
				}
				file.setExecutable(true);
			}
			File libDir = new File(EPADConfig.getEPADWebServerDICOMScriptsDir() + "lib/");
			if (!libDir.exists())
				libDir.mkdirs();
			for (String libFile: libFiles)
			{
				File file = new File(libDir, libFile);
				if (!file.exists()) {
					InputStream in = null;
					OutputStream out = null;
					try {
						in = new Dcm4CheeOperations().getClass().getClassLoader().getResourceAsStream("scripts/" + libFile);
			            out = new FileOutputStream(file);
	
			            // Transfer bytes from in to out
			            byte[] buf = new byte[1024];
			            int len;
			            while ((len = in.read(buf)) > 0)
			            {
			                    out.write(buf, 0, len);
			            }
					} catch (Exception x) {
						
					} finally {
			            IOUtils.closeQuietly(in);
			            IOUtils.closeQuietly(out);
					}
				}
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public static boolean dcmsnd(File inputDirFile, boolean throwException) throws Exception
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileWriter tagFileWriter = null;
		boolean success = false;
		try {
			String aeTitle = EPADConfig.aeTitle;
			String dicomServerIP = EPADConfig.dicomServerIP;
			String dicomServerPort = EPADConfig.dicomServerPort;
			String dicomServerTitleAndPort = aeTitle + "@" + dicomServerIP + ":" + dicomServerPort;

			dicomServerTitleAndPort = dicomServerTitleAndPort.trim();

			String dirPath = inputDirFile.getAbsolutePath();
			if (pathContainsSpaces(dirPath))
				dirPath = escapeSpacesInDirPath(dirPath);

			File dir = new File(dirPath);
			int nbFiles = -1;
			if (dir != null) {
				String[] filePaths = dir.list();
				if (filePaths != null)
					nbFiles = filePaths.length;
			}
			log.info("./dcmsnd: sending " + nbFiles + " file(s) - command: ./dcmsnd " + dicomServerTitleAndPort + " "
					+ dirPath);

			String[] command = { "./dcmsnd", dicomServerTitleAndPort, dirPath };
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			String dicomScriptsDir = EPADConfig.getEPADWebServerDICOMScriptsDir() + "bin/";
			File script = new File(dicomScriptsDir, "dcmsnd");
			if (!script.exists())
				dicomScriptsDir = EPADConfig.getEPADWebServerDICOMBinDir();
			script = new File(dicomScriptsDir, "dcmsnd");
			if (!script.exists())
				throw new Exception("No script found:" + script.getAbsolutePath());
			// Java 6 - Runtime.getRuntime().exec("chmod u+x "+script.getAbsolutePath());
			script.setExecutable(true);
			processBuilder.directory(new File(dicomScriptsDir));
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
				log.info("./dcmsend output: " + line);
			}

			try {
				int exitValue = process.waitFor();
				log.info("DICOM send exit value is: " + exitValue);
				if (exitValue == 0) success = true;
			} catch (InterruptedException e) {
				log.warning("Didn't send DICOM files in: " + inputDirFile.getAbsolutePath(), e);
			}
			String cmdLineOutput = sb.toString();

			if (cmdLineOutput.toLowerCase().contains("error"))
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			return success;
		} catch (Exception e) {
			log.warning("DicomSendTask failed to send DICOM files", e);
			if (e instanceof IllegalStateException && throwException) {
				throw e;
			}
			if (throwException) {
				throw new IllegalStateException("DicomSendTask failed to send DICOM files", e);
			}
			return success;
		} catch (OutOfMemoryError oome) {
			log.warning("DicomSendTask out of memory: ", oome);
			if (throwException) {
				throw new IllegalStateException("DicomSendTask out of memory: ", oome);
			}
			return success;
		} finally {
			IOUtils.closeQuietly(tagFileWriter);
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * TODO This does not work. The ./dcmdeleteSeries script invoked the dcm4chee twiddle command but it appears that the
	 * moveSeriesToTrash operation it calls has no effect in this version of dcm4chee. See:
	 * http://www.dcm4che.org/jira/browse/WEB-955
	 */
	public static boolean deleteSeries(String seriesUID, String seriesPk)
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		boolean success = false;

		try {
			log.info("Deleting series " + seriesUID + " seriesPK:" + seriesPk);

			String[] command = { "./dcmdeleteSeries", seriesPk, EPADConfig.xnatUploadProjectPassword };
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			String dicomScriptsDir = EPADConfig.getEPADWebServerDICOMScriptsDir() + "bin/";
			File script = new File(dicomScriptsDir, "dcmdeleteSeries");
			if (!script.exists())
				dicomScriptsDir = EPADConfig.getEPADWebServerMyScriptsDir();
			script = new File(dicomScriptsDir, "dcmdeleteSeries");
			// Java 6 - Runtime.getRuntime().exec("chmod u+x "+script.getAbsolutePath());
			script.setExecutable(true);
			processBuilder.directory(new File(dicomScriptsDir));
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			process.getOutputStream();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
				log.info("./dcmdeleteSeries: " + line);
			}
			try {
				int exitValue = process.waitFor();
				if (exitValue == 0) {
					log.info("Deleted DICOM series " + seriesUID + " pk:" + seriesPk);
					success = true;
				} else {
					log.warning("Failed to delete DICOM series " + seriesUID + " pk=" + seriesPk + "; exitValue=" + exitValue + "\n" + sb.toString());
				}
			} catch (Exception e) {
				log.warning("Failed to delete DICOM series " + seriesUID, e);
			}

			String cmdLineOutput = sb.toString();

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}
		} catch (IOException e) {
			log.warning("Failed to delete DICOM series " + seriesUID, e);
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(is);
		}
		return success;
	}

	public static void deleteStudy(String studyUID)
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			log.info("Deleting study " + studyUID + " files - command: ./dcmdeleteStudy " + studyUID);

			String[] command = { "./dcmdeleteStudy", studyUID };

			ProcessBuilder pb = new ProcessBuilder(command);
			String dicomScriptsDir = EPADConfig.getEPADWebServerDICOMScriptsDir() + "bin/";
			File script = new File(dicomScriptsDir, "dcmdeleteStudy");
			if (!script.exists())
				dicomScriptsDir = EPADConfig.getEPADWebServerMyScriptsDir();
			script = new File(dicomScriptsDir, "dcmdeleteStudy");
			// Java 6 - Runtime.getRuntime().exec("chmod u+x "+script.getAbsolutePath());
			script.setExecutable(true);
			pb.directory(new File(dicomScriptsDir));

			Process process = pb.start();
			process.getOutputStream();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				log.info("./dcmdeleteStudy: " + line);
				sb.append(line).append("\n");
			}

			try {
				int exitValue = process.waitFor();
				log.info("DICOM delete study exit value is: " + exitValue);
			} catch (Exception e) {
				log.warning("Failed to delete DICOM study " + studyUID, e);
			}
			String cmdLineOutput = sb.toString();

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}
		} catch (Exception e) {
			log.warning("Failed to delete DICOM study " + studyUID, e);
		}
	}

	private static String parseError(String output)
	{
		String result = "";
		try {
			String[] lines = output.split("\n");
			for (String currLine : lines) {
				if (currLine.toLowerCase().contains("error")) {
					result = result + currLine;
				}
			}
		} catch (Exception e) {
			log.warning("DicomSendTask.parseError had: " + e.getMessage() + " for: " + output, e);
		}
		if (result.length() > 0)
			return result;
		else
			return output;
	}

	/**
	 * 
	 * @param dirPath String
	 * @return boolean true if this path contains spaces.
	 */
	private static boolean pathContainsSpaces(String dirPath)
	{
		return dirPath.indexOf(' ') > 0;
	}

	/**
	 * 
	 * @param path String
	 * @return String
	 */
	private static String escapeSpacesInDirPath(String path)
	{
		StringBuilder sb = new StringBuilder();
		String[] parts = path.split(" ");
		boolean isFirst = true;
		for (String part : parts) {
			if (!isFirst) {
				sb.append("\\ ");
			}
			sb.append(part);
			isFirst = false;
		}
		return sb.toString();
	}
}
